package ru.otus.kotlin.course.common.repo

import ru.otus.kotlin.course.common.models.PsImage

class RepoInitializer (
    val repo: IRepoInitializer,
    images: Collection<PsImage> = emptyList()
) : IRepoInitializer by repo {
    val initObjects = save(images).toList()
}