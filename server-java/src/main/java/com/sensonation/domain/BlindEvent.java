package com.sensonation.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BlindEvent {
    private final String blindName;
    private final String actionName;
}
