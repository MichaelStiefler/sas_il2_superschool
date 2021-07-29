/*Modified AirportStatic class for the SAS Engine Mod*/
/*By western, add 360 over head approach for modern jets on 10th/Jul./2018*/
/*By western, add TypeHelicopter on 12th/Feb./2019*/
package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point2f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.air.TypeHelicopter;
import com.maddox.rts.Time;

public abstract class AirportStatic extends Airport {
	private static class Runway {

		public Loc loc;
		public float planeShift[];
		public Aircraft planes[];
		public int curPlaneShift;
		public int oldTickCounter;

		public Runway(Loc loc1) {
			loc = new Loc();
			planeShift = new float[32];
			planes = new Aircraft[32];
			curPlaneShift = 0;
			oldTickCounter = 0;
			loc.set(loc1.getX(), loc1.getY(), World.land().HQ(loc1.getX(), loc1.getY()), loc1.getAzimut(), 0.0F, 0.0F);
		}
	}

	public AirportStatic() {
		runway = new ArrayList();
	}

	public static void make(ArrayList arraylist, Point2f apoint2f[][], Point2f apoint2f1[][], Point2f apoint2f2[][]) {
		if (arraylist == null) return;
		ArrayList arraylist1 = new ArrayList();
		while (arraylist.size() > 0) {
			Loc loc = (Loc) arraylist.remove(0);
			boolean flag = false;
			AirportStatic airportstatic = null;
			int i = 0;
			do {
				if (i >= arraylist1.size()) break;
				airportstatic = (AirportStatic) arraylist1.get(i);
				if (airportstatic.oppositeRunway(loc) != null) {
					flag = true;
					break;
				}
				i++;
			} while (true);
			if (flag) {
				airportstatic.runway.add(new Runway(loc));
				int j = airportstatic.runway.size();
				p3d.set(0.0D, 0.0D, 0.0D);
				for (int k = 0; k < j; k++) {
					loc = ((Runway) airportstatic.runway.get(k)).loc;
					p3d.x += loc.getPoint().x;
					p3d.y += loc.getPoint().y;
					p3d.z += loc.getPoint().z;
				}

				p3d.x /= j;
				p3d.y /= j;
				p3d.z /= j;
				airportstatic.pos.setAbs(p3d);
			} else {
				if (Engine.cur.land.isWater(loc.getPoint().x, loc.getPoint().y)) airportstatic = new AirportMaritime();
				else airportstatic = new AirportGround();
				airportstatic.pos = new ActorPosStatic(airportstatic, loc);
				airportstatic.runway.add(new Runway(loc));
				arraylist1.add(airportstatic);
			}
		}
	}

