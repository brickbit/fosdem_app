package com.rgr.fosdem.app.state

import com.rgr.fosdem.app.navigation.Routes

sealed class ScaffoldState {
    data class Initialized(val visible: Boolean, val route: Routes): ScaffoldState()
}