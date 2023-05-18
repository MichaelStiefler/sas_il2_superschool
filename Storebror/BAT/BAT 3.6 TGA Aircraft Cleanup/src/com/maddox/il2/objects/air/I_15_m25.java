package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class I_15_m25 extends I_153_M62 {

    public I_15_m25() {
    }

    public float getEyeLevelCorrection() {
        return 0.1F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_15_m25.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_15_m25.bChangedPit = true;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 2.0F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 2.0F * f1, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        I_15_m25.moveGear(this.hierMesh(), f, f1, f2);
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = I_15_m25.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-15");
        Property.set(class1, "meshName", "3DO/Plane/I-15/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_15.class });
        Property.set(class1, "FlightModel", "FlightModels/I-15-M25.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
    }
}
