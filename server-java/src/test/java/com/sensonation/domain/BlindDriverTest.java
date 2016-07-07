package com.sensonation.domain;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BlindDriverTest {

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
        BlindDriver blindDriver = BlindDriver.builder().name("test").firstOutput(firstOutput).secondOutput(secondOutput).pullDownLimitSwitch(firstInput).pullUpLimitSwitch(secondInput).build();
//        when
        when(firstInput.isOpen()).thenReturn(true, false);
        blindDriver.pullDown();
//        then
        assertBlindPulledDown();
    }

    @Test
    public void should_pulled_up_using_outputs_and_input(){
//        given
        BlindDriver blindDriver = BlindDriver.builder().name("test").firstOutput(firstOutput).secondOutput(secondOutput).pullDownLimitSwitch(firstInput).pullUpLimitSwitch(secondInput).build();
//        when
        when(secondInput.isOpen()).thenReturn(true, false);
        blindDriver.pullUp();
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