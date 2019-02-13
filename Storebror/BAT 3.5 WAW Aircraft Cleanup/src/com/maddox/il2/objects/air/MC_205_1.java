package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class MC_205_1 extends MC_202xyz {

    public MC_205_1() {
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -88F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -100F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -114F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -114F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Math.max(-f * 1500F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Math.max(-f1 * 1500F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.11F, 0.67F, 0.0F, -38F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f2, 0.01F, 0.09F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f2, 0.01F, 0.09F, 0.0F, -80F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGearC() < 0.65F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, cvt(f, -30F, 30F, 30F, -30F), 0.0F);
            return;
        }
    }

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "M.C.205");
        Property.set(class1, "meshName_it", "3DO/Plane/MC-205_I(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName", "3DO/Plane/MC-205_I(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/MC-205.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMC_205.class} );
        Property.set(class1, "LOSElevation", 0.7898F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
        weaponsRegister(class1, "default", new String[] { "MGunBredaSAFAT127siMC205 370", "MGunBredaSAFAT127siMC205 370", "MGunBredaSAFAT77k 500", "MGunBredaSAFAT77k 500" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null });
    }
}
