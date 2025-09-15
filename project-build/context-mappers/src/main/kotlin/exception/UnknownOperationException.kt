package ru.otus.kotlin.course.mappers.exception

class UnknownOperationException (value: String) : IllegalArgumentException("Wrong operation ${value}")