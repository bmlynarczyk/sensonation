package com.sensonation.config;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.sensonation.application.*;
import com.sensonation.application.McpInputFactory;
import com.sensonation.application.McpOutputFactory;
import com.sensonation.domain.*;
import com.sensonation.interfaces.BlindController;
import com.sensonation.interfaces.BlindLimitSwitchController;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

@Configuration
@Profile("prod")
public class BlindConfig {

    @Bean
    GpioController gpio(){
        return GpioFactory.getInstance();
    }

    @Bean
    public McpOutputFactory mcpOutputFactory(GpioController gpio){
        return new McpOutputFactory(gpio);
    }

    @Bean
    public McpInputFactory mcpInputFactory(GpioController gpio){
        return new McpInputFactory(gpio);
    }

    @Bean
    @SneakyThrows
    public GpioProvider mcpA(){
        return new MCP23017GpioProvider(I2CBus.BUS_1, 0x20);
    }

    @Bean
    public BlindDriversProvider blindDriversProvider(McpOutputFactory mcpOutputFactory,
                                                                   McpInputFactory mcpInputFactory,
                                                                   GpioProvider mcpA){
        return new BlindDriversProvider(new BlindsDriversConfig(mcpOutputFactory, mcpInputFactory, mcpA));
    }

    @Bean
    public BlindActionsExecutor blindActionsExecutor(BlindDriversProvider blindDriversProvider){
        return new BlindActionsExecutor(blindDriversProvider);
    }

    @Bean
    public Supplier<Map<String, ManagedBlind>> managedBlindsProvider(){
        return new ManagedBlindsProvider();
    }

    @Bean
    public ArrayBlockingQueue<BlindEvent> blindEvents(){
        return new ArrayBlockingQueue<>(20);
    }

    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public BlindStopper blindStopper(TaskExecutor taskExecutor, BlindActionsExecutor blindActionsExecutor){
        return new BlindStopper(taskExecutor, blindActionsExecutor);
    }

    @Bean
    public BlindLimitSwitchesMonitor limitSwitchesMonitor(BlindDriversProvider blindDriversProvider,
                                                          TaskExecutor taskExecutor,
                                                          BlindLimitSwitchesExpositor blindLimitSwitchesExpositor,
                                                          BlindService blindService){
        return new BlindLimitSwitchesMonitor(blindDriversProvider, taskExecutor, blindLimitSwitchesExpositor, blindService);
    }

    @Bean
    public BlindLimitSwitchesExpositor blindLimitSwitchesExpositor(BlindDriversProvider blindDriversProvider) {
        return new BlindLimitSwitchesExpositor(blindDriversProvider, Clock.systemDefaultZone());
    }

    @Bean
    public BlindLimitSwitchesMonitorActionsExecutor blindLimitSwitchesMonitorActionExecutor(BlindLimitSwitchesMonitor blindLimitSwitchesMonitor){
        return new BlindLimitSwitchesMonitorActionsExecutor(blindLimitSwitchesMonitor);
    }

    @Bean
    public BlindEventBus blindEventBus(TaskExecutor taskExecutor,
                                       ArrayBlockingQueue<BlindEvent> blindEvents,
                                       BlindActionsExecutor blindActionsExecutor,
                                       BlindLimitSwitchesMonitorActionsExecutor blindLimitSwitchesMonitorActionsExecutor){
        return new BlindEventBus(taskExecutor, blindEvents, blindActionsExecutor, blindLimitSwitchesMonitorActionsExecutor);
    }

    @Bean
    public BlindService blindService(Supplier<Map<String, ManagedBlind>> managedBlindsProvider,
                                     ArrayBlockingQueue<BlindEvent> blindEvents,
                                     BlindStopper blindStopper){
        return new BlindService(managedBlindsProvider, blindEvents, blindStopper);
    }

    @Bean
    public BlindController blindController(BlindService blindService, Supplier<Map<String, ManagedBlind>> managedBlindsProvider){
        return new BlindController(blindService, managedBlindsProvider);
    }

    @Bean
    public BlindLimitSwitchController blindLimitSwitchController(BlindLimitSwitchesExpositor blindLimitSwitchesExpositor, BlindDriversProvider blindDriversProvider){
        return new BlindLimitSwitchController(blindLimitSwitchesExpositor, blindDriversProvider);
    }

}

