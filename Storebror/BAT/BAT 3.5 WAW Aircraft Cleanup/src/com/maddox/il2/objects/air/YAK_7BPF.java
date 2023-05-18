package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class YAK_7BPF extends YAK implements TypeTNBFighter {

    public YAK_7BPF() {
        this.flapps = 0.0F;
    }

    public float getEyeLevelCorrection() {
        return 0.05F;
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Math.max(-f * 1500F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Math.max(-f1 * 1500F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -82.5F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f1, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        YAK_7BPF.moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("Water_luk", 0.0F, 12F * f1, 0.0F);
            this.hierMesh().chunkSetAngles("Oil_luk", 0.0F, 24F * f1, 0.0F);
        }
        super.update(f);
    }

    private float flapps;

    static {
        Class class1 = YAK_7BPF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-7B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_fr", "3DO/Plane/Yak-7B(fr)/hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-7B_PF.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_7.class });
        Property.set(class1, "LOSElevation", 0.6116F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
    }
}
