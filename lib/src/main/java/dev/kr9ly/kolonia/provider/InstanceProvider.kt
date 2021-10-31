package dev.kr9ly.kolonia.provider

interface InstanceProvider<DiProvider, T> {

    fun provide(diProvider: DiProvider): T
}