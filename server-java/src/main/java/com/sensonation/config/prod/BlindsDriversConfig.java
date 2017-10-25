package com.sensonation.config.prod;

import com.google.common.collect.ImmutableMap;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.sensonation.application.McpInputFactory;
import com.sensonation.application.McpOutputFactory;
import com.sensonation.domain.BlindDriver;

import java.util.Map;

public class BlindsDriversConfig {

    private final McpOutputFactory mcpOutputFactory;

    private final McpInputFactory mcpInputFactory;

    private final GpioProvider mcpA;

    private final ImmutableMap<String, BlindDriver> config;

    public BlindsDriversConfig(McpOutputFactory mcpOutputFactory, McpInputFactory mcpInputFactory, GpioProvider mcpA) {
        this.mcpOutputFactory = mcpOutputFactory;
        this.mcpInputFactory = mcpInputFactory;
        this.mcpA = mcpA;
        config = createConfig();
    }

    private ImmutableMap<String, BlindDriver> createConfig() {
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

    public Map<String, BlindDriver> get() {
        return config;
    }
}
