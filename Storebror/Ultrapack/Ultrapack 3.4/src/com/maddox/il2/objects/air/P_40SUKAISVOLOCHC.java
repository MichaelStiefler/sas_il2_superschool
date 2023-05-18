package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class P_40SUKAISVOLOCHC extends P_40SUKAISVOLOCH {

    public P_40SUKAISVOLOCHC() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f, 0.04F, 0.5F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.02F, 0.09F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f, 0.02F, 0.09F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.01F, 0.79F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL21_D0", 0.0F, cvt(f, 0.01F, 0.79F, 0.0F, 94F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(f, 0.01F, 0.39F, 0.0F, -53F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.01F, 0.11F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.01F, 0.79F, 0.0F, 100F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.21F, 0.99F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR21_D0", 0.0F, cvt(f, 0.21F, 0.99F, 0.0F, -94F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(f, 0.21F, 0.59F, 0.0F, -53F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.21F, 0.31F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f, 0.21F, 0.99F, 0.0F, 100F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
    }

    static {
        Class class1 = P_40SUKAISVOLOCHC.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-40");
        Property.set(class1, "meshName", "3DO/Plane/P-40C(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_us", "3DO/Plane/P-40C(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-40C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_40C.class });
        Property.set(class1, "LOSElevation", 1.0728F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 3, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02" });
    }
}
