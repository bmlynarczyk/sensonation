package com.sensonation.domain;

import com.google.common.collect.ImmutableMap;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Builder
@EqualsAndHashCode
@Slf4j
public class Blind {

    private static final String STOP_CODE = "a";

    private final String name;
    private final String pullDownCode;
    private final String pullUpCode;
    private final StateHolder stateHolder;
    private final Consumer<String> executor;

    private Boolean active = true;

    void activate(){
        active = true;
        log.info("blind {} has been activated", name);
    }


    void deactivate(){
        active = false;
        log.info("blind {} has been deactivated", name);
    }

    void stop(){
        if(stateHolder.getState().stopOnly()){
            log.info("stop movement of {}", name);
            executor.accept(STOP_CODE);
        } else {
            throw new IllegalStateException("blind is stopped");
        }
    }

    void pullDown(){
        if(stateHolder.getState().readyToGo()) {
            log.info("pull down {}", name);
            executor.accept(pullDownCode);
            stateHolder.setState(State.STOP_ONLY);
        } else {
            throw new IllegalStateException("blind is moving");
        }
    }

    void pullUp() {
        if (stateHolder.getState().readyToGo()) {
            log.info("pull up {}", name);
            executor.accept(pullUpCode);
            stateHolder.setState(State.STOP_ONLY);
        } else {
            throw new IllegalStateException("blind is moving");
        }
    }

    Map<String, Action> actions = ImmutableMap.of(
            "activate", this::activate,
            "deactivate", this::deactivate,
            "stop", this::stop,
            "pullDown", this::pullDown,
            "pullUp", this::pullUp
    );

    private static final Action THROW_ILLEGAL_ACTION_EXCEPTION = () -> {
        throw new IllegalArgumentException("unknown action");
    };

    public void fireAction(String actionName){
        if(active) {
            log.info("call {}", actionName);
            actions.getOrDefault(actionName, THROW_ILLEGAL_ACTION_EXCEPTION).execute();
        } else if("activate".equals(actionName)){
            activate();
        } else {
            log.info("blind {} is inactive", name);
        }
    }

}
