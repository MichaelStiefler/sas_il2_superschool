/*Modified ChiefGround class for the SAS Engine Mod*/
/*By western, new GetNearestEnemy method with radar search and missile interceptable flags on 24th/Apr./2018*/
/*By western, Add sound preset of radar pulse wave on 23rd/Jun./2018*/

package com.maddox.il2.ai.ground;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2d;
import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.MsgDreamListener;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Finger;
import com.maddox.rts.Message;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.ai.ground:
//            RoadPath, UnitInPackedForm, UnitInterface, Predator, 
//            Prey, UnitSpawn, RoadPart, UnitMove, 
//            RoadSegment, UnitData, NearestEnemies, StaticObstacle

public class ChiefGround extends Chief
    implements MsgDreamListener
{
    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(waitTime > 0L && Time.tick() >= waitTime)
                waitTime = 0L;
            if(unitsPacked.size() > 0)
            {
                moveChiefPacked(Time.tickLenFs());
                return true;
            }
            if(--stateCountdown <= 0)
            {
                int i = ChiefGround.SecsToTicks(World.Rnd().nextFloat(300F, 500F));
                switch(curState)
                {
                default:
                    break;

                case 0:
                    stateCountdown = i;
                    break;

                case 1:
                    curState = 0;
                    stateCountdown = i;
                    reformIfNeed(false);
                    break;

                case 2:
                    if(shift_SwitchToBrakeWhenDone)
                    {
                        curState = 3;
                        stateCountdown = ChiefGround.SecsToTicks(World.Rnd().nextFloat(38F, 65F));
                    } else
                    {
                        curState = 0;
                        stateCountdown = i;
                    }
                    reformIfNeed(false);
                    break;

                case 3:
                    curState = 0;
                    stateCountdown = i;
                    reformIfNeed(true);
                    break;
                }
            }
            if(--posCountdown <= 0)
                recomputeAveragePosition();
            return true;
        }

        Move()
        {
        }
    }


    private static final void ERR_NO_UNITS(String s)
    {
        String s1 = "INTERNAL ERROR IN ChiefGround." + s + "(): No units";
        System.out.println(s1);
        throw new ActorException(s1);
    }

    private static final void ERR(String s)
    {
        String s1 = "INTERNAL ERROR IN ChiefGround: " + s;
        System.out.println(s1);
        throw new ActorException(s1);
    }

    private static final void ConstructorFailure()
    {
        throw new ActorException();
    }

    public boolean isPacked()
    {
        return unitsPacked == null || unitsPacked.size() > 0;
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    private void SetPosition(Point3d point3d, float f)
    {
        pos.getAbs(tmpp);
        pos.setAbs(point3d);
        estim_speed.sub(point3d, tmpp);
        if(f <= 0.0001F)
            estim_speed.set(0.0D, 0.0D, 0.0D);
        else
            estim_speed.scale(1.0D / (double)f);
    }

    public double getSpeed(Vector3d vector3d)
    {
        double d = estim_speed.length();
        if(vector3d == null)
            return d;
        vector3d.set(estim_speed);
        if(d <= 0.0001D)
            vector3d.set(0.0D, 0.0D, 0.0001D);
        return d;
    }

    public ChiefGround(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
    {
        tmpp = new Point3d();
        try
        {
            road = new RoadPath(sectfile1, s2);
            road.RegisterTravellerToBridges(this);
            setName(s);
            setArmy(i);
            chiefSeg = 0;
            chiefAlong = 0.0D;
            minSeg = -1;
            maxSeg = -1;
            waitTime = 0L;
            curForm = null;
            minGrabSeg = maxGrabSeg = -1;
            pos = new ActorPosMove(this);
            pos.setAbs(road.get(0).start);
            pos.reset();
            posCountdown = 0;
            estim_speed = new Vector3d(0.0D, 0.0D, 0.0D);
            int j = sectfile.sectionIndex(s1);
            int k = sectfile.vars(j);
            if(k <= 0)
                throw new ActorException("ChiefGround: Missing units");
            unitsPacked = new ArrayList();
            for(int l = 0; l < k; l++)
            {
                String s3 = sectfile.var(j, l);
                Object obj = Spawn.get(s3);
                if(obj == null)
                    throw new ActorException("ChiefGround: Unknown type of object (" + s3 + ")");
                int i1 = Finger.Int(s3);
                int j1 = 0;
                unitsPacked.add(new UnitInPackedForm(l, i1, j1));
            }

            withPreys = false;
            unpackUnits();
            recomputeAveragePosition();
            if(!interpEnd("move"))
                interpPut(new Move(), "move", Time.current(), null);
        }
        catch(Exception exception)
        {
            System.out.println("ChiefGround creation failure:");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            ConstructorFailure();
        }
    }

    public int getCodeOfBridgeSegment(UnitInterface unitinterface)
    {
        int i = unitinterface.GetUnitData().segmentIdx;
        return road.getCodeOfBridgeSegment(i);
    }

    public void BridgeSegmentDestroyed(int i, int j, Actor actor)
    {
        boolean flag = road.MarkDestroyedSegments(i, j);
        if(!flag)
            return;
        if(unitsPacked.size() > 0)
            return;
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("BridgeSegmentDestroyed");
        for(int k = 0; k < aobj.length; k++)
        {
            int l = ((UnitInterface)aobj[k]).GetUnitData().segmentIdx;
            if(road.segIsWrongOrDamaged(l))
                ((UnitInterface)aobj[k]).absoluteDeath(actor);
        }

    }

    private void recomputeAveragePosition()
    {
        if(unitsPacked.size() > 0)
            ERR("average position when PACKED");
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("recomputeAveragePosition");
        int i = aobj.length;
        int j = 10000;
        int k = -10000;
        for(int l = 0; l < i; l++)
        {
            int i1 = ((UnitInterface)aobj[l]).GetUnitData().segmentIdx;
            if(i1 < j)
                j = i1;
            if(i1 > k)
                k = i1;
        }

        Point3d point3d = new Point3d(((Actor)aobj[0]).pos.getAbsPoint());
        double d = World.land().HQ(point3d.x, point3d.y);
        if(point3d.z < d)
            point3d.z = d;
        SetPosition(point3d, 1.05F);
        posCountdown = SecsToTicks(World.Rnd().nextFloat(0.9F, 1.2F));
        road.unlockBridges(this, minGrabSeg, maxGrabSeg);
        minGrabSeg = j;
        maxGrabSeg = k;
        road.lockBridges(this, minGrabSeg, maxGrabSeg);
    }

    private void computePositionForPacked()
    {
        if(unitsPacked.size() > 0)
            ERR("advanced position when PACKED");
        Point3d point3d = pos.getAbsPoint();
        double d = 999999.90000000002D;
        chiefSeg = minSeg;
        for(int i = minSeg; i <= maxSeg; i++)
        {
            double d1 = road.get(i).computePosAlong(point3d);
            double d2 = road.get(i).computePosSide(point3d);
            double d3 = d1 * d1 + d2 * d2;
            if(d >= d3)
            {
                d = d3;
                chiefSeg = i;
            }
        }

        chiefAlong = road.get(chiefSeg).computePosAlong_Fit(point3d);
        SetPosition(road.get(chiefSeg).computePos_Fit(chiefAlong, 0.0D, 0.0F), 0.0F);
        posCountdown = SecsToTicks(World.Rnd().nextFloat(0.9F, 1.2F));
        road.unlockBridges(this, minGrabSeg, maxGrabSeg);
        minGrabSeg = maxGrabSeg = chiefSeg;
        road.lockBridges(this, minGrabSeg, maxGrabSeg);
    }

    private void recomputeChiefWaitTime(int i)
    {
        long l = road.getMaxWaitTime(i, i);
        long l1 = Time.tick();
        if(l > l1 && l > waitTime)
            waitTime = l;
    }

    private void recomputeMinMaxSegments()
    {
        if(unitsPacked.size() > 0)
            ERR("min/max seg when PACKED");
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("recomputeMinMaxSegments");
        int i = aobj.length;
        int j = 10000;
        int k = -10000;
        for(int l = 0; l < i; l++)
        {
            int i1 = ((UnitInterface)aobj[l]).GetUnitData().segmentIdx;
            if(i1 < j)
                j = i1;
            if(i1 > k)
                k = i1;
        }

        road.unlockBridges(this, minGrabSeg, maxGrabSeg);
        minGrabSeg = j;
        maxGrabSeg = k;
        road.lockBridges(this, minGrabSeg, maxGrabSeg);
        if(j == minSeg && k == maxSeg)
            return;
        long l1 = road.getMaxWaitTime(Math.min(j, minSeg), k);
        long l2 = Time.tick();
        if(l1 > l2 && l1 > waitTime)
            waitTime = l1;
        minSeg = j;
        maxSeg = k;
    }

    private void recomputeUnitsProperties_Packed()
    {
        int i = unitsPacked.size();
        if(i <= 0)
        {
            recomputeUnitsProperties();
            return;
        }
        groupSpeed = 100000F;
        maxSpace = -1F;
        weaponsMask = 0;
        hitbyMask = 0;
        for(int j = 0; j < i; j++)
        {
            UnitInPackedForm unitinpackedform = (UnitInPackedForm)unitsPacked.get(j);
            float f = unitinpackedform.SPEED_AVERAGE;
            if(f < groupSpeed)
                groupSpeed = f;
            f = unitinpackedform.BEST_SPACE;
            if(f > maxSpace)
                maxSpace = f;
            weaponsMask |= unitinpackedform.WEAPONS_MASK;
            hitbyMask |= unitinpackedform.HITBY_MASK;
        }

        if(groupSpeed < 0.001F || groupSpeed > 10000F)
            ERR("group speed is too small");
        if(maxSpace <= 0.01F)
            ERR("maxSpace is too small");
    }

    public void recomputeUnitsProperties()
    {
        if(unitsPacked.size() > 0)
        {
            recomputeUnitsProperties_Packed();
            return;
        }
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("recomputeUnitsProperties");
        int i = aobj.length;
        groupSpeed = 10000F;
        maxSpace = -1F;
        weaponsMask = 0;
        hitbyMask = 0;
        withPreys = false;
        for(int j = 0; j < i; j++)
        {
            UnitInterface unitinterface = (UnitInterface)aobj[j];
            float f = unitinterface.SpeedAverage();
            if(f < groupSpeed)
                groupSpeed = f;
            f = unitinterface.BestSpace();
            if(f > maxSpace)
                maxSpace = f;
            if(unitinterface instanceof Predator)
                weaponsMask |= ((Predator)unitinterface).WeaponsMask();
            if(unitinterface instanceof Prey)
            {
                hitbyMask |= ((Prey)unitinterface).HitbyMask();
                if(!(unitinterface instanceof Predator))
                    withPreys = true;
            }
        }

        if(groupSpeed <= 0.001F)
            ERR("group speed is too small");
        if(maxSpace <= 0.01F)
            ERR("maxSpace is too small");
    }

    private float computeMaxSpace(ArrayList arraylist, int i, int j)
    {
        float f = -1F;
        while(j-- > 0) 
        {
            float f1 = ((UnitInterface)arraylist.get(i++)).BestSpace();
            if(f1 > f)
                f = f1;
        }
        if(f <= 0.01F)
            ERR("maxSpace is too small");
        return f;
    }

    private float computeMaxSpace(Object aobj[], int i, int j)
    {
        float f = -1F;
        while(j-- > 0) 
        {
            float f1 = ((UnitInterface)aobj[i++]).BestSpace();
            if(f1 > f)
                f = f1;
        }
        if(f <= 0.01F)
            ERR("maxSpace is too small");
        return f;
    }

    private static final int SecsToTicks(float f)
    {
        int i = (int)(0.5D + (double)(f / Time.tickLenFs()));
        return i > 0 ? i : 0;
    }

    public Actor GetNearestEnemy(Point3d point3d, double maxDistance, int weaponMask, float limitSpeed)
    {
        return this.GetNearestEnemy(point3d, maxDistance, weaponMask, limitSpeed, false, false, null, null);
    }

    // By western, expanded for flags Intercept missiles and Radar use
    public Actor GetNearestEnemy(Point3d point3d, double maxDistance, int weaponMask, float limitSpeed, boolean bInterceptMissile, boolean bUseRadar, String sPresetPW, Actor owner)
    {
        if(unitsPacked.size() > 0)
            return null;
        if(limitSpeed < 0.0F)
            NearestEnemies.set(weaponMask, bInterceptMissile, bUseRadar, sPresetPW);
        else
            NearestEnemies.set(weaponMask, -9999.9F, limitSpeed, bInterceptMissile, bUseRadar, sPresetPW);
        Actor actor = NearestEnemies.getAFoundEnemy(point3d, maxDistance, getArmy(), owner);
        if(actor == null)
            return null;
        if(!(actor instanceof Prey))
        {
            System.out.println("chiefg: nearest enemies: non-Prey");
            return null;
        }
        switch(curState)
        {
        case 2:
        case 3:
        default:
            break;

        case 0:
            if(!withPreys)
            {
                curState = 1;
                stateCountdown = SecsToTicks(World.Rnd().nextFloat(50F, 90F));
                reformIfNeed(false);
            }
            break;

        case 1:
            stateCountdown = SecsToTicks(World.Rnd().nextFloat(50F, 90F));
            break;
        }
        return actor;
    }

    public void Detach(Actor actor, Actor actor1)
    {
        if(unitsPacked.size() > 0)
            ERR("Detaching when PACKED");
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("Detach");
        int i = aobj.length;
        int j;
        for(j = 0; j < i; j++)
            if(actor == (Actor)aobj[j])
                break;

        if(j >= i)
            ERR("Detaching unknown unit");
        UnitInterface unitinterface = (UnitInterface)actor;
        UnitData unitdata = unitinterface.GetUnitData();
        if(j < i - 1)
        {
            Actor actor2 = (Actor)aobj[j + 1];
            UnitInterface unitinterface1 = (UnitInterface)actor2;
            UnitData unitdata1 = unitinterface1.GetUnitData();
            unitdata1.leader = unitdata.leader;
        }
        unitdata.leader = null;
        actor.setOwner(null);
        if(i > 1)
        {
            recomputeUnitsProperties();
            recomputeMinMaxSegments();
            reformIfNeed(true);
            recomputeAveragePosition();
        }
        if(i <= 1)
        {
            road.UnregisterTravellerFromBridges(this);
            road.unlockBridges(this, minGrabSeg, maxGrabSeg);
            minGrabSeg = maxGrabSeg = -1;
            World.onActorDied(this, actor1);
            destroy();
        }
    }

    public void msgDream(boolean flag)
    {
        boolean flag1 = unitsPacked.size() > 0;
        if(flag)
        {
            if(!flag1)
                ERR("Wakeup out of place");
            unpackUnits();
        } else
        {
            if(flag1)
                ERR("Sleeping out of place");
            packUnits();
        }
    }

    public void packUnits()
    {
        if(unitsPacked.size() > 0)
            return;
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("packUnits");
        computePositionForPacked();
        int i = aobj.length;
        for(int j = 0; j < i; j++)
        {
            unitsPacked.add(((UnitInterface)aobj[j]).Pack());
            ((Actor)aobj[j]).destroy();
        }

        recomputeUnitsProperties();
    }

    public void unpackUnits()
    {
        int i = unitsPacked.size();
        if(i <= 0)
            return;
        if(getOwnerAttached().length > 0)
            ERR("unpack units");
        for(int j = 0; j < i; j++)
        {
            int k = ((UnitInPackedForm)unitsPacked.get(j)).CodeName();
            int l = ((UnitInPackedForm)unitsPacked.get(j)).CodeType();
            Object obj = Spawn.get(l);
            int i1 = ((UnitInPackedForm)unitsPacked.get(j)).State();
            ((UnitSpawn)obj).unitSpawn(k, i1, this);
        }

        unitsPacked.clear();
        curState = 0;
        stateCountdown = 0;
        road.unlockBridges(this, minGrabSeg, maxGrabSeg);
        minGrabSeg = maxGrabSeg = -1;
        recomputeUnitsProperties();
        formUnitsAfterUnpacking();
    }

    private static void AutoChooseFormation(int i, boolean flag, int j, float f, double d, int ai[])
    {
        if(j <= 0)
            return;
        if(i == 2 || i == 3)
        {
            ai[0] = flag ? 2 : 3;
            ai[1] = 1;
            ai[2] = j;
            return;
        }
        ai[0] = 0;
        int k = (int)(d / (double)f);
        if(k <= 0)
            k = 1;
        if(k > j)
            k = j;
        if(k <= 1)
        {
            ai[1] = 1;
            ai[2] = j;
            return;
        }
        if(i == 0)
        {
            ai[1] = 1;
            ai[2] = j;
            return;
        }
        ai[0] = 1;
        if(k >= 3 && j < ((k - 1) + k) - 1)
        {
            k /= 2;
            if(k < 2)
                k = 2;
        }
        ai[1] = k;
        int l = 2 * k - 1;
        int i1 = ((j + l) - 1) / l;
        ai[2] = i1 * 2;
        if(((j + l) - 1) % l < k - 1)
            ai[2]--;
    }

    private void formUnitsAfterUnpacking()
    {
        if(unitsPacked.size() > 0)
            return;
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("formUnitsAfterUnpacking");
        float f = (float)(aobj.length - 1) * maxSpace;
        double d = road.get(chiefSeg).computePosAlong_Fit(pos.getAbsPoint());
        RoadPart roadpart = new RoadPart();
        road.FindFreeSpace(f, chiefSeg, d, roadpart);
        int ai[] = new int[3];
        AutoChooseFormation(curState, shift_ToRightSide, aobj.length, maxSpace, road.ComputeMinRoadWidth(roadpart.begseg, roadpart.endseg), ai);
        curForm = ai;
        f = (float)(ai[2] - 1) * maxSpace;
        road.FindFreeSpace(f, chiefSeg, d, roadpart);
//        double d1 = 1.0D;
        int i = roadpart.begseg;
        double d2 = roadpart.begt;
        int j = 0;
        float f1 = 0.0F;
        for(int k = 0; k < ai[2]; k++)
        {
            int l = ai[1];
            if((k & 1) == 0 && ai[0] == 1)
                l--;
            int i1 = l;
            if(j + i1 > aobj.length)
                i1 = aobj.length - j;
            float f2 = computeMaxSpace(aobj, j, i1);
            float f3 = 0.0F;
            if(k > 0)
            {
                f3 = Math.max(f1, f2);
                double d3 = f3;
                d2 -= d3;
                while(d2 < 0.0D) 
                {
                    double d4 = -d2;
                    d2 = 0.0D;
                    if(!road.segIsWrongOrDamaged(i - 1))
                    {
                        i--;
                        d2 = road.get(i).length2D - d4;
                        continue;
                    }
                    d2 = -1D;
                    break;
                }
                if(d2 < 0.0D)
                    break;
            }
            for(int j1 = 0; j1 < i1; j1++)
            {
                UnitData unitdata = ((UnitInterface)aobj[j]).GetUnitData();
                unitdata.segmentIdx = i;
                unitdata.leaderDist = f3;
                if(j == 0)
                    unitdata.leader = null;
                else
                if(k == 0)
                {
                    unitdata.leader = (Actor)aobj[0];
                    unitdata.leaderDist = 0.0F;
                } else
                if(ai[0] == 0 || (k & 1) == 0 || j1 > 0)
                    unitdata.leader = (Actor)aobj[j - ai[1]];
                else
                    unitdata.leader = (Actor)aobj[(j - ai[1]) + 1];
                float f4 = ai[0] != 0 ? maxSpace : f2;
                float f5 = (float)(ai[1] - 1) * f4;
                if(ai[0] == 1 && (k & 1) == 0)
                    f5 -= f4;
                unitdata.sideOffset = -f5 / 2.0F + (float)j1 * f4;
                Actor actor = (Actor)aobj[j];
                Point3d point3d = road.get(i).computePos_Fit(d2, unitdata.sideOffset, actor.collisionR());
                point3d.z += ((UnitInterface)actor).HeightAboveLandSurface();
                actor.pos.setAbs(point3d);
                Vector3f vector3f = new Vector3f();
                if(road.get(i).IsLandAligned())
                    Engine.land().N(point3d.x, point3d.y, vector3f);
                else
                    vector3f.set(road.get(i).normal);
                Orient orient = new Orient();
                orient.setYPR(road.get(i).yaw, 0.0F, 0.0F);
                orient.orient(vector3f);
                actor.pos.setAbs(orient);
                actor.pos.reset();
             // TODO: HSFX Triggers Backport by Whistler +++
                if(!World.cur().triggersGuard.listTriggerChiefSol.contains(name()))
             // TODO: HSFX Triggers Backport by Whistler ---
                    ((UnitInterface)aobj[j]).startMove();
                j++;
            }

            if(j >= aobj.length)
                break;
            f1 = f2;
        }

        if(j <= 0)
            ERR("No room to place units");
        for(; j < aobj.length; j++)
            ((Actor)aobj[j]).destroy();

        recomputeMinMaxSegments();
    }

 // TODO: HSFX Triggers Backport by Whistler +++
    public void startMove()
    {
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            return;
        for(int j = 0; j < aobj.length; j++)
            ((UnitInterface)aobj[j]).startMove();
    }

 // TODO: HSFX Triggers Backport by Whistler ---

    private void reformForSHIFT(Object aobj[], float f, boolean flag)
    {
        if(aobj == null)
            return;
        ArrayList arraylist = new ArrayList(aobj.length);
        for(int i = 0; i < aobj.length; i++)
            arraylist.add(aobj[i]);

        Collections.sort(arraylist, new Comparator() {

            public int compare(Object obj, Object obj1)
            {
                UnitData unitdata1 = ((UnitInterface)obj).GetUnitData();
                UnitData unitdata2 = ((UnitInterface)obj1).GetUnitData();
                RoadSegment roadsegment = road.get(unitdata1.segmentIdx);
                RoadSegment roadsegment1 = road.get(unitdata2.segmentIdx);
                double d = (roadsegment1.length2Dallprev + roadsegment1.computePosAlong(((Actor)obj1).pos.getAbsPoint())) - roadsegment.length2Dallprev - roadsegment.computePosAlong(((Actor)obj).pos.getAbsPoint());
                return d != 0.0D ? d <= 0.0D ? -1 : 1 : 0;
            }

        } );

        float f1 = flag ? 5000F : -5000F;
        for(int j = 0; j < arraylist.size(); j++)
        {
            UnitData unitdata = ((UnitInterface)arraylist.get(j)).GetUnitData();
            if(j == 0)
            {
                unitdata.leaderDist = 0.0F;
                unitdata.leader = null;
            } else
            {
                unitdata.leaderDist = f;
                unitdata.leader = (Actor)arraylist.get(j - 1);
            }
            unitdata.sideOffset = f1;
        }

        for(int k = 0; k < aobj.length; k++)
            ((UnitInterface)aobj[k]).forceReaskMove();

    }

    private void reform(Object aobj[], int ai[])
    {
        if(aobj == null)
            return;
        if(ai[0] == 2 || ai[0] == 3)
        {
            reformForSHIFT(aobj, maxSpace, ai[0] == 2);
            return;
        }
        ArrayList arraylist = new ArrayList(aobj.length);
        for(int i = 0; i < aobj.length; i++)
            arraylist.add(aobj[i]);

        Collections.sort(arraylist, new Comparator() {

            public int compare(Object obj, Object obj1)
            {
                UnitData unitdata2 = ((UnitInterface)obj).GetUnitData();
                UnitData unitdata3 = ((UnitInterface)obj1).GetUnitData();
                RoadSegment roadsegment = road.get(unitdata2.segmentIdx);
                RoadSegment roadsegment1 = road.get(unitdata3.segmentIdx);
                double d = (roadsegment1.length2Dallprev + roadsegment1.computePosAlong(((Actor)obj1).pos.getAbsPoint())) - roadsegment.length2Dallprev - roadsegment.computePosAlong(((Actor)obj).pos.getAbsPoint());
                return d != 0.0D ? d <= 0.0D ? -1 : 1 : 0;
            }

        } );

        for(int j = 0; j < arraylist.size(); j++)
        {
            UnitData unitdata = ((UnitInterface)arraylist.get(j)).GetUnitData();
            if(ai[0] == 0)
            {
                unitdata.leaderDist = j / ai[1];
            } else
            {
                int i1 = j / (ai[1] * 2 - 1);
                i1 = j % (ai[1] * 2 - 1) >= ai[1] - 1 ? i1 * 2 + 1 : i1 * 2;
                unitdata.leaderDist = i1;
            }
        }

        Collections.sort(arraylist, new Comparator() {

            public int compare(Object obj, Object obj1)
            {
                UnitData unitdata2 = ((UnitInterface)obj).GetUnitData();
                UnitData unitdata3 = ((UnitInterface)obj1).GetUnitData();
                if(unitdata2.leaderDist != unitdata3.leaderDist)
                {
                    double d = unitdata2.leaderDist - unitdata3.leaderDist;
                    return d != 0.0D ? d <= 0.0D ? -1 : 1 : 0;
                } else
                {
                    RoadSegment roadsegment = road.get(unitdata2.segmentIdx);
                    RoadSegment roadsegment1 = road.get(unitdata3.segmentIdx);
                    double d1 = roadsegment.computePosSide(((Actor)obj).pos.getAbsPoint()) - roadsegment1.computePosSide(((Actor)obj1).pos.getAbsPoint());
                    return d1 != 0.0D ? d1 <= 0.0D ? -1 : 1 : 0;
                }
            }

        } );

        int k = 0;
        float f = 0.0F;
        for(int j1 = 0; j1 < ai[2]; j1++)
        {
            int k1 = ai[1];
            if((j1 & 1) == 0 && ai[0] == 1)
                k1--;
            int l1 = k1;
            if(k + l1 > arraylist.size())
                l1 = arraylist.size() - k;
            float f1 = computeMaxSpace(arraylist, k, l1);
            float f2 = 0.0F;
            if(j1 > 0)
                f2 = Math.max(f, f1);
            for(int i2 = 0; i2 < l1; i2++)
            {
                UnitData unitdata1 = ((UnitInterface)arraylist.get(k)).GetUnitData();
                unitdata1.leaderDist = f2;
                int j2;
                if(k == 0)
                    j2 = -1;
                else
                if(j1 == 0)
                {
                    j2 = 0;
                    unitdata1.leaderDist = 0.0F;
                } else
                if(ai[0] == 0 || (j1 & 1) == 0 || i2 > 0)
                    j2 = k - ai[1];
                else
                    j2 = (k - ai[1]) + 1;
                unitdata1.leader = j2 >= 0 ? (Actor)arraylist.get(j2) : null;
                float f3 = ai[0] != 0 ? maxSpace : f1;
                float f4 = (float)(ai[1] - 1) * f3;
                if(ai[0] == 1 && (j1 & 1) == 0)
                    f4 -= f3;
                unitdata1.sideOffset = -f4 / 2.0F + (float)i2 * f3;
                k++;
            }

            if(k >= arraylist.size())
                break;
            f = f1;
        }

        for(int l = 0; l < aobj.length; l++)
            ((UnitInterface)aobj[l]).forceReaskMove();

    }

    private int[] FindBestFormation(int i)
    {
//        float f = (float)(i - 1) * maxSpace;
        int ai[] = new int[3];
        AutoChooseFormation(curState, shift_ToRightSide, i, maxSpace, road.ComputeMinRoadWidth(maxSeg, minSeg), ai);
        return ai;
    }

    private void reformIfNeed(boolean flag)
    {
        Object aobj[] = getOwnerAttached();
        if(aobj.length <= 0)
            ERR_NO_UNITS("reformIfNeed");
        if(flag)
        {
            curForm = FindBestFormation(aobj.length);
            reform(aobj, curForm);
        } else
        {
            int ai[] = FindBestFormation(aobj.length);
            if(ai[0] != curForm[0] || ai[1] != curForm[1])
            {
                curForm = ai;
                reform(aobj, curForm);
            }
        }
    }

    public void CollisionOccured(UnitInterface unitinterface, Actor actor)
    {
        if(!(actor instanceof UnitInterface))
            return;
        if(actor.getArmy() != getArmy() && actor.isAlive() && actor.getArmy() != 0 && isAlive() && getArmy() != 0)
            return;
        Actor actor1 = actor.getOwner();
        if(actor1 == null)
            return;
        if(actor1 == this)
            return;
        if(!(actor1 instanceof ChiefGround))
            throw new ActorException("ChiefGround: ground unit with wrong owner");
        ChiefGround chiefground = (ChiefGround)actor1;
        UnitInterface unitinterface1 = (UnitInterface)actor;
        Vector2d vector2d = road.get(unitinterface.GetUnitData().segmentIdx).dir2D;
        Vector2d vector2d1 = chiefground.road.get(unitinterface1.GetUnitData().segmentIdx).dir2D;
        boolean flag = vector2d.x * vector2d1.x + vector2d.y * vector2d1.y < 0.0D;
        boolean flag1 = false;
        if(chiefground.waitTime < waitTime)
            flag1 = true;
        else
        if(chiefground.waitTime == waitTime)
            if(chiefground.groupSpeed > groupSpeed)
                flag1 = true;
            else
            if(chiefground.groupSpeed == groupSpeed && chiefground.name().compareTo(name()) < 0)
                flag1 = true;
        curState = chiefground.curState = 2;
        int i = SecsToTicks(World.Rnd().nextFloat(47F, 72F));
        int j = SecsToTicks(World.Rnd().nextFloat(47F, 72F));
        if(flag)
        {
            shift_ToRightSide = true;
            shift_SwitchToBrakeWhenDone = false;
            chiefground.shift_ToRightSide = true;
            chiefground.shift_SwitchToBrakeWhenDone = false;
            stateCountdown = i;
            chiefground.stateCountdown = j;
        } else
        {
            shift_ToRightSide = flag1;
            shift_SwitchToBrakeWhenDone = flag1;
            chiefground.shift_ToRightSide = !flag1;
            chiefground.shift_SwitchToBrakeWhenDone = !flag1;
            int k = SecsToTicks(World.Rnd().nextFloat(18F, 25F));
            if(flag1)
            {
                chiefground.stateCountdown = i;
                stateCountdown = chiefground.stateCountdown - k;
            } else
            {
                stateCountdown = i;
                chiefground.stateCountdown = stateCountdown - k;
            }
        }
        reformIfNeed(false);
        chiefground.reformIfNeed(false);
    }

    public double computePosAlong_Fit(int i, Point3d point3d)
    {
        return road.get(i).computePosAlong_Fit(point3d);
    }

    public double computePosAlong(int i, Point3d point3d)
    {
        return road.get(i).computePosAlong(point3d);
    }

    public double computePosSide(int i, Point3d point3d)
    {
        return road.get(i).computePosSide(point3d);
    }

    private static final float distance2D(Point3d point3d, Point3d point3d1)
    {
        return (float)Math.sqrt((point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y));
    }

    private static boolean intersectLineCircle(float f, float f1, float f2, float f3, float f4, float f5, float f6)
    {
        float f7 = f6 * f6;
        float f8 = f2 - f;
        float f9 = f3 - f1;
        float f10 = f8 * f8 + f9 * f9;
        float f11 = ((f4 - f) * f8 + (f5 - f1) * f9) / f10;
        if(f11 >= 0.0F && f11 <= 1.0F)
        {
            float f12 = f + f11 * f8;
            float f14 = f1 + f11 * f9;
            float f16 = (f12 - f4) * (f12 - f4) + (f14 - f5) * (f14 - f5);
            return f7 - f16 >= 0.0F;
        } else
        {
            float f13 = (f2 - f4) * (f2 - f4) + (f3 - f5) * (f3 - f5);
            float f15 = (f - f4) * (f - f4) + (f1 - f5) * (f1 - f5);
            return f13 <= f7 || f15 <= f7;
        }
    }

    private static boolean intersectCircle(Point3d point3d, double d, Point3d point3d1, double d1, float f)
    {
        float f1 = (float)(point3d1.x + d1 * (double)Geom.cosDeg(f));
        float f2 = (float)(point3d1.y + d1 * (double)Geom.sinDeg(f));
        return intersectLineCircle((float)point3d1.x, (float)point3d1.y, f1, f2, (float)point3d.x, (float)point3d.y, (float)d);
    }

    private UnitMove createStay_UnitMove(float f, int i)
    {
        RoadSegment roadsegment = road.get(i);
        if(roadsegment.IsLandAligned())
            return new UnitMove(f, new Vector3f(0.0F, 0.0F, -1F));
        else
            return new UnitMove(f, roadsegment.normal);
    }

    private boolean cantEnterIntoSegment_checkComplete(int i)
    {
        if(i >= road.nsegments() - 1)
        {
            boolean flag = waitTime > 0L && i > maxSeg;
            if(!flag)
                World.onTaskComplete(this);
            return true;
        } else
        {
            return !road.segIsPassableBy(i, this) || waitTime > 0L && i > maxSeg;
        }
    }

    private boolean cantEnterIntoSegment(int i)
    {
        return i >= road.nsegments() - 1 || !road.segIsPassableBy(i, this) || waitTime > 0L && i > maxSeg;
    }

    private boolean cantEnterIntoSegmentPacked_checkComplete(int i)
    {
        if(i >= road.nsegments() - 1)
        {
            boolean flag = waitTime > 0L;
            if(!flag)
                World.onTaskComplete(this);
            return true;
        } else
        {
            return !road.segIsPassableBy(i, this) || waitTime > 0L;
        }
    }

    private void moveChiefPacked(float f)
    {
        RoadSegment roadsegment = road.get(chiefSeg);
        double d;
        for(d = groupSpeed * f; chiefAlong + d >= roadsegment.length2D;)
        {
            if(cantEnterIntoSegmentPacked_checkComplete(chiefSeg + 1))
            {
                chiefAlong = roadsegment.length2D;
                d = 0.0D;
                break;
            }
            chiefAlong = (chiefAlong + d) - roadsegment.length2D;
            chiefSeg++;
            recomputeChiefWaitTime(chiefSeg);
            roadsegment = road.get(chiefSeg);
            road.unlockBridges(this, minGrabSeg, maxGrabSeg);
            minGrabSeg = maxGrabSeg = chiefSeg;
            road.lockBridges(this, minGrabSeg, maxGrabSeg);
        }

        chiefAlong += d;
        SetPosition(roadsegment.computePos_Fit(chiefAlong, 0.0D, 0.0F), f);
    }

    public UnitMove AskMoveCommand(Actor actor, Point3d point3d, StaticObstacle staticobstacle)
    {
        UnitInterface unitinterface = (UnitInterface)actor;
        UnitData unitdata = unitinterface.GetUnitData();
        boolean flag = point3d != null && point3d.z < 0.0D;
        boolean flag1 = point3d != null && point3d.z > 0.0D;
        if((curState == 2 || curState == 3) && flag1)
            flag1 = false;
        RoadSegment roadsegment = road.get(unitdata.segmentIdx);
        Point3d point3d1 = new Point3d(actor.pos.getAbsPoint());
        if(flag)
        {
            int i = 0;
            double d = roadsegment.computePosAlong(point3d1);
            if(d >= 0.0D)
                d = d <= roadsegment.length2D ? 0.0D : d - roadsegment.length2D;
            do
            {
                if(d > 0.0D)
                    cantEnterIntoSegment_checkComplete(unitdata.segmentIdx + 1);
                if(i >= 0)
                    if(cantEnterIntoSegment(unitdata.segmentIdx + 1))
                    {
                        if(i != 0)
                            break;
                    } else
                    {
                        RoadSegment roadsegment1 = road.get(unitdata.segmentIdx + 1);
                        double d5 = roadsegment1.computePosAlong(point3d1);
                        if(d5 >= 0.0D)
                            d5 = d5 <= roadsegment1.length2D ? 0.0D : d5 - roadsegment1.length2D;
                        if(Math.abs(d5) < Math.abs(d))
                        {
                            i = 1;
                            d = d5;
                            unitdata.segmentIdx++;
                        } else
                        if(i != 0)
                            break;
                    }
                if(i > 0)
                    continue;
                if(cantEnterIntoSegment(unitdata.segmentIdx - 1))
                    break;
                RoadSegment roadsegment2 = road.get(unitdata.segmentIdx - 1);
                double d6 = roadsegment2.computePosAlong(point3d1);
                if(d6 >= 0.0D)
                    d6 = d6 <= roadsegment2.length2D ? 0.0D : d6 - roadsegment2.length2D;
                if(Math.abs(d6) >= Math.abs(d))
                    break;
                i = -1;
                d = d6;
                unitdata.segmentIdx--;
            } while(true);
            if(i != 0)
            {
                roadsegment = road.get(unitdata.segmentIdx);
                recomputeMinMaxSegments();
                reformIfNeed(false);
            }
        } else
        {
            boolean flag2 = false;
            UnitMove unitmove = null;
            for(; roadsegment.computePosAlong(actor.pos.getAbsPoint()) >= roadsegment.length2D - 1.0D; roadsegment = road.get(unitdata.segmentIdx))
            {
                if(cantEnterIntoSegment_checkComplete(unitdata.segmentIdx + 1))
                {
                    unitmove = createStay_UnitMove(5F, unitdata.segmentIdx);
                    break;
                }
                flag2 = true;
                unitdata.segmentIdx++;
                if(unitdata.segmentIdx > maxSeg || unitdata.segmentIdx - 1 <= minSeg)
                    recomputeMinMaxSegments();
            }

            if(flag2)
                reformIfNeed(false);
            if(unitmove != null)
                return unitmove;
        }
        Vector3d vector3d = new Vector3d(roadsegment.dir2D.x, roadsegment.dir2D.y, 0.0D);
        if(!flag && staticobstacle.updateState())
        {
            double d1 = roadsegment.computePosAlong(point3d1);
            double d4 = road.ComputeSignedDistAlong(unitdata.segmentIdx, d1, staticobstacle.segIdx, staticobstacle.along);
            double d9 = actor.collisionR();
            if(d9 <= 0.0D)
                d9 = 0.0D;
            d9 += staticobstacle.R;
            if(d9 <= 0.0D)
                d9 = 2D;
            double d12 = d4;
            if(d4 >= 0.0D)
            {
                d12 -= d9;
                if(d12 <= 0.0D)
                    d12 = 0.0D;
            } else
            {
                d12 += d9;
                if(d12 >= 0.0D)
                    d12 = 0.0D;
            }
            if(Math.abs(d4) <= 130D);
            if(d12 < -Math.max(7D * Math.abs(d9), 120D))
                staticobstacle.clear();
            else
            if(d4 <= 0.0D)
            {
                if(d12 >= -1.5D)
                    vector3d.z = 2.5D;
            } else
            if(d12 > Math.max(3D * d9, 20D))
            {
                vector3d.z = Math.max(d9, 3D);
            } else
            {
                double d14 = 0.5D * d9;
                if(d14 < 0.01D)
                {
                    vector3d.z = 1.0D;
                } else
                {
                    Vector2f vector2f = new Vector2f((float)(staticobstacle.pos.x - point3d1.x), (float)(staticobstacle.pos.y - point3d1.y));
                    if(vector2f.length() < 0.1F)
                    {
                        vector3d.z = 4D;
                    } else
                    {
                        vector2f.normalize();
                        AnglesFork anglesfork = new AnglesFork();
                        anglesfork.setSrcRad((float)Math.atan2(vector2f.y, vector2f.x));
                        anglesfork.setDstDeg(anglesfork.getSrcDeg() + (staticobstacle.side <= 0.0D ? -90F : 90F));
                        double d17 = d9 + 0.5D;
                        if(!intersectCircle(staticobstacle.pos, d17, point3d1, d14, anglesfork.getSrcDeg()))
                        {
                            float f6 = anglesfork.getSrcDeg();
                            vector3d.set(Geom.cosDeg(f6), Geom.sinDeg(f6), d14);
                        } else
                        if(intersectCircle(staticobstacle.pos, d17, point3d1, d14, anglesfork.getDstDeg()))
                        {
                            vector3d.z = 2D;
                        } else
                        {
                            for(int j = 0; j < 6; j++)
                            {
                                float f7 = anglesfork.getDeg(0.5F);
                                if(intersectCircle(staticobstacle.pos, d17, point3d1, d14, f7))
                                    anglesfork.setSrcDeg(f7);
                                else
                                    anglesfork.setDstDeg(f7);
                            }

                            float f8 = anglesfork.getDstDeg();
                            vector3d.set(Geom.cosDeg(f8), Geom.sinDeg(f8), d14);
                        }
                    }
                }
            }
        }

        if(vector3d.z > 0.0D)
        {
            flag1 = false;
            flag = true;
            vector3d.x *= vector3d.z;
            vector3d.y *= vector3d.z;
            point3d = new Point3d(vector3d);
            point3d.z = -1D;
        }
        if(flag)
        {
            Point3d point3d2 = new Point3d(point3d1);
            point3d2.x += point3d.x;
            point3d2.y += point3d.y;
            double d2 = roadsegment.computePosAlong(point3d2);
            double d7 = roadsegment.computePosSide(point3d2);
            if(d2 >= roadsegment.length2D - 0.2D)
            {
                point3d2 = roadsegment.computePos_Fit(roadsegment.length2D, d7, actor.collisionR());
                point3d2.x += roadsegment.dir2D.x * 0.2D;
                point3d2.y += roadsegment.dir2D.y * 0.2D;
            } else
            {
                point3d2 = roadsegment.computePos_Fit(d2, d7, actor.collisionR());
            }
            double d10 = distance2D(point3d2, point3d1);
            float f4 = (float)d10 / groupSpeed;
            Vector3f vector3f = road.get(unitdata.segmentIdx).IsLandAligned() ? new Vector3f(0.0F, 0.0F, -1F) : road.get(unitdata.segmentIdx).normal;
            return new UnitMove(unitinterface.HeightAboveLandSurface(), point3d2, f4, vector3f, groupSpeed);
        }
        if(curState == 3)
            return createStay_UnitMove(5F, unitdata.segmentIdx);
        if(unitdata.leader == null)
        {
            float f = unitinterface.CommandInterval() * World.Rnd().nextFloat(0.8F, 1.0F);
            double d3 = groupSpeed * f;
            double d8 = d3 + roadsegment.computePosAlong(point3d1);
            double d11 = unitdata.sideOffset;
            if(flag1)
            {
                d8 += point3d.x;
                d11 += point3d.y;
            }
            Point3d point3d4;
            if(d8 >= roadsegment.length2D - 0.2D)
            {
                point3d4 = roadsegment.computePos_Fit(roadsegment.length2D, d11, actor.collisionR());
                point3d4.x += roadsegment.dir2D.x * 0.2D;
                point3d4.y += roadsegment.dir2D.y * 0.2D;
            } else
            {
                point3d4 = roadsegment.computePos_Fit(d8, d11, actor.collisionR());
            }
            d3 = distance2D(point3d4, point3d1);
            float f5 = (float)d3 / groupSpeed;
            Vector3f vector3f1 = road.get(unitdata.segmentIdx).IsLandAligned() ? new Vector3f(0.0F, 0.0F, -1F) : road.get(unitdata.segmentIdx).normal;
            return new UnitMove(unitinterface.HeightAboveLandSurface(), point3d4, f5, vector3f1, groupSpeed);
        }
        Actor actor1 = unitdata.leader;
        UnitData unitdata1 = ((UnitInterface)actor1).GetUnitData();
        RoadSegment roadsegment3 = road.get(unitdata1.segmentIdx);
        Point3d point3d3 = new Point3d();
        float f1 = unitinterface.CommandInterval();
        f1 *= World.Rnd().nextFloat(0.8F, 1.0F);
        float f3 = actor1.futurePosition(f1, point3d3);
        f3 += 0.4F;
        double d13 = roadsegment3.computePosAlong(point3d3);
        Point3d point3d5 = new Point3d();
        actor.pos.getAbs(point3d5);
        double d15 = roadsegment.computePosAlong(point3d5);
        double d16 = road.ComputeSignedDistAlong(unitdata.segmentIdx, d15, unitdata1.segmentIdx, d13);
        double d18 = unitdata.leaderDist;
        if(flag1)
            d18 += point3d.x;
        double d19 = d16 - d18;
        if(d19 < 0.0D)
        {
            float f2 = unitinterface.StayInterval();
            if(roadsegment.IsLandAligned())
                return new UnitMove(f2, new Vector3f(0.0F, 0.0F, -1F));
            else
                return new UnitMove(f2, road.get(unitdata.segmentIdx).normal);
        }
        double d20 = roadsegment.length2D - d15;
        if(d19 <= d20)
        {
            d15 += d19;
        } else
        {
            d15 = roadsegment.length2D;
            f3 = (float)((double)f3 * (d20 / d19));
        }
        f3 *= 1.05F;
        double d21 = unitdata.sideOffset;
        if(flag1)
            d21 += point3d.y;
        point3d5 = roadsegment.computePos_FitBegCirc(d15, d21, actor.collisionR());
        if(d15 >= roadsegment.length2D - 0.1D)
        {
            point3d5.x += roadsegment.dir2D.x * 0.2D;
            point3d5.y += roadsegment.dir2D.y * 0.2D;
        }
        d18 = distance2D(point3d5, actor.pos.getAbsPoint());
        Vector3f vector3f2 = road.get(unitdata.segmentIdx).IsLandAligned() ? new Vector3f(0.0F, 0.0F, -1F) : road.get(unitdata.segmentIdx).normal;
        return new UnitMove(unitinterface.HeightAboveLandSurface(), point3d5, f3 >= 0.3F ? f3 : 0.3F, vector3f2, -1F);
    }

    static final int PEACE = 0;
    static final int FIGHT = 1;
    static final int SHIFT = 2;
    static final int BRAKE = 3;
    private int curState;
    private int stateCountdown;
    private boolean shift_ToRightSide;
    private boolean shift_SwitchToBrakeWhenDone;
    private ArrayList unitsPacked;
    private RoadPath road;
    private int minGrabSeg;
    private int maxGrabSeg;
    private int chiefSeg;
    private double chiefAlong;
    private int minSeg;
    private int maxSeg;
    private float groupSpeed;
    private float maxSpace;
    private boolean withPreys;
    private long waitTime;
    private int posCountdown;
    private Vector3d estim_speed;
    private Point3d tmpp;
    private int curForm[];


}
