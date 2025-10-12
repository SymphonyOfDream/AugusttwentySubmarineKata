This program will run in either:

1. Interactive mode
    * The user enters commands of ```"forward|up|down" n``` where n is a decimal or integer number.
    * When the user is finished entering commands, they enter "q|Q" to terminate the program.
2. Batch mode
    * User provides the fully qualified filename of the text file containing all commands to be run in sequential order.
    * Once the file has been fully processed, the program terminates.

To run make sure:

* Active spring profile is 'terminal'
* Command lines are provided as follows:
    * To run in batch mode, you must provide a fully qualified filename for the commands file, by entering a command line argument of:
        * ```-command-file "C:\data\input.txt"```
        * If you do not provide this command line argument, the program will start in Interactive mode.
    * To specify a starting horizontal location for your submarine, use a command line argument of:
        * ```-horizontal-start 10```
        * If you do not provide this command line argument, your submarine starting horizontal location will be 0.
    * To specify a starting depth for your submarine, use a command line argument of:
        * ```-depth-start 5```
        * If you do not provide this command line argument, your submarine starting depth will be 0.

When running within an IDE such as IntelliJ, Interactive mode requires you to enter your commands in the Output window amongst the logging lines.

