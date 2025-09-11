package ru.otus.kotlin.course.common.models

data class PsLabel(
    val key: String = "",
    val desc: String = "",
    val value: String = ""
)

data class PsImage (
    var id: PsImageId = PsImageId.NONE,
    var title: String = "",
    var desc: String = "",
    val tags: MutableList<String> = mutableListOf(),
    val labels: MutableList<PsLabel> = mutableListOf(),

    var uploadUrl: String = "",
    var imageUrl: String = "",
    var previewUrl: String = "",
    var permanentLinkUrl: String = "",

) {

    // TODO: Обработка загрузки файла
    var file: ByteArray = ByteArray(0)

    fun isEmpty() = this == NONE
    companion object {
        private val NONE = PsImage()
        fun labelBuilder(): List<PsLabel> {
            val lst = buildList<PsLabel> {
                add(PsLabel("author", "Автор"))
                add(PsLabel("format", "Формат изображения"))
            }
            return lst
        }
    }
}