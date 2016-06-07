package com.sensonation.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Slf4j
public class Blind {

    @Getter
    private final String name;
    private final McpOutput firstOutput;
    private final McpOutput secondOutput;
    private final McpInput firstInput;
    private final McpInput secondInput;

    private Boolean active;

    public boolean isActive(){
        return active;
    }

    public void activate(){
        active = true;
        log.info("blind {} has been activated", name);
    }


    public void deactivate(){
        active = false;
        log.info("blind {} has been deactivated", name);
    }

    public void stop(){
        log.info("stop movement of {}", name);
        firstOutput.setLow();
        secondOutput.setLow();
    }

    @SneakyThrows
    public void pullDown(){
        log.info("pull down {}", name);
        firstOutput.setLow();
        sleep(500);
        secondOutput.setHigh();
        while (firstInput.isOpen())
            sleep(50);
        stop();
    }

    @SneakyThrows
    public void pullUp() {
        log.info("pull up {}", name);
        secondOutput.setLow();
        sleep(500);
        firstOutput.setHigh();
        while (secondInput.isOpen())
            sleep(50);
        stop();
    }

}
