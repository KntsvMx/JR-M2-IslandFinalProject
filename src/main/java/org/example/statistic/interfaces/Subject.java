package org.example.statistic.interfaces;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);

    void notifyObservers(StatsType type, int count);
}
