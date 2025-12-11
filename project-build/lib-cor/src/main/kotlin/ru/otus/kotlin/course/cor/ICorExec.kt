package ru.otus.kotlin.course.cor

// Базовый интерфейс
interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}