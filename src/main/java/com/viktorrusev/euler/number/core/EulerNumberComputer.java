package com.viktorrusev.euler.number.core;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import static com.viktorrusev.euler.number.core.Config.*;

public class EulerNumberComputer {
    static BigDecimal e = new BigDecimal(0); // final E
    static ReentrantLock lockE = new ReentrantLock(); // lock to prevent race conditions for writing / reading E

    static BigDecimal[] FULL_FACTORIALS; // fully computed factorials of a specific partition of the series (6*5*4*3*2*1)
    static BigDecimal[] PARTIAL_FACTORIALS; // partially computed factorials of a specific partition of the series (6*5*4)

    private final int PARTITION_LENGTH; // the size of the partition to be computed by each thread

    //    private ReentrantLock[] LOCKS; // the idea is to notify a specific slave thread by another slave thread (wait notify on lock objects)
    private ArrayList<PartialSumComputer> threads = new ArrayList<>(); // keep the threads so we can .join() them in the main thread

    public EulerNumberComputer() {
        PARTITION_LENGTH = NUMBER_OF_ELEMENTS / NUMBER_OF_THREADS;

//        LOCKS = new ReentrantLock[NUMBER_OF_THREADS];
        FULL_FACTORIALS = new BigDecimal[NUMBER_OF_THREADS];
        PARTIAL_FACTORIALS = new BigDecimal[NUMBER_OF_THREADS];

//        prepareLocks();
    }

//    private void prepareLocks() {
//        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
//            LOCKS[i] = new ReentrantLock();
//        }
//    }

    public BigDecimal getE() {
        return e;
    }

    /**
     * Spawn threads with provided indices of the first and the last elements to be computed by each
     */
    public void computeE() {
        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            int end = getLastElementOfPartition(i);

            PartialSumComputer thread = new PartialSumComputer(i * PARTITION_LENGTH, end, i);
            threads.add(thread);
            thread.start();
        }

        for (PartialSumComputer thread : threads) {
            try {
                thread.join(); // wait for all threads to finish
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Get the last index of the element from the series of the specific partition
     * @param i - index of the partition
     * @return the index of the last element of the series to be included in the computation
     */
    private int getLastElementOfPartition(int i) {
        int end = (i + 1) * PARTITION_LENGTH - 1;

        if (end + PARTITION_LENGTH + 1 > NUMBER_OF_ELEMENTS) {
            end = NUMBER_OF_ELEMENTS - 1;
        }

        return end;
    }

    /**
     * Compute the value of a specific from the series element
     * @param k - index of element
     * @param denominator - the value of the denominator
     * @return the real value of the element in floating point notation with precision of NUMBER_OF_DIGITS
     *  rounded to the closest even number
     */
    static BigDecimal computationalFunction(int k, BigDecimal denominator) {
        return BigDecimal.valueOf(2 * k + 1).divide(denominator, NUMBER_OF_DIGITS, RoundingMode.HALF_EVEN);
    }
}
