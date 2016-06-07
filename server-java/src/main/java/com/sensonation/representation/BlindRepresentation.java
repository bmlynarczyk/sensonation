package com.sensonation.representation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlindRepresentation {
    String name;
    Boolean active;
}
