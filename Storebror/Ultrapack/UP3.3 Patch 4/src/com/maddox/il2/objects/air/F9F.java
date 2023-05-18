package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class F9F extends Scheme1 implements TypeFighter, TypeBNZFighter, TypeFighterAceMaker {

    public F9F() {
        this.oldthrl = -1F;
        this.curthrl = -1F;
        this.arrestor2 = 0.0F;
        this.AirBrakeControl = 0.0F;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.overrideBailout = false;
        this.ejectComplete = false;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        if (this.FM.AP.way.isLanding() && this.FM.getSpeed() > this.FM.VmaxFLAPS * 2D) this.FM.CT.AirBrakeControl = 1.0F;
        else if (this.FM.AP.way.isLanding() && this.FM.getSpeed() < this.FM.VmaxFLAPS * 1.5D) this.FM.CT.AirBrakeControl = 0.0F;
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) this.k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10F;
        if (this.k14Distance > 800F) this.k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10F;
        if (this.k14Distance < 200F) this.k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) this.k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) this.k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.k14Mode);
        netmsgguaranted.writeByte(this.k14WingspanType);
        netmsgguaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.k14Mode = netmsginput.readByte();
        this.k14WingspanType = netmsginput.readByte();
        this.k14Distance = netmsginput.readFloat();
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 12.2F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, 0.0F, 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -70F), 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(true);
        }
        this.moveWingFold(this.hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 70F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -70F * f, 0.0F);
        if (f < 0.5F) hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.5F, 0.0F, -90F), 0.0F);
        else hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.72F, 0.98F, -90F, 0.0F), 0.0F);
        if (f < 0.5F) hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.4F, 0.0F, 90F), 0.0F);
        else hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.72F, 0.98F, 90F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 70F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -70F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        this.hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
        if (this.FM.CT.GearControl > 0.5F) this.hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -60F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = 55F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, 0.0F, f1);
    }

    protected void moveFan(float f1) {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(13.35D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if (shot.power <= 0.0F) this.doRicochetBack(shot);
                } else if (s.endsWith("p2")) this.getEnergyPastArmor(8.77F, shot);
                else if (s.endsWith("g1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(40F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) this.doRicochetBack(shot);
                }
            } else if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
                    case 1:
                    case 2:
                        if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                    case 4:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                }
            } else if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("bloc")) this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if (s.endsWith("cams") && this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 20F) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    this.debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < shot.power / 24000F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        this.debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        this.debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if (s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    this.debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if (!s.endsWith("exht"));
            } else if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F) {
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                        this.debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
            } else if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else if (s.startsWith("xxhyd")) this.FM.AS.setInternalDamage(shot.initiator, 3);
            else if (s.startsWith("xxpnm")) this.FM.AS.setInternalDamage(shot.initiator, 1);
        } else {
            if (s.startsWith("xcockpit")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                this.getEnergyPastArmor(0.05F, shot);
            }
            if (s.startsWith("xxhispa1") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 0);
            if (s.startsWith("xxhispa2") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 1);
            if (s.startsWith("xxhispa3") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 2);
            if (s.startsWith("xxhispa4") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 3);
            if (s.startsWith("xcf")) this.hitChunk("CF", shot);
            else if (s.startsWith("xnose")) this.hitChunk("Nose", shot);
            else if (s.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
            } else if (s.startsWith("xkeel")) {
                if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
            } else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
            else if (s.startsWith("xstab")) {
                if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
                if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
            } else if (s.startsWith("xvator")) {
                if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
                if (s.startsWith("xvatorr")) this.hitChunk("VatorR", shot);
            } else if (s.startsWith("xwing")) {
                if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
                if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
                if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
                if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
                if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
                if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
            } else if (s.startsWith("xarone")) {
                if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
                if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
            } else if (s.startsWith("xgear")) {
                if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
                    this.debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int k;
                if (s.endsWith("a")) {
                    byte0 = 1;
                    k = s.charAt(6) - 49;
                } else if (s.endsWith("b")) {
                    byte0 = 2;
                    k = s.charAt(6) - 49;
                } else k = s.charAt(5) - 49;
                this.hitFlesh(k, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.EI.engines[0].setEngineDies(actor);
                return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void engineSurge(float f) {
        if (this.FM.AS.isMaster()) if (this.curthrl == -1F) this.curthrl = this.oldthrl = this.FM.EI.engines[0].getControlThrottle();
        else {
            this.curthrl = this.FM.EI.engines[0].getControlThrottle();
            if (this.curthrl < 1.05F) {
                if ((this.curthrl - this.oldthrl) / f > 20F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F) {
                    if (((Interpolate) this.FM).actor == World.getPlayerAircraft()) HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                    this.playSound("weapon.MGunMk108s", true);
                    this.engineSurgeDamage += 0.01D * (this.FM.EI.engines[0].getRPM() / 1000F);
                    this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
                    if (World.Rnd().nextFloat() < 0.05F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.AS.hitEngine(this, 0, 100);
                    if (World.Rnd().nextFloat() < 0.05F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.EI.engines[0].setEngineDies(this);
                }
                if ((this.curthrl - this.oldthrl) / f < -20F && (this.curthrl - this.oldthrl) / f > -100F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6) {
                    this.playSound("weapon.MGunMk108s", true);
                    this.engineSurgeDamage += 0.001D * (this.FM.EI.engines[0].getRPM() / 1000F);
                    this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
                    if (World.Rnd().nextFloat() < 0.4F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
                        if (((Interpolate) this.FM).actor == World.getPlayerAircraft()) HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                        this.FM.EI.engines[0].setEngineStops(this);
                    } else if (((Interpolate) this.FM).actor == World.getPlayerAircraft()) HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                }
            }
            this.oldthrl = this.curthrl;
        }
    }

    public void update(float f) {
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && this.FM.getSpeedKMH() > 15F) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            this.bailout();
        }
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) if (this.FM.EI.engines[0].getPowerOutput() > 0.5F && this.FM.EI.engines[0].getStage() == 6) {
            if (this.FM.EI.engines[0].getPowerOutput() > 0.75F) this.FM.AS.setSootState(this, 0, 3);
            else this.FM.AS.setSootState(this, 0, 2);
        } else this.FM.AS.setSootState(this, 0, 0);
        if (this.FM.CT.getArrestor() > 0.9F) if (this.FM.Gears.arrestorVAngle != 0.0F) {
            this.arrestor2 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -65F, 3F, 45F, -23F);
            this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, this.arrestor2, 0.0F);
            if (this.FM.Gears.arrestorVAngle < -35F);
        } else {
            float f1 = -41F * this.FM.Gears.arrestorVSink;
            if (f1 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f1 > 0.0F && this.FM.CT.getArrestor() < 0.9F) f1 = 0.0F;
            if (f1 > 6.2F) f1 = 6.2F;
            this.arrestor2 += f1;
            if (this.arrestor2 < -23F) this.arrestor2 = -23F;
            else if (this.arrestor2 > 45F) this.arrestor2 = 45F;
            this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, this.arrestor2, 0.0F);
        }
        this.engineSurge(f);
        super.update(f);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -60F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -60F * f, 0.0F);
    }

    public void moveCockpitDoor(float paramFloat) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.95F, 0.0F, 0.9F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f = (float) Math.sin(Aircraft.cvt(paramFloat, 0.4F, 0.99F, 0.0F, (float) Math.PI));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f);
        this.hierMesh().chunkSetAngles("Head1_D0", 14F * f, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
            this.setDoorSnd(paramFloat);
        }
    }

    private void bailout() {
        if (this.overrideBailout) if (this.FM.AS.astateBailoutStep >= 0 && this.FM.AS.astateBailoutStep < 2) {
            if (this.FM.CT.cockpitDoorControl > 0.5F && this.FM.CT.getCockpitDoor() > 0.5F) {
                this.FM.AS.astateBailoutStep = 11;
                this.doRemoveBlisters();
            } else this.FM.AS.astateBailoutStep = 2;
        } else if (this.FM.AS.astateBailoutStep >= 2 && this.FM.AS.astateBailoutStep <= 3) {
            switch (this.FM.AS.astateBailoutStep) {
                case 2:
                    if (this.FM.CT.cockpitDoorControl < 0.5F) this.doRemoveBlister1();
                    break;

                case 3:
                    this.doRemoveBlisters();
                    break;
            }
            if (this.FM.AS.isMaster()) this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
            AircraftState tmp178_177 = this.FM.AS;
            tmp178_177.astateBailoutStep = (byte) (tmp178_177.astateBailoutStep + 1);
            if (this.FM.AS.astateBailoutStep == 4) this.FM.AS.astateBailoutStep = 11;
        } else if (this.FM.AS.astateBailoutStep >= 11 && this.FM.AS.astateBailoutStep <= 19) {
            int i = this.FM.AS.astateBailoutStep;
            if (this.FM.AS.isMaster()) this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
            AircraftState tmp383_382 = this.FM.AS;
            tmp383_382.astateBailoutStep = (byte) (tmp383_382.astateBailoutStep + 1);
            if (i == 11) {
                this.FM.setTakenMortalDamage(true, null);
                if (this.FM instanceof Maneuver && ((Maneuver) this.FM).get_maneuver() != 44) {
                    World.cur();
                    if (this.FM.AS.actor != World.getPlayerAircraft()) ((Maneuver) this.FM).set_maneuver(44);
                }
            }
            if (this.FM.AS.astatePilotStates[i - 11] < 99) {
                this.doRemoveBodyFromPlane(i - 10);
                if (i == 11) {
                    this.doEjectCatapult();
                    this.FM.setTakenMortalDamage(true, null);
                    this.FM.CT.WeaponControl[0] = false;
                    this.FM.CT.WeaponControl[1] = false;
                    this.FM.AS.astateBailoutStep = -1;
                    this.overrideBailout = false;
                    this.FM.AS.bIsAboutToBailout = true;
                    this.ejectComplete = true;
                    if (i > 10 && i <= 19) EventLog.onBailedOut(this, i - 11);
                }
            }
        }
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (!Actor.isValid(aircraft)) return;
                else {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(2, loc, vector3d, aircraft);
                    return;
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    private final void doRemoveBlister1() {
        if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1 && this.FM.AS.getPilotHealth(0) > 0.0F) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            localWreckage.collide(false);
            Vector3d localVector3d = new Vector3d();
            localVector3d.set(this.FM.Vwld);
            localWreckage.setSpeed(localVector3d);
        }
    }

    private final void doRemoveBlisters() {
        for (int i = 2; i < 10; i++)
            if (this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && this.FM.AS.getPilotHealth(i - 1) > 0.0F) {
                this.hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister" + i + "_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(this.FM.Vwld);
                localWreckage.setSpeed(localVector3d);
            }

    }

    private float         oldthrl;
    private float         curthrl;
    private float         engineSurgeDamage;
    private boolean       overrideBailout;
    private boolean       ejectComplete;
    public static boolean bChangedPit = false;
    private float         arrestor2;
    public float          AirBrakeControl;
    public int            k14Mode;
    public int            k14WingspanType;
    public float          k14Distance;

    static {
        Class class1 = F9F.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
