package com.lucascauthen.uschat.data.entities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;

import com.lucascauthen.uschat.R;

/**
 * Created by lhc on 6/10/15.
 */
public class Chat {
    private final String from;
    private final float duration;
    private float timeRemaining;
    private Bitmap image;
    private boolean isImageLoaded = false;

    public Chat(String fromName, float durationTime) {
        this.from = fromName;
        this.duration = durationTime;
        this.timeRemaining = this.duration;
    }

    public String getFrom() {
        return from;
    }

    public float getDuration() {
        return duration;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        isImageLoaded = true;
    }

    public Bitmap getImage() {
        if(image != null && isImageLoaded && timeRemaining > 0) {
            return this.image;
        }
        Log.d("Chat", "Error retrieving image... NOT LOADED" );
        return null;

    }
    public void dispose() {
        if(image != null) {
            image.recycle();
        }
    }
    public float tick(float tickAmount) {
        return (this.timeRemaining -= tickAmount);
    }
}
