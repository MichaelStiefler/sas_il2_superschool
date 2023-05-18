package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class I_16TYPE5_SKIS extends I_16FixedSkis implements TypeTNBFighter {

    public I_16TYPE5_SKIS() {
        this.bailingOut = false;
        this.canopyForward = false;
        this.okToJump = false;
        this.flaperonAngle = 0.0F;
        this.aileronsAngle = 0.0F;
        this.oneTimeCheckDone = false;
        this.sideDoorOpened = false;
    }

    public void moveCockpitDoor(float f) {
        if (this.bailingOut && (f >= 1.0F) && !this.canopyForward) {
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
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void hitDaSilk() {
        if (this.okToJump) {
            super.hitDaSilk();
        } else if (this.FM.isPlayers() || this.isNetPlayer()) {
            if ((this.FM.CT.getCockpitDoor() == 1.0D) && !this.bailingOut) {
                this.bailingOut = true;
                this.okToJump = true;
                this.canopyForward = true;
                super.hitDaSilk();
            }
        } else if (!this.FM.AS.isPilotDead(0)) {
            if ((this.FM.CT.getCockpitDoor() < 1.0D) && !this.bailingOut) {
                this.bailingOut = true;
                this.FM.AS.setCockpitDoor(this, 1);
            } else if ((this.FM.CT.getCockpitDoor() == 1.0D) && !this.bailingOut) {
                this.bailingOut = true;
                this.okToJump = true;
                this.canopyForward = true;
                super.hitDaSilk();
            }
        }
        if (!this.sideDoorOpened && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0)) {
            this.sideDoorOpened = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        }
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

    public void moveWheelSink() {
    }

    public void moveGear(float f, float f1, float f2) {
    }

    protected void moveAileron(float f) {
        this.aileronsAngle = f;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, (-30F * f) - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, (-30F * f) + this.flaperonAngle, 0.0F);
    }

    protected void moveFlap(float f) {
        this.flaperonAngle = f * 17F;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, (-30F * this.aileronsAngle) - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, (-30F * this.aileronsAngle) + this.flaperonAngle, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (!this.oneTimeCheckDone && !this.FM.isPlayers() && !this.isNetPlayer() && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
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

    private boolean bailingOut;
    private boolean canopyForward;
    private boolean okToJump;
    private float   flaperonAngle;
    private float   aileronsAngle;
    private boolean oneTimeCheckDone;
    private boolean sideDoorOpened;

    static {
        Class class1 = I_16TYPE5_SKIS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type5(multi)/hier_skis.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar07());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type5/hier_skis.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar07());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type5Skis.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE5.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev07", "_ExternalDev08" });
    }
}
