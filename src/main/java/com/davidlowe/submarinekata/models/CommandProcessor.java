package com.davidlowe.submarinekata.models;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * This class will receive String inputs of commands and convert those to
 * Command objects, then publish those commands via CommandAvailableEvent events.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommandProcessor
{
    private final ApplicationEventPublisher eventPublisher;
    private final CommandStream commandStream;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicBoolean running = new AtomicBoolean(false);


    @SneakyThrows
    public Future<Integer> start()
    {
        return executor.submit(this::processStream);
    }


    public void stop()
    {
        running.set(false);
        executor.shutdownNow();
    }


    @SneakyThrows
    private Integer processStream()
    {
        running.set(true);
        int commandsRetrieved = 0;
        try
        {
            String commandString;
            while (running.get() && (commandString = commandStream.readLine()) != null && running.get())
            {
                if (commandString.equalsIgnoreCase("q"))
                    break;

                log.info("Received command from input stream: \"{}\"", commandString);
                val event = new CommandAvailableEvent(this, Command.create(commandString));
                ++commandsRetrieved;
                log.info("Publishing command \"{}\"", event.getCommand());
                eventPublisher.publishEvent(event);
            }
        }
        catch (Exception e)
        {
            log.error("Error while reading stream and creating command.", e);
            throw e;
        }

        return commandsRetrieved;
    }

}
