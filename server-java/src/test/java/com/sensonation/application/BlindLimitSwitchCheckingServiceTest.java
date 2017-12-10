package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.sensonation.domain.BlindDriver;
import com.sensonation.domain.BlindLimitSwitchState;
import com.sensonation.domain.McpInput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.task.TaskExecutor;

import java.time.Clock;
import java.util.Set;

import static com.sensonation.InstantTestUtils.getDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BlindLimitSwitchCheckingServiceTest {

    private final McpInput pullDownLimitSwitch = mock(McpInput.class);
    private final McpInput pullUpLimitSwitch = mock(McpInput.class);
    private final BlindDriversProvider blindDriversProvider = mock(BlindDriversProvider.class);
    private final TaskExecutor taskExecutor = mock(TaskExecutor.class);

    private final BlindDriver blindDriver = BlindDriver.builder()
            .name("a")
            .firstOutput(null)
            .secondOutput(null)
            .pullDownLimitSwitch(pullDownLimitSwitch)
            .pullUpLimitSwitch(pullUpLimitSwitch)
            .build();

    private final Clock clock = Mockito.mock(Clock.class);

    @Before
    public void setUp(){
        reset(pullDownLimitSwitch, pullUpLimitSwitch);
        stub(blindDriversProvider.get()).toReturn(ImmutableMap.of("a", blindDriver));
    }

    @Test
    public void should_gives_possibility_of_initialisation_by_blinds_drivers(){
        when(pullDownLimitSwitch.isOpen()).thenReturn(true);
        when(pullUpLimitSwitch.isOpen()).thenReturn(true);
        BlindLimitSwitchCheckingService expositor = new BlindLimitSwitchCheckingService(blindDriversProvider, Clock.systemDefaultZone(), taskExecutor);

        Set<BlindLimitSwitchState> blindLimitSwitchStates = expositor.getStates();
        BlindLimitSwitchState blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertThat(blindLimitSwitchState.getName()).isEqualTo("a");
        assertThat(blindLimitSwitchState.isPullDownLimitReached()).isFalse();
        assertThat(blindLimitSwitchState.isPullUpLimitReached()).isFalse();
    }

    @Test
    public void should_expose_new_state_of_blind_limit_switches(){
        when(pullDownLimitSwitch.isOpen()).thenReturn(true, true, false);
        when(pullUpLimitSwitch.isOpen()).thenReturn(true, true, true);
        BlindLimitSwitchCheckingService expositor = new BlindLimitSwitchCheckingService(blindDriversProvider, Clock.systemDefaultZone(), taskExecutor);

        Set<BlindLimitSwitchState> blindLimitSwitchStates = expositor.getStates();
        BlindLimitSwitchState blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertThat(blindLimitSwitchState.getName()).isEqualTo("a");
        assertThat(blindLimitSwitchState.isPullDownLimitReached()).isFalse();
        assertThat(blindLimitSwitchState.isPullUpLimitReached()).isFalse();

        expositor.reportState(blindDriver);

        blindLimitSwitchStates = expositor.getStates();
        blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertThat(blindLimitSwitchState.getName()).isEqualTo("a");
        assertThat(blindLimitSwitchState.isPullDownLimitReached()).isTrue();
        assertThat(blindLimitSwitchState.isPullUpLimitReached()).isFalse();
    }

    @Test
    public void should_expose_new_state_of_blind_limit_switches_when_suspicious_state_occurs(){
        when(pullDownLimitSwitch.isOpen()).thenReturn(true, true, false);
        when(pullUpLimitSwitch.isOpen()).thenReturn(true, true, false);
        when(clock.instant()).thenReturn(getDate(2015, 12, 27, 0, 0));
        BlindLimitSwitchCheckingService expositor = new BlindLimitSwitchCheckingService(blindDriversProvider, clock, taskExecutor);

        Set<BlindLimitSwitchState> blindLimitSwitchStates = expositor.getStates();
        BlindLimitSwitchState blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertThat(blindLimitSwitchState.getName()).isEqualTo("a");
        assertThat(blindLimitSwitchState.isPullDownLimitReached()).isFalse();
        assertThat(blindLimitSwitchState.isPullUpLimitReached()).isFalse();
        assertThat(blindLimitSwitchState.isSuspiciousStateReported()).isFalse();

        expositor.reportState(blindDriver);

        blindLimitSwitchStates = expositor.getStates();
        blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertSuspiciousState(blindLimitSwitchState, "a");
    }



    @Test
    public void should_kept_old_date_of_suspicious_state_when_suspicious_state__still_occurs_between_two_expositions(){
        when(pullDownLimitSwitch.isOpen()).thenReturn(true, true, false);
        when(pullUpLimitSwitch.isOpen()).thenReturn(true, true, false);

        when(clock.instant()).thenReturn(getDate(2015, 12, 27, 0, 0), getDate(2015, 12, 27, 0, 1));

        BlindLimitSwitchCheckingService expositor = new BlindLimitSwitchCheckingService(blindDriversProvider, clock, taskExecutor);

        Set<BlindLimitSwitchState> blindLimitSwitchStates = expositor.getStates();
        BlindLimitSwitchState blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertThat(blindLimitSwitchState.getName()).isEqualTo("a");
        assertThat(blindLimitSwitchState.isPullDownLimitReached()).isFalse();
        assertThat(blindLimitSwitchState.isPullUpLimitReached()).isFalse();
        assertThat(blindLimitSwitchState.isSuspiciousStateReported()).isFalse();

        expositor.reportState(blindDriver);

        blindLimitSwitchStates = expositor.getStates();
        blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertSuspiciousState(blindLimitSwitchState, "a");

        expositor.reportState(blindDriver);

        blindLimitSwitchStates = expositor.getStates();
        blindLimitSwitchState = blindLimitSwitchStates.iterator().next();

        assertThat(blindLimitSwitchStates.size()).isEqualTo(1);
        assertSuspiciousState(blindLimitSwitchState, "a");

    }

    private void assertSuspiciousState(BlindLimitSwitchState blindLimitSwitchState, String expectedBlindName) {
        assertThat(blindLimitSwitchState.getName()).isEqualTo(expectedBlindName);
        assertThat(blindLimitSwitchState.isPullDownLimitReached()).isTrue();
        assertThat(blindLimitSwitchState.isPullUpLimitReached()).isTrue();
        assertThat(blindLimitSwitchState.isSuspiciousStateReported()).isTrue();
    }

}