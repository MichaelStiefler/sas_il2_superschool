// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/12/2012 9:58:07 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Airdrome.java

package com.maddox.il2.ai.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.Time;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.ai.air:
//            Point_Any, Point_Stay, Point_Null, Pilot, 
//            Point_Runaway, Point_Taxi

public class Airdrome
{
    class AiardromeLine
    {

        int from;
        int to;

        AiardromeLine()
        {
        }
    }

    class AiardromePoint
    {

        Point_Any poi;
        int from;
        int poiCounter;

        AiardromePoint()
        {
        }
    }


    public Airdrome()
    {
        aPoints = new AiardromePoint[512];
        poiNum = 0;
        aLines = new AiardromeLine[512];
        lineNum = 0;
        airdromeWay = new Point_Any[512];
        testParkPoint = new Point3d();
        airdromeList = new ArrayList();
        for(int i = 0; i < 512; i++)
            aPoints[i] = new AiardromePoint();

        for(int j = 0; j < 512; j++)
            aLines[j] = new AiardromeLine();

        for(int k = 0; k < 512; k++)
            airdromeWay[k] = new Point_Any(0.0F, 0.0F);

    }

    private void freeStayPoint(Point_Any point_any)
    {
        if(point_any == null)
            return;
        if(point_any instanceof Point_Stay)
        {
            for(int i = 0; i < stayHold.length; i++)
            {
                for(int j = 0; j < stay[i].length - 1; j++)
                    if(point_any == stay[i][j])
                    {
                        stayHold[i] = false;
                        return;
                    }

            }

        }
    }

