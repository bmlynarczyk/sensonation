package com.sensonation.config;

import com.sensonation.application.*;
import com.sensonation.domain.DefaultBlindSchedulerPolicy;
import com.sensonation.interfaces.TaskController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;
import java.util.function.Supplier;

import static java.time.Clock.systemDefaultZone;

@Configuration
@ConditionalOnProperty(prefix = "scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BlindSchedulerConfig {

    @Bean
    TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        threadPoolTaskScheduler.setPoolSize(4);
        return threadPoolTaskScheduler;
    }

    @Bean
    SunService sunService(){
        return new SunService(systemDefaultZone());
    }

    @Bean
    public Supplier<Map<ScheduledTaskName, ScheduledTask>> scheduledTaskStoreProvider(){
        return new ScheduledTaskStoreProvider();
    }

    @Bean
    BlindScheduler blindScheduler(TaskScheduler taskScheduler, BlindService blindService, SunService sunService, Supplier<Map<ScheduledTaskName, ScheduledTask>> scheduledTaskStoreProvider){
        DefaultBlindSchedulerPolicy policy = new DefaultBlindSchedulerPolicy(sunService, systemDefaultZone());
        return new BlindScheduler(policy, taskScheduler, blindService, sunService, scheduledTaskStoreProvider.get());
    }

    @Bean
    TaskController taskController(Supplier<Map<ScheduledTaskName, ScheduledTask>> scheduledTaskStoreProvider){
        return new TaskController(scheduledTaskStoreProvider);
    }

}
