package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class RC_135A extends C_135 {

    public RC_135A() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "RC-135A_";
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            for (int i = 0; i < 4; i++) {
                if ((this.FM.EI.engines[i].getPowerOutput() > 0.8F) && (this.FM.EI.engines[i].getStage() == 6)) {
                    if (this.FM.EI.engines[i].getPowerOutput() > 0.95F) {
                        this.FM.AS.setSootState(this, i, 3);
                    } else {
                        this.FM.AS.setSootState(this, i, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, i, 0);
                }
            }

        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = RC_135A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Stratotanker");
        Property.set(class1, "meshName", "3DO/Plane/C-135/hierC-135A.him");
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/C-135A.fmd:C135FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitC_135.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_InternalDev01", "_InternalDev02", "_InternalDev03", "_InternalDev04", "_InternalDev05", "_InternalDev06", "_InternalDev07", "_InternalDev08", "_InternalDev09", "_InternalDev10", "_InternalDev11", "_InternalDev12", "_InternalDev13", "_InternalDev14", "_InternalDev15", "_InternalDev16", "_InternalDev17", "_InternalDev18", "_InternalDev19", "_InternalDev20", "_InternalDev21", "_InternalDev22", "_InternalDev23", "_InternalDev24", "_InternalDev25", "_InternalDev26", "_InternalDev27", "_InternalDev28", "_InternalDev29", "_InternalDev30", "_InternalDev31", "_InternalDev32", "_InternalDev33", "_InternalDev34", "_InternalDev35", "_InternalDev36", "_InternalDev37", "_InternalDev38", "_InternalDev39", "_InternalDev40", "_InternalDev41", "_InternalDev42", "_InternalDev43", "_InternalDev44", "_InternalDev45", "_InternalDev46", "_InternalDev47", "_InternalDev48", "_InternalDev49", "_InternalDev50", "_InternalDev51", "_InternalDev52", "_InternalDev53",
                "_InternalDev54", "_InternalDev55", "_InternalDev56", "_InternalDev57", "_InternalDev58", "_InternalDev59", "_InternalDev60", "_InternalDev61", "_InternalDev62", "_InternalDev63", "_InternalDev64", "_InternalDev65", "_InternalDev66", "_InternalDev67", "_InternalDev68", "_InternalDev69", "_InternalDev70", "_InternalDev71", "_InternalDev72", "_InternalDev73", "_InternalDev74", "_InternalDev75", "_InternalDev76", "_InternalDev77", "_InternalDev78", "_InternalDev79", "_InternalDev80", "_InternalDev81", "_InternalDev82", "_InternalDev83", "_InternalDev84", "_InternalDev85", "_InternalDev86", "_InternalDev87", "_InternalDev88", "_InternalDev89", "_InternalDev90", "_InternalDev91", "_InternalDev92", "_InternalDev93", "_InternalDev94", "_InternalDev95", "_InternalDev96", "_InternalDev97", "_InternalDev98", "_InternalDev99", "_InternalDev100" });
    }
}
