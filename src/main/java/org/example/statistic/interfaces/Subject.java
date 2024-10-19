package org.example.statistic.interfaces;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
//    TODO: Add args for update method for interaction with statisticMonitor
    void notifyObservers();
}
