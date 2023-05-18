package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.BombGunGBU10_Mk84LGB_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU12_Mk82LGB_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU16_Mk83LGB_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU31_Mk84JDAM_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU32_Mk83JDAM_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU38_Mk82JDAM_gn16;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Pylon_AN_AAQ28_gn16;
import com.maddox.il2.objects.weapons.RocketGun5inchZuniMk71AP_gn16;
import com.maddox.il2.objects.weapons.RocketGun5inchZuniMk71WPFAC_gn16;
import com.maddox.il2.objects.weapons.RocketGun5inchZuniMk71WPFO_gn16;
import com.maddox.il2.objects.weapons.RocketGun5inchZuniMk71_gn16;
import com.maddox.il2.objects.weapons.RocketGunAGM65E_gn16;
import com.maddox.il2.objects.weapons.RocketGunAGM84D_gn16;
import com.maddox.il2.objects.weapons.RocketGunAGM84E_gn16;
import com.maddox.il2.objects.weapons.RocketGunAGM84J_gn16;
import com.maddox.il2.objects.weapons.RocketGunChaff_gn16;
import com.maddox.il2.objects.weapons.RocketGunFlare_gn16;
import com.maddox.il2.objects.weapons.RocketGunHYDRA_WPFAC_gn16;
import com.maddox.il2.objects.weapons.RocketGunHYDRA_WPFO_gn16;
import com.maddox.il2.objects.weapons.RocketGunHYDRA_gn16;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class AV_8B extends AV_8 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeDockable, TypeRadarWarningReceiver, TypeLaserDesignator, TypeRadar, TypeSemiRadar, TypeGroundRadar {

    public AV_8B() {
        this.bRWR_Show_Text_Warning = true;
        this.iDebugLogLevel = 0;
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.queen_last = null;
        this.queen_time = 0L;
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.target_ = null;
        this.queen_ = null;
        this.dockport_ = -1;
        this.fuelReceiveRate = 11.101F;
        this.radartoggle = false;
        this.radarvrt = 0.0F;
        this.radarhol = 0.0F;
        this.lockmode = 0;
        this.radargunsight = 0;
        this.radarmode = 0;
        this.radarrange = 1;
        this.targetnum = 0;
        this.lockrange = 0.04F;
        this.leftscreen = 2;
        this.tf = 0L;
        this.tangate = 0.0F;
        this.azimult = 0.0F;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.counterFlareList = new ArrayList();
        this.counterChaffList = new ArrayList();
        this.backfire = false;
        this.bHasAGM = false;
        this.bHasLAGM = false;
        this.bHasAShM = false;
        this.bHasUGR = false;
        this.lastAGMcheck = -1L;
        this.bHasLaser = false;
        this.FLIR = false;
        this.bLaserOn = false;
        this.laserSpotPos = new Point3d();
        this.laserTimer = -1L;
        this.bLGBengaged = false;
        this.bHasPaveway = false;
        this.bHadLGweapons = false;
        this.bAILaserOn = false;
        this.lAILaserTimer = -1L;
        this.lAIGAskipTimer = -1L;
        this.lastLGBcheck = -1L;
        this.lastAIMissileSelect = -1L;
        this.hold = false;
        this.holdFollow = false;
        this.actorFollowing = null;
        this.t1 = 0L;
        this.counter = 0;
        this.error = 0;
        this.raretimer = 0L;
        this.semiradartarget = null;
        this.groundradartarget = null;
        if (Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) {
            this.bRWR_Show_Text_Warning = false;
        }
        this.rwrUtils = new RadarWarningReceiverUtils(this, AV_8B.RWR_GENERATION, AV_8B.RWR_MAX_DETECT, AV_8B.RWR_KEEP_SECONDS, AV_8B.RWR_RECEIVE_ELEVATION, AV_8B.RWR_DETECT_IRMIS, AV_8B.RWR_DETECT_ELEVATION, this.bRWR_Show_Text_Warning);
//    private static final int RWR_GENERATION = 1;
//    private static final int RWR_MAX_DETECT = 16;
//    private static final int RWR_KEEP_SECONDS = 7;
//    private static final double RWR_RECEIVE_ELEVATION = 45D;
//    private static final boolean RWR_DETECT_IRMIS = false;
//    private static final boolean RWR_DETECT_ELEVATION = false;
        this.iDebugLogLevel = 0;
    }

    private static final float toMeters(float f) {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f) {
        return 0.4470401F * f;
    }

    private void checkAmmo() {
        this.counterFlareList.clear();
        this.counterChaffList.clear();
        this.bHasLaser = false;
        this.bHasPaveway = false;
        this.bHadLGweapons = false;
        this.bHasLAGM = false;
        this.bHasAGM = false;
        this.bHasAShM = false;
        this.bHasUGR = false;
        this.FM.bNoDiveBombing = false;
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] == null) {
                continue;
            }
            for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                if (this.FM.CT.Weapons[i][j].haveBullets()) {
                    if (this.FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16) {
                        this.counterFlareList.add(this.FM.CT.Weapons[i][j]);
                        continue;
                    }
                    if (this.FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16) {
                        this.counterChaffList.add(this.FM.CT.Weapons[i][j]);
                        continue;
                    }
                    if ((this.FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)) {
                        this.bHasPaveway = true;
                        this.bHadLGweapons = true;
                        this.FM.bNoDiveBombing = true;
                        continue;
                    }
                    if ((this.FM.CT.Weapons[i][j] instanceof BombGunGBU38_Mk82JDAM_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU32_Mk83JDAM_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU31_Mk84JDAM_gn16)) {
                        this.FM.bNoDiveBombing = true;
                        continue;
                    }
                    if (this.FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16) {
                        this.bHasAGM = true;
                        continue;
                    }
                    if (this.FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16) {
                        this.bHasLAGM = true;
                        this.bHasAGM = true;
                        this.bHadLGweapons = true;
                        continue;
                    }
                    if ((this.FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)) {
                        this.bHasAShM = true;
                        continue;
                    }
                    if ((this.FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFAC_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFO_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFAC_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFO_gn16)) {
                        this.bHasUGR = true;
                    }
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_AN_AAQ28_gn16) {
                    this.bHasLaser = true;
                }
            }

        }

    }

    private void checkAIAGMrest() {
        this.bHasLAGM = false;
        this.bHasAGM = false;
        this.bHasAShM = false;
        this.bHasUGR = false;
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] == null) {
                continue;
            }
            for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                if (!this.FM.CT.Weapons[i][j].haveBullets()) {
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16) {
                    this.bHasAGM = true;
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16) {
                    this.bHasLAGM = true;
                    this.bHasAGM = true;
                    continue;
                }
                if ((this.FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)) {
                    this.bHasAShM = true;
                    continue;
                }
                if ((this.FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFAC_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFAC_gn16) || (this.FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFO_gn16)) {
                    this.bHasUGR = true;
                }
            }

        }

    }

    private void checkGuidedBombRest() {
        if (!((Maneuver) this.FM).hasBombs()) {
            this.bHasPaveway = false;
            this.FM.bNoDiveBombing = false;
        } else if (this.bHasPaveway || this.FM.bNoDiveBombing) {
            boolean flag = true;
            boolean flag1 = true;
            for (int i = 3; (i < 4) && flag && flag1; i++) {
                if (this.FM.CT.Weapons[i] == null) {
                    continue;
                }
                for (int j = 0; (j < this.FM.CT.Weapons[i].length) && flag && flag1; j++) {
                    if (!this.FM.CT.Weapons[i][j].haveBullets()) {
                        continue;
                    }
                    if ((this.FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)) {
                        flag = false;
                        continue;
                    }
                    if ((this.FM.CT.Weapons[i][j] instanceof BombGunGBU38_Mk82JDAM_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU32_Mk83JDAM_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU31_Mk84JDAM_gn16)) {
                        flag1 = false;
                    }
                }

            }

            if (flag) {
                this.bHasPaveway = false;
            }
            if (flag && flag1) {
                this.FM.bNoDiveBombing = false;
            }
        }
    }

    private void backFire() {
        if (this.counterFlareList.isEmpty()) {
            this.hasFlare = false;
        } else if (Time.current() > (this.lastFlareDeployed + 700L)) {
            ((RocketGunFlare_gn16) this.counterFlareList.get(0)).shots(1);
            this.hasFlare = true;
            this.lastFlareDeployed = Time.current();
            if (!((RocketGunFlare_gn16) this.counterFlareList.get(0)).haveBullets()) {
                this.counterFlareList.remove(0);
            }
        }
        if (this.counterChaffList.isEmpty()) {
            this.hasChaff = false;
        } else if (Time.current() > (this.lastChaffDeployed + 900L)) {
            ((RocketGunChaff_gn16) this.counterChaffList.get(0)).shots(1);
            this.hasChaff = true;
            this.lastChaffDeployed = Time.current();
            if (!((RocketGunChaff_gn16) this.counterChaffList.get(0)).haveBullets()) {
                this.counterChaffList.remove(0);
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

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            if (!this.radartoggle) {
                this.radartoggle = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar ON");
                this.radarmode = 0;
            } else {
                this.radartoggle = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar OFF");
            }
        }
        if (i == 22) {
            this.lockmode++;
            if (this.lockmode > 1) {
                this.lockmode = 0;
            }
        }
        if (i == 23) {
            this.radargunsight++;
            if (this.radargunsight > 3) {
                this.radargunsight = 0;
            }
            switch (this.radargunsight) {
                case 0:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: funnel");
                    break;

                case 1:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Radar ranging");
                    break;

                case 2:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Unguided Rocket");
                    break;

                case 3:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Ground");
                    break;
            }
        }
        if (i == 24) {
            this.leftscreen++;
            if (this.leftscreen > 2) {
                this.leftscreen = 0;
            }
            switch (this.leftscreen) {
                case 0:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Fuel");
                    break;

                case 1:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: FPAS");
                    break;

                case 2:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Engine");
                    break;
            }
        }
        if ((i == 26) && this.bHasLaser) {
            if (this.hold && (Time.current() > (this.t1 + 200L)) && this.FLIR) {
                this.hold = false;
                this.holdFollow = false;
                this.actorFollowing = null;
                HUD.log("Laser Pos Unlock");
                this.t1 = Time.current();
            }
            if (!this.hold && (Time.current() > (this.t1 + 200L)) && this.FLIR) {
                this.hold = true;
                this.holdFollow = false;
                this.actorFollowing = null;
                HUD.log("Laser Pos Lock");
                this.t1 = Time.current();
            }
            if (!this.FLIR) {
                this.setLaserOn(false);
            }
        }
        if ((i == 27) && this.bHasLaser) {
            if (this.holdFollow && (Time.current() > (this.t1 + 200L)) && this.FLIR) {
                this.hold = false;
                this.holdFollow = false;
                this.actorFollowing = null;
                HUD.log("Laser Track Unlock");
                this.t1 = Time.current();
            }
            if (!this.holdFollow && (Time.current() > (this.t1 + 200L)) && this.FLIR) {
                this.hold = false;
                this.holdFollow = true;
                this.actorFollowing = null;
                HUD.log("Laser Track Lock");
                this.t1 = Time.current();
            }
        }
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
        if (this.FLIR) {
            this.azimult++;
            this.tf = Time.current();
        } else if (this.radartoggle && (this.lockmode == 0)) {
            this.radarhol += 0.0035F;
        }
    }

    public void typeBomberAdjDistanceMinus() {
        if (this.FLIR) {
            this.azimult--;
            this.tf = Time.current();
        } else if (this.radartoggle && (this.lockmode == 0)) {
            this.radarhol -= 0.0035F;
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.vtolSlipY = 0;
    }

    public void typeBomberAdjSideslipPlus() {
        if ((this.nozzlemode == 1) && (this.vtolvect > 0.74F)) {
            this.vtolSlipY += 10;
            if (this.vtolSlipY > 100) {
                this.vtolSlipY = 100;
            }
            if (this == World.getPlayerAircraft()) {
                if (this.vtolSlipY == 0) {
                    HUD.log("Side Slip Thrust: Neutral");
                } else if (this.vtolSlipY > 0) {
                    HUD.log("Side Slip Thrust: Right" + Math.abs(this.vtolSlipY));
                } else {
                    HUD.log("Side Slip Thrust: Left" + Math.abs(this.vtolSlipY));
                }
            }
        } else if (this.FLIR) {
            this.tangate++;
            this.tf = Time.current();
        } else if (this.radartoggle && (this.lockmode == 0)) {
            this.radarvrt += 0.0035F;
        }
    }

    public void typeBomberAdjSideslipMinus() {
        if ((this.nozzlemode == 1) && (this.vtolvect > 0.74F)) {
            this.vtolSlipY -= 10;
            if (this.vtolSlipY < -100) {
                this.vtolSlipY = -100;
            }
            if (this == World.getPlayerAircraft()) {
                if (this.vtolSlipY == 0) {
                    HUD.log("Side Slip Thrust: Neutral");
                } else if (this.vtolSlipY > 0) {
                    HUD.log("Side Slip Thrust: Right" + Math.abs(this.vtolSlipY));
                } else {
                    HUD.log("Side Slip Thrust: Left" + Math.abs(this.vtolSlipY));
                }
            }
        } else if (this.FLIR) {
            this.tangate--;
            this.tf = Time.current();
        } else if (this.radartoggle && (this.lockmode == 0)) {
            this.radarvrt -= 0.0035F;
        }
    }

    public void typeBomberAdjAltitudeReset() {
        this.vtolSlipX = 0;
    }

    public void typeBomberAdjAltitudePlus() {
        if ((this.nozzlemode == 1) && (this.vtolvect > 0.74F)) {
            this.vtolSlipX += 10;
            if (this.vtolSlipX > 100) {
                this.vtolSlipX = 100;
            }
            if (this == World.getPlayerAircraft()) {
                if (this.vtolSlipX == 0) {
                    HUD.log("Forward Slip Thrust: Neutral");
                } else if (this.vtolSlipX > 0) {
                    HUD.log("Forward Slip Thrust: " + Math.abs(this.vtolSlipX));
                } else {
                    HUD.log("Backward Slip Thrust: " + Math.abs(this.vtolSlipX));
                }
            }
        } else if (this.FLIR) {
            if (!this.APmode1) {
                this.APmode1 = true;
                this.FM.AP.setStabAltitude((float) this.FM.Loc.z);
                if (this == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                }
            } else if (this.APmode1) {
                this.APmode1 = false;
                this.FM.AP.setStabAltitude(false);
                if (this == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                }
            }
        }
    }

    public void typeBomberAdjAltitudeMinus() {
        if ((this.nozzlemode == 1) && (this.vtolvect > 0.74F)) {
            this.vtolSlipX -= 10;
            if (this.vtolSlipX < -100) {
                this.vtolSlipX = -100;
            }
            if (this == World.getPlayerAircraft()) {
                if (this.vtolSlipX == 0) {
                    HUD.log("Forward Slip Thrust: Neutral");
                } else if (this.vtolSlipX > 0) {
                    HUD.log("Forward Slip Thrust: " + Math.abs(this.vtolSlipX));
                } else {
                    HUD.log("Backward Slip Thrust: " + Math.abs(this.vtolSlipX));
                }
            }
        }
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
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
            this.fSightCurDistance -= AV_8B.toMetersPerSecond(this.fSightCurSpeed) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / AV_8B.toMeters(this.fSightCurAltitude)));
            if (this.fSightCurDistance < (AV_8B.toMetersPerSecond(this.fSightCurSpeed) * (float) Math.sqrt(AV_8B.toMeters(this.fSightCurAltitude) * (2F / 9.81F)))) {
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

    public boolean typeBomberToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        if (this == World.getPlayerAircraft()) {
            switch (this.k14Mode) {
                case 0:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
                    break;

                case 1:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gunnery");
                    break;

                case 2:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
                    break;
            }
        }
        return true;
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

    private void laser(Point3d point3d) {
        point3d.z = World.land().HQ(point3d.x, point3d.y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(point3d.x, point3d.y, point3d.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR() {
        List list = Engine.targets();
        int i = list.size();
        for (int j = 0; j < i; j++) {
            Actor actor = (Actor) list.get(j);
            if ((!(actor instanceof Aircraft) && !(actor instanceof ArtilleryGeneric) && !(actor instanceof CarGeneric) && !(actor instanceof TankGeneric)) || (actor instanceof StationaryGeneric) || (actor instanceof TypeLaserDesignator) || (actor.pos.getAbsPoint().distance(this.pos.getAbsPoint()) >= 30000D)) {
                continue;
            }
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            actor.pos.getAbs(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 1500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
            if (actor instanceof Aircraft) {
                lightpointactor.light.setEmit(8F, 50F);
            } else if (!(actor instanceof ArtilleryGeneric)) {
                lightpointactor.light.setEmit(5F, 30F);
            } else {
                lightpointactor.light.setEmit(3F, 10F);
            }
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }

    }

    public void updatecontrollaser() {
        if ((this.tf + 5L) <= Time.current()) {
            this.tangate = 0.0F;
            this.azimult = 0.0F;
        }
        if (!this.FLIR && (this.laserTimer > 0L) && (Time.current() > this.laserTimer) && this.getLaserOn()) {
            this.setLaserOn(false);
        }
        if (this.bHasPaveway) {
            this.checkgroundlaser();
        }
    }

    private void checkgroundlaser() {
        boolean flag = false;
        float f5 = 0.0F;
        if (this.getLaserOn()) {
            Point3d point3d = new Point3d();
            point3d = this.getLaserSpot();
            if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) >= 1.0F)) {
                double d1 = this.pos.getAbsPoint().distance(point3d);
                if (d1 <= AV_8B.maxPavewayDistance) {
                    float f1 = AV_8B.angleBetween(this, point3d);
                    if (f1 <= AV_8B.maxPavewayFOVfrom) {
                        flag = true;
                    }
                }
            }
        }
        if (!flag) {
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if (!(actor instanceof TypeLaserDesignator) || !((TypeLaserDesignator) actor).getLaserOn() || (actor.getArmy() != this.getArmy())) {
                    continue;
                }
                Point3d point3d1 = new Point3d();
                point3d1 = ((TypeLaserDesignator) actor).getLaserSpot();
                if ((Main.cur().clouds != null) && (Main.cur().clouds.getVisibility(point3d1, this.pos.getAbsPoint()) < 1.0F)) {
                    continue;
                }
                double d2 = this.pos.getAbsPoint().distance(point3d1);
                if (d2 > AV_8B.maxPavewayDistance) {
                    continue;
                }
                float f2 = AV_8B.angleBetween(this, point3d1);
                if (f2 > AV_8B.maxPavewayFOVfrom) {
                    continue;
                }
                float f4 = 1.0F / f2 / (float) (d2 * d2);
                if (f4 > f5) {
                    f5 = f4;
                    flag = true;
                }
            }

        }
        this.setLaserArmEngaged(flag);
    }

    private static float angleBetween(Actor actor, Point3d point3d) {
        float f = 180.1F;
        double d = 0.0D;
        Loc loc = new Loc();
        Point3d point3d1 = new Point3d();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d1);
        vector3d.sub(point3d, point3d1);
        d = vector3d.length();
        vector3d.scale(1.0D / d);
        vector3d1.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d1);
        d = vector3d1.dot(vector3d);
        f = Geom.RAD2DEG((float) Math.acos(d));
        return f;
    }

    public Point3d getLaserSpot() {
        return this.laserSpotPos;
    }

    public boolean setLaserSpot(Point3d point3d) {
        this.laserSpotPos = point3d;
        return true;
    }

    public boolean getLaserOn() {
        return this.bLaserOn;
    }

    public boolean setLaserOn(boolean flag) {
        if (this.bLaserOn != flag) {
            if (!this.bLaserOn) {
                if (this == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
                }
            } else {
                if (this == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: OFF");
                }
                this.hold = false;
                this.holdFollow = false;
                this.actorFollowing = null;
            }
        }
        return this.bLaserOn = flag;
    }

    public boolean getLaserArmEngaged() {
        return this.bLGBengaged;
    }

    public boolean setLaserArmEngaged(boolean flag) {
        if ((this.bLGBengaged != flag) && (this == World.getPlayerAircraft())) {
            if (!this.bLGBengaged) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Engaged");
            } else {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Disengaged");
            }
        }
        return this.bLGBengaged = flag;
    }

    private void AILaserControl() {
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        boolean flag = false;
        if (((Maneuver) this.FM).target_ground != null) {
            point3d = ((Maneuver) this.FM).target_ground.pos.getAbsPoint();
            flag = (this.pos.getAbsPoint().distance(point3d) < 20000D) && (Main.cur().clouds != null) && (Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) > 0.98F) && !Landscape.rayHitHQ(this.pos.getAbsPoint(), point3d, point3d1);
        }
        if ((this.bHasLAGM || this.bHasPaveway) && (this.FM.AP.way.curr().Action == 3) && (((Maneuver) this.FM).target_ground != null) && !this.bAILaserOn) {
            if (flag) {
                this.bAILaserOn = true;
                this.setLaserOn(true);
                this.lAILaserTimer = -1L;
            }
        } else if (this.bAILaserOn) {
            if ((((Maneuver) this.FM).target_ground == null) || !flag) {
                this.bAILaserOn = false;
                this.setLaserOn(false);
                this.lAILaserTimer = -1L;
            } else if ((this.lAILaserTimer > 0L) && (Time.current() > this.lAILaserTimer)) {
                this.bAILaserOn = false;
                this.setLaserOn(false);
                this.lAILaserTimer = -1L;
            } else if (this.lAILaserTimer == -1L) {
                this.lAILaserTimer = Time.current() + 20000L + ((long) Math.min(this.FM.getAltitude(), 4000F) * 5L);
            }
        }
        if (this.bAILaserOn && (((Maneuver) this.FM).target_ground != null) && (((Maneuver) this.FM).target_ground.pos != null) && Actor.isValid(((Maneuver) this.FM).target_ground)) {
            this.setLaserSpot(point3d);
        }
    }

    public RadarWarningReceiverUtils getRadarWarningReceiverUtils() {
        return this.rwrUtils;
    }

    public void myRadarSearchYou(Actor actor, String s) {
        this.rwrUtils.recordRadarSearched(actor, s);
    }

    public void myRadarLockYou(Actor actor, String s) {
        this.rwrUtils.recordRadarLocked(actor, s);
    }

    public Actor getSemiActiveRadarLockedActor() {
        if (this.getSemiActiveRadarOn()) {
            return this.semiradartarget;
        } else {
            return null;
        }
    }

    public Actor setSemiActiveRadarLockedActor(Actor actor) {
        if (this.getSemiActiveRadarOn()) {
            if (this.iDebugLogLevel > 2) {
                if (actor == null) {
                    HUD.log("Semi-Active Radar lock-off.");
                } else {
                    String s = actor.getClass().getName();
                    int i = s.lastIndexOf('.');
                    int j = s.lastIndexOf('$');
                    if (i < j) {
                        i = j;
                    }
                    String s1 = s.substring(i + 1);
                    HUD.log("Semi-Active Radar lock-on " + s1);
                }
            }
            this.semiradartarget = actor;
            return actor;
        } else {
            this.semiradartarget = null;
            return null;
        }
    }

    public boolean getSemiActiveRadarOn() {
        return this.radartoggle && ((this.radarmode == 0) || (this.radarmode == 1));
    }

    public boolean setSemiActiveRadarOn(boolean flag) {
        if (flag) {
            this.radartoggle = true;
            if (this.radarmode == 2) {
                this.radarmode = 0;
            }
        } else {
            this.radartoggle = false;
        }
        return flag;
    }

    public Actor getGroundRadarLockedActor() {
        if (this.getGroundRadarOn()) {
            return this.groundradartarget;
        } else {
            return null;
        }
    }

    public Actor setGroundRadarLockedActor(Actor actor) {
        if (this.getGroundRadarOn()) {
            if (this.iDebugLogLevel > 2) {
                if (actor == null) {
                    HUD.log("Ground Radar lock-off.");
                } else {
                    String s = actor.getClass().getName();
                    int i = s.lastIndexOf('.');
                    int j = s.lastIndexOf('$');
                    if (i < j) {
                        i = j;
                    }
                    String s1 = s.substring(i + 1);
                    HUD.log("Ground Radar lock-on " + s1);
                }
            }
            this.groundradartarget = actor;
            return actor;
        } else {
            this.groundradartarget = null;
            return null;
        }
    }

    public boolean getGroundRadarOn() {
        return this.radartoggle && (this.radarmode == 2);
    }

    public boolean setGroundRadarOn(boolean flag) {
        if (flag) {
            this.radartoggle = true;
            if (this.radarmode != 2) {
                this.radarmode = 2;
            }
        } else {
            this.radartoggle = false;
        }
        return flag;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.turret[0].bIsAIControlled = false;
        this.t1 = Time.current();
        this.rwrUtils.onAircraftLoaded();
        this.rwrUtils.setLockTone("aircraft.usRWRScan", "aircraft.usRWRLock", "aircraft.usRWRLaunchWarningMissileMissile", "aircraft.usRWRThreatNew");
    }

    public void missionStarting() {
        super.missionStarting();
        this.checkAmmo();
        this.checkAsDrone();
        this.FM.CT.toggleRocketHook();
        this.FM.CT.FlapsControlSwitch = 1;
        this.laserTimer = -1L;
        this.bLaserOn = false;
        this.FLIR = false;
        this.iDebugLogLevel = Config.cur.ini.get("Mods", "GuidedMissileDebugLog", 0);
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
            if ((aircraft instanceof TypeTankerDrogue) && (this.FM.CT.getRefuel() > 0.95F)) {
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
        this.moveGear(0.0F, 0.0F, 0.0F);
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
            ActorNet actornet = null;
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

    private void receivingRefuel(float f) {
        int i = this.aircIndex();
        if (this.typeDockableIsDocked()) {
            if (this.FM.CT.getRefuel() < 0.9F) {
                this.typeDockableAttemptDetach();
                return;
            }
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
            FuelTank afueltank[] = this.FM.CT.getFuelTanks();
            if (this.FM.M.fuel < (this.FM.M.maxFuel - this.fuelReceiveRate - 1.1F)) {
                float f1 = ((TypeTankerDrogue) this.queen_).requestRefuel(this, this.fuelReceiveRate, f);
                this.FM.M.fuel += f1;
            } else if ((afueltank.length > 0) && (afueltank[0] != null) && !this.FM.M.bFuelTanksDropped) {
                float f2 = 0.0F;
                for (int k = 0; k < afueltank.length; k++) {
                    f2 += afueltank[k].checkFreeTankSpace();
                }

                if (f2 < (this.fuelReceiveRate + 1.1F)) {
                    this.typeDockableAttemptDetach();
                    return;
                }
                float f3 = ((TypeTankerDrogue) this.queen_).requestRefuel(this, this.fuelReceiveRate, f);
                for (int l = 0; l < afueltank.length; l++) {
                    afueltank[l].doRefuel(f3 * (afueltank[l].checkFreeTankSpace() / f2));
                }

            } else {
                this.typeDockableAttemptDetach();
                return;
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

    public void startCockpitSounds() {
        this.rwrUtils.setSoundEnable(true);
    }

    public void stopCockpitSounds() {
        this.rwrUtils.stopAllRWRSounds();
    }

    public void rareAction(float f, boolean flag) {
        if ((this.raretimer != Time.current()) && (this == World.getPlayerAircraft())) {
            this.counter++;
            if ((this.counter % 12) == 9) {
                this.InertialNavigation();
            }
        }
        this.raretimer = Time.current();
        super.rareAction(f, flag);
        if (this.bHasLaser && (this == World.getPlayerAircraft())) {
            if (this.FLIR) {
                this.FLIR();
            }
            if (!this.FLIR) {
                this.FM.AP.setStabAltitude(false);
            }
        }
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver) && (this.FM.AP.way.curr().Action == 3)) {
            Actor actor = null;
            NearestEnemies.set(this.WeaponsMask(), 25F, 2000F, 20F, 20000F, false, true, (String) null);
            actor = NearestEnemies.getAFoundFlyingPlane(this.pos.getAbsPoint(), 30000D, this.getArmy(), 1.0F);
            if ((actor == null) && (this.FM.AP.way.curr().getTarget() != null) && (Time.current() > (this.lastAIMissileSelect + 8000L))) {
                if (this.bHasAShM && (this.FM.AP.way.curr().getTarget() instanceof TgtShip) && (this.FM.CT.rocketNameSelected != "AGM-84A") && (this.FM.CT.rocketNameSelected != "AGM-84D") && (this.FM.CT.rocketNameSelected != "AGM-84J")) {
                    for (int i = 0; (i < 4) && (this.FM.CT.rocketNameSelected != "AGM-84A") && (this.FM.CT.rocketNameSelected != "AGM-84D") && (this.FM.CT.rocketNameSelected != "AGM-84J"); i++) {
                        this.FM.CT.toggleRocketHook();
                    }

                }
                if ((!this.bHasAShM || !(this.FM.AP.way.curr().getTarget() instanceof TgtShip)) && this.bHasAGM && (this.FM.CT.rocketNameSelected != "AGM-65E")) {
                    for (int j = 0; (j < 4) && (this.FM.CT.rocketNameSelected != "AGM-65E"); j++) {
                        this.FM.CT.toggleRocketHook();
                    }

                }
                if (!this.bHasAGM && this.bHasUGR && !this.bHasAShM && (this.FM.CT.rocketNameSelected != "Zuni") && (this.FM.CT.rocketNameSelected != "ZuniFAC") && (this.FM.CT.rocketNameSelected != "ZuniFO") && (this.FM.CT.rocketNameSelected != "Hydra") && (this.FM.CT.rocketNameSelected != "HydraFAC") && (this.FM.CT.rocketNameSelected != "HydraFO")) {
                    for (int k = 0; (k < 4) && (this.FM.CT.rocketNameSelected != "Zuni") && (this.FM.CT.rocketNameSelected != "ZuniFAC") && (this.FM.CT.rocketNameSelected != "ZuniFO") && (this.FM.CT.rocketNameSelected != "Hydra") && (this.FM.CT.rocketNameSelected != "HydraFAC") && (this.FM.CT.rocketNameSelected != "HydraFO"); k++) {
                        this.FM.CT.toggleRocketHook();
                    }

                }
                this.lastAIMissileSelect = Time.current();
            }
            if ((!((Maneuver) this.FM).hasBombs() && !this.bHasAGM && !this.bHasAShM && !this.bHasUGR) || (!this.bHasAGM && !this.bHasUGR && this.bHasAShM && ((this.FM.AP.way.curr().getTarget() == null) || !(this.FM.AP.way.curr().getTarget() instanceof TgtShip)))) {
                if (((this.lAIGAskipTimer > 0L) && (Time.current() > this.lAIGAskipTimer)) || !this.bHadLGweapons || !this.bHasLaser) {
                    this.FM.AP.way.next();
                    this.FM.bSkipGroundAttack = true;
                    ((Maneuver) this.FM).target_ground = null;
                    ((Maneuver) this.FM).set_maneuver(0);
                    this.FM.CT.toggleRocketHook();
                } else if (this.lAIGAskipTimer == -1L) {
                    this.lAIGAskipTimer = Time.current() + 20000L + ((long) Math.min(this.FM.getAltitude(), 10000F) * 5L);
                }
            }
            if ((this.bHasAGM || this.bHasAShM || this.bHasUGR) && (Time.current() > (this.lastAGMcheck + 30000L))) {
                this.checkAIAGMrest();
                this.lastAGMcheck = Time.current();
            }
        }
        if ((this.bHasPaveway || this.FM.bNoDiveBombing) && (Time.current() > (this.lastLGBcheck + 30000L)) && (this.FM instanceof Maneuver)) {
            this.checkGuidedBombRest();
            this.lastLGBcheck = Time.current();
        }
        if (!this.FM.isPlayers()) {
            if ((((Maneuver) this.FM).get_maneuver() == 25) || (((Maneuver) this.FM).get_maneuver() == 26) || (((Maneuver) this.FM).get_maneuver() == 64) || (((Maneuver) this.FM).get_maneuver() == 66) || (((Maneuver) this.FM).get_maneuver() == 102)) {
                this.radartoggle = false;
            } else {
                this.radartoggle = true;
                this.radarmode = 0;
            }
        }
    }

    private boolean InertialNavigation() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if ((aircraft.getSpeed(vector3d) > 20D) && (aircraft.pos.getAbsPoint().z >= 150D)) {
            this.pos.getAbs(point3d);
            if (Mission.cur() != null) {
                this.error++;
                if (this.error > 99) {
                    this.error = 1;
                }
            }
            int i = this.error;
            int j = i;
            Random random = new Random();
            int k = random.nextInt(100);
            if (k > 50) {
                i -= i * 2;
            }
            k = random.nextInt(100);
            if (k > 50) {
                j -= j * 2;
            }
            double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D / 10D;
            double d2 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D / 10D;
            char c = (char) (int) (65D + Math.floor(((d1 / 676D) - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char) (int) (65D + Math.floor(((d1 / 26D) - Math.floor(d1 / 26D)) * 26D));
            String s = "";
            if (d > 260D) {
                s = "" + c + c1;
            } else {
                s = "" + c1;
            }
            int l = (int) Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "INS: " + s + "-" + l);
        }
        return true;
    }

    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        this.guidedMissileUtils.update();
        if (this.FM instanceof Maneuver) {
            this.receivingRefuel(f);
        }
        this.rwrUtils.update();
        this.backfire = this.rwrUtils.getBackfire();
        this.bRadarWarning = this.rwrUtils.getRadarLockedWarning();
        this.bMissileWarning = this.rwrUtils.getMissileWarning();
        if (this.bHasLaser && (this == World.getPlayerAircraft())) {
            if (this.FLIR) {
                this.laser(this.getLaserSpot());
            }
            this.updatecontrollaser();
        }
        if (this.bHasLaser && (this != World.getPlayerAircraft()) && (this.FM instanceof Maneuver)) {
            this.AILaserControl();
        }
        super.update(f);
        if (this.backfire) {
            this.backFire();
        }
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if ((this.queen_last != null) && (this.queen_last == actor) && ((this.queen_time == 0L) || (Time.current() < (this.queen_time + 5000L)))) {
            aflag[0] = false;
        } else {
            aflag[0] = true;
        }
    }

    public void typeRadarGainPlus() {
    }

    public void typeRadarGainMinus() {
    }

    public void typeRadarRangePlus() {
        this.radarrange++;
        if (this.radarrange > 4) {
            this.radarrange = 4;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar range " + this.radarrange);
    }

    public void typeRadarRangeMinus() {
        this.radarrange--;
        if (this.radarrange < 1) {
            this.radarrange = 1;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar range " + this.radarrange);
    }

    public void typeRadarReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeRadarReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeRadarToggleMode() {
        this.radarmode++;
        if (this.radarmode > 2) {
            this.radarmode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar mode " + this.radarmode);
        return false;
    }

    public boolean                    radartoggle;
    public int                        radarmode;
    public int                        radarrange;
    public int                        radargunsight;
    public int                        lockmode;
    public float                      lockrange;
    public int                        targetnum;
    public float                      radarvrt;
    public float                      radarhol;
    public float                      azimult;
    public float                      tangate;
    public long                       tf;
    public int                        leftscreen;
    private int                       counter;
    private int                       error;
    private long                      raretimer;
    private boolean                   bSightAutomation;
    private boolean                   bSightBombDump;
    private float                     fSightCurDistance;
    private Actor                     queen_last;
    private long                      queen_time;
    private boolean                   bNeedSetup;
    private long                      dtime;
    private Actor                     target_;
    private Aircraft                  queen_;
    private int                       dockport_;
    private float                     fuelReceiveRate;
    private boolean                   hasChaff;
    private boolean                   hasFlare;
    private long                      lastChaffDeployed;
    private long                      lastFlareDeployed;
    private ArrayList                 counterFlareList;
    private ArrayList                 counterChaffList;
    private GuidedMissileUtils        guidedMissileUtils;
    private boolean                   bHasLAGM;
    private boolean                   bHasAGM;
    private boolean                   bHasAShM;
    private boolean                   bHasUGR;
    private long                      lastAGMcheck;
    private RadarWarningReceiverUtils rwrUtils;
    public float                      misslebrg;
    public float                      aircraftbrg;
    private boolean                   backfire;
    public boolean                    bRadarWarning;
    public boolean                    bMissileWarning;
    private static final int          RWR_GENERATION        = 1;
    private static final int          RWR_MAX_DETECT        = 16;
    private static final int          RWR_KEEP_SECONDS      = 7;
    private static final double       RWR_RECEIVE_ELEVATION = 45D;
    private static final boolean      RWR_DETECT_IRMIS      = false;
    private static final boolean      RWR_DETECT_ELEVATION  = false;
    private boolean                   bRWR_Show_Text_Warning;
    public boolean                    FLIR;
    private boolean                   bLaserOn;
    public long                       laserTimer;
    private Point3d                   laserSpotPos;
    private boolean                   bLGBengaged;
    private boolean                   bHasPaveway;
    private boolean                   bHadLGweapons;
    private boolean                   bAILaserOn;
    private long                      lAILaserTimer;
    private long                      lAIGAskipTimer;
    private static float              maxPavewayFOVfrom     = 45F;
    private static double             maxPavewayDistance    = 20000D;
    private long                      lastLGBcheck;
    private long                      lastAIMissileSelect;
    public boolean                    hold;
    public boolean                    holdFollow;
    public Actor                      actorFollowing;
    public boolean                    bHasLaser;
    private long                      t1;
    private Actor                     semiradartarget;
    private Actor                     groundradartarget;
    private int                       iDebugLogLevel;

    static {
        Class class1 = AV_8B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Harrier");
        Property.set(class1, "meshName", "3DO/Plane/AV-8B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1997F);
        Property.set(class1, "yearExpired", 2014F);
        Property.set(class1, "FlightModel", "FlightModels/AV-8B.fmd:AV8B");
        Property.set(class1, "cockpitClass", new Class[] { com.maddox.il2.objects.air.CockpitAV_8B.class, com.maddox.il2.objects.air.CockpitAV8FLIR.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7, 7, 8, 8 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalMis01", "_ExternalMis01", "_ExternalMis02", "_ExternalMis02", "_ExternalMis03", "_ExternalMis03", "_ExternalMis04", "_ExternalMis04", "_ExternalMis05", "_ExternalMis05", "_ExternalMis06", "_ExternalMis06", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02",
                "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Rock29", "_Rock30", "_Rock31", "_Rock32", "_Rock33", "_Rock34", "_Rock35", "_Rock36", "_FlareL", "_FlareR", "_ChaffL", "_ChaffR" });
    }
}
