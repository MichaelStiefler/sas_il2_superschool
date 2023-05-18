/*Modified AirportStatic class for the SAS Engine Mod*/
//Unsure of what is currently modified in here
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
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.rts.Time;

public abstract class AirportStatic extends Airport
{
    private ArrayList runway = new ArrayList();
    public static final int PT_RUNWAY = 1;
    public static final int PT_TAXI = 2;
    public static final int PT_STAY = 4;
    private static Point3d p3d = new Point3d();
    private static float[] x = { -500.0F, 0.0F, 220.0F, 2000.0F, 4000.0F,
				 5000.0F, 4000.0F, 0.0F, 0.0F };
    private static float[] y = { 0.0F, 0.0F, 0.0F, 0.0F, -500.0F, -2000.0F,
				 -4000.0F, -4000.0F, -4000.0F };
    private static float[] z = { 0.0F, 6.0F, 20.0F, 160.0F, 500.0F, 600.0F,
				 700.0F, 700.0F, 700.0F };
    private static float[] v = { 0.0F, 180.0F, 220.0F, 240.0F, 270.0F, 280.0F,
				 300.0F, 300.0F, 300.0F };
    private static Point3d pWay = new Point3d();
    private static Point3d pd = new Point3d();
    private static Point3f pf = new Point3f();
    private static Vector3d v1 = new Vector3d();
    private static Vector3d zeroSpeed = new Vector3d();
    private static Loc tmpLoc = new Loc();
    private static Point3d pcur = new Point3d();
    private static Point3d np = new Point3d();
    private static Vector3d p1 = new Vector3d();
    private static Vector3d p2 = new Vector3d();
    
    private static class Runway
    {
	public Loc loc = new Loc();
	public float[] planeShift = new float[32];
	public Aircraft[] planes = new Aircraft[32];
	public int curPlaneShift = 0;
	public int oldTickCounter = 0;
	
	public Runway(Loc loc) {
	    this.loc.set(loc.getX(), loc.getY(),
			 World.land().HQ(loc.getX(), loc.getY()),
			 loc.getAzimut(), 0.0F, 0.0F);
	}
    }
    
    public static void make(ArrayList arraylist, Point2f[][] point2fs,
			    Point2f[][] point2fs_0_, Point2f[][] point2fs_1_) {
	if (arraylist != null) {
	    ArrayList arraylist_2_ = new ArrayList();
	    double d = 4000000.0;
	    if (arraylist.size() == 4)
		d = 2890000.0;
	    while (arraylist.size() > 0) {
		Loc loc = (Loc) arraylist.remove(0);
		boolean bool = false;
		AirportStatic airportstatic = null;
		for (int i = 0; i < arraylist_2_.size(); i++) {
		    airportstatic = (AirportStatic) arraylist_2_.get(i);
		    if (airportstatic.oppositeRunway(loc) != null) {
			bool = true;
			break;
		    }
		}
		if (bool) {
		    airportstatic.runway.add(new Runway(loc));
		    int i = airportstatic.runway.size();
		    p3d.set(0.0, 0.0, 0.0);
		    for (int i_3_ = 0; i_3_ < i; i_3_++) {
			loc = ((Runway) airportstatic.runway.get(i_3_)).loc;
			p3d.x += loc.getPoint().x;
			p3d.y += loc.getPoint().y;
			p3d.z += loc.getPoint().z;
		    }
		    p3d.x /= (double) i;
		    p3d.y /= (double) i;
		    p3d.z /= (double) i;
		    airportstatic.pos.setAbs(p3d);
		} else {
		    if (Engine.cur.land.isWater(loc.getPoint().x,
						loc.getPoint().y))
			airportstatic = new AirportMaritime();
		    else
			airportstatic = new AirportGround();
		    airportstatic.pos = new ActorPosStatic(airportstatic, loc);
		    airportstatic.runway.add(new Runway(loc));
		    arraylist_2_.add(airportstatic);
		}
	    }
	}
    }
    
