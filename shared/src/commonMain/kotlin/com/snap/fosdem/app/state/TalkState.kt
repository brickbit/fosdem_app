package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.EventBo

sealed class TalkState {
    data object Loading: TalkState()
    data class Loaded(val event: EventBo): TalkState()
}