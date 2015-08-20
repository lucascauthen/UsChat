package com.lucascauthen.uschat.presentation.view.base;

import com.lucascauthen.uschat.data.entities.Chat;

public interface ChatReceivedView {
    void showLoading();

    void hideLoading();

    void showChat(Chat chat);
}
