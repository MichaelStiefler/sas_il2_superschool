package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class RB_36F_early extends B_36X implements TypeBomber {

    static {
        Class class1 = RB_36F_early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "RB-36");
        Property.set(class1, "meshName", "3DO/Plane/B36_early/RBF.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1945.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/RB-36F_early.fmd");
        Property.set(class1, "AutopilotElevatorAboveReferenceAltitudeFactor", 0.00016F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitB36Jets.class, CockpitB36_Bombardier.class, CockpitB36_Radar.class, CockpitB36_AGunner.class, CockpitB36_NGunner.class, CockpitB36_T1Gunner.class, CockpitB36_T2Gunner.class, CockpitB36_T3Gunner.class, CockpitB36_T4Gunner.class, CockpitB36_T5Gunner.class, CockpitB36_T6Gunner.class });

        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_Rock01" });
    }
}
