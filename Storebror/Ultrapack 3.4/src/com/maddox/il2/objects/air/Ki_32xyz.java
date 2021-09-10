package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.VisibilityChecker;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class Ki_32xyz extends Scheme1 implements TypeBomber, TypeStormovik {

    public Ki_32xyz() {
        this.bCanopyInitState = false;
        this.kangle = 0.0F;
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -80F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -70F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay03_D0", 0.0F, -80F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay04_D0", 0.0F, -70F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.68F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.15F, 0.99F, 0.0F, -0.7F);
        this.hierMesh().chunkSetLocate("Blister3_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            for (int i = 1; i < 3; i++) {
                if (this.FM.getAltitude() < 3000F) {
                    this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
                } else {
                    this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
                }
            }

        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrols")) {
                if ((s.endsWith("1") || s.endsWith("2")) && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.35F, shot) > 0.0F)) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                }
                if (s.endsWith("3") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F)) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                }
                if (s.endsWith("4") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                }
                if (s.endsWith("5") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(2.2F, shot) > 0.0F)) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 140000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 85000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(6.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.startsWith("xxeng1mag")) {
                    int i = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if (s.endsWith("oil1")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    }
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.endsWith("li1") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if (s.endsWith("ri1") && (this.chunkDamageVisible("WingRIn") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if (s.endsWith("lo1") && (this.chunkDamageVisible("WingLOut") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if (s.endsWith("ro1") && (this.chunkDamageVisible("WingROut") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if ((s.endsWith("sl1") || s.endsWith("sl2")) && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if ((s.endsWith("sr1") || s.endsWith("sr2")) && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 48;
                int k = 0;
                switch (j) {
                    case 1:
                        k = World.Rnd().nextInt(0, 1);
                        break;

                    case 2:
                        k = World.Rnd().nextInt(2, 3);
                        break;

                    case 3:
                        k = World.Rnd().nextInt(1, 2);
                        break;
                }
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, k, 1);
                    if ((World.Rnd().nextFloat() < 0.05F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.6F))) {
                        this.FM.AS.hitTank(shot.initiator, k, 2);
                    }
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            this.hitChunk("CF", shot);
        } else if (!s.startsWith("xblister")) {
            if (s.startsWith("xeng")) {
                this.hitChunk("Engine1", shot);
            } else if (s.startsWith("xtail")) {
                this.hitChunk("Tail1", shot);
            } else if (s.startsWith("xkeel")) {
                if (this.chunkDamageVisible("Keel1") < 2) {
                    this.hitChunk("Keel1", shot);
                }
            } else if (s.startsWith("xrudder")) {
                if (this.chunkDamageVisible("Rudder1") < 2) {
                    this.hitChunk("Rudder1", shot);
                }
            } else if (s.startsWith("xstab")) {
                if (s.startsWith("xstabl")) {
                    this.hitChunk("StabL", shot);
                }
                if (s.startsWith("xstabr")) {
                    this.hitChunk("StabR", shot);
                }
            } else if (s.startsWith("xvator")) {
                if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 2)) {
                    this.hitChunk("VatorL", shot);
                }
                if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 2)) {
                    this.hitChunk("VatorR", shot);
                }
            } else if (s.startsWith("xwing")) {
                if (s.startsWith("xWingLIn") && (this.chunkDamageVisible("WingLIn") < 2)) {
                    this.hitChunk("WingLIn", shot);
                }
                if (s.startsWith("xWingRIn") && (this.chunkDamageVisible("WingRIn") < 2)) {
                    this.hitChunk("WingRIn", shot);
                }
                if (s.startsWith("xWingLOut") && (this.chunkDamageVisible("WingLOut") < 2)) {
                    this.hitChunk("WingLOut", shot);
                }
                if (s.startsWith("xWingROut") && (this.chunkDamageVisible("WingROut") < 2)) {
                    this.hitChunk("WingROut", shot);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int l;
                if (s.endsWith("a")) {
                    byte0 = 1;
                    l = s.charAt(6) - 49;
                } else if (s.endsWith("b")) {
                    byte0 = 2;
                    l = s.charAt(6) - 49;
                } else {
                    l = s.charAt(5) - 49;
                }
                this.hitFlesh(l, shot, byte0);
            }
        }
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.kangle - f1) > 0.01F) {
            this.kangle = f1;
            this.hierMesh().chunkSetAngles("Cowflap1_D0", -27F * f1, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Cowflap2_D0", 27F * f1, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Cowflap3_D0", 0.0F, 27F * f1, 0.0F);
            this.hierMesh().chunkSetAngles("Cowflap4_D0", 0.0F, 27F * f1, 0.0F);
        }
        if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            float f4 = this.FM.EI.engines[0].getRPM();
            if ((f4 < 499F) && (f4 > 30F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f4) / 3000F;
            }
            float f5 = this.FM.EI.engines[0].getRPM();
            if ((f5 < 1000F) && (f5 > 500F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f5) / 8000F;
            }
            float f7 = this.FM.EI.engines[0].getRPM();
            if ((f7 > 1001F) && (f7 < 1500F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.07F;
            }
            float f8 = this.FM.EI.engines[0].getRPM();
            if ((f8 > 1501F) && (f8 < 2000F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.05F;
            }
            float f9 = this.FM.EI.engines[0].getRPM();
            if ((f9 > 2001F) && (f9 < 2500F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.04F;
            }
            float f10 = this.FM.EI.engines[0].getRPM();
            if (f10 > 2501F) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.03F;
            }
        }
        if (this.FM.getSpeedKMH() > 780F) {
            VisibilityChecker.checkCabinObstacle = true;
        }
        if ((this.FM.getSpeedKMH() > 250F) && (this.FM.getVertSpeed() > 0.0F) && (this.FM.getAltitude() < 5000F)) {
            this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
        }
        if (this.FM.isPlayers() && (this.FM.Sq.squareElevators > 0.0F)) {
            RealFlightModel realflightmodel = (RealFlightModel) this.FM;
            if (realflightmodel.RealMode && (realflightmodel.indSpeed > 120F)) {
                float f6 = 1.0F + (0.005F * (120F - realflightmodel.indSpeed));
                if (f6 < 0.0F) {
                    f6 = 0.0F;
                }
                this.FM.SensPitch = 0.6F * f6;
                if (realflightmodel.indSpeed > 120F) {
                    this.FM.producedAM.y -= 1700F * (120F - realflightmodel.indSpeed);
                }
            } else {
                this.FM.SensPitch = 0.55F;
            }
        }
        if (!this.bCanopyInitState && this.FM.isStationedOnGround() && this.FM.isPlayers()) {
            this.FM.AS.setCockpitDoor(this.FM.actor, 1);
            this.FM.CT.cockpitDoorControl = 1.0F;
            this.bCanopyInitState = true;
            System.out.println("*** Initial canopy state: " + (this.FM.CT.getCockpitDoor() == 1.0F ? "open" : "closed"));
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -31F) {
                    f = -31F;
                    flag = false;
                }
                if (f > 31F) {
                    f = 31F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 52F) {
                    f1 = 52F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public abstract void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException;

    public abstract void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException;

    public abstract void typeBomberUpdate(float f);

    public abstract void typeBomberAdjSpeedMinus();

    public abstract void typeBomberAdjSpeedPlus();

    public abstract void typeBomberAdjSpeedReset();

    public abstract void typeBomberAdjAltitudeMinus();

    public abstract void typeBomberAdjAltitudePlus();

    public abstract void typeBomberAdjAltitudeReset();

    public abstract void typeBomberAdjSideslipMinus();

    public abstract void typeBomberAdjSideslipPlus();

    public abstract void typeBomberAdjSideslipReset();

    public abstract void typeBomberAdjDistanceMinus();

    public abstract void typeBomberAdjDistancePlus();

    public abstract void typeBomberAdjDistanceReset();

    public abstract boolean typeBomberToggleAutomation();

    private boolean bCanopyInitState;
    private float   kangle;

    static {
        Class class1 = Ki_32xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
