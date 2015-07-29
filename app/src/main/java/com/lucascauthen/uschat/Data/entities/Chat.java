package com.lucascauthen.uschat.data.entities;

import android.graphics.Bitmap;


import java.util.List;

/**
 * Created by lhc on 7/10/15.
 */
public class Chat {
    private final String from;
    private final List<String> to;
    private final String id;
    private final ChatType chatType;

    private final boolean isFromCurrentUser;

    private Bitmap image = null;
    private boolean isImageLoaded = false;

    public Bitmap getImage() {
        if(isImageLoaded) {
            return image;
        }
        throw new RuntimeException("Trying to get an image that is not loaded");
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setIsImageLoaded(boolean isImageLoaded) {
        this.isImageLoaded = isImageLoaded;
    }

    public boolean isLoadingImage() {
        return isLoadingImage;
    }

    public void setIsLoadingImage(boolean isLoadingImage) {
        this.isLoadingImage = isLoadingImage;
    }

    private boolean isLoadingImage = false;
    private final LoadImageFunction loadFunction;


    public List<String> getTo() {
        return to;
    }

    public String getId() {
        return id;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public boolean isFromCurrentUser() {
        return isFromCurrentUser;
    }

    public Chat(String from, List<String> to, String id, ChatType chatType, LoadImageFunction function) {
        this.from = from;
        this.to = to;
        this.id = id;
        this.chatType = chatType;
        this.loadFunction = function;
        if(from.equals(Person.getCurrentUser().getName())) {
            isFromCurrentUser = true;
        } else {
            isFromCurrentUser = false;
        }
    }

    public String getFrom() {
        return from;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    public void loadImage(ImageReadyCallback readyCallback, ProgressCallback progressCallback) {
        if(loadFunction != null) {
            loadFunction.loadImage(readyCallback, progressCallback);
        } else {
            throw new RuntimeException("Trying to load an image without a loading function");
        }
    }


    public enum ChatType {
        SENT_CHAT,
        RECEIVED_CHAT
    }

    @Override
    public boolean equals(Object o) {
        if(o.getClass() != Chat.class) {
            return false;
        }
        if(((Chat)o).getId().equals(this.getId())) {
            return true;
        }
        return false;
    }

    public interface ImageReadyCallback {
        void ready(Bitmap image);
    }
    public interface ProgressCallback {
        void getProgress(int progress);
    }
    public interface LoadImageFunction {
        void loadImage(ImageReadyCallback readyCallback, ProgressCallback progressCallback);
    }
}
