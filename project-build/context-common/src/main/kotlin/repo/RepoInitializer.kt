package ru.otus.kotlin.course.common.repo

import ru.otus.kotlin.course.common.models.PsImage

abstract class RepoInitializer (
    images: Collection<PsImage> = emptyList()
) {
    abstract fun save(objects: Collection<PsImage>): Collection<PsImage>
    val initObjects = save(images).toList()
}