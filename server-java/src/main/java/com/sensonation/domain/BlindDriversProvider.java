package com.sensonation.domain;

import com.google.common.collect.ImmutableMap;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.sensonation.application.McpInputFactory;
import com.sensonation.application.McpOutputFactory;

import java.util.Map;
import java.util.function.Supplier;

public class BlindDriversProvider implements Supplier<Map<String, BlindDriver>> {

    private final McpOutputFactory mcpOutputFactory;

    private final McpInputFactory mcpInputFactory;

    public final GpioProvider mcpA;

    private final ImmutableMap<String, BlindDriver> blinds;

    public BlindDriversProvider(McpOutputFactory mcpOutputFactory, McpInputFactory mcpInputFactory, GpioProvider mcpA) {
        this.mcpOutputFactory = mcpOutputFactory;
        this.mcpInputFactory = mcpInputFactory;
        this.mcpA = mcpA;
        blinds = populateBlinds();
    }

    @Override
    public Map<String, BlindDriver> get() {
        return blinds;
    }

    private ImmutableMap<String, BlindDriver> populateBlinds() {
        return ImmutableMap.of(
                "a", BlindDriver.builder()
                        .name("a")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B0))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B1))
                        .pullDownLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A7))
                        .pullUpLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A6))
                        .build(),
                "b", BlindDriver.builder()
                        .name("b")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B2))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B3))
                        .pullDownLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A5))
                        .pullUpLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A4))
                        .build(),
                "c", BlindDriver.builder()
                        .name("c")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B4))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B5))
                        .pullDownLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A3))
                        .pullUpLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A2))
                        .build(),
                "d", BlindDriver.builder()
                        .name("d")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B6))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B7))
                        .pullDownLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A1))
                        .pullUpLimitSwitch(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A0))
                        .build()
        );
    }
}
