package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.Main;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class I_16TYPE6 extends I_16 implements TypeTNBFighter {

    public I_16TYPE6() {
        this.flaperonAngle = 0.0F;
        this.aileronsAngle = 0.0F;
        this.hasTubeSight = false;
        this.pit = null;
        this.sideDoorOpened = false;
        this.removeSpinnerHub = false;
    }

    public void moveGear(float f, float f1, float f2) {
        super.moveGear(f, f1, f2);
        if (f > 0.5F) {
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.5F, 1.0F, -14.5F, 8F), Aircraft.cvt(f, 0.5F, 1.0F, -44F, -62.5F), 0.0F);
        } else if (f > 0.25F) {
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.25F, 0.5F, -33F, -14.5F), Aircraft.cvt(f, 0.25F, 0.5F, -38F, -44F), 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -38F), 0.0F);
        }
        if (f1 > 0.5F) {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f1, 0.5F, 1.0F, 14.5F, -8F), Aircraft.cvt(f1, 0.5F, 1.0F, 44F, 62.5F), 0.0F);
        } else if (f1 > 0.25F) {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f1, 0.25F, 0.5F, 33F, 14.5F), Aircraft.cvt(f1, 0.25F, 0.5F, 38F, 44F), 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 33F), Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 38F), 0.0F);
        }
        if (f > 0.5F) {
            this.hierMesh().chunkVisible("GearWireL2_D0", true);
        } else {
            this.hierMesh().chunkVisible("GearWireL2_D0", false);
        }
        if (f1 > 0.5F) {
            this.hierMesh().chunkVisible("GearWireR2_D0", true);
        } else {
            this.hierMesh().chunkVisible("GearWireR2_D0", false);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxtank1") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.3F)) {
            if (this.FM.AS.astateTankStates[0] == 0) {
                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                this.FM.AS.hitTank(shot.initiator, 0, 2);
            }
            if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.hitTank(shot.initiator, 0, 2);
                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
            }
        } else {
            super.hitBone(s, shot, point3d);
        }
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            float f1 = this.FM.CT.getAileron();
            float f2 = this.FM.CT.getElevator();
            this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 12F * f1, Aircraft.cvt(f2, -1F, 1.0F, -12F, 18F));
            this.hierMesh().chunkSetAngles("pilotarm2_d0", Aircraft.cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, Aircraft.cvt(f1, -1F, 1.0F, 6F, -8F) - (Aircraft.cvt(f2, -1F, 0.0F, -36F, 0.0F) + Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 32F)));
            this.hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, Aircraft.cvt(f1, -1F, 1.0F, -16F, 14F) + Aircraft.cvt(f2, -1F, 0.0F, -62F, 0.0F) + Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 44F));
            if (!this.removeSpinnerHub) {
                boolean flag = this.hierMesh().isChunkVisible("PropRot1_D0");
                this.hierMesh().chunkVisible("PropHubRot1_D0", flag);
                this.hierMesh().chunkVisible("PropHub1_D0", !flag);
            }
        }
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

    public boolean hasTubeSight() {
        return this.hasTubeSight;
    }

    public void missionStarting() {
        super.missionStarting();
        this.customization();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void prepareCamouflage() {
        super.prepareCamouflage();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.hierMesh().chunkVisible("GearWireR1_D0", true);
        this.hierMesh().chunkVisible("GearWireL1_D0", true);
    }

    private void customization() {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        boolean flag = false;
        boolean flag1 = false;
        int i = this.hierMesh().chunkFindCheck("CF_D0");
        int j = this.hierMesh().materialFindInChunk("Gloss1D0o", i);
        Mat mat = this.hierMesh().material(j);
        String s = mat.Name();
        if (s.startsWith("PaintSchemes/Cache")) {
            try {
                s = s.substring(19);
                s = s.substring(0, s.indexOf("/"));
                String s1 = Main.cur().netFileServerSkin.primaryPath();
                File file = new File(HomePath.toFileSystemName(s1 + "/I-16type6/Customization.ini", 0));
                BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
                boolean flag2 = false;
                boolean flag3 = false;
                boolean flag4 = false;
                boolean flag5 = false;
                boolean flag6 = false;
                boolean flag7 = false;
                do {
                    String s2;
                    if ((s2 = bufferedreader.readLine()) == null) {
                        break;
                    }
                    if (s2.equals("[TubeSight]")) {
                        flag2 = true;
                        flag3 = false;
                        flag4 = false;
                        flag5 = false;
                        flag6 = false;
                        flag7 = false;
                    } else if (s2.equals("[RadioWires]")) {
                        flag2 = false;
                        flag3 = true;
                        flag4 = false;
                        flag5 = false;
                        flag6 = false;
                        flag7 = false;
                    } else if (s2.equals("[FullWheelCovers]")) {
                        flag2 = false;
                        flag3 = false;
                        flag4 = true;
                        flag5 = false;
                        flag6 = false;
                        flag7 = false;
                    } else if (s2.equals("[RemoveSpinner]")) {
                        flag2 = false;
                        flag3 = false;
                        flag4 = false;
                        flag5 = false;
                        flag6 = false;
                        flag7 = true;
                    } else if (s2.equals("[KeepSpinner]")) {
                        flag2 = false;
                        flag3 = false;
                        flag4 = false;
                        flag5 = false;
                        flag6 = true;
                        flag7 = false;
                    } else if (s2.equals("[CanopyRails]")) {
                        flag2 = false;
                        flag3 = false;
                        flag4 = false;
                        flag5 = true;
                        flag6 = false;
                        flag7 = false;
                    } else if (s2.equals(s)) {
                        if (flag2) {
                            this.hasTubeSight = true;
                        }
                        if (flag3) {
                            this.hierMesh().chunkVisible("RadioWire1_d0", true);
                            this.hierMesh().chunkVisible("RadioWire2_d0", true);
                        }
                        if (flag4) {
                            this.hierMesh().chunkVisible("GearR3_D0", true);
                            this.hierMesh().chunkVisible("GearL3_D0", true);
                        }
                        if (flag5) {
                            this.hierMesh().chunkVisible("Rails_d0", true);
                            this.hierMesh().chunkVisible("Blister2Rail_D0", true);
                            this.hierMesh().chunkVisible("Blister2_D0", false);
                            this.hierMesh().chunkVisible("T6Rail_D0", false);
                        }
                        if (flag6) {
                            flag1 = true;
                        }
                        if (flag7) {
                            flag = true;
                        }
                    }
                } while (true);
                bufferedreader.close();
            } catch (Exception exception) {
                System.out.println(exception);
            }
        } else {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) {
                this.hasTubeSight = true;
            }
            if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F) {
                this.hierMesh().chunkVisible("GearR3_D0", true);
                this.hierMesh().chunkVisible("GearL3_D0", true);
            }
        }
        if (this.pit != null) {
            this.pit.setTubeSight(this.hasTubeSight);
        }
        this.hierMesh().chunkVisible("Sight_D0", !this.hasTubeSight);
        this.hierMesh().chunkVisible("TubeSight_D0", this.hasTubeSight);
        if (flag || (!flag1 && ((this.FM.CT.Weapons[2] != null) || (this.FM.CT.Weapons[3] != null)))) {
            this.removeSpinnerHub = true;
            this.hierMesh().chunkVisible("PropHubRot1_D0", false);
            this.hierMesh().chunkVisible("PropHub1_D0", false);
        }
    }

    public void registerPit(CockpitI_16TYPE6 cockpiti_16type6) {
        this.pit = cockpiti_16type6;
        if (cockpiti_16type6 != null) {
            cockpiti_16type6.setTubeSight(this.hasTubeSight);
        }
    }

    public void moveCockpitDoor(float f) {
        this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
    }

    public void hitDaSilk() {
        super.hitDaSilk();
        if (!this.sideDoorOpened && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0)) {
            this.sideDoorOpened = true;
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 11:
                this.hierMesh().chunkVisible("RadioWire1_d0", false);
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                break;

            case 36:
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                this.hierMesh().chunkVisible("GearWireR1_D0", false);
                this.hierMesh().chunkVisible("GearWireR2_D0", false);
                break;

            case 38:
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                break;

            case 19:
                this.hierMesh().chunkVisible("RadioWire1_d0", false);
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                break;

            case 9:
            case 33:
                this.hierMesh().chunkVisible("GearWireL1_D0", false);
                this.hierMesh().chunkVisible("GearWireL2_D0", false);
                break;

            case 10:
                this.hierMesh().chunkVisible("GearWireR1_D0", false);
                this.hierMesh().chunkVisible("GearWireR2_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    private float            flaperonAngle;
    private float            aileronsAngle;
    private boolean          hasTubeSight;
    private CockpitI_16TYPE6 pit;
    private boolean          sideDoorOpened;
    private boolean          removeSpinnerHub;

    static {
        Class class1 = I_16TYPE6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type6(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar07());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type6/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar07());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type6.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE6.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 2, 2, 2, 2, 2, 2, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
    }
}
