package com.davidlowe.submarinekata.models;

import lombok.val;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandAvailableEventTest
{
    @Test
    void testEventCreation()
    {
        val source = new Object();
        val command = new Command(Direction.FORWARD, Instancio.gen().doubles().get());

        val event = new CommandAvailableEvent(source, command);

        assertNotNull(event);
        assertEquals(source, event.getSource());
        assertEquals(command, event.getCommand());
    }

    @Test
    void testEventCreation_WithDifferentCommands()
    {
        val source = this;

        var distance = Instancio.gen().doubles().get();
        val forwardCommand = new Command(Direction.FORWARD, distance);
        val forwardEvent = new CommandAvailableEvent(source, forwardCommand);
        assertEquals(forwardCommand, forwardEvent.getCommand());
        assertEquals(Direction.FORWARD, forwardEvent.getCommand().direction());
        assertEquals(distance, forwardEvent.getCommand().distance());

        distance = Instancio.gen().doubles().get();
        val upCommand = new Command(Direction.UP, distance);
        val upEvent = new CommandAvailableEvent(source, upCommand);
        assertEquals(upCommand, upEvent.getCommand());
        assertEquals(Direction.UP, upEvent.getCommand().direction());
        assertEquals(distance, upEvent.getCommand().distance());

        distance = Instancio.gen().doubles().get();
        val downCommand = new Command(Direction.DOWN, distance);
        val downEvent = new CommandAvailableEvent(source, downCommand);
        assertEquals(downCommand, downEvent.getCommand());
        assertEquals(Direction.DOWN, downEvent.getCommand().direction());
        assertEquals(distance, downEvent.getCommand().distance());
    }

    @Test
    void testEventCreation_WithNullCommand_throws()
    {
        Object source = new Object();

        assertThrows(NullPointerException.class, () ->
        {
            new CommandAvailableEvent(source, null);
        });
    }

    @Test
    void testGetCommand()
    {
        val source = new Object();
        val command = new Command(Direction.DOWN, Instancio.gen().doubles().get());
        val event = new CommandAvailableEvent(source, command);

        val retrievedCommand = event.getCommand();

        assertSame(command, retrievedCommand);
    }

    @Test
    void testEventCreation_WithZeroDistance()
    {
        val source = new Object();
        val zeroDistance = 0.0;
        val command = new Command(Direction.FORWARD, zeroDistance);

        val event = new CommandAvailableEvent(source, command);

        assertEquals(zeroDistance, event.getCommand().distance());
    }

    @Test
    void testEventCreation_WithNegativeDistance()
    {
        val source = new Object();
        val distance = Instancio.gen().doubles().max(0.0 - Double.MIN_VALUE).get();
        val command = new Command(Direction.UP, distance);

        val event = new CommandAvailableEvent(source, command);

        assertEquals(distance, event.getCommand().distance());
    }

    @Test
    void testMultipleEventsWithSameSource()
    {
        val source = new Object();
        val command1 = new Command(Direction.FORWARD, Instancio.gen().doubles().get());
        val command2 = new Command(Direction.DOWN, Instancio.gen().doubles().get());

        val event1 = new CommandAvailableEvent(source, command1);
        val event2 = new CommandAvailableEvent(source, command2);

        assertSame(source, event1.getSource());
        assertSame(source, event2.getSource());
        assertNotSame(event1, event2);
        assertNotSame(event1.getCommand(), event2.getCommand());
    }

}
