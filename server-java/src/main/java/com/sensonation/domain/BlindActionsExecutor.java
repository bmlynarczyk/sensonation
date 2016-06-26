package com.sensonation.domain;

public interface BlindActionsExecutor {

    void executeFor(String blindName, String actionName);

    void pullUpAllBlinds();

    void pullDownAllBlinds();

}
