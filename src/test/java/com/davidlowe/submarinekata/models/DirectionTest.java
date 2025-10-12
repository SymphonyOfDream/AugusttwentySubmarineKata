package com.davidlowe.submarinekata.models;

import lombok.val;
import org.junit.jupiter.api.Test;

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

    @Test
    void testFromString_InvalidDirection()
    {
        val direction = Direction.fromString("backward");

        assertNull(direction);
    }

    @Test
    void testFromString_EmptyString()
    {
        val direction = Direction.fromString("");

        assertNull(direction);
    }

    @Test
    void testFromString_NullString()
    {
        val direction = Direction.fromString(null);

        assertNull(direction);
    }

    @Test
    void testFromString_WhitespaceString()
    {
        val direction = Direction.fromString("   ");

        assertNull(direction);
    }

    @Test
    void testFromString_CaseSensitive()
    {
        val direction = Direction.fromString("FORWARD");

        assertNull(direction);
    }

    @Test
    void testFromString_WithExtraSpaces()
    {
        val direction = Direction.fromString(" forward ");

        assertNull(direction);
    }

    @Test
    void testDirectionValueOf()
    {
        val forward = Direction.valueOf("FORWARD");
        val up = Direction.valueOf("UP");
        val down = Direction.valueOf("DOWN");

        assertEquals(Direction.FORWARD, forward);
        assertEquals(Direction.UP, up);
        assertEquals(Direction.DOWN, down);
    }

}
