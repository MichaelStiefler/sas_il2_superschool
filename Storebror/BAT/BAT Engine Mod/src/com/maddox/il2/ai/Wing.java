package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgOwnerListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TestRunway;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.SectFile;

public class Wing extends Chief implements MsgOwnerListener {

    public int aircReady() {
        int i = 0;
        for (int j = 0; j < this.airc.length; j++) {
            if (Actor.isValid(this.airc[j])) {
                i++;
            }
        }

        return i;
    }

    public int aircIndex(Aircraft aircraft) {
        String s = aircraft.name();
        char c = s.charAt(s.length() - 1);
        return c - 48;
    }

    public Regiment regiment() {
        return this.squadron().regiment();
    }

    public Squadron squadron() {
        return (Squadron) this.getOwner();
    }

    public int indexInSquadron() {
        char c = this.name().charAt(this.name().length() - 1);
        return c - 48;
    }

    public void msgOwnerDied(Actor actor) {
        if (this.getDiedFlag()) {
            return;
        }
        for (int i = 0; i < this.airc.length; i++) {
            if (Actor.isValid(this.airc[i]) && this.airc[i].isAlive()) {
                return;
            }
        }

        World.onActorDied(this, null);
    }

    public void msgOwnerTaskComplete(Actor actor) {
        for (int i = 0; i < this.airc.length; i++) {
            if (Actor.isValid(this.airc[i]) && !this.airc[i].isTaskComplete()) {
                return;
            }
        }

        World.onTaskComplete(this);
    }

    public void msgOwnerAttach(Actor actor1) {
    }

    public void msgOwnerDetach(Actor actor1) {
    }

    public void msgOwnerChange(Actor actor2, Actor actor3) {
    }

