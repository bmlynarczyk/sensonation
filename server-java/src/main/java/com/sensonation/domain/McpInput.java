package com.sensonation.domain;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class McpInput {

    private final GpioPinDigitalInput input;

    boolean isOpen(){
        boolean high = input.isHigh();
        log.info("input is high {}", high);
        return high;
    }

}
