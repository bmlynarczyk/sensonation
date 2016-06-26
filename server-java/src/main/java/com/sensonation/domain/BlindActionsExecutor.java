package com.sensonation.domain;

import java.util.function.BiConsumer;

public interface BlindActionsExecutor {

    void executeFor(String blindName, String actionName);

    void pullUpAllBlinds();

    void pullDownAllBlinds();

}
