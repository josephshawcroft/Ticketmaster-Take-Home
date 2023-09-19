package com.shawcroftstudios.ticketmastertakehome.domain

import app.cash.turbine.test
import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.repository.EventListRepository
import com.shawcroftstudios.ticketmastertakehome.domain.usecase.GetEventsForCityUsecaseImpl
import com.shawcroftstudios.ticketmastertakehome.helpers.TestEventBuilder
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetEventsForCityUseCaseTest {

    @MockK
    lateinit var dispatcherProvider: DispatcherProvider

    @MockK
    lateinit var repository: EventListRepository

    @InjectMockKs
    lateinit var sut: GetEventsForCityUsecaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { dispatcherProvider.io } returns Dispatchers.Unconfined
    }

    @Test
    fun `given sut, when execute is called, then return Flow of DataLoadingResult`() =
        runTest {
            val cityName = "TestCity"
            val testEvent = TestEventBuilder.createEvent(city = cityName)
            val testData = listOf(testEvent)

            val expectedLoadingResult = DataLoadingResult.Loading
            val expectedSuccessResult = DataLoadingResult.Success(testData)
            val expectedErrorResult = DataLoadingResult.Error(Exception())

            coEvery { repository.fetchEventsForCity(cityName) } returns flowOf(
                expectedLoadingResult,
                expectedSuccessResult,
                expectedErrorResult
            )

            sut.execute(cityName).test {
                assertEquals(expectedLoadingResult, awaitItem())
                assertEquals(expectedSuccessResult, awaitItem())
                assertEquals(expectedErrorResult, awaitItem())
                awaitComplete()
            }

            verify(exactly = 1) { repository.fetchEventsForCity(cityName) }
            verify(exactly = 1) { dispatcherProvider.io }
        }
}
