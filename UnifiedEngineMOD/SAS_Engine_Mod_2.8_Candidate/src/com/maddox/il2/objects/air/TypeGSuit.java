// Source File Name: TypeGSuit.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

public interface TypeGSuit {

	// <editor-fold defaultstate="collapsed" desc="GFactors Class Prototype">
	public class GFactors {
		// <editor-fold defaultstate="collapsed" desc="Constructor">

		public GFactors() {
		}

		public GFactors(float theNegGToleranceFactor, float theNegGTimeFactor, float theNegGRecoveryFactor, float thePosGToleranceFactor, float thePosGTimeFactor, float thePosGRecoveryFactor) {
			this.setGFactors(theNegGToleranceFactor, theNegGTimeFactor, theNegGRecoveryFactor, thePosGToleranceFactor, thePosGTimeFactor, thePosGRecoveryFactor);
		}

		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Private fields">
		/**
		 * Tolerance factor for negative G-Forces.
		 * Default IL-2 implementation lets G-Forces greater than 2.3G, regardless
		 * whether negative or positive. This factor applies as a multiplicator
		 * to the regarding G Limit.
		 */
		private float fNegGToleranceFactor = 1.0F,
		/**
		 * Time factor for negatice G-Forces.
		 * IL-2 calculates a default time of 4 seconds @ max. G-Limit (default: 2.3G)
		 * until a redout/blackout occurs. Higher G-Stress shortens that time.
		 * This factor applies as a multiplier to this calculative factor, the higher
		 * the factor, the longer it takes to have a redout/blackout.
		 */
		fNegGTimeFactor = 1.0F,
		/**
		 * Recovery factor for negative G-Forces.
		 * When the G-Force is back in limits, IL-2 kind of fills buffers for
		 * G-Tolerance and timeouts again. This is also affected by the current
		 * G-Force, e.g. when a pilot is about to black out and then performs
		 * negative G Manoeuvres, the buffers will be filled quicker, whereas
		 * when he still pulls more than 1G but stays within G Limits, the buffer
		 * is filled, but slower than normal.
		 * 
		 * This factor applies as a multiplicator to the buffer fill rate, both
		 * for the time factor and for refilling blood to remove the effects
		 * of redout/blackout.
		 * The higher this value, the quicker a redout/blackout will vanish, and the
		 * quicker a pilot can withstand high G-Forces afterwards again.
		 */
		fNegGRecoveryFactor = 1.0F,
		/**
		 * Same parameters as before, but for positive G-Forces.
		 */
		fPosGToleranceFactor = 1.0F, fPosGTimeFactor = 1.0F, fPosGRecoveryFactor = 1.0F;

		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Parameter Get Methods">

		public float[] getGFactors() {
			float[] fGFactors = new float[6];
			fGFactors[0] = this.fNegGToleranceFactor;
			fGFactors[1] = this.fNegGTimeFactor;
			fGFactors[2] = this.fNegGRecoveryFactor;
			fGFactors[3] = this.fPosGToleranceFactor;
			fGFactors[4] = this.fPosGTimeFactor;
			fGFactors[5] = this.fPosGRecoveryFactor;
			return fGFactors;
		}

		public float getNegGToleranceFactor() {
			return this.fNegGRecoveryFactor;
		}

		public float getNegGTimeFactor() {
			return this.fNegGTimeFactor;
		}

		public float getNegGRecoveryFactor() {
			return this.fNegGRecoveryFactor;
		}

		public float getPosGToleranceFactor() {
			return this.fPosGToleranceFactor;
		}

		public float getPosGTimeFactor() {
			return this.fPosGTimeFactor;
		}

		public float getPosGRecoveryFactor() {
			return this.fPosGRecoveryFactor;
		}

		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Parameter Set Methods">

		public final void setGFactors(float theNegGToleranceFactor, float theNegGTimeFactor, float theNegGRecoveryFactor, float thePosGToleranceFactor, float thePosGTimeFactor, float thePosGRecoveryFactor) {
			this.fNegGToleranceFactor = theNegGToleranceFactor;
			this.fNegGTimeFactor = theNegGTimeFactor;
			this.fNegGRecoveryFactor = theNegGRecoveryFactor;
			this.fPosGToleranceFactor = thePosGToleranceFactor;
			this.fPosGTimeFactor = thePosGTimeFactor;
			this.fPosGRecoveryFactor = thePosGRecoveryFactor;
		}

		public void setNegGToleranceFactor(float theNegGToleranceFactor) {
			this.fNegGRecoveryFactor = theNegGToleranceFactor;
		}

		public void setNegGTimeFactor(float theNegGTimeFactor) {
			this.fNegGTimeFactor = theNegGTimeFactor;
		}

		public void setNegGRecoveryFactor(float theNegGRecoveryFactor) {
			this.fNegGRecoveryFactor = theNegGRecoveryFactor;
		}

		public void setPosGToleranceFactor(float thePosGToleranceFactor) {
			this.fPosGToleranceFactor = thePosGToleranceFactor;
		}

		public void setPosGTimeFactor(float thePosGTimeFactor) {
			this.fPosGTimeFactor = thePosGTimeFactor;
		}

		public void setPosGRecoveryFactor(float thePosGRecoveryFactor) {
			this.fPosGRecoveryFactor = thePosGRecoveryFactor;
		}
		// </editor-fold>
	}

	// </editor-fold>

	public abstract void getGFactors(GFactors theGFactors);
}