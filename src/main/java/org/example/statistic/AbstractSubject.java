package org.example.statistic;

import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.StatsType;
import org.example.statistic.interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

public class AbstractSubject implements Subject {
    private final List<Observer> observerList = new ArrayList<>();

    protected AbstractSubject() {
        addObserver(StatisticMonitor.getInstance());
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(StatsType type, int count) {
        observerList.forEach(observer -> observer.update(type, count));
    }
}
