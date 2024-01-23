package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.StandBo

sealed class StandsState {
    data object Loading: StandsState()
    data class Loaded(val stands: List<StandBo>): StandsState()
}