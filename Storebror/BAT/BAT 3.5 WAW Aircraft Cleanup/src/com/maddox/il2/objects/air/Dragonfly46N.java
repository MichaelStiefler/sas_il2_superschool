package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.MGunB20k;
import com.maddox.il2.objects.weapons.MGunM4k;
import com.maddox.il2.objects.weapons.MGunMG213C20s;
import com.maddox.il2.objects.weapons.MGunMK108k;
import com.maddox.il2.objects.weapons.MGunNS37k;
import com.maddox.rts.Property;

public class Dragonfly46N extends DragonflyX implements TypeSeaPlane {

    public Dragonfly46N() {
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
    }

    protected void moveGear(float f, float f1, float f2) {
    }

    public void moveWheelSink() {
    }

    public void moveSteering(float f) {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        super.hitBone(s, shot, point3d);
        if (s.startsWith("xgearc")) {
            this.hitChunk("GearC2", shot);
        }
        if (s.startsWith("xgearl")) {
            this.hitChunk("GearL2", shot);
        }
        if (s.startsWith("xgearr")) {
            this.hitChunk("GearR2", shot);
        }
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    Dragonfly46N.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    Dragonfly46N.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(Dragonfly46N.tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }
            }

        }

    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, -30F * f, 0.0F);
        this.updateControlsVisuals();
    }

    protected void moveElevator(float f) {
        this.updateControlsVisuals();
    }

    private final void updateControlsVisuals() {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, (-21.25F * this.FM.CT.getElevator()) - (21.25F * this.FM.CT.getRudder()), 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, (-21.25F * this.FM.CT.getElevator()) + (21.25F * this.FM.CT.getRudder()), 0.0F);
        this.hierMesh().chunkSetAngles("VatorL2_D0", 0.0F, (21.25F * this.FM.CT.getElevator()) + (0.0F * this.FM.CT.getRudder()), 0.0F);
        this.hierMesh().chunkSetAngles("VatorR2_D0", 0.0F, (21.25F * this.FM.CT.getElevator()) - (0.0F * this.FM.CT.getRudder()), 0.0F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_CANNON01") instanceof MGunMK108k) {
            this.FM.M.massEmpty += 40F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunMG213C20s) {
            this.FM.M.massEmpty += 80F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunB20k) {
            this.FM.M.massEmpty -= 30F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunNS37k) {
            this.FM.M.massEmpty += 200F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunM4k) {
            this.FM.M.massEmpty += 120F;
        }
    }

    private static Point3d tmpp = new Point3d();

    static {
        Class class1 = Dragonfly46N.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly");
        Property.set(class1, "meshName", "3DO/Plane/Dragonfly46N/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly46N.fmd:Dragonfly46_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly46N.class });
        Property.set(class1, "LOSElevation", 0.5099F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
    }
}
