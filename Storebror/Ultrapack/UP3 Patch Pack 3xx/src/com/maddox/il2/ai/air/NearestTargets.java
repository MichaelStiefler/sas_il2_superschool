package com.maddox.il2.ai.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.VisCheck;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;

public class NearestTargets
{

    public NearestTargets()
    {
    }

    public static Actor getEnemy(int i, int j, Point3d point3d, double d, int k)
    {
        Class class1 = null;
        Class class2 = null;
        switch(i)
        {
        case 0: // '\0'
        default:
            class1 = com.maddox.il2.engine.Actor.class;
            class2 = com.maddox.il2.engine.Actor.class;
            break;

        case 1: // '\001'
            class1 = com.maddox.il2.ai.ground.TgtTank.class;
            class2 = com.maddox.il2.ai.ground.TgtTank.class;
            break;

        case 2: // '\002'
            class1 = com.maddox.il2.ai.ground.TgtFlak.class;
            class2 = com.maddox.il2.ai.ground.TgtFlak.class;
            break;

        case 3: // '\003'
            class1 = com.maddox.il2.ai.ground.TgtVehicle.class;
            class2 = com.maddox.il2.ai.ground.TgtVehicle.class;
            break;

        case 4: // '\004'
            class1 = com.maddox.il2.ai.ground.TgtTrain.class;
            class2 = com.maddox.il2.ai.ground.TgtTrain.class;
            break;

        case 5: // '\005'
            return getBridge(j, point3d, d);

        case 6: // '\006'
            class1 = com.maddox.il2.ai.ground.TgtShip.class;
            class2 = com.maddox.il2.ai.ground.TgtShip.class;
            break;

        case 7: // '\007'
            class1 = com.maddox.il2.objects.air.TypeFighter.class;
            class2 = com.maddox.il2.objects.air.TypeFighter.class;
            break;

        case 8: // '\b'
            class1 = com.maddox.il2.objects.air.TypeBomber.class;
            class2 = com.maddox.il2.objects.air.TypeStormovik.class;
            break;

        case 9: // '\t'
            class1 = com.maddox.il2.objects.air.Aircraft.class;
            class2 = com.maddox.il2.objects.air.Aircraft.class;
            break;
        }
        List list = Engine.targets();
        int l = list.size();
        double d1 = d * d;
        int i1 = 0;
        for(int j1 = 0; j1 < l; j1++)
        {
            Actor actor = (Actor)list.get(j1);
            int k1 = actor.getArmy();
            if(k1 == 0 || k1 == k || !class1.isInstance(actor) && !class2.isInstance(actor) || i == 0 && (actor instanceof BridgeSegment) /*|| (actor instanceof com.maddox.il2.objects.vehicles.planes.Plane.GenericSpawnPointPlane) */ || (((Prey)actor).HitbyMask() & j) == 0)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d1.x - point3d.x) * (point3d1.x - point3d.x) + (point3d1.y - point3d.y) * (point3d1.y - point3d.y) + (point3d1.z - point3d.z) * (point3d1.z - point3d.z);
            if(d2 > d1)
                continue;
            if(actor instanceof Aircraft)
            {
                tempPos.set(point3d);
                tempPos.z += 5D;
                if(!VisCheck.visCheck(tempPos, point3d1, null, (Aircraft)actor))
                    continue;
            }
            int i2;
            for(i2 = 0; i2 < i1; i2++)
                if(d2 < nearDSq[i2])
                    break;

            if(i2 >= i1)
            {
                if(i1 < MAX_OBJECTS)
                {
                    nearAct[i1] = actor;
                    nearDSq[i1] = d2;
                    i1++;
                }
            } else
            {
                int j2;
                if(i1 < MAX_OBJECTS)
                {
                    j2 = i1 - 1;
                    i1++;
                } else
                {
                    j2 = i1 - 2;
                }
                for(; j2 >= i2; j2--)
                {
                    nearAct[j2 + 1] = nearAct[j2];
                    nearDSq[j2 + 1] = nearDSq[j2];
                }

                nearAct[i2] = actor;
                nearDSq[i2] = d2;
            }
        }

        if(i1 == 0)
            if(i == 0)
                return getBridge(j, point3d, d);
            else
                return null;
        Actor actor1 = nearAct[i1 != 1 ? World.Rnd().nextInt(i1) : 0];
        for(int l1 = 0; l1 < i1; l1++)
            nearAct[l1] = null;

        return actor1;
    }

    public static Actor getBridge(int i, Point3d point3d, double d)
    {
        if((0x18 & i) == 0)
            return null;
        ArrayList arraylist = World.cur().statics.bridges;
        int j = arraylist.size();
        double d1 = d * d;
        LongBridge longbridge = null;
        for(int l = 0; l < j; l++)
        {
            LongBridge longbridge1 = (LongBridge)arraylist.get(l);
            if(longbridge1.isAlive())
            {
                Point3d point3d1 = longbridge1.pos.getAbsPoint();
                double d2 = (point3d1.x - point3d.x) * (point3d1.x - point3d.x) + (point3d1.y - point3d.y) * (point3d1.y - point3d.y) + (point3d1.z - point3d.z) * (point3d1.z - point3d.z);
                if(d2 <= d1)
                {
                    longbridge = longbridge1;
                    d1 = d2;
                }
            }
        }

        if(longbridge == null)
        {
            return null;
        } else
        {
            int k = longbridge.NumStateBits() / 2;
            return BridgeSegment.getByIdx(longbridge.bridgeIdx(), World.Rnd().nextInt(k));
        }
    }

    private static final int MAX_OBJECTS = 32;
    private static Actor nearAct[] = new Actor[MAX_OBJECTS];
    private static double nearDSq[] = new double[MAX_OBJECTS];
    private static Point3d tempPos = new Point3d();

}
