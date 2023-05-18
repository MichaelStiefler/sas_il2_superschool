package com.maddox.il2.ai;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Time;

public class War {

    public static War cur() {
        return World.cur().war;
    }

    public boolean isActive() {
        if (!Mission.isPlaying() || NetMissionTrack.isPlaying()) {
            return false;
        }
        if (Mission.isSingle()) {
            return true;
        }
        return Mission.isServer() && (Mission.isCoop() || Mission.isDogfight());
    }

    public void onActorDied(Actor actor, Actor actor1) {
        if (!this.isActive()) {
            return;
        }
        if ((actor instanceof Aircraft) && (((Aircraft) actor).FM instanceof Maneuver)) {
            Maneuver maneuver = (Maneuver) ((Aircraft) actor).FM;
            if (maneuver.Group != null) {
                maneuver.Group.delAircraft((Aircraft) actor);
                maneuver.Group = null;
            }
        }
    }

    public void missionLoaded() {
    }

    public void resetGameCreate() {
        War.curArmy = 0;
        War.curGroup = 0;
    }

    public void resetGameClear() {
        for (int i = 0; i < 2; i++) {
            while (War.Groups[i] != null) {
                War.Groups[i].G.release();
                AirGroupList.delAirGroup(War.Groups, i, War.Groups[i].G);
            }
        }

    }

    public War() {
    }

