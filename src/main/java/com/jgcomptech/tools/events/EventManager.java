package com.jgcomptech.tools.events;

import com.sun.istack.internal.NotNull;
import javafx.beans.NamedArg;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows global event handler registration and forwards received
 * events to the appropriate registered event handlers.
 * @since 1.4.0
 */
public final class EventManager {
    private static EventManager instance;
    private final Map<String, Event> events = new HashMap<>();

    private EventManager() { }

    public static EventManager getInstance() {
        if(instance == null) instance = new EventManager();
        return instance;
    }

    private Event registerNewEvent(@NamedArg("name") @NotNull final String eventName,
                                     @NamedArg("target") @NotNull final EventTarget<? extends Event> target) {
        if (eventName == null || eventName.isEmpty()) {
            throw new NullPointerException("Event name cannot be null!");
        }
        if (target == null) {
            throw new NullPointerException("Event target cannot be null!");
        }

        return events.put(eventName, new Event(target));
    }

    public <T extends Event> T registerNewEvent(@NamedArg("name") @NotNull final String eventName,
                                                @NamedArg("classRef") @NotNull final Class<T> classRef,
                                                @NamedArg("eventType") @NotNull final EventType<? extends T> eventType)
            throws Exception {
        return registerNewEvent(eventName, classRef, new EventTarget<>(), eventType);
    }

    public <T extends Event> T registerNewEvent(@NamedArg("name") @NotNull final String eventName,
                                                @NamedArg("classRef") @NotNull final Class<T> classRef,
                                                @NamedArg("eventType") @NotNull final EventType<? extends T> eventType,
                                                @NamedArg("args") @NotNull final List<Object> args)
            throws Exception {
        return registerNewEvent(eventName, classRef, new EventTarget<>(), eventType, args);
    }

    public <T extends Event> T registerNewEvent(@NamedArg("name") @NotNull final String eventName,
                                                @NamedArg("classRef") @NotNull final Class<T> classRef,
                                                @NamedArg("target") @NotNull final EventTarget<? extends T> target,
                                                @NamedArg("eventType") @NotNull final EventType<? extends T> eventType)
            throws Exception {
        return registerNewEvent(eventName, classRef, target, eventType, new ArrayList<>());
    }

    public <T extends Event> T registerNewEvent(@NamedArg("name") @NotNull final String eventName,
                                                @NamedArg("classRef") @NotNull final Class<T> classRef,
                                                @NamedArg("target") @NotNull final EventTarget<? extends T> target,
                                                @NamedArg("eventType") @NotNull final EventType<? extends T> eventType,
                                                @NamedArg("args") @NotNull final List<Object> args)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (eventName == null || eventName.isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null!");
        }
        if (classRef == null) {
            throw new IllegalArgumentException("Event class ref cannot be null!");
        }
        if (target == null) {
            throw new IllegalArgumentException("Event target cannot be null!");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null!");
        }
        if(args == null) {
            throw new IllegalArgumentException("Event Args cannot be null!");
        }

        Constructor<T> constructor = classRef.getConstructor(EventTarget.class, EventType.class, List.class);

        T event = constructor.newInstance(target, eventType, args);

        events.put(eventName, event);

        return event;
    }

    public <T extends Event> T getEvent(final String eventName) {
        return (T) events.get(eventName);
    }

    /**
     * Fires the specified event with the specified source.
     * @param eventName the event to fire
     * @param source the event source which sent the event
     */
    public void fireEvent(final String eventName,
                          final Object source) {
        getEvent(eventName).fireEvent(source);
    }

    /**
     * Fires the specified event with the specified source and args.
     * @param eventName the event to fire
     * @param source the event source which sent the event
     * @param args a list of parameters to pass to the EventHandler
     */
    public void fireEvent(final String eventName,
                          final Object source,
                          final Object... args) {
        getEvent(eventName).fireEvent(source, args);
    }
}
