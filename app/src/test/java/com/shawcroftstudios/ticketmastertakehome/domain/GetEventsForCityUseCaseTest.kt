package com.shawcroftstudios.ticketmastertakehome.domain

import app.cash.turbine.test
import com.shawcroftstudios.ticketmastertakehome.data.DataResult
import com.shawcroftstudios.ticketmastertakehome.data.DataSource
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
import com.shawcroftstudios.ticketmastertakehome.data.repository.LocalEventListRepository
import com.shawcroftstudios.ticketmastertakehome.data.repository.RemoteEventListRepository
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.domain.usecase.GetEventsForCityUsecaseImpl
import com.shawcroftstudios.ticketmastertakehome.helpers.TestEventBuilder
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetEventsForCityUseCaseTest {

    @MockK
    lateinit var dispatcherProvider: DispatcherProvider

    @MockK
    lateinit var localRepository: LocalEventListRepository

    @MockK
    lateinit var remoteRepository: RemoteEventListRepository

    @InjectMockKs
    lateinit var sut: GetEventsForCityUsecaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { dispatcherProvider.io } returns Dispatchers.Unconfined
    }

    @Test
    fun `given sut, when execute is called with successful local and successful remote flows, then return contents of remote flow`() =
        runTest {
            val cityName = "TestCity"
            val localTestEvents = listOf(
                TestEventBuilder.createEvent("localId1", city = cityName),
                TestEventBuilder.createEvent("localId2", city = cityName)
            )
            val remoteTestEvents =
                listOf(TestEventBuilder.createEvent("remoteId1", city = cityName))

            val expected = DataResult.Success(remoteTestEvents, DataSource.Remote)

            every { localRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Success(
                    localTestEvents,
                    DataSource.Local,
                )
            )
            coJustRun { localRepository.insertEvents(any()) }
            every { remoteRepository.fetchEventsForCity(cityName) } returns flowOf(expected)

            sut.execute(cityName).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) { localRepository.insertEvents(remoteTestEvents) }
            verify(exactly = 1) { dispatcherProvider.io }
        }

    // a successful empty remote response should have generally priority over a successful local response- indicative of stale data
    @Test
    fun `given sut, when execute is called with successful local and successful remote flows but remote data is empty, then return NoAvailableEventsException`() =
        runTest {
            val cityName = "TestCity"
            val localTestEvents = listOf(
                TestEventBuilder.createEvent("localId1", city = cityName),
                TestEventBuilder.createEvent("localId2", city = cityName)
            )
            val remoteTestEvents: List<Event> =
                emptyList()

            every { localRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Success(
                    localTestEvents,
                    DataSource.Local,
                )
            )
            every { remoteRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Success(
                    remoteTestEvents,
                    DataSource.Remote,
                )
            )

            sut.execute(cityName).test {
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

            coVerify(exactly = 0) { localRepository.insertEvents(remoteTestEvents) }
            verify(exactly = 1) { dispatcherProvider.io }
        }

    @Test
    fun `given sut, when execute is called with 1 successful remote flow item and multiple unsuccessful local flow results, then remote response shown and local db only updated once`() =
        runTest {
            val cityName = "TestCity"
            val remoteTestEvents =
                listOf(TestEventBuilder.createEvent("remoteId1", city = cityName))

            val expected = DataResult.Success(remoteTestEvents, DataSource.Remote)

            every { localRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Loading(),
                DataResult.Error(NoAvailableEventsException()),
            )

            every { remoteRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Success(
                    remoteTestEvents,
                    DataSource.Remote
                )
            )

            coJustRun { localRepository.insertEvents(any()) }

            sut.execute(cityName).test {
                assertEquals(expected, awaitItem())
                assertEquals(expected, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) { localRepository.insertEvents(remoteTestEvents) }
            verify(exactly = 1) { dispatcherProvider.io }
        }

    @Test
    fun `given sut, when execute is called with successful local and loading then unsuccessful remote flow, then return loading flow with local data then success with fallback data source`() =
        runTest {
            val cityName = "TestCity"
            val localTestEvents = listOf(
                TestEventBuilder.createEvent("localId1", city = cityName),
                TestEventBuilder.createEvent("localId2", city = cityName)
            )

            val expectedFirstItem = DataResult.Loading(localTestEvents, DataSource.Local)
            val expectedSecondItem = DataResult.Success(localTestEvents, DataSource.LocalFallback)

            every { localRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Success(
                    localTestEvents,
                    DataSource.Local
                )
            )
            every { remoteRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Loading(),
                DataResult.Error(NoAvailableEventsException())
            )

            sut.execute(cityName).test {
                assertEquals(
                    expectedFirstItem,
                    awaitItem()
                ) // remote is loading, still show local db data
                assertEquals(
                    expectedSecondItem,
                    awaitItem()
                ) // remote has failed, still show local db data
                awaitComplete()
            }

            coVerify(exactly = 0) { localRepository.insertEvents(any()) }
            verify(exactly = 1) { dispatcherProvider.io }
        }

    @Test
    fun `given sut, when execute is called with unsuccessful local but remote flow is in loading state, then return loading flow`() =
        runTest {
            val cityName = "TestCity"
            val expected = DataResult.Loading<List<Event>>()

            every { localRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Error(NoAvailableEventsException())
            )
            every { remoteRepository.fetchEventsForCity(cityName) } returns flowOf(DataResult.Loading())

            sut.execute(cityName).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 0) { localRepository.insertEvents(any()) }
            verify(exactly = 1) { dispatcherProvider.io }
        }

    @Test
    fun `given sut, when execute is called with unsuccessful local and unsuccessful remote flows, then return NoAvailableEventsException`() =
        runTest {
            val cityName = "TestCity"

            every { localRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Error(Exception())
            )

            every { remoteRepository.fetchEventsForCity(cityName) } returns flowOf(
                DataResult.Error(Exception())
            )

            sut.execute(cityName).test {

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

            coVerify(exactly = 0) { localRepository.insertEvents(any()) }
            verify(exactly = 1) { dispatcherProvider.io }
        }
}
