package com.sensonation.application;

import com.pi4j.io.gpio.*;
import com.sensonation.domain.BlindLimitSwitch;

public class BlindLimitSwitchFactory {

    private final GpioController gpio;

    public BlindLimitSwitchFactory(GpioController gpio) {
        this.gpio = gpio;
    }

    public BlindLimitSwitch createInput(GpioProvider mcp, Pin pin) {
        return new BlindLimitSwitch(gpio.provisionDigitalInputPin(mcp, pin, PinPullResistance.PULL_UP));
    }

}
