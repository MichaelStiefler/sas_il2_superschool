package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.Arrays;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
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
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombJATO;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public abstract class F84 extends Scheme1 implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeRadarGunsight, TypeStormovik, TypeGSuit, TypeZBReceiver, TypeFuelDump {

    public float getFlowRate() {
        return F84.FlowRate;
    }

    public float getFuelReserve() {
        return F84.FuelReserve;
    }

    public F84() {
        this.lLightHook = new Hook[4];
        this.SonicBoom = 0.0F;
        this.bSlatsOff = false;
        this.oldthrl = -1F;
        this.curthrl = -1F;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.AirBrakeControl = 0.0F;
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lightTime = 0.0F;
        this.ft = 0.0F;
        this.engineSurgeDamage = 0.0F;
        this.gearTargetAngle = -1F;
        this.hasHydraulicPressure = true;
        this.booster = new Bomb[2];
        this.bHasBoosters = false;
        this.bHasBoosterLoadout = true;
        this.boosterFireOutTime = -1L;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.pk = 0;
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        if (!this.bHasBoosters) {
            return;
        }
        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhiteJATO.eff", 30F);
        Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhiteJATO.eff", 30F);
    }

    public void doCutBoosters() {
        if (!this.bHasBoosters) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].start();
                this.booster[i] = null;
            }
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(F84.NEG_G_TOLERANCE_FACTOR, F84.NEG_G_TIME_FACTOR, F84.NEG_G_RECOVERY_FACTOR, F84.POS_G_TOLERANCE_FACTOR, F84.POS_G_TIME_FACTOR, F84.POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        for (int i = 0; i < 5; i++) {
            if (this.hierMesh().chunkFindCheck("Exhaust" + (i + 1)) >= 0) {
                this.exhaustExists[i] = true;
            }
        }
        this.FM.CT.bHasBombSelect = true;
        this.FM.AS.wantBeaconsNet(true);
        this.polares = (Polares) Reflection.getValue(this.FM, "Wing");
    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        if (this.bHasBoosterLoadout && !this.bHasBoosters) {
            for (int i = 0; i < 2; i++) {
                try {
                    this.booster[i] = new BombJATO();
                    this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
                    this.booster[i].pos.resetAsBase();
                    this.booster[i].drawing(true);
                    this.bHasBoosters = true;
                } catch (Exception exception) {
                    this.debugprintln("Structure corrupt - can't hang JATO Booster..");
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
                    F84.lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, F84.lLightLoc1);
                    F84.lLightLoc1.get(F84.lLightP1);
                    F84.lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, F84.lLightLoc1);
                    F84.lLightLoc1.get(F84.lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(F84.lLightP1, F84.lLightP2, F84.lLightPL)) {
                        F84.lLightPL.z++;
                        F84.lLightP2.interpolate(F84.lLightP1, F84.lLightPL, 0.95F);
                        this.lLight[j].setPos(F84.lLightP2);
                        float f = (float) F84.lLightP1.distance(F84.lLightPL);
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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.0F)) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            if (this.FM.AP.way.isLanding() && (this.FM.getSpeed() > this.FM.VmaxFLAPS) && (this.FM.getSpeed() > (this.FM.AP.way.curr().getV() * 1.4F))) {
                if (this.FM.CT.AirBrakeControl != 1.0F) {
                    this.FM.CT.AirBrakeControl = 1.0F;
                }
            } else if ((((Maneuver) this.FM).get_maneuver() == 25) && this.FM.AP.way.isLanding() && (this.FM.getSpeed() < (this.FM.VmaxFLAPS * 1.16F))) {
                if ((this.FM.getSpeed() > (this.FM.VminFLAPS * 0.5F)) && (this.FM.Gears.nearGround() || this.FM.Gears.onGround())) {
                    if (this.FM.CT.AirBrakeControl != 1.0F) {
                        this.FM.CT.AirBrakeControl = 1.0F;
                    }
                } else if (this.FM.CT.AirBrakeControl != 0.0F) {
                    this.FM.CT.AirBrakeControl = 0.0F;
                }
            } else if (((Maneuver) this.FM).get_maneuver() == 66) {
                if (this.FM.CT.AirBrakeControl != 0.0F) {
                    this.FM.CT.AirBrakeControl = 0.0F;
                }
            } else if (((Maneuver) this.FM).get_maneuver() == 7) {
                if (this.FM.CT.AirBrakeControl != 1.0F) {
                    this.FM.CT.AirBrakeControl = 1.0F;
                }
            } else if (this.hasHydraulicPressure && (this.FM.CT.AirBrakeControl != 0.0F)) {
                this.FM.CT.AirBrakeControl = 0.0F;
            }
        }
        this.ft = World.getTimeofDay() % 0.01F;
        if (this.ft == 0.0F) {
            this.UpdateLightIntensity();
        }
    }

    private final void UpdateLightIntensity() {
        if ((World.getTimeofDay() >= 6F) && (World.getTimeofDay() < 7F)) {
            this.lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        } else if ((World.getTimeofDay() >= 18F) && (World.getTimeofDay() < 19F)) {
            this.lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        } else if ((World.getTimeofDay() >= 7F) && (World.getTimeofDay() < 18F)) {
            this.lightTime = 0.1F;
        } else {
            this.lightTime = 1.0F;
        }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        if (this.FM.actor == World.getPlayerAircraft()) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, F84.k14SightModes[this.k14Mode]);
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
        if (this.FM.actor == World.getPlayerAircraft()) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: " + F84.wingSpans[this.k14WingspanType]);
        }
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        if (this.FM.actor == World.getPlayerAircraft()) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: " + F84.wingSpans[this.k14WingspanType]);
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

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, float gear2Limit, float gear6Limit) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.7F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, gear2Limit), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.6F, 0.0F, -gear2Limit), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, gear6Limit), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.6F, 0.0F, -gear6Limit), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.025F, 0.0F, -110F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.025F, 0.0F, 110F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        F84.moveGear(this.hierMesh(), f, f1, f2, this.gear2Limit, this.gear6Limit);
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
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
                    new EjectionSeat(6, loc, vector3d, aircraft);
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

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, -f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -12F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -12F * f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 13:
                this.FM.Gears.cgear = false;
                float f = World.Rnd().nextFloat(0.0F, 1.0F);
                if (f < 0.1F) {
                    this.FM.AS.hitEngine(this, 0, 100);
                    if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.49D) {
                        this.FM.EI.engines[0].setEngineDies(actor);
                    }
                } else if (f > 0.55D) {
                    this.FM.EI.engines[0].setEngineDies(actor);
                }
                break;
            case 19:
                this.FM.EI.engines[0].setEngineDies(actor);
                break;
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

    public void typeFighterAceMakerRangeFinder() {
        if (this.k14Mode == 2) {
            return;
        }
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (this != World.getPlayerAircraft()) {
            return;
        }
        F84.hunted = Main3D.cur3D().getViewPadlockEnemy();
        if (F84.hunted == null) {
            this.k14Distance = 200F;
            F84.hunted = War.GetNearestEnemyAircraft(this.FM.actor, 2700F, 9);
        }
        if (F84.hunted != null) {
            this.k14Distance = (float) this.FM.actor.pos.getAbsPoint().distance(F84.hunted.pos.getAbsPoint());
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
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
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
            if (this.curthrl == -1F) {
                this.curthrl = this.oldthrl = this.FM.EI.engines[0].getControlThrottle();
            } else {
                this.curthrl = this.FM.EI.engines[0].getControlThrottle();
                if (this.curthrl < 1.05F) {
                    if ((((this.curthrl - this.oldthrl) / f) > 20F) && (this.FM.EI.engines[0].getRPM() < 3200F) && (this.FM.EI.engines[0].getStage() == 6) && (World.Rnd().nextFloat() < 0.4F)) {
                        if (this.FM.actor == World.getPlayerAircraft()) {
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                        this.playSound("weapon.MGunMk108s", true);
                        this.engineSurgeDamage += 0.01D * (this.FM.EI.engines[0].getRPM() / 1000F);
                        this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
                        if ((World.Rnd().nextFloat() < 0.05F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                            this.FM.AS.hitEngine(this, 0, 100);
                        }
                        if ((World.Rnd().nextFloat() < 0.05F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                            this.FM.EI.engines[0].setEngineDies(this);
                        }
                    }
                    if ((((this.curthrl - this.oldthrl) / f) < -20F) && (((this.curthrl - this.oldthrl) / f) > -100F) && (this.FM.EI.engines[0].getRPM() < 3200F) && (this.FM.EI.engines[0].getStage() == 6)) {
                        this.playSound("weapon.MGunMk108s", true);
                        this.engineSurgeDamage += 0.001D * (this.FM.EI.engines[0].getRPM() / 1000F);
                        this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
                        if ((World.Rnd().nextFloat() < 0.4F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
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

    public void applySootState() {
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if ((this.FM.EI.engines[0].getPowerOutput() > 0.5F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 2.0F) {
                    this.FM.AS.setSootState(this, 0, 5);
                } else {
                    this.FM.AS.setSootState(this, 0, 4);
                }
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
    }

    public void update(float f) {
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && (this.FM.getSpeedKMH() > 15F)) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            this.bailout();
        }
        this.engineSurge(f);
        if (this == World.getPlayerAircraft()) {
            this.typeFighterAceMakerRangeFinder();
        }
        this.soundbarier();
        this.computeLift();
        super.update(f);
        this.applySootState();
        if ((this.FM instanceof Pilot) && this.bHasBoosters) {
            if ((this.FM.getAltitude() > 300F) && (this.boosterFireOutTime == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
            }
            if (this.bHasBoosters && (this.boosterFireOutTime == -1L) && this.FM.Gears.onGround() && (this.FM.EI.getPowerOutput() > 0.8F) && (this.FM.getSpeedKMH() > 20F)) {
                this.boosterFireOutTime = Time.current() + 30000L;
                this.doFireBoosters();
                this.FM.AS.setGliderBoostOn();
            }
            if (this.bHasBoosters && (this.boosterFireOutTime > 0L)) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 20000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 10000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
        }
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        this.hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
        if (this.FM.CT.GearControl > 0.5F) {
            this.hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -60F * f, 0.0F);
        }
    }

    protected void moveFlap(float f) {
        float f1 = 55F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, f1);
    }

    protected void moveFan(float f) {
        this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
        if (this.pk >= 1) {
            this.pk = 1;
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Prop1_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("PropRot1_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop1_D0", 0.0F, this.dynamoOrient, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(13.35D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(8.77F, shot);
                } else if (s.endsWith("g1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(40F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                }
            } else if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
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
            } else if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("bloc")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
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
            } else if (s.startsWith("xxtank")) {
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
            } else if (s.startsWith("xxspar")) {
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
            } else if (s.startsWith("xxhyd")) {
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            } else if (s.startsWith("xxpnm")) {
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
        } else {
            if (s.startsWith("xcockpit")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                this.getEnergyPastArmor(0.05F, shot);
            }
            if (s.startsWith("xxmgun1") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(1, 0);
            }
            if (s.startsWith("xxmgun2") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(1, 1);
            }
            if (s.startsWith("xxmgun3") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(1, 2);
            }
            if (s.startsWith("xxmgun4") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(1, 3);
            }
            if (s.startsWith("xxmgun5") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(1, 4);
            }
            if (s.startsWith("xxmgun6") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(1, 5);
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
            } else if (s.startsWith("xstab")) {
                if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                    this.hitChunk("StabL", shot);
                }
                if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
                    this.hitChunk("StabR", shot);
                }
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
                if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                    this.hitChunk("WingLOut", shot);
                }
                if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                    this.hitChunk("WingROut", shot);
                }
            } else if (s.startsWith("xarone")) {
                if (s.startsWith("xaronel")) {
                    this.hitChunk("AroneL", shot);
                }
                if (s.startsWith("xaroner")) {
                    this.hitChunk("AroneR", shot);
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
    }

    protected void moveAirBrake(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.25F);
        this.hierMesh().chunkSetLocate("Brake01_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -70F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 0.9F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
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
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                break;

            case 3:
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                // fall through

            case 2:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 0.75F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                break;

            case 5:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
                // fall through

            case 4:
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                break;
        }
    }

    public void setExhaustFlame(int i, int j) {
        if (j == 0) {
            for (int k = 0; k < 5; k++) {
                if (this.exhaustExists[k]) {
                    this.hierMesh().chunkVisible("Exhaust" + (k + 1), Arrays.binarySearch(F84.exhaustOn[k], i) >= 0);
                }
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
                        if ((byte0 > 10) && (byte0 <= 19)) {
                            EventLog.onBailedOut(this, byte0 - 11);
                        }
                    }
                }
            }
        }
    }

    public void computeLift() {
        float f = this.calculateMach();
        if (f < 0.9F) {
            this.polares.lineCyCoeff = 0.08F;
        } else if (f < 1.25F) {
            float f1 = f * f;
            this.polares.lineCyCoeff = ((0.114286F * f1) - (0.417143F * f)) + 0.362857F;
        } else {
            this.polares.lineCyCoeff = 0.02F;
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

    protected boolean          bSlatsOff;
    private float              oldthrl;
    private float              curthrl;
    public int                 k14Mode;
    public int                 k14WingspanType;
    public float               k14Distance;
    public float               AirBrakeControl;
    private boolean            overrideBailout;
    private boolean            ejectComplete;
    private float              lightTime;
    private float              ft;
    private LightPointWorld    lLight[];
    private Hook               lLightHook[];
    private static Loc         lLightLoc1             = new Loc();
    private static Point3d     lLightP1               = new Point3d();
    private static Point3d     lLightP2               = new Point3d();
    private static Point3d     lLightPL               = new Point3d();
    private float              SonicBoom;
    private Eff3DActor         shockwave;
    private boolean            isSonic;
    public static int          LockState              = 0;
    private static Actor       hunted                 = null;
    private float              engineSurgeDamage;
    protected float            gearTargetAngle;
    public boolean             hasHydraulicPressure;
    public static float        FlowRate               = 8.5F;
    public static float        FuelReserve            = 1000F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;

    private static String[]    k14SightModes          = { "Sight Mode: Caged", "Sight Mode: Uncaged", "Sight Off" };
    private static String[]    wingSpans              = { "MiG-17/19/21", "MiG-15", "Me-262", "Pe-2", "60ft", "Canberra Bomber", "Yak-28/Il-28", "C-47", "Tu-16", "Tu-4" };
    private static int         exhaustOn[][]          = { { 1, 3, 5, 7, 10 }, { 2, 3, 6, 8, 11 }, { 4, 5, 6, 9, 12 }, { 7, 8, 9 }, { 10, 11, 12 } };

    Bomb                       booster[];
    private boolean            bHasBoosters;
    boolean                    bHasBoosterLoadout;
    private long               boosterFireOutTime;
    private float              dynamoOrient;
    private boolean            bDynamoRotary;
    private int                pk;
    float                      gear2Limit             = 81F;
    float                      gear6Limit             = 88F;
    private Polares            polares;

    private boolean            exhaustExists[]        = { false, false, false, false, false };

    static {
        Class class1 = F84.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
