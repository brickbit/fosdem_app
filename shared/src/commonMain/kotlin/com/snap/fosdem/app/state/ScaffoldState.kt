package com.snap.fosdem.app.state

import com.snap.fosdem.app.navigation.Routes

sealed class ScaffoldState {
    data class Initialized(val visible: Boolean, val route: Routes): ScaffoldState()
}