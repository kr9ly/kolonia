package dev.kr9ly.kolonia

import dagger.Component
import dev.kr9ly.kolonia.app.screens.ScreensAppComponent
import dev.kr9ly.kolonia.app.todo.simple.SimpleToDoComponent

@Component
interface KoloniaTestComponent : SimpleToDoComponent, ScreensAppComponent {
}