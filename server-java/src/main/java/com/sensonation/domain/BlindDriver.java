package com.sensonation.domain;

public interface BlindDriver {

    void pullDown();

    void pullUp();

    void stop();

    boolean isPullDownLimitReached();

    boolean isPullUpLimitReached();

    boolean stillPullingDown();

    boolean stillPullingUp();

    boolean isStopped();

    String getName();

}
