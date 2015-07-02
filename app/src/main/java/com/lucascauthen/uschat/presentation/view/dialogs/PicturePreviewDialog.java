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
import android.widget.ImageView;

import com.lucascauthen.uschat.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/2/15.
 */
public class PicturePreviewDialog {
    @InjectView(R.id.preview_image_view)ImageView imageView;
    private final Activity context;
    public PicturePreviewDialog(Activity context) {
        this.context = context;
    }

    public Dialog create(Bitmap image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_picture_preview, null);
        ButterKnife.inject(this, v);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.id.picture_preview_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.picture_preview_reject, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        this.imageView.setImageBitmap(image);
        return builder.create();
    }
}
