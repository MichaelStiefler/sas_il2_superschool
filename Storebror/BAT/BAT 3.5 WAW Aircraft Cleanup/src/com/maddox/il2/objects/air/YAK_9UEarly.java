package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class YAK_9UEarly extends YAK implements TypeBNZFighter {

    public YAK_9UEarly() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = 0.0F;
        f1 = Math.max(1500F * -f, -80F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, f * 80F, 0.0F);
        f1 = Math.max(1500F * -f, -60F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f * 82.5F, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, f * 82.5F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f * -85F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f * -85F, 0.0F);
    }

    protected void moveGear(float f) {
        YAK_9UEarly.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, 15F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Water_luk", 0.0F, 12F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    static {
        Class class1 = YAK_9UEarly.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-9U(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1952.8F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-9UEarly.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_9U.class });
        Property.set(class1, "LOSElevation", 0.6432F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }
}
