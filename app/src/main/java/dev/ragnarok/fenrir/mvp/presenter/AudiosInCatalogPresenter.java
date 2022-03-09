package dev.ragnarok.fenrir.mvp.presenter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import dev.ragnarok.fenrir.domain.IAudioInteractor;
import dev.ragnarok.fenrir.domain.InteractorFactory;
import dev.ragnarok.fenrir.media.music.MusicPlaybackService;
import dev.ragnarok.fenrir.model.Audio;
import dev.ragnarok.fenrir.model.CatalogBlock;
import dev.ragnarok.fenrir.mvp.presenter.base.AccountDependencyPresenter;
import dev.ragnarok.fenrir.mvp.view.IAudiosInCatalogView;
import dev.ragnarok.fenrir.place.PlaceFactory;
import dev.ragnarok.fenrir.settings.Settings;
import dev.ragnarok.fenrir.util.RxUtils;
import dev.ragnarok.fenrir.util.Utils;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AudiosInCatalogPresenter extends AccountDependencyPresenter<IAudiosInCatalogView> {

    private final IAudioInteractor audioInteractor;
    private final ArrayList<Audio> audios;
    private final String block_id;
    private final CompositeDisposable audioListDisposable = new CompositeDisposable();
    private boolean actualReceived;
    private String next_from;
    private boolean loadingNow;
    private boolean endOfContent;
    private boolean doAudioLoadTabs;

    public AudiosInCatalogPresenter(int accountId, String block_id, @Nullable Bundle savedInstanceState) {
        super(accountId, savedInstanceState);
        audioInteractor = InteractorFactory.createAudioInteractor();
        audios = new ArrayList<>();
        this.block_id = block_id;
    }

    public void setLoadingNow(boolean loadingNow) {
        this.loadingNow = loadingNow;
        resolveRefreshingView();
    }

    @Override
    public void onGuiResumed() {
        super.onGuiResumed();
        resolveRefreshingView();
        if (doAudioLoadTabs) {
            return;
        } else {
            doAudioLoadTabs = true;
        }
        fireRefresh();
    }

    private void resolveRefreshingView() {
        callResumedView(v -> v.displayRefreshing(loadingNow));
    }

    public void requestList() {
        setLoadingNow(true);
        audioListDisposable.add(audioInteractor.getCatalogBlockById(getAccountId(), block_id, next_from)
                .compose(RxUtils.applySingleIOToMainSchedulers())
                .subscribe(this::onListReceived, this::onListGetError));
    }

    private void onListReceived(CatalogBlock data) {
        if (data == null || Utils.isEmpty(data.getAudios())) {
            actualReceived = true;
            setLoadingNow(false);
            endOfContent = true;
            return;
        }
        if (Utils.isEmpty(next_from)) {
            audios.clear();
        }
        next_from = data.getNext_from();
        endOfContent = Utils.isEmpty(next_from);
        actualReceived = true;
        setLoadingNow(false);
        audios.addAll(data.getAudios());
        callView(IAudiosInCatalogView::notifyListChanged);
    }

    public void playAudio(Context context, int position) {
        MusicPlaybackService.startForPlayList(context, audios, position, false);
        if (!Settings.get().other().isShow_mini_player())
            PlaceFactory.getPlayerPlace(getAccountId()).tryOpenWith(context);
    }

    @Override
    public void onDestroyed() {
        audioListDisposable.dispose();
        super.onDestroyed();
    }

    private void onListGetError(Throwable t) {
        setLoadingNow(false);
        callResumedView(v -> showError(v, Utils.getCauseIfRuntime(t)));
    }

    public int getAudioPos(Audio audio) {
        if (!Utils.isEmpty(audios) && audio != null) {
            int pos = 0;
            for (Audio i : audios) {
                if (i.getId() == audio.getId() && i.getOwnerId() == audio.getOwnerId()) {
                    i.setAnimationNow(true);
                    int finalPos = pos;
                    callView(v -> v.notifyItemChanged(finalPos));
                    return pos;
                }
                pos++;
            }
        }
        return -1;
    }

    public void fireRefresh() {
        audioListDisposable.clear();
        next_from = null;
        requestList();
    }

    public void fireScrollToEnd() {
        if (actualReceived && !endOfContent) {
            requestList();
        }
    }

    @Override
    public void onGuiCreated(@NonNull IAudiosInCatalogView view) {
        super.onGuiCreated(view);
        view.displayList(audios);
    }

}
