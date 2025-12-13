package ru.otus.kotlin.course.common.repo

interface IImageRepo  {
    abstract suspend fun createImage(req: DBImageRequest): IDBResult
    abstract suspend fun readImage(id: DBImageId, withData: Boolean = false): IDBResult
    abstract suspend fun updateImage(req: DBImageRequest): IDBResult
    abstract suspend fun deleteImage(id: DBImageId): IDBResult
    abstract suspend fun searchImages(criteria: DBImageSearchFilter): IDBResult

    companion object {
        val NONE = object: IImageRepo {
            override suspend fun createImage(req: DBImageRequest): IDBResult {
                TODO("Shouldn't be used")
            }

            override suspend fun readImage(id: DBImageId, withData: Boolean): IDBResult {
                TODO("Shouldn't be used")
            }

            override suspend fun updateImage(req: DBImageRequest): IDBResult {
                TODO("Shouldn't be used")
            }

            override suspend fun deleteImage(id: DBImageId): IDBResult {
                TODO("Shouldn't be used")
            }

            override suspend fun searchImages(criteria: DBImageSearchFilter): IDBResult {
                TODO("Shouldn't be used")
            }

        }
    }
}