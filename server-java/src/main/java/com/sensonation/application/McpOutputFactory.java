package com.sensonation.application;

import com.pi4j.io.gpio.*;
import com.sensonation.domain.McpOutput;

public class McpOutputFactory {

    private final GpioController gpio;

    public McpOutputFactory(GpioController gpio) {
        this.gpio = gpio;
    }


    public McpOutput createOutput(GpioProvider mcp, Pin pin) {
        return new McpOutput(gpio.provisionDigitalOutputPin(mcp, pin, PinState.LOW));
    }

}
