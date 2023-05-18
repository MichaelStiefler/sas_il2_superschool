/*Modified AirportStatic class for the SAS Engine Mod*/
package com.maddox.il2.ai;

import com.maddox.JGP.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.rts.Time;
import java.util.ArrayList;

public abstract class AirportStatic extends Airport
{
    private static class Runway
    {

        public Loc loc;
        public float planeShift[];
        public Aircraft planes[];
        public int curPlaneShift;
        public int oldTickCounter;

        public Runway(Loc loc1)
        {
            loc = new Loc();
            planeShift = new float[32];
            planes = new Aircraft[32];
            curPlaneShift = 0;
            oldTickCounter = 0;
            loc.set(loc1.getX(), loc1.getY(), World.land().HQ(loc1.getX(), loc1.getY()), loc1.getAzimut(), 0.0F, 0.0F);
        }
    }


    public AirportStatic()
    {
        runway = new ArrayList();
    }

    public static void make(ArrayList arraylist, Point2f apoint2f[][], Point2f apoint2f1[][], Point2f apoint2f2[][])
    {
        if(arraylist == null)
            return;
        ArrayList arraylist1 = new ArrayList();
        while(arraylist.size() > 0) 
        {
            Loc loc = (Loc)arraylist.remove(0);
            boolean flag = false;
            AirportStatic airportstatic = null;
            int i = 0;
            do
            {
                if(i >= arraylist1.size())
                    break;
                airportstatic = (AirportStatic)arraylist1.get(i);
                if(airportstatic.oppositeRunway(loc) != null)
                {
                    flag = true;
                    break;
                }
                i++;
            } while(true);
            if(flag)
            {
                airportstatic.runway.add(new Runway(loc));
                int j = airportstatic.runway.size();
                p3d.set(0.0D, 0.0D, 0.0D);
                for(int k = 0; k < j; k++)
                {
                    loc = ((Runway)airportstatic.runway.get(k)).loc;
                    p3d.x += loc.getPoint().x;
                    p3d.y += loc.getPoint().y;
                    p3d.z += loc.getPoint().z;
                }

                p3d.x /= j;
                p3d.y /= j;
                p3d.z /= j;
                airportstatic.pos.setAbs(p3d);
            } else
            {
    		    if (Engine.cur.land.isWater(loc.getPoint().x,
    						loc.getPoint().y))
    			airportstatic = new AirportMaritime();
    		    else
    			airportstatic = new AirportGround();
    		    airportstatic.pos = new ActorPosStatic(airportstatic, loc);
    		    airportstatic.runway.add(new Runway(loc));
    		    arraylist1.add(airportstatic);
            }
        }
    }

