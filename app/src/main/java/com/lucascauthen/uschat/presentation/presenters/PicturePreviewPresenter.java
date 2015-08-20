package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.PicturePreviewView;
import com.lucascauthen.uschat.util.NullObject;

public class PicturePreviewPresenter implements BasePresenter<PicturePreviewView> {

    private static final PicturePreviewView NULL_VIEW = NullObject.create(PicturePreviewView.class);

    private PicturePreviewView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;

    public PicturePreviewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    @Override
    public void attachView(PicturePreviewView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    public void compress(Runnable compressFunction) {
        backgroundExecutor.execute(compressFunction);
    }

    public void onCompressComplete(Runnable completeFunction) {
        foregroundExecutor.execute(completeFunction);
    }
}
