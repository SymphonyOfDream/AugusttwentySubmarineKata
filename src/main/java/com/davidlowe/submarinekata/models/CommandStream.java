package com.davidlowe.submarinekata.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * CommandStream provides the flexibility to use either a file for streaming Commands to the application in batch mode,
 * or the user's Terminal to send Command objects to the application in interactive mode.
 */
@Slf4j
@Getter
@Component
public class CommandStream
{
    private BufferedReader commandReader;

    /**
     * Sets commandReader to either an InputStreamReader or FileReader depending on whether the
     * 'fqInputFilename' value is not blank.
     *
     * @param commandFile If not null, creates 'commandReader' as a buffered FileReader using the specified filename.
     *                    If blank, creates 'commandReader' as a buffered InputStreamReader.
     *
     * @throws FileNotFoundException Thrown if 'commandFile' does not exist, or is not available for reading.
     */
    public void setConfigValue(File commandFile)
            throws FileNotFoundException
    {
        if (commandFile != null)
        {
            try
            {
                this.commandReader = new BufferedReader(new FileReader(commandFile));
            }
            catch (FileNotFoundException e)
            {
                val msg = "File \"%s\" is not available for reading.".formatted(commandFile.getAbsolutePath());
                log.error(msg, e);
                throw e;
            }
        }
        else
        {
            this.commandReader = new BufferedReader(new InputStreamReader(System.in));
        }
    }


    /**
     * Reads the next line from the active input stream (InputStreamReader or FileReader).
     *
     * @return The next line from the input, or null if end of stream is reached.
     * @throws IOException Thrown if an I/O error occurs.
     */
    public String readLine()
            throws IOException
    {
        return commandReader.readLine();
    }

}
