package com.davidlowe.submarinekata.models;

import com.davidlowe.submarinekata.RandomValueUtils;
import lombok.val;
import org.apache.commons.cli.ParseException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest
{

    @Test
    void testCommandCreation()
    {
        val distance = Instancio.gen().doubles().get();
        Command command = new Command(Direction.FORWARD, distance);

        assertEquals(Direction.FORWARD, command.direction());
        assertEquals(distance, command.distance());
    }

    @Test
    void testCreateFromValidString_Forward() throws ParseException
    {
        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        Command command = Command.create("forward %f".formatted(distance));

        assertEquals(Direction.FORWARD, command.direction());
        assertEquals(distance, command.distance());
    }

    @Test
    void testCreateFromValidString_Up() throws ParseException
    {
        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        Command command = Command.create("up %f".formatted(distance));

        assertEquals(Direction.UP, command.direction());
        assertEquals(distance, command.distance());
    }

    @Test
    void testCreateFromValidString_Down() throws ParseException
    {
        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        Command command = Command.create("down %f".formatted(distance));

        assertEquals(Direction.DOWN, command.direction());
        assertEquals(distance, command.distance());
    }

    @Test
    void testCreateFromValidString_WithZeroDistance() throws ParseException
    {
        val zeroDistance = 0.0;
        Command command = Command.create("forward %f".formatted(zeroDistance));

        assertEquals(Direction.FORWARD, command.direction());
        assertEquals(zeroDistance, command.distance());
    }

    @Test
    void testCreateFromValidString_WithNegativeDistance() throws ParseException
    {
        val distance = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        Command command = Command.create("forward %f".formatted(distance));

        assertEquals(Direction.FORWARD, command.direction());
        assertEquals(distance, command.distance());
    }

    @Test
    void testCreateFromInvalidString_TooFewParts()
    {
        ParseException exception = assertThrows(ParseException.class, () ->
        {
            Command.create("forward");
        });

        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    @Test
    void testCreateFromInvalidString_TooManyParts()
    {
        ParseException exception = assertThrows(ParseException.class, () ->
        {
            Command.create("forward 10 extra");
        });

        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    @Test
    void testCreateFromInvalidString_InvalidDirection()
    {
        ParseException exception = assertThrows(ParseException.class, () ->
        {
            Command.create("backward 10");
        });

        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    @Test
    void testCreateFromInvalidString_InvalidDistance()
    {
        ParseException exception = assertThrows(ParseException.class, () ->
        {
            Command.create("forward abc");
        });

        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    @Test
    void testCreateFromInvalidString_EmptyString()
    {
        ParseException exception = assertThrows(ParseException.class, () ->
        {
            Command.create("");
        });

        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    @Test
    void testCreateThrowsNullPointerException_NullString()
    {
        assertThrows(NullPointerException.class, () ->
        {
            Command.create(null);
        });
    }

    @Test
    void testCommandConstructorThrowsNullPointerException_NullDirection()
    {
        assertThrows(NullPointerException.class, () ->
        {
            new Command(null, RandomValueUtils.randomPositiveSinglePrecisionDouble());
        });
    }

    @Test
    void testCommandToString()
    {
        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        Command command = new Command(Direction.FORWARD, distance);
        String result = command.toString();

        assertNotNull(result);
        assertTrue(result.contains("FORWARD"));

        val df = new DecimalFormat("0.#########");
        assertTrue(result.contains(df.format(distance)));
    }
}
