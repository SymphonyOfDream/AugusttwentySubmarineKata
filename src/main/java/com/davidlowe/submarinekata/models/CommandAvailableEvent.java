package com.davidlowe.submarinekata.models;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;

/**
 * An ApplicationEvent when a new Command object has been obtained and is ready for processing
 * by clients interested in the event.
 */
@Getter
public class CommandAvailableEvent extends ApplicationEvent
{
    /**
     * The Command object that is ready for processing
     **/
    @NonNull
    private final Command command;

    public CommandAvailableEvent(Object source, @NonNull Command command)
    {
        super(source);
        this.command = command;
    }
}
