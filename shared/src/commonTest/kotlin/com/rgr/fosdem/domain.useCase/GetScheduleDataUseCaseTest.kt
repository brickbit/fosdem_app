package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.fakeTrackBo
import com.rgr.fosdem.domain.repository.MockJsonProvider
import com.rgr.fosdem.domain.repository.MockRealmRepository
import com.rgr.fosdem.domain.repository.MockScheduleRepository
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
import kotlin.test.assertTrue

class GetScheduleDataUseCaseTest {

    private val mocker = Mocker()
    private val jsonRepository = MockJsonProvider(mocker)
    private val networkRepository = MockScheduleRepository(mocker)
    private val realmRepository = MockRealmRepository(mocker)
    private val track = fakeTrackBo()

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
    fun `when schedule is saved in realm returns realm values`() = runTest {
        mocker.everySuspending { realmRepository.getSchedule() } returns listOf(track)
        val sut = GetScheduleDataUseCase(
            jsonRepository = jsonRepository,
            networkRepository = networkRepository,
            realmRepository = realmRepository
        )
        val result: Result<List<TrackBo>> = sut.invoke()
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()?.get(0), track)
    }

    @Test
    fun `when schedule is not saved in realm and the request to service is ok returns network service values`() = runTest {
        mocker.everySuspending { realmRepository.getSchedule() } returns listOf()
        mocker.everySuspending { realmRepository.saveSchedule(listOf(track)) } returns Unit
        mocker.everySuspending { networkRepository.getSchedule() } returns Result.success(listOf(track))

        val sut = GetScheduleDataUseCase(
            jsonRepository = jsonRepository,
            networkRepository = networkRepository,
            realmRepository = realmRepository
        )
        val result: Result<List<TrackBo>> = sut.invoke()
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()?.get(0), track)
    }

    @Test
    fun `when schedule is not saved in realm and the request to service is ko returns local json values`() = runTest {
        mocker.everySuspending { realmRepository.getSchedule() } returns listOf()
        mocker.everySuspending { networkRepository.getSchedule() } returns Result.failure(Error())
        mocker.everySuspending { jsonRepository.getSchedule() } returns Result.success(listOf(track))

        val sut = GetScheduleDataUseCase(
            jsonRepository = jsonRepository,
            networkRepository = networkRepository,
            realmRepository = realmRepository
        )
        val result: Result<List<TrackBo>> = sut.invoke()
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()?.get(0), track)
    }
}