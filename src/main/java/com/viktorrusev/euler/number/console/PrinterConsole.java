package com.viktorrusev.euler.number.console;

import com.viktorrusev.euler.number.core.Config;

import java.io.*;

import static com.viktorrusev.euler.number.console.EulerNumberComputerConsole.runtimeCalculator;
import static com.viktorrusev.euler.number.console.EulerNumberComputerConsole.eulerNumberComputer;

class PrinterConsole {

    void printSummary() {
        printEulerNumber();

        if (!Config.QUIET_MODE_STATE) {
            printSeparator();
            printDigitsPrinted();
            printThreadsUsed();
            printElementsCalculated();
            printTotalTime();
        }

        printDashedSeparator();
    }

    private void printSeparator() {
        writeToFileAndConsole("");
    }

    private void printDashedSeparator() {
        writeToFileAndConsole("---------- ---------- ---------- ---------- ---------- ---------- ----------");
    }

    private void printDigitsPrinted() {
        writeToFileAndConsole("Printed " + Config.NUMBER_OF_DIGITS + " digits.");
    }

    private void printEulerNumber() {
        writeToFileAndConsole(String.format("The value of the Euler's number is %s", eulerNumberComputer.getE()));
    }

    private void printThreadsUsed() {
        writeToFileAndConsole("Used " + Config.NUMBER_OF_THREADS + " threads.");
    }

    private void printElementsCalculated() {
        writeToFileAndConsole("Did " + Config.NUMBER_OF_ELEMENTS + " iterations.");
    }

    private void printTotalTime() {
        writeToFileAndConsole("The total execution time was " + runtimeCalculator.getRuntimeInSeconds() + " seconds.");
    }

    private void writeToFileAndConsole(String text) {
        System.out.println(text);

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(Config.OUTPUT_FILE),true))) {
            writer.println(text);
        } catch (FileNotFoundException e) {
            System.err.println("Could not create the output file.");
        }
    }
}