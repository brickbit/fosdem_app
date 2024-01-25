package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.SpeakerBo

sealed class SpeakersState {
    data object Loading: SpeakersState()
    data class Loaded(val speakers: List<SpeakerBo>): SpeakersState()
}