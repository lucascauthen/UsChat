package com.lucascauthen.uschat.presentation.controller.implmentations;

/**
 * Created by lhc on 8/4/15.
 */
public class FriendFinderPresenter {
    /*
            searchTextObservable
                .map(value -> {
                    foregroundExecutor.execute(() -> view.showLoading());
                    return value;
                })
                .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .map(event -> event.text().toString())
                .filter(text -> {
                    if (text.equals("")) {
                        personSearchPresenter.updatePersonSet((List<User>)null); //Cast required otherwise it is ambiguous
                        foregroundExecutor.execute(() -> view.hideLoading());
                        return false;

                    }
                    return true;
                })
                .observeOn(backgroundScheduler.getScheduler())
                .subscribe(query -> {
                    personSearchPresenter.updatePersonSet(new Repo.Request(true, query));
                    foregroundExecutor.execute(() -> {
                        view.hideLoading();
                    });
                });






     */
}
