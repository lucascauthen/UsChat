package com.lucascauthen.uschat.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.lucascauthen.uschat.AndroidApplication;
import com.lucascauthen.uschat.data.repository.chat.CachedChatRepo;
import com.lucascauthen.uschat.data.repository.chat.ChatCache;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.data.repository.chat.MultiLevelChatRepo;
import com.lucascauthen.uschat.data.repository.chat.RemoteChatRepo;
import com.lucascauthen.uschat.data.repository.user.CachedPersonRepo;
import com.lucascauthen.uschat.data.repository.user.MultiLevelPersonRepo;
import com.lucascauthen.uschat.data.repository.user.PersonCache;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.data.repository.user.RemotePersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.domain.scheduler.BackgroundScheduler;
import com.lucascauthen.uschat.domain.scheduler.ForegroundScheduler;
import com.lucascauthen.uschat.presentation.presenters.CameraPresenter;
import com.lucascauthen.uschat.presentation.presenters.ChatListPresenter;
import com.lucascauthen.uschat.presentation.presenters.ChatReceivedPresenter;
import com.lucascauthen.uschat.presentation.presenters.ChatSentPresenter;
import com.lucascauthen.uschat.presentation.presenters.FriendListPresenter;
import com.lucascauthen.uschat.presentation.presenters.FriendRequestPresenter;
import com.lucascauthen.uschat.presentation.presenters.FriendSearchPresenter;
import com.lucascauthen.uschat.presentation.presenters.FriendSelectPresenter;
import com.lucascauthen.uschat.presentation.presenters.LoginPresenter;
import com.lucascauthen.uschat.presentation.presenters.PersonListPresenter;
import com.lucascauthen.uschat.presentation.presenters.PicturePreviewPresenter;
import com.lucascauthen.uschat.presentation.presenters.SignUpPresenter;
import com.lucascauthen.uschat.presentation.view.fragments.CameraFragment;
import com.lucascauthen.uschat.presentation.view.fragments.ChatReceivedFragment;
import com.lucascauthen.uschat.presentation.view.fragments.ChatSentFragment;
import com.lucascauthen.uschat.presentation.view.fragments.ChatTabFragment;
import com.lucascauthen.uschat.presentation.view.fragments.FriendListFragment;
import com.lucascauthen.uschat.presentation.view.fragments.FriendRequestFragment;
import com.lucascauthen.uschat.presentation.view.fragments.FriendSearchFragment;
import com.lucascauthen.uschat.presentation.view.fragments.FriendTabFragment;
import com.lucascauthen.uschat.util.ActivityNavigator;
import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    public Activity provideActivityContext(Activity activity) {
        return activity;
    }

    @Provides
    @Singleton
    ActivityNavigator provideActivityNavigator() {
        return new ActivityNavigator();
    }

    //Threading support//
    @Provides
    @Singleton
    BackgroundExecutor provideBackgroundExecutor() {
        return new BackgroundExecutor();
    }

    @Provides
    @Singleton
    ForegroundExecutor provideForegroundExecutor() {
        return new ForegroundExecutor();
    }

    @Provides
    @Singleton
    BackgroundScheduler provideBackgroundScheduler() {
        return new BackgroundScheduler() {
            @Override
            public Scheduler getScheduler() {
                return Schedulers.io();
            }
        };
    }

    @Provides
    @Singleton
    ForegroundScheduler provideForegroundScheduler() {
        return new ForegroundScheduler() {
            @Override
            public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        };
    }
    //////////


    //Presenters//
    @Provides
    SignUpPresenter provideSignUpViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        return new SignUpPresenter(backgroundExecutor, foregroundExecutor);
    }

    @Provides
    LoginPresenter provideLoginPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        return new LoginPresenter(backgroundExecutor, foregroundExecutor);
    }

    @Provides
    ChatListPresenter provideChatListViewPresenter(BackgroundExecutor foregroundExecutor, ForegroundExecutor backgroundExecutor, @Named("MainChatRepo") ChatRepo repo) {
        return new ChatListPresenter(foregroundExecutor, backgroundExecutor, repo);
    }

    @Provides
    PersonListPresenter providePersonListViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, MultiLevelPersonRepo repo) {
        return new PersonListPresenter(backgroundExecutor, foregroundExecutor, repo);
    }

    @Provides
    CameraPresenter provideCameraViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        return new CameraPresenter(backgroundExecutor, foregroundExecutor);
    }

    @Provides
    FriendListPresenter provideFriendsListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, PersonListPresenter subPresenter, @Named("MainPersonRepo") PersonRepo repo) {
        return new FriendListPresenter(backgroundExecutor, foregroundExecutor, subPresenter, repo);
    }

    @Provides
    FriendRequestPresenter provideRequestListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, PersonListPresenter subPresenter, @Named("MainPersonRepo") PersonRepo repo) {
        return new FriendRequestPresenter(backgroundExecutor, foregroundExecutor, subPresenter, repo);
    }

    @Provides
    FriendSearchPresenter provideFriendFinderPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, PersonListPresenter subPresenter, @Named("MainPersonRepo") PersonRepo repo) {
        return new FriendSearchPresenter(backgroundExecutor, foregroundExecutor, subPresenter, repo);
    }

    @Provides
    ChatReceivedPresenter provideChatReceivedPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ChatListPresenter subPresenter, @Named("MainChatRepo") ChatRepo repo) {
        return new ChatReceivedPresenter(backgroundExecutor, foregroundExecutor, subPresenter, repo);
    }

    @Provides
    ChatSentPresenter provideChatSentPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ChatListPresenter subPresenter) {
        return new ChatSentPresenter(backgroundExecutor, foregroundExecutor, subPresenter);
    }

    @Provides
    FriendSelectPresenter provideSelectFriendsDialogPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, PersonListPresenter subPresenter, @Named("MainChatRepo") ChatRepo repo) {
        return new FriendSelectPresenter(backgroundExecutor, foregroundExecutor, subPresenter, repo);
    }

    @Provides
    PicturePreviewPresenter providePicturePreviewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        return new PicturePreviewPresenter(backgroundExecutor, foregroundExecutor);
    }
    //////////


    //Repositories//
    @Provides
    @Singleton
    @Named("MainPersonRepo")
    PersonRepo provideUser(PersonCache cache, @Named("SecondaryPersonRepo") PersonRepo secondaryRepo) {
        return new MultiLevelPersonRepo(cache, secondaryRepo);
    }

    @Provides
    @Singleton
    PersonCache providePersonCache() {
        return new CachedPersonRepo();
    }

    @Provides
    @Singleton
    @Named("SecondaryPersonRepo")
    PersonRepo provideSecondaryPersonRepo() {
        return new RemotePersonRepo();
    }

    @Provides
    @Singleton
    @Named("MainChatRepo")
    ChatRepo provideMainChatRepo(ChatCache chatCache, @Named("SecondaryChatRepo") ChatRepo secondaryRepo) {
        return new MultiLevelChatRepo(chatCache, secondaryRepo);
    }

    @Provides
    @Singleton
    ChatCache provideChatCache() {
        return new CachedChatRepo();
    }

    @Provides
    @Singleton
    @Named("SecondaryChatRepo")
    ChatRepo provideSecondaryChatRepo() {
        return new RemoteChatRepo();
    }
    //////////


    //Fragments//
    @Provides
    CameraFragment provideCameraFragment(CameraPresenter mainPresenter, PicturePreviewPresenter previewPresenter, FriendSelectPresenter selectPresenter) {
        return CameraFragment.newInstance(mainPresenter, previewPresenter, selectPresenter);
    }

    @Provides
    FriendTabFragment provideFriendTabFragment(@Named("FriendList") Fragment friendList, @Named("FriendRequests") Fragment requestList, @Named("FriendSearch") Fragment personSearch) {
        return FriendTabFragment.newInstance(friendList, requestList, personSearch);
    }

    @Provides
    @Named("FriendSearch")
    Fragment provideSearchFriendsFragment(FriendSearchPresenter presenter) {
        return FriendSearchFragment.newInstance(presenter);
    }

    @Provides
    @Named("FriendList")
    Fragment provideFriendsListFragment(FriendListPresenter presenter) {
        return FriendListFragment.newInstance(presenter);
    }

    @Provides
    @Named("FriendRequests")
    Fragment provideFriendRequestsFragment(FriendRequestPresenter presenter) {
        return FriendRequestFragment.newInstance(presenter);
    }

    @Provides
    ChatTabFragment provideChatTabFragment(@Named("ChatSent") Fragment sentChats, @Named("ChatReceived") Fragment receivedChats, ActivityNavigator navigator) {
        return ChatTabFragment.newInstance(sentChats, receivedChats, navigator);
    }

    @Provides
    @Named("ChatSent")
    Fragment provideSentChatFragment(ChatSentPresenter presenter) {
        return ChatSentFragment.newInstance(presenter);
    }

    @Provides
    @Named("ChatReceived")
    Fragment provideReceivedChatFragment(ChatReceivedPresenter presenter) {
        return ChatReceivedFragment.newInstance(presenter);
    }
    //////////

}
