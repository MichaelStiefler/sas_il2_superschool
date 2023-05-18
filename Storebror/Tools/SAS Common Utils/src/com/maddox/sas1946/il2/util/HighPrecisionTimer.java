package com.maddox.sas1946.il2.util;

/**
 * "HighPrecisionTimer" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide helper methods which can be used to measure time intervals of less than 1 millisecond.
 * <p>
 * 
 * @version 1.1.2
 * @since 1.1.2
 * @author SAS~Storebror
 */
public class HighPrecisionTimer {

    /**
     * Default Constructor
     * Automatically initializes the High Precision Timer
     * @since 1.1.2
     */
    public HighPrecisionTimer() {
        this.initTimer();
    }
    
    /**
     * Initializes / Resets the High Precision Timer
     * @since 1.1.2
     */
    public final void initTimer() {
        this.doInitTimer();
    }
   
    /**
     * Returns the seconds elapsed since last Initialization of the High Precision Timer.
     * @return Seconds (and fractions thereof) elapsed, as double.
     * @since 1.1.2
     */
    public final double getSecondsElapsed() {
        return this.doGetSecondsElapsed();
    }
    
    // *****************************************************************************************************************************************************************************************************
    // Private implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private final void doInitTimer() {
        this.start = getCounter();
    }
    
    private final double doGetSecondsElapsed() {
        long frequency = getFrequency();
        if (frequency == 0L) return -1D;
        return (double)(getCounter() - this.start) / (double)frequency;
    }

    private final static long getFrequency() {
        if (!loadNative()) return 0L;
        return queryPerformanceFrequency();
    }
    private final static long getCounter() {
        if (!loadNative()) return 0L;
        return queryPerformanceCounter();
    }
    
    private long start;
    
    // *****************************************************************************************************************************************************************************************************
    // Native Methods implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private final static boolean loadNative() {
        if (nativeLoaded) return true;
        if (initAttemptDone) return false;
        initAttemptDone = true;
        try {
            System.loadLibrary("SAS Common Utils");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        nativeLoaded = true;
        return true;
    }

    private static boolean nativeLoaded = false;
    private static boolean initAttemptDone = false;
    private static native long queryPerformanceFrequency(); 
    private static native long queryPerformanceCounter();
}