    public boolean landWay(FlightModel flightmodel) {
	flightmodel.AP.way.curr().getP(pWay);
	Runway runway = nearestRunway(pWay);
	if (runway == null)
	    return false;
	Way way = new Way();
	float f = (float) Engine.land().HQ_Air(runway.loc.getX(),
					       runway.loc.getY());
	float f_4_ = flightmodel.M.massEmpty / 3000.0F;
	if (f_4_ > 1.0F)
	    f_4_ = 1.0F;
	if (flightmodel.EI.engines[0].getType() > 1)
	    f_4_ = 1.0F;
	if (flightmodel.EI.engines[0].getType() == 3)
	    f_4_ = 1.5F;
	float f_5_ = f_4_;
	if (f_5_ > 1.0F)
	    f_5_ = 1.0F;
	//TODO: Edit for fast flying aircraft
	 if(((Interpolate) (flightmodel)).actor instanceof TypeFastJet)
         f_4_ = 3F;
	for (int i = x.length - 1; i >= 0; i--) {
	    WayPoint waypoint = new WayPoint();
	    pd.set((double) (x[i] * f_4_), (double) (y[i] * f_4_),
		   (double) (z[i] * f_5_));
	    waypoint.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.7F));
	    waypoint.Action = 2;
	    runway.loc.transform(pd);
	    float f_6_ = (float) Engine.land().HQ_Air(pd.x, pd.y);
	    pd.z -= (double) (f_6_ - f);
	    pf.set(pd);
	    waypoint.set(pf);
	    way.add(waypoint);
	}
	way.setLanding(true);
	flightmodel.AP.way = way;
	return true;
    }
    
    public void setTakeoff(Point3d point3d, Aircraft[] aircrafts) {
	Runway runway = nearestRunway(point3d);
	if (runway != null) {
	    Runway runway_7_ = oppositeRunway(runway.loc);
	    double d = 1000.0;
	    if (runway_7_ != null)
		d = runway.loc.getPoint().distance(runway_7_.loc.getPoint());
	    if (Time.tickCounter() != runway.oldTickCounter) {
		runway.oldTickCounter = Time.tickCounter();
		runway.curPlaneShift = 0;
	    }
	    for (int i = 0; i < aircrafts.length; i++) {
		if (Actor.isValid(aircrafts[i])) {
		    float f = aircrafts[i].collisionR() * 2.0F + 20.0F;
		    for (int i_8_ = runway.curPlaneShift; i_8_ > 0; i_8_--) {
			runway.planeShift[i_8_]
			    = runway.planeShift[i_8_ - 1] + f;
			runway.planes[i_8_] = runway.planes[i_8_ - 1];
		    }
		    runway.planeShift[0] = 0.0F;
		    runway.planes[0] = aircrafts[i];
		    runway.curPlaneShift++;
		    if (runway.curPlaneShift > 31)
			throw new RuntimeException
				  ("Too many planes on airdrome");
		    for (int i_9_ = 0; i_9_ < runway.curPlaneShift; i_9_++) {
			if (Actor.isValid(runway.planes[i_9_])) {
			    tmpLoc.set((double) runway.planeShift[i_9_] - d,
				       0.0, 0.0, 0.0F, 0.0F, 0.0F);
			    tmpLoc.add(runway.loc);
			    Point3d point3d_10_ = tmpLoc.getPoint();
			    Orient orient = tmpLoc.getOrient();
			    point3d_10_.z
				= (World.land().HQ(point3d_10_.x,
						   point3d_10_.y)
				   + (double) runway.planes[i_9_].FM.Gears.H);
			    Engine.land().N(point3d_10_.x, point3d_10_.y, v1);
			    orient.orient(v1);
			    orient.increment(0.0F, (runway.planes[i_9_].FM
						    .Gears.Pitch), 0.0F);
			    runway.planes[i_9_].setOnGround(point3d_10_,
							    orient, zeroSpeed);
			    if (runway.planes[i_9_].FM instanceof Maneuver) {
				((Maneuver) runway.planes[i_9_].FM).direction
				    = runway.planes[i_9_].pos.getAbsOrient
					  ().getAzimut();
				((Maneuver) runway.planes[i_9_].FM).rwLoc
				    = runway.loc;
			    }
			    runway.planes[i_9_].FM.AP.way.takeoffAirport
				= this;
			}
		    }
		}
	    }
	    if (Actor.isValid(aircrafts[0])
		&& aircrafts[0].FM instanceof Maneuver) {
		Maneuver maneuver = (Maneuver) aircrafts[0].FM;
		if (maneuver.Group != null && maneuver.Group.w != null)
		    maneuver.Group.w.takeoffAirport = this;
	    }
	}
    }
    
    public double ShiftFromLine(FlightModel flightmodel) {
	tmpLoc.set(flightmodel.Loc);
	if (flightmodel instanceof Maneuver) {
	    Maneuver maneuver = (Maneuver) flightmodel;
	    if (maneuver.rwLoc != null) {
		tmpLoc.sub(maneuver.rwLoc);
		return tmpLoc.getY();
	    }
	}
	return 0.0;
    }
    
    public boolean nearestRunway(Point3d point3d, Loc loc) {
	Runway runway = nearestRunway(point3d);
	if (runway != null) {
	    loc.set(runway.loc);
	    return true;
	}
	return false;
    }
    
    private Runway nearestRunway(Point3d point3d) {
	Runway runway = null;
	double d = 0.0;
	np.set(point3d);
	int i = this.runway.size();
	for (int i_11_ = 0; i_11_ < i; i_11_++) {
	    Runway runway_12_ = (Runway) this.runway.get(i_11_);
	    np.z = runway_12_.loc.getPoint().z;
	    double d_13_ = runway_12_.loc.getPoint().distanceSquared(np);
	    if (runway == null || d_13_ < d) {
		runway = runway_12_;
		d = d_13_;
	    }
	}
	if (d > 2.25E8)
	    runway = null;
	return runway;
    }
    
    private Runway oppositeRunway(Loc loc) {
	int i = this.runway.size();
	for (int i_14_ = 0; i_14_ < i; i_14_++) {
	    Runway runway = (Runway) this.runway.get(i_14_);
	    pcur.set(runway.loc.getPoint());
	    loc.transformInv(pcur);
	    if (Math.abs(pcur.y) < 15.0 && pcur.x < -800.0
		&& pcur.x > -2500.0) {
		p1.set(1.0, 0.0, 0.0);
		p2.set(1.0, 0.0, 0.0);
		runway.loc.getOrient().transform(p1);
		loc.getOrient().transform(p2);
		if (p1.dot(p2) < -0.9)
		    return runway;
	    }
	}
	return null;
    }
}