	public boolean landWay(FlightModel flightmodel) {
		flightmodel.AP.way.curr().getP(pWay);
		Runway runway1 = nearestRunway(pWay);
		if (runway1 == null) return false;
		Way way = new Way();
		// By western, make a reserve way for go-around.
		Way wayReserve = new Way();
		float f = (float) Engine.land().HQ_Air(runway1.loc.getX(), runway1.loc.getY());
		float f1 = flightmodel.M.massEmpty / 3000F;
		if (f1 > 1.0F) f1 = 1.0F;
		if (flightmodel.EI.engines[0].getType() > 1) f1 = 1.0F;
		if (flightmodel.EI.engines[0].getType() == 2) f1 = 1.25F;
		else if (flightmodel.EI.engines[0].getType() == 3) f1 = 1.5F;
		float f2 = f1;
		if (flightmodel.actor instanceof TypeHelicopter) {
			f1 = 0.4F;
			f2 = 0.4F;
		}
		// DBW AI MOD - get rid of stock one
		// if(f2 > 1.0F)
		// f2 = 1.0F;
		for (int i = x.length - 1; i >= 0; i--) {
			WayPoint waypoint = new WayPoint();
			WayPoint waypointReserve = new WayPoint();
			int j = (flightmodel.AP.way.curr().waypointType - 101) + 1;
			if (j < 0) j = 0;
			switch (j) {
			case 0: // '\0' : standard Counterclockwise traffic pattern
			default:
				if (flightmodel.actor instanceof TypeFastJet) pd.set(xFJ[i], yFJ[i], zFJ[i]);
				else pd.set(x[i] * f1, y[i] * f1, z[i] * f2);
				pdRs.set(pd);
				break;

			case 1: // '\001' : Clockwise traffic pattern
				if (flightmodel.actor instanceof TypeFastJet) pd.set(xFJ[i], ryFJ[i], zFJ[i]);
				else pd.set(x[i] * f1, ry[i] * f1, z[i] * f2);
				pdRs.set(pd);
				break;

			case 2: // '\002' : Counterclockwise traffic pattern, short width
				if (flightmodel.actor instanceof TypeFastJet) {
					if (flightmodel.AILandingWP360OverHeadApr == 1)
					{
						if (flightmodel instanceof Maneuver && ((Maneuver)flightmodel).Group != null) {
							int acn = ((Maneuver)flightmodel).Group.numInGroup((Aircraft)flightmodel.actor) % 4;
							pd.set(ohaafxFJ[acn][i], ohaafyFJ[i], ohaafzFJ[i]);
						}
						else
							pd.set(ohaafxFJ[0][i], ohaafyFJ[i], ohaafzFJ[i]);
						pdRs.set(ohaafxFJ[0][i], ohaafyFJ[i], ohaafzFJ[i]);

						((FlightModelMain)flightmodel).bOnGoingOverHeadApproach = true;
					}
					else if (flightmodel.AILandingWP360OverHeadApr == 2)
					{
						if (flightmodel instanceof Maneuver && ((Maneuver)flightmodel).Group != null) {
							int acn = ((Maneuver)flightmodel).Group.numInGroup((Aircraft)flightmodel.actor) % 4;
							pd.set(ohanvxFJ[acn][i], ohanvyFJ[i], ohanvzFJ[i]);
						}
						else
							pd.set(ohanvxFJ[0][i], ohanvyFJ[i], ohanvzFJ[i]);
						pdRs.set(ohanvxFJ[0][i], ohanvyFJ[i], ohanvzFJ[i]);

						((FlightModelMain)flightmodel).bOnGoingOverHeadApproach = true;
					}
					else {
						pd.set(xFJ[i], lsyFJ[i], zFJ[i]);
						pdRs.set(pd);
					}
				}
				else {
					pd.set(x[i] * f1, lsy[i] * f1, z[i] * f2);
					pdRs.set(pd);
				}
				break;

			case 3: // '\003' : Clockwise traffic pattern, short width
				if (flightmodel.actor instanceof TypeFastJet) {
					if (flightmodel.AILandingWP360OverHeadApr == 1)
					{
						if (flightmodel instanceof Maneuver && ((Maneuver)flightmodel).Group != null) {
							int acn = ((Maneuver)flightmodel).Group.numInGroup((Aircraft)flightmodel.actor) % 4;
							pd.set(ohaafxFJ[acn][i], ohaafryFJ[i], ohaafzFJ[i]);
						}
						else
							pd.set(ohaafxFJ[0][i], ohaafryFJ[i], ohaafzFJ[i]);
						pdRs.set(ohaafxFJ[0][i], ohaafryFJ[i], ohaafzFJ[i]);

						((FlightModelMain)flightmodel).bOnGoingOverHeadApproach = true;
					}
					else if (flightmodel.AILandingWP360OverHeadApr == 2)
					{
						if (flightmodel instanceof Maneuver && ((Maneuver)flightmodel).Group != null) {
							int acn = ((Maneuver)flightmodel).Group.numInGroup((Aircraft)flightmodel.actor) % 4;
							pd.set(ohanvxFJ[acn][i], ohanvryFJ[i], ohanvzFJ[i]);
						}
						else
							pd.set(ohanvxFJ[0][i], ohanvryFJ[i], ohanvzFJ[i]);
						pdRs.set(ohanvxFJ[0][i], ohanvryFJ[i], ohanvzFJ[i]);

						((FlightModelMain)flightmodel).bOnGoingOverHeadApproach = true;
					}
					else {
						pd.set(xFJ[i], rsyFJ[i], zFJ[i]);
						pdRs.set(pd);
					}
				}
				else {
					pd.set(x[i] * f1, rsy[i] * f1, z[i] * f2);
					pdRs.set(pd);
				}
				break;

			case 4: // '\004' : Straight In
				if (flightmodel.actor instanceof TypeFastJet) {
					pd.set(sixFJ[i], siyFJ[i], sizFJ[i]);
					pdRs.set(xFJ[i], yFJ[i], zFJ[i]);
				}
				else {
					pd.set(six[i] * f1, siy[i] * f1, siz[i] * f2);
					pdRs.set(x[i] * f1, y[i] * f1, z[i] * f2);
				}
				break;
			}
			float vlanding;
/*			if (flightmodel.VAILandingDownwind > 0F && i < 7)
			{
				if (i < 5)
				{
					if (flightmodel.actor instanceof TypeFastJet) {
						if (i < 3) vlanding = Math.max(vFJ[i] * 0.278F, flightmodel.VminFLAPS * 1.2F);
						else vlanding = Math.max(vFJ[i] * 0.278F, flightmodel.Vmin * 1.2F);
						if (flightmodel.VminAI > 0F && vlanding < flightmodel.VminAI) vlanding = flightmodel.VminAI;
						if (vlanding > flightmodel.VAILandingDownwind)
							vlanding = flightmodel.VAILandingDownwind;
						waypoint.set(Math.min(vlanding, 300F * 0.4F)); // 300F=Mach1 (instead of Vmax)
					} else {
					  vlanding = Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.7F);
						if (vlanding > flightmodel.VAILandingDownwind)
							vlanding = flightmodel.VAILandingDownwind;
						waypoint.set(Math.min(vlanding, 300F * 0.4F));
					}
				}
				else
				{
					if (flightmodel.actor instanceof TypeFastJet) {
						vlanding = Math.max(vFJ[i] * 0.278F, flightmodel.Vmin * 1.2F);
						if (flightmodel.VminAI > 0F && vlanding < flightmodel.VminAI) vlanding = flightmodel.VminAI;
						if (vlanding > flightmodel.VAILandingDownwind)
							vlanding = 0.5F * (vlanding + flightmodel.VAILandingDownwind);
						waypoint.set(Math.min(vlanding, 300F * 0.4F)); // 300F=Mach1 (instead of Vmax)
					} else {
					  vlanding = Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.7F);
						if (vlanding > flightmodel.VAILandingDownwind)
							vlanding = 0.5F * (vlanding + flightmodel.VAILandingDownwind);
						waypoint.set(Math.min(vlanding, 300F * 0.4F));
					}
				}
			}
			else
			{ */
				if (flightmodel.actor instanceof TypeFastJet) {
					if (i < 3) {
						// By western: ignoring vFJ[i] for low speed Jets like IL-28
						if (flightmodel.VminFLAPS < 180F * 0.278F && flightmodel.Vmin < 230F * 0.278F && flightmodel.Vlanding < 180F * 0.278F)
							vlanding = flightmodel.VminFLAPS * 1.2F;
						else
							vlanding = Math.max(vFJ[i] * 0.278F, flightmodel.VminFLAPS * 1.2F);
					}
					else if (i == 3) {
						// By western: ignoring vFJ[i] for low speed Jets like IL-28
						if (flightmodel.VminFLAPS < 180F * 0.278F && flightmodel.Vmin < 230F * 0.278F && flightmodel.Vlanding < 180F * 0.278F)
							vlanding = flightmodel.Vmin * 1.2F;
						else
							vlanding = Math.max(vFJ[i] * 0.278F, flightmodel.Vmin * 1.2F);
					}
					else vlanding = Math.max(vFJ[i] * 0.278F, flightmodel.Vmin * 1.2F);
					if (flightmodel.VminAI > 0F && vlanding < flightmodel.VminAI) vlanding = flightmodel.VminAI;
					waypoint.set(Math.min(vlanding, 300F * 0.4F)); // 300F=Mach1 (instead of Vmax)
					waypointReserve.set(Math.min(vlanding, 300F * 0.4F));
				} else {
					waypoint.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.7F));
					waypointReserve.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.7F));
				}
