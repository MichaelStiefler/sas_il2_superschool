package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HellcatMkIb extends F6F {

    public HellcatMkIb() {
        this.flapps = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 8; i++) {
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);
            }

        }
    }

    private float flapps;

    static {
        Class class1 = HellcatMkIb.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F6F");
        Property.set(class1, "meshName", "3DO/Plane/HellcatMkIb(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F6F-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF6F3.class });
        Property.set(class1, "LOSElevation", 1.16055F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 3, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalBomb03" });
    }
}
