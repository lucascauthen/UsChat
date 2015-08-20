package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.view.dialogs.listeners.OnCloseListener;



public class ChatViewDialog extends Dialog {
    private Bitmap image;
    private NullOnAcceptListener NULL_LISTENER = new NullOnAcceptListener();
    private OnCloseListener onCloseListener = NULL_LISTENER;
    private ImageView imageView;
    private TextView timerText;
    private CountDownTimer timer;

    public ChatViewDialog(Context context, byte[] theImage, long duration) {
        super(context, R.style.DialogSlideStyle);
        this.image = BitmapFactory.decodeByteArray(theImage, 0, theImage.length);
        setContentView(R.layout.dialog_chat_viewer);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        findViewById(R.id.chat_close_view).setOnClickListener(view -> {
            close();
        });
        imageView = ((ImageView) findViewById(R.id.chat_image_view));
        imageView.setImageBitmap(image);

        timerText = ((TextView)findViewById(R.id.chat_timer_text));
        timerText.setText(String.valueOf((int)(duration / 1000)));

        timer = new CountDownTimer(duration, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.valueOf(((float)millisUntilFinished / 1000f)));
            }

            @Override
            public void onFinish() {
                close();
            }
        };
        /*
        this.setOnShowListener((theDialog) -> {
            timer.start();
        });
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        timer.start();
    }

    public void close() {
        ((ViewGroup) getWindow().getDecorView())
                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                getContext(), R.anim.slide_out_bottom));
        cancel();
        image = null;
        onCloseListener.close(this);
    }
    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }
    private static class NullOnAcceptListener implements OnCloseListener {

        @Override
        public void close(Dialog dialog) {

        }
    }
}
