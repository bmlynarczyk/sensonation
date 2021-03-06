package com.sensonation.config;

import com.sensonation.application.BlindScheduler;
import com.sensonation.application.BlindService;
import com.sensonation.application.SunService;
import com.sensonation.domain.DefaultBlindSchedulerPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static java.time.Clock.systemDefaultZone;

@Configuration
@ConditionalOnProperty(prefix = "scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BlindSchedulerConfig {

    @Bean
    TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        threadPoolTaskScheduler.setPoolSize(4);
        return threadPoolTaskScheduler;
    }

    @Bean
    SunService sunService() {
        return new SunService(systemDefaultZone());
    }

    @Bean
    BlindScheduler blindScheduler(TaskScheduler taskScheduler, BlindService blindService, SunService sunService) {
        DefaultBlindSchedulerPolicy policy = new DefaultBlindSchedulerPolicy(sunService, systemDefaultZone());
        return new BlindScheduler(policy, taskScheduler, blindService, sunService);
    }

}
