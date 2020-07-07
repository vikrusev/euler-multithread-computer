package com.viktorrusev.euler.number.console;

import com.viktorrusev.euler.number.core.Config;
import com.viktorrusev.euler.number.core.Defaults;
import com.viktorrusev.euler.number.core.EulerNumberComputer;
import com.viktorrusev.euler.number.util.RuntimeCalculator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class EulerNumberComputerConsole {
    private String[] args;

    private PrinterConsole printer = new PrinterConsole();

    static EulerNumberComputer eulerNumberComputer;
    static RuntimeCalculator runtimeCalculator = new RuntimeCalculator(0);

    public EulerNumberComputerConsole(String[] args) {
        this.args = args;
    }

    public void run() {
        parseArgs();
        setStartTime();
        computeEulerNumber();
        setEndTime();
        printer.printSummary();
    }

    private void computeEulerNumber() {
        eulerNumberComputer = new EulerNumberComputer();

        eulerNumberComputer.computeE();
    }

    private void setStartTime() {
        runtimeCalculator.setStartTime(System.currentTimeMillis());
    }

    private void setEndTime() {
        runtimeCalculator.setEndTime(System.currentTimeMillis());
    }

    private void parseArgs() {
        DefaultParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("t", "tasks", true, "number of threads");
        options.addOption("p", true, "number of elements");
        options.addOption("d", true, "number of digits");
        options.addOption("q", false, "quiet mode");
        options.addOption("o", true, "output file");

        try {
            CommandLine cmd = parser.parse(options, args);

            Config.NUMBER_OF_THREADS = cmd.hasOption("t") ? Integer.parseInt(cmd.getOptionValue("t")) : Defaults.NUMBER_OF_THREADS;
            Config.NUMBER_OF_ELEMENTS = cmd.hasOption("p") ? Integer.parseInt(cmd.getOptionValue("p")) : Defaults.NUMBER_OF_ELEMENTS;
            Config.QUIET_MODE_STATE = cmd.hasOption("q") || Defaults.QUIET_MODE_STATE;
            Config.OUTPUT_FILE = cmd.hasOption("o") ? cmd.getOptionValue("o") : Defaults.OUTPUT_FILE;

            if (cmd.hasOption("d") && validNumberOfDigits(Integer.valueOf(cmd.getOptionValue("d")))) {
                Config.NUMBER_OF_DIGITS = Integer.valueOf(cmd.getOptionValue("d"));
            } else {
                Config.NUMBER_OF_DIGITS = Defaults.NUMBER_OF_DIGITS;
            }
        } catch (ParseException e) {
            System.err.println("Could not parse arguments.");
        }
    }

    private boolean validNumberOfDigits(int numberOfDigits) {
        return numberOfDigits >= 0;
    }
}
