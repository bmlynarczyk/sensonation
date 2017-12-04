package com.sensonation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Slf4j
public class ManagedBlind {

    @Getter
    private final String name;
    private Boolean active;

    public boolean isActive() {
        return active;
    }

}
