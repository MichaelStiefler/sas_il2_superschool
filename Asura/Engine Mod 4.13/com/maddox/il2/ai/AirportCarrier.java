// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   AirportCarrier.java

package com.maddox.il2.ai;

import com.maddox.JGP.*;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Airdrome;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.ai.air.CellAirPlane;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Mission;
import com.maddox.il2.gui.GUINetClientDBrief;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.rts.*;
import com.maddox.util.NumberTokenizer;
import java.io.PrintStream;
import java.util.*;
import com.maddox.il2.objects.air.*;

// Referenced classes of package com.maddox.il2.ai:
//            Airport, Way, WayPoint, World, 
//            RangeRandom, War, Wing

public class AirportCarrier extends Airport
{
    class DeckUpdater extends Interpolate
    {

        public boolean tick()
        {
            if(ship().isAlive())
            {
                if(Time.tickCounter() > oldTickCounter + 150 + rnd1)
                {
                    oldTickCounter = Time.tickCounter();
                    checkIsDeckClear();
                }
                if(Time.tickCounter() > oldIdleTickCounter + 2000 + rnd2)
                {
                    oldIdleTickCounter = Time.tickCounter();
                    checkPlaneIdle();
                    skipCheck = false;
                }
            }
            return true;
        }

        DeckUpdater()
        {
        }
    }


    public BigshipGeneric ship()
    {
        return ship;
    }

    public AirportCarrier(BigshipGeneric bigshipgeneric, Loc aloc[])
    {
        bornPlace = null;
        lastCarrierUsers = new ArrayList();
        ui = null;
        clientLoc = null;
        lastAddedAC = null;
        lastAddedCells = null;
        rnd1 = World.Rnd().nextInt(0, 30);
        rnd2 = World.Rnd().nextInt(40, 60);
        ownDefaultStayPoints = (Point_Stay[][])null;
        skipCheck = false;
        tmpLoc = new Loc();
        tmpP3d = new Point3d();
        tmpP3f = new Point3f();
        tmpOr = new Orient();
        curPlaneShift = 0;
        oldTickCounter = 0;
        oldIdleTickCounter = 0;
        r = new Loc();
        ship = bigshipgeneric;
        pos = new ActorPosMove(this, new Loc());
        pos.setBase(bigshipgeneric, null, false);
        pos.reset();
        runway = aloc;
        if(Mission.isDogfight())
        {
            ownDefaultStayPoints = getStayPlaces(false);
            Point_Stay apoint_stay[][] = World.cur().airdrome.stay;
            Point_Stay apoint_stay1[][] = new Point_Stay[apoint_stay.length + ownDefaultStayPoints.length][];
            int i = 0;
            for(int j = 0; j < apoint_stay.length; j++)
                apoint_stay1[i++] = apoint_stay[j];

            for(int k = 0; k < ownDefaultStayPoints.length; k++)
                apoint_stay1[i++] = ownDefaultStayPoints[k];

            World.cur().airdrome.stay = apoint_stay1;
        }
        startDeckOperations();
    }

    public void destroy()
    {
        if(lastCarrierUsers != null)
            lastCarrierUsers.clear();
        super.destroy();
    }

    public void setCustomStayPoints()
    {
        if(ship.zutiBornPlace != null && ship.zutiBornPlace.zutiMaxBasePilots > 0)
        {
            Point_Stay apoint_stay[][] = World.cur().airdrome.stay;
            Point_Stay apoint_stay1[][] = getStayPlaces(true);
            Point_Stay apoint_stay2[][] = new Point_Stay[(apoint_stay.length - ownDefaultStayPoints.length) + apoint_stay1.length][];
            int i = 0;
            for(int j = 0; j < apoint_stay.length; j++)
            {
                boolean flag = false;
                int l = 0;
                do
                {
                    if(l >= ownDefaultStayPoints.length)
                        break;
                    if(ownDefaultStayPoints[l] == apoint_stay[j])
                    {
                        flag = true;
                        break;
                    }
                    l++;
                } while(true);
                if(!flag)
                    apoint_stay2[i++] = apoint_stay[j];
            }

            for(int k = 0; k < apoint_stay1.length; k++)
                apoint_stay2[i++] = apoint_stay1[k];

            World.cur().airdrome.stay = apoint_stay2;
            ownDefaultStayPoints = (Point_Stay[][])null;
        }
    }

