package com.sensonation.application;

import com.sensonation.domain.BlindDriver;

import java.util.Map;

public interface BlindDriversProvider {

    Map<String, BlindDriver> get();

}
