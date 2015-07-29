package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.view.dialogs.listeners.OnAcceptListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/2/15.
 */
public class PicturePreviewDialog extends Dialog {
    private Bitmap image;
    private NullOnAcceptListener NULL_LISTENER = new NullOnAcceptListener();
    private OnAcceptListener onAcceptListener = NULL_LISTENER;
    private ImageView imageView;

    public PicturePreviewDialog(Context context, Bitmap theImage) {
        super(context, R.style.DialogSlideStyle);
        this.image = theImage;
        setContentView(R.layout.dialog_picture_preview);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        findViewById(R.id.picture_preview_accept).setOnClickListener(view -> {
            getWindow().getAttributes().windowAnimations = R.style.DialogNoAnimation;
            onAcceptListener.accept(this);
        });
        findViewById(R.id.picture_preview_reject).setOnClickListener(view -> {
            cancel();
        });
        imageView = ((ImageView) findViewById(R.id.preview_image_view));
        imageView.setImageBitmap(image);
        imageView.setOnClickListener(view -> {
            //TODO: Add text to the view
        });

    }

    public void setOnAcceptListener(OnAcceptListener onAcceptListener) {
        this.onAcceptListener = onAcceptListener;
    }

    private static class NullOnAcceptListener implements OnAcceptListener {

        @Override
        public void accept(Dialog dialog) {
            dialog.cancel();
        }
    }
}
