package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class I_16TYPE5 extends I_16 implements TypeTNBFighter {

    public I_16TYPE5() {
        this.bailingOut = false;
        this.canopyForward = false;
        this.okToJump = false;
        this.flaperonAngle = 0.0F;
        this.aileronsAngle = 0.0F;
        this.sideDoorOpened = false;
        this.oneTimeCheckDone = false;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxtank1") && this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.3F) {
            if (this.FM.AS.astateTankStates[0] == 0) {
                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                this.FM.AS.hitTank(shot.initiator, 0, 2);
            }
            if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F) {
                this.FM.AS.hitTank(shot.initiator, 0, 2);
                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
            }
        } else super.hitBone(s, shot, point3d);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.Gears.hitCentreGear();
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void moveCockpitDoor(float f) {
        if (this.bailingOut && f >= 1.0F && !this.canopyForward) {
            this.canopyForward = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        } else if (this.canopyForward) {
            this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
            if (f >= 1.0F) {
                this.okToJump = true;
                this.hitDaSilk();
            }
        } else {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = f * 0.548F;
            this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void hitDaSilk() {
        if (this.okToJump) super.hitDaSilk();
        else if (this.FM.isPlayers() || this.isNetPlayer()) {
            if (this.FM.CT.getCockpitDoor() == 1.0D && !this.bailingOut) {
                this.bailingOut = true;
                this.okToJump = true;
                this.canopyForward = true;
                super.hitDaSilk();
            }
        } else if (!this.FM.AS.isPilotDead(0)) if (this.FM.CT.getCockpitDoor() < 1.0D && !this.bailingOut) {
            this.bailingOut = true;
            this.FM.AS.setCockpitDoor(this, 1);
        } else if (this.FM.CT.getCockpitDoor() == 1.0D && !this.bailingOut) {
            this.bailingOut = true;
            this.okToJump = true;
            this.canopyForward = true;
            super.hitDaSilk();
        }
        if (!this.sideDoorOpened && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0)) {
            this.sideDoorOpened = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        }
    }

    public void moveGear(float f) {
        super.moveGear(f);
        if (f > 0.5F) {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.5F, 1.0F, 14.5F, -8F), Aircraft.cvt(f, 0.5F, 1.0F, 44F, 62.5F), 0.0F);
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.5F, 1.0F, -14.5F, 8F), Aircraft.cvt(f, 0.5F, 1.0F, -44F, -62.5F), 0.0F);
        } else if (f > 0.25F) {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.25F, 0.5F, 33F, 14.5F), Aircraft.cvt(f, 0.25F, 0.5F, 38F, 44F), 0.0F);
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.25F, 0.5F, -33F, -14.5F), Aircraft.cvt(f, 0.25F, 0.5F, -38F, -44F), 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, 33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, 38F), 0.0F);
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -38F), 0.0F);
        }
        if (f > 0.5F) {
            this.hierMesh().chunkVisible("GearWireR2_D0", true);
            this.hierMesh().chunkVisible("GearWireL2_D0", true);
        } else {
            this.hierMesh().chunkVisible("GearWireR2_D0", false);
            this.hierMesh().chunkVisible("GearWireL2_D0", false);
        }
    }

    protected void moveAileron(float f) {
        this.aileronsAngle = f;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f + this.flaperonAngle, 0.0F);
    }

    protected void moveFlap(float f) {
        this.flaperonAngle = f * 17F;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * this.aileronsAngle - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * this.aileronsAngle + this.flaperonAngle, 0.0F);
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            float f1 = this.FM.CT.getAileron();
            float f2 = this.FM.CT.getElevator();
            this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 12F * f1, cvt(f2, -1F, 1.0F, -12F, 18F));
            this.hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - (cvt(f2, -1F, 0.0F, -36F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 32F)));
            this.hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -62F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 44F));
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (!this.oneTimeCheckDone && !this.FM.isPlayers() && !this.isNetPlayer() && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) {
            this.oneTimeCheckDone = true;
            if (World.cur().camouflage == 1) {
                if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.25F) {
                    this.FM.CT.cockpitDoorControl = 1.0F;
                    this.FM.AS.setCockpitDoor(this, 1);
                }
            } else if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F) {
                this.FM.CT.cockpitDoorControl = 1.0F;
                this.FM.AS.setCockpitDoor(this, 1);
            }
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D1", true);
                this.hierMesh().chunkVisible("pilotarm2_d0", false);
                this.hierMesh().chunkVisible("pilotarm1_d0", false);
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("pilotarm2_d0", false);
        this.hierMesh().chunkVisible("pilotarm1_d0", false);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.hierMesh().chunkVisible("GearWireR1_D0", true);
        this.hierMesh().chunkVisible("GearWireL1_D0", true);
    }

    public void missionStarting() {
        super.missionStarting();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void prepareCamouflage() {
        super.prepareCamouflage();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    private boolean bailingOut;
    private boolean canopyForward;
    private boolean okToJump;
    private float   flaperonAngle;
    private float   aileronsAngle;
    private boolean sideDoorOpened;
    private boolean oneTimeCheckDone;

    static {
        Class class1 = I_16TYPE5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type5(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar07());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type5/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar07());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type5.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE5.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev07", "_ExternalDev08" });
    }
}
