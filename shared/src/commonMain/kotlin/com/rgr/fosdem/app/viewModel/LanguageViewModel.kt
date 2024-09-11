package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.state.LanguageState
import com.rgr.fosdem.domain.useCase.ChangeLanguageUseCase
import com.rgr.fosdem.domain.useCase.GetLanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val languageUseCase: GetLanguageUseCase,
    private val changeLanguageUseCase: ChangeLanguageUseCase
): ViewModel() {

    private val _state: MutableStateFlow<LanguageState> = MutableStateFlow(LanguageState.NoLanguages)
    val state = _state.asStateFlow()

    fun getLanguages(){
        viewModelScope.launch {
            _state.update {
                LanguageState.LanguageLoaded(languageUseCase.invoke())
            }
        }
    }

    fun onChangeLanguage(language: String) {
        changeLanguageUseCase(language)
    }
}