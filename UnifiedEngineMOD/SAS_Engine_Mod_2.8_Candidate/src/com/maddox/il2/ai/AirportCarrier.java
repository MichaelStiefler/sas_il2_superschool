/*Modified AirportCarrier class for the SAS Engine Mod*/
package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.ai.air.CellAirPlane;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Mission;
import com.maddox.il2.gui.GUINetClientDBrief;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.util.NumberTokenizer;

public class AirportCarrier extends Airport {
	public static final double cellSize = 1.0;
	private BigshipGeneric ship;
	private Loc[] runway;
	public BornPlace bornPlace = null;
	private List lastCarrierUsers = new ArrayList();
	private GUINetClientDBrief ui = null;
	private Loc clientLoc = null;
	private static Vector3d zeroSpeed = new Vector3d();
	private static final Loc invalidLoc = new Loc(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
	private static Vector3d v1 = new Vector3d();
	private Aircraft lastAddedAC = null;
	private CellAirPlane lastAddedCells = null;
	private int rnd1 = World.Rnd().nextInt(0, 30);
	private int rnd2 = World.Rnd().nextInt(40, 60);
	private Point_Stay[][] ownDefaultStayPoints = null;
	private boolean skipCheck = false;
	private static float[] x = { -100.0F, -20.0F, -10.0F, 1000.0F, 3000.0F, 4000.0F, 3000.0F, 0F, -1000F };
	private static float[] xJ = { -100.0F, -20.0F, -10.0F, 2000.0F, 4500.0F, 6000.0F, 4500.0F, 0F, -1500F };
	private static float[] xFJ = { -100.0F, -15.0F, 0.0F, 3000.0F, 6000.0F, 8000.0F, 6000.0F, 0F, -2000F };
	private static float[] y = { 0.0F, 0.0F, 0.0F, 0.0F, -500.0F, -1500.0F, -3000.0F, -3000.0F, -3000.0F };
	private static float[] yJ = { 0.0F, 0.0F, 0.0F, 0.0F, -750.0F, -2300.0F, -4500.0F, -4500.0F, -4500.0F };
	private static float[] yFJ = { 0.0F, 0.0F, 0.0F, 0.0F, -1000.0F, -3000.0F, -6000.0F, -6000.0F, -6000.0F };
	private static float[] z = { -4.0F, 5.0F, 5.0F, 150.0F, 450.0F, 500.0F, 500.0F, 500.0F, 500.0F };
	private static float[] zJ = { -4.0F, 5.0F, 5.0F, 230.0F, 450.0F, 500.0F, 500.0F, 600.0F, 600.0F };
	private static float[] zFJ = { -4.0F, 5.0F, 5.0F, 450.0F, 520.0F, 580.0F, 600.0F, 600.0F, 600.0F };
	private static float[] v = { 0.0F, 80.0F, 100.0F, 180.0F, 250.0F, 270.0F, 280.0F, 300.0F, 300.0F };
	private static float[] vJ = { 0.0F, 80.0F, 160.0F, 220.0F, 260.0F, 290.0F, 300.0F, 300.0F, 300.0F };
	private static float[] vFJ = { 0.0F, 80.0F, 260.0F, 300.0F, 320.0F, 340.0F, 370.0F, 400.0F, 400.0F };
	private Loc tmpLoc = new Loc();
	private Point3d tmpP3d = new Point3d();
	private Point3f tmpP3f = new Point3f();
	private Orient tmpOr = new Orient();
	public int curPlaneShift = 0;
	private int oldTickCounter = 0;
	private int oldIdleTickCounter = 0;
	private Loc r = new Loc();
	private static Vector3d startSpeed = new Vector3d();
	private static Vector3d shipSpeed = new Vector3d();
	private static Class _clsBigArrestorPlane = null;
	private static CellAirPlane _cellBigArrestorPlane = null;
	private static HashMap _clsMapArrestorPlane = new HashMap();

	class DeckUpdater extends Interpolate {
		public boolean tick() {
			if (ship().isAlive()) {
				if (Time.tickCounter() > oldTickCounter + 150 + rnd1) {
					oldTickCounter = Time.tickCounter();
					AirportCarrier.this.checkIsDeckClear();
				}
				if (Time.tickCounter() > oldIdleTickCounter + 2000 + rnd2) {
					oldIdleTickCounter = Time.tickCounter();
					AirportCarrier.this.checkPlaneIdle();
					skipCheck = false;
				}
			}
			return true;
		}
	}

	public BigshipGeneric ship() {
		return ship;
	}

	public AirportCarrier(BigshipGeneric bigshipgeneric, Loc[] locs) {
		ship = bigshipgeneric;
		pos = new ActorPosMove(this, new Loc());
		pos.setBase(bigshipgeneric, null, false);
		pos.reset();
		runway = locs;
		if (Mission.isDogfight()) {
			ownDefaultStayPoints = getStayPlaces(false);
			Point_Stay[][] point_stays = World.cur().airdrome.stay;
			Point_Stay[][] point_stays_0_ = (new Point_Stay[point_stays.length + ownDefaultStayPoints.length][]);
			int i = 0;
			for (int i_1_ = 0; i_1_ < point_stays.length; i_1_++)
				point_stays_0_[i++] = point_stays[i_1_];
			for (int i_2_ = 0; i_2_ < ownDefaultStayPoints.length; i_2_++)
				point_stays_0_[i++] = ownDefaultStayPoints[i_2_];
			World.cur().airdrome.stay = point_stays_0_;
		}
		startDeckOperations();
	}

	public void destroy() {
		if (lastCarrierUsers != null) lastCarrierUsers.clear();
		super.destroy();
	}

	public void setCustomStayPoints() {
		if (ship.zutiBornPlace != null && ship.zutiBornPlace.zutiMaxBasePilots > 0) {
			Point_Stay[][] point_stays = World.cur().airdrome.stay;
			Point_Stay[][] point_stays_3_ = getStayPlaces(true);
			Point_Stay[][] point_stays_4_ = (new Point_Stay[(point_stays.length - ownDefaultStayPoints.length + point_stays_3_.length)][]);
			int i = 0;
			for (int i_5_ = 0; i_5_ < point_stays.length; i_5_++) {
				boolean bool = false;
				for (int i_6_ = 0; i_6_ < ownDefaultStayPoints.length; i_6_++) {
					if (ownDefaultStayPoints[i_6_] == point_stays[i_5_]) {
						bool = true;
						break;
					}
				}
				if (!bool) point_stays_4_[i++] = point_stays[i_5_];
			}
			for (int i_7_ = 0; i_7_ < point_stays_3_.length; i_7_++)
				point_stays_4_[i++] = point_stays_3_[i_7_];
			World.cur().airdrome.stay = point_stays_4_;
			ownDefaultStayPoints = null;
		}
	}

	public boolean isAlive() {
		return Actor.isAlive(ship);
	}

	public int getArmy() {
		if (Actor.isAlive(ship)) return ship.getArmy();
		return super.getArmy();
	}

	public boolean landWay(FlightModel flightmodel) {
		Way way = new Way();
		tmpLoc.set(runway[1]);
		tmpLoc.add(ship.initLoc);
		float f = flightmodel.M.massEmpty * 3.333E-4F;
		if (f > 1.0F) f = 1.0F;
		if (f < 0.4F) f = 0.4F;
		for (int i = x.length - 1; i >= 0; i--) {
			WayPoint waypoint = new WayPoint();
			if (((Interpolate) (flightmodel)).actor instanceof TypeFastJet) tmpP3d.set((double) (xFJ[i]), (double) (yFJ[i]), (double) (zFJ[i]));
			else if (flightmodel.EI.engines[0].getType() == 2) tmpP3d.set((double) (xJ[i]), (double) (yJ[i]), (double) (zJ[i]));
			else tmpP3d.set((double) (x[i] * f), (double) (y[i] * f), (double) (z[i] * f));
			if (((Interpolate) (flightmodel)).actor instanceof TypeFastJet) {
				float vtmp = vFJ[i] * 0.278F;
				if (i == 2) {
					if (vtmp > flightmodel.VminFLAPS + 13F) vtmp = flightmodel.VminFLAPS + 13F;
				} else if (i == 3) {
					if (vtmp > flightmodel.Vmin + 13F) vtmp = flightmodel.Vmin + 13F;
				} else if (i == 4) {
					if (vtmp > flightmodel.Vmin + 25F) vtmp = flightmodel.Vmin + 25F;
				}
				waypoint.set(Math.min(vtmp, 300F * 0.36F)); // 300F=Mach1 , instead of Vmax
			} else if (flightmodel.EI.engines[0].getType() == 2) {
				float vtmp = vJ[i] * 0.278F;
				if (i == 2) {
					if (vtmp > flightmodel.VminFLAPS + 13F) vtmp = flightmodel.VminFLAPS + 13F;
				} else if (i == 3) {
					if (vtmp > flightmodel.Vmin + 13F) vtmp = flightmodel.Vmin + 13F;
				} else if (i == 4) {
					if (vtmp > flightmodel.Vmin + 25F) vtmp = flightmodel.Vmin + 25F;
				}
				waypoint.set(Math.min(vtmp, 300F * 0.30F)); // 300F=Mach1 , instead of Vmax
			} else waypoint.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.6F));
			waypoint.Action = 2;
			waypoint.sTarget = ship.name();
			tmpLoc.transform(tmpP3d);
			tmpP3f.set(tmpP3d);
			waypoint.set(tmpP3f);
			way.add(waypoint);
		}
		way.setLanding(true);
		flightmodel.AP.way = way;
		return true;
	}

	public void rebuildLandWay(FlightModel flightmodel) {
		if (!ship.isAlive()) flightmodel.AP.way.setLanding(false);
		else {
			tmpLoc.set(runway[1]);
			tmpLoc.add(ship.initLoc);
			float f = flightmodel.M.massEmpty * 3.333E-4F;
			if (f > 1.0F) f = 1.0F;
			if (f < 0.4F) f = 0.4F;
			for (int i = 0; i < x.length; i++) {
				WayPoint waypoint = flightmodel.AP.way.get(i);
				if (((Interpolate) (flightmodel)).actor instanceof TypeFastJet) tmpP3d.set((double) (xFJ[x.length - 1 - i]), (double) (yFJ[x.length - 1 - i]), (double) (zFJ[x.length - 1 - i]));
				else tmpP3d.set((double) (x[x.length - 1 - i] * f), (double) (y[x.length - 1 - i] * f), (double) (z[x.length - 1 - i] * f));
				tmpLoc.transform(tmpP3d);
				tmpP3f.set(tmpP3d);
				waypoint.set(tmpP3f);
			}
		}
	}

	public void rebuildLastPoint(FlightModel flightmodel) {
		if (Actor.isAlive(ship)) {
			int i = flightmodel.AP.way.Cur();
			flightmodel.AP.way.last();
			if (flightmodel.AP.way.curr().Action == 2) {
				ship.pos.getAbs(tmpP3d);
				flightmodel.AP.way.curr().set(tmpP3d);
			}
			flightmodel.AP.way.setCur(i);
		}
	}

	public double shiftFromLine(FlightModel flightmodel) {
		tmpLoc.set(flightmodel.Loc);
		r.set(runway[0]);
		r.add(ship.pos.getAbs());
		tmpLoc.sub(r);
		return tmpLoc.getY();
	}

	public boolean nearestRunway(Point3d point3d, Loc loc) {
		loc.add(runway[1], pos.getAbs());
		return true;
	}

	public int landingFeedback(Point3d point3d, Aircraft aircraft) {
		tmpLoc.set(runway[1]);
		tmpLoc.add(ship.initLoc);
		Aircraft aircraft_8_ = War.getNearestFriendAtPoint(tmpLoc.getPoint(), aircraft, 50.0F);
		if (aircraft_8_ != null && aircraft_8_ != aircraft) return 1;
		if (aircraft.FM.CT.GearControl > 0.0F) return 0;
		if (landingRequest > 0) return 1;
		landingRequest = 3000;
		return 0;
	}

	public void setTakeoffOld(Point3d point3d, Aircraft aircraft) {
		if (Actor.isValid(aircraft)) {
			r.set(runway[0]);
			r.add(ship.pos.getAbs());
			if (!Mission.isDogfight() && Time.tickCounter() != oldTickCounter) {
				oldTickCounter = Time.tickCounter();
				curPlaneShift = 0;
			}
			curPlaneShift++;
			aircraft.FM.setStationedOnGround(false);
			aircraft.FM.setWasAirborne(true);
			tmpLoc.set((double) -((float) curPlaneShift * 200.0F), (double) -((float) curPlaneShift * 100.0F), 300.0, 0.0F, 0.0F, 0.0F);
			tmpLoc.add(r);
			aircraft.pos.setAbs(tmpLoc);
			aircraft.pos.getAbs(tmpP3d, tmpOr);
			startSpeed.set(100.0, 0.0, 0.0);
			tmpOr.transform(startSpeed);
			aircraft.setSpeed(startSpeed);
			aircraft.pos.reset();
			if (aircraft.FM instanceof Maneuver) ((Maneuver) aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
			aircraft.FM.AP.way.takeoffAirport = this;
			if (aircraft == World.getPlayerAircraft()) {
				aircraft.FM.EI.setCurControlAll(true);
				aircraft.FM.EI.setEngineRunning();
				aircraft.FM.CT.setPowerControl(0.75F);
			}
		}
	}

	private boolean setStationaryPlaneTakeoff(Aircraft aircraft) {
		Actor actor = Actor.getByName(aircraft.spawnActorName);
		if (!actor.isAlive()) {
			aircraft.destroy();
			return false;
		}
		Point3d point3d = aircraft.spawnLocSingleCoop.getPoint();
		Orient orient = aircraft.spawnLocSingleCoop.getOrient();
		point3d.z = (World.land().HQ(point3d.x, point3d.y) + (double) aircraft.FM.Gears.H);
		Engine.land().N(point3d.x, point3d.y, v1);
		orient.orient(v1);
		orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
		aircraft.setOnGround(point3d, orient, zeroSpeed);
		if (aircraft.FM instanceof Maneuver) {
			((Maneuver) aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
			((Maneuver) aircraft.FM).rwLoc = r;
		}
		aircraft.FM.AP.way.takeoffAirport = this;
		aircraft.spawnLocSingleCoop.set(aircraft.pos.getAbs());
		if (!Mission.isCoop() || aircraft.getWing().bOnlyAI) actor.destroy();
		aircraft.stationarySpawnLocSet = true;
		return true;
	}

	public double speedLen() {
		ship.getSpeed(shipSpeed);
		return shipSpeed.length();
	}

	private void checkIsDeckClear() {
		if (!skipCheck) {
			if (lastAddedAC != null && lastAddedAC.isDestroyed()) {
				CellAirField cellairfield = ship.getCellTO();
				boolean bool = cellairfield.removeAirPlane(lastAddedCells);
				if (bool) curPlaneShift--;
				lastAddedAC = null;
				lastAddedCells = null;
			}
			boolean bool = true;
			for (int i = lastCarrierUsers.size() - 1; i >= 0; i--) {
				Aircraft aircraft = (Aircraft) lastCarrierUsers.get(i);
				if (aircraft != null && !aircraft.isDestroyed() && aircraft.isAlive() && (aircraft.FM.Gears.isUnderDeck() || NetAircraft.isOnCarrierDeck(this, aircraft.pos.getAbs()))) bool = false;
				else lastCarrierUsers.remove(aircraft);
			}
			if (bool) {
				lastCarrierUsers.clear();
				deckCleared();
			}
		}
	}

	private void checkPlaneIdle() {
		for (int i = lastCarrierUsers.size() - 1; i >= 0; i--) {
			Aircraft aircraft = (Aircraft) lastCarrierUsers.get(i);
			if (aircraft != null && !aircraft.isDestroyed() && (aircraft.FM.Gears.isUnderDeck() || NetAircraft.isOnCarrierDeck(this, aircraft.pos.getAbs()))) {
				aircraft.idleTimeOnCarrier++;
				if (aircraft.idleTimeOnCarrier >= 6) {
					if (aircraft.isNetPlayer() && !aircraft.isNetMaster()) {
						Aircraft aircraft_9_ = aircraft;
						NetUser netuser = (((NetAircraft.AircraftNet) aircraft_9_.net).netUser);
						netuser.kick(netuser);
					} else if (aircraft.FM.AP.way.size() != 1) {
						aircraft.net.destroy();
						aircraft.destroy();
						lastCarrierUsers.remove(aircraft);
					}
				}
			}
		}
	}

	public Loc setClientTakeOff(Point3d point3d, Aircraft aircraft) {
		Loc loc = null;
		if (clientLoc != null) loc = new Loc(clientLoc);
		clientLoc = null;
		if (loc == null || !isLocValid(loc)) {
			setTakeoffOld(point3d, aircraft);
			Loc loc_10_ = aircraft.pos.getAbs();
			double d = World.Rnd().nextDouble(400.0, 800.0);
			double d_11_ = World.Rnd().nextDouble(400.0, 800.0);
			if (World.Rnd().nextFloat() < 0.5F) d *= -1.0;
			if (World.Rnd().nextFloat() < 0.5F) d_11_ *= -1.0;
			Point3d point3d_12_ = new Point3d(d, d_11_, 0.0);
			loc_10_.add(point3d_12_);
			aircraft.pos.setAbs(loc_10_);
			return loc_10_;
		}
		loc.add(ship.pos.getAbs());
		Point3d point3d_13_ = loc.getPoint();
		Orient orient = loc.getOrient();
		orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
		ship.getSpeed(shipSpeed);
		aircraft.setOnGround(point3d_13_, orient, shipSpeed);
		if (aircraft.FM instanceof Maneuver) ((Maneuver) aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
		aircraft.FM.AP.way.takeoffAirport = this;
		aircraft.FM.brakeShoe = true;
		aircraft.FM.turnOffCollisions = true;
		aircraft.FM.brakeShoeLoc.set(aircraft.pos.getAbs());
		aircraft.FM.brakeShoeLoc.sub(ship.pos.getAbs());
		aircraft.FM.brakeShoeLastCarrier = ship;
		aircraft.FM.Gears.bFlatTopGearCheck = true;
		aircraft.makeMirrorCarrierRelPos();
		if (aircraft.FM.CT.bHasWingControl) {
			aircraft.FM.CT.wingControl = 1.0F;
			aircraft.FM.CT.forceWing(1.0F);
		}
		return loc;
	}

	private Point3d reservePlaceForPlane(CellAirPlane cellairplane, Aircraft aircraft) {
		CellAirField cellairfield = ship.getCellTO();
		if (cellairfield.findPlaceForAirPlane(cellairplane)) {
			cellairfield.placeAirPlane(cellairplane, cellairfield.resX(), cellairfield.resY());
			double d = (-cellairfield.leftUpperCorner().x - (double) cellairfield.resX() * cellairfield.getCellSize() - cellairplane.ofsX);
			double d_14_ = (cellairfield.leftUpperCorner().y - (double) cellairfield.resY() * cellairfield.getCellSize() - cellairplane.ofsY);
			double d_15_ = runway[0].getZ();
			curPlaneShift++;
			if (Mission.isDogfight() && ship.net.isMaster()) {
				if (aircraft != null) {
					lastCarrierUsers.add(aircraft);
					lastAddedAC = aircraft;
				}
				lastAddedCells = cellairplane;
			}
			return new Point3d(d_14_, d, d_15_);
		}
		return null;
	}

	public void setStationaryPlaneTakeoff(Aircraft[] aircrafts) {
		if (!Mission.isDogfight()) {
			for (int i = 0; i < aircrafts.length; i++) {
				if (aircrafts[i] != null && aircrafts[i].spawnLocSingleCoop != null) setStationaryPlaneTakeoff(aircrafts[i]);
			}
		}
	}

	public void setTakeoff(Point3d point3d, Aircraft[] aircrafts) {
		setStationaryPlaneTakeoff(aircrafts);
		CellAirField cellairfield = ship.getCellTO();
		if (cellairfield == null) {
			for (int i = 0; i < aircrafts.length; i++)
				setTakeoffOld(point3d, aircrafts[i]);
		} else {
			if (!Mission.isDogfight() && Time.tickCounter() != oldTickCounter) {
				oldTickCounter = Time.tickCounter();
				curPlaneShift = 0;
				cellairfield.freeCells();
			}
			ship.getSpeed(shipSpeed);

			// TODO: CTO Mod
			// ----------------------------------------
			for (int k = 0; k < aircrafts.length; k++) {
				if (Actor.isValid(aircrafts[k])) {
					if(aircrafts[k].stationarySpawnLocSet && (!Mission.isCoop() || aircrafts[k].getWing().bOnlyAI)) continue;
					CellAirPlane cellairplane = aircrafts[k].getCellAirPlane();

					// TODO: CTO Mod: replaced old IF below
					// ----------------------------------------
					if (aircrafts[k].FM.WingspanFolded != 0F && aircrafts[k].FM.CT.bHasWingControl)
						cellairplane.setFoldedWidth((int) aircrafts[k].FM.WingspanFolded);
					if (cellairfield.findPlaceForAirPlaneCarrier(cellairplane)) {
						if (aircrafts[k].FM.CT.bHasWingControl) {
							aircrafts[k].FM.CT.wingControl = 1.0F;
							aircrafts[k].FM.CT.forceWing(1.0F);
						}
						cellairfield.placeAirPlaneCarrier(cellairplane, cellairfield.resX(), cellairfield.resY());
						double d = -((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).x - (double) cellairfield.resX() * cellairfield.getCellSize() - (double) (cellairplane.getWidth() / 2);
						double d1 = ((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).y - (double) cellairfield.resY() * cellairfield.getCellSize() - cellairplane.ofsY;
						double d2 = runway[0].getZ();
						tmpLoc.set(d1, d, d2 + (double) aircrafts[k].FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
						tmpLoc.add(((Actor) (ship)).pos.getAbs());
						Point3d point3d1 = tmpLoc.getPoint();
						Orient orient = tmpLoc.getOrient();
						orient.increment(0.0F, aircrafts[k].FM.Gears.Pitch, 0.0F);
						aircrafts[k].setOnGround(point3d1, orient, shipSpeed);
						if (aircrafts[k].FM instanceof com.maddox.il2.ai.air.Maneuver) ((com.maddox.il2.ai.air.Maneuver) aircrafts[k].FM).direction = ((Actor) (aircrafts[k])).pos.getAbsOrient().getAzimut();
						aircrafts[k].FM.AP.way.takeoffAirport = this;
						aircrafts[k].FM.brakeShoe = true;
						aircrafts[k].FM.turnOffCollisions = true;
						aircrafts[k].FM.brakeShoeLoc.set(((Actor) (aircrafts[k])).pos.getAbs());
						aircrafts[k].FM.brakeShoeLoc.sub(((Actor) (ship)).pos.getAbs());
						aircrafts[k].FM.brakeShoeLastCarrier = ship;
						aircrafts[k].FM.Gears.bFlatTopGearCheck = true;
						aircrafts[k].makeMirrorCarrierRelPos();
					}
					// ----------------------------------------
					else setTakeoffOld(point3d, aircrafts[k]);
				}
			}
			if (Actor.isValid(aircrafts[0]) && aircrafts[0].FM instanceof Maneuver) {
				Maneuver maneuver = (Maneuver) aircrafts[0].FM;
				if (maneuver.Group != null && maneuver.Group.w != null) maneuver.Group.w.takeoffAirport = this;
			}
		}
	}

	public void getTakeoff(Aircraft aircraft, Loc loc) {
		tmpLoc.sub(loc, ship.initLoc);
		tmpLoc.set(tmpLoc.getPoint().x, tmpLoc.getPoint().y, runway[0].getZ() + (double) aircraft.FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
		tmpLoc.add(ship.pos.getAbs());
		loc.set(tmpLoc);
		Orient orient = loc.getOrient();
		orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
		if (aircraft.FM instanceof Maneuver) ((Maneuver) aircraft.FM).direction = loc.getAzimut();
		aircraft.FM.AP.way.takeoffAirport = this;
		aircraft.FM.brakeShoe = true;
		aircraft.FM.turnOffCollisions = true;
		aircraft.FM.brakeShoeLoc.set(loc);
		aircraft.FM.brakeShoeLoc.sub(ship.pos.getAbs());
		aircraft.FM.brakeShoeLastCarrier = ship;
	}

	public float height() {
		return (float) (ship.pos.getAbs().getZ() + runway[0].getZ());
	}

	public static boolean isPlaneContainsArrestor(Class var_class) {
		clsBigArrestorPlane();
		return _clsMapArrestorPlane.containsKey(var_class);
	}

	private static Class clsBigArrestorPlane() {
		if (_clsBigArrestorPlane != null) return _clsBigArrestorPlane;
		double d = 0.0;
		SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini", 0);
		int i = sectfile.sections();
		for (int i_20_ = 0; i_20_ < i; i_20_++) {
			int i_21_ = sectfile.vars(i_20_);
			for (int i_22_ = 0; i_22_ < i_21_; i_22_++) {
				StringTokenizer stringtokenizer = new StringTokenizer(sectfile.value(i_20_, i_22_));
				if (stringtokenizer.hasMoreTokens()) {
					String string_23_ = ("com.maddox.il2.objects." + stringtokenizer.nextToken());
					Class var_class = null;
					String string_24_ = null;
					try {
						var_class = Class.forName(string_23_);
						string_24_ = Property.stringValue(var_class, "FlightModel", null);
					} catch (Exception exception) {
						System.out.println(exception.getMessage());
						exception.printStackTrace();
					}
					try {
						if (string_24_ != null) {
							SectFile sectfile_25_ = FlightModelMain.sectFile(string_24_);
							if (sectfile_25_.get("Controls", "CArrestorHook", 0) == 1) {
								_clsMapArrestorPlane.put(var_class, null);
								String string_26_ = Aircraft.getPropertyMesh(var_class, null);
								SectFile sectfile_27_ = new SectFile(string_26_, 0);
								String string_28_ = sectfile_27_.get("_ROOT_", "CollisionObject", (String) null);
								if (string_28_ != null) {
									NumberTokenizer numbertokenizer = new NumberTokenizer(string_28_);
									if (numbertokenizer.hasMoreTokens()) {
										numbertokenizer.next();
										if (numbertokenizer.hasMoreTokens()) {
											double d_29_ = numbertokenizer.next(-1.0);
											if (d_29_ > 0.0 && d < d_29_) {
												d = d_29_;
												_clsBigArrestorPlane = var_class;
											}
										}
									}
								}
							}
						}
					} catch (Exception exception) {
						System.out.println(exception.getMessage());
						exception.printStackTrace();
					}
				}
			}
		}
		return _clsBigArrestorPlane;
	}

	private static CellAirPlane cellBigArrestorPlane() {
		if (_cellBigArrestorPlane != null) return _cellBigArrestorPlane;
		_cellBigArrestorPlane = Aircraft.getCellAirPlane(clsBigArrestorPlane());
		return _cellBigArrestorPlane;
	}

	private Point_Stay[][] getStayPlaces(boolean bool) {
		Point_Stay[][] point_stays = null;
		Class var_class = ship.getClass();
		point_stays = (Point_Stay[][]) Property.value(var_class, "StayPlaces", null);
		if (point_stays == null || bool) {
			ArrayList arraylist = new ArrayList();
			if (bool) {
				if (ship.zutiBornPlace != null) {
					int i = ship.zutiBornPlace.zutiMaxBasePilots;
					if (i > 164) i = 164;
					for (int i_30_ = 0; i_30_ < i; i_30_++)
						arraylist.add(new Point2d(-1.0, 1.0));
				}
			} else {
				String string = ship.getShipProp().typicalPlaneClass;
				CellAirPlane cellairplane;
				if (string != null && !string.equals("")) {
					try {
						Class var_class_31_ = ObjIO.classForName(string);
						cellairplane = Aircraft.getCellAirPlane(var_class_31_);
					} catch (ClassNotFoundException classnotfoundexception) {
						cellairplane = cellBigArrestorPlane();
					}
				} else cellairplane = cellBigArrestorPlane();
				CellAirField cellairfield = ship.getCellTO();
				cellairfield = (CellAirField) cellairfield.getClone();
				for (;;) {
					cellairplane = (CellAirPlane) cellairplane.getClone();
					if (!cellairfield.findPlaceForAirPlane(cellairplane)) break;
					cellairfield.placeAirPlane(cellairplane, cellairfield.resX(), cellairfield.resY());
					double d = (-cellairfield.leftUpperCorner().x - ((double) cellairfield.resX() * cellairfield.getCellSize()) - cellairplane.ofsX);
					double d_32_ = (cellairfield.leftUpperCorner().y - ((double) cellairfield.resY() * cellairfield.getCellSize()) - cellairplane.ofsY);
					arraylist.add(new Point2d(d_32_, d));
				}
			}
			int i = arraylist.size();
			if (i > 0) {
				point_stays = new Point_Stay[i][1];
				for (int i_33_ = 0; i_33_ < i; i_33_++) {
					Point2d point2d = (Point2d) arraylist.get(i_33_);
					point_stays[i_33_][0] = new Point_Stay((float) point2d.x, (float) point2d.y);
				}
				if (!bool) Property.set(var_class, "StayPlaces", point_stays);
			}
		}
		if (point_stays == null) return null;
		Point_Stay[][] point_stays_34_ = new Point_Stay[point_stays.length][1];
		for (int i = 0; i < point_stays.length; i++) {
			Point_Stay point_stay = point_stays[i][0];
			double d = (double) point_stay.x;
			double d_35_ = (double) point_stay.y;
			double d_36_ = runway[0].getZ();
			tmpLoc.set(d, d_35_, d_36_, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
			tmpLoc.add(ship.pos.getAbs());
			Point3d point3d = tmpLoc.getPoint();
			point_stays_34_[i][0] = new Point_Stay((float) point3d.x, (float) point3d.y);
		}
		return point_stays_34_;
	}

	public void setCellUsed(Aircraft aircraft) {
		skipCheck = false;
		if (ship.net.isMaster()) {
			lastCarrierUsers.add(aircraft);
			lastAddedAC = aircraft;
		}
		if (aircraft.FM.CT.bHasWingControl && !aircraft.isNetPlayer()) aircraft.FM.CT.wingControl = 0.0F;
	}

	public Loc requestCell(Aircraft aircraft) {
		if (!ship().isAlive()) return invalidLoc;
		CellAirPlane cellairplane = aircraft.getCellAirPlane();
		Point3d point3d = reservePlaceForPlane(cellairplane, null);
		if (point3d != null) {
			skipCheck = true;
			double d = point3d.x;
			double d_37_ = point3d.y;
			double d_38_ = point3d.z;
			Loc loc = new Loc();
			loc.set(d, d_37_, d_38_ + (double) aircraft.FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
			return loc;
		}
		return invalidLoc;
	}

	private void deckCleared() {
		curPlaneShift = 0;
		CellAirField cellairfield = ship.getCellTO();
		cellairfield.freeCells();
	}

	public void setGuiCallback(GUINetClientDBrief guinetclientdbrief) {
		ui = guinetclientdbrief;
	}

	public void setClientLoc(Loc loc) {
		clientLoc = loc;
		boolean bool = isLocValid(loc);
		if (ui != null) ui.flyFromCarrier(bool);
	}

	private boolean isLocValid(Loc loc) {
		if (loc == null) return false;
		if ((int) loc.getX() == 0 && (int) loc.getY() == 0 && (int) loc.getZ() == 0 && (int) loc.getTangage() == 0 && (int) loc.getKren() == 0) return false;
		return true;
	}

	public void startDeckOperations() {
		if (Mission.isDogfight() && ship.net.isMaster()) interpPut(new DeckUpdater(), "deck_operations", Time.current(), null);
	}
}
