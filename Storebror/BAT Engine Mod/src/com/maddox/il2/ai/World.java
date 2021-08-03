package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.air.Airdrome;
import com.maddox.il2.ai.air.NearestAircraft;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Sun;
import com.maddox.il2.engine.hotkey.HookView;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Wind;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiAircraft;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.game.ZutiTimer_Refly;
import com.maddox.il2.gui.GUINetMission;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.ScoreRegister;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.NetGunner;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.Runaway;
import com.maddox.il2.objects.buildings.HouseManager;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.effects.MiscEffects;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.NetEnv;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;

public class World {
    static class ClipFilter implements ActorFilter {

        public boolean isUse(Actor actor, double d) {
            return actor instanceof BigshipGeneric;
        }

        ClipFilter() {
        }
    }

    static class Remover extends Actor {

        protected void createActorHashCode() {
            this.makeActorRealHashCode();
        }

        Remover() {
        }
    }

    public static RangeRandom Rnd() {
        return World.cur().rnd;
    }

    public static RangeRandom rnd() {
        return World.cur().rnd2;
    }

    public static RangeRandom rndPos(double d, double d1) {
        long l = (long) ((d / d1) + (d * d1));
        World.cur().pseudoRnd.setSeed(l);
        return World.cur().pseudoRnd;
    }

    public void setCamouflage(String s) {
        if ("SUMMER".equalsIgnoreCase(s)) {
            this.camouflage = 0;
        } else if ("WINTER".equalsIgnoreCase(s)) {
            this.camouflage = 1;
        } else if ("DESERT".equalsIgnoreCase(s)) {
            this.camouflage = 2;
        } else if ("PACIFIC".equalsIgnoreCase(s)) {
            this.camouflage = 3;
        } else if ("ETO".equalsIgnoreCase(s)) {
            this.camouflage = 4;
        } else if ("MTO".equalsIgnoreCase(s)) {
            this.camouflage = 5;
        } else if ("CBI".equalsIgnoreCase(s)) {
            this.camouflage = 6;
        } else {
            this.camouflage = 0;
        }
    }

    public void setUserCovers() {
        this.userCoverMashineGun = this.userCfg.coverMashineGun;
        this.userCoverCannon = this.userCfg.coverCannon;
        this.userCoverRocket = this.userCfg.coverRocket;
        this.userRocketDelay = this.userCfg.rocketDelay;
        this.userBombDelay = this.userCfg.bombDelay;
        this.userFuzeType = this.userCfg.fuzeType;
    }

    public boolean isArcade() {
        return Mission.isSingle() && this.bArcade && !NetMissionTrack.isPlaying();
    }

    public void setArcade(boolean flag) {
        this.bArcade = flag;
    }

    public boolean isHighGore() {
        return this.bHighGore;
    }

    public boolean isHakenAllowed() {
        return this.bHakenAllowed;
    }

    public boolean isDebugFM() {
        return this.bDebugFM;
    }

    public boolean isTimeOfDayConstant() {
        return this.bTimeOfDayConstant;
    }

    public void setTimeOfDayConstant(boolean flag) {
        this.bTimeOfDayConstant = flag;
    }

    public boolean isWeaponsConstant() {
        return this.bWeaponsConstant;
    }

    public void setWeaponsConstant(boolean flag) {
        this.bWeaponsConstant = flag;
    }

    public static void getAirports(List list) {
        if (World.cur().airports != null) {
            list.addAll(World.cur().airports);
        }
    }

    public static int getMissionArmy() {
        return World.cur().missionArmy;
    }

    public static void setMissionArmy(int i) {
        World.cur().missionArmy = i;
    }

    public static Aircraft getPlayerAircraft() {
        return World.cur().PlayerAircraft;
    }

    public static int getPlayerArmy() {
        return World.cur().PlayerArmy;
    }

    public static FlightModel getPlayerFM() {
        return World.cur().PlayerFM;
    }

