package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class I_16TYPE17S extends I_16 implements TypeFighter, TypeTNBFighter {

    public I_16TYPE17S() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && (World.cur().camouflage == 1)) {
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearL2_D0", false);
            this.hierMesh().chunkVisible("GearL3_D0", false);
            this.hierMesh().chunkVisible("SkiL1_D0", true);
            this.hierMesh().chunkVisible("SkiL2_D0", true);
            this.hierMesh().chunkVisible("GearL2z_D0", true);
            this.hierMesh().chunkVisible("GearL3z_D0", true);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearR2_D0", false);
            this.hierMesh().chunkVisible("GearR3_D0", false);
            this.hierMesh().chunkVisible("SkiR1_D0", true);
            this.hierMesh().chunkVisible("SkiR2_D0", true);
            this.hierMesh().chunkVisible("GearR2z_D0", true);
            this.hierMesh().chunkVisible("GearR3z_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        float f3 = 1.0F * (float) Math.sin(f * 1.5707963267948966D);
        float f4 = 1.0F * (float) Math.sin(f1 * 1.5707963267948966D);
        Aircraft.xyz[0] = -0.052F * f3;
        Aircraft.ypr[0] = 9F * f3;
        Aircraft.ypr[1] = -22F * f3;
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = 0.052F * f4;
        Aircraft.ypr[0] = -9F * f4;
        Aircraft.ypr[1] = 22F * f4;
        hiermesh.chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL2X_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 33F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2z_D0", 33F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 6F * f1, -20F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR2X_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", -33F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR2z_D0", -33F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", -6F * f1, 20F * f1, 0.0F);
        hiermesh.chunkSetAngles("SkiL2_D0", 0.0F, -91F * f, 0.0F);
        hiermesh.chunkSetAngles("SkiR2_D0", 0.0F, 91F * f, 0.0F);
        hiermesh.chunkSetAngles("SkiL1_D0", 0.0F, 0.0F, -47F * f);
        hiermesh.chunkSetAngles("SkiR1_D0", 0.0F, 0.0F, -47F * f);
    }

    protected void moveGear(float f, float f1, float f2) {
        I_16TYPE17S.moveGear(this.hierMesh(), f, f1, f2);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -55F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -55F * f, 0.0F);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_16TYPE17S.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_16TYPE17S.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = I_16TYPE17S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type17S/hierS.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type17S(ru)/hierS.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE_Early.class });
        Property.set(class1, "FlightModel", "FlightModels/I-16type17.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
    }
}
