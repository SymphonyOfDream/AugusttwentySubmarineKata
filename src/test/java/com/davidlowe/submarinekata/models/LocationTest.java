package com.davidlowe.submarinekata.models;

import com.davidlowe.submarinekata.RandomValueUtils;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest
{
    private Location location;

    @BeforeEach
    void setUp()
    {
        location = new Location();
    }

    @Test
    void testDefaultLocation()
    {
        assertEquals(0.0, location.getHorizontalLocation());
        assertEquals(0.0, location.getDepth());
        assertEquals(0.0, location.getAim());
    }

    @Test
    void testSetConfigValue()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim, location.getAim());
    }

    @Test
    void testSetConfigValue_NegativeValues()
    {
        val horizontalDistance = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        val depth = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        val aim = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim, location.getAim());
    }

    @Test
    void testSetConfigValue_ZeroValues()
    {
        val zeroDistance = 0.0;
        location.setConfigValue(zeroDistance, zeroDistance, zeroDistance);

        assertEquals(zeroDistance, location.getHorizontalLocation());
        assertEquals(zeroDistance, location.getDepth());
        assertEquals(zeroDistance, location.getAim());
    }

    @Test
    void testProcessCommand_Forward()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command = new Command(Direction.FORWARD, distance);

        location.processCommand(command);

        assertEquals(horizontalDistance + distance, location.getHorizontalLocation());
        assertEquals(depth + (aim * distance), location.getDepth());
        assertEquals(aim, location.getAim());
    }

    @Test
    void testProcessCommand_Up()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command = new Command(Direction.UP, distance);

        location.processCommand(command);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim - distance, location.getAim());
    }

    @Test
    void testProcessCommand_Down()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val distance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val command = new Command(Direction.DOWN, distance);

        location.processCommand(command);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim + distance, location.getAim());
    }

    @Test
    void testProcessCommand_ForwardWithZeroDistance()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val command = new Command(Direction.FORWARD, 0.0);

        location.processCommand(command);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim, location.getAim());
    }

    @Test
    void testProcessCommand_UpWithZeroDistance()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val command = new Command(Direction.UP, 0.0);

        location.processCommand(command);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim, location.getAim());
    }

    @Test
    void testProcessCommand_DownWithZeroDistance()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val command = new Command(Direction.DOWN, 0.0);

        location.processCommand(command);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim, location.getAim());
    }

    @Test
    void testProcessCommand_ForwardWithNegativeDistance()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val distance = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        val command = new Command(Direction.FORWARD, distance);

        location.processCommand(command);

        assertEquals(horizontalDistance + distance, location.getHorizontalLocation());
        assertEquals(depth + (aim * distance), location.getDepth());
        assertEquals(aim, location.getAim());
    }

    @Test
    void testProcessCommand_UpWithNegativeDistance()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val distance = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        val command = new Command(Direction.UP, distance);

        location.processCommand(command);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim - distance, location.getAim());
    }

    @Test
    void testProcessCommand_DownWithNegativeDistance()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        val distance = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        val command = new Command(Direction.DOWN, distance);

        location.processCommand(command);

        assertEquals(horizontalDistance, location.getHorizontalLocation());
        assertEquals(depth, location.getDepth());
        assertEquals(aim + distance, location.getAim());
    }


    @Test
    void testProcessCommand_MultipleCommands()
    {
        val zero = 0.0;
        location.setConfigValue(zero, zero, zero);

        // Forward: change horizontal position, depth goes += (aim * distance)
        val horizontalDistance1 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.processCommand(new Command(Direction.FORWARD, horizontalDistance1));
        assertEquals(horizontalDistance1, location.getHorizontalLocation());
        assertEquals(zero, location.getDepth());
        assertEquals(zero, location.getAim());

        // Down: aim goes += distance
        val depthDistance1 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.processCommand(new Command(Direction.DOWN, depthDistance1));
        assertEquals(horizontalDistance1, location.getHorizontalLocation());
        assertEquals(zero, location.getDepth());
        assertEquals(depthDistance1, location.getAim());

        // Forward: change horizontal position, depth goes += (aim * distance)
        val horizontalDistance2 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.processCommand(new Command(Direction.FORWARD, horizontalDistance2));
        val newHorizontalSum = horizontalDistance1 + horizontalDistance2;
        val newDepthSum = horizontalDistance2 * depthDistance1;
        assertEquals(newHorizontalSum, location.getHorizontalLocation());
        assertEquals(newDepthSum, location.getDepth());
        assertEquals(depthDistance1, location.getAim());

        // Up: aim goes -= distance
        val depthDistance2 = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.processCommand(new Command(Direction.UP, depthDistance2));
        assertEquals(newHorizontalSum, location.getHorizontalLocation());
        assertEquals(newDepthSum, location.getDepth());
        assertEquals(depthDistance1 - depthDistance2, location.getAim());
    }

    @Test
    void testProcessCommand_NullCommand()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);

        assertThrows(NullPointerException.class, () ->
        {
            location.processCommand(null);
        });
    }

    @Test
    void testLocationToString()
    {
        val horizontalDistance = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val depth = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        val aim = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        location.setConfigValue(horizontalDistance, depth, aim);
        val result = location.toString();

        assertNotNull(result);

        val df = new DecimalFormat("0.#########");
        val horizontalLocationResults = df.format(horizontalDistance);
        val depthLocationResults = df.format(depth);
        assertTrue(result.contains(horizontalLocationResults) || result.contains("horizontalLocation"));
        assertTrue(result.contains(depthLocationResults) || result.contains("depth"));
    }
}
