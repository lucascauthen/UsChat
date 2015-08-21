package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.ProgressBar;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.presenters.PicturePreviewPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.presentation.view.base.PicturePreviewView;

import java.io.ByteArrayOutputStream;


public class PicturePreviewDialog extends Dialog implements PicturePreviewView{
    @InjectView(R.id.imageView) ImageView imageView;
    @InjectView(R.id.acceptButton) ImageButton accept;
    @InjectView(R.id.rejectButton) ImageButton reject;
    @InjectView(R.id.progressBar) ProgressBar progressBar;

    private OnAcceptPictureListener onAccept;
    private OnRejectPictureListener onReject;
    private byte[] image;
    private PicturePreviewPresenter presenter;

    public PicturePreviewDialog(Context context, byte[] theImage, PicturePreviewPresenter presenter) {
        super(context, R.style.DialogSlideAnimation);
        setContentView(R.layout.dialog_picture_preview);
        this.image = theImage;
        this.presenter = presenter;
        ButterKnife.inject(this);

        accept.setOnClickListener(view -> {
            showLoading();
            presenter.compress(() -> {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //image.compress(Bitmap.CompressFormat.PNG, 50, stream);
                //byte[] data = stream.toByteArray();
                byte[] data = image;
                image = null; //Ensures that the byte array goes out of scope an is no londer stored in the class
                presenter.onCompressComplete(() -> {
                    if (onAccept != null) {
                        hideLoading();
                        onAccept.onAccept(data);
                    }
                    dismiss();
                    dispose();
                });
            });
        });
        reject.setOnClickListener(view -> {
            onReject.onReject();
            dismiss();
            dispose();
        });
        imageView = ((ImageView) findViewById(R.id.imageView));
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(theImage, 0, theImage.length));
        imageView.setOnClickListener(view -> {
            //TODO: Add text to the view
        });

    }

    public void setOnAcceptPictureListener(OnAcceptPictureListener onAccept) {
        this.onAccept = onAccept;
    }

    public void setOnRejectPictureListener(OnRejectPictureListener onReject) {
        this.onReject = onReject;
    }

    private void dispose() {
        this.image = null;
        presenter.detachView();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
