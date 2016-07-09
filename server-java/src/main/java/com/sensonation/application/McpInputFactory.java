package com.sensonation.application;

import com.pi4j.io.gpio.*;
import com.sensonation.domain.McpInput;

public class McpInputFactory {

    private final GpioController gpio;

    public McpInputFactory(GpioController gpio) {
        this.gpio = gpio;
    }

    public McpInput createInput(GpioProvider mcp, Pin pin) {
        return new McpInput(gpio.provisionDigitalInputPin(mcp, pin, PinPullResistance.PULL_UP));
    }

}
