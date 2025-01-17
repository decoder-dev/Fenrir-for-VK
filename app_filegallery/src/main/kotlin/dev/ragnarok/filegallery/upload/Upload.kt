package dev.ragnarok.filegallery.upload

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import dev.ragnarok.filegallery.readTypedObjectCompat
import dev.ragnarok.filegallery.util.ParcelUtils.readObjectLong
import dev.ragnarok.filegallery.util.ParcelUtils.writeObjectLong
import dev.ragnarok.filegallery.writeTypedObjectCompat
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

open class Upload : Parcelable {

    /* Идентификатор обьекта загрузки, генерируется базой данных при вставке */
    var id: Int
        private set

    /* Локальный путь к файлу */
    var fileUri: Uri? = null
        private set

    /* Идентификатор обьекта, к которому прикрепляется файл
       (локальный код сообщения, поста, комментария) */
    lateinit var destination: UploadDestination
        private set

    /* Размер изображения (только для изображений)*/
    var size = 0
        private set

    /* Текущий статус загрузки (QUEUE,UPLOADING,ERROR,CANCELLING)*/
    var status = 0

    /* Текущий прогресс загрузки */
    var progress = 0

    /* Текст ошибки, если она произошла */
    var errorText: String? = null

    /**
     * Дополнительные данные
     */
    var fileId: Long? = null
        private set
    var isAutoCommit = false
        private set

    constructor() {
        id = incrementedUploadId
    }

    internal constructor(parcel: Parcel) {
        id = parcel.readInt()
        fileUri = parcel.readTypedObjectCompat(Uri.CREATOR)
        destination = parcel.readTypedObjectCompat(UploadDestination.CREATOR)!!
        size = parcel.readInt()
        status = parcel.readInt()
        progress = parcel.readInt()
        errorText = parcel.readString()
        fileId = readObjectLong(parcel)
    }

    fun setAutoCommit(autoCommit: Boolean): Upload {
        isAutoCommit = autoCommit
        return this
    }

    fun setId(id: Int): Upload {
        this.id = id
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Upload
        return id == that.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeTypedObjectCompat(fileUri, i)
        parcel.writeTypedObjectCompat(destination, i)
        parcel.writeInt(size)
        parcel.writeInt(status)
        parcel.writeInt(progress)
        parcel.writeString(errorText)
        writeObjectLong(parcel, fileId)
    }

    fun setFileUri(fileUri: Uri?): Upload {
        this.fileUri = fileUri
        return this
    }

    fun setDestination(destination: UploadDestination): Upload {
        this.destination = destination
        return this
    }

    fun setSize(size: Int): Upload {
        this.size = size
        return this
    }

    fun setStatus(status: Int): Upload {
        this.status = status
        return this
    }

    fun setProgress(progress: Int): Upload {
        this.progress = progress
        return this
    }

    fun setErrorText(errorText: String?): Upload {
        this.errorText = errorText
        return this
    }

    fun setFileId(fileId: Long?): Upload {
        this.fileId = fileId
        return this
    }

    private val incrementedUploadId: Int
        get() = IDGEN.incrementAndGet()

    companion object {
        const val STATUS_QUEUE = 1
        const val STATUS_UPLOADING = 2
        const val STATUS_ERROR = 3
        const val STATUS_CANCELLING = 4

        @JvmField
        val CREATOR: Parcelable.Creator<Upload> = object : Parcelable.Creator<Upload> {
            override fun createFromParcel(parcel: Parcel): Upload {
                return Upload(parcel)
            }

            override fun newArray(size: Int): Array<Upload?> {
                return arrayOfNulls(size)
            }
        }
        val IDGEN = AtomicInteger(Random(System.nanoTime()).nextInt(5000))
    }
}