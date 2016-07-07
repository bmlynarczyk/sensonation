package com.sensonation.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Slf4j
public class ManagedBlind {

    @Getter
    private final String name;
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

}
