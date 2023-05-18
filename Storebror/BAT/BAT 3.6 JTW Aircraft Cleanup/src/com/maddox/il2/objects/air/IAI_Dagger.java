package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.rts.Property;

public class IAI_Dagger extends MIRAGE implements TypeGuidedMissileCarrier, TypeThreatDetector, TypeGSuit, TypeAcePlane {

    public IAI_Dagger() {
    }

    public void update(float f) {
        this.computeATAR9B_AB();
        super.update(f);
    }

    public void computeATAR9B_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 12200D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 19F) {
                f1 = 12F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                float f5 = f4 * f;
                f1 = ((((-0.000219756F * f5) + (0.0108394F * f4)) - (0.160409F * f3)) + (0.726606F * f2)) - (0.974738F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "Dagger_";
    }

    static {
        Class class1 = IAI_Dagger.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dagger");
        Property.set(class1, "meshName", "3DO/Plane/Nesher/Nesher.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1959F);
        Property.set(class1, "yearExpired", 1980F);
        Property.set(class1, "FlightModel", "FlightModels/IAI-Nesher.fmd:MIRAGE_FM");
        Property.set(class1, "LOSElevation", 0.725F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIRAGE.class, CockpitMIRAGE_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_Rock01", "_Rock01", "_Rock02", "_Rock02", "_Rock03", "_Rock03", "_Rock04", "_Rock04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_Bomb05", "_Bomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_Bomb11", "_Bomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_Bomb19", "_Bomb20", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_Bomb25", "_Bomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_Bomb31", "_Bomb32", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_Bomb39", "_Bomb40", "_ExternalBomb41" });
    }
}
