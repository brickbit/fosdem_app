package com.rgr.fosdem.app.viewModel

import com.rgr.fosdem.domain.repository.MockJsonProvider
import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.repository.MockRealmRepository
import com.rgr.fosdem.domain.repository.MockScheduleRepository
import com.rgr.fosdem.domain.useCase.GetEventByIdUseCase
import com.rgr.fosdem.domain.useCase.IsEventNotifiedUseCase
import com.rgr.fosdem.domain.useCase.ManageEventNotificationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class TalkViewModelTest {
    private val mocker = Mocker()
    private lateinit var viewModel: TalkViewModel
    private lateinit var getEventById: GetEventByIdUseCase
    private lateinit var manageEventNotification: ManageEventNotificationUseCase
    private lateinit var isEventNotified: IsEventNotifiedUseCase
    private val realm = MockRealmRepository(mocker)
    private val network = MockScheduleRepository(mocker)
    private val json = MockJsonProvider(mocker)
    private val local = MockLocalRepository(mocker)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        getEventById = GetEventByIdUseCase(
            jsonProvider = json,
            repository = network,
            realmRepository = realm,
        )
        manageEventNotification = ManageEventNotificationUseCase(
            repository = local,
        )
        isEventNotified = IsEventNotifiedUseCase(
            repository = local,
        )
        viewModel = TalkViewModel(
            getEventById = getEventById,
            manageEventNotification = manageEventNotification,
            isEventNotified = isEventNotified,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }


}