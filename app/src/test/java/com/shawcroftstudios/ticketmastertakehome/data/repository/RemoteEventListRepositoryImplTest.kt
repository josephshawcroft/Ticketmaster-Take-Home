package com.shawcroftstudios.ticketmastertakehome.data.repository

import app.cash.turbine.test
import com.shawcroftstudios.ticketmastertakehome.data.DataResult
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
import com.shawcroftstudios.ticketmastertakehome.data.mapper.EventMapper
import com.shawcroftstudios.ticketmastertakehome.data.network.EventApi
import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.helpers.TestEventBuilder
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoteEventListRepositoryImplTest {

    @MockK
    lateinit var eventApi: EventApi

    @MockK
    lateinit var eventMapper: EventMapper

    @InjectMockKs
    lateinit var sut: RemoteEventListRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given sut, when fetchEventsForCity is called and api is successful, then return flow that ends in success with events`() =
        runTest {
            val cityName = "cityName"
            val testEvents = createTestEvents(cityName)
            val eventsResponse = EventsResponse(null)
            val successfulMockResponse = Result.success(eventsResponse)

            coEvery { eventApi.fetchEventsForCity(cityName) } returns successfulMockResponse
            every { eventMapper.mapToDomain(any()) } returns testEvents

            sut.fetchEventsForCity(cityName).test {
                assertEquals(DataResult.Loading, awaitItem())
                assertEquals(DataResult.Success(testEvents), awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) { eventApi.fetchEventsForCity(cityName) }
            verify(exactly = 1) { eventMapper.mapToDomain(eventsResponse) }
        }

    @Test
    fun `given sut, when fetchEventsForCity is called and api is successful but no resulting events, then return flow that ends in NoAvailableEventsException`() =
        runTest {
            val cityName = "cityName"
            val eventsResponse = EventsResponse(null)
            val successfulEmptyResponse = Result.success(eventsResponse)

            coEvery { eventApi.fetchEventsForCity(cityName) } returns successfulEmptyResponse
            every { eventMapper.mapToDomain(any()) } returns emptyList()

            sut.fetchEventsForCity(cityName).test {
                assertEquals(DataResult.Loading, awaitItem())
                val errorItem = awaitItem()

                assertTrue(
                    "$errorItem is not of type DataLoadingResult.Error",
                    errorItem is DataResult.Error
                )

                val exception = (errorItem as? DataResult.Error)?.exception
                assertTrue(
                    "$exception is not NoAvailableEventsException",
                    exception is NoAvailableEventsException
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { eventApi.fetchEventsForCity(cityName) }
            verify(exactly = 1) { eventMapper.mapToDomain(eventsResponse) }
        }

    @Test
    fun `given sut, when fetchEventsForCity is called and api is unsuccessful, then return flow that ends in failure`() =
        runTest {
            val cityName = "cityName"
            val exception = Exception()
            val unsuccessfulMockResponse = Result.failure<EventsResponse>(exception)

            coEvery { eventApi.fetchEventsForCity(cityName) } returns unsuccessfulMockResponse

            sut.fetchEventsForCity(cityName).test {
                assertEquals(DataResult.Loading, awaitItem())
                assertEquals(DataResult.Error(exception), awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) { eventApi.fetchEventsForCity(cityName) }
            verify(exactly = 0) { eventMapper.mapToDomain(any()) }
        }

    @Suppress("SameParameterValue")
    private fun createTestEvents(cityName: String): List<Event> {
        val testEvent1 = TestEventBuilder.createEvent(id = "1", city = cityName)
        val testEvent2 = TestEventBuilder.createEvent(id = "2", city = cityName)
        return listOf(testEvent1, testEvent2)
    }
}
