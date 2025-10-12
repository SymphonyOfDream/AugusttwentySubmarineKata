package com.davidlowe.submarinekata.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@ToString
@Component
public class Location
{
    private double horizontalLocation;
    private double depth;

    public void setConfigValue(double horizontalLocation, double depth)
    {
        this.horizontalLocation = horizontalLocation;
        this.depth = depth;
    }


    public void processCommand(@NonNull Command command)
    {
        log.info("Processing command {}", command);
        switch (command.direction())
        {
            case FORWARD -> horizontalLocation += command.distance();
            case UP -> depth -= command.distance();
            case DOWN -> depth += command.distance();
        }
        log.info("New location ({},{})", horizontalLocation, depth);
    }
}
