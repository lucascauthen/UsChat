package com.lucascauthen.uschat.data.entities;

import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 7/10/15.
 */
public class Chat {
    private final List<Person> to;
    private final ChatType chatType;
    private final LoadImageFunction loadFunction;
    private final String from;

    private boolean isImageLoaded = false;
    private boolean isLoadingImage = false;
    private String id = "null";
    private boolean isIdSet = false;

    private byte[] chatData;

    public Chat(List<Person> to, String from, ChatType chatType, LoadImageFunction function) {
        this.to = to;
        this.from = from;
        this.chatType = chatType;
        this.loadFunction = function;
    }

    public byte[] getImage() {
        if (isImageLoaded) {
            isImageLoaded = false;
            //This ensures that the managing of the bitmap is now put on the requester of the image
            //When this function looses scope, reference will no longer exist meaning that this object no longer has a reference to the image
            byte[] reference = chatData;
            chatData = null;
            return reference;
        }
        return null;
    }

    public void setImage(byte[] image) {
        this.chatData = image;
    }

    public void setId(String id) {
        this.id = id;
        this.isIdSet = true;
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

    public List<Person> getTo() {
        return to;
    }

    public List<String> getToString() {
        List<String> names = new ArrayList<>();
        for(Person person : this.to) {
            names.add(person.name());
        }
        return names;
    }

    public String getId() {
        return id;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public boolean isFromCurrentUser() {
        return chatType == ChatType.SENT;
    }

    public String getFrom() {
        return from;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    public void setChatData(byte[] chatData) {
        this.chatData = chatData;
        isImageLoaded = true;
    }


    public void loadImage(ImageReadyCallback readyCallback, ProgressCallback progressCallback) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("Trying to load an image on the UI thread");
        } else {
            if (loadFunction != null) {
                loadFunction.loadImage(readyCallback, progressCallback);
            } else {
                throw new RuntimeException("Trying to load an image without a loading function");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Chat.class) {
            return false;
        }
        if(((Chat)o).isIdSet) {
            if (((Chat) o).getId().equals(this.getId())) {
                return true;
            }
        } else {
            return super.equals(o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if(isIdSet) {
            return getId().hashCode();
        } else {
            return super.hashCode();
        }
    }

    public enum ChatType {
        SENT,
        RECEIVED,
        UNKNOWN
    }

    public interface ImageReadyCallback {
        void ready(byte[] image);
    }

    public interface ProgressCallback {
        void getProgress(int progress);
    }

    public interface LoadImageFunction {
        void loadImage(ImageReadyCallback readyCallback, ProgressCallback progressCallback);
    }
}
