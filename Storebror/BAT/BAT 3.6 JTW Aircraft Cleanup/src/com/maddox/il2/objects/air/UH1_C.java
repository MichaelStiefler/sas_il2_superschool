package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class UH1_C extends HueyX implements TypeScout, TypeTransport, TypeStormovik {

    public UH1_C() {
        this.hoverThrustFactor1 = 1.1F;
        this.hoverThrustFactor2 = 1.03F;
        this.numTurrets = 3;
    }

    public void computeMass() {
        this.FM.M.massEmpty = Aircraft.cvt(this.FM.getSpeedKMH(), 100F, 180F, 2200F, 3000F);
    }

    static {
        Class class1 = UH1_C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Huey_UH-1C");
        Property.set(class1, "meshName", "3DO/Plane/UH1_C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "FlightModel", "FlightModels/UH-1C.fmd:Huey_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHuey1B.class, CockpitUH1_CGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 9, 9, 9, 12, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_MGUN03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46",
                "_ExternalRock47", "_ExternalRock48" });
    }
}
