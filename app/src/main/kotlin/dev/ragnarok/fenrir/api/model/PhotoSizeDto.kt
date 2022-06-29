package dev.ragnarok.fenrir.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PhotoSizeDto {
    @SerialName(value = "url")
    var url: String? = null

    @SerialName(value = "src")
    var src: String? = null

    @SerialName("type")
    var type: String? = null

    @SerialName("width")
    var width = 0

    @SerialName("height")
    var height = 0

    object Type {
        /**
         * пропорциональная копия изображения с максимальной шириной 75px
         */
        const val S = "s"

        /**
         * пропорциональная копия изображения с максимальной шириной 130px
         */
        const val M = "m"

        /**
         * пропорциональная копия изображения с максимальной шириной 604px
         */
        const val X = "x"

        /**
         * пропорциональная копия изображения с максимальной стороной 807px
         */
        const val Y = "y"

        /**
         * пропорциональная копия изображения с максимальным размером 1280x1024
         */
        const val Z = "z"

        /**
         * пропорциональная копия изображения с максимальным размером 2560x2048px
         */
        const val W = "w"

        /**
         * если соотношение "ширина/высота" исходного изображения меньше или равно 3:2,
         * то пропорциональная копия с максимальной шириной 130px.
         * Если соотношение "ширина/высота" больше 3:2, то копия обрезанного слева изображения
         * с максимальной шириной 130px и соотношением сторон 3:2
         */
        const val O = "o"

        /**
         * если соотношение "ширина/высота" исходного изображения меньше или равно 3:2,
         * то пропорциональная копия с максимальной шириной 200px.
         * Если соотношение "ширина/высота" больше 3:2, то копия обрезанного слева и
         * справа изображения с максимальной шириной 200px и соотношением сторон 3:2
         */
        const val P = "p"

        /**
         * если соотношение "ширина/высота" исходного изображения меньше или равно 3:2,
         * то пропорциональная копия с максимальной шириной 320px.
         * Если соотношение "ширина/высота" больше 3:2, то копия обрезанного слева и
         * справа изображения с максимальной шириной 320px и соотношением сторон 3:2
         */
        const val Q = "q"

        /**
         * если соотношение "ширина/высота" исходного изображения меньше или равно 3:2,
         * то пропорциональная копия с максимальной шириной 510px.
         * Если соотношение "ширина/высота" больше 3:2, то копия обрезанного слева
         * и справа изображения с максимальной шириной 510px и соотношением сторон 3:2
         */
        const val R = "r"
    }

    companion object {
        fun create(type: String?, url: String?): PhotoSizeDto {
            val dto = PhotoSizeDto()
            dto.url = url
            dto.type = type
            return dto
        }
    }
}