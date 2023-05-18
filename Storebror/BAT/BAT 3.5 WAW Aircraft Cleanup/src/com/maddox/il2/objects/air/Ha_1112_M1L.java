package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class Ha_1112_M1L extends BF_109 {

    public Ha_1112_M1L() {
        this.cockpitDoor_ = 0.0F;
        this.fMaxKMHSpeedForOpenCanopy = 250F;
        this.kangle = 0.0F;
        this.bHasBlister = true;
        Ha_1112_M1L.kl = 1.0F;
        Ha_1112_M1L.kr = 1.0F;
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
        }
        this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -16F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 16F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -16F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 16F * this.kangle, 0.0F);
        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        super.update(f);
        if ((this.FM.CT.getCockpitDoor() > 0.20000000000000001D) && this.bHasBlister && (this.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy) && (this.hierMesh().chunkFindCheck("Blister1_D0") != -1)) {
            try {
                if (this == World.getPlayerAircraft()) {
                    ((CockpitBuchon) Main3D.cur3D().cockpitCur).removeCanopy();
                }
            } catch (Exception exception) {
            }
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
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("CF_D0", false);
            } else {
                this.hierMesh().chunkVisible("CF_D0", true);
            }
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("CF_D1", false);
            }
            this.hierMesh().chunkVisible("CF_D2", false);
            this.hierMesh().chunkVisible("CF_D3", false);
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
            } else if (this.bHasBlister) {
                this.hierMesh().chunkVisible("Blister1_D0", true);
            }
            com.maddox.JGP.Point3d point3d = ((Actor) (World.getPlayerAircraft())).pos.getAbsPoint();
            if ((point3d.z - World.land().HQ(point3d.x, point3d.y)) < 0.0099999997764825821D) {
                this.hierMesh().chunkVisible("CF_D0", true);
            }
            if (this.FM.AS.bIsAboutToBailout) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
            }
        }
    }

    public static void moveGear(HierMesh paramHierMesh, float paramFloat) {
        float f1 = 0.8F;
        float f2 = (-0.5F * (float) Math.cos((paramFloat / f1) * Math.PI)) + 0.5F;
        if ((paramFloat <= f1) || (paramFloat == 1.0F)) {
            paramHierMesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
            paramHierMesh.chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos(((paramFloat - (1.0F - f1)) / f1) * Math.PI)) + 0.5F;
        if (paramFloat >= (1.0F - f1)) {
            paramHierMesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
            paramHierMesh.chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
        }
        if (paramFloat > 0.99F) {
            paramHierMesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            paramHierMesh.chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
            paramHierMesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            paramHierMesh.chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
        if (paramFloat < 0.01F) {
            paramHierMesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
            paramHierMesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
            paramHierMesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
            paramHierMesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    protected void moveGear(float paramFloat) {
        if (this.FM.Gears.isHydroOperable()) {
            Ha_1112_M1L.kl = 1.0F;
            Ha_1112_M1L.kr = 1.0F;
        }
        float f1 = 0.9F - (((Wing) this.getOwner()).aircIndex(this) * 0.1F);
        float f2 = (-0.5F * (float) Math.cos((paramFloat / f1) * Math.PI) * Ha_1112_M1L.kl) + 0.5F;
        if ((paramFloat <= f1) || (paramFloat == 1.0F)) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos(((paramFloat - (1.0F - f1)) / f1) * Math.PI) * Ha_1112_M1L.kr) + 0.5F;
        if (paramFloat >= (1.0F - f1)) {
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
        }
        if (paramFloat > 0.99F) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
    }

    public void moveSteering(float paramFloat) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -paramFloat, 0.0F);
            return;
        }
    }

    public void moveCockpitDoor(float f) {
        if (this.bHasBlister) {
            this.resetYPRmodifier();
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if (Config.isUSE_RENDER()) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                }
                this.setDoorSnd(f);
            }
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        if (this.thisWeaponsName.startsWith("BoB") || this.thisWeaponsName.startsWith("default")) {
            this.hierMesh().chunkVisible("RocketRail_R", false);
            this.hierMesh().chunkVisible("RocketRail_R2", false);
            this.hierMesh().chunkVisible("RocketRail_L", false);
            this.hierMesh().chunkVisible("RocketRail_L2", false);
        } else {
            this.hierMesh().chunkVisible("RocketRail_R", true);
            this.hierMesh().chunkVisible("RocketRail_R2", true);
            this.hierMesh().chunkVisible("RocketRail_L", true);
            this.hierMesh().chunkVisible("RocketRail_L2", true);
        }
        if (this.thisWeaponsName.startsWith("BoB")) {
            this.hierMesh().chunkVisible("MGLeft", false);
            this.hierMesh().chunkVisible("MGRight", false);
            this.hierMesh().chunkVisible("Line01", false);
            this.hierMesh().chunkVisible("Line02", false);
            this.hierMesh().chunkVisible("MgFFR_D0", true);
            this.hierMesh().chunkVisible("MgFFL_D0", true);
            this.hierMesh().chunkVisible("StabStrutL_D0", true);
            this.hierMesh().chunkVisible("StabStrutR_D0", true);
            this.hierMesh().chunkVisible("antena", true);
        } else {
            this.hierMesh().chunkVisible("MGLeft", true);
            this.hierMesh().chunkVisible("MGRight", true);
            this.hierMesh().chunkVisible("Line01", true);
            this.hierMesh().chunkVisible("Line02", true);
            this.hierMesh().chunkVisible("MgFFR_D0", false);
            this.hierMesh().chunkVisible("MgFFL_D0", false);
            this.hierMesh().chunkVisible("StabStrutL_D0", false);
            this.hierMesh().chunkVisible("StabStrutR_D0", false);
            this.hierMesh().chunkVisible("antena", false);
        }
    }

    private static float kl = 1.0F;
    private static float kr = 1.0F;
    public float         cockpitDoor_;
    private float        fMaxKMHSpeedForOpenCanopy;
    private float        kangle;
    public boolean       bHasBlister;

    static {
        Class localClass = Ha_1112_M1L.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "Bf109");
        Property.set(localClass, "meshName", "3DO/Plane/Ha1112M/hier.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(localClass, "yearService", 1942F);
        Property.set(localClass, "yearExpired", 1945.5F);
        Property.set(localClass, "FlightModel", "FlightModels/Buchon.fmd:Buchon_FM");
        Property.set(localClass, "cockpitClass", new Class[] { CockpitBuchon.class });
        Property.set(localClass, "LOSElevation", 0.7498F);
        int ai[] = new int[14];
        ai[2] = 9;
        ai[3] = 9;
        ai[4] = 2;
        ai[5] = 2;
        ai[6] = 2;
        ai[7] = 2;
        ai[8] = 2;
        ai[9] = 2;
        ai[10] = 2;
        ai[11] = 2;
        Aircraft.weaponTriggersRegister(localClass, ai);
        Aircraft.weaponHooksRegister(localClass, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_CANNON01", "_CANNON02" });
    }
}
