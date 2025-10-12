package com.davidlowe.submarinekata.models;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Getter
@RequiredArgsConstructor
@Component
public class Submarine
{
    private final Location currentLocation;


    /**
     * Move this submarine based on the Command we receive.
     *
     * @param event Event containing a Command that was received.
     */
    @EventListener
    public void commandAvailable(@NonNull CommandAvailableEvent event)
    {
        currentLocation.processCommand(event.getCommand());
    }
}
