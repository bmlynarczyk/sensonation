package com.sensonation.domain;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Supplier;

public class ManagedBlindsProvider implements Supplier<Map<String, ManagedBlind>> {

    private final Map<String, ManagedBlind> blinds;

    public ManagedBlindsProvider() {
        blinds = populateBlinds();
    }

    @Override
    public Map<String, ManagedBlind> get() {
        return blinds;
    }

    private Map<String, ManagedBlind> populateBlinds() {
        Map<String, ManagedBlind> map = Maps.newConcurrentMap();
        map.put("a", ManagedBlind.builder().name("a").active(true).build());
        map.put("b", ManagedBlind.builder().name("b").active(true).build());
        map.put("c", ManagedBlind.builder().name("c").active(true).build());
        map.put("d", ManagedBlind.builder().name("d").active(true).build());
        return map;
    }
}
