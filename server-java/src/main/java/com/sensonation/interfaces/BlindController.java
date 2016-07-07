package com.sensonation.interfaces;

import com.sensonation.application.BlindService;
import com.sensonation.domain.ManagedBlind;
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

    private final BlindService blindService;
    private final Supplier<Map<String, ManagedBlind>> blindsProvider;

    public BlindController(BlindService blindService, Supplier<Map<String, ManagedBlind>> blindsProvider) {
        this.blindService = blindService;
        this.blindsProvider = blindsProvider;
    }

    @RequestMapping(value = "/blinds/{name}/execute/{action}", method = PUT)
    public void executeAction(@PathVariable("name") String blindName, @PathVariable("action") String actionName) {
        blindService.executeFor(blindName, actionName);
    }

    @RequestMapping(value = "/blinds/pullUp", method = PUT)
    public void pullUpAll() {
        blindService.pullUpAllBlinds();
    }

    @RequestMapping(value = "/blinds/pullDown", method = PUT)
    public void pullDownAll() {
        blindService.pullDownAllBlinds();
    }

    @RequestMapping(value = "/blinds", method = GET)
    public List<BlindRepresentation> get() {
        return blindsProvider.get().entrySet().stream()
                .map(entry -> toRepresentation(entry.getValue()))
                .collect(Collectors.toList());
    }

    private BlindRepresentation toRepresentation(ManagedBlind blind) {
        return BlindRepresentation.builder()
                .name(blind.getName())
                .active(blind.isActive())
                .build();
    }

}
