package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public abstract class Sukhoi_15 extends Scheme2 implements TypeSupersonic, TypeFastJet, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit {

    public Sukhoi_15() {
        this.SonicBoom = 0.0F;
        this.fxSirena = this.newSound("aircraft.Sirena2", false);
        this.smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        this.sirenaSoundPlaying = false;
        this.bHasSK1Seat = true;
        this.gearLightsEffects = new Eff3DActor[3];
        this.gearLightsLights = new LightPointActor[3];
        this.bGearLightsOn = false;
        this.bGearLightState = false;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lTimeNextEject = 0L;
        this.oldthrl = -1F;
        this.curthrl = -1F;
        this.smplSirena.setInfinite(true);
        this.radarmode = 0;
        this.targetnum = 0;
        this.lockrange = 0.018F;
        this.APmode1 = false;
        this.APmode2 = false;
        this.llpos = 0.0F;
        this.dosel = true;
        this.dosel2 = true;
        this.guidedMissileUtils = null;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
        this.counter = 0;
    }

    public void typeRadarGainMinus() {
        if (this.radarmode == 0) {
            this.lockrange -= 0.002F;
            if (this.lockrange < -0.01F) {
                this.lockrange = -0.01F;
            }
        } else {
            this.targetnum--;
            if (this.targetnum < 0) {
                this.targetnum = 0;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + this.targetnum);
        }
    }

    public void typeRadarGainPlus() {
        if (this.radarmode == 0) {
            this.lockrange += 0.002F;
            if (this.lockrange > 0.018F) {
                this.lockrange = 0.018F;
            }
        } else {
            this.targetnum++;
            if (this.targetnum > 10) {
                this.targetnum = 0;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + this.targetnum);
        }
    }

    public void typeRadarRangeMinus() {
    }

    public void typeRadarRangePlus() {
    }

    public void typeRadarReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeRadarReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeRadarToggleMode() {
        long l = Time.current();
        if (this.radarmode == 1) {
            this.radarmode++;
        }
        if ((this.radarmode == 0) && (l > (this.twait + 5000L))) {
            this.radarmode++;
            this.twait = l;
        }
        if (this.radarmode > 1) {
            this.radarmode = 0;
        }
        return false;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(Sukhoi_15.NEG_G_TOLERANCE_FACTOR, Sukhoi_15.NEG_G_TIME_FACTOR, Sukhoi_15.NEG_G_RECOVERY_FACTOR, Sukhoi_15.POS_G_TOLERANCE_FACTOR, Sukhoi_15.POS_G_TIME_FACTOR, Sukhoi_15.POS_G_RECOVERY_FACTOR);
    }

    public void rareAction(float f, boolean flag) {
        if ((this.counter++ % 5) == 0) {
            this.RP15();
        }
        super.rareAction(f, flag);
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.0F)) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10F;
        if (this.k14Distance > 800F) {
            this.k14Distance = 800F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10F;
        if (this.k14Distance < 200F) {
            this.k14Distance = 200F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
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

    public void typeFighterAceMakerRangeFinder() {
        if (this.k14Mode == 0) {
            return;
        }
        Sukhoi_15.hunted = Main3D.cur3D().getViewPadlockEnemy();
        if (Sukhoi_15.hunted == null) {
            Sukhoi_15.hunted = War.GetNearestEnemyAircraft(this, 2000F, 9);
        }
        if (Sukhoi_15.hunted != null) {
            this.k14Distance = (float) this.pos.getAbsPoint().distance(Sukhoi_15.hunted.pos.getAbsPoint());
            if (this.k14Distance > 1700F) {
                this.k14Distance = 1700F;
            } else if (this.k14Distance < 200F) {
                this.k14Distance = 200F;
            }
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Glass_Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        if (f > 0.0F) {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -16.5F * this.FM.CT.getElevator(), 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -16.5F * this.FM.CT.getElevator(), 0.0F);
        } else if (f < 0.0F) {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -7F * this.FM.CT.getElevator(), 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -7F * this.FM.CT.getElevator(), 0.0F);
        }
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -44.5F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("AirbrakeL", -35F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("AirbrakeL_1", -35F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("AirbrakeR", 35F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("AirbrakeR_1", 35F * f, 0.0F, 0.0F);
    }

    protected void moveFan(float f) {
    }

    public void moveShockCone() {
        float f = this.calculateMach();
        Aircraft.xyz[0] = Aircraft.cvt(f / 1.9F, 0.0F, 1.0F, 0.0F, 1.2F);
        this.hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f) {
        if (this.FM.Gears.onGround() && (this.FM.getSpeed() < 56F)) {
            this.resetYPRmodifier();
            if (f < 0.1F) {
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, 0.1F);
                Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, 0.1F);
            } else {
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.17F, 0.99F, 0.1F, 1.0F);
                Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, 0.1F);
            }
            this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
            if (Config.isUSE_RENDER()) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                }
                this.setDoorSnd(f);
            }
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(13.35D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(8.77F, shot);
                } else if (s.endsWith("g1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(40F, 60F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(1.1F, shot) > 0.0F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                    case 4:
                        if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("bloc")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                if (s.endsWith("cams") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 20F))) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    this.debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        this.debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.75F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        this.debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if (s.endsWith("eqpt") && (World.Rnd().nextFloat() < (shot.power / 24000F))) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    this.debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if (s.endsWith("exht")) {

                }
            } else if (s.startsWith("xxeng2")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("bloc")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                if (s.endsWith("cams") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[1].getCylindersRatio() * 20F))) {
                    this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    this.debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        this.debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.75F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        this.debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if (s.endsWith("eqpt") && (World.Rnd().nextFloat() < (shot.power / 24000F))) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    this.debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if (s.endsWith("exht")) {

                }
                return;
            }
            if (s.startsWith("xxmgun0")) {
                Aircraft.debugprintln(this, "Armament: Gunpod Disabled..");
                this.FM.AS.setJamBullets(0, 0);
                this.FM.AS.setJamBullets(0, 1);
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.075F)) {
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                        this.debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxhyd")) {
                this.FM.AS.setInternalDamage(shot.initiator, 3);
                return;
            }
            if (s.startsWith("xxpnm")) {
                this.FM.AS.setInternalDamage(shot.initiator, 1);
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcockpit")) {
            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            this.getEnergyPastArmor(0.05F, shot);
        }
        if (s.startsWith("xcf")) {
            this.hitChunk("CF", shot);
        } else if (s.startsWith("xnose")) {
            this.hitChunk("Nose", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl")) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr")) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner")) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xflap")) {
            if (s.startsWith("xflap1")) {
                this.hitChunk("Flap01", shot);
            }
            if (s.startsWith("xflap2")) {
                this.hitChunk("Flap02", shot);
            }
            if (World.Rnd().nextFloat() < 0.4F) {
                this.FM.CT.bHasFlapsControlRed = true;
            }
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
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
            } else {
                k = s.charAt(5) - 49;
            }
            this.hitFlesh(k, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                for (int k = 0; k < 2; k++) {
                    this.FM.CT.bHasAirBrakeControl = false;
                    this.FM.EI.engines[k].setEngineDies(actor);
                }

                // fall through

            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public float getAirPressure(float f) {
        float f1 = 1.0F - ((0.0065F * f) / 288.15F);
        float f2 = 5.255781F;
        return 101325F * (float) Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f) {
        return this.getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f) {
        return (this.getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - (0.0065F * f)));
    }

    public float getAirDensityFactor(float f) {
        return this.getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f) {
        f /= 1000F;
        int i = 0;
        for (i = 0; i < TypeSupersonic.fMachAltX.length; i++) {
            if (TypeSupersonic.fMachAltX[i] > f) {
                break;
            }
        }

        if (i == 0) {
            return TypeSupersonic.fMachAltY[0];
        } else {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + (f2 * f5);
        }
    }

    public float getMpsFromKmh(float f) {
        return f / 3.6F;
    }

    public float calculateMach() {
        return this.FM.getSpeedKMH() / this.getMachForAlt(this.FM.getAltitude());
    }

    public void soundbarier() {
        float f = this.getMachForAlt(this.FM.getAltitude()) - this.FM.getSpeedKMH();
        if (f < 0.5F) {
            f = 0.5F;
        }
        float f1 = this.FM.getSpeedKMH() - this.getMachForAlt(this.FM.getAltitude());
        if (f1 < 0.5F) {
            f1 = 0.5F;
        }
        if (this.calculateMach() <= 1.0D) {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f;
            this.SonicBoom = 0.0F;
            this.isSonic = false;
        }
        if (this.calculateMach() >= 1.0D) {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f1;
            this.isSonic = true;
        }
        if (this.FM.VmaxAllowed > 1500F) {
            this.FM.VmaxAllowed = 1500F;
        }
        if (this.isSonic && (this.SonicBoom < 1.0F)) {
            this.playSound("aircraft.SonicBoom", true);
            this.playSound("aircraft.SonicBoomInternal", true);
            if (this == World.getPlayerAircraft()) {
                HUD.log("Mach 1 Exceeded!");
            }
            if (Config.isUSE_RENDER() && (World.Rnd().nextFloat() < this.getAirDensityFactor(this.FM.getAltitude()))) {
                this.shockwave = Eff3DActor.New(this, this.findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            }
            this.SonicBoom = 1.0F;
        }
        if ((this.calculateMach() > 1.01D) || (this.calculateMach() < 1.0D)) {
            Eff3DActor.finish(this.shockwave);
        }
    }

    public void engineSurge(float f) {
        if (this.FM.AS.isMaster()) {
            for (int i = 0; i < 2; i++) {
                if (this.curthrl == -1F) {
                    this.curthrl = this.oldthrl = this.FM.EI.engines[i].getControlThrottle();
                } else {
                    this.curthrl = this.FM.EI.engines[i].getControlThrottle();
                    if (this.curthrl < 1.05F) {
                        if ((((this.curthrl - this.oldthrl) / f) > 20F) && (this.FM.EI.engines[i].getRPM() < 3200F) && (this.FM.EI.engines[i].getStage() == 6) && (World.Rnd().nextFloat() < 0.4F)) {
                            if (this == World.getPlayerAircraft()) {
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            }
                            this.playSound("weapon.MGunMk108s", true);
                            this.engineSurgeDamage += 0.01D * (this.FM.EI.engines[i].getRPM() / 1000F);
                            this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - this.engineSurgeDamage);
                            if ((World.Rnd().nextFloat() < 0.05F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                                this.FM.AS.hitEngine(this, i, 100);
                            }
                            if ((World.Rnd().nextFloat() < 0.05F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                                this.FM.EI.engines[i].setEngineDies(this);
                            }
                        }
                        if ((((this.curthrl - this.oldthrl) / f) < -20F) && (((this.curthrl - this.oldthrl) / f) > -100F) && (this.FM.EI.engines[i].getRPM() < 3200F) && (this.FM.EI.engines[i].getStage() == 6)) {
                            this.playSound("weapon.MGunMk108s", true);
                            this.engineSurgeDamage += 0.001D * (this.FM.EI.engines[i].getRPM() / 1000F);
                            this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - this.engineSurgeDamage);
                            if ((World.Rnd().nextFloat() < 0.4F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                                if (this == World.getPlayerAircraft()) {
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                }
                                this.FM.EI.engines[i].setEngineStops(this);
                            } else if (this == World.getPlayerAircraft()) {
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            }
                        }
                    }
                    this.oldthrl = this.curthrl;
                }
            }

        }
    }

    private boolean sirenaWarning() {
        if (this != World.getPlayerAircraft()) {
            return false;
        }
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if ((aircraft1 instanceof Aircraft) && (aircraft1.getArmy() != World.getPlayerArmy()) && (aircraft1 instanceof TypeFighterAceMaker) && ((aircraft1 instanceof TypeSupersonic) || (aircraft1 instanceof TypeFastJet)) && (aircraft1 != World.getPlayerAircraft()) && (aircraft1.getSpeed(vector3d) > 20D)) {
            this.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().z;
            double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float) Math.atan2(d8, -d7);
            int k = (int) (Math.floor((int) f) - 90D);
            if (k < 0) {
                k += 360;
            }
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
            float f1 = 57.32484F * (float) Math.atan2(i1, d11);
            int j1 = (int) (Math.floor((int) f1) - 90D);
            if (j1 < 0) {
                j1 += 360;
            }
            int k1 = j1 - j;
            int l1 = (int) (Math.ceil((i1 * 3.28084D) / 100D) * 100D);
            if (l1 >= 5280) {
                l1 = (int) Math.floor(l1 / 5280);
            }
            this.bRadarWarning = (i1 <= 3000D) && (i1 >= 50D) && (k1 >= 195) && (k1 <= 345) && (Math.sqrt(l * l) >= 120D);
            this.playSirenaWarning(this.bRadarWarning);
        } else {
            this.bRadarWarning = false;
            this.playSirenaWarning(this.bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag) {
        if (this != World.getPlayerAircraft()) {
            return;
        }
        if (flag && !this.sirenaSoundPlaying) {
            this.fxSirena.play(this.smplSirena);
            this.sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else if (!flag && this.sirenaSoundPlaying) {
            this.fxSirena.cancel();
            this.sirenaSoundPlaying = false;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        this.bHasSK1Seat = false;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.CT.bHasRefuelControl = true;
    }

    private float flapsMovement(float f, float f1, float f2, float f3, float f4) {
        float f5 = (float) Math.exp(-f / f3);
        float f6 = f1 + ((f2 - f1) * f5);
        if (f6 < f1) {
            f6 += f4 * f;
            if (f6 > f1) {
                f6 = f1;
            }
        } else if (f6 > f1) {
            f6 -= f4 * f;
            if (f6 < f1) {
                f6 = f1;
            }
        }
        return f6;
    }

    public void update(float f) {
        super.update(f);
        this.computeLift();
        this.computeSupersonicLimiter();
        this.computeSubsonicLimiter();
        if (this.FM.AS.bLandingLightOn) {
            if (this.llpos < 1.0F) {
                this.llpos += 0.5F * f;
                this.hierMesh().chunkSetAngles("LampGearL_D0", 0.0F, -90F * this.llpos, 0.0F);
                this.hierMesh().chunkSetAngles("LampGearR_D0", 0.0F, -90F * this.llpos, 0.0F);
            }
        } else if (this.llpos > 0.0F) {
            this.llpos -= 0.5F * f;
            this.hierMesh().chunkSetAngles("LampGearL_D0", 0.0F, -90F * this.llpos, 0.0F);
            this.hierMesh().chunkSetAngles("LampGearR_D0", 0.0F, -90F * this.llpos, 0.0F);
        }
        if (this.FM.isPlayers() && this.FM.Gears.onGround() && this.dosel && !this.FM.AS.bIsAboutToBailout) {
            this.FM.CT.cockpitDoorControl = 0.0F;
            this.FM.CT.getCockpitDoor();
            this.dosel = false;
            this.dosel2 = false;
        } else if (!this.FM.isPlayers() && this.FM.Gears.onGround() && !this.FM.AS.bIsAboutToBailout) {
            if ((this.FM.EI.engines[0].getRPM() < 100F) || (this.FM.EI.engines[1].getRPM() < 100F)) {
                this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
                this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if ((this.FM.CT.cockpitDoorControl != 0.0F) && !this.FM.AS.bIsAboutToBailout) {
            if (!this.FM.Gears.onGround()) {
                this.FM.CT.cockpitDoorControl = 0.0F;
                this.FM.CT.forceCockpitDoor(0.0F);
                World.cur();
                if (this == World.getPlayerAircraft()) {
                    HUD.log("CockpitDoorCLS");
                }
            }
            if ((f1 > 56F) && this.FM.Gears.onGround()) {
                this.FM.CT.cockpitDoorControl = 0.0F;
                if ((this.FM.CT.cockpitDoorControl < 0.1F) && (this.FM.CT.getCockpitDoor() < 0.1F)) {
                    this.FM.CT.cockpitDoorControl = 0.0F;
                    this.FM.CT.forceCockpitDoor(0.0F);
                }
                World.cur();
                if ((this == World.getPlayerAircraft()) && this.dosel2) {
                    HUD.log("CockpitDoorCLS");
                }
            }
            this.dosel2 = true;
        }
        if ((this.FM.getSpeedKMH() > 500F) && this.FM.CT.bHasFlapsControl) {
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
        } else {
            this.FM.CT.bHasFlapsControl = true;
        }
        if (this.FM.CT.FlapsControl < 0.0F) {
            this.FM.CT.FlapsControl = 0.0F;
        }
        if (this.FM.CT.FlapsControl > 1.0F) {
            this.FM.CT.FlapsControl = 1.0F;
        }
        if (this.FM.CT.getFlap() < this.FM.CT.FlapsControl) {
            this.FM.CT.forceFlaps(this.flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        } else if (this.FM.CT.getFlap() > this.FM.CT.FlapsControl) {
            this.FM.CT.forceFlaps(this.flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        }
        for (int i = 1; i < 19; i++) {
            this.hierMesh().chunkSetAngles("EngineExhaustFlap" + i, 0.0F, -38F * this.FM.CT.getPowerControl(), 0.0F);
        }

        this.resetYPRmodifier();
        float f2 = this.FM.CT.getPowerControl() * 1.5F;
        Aircraft.xyz[0] = Aircraft.cvt(f2, 0.0F, 1.5F, 0.0F, 1.5F);
        this.hierMesh().chunkSetLocate("EffectBox", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.calculateMach() / 1.9F, 0.0F, 1.0F, 0.0F, 0.15F);
        this.hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
        float f3 = this.FM.getSpeedKMH() - 1000F;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        this.FM.CT.dvGear = 0.2F - (f3 / 1000F);
        if (this.FM.CT.dvGear < 0.0F) {
            this.FM.CT.dvGear = 0.0F;
        }
        try {
            this.sirenaWarning();
        } catch (NullPointerException nullpointerexception) {
        }
        this.engineSurge(f);
        this.typeFighterAceMakerRangeFinder();
        this.soundbarier();
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            for (int j = 0; j < 2; j++) {
                if ((this.FM.EI.engines[j].getThrustOutput() > 0.5F) && (this.FM.EI.engines[j].getStage() == 6)) {
                    if (this.FM.EI.engines[j].getThrustOutput() > 0.5F) {
                        if (this.FM.EI.engines[j].getThrustOutput() > 1.001F) {
                            this.FM.AS.setSootState(this, j, 5);
                        } else {
                            this.FM.AS.setSootState(this, j, 3);
                        }
                    }
                } else {
                    this.FM.AS.setSootState(this, j, 0);
                }
            }

        }
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            if (this.FM.AP.way.isLanding() && this.FM.Gears.onGround() && (this.FM.getSpeed() > 40F)) {
                this.FM.CT.AirBrakeControl = 1.0F;
                if (this.FM.CT.bHasDragChuteControl) {
                    this.FM.CT.DragChuteControl = 1.0F;
                }
            }
            if (this.FM.AP.way.isLanding() && this.FM.Gears.onGround() && (this.FM.getSpeed() < 40F)) {
                this.FM.CT.AirBrakeControl = 0.0F;
                if (this.FM.getSpeed() < 20F) {
                    this.FM.CT.DragChuteControl = 0.0F;
                }
            }
        }
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && (this.FM.getSpeedKMH() > 15F)) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            if (Time.current() > this.lTimeNextEject) {
                this.bailout();
            }
        }
        if (this.FM.AS.bNavLightsOn && (this.FM.CT.getGear() >= 1.0F)) {
            this.bGearLightsOn = true;
        } else {
            this.bGearLightsOn = false;
        }
        if (this.bGearLightState != this.bGearLightsOn) {
            this.doSetGearLightsState(this.bGearLightsOn);
        }
        if (Config.isUSE_RENDER()) {
            if (!this.FM.AS.bIsAboutToBailout) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null) && Main3D.cur3D().cockpits[0].cockpitDimControl) {
                    this.hierMesh().chunkVisible("Head1_D0", false);
                    this.hierMesh().chunkVisible("Glass_Head1_D0", true);
                } else {
                    this.hierMesh().chunkVisible("Head1_D0", true);
                    this.hierMesh().chunkVisible("Glass_Head1_D0", false);
                }
            } else {
                this.hierMesh().chunkVisible("Glass_Head1_D0", false);
            }
        }
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteMiG21/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(1.0F);
            this.chute.pos.setRel(new Point3d(-11.5D, 0.0D, 1.0D), new Orient(0.0F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                    this.chute.pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                }
                this.chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute.destroy();
        }
        this.guidedMissileUtils.update();
    }

    private void doSetGearLightsState(boolean flag) {
        for (int i = 0; i < this.gearLightsEffects.length; i++) {
            if (this.gearLightsEffects[i] != null) {
                Eff3DActor.finish(this.gearLightsEffects[i]);
                this.gearLightsLights[i].light.setEmit(0.0F, 0.0F);
            }
            this.gearLightsEffects[i] = null;
        }

        if (flag) {
            this.gearLightsEffects[0] = Eff3DActor.New(this, this.findHook("_GearLightC"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            this.gearLightsEffects[1] = Eff3DActor.New(this, this.findHook("_GearLightL"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            this.gearLightsEffects[2] = Eff3DActor.New(this, this.findHook("_GearLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            for (int j = 0; j < this.gearLightsEffects.length; j++) {
                Loc loc = new Loc();
                Loc loc1 = new Loc(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                if (j == 0) {
                    this.findHook("_GearLightC").computePos(this, loc, loc1);
                }
                if (j == 1) {
                    this.findHook("_GearLightL").computePos(this, loc, loc1);
                }
                if (j == 2) {
                    this.findHook("_GearLightR").computePos(this, loc, loc1);
                }
                Point3d point3d = loc1.getPoint();
                this.gearLightsLights[j] = new LightPointActor(new LightPoint(), point3d);
                this.gearLightsLights[j].light.setColor(0.5F, 0.6F, 0.7F);
                this.gearLightsLights[j].light.setEmit(1.0F, 2.5F);
                this.draw.lightMap().put("_GearLightC", this.gearLightsLights[0]);
                this.draw.lightMap().put("_GearLightL", this.gearLightsLights[1]);
                this.draw.lightMap().put("_GearLightR", this.gearLightsLights[2]);
            }

            this.bGearLightState = true;
        } else {
            this.bGearLightState = flag;
        }
    }

    public void doSetSootState(int i, int j) {
        for (int k = 0; k < 2; k++) {
            if (this.FM.AS.astateSootEffects[i][k] != null) {
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            }
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        switch (j) {
            case 1:
            case 2:
            case 3:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                break;

            case 5:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 2.5F, "3DO/Effects/Aircraft/afterburner.eff", -1F);
                // fall through

            case 4:
            default:
                return;
        }
    }

    public void doEjectCatapult(final int i) {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 75D - (i * 10D));
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat0" + i);
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat" + i + "_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    private void bailout() {
        if (this.overrideBailout) {
            if ((this.FM.AS.astateBailoutStep >= 0) && (this.FM.AS.astateBailoutStep < 2)) {
                if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.CT.getCockpitDoor() > 0.5F) && (this.FM.getSpeedKMH() < 15F)) {
                    this.FM.AS.astateBailoutStep = 11;
                } else {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 2) && (this.FM.AS.astateBailoutStep <= 3)) {
                switch (this.FM.AS.astateBailoutStep) {
                    case 2:
                        if ((this.FM.CT.cockpitDoorControl < 0.5F) || (this.FM.getSpeedKMH() > 15F)) {
                            this.doRemoveBlister1();
                        }
                        break;

                    case 3:
                        this.lTimeNextEject = Time.current() + 1000L;
                        this.doRemoveBlister2();
                        break;
                }
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                AircraftState aircraftstate = this.FM.AS;
                aircraftstate.astateBailoutStep = (byte) (aircraftstate.astateBailoutStep + 1);
                if (this.FM.AS.astateBailoutStep == 4) {
                    this.FM.AS.astateBailoutStep = 11;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 11) && (this.FM.AS.astateBailoutStep <= 19)) {
                byte byte0 = this.FM.AS.astateBailoutStep;
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                AircraftState aircraftstate1 = this.FM.AS;
                aircraftstate1.astateBailoutStep = (byte) (aircraftstate1.astateBailoutStep + 1);
                if (byte0 == 11) {
                    this.FM.setTakenMortalDamage(true, null);
                    if ((this.FM instanceof Maneuver) && (((Maneuver) this.FM).get_maneuver() != 44)) {
                        World.cur();
                        if (this.FM.AS.actor != World.getPlayerAircraft()) {
                            ((Maneuver) this.FM).set_maneuver(44);
                        }
                    }
                }
                if (this.FM.AS.astatePilotStates[byte0 - 11] < 99) {
                    this.doRemoveBodyFromPlane(byte0 - 10);
                    if (byte0 == 11) {
                        this.doEjectCatapult(byte0 - 10);
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.lTimeNextEject = Time.current() + 1000L;
                        if (!this.bTwoSeat) {
                            this.FM.AS.astateBailoutStep = -1;
                            this.overrideBailout = false;
                            this.FM.AS.bIsAboutToBailout = true;
                            this.ejectComplete = true;
                            if ((byte0 > 10) && (byte0 <= 19)) {
                                EventLog.onBailedOut(this, byte0 - 11);
                            }
                        }
                    } else if (this.bTwoSeat && (byte0 == 12)) {
                        this.doEjectCatapult(byte0 - 10);
                        this.FM.AS.astateBailoutStep = 51;
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        this.overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        this.ejectComplete = true;
                    }
                    this.FM.AS.astatePilotStates[byte0 - 11] = 99;
                } else {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + this.FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
        }
    }

    private final void doRemoveBlister1() {
        if ((this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            if (!this.bHasSK1Seat) {
                Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
        }
    }

    private final void doRemoveBlister2() {
        if ((this.hierMesh().chunkFindCheck("Blister2_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister2_D0");
            if (!this.bHasSK1Seat) {
                Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister2_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("Glass_Head1_D0", false);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Sukhoi_15.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Sukhoi_15.bChangedPit = true;
        }
    }

    public void computeLift() {
        Polares polares = (Polares) Reflection.getValue(this.FM, "Wing");
        float f = this.calculateMach();
        if (f < 0.9F) {
            polares.lineCyCoeff = 0.065F;
        } else if (f < 2.0F) {
            float f1 = f * f;
            float f2 = f1 * f;
            float f3 = f2 * f;
            polares.lineCyCoeff = ((((0.049062F * f3) - (0.366811F * f2)) + (1.02505F * f1)) - (1.27876F * f)) + 0.620805F;
        } else {
            polares.lineCyCoeff = 0.014F;
        }
    }

    public void computeSupersonicLimiter() {
        float f = super.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6) && (this.calculateMach() >= 1.0D)) {
            if (f > 4F) {
                f1 = 0.0F;
            } else {
                f1 = 0.0001F - (2.5E-005F * f);
            }
        }
        this.FM.Sq.dragParasiteCx += f1;
    }

    public void computeSubsonicLimiter() {
        if ((this.FM.EI.engines[0].getThrustOutput() < 1.001F) && (this.calculateMach() >= 0.97D)) {
            this.FM.Sq.dragParasiteCx += 0.00005F;
        }
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            if (!this.APmode1) {
                this.APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else if (this.APmode1) {
                this.APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        }
        if (i == 21) {
            if (!this.APmode2) {
                this.APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else if (this.APmode2) {
                this.APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public void setCommonThreatActive() {
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 1) {
            this.k14Mode = 0;
        }
        if (this == World.getPlayerAircraft()) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: " + Sukhoi_15.k14Modes[this.k14Mode]);
        }
        return true;
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
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

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -80F);
        float f2 = Math.max(-f * 1500F, -100F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -f2);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, f2);
    }

    protected void moveGear(float f) {
        Sukhoi_15.moveGear(this.hierMesh(), f);
    }

    private boolean RP15() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof Sukhoi_15) {
            flag1 = true;
        }
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int i = -(int) (aircraft.pos.getAbsOrient().getYaw() - 90F);
        if (i < 0) {
            i += 360;
        }
        int j = -(int) (aircraft.pos.getAbsOrient().getPitch() - 90F);
        if (j < 0) {
            j += 360;
        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D)) {
                this.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                Engine.land();
                String s = "level with us";
                if ((d2 - d6 - 300D) >= 0.0D) {
                    s = "below us";
                }
                if (((d2 - d6) + 300D) <= 0.0D) {
                    s = "above us";
                }
                if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                    s = "slightly below";
                }
                if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) {
                    s = "slightly above";
                }
                new String();
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = (float) Math.toDegrees(Math.atan2(d9, -d8));
                int k = (int) (Math.floor((int) f) - 90D);
                if (k < 0) {
                    k += 360;
                }
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((random.nextInt(20) - 10F) / 100F) + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 25000F;
                float f3 = f2;
                if (d3 < 1500D) {
                    f3 = (float) (d3 * 0.8D * 3D);
                }
                int j1 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f1) / 10D) * 10D);
                if (j1 > f2) {
                    j1 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                }
                float f4 = 57.32484F * (float) Math.atan2(j1, d7);
                int k1 = (int) (Math.floor((int) f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int) f2;
                if (j1 < f2) {
                    if (j1 > 1150) {
                        i2 = (int) (Math.ceil(j1 / 900D) * 900D);
                    } else {
                        i2 = (int) (Math.ceil(j1 / 500D) * 500D);
                    }
                }
                int j2 = l + i1;
                int k2 = j2;
                if (k2 < 0) {
                    k2 += 360;
                }
                float f5 = (float) (f3 + (Math.sin(Math.toRadians(Math.abs(l) * 3)) * (f3 * 0.25D)));
                int l2 = (int) (f5 * Math.cos(Math.toRadians(l1)));
                String s3 = "  ";
                if (k2 < 5) {
                    s3 = "dead ahead, ";
                }
                if ((k2 >= 5) && (k2 <= 7)) {
                    s3 = "right by 5\260, ";
                }
                if ((k2 > 7) && (k2 <= 12)) {
                    s3 = "right by 10\260, ";
                }
                if ((k2 > 12) && (k2 <= 17)) {
                    s3 = "right by 15\260, ";
                }
                if ((k2 > 17) && (k2 <= 25)) {
                    s3 = "right by 20\260, ";
                }
                if ((k2 > 25) && (k2 <= 35)) {
                    s3 = "right by 30\260, ";
                }
                if ((k2 > 35) && (k2 <= 45)) {
                    s3 = "right by 40\260, ";
                }
                if ((k2 > 45) && (k2 <= 60)) {
                    s3 = "off our right, ";
                }
                if (k2 > 355) {
                    s3 = "dead ahead, ";
                }
                if ((k2 <= 355) && (k2 > 352)) {
                    s3 = "left by 5\260, ";
                }
                if ((k2 <= 352) && (k2 > 347)) {
                    s3 = "left by 10\260, ";
                }
                if ((k2 <= 347) && (k2 > 342)) {
                    s3 = "left by 15\260, ";
                }
                if ((k2 <= 342) && (k2 >= 335)) {
                    s3 = "left by 20\260, ";
                }
                if ((k2 < 335) && (k2 >= 325)) {
                    s3 = "left by 30\260, ";
                }
                if ((k2 < 325) && (k2 >= 315)) {
                    s3 = "left by 40\260, ";
                }
                if ((k2 < 345) && (k2 >= 300)) {
                    s3 = "off our left, ";
                }
                if ((j1 <= l2) && (j1 > 200) && (l1 >= -20) && (l1 <= 20) && (Math.abs(j2) <= 60)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "RP-15: Contact " + s3 + s + ", " + i2 + "m");
                    this.freq = 1;
                } else {
                    this.freq = 7;
                }
                this.setTimer(this.freq);
            }
        }

        return true;
    }

    public void setTimer(int i) {
        Random random = new Random();
        this.Timer1 = (float) (random.nextInt(i) * 0.1D);
        this.Timer2 = (float) (random.nextInt(i) * 0.1D);
    }

    public void resetTimer(float f) {
        this.Timer1 = f;
        this.Timer2 = f;
    }

    private float               oldthrl;
    private float               curthrl;
    private float               engineSurgeDamage;
    private float               SonicBoom;
    private Eff3DActor          shockwave;
    private boolean             isSonic;
    private boolean             overrideBailout;
    private boolean             ejectComplete;
    private SoundFX             fxSirena;
    private Sample              smplSirena;
    private boolean             sirenaSoundPlaying;
    private boolean             bRadarWarning;
    protected boolean           bHasSK1Seat;
    private static Actor        hunted                 = null;
    private Eff3DActor          gearLightsEffects[];
    private LightPointActor     gearLightsLights[];
    private boolean             bGearLightsOn;
    private boolean             bGearLightState;
    public int                  k14Mode;
    public int                  k14WingspanType;
    public float                k14Distance;
    public boolean              APmode1;
    public boolean              APmode2;
    public int                  radarmode;
    public int                  targetnum;
    public float                lockrange;
    private long                twait;
    public static boolean       bChangedPit            = false;
    private static final float  NEG_G_TOLERANCE_FACTOR = 1.0F;
    private static final float  NEG_G_TIME_FACTOR      = 1.0F;
    private static final float  NEG_G_RECOVERY_FACTOR  = 1.0F;
    private static final float  POS_G_TOLERANCE_FACTOR = 4.8F;
    private static final float  POS_G_TIME_FACTOR      = 1.5F;
    private static final float  POS_G_RECOVERY_FACTOR  = 1.0F;
    private long                lTimeNextEject;
    private boolean             bTwoSeat;
    private float               llpos;
    private boolean             dosel;
    private boolean             dosel2;
    private GuidedMissileUtils  guidedMissileUtils;
    private boolean             hasChaff;
    private boolean             hasFlare;
    private long                lastChaffDeployed;
    private long                lastFlareDeployed;
    private long                lastCommonThreatActive;
    private long                intervalCommonThreat;
    private long                lastRadarLockThreatActive;
    private long                intervalRadarLockThreat;
    private long                lastMissileLaunchThreatActive;
    private long                intervalMissileLaunchThreat;
    private boolean             bHasDeployedDragChute;
    private Chute               chute;
    private long                removeChuteTimer;
    private int                 freq;
    public float                Timer1;
    public float                Timer2;
    private int                 counter;
    private static final String k14Modes[]             = { "On", "Off" };

    static {
        Class class1 = Sukhoi_15.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
