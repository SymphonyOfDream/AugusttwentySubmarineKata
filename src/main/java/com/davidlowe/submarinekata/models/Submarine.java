package com.davidlowe.submarinekata.models;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The Submarine class represents the submarine itself.
 * Its current location is updated via event processing of CommandAvailableEvent objects.
 */
@Getter
@RequiredArgsConstructor
@Component
public class Submarine
{
    private final Location currentLocation;


    /**
     * Processes a CommandAvailableEvent, which could change the submarine's current location.
     *
     * @param event Event containing a Command that was received.
     */
    @EventListener
    public void commandAvailable(@NonNull CommandAvailableEvent event)
    {
        currentLocation.processCommand(event.getCommand());
    }
}
