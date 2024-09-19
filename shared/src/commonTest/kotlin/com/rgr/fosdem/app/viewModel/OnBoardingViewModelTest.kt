package com.rgr.fosdem.app.viewModel

import app.cash.turbine.test
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.LocalRepository
import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.NetworkRepository
import com.rgr.fosdem.domain.useCase.SaveOnBoardingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@UsesFakes(TrackBo::class)
@UsesMocks(
    JsonProvider::class,
    NetworkRepository::class,
    RealmRepository::class,
    LocalRepository::class
)
class OnBoardingViewModelTest {
    private val mocker = Mocker()
    private lateinit var viewModel: OnBoardingViewModel
    private lateinit var saveOnBoarding: SaveOnBoardingUseCase
    private val local = MockLocalRepository(mocker)
    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        saveOnBoarding = SaveOnBoardingUseCase(local)
        viewModel = OnBoardingViewModel(
            useCase = saveOnBoarding
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when saveOnBoarding is called then state is finished`() = runTest {
        mocker.everySuspending { local.setOnBoardingSeen() } returns Unit
        mocker.everySuspending { local.isOnBoardingSeen() } returns true

        val sut = viewModel
        sut.state.test {
            sut.saveOnBoarding()
            assertEquals(OnBoardingState.Init, awaitItem())
            assertEquals(OnBoardingState.Finished, awaitItem())
        }
    }
}