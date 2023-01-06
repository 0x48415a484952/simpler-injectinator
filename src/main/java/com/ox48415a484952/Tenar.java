package com.ox48415a484952;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Tenar {
    private final Syringe syringe;
    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private Tenar(Syringe syringe) {
        this.syringe = syringe;
        this.syringe.configure();
    }

    public static Tenar getTenar(Syringe syringe) {
        return new Tenar(syringe);
    }

    public <T> T inject(Class<T> classToBeInjectInto) throws Exception {
        for (Constructor<?> constructor : classToBeInjectInto.getConstructors()) {
            if (constructor.isAnnotationPresent(Teyne.class)) {
                return injectViaConstructor(constructor, classToBeInjectInto);
            }
        }
        for (Method method : classToBeInjectInto.getMethods()) {
            if (method.isAnnotationPresent(Teyne.class)) {
                return injectViaSetters(classToBeInjectInto);
            }
        }
        return injectViaFields(classToBeInjectInto);
    }

    //this method only works in java versions 11+
    private <T> T injectViaSetters(Class<T> classToBeInjectInto) throws Exception {
        T instance = classToBeInjectInto.getConstructor().newInstance();
        for (Method method : classToBeInjectInto.getMethods()) {
            if (method.isAnnotationPresent(Teyne.class)
                    && method.getName().startsWith("set")
                    && method.getParameterCount() == 1) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (method.getAnnotation(Teyne.class).value().equals(InjectionType.SINGLETON)) {
                    method.invoke(instance, getSingleton(parameterType));
                } else {
                    method.invoke(instance, inject(syringe.getInjectables(parameterType)));
                }
            }
        }
        return instance;
    }

    private Object getSingleton(Class<?> type) throws Exception {
        if (!this.singletons.containsKey(type)) {
            singletons.put(type, inject(syringe.getInjectables(type)));
        }
        return singletons.get(type);
    }

    private <T> T injectViaConstructor(Constructor<?> constructor, Class<T> classToBeInjectInto) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];
        int i = 0;
        for (Class<?> parameterType : parameterTypes) {
            dependencies[i] = inject(syringe.getInjectables(parameterType));
            i++;
        }
        return classToBeInjectInto.getConstructor(parameterTypes).newInstance(dependencies);
    }

    private <T> T injectViaFields(Class<T> classToBeInjectInto) throws Exception {
        T instance = classToBeInjectInto.getConstructor().newInstance();
        for (Field field : classToBeInjectInto.getDeclaredFields()) {
            if (field.isAnnotationPresent(Teyne.class)) {
                field.setAccessible(Boolean.TRUE);
                field.set(instance, inject(syringe.getInjectables(field.getType())));
            }
        }
        return instance;
    }
}
