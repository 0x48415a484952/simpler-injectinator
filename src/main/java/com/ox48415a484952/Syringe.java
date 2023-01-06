package com.ox48415a484952;

public interface Syringe {
    void configure(); //we need this to wire together

    <T> Class<? extends T> getInjectables(Class<T> type);
}
