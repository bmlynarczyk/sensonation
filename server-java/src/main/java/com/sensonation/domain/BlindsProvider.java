package com.sensonation.domain;

import com.google.common.collect.ImmutableMap;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.gpio.extension.mcp.MCP23S17Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.sensonation.config.McpInputFactory;
import com.sensonation.config.McpOutputFactory;

import java.util.Map;
import java.util.function.Supplier;

public class BlindsProvider implements Supplier<Map<String, Blind>> {

    private final McpOutputFactory mcpOutputFactory;

    private final McpInputFactory mcpInputFactory;

    public final GpioProvider mcpA;

    private final ImmutableMap<String, Blind> blinds;

    public BlindsProvider(McpOutputFactory mcpOutputFactory, McpInputFactory mcpInputFactory, GpioProvider mcpA) {
        this.mcpOutputFactory = mcpOutputFactory;
        this.mcpInputFactory = mcpInputFactory;
        this.mcpA = mcpA;
        blinds = populateBlinds();
    }

    @Override
    public Map<String, Blind> get() {
        return blinds;
    }

    private ImmutableMap<String, Blind> populateBlinds() {
        return ImmutableMap.of(
                "a", Blind.builder()
                        .name("a")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B0))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B1))
                        .firstInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A7))
                        .secondInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A6))
                        .active(true)
                        .build(),
                "b", Blind.builder()
                        .name("b")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B2))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B3))
                        .firstInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A5))
                        .secondInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A4))
                        .active(true)
                        .build(),
                "c", Blind.builder()
                        .name("c")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B4))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B5))
                        .firstInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A3))
                        .secondInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A2))
                        .active(true)
                        .build(),
                "d", Blind.builder()
                        .name("d")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B6))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B7))
                        .firstInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A1))
                        .secondInput(mcpInputFactory.createInput(mcpA, MCP23017Pin.GPIO_A0))
                        .active(true)
                        .build()
        );
    }
}
