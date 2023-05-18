package com.maddox.il2.ai.air;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.ai.ground.TgtVehicle;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.sas1946.il2.util.TrueRandom;

public class NearestRadarTargets {

    public static Actor GetNearestEcho(Actor actor, int hitMask, float maxDistance, int targetType) {
        return getEnemy(targetType, hitMask, actor.pos.getAbsPoint(), maxDistance, actor.getArmy());
    }

    public static Actor GetNearestEcho(Actor actor, int hitMask, float maxDistanceStart, float maxDistanceEnd, float maxDistanceStep, int targetType) {
        return getEnemy(targetType, hitMask, actor.pos.getAbsPoint(), maxDistanceStart, maxDistanceEnd, maxDistanceStep, actor.getArmy());
    }

    public static Aircraft GetNearestEnemyEcho(Actor actor, float maxDistance, int targetType) {
        Actor nearestEcho = GetNearestEcho(actor, HIT_MASK_ALL, maxDistance, targetType);
        if (nearestEcho instanceof Aircraft) {
            return (Aircraft) nearestEcho;
        }
        nearestEcho = GetNearestEcho(actor, HIT_MASK_ALL, maxDistance, TARGET_TYPE_AIRCRAFT);
        if (nearestEcho instanceof Aircraft) {
            return (Aircraft) nearestEcho;
        } else {
            return null;
        }
    }

    public static Aircraft GetNearestEnemyEchoInSteps(Actor actor, float maxDistanceStart, float maxDistanceEnd, float maxDistanceStep, int targetType) {
        Actor nearestEcho = GetNearestEcho(actor, HIT_MASK_ALL, maxDistanceStart, maxDistanceEnd, maxDistanceStep, targetType);
        if (nearestEcho instanceof Aircraft) {
            return (Aircraft) nearestEcho;
        }
        nearestEcho = GetNearestEcho(actor, HIT_MASK_ALL, maxDistanceStart, maxDistanceEnd, maxDistanceStep, TARGET_TYPE_AIRCRAFT);
        if (nearestEcho instanceof Aircraft) {
            return (Aircraft) nearestEcho;
        } else {
            return null;
        }
    }

    private static boolean isValidTarget(Actor actor, int targetType) {
        switch (targetType) {
            case TARGET_TYPE_ACTOR:
            default:
                return Actor.class.isInstance(actor);
            case TARGET_TYPE_TANK:
                return TgtTank.class.isInstance(actor);
            case TARGET_TYPE_FLAK:
                return TgtFlak.class.isInstance(actor);
            case TARGET_TYPE_VEHICLE:
                return TgtVehicle.class.isInstance(actor);
            case TARGET_TYPE_TRAIN:
                return TgtTrain.class.isInstance(actor);
            case TARGET_TYPE_SHIP:
                return TgtShip.class.isInstance(actor);
            case TARGET_TYPE_FIGHTER:
                return TypeFighter.class.isInstance(actor);
            case TARGET_TYPE_BOMBER_STURMOVIK:
                return TypeBomber.class.isInstance(actor) || TypeStormovik.class.isInstance(actor);
            case TARGET_TYPE_AIRCRAFT:
                return Aircraft.class.isInstance(actor);
        }
    }

    public static List getEnemyList(int targetType, int hitMask, Point3d radarPos, float maxDistance, int radarArmy) {
        List targetList = Engine.targets();
        ArrayList radarContacts = new ArrayList();

        for (int targetListIndex = 0; targetListIndex < targetList.size(); targetListIndex++) {
            Actor target = (Actor) targetList.get(targetListIndex);
            int targetArmy = target.getArmy();

            if ((targetArmy == 0) || (targetArmy == radarArmy)) {
                continue; // potential target is either neutral or on our side!
            }
            if (!isValidTarget(target, targetType)) {
                continue; // potential target is not of the same type we're searching for!
            }
            if ((targetType == 0) && (target instanceof BridgeSegment)) {
                continue; // potential target is a Bridge Segment, we're searching for all kind of actors but this explicitely does not include Bridges!
            }
            if ((((Prey) target).HitbyMask() & hitMask) == 0) {
                continue; // potential target cannot be hit by our weapon of choice!
            }

            double targetDistance = radarPos.distance(target.pos.getAbsPoint());
            if (targetDistance > maxDistance) {
                continue;
            }
            if ((targetType == TARGET_TYPE_AIRCRAFT) && (target.getSpeed(null) <= 10D)) {
                continue; // potential target is an aircraft, but apparently it's not flying!
            }
            radarContacts.add(new Contact(target, targetDistance));
        }
        if (!radarContacts.isEmpty()) {
            Collections.sort(radarContacts);
        }
        return radarContacts;
    }

