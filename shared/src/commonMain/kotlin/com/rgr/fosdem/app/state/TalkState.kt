package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.EventBo

sealed class TalkState {
    data object Loading: TalkState()
    data class Loaded(val event: EventBo): TalkState()
}