/*			} */
			waypoint.Action = 2;
			runway1.loc.transform(pd);
			float f3 = (float) Engine.land().HQ_Air(pd.x, pd.y);
			pd.z -= f3 - f;
			pdRs.z -= f3 - f;
			pf.set(pd);
			pfRs.set(pdRs);
			waypoint.set(pf);
			waypointReserve.set(pf);
			way.add(waypoint);
			wayReserve.add(waypointReserve);
		}

		way.setLanding(true);
		wayReserve.setLanding(true);
		if (flightmodel.AP.way.curr().waypointType == 104) way.setLanding2(true);
		flightmodel.AP.way = way;
		flightmodel.APreserve.way = wayReserve;
		return true;
	}

	private boolean setStationaryPlaneTakeoff(Aircraft aircraft, Runway runway1) {
		Actor actor = Actor.getByName(aircraft.spawnActorName);
		if (!actor.isAlive()) {
			aircraft.destroy();
			return false;
		}
		Point3d point3d = aircraft.spawnLocSingleCoop.getPoint();
		Orient orient = aircraft.spawnLocSingleCoop.getOrient();
		point3d.z = World.land().HQ(point3d.x, point3d.y) + (double) aircraft.FM.Gears.H;
		Engine.land().N(point3d.x, point3d.y, v1);
		orient.orient(v1);
		orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
		aircraft.setOnGround(point3d, orient, zeroSpeed);
		if (aircraft.FM instanceof Maneuver) {
			((Maneuver) aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
			((Maneuver) aircraft.FM).rwLoc = runway1.loc;
		}
		aircraft.FM.AP.way.takeoffAirport = this;
		if (Actor.isValid(aircraft) && (aircraft.FM instanceof Maneuver)) {
			Maneuver maneuver = (Maneuver) aircraft.FM;
			if (maneuver.Group != null && maneuver.Group.w != null) maneuver.Group.w.takeoffAirport = this;
		}
		aircraft.spawnLocSingleCoop.set(aircraft.pos.getAbs());
		if (!Mission.isCoop() || aircraft.getWing().bOnlyAI) actor.destroy();
		aircraft.stationarySpawnLocSet = true;
		return true;
	}

	public void setTakeoff(Point3d point3d, Aircraft aaircraft[]) {
		Runway runway1 = nearestRunway(point3d);
		if (runway1 == null) return;
		Runway runway2 = oppositeRunway(runway1.loc);
		double d = 1000D;
		if (runway2 != null) d = runway1.loc.getPoint().distance(runway2.loc.getPoint());
		if (Time.tickCounter() != runway1.oldTickCounter) {
			runway1.oldTickCounter = Time.tickCounter();
			runway1.curPlaneShift = 0;
		}
		for (int i = 0; i < aaircraft.length; i++) {
			if (!Actor.isValid(aaircraft[i])) continue;
			if (aaircraft[i].spawnLocSingleCoop != null) {
				boolean flag = setStationaryPlaneTakeoff(aaircraft[i], runway1);
				if (!flag || !Mission.isCoop() || aaircraft[i].getWing().bOnlyAI) continue;
			}
			float f = aaircraft[i].collisionR() * 2.0F + 20F;
			for (int j = runway1.curPlaneShift; j > 0; j--) {
				runway1.planeShift[j] = runway1.planeShift[j - 1] + f;
				runway1.planes[j] = runway1.planes[j - 1];
			}

			runway1.planeShift[0] = 0.0F;
			runway1.planes[0] = aaircraft[i];
			runway1.curPlaneShift++;
			if (runway1.curPlaneShift > 31) throw new RuntimeException("Too many planes on airdrome");
			for (int k = 0; k < runway1.curPlaneShift; k++) {
				if (!Actor.isValid(runway1.planes[k])) continue;
				tmpLoc.set((double) runway1.planeShift[k] - d, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
				tmpLoc.add(runway1.loc);
				Point3d point3d1 = tmpLoc.getPoint();
				Orient orient = tmpLoc.getOrient();
				point3d1.z = World.land().HQ(point3d1.x, point3d1.y) + (double) runway1.planes[k].FM.Gears.H;
				Engine.land().N(point3d1.x, point3d1.y, v1);
				orient.orient(v1);
				orient.increment(0.0F, runway1.planes[k].FM.Gears.Pitch, 0.0F);
				runway1.planes[k].setOnGround(point3d1, orient, zeroSpeed);
				if (runway1.planes[k].FM instanceof Maneuver) {
					((Maneuver) runway1.planes[k].FM).direction = runway1.planes[k].pos.getAbsOrient().getAzimut();
					((Maneuver) runway1.planes[k].FM).rwLoc = runway1.loc;
				}
				runway1.planes[k].FM.AP.way.takeoffAirport = this;
			}

		}

		if (Actor.isValid(aaircraft[0]) && (aaircraft[0].FM instanceof Maneuver)) {
			Maneuver maneuver = (Maneuver) aaircraft[0].FM;
			if (maneuver.Group != null && maneuver.Group.w != null) maneuver.Group.w.takeoffAirport = this;
		}
	}

	public void setAsHomeAirport(Aircraft aaircraft[]) {
		for (int i = 0; i < aaircraft.length; i++)
			if (Actor.isValid(aaircraft[i])) aaircraft[i].FM.AP.way.takeoffAirport = this;

	}

	public double shiftFromLine(FlightModel flightmodel) {
		tmpLoc.set(flightmodel.Loc);
		if (flightmodel instanceof Maneuver) {
			Maneuver maneuver = (Maneuver) flightmodel;
			if (maneuver.rwLoc != null) {
				tmpLoc.sub(maneuver.rwLoc);
				return tmpLoc.getY();
			}
		}
		return 0.0D;
	}

	public boolean nearestRunway(Point3d point3d, Loc loc) {
		Runway runway1 = nearestRunway(point3d);
		if (runway1 != null) {
			loc.set(runway1.loc);
			return true;
		} else {
			return false;
		}
	}

	private Runway nearestRunway(Point3d point3d) {
		Runway runway1 = null;
		double d = 0.0D;
		np.set(point3d);
		int i = runway.size();
		for (int j = 0; j < i; j++) {
			Runway runway2 = (Runway) runway.get(j);
			np.z = runway2.loc.getPoint().z;
			double d1 = runway2.loc.getPoint().distanceSquared(np);
			if (runway1 == null || d1 < d) {
				runway1 = runway2;
				d = d1;
			}
		}

		if (d > 225000000D) runway1 = null;
		return runway1;
	}

	private Runway oppositeRunway(Loc loc) {
		int i = runway.size();
		for (int j = 0; j < i; j++) {
			Runway runway1 = (Runway) runway.get(j);
			pcur.set(runway1.loc.getPoint());
			loc.transformInv(pcur);
			if (Math.abs(pcur.y) >= 15D || pcur.x >= -800D || pcur.x <= -2500D) continue;
			p1.set(1.0D, 0.0D, 0.0D);
			p2.set(1.0D, 0.0D, 0.0D);
			runway1.loc.getOrient().transform(p1);
			loc.getOrient().transform(p2);
			if (p1.dot(p2) < -0.90D) return runway1;
		}

		return null;
	}

	private ArrayList runway;
	public static final int PT_RUNWAY = 1;
	public static final int PT_TAXI = 2;
	public static final int PT_STAY = 4;
	private static Point3d p3d = new Point3d();
	private static float x[] = { -500F, 0.0F, 220F, 2000F, 4000F, 5000F, 4000F, 0F, -2000F };
	private static float xFJ[] = { -1000F, -0.0F, 220F, 4000F, 8000F, 10000F, 8000F, 0F, -4000F };
	private static float y[] = { 0.0F, 0.0F, 0.0F, 0.0F, -500F, -2000F, -4000F, -4000F, -4000F };
	private static float yFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, -1000F, -4000F, -8000F, -8000F, -8000F };
	private static float z[] = { 0.0F, 6F, 20F, 160F, 500F, 600F, 700F, 700F, 700F };
	private static float zFJ[] = { 0.0F, 6F, 20F, 270F, 600F, 880F, 1100F, 1100F, 1100F };
	private static float v[] = { 0.0F, 180F, 220F, 240F, 270F, 280F, 300F, 300F, 300F };
	private static float vFJ[] = { 0.0F, 250F, 280F, 300F, 330F, 350F, 370F, 390F, 400F };
	private static float ry[] = { 0.0F, 0.0F, 0.0F, 0.0F, 500F, 2000F, 4000F, 4000F, 4000F };
	private static float ryFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, 1000F, 4000F, 8000F, 8000F, 8000F };
	private static float lsy[] = { 0.0F, 0.0F, 0.0F, 0.0F, -250F, -1000F, -2000F, -2000F, -2000F };
	private static float lsyFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, -500F, -2000F, -4000F, -4000F, -4000F };
	private static float rsy[] = { 0.0F, 0.0F, 0.0F, 0.0F, 250F, 1000F, 2000F, 2000F, 2000F };
	private static float rsyFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, 500F, 2000F, 4000F, 4000F, 4000F };
	private static float six[] = { -500F, 0.0F, 220F, 2000F, 3000F, 4000F, 5000F, 5500F, 6000F };
	private static float sixFJ[] = { -1000F, 0.0F, 220F, 4000F, 6000F, 8000F, 10000F, 11000F, 12000F };
	private static float siy[] = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
	private static float siyFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
	private static float siz[] = { 0.0F, 6F, 20F, 160F, 500F, 600F, 700F, 700F, 700F };
	private static float sizFJ[] = { 0.0F, 6F, 20F, 270F, 380F, 500F, 590F, 610F, 630F };
	private static float ohaafxFJ[][] = { { -1000F, -10.0F, 212F, 2220F, 3500F, 3500F, 2000F, -2000F, -4000F },
									   	  { -1000F,  -5.0F, 216F, 2220F, 3500F, 3500F, 2000F, -3000F, -5000F },
									   	  { -1000F,   0.0F, 220F, 2220F, 3500F, 3500F, 2000F, -4000F, -6000F },
									   	  { -1000F,   5.0F, 224F, 2220F, 3500F, 3500F, 2000F, -5000F, -7000F } };
	private static float ohaafyFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, -1230F, -2460F, -3700F, -3700F, -1000F };
	private static float ohaafryFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, 1230F, 2460F, 3700F, 3700F, 1000F };
	private static float ohaafzFJ[] = { 0.0F, 6F, 20F, 150F, 280F, 390F, 560F, 560F, 650F };
	private static float ohanvxFJ[][] = { { -1000F, -10.0F, 212F, 2200F, 3000F, 3000F, 2000F, -2000F, -4000F },
										  { -1000F,  -5.0F, 216F, 2200F, 3000F, 3000F, 2000F, -3000F, -5000F },
										  { -1000F,   0.0F, 220F, 2200F, 3000F, 3000F, 2000F, -4000F, -6000F },
										  { -1000F,   5.0F, 224F, 2200F, 3000F, 3000F, 2000F, -5000F, -7000F } };
	private static float ohanvyFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, -800F, -1600F, -2500F, -2500F, -400F };
	private static float ohanvryFJ[] = { 0.0F, 0.0F, 0.0F, 0.0F, 800F, 1600F, 2500F, 2500F, 400F };
	private static float ohanvzFJ[] = { 0.0F, 6F, 20F, 150F, 180F, 220F, 260F, 260F, 300F };
	private static Point3d pWay = new Point3d();
	private static Point3d pd = new Point3d();
	private static Point3f pf = new Point3f();
	// By western, make a reserve way for go-around.
	private static Point3d pdRs = new Point3d();
	private static Point3f pfRs = new Point3f();
	private static Vector3d v1 = new Vector3d();
	private static Vector3d zeroSpeed = new Vector3d();
	private static Loc tmpLoc = new Loc();
	private static Point3d pcur = new Point3d();
	private static Point3d np = new Point3d();
	private static Vector3d p1 = new Vector3d();
	private static Vector3d p2 = new Vector3d();

}