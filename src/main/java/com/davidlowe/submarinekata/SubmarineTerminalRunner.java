package com.davidlowe.submarinekata;

import com.davidlowe.submarinekata.models.CommandProcessor;
import com.davidlowe.submarinekata.models.CommandStream;
import com.davidlowe.submarinekata.models.Location;
import com.davidlowe.submarinekata.models.Submarine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * CommandLineRunner that executes if the 'terminal' Spring profile is active.
 * This code executes as a terminal application and can run either in Interactive or Batch mode, depending on
 * the run's command line parameters.
 * If running in Interactive mode, users interact directly with the application via terminal inputs and outputs.
 * If running in Batch mode, the application will read commands from a file and process them in sequence, with all outputs
 * going to the user's terminal.
 */
@Slf4j
@Component
@Profile("terminal")
@RequiredArgsConstructor
public class SubmarineTerminalRunner implements CommandLineRunner
{
    private static final String HORIZONTAL_START_LONG_OPTION = "horizontal-start";
    private static final String HORIZONTAL_START_SHORT_OPTION = "hs";

    private static final String DEPTH_START_LONG_OPTION = "depth-start";
    private static final String DEPTH_START_SHORT_OPTION = "ds";

    private static final String COMMAND_FILE_LONG_OPTION = "command-file";
    private static final String COMMAND_FILE_SHORT_OPTION = "cf";

    private static final String HELP_LONG_OPTION = "help";
    private static final String HELP_SHORT_OPTION = "h";

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

        applicationCliOptions.addOption(HORIZONTAL_START_SHORT_OPTION, HORIZONTAL_START_LONG_OPTION, true, "Submarine's starting Horizontal location (meters).");
        applicationCliOptions.addOption(DEPTH_START_SHORT_OPTION, DEPTH_START_LONG_OPTION, true, "Submarine's starting Depth (meters).");
        applicationCliOptions.addOption(COMMAND_FILE_SHORT_OPTION, COMMAND_FILE_LONG_OPTION, true, "The fully-qualified filename of a command file to steer the submarine in batch mode. If not specified, program will run in interactive mode.");
        applicationCliOptions.addOption(HELP_SHORT_OPTION, HELP_LONG_OPTION, false, "Display help information");
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

        try
        {
            helpFormatter.printHelp("SubmarineKata-0.0.1-SNAPSHOT", "", applicationCliOptions, "", true);
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
        if (ArrayUtils.isEmpty(args) || ArrayUtils.contains(args, "-h") || ArrayUtils.contains(args, "--help"))
        {
            printHelp();
            return;
        }

        double horizontalStart;
        double depthStart;
        String commandFilename;

        try
        {
            val commandLine = new DefaultParser().parse(applicationCliOptions, args);

            horizontalStart = commandLine.getParsedOptionValue(HORIZONTAL_START_LONG_OPTION, 0.0);
            depthStart = commandLine.getParsedOptionValue(DEPTH_START_LONG_OPTION, 0.0);
            commandFilename = commandLine.getParsedOptionValue(COMMAND_FILE_LONG_OPTION, "");
        }
        catch (Exception e)
        {
            log.error("!!!Error attempting to process command line arguments the application.", e);
            return;
        }

        submarineLocation.setConfigValue(horizontalStart, depthStart);

        File commandFile = null;
        if (StringUtils.isNotBlank(commandFilename))
        {
            commandFile = new File(commandFilename);
            if (!commandFile.exists())
            {
                log.error("File not found: {}", commandFilename);
                return;
            }

            log.info("Batch mode using file \"{}\".", commandFilename);
        }
        else
        {
            log.info("Interactive mode. Please enter your commands, pressing ENTER after each command. To exit, enter 'q' or 'Q' and press ENTER.");
        }

        commandStreamBean.setConfigValue(commandFile);

        log.info("Starting command reader.");
        val commandsRead = commandProcessor.start().get();
        log.info("Finished reading commands. Read {} commands.", commandsRead);

        val subFinalLocation = submarine.getCurrentLocation();
        log.info("RUN COMPLETE");
        log.info("Submarine final location: {}", subFinalLocation);
        log.info("Total distance from origin point to final location: {}", subFinalLocation.getHorizontalLocation() * subFinalLocation.getDepth());
    }
}
