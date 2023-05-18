package com.maddox.il2.objects.vehicles.stationary;

import java.lang.reflect.Method;
import java.util.List;
// import java.util.Random;
import java.util.Map.Entry;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.NearestRadarTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.ai.ground.TgtVehicle;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.Selector;
import com.maddox.il2.game.VisibilityChecker;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.B_17;
import com.maddox.il2.objects.air.B_24;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeScout;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.Ship.SubTypeVIIC_Srf;
import com.maddox.il2.objects.ships.Ship.SubTypeVIIC_Sub;
import com.maddox.il2.objects.ships.Ship.USSGatoSS212_Srf;
import com.maddox.il2.objects.ships.Ship.USSGatoSS212_Sub;
import com.maddox.il2.objects.ships.Ship.USSGreenlingSS213_Srf;
import com.maddox.il2.objects.ships.Ship.USSGreenlingSS213_Sub;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryCY6;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.ObjState;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;

public abstract class CandC {
    public static class DynamicStrikeUnit extends CandCGeneric {

        public boolean danger() {
            if (Time.current() <= this.delay) {
                return true;
            }
            List list = Engine.targets();
            int i = list.size();
            if (this.l < i) {
                Actor actor = (Actor) list.get(this.l);
                if ((actor != null) && ((actor instanceof TypeStormovik) || (actor instanceof TypeFighter) || (actor instanceof TypeDiveBomber)) && !(actor instanceof TypeBomber) && (actor.getArmy() == this.myArmy) && ((Maneuver) ((Aircraft) actor).FM).hasBombs()) {
                    this.findtarget(actor);
                } else if ((actor != null) && (actor instanceof TypeFighter) && !(actor instanceof TypeBomber) && (actor.getArmy() == this.myArmy) && !((Maneuver) ((Aircraft) actor).FM).hasBombs()) {
                    this.fightertarget(actor);
                }
                this.l++;
            } else {
                super.resetTimer(0.1F);
                this.listen();
            }
            return true;
        }

        public void listen() {
            List list = Engine.targets();
            int i;
            for (i = list.size(); this.m < i; this.m++) {
                Actor actor = (Actor) list.get(this.m);
                if ((actor != null) && ((actor instanceof TypeStormovik) || (actor instanceof TypeFighter) || (actor instanceof TypeDiveBomber)) && !(actor instanceof TypeBomber) && (actor.getArmy() == this.myArmy) && ((Maneuver) ((Aircraft) actor).FM).hasBombs()) {
                    Pilot pilot = (Pilot) ((Aircraft) actor).FM;
//                    if(pilot.AP.way.curr().Action == 3 && pilot.get_task() != 7)
                    if ((pilot.Group != null) && (pilot.AP.way.curr().Action == 3) && (pilot.get_task() != 7)) // TODO: Fixed by SAS~Storebror, added pilot Group null check!
                    {
                        pilot.Group.setGroupTask(4);
                        Point3d point3d = actor.pos.getAbsPoint();
                        point3d.z = World.land().HQ(point3d.x, point3d.y);
                        pilot.Group.setGTargMode(point3d, 8000F);
                    }
                }
            }

            if (this.m >= i) {
                this.m = 0;
            }
        }

        public void findtarget(Actor actor) {
            boolean flag = false;
            do {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = TrueRandom.nextInt(i);
                Actor actor1 = (Actor) list.get(j);
                if ((actor1 != null) && !(actor1 instanceof Aircraft) && ((actor1 instanceof TgtVehicle) || (actor1 instanceof TgtTank) || (actor1 instanceof TgtTrain)) && (actor1 instanceof StationaryGeneric) && !(actor1 instanceof BigshipGeneric)) {
                    if (actor1.getArmy() == actor.getArmy()) {
                        ;
                    }
                }
                Point3d point3d = actor1.pos.getAbsPoint();
                if (actor.pos.getAbsPoint().distance(point3d) > 10000D) {
                    if (actor instanceof TypeDiveBomber) {
                        this.assigntarget(point3d, actor, 5000F, true);
                    } else {
                        this.assigntarget(point3d, actor, 1500F, true);
                    }
                    flag = true;
                }
            } while (!flag);
        }

        public void fightertarget(Actor actor) {
            boolean flag = false;
            do {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = TrueRandom.nextInt(i);
                Actor actor1 = (Actor) list.get(j);
                if (((actor1 instanceof TgtVehicle) || (actor1 instanceof TgtTank) || (actor1 instanceof TgtTrain)) && (actor1 instanceof StationaryGeneric)) {
                    if (flag) {
                        ;
                    }
                }
                Point3d point3d = actor1.pos.getAbsPoint();
                flag = true;
                if (actor.pos.getAbsPoint().distance(point3d) > 10000D) {
                    this.assigntarget(point3d, actor, 5000F, false);
                    flag = true;
                }
            } while (!flag);
        }

        public void assigntarget(Point3d point3d, Actor actor, float f, boolean flag) {
            Pilot pilot = (Pilot) ((Aircraft) actor).FM;
            Point3d point3d1 = new Point3d();
            pilot.AP.way.curr().getP(point3d1);
            // // Random random = new Random();
            int i = ((int) f + TrueRandom.nextInt(1000)) - 500;
            Point3d point3d2 = new Point3d(point3d.x, point3d.y, i);
            Point3d point3d3 = new Point3d(point3d2.x - 5000D, point3d2.y, i);
            if ((point3d1.x - point3d2.x) > 0.0D) {
                point3d3.set(point3d2.x + 5000D, point3d2.y, i);
            }
            Point3d point3d4 = new Point3d(point3d2.x, point3d2.y - 5000D, i);
            if ((point3d1.y - point3d2.y) > 0.0D) {
                point3d4.set(point3d2.x, point3d2.y + 5000D, i);
            }
            Point3d point3d5 = new Point3d(point3d1.x + 10000D, point3d1.y + 5000D, i);
            Point3d point3d6 = new Point3d();
            pilot.AP.way.first().getP(point3d6);
            point3d6.z = i;
            WayPoint waypoint = new WayPoint(point3d3);
            WayPoint waypoint1 = new WayPoint(point3d2);
            WayPoint waypoint2 = new WayPoint(point3d4);
            WayPoint waypoint3 = new WayPoint(point3d5);
            WayPoint waypoint4 = new WayPoint(point3d6);
            float f1 = pilot.Vmax * 0.8F; // TODO: Fixed by SAS~Storebror: Useless double conversion removed
            waypoint.set(f1);
            waypoint1.set(f1);
            waypoint2.set(f1);
            waypoint3.set(f1);
            waypoint4.set(f1);
            waypoint.Action = 0;
            waypoint1.Action = 3;
            if (!flag) {
                waypoint1.Action = 0;
            }
            waypoint2.Action = 0;
            waypoint3.Action = 0;
            waypoint4.Action = 0;
            if (pilot.AP.way.first().Action == 1) {
                waypoint4.Action = 2;
                if (pilot.AP.way.first().getTargetName() != null) {
                    waypoint4.setTarget(pilot.AP.way.first().getTargetName());
                }
            }
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint1);
            pilot.AP.way.add(waypoint2);
            if (!flag) {
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
            }
            pilot.AP.way.add(waypoint3);
            pilot.AP.way.add(waypoint4);
        }

        private int l;
        private int m;

