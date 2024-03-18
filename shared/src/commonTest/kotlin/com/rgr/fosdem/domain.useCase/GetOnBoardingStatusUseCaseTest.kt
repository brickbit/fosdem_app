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

class GetOnBoardingStatusUseCaseTest {
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
    fun `when onboarding is not shown returns false`() = runTest {
        mocker.everySuspending { local.isOnBoardingSeen() } returns false
        val sut = GetOnBoardingStatusUseCase(local)
        assertFalse(sut.invoke())
    }

    @Test
    fun `when onboarding is shown returns true`() = runTest {
        mocker.everySuspending { local.isOnBoardingSeen() } returns true
        val sut = GetOnBoardingStatusUseCase(local)
        assertTrue(sut.invoke())
    }
}