package ru.otus.kotlin.course.common.repo

import ru.otus.kotlin.course.common.models.PsImage

interface IRepoInitializer {
    fun save(objects: Collection<PsImage>): Collection<PsImage>
}