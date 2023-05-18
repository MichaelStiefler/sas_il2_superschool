/*4.10.1 class*/
package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Scheme1;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ScoreCounter {
    public ArrayList        enemyItems                   = new ArrayList();
    public ArrayList        friendItems                  = new ArrayList();
    public ArrayList        targetOnItems                = new ArrayList();
    public ArrayList        targetOffItems               = new ArrayList();
    public int              bulletsFire;
    public int              bulletsHit;
    public int              bulletsHitAir;
    public int              rocketsFire;
    public int              rocketsHit;
    public int              bombFire;
    public int              bombHit;
    public static final int PLAYER_IS_AIR_SINGLE         = 0;
    public static final int PLAYER_IS_AIR_MULTI          = 1;
    public static final int PLAYER_IS_AIR_GUNNER         = 2;
    public boolean          bPlayerDead                  = false;
    public boolean          bPlayerParatrooper           = false;
    public boolean          bLandedFarAirdrome           = false;
    public boolean          bPlayerCaptured              = false;
    public int              nPlayerTakeoffs;
    public int              nPlayerLandings;
    public int              nPlayerDitches;
    public boolean          bLanded                      = false;
    public boolean          bPlayerStateUnknown          = false;
    public boolean          bPlayerDroppedExternalStores = false;
    public int              externalStoresValue          = 0;
    public long             timeStart                    = -1L;
    public int              player_is                    = 0;
    public float            todStart;
    public boolean          bCrossCountry                = false;

    static class Register {
        public int    type;
        public double enemy;
        public double friend;

        public Register(int i, double d, double d_0_) {
            this.type = i;
            this.enemy = d;
            this.friend = d_0_;
        }
    }

    public void playerDead() {
        this.bPlayerDead = true;
    }

    public void playerParatrooper() {
        this.bPlayerParatrooper = true;
    }

    public void playerDroppedExternalStores(int i) {
        this.bPlayerDroppedExternalStores = true;
        this.externalStoresValue = i;
    }

    public void playerTakeoff() {
        this.nPlayerTakeoffs++;
        this.bLandedFarAirdrome = false;
        this.bLanded = false;
    }

    public void playerLanding(boolean bool) {
        this.bLanded = true;

        // TODO: Altered by |ZUTI|: this method is called from RealFlightModel. Bool value indicates if
        // player landed on the airfield or not (Airport.distToNearestAirport(Loc) > 1500D). BUT, player
        // can also land on a STD home base... so, check for that here.
        // ---------------------------------
        if (bool) {
            Aircraft playerAc = World.getPlayerAircraft();
            if (playerAc != null) {
                Point3d point = playerAc.pos.getAbsPoint();
                if (ZutiSupportMethods_Net.isOnBornPlace(point.x, point.y) != null) bool = false;
            }
        }
        // ---------------------------------

        if (bool) {
            this.nPlayerDitches++;
            this.bLandedFarAirdrome = true;
        } else {
            this.nPlayerLandings++;
            this.bLandedFarAirdrome = false;
        }
    }

    public void playerCaptured() {
        this.bPlayerCaptured = true;
    }

    public void playerStartAir(Aircraft aircraft) {
        this.timeStart = Time.currentReal();
        this.todStart = World.getTimeofDay();
        if (aircraft instanceof Scheme1) this.player_is = 0;
        else this.player_is = 1;
    }

    public void playerStartGunner() {
        this.timeStart = Time.currentReal();
        this.todStart = World.getTimeofDay();
        this.player_is = 2;
    }

    public void playerDoCrossCountry() {
        this.bCrossCountry = true;
    }

    public void playerUpdateState() {
        this.bPlayerStateUnknown = false;
        if (!this.bPlayerDead && !this.bPlayerParatrooper) {
            FlightModel flightmodel = World.getPlayerFM();
            if (flightmodel != null) this.bLanded = flightmodel.isStationedOnGround();
            else this.bPlayerStateUnknown = true;
        }
    }

    public void enemyDestroyed(Actor actor) {
        Register register = this.getRegistered(actor);
        if (register != null) {
            this.enemyItems.add(new ScoreItem(register.type, register.enemy));
            switch (register.type) {
                case 0:
                    HUD.log("EnemyAircraftDestroyed");
                    break;
                case 1:
                    HUD.log("EnemyTankDestroyed");
                    break;
                case 2:
                    HUD.log("EnemyCarDestroyed");
                    break;
                case 3:
                    HUD.log("EnemyArtilleryDestroyed");
                    break;
                case 4:
                    HUD.log("EnemyAAADestroyed");
                    break;
                case 5:
                    HUD.log("EnemyBridgeDestroyed");
                    break;
                case 6:
                    HUD.log("EnemyWagonDestroyed");
                    break;
                case 7:
                    HUD.log("EnemyShipDestroyed");
                    break;
                case 8:
                    HUD.log("EnemyStaticAircraftDestroyed");
                    break;
                case 9:
                    HUD.log("EnemyRadioDestroyed");
                    break;
            }
        }
    }

    public void friendDestroyed(Actor actor) {
        Register register = this.getRegistered(actor);
        if (register != null) {
            this.friendItems.add(new ScoreItem(register.type, register.friend));
            switch (register.type) {
                case 0:
                    HUD.log("FriendAircraftDestroyed");
                    break;
                case 1:
                    HUD.log("FriendTankDestroyed");
                    break;
                case 2:
                    HUD.log("FriendCarDestroyed");
                    break;
                case 3:
                    HUD.log("FriendArtilleryDestroyed");
                    break;
                case 4:
                    HUD.log("FriendAAADestroyed");
                    break;
                case 5:
                    HUD.log("FriendBridgeDestroyed");
                    break;
                case 6:
                    HUD.log("FriendWagonDestroyed");
                    break;
                case 7:
                    HUD.log("FriendShipDestroyed");
                    break;
                case 8:
                    HUD.log("FriendStaticAircraftDestroyed");
                    break;
                case 9:
                    HUD.log("FriendRadioDestroyed");
                    break;
            }
        }
    }

    public int getRegisteredType(Actor actor) {
        if (actor == null) return -1;
        Register register = (Register) Property.value(actor.getClass(), "scoreDefine", null);
        if (register == null) return -1;
        return register.type;
    }

    private Register getRegistered(Actor actor) {
        if (actor == null) return null;
        Register register = (Register) Property.value(actor.getClass(), "scoreDefine", null);
        if (register == null && World.cur().isDebugFM()) System.out.println("Class '" + actor.getClass().getName() + "' NOT registered in score database");
        return register;
    }

    public void targetOn(Target target, boolean bool) {
        if (bool) {
            double d = 0.0;
            int i = 0;
            switch (target.importance()) {
                case 0:
                    i = 100;
                    d = 50.0;
                    HUD.log("PrimaryTargetComplete");
                    break;
                case 1:
                    i = 101;
                    d = 50.0;
                    HUD.log("SecondaryTargetComplete");
                    break;
                case 2:
                    i = 102;
                    d = 100.0;
                    HUD.log("SecretTargetComplete");
                    break;
            }
            if (Mission.isNet()) d = 0.0;
            this.targetOnItems.add(new ScoreItem(i, d));
        } else {
            double d = 0.0;
            int i = 0;
            switch (target.importance()) {
                case 0:
                    i = 100;
                    d = 0.0;
                    HUD.log("PrimaryTargetFailed");
                    break;
                case 1:
                    i = 101;
                    d = 0.0;
                    HUD.log("SecondaryTargetFailed");
                    break;
                case 2:
                    i = 102;
                    d = 0.0;
                    break;
            }
            if (Mission.isNet()) d = 0.0;
            this.targetOffItems.add(new ScoreItem(i, d));
        }
    }

    public void resetGame() {
        this.enemyItems.clear();
        this.friendItems.clear();
        this.targetOnItems.clear();
        this.targetOffItems.clear();
        this.bulletsFire = 0;
        this.bulletsHit = 0;
        this.bulletsHitAir = 0;
        this.rocketsFire = 0;
        this.rocketsHit = 0;
        this.bombFire = 0;
        this.bombHit = 0;
        this.bPlayerDead = false;
        this.bPlayerParatrooper = false;
        this.bLandedFarAirdrome = false;
        this.bPlayerCaptured = false;
        this.nPlayerTakeoffs = 0;
        this.nPlayerLandings = 0;
        this.nPlayerDitches = 0;
        this.timeStart = -1L;
        this.player_is = 0;
        this.bCrossCountry = false;
        this.bLanded = false;
        this.bPlayerDroppedExternalStores = false;
        this.externalStoresValue = 0;
    }

    public static void register(Class var_class, int i, double d, double d_1_) {
        Property.set(var_class, "scoreDefine", new Register(i, d, d_1_));
    }

    // TODO: Added by |ZUTI|
    // ----------------------------------------------------------------
    // ****** WildWillie *********************
    // This procedure is used to calculate a point percent
    // based on the ordenance that was used
    private double calculateScorePercent(double explosionPower) {
        try {
            double scorePercent = 0.0;
            if (explosionPower > 400F) scorePercent = 0.50;
            else if (explosionPower > 180F) scorePercent = 0.40;
            else if (explosionPower > 150F) scorePercent = 0.30;
            else if (explosionPower > 50F) scorePercent = 0.20;
            else scorePercent = 0.10;
            return scorePercent;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    // *******************************************
    // *********** WildWillie ********************
    // This procedure gives players points for damaging an object
    public void enemyDamaged(Actor actor, double explosionPower) {
        Register register1 = this.getRegistered(actor);
        if (register1 == null) return;
        double scorePercent = this.calculateScorePercent(explosionPower);
        double enemyValue = (float) register1.enemy * scorePercent;
        this.enemyItems.add(new ScoreItem(register1.type, enemyValue));
        switch (register1.type) {
            case 0: // '\0'
                com.maddox.il2.game.HUD.log("EnemyAircraftDamaged");
                break;

            case 1: // '\001'
                com.maddox.il2.game.HUD.log("EnemyTankDamaged");
                break;

            case 2: // '\002'
                com.maddox.il2.game.HUD.log("EnemyCarDamaged");
                break;

            case 3: // '\003'
                com.maddox.il2.game.HUD.log("EnemyArtilleryDamaged");
                break;

            case 4: // '\004'
                com.maddox.il2.game.HUD.log("EnemyAAADamaged");
                break;

            case 5: // '\005'
                com.maddox.il2.game.HUD.log("EnemyBridgeDamaged");
                break;

            case 6: // '\006'
                com.maddox.il2.game.HUD.log("EnemyWagonDamaged");
                break;

            case 7: // '\007'
                com.maddox.il2.game.HUD.log("EnemyShipDamaged");
                break;

            case 8: // '\b'
                com.maddox.il2.game.HUD.log("EnemyStaticAircraftDamaged");
                break;
        }
    }

    // *********** WildWillie ********************
    // This procedure gives players points for damaging an object
    public void friendDamaged(Actor actor, double explosionPower) {
        Register register1 = this.getRegistered(actor);
        if (register1 == null) return;
        double scorePercent = this.calculateScorePercent(explosionPower);
        double friendlyValue = (float) register1.enemy * scorePercent;
        this.friendItems.add(new ScoreItem(register1.type, friendlyValue));
        switch (register1.type) {
            case 0: // '\0'
                com.maddox.il2.game.HUD.log("FriendAircraftDamaged");
                break;

            case 1: // '\001'
                com.maddox.il2.game.HUD.log("FriendTankDamaged");
                break;

            case 2: // '\002'
                com.maddox.il2.game.HUD.log("FriendCarDamaged");
                break;

            case 3: // '\003'
                com.maddox.il2.game.HUD.log("FriendArtilleryDamaged");
                break;

            case 4: // '\004'
                com.maddox.il2.game.HUD.log("FriendAAADamaged");
                break;

            case 5: // '\005'
                com.maddox.il2.game.HUD.log("FriendBridgeDamaged");
                break;

            case 6: // '\006'
                com.maddox.il2.game.HUD.log("FriendWagonDamaged");
                break;

            case 7: // '\007'
                com.maddox.il2.game.HUD.log("FriendShipDamaged");
                break;

            case 8: // '\b'
                com.maddox.il2.game.HUD.log("FriendStaticAircraftDamaged");
                break;
        }
    }

    public void zutiAddEnemyDestroyed() {
        this.enemyItems.add(new ScoreItem(0, 100D));
        com.maddox.il2.game.HUD.log("EnemyAircraftDestroyed");
    }
    // ----------------------------------------------------------------
}