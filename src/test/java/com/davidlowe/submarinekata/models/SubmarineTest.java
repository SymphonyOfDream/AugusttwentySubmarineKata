package com.davidlowe.submarinekata.models;

import com.davidlowe.submarinekata.RandomValueUtils;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubmarineTest
{
    private Location location;
    private Submarine submarine;

    @BeforeEach
    void setUp()
    {
        location = new Location();
        submarine = new Submarine(location);
    }

    @Test
    void testSubmarineCreation()
    {
        assertNotNull(submarine);
        assertNotNull(submarine.getCurrentLocation());
        assertEquals(location, submarine.getCurrentLocation());
    }

    @Test
    void testGetCurrentLocation()
    {
        val currentLocation = submarine.getCurrentLocation();

        assertNotNull(currentLocation);
        assertSame(location, currentLocation);
    }

    @Test
    void testCommandAvailable_Forward()
    {
        val zeroDistance = 0.0;
        location.setConfigValue(zeroDistance, zeroDistance);
        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command = new Command(Direction.FORWARD, distance);
        val event = new CommandAvailableEvent(this, command);

        submarine.commandAvailable(event);

        assertEquals(distance, location.getHorizontalLocation());
        assertEquals(zeroDistance, location.getDepth());
    }

    @Test
    void testCommandAvailable_Up()
    {
        val zeroDistance = 0.0;
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(zeroDistance, depth);

        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command = new Command(Direction.UP, distance);
        val event = new CommandAvailableEvent(this, command);

        submarine.commandAvailable(event);

        assertEquals(zeroDistance, location.getHorizontalLocation());
        assertEquals(depth - distance, location.getDepth());
    }

    @Test
    void testCommandAvailable_Down()
    {
        val zeroDistance = 0.0;
        location.setConfigValue(zeroDistance, zeroDistance);
        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command = new Command(Direction.DOWN, distance);
        val event = new CommandAvailableEvent(this, command);

        submarine.commandAvailable(event);

        assertEquals(zeroDistance, location.getHorizontalLocation());
        assertEquals(distance, location.getDepth());
    }

    @Test
    void testCommandAvailable_MultipleCommands()
    {
        val zeroDistance = 0.0;
        location.setConfigValue(zeroDistance, zeroDistance);

        val horizontalDistance1 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command1 = new Command(Direction.FORWARD, horizontalDistance1);
        val event1 = new CommandAvailableEvent(this, command1);
        submarine.commandAvailable(event1);

        assertEquals(horizontalDistance1, location.getHorizontalLocation());
        assertEquals(zeroDistance, location.getDepth());

        val depthDistance1 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command2 = new Command(Direction.DOWN, depthDistance1);
        val event2 = new CommandAvailableEvent(this, command2);
        submarine.commandAvailable(event2);

        assertEquals(horizontalDistance1, location.getHorizontalLocation());
        assertEquals(depthDistance1, location.getDepth());

        val horizontalDistance2 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command3 = new Command(Direction.FORWARD, horizontalDistance2);
        val event3 = new CommandAvailableEvent(this, command3);
        submarine.commandAvailable(event3);
        val horizontalDistanceSum1 = horizontalDistance1 + horizontalDistance2;
        assertEquals(horizontalDistanceSum1, location.getHorizontalLocation());
        assertEquals(depthDistance1, location.getDepth());

        val depthDistance2 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command4 = new Command(Direction.UP, depthDistance2);
        val event4 = new CommandAvailableEvent(this, command4);
        submarine.commandAvailable(event4);
        val depthDistanceSum1 = depthDistance1 - depthDistance2;
        assertEquals(horizontalDistanceSum1, location.getHorizontalLocation());
        assertEquals(depthDistanceSum1, location.getDepth());
    }

    @Test
    void testCommandAvailable_WithNullEvent()
    {
        assertThrows(NullPointerException.class, () ->
        {
            submarine.commandAvailable(null);
        });
    }

    @Test
    void testCommandAvailable_WithZeroDistance()
    {
        val horizontalLocation = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalLocation, depth);
        val command = new Command(Direction.FORWARD, 0.0);
        val event = new CommandAvailableEvent(this, command);

        submarine.commandAvailable(event);

        assertEquals(horizontalLocation, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
    }

    @Test
    void testCommandAvailable_WithNegativeDistance()
    {
        val horizontalLocation = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalLocation, depth);
        val distance = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        val command = new Command(Direction.FORWARD, distance);
        val event = new CommandAvailableEvent(this, command);

        submarine.commandAvailable(event);

        assertEquals(horizontalLocation + distance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
    }

    @Test
    void testSubmarineLocationIsShared()
    {
        val zero = 0.0;
        location.setConfigValue(zero, zero);
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command = new Command(Direction.FORWARD, horizontalDistance);
        val event = new CommandAvailableEvent(this, command);

        submarine.commandAvailable(event);

        // Verify the submarine's location reference is updated
        assertEquals(horizontalDistance, submarine.getCurrentLocation().getHorizontalLocation());
        assertEquals(zero, submarine.getCurrentLocation().getDepth());

        // Verify the original location object is also updated
        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(zero, location.getDepth());
    }
}
