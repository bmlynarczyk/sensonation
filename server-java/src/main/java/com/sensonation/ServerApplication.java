package com.sensonation;

import com.sensonation.config.BlindConfig;
import com.sensonation.config.BlindSchedulerConfig;
import com.sensonation.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        BlindConfig.class,
        BlindSchedulerConfig.class,
        WebConfig.class
})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
