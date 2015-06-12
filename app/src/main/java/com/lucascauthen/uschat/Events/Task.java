package com.lucascauthen.uschat.Events;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by lhc on 6/12/15.
 */
public class Task extends AsyncTask<Object, Integer, Boolean>{
    private final TaskListener taskListener;
    private final Runnable runnable;
    public Task(@Nullable TaskListener listener, @NonNull Runnable runnable) {
        this.taskListener = listener;
        this.runnable = runnable;
    }
    @Override
    protected Boolean doInBackground(Object... params) {
        runnable.run();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(taskListener != null) {
            taskListener.finished(result);
        }
        super.onPostExecute(result);
    }
}
