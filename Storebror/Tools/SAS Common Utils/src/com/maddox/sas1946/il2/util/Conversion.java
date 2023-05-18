package com.maddox.sas1946.il2.util;

/**
 * "Conversion" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide different type conversions which are not available in Java 1.3 by default.
 * <p>
 * 
 * @version 1.1.0
 * @since 1.0.3
 * @author SAS~Storebror
 */
public class Conversion {

	/**
	 * Override default Constructor to avoid instanciation
	 * @throws Exception
     * @since 1.0.4
	 */
	private Conversion() throws Exception{
        throw new Exception("Class com.maddox.sas1946.il2.util.Conversion cannot be instanciated!");
	}
	
	/**
	 * Generates a long value from two given int values.
	 * The first int parameter defines the "high" portion of the generated long value, the second int parameter defines the "low" portion of the generated long value
	 * @param highInt
	 *            the "high" portion of the generated long value
	 * @param lowInt
	 *            the "low" portion of the generated long value
	 * @return a long value generated from the given int values
     * @since 1.0.3
	 */
	public final static long longFromTwoInts(int highInt, int lowInt) {
		return (((long)highInt) << 32) | (lowInt & 0xffffffffL);
	}
	
	/**
	 * Extracts the "high" portion int value of the given long value
	 * @param theLong
	 *            the long value whichs "high" portion int value shall be extracted
	 * @return the "high" portion int value of the given long value
     * @since 1.0.3
	 */
	public final static int highIntFromLong(long theLong) {
		return (int)(theLong >> 32);
	}
	
	/**
	 * Extracts the "low" portion int value of the given long value
	 * @param theLong
	 *            the long value whichs "low" portion int value shall be extracted
	 * @return the "low" portion int value of the given long value
     * @since 1.0.3
	 */
	public final static int lowIntFromLong(long theLong) {
		return (int)theLong;
	}
}