    public static Actor getEnemy(int targetType, int hitMask, Point3d radarPos, float maxDistance, int radarArmy) {
        if (targetType == TARGET_TYPE_BRIDGE) {
            return getBridge(hitMask, radarPos, maxDistance);
        }
        List radarContacts = getEnemyList(targetType, hitMask, radarPos, maxDistance, radarArmy);
        if (radarContacts.isEmpty()) {
            if (targetType == TARGET_TYPE_ACTOR) {
                return getBridge(hitMask, radarPos, maxDistance); // if we search for any kind of actor and there is none, choose bridges as a last resort.
            }
            return null;
        }
        int indexMax = Math.min(radarContacts.size(), MAX_CONTACTS) - 1;
        int randomIndex = 0;
        if (indexMax > 0) {
            randomIndex = World.cur().rnd.nextInt(indexMax);
        }
        Contact enemyContact = (Contact) radarContacts.get(randomIndex);
        return enemyContact.getContact();
    }

    public static Actor getEnemy(int targetType, int hitMask, Point3d radarPos, float maxDistanceStart, float maxDistanceEnd, float maxDistanceStep, int radarArmy) {
        if (targetType == TARGET_TYPE_BRIDGE) {
            return getBridge(hitMask, radarPos, maxDistanceEnd);
        }
        List radarContacts = getEnemyList(targetType, hitMask, radarPos, maxDistanceEnd, radarArmy);
        if (radarContacts.isEmpty()) {
            if (targetType == TARGET_TYPE_ACTOR) {
                return getBridge(hitMask, radarPos, maxDistanceEnd); // if we search for any kind of actor and there is none, choose bridges as a last resort.
            }
            return null;
        }
        Contact enemyContact = (Contact) radarContacts.get(0);
        float distanceStepStart = maxDistanceStart;
        while (true) {
            if (enemyContact.getDistance() < distanceStepStart) {
                break;
            }
            if (distanceStepStart > maxDistanceEnd) {
                return null;
            }
            distanceStepStart += maxDistanceStep;
        }
        float distanceStepEnd = distanceStepStart + maxDistanceStep;
        int indexMax = 1;
        while (true) {
            if (indexMax >= radarContacts.size()) {
                break;
            }
            enemyContact = (Contact) radarContacts.get(indexMax);
            if (enemyContact.getDistance() > distanceStepEnd) {
                break;
            }
            indexMax++;
        }
        indexMax = Math.min(indexMax, MAX_CONTACTS) - 1;
        int randomIndex = 0;
        if (indexMax > 0) {
            randomIndex = World.cur().rnd.nextInt(indexMax);
        }
        enemyContact = (Contact) radarContacts.get(randomIndex);
        return enemyContact.getContact();
    }

// TODO: Edited by SAS~Storebror: This original method is plain, pure, genuine bullshit!
//    public static Actor getEnemy(int i, int j, Point3d point3d, double d, int k)
//    {
//        Class class1 = null;
//        Class class2 = null;
//        switch(i)
//        {
//        case 0:
//        default:
//            class1 = com.maddox.il2.engine.Actor.class;
//            class2 = com.maddox.il2.engine.Actor.class;
//            break;
//
//        case 1:
//            class1 = com.maddox.il2.ai.ground.TgtTank.class;
//            class2 = com.maddox.il2.ai.ground.TgtTank.class;
//            break;
//
//        case 2:
//            class1 = com.maddox.il2.ai.ground.TgtFlak.class;
//            class2 = com.maddox.il2.ai.ground.TgtFlak.class;
//            break;
//
//        case 3:
//            class1 = com.maddox.il2.ai.ground.TgtVehicle.class;
//            class2 = com.maddox.il2.ai.ground.TgtVehicle.class;
//            break;
//
//        case 4:
//            class1 = com.maddox.il2.ai.ground.TgtTrain.class;
//            class2 = com.maddox.il2.ai.ground.TgtTrain.class;
//            break;
//
//        case 5:
//            return getBridge(j, point3d, d);
//
//        case 6:
//            class1 = com.maddox.il2.ai.ground.TgtShip.class;
//            class2 = com.maddox.il2.ai.ground.TgtShip.class;
//            break;
//
//        case 7:
//            class1 = com.maddox.il2.objects.air.TypeFighter.class;
//            class2 = com.maddox.il2.objects.air.TypeFighter.class;
//            break;
//
//        case 8:
//            class1 = com.maddox.il2.objects.air.TypeBomber.class;
//            class2 = com.maddox.il2.objects.air.TypeStormovik.class;
//            break;
//
//        case 9:
//            class1 = com.maddox.il2.objects.air.Aircraft.class;
//            class2 = com.maddox.il2.objects.air.Aircraft.class;
//            break;
//        }
//        List list = Engine.targets();
//        int l = list.size();
//        double d1 = d * d;
//        int i1 = 0;
//        for(int j1 = 0; j1 < l; j1++)
//        {
//            Actor actor = (Actor)list.get(j1);
//            int k1 = actor.getArmy();
//            if(k1 != 0 && k1 != k && (class1.isInstance(actor) || class2.isInstance(actor)) && (i != 0 || !(actor instanceof BridgeSegment)) && (((Prey)actor).HitbyMask() & j) != 0)
//            {
//                Point3d point3d1 = actor.pos.getAbsPoint();
//                double d2 = (point3d1.x - point3d.x) * (point3d1.x - point3d.x) + (point3d1.y - point3d.y) * (point3d1.y - point3d.y) + (point3d1.z - point3d.z) * (point3d1.z - point3d.z);
//                if(d2 <= d1)
//                {
//                    int i2;
//                    for(i2 = 0; i2 < i1; i2++)
//                        if(d2 < nearDSq[i2])
//                            break;
//
//                    if(i2 >= i1)
//                    {
//                        if(i1 < 32)
//                        {
//                            nearAct[i1] = actor;
//                            nearDSq[i1] = d2;
//                            i1++;
//                        }
//                    } else
//                    {
//                        int j2;
//                        if(i1 < 32)
//                        {
//                            j2 = i1 - 1;
//                            i1++;
//                        } else
//                        {
//                            j2 = i1 - 2;
//                        }
//                        for(; j2 >= i2; j2--)
//                        {
//                            nearAct[j2 + 1] = nearAct[j2];
//                            nearDSq[j2 + 1] = nearDSq[j2];
//                        }
//
//                        nearAct[i2] = actor;
//                        nearDSq[i2] = d2;
//                    }
//                }
//            }
//        }
//
//        if(i1 == 0)
//            if(i == 0)
//                return getBridge(j, point3d, d);
//            else
//                return null;
//        Actor actor1 = nearAct[i1 == 1 ? 0 : TrueRandom.nextInt(i1)];
//        for(int l1 = 0; l1 < i1; l1++)
//            nearAct[l1] = null;
//
//        return actor1;
//    }

