package com.sensonation.domain;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BlindTest {

    McpOutput firstOutput = mock(McpOutput.class);
    McpOutput secondOutput = mock(McpOutput.class);
    McpInput firstInput = mock(McpInput.class);
    McpInput secondInput = mock(McpInput.class);

    @Before
    public void setUp(){
        reset(firstOutput, secondOutput, firstInput, secondInput);
    }

    @Test
    public void should_pulled_down_using_outputs_and_input(){
//        given
        Blind blind = Blind.builder().name("test").firstOutput(firstOutput).secondOutput(secondOutput).firstInput(firstInput).secondInput(secondInput).build();
//        when
        when(firstInput.isOpen()).thenReturn(true, false);
        blind.pullDown();
//        then
        assertBlindPulledDown();
    }

    @Test
    public void should_pulled_up_using_outputs_and_input(){
//        given
        Blind blind = Blind.builder().name("test").firstOutput(firstOutput).secondOutput(secondOutput).firstInput(firstInput).secondInput(secondInput).build();
//        when
        when(secondInput.isOpen()).thenReturn(true, false);
        blind.pullUp();
//        then
        assertBlindPulledUp();
    }

    private void assertBlindPulledDown() {
        verify(firstOutput, times(2)).setLow();
        verify(secondOutput, times(1)).setLow();
        verify(secondOutput, times(1)).setHigh();
    }

    private void assertBlindPulledUp() {
        verify(secondOutput, times(2)).setLow();
        verify(firstOutput, times(1)).setLow();
        verify(firstOutput, times(1)).setHigh();
    }

}