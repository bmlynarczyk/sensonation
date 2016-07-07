package com.sensonation.application;

public interface BlindService {

    void executeFor(String blindName, String actionName);

    void pullUpAllBlinds();

    void pullDownAllBlinds();

}
