package com.rgr.fosdem.app.viewModel

import app.cash.turbine.test
import com.rgr.fosdem.app.state.LanguageState
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.provider.LanguageProvider
import com.rgr.fosdem.domain.provider.MockLanguageProvider
import com.rgr.fosdem.domain.useCase.ChangeLanguageUseCase
import com.rgr.fosdem.domain.useCase.GetLanguageUseCase
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
    LanguageProvider::class,
)
class LanguageViewModelTest {
    private val mocker = Mocker()
    private lateinit var viewModel: LanguageViewModel
    private lateinit var languageUseCase: GetLanguageUseCase
    private lateinit var changeLanguageUseCase: ChangeLanguageUseCase
    private val languageProvider: LanguageProvider = MockLanguageProvider(mocker)
    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        languageUseCase = GetLanguageUseCase(
            provider = languageProvider,
        )
        changeLanguageUseCase = ChangeLanguageUseCase(
            provider = languageProvider
        )
        viewModel = LanguageViewModel(
            languageUseCase = languageUseCase,
            changeLanguageUseCase = changeLanguageUseCase,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getLanguage is called the state is loaded with languages`() = runTest {
        val languages = listOf("Español", "English")
        mocker.every { languageProvider.getLanguages() } returns  languages
        val sut = viewModel
        sut.state.test {
            viewModel.getLanguages()
            awaitItem()
            assertEquals(LanguageState.LanguageLoaded(languages),awaitItem())
        }
    }

    @Test
    fun `when`() = runTest {
        val languages = listOf("Español", "English")
        val sut = viewModel
        sut.state.test {
            sut.onChangeLanguage("English")
        }
    }

}