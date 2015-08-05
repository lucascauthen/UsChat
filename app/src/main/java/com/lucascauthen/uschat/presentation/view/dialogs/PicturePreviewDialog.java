package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.view.dialogs.listeners.OnAcceptListener;

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