    public static Regiment getPlayerRegiment() {
        return World.cur().PlayerRegiment;
    }

    public static String getPlayerLastCountry() {
        Regiment regiment = World.getPlayerRegiment();
        if (regiment != null) {
            World.cur().PlayerLastCountry = regiment.country();
        }
        return World.cur().PlayerLastCountry;
    }

    public static boolean isPlayerGunner() {
        return Actor.isValid(World.cur().PlayerGunner);
    }

    public static NetGunner getPlayerGunner() {
        return World.cur().PlayerGunner;
    }

    public static boolean isPlayerParatrooper() {
        return World.cur().bPlayerParatrooper;
    }

    public static boolean isPlayerDead() {
        return World.cur().bPlayerDead;
    }

    public static boolean isPlayerCaptured() {
        return World.cur().bPlayerCaptured;
    }

    public static boolean isPlayerRemoved() {
        return World.cur().bPlayerRemoved;
    }

    public static void setPlayerAircraft(Aircraft aircraft) {
        World.cur().PlayerAircraft = aircraft;
        if (aircraft != null) {
            World.cur().PlayerFM = aircraft.FM;
            World.cur().scoreCounter.playerStartAir(aircraft);
        } else {
            World.cur().PlayerFM = null;
        }
    }

    public static void setPlayerFM() {
        if (Actor.isValid(World.cur().PlayerAircraft)) {
            World.cur().PlayerFM = World.cur().PlayerAircraft.FM;
        }
    }

    public static void setPlayerRegiment() {
        if (Actor.isValid(World.cur().PlayerAircraft)) {
            Aircraft aircraft = World.cur().PlayerAircraft;
            if (aircraft.getOwner() != null) {
                World.cur().PlayerRegiment = ((Wing) aircraft.getOwner()).regiment();
            } else {
                World.cur().PlayerRegiment = null;
            }
            World.cur().PlayerArmy = aircraft.getArmy();
            if (Mission.isSingle()) {
                World.cur().missionArmy = World.cur().PlayerArmy;
            }
        }
    }

    public static void doPlayerParatrooper(Paratrooper paratrooper) {
        FlightModel flightmodel = World.getPlayerFM();
        if (!World.isPlayerParatrooper()) {
            if (Config.isUSE_RENDER()) {
                RTSConf.cur.hotKeyEnvs.endAllActiveCmd(false);
            }
            World.cur().bPlayerParatrooper = true;
            if (!ZutiAircraft.isPlaneLandedAndDamaged(flightmodel)) {
                World.cur().scoreCounter.playerParatrooper();
            }
            if (Config.isUSE_RENDER()) {
                if (Main3D.cur3D().viewActor() == World.getPlayerAircraft()) {
                    Main3D.cur3D().setViewFlow10(paratrooper, false);
                }
                Main3D.cur3D().ordersTree.unactivate();
                ForceFeedback.stopMission();
            }
        }
        if (Main.cur().mission.zutiMisc_EnableReflyOnlyIfBailedOrDied && Mission.isDogfight()) {
            ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED = false;
            GUINetMission.setPlayerParatrooper(paratrooper);
        }
    }

