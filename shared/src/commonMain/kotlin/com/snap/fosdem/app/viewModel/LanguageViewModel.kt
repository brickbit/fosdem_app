package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.LanguageState
import com.snap.fosdem.domain.useCase.ChangeLanguageUseCase
import com.snap.fosdem.domain.useCase.GetLanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val languageUseCase: GetLanguageUseCase,
    private val changeLanguageUseCase: ChangeLanguageUseCase

): BaseViewModel() {

    private val _state: MutableStateFlow<LanguageState> = MutableStateFlow(LanguageState.NoLanguages)
    val state = _state.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = LanguageState.NoLanguages
    ).toCommonStateFlow()
    fun getLanguages(){
        scope.launch {
            _state.update {
                LanguageState.LanguageLoaded(languageUseCase.invoke())
            }
        }
    }
    fun onChangeLanguage(language: String) {
        changeLanguageUseCase(language)
    }
}