    public boolean landWay(FlightModel flightmodel)
    {
        flightmodel.AP.way.curr().getP(pWay);
        Runway runway1 = nearestRunway(pWay);
        if(runway1 == null)
            return false;
        Way way = new Way();
        float f = (float)Engine.land().HQ_Air(runway1.loc.getX(), runway1.loc.getY());
        float f1 = flightmodel.M.massEmpty / 3000F;
        if(f1 > 1.0F)
            f1 = 1.0F;
        if(flightmodel.EI.engines[0].getType() > 1)
            f1 = 1.0F;
        if(flightmodel.EI.engines[0].getType() == 3)
            f1 = 1.5F;
    	//TODO: Edit for fast flying aircraft
        if(((Interpolate) (flightmodel)).actor instanceof TypeFastJet)
        	f1 = 3F;
        float f2 = f1;
        if(f2 > 1.0F)
            f2 = 1.0F;          
        for(int i = x.length - 1; i >= 0; i--)
        {
            WayPoint waypoint = new WayPoint();
            int j = (flightmodel.AP.way.curr().waypointType - 101) + 1;
            if(j < 0)
                j = 0;
            switch(j)
            {
            case 0: // '\0'
            default:
                pd.set(x[i] * f1, y[i] * f1, z[i] * f2);
                break;

            case 1: // '\001'
                pd.set(x[i] * f1, ry[i] * f1, z[i] * f2);
                break;

            case 2: // '\002'
                pd.set(x[i] * f1, lsy[i] * f1, z[i] * f2);
                break;

            case 3: // '\003'
                pd.set(x[i] * f1, rsy[i] * f1, z[i] * f2);
                break;

            case 4: // '\004'
                pd.set(six[i] * f1, siy[i] * f1, siz[i] * f2);
                break;
            }
            waypoint.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.7F));
            waypoint.Action = 2;
            runway1.loc.transform(pd);
            float f3 = (float)Engine.land().HQ_Air(pd.x, pd.y);
            pd.z -= f3 - f;
            pf.set(pd);
            waypoint.set(pf);
            way.add(waypoint);
        }

        way.setLanding(true);
        if(flightmodel.AP.way.curr().waypointType == 104)
            way.setLanding2(true);
        flightmodel.AP.way = way;
        return true;
    }

    private boolean setStationaryPlaneTakeoff(Aircraft aircraft, Runway runway1)
    {
        Actor actor = Actor.getByName(aircraft.spawnActorName);
        if(!actor.isAlive())
        {
            aircraft.destroy();
            return false;
        }
        Point3d point3d = aircraft.spawnLocSingleCoop.getPoint();
        Orient orient = aircraft.spawnLocSingleCoop.getOrient();
        point3d.z = World.land().HQ(point3d.x, point3d.y) + (double)aircraft.FM.Gears.H;
        Engine.land().N(point3d.x, point3d.y, v1);
        orient.orient(v1);
        orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
        aircraft.setOnGround(point3d, orient, zeroSpeed);
        if(aircraft.FM instanceof Maneuver)
        {
            ((Maneuver)aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
            ((Maneuver)aircraft.FM).rwLoc = runway1.loc;
        }
        aircraft.FM.AP.way.takeoffAirport = this;
        if(Actor.isValid(aircraft) && (aircraft.FM instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)aircraft.FM;
            if(maneuver.Group != null && maneuver.Group.w != null)
                maneuver.Group.w.takeoffAirport = this;
        }
        aircraft.spawnLocSingleCoop.set(aircraft.pos.getAbs());
        if(!Mission.isCoop() || aircraft.getWing().bOnlyAI)
            actor.destroy();
        aircraft.stationarySpawnLocSet = true;
        return true;
    }

    public void setTakeoff(Point3d point3d, Aircraft aaircraft[])
    {
        Runway runway1 = nearestRunway(point3d);
        if(runway1 == null)
            return;
        Runway runway2 = oppositeRunway(runway1.loc);
        double d = 1000D;
        if(runway2 != null)
            d = runway1.loc.getPoint().distance(runway2.loc.getPoint());
        if(Time.tickCounter() != runway1.oldTickCounter)
        {
            runway1.oldTickCounter = Time.tickCounter();
            runway1.curPlaneShift = 0;
        }
        for(int i = 0; i < aaircraft.length; i++)
        {
            if(!Actor.isValid(aaircraft[i]))
                continue;
            if(aaircraft[i].spawnLocSingleCoop != null)
            {
                boolean flag = setStationaryPlaneTakeoff(aaircraft[i], runway1);
                if(!flag || !Mission.isCoop() || aaircraft[i].getWing().bOnlyAI)
                    continue;
            }
            float f = aaircraft[i].collisionR() * 2.0F + 20F;
            for(int j = runway1.curPlaneShift; j > 0; j--)
            {
                runway1.planeShift[j] = runway1.planeShift[j - 1] + f;
                runway1.planes[j] = runway1.planes[j - 1];
            }

            runway1.planeShift[0] = 0.0F;
            runway1.planes[0] = aaircraft[i];
            runway1.curPlaneShift++;
            if(runway1.curPlaneShift > 31)
                throw new RuntimeException("Too many planes on airdrome");
            for(int k = 0; k < runway1.curPlaneShift; k++)
            {
                if(!Actor.isValid(runway1.planes[k]))
                    continue;
                tmpLoc.set((double)runway1.planeShift[k] - d, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                tmpLoc.add(runway1.loc);
                Point3d point3d1 = tmpLoc.getPoint();
                Orient orient = tmpLoc.getOrient();
                point3d1.z = World.land().HQ(point3d1.x, point3d1.y) + (double)runway1.planes[k].FM.Gears.H;
                Engine.land().N(point3d1.x, point3d1.y, v1);
                orient.orient(v1);
                orient.increment(0.0F, runway1.planes[k].FM.Gears.Pitch, 0.0F);
                runway1.planes[k].setOnGround(point3d1, orient, zeroSpeed);
                if(runway1.planes[k].FM instanceof Maneuver)
                {
                    ((Maneuver)runway1.planes[k].FM).direction = runway1.planes[k].pos.getAbsOrient().getAzimut();
                    ((Maneuver)runway1.planes[k].FM).rwLoc = runway1.loc;
                }
                runway1.planes[k].FM.AP.way.takeoffAirport = this;
            }

        }

        if(Actor.isValid(aaircraft[0]) && (aaircraft[0].FM instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)aaircraft[0].FM;
            if(maneuver.Group != null && maneuver.Group.w != null)
                maneuver.Group.w.takeoffAirport = this;
        }
    }

    public void setAsHomeAirport(Aircraft aaircraft[])
    {
        for(int i = 0; i < aaircraft.length; i++)
            if(Actor.isValid(aaircraft[i]))
                aaircraft[i].FM.AP.way.takeoffAirport = this;

    }

    public double shiftFromLine(FlightModel flightmodel)
    {
        tmpLoc.set(flightmodel.Loc);
        if(flightmodel instanceof Maneuver)
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            if(maneuver.rwLoc != null)
            {
                tmpLoc.sub(maneuver.rwLoc);
                return tmpLoc.getY();
            }
        }
        return 0.0D;
    }

    public boolean nearestRunway(Point3d point3d, Loc loc)
    {
        Runway runway1 = nearestRunway(point3d);
        if(runway1 != null)
        {
            loc.set(runway1.loc);
            return true;
        } else
        {
            return false;
        }
    }

    private Runway nearestRunway(Point3d point3d)
    {
        Runway runway1 = null;
        double d = 0.0D;
        np.set(point3d);
        int i = runway.size();
        for(int j = 0; j < i; j++)
        {
            Runway runway2 = (Runway)runway.get(j);
            np.z = runway2.loc.getPoint().z;
            double d1 = runway2.loc.getPoint().distanceSquared(np);
            if(runway1 == null || d1 < d)
            {
                runway1 = runway2;
                d = d1;
            }
        }

        if(d > 225000000D)
            runway1 = null;
        return runway1;
    }

    private Runway oppositeRunway(Loc loc)
    {
        int i = runway.size();
        for(int j = 0; j < i; j++)
        {
            Runway runway1 = (Runway)runway.get(j);
            pcur.set(runway1.loc.getPoint());
            loc.transformInv(pcur);
            if(Math.abs(pcur.y) >= 15D || pcur.x >= -800D || pcur.x <= -2500D)
                continue;
            p1.set(1.0D, 0.0D, 0.0D);
            p2.set(1.0D, 0.0D, 0.0D);
            runway1.loc.getOrient().transform(p1);
            loc.getOrient().transform(p2);
            if(p1.dot(p2) < -0.90000000000000002D)
                return runway1;
        }

        return null;
    }

    private ArrayList runway;
    public static final int PT_RUNWAY = 1;
    public static final int PT_TAXI = 2;
    public static final int PT_STAY = 4;
    private static Point3d p3d = new Point3d();
    private static float x[] = {
        -500F, 0.0F, 220F, 2000F, 4000F, 5000F, 4000F, 0.0F, 0.0F
    };
    private static float y[] = {
        0.0F, 0.0F, 0.0F, 0.0F, -500F, -2000F, -4000F, -4000F, -4000F
    };
    private static float z[] = {
        0.0F, 6F, 20F, 160F, 500F, 600F, 700F, 700F, 700F
    };
    private static float v[] = {
        0.0F, 180F, 220F, 240F, 270F, 280F, 300F, 300F, 300F
    };
    private static float ry[] = {
        0.0F, 0.0F, 0.0F, 0.0F, 500F, 2000F, 4000F, 4000F, 4000F
    };
    private static float lsy[] = {
        0.0F, 0.0F, 0.0F, 0.0F, -250F, -1000F, -2000F, -2000F, -2000F
    };
    private static float rsy[] = {
        0.0F, 0.0F, 0.0F, 0.0F, 250F, 1000F, 2000F, 2000F, 2000F
    };
    private static float six[] = {
        -500F, 0.0F, 220F, 2000F, 3000F, 4000F, 5000F, 5500F, 6000F
    };
    private static float siy[] = {
        0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F
    };
    private static float siz[] = {
        0.0F, 6F, 20F, 160F, 500F, 600F, 700F, 700F, 700F
    };
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

}