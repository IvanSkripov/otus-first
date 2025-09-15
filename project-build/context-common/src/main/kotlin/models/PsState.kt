package ru.otus.kotlin.course.common.models

enum class PsState {
    NONE,
    RUNNING,
    FAILING,
    FINISHING;

    fun isPositive(): Boolean = this == RUNNING || this == FINISHING
}