package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class YAK_3 extends YAK {

    public YAK_3() {
        this.flapps = 0.0F;
    }

    public float getEyeLevelCorrection() {
        return 0.1F;
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        float f3 = Math.max(-f2 * 1500F, -80F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 0.0F, 80F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Math.max(-f * 1500F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Math.max(-f1 * 1500F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 82.5F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f1, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        YAK_3.moveGear(this.hierMesh(), f, f1, f2);
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, f1 * 25F, 0.0F);
            this.hierMesh().chunkSetAngles("Water_luk", 0.0F, f1 * 12F, 0.0F);
        }
        super.update(f);
    }

    private float flapps;

    static {
        Class class1 = YAK_3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_fr", "3DO/Plane/Yak-3(fr)/hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_3.class });
        Property.set(class1, "LOSElevation", 0.6576F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }
}
