package com.davidlowe.submarinekata.models;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DirectionTest
{
    @Test
    void testFromString_Forward()
    {
        val direction = Direction.fromString("forward");

        assertEquals(Direction.FORWARD, direction);
    }

    @Test
    void testFromString_Up()
    {
        val direction = Direction.fromString("up");

        assertEquals(Direction.UP, direction);
    }

    @Test
    void testFromString_Down()
    {
        val direction = Direction.fromString("down");

        assertEquals(Direction.DOWN, direction);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDirectionStringsForTest")
    void testFromString_InvalidDirection(String directionString)
    {
        val direction = Direction.fromString(directionString);

        assertNull(direction);
    }

    private static Stream<Arguments> provideInvalidDirectionStringsForTest()
    {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("\t"),
                Arguments.of("backward"),
                Arguments.of("Forward"),
                Arguments.of("FORWARD"),
                Arguments.of("Up"),
                Arguments.of("UP"),
                Arguments.of("Down"),
                Arguments.of("DOWN"),
                Arguments.of(" forward"),
                Arguments.of("forward ")
        );
    }


    @Test
    void testFromString()
    {
        val forward = Direction.fromString("forward");
        val up = Direction.fromString("up");
        val down = Direction.fromString("down");

        assertEquals(Direction.FORWARD, forward);
        assertEquals(Direction.UP, up);
        assertEquals(Direction.DOWN, down);
    }

}
