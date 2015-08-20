package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.presenters.PicturePreviewPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.presentation.view.base.PicturePreviewView;

import java.io.ByteArrayOutputStream;


public class PicturePreviewDialog extends Dialog implements PicturePreviewView{
    @InjectView(R.id.preview_image_view) ImageView imageView;
    @InjectView(R.id.picture_preview_accept) ImageButton accept;
    @InjectView(R.id.picture_preview_reject) ImageButton reject;

    private OnAcceptPictureListener onAccept;
    private OnRejectPictureListener onReject;
    private Bitmap image;
    private PicturePreviewPresenter presenter;

    public PicturePreviewDialog(Context context, byte[] theImage, PicturePreviewPresenter presenter) {
        super(context, R.style.DialogSlideAnimation);
        setContentView(R.layout.dialog_picture_preview);
        this.image = BitmapFactory.decodeByteArray(theImage, 0, theImage.length);
        ButterKnife.inject(this);

        accept.setOnClickListener(view -> {
            presenter.compress(() -> {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] data = stream.toByteArray();
                presenter.onCompressComplete(() -> {
                    if (onAccept != null) {
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
        imageView = ((ImageView) findViewById(R.id.preview_image_view));
        imageView.setImageBitmap(image);
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
        //TODO
    }

    @Override
    public void hideLoading() {
        //TODO
    }
}
