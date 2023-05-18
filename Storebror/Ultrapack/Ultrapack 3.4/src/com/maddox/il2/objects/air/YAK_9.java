package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class YAK_9 extends YAK implements TypeBNZFighter {

    public YAK_9() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -80F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 80F * f, 0.0F);
        f1 = Math.max(-f * 1500F, -60F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("Water_luk", 0.0F, this.FM.EI.engines[0].getControlRadiator() * 12F, 0.0F);
        super.update(f);
    }

    static {
        Class class1 = YAK_9.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-9(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_fr", "3DO/Plane/Yak-9(fr)/hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1952.8F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-9.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_9.class });
        Property.set(class1, "LOSElevation", 0.6432F);
        weaponTriggersRegister(class1, new int[] { 0, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01" });
    }
}
