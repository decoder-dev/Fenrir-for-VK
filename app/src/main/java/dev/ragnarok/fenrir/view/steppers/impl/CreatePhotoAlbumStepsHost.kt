package dev.ragnarok.fenrir.view.steppers.impl

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import dev.ragnarok.fenrir.R
import dev.ragnarok.fenrir.model.Privacy
import dev.ragnarok.fenrir.view.steppers.base.AbsStepsHost
import dev.ragnarok.fenrir.view.steppers.impl.CreatePhotoAlbumStepsHost.PhotoAlbumState

class CreatePhotoAlbumStepsHost : AbsStepsHost<PhotoAlbumState>(PhotoAlbumState()) {
    var isAdditionalOptionsEnable = false
    var isPrivacySettingsEnable = false
        private set
    override val stepsCount: Int
        get() = 4

    override fun getStepTitle(index: Int): Int {
        return when (index) {
            STEP_TITLE_AND_DESCRIPTION -> R.string.enter_main_album_info
            STEP_UPLOAD_AND_COMMENTS -> R.string.additional_settings
            STEP_PRIVACY_VIEW -> R.string.privacy_view
            STEP_PRIVACY_COMMENT -> R.string.privacy_comment
            else -> throw IllegalStateException("Invalid step index")
        }
    }

    override fun canMoveNext(index: Int, state: PhotoAlbumState): Boolean {
        return when (index) {
            STEP_TITLE_AND_DESCRIPTION -> !TextUtils.isEmpty(state.title) && (state.title?.trim { it <= ' ' }?.length
                ?: 0) > 1
            STEP_UPLOAD_AND_COMMENTS, STEP_PRIVACY_VIEW, STEP_PRIVACY_COMMENT -> true
            else -> throw IllegalStateException("Invalid step index, index: $index")
        }
    }

    fun setPrivacySettingsEnable(privacySettingsEnable: Boolean): CreatePhotoAlbumStepsHost {
        isPrivacySettingsEnable = privacySettingsEnable
        return this
    }

    override fun getNextButtonText(index: Int): Int {
        return if (index == stepsCount - 1) R.string.finish else R.string.button_continue
    }

    override fun getCancelButtonText(index: Int): Int {
        return if (index == 0) R.string.button_cancel else R.string.button_back
    }

    class PhotoAlbumState : AbsState {
        var title: String? = null
            private set
        var description: String? = null
            private set
        var privacyView: Privacy?
            private set
        var privacyComment: Privacy?
            private set
        var isUploadByAdminsOnly = false
            private set
        var isCommentsDisabled = false
            private set

        constructor(p: Parcel) : super() {
            title = p.readString()
            description = p.readString()
            privacyView = p.readParcelable(Privacy::class.java.classLoader)
            privacyComment = p.readParcelable(Privacy::class.java.classLoader)
            isUploadByAdminsOnly = p.readByte().toInt() != 0
            isCommentsDisabled = p.readByte().toInt() != 0
        }

        constructor() {
            privacyView = Privacy()
            privacyComment = Privacy()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(title)
            dest.writeString(description)
            dest.writeParcelable(privacyView, flags)
            dest.writeParcelable(privacyComment, flags)
            dest.writeByte((if (isUploadByAdminsOnly) 1 else 0).toByte())
            dest.writeByte((if (isCommentsDisabled) 1 else 0).toByte())
        }

        override fun describeContents(): Int {
            return 0
        }

        fun setTitle(title: String?): PhotoAlbumState {
            this.title = title
            return this
        }

        fun setDescription(description: String?): PhotoAlbumState {
            this.description = description
            return this
        }

        fun setPrivacyView(privacyView: Privacy): PhotoAlbumState {
            this.privacyView = privacyView
            return this
        }

        fun setPrivacyComment(privacyComment: Privacy): PhotoAlbumState {
            this.privacyComment = privacyComment
            return this
        }

        fun setUploadByAdminsOnly(uploadByAdminsOnly: Boolean): PhotoAlbumState {
            isUploadByAdminsOnly = uploadByAdminsOnly
            return this
        }

        fun setCommentsDisabled(commentsDisabled: Boolean): PhotoAlbumState {
            isCommentsDisabled = commentsDisabled
            return this
        }

        override fun toString(): String {
            return "PhotoAlbumState{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", privacyView=" + privacyView +
                    ", privacyComment=" + privacyComment +
                    ", uploadByAdminsOnly=" + isUploadByAdminsOnly +
                    ", commentsDisabled=" + isCommentsDisabled +
                    '}'
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<PhotoAlbumState> =
                object : Parcelable.Creator<PhotoAlbumState> {
                    override fun createFromParcel(p: Parcel): PhotoAlbumState {
                        return PhotoAlbumState(p)
                    }

                    override fun newArray(size: Int): Array<PhotoAlbumState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    companion object {
        const val STEP_TITLE_AND_DESCRIPTION = 0
        const val STEP_UPLOAD_AND_COMMENTS = 1
        const val STEP_PRIVACY_VIEW = 2
        const val STEP_PRIVACY_COMMENT = 3
    }
}