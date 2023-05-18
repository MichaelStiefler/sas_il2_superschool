/*4.10.1 class + CTO Mod. I have removed new code for carrier spawn handlind (queue by DT...)*/
package com.maddox.il2.ai;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Mission;
import com.maddox.il2.gui.GUINetClientDBrief;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class AirportCarrier extends Airport
{
	public static final double cellSize = 1.0;
	private BigshipGeneric ship;
	private Loc[] runway;
	public BornPlace bornPlace2Move;
	public int bornPlaceArmyBk;
	private GUINetClientDBrief ui = null;
	private Loc clientLoc = null;
	private static final Loc invalidLoc = new Loc(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
	private static float[] x = {-100.0F, -20.0F, -10.0F, 1000.0F, 3000.0F, 4000.0F, 3000.0F, 0.0F, 0.0F};
	private static float[] y = {0.0F, 0.0F, 0.0F, 0.0F, -500.0F, -1500.0F, -3000.0F, -3000.0F, -3000.0F};
	private static float[] z = {-4.0F, 5.0F, 5.0F, 150.0F, 450.0F, 500.0F, 500.0F, 500.0F, 500.0F};
	private static float[] v = {0.0F, 80.0F, 100.0F, 180.0F, 250.0F, 270.0F, 280.0F, 300.0F, 300.0F};
	private Loc tmpLoc = new Loc();
	private Point3d tmpP3d = new Point3d();
	private Point3f tmpP3f = new Point3f();
	private Orient tmpOr = new Orient();
	public int curPlaneShift = 0;
	private Loc r = new Loc();
	private static Vector3d startSpeed = new Vector3d();
	private static Vector3d shipSpeed = new Vector3d();
	private static Class _clsBigArrestorPlane = null;
	private static CellAirPlane _cellBigArrestorPlane = null;
	private static HashMap _clsMapArrestorPlane = new HashMap();
		
	public BigshipGeneric ship()
	{
		return ship;
	}
	
	public AirportCarrier(BigshipGeneric bigshipgeneric, Loc[] locs)
	{
		ship = bigshipgeneric;
		pos = new ActorPosMove(this, new Loc());
		pos.setBase(bigshipgeneric, null, false);
		pos.reset();
		runway = locs;
		if (Mission.isDogfight())
		{
			Point_Stay[][] point_stays = getStayPlaces();
			Point_Stay[][] point_stays_0_ = World.cur().airdrome.stay;
			Point_Stay[][] point_stays_1_ = new Point_Stay[point_stays_0_.length + point_stays.length][];
			int i = 0;
			for (int i_2_ = 0; i_2_ < point_stays_0_.length; i_2_++)
				point_stays_1_[i++] = point_stays_0_[i_2_];
			for (int i_3_ = 0; i_3_ < point_stays.length; i_3_++)
				point_stays_1_[i++] = point_stays[i_3_];
			World.cur().airdrome.stay = point_stays_1_;
		}
	}
	
	public void disableBornPlace()
	{
		if (bornPlace2Move != null)
			bornPlace2Move.army = -2;
	}
	
	public void enableBornPlace()
	{
		bornPlace2Move.army = bornPlaceArmyBk;
	}
	
	public boolean isAlive()
	{
		return Actor.isAlive(ship);
	}
	
	public int getArmy()
	{
		if (Actor.isAlive(ship))
			return ship.getArmy();
		return super.getArmy();
	}
	
	public boolean landWay(FlightModel flightmodel)
	{
		Way way = new Way();
		tmpLoc.set(runway[1]);
		tmpLoc.add(ship.initLoc);
		float f = flightmodel.M.massEmpty * 3.333E-4F;
		if (f > 1.0F)
			f = 1.0F;
		if (f < 0.4F)
			f = 0.4F;
		for (int i = x.length - 1; i >= 0; i--)
		{
			WayPoint waypoint = new WayPoint();
			tmpP3d.set((double)(x[i] * f), (double)(y[i] * f), (double)(z[i] * f));
			waypoint.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.6F));
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
	
	public void rebuildLandWay(FlightModel flightmodel)
	{
		if (!ship.isAlive())
			flightmodel.AP.way.setLanding(false);
		else
		{
			tmpLoc.set(runway[1]);
			tmpLoc.add(ship.initLoc);
			float f = flightmodel.M.massEmpty * 3.333E-4F;
			if (f > 1.0F)
				f = 1.0F;
			if (f < 0.4F)
				f = 0.4F;
			for (int i = 0; i < x.length; i++)
			{
				WayPoint waypoint = flightmodel.AP.way.get(i);
				tmpP3d.set((double)(x[x.length - 1 - i] * f), (double)(y[x.length - 1 - i] * f), (double)(z[x.length - 1 - i] * f));
				tmpLoc.transform(tmpP3d);
				tmpP3f.set(tmpP3d);
				waypoint.set(tmpP3f);
			}
		}
	}
	
	public void rebuildLastPoint(FlightModel flightmodel)
	{
		if (Actor.isAlive(ship))
		{
			int i = flightmodel.AP.way.Cur();
			flightmodel.AP.way.last();
			if (flightmodel.AP.way.curr().Action == 2)
			{
				ship.pos.getAbs(tmpP3d);
				flightmodel.AP.way.curr().set(tmpP3d);
			}
			flightmodel.AP.way.setCur(i);
		}
	}
	
	public double ShiftFromLine(FlightModel flightmodel)
	{
		tmpLoc.set(flightmodel.Loc);
		r.set(runway[0]);
		r.add(ship.pos.getAbs());
		tmpLoc.sub(r);
		return tmpLoc.getY();
	}
	
	public boolean nearestRunway(Point3d point3d, Loc loc)
	{
		loc.add(runway[1], pos.getAbs());
		return true;
	}
	
	public int landingFeedback(Point3d point3d, Aircraft aircraft)
	{
		tmpLoc.set(runway[1]);
		tmpLoc.add(ship.initLoc);
		Aircraft aircraft_4_ = War.getNearestFriendAtPoint(tmpLoc.getPoint(), aircraft, 50.0F);
		if (aircraft_4_ != null && aircraft_4_ != aircraft)
			return 1;
		if (aircraft.FM.CT.GearControl > 0.0F)
			return 0;
		if (landingRequest > 0)
			return 1;
		landingRequest = 3000;
		return 0;
	}
	
	public void setTakeoffOld(Point3d point3d, Aircraft aircraft)
	{
		if (Actor.isValid(aircraft))
		{
			r.set(runway[0]);
			r.add(ship.pos.getAbs());
			curPlaneShift++;
			aircraft.FM.setStationedOnGround(false);
			aircraft.FM.setWasAirborne(true);
			tmpLoc.set((double)-((float)curPlaneShift * 200.0F), (double)-((float)curPlaneShift * 100.0F), 300.0, 0.0F, 0.0F, 0.0F);
			tmpLoc.add(r);
			aircraft.pos.setAbs(tmpLoc);
			aircraft.pos.getAbs(tmpP3d, tmpOr);
			startSpeed.set(100.0, 0.0, 0.0);
			tmpOr.transform(startSpeed);
			aircraft.setSpeed(startSpeed);
			aircraft.pos.reset();
			if (aircraft.FM instanceof Maneuver)
				((Maneuver)aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
			aircraft.FM.AP.way.takeoffAirport = this;
			if (aircraft == World.getPlayerAircraft())
			{
				aircraft.FM.EI.setCurControlAll(true);
				aircraft.FM.EI.setEngineRunning();
				aircraft.FM.CT.setPowerControl(0.75F);
			}
		}
	}
	
	public double speedLen()
	{
		ship.getSpeed(shipSpeed);
		return shipSpeed.length();
	}
	
	public Loc setClientTakeOff(Point3d point3d, Aircraft aircraft)
	{
		Loc loc = null;
		if (clientLoc != null)
			loc = new Loc(clientLoc);
		clientLoc = null;
		if (loc == null || !isLocValid(loc))
		{
			setTakeoffOld(point3d, aircraft);
			Loc loc_6_ = aircraft.pos.getAbs();
			double d = World.Rnd().nextDouble(400.0, 800.0);
			double d_7_ = World.Rnd().nextDouble(400.0, 800.0);
			if (World.Rnd().nextFloat() < 0.5F)
				d *= -1.0;
			if (World.Rnd().nextFloat() < 0.5F)
				d_7_ *= -1.0;
			Point3d point3d_8_ = new Point3d(d, d_7_, 0.0);
			loc_6_.add(point3d_8_);
			aircraft.pos.setAbs(loc_6_);
			return loc_6_;
		}
		loc.add(ship.pos.getAbs());
		Point3d point3d_9_ = loc.getPoint();
		Orient orient = loc.getOrient();
		orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
		ship.getSpeed(shipSpeed);
		aircraft.setOnGround(point3d_9_, orient, shipSpeed);
		if (aircraft.FM instanceof Maneuver)
			((Maneuver)aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
		aircraft.FM.AP.way.takeoffAirport = this;
		aircraft.FM.brakeShoe = true;
		aircraft.FM.turnOffCollisions = true;
		aircraft.FM.brakeShoeLoc.set(aircraft.pos.getAbs());
		aircraft.FM.brakeShoeLoc.sub(ship.pos.getAbs());
		aircraft.FM.brakeShoeLastCarrier = ship;
		aircraft.FM.Gears.bFlatTopGearCheck = true;
		aircraft.makeMirrorCarrierRelPos();
		if (aircraft.FM.CT.bHasWingControl)
		{
			aircraft.FM.CT.wingControl = 1.0F;
			aircraft.FM.CT.forceWing(1.0F);
		}
		return loc;
	}
	
	private Point3d reservePlaceForPlane(CellAirPlane cellairplane, Aircraft aircraft)
	{
		CellAirField cellairfield = ship.getCellTO();
		if (cellairfield.findPlaceForAirPlane(cellairplane))
		{
			cellairfield.placeAirPlane(cellairplane, cellairfield.resX(), cellairfield.resY());
			double d = (-cellairfield.leftUpperCorner().x - (double)cellairfield.resX() * cellairfield.getCellSize() - cellairplane.ofsX);
			double d_10_ = (cellairfield.leftUpperCorner().y - (double)cellairfield.resY() * cellairfield.getCellSize() - cellairplane.ofsY);
			double d_11_ = runway[0].getZ();
			curPlaneShift++;
			return new Point3d(d_10_, d, d_11_);
		}
		return null;
	}
	
	public void setTakeoff(Point3d point3d, Aircraft[] aircrafts)
	{
		CellAirField cellairfield = ship.getCellTO();
		if (cellairfield == null)
		{
			for (int i = 0; i < aircrafts.length; i++)
				setTakeoffOld(point3d, aircrafts[i]);
		}
		else
		{
			ship.getSpeed(shipSpeed);
			
			// TODO: CTO Mod
			// ----------------------------------------
			int j = setAircraftDimensions(aircrafts[0]);
			// ----------------------------------------
			
			for (int k = 0; k < aircrafts.length; k++)
			{
				if (Actor.isValid(aircrafts[k]))
				{
					CellAirPlane cellairplane = aircrafts[k].getCellAirPlane();
					
					// TODO: CTO Mod: replaced old IF below
					// ----------------------------------------
					cellairplane.setFoldedWidth(j);
					
					if(cellairfield.findPlaceForAirPlaneCarrier(cellairplane))
	                {
	                    cellairfield.placeAirPlaneCarrier(cellairplane, cellairfield.resX(), cellairfield.resY());
	                    double d = -((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).x - (double)cellairfield.resX() * cellairfield.getCellSize() - (double)(cellairplane.getWidth() / 2);
	                    double d1 = ((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).y - (double)cellairfield.resY() * cellairfield.getCellSize() - cellairplane.ofsY;
	                    double d2 = runway[0].getZ();
	                    tmpLoc.set(d1, d, d2 + (double)((FlightModelMain) (((SndAircraft) (aircrafts[k])).FM)).Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
	                    tmpLoc.add(((Actor) (ship)).pos.getAbs());
	                    Point3d point3d1 = tmpLoc.getPoint();
	                    Orient orient = tmpLoc.getOrient();
	                    orient.increment(0.0F, ((FlightModelMain) (((SndAircraft) (aircrafts[k])).FM)).Gears.Pitch, 0.0F);
	                    aircrafts[k].setOnGround(point3d1, orient, shipSpeed);
	                    if(((SndAircraft) (aircrafts[k])).FM instanceof com.maddox.il2.ai.air.Maneuver)
	                        ((com.maddox.il2.ai.air.Maneuver)((SndAircraft) (aircrafts[k])).FM).direction = ((Actor) (aircrafts[k])).pos.getAbsOrient().getAzimut();
	                    ((FlightModelMain) (((SndAircraft) (aircrafts[k])).FM)).AP.way.takeoffAirport = this;
	                    ((SndAircraft) (aircrafts[k])).FM.brakeShoe = true;
	                    ((SndAircraft) (aircrafts[k])).FM.turnOffCollisions = true;
	                    ((SndAircraft) (aircrafts[k])).FM.brakeShoeLoc.set(((Actor) (aircrafts[k])).pos.getAbs());
	                    ((SndAircraft) (aircrafts[k])).FM.brakeShoeLoc.sub(((Actor) (ship)).pos.getAbs());
	                    ((SndAircraft) (aircrafts[k])).FM.brakeShoeLastCarrier = ship;
	                    ((FlightModelMain) (((SndAircraft) (aircrafts[k])).FM)).Gears.bFlatTopGearCheck = true;
	                    aircrafts[k].makeMirrorCarrierRelPos();
						
	                    if(j > 0)
	                    {
	                        ((FlightModelMain) (((SndAircraft) (aircrafts[k])).FM)).CT.wingControl = 1.0F;
	                        ((FlightModelMain) (((SndAircraft) (aircrafts[k])).FM)).CT.forceWing(1.0F);
	                    }
	                }
					// ----------------------------------------
					else
						setTakeoffOld(point3d, aircrafts[k]);
				}
			}
			if (Actor.isValid(aircrafts[0]) && aircrafts[0].FM instanceof Maneuver)
			{
				Maneuver maneuver = (Maneuver)aircrafts[0].FM;
				if (maneuver.Group != null && maneuver.Group.w != null)
					maneuver.Group.w.takeoffAirport = this;
			}
		}
	}
	
	public void getTakeoff(Aircraft aircraft, Loc loc)
	{
		tmpLoc.sub(loc, ship.initLoc);
		tmpLoc.set(tmpLoc.getPoint().x, tmpLoc.getPoint().y, runway[0].getZ() + (double)aircraft.FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
		tmpLoc.add(ship.pos.getAbs());
		loc.set(tmpLoc);
		loc.getPoint();
		Orient orient = loc.getOrient();
		orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
		if (aircraft.FM instanceof Maneuver)
			((Maneuver)aircraft.FM).direction = loc.getAzimut();
		aircraft.FM.AP.way.takeoffAirport = this;
		aircraft.FM.brakeShoe = true;
		aircraft.FM.turnOffCollisions = true;
		aircraft.FM.brakeShoeLoc.set(loc);
		aircraft.FM.brakeShoeLoc.sub(ship.pos.getAbs());
		aircraft.FM.brakeShoeLastCarrier = ship;
	}
	
	public float height()
	{
		return (float)(ship.pos.getAbs().getZ() + runway[0].getZ());
	}
	
	public static boolean isPlaneContainsArrestor(Class var_class)
	{
		clsBigArrestorPlane();
		return _clsMapArrestorPlane.containsKey(var_class);
	}
	
	private static Class clsBigArrestorPlane()
	{
		if (_clsBigArrestorPlane != null)
			return _clsBigArrestorPlane;
		double d = 0.0;
		SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini", 0);
		int i = sectfile.sections();
		for (int i_16_ = 0; i_16_ < i; i_16_++)
		{
			int i_17_ = sectfile.vars(i_16_);
			for (int i_18_ = 0; i_18_ < i_17_; i_18_++)
			{
				sectfile.value(i_16_, i_18_);
				StringTokenizer stringtokenizer = new StringTokenizer(sectfile.value(i_16_, i_18_));
				if (stringtokenizer.hasMoreTokens())
				{
					String string_19_ = ("com.maddox.il2.objects." + stringtokenizer.nextToken());
					Class var_class = null;
					String string_20_ = null;
					try
					{
						var_class = Class.forName(string_19_);
						string_20_ = Property.stringValue(var_class, "FlightModel", null);
					}
					catch (Exception exception)
					{
						System.out.println(exception.getMessage());
						exception.printStackTrace();
					}
					try
					{
						if (string_20_ != null)
						{
							SectFile sectfile_21_ = FlightModelMain.sectFile(string_20_);
							if (sectfile_21_.get("Controls", "CArrestorHook", 0) == 1)
							{
								_clsMapArrestorPlane.put(var_class, null);
								String string_22_ = Aircraft.getPropertyMesh(var_class, null);
								SectFile sectfile_23_ = new SectFile(string_22_, 0);
								String string_24_ = sectfile_23_.get("_ROOT_", "CollisionObject", (String)null);
								if (string_24_ != null)
								{
									NumberTokenizer numbertokenizer = new NumberTokenizer(string_24_);
									if (numbertokenizer.hasMoreTokens())
									{
										numbertokenizer.next();
										if (numbertokenizer.hasMoreTokens())
										{
											double d_25_ = numbertokenizer.next(-1.0);
											if (d_25_ > 0.0 && d < d_25_)
											{
												d = d_25_;
												_clsBigArrestorPlane = var_class;
											}
										}
									}
								}
							}
						}
					}
					catch (Exception exception)
					{
						System.out.println(exception.getMessage());
						exception.printStackTrace();
					}
				}
			}
		}
		return _clsBigArrestorPlane;
	}
	
	private static CellAirPlane cellBigArrestorPlane()
	{
		if (_cellBigArrestorPlane != null)
			return _cellBigArrestorPlane;
		_cellBigArrestorPlane = Aircraft.getCellAirPlane(clsBigArrestorPlane());
		return _cellBigArrestorPlane;
	}
	
	private Point_Stay[][] getStayPlaces()
	{
		Point_Stay[][] point_stays = null;
		Class var_class = ship.getClass();
		point_stays = (Point_Stay[][])Property.value(var_class, "StayPlaces", null);
		if (point_stays == null)
		{
			CellAirPlane cellairplane = cellBigArrestorPlane();
			CellAirField cellairfield = ship.getCellTO();
			cellairfield = (CellAirField)cellairfield.getClone();
			ArrayList arraylist = new ArrayList();
			for (;;)
			{
				cellairplane = (CellAirPlane)cellairplane.getClone();
				if (!cellairfield.findPlaceForAirPlane(cellairplane))
					break;
				cellairfield.placeAirPlane(cellairplane, cellairfield.resX(), cellairfield.resY());
				double d = (-cellairfield.leftUpperCorner().x - ((double)cellairfield.resX() * cellairfield.getCellSize()) - cellairplane.ofsX);
				double d_26_ = (cellairfield.leftUpperCorner().y - ((double)cellairfield.resY() * cellairfield.getCellSize()) - cellairplane.ofsY);
				arraylist.add(new Point2d(d_26_, d));
			}
			int i = arraylist.size();
			if (i > 0)
			{
				point_stays = new Point_Stay[i][1];
				for (int i_27_ = 0; i_27_ < i; i_27_++)
				{
					Point2d point2d = (Point2d)arraylist.get(i_27_);
					point_stays[i_27_][0] = new Point_Stay((float)point2d.x, (float)point2d.y);
				}
				Property.set(var_class, "StayPlaces", point_stays);
			}
		}
		if (point_stays == null)
			return null;
		Point_Stay[][] point_stays_28_ = new Point_Stay[point_stays.length][1];
		for (int i = 0; i < point_stays.length; i++)
		{
			Point_Stay point_stay = point_stays[i][0];
			double d = (double)point_stay.x;
			double d_29_ = (double)point_stay.y;
			double d_30_ = runway[0].getZ();
			tmpLoc.set(d, d_29_, d_30_, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
			tmpLoc.add(ship.pos.getAbs());
			Point3d point3d = tmpLoc.getPoint();
			point_stays_28_[i][0] = new Point_Stay((float)point3d.x, (float)point3d.y);
		}
		return point_stays_28_;
	}
	
	public void setCellUsed(Aircraft aircraft)
	{
		if (aircraft.FM.CT.bHasWingControl && !aircraft.isNetPlayer())
			aircraft.FM.CT.wingControl = 0.0F;
	}
	
	public Loc requestCell(Aircraft aircraft)
	{
		if (!ship().isAlive())
			return invalidLoc;
		CellAirPlane cellairplane = aircraft.getCellAirPlane();
		Point3d point3d = reservePlaceForPlane(cellairplane, null);
		if (point3d != null)
		{
			double d = point3d.x;
			double d_31_ = point3d.y;
			double d_32_ = point3d.z;
			Loc loc = new Loc();
			loc.set(d, d_31_, d_32_ + (double)aircraft.FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
			return loc;
		}
		return invalidLoc;
	}
	
	public void setGuiCallback(GUINetClientDBrief guinetclientdbrief)
	{
		ui = guinetclientdbrief;
	}
	
	public void setClientLoc(Loc loc)
	{
		clientLoc = loc;
		boolean bool = isLocValid(loc);
		if (ui != null)
			ui.flyFromCarrier(bool);
	}
	
	private boolean isLocValid(Loc loc)
	{
		if (loc == null)
			return false;
		if ((int)loc.getX() == 0 && (int)loc.getY() == 0 && (int)loc.getZ() == 0 && (int)loc.getTangage() == 0 && (int)loc.getKren() == 0)
			return false;
		return true;
	}
	
	// TODO: CTO Mod
	// ----------------------------------------
	private int setAircraftDimensions(com.maddox.il2.objects.air.Aircraft aircraft)
	{
		byte byte0 = -1;
		if ((aircraft instanceof com.maddox.il2.objects.air.F4F) && !(aircraft instanceof com.maddox.il2.objects.air.F4F3))
			byte0 = 5;
		else if ((aircraft instanceof com.maddox.il2.objects.air.F6F) || (aircraft instanceof com.maddox.il2.objects.air.F4U))
			byte0 = 6;
		else if (aircraft instanceof com.maddox.il2.objects.air.TBF)
			byte0 = 7;
		else if ((aircraft instanceof com.maddox.il2.objects.air.B5N) || (aircraft instanceof com.maddox.il2.objects.air.B6N))
			byte0 = 8;
		else if (aircraft instanceof com.maddox.il2.objects.air.D3A)
			byte0 = 12;
		else if (aircraft instanceof com.maddox.il2.objects.air.A6M2_21)
			byte0 = 11;
		else if ((aircraft instanceof com.maddox.il2.objects.air.SEAFIRE3) || (aircraft instanceof com.maddox.il2.objects.air.SEAFIRE3F))
			byte0 = 5;
		if (byte0 == -1)
			try
			{
				//TODO: Disabled for 410 compatibility
				/*
				if (aircraft instanceof com.maddox.il2.objects.air.F9F)
					byte0 = 6;
				*/
			}
			catch (java.lang.Throwable throwable)
			{}
		if (byte0 == -1)
			try
			{
				//TODO: Disabled for 410 compatibility
				/*
				if (aircraft instanceof com.maddox.il2.objects.air.SeaFury)
					byte0 = 6;
				*/
			}
			catch (java.lang.Throwable throwable1)
			{}
		if (byte0 == -1)
			try
			{
				if (aircraft instanceof com.maddox.il2.objects.air.Swordfish)
					byte0 = 6;
			}
			catch (java.lang.Throwable throwable2)
			{}
		return byte0;
	}
	// ----------------------------------------
}