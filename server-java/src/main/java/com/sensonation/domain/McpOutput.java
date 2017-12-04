package com.sensonation.domain;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class McpOutput {

    private final GpioPinDigitalOutput output;

    void setHigh() {
        output.setState(PinState.HIGH);
    }

    void setLow() {
        output.setState(PinState.LOW);
    }

}
