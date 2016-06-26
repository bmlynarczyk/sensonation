package com.sensonation.controller;

import com.sensonation.domain.Blind;
import com.sensonation.domain.BlindActionsExecutor;
import com.sensonation.representation.BlindRepresentation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class BlindController {

    private final BlindActionsExecutor blindActionsExecutor;
    private final Supplier<Map<String, Blind>> blindsProvider;

    public BlindController(BlindActionsExecutor blindActionsExecutor, Supplier<Map<String, Blind>> blindsProvider) {
        this.blindActionsExecutor = blindActionsExecutor;
        this.blindsProvider = blindsProvider;
    }

    @RequestMapping(value = "/blinds/{name}/execute/{action}", method = PUT)
    public void executeAction(@PathVariable("name") String blindName, @PathVariable("action") String actionName) {
        blindActionsExecutor.executeFor(blindName, actionName);
    }

    @RequestMapping(value = "/blinds/pullUp", method = PUT)
    public void pullUpAll() {
        blindActionsExecutor.pullUpAllBlinds();
    }

    @RequestMapping(value = "/blinds/pullDown", method = PUT)
    public void pullDownAll() {
        blindActionsExecutor.pullDownAllBlinds();
    }

    @RequestMapping(value = "/blinds", method = GET)
    public List<BlindRepresentation> get() {
        return blindsProvider.get().entrySet().stream()
                .map(entry -> toRepresentation(entry.getValue()))
                .collect(Collectors.toList());
    }

    private BlindRepresentation toRepresentation(Blind blind) {
        return BlindRepresentation.builder()
                .name(blind.getName())
                .active(blind.isActive())
                .build();
    }

}
