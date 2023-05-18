package com.maddox.sas1946.il2.util;

import java.security.SecureRandom;
import java.util.Random;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;

/**
 * "TrueRandom" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide "true" randomized numbers for IL-2 Sturmovik 1946, in opposite to the reproduceable "pseudo" random numbers being available in the stock game.
 * <p>
 * 
 * @version 1.1.0
 * @since 1.0.3
 * @author SAS~Storebror
 */
public class TrueRandom {
	/**
	 * Override default Constructor to avoid instanciation
	 * 
	 * @throws Exception
     * @since 1.0.4
	 */
	private TrueRandom() throws Exception {
		throw new Exception("Class com.maddox.sas1946.il2.util.TrueRandom cannot be instanciated!");
	}

	// *****************************************************************************************************************************************************************************************************
	// Public interface section.
	// Methods and Arguments are supposed to be final here.
	// Take care of encapsulation and don't modify methods or arguments declared on this interface
	// to ensure future backward compatibility
	// If you need a new method with the same name but different parameters or return types,
	// simply overload the given methods in this interface.
	// *****************************************************************************************************************************************************************************************************

	/**
	 * This method can turn the "pseudo" random behaviour of IL-2 into "real" random behaviour
	 * with a single line of code.<br>
	 * Just call this method anywhere in your code and from the moment when this line is
	 * processed, all random actions in IL-2 will be really randomized.
     * @since 1.0.4
	 */
	public final static void makeIl2TrueRandom() {
		long lTime = System.currentTimeMillis();
		SecureRandom secRandom = new SecureRandom();
		secRandom.setSeed(lTime);
		int saveCountAccess = World.cur().rnd.countAccess();
		World.cur().rnd = new RangeRandom(secRandom.nextLong());
		Reflection.setInt(World.cur().rnd, "countAccess", saveCountAccess);
        if (BaseGameVersion.is412orLater()) {
    		saveCountAccess = World.cur().rnd2.countAccess();
    		World.cur().rnd2 = new RangeRandom(secRandom.nextLong());
    		Reflection.setInt(World.cur().rnd2, "countAccess", saveCountAccess);
        }
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed int value from this random number generator's sequence.
	 * The general contract of nextInt is that one int value is pseudorandomly generated and returned.
	 * All 2<small><sup>32</sup></small> possible int values are produced with (approximately) equal probability.<br>
	 * 
	 * @return the next pseudorandom, uniformly distributed int value from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static int nextInt() {
		if (theRandom == null) initRandom();
		return theRandom.nextInt();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextInt is that one int value in the specified range is pseudorandomly generated and returned.
	 * All max possible int values are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Must be positive.
	 * @throws
	 *        IllegalArgumentException if max is not positive.
	 * @return a pseudorandom, uniformly distributed int value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static int nextInt(int max) {
		if (theRandom == null) initRandom();
		return theRandom.nextInt(max);
	}

	/**
	 * Returns a pseudorandom, uniformly distributed int value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextInt is that one int value in the specified range is pseudorandomly generated and returned.
	 * All max-min possible int values are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned. Must be larger than min.
	 * @throws
	 *        IllegalArgumentException if max-min is not positive.
	 * @return a pseudorandom, uniformly distributed int value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static int nextInt(int min, int max) {
		if (theRandom == null) initRandom();
		return theRandom.nextInt(max - min) + min;
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed long value from this random number generator's sequence.
	 * The general contract of nextLong is that one long value is pseudorandomly generated and returned.
	 * All 2<small><sup>64</sup></small> possible long values are produced with (approximately) equal probability.<br>
	 * 
	 * @return the next pseudorandom, uniformly distributed long value from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static long nextLong() {
		if (theRandom == null) initRandom();
		return theRandom.nextLong();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed long value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextLong is that one long value in the specified range is pseudorandomly generated and returned.
	 * All max possible long values are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Must be positive.
	 * @throws
	 *        IllegalArgumentException if max is not positive.
	 * @return a pseudorandom, uniformly distributed long value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static long nextLong(long max) {
		if (theRandom == null) initRandom();
		return theRandom.nextLong() % max;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed long value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextLong is that one long value in the specified range is pseudorandomly generated and returned.
	 * All max-min possible long values are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned. Must be larger than min.
	 * @throws
	 *        IllegalArgumentException if max-min is not positive.
	 * @return a pseudorandom, uniformly distributed long value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static long nextLong(long min, long max) {
		if (theRandom == null) initRandom();
		return (theRandom.nextLong() % (max - min)) + min;
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed float value between 0.0 and 1.0 from this random number generator's sequence.
	 * The general contract of nextFloat is that one float value, chosen (approximately) uniformly from the range 0.0f (inclusive) to 1.0f (exclusive), is pseudorandomly generated and returned.
	 * All 2<small><sup>24</sup></small> possible float values of the form m x 2<small><sup>-24</sup></small>, where m is a positive integer less than 2<small><sup>24</sup></small>, are produced with (approximately) equal
	 * probability.
	 * 
	 * @return the next pseudorandom, uniformly distributed float value between 0.0 and 1.0 from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static float nextFloat() {
		if (theRandom == null) initRandom();
		return theRandom.nextFloat();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed float value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextFloat is that one float value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>24</sup></small> possible float values between 0.0 and max are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Can be positive or negative.
	 * @return a pseudorandom, uniformly distributed float value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static float nextFloat(float max) {
		if (theRandom == null) initRandom();
		return theRandom.nextFloat() * max;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed float value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextFloat is that one float value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>24</sup></small> possible float values between min and max are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return a pseudorandom, uniformly distributed float value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static float nextFloat(float min, float max) {
		if (theRandom == null) initRandom();
		return theRandom.nextFloat() * (max - min) + min;
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
	 * The general contract of nextDouble is that one double value, chosen (approximately) uniformly from the range 0.0d (inclusive) to 1.0d (exclusive), is pseudorandomly generated and returned.
	 * All 2<small><sup>53</sup></small> possible double values of the form m x 2<small><sup>-53</sup></small>, where m is a positive integer less than 2<small><sup>53</sup></small>, are produced with (approximately) equal
	 * probability.
	 * 
	 * @return the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextDouble() {
		if (theRandom == null) initRandom();
		return theRandom.nextDouble();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed double value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextDouble is that one double value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>53</sup></small> possible double values between 0.0 and max are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Can be positive or negative.
	 * @return a pseudorandom, uniformly distributed double value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static double nextDouble(double max) {
		if (theRandom == null) initRandom();
		return theRandom.nextDouble() * max;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed double value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextDouble is that one double value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>53</sup></small> possible double values between min and max are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return a pseudorandom, uniformly distributed double value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static double nextDouble(double min, double max) {
		if (theRandom == null) initRandom();
		return theRandom.nextDouble() * (max - min) + min;
	}

	/**
	 * Returns the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.
	 * The general contract of nextGaussian is that one double value, chosen from (approximately) the usual normal distribution with mean 0.0 and standard deviation 1.0, is pseudorandomly generated and returned.
	 * 
	 * @return the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextGaussian() {
		if (theRandom == null) initRandom();
		return theRandom.nextGaussian();
	}

	/**
	 * Returns the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and the specified deviation from this random number generator's sequence.
	 * The general contract of nextGaussian is that one double value, chosen from (approximately) the usual normal distribution with mean 0.0 and the specified deviation, is pseudorandomly generated and returned.
	 * 
	 * @param deviation
	 *            the deviation for this Gaussian Distribution
	 * @return the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and the specified deviation from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextGaussian(double deviation) {
		if (theRandom == null) initRandom();
		return theRandom.nextGaussian() * deviation;
	}

	/**
	 * Returns the next pseudorandom, Gaussian ("normally") distributed double value with the specified mean and deviation from this random number generator's sequence.
	 * The general contract of nextGaussian is that one double value, chosen from (approximately) the usual normal distribution with the specified mean and deviation, is pseudorandomly generated and returned.
	 * 
	 * @param deviation
	 *            the deviation for this Gaussian Distribution
	 * @param center
	 *            the mean for this Gaussian Distribution
	 * @return the next pseudorandom, Gaussian ("normally") distributed double value with the specified mean and deviation from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextGaussian(double deviation, double center) {
		if (theRandom == null) initRandom();
		return theRandom.nextGaussian() * deviation + center;
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextDouble_Dome is that one double value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static double nextDouble_Dome(double min, double max) {
		if (theRandom == null) initRandom();
		double d1 = nextDouble();
		double d2 = nextDouble();
		double d3 = nextDouble();
		double d4 = (d1 + d2 + d3) / 3D;
		return min + (max - min) * d4;
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextFloat_Dome is that one float value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextFloat_Dome(float min, float max) {
		return (float) nextDouble_Dome(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextInt_Dome is that one int value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextInt_Dome(int min, int max) {
		return (int) nextDouble_Dome(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextLong_Dome is that one long value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextLong_Dome(long min, long max) {
		return (long) nextDouble_Dome(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextDouble_DomeInv is that one double value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static double nextDouble_DomeInv(double min, double max) {
		if (theRandom == null) initRandom();
		double d1 = nextDouble();
		double d2 = nextDouble();
		double d3 = nextDouble();
		double d4 = (d1 + d2 + d3) / 3D;
		if (d4 >= 0.5D) d4 -= 0.5D;
		else d4 += 0.5D;
		return min + (max - min) * d4;
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextFloat_DomeInv is that one float value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextFloat_DomeInv(float min, float max) {
		return (float) nextDouble_DomeInv(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextInt_DomeInv is that one int value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextInt_DomeInv(int min, int max) {
		return (int) nextDouble_DomeInv(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextLong_DomeInv is that one long value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextLong_DomeInv(long min, long max) {
		return (long) nextDouble_DomeInv(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed double value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextDouble_Fade is that one double value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed double value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static double nextDouble_Fade(double min, double max) {
		if (theRandom == null) initRandom();
		double d1 = nextDouble();
		double d2 = nextDouble();
		double d3 = nextDouble();
		double d4 = (d1 + d2 + d3) / 3D;
		if (d4 >= 0.5D) d4 -= 0.5D;
		else d4 += 0.5D;
		return min + (max - min) * d4;
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed float value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextFloat_Fade is that one float value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed float value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextFloat_Fade(float min, float max) {
		return (float) nextDouble_Fade(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed int value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextInt_Fade is that one int value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed int value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextInt_Fade(int min, int max) {
		return (int) nextDouble_Fade(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed long value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextLong_Fade is that one long value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed long value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextLong_Fade(long min, long max) {
		return (long) nextDouble_Fade(min, max);
	}
	
	// *****************************************************************************************************************************************************************************************************
	// Private implementation section.
	// Do whatever you like here but keep it private to this class.
	// *****************************************************************************************************************************************************************************************************

	private static Random theRandom = null;

	private static void initRandom() {
		long lTime = System.currentTimeMillis();
		SecureRandom secRandom = new SecureRandom();
		secRandom.setSeed(lTime);
		theRandom = new Random(secRandom.nextLong());
	}

}
