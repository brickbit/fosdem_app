package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.SpeakerBo

sealed class SpeakersState {
    data object Loading: SpeakersState()
    data class Loaded(val speakers: List<SpeakerBo>): SpeakersState()
}