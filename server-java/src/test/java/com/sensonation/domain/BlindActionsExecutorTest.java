package com.sensonation.domain;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class BlindActionsExecutorTest {

    Supplier<Map<String, BlindDriver>> blindsSupplier = mock(Supplier.class);

    Supplier<Map<String, Consumer<BlindDriver>>> blindActionsProvider = mock(Supplier.class);

    Consumer<BlindDriver> action = mock(Consumer.class);

    @Before
    public void setUp(){
        reset(blindsSupplier, blindActionsProvider, action);
    }

    @Test
    public void should_throw_exception_when_blind_doesnt_exist(){
//        given
        when(blindsSupplier.get()).thenReturn(ImmutableMap.of());
        when(blindActionsProvider.get()).thenReturn(ImmutableMap.of());
        final BlindActionsExecutor blindActionsExecutor = new BlindActionsExecutor(blindsSupplier);
//        when
//        then
        assertThatThrownBy(() -> blindActionsExecutor.executeFor("blindName", "actionName")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void should_throw_exception_when_action_is_unsupported(){
//        given
        final BlindDriver blindDriver = BlindDriver.builder().build();
        when(blindsSupplier.get()).thenReturn(ImmutableMap.of("blindName", blindDriver));
        final BlindActionsExecutor blindActionsExecutor = new BlindActionsExecutor(blindsSupplier);
//        when
//        then
        assertThatThrownBy(() -> blindActionsExecutor.executeFor("blindName", "actionName")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_execute_supported_action_on_existing_blind(){
//        given
        final BlindDriver blindDriver = Mockito.mock(BlindDriver.class);
        when(blindsSupplier.get()).thenReturn(ImmutableMap.of("blindName", blindDriver));
        final BlindActionsExecutor blindActionsExecutor = new BlindActionsExecutor(blindsSupplier);
//        when
        blindActionsExecutor.executeFor("blindName", "stop");
//        then
        verify(blindDriver, times(1)).stop();
    }

}