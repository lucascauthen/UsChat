package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseCameraViewPresenter;
import com.lucascauthen.uschat.presentation.view.dialogs.listeners.OnAcceptListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/2/15.
 */
public class PicturePreviewDialog extends Dialog implements BaseCameraViewPresenter.PicturePreview{
    private Bitmap image;
    private BaseCameraViewPresenter.PicturePreview.OnAcceptPicture onAccept;
    private BaseCameraViewPresenter.PicturePreview.OnRejectPicture onReject;

    @InjectView(R.id.preview_image_view) ImageView imageView;
    @InjectView(R.id.picture_preview_accept) ImageButton accept;
    @InjectView(R.id.picture_preview_reject) ImageButton reject;

    public PicturePreviewDialog(Context context, Bitmap theImage) {
        super(context, R.style.DialogSlideAnimation);
        setContentView(R.layout.dialog_picture_preview);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.image = theImage;
        ButterKnife.inject(this);


        accept.setOnClickListener(view -> {
            onAccept.accept(image);
        });
        reject.setOnClickListener(view -> {
            this.dismiss();
        });
        imageView = ((ImageView) findViewById(R.id.preview_image_view));
        imageView.setImageBitmap(image);
        imageView.setOnClickListener(view -> {
            //TODO: Add text to the view
        });

    }

    @Override
    public void setOnAcceptPictureListener(OnAcceptPicture onAccept) {
        this.onAccept = onAccept;
    }

    @Override
    public void setOnRejectPictureListener(OnRejectPicture onReject) {
        this.onReject = onReject;
    }
}
