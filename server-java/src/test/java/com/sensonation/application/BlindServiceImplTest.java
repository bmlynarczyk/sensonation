package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.sensonation.domain.BlindActionsExecutor;
import com.sensonation.domain.BlindActionsExecutorImpl;
import com.sensonation.domain.BlindDriver;
import com.sensonation.domain.ManagedBlind;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BlindServiceImplTest {

    private final Supplier blindsProvider = mock(Supplier.class);
    private final BlockingDeque blindEvents = mock(BlockingDeque.class);
    private final BlindStopper blindStopper = mock(BlindStopper.class);

    @Before
    public void setUp(){
        reset(blindsProvider, blindEvents, blindStopper);
    }

    @Test
    public void should_throw_exception_when_blind_doesnt_exist(){
//        given
        when(blindsProvider.get()).thenReturn(ImmutableMap.of());
        final BlindServiceImpl blindService = new BlindServiceImpl(blindsProvider, blindEvents, blindStopper);
//        when
//        then
        assertThatThrownBy(() -> blindService.executeFor("blindName", "stop")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void should_throw_exception_when_action_is_unsupported(){
//        given
        final ManagedBlind managedBlind = ManagedBlind.builder().name("blindName").build();
        when(blindsProvider.get()).thenReturn(ImmutableMap.of("blindName", managedBlind));
        final BlindServiceImpl blindService = new BlindServiceImpl(blindsProvider, blindEvents, blindStopper);
//        when
//        then
        assertThatThrownBy(() -> blindService.executeFor("blindName", "actionName")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_execute_supported_action_on_existing_blind(){
//        given
        final ManagedBlind managedBlind = ManagedBlind.builder().name("blindName").active(true).build();
        when(blindsProvider.get()).thenReturn(ImmutableMap.of("blindName", managedBlind));
        final BlindServiceImpl blindService = new BlindServiceImpl(blindsProvider, blindEvents, blindStopper);
//        when
        blindService.executeFor("blindName", "stop");
//        then
        verify(blindEvents, times(1)).drainTo(any());
        verify(blindStopper, times(1)).stop();
    }

}