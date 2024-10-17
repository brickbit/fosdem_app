package com.rgr.fosdem.android.provider

import androidx.activity.ComponentActivity

class ActivityProvider {

    private var providedActivity: ComponentActivity? = null

    fun setActivity(activity: ComponentActivity) {
        providedActivity = activity
    }

    fun getActivity(): ComponentActivity? {
        return providedActivity
    }
}