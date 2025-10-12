package com.davidlowe.submarinekata;

import com.davidlowe.submarinekata.models.CommandProcessor;
import com.davidlowe.submarinekata.models.CommandStream;
import com.davidlowe.submarinekata.models.Location;
import com.davidlowe.submarinekata.models.Submarine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Code run if Spring profile 'terminal' is active. Runs as a terminal application with inputs/outputs on the user's terminal,
 * unless running in batch mode, in which case outputs will run on the user's terminal, but inputs will come from a file at some
 * accessible location for the user.
 */
@Slf4j
@Component
@Profile("terminal")
@RequiredArgsConstructor
public class SubmarineTerminalRunner implements CommandLineRunner
{
    private static final String HORIZONTAL_START_OPTION = "horizontal-start";
    private static final String DEPTH_START_OPTION = "depth-start";
    private static final String COMMAND_FILE_OPTION = "command-file";
    private static final String HELP_SHORT_OPTION = "?";
    private static final String HELP_LONG_OPTION = "help";

    private static Options applicationCliOptions;

    private final CommandStream commandStreamBean;
    private final CommandProcessor commandProcessor;
    private final Location submarineLocation;
    private final Submarine submarine;


    /**
     * Sets up the org.apache.commons.cli.Options object with all the terminal application's
     * allowed command-line parameters.
     */
    private static void createApplicationCliOptions()
    {
        applicationCliOptions = new Options();

        val horizontalStartOption = new Option(HORIZONTAL_START_OPTION, true, "Submarine's starting Horizontal location (meters).");
        val depthStartOption = new Option(DEPTH_START_OPTION, true, "Submarine's starting Depth (meters).");
        val commandFileOption = new Option(COMMAND_FILE_OPTION, true, "The fully-qualified filename of a command file to steer the submarine in batch mode. If not specified, program will run in interactive mode.");

        applicationCliOptions.addOption(horizontalStartOption);
        applicationCliOptions.addOption(depthStartOption);
        applicationCliOptions.addOption(commandFileOption);
        applicationCliOptions.addOption("?", "help", false, "Display help information");
    }


    /**
     * Outputs the terminal application's Help, showing the user all allowed command-line parameters.
     *
     * @throws IOException Thrown if HelpFormatter.printHelp(...) throws.
     */
    private static void printHelp()
            throws IOException
    {
        val helpFormatter = HelpFormatter.builder().get();

        val header = "This application allows you to move a submarine via giving it commands, either through Interactive mode where you give commands one at a time, or through Batch mode where you specify a file containing all commands to be run in sequential order.\n\n";
        val footer = "\nIf the command file is not specified, then the program runs in Interactive mode, otherwise it runs in Batch mode.";

        try
        {
            helpFormatter.printHelp("psql [%s 0] [%s 0] [%s c:\\data\\sub-commands.txt]".formatted(HORIZONTAL_START_OPTION, DEPTH_START_OPTION, COMMAND_FILE_OPTION),
                    header, applicationCliOptions, footer, true);
        }
        catch (IOException e)
        {
            log.error("Error attempting to print Help for the application.", e);
            throw e;
        }
    }


    /**
     * Entry point for the terminal application.
     * If in interactive mode, will continue running until user types 'q' or 'Q' in their terminal.
     * If in batch mode, will read each line of the file and process it, and when all lines have been processed, the run ends.
     *
     * @param args Command-line arguments for the terminal application.
     *
     * @throws Exception Any exception encountered will trickle up to this method and be emitted, ending the run.
     */
    @Override
    public void run(String... args)
            throws Exception
    {
        createApplicationCliOptions();

        double horizontalStart;
        double depthStart;
        String commandFile;

        try
        {
            val commandLine = new DefaultParser().parse(applicationCliOptions, args);

            if (commandLine.getParsedOptionValue(HELP_SHORT_OPTION, false)
                    || commandLine.getParsedOptionValue(HELP_LONG_OPTION, false))
            {
                printHelp();
                return;
            }

            horizontalStart = commandLine.getParsedOptionValue(HORIZONTAL_START_OPTION, 0.0);
            depthStart = commandLine.getParsedOptionValue(DEPTH_START_OPTION, 0.0);
            commandFile = commandLine.getParsedOptionValue(COMMAND_FILE_OPTION, "");
        }
        catch (Exception e)
        {
            log.error("!!!Error attempting to process command line arguments the application.", e);
            return;
        }

        if (StringUtils.isBlank(commandFile))
            log.info("Interactive mode. Please enter your commands. To exit, enter [q|Q].");
        else
            log.info("Batch mode using file \"{}\".", commandFile);

        commandStreamBean.setConfigValue(commandFile);

        submarineLocation.setConfigValue(horizontalStart, depthStart);

        log.info("Starting command reader.");
        val commandsRead = commandProcessor.start().get();
        log.info("Finished reading commands. Read {} commands.", commandsRead);

        val subFinalLocation = submarine.getCurrentLocation();
        log.info("RUN COMPLETE");
        log.info("Submarine final location: {}", subFinalLocation);
        log.info("Total distance from origin point to final location: {}", subFinalLocation.getHorizontalLocation() * subFinalLocation.getDepth());
    }
}
