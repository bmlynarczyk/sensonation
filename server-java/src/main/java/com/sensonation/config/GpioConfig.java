package com.sensonation.config;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.sensonation.application.McpInputFactory;
import com.sensonation.application.McpOutputFactory;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GpioConfig {

    @Bean
    public GpioController gpio() {
        return GpioFactory.getInstance();
    }

    @Bean
    public McpOutputFactory mcpOutputFactory(GpioController gpio) {
        return new McpOutputFactory(gpio);
    }

    @Bean
    public McpInputFactory mcpInputFactory(GpioController gpio) {
        return new McpInputFactory(gpio);
    }

    @Bean
    @SneakyThrows
    public GpioProvider mcpA() {
        return new MCP23017GpioProvider(I2CBus.BUS_1, 0x20);
    }

}