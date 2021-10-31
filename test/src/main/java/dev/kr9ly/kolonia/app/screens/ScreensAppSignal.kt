package dev.kr9ly.kolonia.app.screens

sealed class ScreensAppSignal {
    object BackToRoot : ScreensAppSignal()
    object MoveToScreenA : ScreensAppSignal()
    object MoveToScreenB : ScreensAppSignal()
    object BackScreen : ScreensAppSignal()
}
