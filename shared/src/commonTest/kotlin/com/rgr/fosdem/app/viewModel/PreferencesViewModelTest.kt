package com.rgr.fosdem.app.viewModel

import app.cash.turbine.test
import com.rgr.fosdem.app.state.PreferencesState
import com.rgr.fosdem.domain.model.fakeTrackBo
import com.rgr.fosdem.domain.repository.MockJsonProvider
import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.repository.MockRealmRepository
import com.rgr.fosdem.domain.repository.MockScheduleRepository
import com.rgr.fosdem.domain.useCase.GetSavedTracksUseCase
import com.rgr.fosdem.domain.useCase.GetTracksUseCase
import com.rgr.fosdem.domain.useCase.SaveFavouriteTracksShownUseCase
import com.rgr.fosdem.domain.useCase.SavePreferredTracksUseCase
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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PreferencesViewModelTest {
    private val mocker = Mocker()

    private lateinit var viewModel: PreferencesViewModel
    private lateinit var getTracks: GetTracksUseCase
    private lateinit var getSavedTracks: GetSavedTracksUseCase
    private lateinit var saveTracks: SavePreferredTracksUseCase
    private lateinit var checkShownTracks: SaveFavouriteTracksShownUseCase
    private val local = MockLocalRepository(mocker)
    private val realm = MockRealmRepository(mocker)
    private val network = MockScheduleRepository(mocker)
    private val json = MockJsonProvider(mocker)

    private val track = fakeTrackBo()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        getTracks = GetTracksUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm,
        )
        getSavedTracks = GetSavedTracksUseCase(
            repository = local,
        )
        saveTracks = SavePreferredTracksUseCase(
            repository = local,
        )
        checkShownTracks = SaveFavouriteTracksShownUseCase(
            repository = local,
        )
        viewModel = PreferencesViewModel(
            getTracks = getTracks,
            getSavedTracks = getSavedTracks,
            saveTracks = saveTracks,
            checkShownTracks = checkShownTracks,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when tracks has been loaded and there is not saved tracks then all tracks are unchecked`() = runTest {
        val tracks = listOf(track)
        mocker.everySuspending { network.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { realm.getSchedule() } returns tracks
        mocker.everySuspending { json.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { local.getPreferences() } returns listOf()

        val sut = viewModel
        sut.state.test {
            assertEquals(PreferencesState.Loading,awaitItem())
            sut.getPreferences()
            assertEquals(PreferencesState.Loaded(tracks), awaitItem())
        }
    }
    @Test
    fun `when tracks has been loaded and there is saved tracks then returns all tracks with saved tracks checked`() = runTest {
        val tracks = listOf(track.copy(name = "a"), track.copy(name = "b"))
        val localTracks = listOf(track.copy(name = "a"))
        val expectedTracks = listOf(track.copy(name = "a", checked = true), track.copy(name = "b"))
        mocker.everySuspending { network.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { realm.getSchedule() } returns tracks
        mocker.everySuspending { json.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { local.getPreferences() } returns localTracks

        val sut = viewModel
        sut.state.test {
            assertEquals(PreferencesState.Loading,awaitItem())
            sut.getPreferences()
            assertEquals(PreferencesState.Loaded(expectedTracks), awaitItem())
        }
    }

    @Test
    fun `when check a track from list then track change check status`() = runTest {
        val tracks = listOf(track.copy(name = "a"), track.copy(name = "b"))
        mocker.everySuspending { network.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { realm.getSchedule() } returns tracks
        mocker.everySuspending { json.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { local.getPreferences() } returns listOf()

        val sut = viewModel
        sut.state.test {
            awaitItem()
            sut.getPreferences()
            val newTrackList = (awaitItem() as PreferencesState.Loaded).tracks

            sut.onTrackChecked(newTrackList[0], true)
            val newTrackListChecked = (awaitItem() as PreferencesState.Loaded).tracks
            assertTrue { newTrackListChecked[0].checked }

            viewModel.onTrackChecked(newTrackListChecked[0], false)
            val newTrackListCheckedFalse = (awaitItem() as PreferencesState.Loaded).tracks
            assertFalse { newTrackListCheckedFalse[0].checked }

            viewModel.onTrackChecked(newTrackListChecked[1], true)
            val newTrackListCheckedTrue = (awaitItem() as PreferencesState.Loaded).tracks
            assertTrue { newTrackListCheckedTrue[1].checked }
        }
    }

    @Test
    fun `when press save button state is saved`() = runTest {
        val tracks = listOf(track.copy(name = "a"), track.copy(name = "b"))
        mocker.everySuspending { network.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { realm.getSchedule() } returns tracks
        mocker.everySuspending { json.getSchedule() } returns Result.success(tracks)
        mocker.everySuspending { local.getPreferences() } returns listOf()
        mocker.everySuspending { local.setFavouritesTracksSeen() } returns Unit
        mocker.everySuspending { local.setPreferences(listOf()) } returns Unit

        val sut = viewModel
        sut.state.test {
            awaitItem()
            sut.getPreferences()
            awaitItem()
            sut.savePreferredTracks(tracks)
            awaitItem()
            awaitItem()
            assertEquals(PreferencesState.Saved, sut.state.value)
        }
    }

    @Test
    fun `when press skip button shown tracks variable is true`() = runTest {
        mocker.everySuspending { local.isFavouriteTracksSeen() } returns true
        mocker.everySuspending { local.setFavouritesTracksSeen() } returns Unit
        viewModel.skipFavouriteTracks()
        assertTrue { local.isFavouriteTracksSeen() }
    }

    @Test
    fun `when checked tracks are equal or greater than 3 returns true`() = runTest {
        mocker.everySuspending { local.isOnBoardingSeen() } returns false

        val tracks = listOf(
            track.copy(checked = false),
            track.copy(checked = false),
            track.copy(checked = false),
        )
        assertFalse { viewModel.enableContinueButton(tracks) }

        val tracks2 = listOf(
            track.copy(checked = true),
            track.copy(checked = false),
            track.copy(checked = false),
        )

        assertTrue { viewModel.enableContinueButton(tracks2) }
    }
}