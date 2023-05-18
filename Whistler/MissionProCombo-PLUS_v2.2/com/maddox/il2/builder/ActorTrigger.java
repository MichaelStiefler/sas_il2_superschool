package com.maddox.il2.builder;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.buildings.House;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.aeronautics.AeroanchoredGeneric;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.lights.SearchlightGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.stationary.*;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.Message;

public class ActorTrigger extends Actor
    implements ActorAlign
{

    public Actor getTrigger()
    {
        return trigger;
    }

    public void setTrigger(Actor actor)
    {
        if(actor != null && type != 2 && ((actor.getOwner() instanceof PathAir) || (actor.getOwner() instanceof PathChief) || (actor instanceof PlMisRocket.Rocket)))
            trigger = actor;
        else
        if(actor != null && type == 0 && ((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof AeroanchoredGeneric) || (actor instanceof Wagon) || (actor instanceof SearchlightGeneric) || (actor instanceof PlaneGeneric) || (actor instanceof SirenGeneric) || (actor instanceof SmokeGeneric) || (actor instanceof StationaryGeneric) || (actor instanceof TankGeneric) || (actor instanceof House)))
            trigger = actor;
        else
        if(actor != null && type == 2 && (actor.getOwner() instanceof PathAir))
            trigger = actor;
        else
            trigger = null;
    }

    public Actor getLink()
    {
        return link;
    }

    public void setLink(Actor actor)
    {
        if(actor != null && ((actor.getOwner() instanceof PathAir) || (actor.getOwner() instanceof PathChief) || (actor instanceof PlMisRocket.Rocket) || (actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof AeroanchoredGeneric) || (actor instanceof Wagon) || (actor instanceof SearchlightGeneric) || (actor instanceof PlaneGeneric) || (actor instanceof StationaryGeneric) || (actor instanceof TankGeneric) || (actor instanceof House)))
            link = actor;
        else
            link = null;
    }

    public void align()
    {
        alignPosToLand(0.0D, true);
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    public ActorTrigger(Point3d point3d, int i, int k, boolean btimeout, int timeoutmis, int rmis, int zmin, 
            int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, int zAltiDiff, String sTextDisplay, 
            int zTextDuree)
    {
        trigger = null;
        link = null;
        super.flags |= 0x2000;
        super.pos = new ActorPosMove(this);
        super.pos.setAbs(point3d);
        type = i;
        army = k;
        bTimeout = btimeout;
        timeout = timeoutmis;
        r = rmis;
        altiMin = zmin;
        altiMax = zmax;
        iaHumans = ziaHumans;
        bTSortie = bSortie;
        avionMin = zAvionMin;
        proba = zProba;
        altiDiff = zAltiDiff;
        textDisplay = sTextDisplay;
        textDuree = zTextDuree;
        align();
        drawing(true);
        super.icon = IconDraw.get("icons/tdestroyair.mat");
    }

    public void addActor(String s)
    {
        if(s != null && s != "")
        {
            trigger = Actor.getByName(s);
            if(trigger == null)
                throw new RuntimeException("trigger NOT found");
            if(trigger instanceof PathAir)
                trigger = ((Path)trigger).point(0);
            else
            if(trigger instanceof PathChief)
                trigger = ((Path)trigger).point(0);
            else
            if(!(trigger instanceof ShipGeneric) && !(trigger instanceof BigshipGeneric) && !(trigger instanceof ArtilleryGeneric) && !(trigger instanceof CarGeneric) && !(trigger instanceof AeroanchoredGeneric) && !(trigger instanceof Wagon) && !(trigger instanceof SearchlightGeneric) && !(trigger instanceof PlaneGeneric) && !(trigger instanceof SirenGeneric) && !(trigger instanceof SmokeGeneric) && !(trigger instanceof StationaryGeneric) && !(trigger instanceof TankGeneric) && !(trigger instanceof House) && !(trigger instanceof PlMisRocket.Rocket))
                throw new RuntimeException("target NOT found");
        } else
        {
            trigger = null;
        }
    }

    public void addLinkActor(String s)
    {
        if(s != null && s != "")
        {
            link = Actor.getByName(s);
            if(link == null)
                throw new RuntimeException("link NOT found");
            if(link instanceof PathAir)
                link = ((Path)link).point(0);
            else
            if(link instanceof PathChief)
                link = ((Path)link).point(0);
            else
            if(!(link instanceof ShipGeneric) && !(link instanceof BigshipGeneric) && !(link instanceof ArtilleryGeneric) && !(link instanceof CarGeneric) && !(link instanceof AeroanchoredGeneric) && !(link instanceof Wagon) && !(link instanceof SearchlightGeneric) && !(link instanceof PlaneGeneric) && !(link instanceof SirenGeneric) && !(link instanceof SmokeGeneric) && !(link instanceof StationaryGeneric) && !(link instanceof TankGeneric) && !(link instanceof House) && !(link instanceof PlMisRocket.Rocket))
                throw new RuntimeException("link NOT found");
        } else
        {
            link = null;
        }
    }

    protected void createActorHashCode()
    {
        makeActorRealHashCode();
    }

    public int type;
    public Actor trigger;
    public Actor link;
    public int timeout;
    public boolean bTimeout;
    public int r;
    public int army;
    public int altiMin;
    public int altiMax;
    public int iaHumans;
    public boolean bTSortie;
    public int avionMin;
    public int altiDiff;
    public int proba;
    public String textDisplay;
    public int textDuree;
}
