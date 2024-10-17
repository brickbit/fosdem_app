package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.MockLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SaveOnBoardingUseCaseTest {
    private val mocker = Mocker()
    private val local = MockLocalRepository(mocker)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when use case executes onboarding show variable is saved`() = runTest {
        var isOnBoardingShown = false
        mocker.everySuspending { local.setOnBoardingSeen() } runs {
            isOnBoardingShown = true
        }
        mocker.everySuspending { local.isOnBoardingSeen() } returns isOnBoardingShown
        val sut = SaveOnBoardingUseCase(local)
        assertFalse { local.isOnBoardingSeen() }
        sut.invoke()
        assertTrue { local.isOnBoardingSeen() }
    }
}