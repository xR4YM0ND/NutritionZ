package net.nutritionz.api.event;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public final class Event<T> {

    private final Class<T> type;
    private final Function<T[], T> invokerFactory;
    private volatile T[] listeners;
    private volatile T invoker;

    @SuppressWarnings("unchecked")
    private Event(Class<T> type, Function<T[], T> invokerFactory) {
        this.type = type;
        this.invokerFactory = invokerFactory;
        this.listeners = (T[]) Array.newInstance(type, 0);
        this.invoker = invokerFactory.apply(this.listeners);
    }

    public static <T> Event<T> create(Class<T> type, Function<T[], T> invokerFactory) {
        return new Event<>(type, invokerFactory);
    }

    public synchronized void register(T listener) {
        Objects.requireNonNull(listener, "Tried to register a null listener for " + this.type.getName());
        T[] newListeners = Arrays.copyOf(this.listeners, this.listeners.length + 1);
        newListeners[newListeners.length - 1] = listener;
        this.listeners = newListeners;
        this.invoker = this.invokerFactory.apply(newListeners);
    }

    public T invoker() {
        return this.invoker;
    }
}
