package de.uprank.server.proxy.module.api.module.event;

@FunctionalInterface
public interface ModuleEventHandler<T> {

    void handle(T event);

}
