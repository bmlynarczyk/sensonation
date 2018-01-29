package com.sensonation.application;

import com.sensonation.ServerApplication;
import com.sensonation.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
public class BlindProgressActionExecutorTest {

    @Test
    public void contextLoad() {
    }

}