package com.sensonation.domain;

import java.util.function.BiConsumer;

public interface BlindActionsExecutor extends BiConsumer<String, String> {

    void executeFor(String blindName, String actionName);

    default void accept(String blindName, String actionName){
        executeFor(blindName, actionName);
    }

    void stopAll();
}
