package com.maddox.il2.objects.air;

class BombDescriptor {

    public BombDescriptor(String s, double ad[]) {
        this.sBombName = s;
        this.dHeightCorrectionCoeffs = ad;
    }

    public double GetCorrectionCoeff(double d) {
        if (d <= dHeights[0]) return this.dHeightCorrectionCoeffs[0];
        if (d >= dHeights[nLastIndex]) return this.dHeightCorrectionCoeffs[nLastIndex];
        for (int i = 0; i <= nLastIndex; i++)
            if (dHeights[i] > d) return this.dHeightCorrectionCoeffs[i - 1] + (this.dHeightCorrectionCoeffs[i] - this.dHeightCorrectionCoeffs[i - 1]) * ((d - dHeights[i - 1]) / (dHeights[i] - dHeights[i - 1]));

        return 0.0D;
    }

    public String         sBombName;
    private double        dHeightCorrectionCoeffs[];
    private static double dHeights[] = { 500D, 1000D, 1500D, 2000D, 2500D, 3000D, 3500D, 4000D, 4500D, 5000D, 5500D, 6000D, 6500D, 7000D, 7500D, 8000D, 8500D };
    private static int    nLastIndex;

    static {
        nLastIndex = dHeights.length - 1;
    }
}
