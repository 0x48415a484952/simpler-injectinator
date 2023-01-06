package com.ox48415a484952;

import java.util.HashMap;
import java.util.Map;

public class MySyringe implements Syringe {
    private final Map<Class<?>, Class<?>> injectables = new HashMap<>();

    @Override
    public void configure() {
        registerInjectables(Logger.class, ox48415a484952Logger.class);
    }

    private <T> void registerInjectables(Class<T> baseClass, Class<? extends T> subClass) {
        injectables.put(baseClass, subClass.asSubclass(baseClass));
    }

    @Override
    public <T> Class<? extends T> getInjectables(Class<T> type) {
        Class<?> injectable = injectables.get(type);
        if (injectable == null) {
            throw new IllegalArgumentException("No injectable registered for type " + type);
        }
        return injectable.asSubclass(type);
    }
}
