package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.BombJATO;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F84G3 extends DO_335 implements TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeDockable {

    public F84G3() {
        this.bHasBoosters = false;
        this.boosterFireOutTime = -1L;
        this.AirBrakeControl = 0.0F;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.pk = 0;
        this.queen_last = null;
        this.queen_time = 0L;
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.target_ = null;
        this.queen_ = null;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        if (this.FM.AP.way.isLanding() && this.FM.getSpeed() > this.FM.VmaxFLAPS * 2D) this.FM.CT.AirBrakeControl = 1.0F;
        else if (this.FM.AP.way.isLanding() && this.FM.getSpeed() < this.FM.VmaxFLAPS * 1.5D) this.FM.CT.AirBrakeControl = 0.0F;
        if (flag && this.FM.AP.way.curr().Action == 3 && this.typeDockableIsDocked() && Math.abs(((Aircraft) this.queen_).FM.Or.getKren()) < 3F) if (this.FM.isPlayers()) {
            if (this.FM instanceof RealFlightModel && !((RealFlightModel) this.FM).isRealMode()) {
                this.typeDockableAttemptDetach();
                ((Maneuver) this.FM).set_maneuver(22);
                ((Maneuver) this.FM).setCheckStrike(false);
                this.FM.Vwld.z -= 5D;
                this.dtime = Time.current();
            }
        } else {
            this.typeDockableAttemptDetach();
            ((Maneuver) this.FM).set_maneuver(22);
            ((Maneuver) this.FM).setCheckStrike(false);
            this.FM.Vwld.z -= 5D;
            this.dtime = Time.current();
        }
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhite.eff", 30F);
        Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhite.eff", 30F);
        this.startBoosterSound();
    }

    public void doCutBoosters() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].start();
                this.booster[i] = null;
            }
        }
        this.stopBoosterSound();
        this.bHasBoosters = false;
    }

    public void startBoosterSound() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].startSound();
            }
        }
    }

    public void stopBoosterSound() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].stopSound();
            }
        }
    }

