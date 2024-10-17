package com.rgr.fosdem.app.viewModel

import com.rgr.fosdem.domain.repository.MockLocalRepository
import com.rgr.fosdem.domain.useCase.GetNotificationTimeUseCase
import com.rgr.fosdem.domain.useCase.GetNotificationsEnabledUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationTimeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class SettingsViewModelTest {
    private val mocker = Mocker()
    private lateinit var viewModel: SettingsViewModel
    private lateinit var notificationsEnabled: GetNotificationsEnabledUseCase
    private lateinit var manageNotificationPermission: ManageNotificationPermissionUseCase
    private lateinit var manageNotificationTime: ManageNotificationTimeUseCase
    private lateinit var getNotificationTime: GetNotificationTimeUseCase
    private val local = MockLocalRepository(mocker)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        notificationsEnabled = GetNotificationsEnabledUseCase(
            repository = local,
        )
        manageNotificationPermission = ManageNotificationPermissionUseCase(
            repository = local,
        )
        manageNotificationTime = ManageNotificationTimeUseCase(
            repository = local,
        )
        getNotificationTime = GetNotificationTimeUseCase(
            repository = local,
        )
        viewModel = SettingsViewModel(
            notificationsEnabled = notificationsEnabled,
            manageNotificationPermission = manageNotificationPermission,
            manageNotificationTime = manageNotificationTime,
            getNotificationTime = getNotificationTime,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}