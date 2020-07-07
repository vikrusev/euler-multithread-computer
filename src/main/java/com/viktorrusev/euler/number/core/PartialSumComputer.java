package com.viktorrusev.euler.number.core;

import com.viktorrusev.euler.number.util.RuntimeCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.viktorrusev.euler.number.core.Config.*;
import static com.viktorrusev.euler.number.core.EulerNumberComputer.*;

/**
 * A computer evaluating a specific partition of the Euler's series
 */
public class PartialSumComputer extends Thread {
    private final int start; // first element's index for the thread
    private final int end; // last element's index for the thread
    private final int threadNumber; // id of the thread

    private BigDecimal partialSum; // the sum of
    private RuntimeCalculator runtimeCalculator;

    PartialSumComputer(int start, int end, int threadNumber) {
        this.start = start;
        this.end = end;
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
//            LOCKS[threadNumber].lock();
        setStartTime();
        printStartingMessage();
        computePartialFactorial();
        computeFullFactorial();
        addPartialSum();
        printEndMessage();
        setEndTime();
        printThreadRunningTime();
    }

    /**
     * Compute the sum of elements for the specific thread's partition and add it to the final e
     */
    private void addPartialSum() {
        computePartialSum();
        addPartialSumToE();
    }

    /**
     * Add partial sum to E
     */
    private synchronized void addPartialSumToE() {
        lockE.lock();
        e = e.add(partialSum);
        lockE.unlock();
    }

    /**
     * Compute the partial for the thread factorial (e.g. 6*5*4 = 120)
     */
    private void computePartialFactorial() {
        BigDecimal result = new BigDecimal(1);

        for (int i = 2 * end; i >= 2 * start - 1 && i > 0; --i) {
            result = result.multiply(BigDecimal.valueOf(i));
        }

        PARTIAL_FACTORIALS[threadNumber] = result;

        // the initial thread directly finds its full factorial as well
        if (threadNumber == 0) {
            FULL_FACTORIALS[threadNumber] = result;
        }
    }

    /**
     * Each thread waits for the previous to have calculated their parts of the factorials so it can just
     * multiply its own partial factorial with the previous
     */
    private void computeFullFactorial() {
        try {
            // the first tread should not wait any other thread
            if (threadNumber > 0) {
                // better option is to have wait() on Objects (ReentrantLocks) or .await() on Conditions
                // just wait until the previous full factorial has been calculated
                while (FULL_FACTORIALS[threadNumber - 1] == null) {
                    sleep(1);
                }

                // finally just multiply own partial factorial with previous full factorial
                FULL_FACTORIALS[threadNumber] = FULL_FACTORIALS[threadNumber - 1].multiply(PARTIAL_FACTORIALS[threadNumber]);
            }

            // the last thread should not notify any other thread
//                if (threadNumber < NUMBER_OF_THREADS - 1) {
//                    LOCKS[threadNumber].lock();
//                }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Calculates and adds to the partialSum of the threa the values of all elements to be calculated by a specific thread
     */
    private void computePartialSum() {
        partialSum = BigDecimal.ZERO;
        BigDecimal denominator = FULL_FACTORIALS[threadNumber];

        for (int i = end; i >= start; --i) {
            partialSum = partialSum.add(computationalFunction(i, denominator));

            if (i > 0) {
                denominator = denominator.divide(BigDecimal.valueOf(2 * i), NUMBER_OF_DIGITS, RoundingMode.HALF_EVEN);
                denominator = denominator.divide(BigDecimal.valueOf(2 * i - 1), NUMBER_OF_DIGITS, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * Print and runtime functions
     */
    private void printThreadRunningTime() {
        if (!QUIET_MODE_STATE) {
            System.out.println("Thread " + threadNumber + " execution time was " + runtimeCalculator.getRuntimeInSeconds() + " " + "seconds.");
        }
    }

    private void setEndTime() {
        runtimeCalculator.setEndTime(System.currentTimeMillis());
    }

    private void setStartTime() {
        runtimeCalculator = new RuntimeCalculator(System.currentTimeMillis());
    }

    private void printEndMessage() {
        if (!QUIET_MODE_STATE) {
            System.out.println("Thread " + threadNumber + " finished.");
        }
    }

    private void printStartingMessage() {
        if (!QUIET_MODE_STATE) {
            System.out.println("Thread " + threadNumber + " started.");
        }
    }
}