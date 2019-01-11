package com.example.eric.newtraveler.observer;

public interface ISubject {
    void addObserver(IObserver queryCityListObserver);

    void removeObserver(IObserver queryCityListObserver);

    <T> void notifyObservers(T string);
}
