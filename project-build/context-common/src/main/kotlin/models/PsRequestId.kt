package ru.otus.kotlin.course.common.models

@JvmInline
value class PsRequestId (
    private val value: String
) {
  fun asString() = value

  companion object {
      val NONE = PsRequestId("")
  }

}