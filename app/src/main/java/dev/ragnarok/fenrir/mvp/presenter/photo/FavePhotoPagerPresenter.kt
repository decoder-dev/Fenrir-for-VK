package dev.ragnarok.fenrir.mvp.presenter.photo

import android.content.Context
import android.os.Bundle
import dev.ragnarok.fenrir.model.AccessIdPair
import dev.ragnarok.fenrir.model.Photo
import dev.ragnarok.fenrir.util.RxUtils
import dev.ragnarok.fenrir.util.Utils

class FavePhotoPagerPresenter(
    photos: ArrayList<Photo>,
    index: Int,
    accountId: Int,
    context: Context,
    savedInstanceState: Bundle?
) : PhotoPagerPresenter(photos, accountId, false, context, savedInstanceState) {
    private val mUpdated: BooleanArray = BooleanArray(photos.size)
    private val refreshing: BooleanArray = BooleanArray(photos.size)
    override fun close() {
        view?.returnOnlyPos(currentIndex)
    }

    private fun refresh(index: Int) {
        if (mUpdated[index] || refreshing[index]) {
            return
        }
        refreshing[index] = true
        val photo = mPhotos[index]
        val accountId = accountId
        val forUpdate = listOf(AccessIdPair(photo.id, photo.ownerId, photo.accessKey))
        appendDisposable(photosInteractor.getPhotosByIds(accountId, forUpdate)
            .compose(RxUtils.applySingleIOToMainSchedulers())
            .subscribe({ photos: List<Photo> ->
                onPhotoUpdateReceived(
                    photos,
                    index
                )
            }) { t: Throwable -> onRefreshFailed(index, t) })
    }

    private fun onRefreshFailed(index: Int, t: Throwable) {
        refreshing[index] = false
        view?.let {
            showError(
                it,
                Utils.getCauseIfRuntime(t)
            )
        }
    }

    private fun onPhotoUpdateReceived(result: List<Photo>, index: Int) {
        refreshing[index] = false
        if (result.size == 1) {
            val p = result[0]
            mPhotos[index] = p
            mUpdated[index] = true
            if (currentIndex == index) {
                refreshInfoViews(true)
            }
        }
    }

    override fun afterPageChangedFromUi(oldPage: Int, newPage: Int) {
        super.afterPageChangedFromUi(oldPage, newPage)
        refresh(newPage)
    }

    init {
        currentIndex = index
        refresh(index)
    }
}