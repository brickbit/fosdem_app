package com.rgr.fosdem.app.viewModel

import app.cash.turbine.test
import com.rgr.fosdem.app.navigation.Routes
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.fakeTrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.LocalRepository
import com.rgr.fosdem.domain.repository.MockJsonProvider
import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.repository.MockRealmRepository
import com.rgr.fosdem.domain.repository.MockScheduleRepository
import com.rgr.fosdem.domain.repository.NetworkRepository
import com.rgr.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksShownUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleDataUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationPermissionUseCase
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
class SplashViewModelTest {
    val mocker = Mocker()

    private lateinit var getSchedule: GetScheduleDataUseCase
    private lateinit var getOnBoarding: GetOnBoardingStatusUseCase
    private lateinit var isFavourite: GetPreferredTracksShownUseCase
    private lateinit var manageNotifications: ManageNotificationPermissionUseCase
    private lateinit var viewModel: SplashViewModel
    private val local = MockLocalRepository(mocker)
    private val realm = MockRealmRepository(mocker)
    private val network = MockScheduleRepository(mocker)
    private val json = MockJsonProvider(mocker)

    private val track = fakeTrackBo()


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        getSchedule = GetScheduleDataUseCase(
            jsonRepository = json,
            networkRepository = network,
            realmRepository = realm,
        )
        getOnBoarding = GetOnBoardingStatusUseCase(
            localRepository = local
        )
        isFavourite = GetPreferredTracksShownUseCase(
            repository = local
        )
        manageNotifications = ManageNotificationPermissionUseCase(
            repository = local
        )
        viewModel = SplashViewModel(
            dispatcher = StandardTestDispatcher(),
            getSchedule = getSchedule,
            getOnBoardingStatus = getOnBoarding,
            isFavouriteTracksShown = isFavourite,
            manageNotificationPermission = manageNotifications,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is init state is initialized`() = runTest {
        assertEquals(SplashState.Init,viewModel.state.value)
    }

    @Test
    fun `when onboarding is not shown and favorite tracks is not shown navigates to onboarding`() = runTest(
        StandardTestDispatcher()
    ) {
        mocker.everySuspending { local.isOnBoardingSeen() } returns false
        mocker.everySuspending { local.isFavouriteTracksSeen() } returns false
        mocker.everySuspending { local.getPreferences() } returns listOf()
        mocker.everySuspending { realm.getSchedule() } returns listOf()
        mocker.everySuspending { realm.saveSchedule(listOf()) } returns Unit
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf())
        mocker.everySuspending { json.getSchedule() } returns Result.success(listOf())

        val sut = viewModel
        sut.state.test {
            assertEquals(SplashState.Init,awaitItem())
            sut.initializeSplash()
            assertEquals(SplashState.Finished(Routes.OnBoarding),awaitItem())
        }
    }

    @Test
    fun `when onboarding is not shown and favorite tracks is shown navigates to onboarding`() = runTest(
        StandardTestDispatcher()
    ) {
        mocker.everySuspending { local.isOnBoardingSeen() } returns false
        mocker.everySuspending { local.isFavouriteTracksSeen() } returns true
        mocker.everySuspending { local.getPreferences() } returns listOf()
        mocker.everySuspending { realm.getSchedule() } returns listOf()
        mocker.everySuspending { realm.saveSchedule(listOf()) } returns Unit
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf())
        mocker.everySuspending { json.getSchedule() } returns Result.success(listOf())

        val sut = viewModel
        sut.state.test {
            assertEquals(SplashState.Init,awaitItem())
            sut.initializeSplash()
            assertEquals(SplashState.Finished(Routes.OnBoarding),awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onboarding is shown and favorite tracks is not shown navigates to favourite tracks`()  = runTest(
        StandardTestDispatcher()
    ) {
        mocker.everySuspending { local.isOnBoardingSeen() } returns true
        mocker.everySuspending { local.isFavouriteTracksSeen() } returns false
        mocker.everySuspending { local.getPreferences() } returns listOf()
        mocker.everySuspending { realm.getSchedule() } returns listOf()
        mocker.everySuspending { realm.saveSchedule(listOf()) } returns Unit
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf())
        mocker.everySuspending { json.getSchedule() } returns Result.success(listOf())

        val sut = viewModel
        sut.state.test {
            assertEquals(SplashState.Init,awaitItem())
            sut.initializeSplash()
            assertEquals(SplashState.Finished(Routes.FavouriteTracks),awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onboarding is shown and favorite tracks is shown navigates to main`() = runTest(
        StandardTestDispatcher()
    ) {
        mocker.everySuspending { local.isOnBoardingSeen() } returns true
        mocker.everySuspending { local.isFavouriteTracksSeen() } returns true
        mocker.everySuspending { local.getPreferences() } returns listOf()
        mocker.everySuspending { realm.getSchedule() } returns listOf()
        mocker.everySuspending { realm.saveSchedule(listOf()) } returns Unit
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf())
        mocker.everySuspending { json.getSchedule() } returns Result.success(listOf())

        val sut = viewModel
        sut.state.test {
            assertEquals(SplashState.Init,awaitItem())
            sut.initializeSplash()
            assertEquals(SplashState.Finished(Routes.Main),awaitItem())
        }
    }

}