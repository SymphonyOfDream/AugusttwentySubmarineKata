package com.davidlowe.submarinekata.models;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.cli.ParseException;

import java.io.Serializable;

/**
 * Command represents a single command that will move a submarine.
 *
 * @param direction The direction the command wants the submarine to move.
 * @param distance  The distance the command wants the submarine to move.
 */
@Slf4j
public record Command(@NonNull Direction direction, double distance) implements Serializable
{
    /**
     * Creates a Command object from a space-delimited string.
     *
     * @param commandString The string used to obtain the command's state from.
     *
     * @return Command object.
     * @throws ParseException Thrown if 'commandString' is not properly formatted.
     */
    public static @NonNull Command create(@NonNull String commandString)
            throws ParseException
    {
        val invalidErrMsgTemplate = "Invalid command \"%s\"";

        String[] parts = commandString.split(" ");
        if (parts.length != 2)
        {
            val msg = invalidErrMsgTemplate.formatted(commandString);
            log.warn(msg);
            throw new ParseException(msg);
        }

        val direction = Direction.fromString(parts[0]);
        if (direction == null)
        {
            val msg = invalidErrMsgTemplate.formatted(commandString);
            log.error(msg);
            throw new ParseException(msg);
        }

        double distance;
        try
        {
            distance = Double.parseDouble(parts[1]);
        }
        catch (NumberFormatException e)
        {
            val msg = invalidErrMsgTemplate.formatted(commandString);
            log.error(msg, e);
            throw new ParseException(msg);
        }

        val newCommand = new Command(direction, distance);
        log.info("Created command: {}.", newCommand);

        return newCommand;
    }
}

