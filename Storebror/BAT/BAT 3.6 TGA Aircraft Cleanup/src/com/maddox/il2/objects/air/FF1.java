package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class FF1 extends Intruderxyz {

    public FF1() {
        this.arrestor = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        Math.max(-f * 800F, -70F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 100F * f, 0.0F);
    }

    protected void moveGear(float f) {
        FF1.moveGear(this.hierMesh(), f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this == World.getPlayerAircraft()) {
            this.FM.Gears.setOperable(true);
            this.FM.Gears.setHydroOperable(false);
        }
    }

    public void moveArrestorHook(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -1.045F * f;
        Aircraft.ypr[1] = -this.arrestor;
        this.hierMesh().chunkSetLocate("Hook1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.CT.getArrestor();
        float f2 = 81F * f1 * f1 * f1 * f1 * f1 * f1 * f1;
        if (f1 > 0.01F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                this.arrestor = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -f2, f2, -f2, f2);
                this.moveArrestorHook(f1);
                if (this.FM.Gears.arrestorVAngle < -81F) {

                }
            } else {
                float f3 = 58F * this.FM.Gears.arrestorVSink;
                if ((f3 > 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                this.arrestor += f3;
                if (this.arrestor > f2) {
                    this.arrestor = f2;
                }
                if (this.arrestor < -f2) {
                    this.arrestor = -f2;
                }
                this.moveArrestorHook(f1);
            }
        }
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("Blister2_D0", true);
            }
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister2_D1", false);
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("Blister2_D3", false);
        }
    }

    private float arrestor;

    static {
        Class class1 = FF1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FF1");
        Property.set(class1, "meshName", "3DO/Plane/Goblin/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/FF1.fmd:FF1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDelfin.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03" });
    }
}
