package com.rgr.fosdem.app.viewModel

import app.cash.turbine.test
import com.rgr.fosdem.app.state.FavouriteEventsState
import com.rgr.fosdem.app.state.ScheduleFilter
import com.rgr.fosdem.app.state.ScheduleState
import com.rgr.fosdem.domain.repository.MockJsonProvider
import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.repository.MockRealmRepository
import com.rgr.fosdem.domain.repository.MockScheduleRepository
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetHoursUseCase
import com.rgr.fosdem.domain.useCase.GetRoomsUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByParameterUseCase
import com.rgr.fosdem.domain.useCase.GetTracksUseCase
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

class ScheduleViewModelTest {
    private val mocker = Mocker()
    private lateinit var viewModel: ScheduleViewModel
    private lateinit var getFavouritesEvents: GetFavouritesEventsUseCase
    private lateinit var getScheduleByParameter: GetScheduleByParameterUseCase
    private lateinit var getRoomsUseCase: GetRoomsUseCase
    private lateinit var getTracksUseCase: GetTracksUseCase
    private lateinit var getHoursUseCase: GetHoursUseCase
    private val realm = MockRealmRepository(mocker)
    private val network = MockScheduleRepository(mocker)
    private val json = MockJsonProvider(mocker)
    private val local = MockLocalRepository(mocker)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        getFavouritesEvents = GetFavouritesEventsUseCase(
            repository = local
        )
        getScheduleByParameter = GetScheduleByParameterUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        getRoomsUseCase = GetRoomsUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        getTracksUseCase = GetTracksUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        getHoursUseCase = GetHoursUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm
        )
        viewModel = ScheduleViewModel(
            getHoursUseCase = getHoursUseCase,
            getTracksUseCase = getTracksUseCase,
            getRoomsUseCase = getRoomsUseCase,
            getScheduleByParameter = getScheduleByParameter,
            getFavouritesEvents = getFavouritesEvents,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getHours is called then state is a list of String containing hours`() = runTest {
        val sut = viewModel
        sut.stateHour.test {
            sut.getHours("Saturday")
            val list = listOf("")
            //assertEquals(listOf(""), sut.stateHour)
        }
    }

    @Test
    fun `when getTracks is called then state is a list of String containing the name of tracks`() =
        runTest {
            /*val sut = viewModel
            sut.getTracks()
            assertEquals(listOf(""),sut.stateTracks)*/
        }

    @Test
    fun `when getRooms is called then state is a list of String containing the name of rooms`() =
        runTest {
            /*val sut = viewModel
            sut.stateRooms.test {
                sut.getRooms("")
                assertEquals(listOf(""),sut.stateRooms)
            }*/
        }

    @Test
    fun `when call getFavouritesEvents and there is not favorite events then state is empty`() =
        runTest {
            val sut = viewModel
            sut.stateFavouriteEvents.test {
                sut.getFavouritesEvents()
                assertEquals(FavouriteEventsState.Empty,awaitItem())
            }
        }

    @Test
    fun `when call getFavouritesEvents and there is favorite events then state is loaded`() =
        runTest {
            val sut = viewModel
            sut.stateFavouriteEvents.test {
                sut.getFavouritesEvents()
                assertEquals(FavouriteEventsState.Loading,awaitItem())
            }
        }

    @Test
    fun `when remove hour from filter and the current state is empty and the is not tracks for selected hour then state is empty`() = runTest {
        val sut = viewModel
        sut.state.test {
            sut.getScheduleBy("", listOf(),"","")
            sut.removeSelectedHour("")
            assertEquals(ScheduleState.Empty(ScheduleFilter()),awaitItem())
        }
    }

    @Test
    fun `when remove hour from filter and the current state is empty and the is tracks for selected hour then state is loaded`() = runTest {
        val sut = viewModel
        sut.state.test {
            sut.getScheduleBy("", listOf(),"","")
            sut.removeSelectedHour("")
            assertEquals(ScheduleState.Loaded(ScheduleFilter()),awaitItem())
        }
    }

    @Test
    fun `when remove hour from filter and the current state is loaded and the is tracks for selected hour then state is loaded`() = runTest {
        val sut = viewModel
        sut.state.test {
            sut.getScheduleBy("", listOf(),"","")
            sut.removeSelectedHour("")
            assertEquals(ScheduleState.Loaded(ScheduleFilter()),awaitItem())
        }
    }






    @Test
    fun `when add hour from filter and the current state is empty and the is not tracks for selected hours then state is empty`() = runTest {
        val sut = viewModel
        sut.state.test {
            sut.getScheduleBy("", listOf(),"","")
            sut.addSelectedHour("")
            assertEquals(ScheduleState.Empty(ScheduleFilter()),awaitItem())
        }
    }

    @Test
    fun `when add hour from filter and the current state is loaded and the is tracks for selected hour then state is loaded`() = runTest {
        val sut = viewModel
        sut.state.test {
            sut.getScheduleBy("", listOf(),"","")
            sut.removeSelectedHour("")
            assertEquals(ScheduleState.Loaded(ScheduleFilter()),awaitItem())
        }
    }

    @Test
    fun `when add hour from filter and the current state is loaded and the is not tracks for selected hour then state is empty`() = runTest {
        val sut = viewModel
        sut.state.test {
            sut.getScheduleBy("", listOf(),"","")
            sut.removeSelectedHour("")
            assertEquals(ScheduleState.Loaded(ScheduleFilter()),awaitItem())
        }
    }
}