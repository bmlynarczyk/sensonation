package com.sensonation.config.prod;

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
    public Supplier<Map<String, BlindDriver>> blindDriversProvider(McpOutputFactory mcpOutputFactory,
                                                                   McpInputFactory mcpInputFactory,
                                                                   GpioProvider mcpA){
        return new BlindDriversProvider(mcpOutputFactory, mcpInputFactory, mcpA);
    }

    @Bean
    public BlindActionsExecutor blindActionsExecutor(Supplier<Map<String, BlindDriver>> blindDriversProvider){
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
    public BlindLimitSwitchesMonitor limitSwitchesMonitor(Supplier<Map<String, BlindDriver>> blindDriversProvider,
                                                          TaskExecutor taskExecutor){
        return new BlindLimitSwitchesMonitor(blindDriversProvider, taskExecutor, new BlindLimitSwitchesExpositor(blindDriversProvider, Clock.systemDefaultZone()));
    }

    @Bean
    public BlindEventRouter blindEventRouter(TaskExecutor taskExecutor,
                                             ArrayBlockingQueue<BlindEvent> blindEvents,
                                             BlindActionsExecutor blindActionsExecutor,
                                             BlindLimitSwitchesMonitor blindLimitSwitchesMonitor){
        return new BlindEventRouter(taskExecutor, blindEvents, blindActionsExecutor, blindLimitSwitchesMonitor);
    }

    @Bean
    public BlindService blindService(Supplier<Map<String, ManagedBlind>> managedBlindsProvider,
                                     ArrayBlockingQueue<BlindEvent> blindEvents,
                                     BlindStopper blindStopper){
        return new BlindServiceImpl(managedBlindsProvider, blindEvents, blindStopper);
    }

    @Bean
    public BlindController blindController(BlindService blindService, Supplier<Map<String, ManagedBlind>> managedBlindsProvider){
        return new BlindController(blindService, managedBlindsProvider);
    }

}

