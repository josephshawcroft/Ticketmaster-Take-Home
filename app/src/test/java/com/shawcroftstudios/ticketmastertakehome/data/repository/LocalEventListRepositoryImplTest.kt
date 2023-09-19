package com.shawcroftstudios.ticketmastertakehome.data.repository

import app.cash.turbine.test
import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.database.EventDao
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.helpers.TestEventBuilder
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LocalEventListRepositoryImplTest {

    @MockK
    lateinit var eventDao: EventDao

    @InjectMockKs
    lateinit var sut: LocalEventListRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given sut and events for city exist in local db, when fetchEventsForCity is called, then return flow that ends in success with events`() =
        runTest {
            val cityName = "cityName"
            val testEvents = createTestEvents(cityName)

            coEvery { eventDao.getEventsForCity(cityName) } returns testEvents


            sut.fetchEventsForCity(cityName).test {
                assertEquals(DataLoadingResult.Loading, awaitItem()) // always loads first
                assertEquals(DataLoadingResult.Success(testEvents), awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) { eventDao.getEventsForCity(cityName) }
        }

    @Test
    fun `given sut and events for city do not exist in local db, when fetchEventsForCity is called, then return flow that ends in error with exception`() =
        runTest {
            val cityName = "cityName"

            coEvery { eventDao.getEventsForCity(cityName) } returns emptyList()

            sut.fetchEventsForCity(cityName).test {
                assertEquals(DataLoadingResult.Loading, awaitItem())

                val errorItem = awaitItem()
                assertTrue(
                    "$errorItem is not of type DataLoadingResult.Error",
                    errorItem is DataLoadingResult.Error
                )

                val exception = (errorItem as? DataLoadingResult.Error)?.exception
                assertTrue(
                    "$exception is not NoAvailableEventsException",
                    exception is NoAvailableEventsException
                )

                awaitComplete()
            }

            coVerify(exactly = 1) { eventDao.getEventsForCity(cityName) }
        }

    @Test
    fun `given sut, when insertEvents is called, then events inserted into dao`() =
        runTest {
            val cityName = "cityName"
            val events = createTestEvents(cityName)

            coJustRun { eventDao.insertAll(events) }

            sut.insertEvents(events)

            coVerify(exactly = 1) { eventDao.insertAll(events) }
        }

    @Suppress("SameParameterValue")
    private fun createTestEvents(cityName: String): List<Event> {
        val testEvent1 = TestEventBuilder.createEvent(id = "1", city = cityName)
        val testEvent2 = TestEventBuilder.createEvent(id = "2", city = cityName)
        return listOf(testEvent1, testEvent2)
    }
}
