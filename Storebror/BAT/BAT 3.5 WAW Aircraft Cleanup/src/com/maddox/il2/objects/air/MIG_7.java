package com.maddox.il2.objects.air;

import java.security.SecureRandom;
import java.util.ArrayList;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class MIG_7 extends MIG_3 {

    public MIG_7() {
        SecureRandom securerandom = new SecureRandom();
        securerandom.setSeed(System.currentTimeMillis());
        RangeRandom rangerandom = new RangeRandom(securerandom.nextLong());
        for (int i = 0; i < this.rndgear.length; i++) {
            this.rndgear[i] = rangerandom.nextFloat(0.0F, 0.15F);
        }

    }

    private static void myResetYPRmodifier() {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, float af[]) {
        MIG_7.myResetYPRmodifier();
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F + af[0], 0.915F + af[0], 0.0F, 88F);
        if (f <= (af[0] + 0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F + af[0], 0.07F + af[0], 0.0F, 0.05F);
        } else if (f > (af[0] + 0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.12F + af[0], 0.22F + af[0], 0.05F, 0.0F);
        }
        hiermesh.chunkSetLocate("GearL2_BASE", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL2_D0", Aircraft.cvt(f, 0.21F + af[0], 0.915F + af[0], 0.0F, 20F), 0.0F, Aircraft.cvt(f1, 0.21F + af[1], 0.915F + af[1], 0.0F, 6.05F));
        hiermesh.chunkSetAngles("GearL3_BASE", 0.0F, Aircraft.cvt(f, 0.01F + af[0], 0.915F + af[0], 0.0F, 82F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.21F + af[0], 0.915F + af[0], 0.0F, -15F), 0.0F, 0.0F);
        Aircraft.ypr[1] = Aircraft.cvt(f1, 0.01F + af[1], 0.915F + af[1], 0.0F, -88F);
        if (f1 <= (af[1] + 0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(f1, 0.01F + af[1], 0.07F + af[1], 0.0F, -0.05F);
        } else if (f1 > (af[1] + 0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(f1, 0.12F + af[1], 0.22F + af[1], -0.05F, 0.0F);
        }
        hiermesh.chunkSetLocate("GearR2_BASE", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR2_D0", Aircraft.cvt(f1, 0.21F + af[1], 0.915F + af[1], 0.0F, -20F), 0.0F, Aircraft.cvt(f1, 0.21F + af[1], 0.915F + af[1], 0.0F, 6.05F));
        hiermesh.chunkSetAngles("GearR3_BASE", 0.0F, Aircraft.cvt(f1, 0.01F + af[1], 0.915F + af[1], 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(f1, 0.21F + af[1], 0.915F + af[1], 0.0F, 15F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F + af[2], 0.85F + af[2], 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F + af[2], 0.47F + af[2], 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F + af[2], 0.47F + af[2], 0.0F, 80F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        MIG_7.moveGear(this.hierMesh(), f, f1, f2, this.rndgear);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        MIG_7.moveGear(hiermesh, f, f1, f2, MIG_7.rndgearnull);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        MIG_7.moveGear(hiermesh, f, f, f, MIG_7.rndgearnull);
    }

    protected void moveGear(float f) {
        MIG_7.moveGear(this.hierMesh(), f, f, f, this.rndgear);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    private float        rndgear[]     = { 0.0F, 0.0F, 0.0F };
    private static float rndgearnull[] = { 0.0F, 0.0F, 0.0F };

    static {
        Class class1 = MIG_7.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-7");
        Property.set(class1, "meshName", "3DO/Plane/MiG-7/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1953.5F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-7.fmd:MIG_7_11_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_7.class });
        Property.set(class1, "LOSElevation", 0.906F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
