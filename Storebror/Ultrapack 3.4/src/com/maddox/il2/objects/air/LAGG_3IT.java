package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class LAGG_3IT extends LAGG_3 {

    public LAGG_3IT() {
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.2F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.2F), 0.0F);
        }
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", -75F * f, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1200F, -80F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    static {
        Class class1 = LAGG_3IT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "LaGG");
        Property.set(class1, "meshName", "3do/plane/LaGG-3IT/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/LaGG-3IT.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLAGG_3SERIES66.class });
        Property.set(class1, "LOSElevation", 0.69445F);
        weaponTriggersRegister(class1, new int[] { 0, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01" });
    }
}
