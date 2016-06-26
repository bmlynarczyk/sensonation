package com.sensonation.config.dev;

import com.google.common.collect.ImmutableMap;
import com.sensonation.controller.BlindController;
import com.sensonation.domain.Blind;
import com.sensonation.domain.BlindActionsExecutor;
import com.sensonation.domain.BlindActionsExecutorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;
import java.util.function.Supplier;

@Configuration
@Profile("dev")
public class BlindConfig {

    @Bean
    public Supplier<Map<String, Blind>> blindsProvider(){
        return ImmutableMap::of;
    }

    @Bean
    public BlindActionsExecutor blindActionsExecutor(){
        return new BlindActionsExecutor() {

            @Override
            public void pullUpAllBlinds() {

            }

            @Override
            public void pullDownAllBlinds() {

            }

            @Override
            public void executeFor(String s, String s2) {

            }
        };
    }

    @Bean
    public BlindController blindController(BlindActionsExecutor blindActionsExecutor, Supplier<Map<String, Blind>> blindsProvider){
        return new BlindController(blindActionsExecutor, blindsProvider);
    }

}

