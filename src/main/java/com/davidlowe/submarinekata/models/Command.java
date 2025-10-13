package com.davidlowe.submarinekata.models;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

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
    private static void throwParseException(String commandString)
            throws ParseException
    {
        val msg = "Invalid command \"%s\"".formatted(commandString);
        log.warn(msg);
        throw new ParseException(msg);
    }

    /**
     * Creates a Command object from a space-delimited string.
     *
     * @param commandString The string used to obtain the command's state from.
     *
     * @return Command object.
     * @throws ParseException Thrown if 'commandString' is not properly formatted or null.
     */
    public static @NonNull Command create(String commandString)
            throws ParseException
    {
        if (StringUtils.isBlank(commandString))
            throwParseException(commandString);

        String[] parts = commandString.split(" ");
        if (parts.length != 2)
            throwParseException(commandString);

        val direction = Direction.fromString(parts[0]);
        if (direction == null)
            throwParseException(commandString);

        double distance = 0;
        try
        {
            distance = Double.parseDouble(parts[1]);
        }
        catch (NumberFormatException _)
        {
            throwParseException(commandString);
        }

        // If 'direction' is null, an exception is thrown above. Any IntelliJ/SonarQube warnings can be ignored.
        val newCommand = new Command(direction, distance);
        log.info("Created command: {}.", newCommand);

        return newCommand;
    }
}

