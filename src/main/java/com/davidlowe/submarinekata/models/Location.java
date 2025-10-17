package com.davidlowe.submarinekata.models;

import lombok.NonNull;

/**
 * Represents the current location of a submarine.
 */
public interface Location
{
    /**
     * Sets initial state for Location.
     *
     * @param horizontalLocation Initial horizontal location.
     * @param depth              Initial depth.
     * @param aim                Initial aim.
     */
    void setConfigValue(double horizontalLocation, double depth, double aim);

    /**
     * Potentially changes horizontal or depth based on 'command' (command distance could be 0, resulting in no change)..
     *
     * @param command Command to process.
     */
    void processCommand(@NonNull Command command);


    double getHorizontalLocation();

    double getDepth();

    double getAim();
}
