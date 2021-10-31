package dev.kr9ly.kolonia.app

interface Mapper<Props, State, View> {

    fun generate(props: Props, state: State): View
}