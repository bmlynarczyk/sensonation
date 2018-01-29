package com.sensonation;

import com.sensonation.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        BlindConfig.class,
        BlindServiceConfig.class,
        BlindSchedulerConfig.class,
        ControllerConfig.class,
        GpioConfig.class,
        BlindEventConfig.class,
        WebConfig.class
})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
