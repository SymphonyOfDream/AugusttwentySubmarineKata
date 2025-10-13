package com.davidlowe.submarinekata.models;

import com.davidlowe.submarinekata.RandomValueUtils;
import lombok.val;
import org.apache.commons.cli.ParseException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.DecimalFormat;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("provideInvalidCommandStringsForTest")
    void testCreateCommandFromInvalidString(String commandString)
    {
        Exception exception = assertThrows(Exception.class, () -> Command.create(commandString));

        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    private static Stream<Arguments> provideInvalidCommandStringsForTest()
    {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of("forward"),
                Arguments.of("forward 10 extra"),
                Arguments.of("backward 10"),
                Arguments.of("forward abc")
        );
    }

    @Test
    void testCommandConstructorThrowsNullPointerException_NullDirection()
    {
        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        assertThrows(NullPointerException.class, () -> new Command(null, distance));
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