    public void findTheWay(Pilot pilot)
    {
        int i4 = 0;
        int j4 = 0;
        poiNum = 0;
        lineNum = 0;
        Vrun.x = (float)pilot.Vwld.x;
        Vrun.y = (float)pilot.Vwld.y;
        Point_Null point_null = new Point_Null((float)pilot.Loc.x, (float)pilot.Loc.y);
        int i3 = -1;
        int j3 = -1;
        int k3 = -1;
        int l3 = -1;
        float f2;
        float f3 = f2 = 2000F;
        for(int i = 0; i < runw.length; i++)
        {
            for(int k1 = 0; k1 < runw[i].length; k1++)
            {
                float f = point_null.distance(runw[i][k1]);
                if(f < f2)
                {
                    f2 = f;
                    i3 = i;
                    j3 = k1;
                }
                if(f >= f3)
                    continue;
                V_pn.sub(runw[i][k1], point_null);
                V_pn.normalize();
                Vrun.normalize();
                if(V_pn.dot(Vrun) > 0.9F)
                {
                    f3 = f;
                    k3 = i;
                    l3 = k1;
                }
            }

        }

        aPoints[poiNum].poiCounter = 0;
        if(k3 >= 0)
            aPoints[poiNum++].poi = runw[k3][l3];
        else
        if(i3 >= 0)
            aPoints[poiNum++].poi = runw[i3][j3];
        for(int j = 0; j < stay.length; j++)
        {
            if(stay[j].length < 2)
                continue;
            float f1 = point_null.distance(stay[j][1]);
            if(f1 >= 2000F || stayHold[j])
                continue;
            Engine.land();
            testParkPoint.set(stay[j][1].x, stay[j][1].y, Landscape.HQ_Air(stay[j][1].x, stay[j][1].y));
            Engine.collideEnv().getSphere(airdromeList, testParkPoint, 1.5F * pilot.actor.collisionR() + 10F);
            int k4 = airdromeList.size();
            airdromeList.clear();
            if(k4 == 0)
            {
                aLines[lineNum].to = poiNum;
                aPoints[poiNum].poiCounter = 777 + j;
                aPoints[poiNum++].poi = stay[j][1];
                aLines[lineNum++].from = poiNum;
                aPoints[poiNum].poiCounter = 255;
                aPoints[poiNum++].poi = stay[j][0];
            }
        }

        if(poiNum >= 3)
        {
            byte byte0 = -1;
            byte byte1 = -1;
label0:
            for(int k = 0; k < taxi.length; k++)
            {
                if(taxi[k].length < 2 || point_null.distance(taxi[k][0]) > 2000F)
                    continue;
                boolean flag = false;
                int j2 = 0;
                do
                {
                    if(j2 >= poiNum)
                        break;
                    if(aPoints[j2].poi.distance(taxi[k][0]) < 18F)
                    {
                        i4 = j2;
                        flag = true;
                        break;
                    }
                    j2++;
                } while(true);
                if(!flag)
                {
                    i4 = poiNum;
                    //TODO: Disabled to prevent rare freeze when AI landing on certain runways
                    //aPoints[poiNum].poiCounter = 255;
                    //aPoints[poiNum++].poi = taxi[k][0];
                }
                int l1 = 1;
                do
                {
                    if(l1 >= taxi[k].length)
                        continue label0;
                    boolean flag1 = false;
                    int k2 = 0;
                    do
                    {
                        if(k2 >= poiNum)
                            break;
                        if(aPoints[k2].poi.distance(taxi[k][l1]) < 18F)
                        {
                            j4 = k2;
                            flag1 = true;
                            break;
                        }
                        k2++;
                    } while(true);
                    if(!flag1)
                    {
                        j4 = poiNum;
                        aPoints[poiNum].poiCounter = 255;
                        aPoints[poiNum++].poi = taxi[k][l1];
                    }
                    aLines[lineNum].from = i4;
                    aLines[lineNum++].to = j4;
                    i4 = j4;
                    l1++;
                } while(true);
            }

            for(int l = 0; l < poiNum; l++)
            {
                Engine.land();
                testParkPoint.set(aPoints[l].poi.x, aPoints[l].poi.y, Landscape.HQ_Air(aPoints[l].poi.x, aPoints[l].poi.y));
                Engine.collideEnv().getSphere(airdromeList, testParkPoint, 1.2F * pilot.actor.collisionR() + 3F);
                int l4 = airdromeList.size();
                if(l4 == 1 && (airdromeList.get(0) instanceof Aircraft))
                    l4 = 0;
                airdromeList.clear();
                if(l4 > 0)
                    aPoints[l].poiCounter = -100;
            }

            int i5 = 0;
            do
            {
                if(i5 >= 255)
                    break;
                boolean flag2 = false;
                for(int i1 = 0; i1 < poiNum; i1++)
                {
                    if(aPoints[i1].poiCounter != i5)
                        continue;
                    for(int i2 = 0; i2 < lineNum; i2++)
                    {
                        int j5 = 0;
                        if(aLines[i2].to == i1)
                            j5 = aLines[i2].from;
                        if(aLines[i2].from == i1)
                            j5 = aLines[i2].to;
                        if(j5 == 0)
                            continue;
                        if(aPoints[j5].poiCounter >= 777)
                        {
                            aPoints[j5].from = i1;
                            stayHold[aPoints[j5].poiCounter - 777] = true;
                            int k5 = j5;
                            int l2;
                            for(l2 = 0; k5 > 0 || l2 > 128; k5 = aPoints[k5].from)
                                airdromeWay[l2++] = aPoints[k5].poi;

                            airdromeWay[l2++] = aPoints[0].poi;
                            pilot.airdromeWay = new Point_Any[l2];
                            for(int l5 = 0; l5 < l2; l5++)
                            {
                                pilot.airdromeWay[l5] = new Point_Any(0.0F, 0.0F);
                                pilot.airdromeWay[l5].set(airdromeWay[l2 - l5 - 1]);
                            }

                            return;
                        }
                        if(i5 + 1 < aPoints[j5].poiCounter)
                        {
                            aPoints[j5].poiCounter = i5 + 1;
                            aPoints[j5].from = i1;
                            flag2 = true;
                        }
                    }

                }

                if(!flag2)
                    break;
                i5++;
            } while(true);
        }
        World.cur();
        if(pilot.actor != World.getPlayerAircraft())
        {
            MsgDestroy.Post(Time.current() + 30000L, pilot.actor);
            pilot.setStationedOnGround(true);
        }
        if(poiNum > 0)
        {
            pilot.airdromeWay = new Point_Any[poiNum];
            for(int j1 = 0; j1 < poiNum; j1++)
                pilot.airdromeWay[j1] = aPoints[j1].poi;

        }
    }

    private Point_Any getNext(Pilot pilot)
    {
        if(pilot.airdromeWay == null)
            return null;
        if(pilot.airdromeWay.length == 0)
            return null;
        if(pilot.curAirdromePoi >= pilot.airdromeWay.length)
            return null;
        else
            return pilot.airdromeWay[pilot.curAirdromePoi++];
    }

