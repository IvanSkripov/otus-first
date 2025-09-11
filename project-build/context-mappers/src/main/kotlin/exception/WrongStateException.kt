package ru.otus.kotlin.course.mappers.exception

class WrongStateException (value: String) : IllegalStateException("Wrong state ${value}")