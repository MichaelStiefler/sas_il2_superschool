package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class Yermolayev extends Scheme2 implements TypeBomber {

    public Yermolayev() {
        this.radist = new int[3];
        this.tme = 0L;
        this.bChangedExts = false;
        bChangedPit = true;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 250F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 75F) this.fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15F) this.fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip++;
        if (this.fSightCurSideslip > 45F) this.fSightCurSideslip = 45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip--;
        if (this.fSightCurSideslip < -45F) this.fSightCurSideslip = -45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 6000F) this.fSightCurAltitude = 6000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 300F) this.fSightCurAltitude = 300F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 650F) this.fSightCurSpeed = 650F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) this.fSightCurSpeed = 50F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(this.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readFloat();
        this.fSightCurSideslip = netmsginput.readFloat();
    }

    protected void moveBayDoor(float f) {
        if (this.thisWeaponsName.startsWith("2 x")) {
            this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, 0.0F * f, 0.0F);
            this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, 0.0F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, -99F * f, 0.0F);
            this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, 99F * f, 0.0F);
        }
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f) {
        if (this.thisWeaponsName.equals("2 x FAB2000")) {
            this.hierMesh().chunkSetAngles("FlapCL_D0", 0.0F, 0.0F, 0.0F * f);
            this.hierMesh().chunkSetAngles("FlapCR_D0", 0.0F, 0.0F, 0.0F * f);
            this.hierMesh().chunkSetAngles("FlapWL_D0", 0.0F, 0.0F, 40F * f);
            this.hierMesh().chunkSetAngles("FlapWR_D0", 0.0F, 0.0F, 40F * f);
        } else {
            this.hierMesh().chunkSetAngles("FlapCL_D0", 0.0F, 0.0F, 45F * f);
            this.hierMesh().chunkSetAngles("FlapCR_D0", 0.0F, 0.0F, 45F * f);
            this.hierMesh().chunkSetAngles("FlapWL_D0", 0.0F, 0.0F, 40F * f);
            this.hierMesh().chunkSetAngles("FlapWR_D0", 0.0F, 0.0F, 40F * f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1100F, -80F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 100F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 100F * f);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 45F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3) {
                if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 0, 1);
                if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 1, 1);
            }
            if (this.FM.AS.astateEngineStates[1] > 3) {
                if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 2, 1);
                if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 3, 1);
            }
            if (this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 2, 1);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if (this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
        }
        for (int i = 1; i < 5; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -25F) {
                    f = -25F;
                    flag = false;
                }
                if (f > 25F) {
                    f = 25F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 25F) {
                    f1 = 25F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -25F) {
                    f = -25F;
                    flag = false;
                }
                if (f > 25F) {
                    f = 25F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > -2F) {
                    f1 = -2F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 16F), shot);
            } else if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000F) this.FM.AS.setEngineStuck(shot.initiator, i);
                        if (World.Rnd().nextFloat() < shot.power / 50000F) this.FM.AS.hitEngine(shot.initiator, i, 2);
                    } else if (World.Rnd().nextFloat() < 0.04F) this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    else this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.9878F) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        if (World.Rnd().nextFloat() < shot.power / 48000F) this.FM.AS.hitEngine(shot.initiator, 0, 2);
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    this.getEnergyPastArmor(2.0F, shot);
                }
            } else if (s.endsWith("oil1")) {
                int j = s.charAt(5) - 49;
                if (this.getEnergyPastArmor(4.21F, shot) > 0.0F) {
                    this.FM.AS.hitOil(shot.initiator, j);
                    this.getEnergyPastArmor(0.42F, shot);
                }
            } else if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.6F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[k] == 0) {
                        this.FM.AS.hitTank(shot.initiator, k, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F) this.FM.AS.hitTank(shot.initiator, k, 2);
                }
            }
        } else if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) this.hitChunk("CF", shot);
            if (this.chunkDamageVisible("CN") < 2) this.hitChunk("CN", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 2) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 2) this.hitChunk("Rudder2", shot);
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 2) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 2) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 2) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 2) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xxgun")) {
            int j = s.charAt(7) - 49;
            if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                this.FM.AS.setJamBullets(0, j);
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a") || s.endsWith("a2")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b") || s.endsWith("b2")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else l = s.charAt(5) - 49;
            this.hitFlesh(l, shot, byte0);
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 2:
                this.FM.turret[1].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[2].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                if (this.hierMesh().isChunkVisible("Pilot2_D0")) {
                    this.hierMesh().chunkVisible("Pilot2_D0", false);
                    this.hierMesh().chunkVisible("Pilot2_D1", true);
                } else {
                    this.hierMesh().chunkVisible("Pilot2a_D0", false);
                    this.hierMesh().chunkVisible("Pilot2a_D0", true);
                }
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                break;
        }
    }

    public void update(float f) {
        if (Time.current() > this.tme) {
            this.tme = Time.current() + World.Rnd().nextLong(1000L, 5000L);
            if (this.FM.turret.length != 0) {
                if (this.FM.turret[0].bIsOperable != (this.radist[1] == 0)) {
                    Actor actor = this.FM.turret[0].target;
                    if (actor != null) this.setRadist(1, 1);
                }
                if (this.FM.turret[1].bIsOperable) {
                    Actor actor1 = this.FM.turret[1].target;
                    if (actor1 != null && Actor.isValid(actor1)) {
                        this.pos.getAbs(Aircraft.tmpLoc2);
                        actor1.pos.getAbs(Aircraft.tmpLoc3);
                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                        if (Aircraft.tmpLoc3.getPoint().z < 0.0D) this.setRadist(2, 1);
                    }
                }
            }
        }
        if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            for (int engineIndex = 0; engineIndex < 2; engineIndex++) {
                RealFlightModel realFlightModel = (RealFlightModel) this.FM;
                float rpm = this.FM.EI.engines[engineIndex].getRPM();
                if (rpm > 30) {
                    if (rpm < 300F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, (1500F - rpm) / 3000F);
                    } else if (rpm < 1000F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, (1500F - rpm) / 8000F);
                    } else if (rpm < 1500F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, 0.07F);
                    } else if (rpm < 2000F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, 0.05F);
                    } else if (rpm < 2300F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, 0.04F);
                    }
                }
            }
        }
        if (this.FM.getSpeedKMH() > 250F && this.FM.getVertSpeed() > 0.0F && this.FM.getAltitude() < 5000F) this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
        if (this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F) {
            if (this.FM.getSpeedKMH() > 390F && this.FM.getSpeedKMH() < 500F) {
                this.FM.SensPitch = 0.33F;
                this.FM.producedAM.y -= 400F * (300F - this.FM.getSpeedKMH());
            }
            if (this.FM.getSpeedKMH() >= 501F) {
                this.FM.SensPitch = 0.2F;
                this.FM.producedAM.y -= 250F * (300F - this.FM.getSpeedKMH());
            } else this.FM.SensPitch = 0.5F;
        }
        super.update(f);
    }

    private void setRadist(int i, int j) {
        this.radist[i] = j;
        if (this.FM.AS.astatePilotStates[i] <= 90) switch (i) {
            case 2:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2a_D0", false);
                this.FM.turret[0].bIsOperable = true;
                switch (j) {
                    case 0:
                        this.hierMesh().chunkVisible("Pilot2_D0", true);
                        this.FM.turret[0].bIsOperable = true;
                        break;

                    case 1:
                        this.hierMesh().chunkVisible("Pilot2a_D0", true);
                        this.FM.turret[0].bIsOperable = true;
                        break;
                }
                break;
        }
    }

    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurForwardAngle;
    public float          fSightSetForwardAngle;
    public float          fSightCurSideslip;
    private long          tme;
    private int           radist[];
    public boolean        bChangedExts;
    public static boolean bChangedPit = false;

    static {
        Class class1 = Yermolayev.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
