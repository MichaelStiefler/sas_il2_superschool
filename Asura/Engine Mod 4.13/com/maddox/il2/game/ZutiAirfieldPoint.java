// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:09:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ZutiAirfieldPoint.java

package com.maddox.il2.game;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.objects.ships.BigshipGeneric;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.game:
//            Main, Mission

public class ZutiAirfieldPoint
{

    public ZutiAirfieldPoint(double x1, double y1, double x2, double y2, double friction)
    {
        this.x1 = 0.0D;
        this.y1 = 0.0D;
        this.x2 = 0.0D;
        this.y2 = 0.0D;
        this.friction = 3.7999999999999998D;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.friction = friction;
    }

    public double isInZAPArea(double in_x, double in_y)
    {
        if(in_x >= x1 && in_x <= x2 && in_y <= y1 && in_y >= y2)
            return friction;
        else
            return -1D;
    }

    public static boolean isLocatedOnCarrier(Point3d point3d)
    {
        for(int i = 0; i < Main.cur().mission.actors.size(); i++)
        {
            Actor actor = (Actor)Main.cur().mission.actors.get(i);
            if((actor instanceof BigshipGeneric) && (actor.toString().indexOf("CV") > 0 || actor.toString().indexOf("Carrier") > 0) && actor.name().indexOf("Chief") > 0)
            {
                double d = 22500D;
                double x = ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double y = ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d_22_ = (x - ((Tuple3d) (point3d)).x) * (x - ((Tuple3d) (point3d)).x) + (y - ((Tuple3d) (point3d)).y) * (y - ((Tuple3d) (point3d)).y);
                if(d_22_ <= d)
                    return true;
            }
        }

        return false;
    }

    public static boolean isLocatedTestRunway(Point3d point3d)
    {
        for(int i = 0; i < Main.cur().mission.actors.size(); i++)
        {
            Actor actor = (Actor)Main.cur().mission.actors.get(i);
            if((actor instanceof BigshipGeneric) && actor.toString().indexOf("Ship$Rwy") > 0)
            {
                double d = 1000000D;
                double x = ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double y = ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d_22_ = (x - ((Tuple3d) (point3d)).x) * (x - ((Tuple3d) (point3d)).x) + (y - ((Tuple3d) (point3d)).y) * (y - ((Tuple3d) (point3d)).y);
                if(d_22_ <= d)
                    return true;
            }
        }

        return false;
    }

    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double friction;
}