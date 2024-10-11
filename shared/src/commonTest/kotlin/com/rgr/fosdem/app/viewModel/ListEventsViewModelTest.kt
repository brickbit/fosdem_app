package com.rgr.fosdem.app.viewModel

import app.cash.turbine.test
import com.rgr.fosdem.app.state.FavouriteEventsState
import com.rgr.fosdem.app.state.MainPreferredTracksState
import com.rgr.fosdem.app.state.MainTracksNowState
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.TalkBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.fakeEventBo
import com.rgr.fosdem.domain.model.fakeTalkBo
import com.rgr.fosdem.domain.model.fakeTrackBo
import com.rgr.fosdem.domain.repository.LocalRepository
import com.rgr.fosdem.domain.repository.MockJsonProvider
import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.repository.MockRealmRepository
import com.rgr.fosdem.domain.repository.MockScheduleRepository
import com.rgr.fosdem.domain.repository.NetworkRepository
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByTrackUseCase
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
)
@UsesMocks(
    JsonProvider::class,
    NetworkRepository::class,
    RealmRepository::class,
    LocalRepository::class
)
class ListEventsViewModelTest {
    private val mocker = Mocker()
    private lateinit var viewModel: ListEventsViewModel
    private lateinit var getPreferredTracks: GetPreferredTracksUseCase
    private lateinit var getScheduleByHour: GetScheduleByHourUseCase
    private lateinit var getScheduleByTrack: GetScheduleByTrackUseCase
    private lateinit var getFavouritesEvents: GetFavouritesEventsUseCase
    private val local = MockLocalRepository(mocker)
    private val realm = MockRealmRepository(mocker)
    private val network = MockScheduleRepository(mocker)
    private val json = MockJsonProvider(mocker)
    private val track = fakeTrackBo()
    private val event = fakeEventBo()
    private val talk = fakeTalkBo()
    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        getPreferredTracks = GetPreferredTracksUseCase(
            repository = local
        )
        getScheduleByHour = GetScheduleByHourUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm,
        )
        getScheduleByTrack = GetScheduleByTrackUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm,
        )
        getFavouritesEvents = GetFavouritesEventsUseCase(
            repository = local
        )
        viewModel = ListEventsViewModel(
            getFavouritesEvents = getFavouritesEvents,
            getScheduleByHour = getScheduleByHour,
            getPreferredTracks = getPreferredTracks,
            getScheduleByTrack = getScheduleByTrack,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when call favorite events and there is not favourite events then state is empty`() = runTest {
        mocker.everySuspending { local.getNotificationEvents() } returns listOf()
        val sut = viewModel
        sut.stateFavouriteEvents.test {
            sut.getFavouritesEvents()
            assertEquals(FavouriteEventsState.Loading, awaitItem())
            assertEquals(FavouriteEventsState.Empty, awaitItem())
        }
    }

    @Test
    fun `when call favorite events and there is favourite events then state is loaded`() = runTest {
        val events = listOf(EventBo(id = "1", day = "Saturday", talk = talk, speaker = listOf(), startHour = "10:15", endHour = "10:45", color = null))
        mocker.everySuspending { local.getNotificationEvents() } returns events
        val sut = viewModel
        sut.stateFavouriteEvents.test {
            sut.getFavouritesEvents()
            assertEquals(FavouriteEventsState.Loading, awaitItem())
            assertEquals(FavouriteEventsState.Loaded(events), awaitItem())
        }
    }

    @Test
    fun `when call getScheduleByMoment and there is not events at this moment then state is empty`() = runTest {
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
    fun `when call getScheduleByMoment and there is events at this moment then state is loaded`() = runTest {
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
    fun `when call getPreferredTracks and there is not favorite events then state is empty`() = runTest {
        mocker.everySuspending { local.getPreferences() } returns listOf()

        val sut = viewModel
        sut.statePreferredTracks.test {
            sut.getPreferredTracks()
            assertEquals(MainPreferredTracksState.Loading,awaitItem())
            assertEquals(MainPreferredTracksState.Empty,awaitItem())
        }
    }

    @Test
    fun `when call getPreferredTracks and there is favorite events then state is loaded`() = runTest {
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
}