    public boolean isAlive()
    {
        return Actor.isAlive(ship);
    }

    public int getArmy()
    {
        if(Actor.isAlive(ship))
            return ship.getArmy();
        else
            return super.getArmy();
    }

    public boolean landWay(FlightModel flightmodel)
    {
        Way way = new Way();
        tmpLoc.set(runway[1]);
        tmpLoc.add(ship.initLoc);
        float f = flightmodel.M.massEmpty * 0.0003333F;
        if(f > 1.0F)
            f = 1.0F;
        if(f < 0.4F)
            f = 0.4F;
        for(int i = x.length - 1; i >= 0; i--)
        {
            WayPoint waypoint = new WayPoint();
            if(flightmodel.actor instanceof TypeFastJet)
                tmpP3d.set(xFJ[i], yFJ[i], zFJ[i]);
            else
            if(flightmodel.EI.engines[0].getType() == 2)
                tmpP3d.set(xJ[i], yJ[i], zJ[i]);
            else
                tmpP3d.set(x[i] * f, y[i] * f, z[i] * f);
            if(flightmodel.actor instanceof TypeFastJet)
            {
                float f1 = vFJ[i] * 0.278F;
                if(i == 2)
                {
                    if(f1 > flightmodel.VminFLAPS + 13F)
                        f1 = flightmodel.VminFLAPS + 13F;
                } else
                if(i == 3)
                {
                    if(f1 > flightmodel.Vmin + 13F)
                        f1 = flightmodel.Vmin + 13F;
                } else
                if(i == 4 && f1 > flightmodel.Vmin + 25F)
                    f1 = flightmodel.Vmin + 25F;
                waypoint.set(Math.min(f1, 108F));
            } else
            if(flightmodel.EI.engines[0].getType() == 2)
            {
                float f2 = vJ[i] * 0.278F;
                if(i == 2)
                {
                    if(f2 > flightmodel.VminFLAPS + 13F)
                        f2 = flightmodel.VminFLAPS + 13F;
                } else
                if(i == 3)
                {
                    if(f2 > flightmodel.Vmin + 13F)
                        f2 = flightmodel.Vmin + 13F;
                } else
                if(i == 4 && f2 > flightmodel.Vmin + 25F)
                    f2 = flightmodel.Vmin + 25F;
                waypoint.set(Math.min(f2, 90F));
            } else
            {
                waypoint.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.6F));
            }
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
    	if(!ship.isAlive())
        {
            flightmodel.AP.way.setLanding(false);
            return;
        }
        tmpLoc.set(runway[1]);
        tmpLoc.add(ship.initLoc);
        float f = flightmodel.M.massEmpty * 0.0003333F;
        if(f > 1.0F)
            f = 1.0F;
        if(f < 0.4F)
            f = 0.4F;
        for(int i = 0; i < x.length; i++)
        {
            WayPoint waypoint = flightmodel.AP.way.get(i);
            if(flightmodel.actor instanceof TypeFastJet)
                tmpP3d.set(xFJ[x.length - 1 - i], yFJ[x.length - 1 - i], zFJ[x.length - 1 - i]);
            else
                tmpP3d.set(x[x.length - 1 - i] * f, y[x.length - 1 - i] * f, z[x.length - 1 - i] * f);
            tmpLoc.transform(tmpP3d);
            tmpP3f.set(tmpP3d);
            waypoint.set(tmpP3f);
        }
        
    }

    public void rebuildLastPoint(FlightModel flightmodel)
    {
        if(!Actor.isAlive(ship))
            return;
        int i = flightmodel.AP.way.Cur();
        flightmodel.AP.way.last();
        if(flightmodel.AP.way.curr().Action == 2)
        {
            ship.pos.getAbs(tmpP3d);
            flightmodel.AP.way.curr().set(tmpP3d);
        }
        flightmodel.AP.way.setCur(i);
    }

    public double shiftFromLine(FlightModel flightmodel)
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
        Aircraft aircraft1 = War.getNearestFriendAtPoint(tmpLoc.getPoint(), aircraft, 50F);
        if(aircraft1 != null && aircraft1 != aircraft)
            return 1;
        if(aircraft.FM.CT.GearControl > 0.0F)
            return 0;
        if(landingRequest > 0)
        {
            return 1;
        } else
        {
            landingRequest = 3000;
            return 0;
        }
    }

    public void setTakeoffOld(Point3d point3d, Aircraft aircraft)
    {
        if(!Actor.isValid(aircraft))
            return;
        r.set(runway[0]);
        r.add(ship.pos.getAbs());
        if(!Mission.isDogfight() && Time.tickCounter() != oldTickCounter)
        {
            oldTickCounter = Time.tickCounter();
            curPlaneShift = 0;
        }
        curPlaneShift++;
        aircraft.FM.setStationedOnGround(false);
        aircraft.FM.setWasAirborne(true);
        tmpLoc.set(-((float)curPlaneShift * 200F), -((float)curPlaneShift * 100F), 300D, 0.0F, 0.0F, 0.0F);
        tmpLoc.add(r);
        aircraft.pos.setAbs(tmpLoc);
        aircraft.pos.getAbs(tmpP3d, tmpOr);
        startSpeed.set(100D, 0.0D, 0.0D);
        tmpOr.transform(startSpeed);
        aircraft.setSpeed(startSpeed);
        aircraft.pos.reset();
        if(aircraft.FM instanceof Maneuver)
            ((Maneuver)aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
        aircraft.FM.AP.way.takeoffAirport = this;
        if(aircraft == World.getPlayerAircraft())
        {
            aircraft.FM.EI.setCurControlAll(true);
            aircraft.FM.EI.setEngineRunning();
            aircraft.FM.CT.setPowerControl(0.75F);
        }
    }

    private boolean setStationaryPlaneTakeoff(Aircraft aircraft)
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
            ((Maneuver)aircraft.FM).rwLoc = r;
        }
        aircraft.FM.AP.way.takeoffAirport = this;
        aircraft.spawnLocSingleCoop.set(aircraft.pos.getAbs());
        if(!Mission.isCoop() || aircraft.getWing().bCoopSpawnStationary)
            actor.destroy();
        aircraft.stationarySpawnLocSet = true;
        return true;
    }

    public double speedLen()
    {
        ship.getSpeed(shipSpeed);
        return shipSpeed.length();
    }

    private void checkIsDeckClear()
    {
        if(skipCheck)
            return;
        if(lastAddedAC != null && lastAddedAC.isDestroyed())
        {
            CellAirField cellairfield = ship.getCellTO();
            boolean flag1 = cellairfield.removeAirPlane(lastAddedCells);
            if(flag1)
                curPlaneShift--;
            lastAddedAC = null;
            lastAddedCells = null;
        }
        boolean flag = true;
        for(int i = lastCarrierUsers.size() - 1; i >= 0; i--)
        {
            Aircraft aircraft = (Aircraft)lastCarrierUsers.get(i);
            if(aircraft != null && !aircraft.isDestroyed() && aircraft.isAlive() && (aircraft.FM.Gears.isUnderDeck() || NetAircraft.isOnCarrierDeck(this, aircraft.pos.getAbs())))
                flag = false;
            else
                lastCarrierUsers.remove(aircraft);
        }

        if(flag)
        {
            lastCarrierUsers.clear();
            deckCleared();
        }
    }

    private void checkPlaneIdle()
    {
        for(int i = lastCarrierUsers.size() - 1; i >= 0; i--)
        {
            Aircraft aircraft = (Aircraft)lastCarrierUsers.get(i);
            if(aircraft == null || aircraft.isDestroyed() || !aircraft.FM.Gears.isUnderDeck() && !NetAircraft.isOnCarrierDeck(this, aircraft.pos.getAbs()))
                continue;
            aircraft.idleTimeOnCarrier++;
            if(aircraft.idleTimeOnCarrier < 6)
                continue;
            if(aircraft.isNetPlayer() && !aircraft.isNetMaster())
            {
                Aircraft aircraft1 = aircraft;
                NetUser netuser = ((com.maddox.il2.objects.air.NetAircraft.AircraftNet)((NetAircraft) (aircraft1)).net).netUser;
                netuser.kick(netuser);
                continue;
            }
            if(aircraft.FM.AP.way.size() != 1)
            {
                aircraft.net.destroy();
                aircraft.destroy();
                lastCarrierUsers.remove(aircraft);
            }
        }

    }

    public Loc setClientTakeOff(Point3d point3d, Aircraft aircraft)
    {
        Loc loc = null;
        if(clientLoc != null)
            loc = new Loc(clientLoc);
        clientLoc = null;
        if(loc == null || !isLocValid(loc))
        {
            setTakeoffOld(point3d, aircraft);
            Loc loc1 = aircraft.pos.getAbs();
            double d = World.Rnd().nextDouble(400D, 800D);
            double d1 = World.Rnd().nextDouble(400D, 800D);
            if(World.Rnd().nextFloat() < 0.5F)
                d *= -1D;
            if(World.Rnd().nextFloat() < 0.5F)
                d1 *= -1D;
            Point3d point3d2 = new Point3d(d, d1, 0.0D);
            loc1.add(point3d2);
            aircraft.pos.setAbs(loc1);
            return loc1;
        }
        loc.add(ship.pos.getAbs());
        Point3d point3d1 = loc.getPoint();
        Orient orient = loc.getOrient();
        orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
        ship.getSpeed(shipSpeed);
        aircraft.setOnGround(point3d1, orient, shipSpeed);
        if(aircraft.FM instanceof Maneuver)
            ((Maneuver)aircraft.FM).direction = aircraft.pos.getAbsOrient().getAzimut();
        aircraft.FM.AP.way.takeoffAirport = this;
        aircraft.FM.brakeShoe = true;
        aircraft.FM.turnOffCollisions = true;
        aircraft.FM.brakeShoeLoc.set(aircraft.pos.getAbs());
        aircraft.FM.brakeShoeLoc.sub(ship.pos.getAbs());
        aircraft.FM.brakeShoeLastCarrier = ship;
        aircraft.FM.Gears.bFlatTopGearCheck = true;
        aircraft.makeMirrorCarrierRelPos();
        if(aircraft.FM.CT.bHasWingControl)
        {
            aircraft.FM.CT.wingControl = 1.0F;
            aircraft.FM.CT.forceWing(1.0F);
        }
        return loc;
    }

    private Point3d reservePlaceForPlane(CellAirPlane cellairplane, Aircraft aircraft)
    {
        CellAirField cellairfield = ship.getCellTO();
        if(cellairfield.findPlaceForAirPlane(cellairplane))
        {
            cellairfield.placeAirPlane(cellairplane, cellairfield.resX(), cellairfield.resY());
            double d = -cellairfield.leftUpperCorner().x - (double)cellairfield.resX() * cellairfield.getCellSize() - cellairplane.ofsX;
            double d1 = cellairfield.leftUpperCorner().y - (double)cellairfield.resY() * cellairfield.getCellSize() - cellairplane.ofsY;
            double d2 = runway[0].getZ();
            curPlaneShift++;
            if(Mission.isDogfight() && ship.net.isMaster())
            {
                if(aircraft != null)
                {
                    lastCarrierUsers.add(aircraft);
                    lastAddedAC = aircraft;
                }
                lastAddedCells = cellairplane;
            }
            return new Point3d(d1, d, d2);
        } else
        {
            return null;
        }
    }

    public void setStationaryPlaneTakeoff(Aircraft aaircraft[])
    {
        if(!Mission.isDogfight())
        {
            for(int i = 0; i < aaircraft.length; i++)
                if(aaircraft[i] != null && aaircraft[i].spawnLocSingleCoop != null)
                    setStationaryPlaneTakeoff(aaircraft[i]);

        }
    }

