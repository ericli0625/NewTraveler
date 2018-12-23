package com.example.eric.newtraveler.presenter;

import com.example.eric.newtraveler.view.IObserver;

public interface ISubject {
    void addObserver(IObserver queryCityListObserver);

    void removeObserver(IObserver queryCityListObserver);

    void notifyObservers(String string);
}
