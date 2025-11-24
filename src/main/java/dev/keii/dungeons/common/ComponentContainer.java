package dev.keii.dungeons.common;

import java.util.List;

public interface ComponentContainer<C> {
    <T extends C> T getComponent(Class<T> type);

    boolean hasComponent(Class<? extends C> type);

    List<C> components();
}