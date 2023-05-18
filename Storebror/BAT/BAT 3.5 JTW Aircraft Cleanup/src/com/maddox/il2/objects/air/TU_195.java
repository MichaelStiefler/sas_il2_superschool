package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.MissileSAM;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class TU_195 extends TU_95X implements TypeBomber, TypeGuidedMissileCarrier, TypeX4Carrier, TypeFastJet, TypeFuelDump, TypeSupersonic, TypeDockable {

    public TU_195() {
        this.SonicBoom = 0.0F;
        this.guidedMissileUtils = null;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.fxSPO2 = this.newSound("aircraft.Sirena2", false);
        this.smplSPO2 = new Sample("sample.Sirena2.wav", 256, 65535);
        this.SPO2SoundPlaying = false;
        this.smplSPO2.setInfinite(true);
        this.counter = 0;
        this.radarmode = 0;
        this.backfireList = new ArrayList();
    }

    public float getFlowRate() {
        return TU_195.FlowRate;
    }

    public float getFuelReserve() {
        return TU_195.FuelReserve;
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
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if ((this.counter++ % 5) == 0) {
            this.sirenaWarning();
        }
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !flag || !(this.FM instanceof Pilot)) {
            return;
        }
        if (flag && (this.FM.AP.way.curr().Action == 3) && this.typeDockableIsDocked() && (Math.abs(this.queen_.FM.Or.getKren()) < 3F)) {
            if (this.FM.isPlayers()) {
                if ((this.FM instanceof RealFlightModel) && !((RealFlightModel) this.FM).isRealMode()) {
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
        for (int i = 1; i < 7; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TU_195.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TU_195.bChangedPit = true;
        }
    }

    private String getPropMeshName(int i, int j) {
        String s = "Prop";
        if (j == 1) {
            s = s + "Rot";
        }
        s = s + (i + 1) + "_D";
        if (j == 2) {
            s = s + "1";
        } else {
            s = s + "0";
        }
        return s;
    }

    protected void moveFan(float f) {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        int i = this.FM.EI.getNum();
        if (this.oldProp.length < (i * 2)) {
            this.oldProp = new int[i * 2];
            this.propPos = new float[i * 2];
            for (int j = 0; j < (i * 2); j++) {
                this.oldProp[j] = 0;
                this.propPos[j] = World.Rnd().nextFloat(-180F, 180F);
            }

        }
        for (int k = 0; k < this.FM.EI.getNum(); k++) {
            int l = this.FM.EI.engines[k].getStage();
            if ((l > 0) && (l < 6)) {
                f = 0.005F * l;
            }
            for (int i1 = 0; i1 < 2; i1++) {
                this.hierMesh().chunkFind(this.getPropMeshName(i1, k));
                int j1 = (k * 2) + i1;
                int k1 = this.oldProp[j1];
                if (k1 < 2) {
                    k1 = Math.abs((int) (this.FM.EI.engines[k].getPropw() * 0.06F));
                    if (k1 >= 1) {
                        k1 = 1;
                    }
                    if ((k1 != this.oldProp[j1]) && this.hierMesh().isChunkVisible(this.getPropMeshName(j1, this.oldProp[j1]))) {
                        this.hierMesh().chunkVisible(this.getPropMeshName(j1, this.oldProp[j1]), false);
                        this.hierMesh().chunkVisible(this.getPropMeshName(j1, k1), true);
                        this.oldProp[j1] = k1;
                    }
                }
                if (k1 == 0) {
                    this.propPos[j1] = (this.propPos[j1] + (57.3F * this.FM.EI.engines[k].getPropw() * f)) % 360F;
                } else {
                    float f1 = 57.3F * this.FM.EI.engines[k].getPropw();
                    f1 %= 2880F;
                    f1 /= 2880F;
                    if (f1 <= 0.5F) {
                        f1 *= 2.0F;
                    } else {
                        f1 = (f1 * 2.0F) - 2.0F;
                    }
                    f1 *= 1200F;
                    this.propPos[j1] = (this.propPos[j1] + (f1 * f)) % 360F;
                }
                if (i1 == 0) {
                    this.hierMesh().chunkSetAngles(this.getPropMeshName(j1, k1), 0.0F, this.propPos[j1], 0.0F);
                } else {
                    this.hierMesh().chunkSetAngles(this.getPropMeshName(j1, k1), 0.0F, -this.propPos[j1], 0.0F);
                }
            }

        }

    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[1].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.hitProp(1, j, actor);
                this.FM.EI.engines[1].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
                // fall through

            case 34:
                this.hitProp(0, j, actor);
                this.FM.EI.engines[0].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
                // fall through

            case 35:
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
                break;

            case 36:
                this.hitProp(2, j, actor);
                this.FM.EI.engines[2].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
                // fall through

            case 37:
                this.hitProp(3, j, actor);
                this.FM.EI.engines[3].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
                // fall through

            case 38:
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
                break;

            case 25:
                this.FM.turret[0].bIsOperable = false;
                return false;

            case 26:
                this.FM.turret[1].bIsOperable = false;
                return false;

            case 19:
                this.killPilot(this, 5);
                this.killPilot(this, 6);
                break;
        }
        return super.cutFM(i, j, actor);
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
        if (this.fSightCurForwardAngle < -15F) {
            this.fSightCurForwardAngle = -15F;
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
        this.fSightCurSideslip += 0.1F;
        if (this.fSightCurSideslip > 3F) {
            this.fSightCurSideslip = 3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.1F;
        if (this.fSightCurSideslip < -3F) {
            this.fSightCurSideslip = -3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 10000F) {
            this.fSightCurAltitude = 10000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 850F) {
            this.fSightCurAltitude = 850F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 150F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 800F) {
            this.fSightCurSpeed = 800F;
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
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
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
            if (this.fSightCurDistance < ((this.fSightCurSpeed / 3.6F) * Math.sqrt(this.fSightCurAltitude * 0.203874F))) {
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
        netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
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
        this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        this.sirenaLaunchWarning();
        int i = this.aircIndex();
        if (this.FM instanceof Maneuver) {
            if (this.typeDockableIsDocked()) {
                if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                    ((Maneuver) this.FM).unblock();
                    ((Maneuver) this.FM).set_maneuver(48);
                    for (int j = 0; j < i; j++) {
                        ((Maneuver) this.FM).push(48);
                    }

                    if (this.FM.AP.way.curr().Action != 3) {
                        ((FlightModelMain) ((Maneuver) this.FM)).AP.way.setCur(this.queen_.FM.AP.way.Cur());
                    }
                    ((Pilot) this.FM).setDumbTime(3000L);
                }
                if (this.FM.M.fuel < this.FM.M.maxFuel) {
                    this.FM.M.fuel += 20F * f;
                }
            } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                if ((this.FM.CT.GearControl == 0.0F) && (this.FM.EI.engines[0].getStage() == 0)) {
                    this.FM.EI.setEngineRunning();
                }
                if ((this.dtime > 0L) && (((Maneuver) this.FM).Group != null)) {
                    ((Maneuver) this.FM).Group.leaderGroup = null;
                    ((Maneuver) this.FM).set_maneuver(22);
                    ((Pilot) this.FM).setDumbTime(3000L);
                    if (Time.current() > (this.dtime + 3000L)) {
                        this.dtime = -1L;
                        ((Maneuver) this.FM).clear_stack();
                        ((Maneuver) this.FM).set_maneuver(0);
                        ((Pilot) this.FM).setDumbTime(0L);
                    }
                } else if (this.FM.AP.way.curr().Action == 0) {
                    Maneuver maneuver = (Maneuver) this.FM;
                    if ((maneuver.Group != null) && (maneuver.Group.airc[0] == this) && (maneuver.Group.clientGroup != null)) {
                        maneuver.Group.setGroupTask(2);
                    }
                }
            }
        }
        this.guidedMissileUtils.update();
        super.update(f);
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if ((this.queen_last != null) && (this.queen_last == actor) && ((this.queen_time == 0L) || (Time.current() < (this.queen_time + 5000L)))) {
            aflag[0] = false;
        } else {
            aflag[0] = true;
        }
    }

    public void missionStarting() {
        this.checkAsDrone();
    }

    private void checkAsDrone() {
        if (this.target_ == null) {
            if (this.FM.AP.way.curr().getTarget() == null) {
                this.FM.AP.way.next();
            }
            this.target_ = this.FM.AP.way.curr().getTarget();
            if (Actor.isValid(this.target_) && (this.target_ instanceof Wing)) {
                Wing wing = (Wing) this.target_;
                int i = this.aircIndex();
                if (Actor.isValid(wing.airc[i / 2])) {
                    this.target_ = wing.airc[i / 2];
                } else {
                    this.target_ = null;
                }
            }
        }
        if (Actor.isValid(this.target_) && (this.target_ instanceof TypeTankerDrogue)) {
            this.queen_last = this.target_;
            this.queen_time = Time.current();
            if (this.isNetMaster()) {
                ((TypeDockable) this.target_).typeDockableRequestAttach(this, this.aircIndex() % 2, true);
            }
        }
        this.bNeedSetup = false;
        this.target_ = null;
    }

    public int typeDockableGetDockport() {
        if (this.typeDockableIsDocked()) {
            return this.dockport_;
        } else {
            return -1;
        }
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
            if (aircraft instanceof TypeTankerDrogue) {
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
            }
        }
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) {
            ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
        }
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
        this.queen_ = (Aircraft) actor;
        this.dockport_ = i;
        this.queen_last = this.queen_;
        this.queen_time = 0L;
        this.FM.EI.setEngineRunning();
        this.FM.CT.setGearAirborne();
        this.moveGear(0.0F);
        FlightModel flightmodel = this.queen_.FM;
        if ((this.aircIndex() == 0) && (this.FM instanceof Maneuver) && (flightmodel instanceof Maneuver)) {
            Maneuver maneuver = (Maneuver) flightmodel;
            Maneuver maneuver1 = (Maneuver) this.FM;
            if ((maneuver.Group != null) && (maneuver1.Group != null) && (maneuver1.Group.numInGroup(this) == (maneuver1.Group.nOfAirc - 1))) {
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
            com.maddox.il2.engine.ActorNet actornet = null;
            if (Actor.isValid(this.queen_)) {
                actornet = this.queen_.net;
                if (actornet.countNoMirrors() > 0) {
                    actornet = null;
                }
            }
            netmsgguaranted.writeByte(this.dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else {
            netmsgguaranted.writeByte(0);
        }
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

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
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

    public void backFire() {
        if (this.backfireList.isEmpty()) {
            return;
        } else {
            ((RocketGunFlare) this.backfireList.remove(0)).shots(3);
            return;
        }
    }

    public boolean typeRadarToggleMode() {
        return true;
    }

    private boolean sirenaWarning() {
        boolean flag = false;
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if (aircraft != null) {
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i += 360;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j += 360;
            }
            Aircraft aircraft1 = War.getNearestEnemy(aircraft, 6000F);
            if ((aircraft1 instanceof Aircraft) && (aircraft.getArmy() != World.getPlayerArmy()) && (aircraft instanceof TypeFighterAceMaker) && ((aircraft instanceof TypeSupersonic) || (aircraft instanceof TypeFastJet)) && (aircraft1 == World.getPlayerAircraft()) && (aircraft1.getSpeed(vector3d) > 20D)) {
                this.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y;
                double d8 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().z;
                new String();
                new String();
                Math.floor(aircraft1.pos.getAbsPoint().z * 0.1D);
                Math.floor((aircraft1.getSpeed(vector3d) * 60D * 60D) / 10000D);
                double d10 = (int) (Math.ceil((d2 - d8) / 10D) * 10D);
                Engine.land();
                int j1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(aircraft1.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(aircraft1.pos.getAbsPoint().y));
                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                if ((j1 >= 28) && (j1 < 32) && (f < 7.5F)) {
                }
                new String();
                double d14 = d4 - d;
                double d16 = d6 - d1;
                float f1 = 57.32484F * (float) Math.atan2(d16, -d14);
                int k1 = (int) (Math.floor((int) f1) - 90D);
                if (k1 < 0) {
                    k1 += 360;
                }
                int l1 = k1 - i;
                double d19 = d - d4;
                double d20 = d1 - d6;
                Random random = new Random();
                float f3 = ((random.nextInt(20) - 10F) / 100F) + 1.0F;
                int l2 = random.nextInt(6) - 3;
                float f4 = 19000F;
                float f5 = f4;
                if (d3 < 1200D) {
                    f5 = (float) (d3 * 0.8D * 3D);
                }
                int i3 = (int) (Math.ceil(Math.sqrt(((d20 * d20) + (d19 * d19)) * f3) / 10D) * 10D);
                if (i3 > f4) {
                    i3 = (int) (Math.ceil(Math.sqrt((d20 * d20) + (d19 * d19)) / 10D) * 10D);
                }
                float f6 = 57.32484F * (float) Math.atan2(i3, d10);
                int j3 = (int) (Math.floor((int) f6) - 90D);
                int k3 = (j3 - (90 - j)) + l2;
                int i4 = l1 + l2;
                int j4 = i4;
                if (j4 < 0) {
                    j4 += 360;
                }
                float f7 = (float) (f5 + (Math.sin(Math.toRadians(Math.sqrt(l1 * l1) * 3D)) * (f5 * 0.25D)));
                int k4 = (int) (f7 * Math.cos(Math.toRadians(k3)));
                if (((double) i3 <= (double) k4) && (i3 <= 14000D) && (i3 >= 200D) && (k3 >= -30) && (k3 <= 30) && (Math.sqrt(i4 * i4) <= 60D)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
            Aircraft aircraft2 = World.getPlayerAircraft();
            double d5 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x;
            double d7 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y;
            double d9 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().z;
            int i1 = (int) (-(aircraft2.pos.getAbsOrient().getYaw() - 90D));
            if (i1 < 0) {
                i1 += 360;
            }
            if (flag && (aircraft1 == World.getPlayerAircraft())) {
                this.pos.getAbs(point3d);
                double d11 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d12 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                double d13 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                double d15 = (int) (Math.ceil((d9 - d13) / 10D) * 10D);
                String s = "";
                if ((d9 - d13 - 500D) >= 0.0D) {
                    s = " low";
                }
                if (((d9 - d13) + 500D) < 0.0D) {
                    s = " high";
                }
                new String();
                double d17 = d11 - d5;
                double d18 = d12 - d7;
                float f2 = 57.32484F * (float) Math.atan2(d18, -d17);
                int i2 = (int) (Math.floor((int) f2) - 90D);
                if (i2 < 0) {
                    i2 += 360;
                }
                int j2 = i2 - i1;
                if (j2 < 0) {
                    j2 += 360;
                }
                int k2 = (int) (Math.ceil((j2 + 15) / 30D) - 1.0D);
                if (k2 < 1) {
                    k2 = 12;
                }
                double d21 = d5 - d11;
                double d22 = d7 - d12;
                double d23 = Math.ceil(Math.sqrt((d22 * d22) + (d21 * d21)) / 10D) * 10D;
                this.bRadarWarning = (d23 <= 8000D) && (d23 >= 500D) && (Math.sqrt(d15 * d15) <= 6000D);
                HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Spike at " + k2 + " o'clock" + s + "!");
                this.bRadarWarning = false;
                this.playSirenaWarning(this.bRadarWarning);
            }
        }
        return true;
    }

    private boolean sirenaLaunchWarning() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        this.pos.getAbs(point3d);
        Aircraft aircraft1 = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) (-(aircraft1.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        List list = Engine.missiles();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor = (Actor) list.get(k);
            if (((actor instanceof Missile) || (actor instanceof MissileSAM)) && (actor.getArmy() != World.getPlayerArmy()) && (actor.getSpeed(vector3d) > 20D)) {
                this.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                String s = "";
                if ((d2 - d5 - 500D) >= 0.0D) {
                    s = " LOW";
                }
                if (((d2 - d5) + 500D) < 0.0D) {
                    s = " HIGH";
                }
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float) Math.atan2(d8, -d7);
                int l = (int) (Math.floor((int) f) - 90D);
                if (l < 0) {
                    l += 360;
                }
                int i1 = l - i;
                if (i1 < 0) {
                    i1 += 360;
                }
                int j1 = (int) (Math.ceil((i1 + 15) / 30D) - 1.0D);
                if (j1 < 1) {
                    j1 = 12;
                }
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D;
                this.bRadarWarning = (d11 <= 8000D) && (d11 >= 500D) && (Math.sqrt(d6 * d6) <= 6000D);
                HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: MISSILE AT " + j1 + " O'CLOCK" + s + "!!!");
                this.bRadarWarning = true;
                this.playSirenaWarning(this.bRadarWarning);
                if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
                    this.backFire();
                }
            } else {
                this.bRadarWarning = false;
                this.playSirenaWarning(this.bRadarWarning);
            }
        }

        return true;
    }

    public void playSirenaWarning(boolean flag) {
        if (flag && !this.SPO2SoundPlaying) {
            this.fxSPO2.play(this.smplSPO2);
            this.SPO2SoundPlaying = true;
        } else if (!flag && this.SPO2SoundPlaying) {
            this.fxSPO2.cancel();
            this.SPO2SoundPlaying = false;
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

    private int                counter;
    public static boolean      bChangedPit = false;
    private float              SonicBoom;
    private Eff3DActor         shockwave;
    private boolean            isSonic;
    private boolean            bSightAutomation;
    private boolean            bSightBombDump;
    private float              fSightCurDistance;
    public float               fSightCurForwardAngle;
    public float               fSightCurSideslip;
    public float               fSightCurAltitude;
    public float               fSightCurSpeed;
    public float               fSightCurReadyness;
    private GuidedMissileUtils guidedMissileUtils;
    private float              deltaAzimuth;
    private float              deltaTangage;
    private Actor              queen_last;
    private long               queen_time;
    private boolean            bNeedSetup;
    private long               dtime;
    private Actor              target_;
    private Aircraft           queen_;
    private int                dockport_;
    public static float        FlowRate    = 100F;
    public static float        FuelReserve = 6500F;
    private SoundFX            fxSPO2;
    private Sample             smplSPO2;
    private boolean            SPO2SoundPlaying;
    private boolean            bRadarWarning;
    private ArrayList          backfireList;
    public int                 radarmode;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;

    static {
        Class class1 = TU_195.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TU-195");
        Property.set(class1, "meshName", "3DO/Plane/Tu-195/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/TU_195FM.fmd:TU195_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTU195.class, CockpitTU195_Bombardier.class, CockpitTU195_RGunner.class, CockpitTU195_AGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 3, 3, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock12", "_ExternalRock12", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalDev06", "_ExternalDev07", "_ExternalRock08", "_ExternalRock08", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalDev08", "_ExternalDev09", "_ExternalRock13", "_ExternalRock13", "_ExternalRock14", "_ExternalRock14", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_InternalRock04", "_InternalRock04", "_Flare01", "_Flare02" });
    }
}
