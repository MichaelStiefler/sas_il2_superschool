// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 28.04.2015 16:32:07
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TypeGSuit.java

package com.maddox.il2.objects.air;


public interface TypeGSuit
{
    public static class GFactors
    {

        public float[] getGFactors()
        {
            float fGFactors[] = new float[6];
            fGFactors[0] = fNegGToleranceFactor;
            fGFactors[1] = fNegGTimeFactor;
            fGFactors[2] = fNegGRecoveryFactor;
            fGFactors[3] = fPosGToleranceFactor;
            fGFactors[4] = fPosGTimeFactor;
            fGFactors[5] = fPosGRecoveryFactor;
            return fGFactors;
        }

        public float getNegGToleranceFactor()
        {
            return fNegGRecoveryFactor;
        }

        public float getNegGTimeFactor()
        {
            return fNegGTimeFactor;
        }

        public float getNegGRecoveryFactor()
        {
            return fNegGRecoveryFactor;
        }

        public float getPosGToleranceFactor()
        {
            return fPosGToleranceFactor;
        }

        public float getPosGTimeFactor()
        {
            return fPosGTimeFactor;
        }

        public float getPosGRecoveryFactor()
        {
            return fPosGRecoveryFactor;
        }

        public final void setGFactors(float theNegGToleranceFactor, float theNegGTimeFactor, float theNegGRecoveryFactor, float thePosGToleranceFactor, float thePosGTimeFactor, float thePosGRecoveryFactor)
        {
            fNegGToleranceFactor = theNegGToleranceFactor;
            fNegGTimeFactor = theNegGTimeFactor;
            fNegGRecoveryFactor = theNegGRecoveryFactor;
            fPosGToleranceFactor = thePosGToleranceFactor;
            fPosGTimeFactor = thePosGTimeFactor;
            fPosGRecoveryFactor = thePosGRecoveryFactor;
        }

        public void setNegGToleranceFactor(float theNegGToleranceFactor)
        {
            fNegGRecoveryFactor = theNegGToleranceFactor;
        }

        public void setNegGTimeFactor(float theNegGTimeFactor)
        {
            fNegGTimeFactor = theNegGTimeFactor;
        }

        public void setNegGRecoveryFactor(float theNegGRecoveryFactor)
        {
            fNegGRecoveryFactor = theNegGRecoveryFactor;
        }

        public void setPosGToleranceFactor(float thePosGToleranceFactor)
        {
            fPosGToleranceFactor = thePosGToleranceFactor;
        }

        public void setPosGTimeFactor(float thePosGTimeFactor)
        {
            fPosGTimeFactor = thePosGTimeFactor;
        }

        public void setPosGRecoveryFactor(float thePosGRecoveryFactor)
        {
            fPosGRecoveryFactor = thePosGRecoveryFactor;
        }

        private float fNegGToleranceFactor;
        private float fNegGTimeFactor;
        private float fNegGRecoveryFactor;
        private float fPosGToleranceFactor;
        private float fPosGTimeFactor;
        private float fPosGRecoveryFactor;

        public GFactors()
        {
            fNegGToleranceFactor = 1.0F;
            fNegGTimeFactor = 1.0F;
            fNegGRecoveryFactor = 1.0F;
            fPosGToleranceFactor = 1.0F;
            fPosGTimeFactor = 1.0F;
            fPosGRecoveryFactor = 1.0F;
        }

        public GFactors(float theNegGToleranceFactor, float theNegGTimeFactor, float theNegGRecoveryFactor, float thePosGToleranceFactor, float thePosGTimeFactor, float thePosGRecoveryFactor)
        {
            fNegGToleranceFactor = 1.0F;
            fNegGTimeFactor = 1.0F;
            fNegGRecoveryFactor = 1.0F;
            fPosGToleranceFactor = 1.0F;
            fPosGTimeFactor = 1.0F;
            fPosGRecoveryFactor = 1.0F;
            setGFactors(theNegGToleranceFactor, theNegGTimeFactor, theNegGRecoveryFactor, thePosGToleranceFactor, thePosGTimeFactor, thePosGRecoveryFactor);
        }
    }


    public abstract void getGFactors(GFactors gfactors);
}
