package com.sensonation.config;

import com.google.common.collect.ImmutableMap;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.sensonation.application.BlindDriversProvider;
import com.sensonation.application.BlindLimitSwitchFactory;
import com.sensonation.application.McpOutputFactory;
import com.sensonation.domain.BlindDriver;
import com.sensonation.domain.CustomServoBlindDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomServoBlindDriversProvider implements BlindDriversProvider {
    private final McpOutputFactory mcpOutputFactory;

    private final BlindLimitSwitchFactory blindLimitSwitchFactory;

    private final GpioProvider mcpA;
    private final GpioProvider mcpB;

    private final ImmutableMap<String, BlindDriver> config;

    public CustomServoBlindDriversProvider(McpOutputFactory mcpOutputFactory, BlindLimitSwitchFactory blindLimitSwitchFactory, GpioProvider mcpA, GpioProvider mcpB) {
        this.mcpOutputFactory = mcpOutputFactory;
        this.blindLimitSwitchFactory = blindLimitSwitchFactory;
        this.mcpA = mcpA;
        this.mcpB = mcpB;
        config = createConfig();
    }

    private ImmutableMap<String, BlindDriver> createConfig() {
        return ImmutableMap.of(
                "a", CustomServoBlindDriver.builder()
                        .name("a")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B0))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B1))
                        .pullDownLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A7))
                        .pullUpLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A6))
                        .build(),
                "b", CustomServoBlindDriver.builder()
                        .name("b")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B2))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B3))
                        .pullDownLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A5))
                        .pullUpLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A4))
                        .build(),
                "c", CustomServoBlindDriver.builder()
                        .name("c")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B4))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B5))
                        .pullDownLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A3))
                        .pullUpLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A2))
                        .build(),
                "d", CustomServoBlindDriver.builder()
                        .name("d")
                        .firstOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B6))
                        .secondOutput(mcpOutputFactory.createOutput(mcpA, MCP23017Pin.GPIO_B7))
                        .pullDownLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A1))
                        .pullUpLimitSwitch(blindLimitSwitchFactory.createInput(mcpA, MCP23017Pin.GPIO_A0))
                        .build(),
                "e", CustomServoBlindDriver.builder()
                        .name("e")
                        .firstOutput(mcpOutputFactory.createOutput(mcpB, MCP23017Pin.GPIO_B0))
                        .secondOutput(mcpOutputFactory.createOutput(mcpB, MCP23017Pin.GPIO_B1))
                        .pullDownLimitSwitch(blindLimitSwitchFactory.createInput(mcpB, MCP23017Pin.GPIO_A7))
                        .pullUpLimitSwitch(blindLimitSwitchFactory.createInput(mcpB, MCP23017Pin.GPIO_A6))
                        .build()
        );
    }

    public Map<String, BlindDriver> get() {
        return new ConcurrentHashMap<>(config);
    }
}
