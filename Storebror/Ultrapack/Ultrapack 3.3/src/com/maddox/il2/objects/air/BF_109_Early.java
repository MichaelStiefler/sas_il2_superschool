package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class BF_109_Early extends BF_109 {

    public BF_109_Early() {
        this.cockpitDoor_ = 0.0F;
        this.fMaxKMHSpeedForOpenCanopy = 40F;
        this.kangle = 0.0F;
        this.bHasBlister = true;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.dvCockpitDoor = 0.5F;
        }
    }

    public void update(float f) {
        super.update(f);
        this.moveWingSlats();
        this.moveRadiatorFlaps();
        this.cockpitDoorVisibilty();
        this.partsVisibilty();
    }

    private void moveWingSlats() {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
        }
    }

    private void moveRadiatorFlaps() {
        if (this.hierMesh().chunkFindCheck("WaterL_D0") != -1) this.hierMesh().chunkSetAngles("WaterL_D0", 0.0F, -38F * this.kangle, 0.0F);
        if (this.hierMesh().chunkFindCheck("WaterR_D0") != -1) this.hierMesh().chunkSetAngles("WaterR_D0", 0.0F, -38F * this.kangle, 0.0F);
        this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if (this.kangle > 1.0F) this.kangle = 1.0F;
    }

    private void cockpitDoorVisibilty() {
        if (this.FM.CT.getCockpitDoor() > 0.2D && this.bHasBlister && this.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy && this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
            if (Config.isUSE_RENDER()) try {
                if (this == World.getPlayerAircraft()) ((CockpitBF_109Bx) Main3D.cur3D().cockpitCur).removeCanopy();
            } catch (Exception exception) {}
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.bHasBlister = false;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.setGCenter(-0.5F);
        }
    }

    private void partsVisibilty() {
        if (!Config.isUSE_RENDER()) return;
        if (!this.FM.isPlayers()) return;
        if (this != World.getPlayerAircraft()) return;
        boolean isOutside = Main3D.cur3D().isViewOutside();
        this.hierMesh().chunkVisible("CF_D0", isOutside);
        this.hierMesh().chunkVisible("Blister1_D0", isOutside && this.bHasBlister);
        if (!isOutside) {
            this.hierMesh().chunkVisible("CF_D1", false);
            this.hierMesh().chunkVisible("CF_D2", false);
            this.hierMesh().chunkVisible("CF_D3", false);
        }
        Point3d point3d = this.pos.getAbsPoint();
        if (point3d.z - World.land().HQ(point3d.x, point3d.y) < 0.01D) this.hierMesh().chunkVisible("CF_D0", true);
        if (this.FM.AS.bIsAboutToBailout) this.hierMesh().chunkVisible("Blister1_D0", false);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = 0.8F;
        float f2 = -0.5F * (float) Math.cos(f / f1 * Math.PI) + 0.5F;
        if (f <= f1) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -78F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -24F * f2, 0.0F, 0.0F);
        }
        f2 = -0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * Math.PI) + 0.5F;
        if (f >= 1.0F - f1) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 78F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 24F * f2, 0.0F, 0.0F);
        }
        if (f > 0.99F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -78F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -24F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 78F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 24F, 0.0F, 0.0F);
        }
        if (f < 0.01F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    protected void moveGear(float f) {
        float f1 = 0.9F - ((Wing) this.getOwner()).aircIndex(this) * 0.1F;
        float f2 = -0.5F * (float) Math.cos(f / f1 * Math.PI) + 0.5F;
        if (f <= f1) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -78F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -24F * f2, 0.0F, 0.0F);
        }
        f2 = -0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * Math.PI) + 0.5F;
        if (f >= 1.0F - f1) {
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 78F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 24F * f2, 0.0F, 0.0F);
        }
        if (f > 0.99F) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -78F, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -24F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 78F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 24F, 0.0F, 0.0F);
        }
    }

    public void moveSteering(float f) {
        if (f > 77.5F) {
            f = 77.5F;
            this.FM.Gears.steerAngle = f;
        }
        if (f < -77.5F) {
            f = -77.5F;
            this.FM.Gears.steerAngle = f;
        }
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        if (this.bHasBlister) {
            this.resetYPRmodifier();
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if (Config.isUSE_RENDER()) {
                if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
                this.setDoorSnd(f);
            }
        }
    }

    static void init(Class airClass) {
        new NetAircraft.SPAWN(airClass);
        Property.set(airClass, "iconFar_shortClassName", "Bf109");
        Property.set(airClass, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(airClass, "yearService", 1938F);
        Property.set(airClass, "yearExpired", 1941F);
        Property.set(airClass, "cockpitClass", new Class[] { CockpitBF_109Bx.class });
        Property.set(airClass, "LOSElevation", 0.74985F);
        Aircraft.weaponTriggersRegister(airClass, new int[] { 0, 0, 1, 1, 1 });
        Aircraft.weaponHooksRegister(airClass, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON01", "_CANNON02" });
    }

    public float   cockpitDoor_;
    private float  fMaxKMHSpeedForOpenCanopy;
    private float  kangle;
    public boolean bHasBlister;
}
