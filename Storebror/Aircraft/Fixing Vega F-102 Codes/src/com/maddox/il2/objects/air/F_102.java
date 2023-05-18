package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.Map.Entry;

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
import com.maddox.il2.fm.Motor;
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
import com.maddox.sas1946.il2.util.AircraftTools;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;

public class F_102 extends Scheme1 implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar, TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector {

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
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
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
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
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

        // Tracking Code Consolidation
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
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
            this.FM.CT.ElevatorControl = 0F;
            this.FM.CT.AileronControl = 0F;
            this.FM.CT.RudderControl = 0F;
            this.FM.CT.AirBrakeControl = 0F;
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
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL)) {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        this.lLight[j].setPos(lLightP2);
                        float f = (float) lLightP1.distance(lLightPL);
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
            bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
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
        float f3 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, -90F, -90F) : Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
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
        moveGear(this.hierMesh(), f);
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
        this.rudderPos = f; // Store latest Rudder pos for neutralization if needed.
        float speedFactor = Aircraft.cvt(this.FM.getSpeedKMH(), RUDDER_LIMIT_SPEED_LOW, RUDDER_LIMIT_SPEED_HIGH, RUDDER_LIMIT_LOW, RUDDER_LIMIT_HIGH);
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -f * speedFactor, 0.0F);
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
        this.elevatorPos = -f; // Just store desired position, movement will be done in update()/moveElevon() methods
    }

    protected void moveAileron(float f) {
        this.aileronPos = f; // Just store desired position, movement will be done in update()/moveElevon() methods
    }

    protected void moveElevon() {
        float elevonLeft = this.elevatorPos + this.aileronPos;
        float elevonRight = this.elevatorPos - this.aileronPos;
        if (elevonLeft > 1.0F)
            elevonLeft = 1.0F;
        if (elevonLeft < -1.0F)
            elevonLeft = -1.0F;
        if (elevonRight > 1.0F)
            elevonRight = 1.0F;
        if (elevonRight < -1.0F)
            elevonRight = -1.0F;
        float speedFactor = Aircraft.cvt(this.FM.getSpeedKMH(), ELEVON_LIMIT_SPEED_LOW, ELEVON_LIMIT_SPEED_HIGH, ELEVON_LIMIT_LOW, ELEVON_LIMIT_HIGH);
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, elevonLeft * speedFactor);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, elevonRight * speedFactor);
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
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if (hunted == null) {
            this.k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(this.FM.actor, 2700F, 9);
        }
        if (hunted != null) {
            this.k14Distance = (float) this.FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
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
            ;
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
        this.moveElevon(); // Move Elevon Control Surfaces here instead of moveAileron() / moveElevator() methods to avoid flickering
        this.moveHydraulics(f);
        this.soundbarier();
        this.computeLift();
        this.computeLift2();
        this.computeEngine();
        if (!this.hasHydraulicPressure) { // Slowly neutralize all controls if hydraulics went off
            this.airbrakePos *= CONTROLS_NEUTRAL_FACTOR;
            this.rudderPos *= CONTROLS_NEUTRAL_FACTOR;
            this.aileronPos *= CONTROLS_NEUTRAL_FACTOR;
            this.elevatorPos *= CONTROLS_NEUTRAL_FACTOR;
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
            ((Actor) (this.chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                    ((Actor) (this.chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                }
                ((Actor) (this.chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
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
        this.airbrakePos = f; // Store latest Airbrake pos for neutralization if needed.
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
        if (this.mn >= lteb) {
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

    public void computeSubsonicLimiter() {
        float f = this.calculateMach();
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() < 1.001F) && (this.FM.EI.engines[0].getStage() == 6) && (this.calculateMach() >= 0.9D)) {
            if (f > 0.97D) {
                f1 = 0.001F;
            } else {
                f1 = (0.0128571F * f) - 0.0114714F;
            }
        }
        this.FM.Sq.dragParasiteCx += f1;
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
        } else if (f < 0.265F) {
            polares.Cy0_0 = 0.03F;
        } else if (f < 0.3F) {
            polares.Cy0_0 = 0.07F;
        } else {
            polares.Cy0_0 = 0.008F;
        }
    }

    public void computeEngine() {
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() < 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            if (this.calculateMach() <= 0.0F) {
                ;
            }
        }
        if (f > 13.5F) {
            f1 = 1.5F;
        } else {
            float f2 = f * f;
            f1 = (0.0130719F * f2) - (0.0653595F * f);
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = !this.bSightAutomation;
        this.bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
        return this.bSightAutomation;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle++;
        if (this.fSightCurForwardAngle > 85F) {
            this.fSightCurForwardAngle = 85F;
        }
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle--;
        if (this.fSightCurForwardAngle < 0.0F) {
            this.fSightCurForwardAngle = 0.0F;
        }
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        if (this.deltaAzimuth == 0.0F) {
            return;
        }
        this.fSightCurSideslip += 0.05F;
        if (this.fSightCurSideslip > 3F) {
            this.fSightCurSideslip = 3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjSideslipMinus() {
        if (this.deltaAzimuth == 0.0F) {
            return;
        }
        this.fSightCurSideslip -= 0.05F;
        if (this.fSightCurSideslip < -3F) {
            this.fSightCurSideslip = -3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus() {
        if (this.deltaTangage == 0.0F) {
            return;
        }
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 6000F) {
            this.fSightCurAltitude = 6000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus() {
        if (this.deltaTangage == 0.0F) {
            return;
        }
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 850F) {
            this.fSightCurAltitude = 850F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 250F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 900F) {
            this.fSightCurSpeed = 900F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10F;
        if (this.fSightCurSpeed < 150F) {
            this.fSightCurSpeed = 150F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        if (Math.abs(this.FM.Or.getKren()) > 4.5F) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) {
                this.fSightCurReadyness = 0.0F;
            }
        }
        if (this.fSightCurReadyness < 1.0F) {
            this.fSightCurReadyness += 0.0333333F * f;
        } else if (this.bSightAutomation) {
            this.fSightCurDistance -= (this.fSightCurSpeed / 3.6F) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude));
            if (this.fSightCurDistance < ((this.fSightCurSpeed / 3.6F) * Math.sqrt(this.fSightCurAltitude * 0.2038736F))) {
                this.bSightBombDump = true;
            }
            if (this.bSightBombDump) {
                if (this.FM.isTick(3, 0)) {
                    if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
                        this.FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else {
                    this.FM.CT.WeaponControl[3] = false;
                }
            }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(this.fSightCurDistance);
        netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.bSightAutomation = (i & 1) != 0;
        this.bSightBombDump = (i & 2) != 0;
        this.fSightCurDistance = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
        this.fSightCurSideslip = -3F + (netmsginput.readUnsignedByte() / 33.33333F);
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.1F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.1F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.1F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
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

    // Methods carried back from inherited F_102A_Early and F_102A classes to F_102 superclass
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

    String AltitudeDifferenceToString(double ownAltitude, double targetAltitude) {
        String retVal = "level with us";
        //@formatter:off
        if (ownAltitude - targetAltitude - 300D >= 0.0D) retVal = "below us";
        else if (ownAltitude - targetAltitude + 300D <= 0.0D) retVal = "above us";
        else if (ownAltitude - targetAltitude - 150D >= 0.0D) retVal = "slightly below";
        else if (ownAltitude - targetAltitude + 150D < 0.0D) retVal = "slightly above";
        //@formatter:on
        return retVal;
    }

    String TrackOffsetToString(int trackOffset) {
        String retVal = "  ";
        //@formatter:off
        if (trackOffset < 5 || trackOffset > 355) retVal = "dead ahead, ";
        else if (trackOffset < 8) retVal = "right by 5\260, ";
        else if (trackOffset < 13) retVal = "right by 10\260, ";
        else if (trackOffset < 18) retVal = "right by 15\260, ";
        else if (trackOffset < 26) retVal = "right by 20\260, ";
        else if (trackOffset < 36) retVal = "right by 30\260, ";
        else if (trackOffset < 46) retVal = "right by 40\260, ";
        else if (trackOffset <= 60) retVal = "off our right, ";
        else if (trackOffset > 352) retVal = "left by 5\260, ";
        else if (trackOffset > 347) retVal = "left by 10\260, ";
        else if (trackOffset > 342) retVal = "left by 15\260, ";
        else if (trackOffset > 334) retVal = "left by 20\260, ";
        else if (trackOffset > 324) retVal = "left by 30\260, ";
        else if (trackOffset > 314) retVal = "left by 40\260, ";
        else if (trackOffset >= 300) retVal = "off our left, ";
        //@formatter:on
        return retVal;
    }

    private int MtargetDistance(int trackingRange, int targetDistance) {
        int retVal = 0;
        if (targetDistance < trackingRange)
            if (targetDistance > 1150)
                retVal = (int) (Math.ceil(targetDistance / 900D) * 900D);
            else
                retVal = (int) (Math.ceil(targetDistance / 500D) * 500D);
        return retVal;
    }

    private int IRSTtargetDistance(Actor target) {
        double dRetVal = 0D;
        EnginesInterface enginesinterface = ((Aircraft) target).FM.EI;
        for (int engineIndex = 0; engineIndex < enginesinterface.engines.length; engineIndex++) {
            float enginePowerLevel = 0.0F;
            if ((enginesinterface.engines[engineIndex].getType() == Motor._E_TYPE_JET) || (enginesinterface.engines[engineIndex].getType() == Motor._E_TYPE_ROCKET)) {
                enginePowerLevel = enginesinterface.engines[engineIndex].thrustMax;
                if (enginesinterface.engines[engineIndex].getPowerOutput() > 1.0F)
                    enginePowerLevel *= enginesinterface.engines[engineIndex].getPowerOutput() * 1.22F;
                else
                    enginePowerLevel *= (enginesinterface.engines[engineIndex].getPowerOutput() * 0.8F) + 0.2F;
            } else {
                enginePowerLevel = Reflection.getFloat(enginesinterface.engines[engineIndex], "horsePowers") * ((enginesinterface.engines[engineIndex].getPowerOutput() * 0.9F) + 0.1F) * 0.36F;
            }
            dRetVal += enginePowerLevel;
        }

        if (enginesinterface.engines.length > 4)
            dRetVal *= 2D / enginesinterface.engines.length;
        else
            dRetVal /= Math.sqrt(enginesinterface.engines.length);
        dRetVal *= 1.5D;
        return (int) dRetVal;
    }

    boolean TrackingSystem(int trackingMode, int trackingRange, int minAGL) {
        boolean retVal = false;
        Aircraft aircraft = World.getPlayerAircraft();
        if (!(aircraft instanceof F_102))
            return retVal;
        double playerX = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double playerY = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double playerAltitude = aircraft.pos.getAbsPoint().z;
        double playerAGL = playerAltitude - World.land().HQ(aircraft.pos.getAbsPoint().x, aircraft.pos.getAbsPoint().y);
        if (playerAGL < 0.0D)
            playerAGL = 0.0D;
        int playerCourseAbs = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        while (playerCourseAbs < 0)
            playerCourseAbs += 360;
        int playerPitchAbs = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        while (playerPitchAbs < 0)
            playerPitchAbs += 360;
        for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor trackingTarget = (Actor) entry.getValue();
            if ((trackingTarget instanceof Aircraft) && (trackingTarget.getArmy() != World.getPlayerArmy()) && (trackingTarget != World.getPlayerAircraft()) && (trackingTarget.getSpeed(null) > 20D)) {
                double trackingTargetX = Main3D.cur3D().land2D.worldOfsX() + trackingTarget.pos.getAbsPoint().x;
                double trackingTargetY = Main3D.cur3D().land2D.worldOfsY() + trackingTarget.pos.getAbsPoint().y;
                double trackingTargetAGL = trackingTarget.pos.getAbsPoint().z - World.land().HQ(trackingTarget.pos.getAbsPoint().x, trackingTarget.pos.getAbsPoint().y);
                double deltaAGLby10m = Math.ceil((playerAltitude - trackingTargetAGL) / 10D) * 10D;
                String altitudeDifference = AltitudeDifferenceToString(playerAltitude, trackingTargetAGL);
                double xDiff = playerX - trackingTargetX;
                double yDiff = trackingTargetY - playerY;
                double diffSquared = (yDiff * yDiff) + (xDiff * xDiff);
                int targetCourseAbs = (int) (Math.floor(Math.toDegrees(Math.atan2(yDiff, xDiff))) - 90D);
                while (targetCourseAbs < 0)
                    targetCourseAbs += 360;
                int targetCourseRel = targetCourseAbs - playerCourseAbs;
                float trackingRangeAtAltitude = trackingRange;
                if (playerAGL < minAGL)
                    trackingRangeAtAltitude = (float) (playerAGL * 0.8D * 3D);
                int distanceForAltitudeOffset = (int) (Math.ceil(Math.sqrt(diffSquared * TrueRandom.nextDouble(0.9D, 1.1D)) / 10D) * 10D);
                if (distanceForAltitudeOffset > trackingRange)
                    distanceForAltitudeOffset = (int) (Math.ceil(Math.sqrt(diffSquared) / 10D) * 10D);
                int verticalDirectionAbs = (int) (Math.floor(Math.toDegrees(Math.atan2(distanceForAltitudeOffset, deltaAGLby10m))) - 90D);
                int verticalDirectionRel = (verticalDirectionAbs - (90 - playerPitchAbs)) + TrueRandom.nextInt(-3, 3);

                int detectedRange = 0;
                switch (trackingMode) {
                    case TRACKING_SYSTEM_M3:
                    case TRACKING_SYSTEM_M10:
                    default:
                        detectedRange = MtargetDistance(trackingRange, distanceForAltitudeOffset);
                        break;
                    case TRACKING_SYSTEM_IRST:
                        detectedRange = IRSTtargetDistance(trackingTarget);
                        break;
                }

                int targetCourseRelRand = targetCourseRel + TrueRandom.nextInt(-3, 3);
                int targetCourseRelRand360 = targetCourseRelRand;
                while (targetCourseRelRand360 < 0)
                    targetCourseRelRand360 += 360;
                float trackingVertLimit = (float) (trackingRangeAtAltitude + (Math.sin(Math.toRadians(Math.abs(targetCourseRel) * 3)) * (trackingRangeAtAltitude * 0.25D)));
                int distanceForAltitudeLimit = (int) (trackingVertLimit * Math.cos(Math.toRadians(verticalDirectionRel)));
                String trackOffset = TrackOffsetToString(targetCourseRelRand360);

                switch (trackingMode) {
                    case TRACKING_SYSTEM_M3:
                    case TRACKING_SYSTEM_M10:
                    default:
                        if (distanceForAltitudeOffset <= distanceForAltitudeLimit && distanceForAltitudeOffset > 300 && Math.abs(verticalDirectionRel) <= 20 && (Math.abs(targetCourseRelRand) <= 60)) {
                            HUD.log(AircraftHotKeys.hudLogWeaponId, TRACKING_SYSTEM_MESSAGE[trackingMode] + " " + trackOffset + altitudeDifference + ", " + detectedRange + "m");
                            this.freq = 1;
                            retVal = true;
                        } else {
                            this.freq = 7;
                        }
                        break;
                    case TRACKING_SYSTEM_IRST:
                        if (distanceForAltitudeOffset <= distanceForAltitudeLimit && Math.abs(verticalDirectionRel) <= 20 && Math.abs(targetCourseRelRand) <= 60 && distanceForAltitudeOffset < detectedRange) {
                            HUD.log(AircraftHotKeys.hudLogWeaponId, TRACKING_SYSTEM_MESSAGE[trackingMode] + " " + trackOffset + altitudeDifference);
                            this.freq = 1;
                            retVal = true;
                        }
                        break;
                }
                this.setTimer(this.freq);
            }
        }
        return retVal;
    }

    void setTimer(int i) {
        this.Timer1 = TrueRandom.nextFloat(i) * 0.1F;
        this.Timer2 = TrueRandom.nextFloat(i) * 0.1F;
    }

    // Values required for control surface animation
    private static final float ELEVON_LIMIT_SPEED_LOW    = 500F;
    private static final float ELEVON_LIMIT_SPEED_HIGH   = 800F;
    private static final float ELEVON_LIMIT_LOW          = 30F;
    private static final float ELEVON_LIMIT_HIGH         = 20F;
    private static final float RUDDER_LIMIT_SPEED_LOW    = 200F;
    private static final float RUDDER_LIMIT_SPEED_HIGH   = 800F;
    private static final float RUDDER_LIMIT_LOW          = 45F;
    private static final float RUDDER_LIMIT_HIGH         = 20F;
    private static final float CONTROLS_NEUTRAL_FACTOR   = 0.98F;
    private float              elevatorPos               = 0F;
    private float              aileronPos                = 0F;
    private float              rudderPos                 = 0F;
    private float              airbrakePos               = 0F;

    // Values carried back from inherited F_102A_Early and F_102A classes to F_102 superclass
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

    // Tracking Code Consolidation
    public float               Timer1;
    public float               Timer2;
    int                        freq;
    static final int   TRACKING_SYSTEM_M3        = 0;
    static final int   TRACKING_SYSTEM_M10       = 1;
    static final int   TRACKING_SYSTEM_IRST      = 2;
    private static String      TRACKING_SYSTEM_MESSAGE[] = { "M-3: Contact", "M-10: Contact", "IR Tracking!" };

    private boolean            bSightAutomation;
    private boolean            bSightBombDump;
    private float              fSightCurDistance;
    public float               fSightCurForwardAngle;
    public float               fSightCurSideslip;
    public float               fSightCurAltitude;
    public float               fSightCurSpeed;
    public float               fSightCurReadyness;
    private float              deltaAzimuth;
    private float              deltaTangage;
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
    
    static void initCommon(Class f102Class) {
        new NetAircraft.SPAWN(f102Class);
        Property.set(f102Class, "iconFar_shortClassName", "F-102A");
        Property.set(f102Class, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(f102Class, "yearService", 1955F);
        Property.set(f102Class, "yearExpired", 1970F);
        Property.set(f102Class, "FlightModel", "FlightModels/F-102.fmd:F102");
        Property.set(f102Class, "cockpitClass", new Class[] { CockpitF_102.class });
        Property.set(f102Class, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(f102Class, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(f102Class, new String[] { "_CANNON01", "_InternalRock01", "_InternalRock02", "_InternalRock03", "_InternalRock04", "_InternalRock05", "_InternalRock05", "_InternalRock06", "_InternalRock06", "_InternalRock07", "_InternalRock07", "_InternalRock08", "_InternalRock08", "_InternalRock09", "_InternalRock09", "_InternalRock10", "_InternalRock10", "_ExternalDev01", "_ExternalDev02" });
        
        AircraftTools.weaponsRegister(f102Class, "Default", new String[] {"0 MGunNull 250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null});
        AircraftTools.weaponsRegister(f102Class, "24x70mmRockets", new String[] {"0 MGunNull 250", "2 RocketGunFFARMk4_gn16 6", "2 RocketGunFFARMk4_gn16 6", "2 RocketGunFFARMk4_gn16 6", "2 RocketGunFFARMk4_gn16 6", null, null, null, null, null, null, null, null, null, null, null, null, null, null});
        AircraftTools.weaponsRegister(f102Class, "3xAIM4A+3xAIM4C", new String[] {"0 MGunNull 250", null, null, null, null, "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", null, null});
        AircraftTools.weaponsRegister(f102Class, "3xAIM4A+3xAIM4C+24x70mmRockets", new String[] {"0 MGunNull 250", "5 RocketGunFFARMk4_gn16 6", "5 RocketGunFFARMk4_gn16 6", "5 RocketGunFFARMk4_gn16 6", "5 RocketGunFFARMk4_gn16 6", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", null, null});
        AircraftTools.weaponsRegister(f102Class, "2xDroptanks", new String[] {"0 MGunNull 250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "9 FuelTankGun_TankF102 1", "9 FuelTankGun_TankF102 1"});
        AircraftTools.weaponsRegister(f102Class, "24x70mmRockets+2xDroptanks", new String[] {"0 MGunNull 250", "2 RocketGunFFARMk4_gn16 6", "2 RocketGunFFARMk4_gn16 6", "2 RocketGunFFARMk4_gn16 6", "2 RocketGunFFARMk4_gn16 6", null, null, null, null, null, null, null, null, null, null, null, null, "9 FuelTankGun_TankF102 1", "9 FuelTankGun_TankF102 1"});
        AircraftTools.weaponsRegister(f102Class, "3xAIM4A+3xAIM4C+2xDroptanks", new String[] {"0 MGunNull 250", null, null, null, null, "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "9 FuelTankGun_TankF102 1", "9 FuelTankGun_TankF102 1"});
        AircraftTools.weaponsRegister(f102Class, "3xAIM4A+3xAIM4C+24x70mmRockets+2xDroptanks", new String[] {"0 MGunNull 250", "5 RocketGunFFARMk4_gn16 6", "5 RocketGunFFARMk4_gn16 6", "5 RocketGunFFARMk4_gn16 6", "5 RocketGunFFARMk4_gn16 6", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "4 RocketGunAIM4A 1", "4 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "2 RocketGunAIM4C 1", "2 RocketGunNull 1", "9 FuelTankGun_TankF102 1", "9 FuelTankGun_TankF102 1"});
        AircraftTools.weaponsRegister(f102Class, "none", new String[] {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null});
    }

    static {
        Class class1 = F_102.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
