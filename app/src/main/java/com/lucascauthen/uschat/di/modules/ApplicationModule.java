package com.lucascauthen.uschat.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.lucascauthen.uschat.AndroidApplication;
import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.data.repository.chat.CachedChatRepo;
import com.lucascauthen.uschat.data.repository.chat.ChatCache;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.data.repository.chat.MultiLevelChatRepo;
import com.lucascauthen.uschat.data.repository.chat.RemoteChatRepo;
import com.lucascauthen.uschat.data.repository.user.CachedPersonRepo;
import com.lucascauthen.uschat.data.repository.user.PersonCache;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.data.repository.user.RemotePersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.domain.scheduler.BackgroundScheduler;
import com.lucascauthen.uschat.domain.scheduler.ForegroundScheduler;
import com.lucascauthen.uschat.presentation.controller.base.BaseCameraViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatReceivedPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatSentPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatTabViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendSearchPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendRequestPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendsListPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePeopleTabViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseLoginViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseSignUpViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.CameraViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.ChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.ChatReceivedPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.ChatSentPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.ChatTabViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.FriendSearchPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.FriendListPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.FriendRequestPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.PeopleTabViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.LoginViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.PagerViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.PersonListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.SignUpViewPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.PersonViewAdapter;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.ChatReceivedFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.ChatSentFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.ChatTabFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.FriendRequestsFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.FriendSearchFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.FriendListFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.FriendTabFragment;
import com.lucascauthen.uschat.util.ActivityNavigator;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.ChatViewAdapter;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.CameraFragment;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lhc on 7/29/15.
 */
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
        return new ForegroundExecutor(application);
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
    BaseSignUpViewPresenter provideSignUpViewPresenter() {
        return new SignUpViewPresenter();
    }

    @Provides
    BaseLoginViewPresenter provideLoginPresenter(ForegroundScheduler foregroundScheduler) {
        return new LoginViewPresenter(foregroundScheduler.getScheduler());
    }

    @Provides
    BasePagerViewPresenter providePagerPresenter() {
        return new PagerViewPresenter();
    }

    @Provides
    BaseChatListViewPresenter provideChatListViewPresenter(BackgroundExecutor foregroundExecutor, ForegroundExecutor backgroundExecutor, @Named("MainChatRepo") ChatRepo repo) {
        return new ChatListViewPresenter(foregroundExecutor, backgroundExecutor, repo);
    }
    @Provides
    BasePersonListViewPresenter providePersonListViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, User user) {
        return new PersonListViewPresenter(foregroundExecutor, backgroundExecutor, user);
    }
    @Provides
    BasePeopleTabViewPresenter provideTabViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BackgroundScheduler backgroundScheduler) {
        return new PeopleTabViewPresenter(backgroundExecutor, foregroundExecutor, backgroundScheduler);
    }
    @Provides
    BaseCameraViewPresenter provideCameraViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        return new CameraViewPresenter(backgroundExecutor, foregroundExecutor);
    }
    @Provides
    BaseFriendsListPresenter provideFriendsListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BasePersonListViewPresenter subPresenter) {
        return new FriendListPresenter(backgroundExecutor, foregroundExecutor, subPresenter);
    }
    @Provides
    BaseFriendRequestPresenter provideRequestListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BasePersonListViewPresenter subPresenter) {
        return new FriendRequestPresenter(backgroundExecutor, foregroundExecutor, subPresenter);
    }
    @Provides
    BaseFriendSearchPresenter provideFriendFinderPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BackgroundScheduler backgroundScheduler, BasePersonListViewPresenter subPresenter) {
        return new FriendSearchPresenter(backgroundExecutor, foregroundExecutor, backgroundScheduler, subPresenter);
    }
    @Provides
    BaseChatTabViewPresenter provideChatTabViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        return new ChatTabViewPresenter(backgroundExecutor, foregroundExecutor);
    }
    @Provides
    BaseChatReceivedPresenter provideChatReceivedPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BaseChatListViewPresenter subPresenter) {
        return new ChatReceivedPresenter(backgroundExecutor, foregroundExecutor, subPresenter);
    }
    @Provides
    BaseChatSentPresenter provideChatSentPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BaseChatListViewPresenter subPresenter) {
        return new ChatSentPresenter(backgroundExecutor, foregroundExecutor, subPresenter);
    }
    //////////



    //Repositories//
    @Provides
    @Singleton
    User provideUser(PersonCache cache, PersonRepo secondaryRepo) {
        return new User(cache, secondaryRepo);
    }
    @Provides
    @Singleton
    PersonCache providePersonCache() {
        return new CachedPersonRepo();
    }

    @Provides
    @Singleton
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
    @Singleton
    CameraFragment provideCameraFragment(BaseCameraViewPresenter mainPresenter, BasePersonListViewPresenter subPresenter, PersonViewAdapter adapter) {
        return CameraFragment.newInstance(mainPresenter, subPresenter, adapter);
    }

    @Provides
    FriendTabFragment provideFriendTabFragment(BasePeopleTabViewPresenter presenter, @Named("FriendList") Fragment friendList, @Named("FriendRequests") Fragment requestList, @Named("FriendSearch") Fragment personSearch) {
        return FriendTabFragment.newInstance(presenter, friendList, requestList, personSearch);
    }

    @Provides
    @Named("FriendSearch")
    Fragment provideSearchFriendsFragment(BaseFriendSearchPresenter presenter, PersonViewAdapter adapter) {
        return FriendSearchFragment.newInstance(presenter, adapter);
    }
    @Provides
    @Named("FriendList")
    Fragment provideFriendsListFragment(BaseFriendsListPresenter presenter, PersonViewAdapter adapter) {
        return FriendListFragment.newInstance(presenter, adapter);
    }
    @Provides
    @Named("FriendRequests")
    Fragment provideFriendRequestsFragment(BaseFriendRequestPresenter presenter, PersonViewAdapter adapter) {
        return FriendRequestsFragment.newInstance(presenter, adapter);
    }

    @Provides
    ChatTabFragment provideChatTabFragment(BaseChatTabViewPresenter presenter, @Named("ChatSent") Fragment sentChats, @Named("ChatReceived") Fragment receivedChats, ActivityNavigator navigator) {
        return ChatTabFragment.newInstance(presenter, sentChats, receivedChats, navigator);
    }

    @Provides
    @Named("ChatSent")
    Fragment provideSentChatFragment(BaseChatSentPresenter presenter, ChatViewAdapter adapter) {
        return ChatSentFragment.newInstance(presenter, adapter);
    }
    @Provides
    @Named("ChatReceived")
    Fragment provideReceivedChatFragment(BaseChatReceivedPresenter presenter, ChatViewAdapter adapter) {
        return ChatReceivedFragment.newInstance(presenter, adapter);
    }
    //////////



    //Adapters//
    @Provides
    ChatViewAdapter provideChatViewAdapter() {
        return new ChatViewAdapter();
    }

    @Provides
    PersonViewAdapter providePersonViewAdapter(ForegroundExecutor foregroundExecutor) {
        return new PersonViewAdapter(foregroundExecutor);
    }
    //////////



}
