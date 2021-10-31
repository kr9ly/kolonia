package dev.kr9ly.kolonia

import dev.kr9ly.kolonia.provider.InstanceProvider

class DaggerInstanceProvider<DaggerComponent, T>(
    private val getter: (DaggerComponent) -> T
) : InstanceProvider<DaggerComponent, T> {

    override fun provide(diProvider: DaggerComponent): T = getter(diProvider)
}