package ru.otus.kotlin.course.common.models

enum class PsCommand {
    NONE,
    CREATE,
    READ,
    UPDATE,
    DELETE,
    LINK,
    DOWNLOAD,
    SEARCH,
    TAGS,
    LABELS,
    INIT,       // For WS processing
    FINISHED,   // For WS processing
}