package dev.kr9ly.kolonia.app.screens.child

sealed class ChildScreenTrigger {
    object BackToTop : ChildScreenTrigger()
    object OpenScreenA : ChildScreenTrigger()
    object OpenScreenB : ChildScreenTrigger()
    object BackScreen : ChildScreenTrigger()
}