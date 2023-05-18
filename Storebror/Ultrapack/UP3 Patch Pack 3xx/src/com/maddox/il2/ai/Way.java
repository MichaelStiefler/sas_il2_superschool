/*4.10.1 class*/
package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class Way
{
	private ArrayList WList;
	private int Cur;
	private boolean landing;
	private boolean landingOnShip;
	public Airport landingAirport;
	public Airport takeoffAirport;
	private double prevdist2;
	private double prevdistToNextWP2;
	private double curDist;
	private static Vector3d V = new Vector3d();
	
	//TODO: Modified by |ZUTI|: Changed from private to public
	public static Point3d P = new Point3d();
	
	private static Point3d tmpP = new Point3d();
	private static WayPoint defaultWP = new WayPoint();

	public int Cur()
	{
		return Cur;
	}

	public Way()
	{
		WList = new ArrayList();
		prevdist2 = 1.0E8;
		prevdistToNextWP2 = 3.4028234663852886E38;
		WList.clear();
		Cur = 0;
		landing = false;
		landingOnShip = false;
		landingAirport = null;
		takeoffAirport = null;
	}

	public Way(Way way_0_)
	{
		WList = new ArrayList();
		prevdist2 = 1.0E8;
		prevdistToNextWP2 = 3.4028234663852886E38;
		set(way_0_);
	}

	public void set(Way way_1_)
	{
		WList.clear();
		Cur = 0;
		for (int i = 0; i < way_1_.WList.size(); i++)
		{
			WayPoint waypoint = new WayPoint();
			waypoint.set(way_1_.get(i));
			WList.add(waypoint);
			if (waypoint.Action == 2 && waypoint.sTarget != null) landingOnShip = true;
		}
		landing = way_1_.landing;
		landingAirport = way_1_.landingAirport;
		if (takeoffAirport == null) takeoffAirport = way_1_.takeoffAirport;
	}

	public WayPoint first()
	{
		Cur = 0;
		return curr();
	}

	public WayPoint last()
	{
		Cur = Math.max(0, WList.size() - 1);
		return curr();
	}

	public WayPoint next()
	{
		int i = WList.size();
		Cur++;
		if (Cur >= i)
		{
			Cur = Math.max(0, i - 1);
			WayPoint waypoint = curr();
			return waypoint;
		}
		return curr();
	}

	public WayPoint look_at_point(int i)
	{
		int i_2_ = WList.size();
		if (i_2_ == 0) return defaultWP;
		if (i < 0) i = 0;
		if (i > i_2_ - 1) i = i_2_ - 1;
		return (WayPoint) WList.get(i);
	}

	public void setCur(int i)
	{
		if (i < WList.size() && i >= 0) Cur = i;
	}

	public WayPoint prev()
	{
		Cur--;
		if (Cur < 0) Cur = 0;
		return curr();
	}

	public WayPoint curr()
	{
		if (WList.size() == 0) return defaultWP;
		return (WayPoint) WList.get(Cur);
	}

	public WayPoint auto(Point3d point3d)
	{
		if (Cur == 0 || isReached(point3d)) return next();
		return curr();
	}

	public double getCurDist()
	{
		return Math.sqrt(curDist);
	}

	public boolean isReached(Point3d point3d)
	{
		curr().getP(P);
		V.sub(point3d, P);
		if (curr().timeout == -1 && !isLast())
		{
			((WayPoint) WList.get(Cur + 1)).getP(tmpP);
			V.sub(point3d, tmpP);
			curDist = V.x * V.x + V.y * V.y;
			if (curDist < 1.0E8 && curDist > prevdistToNextWP2)
			{
				curr().setTimeout(0);
				prevdistToNextWP2 = 3.4028234663852886E38;
				return true;
			}
			prevdistToNextWP2 = curDist;
		}
		else
		{
			curDist = V.x * V.x + V.y * V.y;
			if (curDist < 1000000.0 && curDist > prevdist2)
			{
				prevdist2 = 1.0E8;
				return true;
			}
			prevdist2 = curDist;
		}
		return false;
	}

	public boolean isLanding()
	{
		return landing;
	}

	public boolean isLandingOnShip()
	{
		return landingOnShip;
	}

	public boolean isLast()
	{
		return Cur == WList.size() - 1;
	}

	public void setLanding(boolean bool)
	{
		landing = bool;
	}

	public void add(WayPoint waypoint)
	{
		WList.add(waypoint);
		if (waypoint.Action == 2 && waypoint.sTarget != null) landingOnShip = true;
	}

	public WayPoint get(int i)
	{
		if (i < 0 || i >= WList.size()) return null;
		return (WayPoint) WList.get(i);
	}

	public void insert(int i, WayPoint waypoint)
	{
		if (i < 0)
			i = 0;
		else if (i > WList.size())
		{
			add(waypoint);
			return;
		}
		WList.add(i, waypoint);
	}

	public int size()
	{
		return WList.size();
	}

	public void load(SectFile sectfile, String string) throws Exception
	{
		int i = sectfile.sectionIndex(string);
		int j = sectfile.vars(i);
		for (int k = 0; k < j; k++)
		{
			String s1 = sectfile.var(i, k);
			WayPoint waypoint = new WayPoint();

			// TODO: +++ TD AI code backport from 4.13 +++
			
//			if (s1.equalsIgnoreCase("TAKEOFF"))
//				waypoint.Action = 1;
//			else if (s1.equalsIgnoreCase("LANDING"))
//				waypoint.Action = 2;
//			else if (s1.equalsIgnoreCase("GATTACK"))
//				waypoint.Action = 3;
//			else
//				waypoint.Action = 0;
			
			
            s1 = s1.toUpperCase();
            waypoint.resetWayPointExtra();
            if(s1.startsWith("NORMFLY"))
            {
                waypoint.Action = 0;
                if(s1.endsWith("_401"))
                    waypoint.waypointType = 401;
                else
                if(s1.endsWith("_402"))
                    waypoint.waypointType = 402;
                else
                if(s1.endsWith("_403"))
                    waypoint.waypointType = 403;
                else
                if(s1.endsWith("_404"))
                    waypoint.waypointType = 404;
                else
                if(s1.endsWith("_405"))
                    waypoint.waypointType = 405;
                else
                if(s1.endsWith("_407"))
                    waypoint.waypointType = 407;
            } else
            if(s1.startsWith("TAKEOFF"))
            {
                waypoint.Action = 1;
                if(s1.endsWith("_001"))
                    waypoint.waypointType = 1;
                else
                if(s1.endsWith("_002"))
                    waypoint.waypointType = 2;
                else
                if(s1.endsWith("_003"))
                    waypoint.waypointType = 3;
                else
                if(s1.endsWith("_004"))
                    waypoint.waypointType = 4;
                else
                if(s1.endsWith("_005"))
                    waypoint.waypointType = 5;
            } else
            if(s1.startsWith("LANDING"))
            {
                waypoint.Action = 2;
                if(s1.endsWith("_101"))
                    waypoint.waypointType = 101;
                else
                if(s1.endsWith("_102"))
                    waypoint.waypointType = 102;
                else
                if(s1.endsWith("_103"))
                    waypoint.waypointType = 103;
                else
                if(s1.endsWith("_104"))
                    waypoint.waypointType = 104;
            } else
            if(s1.startsWith("GATTACK"))
                waypoint.Action = 3;

            // TODO: --- TD AI code backport from 4.13 ---
			
			
			NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i, k));
			P.x = (double) numbertokenizer.next(0.0F, -1000000.0F, 1000000.0F);
			P.y = (double) numbertokenizer.next(0.0F, -1000000.0F, 1000000.0F);
			P.z = ((double) numbertokenizer.next(0.0F, -1000000.0F, 1000000.0F) + World.land().HQ(P.x, P.y));
			float f = numbertokenizer.next(0.0F, 0.0F, 2800.0F);
			waypoint.set(P);
			waypoint.set(f / 3.6F);
			String s2 = numbertokenizer.next((String) null);
			if (s2 != null)
			{
				if (s2.equals("&0"))
				{
					waypoint.bRadioSilence = false;
					s2 = null;
				}
				else if (s2.equals("&1"))
				{
					waypoint.bRadioSilence = true;
					s2 = null;
				}
				else
				{
					numbertokenizer.next(0);
					String s3 = numbertokenizer.next((String) null);
					if (s3 != null && s3.equals("&1")) waypoint.bRadioSilence = true;
				}
			}
            String s4 = numbertokenizer.next(null);
            if(s4 != null && s4.startsWith("F"))
            {
                String s5 = s4.substring(1);
                waypoint.formation = Integer.parseInt(s5);
            }
			if (s2 != null && s2.startsWith("Bridge")) s2 = " " + s2;
			waypoint.setTarget(s2);
			// TODO: +++ TD AI code backport from 4.13 +++
            if(waypoint.waypointType > 0 && waypoint.Action != 2)
            {
                String s6 = sectfile.value(i, k + 1);
                NumberTokenizer numbertokenizer1 = new NumberTokenizer(s6);
                switch(waypoint.waypointType)
                {
                case 401: 
                case 402: 
                case 403: 
                case 404: 
                case 405: 
                    waypoint.cycles = numbertokenizer1.next(0);
                    waypoint.delayTimer = numbertokenizer1.next(0);
                    waypoint.orient = numbertokenizer1.next(0);
                    waypoint.baseSize = numbertokenizer1.next(0);
                    waypoint.altDifference = numbertokenizer1.next(0);
                    break;

                case 407: 
                    waypoint.cycles = numbertokenizer1.next(0);
                    waypoint.delayTimer = numbertokenizer1.next(0);
                    waypoint.orient = numbertokenizer1.next(0);
                    waypoint.baseSize = numbertokenizer1.next(0);
                    break;

                case 1: 
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                    waypoint.targetTrigger = numbertokenizer1.next(0);
                    waypoint.delayTimer = numbertokenizer1.next(0);
                    waypoint.takeoffSpacing = numbertokenizer1.next(0);
                    waypoint.ignoreAlt = numbertokenizer1.next(0);
                    break;
                }
                k++;
            }
            // TODO: --- TD AI code backport from 4.13 ---
			add(waypoint);
		}
	}
	
	// TODO: +++ TD AI code backport from 4.13 +++
    public void setNearestTaxingPoint(Point3d point3d, Orient orient)
    {
        double d = 1.7976931348623157E+308D;
        int i = 0;
        for(int j = Cur; j < WList.size(); j++)
        {
            WayPoint waypoint = (WayPoint)WList.get(j);
            if(waypoint.waypointType != 4 && waypoint.waypointType != 5)
                break;
            waypoint.getP(tmpP);
            V.sub(tmpP, point3d);
            double d1 = V.x * V.x + V.y * V.y;
            if(d1 < d)
            {
                i = j;
                d = d1;
            }
        }

        WayPoint waypoint1 = (WayPoint)WList.get(i);
        waypoint1.getP(tmpP);
        V.sub(tmpP, point3d);
        orient.transformInv(V);
        V.normalize();
        if(V.x < -0.5D && i < WList.size() - 1)
        {
            WayPoint waypoint2 = (WayPoint)WList.get(i + 1);
            if(waypoint2.waypointType == 4 || waypoint2.waypointType == 5)
                i++;
        }
        Cur = i;
    }
	// TODO: --- TD AI code backport from 4.13 ---

}