        public DynamicStrikeUnit() {
            this.l = 0;
            this.m = 0;
            this.Timer1 = this.Timer2 = 0.01F;
            this.delay = 10F;
        }
    }

    public static class DynamicStreamUnit extends CandCGeneric {

        public boolean danger() {
            List list = Engine.targets();
            int i = list.size();
            if (this.l < i) {
                Actor actor = (Actor) list.get(this.l);
                if ((actor != null) && (actor instanceof TypeBomber) && (actor.getArmy() == this.myArmy)) {
                    this.findtarget(actor);
                }
                if ((actor != null) && (actor instanceof TypeFighter) && (actor.getArmy() == this.myArmy)) {
                    // // Random random = new Random();
                    int j = TrueRandom.nextInt(100);
                    if (j > 50) {
                        this.findtarget(actor);
                    } else {
                        this.intrudertarget(actor);
                    }
                }
                this.l++;
            } else {
                this.cleanup();
//                ObjState.destroy(this); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            return true;
        }

        public void findtarget(Actor actor) {
            for (boolean flag = false; !flag;) {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = TrueRandom.nextInt(i);
                Actor actor1 = (Actor) list.get(j);
                if ((actor1 instanceof Stationary.Wagon8) && (actor1.getArmy() != this.myArmy) && !flag) {
//                    int k = TrueRandom.nextInt(100);
                    Point3d point3d = new Point3d();
                    actor1.pos.getAbs(point3d);
                    falsetarget = point3d;
                    flag = true;
                    if (!this.first) {
                        maintarget = point3d;
                        ObjState.destroy(actor1); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor1.postDestroy();
                        this.first = true;
                    }
                }
            }

            if (((Maneuver) ((Aircraft) actor).FM).Skill > 2) {
                this.assigntarget(falsetarget, actor);
            } else {
                this.assigntarget(maintarget, actor);
            }
        }

        public void intrudertarget(Actor actor) {
            for (boolean flag = false; !flag;) {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = TrueRandom.nextInt(i);
                Actor actor1 = (Actor) list.get(j);
                if ((actor1 instanceof Stationary.OpelBlitz6700A_fuel) && (actor1.getArmy() != this.myArmy) && !flag) {
                    actor1.pos.getAbs(falsetarget);
                    flag = true;
                }
            }

            this.assignintrudertarget(falsetarget, actor);
        }

        public void cleanup() {
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor != null) && ((actor instanceof OBOEUnit) || (actor instanceof H2SUnit) || (actor instanceof AimpointUnit) || (actor instanceof ErrorUnit)) && (actor.pos.getAbsPoint().distance(maintarget) > 1000D) && (actor.getArmy() == this.myArmy)) {
                    //                    ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    actor.postDestroy();
                }
            }

        }

        public void assignintrudertarget(Point3d point3d, Actor actor) {
            Pilot pilot = (Pilot) ((Aircraft) actor).FM;
            Point3d point3d1 = new Point3d();
            pilot.AP.way.curr().getP(point3d1);
            // // Random random = new Random();
            int i = TrueRandom.nextInt(4000) - 2000;
            int j = TrueRandom.nextInt(4000) - 2000;
            int k = (3000 + TrueRandom.nextInt(2000)) - 1000;
            Point3d point3d2 = new Point3d(point3d.x + i, point3d.y + j, k);
            Point3d point3d3 = new Point3d(point3d2.x - 10000D, point3d2.y, k);
            if ((point3d1.x - point3d2.x) > 0.0D) {
                point3d3.set(point3d2.x + 10000D, point3d2.y, k);
            }
            Point3d point3d4 = new Point3d(point3d2.x, point3d2.y - 10000D, k);
            if ((point3d1.y - point3d2.y) > 0.0D) {
                point3d4.set(point3d2.x, point3d2.y + 10000D, k);
            }
            Point3d point3d5 = new Point3d(point3d1.x + 10000D, point3d1.y + 10000D, k);
            Point3d point3d6 = new Point3d();
            pilot.AP.way.first().getP(point3d6);
            point3d6.z = k;
            WayPoint waypoint = new WayPoint(point3d3);
            WayPoint waypoint1 = new WayPoint(point3d2);
            WayPoint waypoint2 = new WayPoint(point3d4);
            WayPoint waypoint3 = new WayPoint(point3d5);
            WayPoint waypoint4 = new WayPoint(point3d6);
            waypoint.set(400F);
            waypoint1.set(400F);
            waypoint2.set(400F);
            waypoint3.set(400F);
            waypoint4.set(400F);
            waypoint.Action = 0;
            waypoint1.Action = 0;
            waypoint2.Action = 0;
            waypoint3.Action = 0;
            waypoint4.Action = 0;
            if (pilot.AP.way.first().Action == 1) {
                waypoint4.Action = 2;
            }
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint1);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint3);
            pilot.AP.way.add(waypoint4);
        }

        public void assigntarget(Point3d point3d, Actor actor) {
            Pilot pilot = (Pilot) ((Aircraft) actor).FM;
            Point3d point3d1 = new Point3d();
            pilot.AP.way.curr().getP(point3d1);
            // // Random random = new Random();
            int i = TrueRandom.nextInt(4000) - 2000;
            int j = TrueRandom.nextInt(4000) - 2000;
            int k = (6000 + TrueRandom.nextInt(2000)) - 1000;
            Point3d point3d2 = new Point3d(point3d.x + i, point3d.y + j, k);
            Point3d point3d3 = new Point3d(point3d2.x - 10000D, point3d2.y, k);
            if ((point3d1.x - point3d2.x) > 0.0D) {
                point3d3.set(point3d2.x + 10000D, point3d2.y, k);
            }
            Point3d point3d4 = new Point3d(point3d2.x, point3d2.y - 10000D, k);
            if ((point3d1.y - point3d2.y) > 0.0D) {
                point3d4.set(point3d2.x, point3d2.y + 10000D, k);
            }
            Point3d point3d5 = new Point3d(point3d1.x + 10000D, point3d1.y + 10000D, k);
            Point3d point3d6 = new Point3d();
            pilot.AP.way.first().getP(point3d6);
            point3d6.z = k;
            WayPoint waypoint = new WayPoint(point3d3);
            WayPoint waypoint1 = new WayPoint(point3d2);
            WayPoint waypoint2 = new WayPoint(point3d4);
            WayPoint waypoint3 = new WayPoint(point3d5);
            WayPoint waypoint4 = new WayPoint(point3d6);
            waypoint.set(400F);
            waypoint1.set(400F);
            waypoint2.set(400F);
            waypoint3.set(400F);
            waypoint4.set(400F);
            waypoint.Action = 0;
            waypoint1.Action = 3;
            if (actor instanceof TypeFighter) {
                waypoint1.Action = 0;
            }
            waypoint2.Action = 0;
            waypoint3.Action = 0;
            waypoint4.Action = 0;
            if (pilot.AP.way.first().Action == 1) {
                waypoint4.Action = 2;
            }
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint1);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint3);
            pilot.AP.way.add(waypoint4);
        }

        private static Point3d falsetarget = new Point3d();
        private static Point3d maintarget  = new Point3d();
        private boolean        first;
        private int            l;

        public DynamicStreamUnit() {
            this.first = false;
            this.l = 0;
            this.Timer1 = this.Timer2 = 0.01F;
            this.delay = 1.0F;
        }
    }

    public static class LandingUnit extends CandCGeneric {

        public boolean danger() {
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if ((actor instanceof Aircraft) && ((Maneuver) ((Aircraft) actor).FM).AP.way.isLandingOnShip() && (((Maneuver) ((Aircraft) actor).FM).get_maneuver() == 25) && (actor != World.getPlayerAircraft())) {
                    Pilot pilot = (Pilot) ((Aircraft) actor).FM;
//                    Point3d point3d = new Point3d();
                    actor.pos.getAbs(this.point3d);
                    float f = ((Pilot) ((Aircraft) actor).FM).getAltitude() - (float) World.land().HQ(this.point3d.x, this.point3d.y); // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    if (((float) pilot.Vwld.length() > pilot.VminFLAPS) && (f <= 60F)) {
                        pilot.Vwld.scale(0.955D);
                    }
//                    Vector3d vector3d = new Vector3d();
                    actor.getSpeed(this.vector3d);
                    if ((f <= 10F) && (this.vector3d.z < 0.0D)) {
                        this.vector3d.z = 0.0D;
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public LandingUnit() {
            this.Timer1 = this.Timer2 = 0.1F;
        }
    }

    public static class LevelUnit extends CandCGeneric {

        public boolean danger() {
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);

                // TODO: Fixed by SAS~Storebror: Completely rewritten code part to clean up the mess...
//                if(actor != null && (actor instanceof Aircraft) && !(actor instanceof TypeTransport) && ((Pilot)((Aircraft)actor).FM).get_task() == 7 && actor != World.getPlayerAircraft())
//                {
//
//                    if(!((Maneuver)((Aircraft)actor).FM).hasRockets() && !((Maneuver)((Aircraft)actor).FM).hasBombs())
//                    {
//                        ((Maneuver)((Aircraft)actor).FM).AP.way.next();
//                        ((Maneuver)((Aircraft)actor).FM).target_ground = null;
//                        ((Maneuver)((Aircraft)actor).FM).Group.setGroupTask(1);
//                        ((Maneuver)((Aircraft)actor).FM).Group.setGroupTask(1);
//                    }
//                    ((Maneuver)((Aircraft)actor).FM).CT.dropFuelTanks();
//                    Vector3d vector3d = new Vector3d();
//                    actor.getSpeed(vector3d);
//                    Point3d point3d = new Point3d();
//                    actor.pos.getAbs(point3d);
//                    float f = (float)((double)((Pilot)((Aircraft)actor).FM).getAltitude() - World.land().HQ(point3d.x, point3d.y));
//                    int k = 100;
//                    if(((Maneuver)((Aircraft)actor).FM).hasBombs())
//                        k = 300;
//                    if(f <= (float)k && vector3d.z < 0.0D)
//                        vector3d.z = 0.0D;
//                    actor.setSpeed(vector3d);
//                    if(f >= 1500F && vector3d.z > 0.0D)
//                        vector3d.z = 0.0D;
//                    actor.setSpeed(vector3d);
//                }

                if (actor == null) {
                    continue;
                }
                if (!(actor instanceof Aircraft)) {
                    continue;
                }
                Aircraft actorAircraft = (Aircraft) actor;
                if ((actorAircraft instanceof TypeTransport) || (actorAircraft == World.getPlayerAircraft())) {
                    continue;
                }
                if (!(actorAircraft.FM instanceof Pilot)) {
                    continue;
                }
                Pilot actorPilot = (Pilot) actorAircraft.FM;
                if (actorPilot.get_task() != 7) {
                    continue;
                }
                if (actorPilot.hasRockets() && !actorPilot.hasBombs()) {
                    actorPilot.AP.way.next();
                    actorPilot.target_ground = null;
                    if (actorPilot.Group != null) {
                        actorPilot.Group.setGroupTask(1); // FIXME: Why twice? No idea, but doesn't hurt and might
                        actorPilot.Group.setGroupTask(1); //        be necessary, code is impossible to see through!
                    }
                }
                actorPilot.CT.dropFuelTanks();
                actor.getSpeed(this.vector3d);
                float f = (float) (actorPilot.getAltitude() - World.land().HQ(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y));
                int k = 100;
                if (actorPilot.hasBombs()) {
                    k = 300;
                }
                if ((f <= k) && (this.vector3d.z < 0D)) {
                    this.vector3d.z = 0D;
                }
                if ((f >= 1500F) && (this.vector3d.z > 0D)) {
                    this.vector3d.z = 0D;
                }
                actor.setSpeed(this.vector3d);
            }

            return true;
        }

//        private int counter;
//        private boolean hadtarget;

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        public LevelUnit() {
//            counter = 0;
//            hadtarget = false;
            this.Timer1 = this.Timer2 = 0.1F;
        }
    }

    public static class CityLightsUnit extends CandCGeneric {

        public boolean danger() {
//            World.MaxVisualDistance = 50000F; // TODO: Fixed by SAS~Storebror: WTF???!??
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            if ((this.counter <= 100) && (aircraft.pos.getAbsPoint().distance(this.point3d) < 20000D)) {
                while ((this.counter <= 100) && !flag) {
                    // TODO: Fixed by SAS~Storebror: Replace that crap!
                    // // Random random = new Random();
//                    int i = TrueRandom.nextInt(3000);
//                    float f = i - 1500;
//                    i = TrueRandom.nextInt(3000);
//                    float f1 = i - 1500;
//                    Engine.land();

                    double d0 = TrueRandom.nextDouble(-1500D, 1500D);
                    double d1 = TrueRandom.nextDouble(-1500D, 1500D);

                    int j = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(this.point3d.x + d0), Engine.land().WORLD2PIXY(this.point3d.y + d1));
//                    float f2 = (float)(World.land().HQ(point3d.x + (double)f, point3d.y + (double)f1) - point3d.z);
                    double d2 = World.land().HQ(this.point3d.x + d0, this.point3d.y + d1) - this.point3d.z;
                    if ((j >= 16) && (j < 20)) {
                        Eff3DActor.New(new Loc(this.point3d.x + d0, this.point3d.y + d1, this.point3d.z + d2, 0.0F, 90F, 0.0F), 1.0F, "3DO/Effects/Fireworks/CityLight.eff", -1F);
                        flag = true;
                        this.counter++;
                    } else {
                        flag = true;
                    }
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     counter;

        public CityLightsUnit() {
            this.counter = 0;
            this.Timer1 = this.Timer2 = this.delay = 0.01F;
        }
    }

    public static class SLD2Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
            Aircraft aircraft = World.getPlayerAircraft();
            if (aircraft.pos.getAbsPoint().distance(this.point3d) < 5000D) {
                for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                    Actor actor = (Actor) entry.getValue();
                    if (Actor.isAlive(actor) && (actor instanceof ArtilleryCY6.ProneInfantry)) {
                        // // Random random = new Random();
                        int i = TrueRandom.nextInt(360);
                        new Soldier(this, this.getArmy(), new Loc(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y, actor.pos.getAbsPoint().z, i, 0.0F, 0.0F));
                        super.setTimer(500);
                    }
                }

            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public SLD2Unit() {
            this.Timer1 = this.Timer2 = 0.1F;
        }
    }

    public static class SLDUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
            Aircraft aircraft = World.getPlayerAircraft();
            if (aircraft.pos.getAbsPoint().distance(this.point3d) < 10000D) {
                // // Random random = new Random();
                int i = TrueRandom.nextInt(360);
                new Soldier(this, this.getArmy(), new Loc(this.pos.getAbsPoint().x, this.pos.getAbsPoint().y, this.pos.getAbsPoint().z, i, 0.0F, 0.0F));
                super.setTimer(500);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public SLDUnit() {
            this.Timer1 = this.Timer2 = 0.1F;
        }
    }

    public static class NGS1Unit extends CandCGeneric {

        public boolean danger() {
            if (!this.hadtarget) {
                this.actor = Selector.getTarget();
                if ((this.actor != null) && !(this.actor instanceof Aircraft)) {
                    this.hadtarget = true;
                    this.counter = 0;
                }
            }
            if (this.hadtarget) {
                if (this.counter >= 50) {
                    String s = "weapon.bomb_std";
//                    Point3d point3d = new Point3d();
                    this.actor.pos.getAbs(this.point3d);
                    // Random random = new Random();
                    int i = TrueRandom.nextInt(400);
                    int j = i - 200;
                    this.point3d.x += j;
                    i = TrueRandom.nextInt(400);
                    j = i - 200;
                    this.point3d.y += j;
                    float f = 25F;
                    float f1 = 136F;
                    i = TrueRandom.nextInt(100);
                    if (i > 50) {
                        f = 50F;
                        f1 = 210F;
                    }
                    Explosions.generate(this.actor, this.point3d, f, 0, f1, !Mission.isNet());
                    MsgExplosion.send(this.actor, s, this.point3d, this.getOwner(), 0.0F, f, 0, f1);
                }
                if ((this.counter >= 43) && (this.counter < 45)) {
                    HUD.logCenter("                                                                             Splash!");
                } else if ((this.counter > 21) && (this.counter < 25)) {
                    HUD.logCenter("                                                                             Rounds Complete.");
                } else if ((this.counter > 15) && (this.counter < 25)) {
                    HUD.logCenter("                                                                             Firing.");
                } else if ((this.counter > 5) && (this.counter < 10)) {
                    HUD.logCenter("                                                                             Target Received.");
                }
                this.counter++;
            }
            if (this.counter > 70) {
                this.hadtarget = false;
                this.counter = 0;
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     counter;
        private boolean hadtarget;
        Actor           actor;

        public NGS1Unit() {
            this.counter = 0;
            this.hadtarget = false;
            this.actor = null;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class NGSUnit extends CandCGeneric {

        public boolean danger() {
            if (!this.hadtarget) {
                this.actor = Selector.getTarget();
                if ((this.actor != null) && !(this.actor instanceof Aircraft)) {
                    this.hadtarget = true;
                    this.counter = 0;
                }
            }
            if (this.hadtarget) {
                if (this.counter >= 50) {
                    String s = "weapon.bomb_std";
//                    Point3d point3d = new Point3d();
                    this.actor.pos.getAbs(this.point3d);
                    // Random random = new Random();
                    int i = TrueRandom.nextInt(200);
                    int j = i - 100;
                    this.point3d.x += j;
                    i = TrueRandom.nextInt(200);
                    j = i - 100;
                    this.point3d.y += j;
                    Explosions.generate(this.actor, this.point3d, 7F, 0, 30F, !Mission.isNet());
                    MsgExplosion.send(this.actor, s, this.point3d, this.getOwner(), 0.0F, 7F, 0, 30F);
                }
                if ((this.counter >= 43) && (this.counter < 45)) {
                    HUD.logCenter("                                                                             Splash!" + s1);
                } else if ((this.counter > 21) && (this.counter < 25)) {
                    HUD.logCenter("                                                                             Rounds Complete.");
                } else if ((this.counter > 15) && (this.counter < 25)) {
                    HUD.logCenter("                                                                             Firing.");
                } else if ((this.counter > 5) && (this.counter < 10)) {
                    HUD.logCenter("                                                                             Target Received.");
                }
                this.counter++;
            }
            if (this.counter > 70) {
                this.hadtarget = false;
                this.counter = 0;
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d       point3d = new Point3d();

        private int           counter;
        private boolean       hadtarget;
        Actor                 actor;
        private static String s1      = null;

        public NGSUnit() {
            this.counter = 0;
            this.hadtarget = false;
            this.actor = null;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class BombUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
//            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(this, 10000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
            Aircraft aircraft = GetNearestEnemy(this, 10000F, 9);
            if (this.counter > 10) {
                this.counter = 0;
                // TODO: Fix by SAS~Storebror: Strip that crappy double type casting and work with doubles where they're due!
//                startpoint.set(point3d.x + (double)(TrueRandom.nextInt(1000) - 500), point3d.y + (double)(TrueRandom.nextInt(1000) - 500), point3d.z);
                startpoint.set(this.point3d.x + TrueRandom.nextDouble(-500D, 500D), this.point3d.y + TrueRandom.nextDouble(-500D, 500D), this.point3d.z);
            }
            if ((aircraft != null) && (aircraft instanceof TypeBomber) && (aircraft.getArmy() != this.myArmy)) {
//                World.MaxVisualDistance = 50000F; // TODO: Fixed by SAS~Storebror: WTF???!??
                this.counter++;
                String s = "weapon.bomb_std";
                startpoint.x += TrueRandom.nextInt(40) - 20;
                startpoint.y += TrueRandom.nextInt(40) - 20;
                Explosions.generate(this, startpoint, 7F, 0, 30F, !Mission.isNet());
                startpoint.z = World.land().HQ(startpoint.x, startpoint.y);
                MsgExplosion.send(this, s, startpoint, this.getOwner(), 0.0F, 7F, 0, 30F);
                Engine.land();
                int i = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(startpoint.x), Engine.land().WORLD2PIXY(startpoint.y));
                if ((this.firecounter < 100) && (i >= 16) && (i < 20)) {
                    Eff3DActor.New(null, null, new Loc(startpoint.x, startpoint.y, startpoint.z + 5D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/CityFire3.eff", 300F);
                    this.firecounter++;
                }
                super.setTimer(15);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d        point3d    = new Point3d();

        private static Point3d startpoint = new Point3d();
        private int            counter;
        private int            firecounter;

        public BombUnit() {
            this.counter = 11;
            this.firecounter = 0;
            this.Timer1 = this.Timer2 = 0.05F;
        }
    }

    public static class BRGUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if (aircraft.pos.getAbsPoint().distance(this.point3d) < 10000D) {
                String s = "weapon.bomb_std";
                // Random random = new Random();
                int i = TrueRandom.nextInt(200);
                int j = i - 100;
                this.point3d.x += j;
                i = TrueRandom.nextInt(200);
                j = i - 100;
                this.point3d.y += j;
                i = TrueRandom.nextInt(5);
                this.point3d.z += i;
                Explosions.generate(this, this.point3d, 7F, 0, 30F, !Mission.isNet());
                MsgExplosion.send(this, s, this.point3d, this.getOwner(), 0.0F, 7F, 0, 30F);
                super.setTimer(15);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public BRGUnit() {
            this.Timer1 = this.Timer2 = 0.1F;
        }
    }

    public static class BoxUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Orient orient = new Orient();
            this.pos.getAbs(this.point3d, orient);
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if ((actor.pos.getAbsPoint().distance(this.point3d) < 10000D) && (actor.getArmy() != this.myArmy)) {
                    String s = "weapon.bomb_std";
                    // Random random = new Random();
                    // TODO: Fix by SAS~Storebror: Strip that crappy double type casting and work with doubles where they're due!
//                    int k = TrueRandom.nextInt(1000);
//                    int i1 = k - 500;
//                    point3d.x += i1;
//                    k = TrueRandom.nextInt(1000);
//                    i1 = k - 500;
//                    point3d.y += i1;
//                    k = TrueRandom.nextInt(300);
//                    i1 = k - 150;
//                    point3d.z += actor.pos.getAbsPoint().z + (double)i1;
                    this.point3d.x += TrueRandom.nextDouble(-500D, 500D);
                    this.point3d.y += TrueRandom.nextDouble(-500D, 500D);
                    this.point3d.z += actor.pos.getAbsPoint().z + TrueRandom.nextDouble(-150D, 150D);

                    Explosions.AirFlak(this.point3d, 1);
                    MsgExplosion.send(this, s, this.point3d, this.getOwner(), 0.0F, 0.9F, 0, 30F);
                    l.set(this.point3d, orient);
                    Eff3DActor eff3dactor = Eff3DActor.New(l, 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Burn.eff", 1.0F);
                    eff3dactor.postDestroy(Time.current() + 1500L);
                    LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                    lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                    lightpointactor.light.setEmit(1.0F, 300F);
                    eff3dactor.draw.lightMap().put("light", lightpointactor);
                    super.setTimer(15);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d    point3d = new Point3d();

//        private int counter;
//        private boolean hadtarget;
        private static Loc l       = new Loc();

        public BoxUnit() {
//            counter = 0;
//            hadtarget = false;
            this.Timer1 = this.Timer2 = 0.1F;
        }
    }

    public static class WindowUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof TypeBomber) && (actor.getArmy() == this.myArmy) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    int i = (int) (-(actor.pos.getAbsOrient().getYaw() - 90D));
                    if (i < 0) {
                        i = 360 + i;
                    }
//                    boolean flag1 = false;
//                    Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(actor, 4000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                    Aircraft aircraft = GetNearestEnemy(actor, 4000F, 9);
                    if (aircraft == null) {
                        continue; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
                    }
                    if ((aircraft instanceof TypeFighter) && (aircraft.getSpeed(this.vector3d) > 20D)) {
                        if (aircraft == World.getPlayerAircraft()) {
                            flag = true;
                        }
                        double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                        double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                        double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                        // new String();
                        double d6 = (int) Math.ceil(d2 - d5);
                        // new String();
                        double d7 = d3 - d;
                        double d8 = d4 - d1;
                        float f = 57.32484F * (float) Math.atan2(d8, -d7);
                        int j = (int) (Math.floor((int) f) - 90D);
                        if (j < 0) {
                            j = 360 + j;
                        }
                        int k = j - i;
                        if (k < 0) {
                            k = 360 + k;
                        }
                        boolean flag2 = false;
                        if ((k >= 90) && (k <= 270)) {
                            flag2 = true;
                        }
                        double d9 = d - d3;
                        double d10 = d1 - d4;
                        boolean flag3 = false;
                        boolean flag4 = false;
                        int l = 0;
                        if (Mission.cur() != null) {
                            l = Mission.cur().sectFile().get("Mods", "WindowEffect", 0);
                        }
                        if (l > 100) {
                            l = 100;
                        }
                        if (l < 0) {
                            l = 0;
                        }
                        // Random random = new Random();
                        int i1 = TrueRandom.nextInt(100);
                        if (i1 <= (3 + l)) {
                            flag3 = true;
                        }
                        if (i1 <= (11 + l)) {
                            flag4 = true;
                        }
                        int j1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                        if ((j1 <= 4000) && (Math.abs(d6) <= 500D) && flag2 && flag3 && !flag) {
                            ((Pilot) aircraft.FM).set_maneuver(14);
                        }
                        if ((j1 <= 4000) && (Math.abs(d6) <= 500D) && flag2 && flag4 && flag) {
                            HUD.logCenter("                                          RO: We're being jammed!");
                        }
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public WindowUnit() {
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class VectorUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            char c = '\u03E8';
//            Vector3d vector3d = new Vector3d();
            int i = 1000;
            int j = 1000;
            // Random random = new Random();
            int k = TrueRandom.nextInt(140);
            int l = k + 10;
//            Object obj = null;
            if (Mission.cur() != null) {
                this.maxrange = Mission.cur().sectFile().get("Mods", "VectorRange", 100) * 1000;
            }
            for (; i <= this.maxrange; i += 1000) {
                List list = Engine.targets();
                int i1 = list.size();
                for (int j1 = 0; j1 < i1; j1++) {
                    Actor actor = (Actor) list.get(j1);
                    if ((actor instanceof Aircraft) && (Mission.cur() != null) && (((Mission.cur().sectFile().get("Mods", "VectorVarAlt", 0) == 1) && (Config.cur.ini.get("Mods", "VectorVarAlt", 0) != 1)) || (Mission.cur().sectFile().get("Mods", "VectorVarAlt", 0) == 1))) {
                        this.maxheight = (actor.pos.getAbsPoint().distance(this.point3d) / this.maxrange) * 8000D;
                        if (this.maxheight < l) {
                            this.maxheight = l;
                        }
                    }
                    if (((Config.cur.ini.get("Mods", "VectorMode", 0) == 1) && (Mission.cur().sectFile().get("Mods", "VectorMode", 0) != 1)) || (Mission.cur().sectFile().get("Mods", "VectorMode", 0) == 1)) {
                        this.bombers = true;
                    }
                    if (((this.bombers && (actor instanceof TypeBomber)) || (!this.bombers && (actor instanceof Aircraft))) && Actor.isAlive(actor) && (actor.getArmy() != this.myArmy) && (actor.pos.getAbsPoint().distance(this.point3d) <= i) && (actor.pos.getAbsPoint().z >= this.maxheight)) {
                        while (j <= this.maxrange) {
//                            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(actor, j, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                            Aircraft aircraft = GetNearestEnemy(actor, j, 9);
//                            j = (int)((float)j + 1000F); // Fixed by SAS~Storebror: What the... ???
                            j += 1000;
                            if ((aircraft != null) && (aircraft instanceof TypeFighter) && (aircraft.pos.getAbsPoint().distance(this.point3d) <= this.maxrange)) {
                                if ((World.Sun().ToSun.z < 0.0F) && (aircraft.FM.EI.getNum() > 1)) {
                                    //                                    VisCheck.hasVector(aircraft, myArmy); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                                    HasVector(aircraft, this.myArmy);
                                }
                                AirGroup airgroup = ((Maneuver) ((Aircraft) actor).FM).Group;
                                Pilot pilot = (Pilot) aircraft.FM;
                                if (airgroup.grTask == 1) {
                                    pilot.targetAll();
                                    if (pilot.Group != null) { // TODO: Fixed by SAS~Storebror, added null check!
                                        pilot.Group.targetGroup = airgroup;
                                        pilot.Group.setGroupTask(3);
                                    }
                                }
                            }
                        }
                    }
                }

            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     maxrange;
        private double  maxheight;
        private boolean bombers;

        public VectorUnit() {
            this.maxrange = 0x186a0;
            this.maxheight = 150D;
            this.bombers = false;
            this.Timer1 = this.Timer2 = 300F;
        }
    }

    public static class TakiUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft())) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
                    int i2 = l1 - j;
                    k1 = (int) (k1 / 1000D);
                    int j2 = (int) Math.ceil(k1);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if (actor instanceof ShipGeneric) {
                        byte0 = 40;
                    }
                    if (actor instanceof BigshipGeneric) {
                        byte0 = 60;
                    }
                    if ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) {
                        byte0 = 5;
                    }
                    if ((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf)) {
                        byte0 = 15;
                    }
                    if ((k1 <= byte0) && (i2 >= 210) && (i2 <= 270) && (Math.sqrt(j1 * j1) <= 20D)) {
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " km");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public TakiUnit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class StormFrontUnit extends CandCGeneric {

        public boolean danger() {
            if (Time.current() < (Mission.cur().sectFile().get("Mods", "StormFrontDelay", 0) * 60)) {
                return false;
            }
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
            Aircraft aircraft = World.getPlayerAircraft();
            if (aircraft.pos.getAbsPoint().distance(this.point3d) > 0.0D) {
                if (aircraft.pos.getAbsPoint().distance(this.point3d) > 250000D) {
                    this.front = 1;
                } else if (aircraft.pos.getAbsPoint().distance(this.point3d) > 250000D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    this.front = 2;
                } else if (aircraft.pos.getAbsPoint().distance(this.point3d) > 200000D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    this.front = 3;
                } else if (aircraft.pos.getAbsPoint().distance(this.point3d) > 150000D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    this.front = 4;
                } else if (aircraft.pos.getAbsPoint().distance(this.point3d) > 100000D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    this.front = 5;
                } else if (aircraft.pos.getAbsPoint().distance(this.point3d) > 50000D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    this.front = 6;
                } else {
                    this.front = 7;
                }
                int i = Mission.cur().sectFile().get("Mods", "WorstClouds", 7);
                if (this.front > i) {
                    this.front = i;
                }
            }
            if (this.front != this.lastfront) {
                Mission.createClouds(this.front, 1000F);
                World.land().cubeFullUpdate();
            }
            this.lastfront = this.front;
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     front;
        private int     lastfront;

        public StormFrontUnit() {
            this.front = 1;
            this.lastfront = 1;
            this.Timer1 = this.Timer2 = 300F;
        }
    }

    public static class SPRUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            Pilot pilot = (Pilot) aircraft.FM;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((aircraft.getArmy() == this.myArmy) && ((pilot.Wingman != null) || (pilot.crew > 1)) && Actor.isAlive(actor) && ((actor instanceof TankGeneric) || (actor instanceof Wagon) || (actor instanceof ArtilleryGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric) || (actor instanceof CarGeneric)) && (actor.getArmy() != this.myArmy)) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    double d6 = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    double d7 = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    // new String();
                    double d8 = (d2 - d5) * 2D;
                    if (d8 > 6000D) {
                        d8 = 6000D;
                    }
                    float f = World.getTimeofDay();
                    boolean flag1 = false;
                    if (((f >= 0.0F) && (f <= 5F)) || ((f >= 21F) && (f <= 24F))) {
                        flag1 = true;
                    }
//                    // Random random = new Random();
//                    int j = TrueRandom.nextInt(100);
                    if (flag1) {
                        d8 = 1500D - (d2 - d5);
                    }
                    String s = "units";
                    if (actor instanceof TankGeneric) {
                        s = "armor";
                    }
                    if (actor instanceof ArtilleryGeneric) {
                        s = "guns";
                    }
                    if (actor instanceof CarGeneric) {
                        s = "vehicles";
                    }
                    if (actor instanceof Wagon) {
                        s = "train";
                    }
                    if ((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) {
                        s = "ship";
                        d8 *= 2D;
                    }
                    double d9 = d3 - d;
                    double d10 = d4 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d10, -d9);
                    double d11 = Math.floor((int) f1) - 90D;
                    if (d11 < 0.0D) {
                        d11 = 360D + d11;
                    }
                    int k = (int) (d11 - i);
                    if (k < 0) {
                        k = 360 + k;
                    }
                    int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
                    if (l < 1) {
                        l = 12;
                    }
                    double d12 = d - d3;
                    double d13 = d1 - d4;
                    double d14 = Math.ceil(Math.sqrt((d13 * d13) + (d12 * d12)));
                    if (d14 <= d8) {
                        HUD.logCenter("                                          Enemy " + s + " spotted at " + l + " o'clock!");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public SPRUnit() {
            this.Timer1 = this.Timer2 = 30F;
        }
    }

    public static class SpawnUnit extends CandCGeneric {

        public boolean danger() {
            this.count++;
            if (this.count > 100) {
                for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                    Actor actor = (Actor) entry.getValue();
                    if (actor instanceof Stationary.Motorcycle) {
                        //                        ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor.postDestroy();
                    }
                }

//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
//            boolean flag1 = false;
            // Random random = new Random();
//            boolean flag2 = false;
            List list = Engine.targets();
            int j = list.size();
            while (!flag) {
                int i = TrueRandom.nextInt(j);
                Actor actor1 = (Actor) list.get(i);
                if ((actor1 instanceof Stationary.Motorcycle) && (actor1.getArmy() == this.myArmy)) {
                    int k = 0;
                    Engine.land();
                    int l = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor1.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor1.pos.getAbsPoint().y));
                    for (; (k <= j) && !flag; k++) {
                        Actor actor2 = (Actor) list.get(k);
                        int i1 = TrueRandom.nextInt(100);
                        boolean flag3 = false;
                        if (((actor2 instanceof ShipGeneric) || (actor2 instanceof BigshipGeneric)) && (l >= 28) && (l <= 32)) {
                            flag3 = true;
                        }
                        if (((actor2 instanceof StationaryGeneric) || (actor2 instanceof ArtilleryGeneric)) && ((l < 28) || (l > 32))) {
                            flag3 = true;
                        }
                        if ((i1 < 33) && (actor2.pos.getAbsPoint().distance(this.point3d) < 500D) && flag3 && (actor2.getArmy() == this.myArmy)) {
                            Point3d point3d1 = new Point3d();
                            actor1.pos.getAbs(point3d1);
                            int j1 = TrueRandom.nextInt(1000) - 500;
                            point3d1.x += j1;
                            j1 = TrueRandom.nextInt(1000) - 500;
                            point3d1.y += j1;
                            j1 = TrueRandom.nextInt(30) - 15;
                            CandCGeneric.o.setYPR(actor1.pos.getAbsOrient().getYaw() + j1, 0.0F, 0.0F);
                            Engine.land();
                            int k1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(point3d1.x), Engine.land().WORLD2PIXY(point3d1.y));
                            if ((l == k1) && flag3) {
                                actor2.pos.setRel(point3d1, CandCGeneric.o);
                                actor2.pos = new ActorPosMove(actor2.pos);
                                actor2.collide(true);
                                ((ActorAlign) actor2).align();
                                flag = true;
                            }
                        }
                    }

                }
                i++;
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     count;

        public SpawnUnit() {
            this.count = 0;
            this.Timer1 = this.Timer2 = this.delay = 1.0F;
        }
    }

    public static class AmbushUnit extends CandCGeneric {

        public boolean danger() {
            if (this.counter > 50) {
                //                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
            AmbushUnit ambushunit = this;
            Actor actor = War.GetNearestEnemy(ambushunit, ambushunit.getArmy(), 200F);
            // Random random = new Random();
            List list = Engine.targets();
            int i = 0;
            for (int j = list.size(); (actor != null) && ((actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && ((this.counter <= 50) || (i <= j)); i++) {
                Actor actor1 = (Actor) list.get(i);
                if ((actor1 instanceof ArtilleryGeneric) && (actor1.pos.getAbsPoint().distance(this.point3d) > 5000D) && (actor1.getArmy() == ambushunit.getArmy())) {
                    Point3d point3d1 = new Point3d();
                    point3d1 = this.point3d;
                    int k = TrueRandom.nextInt(200) - 100;
                    point3d1.x += k;
                    k = TrueRandom.nextInt(200) - 100;
                    point3d1.y += k;
                    actor1.pos.setRel(point3d1, CandCGeneric.o);
                    actor1.pos = new ActorPosMove(actor1.pos);
                    actor1.collide(true);
                    ((ActorAlign) actor1).align();
                    this.counter++;
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     counter;

        public AmbushUnit() {
            this.counter = 0;
            this.Timer1 = this.Timer2 = this.delay = 5F;
        }
    }

    public static class SN2Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    float f3 = 4000F;
                    float f4 = f3;
                    if ((d3 < (1.25F * f3)) && !flag1) {
                        f4 = (float) d3 * 0.8F;
                    }
                    if ((d3 < (1.25F * f3)) && flag1) {
                        if (d3 <= (1.25F * f3 * 0.5F)) {
                            f4 = (float) (d3 * 0.8D * 2D);
                        } else {
                            f4 = f3;
                        }
                    }
                    int i2 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f2) / 10D) * 10D);
                    if (i2 > f3) {
                        i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                    }
                    float f5 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil(i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if (j3 < 0) {
                        j3 += 360;
                    }
                    float f6 = (float) (f4 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * (f4 * 0.25D)));
                    int k3 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    if (j3 < 5) {
                        s1 = "Dead ahead, ";
                    } else if ((j3 >= 5) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 5, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 10, ";
                    } else if ((j3 > 12) && (j3 < 18)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 15, ";
                    } else if ((j3 > 17) && (j3 <= 25)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 20, ";
                    } else if ((j3 > 25) && (j3 <= 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 30, ";
                    } else if ((j3 > 35) && (j3 <= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 40, ";
                    } else if ((j3 > 45) && (j3 <= 60)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Turn right, ";
                    } else if (j3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Dead ahead, ";
                    } else if ((j3 <= 355) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 5, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 10, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 15, ";
                    } else if ((j3 < 343) && (j3 >= 335)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 20, ";
                    } else if ((j3 < 335) && (j3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 30, ";
                    } else if ((j3 < 325) && (j3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 40, ";
                    } else if ((j3 < 345) && (j3 >= 300)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Turn left, ";
                    }
                    String s2 = "  ";
                    if (k2 < -10) {
                        s2 = "nose down";
                    } else if ((k2 >= -10) && (k2 <= -5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "down a bit";
                    } else if ((k2 > -5) && (k2 < 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "level";
                    } else if ((k2 <= 10) && (k2 >= 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "up a bit";
                    } else if (k2 > 10) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "pull up";
                    }
                    String s3 = "  ";
                    if (j3 < 5) {
                        s3 = "dead ahead, ";
                    } else if ((j3 >= 5) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 5\260, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 10\260, ";
                    } else if ((j3 > 12) && (j3 < 18)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 15\260, ";
                    } else if ((j3 > 17) && (j3 <= 25)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 20\260, ";
                    } else if ((j3 > 25) && (j3 <= 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 30\260, ";
                    } else if ((j3 > 35) && (j3 <= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 40\260, ";
                    } else if ((j3 > 45) && (j3 <= 60)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "off our right, ";
                    } else if (j3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "dead ahead, ";
                    } else if ((j3 <= 355) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 5\260, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 10\260, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 15\260, ";
                    } else if ((j3 < 343) && (j3 >= 335)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 20\260, ";
                    } else if ((j3 < 335) && (j3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 30\260, ";
                    } else if ((j3 < 325) && (j3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 40\260, ";
                    } else if ((j3 < 345) && (j3 >= 300)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "off our left, ";
                    }
                    if ((i2 <= k3) && (i2 > 1500) && (k2 >= -50) && (k2 <= 50) && (Math.abs(i3) <= 60)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: Contact " + s3 + s + ", " + l2 + "m");
                        this.freq = 6F;
                    } else if ((i2 <= k3) && (i2 <= 1500) && (i2 >= 500) && (k2 >= -50) && (k2 <= 50) && (Math.abs(i3) <= 60)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: " + s1 + s2 + ", " + l2 + "m");
                        this.freq = 4F;
                    } else {
                        this.freq = 6F;
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public SN2Unit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class SN2bUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    float f3 = 4000F;
                    float f4 = f3;
                    if ((d3 < (1.25F * f3)) && !flag1) {
                        f4 = (float) d3 * 0.8F;
                    }
                    if ((d3 < (1.25F * f3)) && flag1) {
                        if (d3 <= (1.25F * f3 * 0.5F)) {
                            f4 = (float) (d3 * 0.8D * 2D);
                        } else {
                            f4 = f3;
                        }
                    }
                    int i2 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f2) / 10D) * 10D);
                    if (i2 > f3) {
                        i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                    }
                    float f5 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil(i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if (j3 < 0) {
                        j3 += 360;
                    }
                    float f6 = (float) (f4 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * (f4 * 0.25D)));
                    int k3 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    if (j3 < 5) {
                        s1 = "Dead ahead, ";
                    } else if ((j3 > 4) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 5, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 10, ";
                    } else if ((j3 > 12) && (j3 < 18)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 15, ";
                    } else if ((j3 > 17) && (j3 < 26)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 20, ";
                    } else if ((j3 > 25) && (j3 < 36)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 30, ";
                    } else if ((j3 > 35) && (j3 < 46)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 40, ";
                    } else if ((j3 > 45) && (j3 < 61)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Turn right, ";
                    } else if (j3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Dead ahead, ";
                    } else if ((j3 < 356) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 5, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 10, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 15, ";
                    } else if ((j3 < 343) && (j3 > 334)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 20, ";
                    } else if ((j3 < 335) && (j3 > 324)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 30, ";
                    } else if ((j3 < 325) && (j3 > 314)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 40, ";
                    } else if ((j3 < 345) && (j3 > 299)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Turn left, ";
                    }
                    String s2 = "  ";
                    if (k2 < -10) {
                        s2 = "nose down";
                    } else if ((k2 >= -10) && (k2 <= -5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "down a bit";
                    } else if ((k2 > -5) && (k2 < 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "level";
                    } else if ((k2 <= 10) && (k2 >= 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "up a bit";
                    } else if (k2 > 10) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "pull up";
                    }
                    String s3 = "  ";
                    if (j3 < 5) {
                        s3 = "dead ahead, ";
                    } else if ((j3 > 4) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 5\260, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 10\260, ";
                    } else if ((j3 > 12) && (j3 < 18)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 15\260, ";
                    } else if ((j3 > 17) && (j3 < 26)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 20\260, ";
                    } else if ((j3 > 25) && (j3 < 36)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 30\260, ";
                    } else if ((j3 > 35) && (j3 < 46)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 40\260, ";
                    } else if ((j3 > 45) && (j3 < 61)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "off our right, ";
                    } else if (j3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "dead ahead, ";
                    } else if ((j3 < 356) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 5\260, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 10\260, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 15\260, ";
                    } else if ((j3 < 343) && (j3 > 334)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 20\260, ";
                    } else if ((j3 < 335) && (j3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 30\260, ";
                    } else if ((j3 < 325) && (j3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 40\260, ";
                    } else if ((j3 < 345) && (j3 >= 300)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "off our left, ";
                    }
                    if ((i2 <= k3) && (i2 > 1500) && (k2 >= -50) && (k2 <= 50) && (Math.abs(i3) <= 60)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: Contact " + s3 + s + ", " + l2 + "m");
                        this.freq = 6F;
                    } else if ((i2 <= k3) && (i2 <= 1500) && (i2 >= 200) && (k2 >= -50) && (k2 <= 50) && (Math.abs(i3) <= 60)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: " + s1 + s2 + ", " + l2 + "m");
                        this.freq = 4F;
                    } else {
                        this.freq = 6F;
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public SN2bUnit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class SerrateUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof TypeFighter) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    Aircraft aircraft = World.getPlayerAircraft();
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    int i = (int) (-(actor.pos.getAbsOrient().getYaw() - 90D));
                    if (i < 0) {
                        i = 360 + i;
                    }
                    int j = (int) (-(actor.pos.getAbsOrient().getPitch() - 90D));
                    if (j < 0) {
                        j = 360 + j;
                    }
                    // new String();
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
//                    double d9 = d - d3;
//                    double d10 = d1 - d4;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    float f1 = 57.32484F * (float) Math.atan2(d8, -d7);
                    int k = (int) (Math.floor((int) f) - 90D);
                    if (k < 0) {
                        k = 360 + k;
                    }
                    int l = (int) (Math.floor((int) f1) + 90D);
                    if (l < 0) {
                        l = 360 + l;
                    }
                    int i1 = k - i;
                    double d11 = d - d3;
                    double d12 = d1 - d4;
                    double d13 = Math.sqrt(d6 * d6);
                    int j1 = (int) (Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D);
                    float f2 = 57.32484F * (float) Math.atan2(j1, d13);
                    int k1 = (int) (Math.floor((int) f2) - 90D);
                    if (k1 < 0) {
                        k1 = 360 + k1;
                    }
                    int l1 = k1 - j;
                    int i2 = 16000;
                    if ((l1 >= 220) && (l1 <= 320) && (((i1 <= 60) && (i1 >= 0)) || ((i1 >= 300) && (i1 <= 360)))) {
                        i2 = 0x13880;
                    }
                    if (j1 <= i2) {
                        HUD.logCenter("                                         Serrate: Target bearing " + l + "\260");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public SerrateUnit() {
            this.Timer1 = this.Timer2 = this.delay = 15F;
        }
    }

    public static class SARUnit extends CandCGeneric {

        public boolean danger() {
            if (Time.current() < (Mission.cur().sectFile().get("Mods", "SARDelay", 0) * 60)) {
                return false;
            }
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            float f = World.getTimeofDay();
            String s = "Effects/Smokes/Yellowsmoke.eff";
            if (((f >= 0.0F) && (f <= 5F)) || ((f >= 21F) && (f <= 24F))) {
                s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
            }
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                this.pos.getAbs(this.point3d);
                if ((actor instanceof Aircraft) && (actor.pos.getAbsPoint().distance(this.point3d) <= 1000D) && (actor.getArmy() == this.myArmy)) {
                    if (!this.popped) {
                        Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, s, -1F);
                        this.popped = true;
                    }
                    if ((actor instanceof Aircraft) && (actor.pos.getAbsPoint().distance(this.point3d) <= 300D) && (actor.getSpeed(this.vector3d) < 10D)) {
                        HUD.logCenter("                                                                             We got him! Heading home!");
                        this.pickup = true;
                        World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                        destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        this.postDestroy();
                    }
                }
                if (!this.pickup) {
                    boolean flag = false;
                    Aircraft aircraft1 = World.getPlayerAircraft();
                    if ((aircraft1.pos.getAbsPoint().distance(this.point3d) < 3000D) && (aircraft1.getArmy() == this.myArmy)) {
                        flag = true;
                    }
                    this.pos.getAbs(this.point3d);
                    double d2 = (Main3D.cur3D().land2D.worldOfsX() + this.point3d.x) / 10000D;
                    double d3 = (Main3D.cur3D().land2D.worldOfsY() + this.point3d.y) / 10000D;
                    double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
//                    double d5 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x / 10000D;
//                    double d6 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y / 10000D;
                    char c = (char) (int) (65D + Math.floor(((d2 / 676D) - Math.floor(d2 / 676D)) * 26D));
                    char c1 = (char) (int) (65D + Math.floor(((d2 / 26D) - Math.floor(d2 / 26D)) * 26D));
                    double d7 = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                    double d8 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                    // new String();
                    String s1;
                    if (d4 > 260D) {
                        s1 = "" + c + c1;
                    } else {
                        s1 = "" + c1;
                    }
                    double d9 = d7 - d;
                    double d10 = d8 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d10, -d9);
                    double d11 = Math.floor((int) f1) - 90D;
                    if (d11 < 0.0D) {
                        d11 = 360D + d11;
                    }
                    int j = (int) (d11 - i);
                    if (j < 0) {
                        j = 360 + j;
                    }
                    int k = (int) (Math.ceil((j + 15) / 30D) - 1.0D);
                    if (k == 0) {
                        k = 12;
                    }
                    // new String();
                    int l = (int) Math.ceil(d3);
                    if (flag && (aircraft1.pos.getAbsPoint().distance(this.point3d) < 2000D) && (aircraft1.getArmy() == this.myArmy)) {
                        HUD.logCenter("                                                                             Man in the water! " + k + " o'clock!");
                    } else if (!flag && (aircraft1.getArmy() == this.myArmy)) {
                        HUD.logCenter("                                                                             SAR required at map grid " + s1 + "-" + l);
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private boolean  pickup;
        private boolean  popped;
//        private float delay;

        public SARUnit() {
            this.pickup = false;
            this.popped = false;
            this.delay = 0.0F;
            this.Timer1 = this.Timer2 = 15F;
            this.setMesh("3do/humans/Paratroopers/Water/US_Dinghy/live.sim");
        }
    }

    public static class RESCAPUnit extends CandCGeneric {

        public boolean danger() {
            if (Time.current() < (Mission.cur().sectFile().get("Mods", "RESCAPDelay", 0) * 60)) {
                return false;
            }
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            float f = World.getTimeofDay();
            String s = "Effects/Smokes/Yellowsmoke.eff";
            if (((f >= 0.0F) && (f <= 5F)) || ((f >= 21F) && (f <= 24F))) {
                s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
            }
            Actor actor = War.GetNearestEnemy(this, this.getArmy(), 100F);
            Aircraft aircraft = World.getPlayerAircraft();
            if ((actor != null) && (aircraft != null) && (aircraft.getArmy() == this.myArmy)) {
                if (actor.pos.getAbsPoint().distance(this.point3d) > 10D) {
                    HUD.logCenter("                                                                             They're getting close! Hurry!");
                } else {
                    HUD.logCenter("                                                                             They're right on top of me!");
//                    destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    this.postDestroy();
                }
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor1 = (Actor) entry.getValue();
                this.pos.getAbs(this.point3d);
                if ((actor1.getArmy() == this.myArmy) && (actor1 instanceof Aircraft)) {
                    if ((actor1.pos.getAbsPoint().distance(this.point3d) <= 1000D) && !this.popped) {
                        Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, s, -1F);
                        this.popped = true;
                    }
                    if (((actor1 != World.getPlayerAircraft()) && (actor1.pos.getAbsPoint().distance(this.point3d) <= 300D) && (actor1.getSpeed(this.vector3d) <= 27D)) || ((actor1 == World.getPlayerAircraft()) && (actor1.pos.getAbsPoint().distance(this.point3d) <= 30D) && (actor1.getSpeed(this.vector3d) <= 27D))) {
                        HUD.logCenter("                                                                             We got him! Heading home!");
                        this.pickup = true;
                        World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                        destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        this.postDestroy();
                    }
                }
                if (!this.pickup) {
                    boolean flag = false;
                    Aircraft aircraft1 = World.getPlayerAircraft();
                    if (aircraft1.pos.getAbsPoint().distance(this.point3d) < 3000D) {
                        flag = true;
                    }
                    this.pos.getAbs(this.point3d);
                    double d = (Main3D.cur3D().land2D.worldOfsX() + this.point3d.x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + this.point3d.y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + (aircraft1.pos.getAbsPoint().x / 10000D);
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + (aircraft1.pos.getAbsPoint().y / 10000D);
                    char c = (char) (int) (65D + Math.floor(((d / 676D) - Math.floor(d / 676D)) * 26D));
                    char c1 = (char) (int) (65D + Math.floor(((d / 26D) - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if (d2 > 260D) {
                        s1 = "" + c + c1;
                    } else {
                        s1 = "" + c1;
                    }
                    double d5 = d3 - d;
                    double d6 = d4 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d6, d5);
                    int i = (int) f1;
                    i = (i + 180) % 360;
                    // new String();
                    String s2 = "east";
                    if ((i < 316) && (i > 224)) {
                        s2 = "south";
                    } else if ((i < 136) && (i > 44)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "north";
                    } else if ((i < 45) && (i > 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "east";
                    } else if ((i < 225) && (i > 135)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "west";
                    }
                    // new String();
                    double d7 = (int) Math.ceil(d1);
                    if (flag && (aircraft1.getArmy() == this.myArmy)) {
                        HUD.logCenter("                                                                             Got you in sight! I'm to the " + s2 + "!");
                    } else if (!flag && (aircraft1.getArmy() == this.myArmy)) {
                        HUD.logCenter("                                                                             RESCAP required at map grid " + s1 + "-" + (int) d7);
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private boolean  pickup;
        private boolean  popped;
//        private float delay;

        public RESCAPUnit() {
            this.pickup = false;
            this.popped = false;
            this.delay = 0.0F;
            this.Timer1 = this.Timer2 = 15F;
            this.setMesh("3do/Buildings/addobjects/Human_02/live.sim");
        }
    }

    public static class DynamicRescueUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                actor2 = (Actor) entry.getValue();
                if (!this.pilotdown && (actor2 != null) && Actor.isAlive(actor2) && (actor2.getArmy() == this.myArmy) && (actor2 instanceof Paratrooper) && ((actor2.pos.getAbsPoint().z - World.land().HQ(actor2.pos.getAbsPoint().x, actor2.pos.getAbsPoint().y)) < 1.0D)) {
                    o.set(0.0F, 0.0F, 0.0F);
                    this.pos.setRel(actor2.pos.getAbsPoint(), o);
                    this.pos = new ActorPosMove(this.pos);
                    this.collide(false);
                    this.align();
                    Engine.land();
                    int i = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor2.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor2.pos.getAbsPoint().y));
                    if ((i >= 28) && (i < 32)) {
                        this.setMesh("3do/humans/Paratroopers/Water/US_Dinghy/live.sim");
                    } else {
                        this.setMesh("3do/Buildings/addobjects/Human_02/live.sim");
                    }
//                    ObjState.destroy(actor2); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    actor2.postDestroy();
                    this.pilotdown = true;
                    this.callchopper();
                }
            }

            if (!this.pilotdown) {
                return true;
            }
            super.resetTimer(15F);
            this.pos.getAbs(this.point3d);
            float f = World.getTimeofDay();
            String s = "Effects/Smokes/Yellowsmoke.eff";
            if (((f >= 0.0F) && (f <= 5F)) || ((f >= 21F) && (f <= 24F))) {
                s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
            }
            Actor actor = War.GetNearestEnemy(this, this.getArmy(), 100F);
            Aircraft aircraft = World.getPlayerAircraft();
            if ((actor != null) && (aircraft != null) && (aircraft.getArmy() == this.myArmy)) {
                if (actor.pos.getAbsPoint().distance(this.point3d) > 10D) {
                    HUD.logCenter("                                                                             They're getting close! Hurry!");
                } else {
                    HUD.logCenter("                                                                             They're right on top of me!");
//                    destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    this.postDestroy();
                }
            }
            for (Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
                Actor actor1 = (Actor) entry1.getValue();
                this.pos.getAbs(this.point3d);
                if ((actor1.getArmy() == this.myArmy) && (actor1 instanceof Aircraft)) {
                    if ((actor1.pos.getAbsPoint().distance(this.point3d) <= 1000D) && !this.popped) {
                        Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, s, -1F);
                        this.popped = true;
                    }
                    if (((actor1 != World.getPlayerAircraft()) && (actor1.pos.getAbsPoint().distance(this.point3d) <= 200D) && (actor1.getSpeed(this.vector3d) <= 27D)) || ((actor1 == World.getPlayerAircraft()) && (actor1.pos.getAbsPoint().distance(this.point3d) <= 30D) && (actor1.getSpeed(this.vector3d) <= 27D))) {
                        HUD.logCenter("                                                                             We got him! Heading home!");
                        this.pickup = true;
                        World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
                        this.setMesh("3do/primitive/siren/mono.sim");
//                        destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        this.postDestroy();
                    }
                }
                if (!this.pickup) {
                    boolean flag = false;
                    Aircraft aircraft1 = World.getPlayerAircraft();
                    if (aircraft1.pos.getAbsPoint().distance(this.point3d) < 3000D) {
                        flag = true;
                    }
                    this.pos.getAbs(this.point3d);
                    double d = (Main3D.cur3D().land2D.worldOfsX() + this.point3d.x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + this.point3d.y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + (aircraft1.pos.getAbsPoint().x / 10000D);
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + (aircraft1.pos.getAbsPoint().y / 10000D);
                    char c = (char) (int) (65D + Math.floor(((d / 676D) - Math.floor(d / 676D)) * 26D));
                    char c1 = (char) (int) (65D + Math.floor(((d / 26D) - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if (d2 > 260D) {
                        s1 = "" + c + c1;
                    } else {
                        s1 = "" + c1;
                    }
                    double d5 = d3 - d;
                    double d6 = d4 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d6, d5);
                    int j = (int) f1;
                    j = (j + 180) % 360;
                    // new String();
                    String s2 = "east";
                    if ((j < 316) && (j > 224)) {
                        s2 = "south";
                    } else if ((j < 136) && (j > 44)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "north";
                    } else if ((j < 45) && (j > 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "east";
                    } else if ((j < 225) && (j > 135)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "west";
                    }
                    // new String();
                    double d7 = (int) Math.ceil(d1);
                    if (flag && (aircraft1.getArmy() == this.myArmy)) {
                        HUD.logCenter("                                                                             Got you in sight! I'm to the " + s2 + "!");
                    } else if (!flag && (aircraft1.getArmy() == this.myArmy)) {
                        HUD.logCenter("                                                                             RESCAP required at map grid " + s1 + "-" + (int) d7);
                    }
                }
                if (this.inbound) {
                    this.checkchopper();
                }
            }

            return true;
        }

        private void callchopper() {
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                chopper = (Actor) entry.getValue();
//                if(chopper != null && (chopper instanceof HRS3AI) && chopper.pos.getAbsPoint().distance(actor2.pos.getAbsPoint()) < 200000D)
                if ((chopper != null) && isHRS3AI(chopper) && (chopper.pos.getAbsPoint().distance(actor2.pos.getAbsPoint()) < 200000D)) {
                    this.inbound = true;
                    Pilot pilot = (Pilot) ((Aircraft) chopper).FM;
                    Point3d point3d = new Point3d();
                    Point3d point3d2 = new Point3d();
                    actor2.pos.getAbs(point3d);
                    chopper.pos.getAbs(point3d2);
                    Point3d point3d3 = new Point3d();
                    Point3d point3d4 = new Point3d();
                    point3d3.x = point3d.x + ((point3d.x - point3d2.x) / 20D);
                    point3d3.y = point3d.y + ((point3d.y - point3d2.y) / 20D);
                    point3d4.x = point3d.x - ((point3d.x - point3d2.x) / 10D);
                    point3d4.y = point3d.y - ((point3d.y - point3d2.y) / 10D);
                    point3d3.z = point3d.z = 100D;
                    point3d4.z = 300D;
                    WayPoint waypoint = new WayPoint(point3d4);
                    waypoint.set(220F);
                    waypoint.Action = 0;
                    pilot.AP.way.add(waypoint);
                    WayPoint waypoint1 = new WayPoint(point3d);
                    waypoint1.set(220F);
                    waypoint1.Action = 0;
                    pilot.AP.way.add(waypoint1);
                    WayPoint waypoint2 = new WayPoint(point3d3);
                    waypoint2.set(220F);
                    waypoint2.Action = 3;
                    pilot.AP.way.add(waypoint2);
                    WayPoint waypoint3 = new WayPoint(pilot.AP.way.first().getP());
                    waypoint3.set(220F);
                    waypoint3.Action = 0;
                    pilot.AP.way.add(waypoint3);
                    return;
                }
            }

        }

        private void checkchopper() {
            this.check++;
            if (this.check > 10) {
                if (Actor.isAlive(chopper)) {
                    int i = (int) ((chopper.pos.getAbsPoint().distance(actor2.pos.getAbsPoint()) / 1000D / 220D) * 60D);
                    HUD.logCenter("                                                                             Chopper inbound. ETA " + i + " minutes!");
                    this.check = 0;
                } else {
                    HUD.logCenter("                                                                             Chopper down! Abort rescue!");
//                    destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    this.postDestroy();
                }
            }
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d     vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d      point3d  = new Point3d();

        private static Actor chopper  = null;
        private static Actor actor2   = null;
        private boolean      pickup;
        private boolean      popped;
//        private float delay;
        private boolean      pilotdown;
//        private static Point3d point3d1 = new Point3d();
//        private static Orient o = new Orient();
        private boolean      inbound;
        private int          check;

        public DynamicRescueUnit() {
            this.pickup = false;
            this.popped = false;
            this.delay = 0.0F;
            this.pilotdown = false;
            this.inbound = false;
            this.check = 0;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class RefuelUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            float f = 0.0F;
//            Object obj = null;
            if (Mission.cur() != null) {
                if (Mission.cur().sectFile().get("Mods", "RefuelAll", 0) != 1) {
                    ;
                }
                this.refuelall = true;
                if (Config.cur.ini.get("Mods", "RefuelAlI", 0) != 1) {
                    ;
                }
                this.refuelall = true;
                if (Mission.cur().sectFile().get("Mods", "RefuelPlayer", 0) != 1) {
                    ;
                }
                this.refuelplayer = true;
                if (Config.cur.ini.get("Mods", "RefuelPlayer", 0) != 1) {
                    ;
                }
                this.refuelplayer = true;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor1 = (Actor) entry.getValue();
//                if(((actor1 instanceof KB_29P) || (actor1 instanceof A1H_Tanker)) && actor1.getArmy() == myArmy && actor1.getSpeed(vector3d) > 20D)
                if ((isKB_29P(actor1) || isA1H_Tanker(actor1)) && (actor1.getArmy() == this.myArmy) && (actor1.getSpeed(this.vector3d) > 20D)) {
                    do {
//                        Point3d point3d1 = actor1.pos.getAbsPoint();
                        List list = Engine.targets();
                        int i = list.size();
                        for (int j = 0; j < i; j++) {
                            Actor actor = (Actor) list.get(j);
//                            if(!refuelall && (actor instanceof F84G3) || (actor instanceof FJ_3M) || refuelall && (actor instanceof Aircraft) && actor.getArmy() == myArmy)
                            if ((!this.refuelall && isF84G3(actor)) || isFJ_3M(actor) || (this.refuelall && (actor instanceof Aircraft) && (actor.getArmy() == this.myArmy))) {
                                int k = (int) (-(actor1.pos.getAbsOrient().getYaw() - 90D));
                                if (k < 0) {
                                    k = 360 + k;
                                }
                                double d = Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x;
                                double d1 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y;
                                double d2 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().z;
                                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                                double d6 = d3 - d;
                                double d7 = d4 - d1;
                                double d8 = d - d3;
                                double d9 = d1 - d4;
                                double d10 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                                double d11 = (int) Math.sqrt((actor1.getSpeed(this.vector3d) - actor.getSpeed(this.vector3d)) * (actor1.getSpeed(this.vector3d) - actor.getSpeed(this.vector3d)));
                                int l = (int) Math.ceil(Math.sqrt((d9 * d9) + (d8 * d8)));
                                float f1 = 57.32484F * (float) Math.atan2(d7, -d6);
                                int i1 = (int) (Math.floor((int) f1) - 90D);
                                if (i1 < 0) {
                                    i1 = 360 + i1;
                                }
                                int j1 = i1 - k;
                                if (j1 < 0) {
                                    j1 = 360 + j1;
                                }
                                boolean flag1 = false;
                                if ((j1 >= 150) && (j1 <= 210)) {
                                    flag1 = true;
                                }
                                float f2 = World.getTimeofDay();
//                                boolean flag2 = false;
                                if (((f2 >= 0.0F) && (f2 <= 5F)) || ((f2 >= 21F) && (f2 <= 24F))) {
                                    Pilot pilot = (Pilot) ((Aircraft) actor1).FM;
                                    pilot.AS.setNavLightsState(true);
                                }
                                if (!this.refuelplayer && (actor != World.getPlayerAircraft()) && (l <= f) && (d10 <= 100D) && (d11 <= 30D) && (((Aircraft) actor).FM.M.fuel < (((Aircraft) actor).FM.M.maxFuel - 100F))) {
                                    ((Aircraft) actor).FM.M.fuel += 20F;
                                    if (((Aircraft) actor).FM.M.fuel > ((Aircraft) actor).FM.M.maxFuel) {
                                        ((Aircraft) actor).FM.M.fuel = ((Aircraft) actor).FM.M.maxFuel;
                                    }
                                    flag = true;
                                }
                                if (this.refuelplayer && (actor == World.getPlayerAircraft()) && flag1 && (l <= 100) && ((d2 - d5) < 50D) && (d11 <= 10D) && (((Aircraft) actor).FM.M.fuel < (((Aircraft) actor).FM.M.maxFuel - 100F))) {
                                    ((Aircraft) actor).FM.M.fuel += 20F;
                                    if (((Aircraft) actor).FM.M.fuel > ((Aircraft) actor).FM.M.maxFuel) {
                                        ((Aircraft) actor).FM.M.fuel = ((Aircraft) actor).FM.M.maxFuel;
                                    }
                                    flag = true;
                                }
                            }
                        }

                        f += 10F;
                    } while (!flag && (f <= 300F));
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private boolean  refuelall;
        private boolean  refuelplayer;

        public RefuelUnit() {
            this.refuelall = false;
            this.refuelplayer = false;
            this.Timer1 = this.Timer2 = this.delay = 1.0F;
        }
    }

    public static class RCGCIUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            actor = null;
            if ((mainforce != null) && !((Maneuver) ((Aircraft) mainforce).FM).hasBombs()) {
                actor = mainforce;
                this.mainforcefound = true;
                this.mainforcewrong = false;
            }
            if (this.mainforcewrong && ((Maneuver) ((Aircraft) spoof).FM).hasBombs()) {
                actor = spoof;
                this.mainforcewrong = true;
                this.mainforcefound = false;
            }
            if (this.mainforcewrong && !((Maneuver) ((Aircraft) spoof).FM).hasBombs()) {
                this.spoofed = true;
                this.mainforcewrong = false;
                this.mainforcefound = false;
                actor = null;
            }
            for (this.counter = 0; !this.mainforcefound && !this.mainforcewrong && (actor == null) && (this.counter <= 100);) {
                this.dosearchmainforce();
            }

            if (Mission.cur() != null) {
                this.maxrange = Mission.cur().sectFile().get("Mods", "RCGCIRange", 200) * 1000;
            }
            if ((actor != null) && (actor.pos.getAbsPoint().distance(this.point3d) < this.maxrange) && (actor.getSpeed(this.vector3d) > 20D) && (actor.pos.getAbsPoint().z >= 150D)) {
                double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d1 = (Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x) / 10000D;
                double d2 = (Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y) / 10000D;
//                double d3 = (Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x) / 1000D;
//                double d4 = (Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y) / 1000D;
                char c = (char) (int) (65D + Math.floor(((d1 / 676D) - Math.floor(d1 / 676D)) * 26D));
                char c1 = (char) (int) (65D + Math.floor(((d1 / 26D) - Math.floor(d1 / 26D)) * 26D));
                // new String();
                String s;
                if (d > 260D) {
                    s = "" + c + c1;
                } else {
                    s = "" + c1;
                }
                // new String();
                int i = (int) Math.ceil(d2);
                int j = (int) (-(actor.pos.getAbsOrient().getYaw() - 90D));
                if (j < 0) {
                    j = 360 + j;
                }
                if (this.spoofed) {
                    HUD.logCenter("                                          Mainforce was a spoof! Abort!");
                } else if (this.mainforcefound || this.mainforcewrong) {
                    HUD.logCenter("                                          Confirmed Mainforce at " + s + "-" + i + ", heading " + j + "\260");
                } else {
                    HUD.logCenter("                                          Incoming Raid at " + s + "-" + i + ", heading " + j + "\260");
                }
                this.spoofed = false;
            }
            return true;
        }

        private void dosearchmainforce() {
//            actor = NearestRadarTargets.GetNearestEnemyEcho(World.getPlayerAircraft(), maxrange, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
            actor = GetNearestEnemy(World.getPlayerAircraft(), this.maxrange, 9);
            // Random random = new Random();
//            boolean flag = false;
            this.counter++;
            if ((actor != null) && (actor instanceof TypeBomber) && (((Maneuver) ((Aircraft) actor).FM).Skill == 2) && ((Maneuver) ((Aircraft) actor).FM).hasBombs()) {
                int i = TrueRandom.nextInt(10);
                if (i == 1) {
                    mainforce = actor;
                    this.mainforcefound = true;
                }
            } else if ((actor != null) && (actor instanceof TypeBomber) && (((Maneuver) ((Aircraft) actor).FM).Skill == 3) && ((Maneuver) ((Aircraft) actor).FM).hasBombs()) {
                int j = TrueRandom.nextInt(10);
                if (j == 1) {
                    spoof = actor;
                    this.mainforcewrong = true;
                }
            } else {
                actor = null;
            }
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d     vector3d  = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d      point3d   = new Point3d();

        private boolean      mainforcefound;
        private boolean      mainforcewrong;
        private boolean      spoofed;
        private int          maxrange;
        private static Actor mainforce = null;
        private static Actor spoof     = null;
        private static Actor actor     = null;
        private int          counter;

        public RCGCIUnit() {
            this.mainforcefound = false;
            this.mainforcewrong = false;
            this.spoofed = false;
            this.maxrange = 200000;
            this.counter = 0;
            this.Timer1 = this.Timer2 = 30F;
        }
    }

    public static class RandomWeatherUnit extends CandCGeneric {

        public boolean danger() {
            // Random random = new Random();
            int i = TrueRandom.nextInt(5);
            int j = TrueRandom.nextInt(1500) + 500;
            Mission.createClouds(i, j);
//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        public RandomWeatherUnit() {
            this.Timer1 = this.Timer2 = this.delay = 0.0F;
        }
    }

    public static class RandomVehiclesUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((actor instanceof TankGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric)) && (actor.getArmy() == this.myArmy)) {
                    double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                    if (i <= 1000) {
                        // Random random = new Random();
                        int j = TrueRandom.nextInt(100);
                        if (j >= 50) {
                            //                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                        }
                    }
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public RandomVehiclesUnit() {
            this.Timer1 = this.Timer2 = this.delay = 0.0F;
        }
    }

    public static class RandomVehicles2Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if (!this.sprung && (this.counter < 1)) {
                this.randomInt = TrueRandom.nextInt(100);
            }
            if (this.randomInt > 50) {
                this.sprung = true;
            }
            if (this.sprung) {
                for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                    Actor actor = (Actor) entry.getValue();
                    if (((actor instanceof TankGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric)) && (actor.getArmy() == this.myArmy)) {
                        double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                        if (i <= 1000) {
                            //                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                        }
                    }
                }

            }
            this.counter++;
            if (this.counter > 60) {
                //                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int     counter;
        private int     randomInt;

        public RandomVehicles2Unit() {
            this.sprung = false;
            this.counter = 0;
            this.randomInt = 0;
            this.Timer1 = this.Timer2 = this.delay = 0.0F;
        }
    }

    public static class RandomTrainsUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if (!this.sprung && (this.counter < 1)) {
                this.randomInt = TrueRandom.nextInt(100);
            }
            if (this.randomInt > 50) {
                this.sprung = true;
            }
            if (this.sprung) {
                for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                    Actor actor = (Actor) entry.getValue();
                    if ((actor instanceof Wagon) && (actor.getArmy() == this.myArmy)) {
                        double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                        if (i <= 1000) {
                            //                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                        }
                    }
                }

            }
            this.counter++;
            if (this.counter > 60) {
                //                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int     counter;
        private int     randomInt;

        public RandomTrainsUnit() {
            this.sprung = false;
            this.counter = 0;
            this.randomInt = 0;
            this.Timer1 = this.Timer2 = this.delay = 0.0F;
        }
    }

    public static class RandomTimeUnit extends CandCGeneric {

        public boolean danger() {
            float f = World.getTimeofDay();
            // Random random = new Random();
            int i = TrueRandom.nextInt(6) - 3;
            f += i;
            if (f > 24D) {
                f = (float) (f - 24D);
            }
            if (f < 0.0F) {
                f = (float) (24D + f);
            }
            World.setTimeofDay(f);
            if (Config.isUSE_RENDER()) {
                World.land().cubeFullUpdate();
            }
            if (Mission.cur() != null) {
                Mission.cur().replicateTimeofDay();
            }
//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        public RandomTimeUnit() {
            this.Timer1 = this.Timer2 = this.delay = 0.0F;
        }
    }

    public static class RandomSkillUnit extends CandCGeneric {

        public boolean danger() {
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof TypeFighter) && (actor.getArmy() == this.myArmy) && (((Maneuver) ((Aircraft) actor).FM).Skill == 1)) {
                    byte byte0 = 1;
                    // Random random = new Random();
                    int i = TrueRandom.nextInt(100);
                    if (i <= 20) {
                        byte0 = 0;
                    }
                    if ((i > 20) && (i <= 30)) {
                        byte0 = 2;
                    }
                    if ((i >= 50) && (i <= 55)) {
                        byte0 = 3;
                    }
                    ((Aircraft) actor).FM.setSkill(byte0);
                }
            }

            this.counter++;
            if (this.counter > 60) {
                //                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            return true;
        }

        private int counter;

        public RandomSkillUnit() {
            this.counter = 0;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class RandomShipsUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if (!this.sprung && (this.counter < 1)) {
                this.randomInt = TrueRandom.nextInt(100);
            }
            if (this.randomInt > 50) {
                this.sprung = true;
            }
            if (this.sprung) {
                for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                    Actor actor = (Actor) entry.getValue();
                    if ((actor instanceof BigshipGeneric) && (actor.getArmy() == this.myArmy)) {
                        double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                        if (i <= 500) {
                            //                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                        }
                    }
                }

            }
            this.counter++;
            if (this.counter > 60) {
                //                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int     counter;
        private int     randomInt;

        public RandomShipsUnit() {
            this.sprung = false;
            this.counter = 0;
            this.randomInt = 0;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class RandomShips2Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof BigshipGeneric) && (actor.getArmy() == this.myArmy)) {
                    double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                    if (i <= 1000) {
                        // Random random = new Random();
                        int j = TrueRandom.nextInt(2);
                        if (j != 0) {
                            //                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                        }
                    }
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public RandomShips2Unit() {
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class RandomFlakUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof ArtilleryGeneric) && (actor.getArmy() == this.myArmy)) {
//                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
//                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
//                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
//                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
//                    double d4 = d2 - d;
//                    double d5 = d3 - d1;
//                    int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                    // Random random = new Random();
                    int j = TrueRandom.nextInt(100);
                    if (j >= 50) {
                        //                        ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor.postDestroy();
                    }
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public RandomFlakUnit() {
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class RandomAircraftUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if (!this.sprung && (this.counter < 1)) {
                this.randomInt = TrueRandom.nextInt(100);
            }
            if (this.randomInt > 50) {
                this.sprung = true;
            }
            if (this.sprung) {
                for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                    Actor actor = (Actor) entry.getValue();
                    if ((actor instanceof Aircraft) && (actor.getArmy() == this.myArmy)) {
                        double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                        if (i <= 1000) {
                            //                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                        }
                    }
                }

            }
            this.counter++;
            if (this.counter > 60) {
                //                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int     counter;
        private int     randomInt;
//        private float delay;
//        protected int engineSTimer;

        public RandomAircraftUnit() {
            this.sprung = false;
            this.counter = 0;
            this.randomInt = 0;
            this.delay = 0.0F;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class RandomAircraft2Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() == this.myArmy)) {
                    double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                    if (i <= 1000) {
                        // Random random = new Random();
                        int j = TrueRandom.nextInt(100);
                        if (j >= 50) {
                            //                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                        }
                    }
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

//        private float delay;
//        protected int engineSTimer;

        public RandomAircraft2Unit() {
            this.delay = 0.0F;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class RadioOperatorUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof TypeFighter) && (actor.getArmy() == this.myArmy) && (actor.getSpeed(this.vector3d) > 20D)) {
                    if (actor == World.getPlayerAircraft()) {
                        flag = true;
                    }
                    this.pos.getAbs(this.point3d);
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    int i = (int) (-(actor.pos.getAbsOrient().getYaw() - 90D));
                    if (i < 0) {
                        i = 360 + i;
                    }
//                    boolean flag1 = false;
                    Aircraft aircraft = War.GetNearestEnemyAircraft(actor, 650F, 9);
                    if (aircraft == null) {
                        continue; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
                    }
                    if ((aircraft instanceof TypeFighter) && (aircraft.getSpeed(this.vector3d) > 20D)) {
                        double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                        double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                        double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                        // new String();
                        double d6 = (int) Math.ceil(d2 - d5);
                        // new String();
                        double d7 = d3 - d;
                        double d8 = d4 - d1;
                        float f = 57.32484F * (float) Math.atan2(d8, -d7);
                        int j = (int) (Math.floor((int) f) - 90D);
                        if (j < 0) {
                            j = 360 + j;
                        }
                        int k = j - i;
                        if (k < 0) {
                            k = 360 + k;
                        }
                        boolean flag2 = false;
                        if ((k >= 90) && (k <= 270)) {
                            flag2 = true;
                        }
                        String s = "left";
                        if ((k <= 180) && (k >= 90)) {
                            s = "right";
                        }
                        double d9 = d - d3;
                        double d10 = d1 - d4;
                        // Random random = new Random();
                        int l = TrueRandom.nextInt(100);
                        int i1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                        if ((i1 <= 650D) && (Math.sqrt(d6 * d6) <= 650D) && flag2 && flag && (l <= 50)) {
                            HUD.logCenter("                                          Nightfighter! Break " + s + "!");
                        }
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public RadioOperatorUnit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class OBSUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor == World.getPlayerAircraft()) && (actor.pos.getAbsPoint().distance(this.point3d) < 10000D)) {
                    flag = true;
                }
            }

            Aircraft aircraft = World.getPlayerAircraft();
            double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
            Aircraft aircraft1 = World.getPlayerAircraft();
            for (Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
                Actor actor1 = (Actor) entry1.getValue();
                if ((aircraft1.getArmy() == this.myArmy) && Actor.isAlive(actor1) && (actor1 instanceof Aircraft) && (actor1.getArmy() != this.myArmy) && (actor1.pos.getAbsPoint().distance(this.point3d) < 15000D)) {
                    this.pos.getAbs(this.point3d);
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                    double d4 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
                    char c = (char) (int) (65D + Math.floor(((d3 / 676D) - Math.floor(d3 / 676D)) * 26D));
                    char c1 = (char) (int) (65D + Math.floor(((d3 / 26D) - Math.floor(d3 / 26D)) * 26D));
                    // new String();
                    String s;
                    if (d2 > 260D) {
                        s = "" + c + c1;
                    } else {
                        s = "" + c1;
                    }
                    // new String();
                    double d5 = (int) (Math.floor(actor1.pos.getAbsPoint().z) / 100D) * 100D;
//                    double d6 = (int)(Math.floor((actor1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    // new String();
                    int i = (int) Math.ceil(d4);
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    double d9 = Math.floor((int) f) - 90D;
                    if (d9 < 0.0D) {
                        d9 = 360D + d9;
                    }
                    String s1 = "aircraft";
                    if (actor1 instanceof TypeBomber) {
                        s1 = "bombers";
                    } else if (actor1 instanceof TypeFighter) {
                        s1 = "fighters";
                    }
                    if (!flag && (Mission.cur().sectFile().get("Main", "CloudType", 2) <= 3)) {
                        HUD.logCenter("                                                                     Enemy " + s1 + " at map grid " + s + "-" + i + ", altitude " + d5 + "m");
                    }
                    if (!flag && (Mission.cur().sectFile().get("Main", "CloudType", 2) > 3)) {
                        HUD.logCenter("                                                                     Enemy aircraft at map grid " + s + "-" + i);
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public OBSUnit() {
            this.Timer1 = this.Timer2 = 30F;
        }
    }

    public static class OBOEUnit extends CandCGeneric {

        public boolean danger() {
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            int i = Mission.cur().sectFile().get("Mods", "OBOERange", 500) * 1000;
            if ((aircraft.pos.getAbsPoint().distance(this.point3d) > i) || !this.active) {
                return true;
            }
            boolean flag = ((Maneuver) aircraft.FM).hasBombs();
            if ((aircraft.getSpeed(this.vector3d) > 20D) && (aircraft.pos.getAbsPoint().z >= 500D) && flag) {
                double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
//                double d2 = World.land().HQ(point3d.x, point3d.y);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
//                double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                double d6 = aircraft.getSpeed(this.vector3d);
                double d7 = ((Maneuver) aircraft.FM).getAltitude();
                double d8 = d - d3;
                double d9 = d1 - d4;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                double d10 = Math.floor((int) f) - 90D;
                if (d10 < 0.0D) {
                    d10 = 360D + d10;
                }
                double d11 = (-aircraft.pos.getAbsOrient().getYaw()) + 90D;
                if (d11 < 0.0D) {
                    d11 += 360D;
                }
                double d12 = d3 - d;
                double d13 = d4 - d1;
                double d14 = Math.sqrt((d13 * d13) + (d12 * d12));
                double d15 = d7 - World.land().HQ(this.point3d.x, this.point3d.y);
                if (d15 < 0.0D) {
                    d15 = 0.0D;
                }
                double d16 = d10 - d11;
                String s = "<<<";
                if (d16 > 60D) {
                    s = ">>>";
                } else if (d16 > 30D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    s = ">>";
                } else if (d16 > 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    s = ">";
                } else if (d16 < -60D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    s = "<<<";
                } else if (d16 < -30D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    s = "<<";
                } else if (d16 < 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    s = "<";
                }
                double d17 = d6 * Math.sqrt(d15 * 0.20386999845504761D);
                int j = (int) ((d14 - d17) / d6 / 60D);
                if (j < 1) {
                    this.resetTimer(1.0F);
                } else {
                    this.resetTimer(5.5F);
                }
                String s1 = "|" + j + "|";
                if (j < 1) {
                    j = (int) ((d14 - d17) / d6);
                    s1 = "[" + j + "]";
                }
                if ((d16 <= 1.0D) || (d16 >= -1D)) {
                    HUD.logCenter("                                                                             " + s1);
                }
                if ((d16 >= 1.0D) || (d16 <= -1D)) {
                    HUD.logCenter("                                                                             " + s);
                }
                if (((d16 <= 1.0D) || (d16 >= -1D)) && ((d14 <= d17) || (j == 0))) {
                    HUD.logCenter("                                                                             Drop!");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

//        private boolean flag2;
//        public static Point3d point3d = new Point3d();
        public boolean   active;

        public OBOEUnit() {
//            flag2 = false;
            this.active = true;
            this.Timer1 = this.Timer2 = this.delay = 5.5F;
        }
    }

    public static class NaxosUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof TypeBomber) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
//                    String s = "level with us";
//                    if(d2 - d5 - 200D >= 0.0D)
//                        s = "below us";
//                    if((d2 - d5) + 200D < 0.0D)
//                        s = "above us";
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
//                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
//                    int i2 = l1 - j;
                    // Random random = new Random();
                    boolean flag1 = false;
                    if (TrueRandom.nextInt(100) < 10) {
                        flag1 = true;
                    }
                    if (Mission.cur().sectFile().get("Mods", "NaxosLate", 0) != 1) {
                        flag1 = true;
                    }
                    if ((k1 <= 50000D) && (k1 >= 100D) && (d6 < 0.0D) && flag1) {
                        HUD.logCenter("                                          Naxos: Target bearing " + i1 + "\260");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public NaxosUnit() {
            this.Timer1 = this.Timer2 = 15F;
        }
    }

    public static class NavUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if ((aircraft.getSpeed(this.vector3d) > 20D) && (aircraft.pos.getAbsPoint().z >= 150D) && (aircraft.getArmy() == this.myArmy)) {
                this.pos.getAbs(this.point3d);
                if ((Mission.cur() != null) && (Mission.cur().sectFile().get("Mods", "NavError", 0) == 1)) {
                    this.error++;
                    if (this.error > 50) {
                        this.error = 50;
                    }
                }
                int i = this.error + (Mission.cur().sectFile().get("Main", "CloudType", 2) * 2);
                int j = i;
                // Random random = new Random();
                int k = TrueRandom.nextInt(100);
                if (k > 50) {
                    i -= i * 2;
                }
                k = TrueRandom.nextInt(100);
                if (k > 50) {
                    j -= j * 2;
                }
                double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d1 = (((Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D) + i) / 10D;
                double d2 = (((Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D) + j) / 10D;
                char c = (char) (int) (65D + Math.floor(((d1 / 676D) - Math.floor(d1 / 676D)) * 26D));
                char c1 = (char) (int) (65D + Math.floor(((d1 / 26D) - Math.floor(d1 / 26D)) * 26D));
                // new String();
                String s;
                if (d > 260D) {
                    s = "" + c + c1;
                } else {
                    s = "" + c1;
                }
                // new String();
                int l = (int) Math.ceil(d2);
                HUD.logCenter("                                          Position Fix: " + s + "-" + l);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private int      error;

        public NavUnit() {
            this.error = 0;
            this.Timer1 = this.Timer2 = 300F;
        }
    }

    public static class MonicaUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            Aircraft aircraft1 = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((aircraft1.getArmy() == this.myArmy) && (actor instanceof Aircraft) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
                    int i2 = l1 - j;
                    int j2 = (int) (Math.ceil((k1 * 3.28084D) / 100D) * 100D);
                    String s = "ft";
                    if (j2 >= 5280) {
                        j2 = (int) Math.floor(j2 / 5280);
                        s = "mi";
                    }
//                    int k2 = (int)Math.ceil((double)l * 0.62137119200000002D);
                    if ((k1 <= 6400) && (k1 >= 308) && (i2 >= 255) && (i2 <= 285) && (Math.abs(j1) >= 120)) {
                        HUD.logCenter("                                                                   (" + j2 + s + ")");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public MonicaUnit() {
            this.Timer1 = this.Timer2 = 10F;
        }
    }

    public static class MADUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((aircraft.getArmy() == this.myArmy) && ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft())) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
//                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
//                    int i2 = l1 - j;
                    int j2 = (int) (Math.ceil(k1 * 3.28084D) / 100D) * 100;
                    String s = "MAD: Contact";
                    if ((k1 <= 1000) && (d2 <= 500D)) {
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " feet");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public MADUnit() {
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class JammerUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            char c = '\u03E8';
//            Vector3d vector3d = new Vector3d();
            int i = 1000;
//            Object obj = null;
            for (; i <= 0x30d40; i += 1000) {
                List list = Engine.targets();
                int j = list.size();
                for (int k = 0; k < j; k++) {
                    Actor actor = (Actor) list.get(k);
                    if (((actor instanceof B_17) || (actor instanceof B_24)) && Actor.isAlive(actor) && (actor.getArmy() == this.myArmy) && (actor.pos.getAbsPoint().z >= 500D)) {
//                        Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(actor, 200000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                        Aircraft aircraft = GetNearestEnemy(actor, 200000F, 9);
                        if ((aircraft != null) && (aircraft instanceof TypeFighter) && (aircraft.getArmy() != this.myArmy)) {
                            Pilot pilot = (Pilot) aircraft.FM;
                            AirGroup airgroup = ((Maneuver) aircraft.FM).Group;
                            // Random random = new Random();
                            int l = TrueRandom.nextInt(100);
                            if (l < 10) {
//                                if(airgroup.grTask == 3)
                                if ((pilot.Group != null) && (airgroup.grTask == 3)) {
                                    pilot.Group.setGroupTask(1);
                                }
                                if (aircraft == World.getPlayerAircraft()) {
                                    int i1 = TrueRandom.nextInt(360);
                                    int j1 = (int) (actor.pos.getAbsPoint().z * 0.1D) * 10;
                                    int k1 = TrueRandom.nextInt(100);
                                    int l1 = TrueRandom.nextInt(360);
                                    if (Mission.cur().sectFile().get("Mods", "JammerMode", 0) == 1) {
                                        int i2 = TrueRandom.nextInt(50) + 30;
                                        int j2 = i2;
                                        int k2 = TrueRandom.nextInt(100);
                                        if (k2 > 50) {
                                            i2 -= i2 * 2;
                                        }
                                        k2 = TrueRandom.nextInt(100);
                                        if (k2 > 50) {
                                            j2 -= j2 * 2;
                                        }
                                        double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                                        double d1 = (((Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x) / 1000D) + i2) / 10D;
                                        double d2 = (((Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y) / 1000D) + j2) / 10D;
                                        char c1 = (char) (int) (65D + Math.floor(((d1 / 676D) - Math.floor(d1 / 676D)) * 26D));
                                        char c2 = (char) (int) (65D + Math.floor(((d1 / 26D) - Math.floor(d1 / 26D)) * 26D));
                                        // new String();
                                        String s;
                                        if (d > 260D) {
                                            s = "" + c1 + c2;
                                        } else {
                                            s = "" + c2;
                                        }
                                        // new String();
                                        int l2 = (int) Math.ceil(d2);
                                        if (actor instanceof B_17) {
                                            HUD.logCenter("                                          Incoming Raid at " + s + "-" + l2 + ", heading " + i1 + "\260");
                                        } else if (actor instanceof B_24) {
                                            HUD.logCenter("                                          Ground signals are being jammed!");
                                        }
                                    } else if (actor instanceof B_17) {
                                        HUD.logCenter("                                          Target bearing " + l1 + "\260" + ", range " + k1 + " km, height " + j1 + "m, heading " + i1 + "\260");
                                    } else if (actor instanceof B_24) {
                                        HUD.logCenter("                                          Ground signals are being jammed!");
                                    }
                                }
                            }
                        }
                    }
                }

            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public JammerUnit() {
        }
    }

    public static class H2SUnit extends CandCGeneric {

        public boolean danger() {
            if (!this.active) {
                return true;
            }
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            boolean flag = ((Maneuver) aircraft.FM).hasBombs();
            if ((aircraft.getSpeed(this.vector3d) > 20D) && (aircraft.pos.getAbsPoint().z >= 500D) && flag && (aircraft.getArmy() == this.myArmy)) {
                double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
//                double d2 = World.land().HQ(point3d.x, point3d.y);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
//                double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
//                double d6 = aircraft.getSpeed(vector3d);
//                double d7 = ((Maneuver)((Aircraft)aircraft).FM).getAltitude();
                double d8 = d - d3;
                double d9 = d1 - d4;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                int i = (int) (Math.floor((int) f) - 90D);
                if (i < 0) {
                    i = 360 + i;
                }
                int j = (int) ((-aircraft.pos.getAbsOrient().getYaw()) + 90D);
                if (j < 0) {
                    j += 360;
                }
                double d10 = d3 - d;
                double d11 = d4 - d1;
                double d12 = Math.sqrt((d11 * d11) + (d10 * d10));
                // Random random = new Random();
                int k = TrueRandom.nextInt(100);
                Engine.land();
                int l = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(aircraft.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(aircraft.pos.getAbsPoint().y));
                if (d12 <= 5000D) {
                    HUD.logCenter("                                                                                 H2S: Over Target Area");
                } else if ((aircraft.pos.getAbsPoint().distance(this.point3d) < 25000D) || (k < 10) || ((l >= 0) && (l < 4)) || ((l >= 16) && (l < 29))) {
                    HUD.logCenter("                                                                                 H2S: Turn to heading " + i + " \260");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

//        public static Point3d point3d = new Point3d();
        public boolean   active;

        public H2SUnit() {
            this.active = true;
            this.Timer1 = this.Timer2 = 30F;
        }
    }

    public static class GTWUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
//                    String s = "level with us";
//                    if(d2 - d6 - 300D >= 0.0D)
//                        s = "below us";
//                    if((d2 - d6) + 300D <= 0.0D)
//                        s = "above us";
//                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
//                        s = "slightly below";
//                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
//                        s = "slightly above";
//                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    float f3 = 4000F;
                    float f4 = f3;
                    if ((d3 < (1.25F * f3)) && !flag1) {
                        f4 = (float) d3 * 0.8F;
                    }
                    if ((d3 < (1.25F * f3)) && flag1) {
                        if (d3 <= (1.25F * f3 * 0.5F)) {
                            f4 = (float) (d3 * 0.8D * 2D);
                        } else {
                            f4 = f3;
                        }
                    }
                    int i2 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f2) / 10D) * 10D);
                    if (i2 > f3) {
                        i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                    }
                    float f5 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil(i2 / 100D) * 100D);
                    int i3 = (k1 + l1) - 180;
                    float f6 = (float) (f4 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * (f4 * 0.25D)));
                    int j3 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    if ((i2 <= j3) && (i2 > 400) && (k2 >= -45) && (k2 <= 30) && (Math.abs(i3) <= 30)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                                                   (" + l2 + "m" + ")");
                        this.freq = 6F;
                    } else {
                        this.freq = 10F;
                    }
                    super.resetTimer(this.freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public GTWUnit() {
            this.freq = 10F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class GNRUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            Pilot pilot = (Pilot) aircraft.FM;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((pilot.Wingman != null) || (pilot.crew > 1)) && (actor instanceof Aircraft) && (actor != World.getPlayerAircraft()) && (actor.getArmy() != this.myArmy) && (aircraft.getArmy() == this.myArmy) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    double d6 = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    double d7 = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    String s = "";
                    if ((d2 - d5 - 500D) >= 0.0D) {
                        s = " low";
                    }
                    if (((d2 - d5) + 500D) < 0.0D) {
                        s = " high";
                    }
                    // new String();
                    double d9 = d3 - d;
                    double d10 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d10, -d9);
                    int j = (int) (Math.floor((int) f) - 90D);
                    if (j < 0) {
                        j = 360 + j;
                    }
                    int k = j - i;
                    if (k < 0) {
                        k = 360 + k;
                    }
                    int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
                    if (l < 1) {
                        l = 12;
                    }
                    double d11 = d - d3;
                    double d12 = d1 - d4;
                    double d13 = Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D;
                    String s1 = "Aircraft ";
                    if (actor instanceof TypeFighter) {
                        s1 = "Fighters ";
                    }
                    if (actor instanceof TypeBomber) {
                        s1 = "Bombers ";
                    }
                    float f1 = World.getTimeofDay();
                    boolean flag = false;
                    if (((f1 >= 0.0F) && (f1 <= 5F)) || ((f1 >= 21F) && (f1 <= 24F))) {
                        flag = true;
                    }
                    // Random random = new Random();
                    int i1 = TrueRandom.nextInt(100);
                    if (!flag && (d13 <= 6000D) && (d13 >= 500D) && (Math.sqrt(d8 * d8) <= 2000D)) {
                        HUD.logCenter("                                          " + s1 + "at " + l + " o'clock" + s + "!");
                    }
                    if (flag && (i1 <= 50) && (d13 <= 1000D) && (d13 >= 100D) && (Math.sqrt(d8 * d8) <= 500D)) {
                        HUD.logCenter("                                          " + s1 + "at " + l + " o'clock" + s + "!");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public GNRUnit() {
            this.Timer1 = this.Timer2 = 30F;
        }
    }

    public static class GCIUnit extends CandCGeneric {

        public boolean danger() {
            if (Mission.cur() != null) {
                this.maxrange = Mission.cur().sectFile().get("Mods", "GCIRange", 100);
            }
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
//            float f = 1000F;
            // Random random = new Random();
            int i = TrueRandom.nextInt(140);
            int j = i + 10;
            Aircraft aircraft1 = World.getPlayerAircraft();
            if (aircraft1 == null) {
                return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
            }

            // Get rid of endless recursion through all game's target list (a few hundred times)
            // New method introduced to get the same result with on single target list search!
            Aircraft aircraft = GetNearestEnemyInSteps(aircraft1, 1000F, this.maxrange * 1000F, 1000F, 9);

////            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//            Aircraft aircraft = GetNearestEnemy(aircraft1, 1000F, 9);
//            for(int k = 0; aircraft == null && k < maxrange; k++)
//            {
////                aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F + f, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//                aircraft = GetNearestEnemy(aircraft1, 1000F + f, 9);
//                f += 1000F;
//            }
            if (aircraft == null) {
                return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
            }

//            boolean flag1 = false;
            this.maxrange *= 1000;
            if ((Mission.cur() != null) && (Mission.cur().sectFile().get("Mods", "GCIVarAlt", 0) == 1)) {
                this.maxheight1 = (aircraft.pos.getAbsPoint().distance(this.point3d) / this.maxrange) * 8000D;
                if (this.maxheight1 < j) {
                    this.maxheight1 = j;
                }
                this.maxheight2 = (aircraft1.pos.getAbsPoint().distance(this.point3d) / this.maxrange) * 8000D;
                if (this.maxheight2 < j) {
                    this.maxheight2 = j;
                }
            }
            if ((aircraft1.pos.getAbsPoint().distance(this.point3d) < this.maxrange) && (aircraft1.pos.getAbsPoint().z >= this.maxheight2)) {
                flag = true;
            }
            if ((aircraft.getSpeed(this.vector3d) > 20D) && (aircraft.pos.getAbsPoint().distance(this.point3d) < this.maxrange) && (aircraft.pos.getAbsPoint().z >= this.maxheight1) && (aircraft != World.getPlayerAircraft()) && (aircraft.getArmy() != this.myArmy) && (aircraft1.getArmy() == this.myArmy)) {
                double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 10000D;
                double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 10000D;
                double d2 = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 1000D;
                double d3 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 1000D;
                double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d5 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
                double d6 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
                double d7 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D;
                double d8 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D;
                char c = (char) (int) (65D + Math.floor(((d5 / 676D) - Math.floor(d5 / 676D)) * 26D));
                char c1 = (char) (int) (65D + Math.floor(((d5 / 26D) - Math.floor(d5 / 26D)) * 26D));
                // new String();
                String s;
                if (d4 > 260D) {
                    s = "" + c + c1;
                } else {
                    s = "" + c1;
                }
                // new String();
                int l = (int) (Math.floor(aircraft.pos.getAbsPoint().z * 0.1D) * 10D);
//                int i1 = (int)(Math.floor((aircraft.getSpeed(vector3d) * 0.62137119200000002D * 60D * 60D) / 10000D) * 10D);
                // new String();
                int j1 = (int) Math.ceil(d6);
                double d9 = d5 - d;
                double d10 = d6 - d1;
                float f1 = 57.32484F * (float) Math.atan2(d10, -d9);
                double d11 = Math.floor((int) f1) - 90D;
                if (d11 < 0.0D) {
                    d11 = 360D + d11;
                }
                int k1 = (int) d11;
                double d12 = d2 - d7;
                double d13 = d3 - d8;
                int l1 = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
                if (l1 < 0) {
                    l1 = 360 + l1;
                }
                int i2 = (int) Math.ceil(Math.sqrt((d13 * d13) + (d12 * d12)));
                if (flag) {
                    if (i2 > 4) {
                        HUD.logCenter("                                          Target bearing " + k1 + "\260" + ", range " + i2 + "km, height " + l + "m, heading " + l1 + "\260");
                    } else {
                        HUD.logCenter(" ");
                    }
                } else {
                    HUD.logCenter("                                                                             Target at " + s + "-" + j1 + ", height " + l + "m, heading " + l1 + "\260");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private int      maxrange;
        private double   maxheight1;
        private double   maxheight2;

        public GCIUnit() {
            this.maxrange = 0x186a0;
            this.maxheight1 = 150D;
            this.maxheight2 = 150D;
        }
    }

    public static class FD2Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
//                    boolean flag1 = false;
                    Engine.land();
//                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
//                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
//                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
//                        flag1 = true;
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    float f3 = 3000F;
                    float f4 = f3;
                    if (d3 < 1250D) {
                        f4 = (float) (d3 * 0.8D * 3D);
                    }
                    int i2 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f2) / 10D) * 10D);
                    if (i2 > f3) {
                        i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                    }
                    float f5 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) f3;
                    if (i2 < f3) {
                        if (i2 > 1200) {
                            l2 = (int) (Math.ceil(i2 / 500D) * 500D);
                        } else {
                            l2 = (int) (Math.ceil(i2 / 200D) * 200D);
                        }
                    }
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if (j3 < 0) {
                        j3 += 360;
                    }
                    float f6 = (float) (f4 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 5.15D)) * (f4 * 0.25D)));
                    int k3 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    String s2 = "  ";
                    if (j3 < 3) {
                        s1 = "Dead ahead, ";
                    } else if ((j3 > 2) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 5, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 10, ";
                    } else if ((j3 > 12) && (j3 < 18)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 15, ";
                    } else if ((j3 > 17) && (j3 < 23)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 20, ";
                    } else if ((j3 > 22) && (j3 <= 30)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    {
                        s1 = "Right 25, ";
                        s2 = ", evading";
                    } else if (j3 > 357) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Dead ahead, ";
                    } else if ((j3 < 358) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 5, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 10, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 15, ";
                    } else if ((j3 < 343) && (j3 > 332)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 20, ";
                    } else if ((j3 < 333) && (j3 >= 330)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    {
                        s1 = "Left 25, ";
                        s2 = ", evading";
                    }
                    String s3 = "  ";
                    if (k2 < -10) {
                        s3 = "nose down";
                    } else if ((k2 >= -10) && (k2 <= -5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "down a bit";
                    } else if ((k2 > -5) && (k2 < 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "level";
                    } else if ((k2 <= 10) && (k2 >= 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "up a bit";
                    } else if (k2 > 10) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "pull up";
                    }
                    String s4 = "  ";
                    if (j3 < 3) {
                        s4 = "dead ahead, ";
                    } else if ((j3 > 2) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 5\260, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 10\260, ";
                    } else if ((j3 > 12) && (j3 < 18)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 15\260, ";
                    } else if ((j3 > 17) && (j3 < 23)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 20\260, ";
                    } else if ((j3 > 22) && (j3 <= 30)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 25\260, ";
                    } else if (j3 > 357) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "dead ahead, ";
                    } else if ((j3 < 358) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 5\260, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 10\260, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 15\260, ";
                    } else if ((j3 < 343) && (j3 > 332)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 20\260, ";
                    } else if ((j3 < 333) && (j3 >= 330)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 25\260, ";
                    }
                    if ((i2 <= k3) && (i2 > 1500) && (k2 >= -30) && (k2 <= 30) && (Math.abs(i3) <= 30)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: Contact " + s4 + s + ", " + l2 + "m" + s2);
                        float f7 = 7F;
                        this.engineSTimer = -(int) CandCGeneric.SecsToTicks(CandCGeneric.Rnd(f7, f7));
                    } else if ((i2 <= k3) && (i2 <= 1500) && (i2 >= 500) && (k2 >= -30) && (k2 <= 30) && (Math.abs(i3) <= 30)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: " + s1 + s3 + ", " + l2 + "m" + s2);
                        float f8 = 5F;
                        this.engineSTimer = -(int) CandCGeneric.SecsToTicks(CandCGeneric.Rnd(f8, f8));
                    } else {
                        this.freq = 7F;
                        this.engineSTimer = -(int) CandCGeneric.SecsToTicks(CandCGeneric.Rnd(this.freq, this.freq));
                    }
                    super.resetTimer(this.freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public FD2Unit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class FuGUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
//                    boolean flag1 = false;
                    Engine.land();
//                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
//                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
//                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
//                        flag1 = true;
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    float f3 = 3500F;
                    float f4 = f3;
                    if (d3 < 1000D) {
                        f4 = (float) (d3 * 0.8D * 3.5D);
                    }
                    int i2 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f2) / 10D) * 10D);
                    if (i2 > f3) {
                        i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                    }
                    float f5 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil(i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if (j3 < 0) {
                        j3 += 360;
                    }
                    float f6 = (float) (f4 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 5.15D)) * (f4 * 0.25D)));
                    int k3 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    String s2 = "  ";
                    if (j3 < 3) {
                        s1 = "Dead ahead, ";
                    } else if ((j3 > 2) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 5, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 10, ";
                    } else if ((j3 > 12) && (j3 < 18)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    {
                        s1 = "Right 15, ";
                        s2 = ", evading";
                    } else if ((j3 > 17) && (j3 < 26)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 20, ";
                    } else if ((j3 > 25) && (j3 < 36)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 30, ";
                        s2 = ", evading";
                    } else if (j3 > 357) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Dead ahead, ";
                    } else if ((j3 < 358) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 5, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 10, ";
                    } else if ((j3 < 348) && (j3 > 342)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    {
                        s1 = "Left 15, ";
                        s2 = ", evading";
                    } else if ((j3 < 343) && (j3 > 334)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 20, ";
                    } else if ((j3 < 335) && (j3 > 324)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 30, ";
                        s2 = ", evading";
                    }
                    String s3 = "  ";
                    if (k2 < -10) {
                        s3 = "nose down";
                    } else if ((k2 > -11) && (k2 < -4)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "down a bit";
                    } else if ((k2 > -5) && (k2 < 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "level";
                    } else if ((k2 < 11) && (k2 > 4)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "up a bit";
                    } else if (k2 > 10) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "pull up";
                    }
                    String s4 = "  ";
                    if (j3 < 3) {
                        s4 = "dead ahead, ";
                    } else if ((j3 > 2) && (j3 < 8)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 5\260, ";
                    } else if ((j3 > 7) && (j3 < 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 10\260, ";
                    } else if ((j3 > 12) && (j3 < 18)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 15\260, ";
                    } else if ((j3 > 17) && (j3 < 26)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 20\260, ";
                    } else if ((j3 > 25) && (j3 < 36)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 30\260, ";
                    } else if (j3 > 357) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "dead ahead, ";
                    } else if ((j3 < 358) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 5\260, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 10\260, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 15\260, ";
                    } else if ((j3 < 343) && (j3 > 334)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 20\260, ";
                    } else if ((j3 < 335) && (j3 > 324)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 30\260, ";
                    }
                    if ((i2 <= k3) && (i2 > 1500) && (k2 >= -35) && (k2 <= 35) && (Math.abs(i3) < 36)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: Contact " + s4 + s + ", " + l2 + "m" + s2);
                        this.freq = 6F;
                    } else if ((i2 <= k3) && (i2 <= 1500) && (i2 >= 200) && (k2 >= -35) && (k2 <= 35) && (Math.abs(i3) < 36)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: " + s1 + s3 + ", " + l2 + "m" + s2);
                        this.freq = 4F;
                    } else {
                        this.freq = 6F;
                    }
                    super.resetTimer(this.freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public FuGUnit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class FuG218Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    float f3 = 5000F;
                    float f4 = f3;
                    if ((d3 < (1.25F * f3)) && !flag1) {
                        f4 = (float) d3 * 0.8F;
                    }
                    if ((d3 < (1.25F * f3)) && flag1) {
                        if (d3 <= (1.25F * f3 * 0.5F)) {
                            f4 = (float) (d3 * 0.8D * 2D);
                        } else {
                            f4 = f3;
                        }
                    }
                    int i2 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f2) / 10D) * 10D);
                    if (i2 > f3) {
                        i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                    }
                    float f5 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil(i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if (j3 < 0) {
                        j3 += 360;
                    }
                    float f6 = (float) (f4 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * (f4 * 0.25D)));
                    int k3 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    if (j3 < 5) {
                        s1 = "Dead ahead, ";
                    } else if ((j3 >= 5) && (j3 <= 7)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 5, ";
                    } else if ((j3 > 7) && (j3 <= 12)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 10, ";
                    } else if ((j3 > 12) && (j3 <= 17)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 15, ";
                    } else if ((j3 > 17) && (j3 <= 25)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 20, ";
                    } else if ((j3 > 25) && (j3 <= 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 30, ";
                    } else if ((j3 > 35) && (j3 <= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Right 40, ";
                    } else if ((j3 > 45) && (j3 <= 60)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Turn right, ";
                    } else if (j3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Dead ahead, ";
                    } else if ((j3 <= 355) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 5, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 10, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 15, ";
                    } else if ((j3 < 343) && (j3 >= 335)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 20, ";
                    } else if ((j3 < 335) && (j3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 30, ";
                    } else if ((j3 < 325) && (j3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Left 40, ";
                    } else if ((j3 < 345) && (j3 >= 300)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s1 = "Turn left, ";
                    }
                    String s2 = "  ";
                    if (k2 < -10) {
                        s2 = "nose down";
                    } else if ((k2 >= -10) && (k2 <= -5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "down a bit";
                    } else if ((k2 > -5) && (k2 < 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "level";
                    } else if ((k2 <= 10) && (k2 >= 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "up a bit";
                    } else if (k2 > 10) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "pull up";
                    }
                    String s3 = "  ";
                    if (j3 < 5) {
                        s3 = "dead ahead, ";
                    } else if ((j3 >= 5) && (j3 <= 7)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 5\260, ";
                    } else if ((j3 > 7) && (j3 <= 12)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 10\260, ";
                    } else if ((j3 > 12) && (j3 <= 17)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 15\260, ";
                    } else if ((j3 > 17) && (j3 <= 25)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 20\260, ";
                    } else if ((j3 > 25) && (j3 <= 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 30\260, ";
                    } else if ((j3 > 35) && (j3 <= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "right by 40\260, ";
                    } else if ((j3 > 45) && (j3 <= 60)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "off our right, ";
                    } else if (j3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "dead ahead, ";
                    } else if ((j3 <= 355) && (j3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 5\260, ";
                    } else if ((j3 < 353) && (j3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 10\260, ";
                    } else if ((j3 < 348) && (j3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 15\260, ";
                    } else if ((j3 < 343) && (j3 >= 335)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 20\260, ";
                    } else if ((j3 < 335) && (j3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 30\260, ";
                    } else if ((j3 < 325) && (j3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "left by 40\260, ";
                    } else if ((j3 < 345) && (j3 >= 300)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "off our left, ";
                    }
                    if ((i2 <= k3) && (i2 > 1500) && (k2 >= -60) && (k2 <= 60) && (Math.abs(i3) <= 60D)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: Contact " + s3 + s + ", " + l2 + "m");
                        this.freq = 6F;
                    } else if ((i2 <= k3) && (i2 <= 1500) && (i2 >= 120) && (k2 >= -60) && (k2 <= 60) && (Math.abs(i3) <= 60D)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: " + s1 + s2 + ", " + l2 + "m");
                        this.freq = 4F;
                    } else {
                        this.freq = 6F;
                    }
                    super.resetTimer(this.freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public FuG218Unit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class FlensburgUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((actor instanceof TypeFighter) || (actor instanceof TypeBomber)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    Aircraft aircraft = World.getPlayerAircraft();
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    int i = (int) (-(actor.pos.getAbsOrient().getYaw() - 90D));
                    if (i < 0) {
                        i = 360 + i;
                    }
                    int j = (int) (-(actor.pos.getAbsOrient().getPitch() - 90D));
                    if (j < 0) {
                        j = 360 + j;
                    }
                    // new String();
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
//                    double d9 = d - d3;
//                    double d10 = d1 - d4;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    float f1 = 57.32484F * (float) Math.atan2(d8, -d7);
                    int k = (int) (Math.floor((int) f) - 90D);
                    if (k < 0) {
                        k = 360 + k;
                    }
                    int l = (int) (Math.floor((int) f1) + 90D);
                    if (l < 0) {
                        l = 360 + l;
                    }
                    int i1 = k - i;
                    double d11 = d - d3;
                    double d12 = d1 - d4;
                    double d13 = Math.sqrt(d6 * d6);
                    int j1 = (int) (Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D);
                    float f2 = 57.32484F * (float) Math.atan2(j1, d13);
                    int k1 = (int) (Math.floor((int) f2) - 90D);
                    if (k1 < 0) {
                        k1 = 360 + k1;
                    }
                    int l1 = k1 - j;
                    int i2 = 0;
                    if ((l1 >= 220) && (l1 <= 320) && (Math.sqrt(i1 * i1) >= 120D)) {
                        i2 = 0x186a0;
                    }
                    if (j1 <= i2) {
                        HUD.logCenter("                                         Flensburg: Target bearing " + l + "\260");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public FlensburgUnit() {
            this.Timer1 = this.Timer2 = 15F;
        }
    }

    public static class FireUnit extends CandCGeneric {

        public boolean danger() {
            boolean flag = false;
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
            List list = Engine.targets();
//            World.MaxVisualDistance = 50000F; // TODO: Fixed by SAS~Storebror: WTF???!??
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if (!flag && (actor.pos.getAbsPoint().distance(this.point3d) < 10000D) && (actor instanceof TypeBomber) && (actor.getArmy() != this.myArmy)) {
                    // Random random = new Random();
                    int k = TrueRandom.nextInt(500);
                    int l = k - 250;
                    k = TrueRandom.nextInt(500);
                    int i1 = k - 250;
                    Eff3DActor.New(this, null, new Loc(l, i1, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/CityFire.eff", 600F);
                    flag = true;
                    int j1 = TrueRandom.nextInt(10);
                    this.wait = (float) (1.0D + (j1 * 0.1D));
                }
            }

            return true;
        }

//        private int counter;
        private float   wait;

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public FireUnit() {
//            counter = 0;
            this.wait = 1.0F;
            this.Timer1 = this.Timer2 = this.wait;
        }
    }

    public static class ASVmkIIIUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft())) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
                    int i2 = l1 - j;
                    k1 = (int) (k1 / 1000D);
                    int j2 = (int) Math.ceil(k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if (actor instanceof ShipGeneric) {
                        byte0 = 50;
                    }
                    if (actor instanceof BigshipGeneric) {
                        byte0 = 80;
                    }
                    if ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) {
                        byte0 = 15;
                    }
                    if ((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf)) {
                        byte0 = 30;
                    }
                    if ((k1 <= byte0) && (i2 >= 210) && (i2 <= 270) && (Math.sqrt(j1 * j1) <= 60D)) {
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASVmkIIIUnit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class FACUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            boolean flag1 = false;
            int i = Mission.cur().sectFile().get("Mods", "FACDelay", 0) * 60;
            if (Time.current() > i) {
                this.active = true;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor.getArmy() == this.myArmy) && (actor instanceof Aircraft) && (actor.pos.getAbsPoint().distance(this.point3d) < 30000D)) {
                    flag = true;
                }
            }

            for (Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
                Actor actor1 = (Actor) entry1.getValue();
                if (this.active && Actor.isAlive(actor1) && ((actor1 instanceof TankGeneric) || (actor1 instanceof ArtilleryGeneric) || (actor1 instanceof CarGeneric)) && (actor1.getArmy() != this.myArmy) && (actor1.pos.getAbsPoint().distance(this.point3d) < 2000D)) {
                    this.pos.getAbs(this.point3d);
                    double d = (Main3D.cur3D().land2D.worldOfsX() + this.point3d.x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + this.point3d.y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = actor1.pos.getAbsPoint().distance(this.point3d);
//                    double d4 = point3d.z;
                    double d5 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                    double d6 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
//                    double d7 = actor1.pos.getAbsPoint().z;
                    char c = (char) (int) (65D + Math.floor(((d / 676D) - Math.floor(d / 676D)) * 26D));
                    char c1 = (char) (int) (65D + Math.floor(((d / 26D) - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if (d2 > 260D) {
                        s1 = "" + c + c1;
                    } else {
                        s1 = "" + c1;
                    }
                    double d8 = d5 - d;
                    double d9 = d6 - d1;
                    float f = 57.32484F * (float) Math.atan2(d9, d8);
                    int j = (int) f;
                    j = (j + 180) % 360;
                    // new String();
                    String s2 = "west";
                    if ((j <= 315) && (j >= 225)) {
                        s2 = "north";
                    } else if ((j <= 135) && (j >= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "south";
                    } else if ((j <= 44) && (j >= 316)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "west";
                    } else if ((j <= 224) && (j >= 136)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "east";
                    }
                    // new String();
                    String s3 = "units";
                    if (actor1 instanceof TgtTank) {
                        s3 = "armor";
                    }
                    if (actor1 instanceof ArtilleryGeneric) {
                        s3 = "units";
                    }
                    if (actor1 instanceof TgtVehicle) {
                        s3 = "vehicle";
                    }
                    if ((actor1 instanceof ArtilleryCY6.ProneInfantry) || (actor1 instanceof ArtilleryCY6.DugInInfantry)) {
                        s3 = "troops";
                    }
                    if (actor1 instanceof AAA) {
                        flag1 = true;
                    }
                    String s4 = "";
                    if (flag1) {
                        s4 = ", watch out for AA";
                    }
//                    boolean flag3 = false;
                    String s5 = "";
                    Engine.land();
                    int k = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor1.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor1.pos.getAbsPoint().y));
                    if ((k >= 28) && (k < 32)) {
                        s5 = ", near the water";
                    } else if ((k >= 16) && (k < 20)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = ", in the town";
                    } else if ((k >= 24) && (k < 29)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = ", near the treeline";
                    } else if (((k >= 32) && (k < 64)) || (k >= 128)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = ", near the road";
                    } else if ((k >= 64) && (k < 128)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = ", near the tracks";
                    }
                    String s6 = "";
                    Actor actor3 = null;
                    actor3 = War.GetNearestEnemy(actor1, actor1.getArmy(), 300F);
                    if (actor3 != null) {
                        s6 = ", friendlies close";
                    }
                    int l = (int) (Math.ceil(d3 / 10D) * 10D);
                    int i1 = (int) Math.ceil(d1);
                    float f1 = World.getTimeofDay();
                    boolean flag4 = false;
                    if (((f1 >= 0.0F) && (f1 <= 5F)) || ((f1 >= 21F) && (f1 <= 24F))) {
                        flag4 = true;
                    }
                    if (flag) {
                        if (!this.flag2) {
                            if (!flag4) {
                                HUD.logCenter("                                                                             Good to see you! Popping Smoke!");
                                Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/Marksmoke.eff", -1F);
                            } else {
                                HUD.logCenter("                                                                             Popping Flare!");
                                Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", -1F);
                            }
                            this.flag2 = true;
                        } else {
                            HUD.logCenter("                                                                             Enemy " + s3 + " " + l + " yards " + s2 + " of my mark" + s5 + s6 + s4 + "!");
                        }
                        Eff3DActor.New(actor1, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/WPSmoke.eff", 60F);
                        this.popped = true;
                        List list = Engine.targets();
                        int j1 = list.size();
                        for (int k1 = 0; k1 < j1; k1++) {
                            Actor actor4 = (Actor) list.get(k1);
                            Aircraft aircraft = (Aircraft) actor4;
                            if (((actor4 instanceof TypeStormovik) || (actor4 instanceof TypeFighter) || (actor4 instanceof TypeFastJet)) && !(actor4 instanceof TypeScout) && (actor4.pos.getAbsPoint().distance(this.point3d) < 30000D) && (actor4.pos.getAbsPoint().distance(this.point3d) > 5000D) && (Mission.cur().sectFile().get("Mods", "FACNoGuide", 0) != 1)) {
                                AirGroup airgroup = ((Maneuver) ((Aircraft) actor4).FM).Group;
                                Pilot pilot = (Pilot) ((Aircraft) actor4).FM;
//                                if(pilot.get_task() != 7)
                                if ((pilot.Group != null) && (pilot.get_task() != 7)) // TODO: Fixed by SAS~Storebror, added pilot Group null check!
                                {
                                    pilot.Group.setGroupTask(4);
                                    pilot.Group.setGTargMode(0);
                                    pilot.Group.setFormationAndScale(airgroup.formationType, 3F, true);
                                    pilot.Group.setGTargMode(actor1.pos.getAbsPoint(), 200F);
                                    Voice.speakOk(aircraft);
                                }

                                // TODO: Fixed by SAS~Storebror: WHAT THE FLYING FUCK???
                                //       Eating up CPU cycles for what?
//                                int l1;
//                                for(l1 = 0; l1 < 1000; l1++);
//                                if(l1 == 1000)
//                                    pilot.Group.setGroupTask(1);
                                if (pilot.Group != null) {
                                    pilot.Group.setGroupTask(1);
                                }
                            }
                        }

                    } else {
                        HUD.logCenter("                                                                             Request CAS at map grid " + s1 + "-" + i1);
                    }
                }
            }

            if (!flag && this.popped && !this.BDA) {
                for (Entry entry2 = Engine.name2Actor().nextEntry(null); entry2 != null; entry2 = Engine.name2Actor().nextEntry(entry2)) {
                    Actor actor2 = (Actor) entry2.getValue();
                    if (!Actor.isAlive(actor2) && ((actor2 instanceof TankGeneric) || (actor2 instanceof ArtilleryGeneric) || (actor2 instanceof CarGeneric)) && (actor2.getArmy() != this.myArmy) && (actor2.pos.getAbsPoint().distance(this.point3d) < 500D)) {
                        if ((actor2 instanceof ArtilleryCY6.ProneInfantry) || (actor2 instanceof ArtilleryCY6.DugInInfantry)) {
                            this.troops += 5;
                        } else if (actor2 instanceof TgtTank) {
                            this.armor++;
                        } else if ((actor2 instanceof CarGeneric) || (actor2 instanceof TgtVehicle)) {
                            this.trucks++;
                        } else if (actor2 instanceof ArtilleryGeneric) {
                            this.guns++;
                        }
                        if (this.armor > 0) {
                            e1 = "" + this.armor + " tank ";
                            if (this.armor > 1) {
                                e1 = "" + this.armor + "tanks ";
                            }
                        }
                        if (this.guns > 0) {
                            e2 = "" + this.guns + " emplacement ";
                            if (this.guns > 1) {
                                e1 = "" + this.guns + " emplacements ";
                            }
                        }
                        if (this.trucks > 0) {
                            e3 = "" + this.trucks + " vehicle ";
                            if (this.trucks > 1) {
                                e3 = "" + this.trucks + " vehicles ";
                            }
                        }
                        if (this.troops > 0) {
                            e4 = "" + this.troops + " troops";
                        }
                        String s = e1 + e2 + e3 + e4 + " destroyed.";
                        if ((this.troops + this.armor + this.trucks + this.guns) <= 0) {
                            s = " No targets hit.";
                        }
                        HUD.logCenter("                                                                             Thanks for the help! BDA: " + s);
                        this.BDA = true;
                    }
                }

            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d       point3d = new Point3d();

        private boolean       active;
        private boolean       flag2;
//        private int spread;
        private int           armor;
        private int           trucks;
        private int           guns;
        private int           troops;
        private static String e1      = "";
        private static String e2      = "";
        private static String e3      = "";
        private static String e4      = "";
        private boolean       popped;
        private boolean       BDA;

        public FACUnit() {
            this.active = false;
            this.flag2 = false;
//            spread = 0;
            this.armor = 0;
            this.trucks = 0;
            this.guns = 0;
            this.troops = 0;
            this.popped = false;
            this.BDA = false;
            this.Timer1 = this.Timer2 = 90F;
        }
    }

    public static class DZUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (Actor.isAlive(actor) && (actor != World.getPlayerAircraft()) && (actor instanceof Aircraft) && (actor.getArmy() == this.myArmy)) {
                    this.pos.getAbs(this.point3d);
                    double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int) Math.ceil(Math.sqrt((d5 * d5) + (d4 * d4)));
                    if (i <= 1000) {
                        //                        ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor.postDestroy();
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public DZUnit() {
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class DynamicWeatherUnit extends CandCGeneric {

        public boolean danger() {
            int i = Mission.cur().sectFile().get("Main", "DWInterval", 20) * 60;
            this.resetTimer(i);
            // Random random = new Random();
            int j = TrueRandom.nextInt(100);
            if (j <= 33) {
                this.clouds++;
            }
            if ((j > 33) && (j < 66)) {
                this.clouds--;
            }
            if (this.clouds < this.bestclouds) {
                this.clouds = this.bestclouds;
            }
            if (this.clouds > this.worstclouds) {
                this.clouds = this.worstclouds;
            }
            j = TrueRandom.nextInt(100);
            if (j <= 33) {
                this.height += 200;
            } else if ((j > 33) && (j < 66)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                this.height -= 200;
            } else if (this.height < 200) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                this.height = 200;
            } else if (this.height > 2000) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                this.height = 2000;
            }
            if ((Mission.cur() != null) && this.start) {
                this.clouds = Mission.cur().sectFile().get("Main", "CloudType", 2);
                this.height = Mission.cur().sectFile().get("Main", "CloudHeight", 1500);
            }
            if ((Mission.cur() != null) && this.start) {
                this.bestclouds = Mission.cur().sectFile().get("Mods", "BestClouds", 0);
                if ((this.bestclouds < 0) || (this.bestclouds > 7)) {
                    this.bestclouds = 0;
                }
            }
            if ((Mission.cur() != null) && this.start) {
                this.worstclouds = Mission.cur().sectFile().get("Mods", "WorstClouds", 7);
                if ((this.worstclouds < 0) || (this.worstclouds > 7)) {
                    this.worstclouds = 7;
                }
            }
            if (this.worstclouds <= this.bestclouds) {
                this.worstclouds = 7;
                this.bestclouds = 0;
            }
            this.start = false;
            Mission.createClouds(this.clouds, this.height);
            World.land().cubeFullUpdate();
            return true;
        }

        private int     clouds;
        private int     height;
//        private int startclouds;
//        private int startheight;
        private int     bestclouds;
        private int     worstclouds;
        private boolean start;

        public DynamicWeatherUnit() {
            this.clouds = 2;
            this.height = 1500;
//            startclouds = 2;
//            startheight = 1500;
            this.bestclouds = 0;
            this.worstclouds = 7;
            this.start = true;
            this.Timer1 = this.Timer2 = 1200F;
        }
    }

    public static class CWUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor == World.getPlayerAircraft()) && (actor.pos.getAbsPoint().distance(this.point3d) < 10000D)) {
                    flag = true;
                }
            }

            Aircraft aircraft = World.getPlayerAircraft();
            double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
            for (Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
                Actor actor1 = (Actor) entry1.getValue();
                if (Actor.isAlive(actor1) && ((actor1 instanceof BigshipGeneric) || (actor1 instanceof ShipGeneric)) && (actor1.getArmy() != this.myArmy) && (actor1.pos.getAbsPoint().distance(this.point3d) < 5000D)) {
                    this.pos.getAbs(this.point3d);
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                    double d4 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
                    char c = (char) (int) (65D + Math.floor(((d3 / 676D) - Math.floor(d3 / 676D)) * 26D));
                    char c1 = (char) (int) (65D + Math.floor(((d3 / 26D) - Math.floor(d3 / 26D)) * 26D));
                    // new String();
                    String s;
                    if (d2 > 260D) {
                        s = "" + c + c1;
                    } else {
                        s = "" + c1;
                    }
                    // new String();
//                    double d5 = (double)(int)(Math.floor(actor1.pos.getAbsPoint().z) / 100D) * 100D;
//                    double d6 = (int)(Math.floor((actor1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    // new String();
                    int i = (int) Math.ceil(d4);
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    double d9 = Math.floor((int) f) - 90D;
                    if (d9 < 0.0D) {
                        d9 = 360D + d9;
                    }
                    if (!flag) {
                        HUD.logCenter("                                                                     Enemy ship spotted at map grid " + s + "-" + i);
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public CWUnit() {
            this.Timer1 = this.Timer2 = 30F;
        }
    }

    public static class HighReconUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if (this.footage == 10) {
                HUD.logCenter("                                          Run Complete!");
                World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            if ((aircraft.pos.getAbsPoint().distance(this.point3d) <= 15000D) && (aircraft.getArmy() == this.myArmy) && (aircraft.pos.getAbsPoint().distance(this.point3d) <= 8000D)) {
                int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
                if (i < 0) {
                    i += 360;
                } else if (i > 360) {
                    i -= 360;
                }
                double d = 57.32484D * Math.atan2(this.point3d.y - aircraft.pos.getAbsPoint().y, -(this.point3d.x - aircraft.pos.getAbsPoint().x));
                int j = (int) (Math.floor((int) d) - 90D);
                if (j < 0) {
                    j += 360;
                } else if (j > 360) {
                    j -= 360;
                }
                int k = j - i;
                if (k < 0) {
                    k += 360;
                } else if (k > 360) {
                    k -= 360;
                }
                int l = (int) aircraft.pos.getAbsOrient().getRoll();
                int i1 = (int) aircraft.pos.getAbsOrient().getPitch();
                VisibilityChecker.checkLandObstacle = true;
                VisibilityChecker.checkCabinObstacle = false;
                VisibilityChecker.checkPlaneObstacle = false;
                VisibilityChecker.checkObjObstacle = false;
                VisibilityChecker.targetPosInput = this.point3d;
                float f = Main3D.cur3D().clouds.getVisibility(aircraft.pos.getAbsPoint(), this.point3d);
                double d1 = Math.sqrt(Math.pow(this.point3d.y - aircraft.pos.getAbsPoint().y, 2D) + Math.pow(this.point3d.x - aircraft.pos.getAbsPoint().x, 2D));
                boolean flag = false;
                if ((((k <= 25) && (k >= 0)) || ((k >= 335) && (k <= 360))) && (l > 350) && (l < 370) && (i1 > 350) && (i1 < 370) && ((aircraft.pos.getAbsPoint().z - this.point3d.z) > 1000D)) {
                    flag = true;
                }
                if ((d1 < 2000D) && (f < 1.0D)) {
                    HUD.logCenter("                                          Target Obscured!");
                    this.footage = 0;
                } else if (flag && (d1 < 2000D) && (f >= 1.0D)) {
                    this.rolling = true;
                    if (this.footage <= 10) {
                        HUD.logCenter("                                          Rolling..." + (this.footage * 10) + "%");
                        this.footage++;
                    }
                } else if (!flag && (d1 < 2000D) && (this.footage <= 10) && this.rolling) {
                    this.footage = 0;
                    this.rolling = false;
                    HUD.logCenter("                                          Run Incomplete. Resetting.");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     footage;
        private boolean rolling;

        public HighReconUnit() {
            this.footage = 0;
            this.rolling = false;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class LowReconUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if (this.footage == 10) {
                HUD.logCenter("                                          Run Complete!");
                World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            if ((aircraft.pos.getAbsPoint().distance(this.point3d) <= 5000D) && (aircraft.getArmy() == this.myArmy) && (aircraft.pos.getAbsPoint().distance(this.point3d) <= 2000D)) {
                int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
                if (i < 0) {
                    i += 360;
                } else if (i > 360) {
                    i -= 360;
                }
                double d = 57.32484D * Math.atan2(this.point3d.y - aircraft.pos.getAbsPoint().y, -(this.point3d.x - aircraft.pos.getAbsPoint().x));
                int j = (int) (Math.floor((int) d) - 90D);
                if (j < 0) {
                    j += 360;
                } else if (j > 360) {
                    j -= 360;
                }
                int k = j - i;
                if (k < 0) {
                    k += 360;
                } else if (k > 360) {
                    k -= 360;
                }
                int l = (int) aircraft.pos.getAbsOrient().getRoll();
                int i1 = (int) aircraft.pos.getAbsOrient().getPitch();
                if ((((k <= 25) && (k >= 0)) || ((k >= 335) && (k <= 360))) && (l > 350) && (l < 370) && (i1 > 350) && (i1 < 370) && ((aircraft.pos.getAbsPoint().z - this.point3d.z) > 50D) && ((aircraft.pos.getAbsPoint().z - this.point3d.z) < 500D)) {
                    this.rolling = true;
                    if (this.footage <= 10) {
                        HUD.logCenter("                                          Rolling..." + (this.footage * 10) + "%");
                        this.footage++;
                    }
                } else if ((this.footage <= 10) && this.rolling) {
                    this.footage = 0;
                    this.rolling = false;
                    HUD.logCenter("                                          Run Incomplete. Resetting.");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int     footage;
        private boolean rolling;

        public LowReconUnit() {
            this.footage = 0;
            this.rolling = false;
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class CorkscrewUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof TypeBomber) && (actor.getArmy() == this.myArmy) && (actor.getSpeed(this.vector3d) > 20D)) {
                    Aircraft aircraft = War.GetNearestEnemyAircraft(actor, 500F, 9);
                    if (aircraft != null) {
                        double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                        int i = (int) (-(actor.pos.getAbsOrient().getYaw() - 90D));
                        if (i < 0) {
                            i = 360 + i;
                        }
                        double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                        double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                        double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                        // new String();
                        double d6 = (int) Math.ceil(d2 - d5);
                        // new String();
                        double d7 = d3 - d;
                        double d8 = d4 - d1;
                        float f = 57.32484F * (float) Math.atan2(d8, -d7);
                        int j = (int) (Math.floor((int) f) - 90D);
                        if (j < 0) {
                            j = 360 + j;
                        }
                        int k = j - i;
                        if (k < 0) {
                            k = 360 + k;
                        }
                        boolean flag = false;
                        if ((k >= 90) && (k <= 270)) {
                            flag = true;
                        }
                        String s = "left";
                        if ((k <= 180) && (k >= 90)) {
                            s = "right";
                        }
//                        double d9 = d - d3;
//                        double d10 = d1 - d4;
                        // Random random = new Random();
                        int l = TrueRandom.nextInt(100);
                        if ((actor.pos.getAbsPoint().distance(aircraft.pos.getAbsPoint()) <= 500D) && (l <= 10) && flag && (d6 < 300D)) {
                            if (actor != World.getPlayerAircraft()) {
                                ((Pilot) ((Aircraft) actor).FM).AP.setWayPoint(false);
                                ((Maneuver) ((Aircraft) actor).FM).Group.setGroupTask(0);
                                ((Pilot) ((Aircraft) actor).FM).set_task(0);
                                ((Pilot) ((Aircraft) actor).FM).set_maneuver(54);
                            } else {
                                HUD.logCenter("                                          Corkscrew " + s + ", now!");
                            }
                        }
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        public CorkscrewUnit() {
            this.Timer1 = this.Timer2 = 1.0F;
        }
    }

    public static class ASVmkVIIIUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft())) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
                    int i2 = l1 - j;
                    k1 = (int) (k1 / 1000D);
                    int j2 = (int) Math.ceil(k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if (actor instanceof ShipGeneric) {
                        byte0 = 50;
                    }
                    if (actor instanceof BigshipGeneric) {
                        byte0 = 80;
                    }
                    if ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) {
                        byte0 = 15;
                    }
                    if ((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf)) {
                        byte0 = 30;
                    }
                    if ((k1 <= byte0) && (i2 >= 210) && (i2 <= 270) && (Math.sqrt(j1 * j1) <= 60D)) {
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASVmkVIIIUnit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class ASVUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof BigshipGeneric) || ((actor instanceof ShipGeneric) && Actor.isAlive(actor))) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
//                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
                    // new String();
                    double d6 = d3 - d;
                    double d7 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d7, -d6);
                    int j = (int) (Math.floor((int) f) - 90D);
                    if (j < 0) {
                        j = 360 + j;
                    }
                    int k = j - i;
                    double d8 = d - d3;
                    double d9 = d1 - d4;
                    String s = "Ship";
                    byte byte0 = 30;
                    if (actor instanceof BigshipGeneric) {
                        s = "Warship";
                    }
                    if ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) {
                        s = "Periscope";
                        byte0 = 5;
                    }
                    if ((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf)) {
                        s = "Surfaced sub";
                        byte0 = 15;
                    }
                    int l = (int) (Math.ceil(Math.sqrt((d9 * d9) + (d8 * d8))) / 1000D);
                    if ((l <= byte0) && (d2 <= 3000D) && (Math.sqrt(k * k) <= 60D)) {
                        HUD.logCenter("                                              Contact: " + s + ", bearing " + j + "\260" + ", range " + l + "km");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASVUnit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class ASGUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft())) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
                    int i2 = l1 - j;
                    k1 = (int) (k1 / 1000D);
                    int j2 = (int) Math.ceil(k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if (actor instanceof ShipGeneric) {
                        byte0 = 50;
                    }
                    if (actor instanceof BigshipGeneric) {
                        byte0 = 64;
                    }
                    if ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) {
                        byte0 = 10;
                    }
                    if ((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf)) {
                        byte0 = 29;
                    }
                    if ((k1 <= byte0) && (i2 >= 210) && (i2 <= 270) && (Math.sqrt(j1 * j1) <= 60D)) {
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASGUnit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class ASDUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft())) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
                    int i2 = l1 - j;
                    k1 = (int) (k1 / 1000D);
                    int j2 = (int) Math.ceil(k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if (actor instanceof ShipGeneric) {
                        byte0 = 30;
                    }
                    if (actor instanceof BigshipGeneric) {
                        byte0 = 40;
                    }
                    if ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) {
                        byte0 = 3;
                    }
                    if ((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf)) {
                        byte0 = 15;
                    }
                    if ((k1 <= byte0) && (i2 >= 210) && (i2 <= 270) && (Math.sqrt(j1 * j1) <= 60D)) {
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASDUnit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class ANAPS4Unit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((((actor instanceof Aircraft) && (actor.getSpeed(this.vector3d) > 20D)) || (actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft())) {
                    this.pos.getAbs(this.point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    String s = ", level with us";
                    if ((d2 - d5 - 200D) >= 0.0D) {
                        s = ", below us";
                    } else if (((d2 - d5) + 200D) < 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = ", above us";
                    }
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d8, -d7);
                    int i1 = (int) (Math.floor((int) f) - 90D);
                    if (i1 < 0) {
                        i1 = 360 + i1;
                    }
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                    float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f1) - 90D);
                    if (l1 < 0) {
                        l1 = 360 + l1;
                    }
                    int i2 = l1 - j;
                    k1 = (int) (k1 / 1000D);
                    int j2 = (int) Math.ceil(k1 * 0.621371192D);
                    String s1 = "Surface Contact";
                    byte byte0 = 9;
                    if (actor instanceof Aircraft) {
                        s1 = "Aircraft";
                    }
                    if (!(actor instanceof Aircraft)) {
                        s = " ";
                    }
                    if (actor instanceof ShipGeneric) {
                        byte0 = 40;
                    }
                    if (actor instanceof BigshipGeneric) {
                        byte0 = 55;
                    }
                    if ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) {
                        byte0 = 5;
                    }
                    if ((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf)) {
                        byte0 = 30;
                    }
                    if ((k1 <= byte0) && (i2 >= 255) && (i2 <= 285) && (Math.sqrt(j1 * j1) <= 35D)) {
                        HUD.logCenter("                                              " + s1 + " bearing " + i1 + "\260" + ", range " + j2 + " miles" + s);
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        public ANAPS4Unit() {
            this.Timer1 = this.Timer2 = 5F;
        }
    }

    public static class ErrorUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Aircraft aircraft = World.getPlayerAircraft();
                this.pos.getAbs(this.point3d);
                if (aircraft.pos.getAbsPoint().distance(this.point3d) < 10000D) {
                    boolean flag = ((Maneuver) aircraft.FM).hasBombs();
                    double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
//                    double d2 = World.land().HQ(point3d.x, point3d.y);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
//                    double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                    double d6 = aircraft.getSpeed(this.vector3d);
                    double d7 = ((Maneuver) aircraft.FM).getAltitude();
                    double d8 = d - d3;
                    double d9 = d1 - d4;
                    float f = 57.32484F * (float) Math.atan2(d9, -d8);
                    double d10 = Math.floor((int) f) - 90D;
                    if (d10 < 0.0D) {
                        d10 = 360D + d10;
                    }
                    double d11 = (-aircraft.pos.getAbsOrient().getYaw()) + 90D;
                    if (d11 < 0.0D) {
                        d11 += 360D;
                    }
                    double d12 = d3 - d;
                    double d13 = d4 - d1;
                    double d14 = Math.sqrt((d13 * d13) + (d12 * d12));
                    double d15 = d7 - World.land().HQ(this.point3d.x, this.point3d.y);
                    if (d15 < 0.0D) {
                        d15 = 0.0D;
                    }
                    double d16 = d6 * Math.sqrt(d15 * 0.20386999845504761D);
                    if (!this.marked && !flag) {
                        this.error = (float) (d14 - d16);
                        this.marked = true;
                        HUD.logCenter("                                                                             Bombs Gone! Aimpoint Error: " + (int) this.error + "m");
                        if (this.error < 100F) {
                            World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
                        }
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    error;
        private boolean  marked;
//        private int randomInt;
//        private int maxerror;

        public ErrorUnit() {
            this.error = 0.0F;
            this.marked = false;
//            randomInt = 0;
//            maxerror = 300;
            this.Timer1 = this.Timer2 = 0.5F;
        }
    }

    public static class AimpointUnit extends CandCGeneric {

        public boolean danger() {
            if (!this.active) {
                return false;
            }
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                this.pos.getAbs(this.point3d);
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof TypeBomber) || ((actor instanceof TypeStormovik) && (actor.getArmy() == this.myArmy) && (actor.pos.getAbsPoint().distance(this.point3d) < 10000D))) {
                    boolean flag = ((Maneuver) ((Aircraft) actor).FM).hasBombs();
                    double d = Main3D.cur3D().land2D.worldOfsX() + this.point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + this.point3d.y;
//                    double d2 = World.land().HQ(point3d.x, point3d.y);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
//                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    double d6 = actor.getSpeed(this.vector3d);
                    double d7 = ((Maneuver) ((Aircraft) actor).FM).getAltitude();
                    double d8 = d - d3;
                    double d9 = d1 - d4;
                    float f = 57.32484F * (float) Math.atan2(d9, -d8);
                    double d10 = Math.floor((int) f) - 90D;
                    if (d10 < 0.0D) {
                        d10 = 360D + d10;
                    }
                    double d11 = (-actor.pos.getAbsOrient().getYaw()) + 90D;
                    if (d11 < 0.0D) {
                        d11 += 360D;
                    }
                    double d12 = d3 - d;
                    double d13 = d4 - d1;
                    double d14 = Math.sqrt((d13 * d13) + (d12 * d12));
                    double d15 = d7 - World.land().HQ(this.point3d.x, this.point3d.y);
                    if (d15 < 0.0D) {
                        d15 = 0.0D;
                    }
                    double d16 = d6 * Math.sqrt(d15 * 0.20386999845504761D);
                    if (!this.marked && (actor == World.getPlayerAircraft()) && !flag) {
                        this.error = (float) (d14 - d16);
                        this.marked = true;
                        HUD.logCenter("                                                                             Bombs Gone! Aimpoint Error: " + (int) this.error);
                    }
                    // Random random = new Random();
                    int i = Mission.cur().sectFile().get("Mods", "AimpointError", 300);
                    this.randomInt = TrueRandom.nextInt(i);
                    if ((Mission.cur() != null) && (Mission.cur().sectFile().get("Mods", "AimpointNoMark", 0) == 1)) {
                        this.error = 0.0F;
                    }
                    if ((Mission.cur() != null) && (Mission.cur().sectFile().get("Mods", "AimpointMode", 0) != 1)) {
                        d16 += this.randomInt + this.error;
                    }
                    if (d14 <= d16) {
                        ((Pilot) ((Aircraft) actor).FM).bombsOut = true;
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    error;
        private boolean  marked;
        private int      randomInt;
//        private int maxerror;
        public boolean   active;

        public AimpointUnit() {
            this.error = 0.0F;
            this.marked = false;
            this.randomInt = 0;
//            maxerror = 300;
            this.active = true;
            this.Timer1 = this.Timer2 = 1.0F;
            this.delay = 10F;
        }
    }

    public static class AImkXVUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(10) - 5F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    int i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D * f2);
                    float f3 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f3) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil((i2 * 3.2808399D) / 100D) * 100D);
                    int i3 = (int) (Math.ceil((i2 * 3.2808399D) / 300D) * 300D);
                    int j3 = 0;
                    String s1 = "ft";
                    if (l2 >= 5280) {
                        l2 = (int) Math.floor(l2 / 5280);
                        s1 = "mi";
                        j3 = 1;
                    }
                    int k3 = k1 + l1;
                    int l3 = k3;
                    if (l3 < 0) {
                        l3 += 360;
                    }
                    int i4 = (int) (Math.round(j1 / 10D) * 10D);
                    if (Math.sqrt(k1 * k1) <= 20D) {
                        i4 = (int) (Math.round(j1 / 5D) * 5D);
                    }
                    float f4 = 6000F;
                    float f5 = f4;
                    if (d3 < 1524D) {
                        f5 = f4 * (((float) d3 / 6096F) + 0.75F);
                        if ((d6 < d2) && !flag1) {
                            f5 = (float) d3 * 4F;
                        }
                    }
                    float f6 = (float) Math.toDegrees(Math.atan(d3 / f4));
                    float f7 = 90F + f6;
                    int j4 = (int) (f5 * Math.cos(Math.toRadians(k2)));
                    float f8 = j4;
                    if (j > f7) {
                        f8 = j4 * (float) ((Math.cos(Math.toRadians((j - 90) * (f7 / 45F))) * 0.5D) + 0.5D);
                    }
                    String s2 = "  ";
                    String s3 = "  ";
                    if (l3 <= 2) {
                        s2 = "Dead ahead, ";
                    } else if ((l3 > 2) && (l3 <= 7)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 5, ";
                    } else if ((l3 > 7) && (l3 <= 12)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 10, ";
                    } else if ((l3 > 12) && (l3 <= 17)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 15, ";
                    } else if ((l3 > 17) && (l3 <= 25)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 20, ";
                    } else if ((l3 > 25) && (l3 <= 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 30, ";
                    } else if ((l3 > 35) && (l3 <= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 40, ";
                    } else if ((l3 > 45) && (l3 <= 55)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 50, ";
                    } else if ((l3 > 55) && (l3 <= 65)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 60, ";
                    } else if ((l3 > 65) && (l3 <= 75)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 70, ";
                        s3 = ", evading";
                    } else if (l3 > 357) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Dead ahead, ";
                    } else if ((l3 < 358) && (l3 > 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 5, ";
                    } else if ((l3 < 353) && (l3 > 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 10, ";
                    } else if ((l3 < 348) && (l3 > 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 15, ";
                    } else if ((l3 < 343) && (l3 >= 335)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 20, ";
                    } else if ((l3 < 335) && (l3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 30, ";
                    } else if ((l3 < 325) && (l3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 40, ";
                    } else if ((l3 < 315) && (l3 >= 305)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 50, ";
                    } else if ((l3 < 305) && (l3 >= 295)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 60, ";
                    } else if ((l3 < 295) && (l3 >= 285)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 70, ";
                        s3 = ", evading";
                    }
                    String s4 = "  ";
                    if ((k2 >= -12) && (k2 < -10)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        s4 = "down 12, ";
                        s3 = ", evading";
                    } else if ((k2 > -11) && (k2 < -7)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    {
                        s4 = "down 9, ";
                        s3 = ", evading";
                    } else if ((k2 >= -7) && (k2 < -4)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 6, ";
                    } else if ((k2 >= -4) && (k2 < -1)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 3, ";
                    } else if ((k2 >= -1) && (k2 <= 2)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "level, ";
                    } else if ((k2 <= 12) && (k2 > 10)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    {
                        s4 = "up 12, ";
                        s3 = ", evading";
                    } else if ((k2 < 11) && (k2 > 7)) // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                    {
                        s4 = "up 9, ";
                        s3 = ", evading";
                    } else if ((k2 < 8) && (k2 > 4)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 6, ";
                    } else if ((k2 < 5) && (k2 > 1)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 3, ";
                    }
                    if (((j > f7) && !flag1) || ((d3 < 1524D) && (d6 < d2) && !flag1)) {
                        s3 = ", ground returns";
                    }
                    if ((j - 40) > f7) {
                        s3 = ", ground clutter";
                    }
                    if (((double) i2 <= (double) f8) && (j3 > 0) && (k2 >= -12) && (k2 <= 12) && (Math.abs(k3) <= 75)) {
                        HUD.logCenter("                                          RO: Target bearing " + i4 + "\260" + ", " + s + ", " + l2 + s1 + s3);
                        this.freq = 6F;
                    } else if (((double) i2 <= (double) f8) && (j3 < 1) && (i2 >= 75) && (k2 >= -12) && (k2 <= 12) && (Math.abs(k3) <= 75)) {
                        HUD.logCenter("                                          RO: " + s2 + s4 + i3 + "ft" + s3);
                        this.freq = 3F;
                    } else {
                        this.freq = 6F;
                        super.resetTimer(this.freq);
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public AImkXVUnit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class AImkXUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(10) - 5F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    int i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D * f2);
                    float f3 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f3) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    float f4 = (float) (Math.ceil((i2 * 3.2808399D) / 100D) * 100D);
                    int l2 = (int) (Math.ceil((i2 * 3.2808399D) / 300D) * 300D);
                    int i3 = 0;
                    String s1 = "ft";
                    if (f4 >= 5280F) {
                        f4 = (float) (Math.round(f4 / 5280D / 0.5D) * 0.5D);
                        s1 = "mi";
                        i3 = 1;
                    }
                    int j3 = k1 + l1;
                    int k3 = j3;
                    if (k3 < 0) {
                        k3 += 360;
                    }
//                    int l3 = j2 - j;
                    int i4 = (int) (Math.round(j1 / 5D) * 5D);
                    float f5 = 10000F;
                    float f6 = f5;
                    if (d3 < 2130D) {
                        f6 = f5 * (((float) d3 / 8520F) + 0.75F);
                        if ((d6 < d2) && !flag1) {
                            f6 = (float) ((d3 * 2D) + Math.pow(d3, 1.12935D));
                        }
                    }
                    float f7 = (float) Math.toDegrees(Math.atan(d3 / f5));
                    float f8 = 90F + f7;
                    int j4 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    float f9 = j4;
                    if (j > f8) {
                        f9 = j4 * (float) ((Math.cos(Math.toRadians((j - 90) * (f8 / 45F))) * 0.5D) + 0.5D);
                    }
                    String s2 = "  ";
                    if ((i >= 348.75D) && (i < 11.25D)) {
                        s2 = ", heading north";
                    } else if ((i >= 11.25D) && (i < 33.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading NNE";
                    } else if ((i >= 33.75D) && (i < 56.25D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading NE";
                    } else if ((i >= 56.25D) && (i < 78.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading ENE";
                    } else if ((i >= 78.75D) && (i < 101.25D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading east";
                    } else if ((i >= 101.25D) && (i < 123.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading ESE";
                    } else if ((i >= 123.75D) && (i < 146.25D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading SE";
                    } else if ((i >= 146.25D) && (i < 168.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading SSE";
                    } else if ((i >= 168.75D) && (i < 191.25D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading south";
                    } else if ((i >= 191.25D) && (i < 213.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading SSW";
                    } else if ((i >= 213.75D) && (i < 236.25D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading SW";
                    } else if ((i >= 236.25D) && (i < 258.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading WSW";
                    } else if ((i >= 258.75D) && (i < 281.25D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading west";
                    } else if ((i >= 281.25D) && (i < 303.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading WNW";
                    } else if ((i >= 303.75D) && (i < 326.25D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading NW";
                    } else if ((i >= 326.25D) && (i < 348.75D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = ", heading NNW";
                    }
                    String s3 = "  ";
                    String s4 = "  ";
                    if (k3 <= 3) {
                        s3 = "Dead ahead, ";
                    } else if ((k3 > 3) && (k3 <= 7)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 5, ";
                    } else if ((k3 > 7) && (k3 <= 12)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 10, ";
                    } else if ((k3 > 12) && (k3 <= 17)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 15, ";
                    } else if ((k3 > 17) && (k3 <= 22)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 20, ";
                    } else if ((k3 > 22) && (k3 <= 27)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 25, ";
                    } else if ((k3 > 27) && (k3 <= 32)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 30, ";
                    } else if ((k3 > 32) && (k3 <= 37)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 35, ";
                    } else if ((k3 > 37) && (k3 <= 42)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 40, ";
                    } else if ((k3 > 42) && (k3 <= 47)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 45, ";
                    } else if ((k3 > 47) && (k3 <= 55)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 50, ";
                    } else if ((k3 > 55) && (k3 <= 65)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 60, ";
                    } else if ((k3 > 65) && (k3 <= 75)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Right 70, ";
                        s4 = ", evading";
                    } else if (k3 >= 357) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Dead ahead, ";
                    } else if ((k3 < 357) && (k3 >= 352)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 5, ";
                    } else if ((k3 < 352) && (k3 >= 347)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 10, ";
                    } else if ((k3 < 347) && (k3 >= 342)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 15, ";
                    } else if ((k3 < 342) && (k3 >= 337)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 20, ";
                    } else if ((k3 < 337) && (k3 >= 332)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 25, ";
                    } else if ((k3 < 332) && (k3 >= 327)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 30, ";
                    } else if ((k3 < 327) && (k3 >= 322)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 35, ";
                    } else if ((k3 < 322) && (k3 >= 317)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 40, ";
                    } else if ((k3 < 317) && (k3 >= 312)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 45, ";
                    } else if ((k3 < 312) && (k3 >= 305)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 50, ";
                    } else if ((k3 < 305) && (k3 >= 295)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 60, ";
                    } else if ((k3 < 295) && (k3 >= 285)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "Left 70, ";
                        s4 = ", evading";
                    }
                    String s5 = "  ";
                    if ((k2 >= -5) && (k2 <= -3)) {
                        s5 = "down a bit, ";
                        s4 = ", evading";
                    } else if ((k2 >= -3) && (k2 <= 3)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "level, ";
                    } else if ((k2 <= 40) && (k2 >= 37)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up 40, ";
                        s4 = ", evading";
                    } else if ((k2 <= 37) && (k2 > 33)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up 35, ";
                    } else if ((k2 <= 33) && (k2 > 27)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up 30, ";
                    } else if ((k2 <= 27) && (k2 > 23)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up 25, ";
                    } else if ((k2 <= 23) && (k2 > 17)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up 20, ";
                    } else if ((k2 <= 17) && (k2 > 13)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up 15, ";
                    } else if ((k2 <= 13) && (k2 > 7)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up 10, ";
                    } else if ((k2 < 7) && (k2 > 3)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s5 = "up a bit, ";
                    }
                    if (((j > f8) && !flag1) || ((d3 < 2130D) && (d6 < d2) && !flag1)) {
                        s4 = ", ground returns";
                        s2 = ", ground returns";
                    }
                    if ((j - 40) > f8) {
                        s4 = ", ground clutter";
                        s2 = ", ground clutter";
                    }
                    if (((double) i2 <= (double) f9) && (i3 > 0) && (k2 >= -20) && (k2 <= 40) && (Math.sqrt(j3 * j3) <= 75D)) {
                        HUD.logCenter("                                          RO: Target bearing " + i4 + "\260" + ", " + s + ", " + f4 + s1 + s2);
                        this.freq = 8F;
                    } else if (((double) i2 <= (double) f9) && (i3 < 1) && (i2 >= 115D) && (k2 >= -5) && (k2 <= 40) && (Math.sqrt(j3 * j3) <= 75D)) {
                        HUD.logCenter("                                          RO: " + s3 + s5 + l2 + "ft" + s4);
                        this.freq = 4.6F;
                    } else {
                        this.freq = 8F;
                    }
                    super.resetTimer(this.freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public AImkXUnit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class AImkVIIIUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(10) - 5F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(2) - 1;
                    int i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D * f2);
                    float f3 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f3) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil((i2 * 3.2808399D) / 100D) * 100D);
                    int i3 = (int) (Math.ceil((i2 * 3.2808399D) / 300D) * 300D);
                    int j3 = 0;
                    String s1 = "ft";
                    if (l2 >= 5280) {
                        l2 = (int) Math.floor(l2 / 5280);
                        s1 = "mi";
                        j3 = 1;
                    }
                    int k3 = k1 + l1;
                    int l3 = k3;
                    if (l3 < 0) {
                        l3 += 360;
                    }
                    int i4 = (int) (Math.round(j1 / 10D) * 10D);
                    if (Math.sqrt(k1 * k1) <= 5D) {
                        i4 = j1;
                    }
                    float f4 = 9000F;
                    float f5 = f4;
                    if (d3 < 2000D) {
                        f5 = f4 * (((float) d3 / 8000F) + 0.75F);
                        if ((d6 < d2) && !flag1) {
                            f5 = (float) ((d3 * 2D) + Math.pow(d3, 1.1205D));
                        }
                    }
                    float f6 = (float) Math.toDegrees(Math.atan(d3 / f4));
                    float f7 = 90F + f6;
                    float f8 = (float) (Math.cos(Math.toRadians(Math.sqrt(k1 * k1) * 1.4D)) * f5);
                    if (Math.sqrt(k1 * k1) < Math.sqrt(k2 * k2)) {
                        f8 = (float) (Math.cos(Math.toRadians(Math.sqrt(k2 * k2) * 1.4D)) * f5);
                    }
                    float f9 = f8;
                    if (j > f7) {
                        f9 = f8 * (float) ((Math.cos(Math.toRadians((j - 90) * (f7 / 45F))) * 0.5D) + 0.5D);
                    }
                    String s2 = "  ";
                    String s3 = "  ";
                    if (l3 == 0) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s2 = "Dead ahead, ";
                    } else if (l3 == 1) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 1, ";
                    } else if (l3 == 2) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 2, ";
                    } else if (l3 == 3) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 3, ";
                    } else if (l3 == 4) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 4, ";
                    } else if ((l3 > 4) && (l3 < 8)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 5, ";
                    } else if ((l3 > 7) && (l3 <= 15)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 10, ";
                    } else if ((l3 > 15) && (l3 <= 25)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 20, ";
                    } else if ((l3 > 25) && (l3 <= 35)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 30, ";
                    } else if ((l3 > 35) && (l3 <= 45)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 40, ";
                        s3 = ", evading";
                    } else if (l3 > 359) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Dead ahead, ";
                    } else if (l3 == 359) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 1, ";
                    } else if (l3 == 358) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 2, ";
                    } else if (l3 == 357) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 3, ";
                    } else if (l3 == 356) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 4, ";
                    } else if ((l3 < 356) && (l3 >= 353)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 5, ";
                    } else if ((l3 < 353) && (l3 >= 345)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 10, ";
                    } else if ((l3 < 345) && (l3 >= 335)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 20, ";
                    } else if ((l3 < 335) && (l3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 30, ";
                    } else if ((l3 < 325) && (l3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 40, ";
                        s3 = ", evading";
                    }
                    String s4 = "  ";
                    if ((k2 >= -45) && (k2 < -35)) {
                        s4 = "down 40, ";
                        s3 = ", evading";
                    } else if ((k2 >= -35) && (k2 < -25)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 30, ";
                    } else if ((k2 >= -25) && (k2 < -15)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 20, ";
                    } else if ((k2 >= -15) && (k2 < -5)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 10, ";
                    } else if (k2 == -5) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 5, ";
                    } else if (k2 == -4) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 4, ";
                    } else if (k2 == -3) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 3, ";
                    } else if (k2 == -2) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 2, ";
                    } else if (k2 == -1) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "down 1, ";
                    } else if (k2 == 0) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "level, ";
                    } else if ((k2 <= 45) && (k2 > 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 40, ";
                        s3 = ", evading";
                    } else if ((k2 <= 35) && (k2 > 25)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 30, ";
                    } else if ((k2 <= 25) && (k2 > 15)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 20, ";
                    } else if ((k2 <= 15) && (k2 > 5)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 10, ";
                    } else if (k2 == 5) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 5, ";
                    } else if (k2 == 4) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 4, ";
                    } else if (k2 == 3) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 3, ";
                    } else if (k2 == 2) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 2, ";
                    } else if (k2 == 1) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "up 1, "; // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    }
                    if (((j > f7) && !flag1) || ((d3 < 2000D) && (d6 < d2) && !flag1)) {
                        s3 = ", ground returns";
                    }
                    if ((j - 40) > f7) {
                        s3 = ", ground clutter";
                    }
                    if (((double) i2 <= (double) f9) && (j3 > 0) && (k2 >= -45) && (k2 <= 45) && (Math.abs(k3) <= 45)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        HUD.logCenter("                                          RO: Target bearing " + i4 + "\260" + ", " + s + ", " + l2 + s1 + s3);
                        this.freq = 6F;
                    } else if (((double) i2 <= (double) f9) && (j3 < 1) && (i2 >= 120) && (k2 >= -45) && (k2 <= 45) && (Math.abs(k3) <= 45)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        HUD.logCenter("                                          RO: " + s2 + s4 + i3 + "ft" + s3);
                        this.freq = 3F;
                    } else {
                        this.freq = 6F;
                    }
                    super.resetTimer(this.freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public AImkVIIIUnit() {
            this.freq = 8F;
            this.Timer1 = this.Timer2 = this.freq;
        }
    }

    public static class AImkIVUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
            if (d3 < 0.0D) {
                d3 = 0.0D;
            }
            int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
            if (i < 0) {
                i = 360 + i;
            }
            int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
            if (j < 0) {
                j = 360 + j;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if ((actor instanceof Aircraft) && (actor.getArmy() != this.myArmy) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(this.vector3d) > 20D)) {
                    this.pos.getAbs(this.point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if ((i1 >= 28) && (i1 < 32) && (f < 7.5F)) {
                        flag1 = true;
                    }
                    String s = "level with us";
                    if ((d2 - d6 - 300D) >= 0.0D) {
                        s = "below us";
                    } else if (((d2 - d6) + 300D) <= 0.0D) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "above us";
                    }
                    if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                        s = "slightly below";
                    } else if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s = "slightly above";
                    }
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float) Math.atan2(d9, -d8);
                    int j1 = (int) (Math.floor((int) f1) - 90D);
                    if (j1 < 0) {
                        j1 = 360 + j1;
                    }
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                    int l1 = TrueRandom.nextInt(6) - 3;
                    float f3 = 5630F;
                    float f4 = f3;
                    if ((d3 < (1.25F * f3)) && !flag1) {
                        f4 = (float) d3 * 0.8F;
                    }
                    if ((d3 < (1.25F * f3)) && flag1) {
                        if (d3 <= (1.25F * f3 * 0.5F)) {
                            f4 = (float) (d3 * 0.8D * 2D);
                        } else {
                            f4 = f3;
                        }
                    }
                    int i2 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f2) / 10D) * 10D);
                    if (i2 > f3) {
                        i2 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                    }
                    float f5 = 57.32484F * (float) Math.atan2(i2, d7);
                    int j2 = (int) (Math.floor((int) f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int) (Math.ceil((i2 * 3.28084D) / 100D) * 100D);
                    int i3 = (int) (Math.ceil((i2 * 3.28084D) / 300D) * 300D);
                    int j3 = 0;
                    String s1 = "ft";
                    if (l2 >= 5280) {
                        l2 = (int) Math.floor(l2 / 5280);
                        s1 = "mi";
                        j3 = 1;
                    }
                    int k3 = k1 + l1;
                    int l3 = k3;
                    if (l3 < 0) {
                        l3 += 360;
                    }
                    float f6 = (float) (f4 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 2.575D)) * (f4 * 0.25D)));
                    int i4 = (int) (f6 * Math.cos(Math.toRadians(k2)));
                    String s2 = "  ";
                    if (l3 < 5) {
                        s2 = "Dead ahead, ";
                    } else if ((l3 >= 5) && (l3 <= 7)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 5, ";
                    } else if ((l3 > 7) && (l3 <= 12)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 10, ";
                    } else if ((l3 > 12) && (l3 <= 17)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 15, ";
                    } else if ((l3 > 17) && (l3 <= 25)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 20, ";
                    } else if ((l3 > 25) && (l3 <= 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 30, ";
                    } else if ((l3 > 35) && (l3 <= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Right 40, ";
                    } else if ((l3 > 45) && (l3 <= 70)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Turn right, ";
                    } else if (l3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Dead ahead, ";
                    } else if ((l3 <= 355) && (l3 >= 352)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 5, ";
                    } else if ((l3 < 352) && (l3 >= 347)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 10, ";
                    } else if ((l3 < 347) && (l3 >= 342)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 15, ";
                    } else if ((l3 < 342) && (l3 >= 335)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 20, ";
                    } else if ((l3 < 335) && (l3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 30, ";
                    } else if ((l3 < 325) && (l3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Left 40, ";
                    } else if ((l3 < 345) && (l3 >= 290)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s2 = "Turn left, ";
                    }
                    String s3 = "  ";
                    if (k2 < -10) {
                        s3 = "nose down";
                    } else if ((k2 >= -10) && (k2 <= -5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "down a bit";
                    } else if ((k2 > -5) && (k2 < 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "level";
                    } else if ((k2 <= 10) && (k2 >= 5)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "up a bit";
                    } else if (k2 > 10) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s3 = "pull up";
                    }
                    String s4 = "  ";
                    if (l3 < 5) {
                        s4 = "dead ahead, ";
                    } else if ((l3 >= 5) && (l3 <= 7)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 5\260, ";
                    } else if ((l3 > 7) && (l3 <= 12)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 10\260, ";
                    } else if ((l3 > 12) && (l3 <= 17)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 15\260, ";
                    } else if ((l3 > 17) && (l3 <= 25)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 20\260, ";
                    } else if ((l3 > 25) && (l3 <= 35)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 30\260, ";
                    } else if ((l3 > 35) && (l3 <= 45)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "right by 40\260, ";
                    } else if ((l3 > 45) && (l3 <= 70)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "off our right, ";
                    } else if (l3 > 355) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "dead ahead, ";
                    } else if ((l3 <= 355) && (l3 >= 352)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 5\260, ";
                    } else if ((l3 < 352) && (l3 >= 347)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 10\260, ";
                    } else if ((l3 < 347) && (l3 >= 342)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 15\260, ";
                    } else if ((l3 < 342) && (l3 >= 335)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 20\260, ";
                    } else if ((l3 < 335) && (l3 >= 325)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 30\260, ";
                    } else if ((l3 < 325) && (l3 >= 315)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "left by 40\260, ";
                    } else if ((l3 < 345) && (l3 >= 290)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = "off our left, ";
                    }
                    if ((i2 <= i4) && (j3 > 0) && (k2 >= -20) && (k2 <= 20) && (Math.abs(k3) <= 70)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        HUD.logCenter("                                          RO: Contact " + s4 + s + ", " + l2 + s1);
                        this.freq = 6F;
                    } else if ((i2 <= i4) && (j3 < 1) && (i2 >= 120) && (k2 >= -20) && (k2 <= 20) && (Math.abs(k3) <= 70)) { // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        HUD.logCenter("                                          RO: " + s2 + s3 + ", " + i3 + s1);
                        this.freq = 4F;
                    } else {
                        this.freq = 6F;
                    }
                    super.resetTimer(this.freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private float    freq;

        public AImkIVUnit() {
            this.freq = 8F;
            this.Timer1 = this.freq;
            this.Timer2 = this.freq;
        }
    }

    public static class AGCIUnit extends CandCGeneric {

        public boolean danger() {
            if (Mission.cur() != null) {
                this.maxrange = Mission.cur().sectFile().get("Mods", "GCIRange", 100);
            }
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
//            float f = 1000F;
            // Random random = new Random();
//            int i = TrueRandom.nextInt(140);
//            int j = i + 10;
            double randomMaxHeight = TrueRandom.nextDouble(10D, 150D);
            Aircraft aircraft1 = World.getPlayerAircraft();
            if (aircraft1 == null) {
                return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
            }

            // TODO: Fix by SAS~Storebror:
            // Get rid of endless recursion through all game's target list (a few hundred times)
            // New method introduced to get the same result with on single target list search!
            Aircraft aircraft = GetNearestEnemyInSteps(aircraft1, 1000F, this.maxrange * 1000F, 1000F, 9);

////            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//            Aircraft aircraft = GetNearestEnemy(aircraft1, 1000F, 9);
//            for(int k = 0; aircraft == null && k < maxrange; k++)
//            {
////                aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F + f, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//                aircraft = GetNearestEnemy(aircraft1, 1000F + f, 9);
//                f += 1000F;
//            }
            if (aircraft == null) {
                return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
            }

//            boolean flag1 = false;
            this.maxrange *= 1000;
            if ((Mission.cur() != null) && (Mission.cur().sectFile().get("Mods", "GCIVarAlt", 0) == 1)) {
                this.maxheight1 = (aircraft.pos.getAbsPoint().distance(this.point3d) / this.maxrange) * 8000D;
                if (this.maxheight1 < randomMaxHeight) {
                    this.maxheight1 = randomMaxHeight;
                }
                this.maxheight2 = (aircraft1.pos.getAbsPoint().distance(this.point3d) / this.maxrange) * 8000D;
                if (this.maxheight2 < randomMaxHeight) {
                    this.maxheight2 = randomMaxHeight;
                }
            }
            if ((aircraft1.pos.getAbsPoint().distance(this.point3d) < this.maxrange) && (aircraft1.pos.getAbsPoint().z >= this.maxheight2)) {
                flag = true;
            }
            if ((aircraft.getSpeed(this.vector3d) > 20D) && (aircraft.pos.getAbsPoint().distance(this.point3d) < this.maxrange) && (aircraft.pos.getAbsPoint().z >= this.maxheight1) && (aircraft != World.getPlayerAircraft()) && (aircraft.getArmy() != this.myArmy) && (aircraft1.getArmy() == this.myArmy)) {
                double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 10000D;
                double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 10000D;
                double d2 = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 1000D;
                double d3 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 1000D;
                double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d5 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
                double d6 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
                double d7 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D;
                double d8 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D;
                char c = (char) (int) (65D + Math.floor(((d5 / 676D) - Math.floor(d5 / 676D)) * 26D));
                char c1 = (char) (int) (65D + Math.floor(((d5 / 26D) - Math.floor(d5 / 26D)) * 26D));
                // new String();
                String s;
                if (d4 > 260D) {
                    s = "" + c + c1;
                } else {
                    s = "" + c1;
                }
                // new String();
                int l = (int) (Math.floor(aircraft.pos.getAbsPoint().z * 0.328084D) * 10D);
//                int i1 = (int)(Math.floor((aircraft.getSpeed(vector3d) * 0.62137119200000002D * 60D * 60D) / 10000D) * 10D);
                // new String();
                int j1 = (int) Math.ceil(d6);
                double d9 = d5 - d;
                double d10 = d6 - d1;
                float f1 = 57.32484F * (float) Math.atan2(d10, -d9);
                double d11 = Math.floor((int) f1) - 90D;
                if (d11 < 0.0D) {
                    d11 = 360D + d11;
                }
                int k1 = (int) d11;
                double d12 = d2 - d7;
                double d13 = d3 - d8;
                int l1 = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
                if (l1 < 0) {
                    l1 = 360 + l1;
                }
                int i2 = (int) Math.ceil(Math.sqrt((d13 * d13) + (d12 * d12)));
                int j2 = (int) (i2 * 0.621371192D);
                if (flag) {
                    if (i2 > 4) {
                        HUD.logCenter("                                          Target bearing " + k1 + "\260" + ", range " + j2 + " miles, height " + l + "ft, heading " + l1 + "\260");
                    } else {
                        HUD.logCenter(" ");
                    }
                } else {
                    HUD.logCenter("                                                                             Target at " + s + "-" + j1 + ", height " + l + "ft, heading " + l1 + "\260");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d  point3d  = new Point3d();

        private int      maxrange;
        private double   maxheight1;
        private double   maxheight2;

        public AGCIUnit() {
            this.maxrange = 0x186a0;
            this.maxheight1 = 150D;
            this.maxheight2 = 150D;
        }
    }

    public static class AFACUnit extends CandCGeneric {

        public boolean danger() {
//            Point3d point3d = new Point3d();
            this.pos.getAbs(this.point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            int i = Mission.cur().sectFile().get("Mods", "FACDelay", 0) * 60;
            if (Time.current() > i) {
                this.active = true;
            }
            Aircraft aircraft = World.getPlayerAircraft();
            if (aircraft.pos.getAbsPoint().distance(this.point3d) < 10000D) {
                flag = true;
            }
            for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                actor2 = (Actor) entry.getValue();
//                if(((actor2 instanceof TypeScout) || (actor2 instanceof AT6)) && actor2.getArmy() == myArmy && actor2.pos.getAbsPoint().distance(point3d) < 5000D)
                if (((actor2 instanceof TypeScout) || isAT6(actor2)) && (actor2.getArmy() == this.myArmy) && (actor2.pos.getAbsPoint().distance(this.point3d) < 5000D)) {
                    flag1 = true;
                }
            }

            for (Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
                Actor actor = (Actor) entry1.getValue();
                if (this.active && flag1 && Actor.isAlive(actor) && ((actor instanceof TankGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric)) && (actor.getArmy() != this.myArmy) && (actor.pos.getAbsPoint().distance(this.point3d) < 1000D)) {
                    double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
//                    double d3 = actor.pos.getAbsPoint().distance(point3d);
//                    double d4 = point3d.z;
                    double d5 = (Main3D.cur3D().land2D.worldOfsX() + markpoint.x) / 10000D;
                    double d6 = (Main3D.cur3D().land2D.worldOfsY() + markpoint.y) / 10000D;
//                    double d7 = actor.pos.getAbsPoint().z;
                    char c = (char) (int) (65D + Math.floor(((d / 676D) - Math.floor(d / 676D)) * 26D));
                    char c1 = (char) (int) (65D + Math.floor(((d / 26D) - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if (d2 > 260D) {
                        s1 = "" + c + c1;
                    } else {
                        s1 = "" + c1;
                    }
                    double d8 = d5 - d;
                    double d9 = d6 - d1;
                    float f = 57.32484F * (float) Math.atan2(d9, d8);
                    int j = (int) f;
                    j = (((j + 180) % 360) / 10) * 10;
                    // new String();
                    String s2 = "units";
                    if ((actor instanceof ArtilleryCY6.ProneInfantry) || (actor instanceof ArtilleryCY6.DugInInfantry)) {
                        s2 = "troops";
                    } else if (actor instanceof ArtilleryGeneric) {
                        s2 = "units";
                    } else if (actor instanceof TgtTank) {
                        s2 = "armor";
                    } else if (actor instanceof TgtVehicle) {
                        s2 = "vehicle";
                    }
                    boolean flag3 = false;
                    if (actor instanceof AAA) {
                        flag3 = true;
                    }
                    String s3 = "";
                    if (flag3) {
                        s3 = ", watch out for AA";
                    }
//                    boolean flag4 = false;
                    String s4 = "";
                    Engine.land();
                    int k = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    if ((k >= 28) && (k < 32)) {
                        s4 = ", near the water";
                    } else if ((k >= 16) && (k < 20)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = ", in the town";
                    } else if ((k >= 24) && (k < 29)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = ", near the treeline";
                    } else if (((k >= 32) && (k < 64)) || (k >= 128)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = ", near the road";
                    } else if ((k >= 64) && (k < 128)) { // TODO: Fixed by SAS~Storebror: Avoid unnecessary "if" clauses, add "else" clause!
                        s4 = ", near the tracks";
                    }
                    String s5 = "";
                    Actor actor3 = null;
                    actor3 = War.GetNearestEnemy(actor, actor.getArmy(), 300F);
                    if (actor3 != null) {
                        s5 = ", friendlies close";
                    }
//                    int l = (int)(Math.ceil(d3 / 10D) * 10D);
                    int i1 = (int) Math.ceil(d1);
                    if (flag && flag2) {
                        HUD.logCenter("                                                                             Enemy " + s2 + ", bearing " + j + "\260" + s4 + s5 + s3 + "!");
                    } else if (flag && !flag2) {
                        HUD.logCenter("                                                                             You're cleared hot. Marking target.");
                        markpoint = actor.pos.getAbsPoint();
                        for (entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
                            actor2 = (Actor) entry1.getValue();
//                            if(((actor2 instanceof TypeScout) || (actor2 instanceof AT6)) && actor2.getArmy() == myArmy && actor2.pos.getAbsPoint().distance(point3d) < 5000D)
                            if (((actor2 instanceof TypeScout) || isAT6(actor2)) && (actor2.getArmy() == this.myArmy) && (actor2.pos.getAbsPoint().distance(this.point3d) < 5000D)) {
                                pilot = (Pilot) ((Aircraft) actor2).FM;
                                pilot.Group.setGTargMode(0);
                                pilot.Group.setGTargMode(actor);
                                pilot.Group.setGroupTask(4);
                            }
                        }

                        float f1 = World.getTimeofDay();
                        if (((f1 >= 0.0F) && (f1 <= 5F)) || ((f1 >= 21F) && (f1 <= 24F))) {
                            pilot.AS.setNavLightsState(true);
                        }
                        flag2 = true;
                    } else if (!flag) {
                        HUD.logCenter("                                                                             Request CAS at map grid " + s1 + "-" + i1);
                    }
                }
            }

            if (!flag && flag2 && !this.BDA && flag1) {
                for (Entry entry2 = Engine.name2Actor().nextEntry(null); entry2 != null; entry2 = Engine.name2Actor().nextEntry(entry2)) {
                    Actor actor1 = (Actor) entry2.getValue();
                    if (!Actor.isAlive(actor1) && ((actor1 instanceof TankGeneric) || (actor1 instanceof ArtilleryGeneric) || (actor1 instanceof CarGeneric)) && (actor1.getArmy() != this.myArmy) && (actor1.pos.getAbsPoint().distance(this.point3d) < 500D)) {
                        if ((actor1 instanceof ArtilleryCY6.ProneInfantry) || (actor1 instanceof ArtilleryCY6.DugInInfantry)) {
                            this.troops += 5;
                        } else if (actor1 instanceof TgtTank) {
                            this.armor++;
                        } else if ((actor1 instanceof CarGeneric) || (actor1 instanceof TgtVehicle)) {
                            this.trucks++;
                        } else if (actor1 instanceof ArtilleryGeneric) {
                            this.guns++;
                        }
                        if (this.armor > 0) {
                            e1 = "" + this.armor + " tank ";
                            if (this.armor > 1) {
                                e1 = "" + this.armor + "tanks ";
                            }
                        }
                        if (this.guns > 0) {
                            e2 = "" + this.guns + " emplacement ";
                            if (this.guns > 1) {
                                e1 = "" + this.guns + " emplacements ";
                            }
                        }
                        if (this.trucks > 0) {
                            e3 = "" + this.trucks + " vehicle ";
                            if (this.trucks > 1) {
                                e3 = "" + this.trucks + " vehicles ";
                            }
                        }
                        if (this.troops > 0) {
                            e4 = "" + this.troops + " troops";
                        }
                        String s = e1 + e2 + e3 + e4 + " destroyed.";
                        if ((this.troops + this.armor + this.trucks + this.guns) <= 0) {
                            s = " No targets hit.";
                        }
                        HUD.logCenter("                                                                             Thanks for the help! BDA: " + s);
                        this.BDA = true;
                    }
                }

            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d        point3d   = new Point3d();

        private boolean        active;
//        private int spread;
        private int            armor;
        private int            trucks;
        private int            guns;
        private int            troops;
        private static String  e1        = "";
        private static String  e2        = "";
        private static String  e3        = "";
        private static String  e4        = "";
        private boolean        BDA;
        private static Actor   actor2    = null;
        private static Pilot   pilot     = null;
        private static Point3d markpoint = new Point3d();

        public AFACUnit() {
            this.active = false;
//            spread = 0;
            this.armor = 0;
            this.trucks = 0;
            this.guns = 0;
            this.troops = 0;
            this.BDA = false;
            this.Timer1 = this.Timer2 = 60F;
        }
    }

    // TODO: Fixed by SAS~Storebror: Strip surplus default Constructor
//    public CandC()
//    {
//    }

    // TODO: Fixed by SAS~Storebror: Add generic methods and fields to avoid dependence of probably non-present mod classes!
    private static void HasVector(Aircraft aircraft, int i) {
        if (isHasVectorAvailableType == -1) {
            try {
                Class class1 = Class.forName("com.maddox.il2.ai.VisCheck");
                // TODO: ATTENTION! Needs SAS Common Utils 1.10 or later!
                hasVectorMethod = Reflection.getMethod(class1, "hasVector", new Class[] { Aircraft.class, int.class });
                isHasVectorAvailableType = HAS_VECTOR_AVAILABLE;
            } catch (Exception e) {
                isHasVectorAvailableType = HAS_VECTOR_NOT_AVAILABLE;
            }
        }
        switch (isHasVectorAvailableType) {
            case HAS_VECTOR_AVAILABLE:
                try {
                    Reflection.invokeMethod(hasVectorMethod, new Object[] { aircraft, new Integer(i) });
                } catch (Exception e) {
                }
                return;
            case HAS_VECTOR_NOT_AVAILABLE:
            default:
                return;
        }
    }

    private static int       isHasVectorAvailableType = -1;
    private static Method    hasVectorMethod          = null;
    private static final int HAS_VECTOR_NOT_AVAILABLE = 0;
    private static final int HAS_VECTOR_AVAILABLE     = 1;

    private static Aircraft GetNearestEnemyInSteps(Actor actor, float maxDistanceStart, float maxDistanceEnd, float maxDistanceStep, int targetType) {
        return NearestRadarTargets.GetNearestEnemyEchoInSteps(actor, maxDistanceStart, maxDistanceEnd, maxDistanceStep, targetType);
    }

    private static Aircraft GetNearestEnemy(Actor actor, float maxDistance, int targetType) {
        return NearestRadarTargets.GetNearestEnemyEcho(actor, maxDistance, targetType);
    }

    private static boolean isAT6(Object o) {
        if (!Class_AT6_Initialized && (Class_AT6 == null)) {
            Class_AT6_Initialized = true;
            try {
                Class_AT6 = Class.forName("com.maddox.il2.objects.air.AT6");
            } catch (Exception e) {
            }
        }
        if (Class_AT6 == null) {
            return false;
        }
        return Class_AT6.isInstance(o);
    }

    private static Class   Class_AT6             = null;
    private static boolean Class_AT6_Initialized = false;

    private static boolean isHRS3AI(Object o) {
        if (!Class_HRS3AI_Initialized && (Class_HRS3AI == null)) {
            Class_HRS3AI_Initialized = true;
            try {
                Class_HRS3AI = Class.forName("com.maddox.il2.objects.air.HRS3AI");
            } catch (Exception e) {
            }
        }
        if (Class_HRS3AI == null) {
            return false;
        }
        return Class_HRS3AI.isInstance(o);
    }

    private static Class   Class_HRS3AI             = null;
    private static boolean Class_HRS3AI_Initialized = false;

    private static boolean isFJ_3M(Object o) {
        if (!Class_FJ_3M_Initialized && (Class_FJ_3M == null)) {
            Class_FJ_3M_Initialized = true;
            try {
                Class_FJ_3M = Class.forName("com.maddox.il2.objects.air.FJ_3M");
            } catch (Exception e) {
            }
        }
        if (Class_FJ_3M == null) {
            return false;
        }
        return Class_FJ_3M.isInstance(o);
    }

    private static Class   Class_FJ_3M             = null;
    private static boolean Class_FJ_3M_Initialized = false;

    private static boolean isF84G3(Object o) {
        if (!Class_F84G3_Initialized && (Class_F84G3 == null)) {
            Class_F84G3_Initialized = true;
            try {
                Class_F84G3 = Class.forName("com.maddox.il2.objects.air.F84G3");
            } catch (Exception e) {
            }
        }
        if (Class_F84G3 == null) {
            return false;
        }
        return Class_F84G3.isInstance(o);
    }

    private static Class   Class_F84G3             = null;
    private static boolean Class_F84G3_Initialized = false;

    private static boolean isA1H_Tanker(Object o) {
        if (!Class_A1H_Tanker_Initialized && (Class_A1H_Tanker == null)) {
            Class_A1H_Tanker_Initialized = true;
            try {
                Class_A1H_Tanker = Class.forName("com.maddox.il2.objects.air.A1H_Tanker");
            } catch (Exception e) {
            }
        }
        if (Class_A1H_Tanker == null) {
            return false;
        }
        return Class_A1H_Tanker.isInstance(o);
    }

    private static Class   Class_A1H_Tanker             = null;
    private static boolean Class_A1H_Tanker_Initialized = false;

    private static boolean isKB_29P(Object o) {
        if (!Class_KB_29P_Initialized && (Class_KB_29P == null)) {
            Class_KB_29P_Initialized = true;
            try {
                Class_KB_29P = Class.forName("com.maddox.il2.objects.air.KB_29P");
            } catch (Exception e) {
            }
        }
        if (Class_KB_29P == null) {
            return false;
        }
        return Class_KB_29P.isInstance(o);
    }

    private static Class   Class_KB_29P             = null;
    private static boolean Class_KB_29P_Initialized = false;

    static {
        new CandCGeneric.SPAWN(AFACUnit.class);
        new CandCGeneric.SPAWN(AGCIUnit.class);
        new CandCGeneric.SPAWN(AImkIVUnit.class);
        new CandCGeneric.SPAWN(AImkVIIIUnit.class);
        new CandCGeneric.SPAWN(AImkXUnit.class);
        new CandCGeneric.SPAWN(AImkXVUnit.class);
        new CandCGeneric.SPAWN(AimpointUnit.class);
        new CandCGeneric.SPAWN(ErrorUnit.class);
        new CandCGeneric.SPAWN(ANAPS4Unit.class);
        new CandCGeneric.SPAWN(ASDUnit.class);
        new CandCGeneric.SPAWN(ASVUnit.class);
        new CandCGeneric.SPAWN(ASGUnit.class);
        new CandCGeneric.SPAWN(ASVmkVIIIUnit.class);
        new CandCGeneric.SPAWN(CorkscrewUnit.class);
        new CandCGeneric.SPAWN(CWUnit.class);
        new CandCGeneric.SPAWN(DynamicWeatherUnit.class);
        new CandCGeneric.SPAWN(DZUnit.class);
        new CandCGeneric.SPAWN(FACUnit.class);
        new CandCGeneric.SPAWN(ASVmkIIIUnit.class);
        new CandCGeneric.SPAWN(FuGUnit.class);
        new CandCGeneric.SPAWN(FD2Unit.class);
        new CandCGeneric.SPAWN(FuG218Unit.class);
        new CandCGeneric.SPAWN(FlensburgUnit.class);
        new CandCGeneric.SPAWN(FireUnit.class);
        new CandCGeneric.SPAWN(GCIUnit.class);
        new CandCGeneric.SPAWN(GNRUnit.class);
        new CandCGeneric.SPAWN(GCIUnit.class);
        new CandCGeneric.SPAWN(GTWUnit.class);
        new CandCGeneric.SPAWN(H2SUnit.class);
        new CandCGeneric.SPAWN(JammerUnit.class);
        new CandCGeneric.SPAWN(MADUnit.class);
        new CandCGeneric.SPAWN(MonicaUnit.class);
        new CandCGeneric.SPAWN(NavUnit.class);
        new CandCGeneric.SPAWN(NaxosUnit.class);
        new CandCGeneric.SPAWN(OBOEUnit.class);
        new CandCGeneric.SPAWN(OBSUnit.class);
        new CandCGeneric.SPAWN(RadioOperatorUnit.class);
        new CandCGeneric.SPAWN(RandomAircraft2Unit.class);
        new CandCGeneric.SPAWN(RandomAircraftUnit.class);
        new CandCGeneric.SPAWN(RandomShips2Unit.class);
        new CandCGeneric.SPAWN(RandomShipsUnit.class);
        new CandCGeneric.SPAWN(RandomVehicles2Unit.class);
        new CandCGeneric.SPAWN(RandomVehiclesUnit.class);
        new CandCGeneric.SPAWN(RandomTrainsUnit.class);
        new CandCGeneric.SPAWN(RandomFlakUnit.class);
        new CandCGeneric.SPAWN(RandomTimeUnit.class);
        new CandCGeneric.SPAWN(RandomSkillUnit.class);
        new CandCGeneric.SPAWN(RandomWeatherUnit.class);
        new CandCGeneric.SPAWN(RCGCIUnit.class);
        new CandCGeneric.SPAWN(RefuelUnit.class);
        new CandCGeneric.SPAWN(RESCAPUnit.class);
        new CandCGeneric.SPAWN(DynamicRescueUnit.class);
        new CandCGeneric.SPAWN(SARUnit.class);
        new CandCGeneric.SPAWN(SerrateUnit.class);
        new CandCGeneric.SPAWN(SN2bUnit.class);
        new CandCGeneric.SPAWN(SN2Unit.class);
        new CandCGeneric.SPAWN(AmbushUnit.class);
        new CandCGeneric.SPAWN(SpawnUnit.class);
        new CandCGeneric.SPAWN(SPRUnit.class);
        new CandCGeneric.SPAWN(StormFrontUnit.class);
        new CandCGeneric.SPAWN(TakiUnit.class);
        new CandCGeneric.SPAWN(VectorUnit.class);
        new CandCGeneric.SPAWN(WindowUnit.class);
        new CandCGeneric.SPAWN(BoxUnit.class);
        new CandCGeneric.SPAWN(BombUnit.class);
        new CandCGeneric.SPAWN(BRGUnit.class);
        new CandCGeneric.SPAWN(NGSUnit.class);
        new CandCGeneric.SPAWN(NGS1Unit.class);
        new CandCGeneric.SPAWN(SLDUnit.class);
        new CandCGeneric.SPAWN(SLD2Unit.class);
        new CandCGeneric.SPAWN(CityLightsUnit.class);
        new CandCGeneric.SPAWN(LowReconUnit.class);
        new CandCGeneric.SPAWN(HighReconUnit.class);
        new CandCGeneric.SPAWN(LevelUnit.class);
        new CandCGeneric.SPAWN(LandingUnit.class);
        new CandCGeneric.SPAWN(DynamicStreamUnit.class);
        new CandCGeneric.SPAWN(DynamicStrikeUnit.class);
    }
}
