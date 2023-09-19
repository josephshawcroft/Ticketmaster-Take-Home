package com.shawcroftstudios.ticketmastertakehome.data.repository

import app.cash.turbine.test
import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.database.EventDao
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
import com.shawcroftstudios.ticketmastertakehome.data.mapper.EventMapper
import com.shawcroftstudios.ticketmastertakehome.data.network.EventApi
import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.helpers.TestEventBuilder
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EventListRepositoryImplTest {

    @MockK
    lateinit var dispatcherProvider: DispatcherProvider

    @MockK
    lateinit var eventApi: EventApi

    @MockK
    lateinit var eventDao: EventDao

    @MockK
    lateinit var eventMapper: EventMapper

    @InjectMockKs
    lateinit var sut: EventListRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { dispatcherProvider.io } returns Dispatchers.Unconfined
    }

    // fetchLocalEventsForCity tests
    @Test
    fun `given sut and events for city exist in local db, when fetchLocalEventsForCity is called, then return flow that ends in success with events`() =
        runTest {
            val cityName = "cityName"
            val testEvents = createTestEvents(cityName)

            coEvery { eventDao.getEventsForCity(cityName) } returns testEvents


            sut.fetchLocalEventsForCity(cityName).test {
                assertEquals(DataLoadingResult.Loading, awaitItem()) // always loads first
                assertEquals(DataLoadingResult.Success(testEvents), awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) { eventDao.getEventsForCity(cityName) }
        }

    @Test
    fun `given sut and events for city do not exist in local db, when fetchLocalEventsForCity is called, then return flow that ends in error with exception`() =
        runTest {
            val cityName = "cityName"

            coEvery { eventDao.getEventsForCity(cityName) } returns emptyList()

            sut.fetchLocalEventsForCity(cityName).test {
                assertEquals(DataLoadingResult.Loading, awaitItem())

                val errorItem = awaitItem()
                assertTrue("$errorItem is not of type DataLoadingResult.Error", errorItem is DataLoadingResult.Error)

                val exception = (errorItem as? DataLoadingResult.Error)?.exception
                assertTrue("$exception is not NoAvailableEventsException" , exception is NoAvailableEventsException)

                awaitComplete()
            }

            coVerify(exactly = 1) { eventDao.getEventsForCity(cityName) }
        }

    // fetchRemoteEventsForCity tests
    @Test
    fun `given sut, when fetchRemoteEventsForCity is called and api is successful, then return flow that ends in success with events and events inserted into local db`() =
        runTest {
            val cityName = "cityName"
            val testEvents = createTestEvents(cityName)
            val eventsResponse = EventsResponse(null)
            val successfulMockResponse = Result.success(eventsResponse)

            coEvery { eventApi.fetchEventsForCity(cityName) } returns successfulMockResponse
            every { eventMapper.mapToDomain(any())} returns testEvents
            coJustRun { eventDao.insertAll(any())}

            sut.fetchRemoteEventsForCity(cityName).test {
                assertEquals(DataLoadingResult.Loading, awaitItem())
                assertEquals(DataLoadingResult.Success(testEvents), awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) { eventApi.fetchEventsForCity(cityName) }
            verify(exactly = 1) { eventMapper.mapToDomain(eventsResponse) }
            coVerify(exactly = 1) { eventDao.insertAll(testEvents) }
        }

    @Suppress("SameParameterValue")
    private fun createTestEvents(cityName: String) : List<Event> {
        val testEvent1 = TestEventBuilder.createEvent(id = "1", city = cityName)
        val testEvent2 = TestEventBuilder.createEvent(id = "2", city = cityName)
        return listOf(testEvent1, testEvent2)
    }
}
