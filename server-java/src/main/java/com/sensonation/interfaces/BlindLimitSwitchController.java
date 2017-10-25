package com.sensonation.interfaces;

import com.sensonation.application.BlindDriversProvider;
import com.sensonation.application.BlindLimitSwitchesExpositor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/blinds/limits/switches")
public class BlindLimitSwitchController {

    private final BlindLimitSwitchesExpositor blindLimitSwitchesExpositor;
    private final BlindDriversProvider blindDriversProvider;

    @Autowired
    public BlindLimitSwitchController(BlindLimitSwitchesExpositor blindLimitSwitchesExpositor, BlindDriversProvider blindDriversProvider) {
        this.blindLimitSwitchesExpositor = blindLimitSwitchesExpositor;
        this.blindDriversProvider = blindDriversProvider;
    }

    @RequestMapping(method = GET)
    public List<Foo> get() {
        return blindDriversProvider.get().values().stream()
                .map(blindLimitSwitchesExpositor::getState)
                .map(Foo::new)
                .collect(toList());
    }

    @Data
    @AllArgsConstructor
    class Foo {

        private String state;

    }

}