    public static void doGunnerParatrooper(Paratrooper paratrooper) {
        if (World.isPlayerParatrooper()) {
            return;
        }
        if (Config.isUSE_RENDER()) {
            RTSConf.cur.hotKeyEnvs.endAllActiveCmd(false);
        }
        World.cur().bPlayerParatrooper = true;
        World.cur().scoreCounter.playerParatrooper();
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().viewActor() == World.getPlayerAircraft()) {
                Main3D.cur3D().setViewFlow10(paratrooper, false);
            }
            ForceFeedback.stopMission();
            if (Main.cur().mission.zutiMisc_EnableReflyOnlyIfBailedOrDied && Mission.isDogfight()) {
                ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED = false;
                GUINetMission.setPlayerParatrooper(paratrooper);
            }
        }
    }

    public static void doPlayerUnderWater() {
        if (Config.isUSE_RENDER() && (Main3D.cur3D().viewActor() == World.getPlayerAircraft()) && !Main3D.cur3D().isViewOutside()) {
            Main3D.cur3D().setViewFlow10(World.getPlayerAircraft(), false);
        }
    }

    public static void setPlayerDead() {
        if (Config.isUSE_RENDER()) {
            RTSConf.cur.hotKeyEnvs.endAllActiveCmd(false);
        }
        World.cur().bPlayerDead = true;
        World.cur().scoreCounter.playerDead();
        if (Main.cur().mission.zutiMisc_EnableReflyOnlyIfBailedOrDied && Mission.isDogfight()) {
            if (((FlightModelMain) (World.getPlayerFM())).Gears.nOfGearsOnGr < 3) {
                ZutiSupportMethods.ZUTI_KIA_COUNTER++;
            }
            ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED = false;
            float f = Main.cur().mission.zutiMisc_ReflyKIADelay + (ZutiSupportMethods.ZUTI_KIA_COUNTER * Main.cur().mission.zutiMisc_ReflyKIADelayMultiplier);
            ZutiSupportMethods.setPlayerBanDuration((long) f);
            GUINetMission.setReflyTimer(new ZutiTimer_Refly(f));
            System.out.println(((NetUser) NetEnv.host()).uniqueName() + " has died for " + ZutiSupportMethods.ZUTI_KIA_COUNTER + " times. Refly penalty is " + f + "s.");
            EventLog.type(true, ((NetUser) NetEnv.host()).uniqueName() + " has died for " + ZutiSupportMethods.ZUTI_KIA_COUNTER + " times. Refly penalty is " + f + "s.");
        }
    }

    public static void setPlayerCaptured() {
        World.cur().bPlayerCaptured = true;
        World.cur().scoreCounter.playerCaptured();
    }

    public static void setPlayerGunner(NetGunner netgunner) {
        World.cur().PlayerGunner = netgunner;
        World.cur().scoreCounter.playerStartGunner();
    }

    public static void onActorDied(Actor actor, Actor actor1) {
        World.onActorDied(actor, actor1, null, true);
    }

    public static void onActorDied(Actor actor, Actor actor1, Actor actor2) {
        World.onActorDied(actor, actor1, actor2, true);
    }

    public static void onActorDied(Actor actor, Actor actor1, Actor actor2, boolean flag) {
        if (actor.getDiedFlag()) {
            throw new ActorException("actor " + actor.getClass() + ":" + actor.name() + " alredy dead");
        }
        if (Mission.isDogfight() && (Main.cur().netServerParams != null) && Main.cur().netServerParams.isMaster()) {
            if (actor instanceof PlaneGeneric) {
                World.cur().zutiManagePilotsBornPlacePlaneCounter((PlaneGeneric) actor);
            }
            if (actor instanceof NetAircraft) {
                try {
                    boolean flag1 = false;
                    boolean flag2 = false;
                    if (actor1 != null) {
                        flag1 = actor.name().equals(actor1.name());
                        flag2 = actor1.name().equals("NONAME");
                    }
                    boolean flag4 = ZutiSupportMethods.isPlaneStationary(((SndAircraft) ((NetAircraft) actor)).FM);
                    if ((actor1 != null) && !flag2 && !flag1 && !flag4) {
                        ZutiSupportMethods.managePilotBornPlacePlaneCounter((NetAircraft) actor, false);
                    } else {
                        ZutiSupportMethods.managePilotBornPlacePlaneCounter((NetAircraft) actor, true);
                    }
                } catch (Exception exception) {
                    System.out.println("onActorDied Exception: " + exception);
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
        if (!Mission.isDogfight()) {
            Voice.testTargDestr(actor, actor1 == World.remover ? null : actor1);
        }
        World.trySendChatMsgDied(actor, actor1 == World.remover ? actor : actor1);
        if (World.cur().diffCur.SharedKills) {
            if (actor2 != null) {
                if ((actor2 == actor1) || (actor2.getArmy() != actor1.getArmy())) {
                    actor2 = null;
                } else if ((actor2 instanceof Aircraft) && (actor1 instanceof Aircraft)) {
                    Aircraft aircraft = (Aircraft) actor2;
                    Aircraft aircraft1 = (Aircraft) actor1;
                    if (World.cur().diffCur.SharedKillsHistorical && (!World.isSharedKillUserCountry(aircraft.getRegiment().country) || !World.isSharedKillUserCountry(aircraft1.getRegiment().country))) {
                        actor2 = null;
                    }
                }
            }
        } else {
            actor2 = null;
        }
        actor.setDiedFlag(true);
        if ((actor1 == World.remover) && (actor == World.cur().PlayerAircraft)) {
            World.cur().bPlayerRemoved = true;
        }
        if (flag) {
            EventLog.onActorDied(actor, actor1, actor2);
        }
        Engine.cur.world.war.onActorDied(actor, actor1 == World.remover ? null : actor1);
        Engine.cur.world.targetsGuard.checkActorDied(actor);
        Object obj = World.cur().PlayerAircraft;
        if (World.isPlayerGunner()) {
            obj = World.cur().PlayerGunner;
        }
        if ((actor.getArmy() != 0) && (actor != obj) && (obj != null) && ((actor1 == obj) || (actor2 == obj)) && Actor.isAlive(actor1)) {
            boolean flag3 = false;
            String s = null;
            if (((actor1 == obj) && (actor2 != null)) || (actor2 == obj)) {
                flag3 = true;
            }
            if (((actor1 == obj) || (actor2 == obj)) && (actor instanceof Aircraft) && (actor.getArmy() != ((Actor) obj).getArmy())) {
                Aircraft aircraft2 = (Aircraft) actor;
                Regiment regiment = aircraft2.getRegiment();
                if (regiment != null) {
                    s = regiment.branch();
                }
                if (obj instanceof Aircraft) {
                    Aircraft aircraft3 = (Aircraft) obj;
                    Regiment regiment1 = aircraft3.getRegiment();
                    if (regiment1 != null) {
                        Scores.playerArmy = regiment1.country();
                    }
                } else if (obj instanceof NetGunner) {
                    NetGunner netgunner = (NetGunner) obj;
                    Regiment regiment2 = netgunner.getAircraft().getRegiment();
                    if (regiment2 != null) {
                        Scores.playerArmy = regiment2.country();
                    }
                }
            }
            if (actor.getArmy() != ((Actor) obj).getArmy()) {
                World.cur().scoreCounter.enemyDestroyed(actor, flag3, s);
            } else {
                World.cur().scoreCounter.friendDestroyed(actor);
            }
        }
        if (actor == World.cur().PlayerAircraft) {
            World.cur().checkViewOnPlayerDied(actor);
            if (Config.isUSE_RENDER()) {
                CmdEnv.top().exec("music RAND music/crash");
                ForceFeedback.stopMission();
            }
            if (actor1 != World.cur().PlayerAircraft) {
                World.cur();
                if (!World.isPlayerParatrooper()) {
                    World.cur().scoreCounter.playerDead();
                }
            }
        }
    }

    public void checkViewOnPlayerDied(Actor actor) {
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Point3d point3d2 = new Point3d();
        point3d.set(actor.pos.getAbsPoint());
        point3d1.set(actor.pos.getAbsPoint());
        point3d.z -= 40D;
        point3d1.z += 40D;
        Actor actor1 = Engine.collideEnv().getLine(point3d1, point3d, false, World.clipFilter, point3d2);
        if (Actor.isValid(actor1)) {
            if (Config.isUSE_RENDER() && (Main3D.cur3D().viewActor() == actor)) {
                Main3D.cur3D().setViewFlow10(actor1, false);
            }
            return;
        }
        ActorViewPoint actorviewpoint = new ActorViewPoint();
        actorviewpoint.pos.setAbs(actor.pos.getAbs());
        actorviewpoint.pos.reset();
        actorviewpoint.dreamFire(true);
        if (Config.isUSE_RENDER() && (Main3D.cur3D().viewActor() == actor)) {
            Main3D.cur3D().hookView.set(actorviewpoint, 3F * HookView.defaultLen(), 10F, -10F);
            Main3D.cur3D().setView(actorviewpoint, true);
        }
    }

    public static void onTaskComplete(Actor actor) {
        if (actor.isTaskComplete()) {
            return;
        }
        actor.setTaskCompleteFlag(true);
        Engine.cur.world.targetsGuard.checkTaskComplete(actor);
        if (actor.isNetMaster()) {
            ((NetUser) NetEnv.host()).postTaskComplete(actor);
        }
    }

    private static void trySendChatMsgDied(Actor actor, Actor actor1) {
        if (!Actor.isValid(actor1) || Mission.isSingle() || (Main.cur().chat == null) || !(actor instanceof Aircraft)) {
            return;
        }
        if (actor.net == null) {
            return;
        }
        if (!actor.net.isMaster()) {
            return;
        }
        Aircraft aircraft = (Aircraft) actor;
        NetUser netuser = aircraft.netUser();
        if (netuser == null) {
            return;
        }
        if (actor == actor1) {
            return;
        }
        int i = Engine.cur.world.scoreCounter.getRegisteredType(actor1);
        if (!aircraft.FM.isSentBuryNote()) {
            aircraft.FM.setSentBuryNote(true);
            switch (i) {
                case 0:
                    if ((actor1 instanceof Aircraft) && (((Aircraft) actor1).netUser() != null)) {
                        Chat.sendLog(1, "gore_kill" + World.Rnd().nextInt(1, 5), (Aircraft) actor1, aircraft);
                        if (!aircraft.FM.isWasAirborne() && aircraft.isDamagerExclusive()) {
                            Chat.sendLogRnd(2, "gore_vulcher", (Aircraft) actor1, null);
                        }
                    } else {
                        Chat.sendLogRnd(1, "gore_ai", aircraft, null);
                    }
                    return;

                case 1:
                    Chat.sendLogRnd(2, "gore_tank", aircraft, null);
                    return;

                case 2:
                    Chat.sendLogRnd(2, "gore_gun", aircraft, null);
                    return;

                case 3:
                    Chat.sendLogRnd(2, "gore_gun", aircraft, null);
                    return;

                case 4:
                    Chat.sendLogRnd(2, "gore_killaaa", aircraft, null);
                    return;

                case 7:
                    Chat.sendLogRnd(2, "gore_ship", aircraft, null);
                    return;

                case 6:
                    Chat.sendLogRnd(2, "gore_killaaa", aircraft, null);
                    return;

                case 5:
                case 8:
                case 9:
                default:
                    Chat.sendLogRnd(1, "gore_crashes", aircraft, null);
                    break;
            }
        }
    }

    public static Landscape land() {
        return Engine.land();
    }

    public static Wind wind() {
        return World.cur().wind;
    }

    public static World cur() {
        return Engine.cur.world;
    }

    public void resetGameClear() {
        EventLog.resetGameClear();
        this.front.resetGameClear();
        this.war.resetGameClear();
        this.bTimeOfDayConstant = false;
        if (this.statics != null) {
            this.statics.resetGame();
        }
        if (this.getAirports() != null) {
            for (int i = 0; i < this.getAirports().size(); i++) {
                ((Airport) this.getAirports().get(i)).destroy();
            }

            this.getAirports().clear();
            this.setAirports(null);
        }
        do {
            if (this.runawayList == null) {
                break;
            }
            Runaway runaway = this.runawayList;
            this.runawayList = runaway.next();
            if (Actor.isValid(runaway)) {
                runaway.destroy();
            }
        } while (true);
        this.targetsGuard.resetGame();
        this.triggersGuard.resetGame();
        this.scoreCounter.resetGame();
        NearestEnemies.resetGameClear();
        NearestAircraft.resetGameClear();
        Aircraft.resetGameClear();
        MsgExplosion.resetGame();
        MsgShot.resetGame();
        Regiment.resetGame();
        PlaneGeneric.reset();
        if (this.bornPlaces != null) {
            for (int j = 0; j < this.bornPlaces.size(); j++) {
                ((BornPlace) this.bornPlaces.get(j)).clear();
            }

            this.bornPlaces.clear();
        }
        this.bornPlaces = null;
        this.bPlayerParatrooper = false;
        this.bPlayerDead = false;
        this.bPlayerCaptured = false;
        this.bPlayerRemoved = false;
        if (Actor.isValid(this.houseManager)) {
            this.houseManager.destroy();
        }
        this.houseManager = null;
        MiscEffects.clear();
    }

    public void resetGameCreate() {
        if (this.statics == null) {
            this.statics = new Statics();
        }
        this.ChiefMan = new ChiefManager();
        World.setPlayerAircraft(null);
        this.PlayerArmy = 1;
        this.rnd = new RangeRandom();
        this.rnd2 = new RangeRandom();
        this.voicebase = new Voice();
        World.setTimeofDay(12F);
        this.setAirports(new ArrayList());
        this.war.resetGameCreate();
        this.front.resetGameCreate();
        EventLog.resetGameCreate();
    }

    public void resetUser() {
        this.bPlayerParatrooper = false;
        this.bPlayerDead = false;
        ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED = false;
    }

    public World() {
        this.blockMorseChat = false;
        this.smallMapWPLabels = true;
        this.showMorseAsText = false;
        this.useSmartAxis = false;
        this.noParaTrooperViews = false;
        this.noMissionInfoHud = false;
        this.noKillInfoHud = false;
        this.debugSounds = false;
        this.rnd = new RangeRandom();
        this.rnd2 = new RangeRandom();
        this.pseudoRnd = new RangeRandom();
        this.camouflage = 0;
        this.diffCur = new DifficultySettings();
        this.diffUser = new DifficultySettings();
        this.userCoverMashineGun = 500F;
        this.userCoverCannon = 500F;
        this.userCoverRocket = 500F;
        this.userRocketDelay = 10F;
        this.userBombDelay = 0.0F;
        this.userFuzeType = 0;
        this.bArcade = false;
        this.bHighGore = false;
        this.bHakenAllowed = false;
        this.bDebugFM = false;
        this.bTimeOfDayConstant = false;
        this.bWeaponsConstant = false;
        this.missionArmy = 1;
        this.PlayerArmy = 1;
        this.bPlayerParatrooper = false;
        this.bPlayerDead = false;
        this.bPlayerCaptured = false;
        this.bPlayerRemoved = false;
        this.targetsGuard = new TargetsGuard();
        this.triggersGuard = new TriggersGuard();
        this.scoreCounter = new ScoreCounter();
        this.wind = new Wind();
        this.front = new Front();
        this.startTimeofDay = 43200;
        this.Atm = new Atmosphere();
        this.ChiefMan = new ChiefManager();
        this.sun = new Sun();
        this.voicebase = new Voice();
        this.zutiCurrentBornPlace = null;
        this.war = new War();
        this.bArcade = Config.cur.ini.get("game", "Arcade", this.bArcade);
        if (Config.LOCALE.equals("RU")) {
            this.bHighGore = Config.cur.ini.get("game", "HighGore", this.bHighGore);
            this.bHakenAllowed = Config.cur.ini.get("game", "HakenAllowed", this.bHakenAllowed);
        }
        this.blockMorseChat = Config.cur.ini.get("game", "BlockMorseChat", this.blockMorseChat);
        this.smallMapWPLabels = Config.cur.ini.get("game", "SmallMapWPLabels", this.smallMapWPLabels);
        this.showMorseAsText = Config.cur.ini.get("game", "ShowMorseAsText", this.showMorseAsText);
        this.useSmartAxis = Config.cur.ini.get("rts", "UseSmartAxis", this.useSmartAxis);
        this.noParaTrooperViews = Config.cur.ini.get("game", "SkipParatrooperViews", this.noParaTrooperViews);
        this.noMissionInfoHud = Config.cur.ini.get("game", "NoMissionInfoHud", this.noMissionInfoHud);
        this.noKillInfoHud = Config.cur.ini.get("game", "noKillInfoHud", this.noKillInfoHud);
        this.debugSounds = Config.cur.ini.get("game", "DebugSounds", this.debugSounds);
    }

    public void save() {
        Config.cur.ini.setValue("rts", "UseSmartAxis", this.useSmartAxis ? "1" : "0");
        Config.cur.ini.setValue("game", "SkipParatrooperViews", this.noParaTrooperViews ? "1" : "0");
        Config.cur.ini.setValue("game", "NoMissionInfoHud", this.noMissionInfoHud ? "1" : "0");
        Config.cur.ini.setValue("game", "noKillInfoHud", this.noKillInfoHud ? "1" : "0");
        Config.cur.ini.setValue("game", "BlockMorseChat", this.blockMorseChat ? "1" : "0");
        Config.cur.ini.setValue("game", "SmallMapWPLabels", this.smallMapWPLabels ? "1" : "0");
        Config.cur.ini.setValue("game", "ShowMorseAsText", this.showMorseAsText ? "1" : "0");
        Config.cur.ini.saveFile();
    }

    public static float getTimeofDay() {
        if (World.cur().bTimeOfDayConstant) {
            return (World.cur().startTimeofDay / 3600F) % 24F;
        } else {
            return (((Time.current() / 1000L) + World.cur().startTimeofDay) / 3600F) % 24F;
        }
    }

    public static void setTimeofDay(float f) {
        int i = (int) ((f * 3600F) % 86400F);
        if (World.cur().bTimeOfDayConstant) {
            World.cur().startTimeofDay = i;
        } else {
            World.cur().startTimeofDay = i - (int) (Time.current() / 1000L);
        }
    }

    public static float g() {
        return Atmosphere.g();
    }

    public static Sun Sun() {
        return World.cur().sun;
    }

    public Sun sun() {
        return this.sun;
    }

    private void zutiManagePilotsBornPlacePlaneCounter(PlaneGeneric planegeneric) {
        String s = ZutiAircraft.getStaticAcNameFromActor(planegeneric.toString());
        Point3d point3d = planegeneric.pos.getAbsPoint();
        double d = point3d.x;
        double d1 = point3d.y;
        try {
            boolean flag = false;
            if (this.zutiCurrentBornPlace != null) {
                double d2 = Math.sqrt(Math.pow(this.zutiCurrentBornPlace.place.x - d, 2D) + Math.pow(this.zutiCurrentBornPlace.place.y - d1, 2D));
                if (d2 <= this.zutiCurrentBornPlace.r) {
                    this.zutiCurrentBornPlace.zutiReleaseAircraft(null, s, false, true, false);
                    flag = true;
                }
            }
            if (!flag && (this.bornPlaces != null)) {
                for (int i = 0; i < this.bornPlaces.size(); i++) {
                    BornPlace bornplace = (BornPlace) this.bornPlaces.get(i);
                    if (bornplace.zutiIncludeStaticPlanes) {
                        double d3 = Math.pow(bornplace.place.x - d, 2D) + Math.pow(bornplace.place.y - d1, 2D);
                        if (d3 <= bornplace.r * bornplace.r) {
                            bornplace.zutiReleaseAircraft(null, s, false, true, false);
                            this.zutiCurrentBornPlace = bornplace;
                        }
                    }
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static boolean isSharedKillUserCountry(String s) {
        for (int i = 0; i < World.sharedKillUsers.length; i++) {
            if (s.equals(World.sharedKillUsers[i])) {
                return true;
            }
        }

        return false;
    }

    public void setAirports(ArrayList arraylist) {
        this.airports = arraylist;
    }

    public ArrayList getAirports() {
        return this.airports;
    }

    public static final float   NORD                    = 270F;
    public static final float   PIXEL                   = 200F;
    public static float         MaxVisualDistance       = 5000F;
    public static float         MaxStaticVisualDistance = 4000F;
    public static float         MaxLongVisualDistance   = 10000F;
    public static float         MaxPlateVisualDistance  = 16000F;
    public boolean              blockMorseChat;
    public boolean              smallMapWPLabels;
    public boolean              showMorseAsText;
    public boolean              useSmartAxis;
    public boolean              noParaTrooperViews;
    public boolean              noMissionInfoHud;
    public boolean              noKillInfoHud;
    public boolean              debugSounds;
    public RangeRandom          rnd;
    public RangeRandom          rnd2;
    public RangeRandom          pseudoRnd;
    public int                  camouflage;
    public static final int     CAMOUFLAGE_SUMMER       = 0;
    public static final int     CAMOUFLAGE_WINTER       = 1;
    public static final int     CAMOUFLAGE_DESERT       = 2;
    public static final int     CAMOUFLAGE_PACIFIC      = 3;
    public static final int     CAMOUFLAGE_ETO          = 4;
    public static final int     CAMOUFLAGE_MTO          = 5;
    public static final int     CAMOUFLAGE_CBI          = 6;
    public DifficultySettings   diffCur;
    public DifficultySettings   diffUser;
    public UserCfg              userCfg;
    public float                userCoverMashineGun;
    public float                userCoverCannon;
    public float                userCoverRocket;
    public float                userRocketDelay;
    public float                userBombDelay;
    public int                  userFuzeType;
    private boolean             bArcade;
    private boolean             bHighGore;
    private boolean             bHakenAllowed;
    private boolean             bDebugFM;
    private boolean             bTimeOfDayConstant;
    private boolean             bWeaponsConstant;
    protected War               war;
    private ArrayList           airports;
    public ArrayList            bornPlaces;
    public HouseManager         houseManager;
    public Runaway              runawayList;
    public Airdrome             airdrome;
    private int                 missionArmy;
    private Aircraft            PlayerAircraft;
    private NetGunner           PlayerGunner;
    private int                 PlayerArmy;
    private FlightModel         PlayerFM;
    private Regiment            PlayerRegiment;
    private String              PlayerLastCountry;
    private boolean             bPlayerParatrooper;
    private boolean             bPlayerDead;
    private boolean             bPlayerCaptured;
    private boolean             bPlayerRemoved;
    public static Actor         remover                 = new Remover();
    static ClipFilter           clipFilter              = new ClipFilter();
    public TargetsGuard         targetsGuard;
    public TriggersGuard        triggersGuard;
    public ScoreCounter         scoreCounter;
    private Wind                wind;
    protected Front             front;
    public Statics              statics;
    private int                 startTimeofDay;
    public Atmosphere           Atm;
    public float                fogColor[]              = { 0.53F, 0.64F, 0.8F, 1.0F };
    public float                beachColor[]            = { 0.6F, 0.6F, 0.6F };
    public float                lodColor[]              = { 0.7F, 0.7F, 0.7F };
    public ChiefManager         ChiefMan;
    private Sun                 sun;
    public Voice                voicebase;
    private BornPlace           zutiCurrentBornPlace;
    private static final String sharedKillUsers[];

    static {
        ScoreRegister.load();
        sharedKillUsers = (new String[] { PaintScheme.countryFinland, PaintScheme.countryBritain, PaintScheme.countryHungary, PaintScheme.countryPoland, PaintScheme.countryRomania, PaintScheme.countryRussia, PaintScheme.countryNewZealand, PaintScheme.countryUSA });
    }
}
