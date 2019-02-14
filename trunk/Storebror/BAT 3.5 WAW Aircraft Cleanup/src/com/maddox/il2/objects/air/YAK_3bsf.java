package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class YAK_3bsf extends YAK {

    public YAK_3bsf() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -80F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 0.0F, 80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        f1 = Math.max(-f * 1500F, -60F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
    }

    protected void moveGear(float f) {
        YAK_3bsf.moveGear(this.hierMesh(), f);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -57F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -12F * f, 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        this.hierMesh().chunkSetLocate("Hook3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * 25F, 0.0F);
        this.hierMesh().chunkSetAngles("Water_luk", 0.0F, this.FM.EI.engines[0].getControlRadiator() * 12F, 0.0F);
        super.update(f);
    }

    static {
        Class class1 = YAK_3bsf.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-3(Multi1)/hieryak3n.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_fr", "3DO/Plane/Yak-3(fr)/hieryak3n.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-3bsf.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_3.class });
        Property.set(class1, "LOSElevation", 0.6576F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }
}