//    public void onAircraftLoaded() {
//        super.onAircraftLoaded();
//        for (int i = 0; i < 2; i++)
//            try {
//                this.booster[i] = new BombJATO();
//                this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
//                this.booster[i].pos.resetAsBase();
//                this.booster[i].drawing(true);
//            } catch (Exception exception) {
//                this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
//            }
//
//    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        for (int i = 0; i < 2; i++) {
            try {
                this.booster[i] = new BombJATO();
                this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
                this.booster[i].pos.resetAsBase();
                this.booster[i].drawing(true);
            } catch (Exception exception) {
                this.debugprintln("Structure corrupt - can't hang JATO..");
                return;
            }
        }
        this.bHasBoosters = true;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) if (this.FM.EI.engines[0].getPowerOutput() > 0.5F && this.FM.EI.engines[0].getStage() == 6) {
            if (this.FM.EI.engines[0].getPowerOutput() > 2.0F) this.FM.AS.setSootState(this, 0, 5);
            else this.FM.AS.setSootState(this, 0, 4);
        } else this.FM.AS.setSootState(this, 0, 0);
        if (this.FM instanceof Pilot && this.bHasBoosters) {
            // TODO: Changed Booster cutoff reasons from absolute altitude to altitude above
            // ground
            // if (FM.getAltitude() > 300F && boosterFireOutTime == -1L && FM.Loc.z != 0.0D
            // && World.Rnd().nextFloat() < 0.05F) {
            if (this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 300F && this.boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
            }
            if (this.bHasBoosters && this.boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.getSpeedKMH() > 20F) {
                this.boosterFireOutTime = Time.current() + 30000L;
                this.doFireBoosters();
                this.FM.AS.setGliderBoostOn();
            }
            if (this.bHasBoosters && this.boosterFireOutTime > 0L) {
                if (Time.current() < this.boosterFireOutTime)
                    this.FM.producedAF.x += 20000D;
                else // Stop sound
                    this.stopBoosterSound();
                if (Time.current() > this.boosterFireOutTime + 10000L) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
        }
        if (this.bNeedSetup) this.checkAsDrone();
        int i = this.aircIndex();
        if (this.FM instanceof Maneuver) if (this.typeDockableIsDocked()) {
            if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                ((Maneuver) this.FM).unblock();
                ((Maneuver) this.FM).set_maneuver(48);
                for (int j = 0; j < i; j++)
                    ((Maneuver) this.FM).push(48);

                if (this.FM.AP.way.curr().Action != 3) ((Maneuver) this.FM).AP.way.setCur(((Aircraft) this.queen_).FM.AP.way.Cur());
                ((Pilot) this.FM).setDumbTime(3000L);
            }
            if (this.FM.M.fuel < this.FM.M.maxFuel) this.FM.M.fuel += 20F * f;
        } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
            if (this.FM.EI.engines[0].getStage() == 0) this.FM.EI.setEngineRunning();
            if (this.dtime > 0L && ((Maneuver) this.FM).Group != null) {
                ((Maneuver) this.FM).Group.leaderGroup = null;
                ((Maneuver) this.FM).set_maneuver(22);
                ((Pilot) this.FM).setDumbTime(3000L);
                if (Time.current() > this.dtime + 3000L) {
                    this.dtime = -1L;
                    ((Maneuver) this.FM).clear_stack();
                    ((Maneuver) this.FM).set_maneuver(0);
                    ((Pilot) this.FM).setDumbTime(0L);
                }
            } else if (this.FM.AP.way.curr().Action == 0) {
                Maneuver maneuver = (Maneuver) this.FM;
                if (maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null) maneuver.Group.setGroupTask(2);
            }
        }
        super.update(f);
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

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 81F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.6F, 0.0F, -81F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 88F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.6F, 0.0F, -88F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.025F, 0.0F, -110F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.025F, 0.0F, 110F), 0.0F);
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
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 0.9F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    protected void moveFan(float f) {
        this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
        if (this.pk >= 1) this.pk = 1;
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Prop1_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("PropRot1_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - this.FM.Vwld.length() * 1.5444015264511108D) % 360F;
        this.hierMesh().chunkSetAngles("Prop1_D0", 0.0F, this.dynamoOrient, 0.0F);
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
                if (s.endsWith("exht"));
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
            if (s.startsWith("xxmgun1") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 0);
            if (s.startsWith("xxmgun2") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 1);
            if (s.startsWith("xxmgun3") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 2);
            if (s.startsWith("xxmgun4") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 3);
            if (s.startsWith("xxmgun5") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 4);
            if (s.startsWith("xxmgun6") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 5);
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

    protected void moveAirBrake(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.25F);
        this.hierMesh().chunkSetLocate("Brake01_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -70F * f, 0.0F);
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if (this.queen_last != null && this.queen_last == actor && (this.queen_time == 0L || Time.current() < this.queen_time + 5000L)) aflag[0] = false;
        else aflag[0] = true;
    }

    public void missionStarting() {
        this.checkAsDrone();
    }

    private void checkAsDrone() {
        if (this.target_ == null) {
            if (this.FM.AP.way.curr().getTarget() == null) this.FM.AP.way.next();
            this.target_ = this.FM.AP.way.curr().getTarget();
            if (Actor.isValid(this.target_) && this.target_ instanceof Wing) {
                Wing wing = (Wing) this.target_;
                int i = this.aircIndex();
                if (Actor.isValid(wing.airc[i / 2])) this.target_ = wing.airc[i / 2];
                else this.target_ = null;
            }
        }
        if (Actor.isValid(this.target_) && this.target_ instanceof KB_29P) {
            this.queen_last = this.target_;
            this.queen_time = Time.current();
            if (this.isNetMaster()) ((TypeDockable) this.target_).typeDockableRequestAttach(this, this.aircIndex() % 2, true);
        }
        this.bNeedSetup = false;
        this.target_ = null;
    }

    public int typeDockableGetDockport() {
        if (this.typeDockableIsDocked()) return this.dockport_;
        else return -1;
    }

    public Actor typeDockableGetQueen() {
        return this.queen_;
    }

    public boolean typeDockableIsDocked() {
        return Actor.isValid(this.queen_);
    }

    public void typeDockableAttemptAttach() {
        if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof KB_29P) ((TypeDockable) aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor) {
    }

    public void typeDockableRequestDetach(Actor actor) {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
    }

    public void typeDockableDoDetachFromDrone(int i) {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
        this.queen_ = actor;
        this.dockport_ = i;
        this.queen_last = this.queen_;
        this.queen_time = 0L;
        this.FM.EI.setEngineRunning();
        this.FM.CT.setGearAirborne();
        this.moveGear(0.0F);
        FlightModel flightmodel = ((Aircraft) this.queen_).FM;
        if (this.aircIndex() == 0 && this.FM instanceof Maneuver && flightmodel instanceof Maneuver) {
            Maneuver maneuver = (Maneuver) flightmodel;
            Maneuver maneuver1 = (Maneuver) this.FM;
            if (maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1) {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i) {
        if (this.dockport_ == i) {
            this.queen_last = this.queen_;
            this.queen_time = Time.current();
            this.queen_ = null;
            this.dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (this.typeDockableIsDocked()) {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if (Actor.isValid(this.queen_)) {
                actornet = this.queen_.net;
                if (actornet.countNoMirrors() > 0) actornet = null;
            }
            netmsgguaranted.writeByte(this.dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else netmsgguaranted.writeByte(0);
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        if (netmsginput.readByte() == 1) {
            this.dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) {
                Actor actor = (Actor) netobj.superObj();
                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, this.dockport_);
            }
        }
    }

    public static boolean bChangedPit = false;
    private BombJATO          booster[]   = { null, null };
    protected boolean     bHasBoosters;
    protected long        boosterFireOutTime;
    public float          AirBrakeControl;
    public int            k14Mode;
    public int            k14WingspanType;
    public float          k14Distance;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           pk;
    private Actor         queen_last;
    private long          queen_time;
    private boolean       bNeedSetup;
    private long          dtime;
    private Actor         target_;
    private Actor         queen_;
    private int           dockport_;

    static {
        Class class1 = F84G3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F84G");
        Property.set(class1, "meshName", "3DO/Plane/F84G_Nuke/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F84G.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF84G3.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08",
                        "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04",
                        "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16",
                        "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22" });
    }
}
