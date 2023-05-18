package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class YAK_1B extends YAK implements TypeTNBFighter {

    public YAK_1B() {
        this.flapps = 0.0F;
    }

    public float getEyeLevelCorrection() {
        return 0.1F;
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("Wind_luk", 0.0F, f1 * 19F, 0.0F);
            this.hierMesh().chunkSetAngles("Water_luk", 0.0F, f1 * 15F, 0.0F);
        }
        super.update(f);
    }

    public void update_windluk(float f) {
        this.update(f);
    }

    private float flapps;

    static {
        Class class1 = YAK_1B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-1B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_fr", "3DO/Plane/Yak-1B(fr)/hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1942.6F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-1B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_1.class });
        Property.set(class1, "LOSElevation", 0.6759F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
