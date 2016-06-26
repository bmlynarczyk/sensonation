package com.sensonation.domain;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class BlindActionsExecutorImplTest {

    Supplier<Map<String, Blind>> blindsSupplier = mock(Supplier.class);

    Supplier<Map<String, Consumer<Blind>>> blindActionsProvider = mock(Supplier.class);

    Consumer<Blind> action = mock(Consumer.class);

    @Before
    public void setUp(){
        reset(blindsSupplier, blindActionsProvider, action);
    }

    @Test
    public void should_throw_exception_when_blind_doesnt_exist(){
//        given
        when(blindsSupplier.get()).thenReturn(ImmutableMap.of());
        when(blindActionsProvider.get()).thenReturn(ImmutableMap.of());
        final BlindActionsExecutor blindActionsExecutor = new BlindActionsExecutorImpl(blindsSupplier, blindActionsProvider);
//        when
//        then
        assertThatThrownBy(() -> blindActionsExecutor.executeFor("blindName", "actionName")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void should_throw_exception_when_action_is_unsupported(){
//        given
        final Blind blind = Blind.builder().active(true).build();
        when(blindsSupplier.get()).thenReturn(ImmutableMap.of("blindName", blind));
        when(blindActionsProvider.get()).thenReturn(ImmutableMap.of());
        final BlindActionsExecutor blindActionsExecutor = new BlindActionsExecutorImpl(blindsSupplier, blindActionsProvider);
//        when
//        then
        assertThatThrownBy(() -> blindActionsExecutor.executeFor("blindName", "actionName")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_execute_supported_action_on_existing_blind(){
//        given
        final Blind blind = Blind.builder().active(true).build();
        when(blindsSupplier.get()).thenReturn(ImmutableMap.of("blindName", blind));
        when(blindActionsProvider.get()).thenReturn(ImmutableMap.of("actionName", action));
        final BlindActionsExecutor blindActionsExecutor = new BlindActionsExecutorImpl(blindsSupplier, blindActionsProvider);
//        when
        blindActionsExecutor.executeFor("blindName", "actionName");
//        then
        verify(action, times(1)).accept(blind);
    }

}