package com.sensonation.domain;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlindLimitSwitch {

    private final GpioPinDigitalInput input;

    public boolean isOpen(){
        return input.isHigh();
    }

}
