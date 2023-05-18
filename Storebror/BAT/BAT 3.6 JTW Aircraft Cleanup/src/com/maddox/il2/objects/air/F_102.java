package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.EnginesInterface;
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
import com.maddox.sas1946.il2.util.TrueRandom;

public class F_102 extends Scheme1 implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector {

    public float getDragForce(float f, float f1, float f2, float f3) {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float f, float f1, float f2, float f3, float f4, float f5) {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float f, float f1, float f2) {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float f, float f1) {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float f, float f1) {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public F_102() {
        this.elevatorPos = 0.0F;
        this.aileronPos = 0.0F;
        this.rudderPos = 0.0F;
        this.airbrakePos = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.lLightHook = new Hook[4];
        this.SonicBoom = 0.0F;
        this.oldthrl = -1F;
        this.curthrl = -1F;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.AirBrakeControl = 0.0F;
        this.DragChuteControl = 0.0F;
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lightTime = 0.0F;
        this.mn = 0.0F;
        this.ts = false;
        this.ictl = false;
        this.engineSurgeDamage = 0.0F;
        this.gearTargetAngle = -1F;
        this.gearCurrentAngle = -1F;
        this.hasHydraulicPressure = true;
        this.radarmode = 0;
        this.targetnum = 0;
        this.lockrange = 0.018F;
        this.APmode1 = false;
        this.APmode2 = false;
        this.pylonOccupied = false;
        this.pylonOccupied_DT = false;
        this.bSlatsOff = false;
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
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(F_102.NEG_G_TOLERANCE_FACTOR, F_102.NEG_G_TIME_FACTOR, F_102.NEG_G_RECOVERY_FACTOR, F_102.POS_G_TOLERANCE_FACTOR, F_102.POS_G_TIME_FACTOR, F_102.POS_G_RECOVERY_FACTOR);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    if (this.hierMesh().isChunkVisible("Blister1_D0")) {
                        this.hierMesh().chunkVisible("Gore1_D0", true);
                    }
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public void onAircraftLoaded() {
        this.FM.CT.bHasBombSelect = true;
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        this.FM.CT.DiffBrakesType = 3;
        System.out.println("*** Diff Brakes Set to Type: " + this.FM.CT.DiffBrakesType);
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 1.0F;
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void checkHydraulicStatus() {
        if ((this.FM.EI.engines[0].getStage() < 6) && (this.FM.Gears.nOfGearsOnGr > 0)) {
            this.gearTargetAngle = 90F;
            this.hasHydraulicPressure = false;
            this.FM.CT.bHasAileronControl = false;
            this.FM.CT.bHasElevatorControl = false;
            this.FM.CT.bHasRudderControl = false;
            this.FM.CT.bHasAirBrakeControl = false;
            this.FM.CT.ElevatorControl = 0.0F;
            this.FM.CT.AileronControl = 0.0F;
            this.FM.CT.RudderControl = 0.0F;
            this.FM.CT.AirBrakeControl = 0.0F;
        } else if (!this.hasHydraulicPressure) {
            this.gearTargetAngle = 0.0F;
            this.hasHydraulicPressure = true;
            this.FM.CT.bHasAileronControl = true;
            this.FM.CT.bHasElevatorControl = true;
            this.FM.CT.bHasAirBrakeControl = true;
            this.FM.CT.bHasRudderControl = true;
        }
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay5_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -90F * f, 0.0F);
    }

    public void moveHydraulics(float f) {
        if (this.gearTargetAngle >= 0.0F) {
            if (this.gearCurrentAngle < this.gearTargetAngle) {
                this.gearCurrentAngle += 90F * f * 0.8F;
                if (this.gearCurrentAngle >= this.gearTargetAngle) {
                    this.gearCurrentAngle = this.gearTargetAngle;
                    this.gearTargetAngle = -1F;
                }
            } else {
                this.gearCurrentAngle -= 90F * f * 0.8F;
                if (this.gearCurrentAngle <= this.gearTargetAngle) {
                    this.gearCurrentAngle = this.gearTargetAngle;
                    this.gearTargetAngle = -1F;
                }
            }
        }
    }

    public void updateLLights() {
        this.pos.getRender(Actor._tmpLoc);
        if (this.lLight == null) {
            if (Actor._tmpLoc.getX() >= 1.0D) {
                this.lLight = new LightPointWorld[4];
                for (int i = 0; i < 4; i++) {
                    this.lLight[i] = new LightPointWorld();
                    this.lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    this.lLight[i].setEmit(0.0F, 0.0F);
                    try {
                        this.lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    } catch (Exception exception) {
                    }
                }

            }
        } else {
            for (int j = 0; j < 4; j++) {
                if (this.FM.AS.astateLandingLightEffects[j] != null) {
                    F_102.lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, F_102.lLightLoc1);
                    F_102.lLightLoc1.get(F_102.lLightP1);
                    F_102.lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, F_102.lLightLoc1);
                    F_102.lLightLoc1.get(F_102.lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(F_102.lLightP1, F_102.lLightP2, F_102.lLightPL)) {
                        F_102.lLightPL.z++;
                        F_102.lLightP2.interpolate(F_102.lLightP1, F_102.lLightPL, 0.95F);
                        this.lLight[j].setPos(F_102.lLightP2);
                        float f = (float) F_102.lLightP1.distance(F_102.lLightPL);
                        float f1 = (f * 0.5F) + 60F;
                        float f2 = 0.7F - ((0.8F * f * this.lightTime) / 2000F);
                        this.lLight[j].setEmit(f2, f1);
                    } else {
                        this.lLight[j].setEmit(0.0F, 0.0F);
                    }
                } else if (this.lLight[j].getR() != 0.0F) {
                    this.lLight[j].setEmit(0.0F, 0.0F);
                }
            }

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            F_102.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            F_102.bChangedPit = true;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && (TrueRandom.nextFloat() < 0.2F) && (this.FM.AS.astateEngineStates[0] > 3) && (TrueRandom.nextFloat() < 0.12F)) {
            this.FM.AS.explodeEngine(this, 0);
            this.msgCollision(this, "WingLIn_D0", "WingLIn_D0");
            if (TrueRandom.nextInt(2) != 0) {
                this.FM.AS.hitTank(this, 0, TrueRandom.nextInt(1, 8));
            } else {
                this.FM.AS.hitTank(this, 1, TrueRandom.nextInt(1, 8));
            }
        }
        if (this.FM.getAltitude() < 200F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
        }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        if (this.k14Mode == 0) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
            }
        } else if (this.k14Mode == 1) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
            }
        } else if ((this.k14Mode == 2) && (this.FM.actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
        }
        return true;
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
        if (this.k14WingspanType == 0) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
            }
        } else if (this.k14WingspanType == 1) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
            }
        } else if (this.k14WingspanType == 2) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
            }
        } else if (this.k14WingspanType == 3) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
            }
        } else if (this.k14WingspanType == 4) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
            }
        } else if (this.k14WingspanType == 5) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
            }
        } else if (this.k14WingspanType == 6) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
            }
        } else if (this.k14WingspanType == 7) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
            }
        } else if (this.k14WingspanType == 8) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
            }
        } else if ((this.k14WingspanType == 9) && (this.FM.actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
        }
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        if (this.k14WingspanType == 0) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
            }
        } else if (this.k14WingspanType == 1) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
            }
        } else if (this.k14WingspanType == 2) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
            }
        } else if (this.k14WingspanType == 3) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
            }
        } else if (this.k14WingspanType == 4) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
            }
        } else if (this.k14WingspanType == 5) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
            }
        } else if (this.k14WingspanType == 6) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
            }
        } else if (this.k14WingspanType == 7) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
            }
        } else if (this.k14WingspanType == 8) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
            }
        } else if ((this.k14WingspanType == 9) && (this.FM.actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
        }
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

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat1_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -117F);
        float f2 = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
        float f3 = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F) : Aircraft.cvt(f, 0.8F, 1.0F, -90F, -90F);
        float f4 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -87F);
        float f5 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -100F);
        float f6 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -45F);
        float f7 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, 45F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f5, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f5, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, f6, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, f7, 0.0F);
    }

    protected void moveGear(float f) {
        F_102.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        this.hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 35F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 70F * f, 0.0F, 0.0F);
    }

    protected void moveRudder(float f) {
        this.rudderPos = f;
        float f1 = Aircraft.cvt(this.FM.getSpeedKMH(), F_102.RUDDER_LIMIT_SPEED_LOW, F_102.RUDDER_LIMIT_SPEED_HIGH, F_102.RUDDER_LIMIT_LOW, F_102.RUDDER_LIMIT_HIGH);
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -f * f1, 0.0F);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.DiffBrakesType > 0) {
            if (this.FM.CT.getGear() < 0.75F) {
                return;
            }
            this.hierMesh().chunkSetAngles("GearC21_D0", 0.0F, -30F * f, 0.0F);
        }
    }

    protected void moveElevator(float f) {
        this.elevatorPos = -f;
    }

    protected void moveAileron(float f) {
        this.aileronPos = f;
    }

    protected void moveElevon() {
        float f = this.elevatorPos + this.aileronPos;
        float f1 = this.elevatorPos - this.aileronPos;
        if (f > 1.0F) {
            f = 1.0F;
        }
        if (f < -1F) {
            f = -1F;
        }
        if (f1 > 1.0F) {
            f1 = 1.0F;
        }
        if (f1 < -1F) {
            f1 = -1F;
        }
        float f2 = Aircraft.cvt(this.FM.getSpeedKMH(), F_102.ELEVON_LIMIT_SPEED_LOW, F_102.ELEVON_LIMIT_SPEED_HIGH, F_102.ELEVON_LIMIT_LOW, F_102.ELEVON_LIMIT_HIGH);
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, f * f2);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, f1 * f2);
    }

    protected void moveFan(float f) {
    }

    protected void moveSlats(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 0.04F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.04F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Slat01_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("Slat01_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.04F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.04F);
        this.hierMesh().chunkSetAngles("Slat02_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("Slat02_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff() {
        if (!this.bSlatsOff) {
            this.resetYPRmodifier();
            Aircraft.xyz[0] = 0.04F;
            Aircraft.xyz[1] = -0.04F;
            Aircraft.xyz[2] = 0.0F;
            this.hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("Slat01_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[0] = -0.04F;
            Aircraft.xyz[1] = -0.04F;
            this.hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("Slat02_D0", Aircraft.xyz, Aircraft.ypr);
            this.bSlatsOff = true;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (point3d.x > 1.7D) {
                if (TrueRandom.nextFloat() < 0.07F) {
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (TrueRandom.nextFloat() < 0.07F) {
                    this.FM.AS.setJamBullets(0, 1);
                }
                if (TrueRandom.nextFloat() < 0.12F) {
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (TrueRandom.nextFloat() < 0.12F) {
                    this.FM.AS.setJamBullets(1, 1);
                }
            }
            if ((point3d.x > -0.999D) && (point3d.x < 0.535D) && (point3d.z > -0.224D)) {
                if (TrueRandom.nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                }
                if (TrueRandom.nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
                if (TrueRandom.nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                }
                if (TrueRandom.nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                }
                if (TrueRandom.nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
                if (TrueRandom.nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
                if (TrueRandom.nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
            }
            if ((point3d.x > 0.8D) && (point3d.x < 1.58D) && (TrueRandom.nextFloat() < 0.25F) && (((shot.powerType == 3) && (this.getEnergyPastArmor(0.4F, shot) > 0.0F)) || (shot.powerType == 0))) {
                this.FM.AS.hitTank(shot.initiator, 0, TrueRandom.nextInt(1, (int) (shot.power / 4000F)));
            }
            if ((point3d.x > -2.485D) && (point3d.x < -1.6D) && (TrueRandom.nextFloat() < 0.25F) && (((shot.powerType == 3) && (this.getEnergyPastArmor(0.4F, shot) > 0.0F)) || (shot.powerType == 0))) {
                this.FM.AS.hitTank(shot.initiator, 1, TrueRandom.nextInt(1, (int) (shot.power / 4000F)));
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xstabl")) {
            this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            this.hitChunk("StabR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.endsWith("lin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.endsWith("rin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.endsWith("lmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.endsWith("rmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.endsWith("lout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.endsWith("rout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xengine")) {
            int i = s.charAt(7) - 49;
            if ((point3d.x > 0.0D) && (point3d.x < 0.697D)) {
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, TrueRandom.nextInt(1, 6));
            }
            if (TrueRandom.nextFloat(0.009F, 0.1357F) < shot.mass) {
                this.FM.AS.hitEngine(shot.initiator, i, 5);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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
            this.hitFlesh(j, shot, byte0);
        }
    }

    public void typeFighterAceMakerRangeFinder() {
        if (this.k14Mode == 2) {
            return;
        }
        F_102.hunted = Main3D.cur3D().getViewPadlockEnemy();
        if (F_102.hunted == null) {
            this.k14Distance = 200F;
            F_102.hunted = War.GetNearestEnemyAircraft(this.FM.actor, 2700F, 9);
        }
        if (F_102.hunted != null) {
            this.k14Distance = (float) this.FM.actor.pos.getAbsPoint().distance(F_102.hunted.pos.getAbsPoint());
            if (this.k14Distance > 800F) {
                this.k14Distance = 800F;
            } else if (this.k14Distance < 200F) {
                this.k14Distance = 200F;
            }
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
        for (i = 0; (i < TypeSupersonic.fMachAltX.length) && (TypeSupersonic.fMachAltX[i] <= f); i++) {

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
        if (this.calculateMach() <= 1.0F) {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f;
            this.SonicBoom = 0.0F;
            this.isSonic = false;
        }
        if (this.calculateMach() >= 1.0F) {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f1;
            this.isSonic = true;
        }
        if (this.FM.VmaxAllowed > 1500F) {
            this.FM.VmaxAllowed = 1500F;
        }
        if (this.isSonic && (this.SonicBoom < 1.0F)) {
            this.playSound("aircraft.SonicBoom", true);
            this.playSound("aircraft.SonicBoomInternal", true);
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            }
            if (Config.isUSE_RENDER() && (TrueRandom.nextFloat() < this.getAirDensityFactor(this.FM.getAltitude()))) {
                this.shockwave = Eff3DActor.New(this, this.findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            }
            this.SonicBoom = 1.0F;
        }
        if ((this.calculateMach() > 1.01F) || (this.calculateMach() < 1.0F)) {
            Eff3DActor.finish(this.shockwave);
        }
    }

    public void engineSurge(float f) {
        if (this.FM.AS.isMaster()) {
            for (int i = 0; i < 2; i++) {
                if (this.curthrl == -1F) {
                    this.curthrl = this.oldthrl = this.FM.EI.engines[0].getControlThrottle();
                } else {
                    this.curthrl = this.FM.EI.engines[0].getControlThrottle();
                    if (this.curthrl < 1.05F) {
                        if ((((this.curthrl - this.oldthrl) / f) > 20F) && (this.FM.EI.engines[0].getRPM() < 3200F) && (this.FM.EI.engines[0].getStage() == 6) && (TrueRandom.nextFloat() < 0.4F)) {
                            if (this.FM.actor == World.getPlayerAircraft()) {
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            }
                            this.playSound("weapon.MGunMk108s", true);
                            this.engineSurgeDamage += (0.01F * this.FM.EI.engines[0].getRPM()) / 1000F;
                            this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
                            if ((TrueRandom.nextFloat() < 0.05F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                                this.FM.AS.hitEngine(this, i, 100);
                            }
                            if ((TrueRandom.nextFloat() < 0.05F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                                this.FM.EI.engines[0].setEngineDies(this);
                            }
                        }
                        if ((((this.curthrl - this.oldthrl) / f) < -20F) && (((this.curthrl - this.oldthrl) / f) > -100F) && (this.FM.EI.engines[0].getRPM() < 3200F) && (this.FM.EI.engines[0].getStage() == 6)) {
                            this.playSound("weapon.MGunMk108s", true);
                            this.engineSurgeDamage += (0.001F * this.FM.EI.engines[0].getRPM()) / 1000F;
                            this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
                            if ((TrueRandom.nextFloat() < 0.4F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                                if (this.FM.actor == World.getPlayerAircraft()) {
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                }
                                this.FM.EI.engines[0].setEngineStops(this);
                            } else if (this.FM.actor == World.getPlayerAircraft()) {
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            }
                        }
                    }
                    this.oldthrl = this.curthrl;
                }
            }

        }
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
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && (this.FM.getSpeedKMH() > 15F)) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            this.bailout();
        }
        if (this.FM.CT.getFlap() < this.FM.CT.FlapsControl) {
            this.FM.CT.forceFlaps(this.flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        } else {
            this.FM.CT.forceFlaps(this.flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        }
        if (this.FM.CT.dvGear < 0.0F) {
            this.FM.CT.dvGear = 0.0F;
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
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            for (int i = 0; i < 2; i++) {
                if ((this.FM.EI.engines[0].getPowerOutput() > 0.25F) && (this.FM.EI.engines[0].getStage() == 6)) {
                    if (this.FM.EI.engines[0].getPowerOutput() > 1.001F) {
                        this.FM.AS.setSootState(this, i, 5);
                    } else if ((this.FM.EI.engines[0].getPowerOutput() > 0.65F) && (this.FM.EI.engines[0].getPowerOutput() < 1.001F)) {
                        this.FM.AS.setSootState(this, i, 3);
                    } else {
                        this.FM.AS.setSootState(this, i, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, i, 0);
                }
                this.setExhaustFlame(Math.round(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            }

            if (this.FM instanceof RealFlightModel) {
                this.umn();
            }
        }
        float f1 = Aircraft.cvt(this.FM.getSpeedKMH(), 500F, 1000F, 0.999F, 0.601F);
        if ((this.FM.getSpeed() > 7F) && (TrueRandom.nextFloat() < this.getAirDensityFactor(this.FM.getAltitude()))) {
            if (this.FM.getOverload() > 5.7F) {
                this.pull01 = Eff3DActor.New(this, this.findHook("_Pull01"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                this.pull02 = Eff3DActor.New(this, this.findHook("_Pull02"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
            }
            if (this.FM.getOverload() <= 5.7F) {
                Eff3DActor.finish(this.pull01);
                Eff3DActor.finish(this.pull02);
            }
        }
        if (this.pylonOccupied && (this.airBrake_State < 0.0015D) && this.pylonOccupied_DT && !this.getBulletEmitterByHookName("_ExternalTank01").haveBullets()) {
            this.pylonOccupied = false;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx * 2.0F;
        }
        if (this.FM.getSpeed() > 5F) {
            this.moveSlats(f);
            this.bSlatsOff = false;
        } else {
            this.slatsOff();
        }
        this.engineSurge(f);
        this.typeFighterAceMakerRangeFinder();
        this.checkHydraulicStatus();
        this.moveElevon();
        this.moveHydraulics(f);
        this.soundbarier();
        this.computeLift();
        this.computeLift2();
        this.computeSubsonicLimiter();
        if (!this.hasHydraulicPressure) {
            this.airbrakePos *= F_102.CONTROLS_NEUTRAL_FACTOR;
            this.rudderPos *= F_102.CONTROLS_NEUTRAL_FACTOR;
            this.aileronPos *= F_102.CONTROLS_NEUTRAL_FACTOR;
            this.elevatorPos *= F_102.CONTROLS_NEUTRAL_FACTOR;
            Reflection.setFloat(this.FM.CT, "Ailerons", this.aileronPos);
            Reflection.setFloat(this.FM.CT, "Elevators", -this.elevatorPos);
            Reflection.setFloat(this.FM.CT, "Rudder", this.rudderPos);
            Reflection.setFloat(this.FM.CT, "airBrake", this.airbrakePos);
        }
        super.update(f);
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteF-102/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(0.8F);
            this.chute.pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
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
        this.computeJ57_AB();
        this.guidedMissileUtils.update();
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
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                break;

            case 3:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                break;

            case 2:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                break;

            case 5:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                break;

            case 4:
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                break;
        }
    }

    protected void moveAirBrake(float f) {
        this.airbrakePos = f;
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 80F * f);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 80F * f);
    }

    public void setExhaustFlame(int i, int j) {
        if (j == 0) {
            switch (i) {
                case 0:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 1:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 2:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 3:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    // fall through

                case 4:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 5:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 6:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 7:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", true);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 8:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", true);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 9:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", true);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 10:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", true);
                    break;

                case 11:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", true);
                    break;

                case 12:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", true);
                    break;

                default:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;
            }
        }
    }

    private void bailout() {
        if (this.overrideBailout) {
            if ((this.FM.AS.astateBailoutStep >= 0) && (this.FM.AS.astateBailoutStep < 2)) {
                if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.CT.getCockpitDoor() > 0.5F)) {
                    this.FM.AS.astateBailoutStep = 11;
                    this.doRemoveBlisters();
                } else {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 2) && (this.FM.AS.astateBailoutStep <= 3)) {
                switch (this.FM.AS.astateBailoutStep) {
                    case 2:
                        if (this.FM.CT.cockpitDoorControl < 0.5F) {
                            this.doRemoveBlister1();
                        }
                        break;

                    case 3:
                        this.doRemoveBlisters();
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
                        this.doEjectCatapult();
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        this.overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        this.ejectComplete = true;
                    }
                }
            }
        }
    }

    private final void doRemoveBlister1() {
        if ((this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveBlisters() {
        for (int i = 2; i < 10; i++) {
            if ((this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1) && (this.FM.AS.getPilotHealth(i - 1) > 0.0F)) {
                this.hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
        }

    }

    private final void umn() {
        Vector3d vector3d = this.FM.getVflow();
        this.mn = (float) vector3d.lengthSquared();
        this.mn = (float) Math.sqrt(this.mn);
        float f = this.mn;
        World.cur().getClass();
        this.mn = f / Atmosphere.sonicSpeed((float) this.FM.Loc.z);
        if (this.mn >= F_102.lteb) {
            this.ts = true;
        } else {
            this.ts = false;
        }
    }

    public boolean ist() {
        return this.ts;
    }

    public float gmnr() {
        return this.mn;
    }

    public boolean inr() {
        return this.ictl;
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

    public void computeLift() {
        Polares polares = (Polares) Reflection.getValue(this.FM, "Wing");
        float f = this.calculateMach();
        if (f < 0.9F) {
            polares.lineCyCoeff = 0.08F;
        } else if (f < 2.3F) {
            float f1 = f * f;
            float f2 = f1 * f;
            float f3 = f2 * f;
            polares.lineCyCoeff = ((((0.0556973F * f3) - (0.398257F * f2)) + (1.06557F * f1)) - (1.28898F * f)) + 0.630757F;
        } else {
            polares.lineCyCoeff = 0.016F;
        }
    }

    public void computeLift2() {
        Polares polares = (Polares) Reflection.getValue(this.FM, "Wing");
        float f = this.calculateMach();
        if (f < 0.25F) {
            polares.Cy0_0 = 0.01F;
        } else if (f < 0.27F) {
            polares.Cy0_0 = 0.03F;
        } else {
            polares.Cy0_0 = 0.008F;
        }
    }

    public void computeSubsonicLimiter() {
        float f = Aircraft.cvt(this.calculateMach(), 0.5F, 1.0F, 0.0F, 24000F);
        float f1 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 14000F, 1.0F, 0.1F);
        if (this.FM.EI.engines[0].getThrustOutput() < 1.001D) {
            this.FM.producedAF.x += f * f1;
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

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void computeJ57_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 20000D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 17D) {
                f1 = 1.0F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                f1 = ((0.0166647F * f3) - (0.24023F * f2)) + (0.444286F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    String AltitudeDifferenceToString(double d, double d1) {
        String s = "level with us";
        if ((d - d1 - 300D) >= 0.0D) {
            s = "below us";
        } else if (((d - d1) + 300D) <= 0.0D) {
            s = "above us";
        } else if ((d - d1 - 150D) >= 0.0D) {
            s = "slightly below";
        } else if (((d - d1) + 150D) < 0.0D) {
            s = "slightly above";
        }
        return s;
    }

    String TrackOffsetToString(int i) {
        String s = "  ";
        if ((i < 5) || (i > 355)) {
            s = "dead ahead, ";
        } else if (i < 8) {
            s = "right by 5\260, ";
        } else if (i < 13) {
            s = "right by 10\260, ";
        } else if (i < 18) {
            s = "right by 15\260, ";
        } else if (i < 26) {
            s = "right by 20\260, ";
        } else if (i < 36) {
            s = "right by 30\260, ";
        } else if (i < 46) {
            s = "right by 40\260, ";
        } else if (i <= 60) {
            s = "off our right, ";
        } else if (i > 352) {
            s = "left by 5\260, ";
        } else if (i > 347) {
            s = "left by 10\260, ";
        } else if (i > 342) {
            s = "left by 15\260, ";
        } else if (i > 334) {
            s = "left by 20\260, ";
        } else if (i > 324) {
            s = "left by 30\260, ";
        } else if (i > 314) {
            s = "left by 40\260, ";
        } else if (i >= 300) {
            s = "off our left, ";
        }
        return s;
    }

    private int MtargetDistance(int i, int j) {
        int k = 0;
        if (j < i) {
            if (j > 1150) {
                k = (int) (Math.ceil(j / 900D) * 900D);
            } else {
                k = (int) (Math.ceil(j / 500D) * 500D);
            }
        }
        return k;
    }

    private int IRSTtargetDistance(Actor actor) {
        double d = 0.0D;
        EnginesInterface enginesinterface = ((Aircraft) actor).FM.EI;
        for (int i = 0; i < enginesinterface.engines.length; i++) {
            float f = 0.0F;
            if ((enginesinterface.engines[i].getType() == 2) || (enginesinterface.engines[i].getType() == 3)) {
                f = enginesinterface.engines[i].thrustMax;
                if (enginesinterface.engines[i].getPowerOutput() > 1.0F) {
                    f *= enginesinterface.engines[i].getPowerOutput() * 1.22F;
                } else {
                    f *= (enginesinterface.engines[i].getPowerOutput() * 0.8F) + 0.2F;
                }
            } else {
                f = Reflection.getFloat(enginesinterface.engines[i], "horsePowers") * ((enginesinterface.engines[i].getPowerOutput() * 0.9F) + 0.1F) * 0.36F;
            }
            d += f;
        }

        if (enginesinterface.engines.length > 4) {
            d *= 2D / enginesinterface.engines.length;
        } else {
            d /= Math.sqrt(enginesinterface.engines.length);
        }
        d *= 1.5D;
        return (int) d;
    }

    boolean TrackingSystem(int i, int j, int k) {
        boolean flag = false;
        Aircraft aircraft = World.getPlayerAircraft();
        if (!(aircraft instanceof F_102)) {
            return flag;
        }
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = aircraft.pos.getAbsPoint().z;
        double d3 = d2 - World.land().HQ(aircraft.pos.getAbsPoint().x, aircraft.pos.getAbsPoint().y);
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int l;
        for (l = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D)); l < 0; l += 360) {

        }
        int i1;
        for (i1 = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D)); i1 < 0; i1 += 360) {

        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if ((actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(null) > 20D)) {
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = actor.pos.getAbsPoint().z - World.land().HQ(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y);
                double d7 = Math.ceil((d2 - d6) / 10D) * 10D;
                String s = this.AltitudeDifferenceToString(d2, d6);
                double d8 = d - d4;
                double d9 = d5 - d1;
                double d10 = (d9 * d9) + (d8 * d8);
                int j1;
                for (j1 = (int) (Math.floor(Math.toDegrees(Math.atan2(d9, d8))) - 90D); j1 < 0; j1 += 360) {

                }
                int k1 = j1 - l;
                float f = j;
                if (d3 < k) {
                    f = (float) (d3 * 0.8D * 3D);
                }
                int l1 = (int) (Math.ceil(Math.sqrt(d10 * TrueRandom.nextDouble(0.9D, 1.1D)) / 10D) * 10D);
                if (l1 > j) {
                    l1 = (int) (Math.ceil(Math.sqrt(d10) / 10D) * 10D);
                }
                int i2 = (int) (Math.floor(Math.toDegrees(Math.atan2(l1, d7))) - 90D);
                int j2 = (i2 - (90 - i1)) + TrueRandom.nextInt(-3, 3);
                int k2 = 0;
                switch (i) {
                    case 0:
                    case 1:
                    default:
                        k2 = this.MtargetDistance(j, l1);
                        break;

                    case 2:
                        k2 = this.IRSTtargetDistance(actor);
                        break;
                }
                int l2 = k1 + TrueRandom.nextInt(-3, 3);
                int i3;
                for (i3 = l2; i3 < 0; i3 += 360) {

                }
                float f1 = (float) (f + (Math.sin(Math.toRadians(Math.abs(k1) * 3)) * (f * 0.25D)));
                int j3 = (int) (f1 * Math.cos(Math.toRadians(j2)));
                String s1 = this.TrackOffsetToString(i3);
                switch (i) {
                    case 0:
                    case 1:
                    default:
                        if ((l1 <= j3) && (l1 > 300) && (Math.abs(j2) <= 20) && (Math.abs(l2) <= 60)) {
                            HUD.log(AircraftHotKeys.hudLogWeaponId, F_102.TRACKING_SYSTEM_MESSAGE[i] + " " + s1 + s + ", " + k2 + "m");
                            this.freq = 1;
                            flag = true;
                        } else {
                            this.freq = 7;
                        }
                        break;

                    case 2:
                        if ((l1 <= j3) && (Math.abs(j2) <= 20) && (Math.abs(l2) <= 60) && (l1 < k2)) {
                            HUD.log(AircraftHotKeys.hudLogWeaponId, F_102.TRACKING_SYSTEM_MESSAGE[i] + " " + s1 + s);
                            this.freq = 1;
                            flag = true;
                        }
                        break;
                }
                this.setTimer(this.freq);
            }
        }

        return flag;
    }

    void setTimer(int i) {
        this.Timer1 = TrueRandom.nextFloat(i) * 0.1F;
        this.Timer2 = TrueRandom.nextFloat(i) * 0.1F;
    }

    private static final float ELEVON_LIMIT_SPEED_LOW    = 500F;
    private static final float ELEVON_LIMIT_SPEED_HIGH   = 800F;
    private static final float ELEVON_LIMIT_LOW          = 30F;
    private static final float ELEVON_LIMIT_HIGH         = 20F;
    private static final float RUDDER_LIMIT_SPEED_LOW    = 200F;
    private static final float RUDDER_LIMIT_SPEED_HIGH   = 800F;
    private static final float RUDDER_LIMIT_LOW          = 45F;
    private static final float RUDDER_LIMIT_HIGH         = 20F;
    private static final float CONTROLS_NEUTRAL_FACTOR   = 0.98F;
    private float              elevatorPos;
    private float              aileronPos;
    private float              rudderPos;
    private float              airbrakePos;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    public float               Timer1;
    public float               Timer2;
    int                        freq;
    static final int           TRACKING_SYSTEM_M3        = 0;
    static final int           TRACKING_SYSTEM_M10       = 1;
    static final int           TRACKING_SYSTEM_IRST      = 2;
    private static String      TRACKING_SYSTEM_MESSAGE[] = { "M-3: Contact", "M-10: Contact", "IR Tracking!" };
    public float               fSightCurForwardAngle;
    public float               fSightCurSideslip;
    public float               fSightCurAltitude;
    public float               fSightCurSpeed;
    public float               fSightCurReadyness;
    private long               twait;
    private float              oldthrl;
    private float              curthrl;
    public int                 k14Mode;
    public int                 k14WingspanType;
    public float               k14Distance;
    public float               AirBrakeControl;
    public float               DragChuteControl;
    private boolean            overrideBailout;
    private boolean            ejectComplete;
    private float              lightTime;
    private LightPointWorld    lLight[];
    private Hook               lLightHook[];
    private static Loc         lLightLoc1                = new Loc();
    private static Point3d     lLightP1                  = new Point3d();
    private static Point3d     lLightP2                  = new Point3d();
    private static Point3d     lLightPL                  = new Point3d();
    private boolean            ictl;
    private float              mn;
    private static float       lteb                      = 0.92F;
    private boolean            ts;
    public static boolean      bChangedPit               = false;
    private float              SonicBoom;
    private Eff3DActor         shockwave;
    private boolean            isSonic;
    public static int          LockState                 = 0;
    static Actor               hunted                    = null;
    private float              engineSurgeDamage;
    private float              gearTargetAngle;
    private float              gearCurrentAngle;
    public boolean             hasHydraulicPressure;
    private static final float NEG_G_TOLERANCE_FACTOR    = 2.5F;
    private static final float NEG_G_TIME_FACTOR         = 2.5F;
    private static final float NEG_G_RECOVERY_FACTOR     = 2F;
    private static final float POS_G_TOLERANCE_FACTOR    = 6.5F;
    private static final float POS_G_TIME_FACTOR         = 3F;
    private static final float POS_G_RECOVERY_FACTOR     = 3F;
    public int                 radarmode;
    public int                 targetnum;
    public float               lockrange;
    public boolean             APmode1;
    public boolean             APmode2;
    private boolean            pylonOccupied_DT;
    private boolean            pylonOccupied;
    private float              airBrake_State;
    protected boolean          bSlatsOff;
    private Eff3DActor         pull01;
    private Eff3DActor         pull02;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;

    static {
        Class class1 = F_102.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