//    public void setTakeoff(Point3d point3d, Aircraft aaircraft[])
//    {    	
//    	setStationaryPlaneTakeoff(aaircraft);
//        CellAirField cellairfield = ship.getCellTO();
//        if(cellairfield == null)
//        {
//            for(int i = 0; i < aaircraft.length; i++)
//                setTakeoffOld(point3d, aaircraft[i]);
//
//            return;
//        }
//        if(!Mission.isDogfight() && Time.tickCounter() != oldTickCounter)
//        {
//            oldTickCounter = Time.tickCounter();
//            curPlaneShift = 0;
//            cellairfield.freeCells();
//        }
//        ship.getSpeed(shipSpeed);
//        for(int j = 0; j < aaircraft.length; j++)
//        {
//            if(!Actor.isValid(aaircraft[j]) || aaircraft[j].stationarySpawnLocSet && (!Mission.isCoop() || aaircraft[j].getWing().bCoopSpawnStationary))
//                continue;
//            CellAirPlane cellairplane = aaircraft[j].getCellAirPlane();
//            Point3d point3d1 = reservePlaceForPlane(cellairplane, aaircraft[j]);
//            if(point3d1 != null)
//            {
//                double d = point3d1.x;
//                double d1 = point3d1.y;
//                double d2 = point3d1.z;
//                tmpLoc.set(d, d1, d2 + (double)aaircraft[j].FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
//                tmpLoc.add(ship.pos.getAbs());
//                Point3d point3d2 = tmpLoc.getPoint();
//                Orient orient = tmpLoc.getOrient();
//                orient.increment(0.0F, aaircraft[j].FM.Gears.Pitch, 0.0F);
//                aaircraft[j].setOnGround(point3d2, orient, shipSpeed);
//                if(aaircraft[j].FM instanceof Maneuver)
//                    ((Maneuver)aaircraft[j].FM).direction = aaircraft[j].pos.getAbsOrient().getAzimut();
//                aaircraft[j].FM.AP.way.takeoffAirport = this;
//                aaircraft[j].FM.brakeShoe = true;
//                aaircraft[j].FM.turnOffCollisions = true;
//                aaircraft[j].FM.brakeShoeLoc.set(aaircraft[j].pos.getAbs());
//                aaircraft[j].FM.brakeShoeLoc.sub(ship.pos.getAbs());
//                aaircraft[j].FM.brakeShoeLastCarrier = ship;
//                aaircraft[j].FM.Gears.bFlatTopGearCheck = true;
//                aaircraft[j].makeMirrorCarrierRelPos();
//                if(curPlaneShift > 1 && aaircraft[j].FM.CT.bHasWingControl)
//                {
//                    aaircraft[j].FM.CT.wingControl = 1.0F;
//                    aaircraft[j].FM.CT.forceWing(1.0F);
//                }
//            } else
//            {
//                setTakeoffOld(point3d, aaircraft[j]);
//            }
//        }
//
//        if(Actor.isValid(aaircraft[0]) && (aaircraft[0].FM instanceof Maneuver))
//        {
//            Maneuver maneuver = (Maneuver)aaircraft[0].FM;
//            if(maneuver.Group != null && maneuver.Group.w != null)
//                maneuver.Group.w.takeoffAirport = this;
//        }
//    }

    public void setTakeoff(Point3d point3d, Aircraft aaircraft[])
    {
        CellAirField cellairfield = ship.getCellTO();
        if(cellairfield == null)
        {
            for(int i = 0; i < aaircraft.length; i++)
                setTakeoffOld(point3d, aaircraft[i]);

        } else
        {
            if(!Mission.isDogfight() && Time.tickCounter() != oldTickCounter)
            {
                oldTickCounter = Time.tickCounter();
                curPlaneShift = 0;
                cellairfield.freeCells();
            }
            ship.getSpeed(shipSpeed);
            for(int j = 0; j < aaircraft.length; j++)
            {
                if(!Actor.isValid(aaircraft[j]))
                    continue;
                CellAirPlane cellairplane = aaircraft[j].getCellAirPlane();
                if(aaircraft[j].FM.WingspanFolded != 0.0F && aaircraft[j].FM.CT.bHasWingControl)
                {
                    cellairplane.setFoldedWidth((int)aaircraft[j].FM.WingspanFolded);
                    aaircraft[j].FM.CT.wingControl = 1.0F;
                    aaircraft[j].FM.CT.forceWing(1.0F);
                } else
                if(aaircraft[j].FM.CT.bHasWingControl)
                {
                    aaircraft[j].FM.CT.wingControl = 1.0F;
                    aaircraft[j].FM.CT.forceWing(1.0F);
                }
                if(cellairfield.findPlaceForAirPlaneCarrier(cellairplane))
                {
                    cellairfield.placeAirPlaneCarrier(cellairplane, cellairfield.resX(), cellairfield.resY());
                    double d = -((Tuple3d) (cellairfield.leftUpperCorner())).x - (double)cellairfield.resX() * cellairfield.getCellSize() - (double)(cellairplane.getWidth() / 2);
                    double d1 = ((Tuple3d) (cellairfield.leftUpperCorner())).y - (double)cellairfield.resY() * cellairfield.getCellSize() - cellairplane.ofsY;
                    double d2 = runway[0].getZ();                    
                    tmpLoc.set(d1, d, d2 + (double)aaircraft[j].FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
                    tmpLoc.add(((Actor) (ship)).pos.getAbs());
                    Point3d point3d1 = tmpLoc.getPoint();
                    Orient orient = tmpLoc.getOrient();
                    orient.increment(0.0F, aaircraft[j].FM.Gears.Pitch, 0.0F);
                    aaircraft[j].setOnGround(point3d1, orient, shipSpeed);
                    if(aaircraft[j].FM instanceof Maneuver)
                      ((Maneuver)aaircraft[j].FM).direction = aaircraft[j].pos.getAbsOrient().getAzimut();
                    aaircraft[j].FM.AP.way.takeoffAirport = this;
                    aaircraft[j].FM.brakeShoe = true;
                    aaircraft[j].FM.turnOffCollisions = true;
                    aaircraft[j].FM.brakeShoeLoc.set(((Actor) (aaircraft[j])).pos.getAbs());
                    aaircraft[j].FM.brakeShoeLoc.sub(((Actor) (ship)).pos.getAbs());
                    aaircraft[j].FM.brakeShoeLastCarrier = ship;
                    aaircraft[j].FM.Gears.bFlatTopGearCheck = true;
                    aaircraft[j].makeMirrorCarrierRelPos();
                } else
                {
                    setTakeoffOld(point3d, aaircraft[j]);
                }
            }

            if(Actor.isValid(aaircraft[0]) && (aaircraft[0].FM instanceof Maneuver))
            {
                Maneuver maneuver = (Maneuver)aaircraft[0].FM;
                if(maneuver.Group != null && maneuver.Group.w != null)
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
        Point3d point3d = loc.getPoint();
        Orient orient = loc.getOrient();
        orient.increment(0.0F, aircraft.FM.Gears.Pitch, 0.0F);
        if(aircraft.FM instanceof Maneuver)
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

    public static boolean isPlaneContainsArrestor(Class class1)
    {
        clsBigArrestorPlane();
        return _clsMapArrestorPlane.containsKey(class1);
    }

    private static Class clsBigArrestorPlane()
    {
      if (_clsBigArrestorPlane != null) {
        return _clsBigArrestorPlane;
      }
      double d1 = 0.0D;
      SectFile localSectFile1 = new SectFile("com/maddox/il2/objects/air.ini", 0);
      int i = localSectFile1.sections();
      for (int j = 0; j < i; j++)
      {
        int k = localSectFile1.vars(j);
        for (int m = 0; m < k; m++)
        {
          StringTokenizer localStringTokenizer = new StringTokenizer(localSectFile1.value(j, m));
          if (localStringTokenizer.hasMoreTokens())
          {
            String str1 = "com.maddox.il2.objects." + localStringTokenizer.nextToken();
            
            Class localClass = null;
            String str2 = null;
            try
            {
              localClass = Class.forName(str1);
              str2 = Property.stringValue(localClass, "FlightModel", null);
            }
            catch (Exception localException1)
            {
              System.out.println(localException1.getMessage());
              localException1.printStackTrace();
            }
            try
            {
              if (str2 != null)
              {
                SectFile localSectFile2 = FlightModelMain.sectFile(str2);
                if (localSectFile2.get("Controls", "CArrestorHook", 0) == 1)
                {
                  _clsMapArrestorPlane.put(localClass, null);
                  String str3 = Aircraft.getPropertyMesh(localClass, null);
                  
                  SectFile localSectFile3 = new SectFile(str3, 0);
                  
                  String str4 = localSectFile3.get("_ROOT_", "CollisionObject", (String)null);
                  if (str4 != null)
                  {
                    NumberTokenizer localNumberTokenizer = new NumberTokenizer(str4);
                    if (localNumberTokenizer.hasMoreTokens())
                    {
                      localNumberTokenizer.next();
                      if (localNumberTokenizer.hasMoreTokens())
                      {
                        double d2 = localNumberTokenizer.next(-1.0D);
                        if ((d2 > 0.0D) && (d1 < d2))
                        {
                          d1 = d2;
                          _clsBigArrestorPlane = localClass;
                        }
                      }
                    }
                  }
                }
              }
            }
            catch (Exception localException2)
            {
              System.out.println(localException2.getMessage());
              localException2.printStackTrace();
            }
          }
        }
      }
      return _clsBigArrestorPlane;
    }


    private static CellAirPlane cellBigArrestorPlane()
    {
        if(_cellBigArrestorPlane != null)
        {
            return _cellBigArrestorPlane;
        } else
        {
            _cellBigArrestorPlane = Aircraft.getCellAirPlane(clsBigArrestorPlane());
            return _cellBigArrestorPlane;
        }
    }

    private Point_Stay[][] getStayPlaces(boolean flag)
    {
        Point_Stay apoint_stay[][] = (Point_Stay[][])null;
        Class class1 = ship.getClass();
        apoint_stay = (Point_Stay[][])(Point_Stay[][])Property.value(class1, "StayPlaces", null);
        if(apoint_stay == null || flag)
        {
            ArrayList arraylist = new ArrayList();
            if(flag)
            {
                if(ship.zutiBornPlace != null)
                {
                    int i = ship.zutiBornPlace.zutiMaxBasePilots;
                    if(i > 164)
                        i = 164;
                    for(int l = 0; l < i; l++)
                        arraylist.add(new Point2d(-1D, 1.0D));

                }
            } else
            {
                CellAirPlane cellairplane = null;
                String s = ship.getShipProp().typicalPlaneClass;
                if(s != null && !s.equals(""))
                    try
                    {
                        Class class2 = ObjIO.classForName(s);
                        cellairplane = Aircraft.getCellAirPlane(class2);
                    }
                    catch(ClassNotFoundException classnotfoundexception)
                    {
                        cellairplane = cellBigArrestorPlane();
                    }
                else
                    cellairplane = cellBigArrestorPlane();
                CellAirField cellairfield = ship.getCellTO();
                cellairfield = (CellAirField)cellairfield.getClone();
                do
                {
                    cellairplane = (CellAirPlane)cellairplane.getClone();
                    if(!cellairfield.findPlaceForAirPlane(cellairplane))
                        break;
                    cellairfield.placeAirPlane(cellairplane, cellairfield.resX(), cellairfield.resY());
                    double d1 = -cellairfield.leftUpperCorner().x - (double)cellairfield.resX() * cellairfield.getCellSize() - cellairplane.ofsX;
                    double d3 = cellairfield.leftUpperCorner().y - (double)cellairfield.resY() * cellairfield.getCellSize() - cellairplane.ofsY;
                    arraylist.add(new Point2d(d3, d1));
                } while(true);
            }
            int j = arraylist.size();
            if(j > 0)
            {
                apoint_stay = new Point_Stay[j][1];
                for(int i1 = 0; i1 < j; i1++)
                {
                    Point2d point2d = (Point2d)arraylist.get(i1);
                    apoint_stay[i1][0] = new Point_Stay((float)point2d.x, (float)point2d.y);
                }

                if(!flag)
                    Property.set(class1, "StayPlaces", apoint_stay);
            }
        }
        if(apoint_stay == null)
            return (Point_Stay[][])null;
        Point_Stay apoint_stay1[][] = new Point_Stay[apoint_stay.length][1];
        for(int k = 0; k < apoint_stay.length; k++)
        {
            Point_Stay point_stay = apoint_stay[k][0];
            double d = point_stay.x;
            double d2 = point_stay.y;
            double d4 = runway[0].getZ();
            tmpLoc.set(d, d2, d4, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
            tmpLoc.add(ship.pos.getAbs());
            Point3d point3d = tmpLoc.getPoint();
            apoint_stay1[k][0] = new Point_Stay((float)point3d.x, (float)point3d.y);
        }

        return apoint_stay1;
    }

    public void setCellUsed(Aircraft aircraft)
    {
        skipCheck = false;
        if(ship.net.isMaster())
        {
            lastCarrierUsers.add(aircraft);
            lastAddedAC = aircraft;
        }
        if(aircraft.FM.CT.bHasWingControl && !aircraft.isNetPlayer())
            aircraft.FM.CT.wingControl = 0.0F;
    }

    public Loc requestCell(Aircraft aircraft)
    {
        if(!ship().isAlive())
            return invalidLoc;
        CellAirPlane cellairplane = aircraft.getCellAirPlane();
        Point3d point3d = reservePlaceForPlane(cellairplane, null);
        if(point3d != null)
        {
            skipCheck = true;
            double d = point3d.x;
            double d1 = point3d.y;
            double d2 = point3d.z;
            Loc loc = new Loc();
            loc.set(d, d1, d2 + (double)aircraft.FM.Gears.H, runway[0].getAzimut(), runway[0].getTangage(), runway[0].getKren());
            return loc;
        } else
        {
            return invalidLoc;
        }
    }

    private void deckCleared()
    {
        curPlaneShift = 0;
        CellAirField cellairfield = ship.getCellTO();
        cellairfield.freeCells();
    }

    public void setGuiCallback(GUINetClientDBrief guinetclientdbrief)
    {
        ui = guinetclientdbrief;
    }

    public void setClientLoc(Loc loc)
    {
        clientLoc = loc;
        boolean flag = isLocValid(loc);
        if(ui != null)
            ui.flyFromCarrier(flag);
    }

    private boolean isLocValid(Loc loc)
    {
        if(loc == null)
            return false;
        return (int)loc.getX() != 0 || (int)loc.getY() != 0 || (int)loc.getZ() != 0 || (int)loc.getTangage() != 0 || (int)loc.getKren() != 0;
    }

    public void startDeckOperations()
    {
        if(Mission.isDogfight() && ship.net.isMaster())
            interpPut(new DeckUpdater(), "deck_operations", Time.current(), null);
    }

    public static final double cellSize = 1D;
    private BigshipGeneric ship;
    private Loc runway[];
    public BornPlace bornPlace;
    private List lastCarrierUsers;
    private GUINetClientDBrief ui;
    private Loc clientLoc;
    private static Vector3d zeroSpeed = new Vector3d();
    private static final Loc invalidLoc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
    private static Vector3d v1 = new Vector3d();
    private Aircraft lastAddedAC;
    private CellAirPlane lastAddedCells;
    private int rnd1;
    private int rnd2;
    private Point_Stay ownDefaultStayPoints[][];
    private boolean skipCheck;
    private static float x[] = {
        -100F, -20F, -10F, 1000F, 3000F, 4000F, 3000F, 0.0F, -1000F
    };
    private static float xJ[] = {
        -100F, -20F, -10F, 2000F, 4500F, 6000F, 4500F, 0.0F, -1500F
    };
    private static float xFJ[] = {
        -100F, -15F, 0.0F, 3000F, 6000F, 8000F, 6000F, 0.0F, -2000F
    };
    private static float y[] = {
        0.0F, 0.0F, 0.0F, 0.0F, -500F, -1500F, -3000F, -3000F, -3000F
    };
    private static float yJ[] = {
        0.0F, 0.0F, 0.0F, 0.0F, -750F, -2300F, -4500F, -4500F, -4500F
    };
    private static float yFJ[] = {
        0.0F, 0.0F, 0.0F, 0.0F, -1000F, -3000F, -6000F, -6000F, -6000F
    };
    private static float z[] = {
        -4F, 5F, 5F, 150F, 450F, 500F, 500F, 500F, 500F
    };
    private static float zJ[] = {
        -4F, 5F, 5F, 230F, 450F, 500F, 500F, 600F, 600F
    };
    private static float zFJ[] = {
        -4F, 5F, 5F, 450F, 520F, 580F, 600F, 600F, 600F
    };
    private static float v[] = {
        0.0F, 80F, 100F, 180F, 250F, 270F, 280F, 300F, 300F
    };
    private static float vJ[] = {
        0.0F, 80F, 160F, 220F, 260F, 290F, 300F, 300F, 300F
    };
    private static float vFJ[] = {
        0.0F, 80F, 260F, 300F, 320F, 340F, 370F, 400F, 400F
    };
    private Loc tmpLoc;
    private Point3d tmpP3d;
    private Point3f tmpP3f;
    private Orient tmpOr;
    public int curPlaneShift;
    private int oldTickCounter;
    private int oldIdleTickCounter;
    private Loc r;
    private static Vector3d startSpeed = new Vector3d();
    private static Vector3d shipSpeed = new Vector3d();
    private static Class _clsBigArrestorPlane = null;
    private static CellAirPlane _cellBigArrestorPlane = null;
    private static HashMap _clsMapArrestorPlane = new HashMap();










}
