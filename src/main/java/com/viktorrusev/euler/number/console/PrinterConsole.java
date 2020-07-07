package com.viktorrusev.euler.number.console;

import com.viktorrusev.euler.number.core.Config;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static com.viktorrusev.euler.number.console.EulerNumberComputerConsole.runtimeCalculator;
import static com.viktorrusev.euler.number.console.EulerNumberComputerConsole.eulerNumberComputer;

class PrinterConsole {
    private final String PRINT_MESSAGE = "The value of the Euler's number is %s";

    void printSummary() {
        printSeparator();
        printEulerNumber();
        writeEulerNumberToFile();

        if (!Config.QUIET_MODE_STATE) {
            printSeparator();
            printDigitsPrinted();
            printThreadsUsed();
            printElementsCalculated();
            printTotalTime();
        }
    }

    private void printSeparator() {
        System.out.println();
    }

    private void printDigitsPrinted() {
        System.out.println("Printed " + Config.NUMBER_OF_DIGITS + " digits.");
    }

    private void printEulerNumber() {
        System.out.println(String.format(PRINT_MESSAGE, eulerNumberComputer.getE()));
    }

    private void printThreadsUsed() {
        System.out.println("Used " + Config.NUMBER_OF_THREADS + " threads.");
    }

    private void printElementsCalculated() {
        System.out.println("Did " + Config.NUMBER_OF_ELEMENTS + " iterations.");
    }

    private void printTotalTime() {
        System.out.println("The total execution time was " + runtimeCalculator.getRuntimeInSeconds() + " seconds.");
    }

    private void writeEulerNumberToFile() {
        try (PrintWriter writer = new PrintWriter(Config.OUTPUT_FILE, "UTF-8")) {
            writer.println(String.format(PRINT_MESSAGE, eulerNumberComputer.getE()));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.err.println("Could not create the output file.");
        }
    }
}