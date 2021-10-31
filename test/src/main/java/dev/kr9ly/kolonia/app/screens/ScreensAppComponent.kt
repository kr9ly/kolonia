package dev.kr9ly.kolonia.app.screens

import dev.kr9ly.kolonia.app.screens.child.ChildScreenModel
import dev.kr9ly.kolonia.app.screens.root.RootScreenModel

interface ScreensAppComponent {

    fun screensAppRootScreenHive(): RootScreenModel.RootScreenHive

    fun screensAppRootScreenMapper(): RootScreenModel.RootScreenMapper

    fun screensAppChildScreenHive(): ChildScreenModel.ChildScreenHive

    fun screensAppChildScreenMapper(): ChildScreenModel.ChildScreenMapper
}