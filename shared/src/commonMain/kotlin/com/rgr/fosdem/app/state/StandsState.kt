package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.StandBo

sealed class StandsState {
    data object Loading: StandsState()
    data object Empty: StandsState()
    data class Loaded(val stands: List<StandBo>): StandsState()
}