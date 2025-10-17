package com.davidlowe.submarinekata.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Slf4j
@Getter
@Component
public class LocationImpl implements Location
{
    private double horizontalLocation;
    private double depth;
    private double aim;

    public void setConfigValue(double horizontalLocation, double depth, double aim)
    {
        this.horizontalLocation = horizontalLocation;
        this.depth = depth;
        this.aim = aim;
    }

    /**
     * Potentially changes horizontal or depth based on 'command' (command distance could be 0, resulting in no change)..
     *
     * @param command Command to process.
     */
    public void processCommand(@NonNull Command command)
    {
        log.info("Processing command {}", command);
        switch (command.direction())
        {
            case FORWARD -> executeForwardCommand(command.distance());
            case UP -> executeUpCommand(command.distance());
            case DOWN -> executeDownCommand(command.distance());
        }
        log.info("New location ({},{})", horizontalLocation, depth);
    }

    private void executeForwardCommand(double distance)
    {
        horizontalLocation += distance;
        depth += aim * distance;
    }

    private void executeUpCommand(double distance)
    {
        aim -= distance;
    }

    private void executeDownCommand(double distance)
    {
        aim += distance;
    }

    /**
     * @return Returns a string representation of the object, with any trailing '0' decimal digits stripped.
     */
    @Override
    public String toString()
    {
        val df = new DecimalFormat("0.#########");
        return String.format("Location (Horizontal: %s, Depth: %s, Aim: %s)", df.format(horizontalLocation), df.format(depth), df.format(aim));
    }
}
