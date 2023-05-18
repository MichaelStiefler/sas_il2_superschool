// Source File Name:   Airport.java

package com.maddox.il2.ai;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Point_Runaway;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgDreamListener;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeAmphibiousPlane;
import com.maddox.il2.objects.air.TypeSeaPlane;
import com.maddox.rts.Message;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.ai:
//            AirportGround, AirportMaritime, AirportCarrier, World, 
//            Way, WayPoint

public abstract class Airport extends Actor implements MsgDreamListener {
	class Interpolater extends Interpolate {

		public boolean tick() {
			update();
			return true;
		}

		Interpolater() {
		}
	}

	public static Airport nearest(Point3f point3f, int i, int j) {
		pd.set(point3f.x, point3f.y, point3f.z);
		return nearest(pd, i, j);
	}

	public static Airport nearest(Point3d point3d, int i, int j) {
		Airport airport = null;
		double d = 0.0D;
		pd.set(point3d.x, point3d.y, point3d.z);
		int k = World.cur().getAirports().size();
		for (int l = 0; l < k; l++) {
			Airport airport1 = (Airport) World.cur().getAirports().get(l);
			if (((j & 1) == 0 || !(airport1 instanceof AirportGround)) && ((j & 2) == 0 || !(airport1 instanceof AirportMaritime)) && ((j & 4) == 0 || !(airport1 instanceof AirportCarrier)) || !Actor.isAlive(airport1)) continue;
			if (i >= 0) {
				int i1 = airport1.getArmy();
				if (i1 != 0 && i1 != i) continue;
			}
			pd.z = airport1.pos.getAbsPoint().z;
			double d1 = pd.distanceSquared(airport1.pos.getAbsPoint());
			if (airport == null || d1 < d) {
				airport = airport1;
				d = d1;
			}
		}

		if (d > 225000000D) airport = null;
		return airport;
	}

	public Airport() {
		takeoffRequest = 0;
		landingRequest = 0;
//		Airport _tmp = this;
		flags |= 0x200;
		World.cur().getAirports().add(this);
	}

	public static double distToNearestAirport(Point3d point3d) {
		return distToNearestAirport(point3d, -1, 7);
	}

	public static double distToNearestAirport(Point3d point3d, int i, int j) {
		Airport airport = nearest(point3d, i, j);
		if (airport == null) return 225000000D;
		else return airport.pos.getAbsPoint().distance(point3d);
	}

	public static Airport makeLandWay(FlightModel flightmodel) {
		flightmodel.AP.way.curr().getP(PlLoc);
		int i = 0;
		Airport airport = null;
		int j = flightmodel.actor.getArmy();
		if (flightmodel.actor instanceof TypeSeaPlane) {
			i = 2;
			airport = nearest(PlLoc, j, i);
		} else if (flightmodel.AP.way.isLandingOnShip()) {
			i = 4;
			airport = nearest(PlLoc, j, i);
			if (!Actor.isAlive(airport)) {
				i = 1;
				airport = nearest(PlLoc, j, i);
			}
		} else {
			i = 3;
			if (!(flightmodel.actor instanceof TypeAmphibiousPlane)) i &= -3;
			airport = nearest(PlLoc, j, i);
			if (!Actor.isAlive(airport)) {
				i = 4;
				airport = nearest(PlLoc, j, i);
			}
		}
		Aircraft.debugprintln(flightmodel.actor, "Searching a place to land - Selecting RWY Type " + i);
		if (Actor.isAlive(airport)) {
			if (airport.landWay(flightmodel)) {
				flightmodel.AP.way.landingAirport = airport;
				return airport;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean landWay(FlightModel flightmodel) {
		return false;
	}

	public void rebuildLandWay(FlightModel flightmodel) {
	}

	public void rebuildLastPoint(FlightModel flightmodel) {
	}

	public double shiftFromLine(FlightModel flightmodel) {
		return 0.0D;
	}

	public int landingFeedback(Point3d point3d, Aircraft aircraft) {
		if (aircraft.FM.CT.GearControl > 0.0F) return 0;
		if (landingRequest > 0) return 1;
		double d = 640000D;
		List list = Engine.targets();
		int i = list.size();
		for (int j = 0; j < i; j++) {
			Actor actor = (Actor) list.get(j);
			if (!(actor instanceof Aircraft) || actor == aircraft) continue;
			Aircraft aircraft1 = (Aircraft) actor;
			Point3d point3d1 = aircraft1.pos.getAbsPoint();
			double d1 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
			if (d1 >= d) continue;
			if (((Maneuver) aircraft1.FM).get_maneuver() == 25) {
				if (((Maneuver) aircraft1.FM).wayCurPos instanceof Point_Runaway) return 2;
				if (aircraft1.FM.AP.way.isLanding() && aircraft1.FM.AP.way.Cur() > 5) return 1;
			}
			if (((Maneuver) aircraft1.FM).get_maneuver() == 26 || ((Maneuver) aircraft1.FM).get_maneuver() == 64) return 2;
		}

		if (aircraft.FM.AP.way.isLanding2()) landingRequest = 0;
		else if (aircraft.FM.AP.way.isLandingOnShip()) landingRequest = 1000;
		else landingRequest = 2000;
		return 0;
	}

	public abstract boolean nearestRunway(Point3d point3d, Loc loc);

	public abstract void setTakeoff(Point3d point3d, Aircraft aaircraft[]);

	public Object getSwitchListener(Message message) {
		return this;
	}

	protected void createActorHashCode() {
		makeActorRealHashCode();
	}

	protected void update() {
		if (takeoffRequest > 0) takeoffRequest--;
		if (landingRequest > 0) landingRequest--;
	}

	public void msgDream(boolean flag) {
		if (flag) {
			if (interpGet("AirportTicker") == null) interpPut(new Interpolater(), "AirportTicker", Time.current(), null);
		} else {
			interpEnd("AirportTicker");
		}
	}

	public static final int TYPE_ANY = 7;
	public static final int TYPE_GROUND = 1;
	public static final int TYPE_MARITIME = 2;
	public static final int TYPE_CARRIER = 4;
	private static Point3f PlLoc = new Point3f();
	public int takeoffRequest;
	public int landingRequest;
	private static Point3d pd = new Point3d();

}
