package com.rgr.fosdem.app.viewModel

import app.cash.turbine.test
import com.rgr.fosdem.app.state.FavouriteEventsState
import com.rgr.fosdem.app.state.MainPreferredTracksState
import com.rgr.fosdem.app.state.MainTracksNowState
import com.rgr.fosdem.app.state.StandsState
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.TalkBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.fakeEventBo
import com.rgr.fosdem.domain.model.fakeSpeakerBo
import com.rgr.fosdem.domain.model.fakeStandBo
import com.rgr.fosdem.domain.model.fakeTalkBo
import com.rgr.fosdem.domain.model.fakeTrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.LocalRepository
import com.rgr.fosdem.domain.repository.MockJsonProvider
import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.repository.MockRealmRepository
import com.rgr.fosdem.domain.repository.MockScheduleRepository
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByTrackUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleDataUseCase
import com.rgr.fosdem.domain.useCase.GetSpeakersUseCase
import com.rgr.fosdem.domain.useCase.GetStandsUseCase
import com.rgr.fosdem.domain.useCase.IsUpdateNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@UsesFakes(
    TrackBo::class,
    EventBo::class,
    TalkBo::class,
    SpeakerBo::class,
    StandBo::class
)
@UsesMocks(
    JsonProvider::class,
    ScheduleRepository::class,
    RealmRepository::class,
    LocalRepository::class
)
class MainViewModelTest {
    private val mocker = Mocker()
    private lateinit var viewModel: MainViewModel
    private lateinit var getSchedule: GetScheduleDataUseCase
    private lateinit var getPreferredTracks: GetPreferredTracksUseCase
    private lateinit var getScheduleByTrack: GetScheduleByTrackUseCase
    private lateinit var getScheduleByHour: GetScheduleByHourUseCase
    private lateinit var getSpeakers: GetSpeakersUseCase
    private lateinit var getFavouritesEvents: GetFavouritesEventsUseCase
    private lateinit var getStands: GetStandsUseCase
    private lateinit var needUpdate: IsUpdateNeeded
    private val local = MockLocalRepository(mocker)
    private val realm = MockRealmRepository(mocker)
    private val network = MockScheduleRepository(mocker)
    private val json = MockJsonProvider(mocker)
    private val track = fakeTrackBo()
    private val event = fakeEventBo()
    private val talk = fakeTalkBo()
    private val speaker = fakeSpeakerBo()
    private val stand = fakeStandBo()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        getSchedule = GetScheduleDataUseCase(
            jsonRepository = json,
            networkRepository = network,
            realmRepository = realm
        )
        getPreferredTracks = GetPreferredTracksUseCase(
            repository = local
        )
        getScheduleByTrack = GetScheduleByTrackUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        getScheduleByHour = GetScheduleByHourUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        getSpeakers = GetSpeakersUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        getFavouritesEvents = GetFavouritesEventsUseCase(
            repository = local
        )
        getStands = GetStandsUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        needUpdate = IsUpdateNeeded(
            repository = network,
            localRepository = local
        )
        viewModel = MainViewModel(
            getSchedule = getSchedule,
            getPreferredTracks = getPreferredTracks,
            getScheduleByTrack = getScheduleByTrack,
            getScheduleByHour = getScheduleByHour,
            getSpeakers = getSpeakers,
            getFavouritesEvents = getFavouritesEvents,
            getStands = getStands,
            needUpdate = needUpdate
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getPreferredTracks is called and there is not schedules then state is empty`() = runTest {
        mocker.everySuspending { local.getPreferences() } returns listOf()

        val sut = viewModel
        sut.statePreferredTracks.test {
            sut.getPreferredTracks()
            assertEquals(MainPreferredTracksState.Loading,awaitItem())
            assertEquals(MainPreferredTracksState.Empty,awaitItem())
        }
    }

    @Test
    fun `when getPreferredTracks is called and there is schedules then state is loaded`() = runTest {
        mocker.everySuspending { local.getPreferences() } returns listOf(track)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(track))
        mocker.everySuspending { realm.getSchedule() } returns listOf(track)

        val sut = viewModel
        sut.statePreferredTracks.test {
            sut.getPreferredTracks()
            assertEquals(MainPreferredTracksState.Loading,awaitItem())
            assertEquals(MainPreferredTracksState.Loaded(listOf(track)),awaitItem())
        }
    }

    @Test
    fun `when getScheduleByMoment is called and there is not schedules then state is empty`() = runTest {
        mocker.everySuspending { local.getPreferences() } returns listOf(track)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(track))
        mocker.everySuspending { realm.getSchedule() } returns listOf(track)

        val sut = viewModel
        sut.stateCurrentTracks.test {
            sut.getScheduleByMoment()
            assertEquals(MainTracksNowState.Loading,awaitItem())
            assertEquals(MainTracksNowState.Empty, awaitItem())
        }
    }

    @Test
    fun `when getScheduleByMoment is called and there is schedules then state is loaded`() = runTest {
        val events = listOf(EventBo(id = "1", day = "Saturday", talk = talk, speaker = listOf(), startHour = "10:15", endHour = "10:45", color = null))
        val trackForMoment = track.copy(events = events)
        mocker.everySuspending { local.getPreferences() } returns listOf(trackForMoment)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(trackForMoment))
        mocker.everySuspending { realm.getSchedule() } returns listOf(trackForMoment)

        val instant = LocalDateTime.parse("2024-02-03T10:00:00").toInstant(TimeZone.currentSystemDefault())
        val sut = viewModel
        sut.stateCurrentTracks.test {
            sut.getScheduleByMoment(instant)
            assertEquals(MainTracksNowState.Loading,awaitItem())
            assertEquals(MainTracksNowState.Loaded(events), awaitItem())
        }
    }

    @Test
    fun `when getFavouritesEvents is called and there is not schedules then state is empty`() = runTest {
        mocker.everySuspending { local.getNotificationEvents() } returns listOf()
        mocker.everySuspending { local.getPreferences() } returns listOf(track)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(track))
        mocker.everySuspending { realm.getSchedule() } returns listOf(track)

        val sut = viewModel
        sut.stateFavouriteEvents.test {
            sut.getFavouritesEvents()
            assertEquals(FavouriteEventsState.Loading,awaitItem())
            assertEquals(FavouriteEventsState.Empty, awaitItem())
        }
    }

    @Test
    fun `when getFavouritesEvents is called and there is schedules then state is loaded`() = runTest {
        mocker.everySuspending { local.getNotificationEvents() } returns listOf(event)
        mocker.everySuspending { local.getPreferences() } returns listOf(track)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(track))
        mocker.everySuspending { realm.getSchedule() } returns listOf(track)

        val sut = viewModel
        sut.stateFavouriteEvents.test {
            sut.getFavouritesEvents()
            assertEquals(FavouriteEventsState.Loading,awaitItem())
            assertEquals(FavouriteEventsState.Loaded(listOf(event)), awaitItem())
        }
    }

    @Test
    fun `when getSpeakerList is called and there is not speakers then state is empty`() = runTest {
        mocker.everySuspending { local.getPreferences() } returns listOf(track)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(track))
        mocker.everySuspending { realm.getSchedule() } returns listOf(track)

        val sut = viewModel
        sut.stateSpeaker.test {
            sut.getSpeakerList()
            assertEquals(SpeakersState.Loading,awaitItem())
            assertEquals(SpeakersState.Empty, awaitItem())
        }
    }

    @Test
    fun `when getSpeakerList is called and there is speakers then state is loaded`() = runTest {
        val speakers = listOf(speaker)
        val events = listOf(EventBo(id = "1", day = "Saturday", talk = talk, speaker = speakers, startHour = "10:15", endHour = "10:45", color = null))
        val trackSpeaker = track.copy(events = events)
        mocker.everySuspending { local.getPreferences() } returns listOf(trackSpeaker)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(trackSpeaker))
        mocker.everySuspending { realm.getSchedule() } returns listOf(trackSpeaker)

        val sut = viewModel
        sut.stateSpeaker.test {
            sut.getSpeakerList()
            assertEquals(SpeakersState.Loading,awaitItem())
            assertEquals(SpeakersState.Loaded(speakers), awaitItem())
        }
    }

    @Test
    fun `when getStandList is called and there is not stands then state is empty`() = runTest {
        val trackStand = track.copy(stands = listOf())
        mocker.everySuspending { local.getPreferences() } returns listOf(trackStand)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(trackStand))
        mocker.everySuspending { realm.getSchedule() } returns listOf(trackStand)

        val sut = viewModel
        sut.stateStand.test {
            sut.getStandList()
            assertEquals(StandsState.Loading,awaitItem())
            assertEquals(StandsState.Empty, awaitItem())
        }
    }

    @Test
    fun `when getStandList is called and there is stands then state is loaded`() = runTest {
        val stands = listOf(stand)
        val trackStand = track.copy(stands = stands)
        mocker.everySuspending { local.getPreferences() } returns listOf(trackStand)
        mocker.everySuspending { network.getSchedule() } returns Result.success(listOf(trackStand))
        mocker.everySuspending { realm.getSchedule() } returns listOf(trackStand)

        val sut = viewModel
        sut.stateStand.test {
            sut.getStandList()
            assertEquals(StandsState.Loading,awaitItem())
            assertEquals(StandsState.Loaded(stands), awaitItem())
        }
    }


}