    public void destroy() {
        for (int i = 0; i < this.airc.length; i++) {
            this.airc[i] = null;
        }

        super.destroy();
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public Wing() {
        this.airc = new Aircraft[4];
        this.way = new Way();
        this.bOnlyAI = false;
    }

    public void load(SectFile sectfile, String s, NetChannel netchannel) throws Exception {
        if (sectfile.sectionIndex(s) < 0) {
            this.destroy();
            throw new Exception("Mission: section '" + s + "' not found");
        }
        String s1 = sectfile.get(s, "Class", (String) null);
        if (s1 == null) {
            this.destroy();
            throw new Exception("Mission: in section '" + s + "' class aircraft not defined");
        }
        if (s1.endsWith("GenericSpawnPointPlane")) {
            return;
        }
        String s2 = s + "_Way";
        if (sectfile.sectionIndex(s2) < 0) {
            this.destroy();
            throw new Exception("Mission: section '" + s2 + "' not found");
        }
        if (Actor.getByName(s) != null) {
            this.destroy();
            throw new Exception("Mission: dublicate wing '" + s + "'");
        }
        this.setName(s);
        int i = this.indexInSquadron();
        if ((i < 0) || (i > 3)) {
            throw new RuntimeException("Wing '" + s + "' NOT valid");
        }
        Squadron squadron1 = Squadron.New(s.substring(0, s.length() - 1));
        this.setOwner(squadron1);
        squadron1.wing[this.indexInSquadron()] = this;
        this.setArmy(squadron1.getArmy());
        this.bOnlyAI = sectfile.get(s, "OnlyAI", 0, 0, 1) == 1;
        NetAircraft.loadingCountry = squadron1.regiment().country();
        int j = sectfile.get(s, "Planes", 1, 1, 4);
        try {
            this.way.load(sectfile, s2);
            for (int k = 0; k < j; k++) {
                this.airc[k] = Mission.cur().loadAir(sectfile, s1, s, s + k, k);
                this.airc[k].setArmy(this.getArmy());
                this.airc[k].setOwner(this);
                if (this.airc[k] == World.getPlayerAircraft()) {
                    World.setPlayerRegiment();
                }
                this.airc[k].preparePaintScheme();
                this.airc[k].prepareCamouflage();
                this.setPosAndSpeed(this.airc[k], this.way);
                if (World.cur().triggersGuard.listTriggerAvionSol.contains(s) && (this.airc[k].FM instanceof Maneuver)) {
                    ((Maneuver) this.airc[k].FM).triggerTakeOff = false;
                }
            }

            this.setArmy(this.airc[0].getArmy());
            for (int l = 0; l < j; l++) {
                this.airc[l].FM.AP.way = new Way(this.way);
            }

            if ((Mission.isSingle() || ((Mission.isCoop() || Mission.isDogfight()) && Mission.isServer())) && !NetMissionTrack.isPlaying()) {
                AirGroup airgroup = new AirGroup(this.airc[0].getSquadron(), this.way);
                for (int i1 = 0; i1 < j; i1++) {
                    airgroup.addAircraft(this.airc[i1]);
                }

                AirGroupList.addAirGroup(War.Groups, (this.airc[0].getArmy() - 1) & 1, airgroup);
            }
            NetAircraft.loadingCountry = null;
            Formation.generate(this.airc);
        } catch (Exception exception) {
            NetAircraft.loadingCountry = null;
            this.destroy();
            throw exception;
        }
    }

    private void setPosAndSpeed(Aircraft aircraft, Way way1) {
        if (way1.size() == 1) {
            WayPoint waypoint = way1.get(0);
            waypoint.getP(Wing.tmpPoint0);
            aircraft.pos.setAbs(Wing.tmpPoint0);
            Wing.tmpSpeed.set(waypoint.Speed, 0.0D, 0.0D);
            aircraft.setSpeed(Wing.tmpSpeed);
        } else {
            WayPoint waypoint1 = way1.get(0);
            WayPoint waypoint2 = way1.get(1);
            waypoint1.getP(Wing.tmpPoint0);
            waypoint2.getP(Wing.tmpPoint1);
            Wing.tmpSpeed.sub(Wing.tmpPoint1, Wing.tmpPoint0);
            Wing.tmpSpeed.normalize();
            Actor._tmpOrient.setAT0(Wing.tmpSpeed);
            Wing.tmpSpeed.scale(waypoint1.Speed);
            aircraft.pos.setAbs(Wing.tmpPoint0, Actor._tmpOrient);
            aircraft.setSpeed(Wing.tmpSpeed);
        }
        aircraft.pos.reset();
    }

    public void setOnAirport() {
        WayPoint waypoint = this.way.get(0);
        waypoint.getP(Wing.tmpPoint0);
        this.takeoffSpacing = waypoint.takeoffSpacing;
        boolean flag = false;
        if (waypoint.Action == 1) {
            boolean flag1 = false;
            if (waypoint.sTarget != null) {
                Actor actor = waypoint.getTarget();
                if (actor instanceof BigshipGeneric) {
                    AirportCarrier airportcarrier = ((BigshipGeneric) actor).getAirport();
                    if (Actor.isValid(airportcarrier)) {
                        airportcarrier.setTakeoff(Wing.tmpPoint0, this.airc);
                        flag1 = true;
                    }
                }
            }
            if (!flag1) {
                Airport airport = Airport.nearest(Wing.tmpPoint0, this.getArmy(), 7);
                if (airport instanceof AirportCarrier) {
                    AirportCarrier airportcarrier1 = (AirportCarrier) airport;
                    if (airportcarrier1.ship() instanceof TestRunway) {
                        flag = true;
                    } else {
                        airport = null;
                    }
                }
                if (airport != null) {
                    airport.setArmy(this.getArmy());
                    if (waypoint.waypointType < 2) {
                        double d = ((Actor) (airport)).pos.getAbsPoint().distance(Wing.tmpPoint0);
                        if (d < 1250D) {
                            if (flag) {
                                ((AirportCarrier) airport).setStationaryPlaneTakeoff(this.airc);
                                this.setonground();
                            } else {
                                airport.setTakeoff(Wing.tmpPoint0, this.airc);
                            }
                        } else {
                            this.setonground();
                        }
                    } else {
                        if (waypoint.waypointType == 2) {
                            this.setonpair();
                        } else if ((waypoint.waypointType == 3) || (waypoint.waypointType == 4)) {
                            this.setonline();
                        } else {
                            if (waypoint.waypointType == 5) {
                                airport.setTakeoff(Wing.tmpPoint0, this.airc);
                            }
                            this.setonground();
                        }
                        if (((waypoint.waypointType == 2) || (waypoint.waypointType == 3) || (waypoint.waypointType == 4) || (waypoint.waypointType == 5)) && (((Actor) (airport)).pos.getAbsPoint().distance(Wing.tmpPoint0) < 1250D)) {
                            ((AirportStatic) airport).setAsHomeAirport(this.airc);
                        }
                    }
                } else {
                    this.setonground();
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (Actor.isValid(this.airc[i])) {
                    if (this.airc[i] == World.getPlayerAircraft()) {
                        this.airc[i].FM.EI.setCurControlAll(true);
                        this.airc[i].FM.EI.setEngineRunning();
                        this.airc[i].FM.CT.setPowerControl(0.75F);
                    }
                    this.airc[i].FM.setStationedOnGround(false);
                    this.airc[i].FM.setWasAirborne(true);
                }
            }

        }
        if (flag) {
            for (int j = 0; j < 4; j++) {
                if (Actor.isValid(this.airc[j])) {
                    ((SndAircraft) (this.airc[j])).FM.brakeShoe = false;
                }
            }

        }
    }

    private void setonground() {
        for (int i = 0; i < this.airc.length; i++) {
            if (this.airc[i] != null) {
                ((Actor) (this.airc[i])).pos.getAbs(Wing.pGround, Wing.oGround);
                Wing.pGround.z = World.land().HQ(Wing.pGround.x, Wing.pGround.y) + this.airc[i].FM.Gears.H;
                Engine.land().N(Wing.pGround.x, Wing.pGround.y, Wing.vGround);
                Wing.oGround.orient(Wing.vGround);
                Wing.oGround.increment(0.0F, this.airc[i].FM.Gears.Pitch, 0.0F);
                this.airc[i].setOnGround(Wing.pGround, Wing.oGround, Wing.zeroSpeed);
            }
        }

    }

    private void setonpair() {
        for (int i = 0; i < this.airc.length; i++) {
            if (this.airc[i] != null) {
                double d = this.takeoffSpacing;
                this.airc[0].pos.getAbs(Wing.pGround, Wing.oGround);
                float f = Wing.oGround.getYaw();
                switch (i) {
                    case 0:
                        d = 0.0D;
                        break;

                    case 1:
                        f -= 90F;
                        break;

                    case 2:
                        f -= 180F;
                        break;

                    case 3:
                        f -= 135F;
                        d = Math.sqrt(2D) * this.takeoffSpacing;
                        break;

                    default:
                        f = Wing.oGround.getYaw();
                        d = 0.0D;
                        break;
                }
                f = (float)Math.toRadians(f);
                Wing.tmpLoc.set(Math.cos(f) * d, Math.sin(f) * d, 0.0D, 0.0F, 0.0F, 0.0F);
                Wing.tmpLoc.add(Wing.pGround);
                Point3d point3d = Wing.tmpLoc.getPoint();
                Orient orient = Wing.oGround;
                point3d.z = World.land().HQ(point3d.x, point3d.y) + this.airc[i].FM.Gears.H;
                Engine.land().N(point3d.x, point3d.y, Wing.v1);
                orient.orient(Wing.v1);
                orient.increment(0.0F, this.airc[i].FM.Gears.Pitch, 0.0F);
                this.airc[i].setOnGround(point3d, orient, Wing.zeroSpeed);
            }
        }

    }

    private void setonline() {
        for (int i = 0; i < this.airc.length; i++) {
            if (this.airc[i] != null) {
                double d = (double) this.takeoffSpacing * (double) i;
                this.airc[0].pos.getAbs(Wing.pGround, Wing.oGround);
                float f = (float) Math.toRadians(Wing.oGround.getYaw() - 90F);
                Wing.tmpLoc.set(Math.cos(f) * d, Math.sin(f) * d, 0.0D, 0.0F, 0.0F, 0.0F);
                Wing.tmpLoc.add(Wing.pGround);
                Point3d point3d = Wing.tmpLoc.getPoint();
                Orient orient = Wing.oGround;
                point3d.z = World.land().HQ(point3d.x, point3d.y) + this.airc[i].FM.Gears.H;
                Engine.land().N(point3d.x, point3d.y, Wing.v1);
                orient.orient(Wing.v1);
                orient.increment(0.0F, this.airc[i].FM.Gears.Pitch, 0.0F);
                this.airc[i].setOnGround(point3d, orient, Wing.zeroSpeed);
            }
        }

    }

    public Aircraft         airc[];
    public Way              way;
    public boolean          bOnlyAI;
    private static Vector3d tmpSpeed  = new Vector3d();
    private static Point3d  tmpPoint0 = new Point3d();
    private static Point3d  tmpPoint1 = new Point3d();
    private static Vector3d zeroSpeed = new Vector3d();
    private static Point3d  pGround   = new Point3d();
    private static Orient   oGround   = new Orient();
    private static Vector3d vGround   = new Vector3d();
    private int             takeoffSpacing;
    private static Loc      tmpLoc    = new Loc();
    private static Vector3d v1        = new Vector3d();

}
