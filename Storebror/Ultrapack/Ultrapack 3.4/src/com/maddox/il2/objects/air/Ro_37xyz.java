package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class Ro_37xyz extends Scheme1 implements TypeScout, TypeBomber, TypeFighter, TypeStormovik {

    public Ro_37xyz() {
        this.radPos = 0.0F;
        this.suspR = 0.0F;
        this.suspL = 0.0F;
        this.gunnerAiming = false;
        this.gunnerAnimation = 0.0F;
        this.gunnerDead = false;
        this.gunnerEjected = false;
        this.strafeWithGuns = false;
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
    }

    public boolean cut(String s) {
        boolean flag = super.cut(s);
        if (s.equalsIgnoreCase("WingLIn")) {
            this.hierMesh().chunkVisible("WingLMid_CAP", true);
        } else if (s.equalsIgnoreCase("WingRIn")) {
            this.hierMesh().chunkVisible("WingRMid_CAP", true);
        }
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 19:
                this.FM.Gears.hitCentreGear();
                break;

            case 9:
                if (this.hierMesh().chunkFindCheck("GearL0_D0") != -1) {
                    this.hierMesh().hideSubTrees("GearL0_D0");
                    Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("GearL0_D0"));
                    wreckage.collide(true);
                    this.FM.Gears.hitLeftGear();
                }
                break;

            case 10:
                if (this.hierMesh().chunkFindCheck("GearR0_D0") != -1) {
                    this.hierMesh().hideSubTrees("GearR0_D0");
                    Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("GearR0_D0"));
                    wreckage1.collide(true);
                    this.FM.Gears.hitRightGear();
                }
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -25F) {
            af[0] = -25F;
            flag = false;
        } else if (af[0] > 25F) {
            af[0] = 25F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (f < 10F) {
            if (af[1] < -5F) {
                af[1] = -5F;
                flag = false;
            }
        } else if (af[1] < -15F) {
            af[1] = -15F;
            flag = false;
        }
        if (af[1] > 35F) {
            af[1] = 35F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 2.0F) && (f1 < 17F)) {
            return false;
        }
        if (f1 > -5F) {
            return true;
        }
        if (f1 > -12F) {
            f1 += 12F;
            return f > (12F + (f1 * 2.571429F));
        } else {
            f1 = -f1;
            return f > f1;
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLrod1_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLrod2_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRrod1_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRrod2_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = f1 * -0.45F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        this.hierMesh().chunkSetLocate("Water_D0", Aircraft.xyz, Aircraft.ypr);
        super.update(f);
        if (this.gunnerAiming) {
            if (this.gunnerAnimation < 1.0D) {
                this.gunnerAnimation += 0.025F;
                this.moveGunner();
            }
        } else if (this.gunnerAnimation > 0.0D) {
            this.gunnerAnimation -= 0.025F;
            this.moveGunner();
        }
    }

    private void moveGunner() {
        if (this.gunnerDead || this.gunnerEjected) {
            return;
        }
        if (this.gunnerAnimation > 0.5D) {
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkSetAngles("Pilot2_D0", ((this.gunnerAnimation - 0.5F) * 360F) - 180F, 0.0F, 0.0F);
        } else if (this.gunnerAnimation > 0.25D) {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = (this.gunnerAnimation - 0.5F) * 0.5F;
            Aircraft.ypr[0] = 180F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            this.hierMesh().chunkSetLocate("Pilot2_D0", Aircraft.xyz, Aircraft.ypr);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
        } else {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = this.gunnerAnimation * 0.5F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = this.gunnerAnimation * -110F;
            this.hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", true);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        Actor actor = War.GetNearestEnemy(this, 16, 7000F);
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        boolean flag1 = ((this.FM.CT.Weapons[10] != null) && this.FM.CT.Weapons[10][0].haveBullets()) || ((this.FM.CT.Weapons[10] != null) && this.FM.CT.Weapons[10][1].haveBullets());
        if (!flag1) {
            this.FM.turret[0].bIsOperable = false;
        }
        if (flag1 && (((actor != null) && !(actor instanceof BridgeSegment)) || (aircraft != null))) {
            if (!this.gunnerAiming) {
                this.gunnerAiming = true;
            }
        } else if (this.gunnerAiming) {
            this.gunnerAiming = false;
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
        }
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Prop2_d0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("PropRot2_d0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop2_d0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        if (i == 2) {
            super.doRemoveBodyFromPlane(3);
            this.gunnerEjected = true;
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                if (f <= 0.0F) {
                    this.gunnerDead = true;
                }
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D1", this.hierMesh().isChunkVisible("Pilot2_D0"));
                this.hierMesh().chunkVisible("Pilot3_D1", this.hierMesh().isChunkVisible("Pilot3_D0"));
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.gunnerDead = true;
                break;
        }
    }

    protected void moveGear(float f) {
        super.moveGear(f);
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float f) {
    }

    protected void moveFlap(float f) {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(9.96F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("7")) {
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#7)");
                    }
                } else if (s.endsWith("8")) {
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#8)");
                    }
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out.. (#8)");
                    }
                } else if (s.endsWith("5")) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#5)");
                    }
                } else if (s.endsWith("6")) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#6)");
                    }
                } else if ((s.endsWith("2") || s.endsWith("4")) && (World.Rnd().nextFloat() < 0.5F)) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2/#4)");
                }
                return;
            }
            if (s.startsWith("xxeng") || s.startsWith("xxEng")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    Aircraft.debugprintln(this, "*** Prop hit");
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12.7F, shot);
                } else if (s.startsWith("xxeng1cyls")) {
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.005F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("Oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                this.FM.AS.hitOil(shot.initiator, 0);
                this.getEnergyPastArmor(0.22F, shot);
                Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            } else if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.4F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                    if (this.FM.AS.astateTankStates[i] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                } else if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                } else if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
            } else if (s.startsWith("xMgun")) {
                if (s.endsWith("1")) {
                    Aircraft.debugprintln(this, "*** Rear Gun #1: Disabled..");
                    this.FM.AS.setJamBullets(10, 0);
                }
                if (s.endsWith("2")) {
                    Aircraft.debugprintln(this, "*** Rear Gun #2: Disabled..");
                    this.FM.AS.setJamBullets(10, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else if (s.startsWith("xxgun")) {
                if (s.endsWith("0")) {
                    Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(9.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                } else if (s.startsWith("xxsparli") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                } else if (s.startsWith("xxspar2i") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                } else if (s.startsWith("xxsparlm") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                } else if (s.startsWith("xxsparrm") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                } else if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                } else if (s.startsWith("xxsparro") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else {
                j = s.charAt(5) - 49;
            }
            if (j == 2) {
                j = 1;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            this.hitFlesh(j, shot, byte0);
        } else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xturret1b")) {
            Aircraft.debugprintln(this, "*** Turret Gun: Disabled.. (xturret1b)");
            this.FM.AS.setJamBullets(10, 0);
            this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 2)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xWing")) {
            Aircraft.debugprintln(this, "*** xWing: " + s);
            if (s.startsWith("xWingLIn") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xWingRIn") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xWingLmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xWingRmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xwing")) {
            Aircraft.debugprintln(this, "*** xwing: " + s);
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL1", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR1", shot);
            }
        }
    }

    float           radPos;
    float           suspR;
    float           suspL;
    private boolean bDynamoOperational;
    private float   dynamoOrient;
    private boolean bDynamoRotary;
    private int     pk;
    private boolean gunnerAiming;
    private float   gunnerAnimation;
    private boolean gunnerDead;
    private boolean gunnerEjected;
    public boolean  strafeWithGuns;

    static {
        Class class1 = Ro_37xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
    }
}