    public static Actor getBridge(int i, Point3d point3d, double d) {
        if ((0x18 & i) == 0) {
            return null;
        }
        ArrayList arraylist = World.cur().statics.bridges;
        int j = arraylist.size();
        double d1 = d * d;
        LongBridge longbridge = null;
        for (int k = 0; k < j; k++) {
            LongBridge longbridge1 = (LongBridge) arraylist.get(k);
            if (longbridge1.isAlive()) {
                Point3d point3d1 = longbridge1.pos.getAbsPoint();
                double d2 = ((point3d1.x - point3d.x) * (point3d1.x - point3d.x)) + ((point3d1.y - point3d.y) * (point3d1.y - point3d.y)) + ((point3d1.z - point3d.z) * (point3d1.z - point3d.z));
                if (d2 <= d1) {
                    longbridge = longbridge1;
                    d1 = d2;
                }
            }
        }

        if (longbridge == null) {
            return null;
        } else {
            int l = longbridge.NumStateBits() / 2;
            return BridgeSegment.getByIdx(longbridge.bridgeIdx(), TrueRandom.nextInt(l));
        }
    }

    public static final int  TARGET_TYPE_ACTOR            = 0;
    public static final int  TARGET_TYPE_TANK             = 1;
    public static final int  TARGET_TYPE_FLAK             = 2;
    public static final int  TARGET_TYPE_VEHICLE          = 3;
    public static final int  TARGET_TYPE_TRAIN            = 4;
    public static final int  TARGET_TYPE_BRIDGE           = 5;
    public static final int  TARGET_TYPE_SHIP             = 6;
    public static final int  TARGET_TYPE_FIGHTER          = 7;
    public static final int  TARGET_TYPE_BOMBER_STURMOVIK = 8;
    public static final int  TARGET_TYPE_AIRCRAFT         = 9;
    private static final int MAX_CONTACTS                 = 32;
    private static final int HIT_MASK_ALL                 = -1;

    public static class Contact implements Comparable {
        private Actor contact;

        public Actor getContact() {
            return this.contact;
        }

        public double getDistance() {
            return this.distance;
        }

        private double distance;

        public int compareTo(Object other) {
            if (this.distance < ((Contact) other).distance) {
                return -1;
            }
            if (this.distance > ((Contact) other).distance) {
                return 1;
            }
            return 0;
        }

        public Contact(Actor contact, double distance) {
            this.contact = contact;
            this.distance = distance;
        }
    }

//    private static final int MAX_OBJECTS = 32;
//    private static Actor nearAct[] = new Actor[32];
//    private static double nearDSq[] = new double[32];

}
