package dev.kr9ly.kolonia.app.screens

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ScreensAppRoute : Parcelable {

    @Parcelize
    object RootScreen : ScreensAppRoute()

    @Parcelize
    object ScreenA : ScreensAppRoute()

    @Parcelize
    object ScreenB : ScreensAppRoute()
}
