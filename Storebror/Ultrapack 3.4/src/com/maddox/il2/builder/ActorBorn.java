/*4.10.1 class*/
package com.maddox.il2.builder;

import java.util.ArrayList;

import com.maddox.il2.engine.ActorPosMove;

public class ActorBorn extends com.maddox.il2.engine.Actor implements com.maddox.il2.objects.ActorAlign {
    public int                 r;
    // TODO: Changed by |ZUTI|: changed it's name to zutiAircraft so that it reflects
    // what kind of objects it holds. Original name was airNames
    // ----------------------------------
    public java.util.ArrayList zutiAircraft;
    // ----------------------------------
    public boolean             bParachute;

    // TODO: |ZUTI| variables
    // ------------------------------------------------------
    public boolean               zutiToggleSpawnPlaceIndicatorsStatus   = false;
    public boolean               zutiToggleAirdromeInfrastructureStatus = false;
    public ArrayList             airdromeInfrastructurePoints           = new ArrayList();
    public boolean               zutiStaticPositionOnly                 = false;
    public int                   zutiSpawnHeight                        = 1000;
    public int                   zutiSpawnSpeed                         = 200;
    public int                   zutiSpawnOrient                        = 0;
    // Called from: PlMisBorn, Front
    public String                zutiBaseCapturedRedPlanes              = "";
    public String                zutiBaseCapturedBluePlanes             = "";
    public boolean               zutiCanThisHomeBaseBeCaptured          = false;
    // Called from: PlMisBorn
    public boolean               zutiWasPrerenderedBlue                 = false;
    public boolean               zutiWasPrerenderedRed                  = false;
    public boolean               zutiWasPrerenderedGlobal               = false;
    public boolean               zutiWasPrerenderedCaptured             = false;
    public int                   zutiRadarHeight_MIN                    = 0;// m
    public int                   zutiRadarHeight_MAX                    = 5000;// m
    public int                   zutiRadarRange                         = 50;// km
    public boolean               zutiWasPrerenderedMIN                  = false;
    public boolean               zutiWasPrerenderedMAX                  = false;
    public boolean               zutiWasPrerenderedRange                = false;
    public int                   zutiMaxBasePilots                      = 0;
    public boolean               zutiAirspawnOnly                       = false;
    public Zuti_WManageAircrafts zutiCapturedAc_Red                     = null;
    public Zuti_WManageAircrafts zutiCapturedAc_Blue                    = null;
    public ArrayList             zutiHomeBaseCountries                  = null;
    public ArrayList             zutiHomeBaseCapturedRedCountries       = null;
    public ArrayList             zutiHomeBaseCapturedBlueCountries      = null;
    public boolean               zutiEnablePlaneLimits                  = false;
    public boolean               zutiDecreasingNumberOfPlanes           = false;
    public boolean               zutiIncludeStaticPlanes                = false;
    public boolean               zutiDisableSpawning                    = false;
    public boolean               zutiEnableFriction                     = false;
    public double                zutiFriction                           = 3.8D;
    public boolean               zutiDisableRendering                   = false;

    // RRR options
    public boolean                   zutiOverrideDefaultRRRSettings       = false;
    public int                       zutiOneMgCannonRearmSecond           = 10;
    public int                       zutiOneBombFTankTorpedoeRearmSeconds = 25;
    public int                       zutiOneRocketRearmSeconds            = 20;
    public boolean                   zutiRearmOnlyIfAmmoBoxesExist        = false;

    public int                       zutiLoadoutChangePenaltySeconds      = 30;
    public boolean                   zutiOnlyHomeBaseSpecificLoadouts     = true;

    public int                       zutiGallonsLitersPerSecond           = 3;
    public boolean                   zutiRefuelOnlyIfFuelTanksExist       = false;

    public int                       zutiEngineRepairSeconds              = 90;
    public int                       zutiOneControlCableRepairSeconds     = 15;
    public int                       zutiFlapsRepairSeconds               = 30;
    public int                       zutiOneWeaponRepairSeconds           = 3;
    public int                       zutiCockpitRepairSeconds             = 30;
    public int                       zutiOneFuelOilTankRepairSeconds      = 20;
    public boolean                   zutiRepairOnlyIfWorkshopExist        = false;

    public boolean                   zutiEnableResourcesManagement        = false;
    public Zuti_WResourcesManagement resourcesManagement                  = null;
    public long                      zutiBulletsSupply                    = 5000;
    public int[]                     zutiBombsSupply                      = new int[] { 10, 10, 10, 10, 10, 10 };
    public long                      zutiRocketsSupply                    = 10;
    public long                      zutiFuelSupply                       = 10000;
    public long                      zutiEnginesSupply                    = 2;
    public long                      zutiRepairKitsSupply                 = 5;

    // capturing
    public int     zutiCapturingRequiredParatroopers   = 100;

    public int     zutiCarrierSpawnPoints              = 0;
    public int     zutiCarrierSelectedSpawnPointsIndex = 99;
    public boolean zutiEnableQueue                     = false;
    public int     zutiDeckClearTimeout                = 30;
    public boolean zutiAirspawnIfQueueFull             = false;
    public boolean zutiPilotInVulnerableWhileOnTheDeck = false;

    // Stand alone BP
    public boolean zutiIsStandAloneBornPlace = false;

    /**
     * is friction enabled or not for this airfield
     *
     * @return
     */
    public boolean zutiIsFrictionEnabled() {
        return this.zutiFriction == 3.8D ? false : true;
    }

    public boolean zutiCaptureOnlyIfNoChiefPresent = false;
    // ------------------------------------------------------

    public void align() {
        this.alignPosToLand(0.0D, true);
    }

    public java.lang.Object getSwitchListener(com.maddox.rts.Message message) {
        return this;
    }

    public ActorBorn(com.maddox.JGP.Point3d point3d) {
        this.r = 3000;
        this.zutiAircraft = new ArrayList();
        this.bParachute = true;
        this.flags |= 0x2000;
        this.pos = new ActorPosMove(this);
        this.pos.setAbs(point3d);
        this.align();
        this.drawing(true);
        this.icon = com.maddox.il2.engine.IconDraw.get("icons/born.mat");
    }

    protected void createActorHashCode() {
        this.makeActorRealHashCode();
    }
}