    public void update(Pilot pilot, float f)
    {
        if(!pilot.isCapableOfTaxiing() || pilot.EI.getThrustOutput() < 0.01F)
        {
            pilot.TaxiMode = false;
            pilot.set_task(3);
            pilot.set_maneuver(49);
            pilot.AP.setStabAll(false);
            return;
        }
        if(pilot.AS.isPilotDead(0))
        {
            pilot.TaxiMode = false;
            pilot.setSpeedMode(8);
            pilot.smConstPower = 0.0F;
            if(Airport.distToNearestAirport(pilot.Loc) > 900D)
                ((Aircraft)pilot.actor).postEndAction(6000D, pilot.actor, 3, null);
            else
                MsgDestroy.Post(Time.current() + 0x493e0L, pilot.actor);
            return;
        }
        P.x = pilot.Loc.x;
        P.y = pilot.Loc.y;
        Vcur.x = (float)pilot.Vwld.x;
        Vcur.y = (float)pilot.Vwld.y;
        pilot.super_update(f);
        P.z = pilot.Loc.z;
        if(pilot.wayCurPos == null)
        {
            findTheWay(pilot);
            pilot.wayPrevPos = pilot.wayCurPos = getNext(pilot);
        }
        if(pilot.wayCurPos != null)
        {
            Point_Any point_any = pilot.wayCurPos;
            Point_Any point_any1 = pilot.wayPrevPos;
            Pcur.set((float)P.x, (float)P.y);
            float f1 = Pcur.distance(point_any);
            float f2 = Pcur.distance(point_any1);
            V_to.sub(point_any, Pcur);
            V_to.normalize();
            float f3 = 5F + 0.1F * f1;
            if(f3 > 12F)
                f3 = 12F;
            if(f3 > 0.9F * pilot.VminFLAPS)
                f3 = 0.9F * pilot.VminFLAPS;
            if(pilot.curAirdromePoi < pilot.airdromeWay.length && f1 < 15F || f1 < 4F)
            {
                f3 = 0.0F;
                Point_Any point_any2 = getNext(pilot);
                if(point_any2 == null)
                {
                    pilot.CT.setPowerControl(0.0F);
                    pilot.Loc.set(P);
                    if(pilot.finished)
                        return;
                    pilot.finished = true;
                    int i = 1000;
                    if(pilot.wayCurPos != null)
                        i = 0x249f00;
                    pilot.actor.collide(true);
                    pilot.Vwld.set(0.0D, 0.0D, 0.0D);
                    pilot.CT.setPowerControl(0.0F);
                    pilot.EI.setCurControlAll(true);
                    pilot.EI.setEngineStops();
                    pilot.TaxiMode = false;
                    World.cur();
                    if(pilot.actor != World.getPlayerAircraft())
                        if(Mission.isDogfight() && Main.cur().mission.zutiMisc_DespawnAIPlanesAfterLanding)
                            MsgDestroy.Post(Time.current() + 4000L, pilot.actor);
                        else
                            MsgDestroy.Post(Time.current() + (long)i, pilot.actor);
                    pilot.setStationedOnGround(true);
                    pilot.set_maneuver(1);
                    pilot.setSpeedMode(8);
                    return;
                }
                pilot.wayPrevPos = pilot.wayCurPos;
                pilot.wayCurPos = point_any2;
            }
            V_to.scale(f3);
            float f4 = 2.0F * f;
            Vdiff.set(V_to);
            Vdiff.sub(Vcur);
            float f5 = Vdiff.length();
            if(f5 > f4)
            {
                Vdiff.normalize();
                Vdiff.scale(f4);
            }
            Vcur.add(Vdiff);
            tmpOr.setYPR(pilot.RAD2DEG(Vcur.direction()), pilot.Or.getPitch(), 0.0F);
            pilot.Or.interpolate(tmpOr, 0.2F);
            pilot.Vwld.x = Vcur.x;
            pilot.Vwld.y = Vcur.y;
            P.x += Vcur.x * f;
            P.y += Vcur.y * f;
        } else
        {
            pilot.TaxiMode = false;
            pilot.wayPrevPos = pilot.wayCurPos = new Point_Null((float)pilot.Loc.x, (float)pilot.Loc.y);
        }
        pilot.Loc.set(P);
    }

    public static float CONN_DIST = 10F;
    public Point_Runaway runw[][];
    public Point_Taxi taxi[][];
    public Point_Stay stay[][];
    public boolean stayHold[];
    AiardromePoint aPoints[];
    int poiNum;
    AiardromeLine aLines[];
    int lineNum;
    Point_Any airdromeWay[];
    Point3d testParkPoint;
    ArrayList airdromeList;
    private static Point3d P = new Point3d();
    private static Point2f Pcur = new Point2f();
    private static Vector2f Vcur = new Vector2f();
    private static Vector2f V_to = new Vector2f();
    private static Vector2f Vdiff = new Vector2f();
    private static Vector2f V_pn = new Vector2f();
    private static Vector2f Vrun = new Vector2f();
    private static Orient tmpOr = new Orient();

}