    public void interpolateTick() {
        if (!this.isActive()) {
            return;
        }
        try {
            if ((Time.tickCounter() % 128) == 0) {
                World.cur().triggersGuard.checkTask();
            }
            if (Time.current() > 1000L) {
                this.checkGroupsContact();
            }
            this.checkCollisionForAircraft();
            if ((Time.tickCounter() % 4) == 0) {
                if ((Time.tickCounter() % 64) == 0) {
                    this.delEmptyGroups();
                }
                this.upgradeGroups();
            }
            this.formationUpdate();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void upgradeGroups() {
        int i = AirGroupList.length(War.Groups[War.curArmy]);
        if (i > War.curGroup) {
            AirGroupList.getGroup(War.Groups[War.curArmy], War.curGroup).update();
        } else {
            War.curArmy++;
            if (War.curArmy > 1) {
                War.curArmy = 0;
            }
            War.curGroup = 0;
            return;
        }
        War.curGroup++;
    }

    private void formationUpdate() {
        for (int i = 0; i < 2; i++) {
            if (War.Groups[i] != null) {
                int j = AirGroupList.length(War.Groups[i]);
                for (int k = 0; k < j; k++) {
                    AirGroupList.getGroup(War.Groups[i], k).formationUpdate();
                }

            }
        }

    }

    public static boolean informOtherGroupsNearBy(AirGroup airgroup, AirGroupList airgrouplist, AirGroup airgroup1) {
        boolean flag = false;
        int i = AirGroupList.length(airgrouplist);
        for (int j = 0; j < i; j++) {
            AirGroup airgroup2 = AirGroupList.getGroup(airgrouplist, j);
            if (airgroup != airgroup2) {
                War.tmpV.sub(airgroup2.Pos, airgroup.Pos);
                double d = War.tmpV.length();
                if ((airgroup2.clientGroup != null) && (airgroup == airgroup2.clientGroup)) {
                    AirGroupList.addAirGroup(airgroup2.enemies, 0, airgroup1);
                    airgroup2.setEnemyFighters();
                    airgroup2.targetGroup = airgroup1;
                    airgroup2.bInitAttack = false;
                    flag = true;
                } else if (d < 12000D) {
                    AirGroupList.addAirGroup(airgroup2.enemies, 0, airgroup1);
                    airgroup2.setEnemyFighters();
                    flag = true;
                }
            }
        }

        return flag;
    }

    private void checkGroupsContact() {
        int i = AirGroupList.length(War.Groups[0]);
        int j = AirGroupList.length(War.Groups[1]);
        for (int k = 0; k < i; k++) {
            AirGroup airgroup = AirGroupList.getGroup(War.Groups[0], k);
            if ((airgroup != null) && (airgroup.Pos != null)) {
                for (int l = 0; l < j; l++) {
                    AirGroup airgroup1 = AirGroupList.getGroup(War.Groups[1], l);
                    if ((airgroup1 != null) && (airgroup1.Pos != null)) {
                        War.tmpV.sub(airgroup.Pos, airgroup1.Pos);
                        if (War.tmpV.lengthSquared() < 400000000D) {
                            if (!AirGroupList.groupInList(airgroup.enemies[0], airgroup1)) {
                                for (int i1 = 0; i1 < airgroup.nOfAirc; i1++) {
                                    Aircraft aircraft = airgroup.airc[i1];
                                    if (!((SndAircraft) (aircraft)).FM.isTick(32, 0)) {
                                        continue;
                                    }
                                    int k1 = World.Rnd().nextInt(0, airgroup1.nOfAirc - 1);
                                    VisCheck.checkGroup(aircraft, airgroup1.airc[k1], airgroup, airgroup1);
                                    if (!airgroup.bSee) {
                                        continue;
                                    }
                                    War.informOtherGroupsNearBy(airgroup, War.Groups[0], airgroup1);
                                    AirGroupList.addAirGroup(airgroup.enemies, 0, airgroup1);
                                    if ((airgroup.airc[0] != null) && (airgroup1.airc[k1] != null)) {
                                        Voice.speakEnemyDetected(airgroup.airc[0], airgroup1.airc[k1]);
                                    }
                                    airgroup.setEnemyFighters();
                                    airgroup.bInitAttack = true;
                                    airgroup.bSee = false;
                                    break;
                                }

                            }
                            if (!AirGroupList.groupInList(airgroup1.enemies[0], airgroup)) {
                                int j1 = 0;
                                do {
                                    if (j1 >= airgroup1.nOfAirc) {
                                        break;
                                    }
                                    Aircraft aircraft1 = airgroup1.airc[j1];
                                    if (((SndAircraft) (aircraft1)).FM.isTick(32, 0)) {
                                        int l1 = World.Rnd().nextInt(0, airgroup.nOfAirc - 1);
                                        VisCheck.checkGroup(aircraft1, airgroup.airc[l1], airgroup1, airgroup);
                                        if (airgroup1.bSee) {
                                            War.informOtherGroupsNearBy(airgroup1, War.Groups[1], airgroup);
                                            AirGroupList.addAirGroup(airgroup1.enemies, 0, airgroup);
                                            if ((airgroup.airc[0] != null) && (airgroup1.airc[l1] != null)) {
                                                Voice.speakEnemyDetected(airgroup1.airc[0], airgroup.airc[l1]);
                                            }
                                            airgroup1.setEnemyFighters();
                                            airgroup1.bInitAttack = true;
                                            airgroup1.bSee = false;
                                            break;
                                        }
                                    }
                                    j1++;
                                } while (true);
                            }
                        } else {
                            if (AirGroupList.groupInList(airgroup.enemies[0], airgroup1)) {
                                AirGroupList.delAirGroup(airgroup.enemies, 0, airgroup1);
                                airgroup.setEnemyFighters();
                            }
                            if (AirGroupList.groupInList(airgroup1.enemies[0], airgroup)) {
                                AirGroupList.delAirGroup(airgroup1.enemies, 0, airgroup);
                                airgroup1.setEnemyFighters();
                            }
                        }
                    }
                }

            }
        }

    }

    private void delEmptyGroups() {
        int i = AirGroupList.length(War.Groups[0]);
        int j = AirGroupList.length(War.Groups[1]);
        for (int k = 0; k < i; k++) {
            AirGroup airgroup = AirGroupList.getGroup(War.Groups[0], k);
            if ((airgroup != null) && (airgroup.nOfAirc == 0)) {
                airgroup.release();
                AirGroupList.delAirGroup(War.Groups, 0, airgroup);
            }
        }

        for (int l = 0; l < j; l++) {
            AirGroup airgroup1 = AirGroupList.getGroup(War.Groups[1], l);
            if ((airgroup1 != null) && (airgroup1.nOfAirc == 0)) {
                airgroup1.release();
                AirGroupList.delAirGroup(War.Groups, 1, airgroup1);
            }
        }

    }

    private void checkCollisionForAircraft() {
        List list = Engine.targets();
        int k = list.size();
        for (int i = 0; i < k; i++) {
            Actor actor = (Actor) list.get(i);
            if (actor instanceof Aircraft) {
                FlightModel flightmodel = ((Aircraft) actor).FM;
                if ((flightmodel != null) && flightmodel.isTick(4, 0)) {
                    for (int j = i + 1; j < k; j++) {
                        Actor actor1 = (Actor) list.get(j);
                        if ((i != j) && (actor1 instanceof Aircraft)) {
                            FlightModel flightmodel1 = ((Aircraft) actor1).FM;
                            if ((flightmodel instanceof Pilot) && (flightmodel1 instanceof Pilot)) {
                                float f = (float) flightmodel.Loc.distanceSquared(flightmodel1.Loc);
                                if (f <= 1E+007F) {
                                    if (flightmodel.actor.getArmy() != flightmodel1.actor.getArmy()) {
                                        if (flightmodel instanceof RealFlightModel) {
                                            War.testAsDanger(flightmodel, flightmodel1);
                                        }
                                        if (flightmodel1 instanceof RealFlightModel) {
                                            War.testAsDanger(flightmodel1, flightmodel);
                                        }
                                    }
                                    War.Ve.sub(flightmodel.Loc, flightmodel1.Loc);
                                    float f1 = (float) War.Ve.length();
                                    War.Ve.normalize();
                                    if (flightmodel.actor.getArmy() == flightmodel1.actor.getArmy()) {
                                        War.tmpV.set(War.Ve);
                                        flightmodel1.Or.transformInv(War.tmpV);
                                        if ((War.tmpV.x > 0.0D) && (War.tmpV.y > -0.1D) && (War.tmpV.y < 0.1D) && (War.tmpV.z > -0.1D) && (War.tmpV.z < 0.1D)) {
                                            ((Maneuver) flightmodel1).setShotAtFriend(f1);
                                        }
                                        War.tmpV.set(War.Ve);
                                        War.tmpV.scale(-1D);
                                        flightmodel.Or.transformInv(War.tmpV);
                                        if ((War.tmpV.x > 0.0D) && (War.tmpV.y > -0.1D) && (War.tmpV.y < 0.1D) && (War.tmpV.z > -0.1D) && (War.tmpV.z < 0.1D)) {
                                            ((Maneuver) flightmodel).setShotAtFriend(f1);
                                        }
                                    }
                                    if (f <= 20000F) {
                                        float f2 = (flightmodel.actor.collisionR() + flightmodel1.actor.collisionR()) * 1.5F;
                                        f1 -= f2;
                                        War.Vtarg.sub(flightmodel1.Vwld, flightmodel.Vwld);
                                        War.Vtarg.scale(1.5D);
                                        float f3 = (float) War.Vtarg.length();
                                        if (f3 >= f1) {
                                            War.Vtarg.normalize();
                                            War.Vtarg.scale(f1);
                                            War.Ve.scale(War.Vtarg.dot(War.Ve));
                                            War.Vtarg.sub(War.Ve);
                                            if ((War.Vtarg.length() < f2) || (f1 < 0.0F)) {
                                                if (((Aircraft) actor).FM instanceof Pilot) {
                                                    ((Maneuver) ((Aircraft) actor).FM).setStrikeEmer(flightmodel1);
                                                }
                                                if (((Aircraft) actor1).FM instanceof Pilot) {
                                                    ((Maneuver) ((Aircraft) actor1).FM).setStrikeEmer(flightmodel);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

    }

    public static void testAsDanger(FlightModel flightmodel, FlightModel flightmodel1) {
        if (flightmodel.actor instanceof TypeTransport) {
            return;
        }
        if ((flightmodel instanceof Maneuver) && (flightmodel1 instanceof Maneuver)) {
            AirGroup airgroup = ((Maneuver) flightmodel).Group;
            AirGroup airgroup1 = ((Maneuver) flightmodel1).Group;
            if ((airgroup != null) && (airgroup1 != null) && !AirGroupList.groupInList(airgroup1.enemies[0], airgroup)) {
                return;
            }
        }
        War.Ve.sub(flightmodel1.Loc, flightmodel.Loc);
        flightmodel.Or.transformInv(War.Ve);
        if (War.Ve.x > 0.0D) {
            float f = (float) War.Ve.length();
            War.Ve.normalize();
            ((Maneuver) flightmodel1).incDangerAggressiveness(4, (float) War.Ve.x, f, flightmodel);
        }
    }

    public static Aircraft getNearestSpotter(Actor actor, float f) {
        Point3d point3d = actor.pos.getAbsPoint();
        double d = f * f;
        int i = actor.getArmy();
        Aircraft aircraft = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor1 = (Actor) list.get(k);
            if ((actor1 instanceof Aircraft) && (actor1.getArmy() == i) && ((Aircraft) actor1).bSpotter) {
                Point3d point3d1 = actor1.pos.getAbsPoint();
                double d1 = ((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z));
                if (d1 < d) {
                    aircraft = (Aircraft) actor1;
                    d = d1;
                }
            }
        }

        return aircraft;
    }

    public static Aircraft getNearestFriend(Aircraft aircraft) {
        return War.getNearestFriend(aircraft, 10000F);
    }

    public static Aircraft getNearestFriend(Aircraft aircraft, float f) {
        Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
        double d = f * f;
        int i = aircraft.getArmy();
        Aircraft aircraft1 = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor = (Actor) list.get(k);
            if ((actor instanceof Aircraft) && (actor != aircraft) && (actor.getArmy() == i)) {
                Point3d point3d1 = actor.pos.getAbsPoint();
                double d1 = ((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z));
                if (d1 < d) {
                    aircraft1 = (Aircraft) actor;
                    d = d1;
                }
            }
        }

        return aircraft1;
    }

    public static Aircraft getNearestFriendAtPoint(Point3d point3d, Aircraft aircraft, float f) {
        double d = f * f;
        int i = aircraft.getArmy();
        Aircraft aircraft1 = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor = (Actor) list.get(k);
            if ((actor instanceof Aircraft) && (actor.getArmy() == i)) {
                Point3d point3d1 = actor.pos.getAbsPoint();
                double d1 = ((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z));
                if (d1 < d) {
                    aircraft1 = (Aircraft) actor;
                    d = d1;
                }
            }
        }

        return aircraft1;
    }

    public static Actor getNearestAnyFriendAtPoint(Point3d point3d, Aircraft aircraft, float f) {
        double d = f * f;
        int i = aircraft.getArmy();
        Actor actor = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor1 = (Actor) list.get(k);
            if (actor1.getArmy() == i) {
                Point3d point3d1 = actor1.pos.getAbsPoint();
                double d1 = ((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z));
                if (d1 < d) {
                    actor = actor1;
                    d = d1;
                }
            }
        }

        return actor;
    }

    public static Aircraft getNearestFriendlyFighter(Aircraft aircraft, float f) {
        double d = f * f;
        Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
        int i = aircraft.getArmy();
        Aircraft aircraft1 = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor = (Actor) list.get(k);
            if (actor instanceof Aircraft) {
                Aircraft aircraft2 = (Aircraft) actor;
                if ((aircraft2 != aircraft) && (aircraft2.getArmy() == i) && (aircraft2.getWing() != aircraft.getWing()) && (aircraft2 instanceof TypeFighter)) {
                    Point3d point3d1 = ((Actor) (aircraft2)).pos.getAbsPoint();
                    double d1 = ((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z));
                    if (d1 < d) {
                        aircraft1 = aircraft2;
                        d = d1;
                    }
                }
            }
        }

        return aircraft1;
    }

    public static Aircraft getNearestEnemy(Aircraft aircraft, float f) {
        double d = f * f;
        War.tmpPoint.set(((Actor) (aircraft)).pos.getAbsPoint());
        int i = aircraft.getArmy();
        Aircraft aircraft1 = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor = (Actor) list.get(k);
            if ((actor instanceof Aircraft) && (actor.getArmy() != i)) {
                Point3d point3d = actor.pos.getAbsPoint();
                double d1 = ((War.tmpPoint.x - point3d.x) * (War.tmpPoint.x - point3d.x)) + ((War.tmpPoint.y - point3d.y) * (War.tmpPoint.y - point3d.y)) + ((War.tmpPoint.z - point3d.z) * (War.tmpPoint.z - point3d.z));
                if (d1 < d) {
                    War.tmpPoint.z += 5D;
                    if (VisCheck.visCheck(War.tmpPoint, point3d, null, (Aircraft) actor)) {
                        aircraft1 = (Aircraft) actor;
                        d = d1;
                    }
                }
            }
        }

        return aircraft1;
    }

    public static Actor GetNearestEnemy(Actor actor, int i, float f) {
        return NearestTargets.getEnemy(0, i, actor.pos.getAbsPoint(), f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, int i, float f, int j) {
        return NearestTargets.getEnemy(j, i, actor.pos.getAbsPoint(), f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, int i, float f, Point3d point3d) {
        return NearestTargets.getEnemy(0, i, point3d, f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, int i, Point3d point3d, float f) {
        return NearestTargets.getEnemy(i, 16, point3d, f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, Point3d point3d, float f) {
        return NearestTargets.getEnemy(0, 16, point3d, f, actor.getArmy());
    }

    public static AirGroupList getEnemyGroups(Actor actor) {
        return actor.getArmy() == 1 ? War.Groups[1] : War.Groups[0];
    }

    public static AirGroupList getFriendlyGroups(Actor actor) {
        if (War.Groups.length < actor.getArmy()) {
            return War.Groups[actor.getArmy()];
        } else {
            return null;
        }
    }

    public static Actor GetNearestFromChief(Actor actor, Actor actor1) {
        if (!Actor.isAlive(actor1)) {
            return null;
        }
        Actor actor2 = null;
        if ((actor1 instanceof Chief) || (actor1 instanceof Bridge)) {
            int i = actor1.getOwnerAttachedCount();
            if (i < 1) {
                return null;
            }
            actor2 = (Actor) actor1.getOwnerAttached(0);
            double d = actor.pos.getAbsPoint().distance(actor2.pos.getAbsPoint());
            for (int j = 1; j < i; j++) {
                Actor actor3 = (Actor) actor1.getOwnerAttached(j);
                double d1 = actor.pos.getAbsPoint().distance(actor3.pos.getAbsPoint());
                if (d1 < d) {
                    actor2 = actor3;
                }
            }

        }
        return actor2;
    }

    public static Actor GetRandomFromChief(Actor actor, Actor actor1) {
        if (!Actor.isAlive(actor1)) {
            return null;
        }
        if ((actor1 instanceof Chief) || (actor1 instanceof Bridge)) {
            int i = actor1.getOwnerAttachedCount();
            if (i < 1) {
                return null;
            }
            for (int j = 0; j < i; j++) {
                Actor actor2 = (Actor) actor1.getOwnerAttached(World.Rnd().nextInt(0, i - 1));
                if (Actor.isValid(actor2) && actor2.isAlive()) {
                    return actor2;
                }
            }

            for (int k = 0; k < i; k++) {
                Actor actor3 = (Actor) actor1.getOwnerAttached(k);
                if (Actor.isValid(actor3) && actor3.isAlive()) {
                    return actor3;
                }
            }

        }
        return actor1;
    }

    public static Aircraft GetNearestEnemyAircraft(Actor actor, float f, int i) {
        Actor actor1 = War.GetNearestEnemy(actor, -1, f, i);
        if (actor1 instanceof Aircraft) {
            return (Aircraft) actor1;
        }
        actor1 = War.GetNearestEnemy(actor, -1, f, 9);
        if (actor1 instanceof Aircraft) {
            return (Aircraft) actor1;
        } else {
            return null;
        }
    }

    public static final int    TICK_DIV4  = 4;
    public static final int    TICK_DIV8  = 8;
    public static final int    TICK_DIV16 = 16;
    public static final int    TICK_DIV32 = 32;
    public static final int    ARMY_NUM   = 2;
    public static AirGroupList Groups[]   = new AirGroupList[2];
    private static int         curArmy    = 0;
    private static int         curGroup   = 0;
    private static Vector3d    tmpV       = new Vector3d();
    private static Vector3d    Ve         = new Vector3d();
    private static Vector3d    Vtarg      = new Vector3d();
    private static Point3d     tmpPoint   = new Point3d();

}
