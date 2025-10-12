# Submarine Puzzle Kata - 1

## Goal

The goal of this assignment is to create an application that keeps track of a submarine's current location.

## Directing the Submarine

The submarine's location will potentially change with each command input into the system. The commands follow the following format:

```Command's Grammar
command: direction + " " + distance
direction: "forward" | "up" | "down"
distance: [sign] digits [decimal]
sign: "+" | "-"
digits: digit+
decimal: "." digit+
digit: "0".."9"
```

Where "distance" is meters.

Which equates to Regular Expression of

```
^(forward|up|down)\s+[+-]?\d+\.?\d*$
```

The submarine's location will be tracked via the tuple (horizontal location, depth).

The starting location of the submarine will be (0,0); however, this starting location may be overridden via a parameter during system initialization.

The command's distance will have the following effect to a command's direction component:

* forward
    * Positive numbers: Added to the submarine's current horizontal location.
    * Negative numbers: Subtracted from the submarine's current horizontal location.
* up
    * Positive numbers: Subtracted from to the submarine's current depth (sub moving towards surface).
    * Negative numbers: Added to the submarine's current depth (sub moving down from the surface).
* down:
    * Positive numbers: Added to the submarine's current depth (sub moving down from the surface).
    * Negative numbers: Subtracted from to the submarine's current depth (sub moving towards surface).

Initial implementation will not enforce min/max values for the submarines current location, nor will min/max values be enforced for the command's "distance."

Invalid commands--any combination of an unknown direction or invalid distance--results in a thrown exception and no change to the submarine's location.

## Starting Program

### Interactive mode

Interactive mode allows a user to send commands to the submarine one at a time, each command typed into the user's terminal and the ENTER key pressed on their keyboard.

The program will start in interactive mode when the following is entered into the terminal:

```
augusttwentysubarminekata [--horizontal-start 0 --depth-start 0]
```

If the *start command line parameters are not specified, those values will default to 0. Both can be specified, or only one can be specified, or none can be specified.

The program should then enter interactive mode where individual commands may be given, as described above.

The program will terminate when the user enters [q|Q] for the command.

Upon program termination, the submarine's current location is displayed in the terminal.

### Batch mode

Batch mode allows the user to create an input file containing all commands they want to submit to the program, with each line of the file containing a single command.

The program will start in batch mode when the following is entered into the terminal:

```
augusttwentysubarminekata [--horizontal-start 0 --depth-start 0] --command-file fully-qualified-input-file-path
```

If the *start command line parameters are not specified, those values will default to 0.

fully-qualified-input-file-path will be the drive+directory+filename of a text input file with each line of the file being a command, as described above.

**NOTE**: if the file is not specified, the program will go into interactive mode, as explained above.

Upon program termination, the submarine's current location is displayed in the terminal.



