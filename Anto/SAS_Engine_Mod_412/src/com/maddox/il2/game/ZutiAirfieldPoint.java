package com.maddox.il2.game;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;

public class ZutiAirfieldPoint 
{
	private double x1 = 0.0D;
	private double y1 = 0.0D;
	private double x2 = 0.0D;
	private double y2 = 0.0D;
	private double friction = 3.8D;
	
	public ZutiAirfieldPoint(double x1, double y1, double x2, double y2, double friction)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
		this.friction = friction;
	}
	
	public double isInZAPArea(double in_x, double in_y)
	{
		if(in_x >= x1 && in_x <= x2 && in_y <= y1 && in_y >= y2)
		{
			return this.friction;
		}
		
		return -1.0D;
	}
	
	public static boolean isLocatedOnCarrier(Point3d point3d)
	{
		for( int i=0; i<Main.cur().mission.actors.size(); i++ )
		{
			Actor actor = (Actor)Main.cur().mission.actors.get(i);
			
            if(actor instanceof com.maddox.il2.objects.ships.BigshipGeneric && (actor.toString().indexOf("CV") > 0 || actor.toString().indexOf("Carrier") > 0 ) && actor.name().indexOf("Chief") > 0)
            {
				//ok, check if the location is near enough (radius of 150m max!)
				double d = 150*150;
				double x = actor.pos.getAbsPoint().x;
				double y = actor.pos.getAbsPoint().y;
				double d_22_ = (((x - point3d.x) * (x - point3d.x)) + ((y - point3d.y) * (y - point3d.y)));
				if (d_22_ <= d)
				{
					//System.out.println("Point stay position: " + point_stay.toString());
					return true;
				}
            }
        }
		
		return false;
	}
	
	/**
	 * Check if aircraft is test runway.
	 * @param point3d
	 * @return
	 */
	public static boolean isLocatedTestRunway(Point3d point3d)
	{
		for( int i=0; i<Main.cur().mission.actors.size(); i++ )
		{
			Actor actor = (Actor)Main.cur().mission.actors.get(i);
			//System.out.println(actor.toString());
			
			if( actor instanceof com.maddox.il2.objects.ships.BigshipGeneric && actor.toString().indexOf("Ship$Rwy") > 0 )
            {
				//ok, check if the location is near enough (radius of 150m max!)
				double d = 1000*1000;
				double x = actor.pos.getAbsPoint().x;
				double y = actor.pos.getAbsPoint().y;
				double d_22_ = (((x - point3d.x) * (x - point3d.x)) + ((y - point3d.y) * (y - point3d.y)));
				if (d_22_ <= d)
				{
					//System.out.println("Point stay position: " + point_stay.toString());
					return true;
				}
            }
		}
		return false;
	}
}