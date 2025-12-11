package ru.otus.kotlin.course.app.spring.biz

import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.helpers.fail
import ru.otus.kotlin.course.common.helpers.validationErr
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsLabel
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.cor.ICorChainDsl
import ru.otus.kotlin.course.cor.worker

fun ICorChainDsl<PsBeContext>.validateId (title: String = "") = worker {
    this.title = title
    this.description = "Prepare PsBeContext"
    on { response.id == PsImageId.NONE && this.state == PsState.RUNNING  }
    handle {
        this.fail(
            validationErr(
                code = "incorrect",
                field = "id",
                message = "Id not set"
            )
        )
    }
}

fun ICorChainDsl<PsBeContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { response.title.isEmpty() && this.state == PsState.RUNNING}
    handle {
        this.fail(
            validationErr(
                code = "empty",
                field = "title",
                message = "Title must not be empty"
            )
        )
    }
}

fun ICorChainDsl<PsBeContext>.validateSearchStringNotEmpty(title: String) = worker {
    this.title = title
    on { filterString.isEmpty() && this.state == PsState.RUNNING}
    handle {
        this.fail(
            validationErr(
                code = "empty",
                field = "filterString",
                message = "Search String must not be empty"
            )
        )
    }
}

fun ICorChainDsl<PsBeContext>.endValidation(title: String) = worker {
    this.title = title
    on { this.state == PsState.RUNNING && errors.size > 0}
    handle {
        state = PsState.FAILING
    }
}