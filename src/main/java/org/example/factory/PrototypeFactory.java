package org.example.factory;

public interface PrototypeFactory<T> {
    T create(Class<? extends T> type);
}
