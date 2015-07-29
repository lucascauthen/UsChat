package com.lucascauthen.uschat.domain;

import com.lucascauthen.uschat.data.entities.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lhc on 7/15/15.
 */

//No checks for null pointer exceptions

    @Deprecated
public class ListenerListRepository<T> {
    private static final NullListAddListener NULL_ADD_LISTENER = new NullListAddListener();
    private static final NullListRemoveListener NULL_REMOVE_LISTENER = new NullListRemoveListener();


    private List<T> activeList = new CopyOnWriteArrayList<>();
    private List<T> updatedList = new ArrayList<>();

    private boolean isStale = false;

    private ListAddListener addListener = NULL_ADD_LISTENER;
    private ListRemoveListener removeListener = NULL_REMOVE_LISTENER;

    public ListenerListRepository() {

    }
    public ListenerListRepository(List<T> list) {
        this.activeList = new CopyOnWriteArrayList<>(list);
    }
    public void setAddListener(ListAddListener addListener) {
        this.addListener = addListener;
    }

    public void setRemoveListener(ListRemoveListener removeListener) {
        this.removeListener = removeListener;
    }
    public void removeAddListener() {
        this.addListener = NULL_ADD_LISTENER;
    }

    public void removeRemoveListener() {
        this.removeListener = NULL_REMOVE_LISTENER;
    }
    public void add(T item) {
        if(!isStale) {
            activeList.add(item);
            addListener.onAdd(item, activeList.indexOf(item));
        } else {
            updatedList.add(item);
        }
    }

    public synchronized void remove(T item) {
        int pos = activeList.indexOf(item);
        activeList.remove(item);
        removeListener.onRemove(item, pos);
    }

    public T get(int index) {
        return activeList.get(index);
    }

    public int size() {
        return activeList.size();
    }

    public synchronized void clear() {
        for(T item : activeList) {
            this.remove(item);
        }
    }

    public void beforePurge() {
        if(activeList.size() > 0) { //If there are no items in the active list, none of the items can be stale
            isStale = true;
        }
    }

    public void afterPurge() {
        if(isStale) { //If the list is not stale, no need to update it
            isStale = false;
            if (updatedList.size() > 0) {
                deleteStale();
                addNew();
                updatedList.clear();
            } else {
                clear();
            }
        }
    }
    public void deleteStale() { //Deletes all the items in activeList that are not also in updatedList
        for(T oldItem : activeList) {
            boolean isStale = true;
            for(T newItem : updatedList) {
                if(oldItem.equals(newItem)) {
                    isStale = false;
                    break;
                }
            }
            if(isStale) {
                remove(oldItem);
            }
        }
    }
    public void addNew() { //Adds all items from updatedList that are not already contained in activeList
        if(updatedList.size() != activeList.size()) { //If the lists are the same size, there will be no new items to add
            for(T newItem : updatedList) {
                boolean contained = true; //Default assumption that the item is already contained in activeList
                for(T oldItem : activeList) {
                    if(oldItem.equals(newItem)) {
                        contained = true;
                        break; //The item is already in the list
                    } else {
                        contained = false;
                    }
                }
                if(!contained) {
                    add(newItem);
                }
            }
        }
    }

    public interface ListAddListener {
        void onAdd(Object item, int index);
    }

    public interface ListRemoveListener {
        void onRemove(Object item, int index);
    }

    public static class NullListAddListener implements ListAddListener {

        @Override
        public void onAdd(Object item, int index) {

        }
    }

    public static class NullListRemoveListener implements ListRemoveListener {

        @Override
        public void onRemove(Object item, int index) {

        }
    }
}