package com.maddox.il2.engine;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtInfantry;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.ai.ground.TgtVehicle;
import com.maddox.il2.builder.ActorTrigger;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.artillery.SArtillery;
import com.maddox.il2.objects.vehicles.artillery.STank;
import com.maddox.il2.objects.vehicles.artillery.SWagon;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapXY16Hash;
import com.maddox.util.HashMapXY16List;

public class CollideEnvXY extends CollideEnv
{
    class CollideEnvXYIndex
    {

        public void make(int i, int j)
        {
            int k = i % 3;
            int l = i / 3;
            int i1 = j % 3;
            int j1 = j / 3;
            x[0] = l;
            y[0] = j1;
            switch(i1)
            {
            default:
                break;

            case 0:
                switch(k)
                {
                case 0:
                    x[1] = l - 1;
                    y[1] = j1;
                    x[2] = l - 1;
                    y[2] = j1 - 1;
                    x[3] = l;
                    y[3] = j1 - 1;
                    count = 4;
                    break;

                case 1:
                    x[1] = l;
                    y[1] = j1 - 1;
                    count = 2;
                    break;

                case 2:
                    x[1] = l + 1;
                    y[1] = j1;
                    x[2] = l + 1;
                    y[2] = j1 - 1;
                    x[3] = l;
                    y[3] = j1 - 1;
                    count = 4;
                    break;
                }
                break;

            case 1:
                switch(k)
                {
                case 0:
                    x[1] = l - 1;
                    y[1] = j1;
                    count = 2;
                    break;

                case 1:
                    count = 1;
                    break;

                case 2:
                    x[1] = l + 1;
                    y[1] = j1;
                    count = 2;
                    break;
                }
                break;

            case 2:
                switch(k)
                {
                case 0:
                    x[1] = l - 1;
                    y[1] = j1;
                    x[2] = l - 1;
                    y[2] = j1 + 1;
                    x[3] = l;
                    y[3] = j1 + 1;
                    count = 4;
                    break;

                case 1:
                    x[1] = l;
                    y[1] = j1 + 1;
                    count = 2;
                    break;

                case 2:
                    x[1] = l + 1;
                    y[1] = j1;
                    x[2] = l + 1;
                    y[2] = j1 + 1;
                    x[3] = l;
                    y[3] = j1 + 1;
                    count = 4;
                    break;
                }
                break;
            }
        }

        public void make(Point3d point3d, float f)
        {
            int i = (int)((point3d.x - f - 96D) / 96D);
            int j = (int)((point3d.x + f + 96D) / 96D);
            int k = (int)((point3d.y - f - 96D) / 96D);
            int l = (int)((point3d.y + f + 96D) / 96D);
            int i1 = (j - i) + 1;
            int j1 = (l - k) + 1;
            count = i1 * j1;
            if(count > x.length)
            {
                x = new int[count];
                y = new int[count];
            }
            int k1 = 0;
            while(j1-- > 0) 
            {
                int l1 = i1;
                int i2 = i;
                while(l1-- > 0) 
                {
                    x[k1] = i2++;
                    y[k1++] = k;
                }
                k++;
            }
        }

        public int count;
        public int x[];
        public int y[];

        CollideEnvXYIndex()
        {
            x = new int[4];
            y = new int[4];
        }
    }


    public boolean isDoCollision()
    {
        return bDoCollision;
    }

    public static final double intersectPointSphere(double d, double d1, double d2, double d3, 
            double d4, double d5, double d6)
    {
        double d7 = d6 * d6;
        return d7 < (d - d3) * (d - d3) + (d1 - d4) * (d1 - d4) + (d2 - d5) * (d2 - d5) ? -1D : 0.0D;
    }

    public static final double intersectLineSphere(double d, double d1, double d2, double d3, 
            double d4, double d5, double d6, double d7, double d8, double d9)
    {
        double d10 = d9 * d9;
        double d11 = d3 - d;
        double d12 = d4 - d1;
        double d13 = d5 - d2;
        double d14 = d11 * d11 + d12 * d12 + d13 * d13;
        if(d14 < 0.000001D)
            return d10 < (d - d6) * (d - d6) + (d1 - d7) * (d1 - d7) + (d2 - d8) * (d2 - d8) ? -1D : 0.0D;
        double d15 = ((d6 - d) * d11 + (d7 - d1) * d12 + (d8 - d2) * d13) / d14;
        if(d15 >= 0.0D && d15 <= 1.0D)
        {
            double d16 = d + d15 * d11;
            double d18 = d1 + d15 * d12;
            double d20 = d2 + d15 * d13;
            double d21 = (d16 - d6) * (d16 - d6) + (d18 - d7) * (d18 - d7) + (d20 - d8) * (d20 - d8);
            double d22 = d10 - d21;
            if(d22 < 0.0D)
                return -1D;
            d15 -= Math.sqrt(d22 / d14);
            if(d15 < 0.0D)
                d15 = 0.0D;
            return d15;
        }
        double d17 = (d3 - d6) * (d3 - d6) + (d4 - d7) * (d4 - d7) + (d5 - d8) * (d5 - d8);
        double d19 = (d - d6) * (d - d6) + (d1 - d7) * (d1 - d7) + (d2 - d8) * (d2 - d8);
        if(d17 <= d10 || d19 <= d10)
            return d17 >= d19 ? 0.0D : 1.0D;
        else
            return -1D;
    }

    private void collidePoint()
    {
        makeBoundBox(_p.x, _p.y, _p.z);
        int i = makeIndexLine(_p);
        for(int j = 0; j < i; j++)
        {
            HashMapExt hashmapext = mapXY.get(indexLineY[j], indexLineX[j]);
            if(hashmapext == null)
                continue;
            for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
            {
                Actor actor = (Actor)entry.getKey();
                if(_current.getOwner() != actor && Actor.isValid(actor) && !current.containsKey(actor))
                    _collidePoint(actor);
            }

        }

        double d = Engine.cur.land.Hmax(_p.x, _p.y);
        if(boundZmin < d + (double)STATIC_HMAX)
        {
            for(int k = 0; k < i; k++)
            {
                List list = lstXY.get(indexLineY[k], indexLineX[k]);
                if(list == null)
                    continue;
                int l = list.size();
                for(int i1 = 0; i1 < l; i1++)
                {
                    Actor actor1 = (Actor)list.get(i1);
                    if(_current.getOwner() != actor1 && Actor.isValid(actor1) && !current.containsKey(actor1))
                        _collidePoint(actor1);
                }

            }

        }
        if(_current.isCollideOnLand() && _p.z - d <= 0.0D)
        {
            double d1 = _p.z - Engine.cur.land.HQ(_p.x, _p.y);
            if(d1 <= 0.0D)
            {
                long l1 = Time.tick();
                MsgCollision.post(l1, _current, Engine.actorLand(), "<edge>", "Body");
            }
        }
    }

    private void _collidePoint(Actor actor)
    {
        double d = actor.collisionR();
        if(d > 0.0D)
        {
            Point3d point3d = actor.pos.getAbsPoint();
            double d1 = -1D;
            boolean flag = true;
            p0.set(_p);
            if(actor.pos.isChanged())
            {
                Point3d point3d1 = actor.pos.getCurrentPoint();
                if(collideBoundBox(point3d1.x, point3d1.y, point3d1.z, point3d.x, point3d.y, point3d.z, d))
                    p0.x += point3d.x - point3d1.x;
                p0.y += point3d.y - point3d1.y;
                p0.z += point3d.z - point3d1.z;
                flag = (p0.x - _p.x) * (p0.x - _p.x) + (p0.y - _p.y) * (p0.y - _p.y) + (p0.z - _p.z) * (p0.z - _p.z) < 0.000001D;
                if(flag)
                    d1 = intersectPointSphere(_p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
                else
                    d1 = intersectLineSphere(p0.x, p0.y, p0.z, _p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
            } else
            if(collideBoundBox(point3d.x, point3d.y, point3d.z, d))
                d1 = intersectPointSphere(_p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
            if(d1 >= 0.0D && MsgCollisionRequest.on(_current, actor))
            {
                String s = "Body";
                if(actor instanceof ActorMesh)
                {
                    Mesh mesh = ((ActorMesh)actor).mesh();
                    Loc loc = actor.pos.getAbs();
                    if(flag)
                        d1 = mesh.detectCollisionPoint(loc, _p) == 0 ? -1D : 0.0D;
                    else
                        d1 = mesh.detectCollisionLine(loc, p0, _p);
                    if(d1 >= 0.0D)
                        s = Mesh.collisionChunk(0);
                }
                if(d1 >= 0.0D)
                {
                    long l = Time.tick() + (long)(d1 * (double)Time.tickLenFms());
                    if(l >= Time.tickNext())
                        l = Time.tickNext() - 1L;
                    MsgCollision.post2(l, _current, actor, "<edge>", s);
                }
            }
        }
        current.put(actor, null);
    }

    private void collideLine()
    {
        _currentP = _current.pos.getCurrentPoint();
        _p = _current.pos.getAbsPoint();
        if((_currentP.x - _p.x) * (_currentP.x - _p.x) + (_currentP.y - _p.y) * (_currentP.y - _p.y) + (_currentP.z - _p.z) * (_currentP.z - _p.z) < 0.000001D)
        {
            collidePoint();
            return;
        }
        makeBoundBox(_currentP.x, _currentP.y, _currentP.z, _p.x, _p.y, _p.z);
        int i = makeIndexLine(_currentP, _p);
        if(i == 0)
            System.out.println("CollideEnvXY.collideLine: " + _current + " very big step moved actor - IGNORED !!!");
        for(int j = 0; j < i; j++)
        {
            HashMapExt hashmapext = mapXY.get(indexLineY[j], indexLineX[j]);
            if(hashmapext == null)
                continue;
            for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
            {
                Actor actor = (Actor)entry.getKey();
                if(_current.getOwner() != actor && Actor.isValid(actor) && !current.containsKey(actor))
                    _collideLine(actor);
            }

        }

        double d = Engine.cur.land.Hmax(_p.x, _p.y);
        if(boundZmin < d + (double)STATIC_HMAX)
        {
            for(int k = 0; k < i; k++)
            {
                List list = lstXY.get(indexLineY[k], indexLineX[k]);
                if(list == null)
                    continue;
                int l = list.size();
                for(int i1 = 0; i1 < l; i1++)
                {
                    Actor actor1 = (Actor)list.get(i1);
                    if(_current.getOwner() != actor1 && Actor.isValid(actor1) && !current.containsKey(actor1))
                        _collideLine(actor1);
                }

            }

        }
        if(_current.isCollideOnLand() && _p.z - d <= 0.0D)
        {
            double d1 = _p.z - Engine.cur.land.HQ(_p.x, _p.y);
            if(d1 <= 0.0D)
            {
                long l1 = Time.tick();
                double d2 = _currentP.z - Engine.cur.land.HQ(_currentP.x, _currentP.y);
                if(d2 > 0.0D)
                {
                    double d3 = 1.0D + d1 / (d2 - d1);
                    l1 += (long)(d3 * (double)Time.tickLenFms());
                    if(l1 >= Time.tickNext())
                        l1 = Time.tickNext() - 1L;
                }
                MsgCollision.post(l1, _current, Engine.actorLand(), "<edge>", "Body");
            }
        }
    }

    private void _collideLine(Actor actor)
    {
        Engine.cur.profile.collideLineAll++;
        double d = actor.collisionR();
        if(d > 0.0D)
        {
            Point3d point3d = actor.pos.getAbsPoint();
            double d1 = -1D;
            boolean flag = false;
            p0.set(_currentP);
            if(actor.pos.isChanged())
            {
                Point3d point3d1 = actor.pos.getCurrentPoint();
                if(collideBoundBox(point3d1.x, point3d1.y, point3d1.z, point3d.x, point3d.y, point3d.z, d))
                {
                    p0.x += point3d.x - point3d1.x;
                    p0.y += point3d.y - point3d1.y;
                    p0.z += point3d.z - point3d1.z;
                    flag = (p0.x - _p.x) * (p0.x - _p.x) + (p0.y - _p.y) * (p0.y - _p.y) + (p0.z - _p.z) * (p0.z - _p.z) < 0.000001D;
                    if(flag)
                        d1 = intersectPointSphere(_p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
                    else
                        d1 = intersectLineSphere(p0.x, p0.y, p0.z, _p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
                }
            } else
            if(collideBoundBox(point3d.x, point3d.y, point3d.z, d))
                d1 = intersectLineSphere(p0.x, p0.y, p0.z, _p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
            if(d1 >= 0.0D)
            {
                Engine.cur.profile.collideLineSphere++;
                if(MsgCollisionRequest.on(_current, actor))
                {
                    String s = "Body";
                    if(actor instanceof ActorMesh)
                    {
                        Mesh mesh = ((ActorMesh)actor).mesh();
                        Loc loc = actor.pos.getAbs();
                        if(flag)
                            d1 = mesh.detectCollisionPoint(loc, _p) == 0 ? -1D : 0.0D;
                        else
                            d1 = mesh.detectCollisionLine(loc, p0, _p);
                        if(d1 >= 0.0D)
                            s = Mesh.collisionChunk(0);
                    }
                    if(d1 >= 0.0D)
                    {
                        Engine.cur.profile.collideLine++;
                        long l = Time.tick() + (long)(d1 * (double)Time.tickLenFms());
                        if(l >= Time.tickNext())
                            l = Time.tickNext() - 1L;
                        MsgCollision.post2(l, _current, actor, "<edge>", s);
                    }
                }
            }
        }
        current.put(actor, null);
    }

    private int makeIndexLine(Point3d point3d)
    {
        int i = (int)point3d.x / 96;
        int j = (int)point3d.y / 96;
        int k = 1;
        if(indexLineX == null || k > indexLineX.length)
        {
            indexLineX = new int[2 * k];
            indexLineY = new int[2 * k];
        }
        indexLineX[0] = i;
        indexLineY[0] = j;
        return k;
    }

    private int makeIndexLine(Point3d point3d, Point3d point3d1)
    {
        int i = (int)point3d.x / 96;
        int j = (int)point3d.y / 96;
        int k = Math.abs((int)point3d1.x / 96 - i) + Math.abs((int)point3d1.y / 96 - j) + 1;
        if(k > 100)
            return 0;
        indexLineX[0] = i;
        indexLineY[0] = j;
        if(k > 1)
        {
            byte byte0 = 1;
            if(point3d1.x < point3d.x)
                byte0 = -1;
            byte byte1 = 1;
            if(point3d1.y < point3d.y)
                byte1 = -1;
            if(Math.abs(point3d1.x - point3d.x) >= Math.abs(point3d1.y - point3d.y))
            {
                double d = Math.abs(point3d.y % 96D);
                double d2 = (96D * (point3d1.y - point3d.y)) / Math.abs(point3d1.x - point3d.x);
                if(d2 >= 0.0D)
                {
                    for(int l = 1; l < k; l++)
                    {
                        if(d < 96D)
                        {
                            i += byte0;
                            d += d2;
                        } else
                        {
                            j += byte1;
                            d -= 96D;
                        }
                        indexLineX[l] = i;
                        indexLineY[l] = j;
                    }

                } else
                {
                    for(int i1 = 1; i1 < k; i1++)
                    {
                        if(d > 0.0D)
                        {
                            i += byte0;
                            d += d2;
                        } else
                        {
                            j += byte1;
                            d += 96D;
                        }
                        indexLineX[i1] = i;
                        indexLineY[i1] = j;
                    }

                }
            } else
            {
                double d1 = Math.abs(point3d.x % 96D);
                double d3 = (96D * (point3d1.x - point3d.x)) / Math.abs(point3d1.y - point3d.y);
                if(d3 >= 0.0D)
                {
                    for(int j1 = 1; j1 < k; j1++)
                    {
                        if(d1 < 96D)
                        {
                            j += byte1;
                            d1 += d3;
                        } else
                        {
                            i += byte0;
                            d1 -= 96D;
                        }
                        indexLineX[j1] = i;
                        indexLineY[j1] = j;
                    }

                } else
                {
                    for(int k1 = 1; k1 < k; k1++)
                    {
                        if(d1 > 0.0D)
                        {
                            j += byte1;
                            d1 += d3;
                        } else
                        {
                            i += byte0;
                            d1 += 96D;
                        }
                        indexLineX[k1] = i;
                        indexLineY[k1] = j;
                    }

                }
            }
        }
        return k;
    }

    private void collideInterface()
    {
        CollisionInterface collisioninterface = (CollisionInterface)_current;
        if(!collisioninterface.collision_isEnabled())
            return;
        _currentP = _current.pos.getCurrentPoint();
        _p = _current.pos.getAbsPoint();
        int i = (int)_p.x / 96;
        int j = (int)_p.y / 96;
        _currentCollisionR = collisioninterface.collision_getCylinderR();
        makeBoundBox2D(_currentP.x, _currentP.y, _p.x, _p.y, _currentCollisionR);
        HashMapExt hashmapext = mapXY.get(j, i);
        if(hashmapext != null)
        {
            for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
            {
                Actor actor = (Actor)entry.getKey();
                if(!Actor.isValid(actor) || current.containsKey(actor))
                    continue;
                double d = actor.collisionR();
                if(d <= 0.0D)
                    continue;
                Point3d point3d = actor.pos.getAbsPoint();
                if(actor.pos.isChanged())
                {
                    Point3d point3d1 = actor.pos.getCurrentPoint();
                    if(collideBoundBox2D(point3d1.x, point3d1.y, point3d.x, point3d.y, d) && MsgCollisionRequest.on(_current, actor))
                        collisioninterface.collision_processing(actor);
                } else
                if(collideBoundBox2D(point3d.x, point3d.y, d) && MsgCollisionRequest.on(_current, actor))
                    collisioninterface.collision_processing(actor);
                current.put(actor, null);
            }

        }
        current.clear();
    }

    private void collideSphere()
    {
        _currentP = _current.pos.getCurrentPoint();
        _p = _current.pos.getAbsPoint();
        int i = (int)_p.x / 96;
        int j = (int)_p.y / 96;
        _currentCollisionR = _current.collisionR();
        if(_currentCollisionR <= 0.0D)
            return;
        makeBoundBox(_p.x, _p.y, _p.z, _currentCollisionR);
        if(_currentCollisionR <= 32D)
        {
            HashMapExt hashmapext = mapXY.get(j, i);
            if(hashmapext != null)
            {
                for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getKey();
                    if(Actor.isValid(actor) && !current.containsKey(actor) && !moved.containsKey(actor))
                        _collideSphere(actor);
                }

            }
        } else
        {
            index.make(_p, (float)_currentCollisionR);
            for(int k = 0; k < index.count; k++)
            {
                HashMapExt hashmapext1 = mapXY.get(index.y[k], index.x[k]);
                if(hashmapext1 == null)
                    continue;
                for(java.util.Map.Entry entry1 = hashmapext1.nextEntry(null); entry1 != null; entry1 = hashmapext1.nextEntry(entry1))
                {
                    Actor actor1 = (Actor)entry1.getKey();
                    if(Actor.isValid(actor1) && !current.containsKey(actor1) && !moved.containsKey(actor1))
                        _collideSphere(actor1);
                }

            }

        }
        double d = Engine.cur.land.Hmax(_p.x, _p.y);
        if(boundZmin < d + (double)STATIC_HMAX)
        {
            List list = lstXY.get(j, i);
            if(list != null)
            {
                int l = list.size();
                for(int i1 = 0; i1 < l; i1++)
                {
                    Actor actor2 = (Actor)list.get(i1);
                    if(Actor.isValid(actor2) && !current.containsKey(actor2) && !moved.containsKey(actor2))
                        _collideSphere(actor2);
                }

            }
        }
        if(_current.isCollideOnLand() && _p.z - _currentCollisionR - d <= 0.0D)
        {
            double d1 = _p.z - _currentCollisionR - Engine.cur.land.HQ(_p.x, _p.y);
            if(d1 <= 0.0D)
            {
                long l1 = Time.tick();
                if(_current instanceof ActorMesh)
                {
                    Mesh mesh = ((ActorMesh)_current).mesh();
                    Loc loc = _current.pos.getAbs();
                    double d3 = Engine.cur.land.EQN(_p.x, _p.y, normal);
                    if(mesh.detectCollisionPlane(loc, normal, d3) >= 0.0F)
                        MsgCollision.post(l1, _current, Engine.actorLand(), Mesh.collisionChunk(0), "Body");
                } else
                {
                    double d2 = _currentP.z - _currentCollisionR - Engine.cur.land.HQ(_currentP.x, _currentP.y);
                    if(d2 > 0.0D)
                    {
                        double d4 = 1.0D + d1 / (d2 - d1);
                        l1 += (long)(d4 * (double)Time.tickLenFms());
                        if(l1 >= Time.tickNext())
                            l1 = Time.tickNext() - 1L;
                    }
                    MsgCollision.post(l1, _current, Engine.actorLand(), "Body", "Body");
                }
            }
        }
    }

    private void _collideSphere(Actor actor)
    {
        Engine.cur.profile.collideSphereAll++;
        double d = actor.collisionR();
        if(d > 0.0D)
        {
            Point3d point3d = actor.pos.getAbsPoint();
            if(collideBoundBox(point3d.x, point3d.y, point3d.z, d))
            {
                double d1 = (_currentCollisionR + d) * (_currentCollisionR + d);
                double d2 = (_p.x - point3d.x) * (_p.x - point3d.x) + (_p.y - point3d.y) * (_p.y - point3d.y) + (_p.z - point3d.z) * (_p.z - point3d.z);
                if(d2 <= d1)
                {
                    Engine.cur.profile.collideSphereSphere++;
                    if(MsgCollisionRequest.on(_current, actor))
                    {
                        long l = Time.tick();
                        if((_current instanceof ActorMesh) && (actor instanceof ActorMesh))
                        {
                            Loc loc = _current.pos.getAbs();
                            Loc loc1 = actor.pos.getAbs();
                            Mesh mesh = ((ActorMesh)_current).mesh();
                            Mesh mesh1 = ((ActorMesh)actor).mesh();
                            if(mesh instanceof HierMesh)
                            {
                                if(mesh1 instanceof HierMesh)
                                {
                                    if(0 != ((HierMesh)mesh).detectCollision(loc, (HierMesh)mesh1, loc1))
                                    {
                                        Engine.cur.profile.collideSphere++;
                                        MsgCollision.post2(l, _current, actor, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                                    }
                                } else
                                if(0 != ((HierMesh)mesh).detectCollision(loc, mesh1, loc1))
                                {
                                    Engine.cur.profile.collideSphere++;
                                    MsgCollision.post2(l, _current, actor, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                                }
                            } else
                            if(mesh1 instanceof HierMesh)
                            {
                                if(0 != ((HierMesh)mesh1).detectCollision(loc1, mesh, loc))
                                {
                                    Engine.cur.profile.collideSphere++;
                                    MsgCollision.post2(l, actor, _current, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                                }
                            } else
                            if(0 != mesh.detectCollision(loc, mesh1, loc1))
                            {
                                Engine.cur.profile.collideSphere++;
                                MsgCollision.post2(l, _current, actor, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                            }
                        } else
                        {
                            if(actor.pos.isChanged())
                                point3d = actor.pos.getCurrentPoint();
                            double d3 = (_currentP.x - point3d.x) * (_currentP.x - point3d.x) + (_currentP.y - point3d.y) * (_currentP.y - point3d.y) + (_currentP.z - point3d.z) * (_currentP.z - point3d.z);
                            if(d3 > d1)
                            {
                                d1 = Math.sqrt(d1);
                                d2 = Math.sqrt(d2);
                                d3 = Math.sqrt(d3);
                                double d4 = 1.0D - (d1 - d2) / (d3 - d2);
                                l += (long)(d4 * (double)Time.tickLenFms());
                                if(l >= Time.tickNext())
                                    l = Time.tickNext() - 1L;
                            }
                            Engine.cur.profile.collideSphere++;
                            MsgCollision.post2(l, _current, actor, "Body", "Body");
                        }
                    }
                }
            }
        }
        current.put(actor, null);
    }

    private void makeBoundBox(double d, double d1, double d2, double d3, double d4, double d5)
    {
        if(d < d3)
        {
            boundXmin = d;
            boundXmax = d3;
        } else
        {
            boundXmin = d3;
            boundXmax = d;
        }
        if(d1 < d4)
        {
            boundYmin = d1;
            boundYmax = d4;
        } else
        {
            boundYmin = d4;
            boundYmax = d1;
        }
        if(d2 < d5)
        {
            boundZmin = d2;
            boundZmax = d5;
        } else
        {
            boundZmin = d5;
            boundZmax = d2;
        }
    }

    private void makeBoundBox2D(double d, double d1, double d2, double d3, double d4)
    {
        if(d < d2)
        {
            boundXmin = d - d4;
            boundXmax = d2 + d4;
        } else
        {
            boundXmin = d2 - d4;
            boundXmax = d + d4;
        }
        if(d1 < d3)
        {
            boundYmin = d1 - d4;
            boundYmax = d3 + d4;
        } else
        {
            boundYmin = d3 - d4;
            boundYmax = d1 + d4;
        }
    }

    private void makeBoundBox(double d, double d1, double d2, double d3)
    {
        boundXmin = d - d3;
        boundXmax = d + d3;
        boundYmin = d1 - d3;
        boundYmax = d1 + d3;
        boundZmin = d2 - d3;
        boundZmax = d2 + d3;
    }

    private void makeBoundBox(double d, double d1, double d2)
    {
        boundXmin = boundXmax = d;
        boundYmin = boundYmax = d1;
        boundZmin = boundZmax = d2;
    }

    private boolean collideBoundBox(double d, double d1, double d2, double d3)
    {
        if(d2 + d3 < boundZmin)
            return false;
        if(d2 - d3 > boundZmax)
            return false;
        if(d + d3 < boundXmin)
            return false;
        if(d - d3 > boundXmax)
            return false;
        if(d1 + d3 < boundYmin)
            return false;
        return d1 - d3 <= boundYmax;
    }

    private boolean collideBoundBox2D(double d, double d1, double d2)
    {
        if(d + d2 < boundXmin)
            return false;
        if(d - d2 > boundXmax)
            return false;
        if(d1 + d2 < boundYmin)
            return false;
        return d1 - d2 <= boundYmax;
    }

    private boolean collideBoundBox(double d, double d1, double d2, double d3, double d4, double d5, double d6)
    {
        if(d2 < d5)
        {
            if(d5 + d6 < boundZmin)
                return false;
            if(d2 - d6 > boundZmax)
                return false;
        } else
        {
            if(d2 + d6 < boundZmin)
                return false;
            if(d5 - d6 > boundZmax)
                return false;
        }
        if(d < d3)
        {
            if(d3 + d6 < boundXmin)
                return false;
            if(d - d6 > boundXmax)
                return false;
        } else
        {
            if(d + d6 < boundXmin)
                return false;
            if(d3 - d6 > boundXmax)
                return false;
        }
        if(d1 < d4)
        {
            if(d4 + d6 < boundYmin)
                return false;
            if(d1 - d6 > boundYmax)
                return false;
        } else
        {
            if(d1 + d6 < boundYmin)
                return false;
            if(d4 - d6 > boundYmax)
                return false;
        }
        return true;
    }

    private boolean collideBoundBox2D(double d, double d1, double d2, double d3, double d4)
    {
        if(d < d2)
        {
            if(d2 + d4 < boundXmin)
                return false;
            if(d - d4 > boundXmax)
                return false;
        } else
        {
            if(d + d4 < boundXmin)
                return false;
            if(d2 - d4 > boundXmax)
                return false;
        }
        if(d1 < d3)
        {
            if(d3 + d4 < boundYmin)
                return false;
            if(d1 - d4 > boundYmax)
                return false;
        } else
        {
            if(d1 + d4 < boundYmin)
                return false;
            if(d3 - d4 > boundYmax)
                return false;
        }
        return true;
    }

    protected void doCollision(List list)
    {
        bDoCollision = true;
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            _current = (Actor)list.get(j);
            if(!Actor.isValid(_current))
                continue;
            if(_current.isCollide())
            {
                if(_current.isCollideAsPoint())
                {
                    collideLine();
                } else
                {
                    moved.put(_current, null);
                    collideSphere();
                }
                current.clear();
                continue;
            }
            if(_current instanceof CollisionInterface)
                collideInterface();
        }

        moved.clear();
        _current = null;
        bDoCollision = false;
    }

    private void _bulletCollide(Actor actor, Actor actor1)
    {
        Engine.cur.profile.collideLineAll++;
        double d = actor.collisionR();
        if(d > 0.0D)
        {
            Point3d point3d = actor.pos.getAbsPoint();
            double d1 = -1D;
            boolean flag = false;
            p0.set(_currentP);
            if(actor.pos.isChanged())
            {
                Point3d point3d1 = actor.pos.getCurrentPoint();
                if(collideBoundBox(point3d1.x, point3d1.y, point3d1.z, point3d.x, point3d.y, point3d.z, d))
                {
                    p0.x += point3d.x - point3d1.x;
                    p0.y += point3d.y - point3d1.y;
                    p0.z += point3d.z - point3d1.z;
                    flag = (p0.x - _p.x) * (p0.x - _p.x) + (p0.y - _p.y) * (p0.y - _p.y) + (p0.z - _p.z) * (p0.z - _p.z) < 0.000001D;
                    if(flag)
                        d1 = intersectPointSphere(_p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
                    else
                        d1 = intersectLineSphere(p0.x, p0.y, p0.z, _p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
                }
            } else
            if(collideBoundBox(point3d.x, point3d.y, point3d.z, d))
                d1 = intersectLineSphere(p0.x, p0.y, p0.z, _p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d);
            if(d1 >= 0.0D)
            {
                Engine.cur.profile.collideLineSphere++;
                String s = "Body";
                if(actor instanceof ActorMesh)
                {
                    Mesh mesh = ((ActorMesh)actor).mesh();
                    Loc loc = actor.pos.getAbs();
                    if(flag)
                        d1 = mesh.detectCollisionPoint(loc, _p) == 0 ? -1D : 0.0D;
                    else
                        d1 = mesh.detectCollisionLine(loc, p0, _p);
                    if(d1 >= 0.0D)
                        s = Mesh.collisionChunk(0);
                }
                if(d1 >= 0.0D && d1 <= 1.0D)
                {
                    Engine.cur.profile.collideLine++;
                    if(_bulletActor == null || d1 < _bulletTickOffset)
                    {
                        _bulletActor = actor;
                        _bulletTickOffset = d1;
                        _bulletChunk = s;
                        _bulletArcade = false;
                    }
                } else
                if(_bulletArcade && actor.getArmy() != actor1.getArmy())
                {
                    double d2;
                    if(actor.pos.isChanged())
                    {
                        if(flag)
                            d2 = intersectPointSphere(_p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d / 2D);
                        else
                            d2 = intersectLineSphere(p0.x, p0.y, p0.z, _p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d / 2D);
                    } else
                    {
                        d2 = intersectLineSphere(p0.x, p0.y, p0.z, _p.x, _p.y, _p.z, point3d.x, point3d.y, point3d.z, d / 2D);
                    }
                    if(d2 >= 0.0D && d2 <= 1.0D)
                    {
                        Engine.cur.profile.collideLine++;
                        if(_bulletActor == null || d2 < _bulletTickOffset)
                        {
                            _bulletActor = actor;
                            _bulletTickOffset = d2;
                            _bulletChunk = "Body";
                        }
                    }
                }
            }
        }
    }

    private boolean bulletCollide(BulletGeneric bulletgeneric)
    {
        _bulletArcade = (bulletgeneric.flags & 0x40000000) != 0;
        _currentP = bulletgeneric.p0;
        _p = bulletgeneric.p1;
        if((_currentP.x - _p.x) * (_currentP.x - _p.x) + (_currentP.y - _p.y) * (_currentP.y - _p.y) + (_currentP.z - _p.z) * (_currentP.z - _p.z) < 0.000001D)
            return false;
        Actor actor = bulletgeneric.gunOwnerBody();
        makeBoundBox(_currentP.x, _currentP.y, _currentP.z, _p.x, _p.y, _p.z);
        int i = makeIndexLine(_currentP, _p);
        if(i == 0)
            System.out.println("CollideEnvXY.doBulletMoveAndCollision: " + bulletgeneric + " very big step moved bullet - IGNORED !!!");
        double d = Engine.cur.land.Hmax(_p.x, _p.y);
        _bulletActor = null;
        int j = 0;
        do
        {
            if(j >= i)
                break;
            HashMapExt hashmapext = mapXY.get(indexLineY[j], indexLineX[j]);
            if(hashmapext != null)
            {
                for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
                {
                    Actor actor1 = (Actor)entry.getKey();
                    if(actor != null && actor != actor1 && Actor.isValid(actor1))
                        _bulletCollide(actor1, actor);
                }

            }
            if(boundZmin < d + (double)STATIC_HMAX)
            {
                List list = lstXY.get(indexLineY[j], indexLineX[j]);
                if(list != null)
                {
                    int k = list.size();
                    for(int i1 = 0; i1 < k; i1++)
                    {
                        Actor actor2 = (Actor)list.get(i1);
                        if(actor != null && actor != actor2 && Actor.isValid(actor2))
                            _bulletCollide(actor2, actor);
                    }

                }
            }
            if(_bulletActor != null)
                break;
            j++;
        } while(true);
        if(_p.z - d <= 0.0D)
        {
            double d1 = _p.z - Engine.cur.land.HQ(_p.x, _p.y);
            if(d1 <= 0.0D)
            {
                double d2 = 0.0D;
                double d3 = _currentP.z - Engine.cur.land.HQ(_currentP.x, _currentP.y);
                if(d3 > 0.0D)
                {
                    d2 = 1.0D + d1 / (d3 - d1);
                    if(d2 < 0.0D)
                        d2 = 0.0D;
                    if(d2 > 1.0D)
                        d2 = 1.0D;
                }
                if(_bulletActor == null || d2 < _bulletTickOffset)
                {
                    _bulletActor = Engine.actorLand();
                    _bulletTickOffset = d2;
                    _bulletChunk = "Body";
                    _bulletArcade = false;
                }
            }
        }
        if(_bulletActor != null)
        {
            if((bulletgeneric.flags & 0x40000000) != 0 && _bulletArcade)
                bulletgeneric.flags |= 0x2000;
            long l = Time.tick() + (long)(_bulletTickOffset * (double)Time.tickLenFms());
            if(l >= Time.tickNext())
                l = Time.tickNext() - 1L;
            MsgBulletCollision.post(l, _bulletTickOffset, _bulletActor, _bulletChunk, bulletgeneric);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doBulletMoveAndCollision()
    {
        BulletGeneric bulletgeneric = null;
        BulletGeneric bulletgeneric1 = Engine.cur.bulletList;
        long l = Time.current();
        float f = Time.tickLenFs();
        while(bulletgeneric1 != null) 
        {
            if(l < bulletgeneric1.timeEnd)
            {
                if(!bulletgeneric1.bMoved)
                {
                    bulletgeneric1.move(f);
                    bulletgeneric1.flags &= 0xffffefff;
                }
                bulletgeneric1.bMoved = false;
            } else
            {
                bulletgeneric1.timeOut();
                bulletgeneric1.destroy();
            }
            if(bulletgeneric1.isDestroyed() || bulletCollide(bulletgeneric1))
            {
                BulletGeneric bulletgeneric2 = bulletgeneric1;
                bulletgeneric1 = bulletgeneric1.nextBullet;
                if(bulletgeneric == null)
                    Engine.cur.bulletList = bulletgeneric1;
                else
                    bulletgeneric.nextBullet = bulletgeneric1;
                bulletgeneric2.nextBullet = null;
            } else
            {
                bulletgeneric = bulletgeneric1;
                bulletgeneric1 = bulletgeneric1.nextBullet;
            }
        }
        _bulletActor = null;
    }

    public void getSphere(AbstractCollection abstractcollection, Point3d point3d, double d)
    {
        int i = (int)(point3d.x - d) / 96;
        int j = (int)(point3d.y - d) / 96;
        int k = (int)(point3d.x + d) / 96;
        int l = (int)(point3d.y + d) / 96;
        _getSphereLst = abstractcollection;
        _getSphereCenter = point3d;
        _getSphereR = d;
        for(int i1 = j; i1 <= l; i1++)
        {
            for(int j1 = i; j1 <= k; j1++)
            {
                HashMapExt hashmapext = mapXY.get(i1, j1);
                if(hashmapext != null)
                {
                    for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
                    {
                        Actor actor = (Actor)entry.getKey();
                        if(Actor.isValid(actor) && !current.containsKey(actor))
                            _getSphere(actor);
                    }

                }
                List list = lstXY.get(i1, j1);
                if(list == null)
                    continue;
                int k1 = list.size();
                for(int l1 = 0; l1 < k1; l1++)
                {
                    Actor actor1 = (Actor)list.get(l1);
                    if(Actor.isValid(actor1) && !current.containsKey(actor1))
                        _getSphere(actor1);
                }

            }

        }

        current.clear();
    }

    private void _getSphere(Actor actor)
    {
        current.put(actor, null);
        double d = actor.collisionR();
        Point3d point3d = actor.pos.getAbsPoint();
        double d1 = (_getSphereR + d) * (_getSphereR + d);
        double d2 = (_getSphereCenter.x - point3d.x) * (_getSphereCenter.x - point3d.x) + (_getSphereCenter.y - point3d.y) * (_getSphereCenter.y - point3d.y) + (_getSphereCenter.z - point3d.z) * (_getSphereCenter.z - point3d.z);
        if(d2 <= d1)
            _getSphereLst.add(actor);
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, Actor actor, Point3d point3d2)
    {
        if((point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z) < 0.000001D)
            return null;
        _getLineP1.set(point3d1);
        point3d1 = _getLineP1;
        _getLineP0 = point3d;
        int i = 0;
        do
        {
            i = makeIndexLine(point3d, point3d1);
            if(i > 0)
                break;
            point3d1.interpolate(point3d, point3d1, 0.5D);
        } while(true);
        if(Engine.cur.land.HQ(point3d.x, point3d.y) > point3d.z)
            return null;
        double d = point3d.z;
        if(d > point3d1.z)
        {
            d = point3d1.z;
        }
        _getLineDx = point3d1.x - point3d.x;
        _getLineDy = point3d1.y - point3d.y;
        _getLineDz = point3d1.z - point3d.z;
        _getLineLen2 = _getLineDx * _getLineDx + _getLineDy * _getLineDy + _getLineDz * _getLineDz;
        _getLineBOnlySphere = flag;
        int j = 0;
        do
        {
            if(j >= i)
                break;
            int k = indexLineX[j];
            int l = indexLineY[j];
            HashMapExt hashmapext = mapXY.get(l, k);
            if(hashmapext != null)
            {
                for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
                {
                    Actor actor1 = (Actor)entry.getKey();
                    if(Actor.isValid(actor1) && actor1 != actor)
                        _getLineIntersect(actor1);
                }

            }
            double d3 = Landscape.Hmax((float)(k * 96) + 48F, (float)(l * 96) + 48F);
            if(d < d3 + (double)STATIC_HMAX)
            {
                List list = lstXY.get(l, k);
                if(list != null)
                {
                    int i1 = list.size();
                    for(int j1 = 0; j1 < i1; j1++)
                    {
                        Actor actor3 = (Actor)list.get(j1);
                        if(Actor.isValid(actor3) && actor3 != actor)
                            _getLineIntersect(actor3);
                    }

                }
            }
            if(_getLineA != null)
            {
                Actor actor2 = _getLineA;
                _getLineA = null;
                _getLineP1.set(_getLineRayHit);
                Engine.land();
                if(!Landscape.rayHitHQ(point3d, _getLineP1, _getLineLandHit))
                {
                    if(point3d2 != null)
                        point3d2.set(_getLineRayHit);
                    return actor2;
                }
                break;
            }
            j++;
        } while(true);
        Engine.land();
        if(Landscape.rayHitHQ(point3d, _getLineP1, _getLineLandHit))
        {
            if(point3d2 != null)
                point3d2.set(_getLineLandHit);
            return Engine.actorLand();
        } else
        {
            return null;
        }
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, ActorFilter actorfilter, Point3d point3d2)
    {
        if((point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z) < 0.000001D)
            return null;
        _getLineP1.set(point3d1);
        point3d1 = _getLineP1;
        _getLineP0 = point3d;
        int i = 0;
        do
        {
            i = makeIndexLine(point3d, point3d1);
            if(i > 0)
                break;
            point3d1.interpolate(point3d, point3d1, 0.5D);
        } while(true);
        if(Engine.cur.land.HQ(point3d.x, point3d.y) > point3d.z)
            return null;
        double d = point3d.z;
        if(d > point3d1.z)
        {
            d = point3d1.z;
        }
        _getLineDx = point3d1.x - point3d.x;
        _getLineDy = point3d1.y - point3d.y;
        _getLineDz = point3d1.z - point3d.z;
        _getLineLen2 = _getLineDx * _getLineDx + _getLineDy * _getLineDy + _getLineDz * _getLineDz;
        _getLineBOnlySphere = flag;
        int j = 0;
        do
        {
            if(j >= i)
                break;
            int k = indexLineX[j];
            int l = indexLineY[j];
            HashMapExt hashmapext = mapXY.get(l, k);
            if(hashmapext != null)
            {
                for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getKey();
                    if(Actor.isValid(actor) && actorfilter.isUse(actor, 0.0D))
                        _getLineIntersect(actor);
                }

            }
            double d3 = Landscape.Hmax((float)(k * 96) + 48F, (float)(l * 96) + 48F);
            if(d < d3 + (double)STATIC_HMAX)
            {
                List list = lstXY.get(l, k);
                if(list != null)
                {
                    int i1 = list.size();
                    for(int j1 = 0; j1 < i1; j1++)
                    {
                        Actor actor2 = (Actor)list.get(j1);
                        if(Actor.isValid(actor2) && actorfilter.isUse(actor2, 0.0D))
                            _getLineIntersect(actor2);
                    }

                }
            }
            if(_getLineA != null)
            {
                Actor actor1 = _getLineA;
                _getLineA = null;
                _getLineP1.set(_getLineRayHit);
                if(actorfilter.isUse(Engine.actorLand(), 0.0D))
                {
                    Engine.land();
                    if(Landscape.rayHitHQ(point3d, _getLineP1, _getLineLandHit))
                        break;
                }
                if(point3d2 != null)
                    point3d2.set(_getLineRayHit);
                return actor1;
            }
            j++;
        } while(true);
        if(actorfilter.isUse(Engine.actorLand(), 0.0D))
        {
            Engine.land();
            if(Landscape.rayHitHQ(point3d, _getLineP1, _getLineLandHit))
            {
                if(point3d2 != null)
                    point3d2.set(_getLineLandHit);
                return Engine.actorLand();
            }
        }
        return null;
    }

    private void _getLineIntersect(Actor actor)
    {
        Point3d point3d = actor.pos.getAbsPoint();
        double d = actor.collisionR();
        double d1 = d * d;
        double d2 = ((point3d.x - _getLineP0.x) * _getLineDx + (point3d.y - _getLineP0.y) * _getLineDy + (point3d.z - _getLineP0.z) * _getLineDz) / _getLineLen2;
        if(d2 >= 0.0D && d2 <= 1.0D)
        {
            double d3 = _getLineP0.x + d2 * _getLineDx;
            double d5 = _getLineP0.y + d2 * _getLineDy;
            double d7 = _getLineP0.z + d2 * _getLineDz;
            double d8 = (d3 - point3d.x) * (d3 - point3d.x) + (d5 - point3d.y) * (d5 - point3d.y) + (d7 - point3d.z) * (d7 - point3d.z);
            double d9 = d1 - d8;
            if(d9 < 0.0D)
            {
                d2 = -1D;
            } else
            {
                d2 -= Math.sqrt(d9 / _getLineLen2);
                if(d2 < 0.0D)
                    d2 = 0.0D;
            }
        } else
        {
            double d4 = (_getLineP1.x - point3d.x) * (_getLineP1.x - point3d.x) + (_getLineP1.y - point3d.y) * (_getLineP1.y - point3d.y) + (_getLineP1.z - point3d.z) * (_getLineP1.z - point3d.z);
            double d6 = (_getLineP0.x - point3d.x) * (_getLineP0.x - point3d.x) + (_getLineP0.y - point3d.y) * (_getLineP0.y - point3d.y) + (_getLineP0.z - point3d.z) * (_getLineP0.z - point3d.z);
            if(d4 <= d1 || d6 <= d1)
            {
                if(d4 < d6)
                    d2 = 1.0D;
                else
                    d2 = 0.0D;
            } else
            {
                d2 = -1D;
            }
        }
        if(d2 < 0.0D)
            return;
        if(!_getLineBOnlySphere && (actor instanceof ActorMesh))
        {
            Mesh mesh = ((ActorMesh)actor).mesh();
            Loc loc = actor.pos.getAbs();
            d2 = mesh.detectCollisionLine(loc, _getLineP0, _getLineP1);
            if(d2 < 0.0D)
                return;
        }
        if(_getLineA != null && d2 > _getLineU)
        {
            return;
        } else
        {
            _getLineA = actor;
            _getLineU = d2;
            _getLineRayHit.interpolate(_getLineP0, _getLineP1, d2);
            return;
        }
    }

    public void getFiltered(AbstractCollection abstractcollection, Point3d point3d, double d, ActorFilter actorfilter)
    {
        int i = (int)(point3d.x - d) / 96;
        int j = (int)(point3d.y - d) / 96;
        int k = (int)(point3d.x + d) / 96;
        int l = (int)(point3d.y + d) / 96;
        _getFilteredCenter = point3d;
        _getFilteredFilter = actorfilter;
        _getFilteredR = d;
        for(int i1 = j; i1 <= l; i1++)
        {
            for(int j1 = i; j1 <= k; j1++)
            {
                HashMapExt hashmapext = mapXY.get(i1, j1);
                if(hashmapext != null)
                {
                    for(java.util.Map.Entry entry1 = hashmapext.nextEntry(null); entry1 != null; entry1 = hashmapext.nextEntry(entry1))
                    {
                        Actor actor = (Actor)entry1.getKey();
                        if(Actor.isValid(actor) && !current.containsKey(actor) && !moved.containsKey(actor))
                            _getFiltered(actor);
                    }

                }
                List list = lstXY.get(i1, j1);
                if(list == null)
                    continue;
                int k1 = list.size();
                for(int l1 = 0; l1 < k1; l1++)
                {
                    Actor actor1 = (Actor)list.get(l1);
                    if(Actor.isValid(actor1) && !current.containsKey(actor1) && !moved.containsKey(actor1))
                        _getFiltered(actor1);
                }

            }

        }

        if(abstractcollection != null)
        {
            for(java.util.Map.Entry entry = current.nextEntry(null); entry != null; entry = current.nextEntry(entry))
                abstractcollection.add(entry.getKey());

        }
        current.clear();
        moved.clear();
    }

    private void _getFiltered(Actor actor)
    {
        double d = actor.collisionR();
        Point3d point3d = actor.pos.getAbsPoint();
        double d1 = (_getFilteredR + d) * (_getFilteredR + d);
        double d2 = (_getFilteredCenter.x - point3d.x) * (_getFilteredCenter.x - point3d.x) + (_getFilteredCenter.y - point3d.y) * (_getFilteredCenter.y - point3d.y) + (_getFilteredCenter.z - point3d.z) * (_getFilteredCenter.z - point3d.z);
        if(d2 <= d1 && _getFilteredFilter.isUse(actor, d2))
            current.put(actor, null);
        else
            moved.put(actor, null);
    }

    public void getNearestEnemies(Point3d point3d, double d, int i, Accumulator accumulator)
    {
        if(d >= MAX_ENEMY_DIST)
            d = MAX_ENEMY_DIST;
        int j = (int)(point3d.x - d) / 96;
        int k = (int)(point3d.y - d) / 96;
        int l = (int)(point3d.x + d) / 96;
        int i1 = (int)(point3d.y + d) / 96;
        for(int j1 = k; j1 <= i1; j1++)
        {
            for(int k1 = j; k1 <= l; k1++)
            {
                HashMapExt hashmapext = mapXY.get(j1, k1);
                if(hashmapext != null)
                {
                    for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
                    {
                        Actor actor = (Actor)entry.getKey();
                        int i2 = actor.getArmy();
                        if(i2 == 0 || i2 == i || !actor.isAlive() || current.containsKey(actor))
                            continue;
                        current.put(actor, null);
                        double d1 = actor.collisionR();
                        Point3d point3d1 = actor.pos.getAbsPoint();
                        double d3 = (d + d1) * (d + d1);
                        double d4 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
                        if(d4 <= d3 && !accumulator.add(actor, d4))
                        {
                            current.clear();
                            return;
                        }
                    }

                }
                List list = lstXY.get(j1, k1);
                if(list == null)
                    continue;
                int l1 = list.size();
                for(int j2 = 0; j2 < l1; j2++)
                {
                    Actor actor1 = (Actor)list.get(j2);
                    int k2 = actor1.getArmy();
                    if(k2 == 0 || k2 == i || !actor1.isAlive() || current.containsKey(actor1))
                        continue;
                    current.put(actor1, null);
                    double d2 = actor1.collisionR();
                    Point3d point3d2 = actor1.pos.getAbsPoint();
                    double d5 = (d + d2) * (d + d2);
                    double d6 = (point3d.x - point3d2.x) * (point3d.x - point3d2.x) + (point3d.y - point3d2.y) * (point3d.y - point3d2.y) + (point3d.z - point3d2.z) * (point3d.z - point3d2.z);
                    if(d6 <= d5 && !accumulator.add(actor1, d6))
                    {
                        current.clear();
                        return;
                    }
                }

            }

        }

        current.clear();
    }

    public void getNearestEnemiesCyl(Point3d point3d, double d, double d1, double d2, 
            int i, Accumulator accumulator)
    {
        if(d >= MAX_ENEMY_DIST)
            d = MAX_ENEMY_DIST;
        int j = (int)(point3d.x - d) / 96;
        int k = (int)(point3d.y - d) / 96;
        int l = (int)(point3d.x + d) / 96;
        int i1 = (int)(point3d.y + d) / 96;
        for(int j1 = k; j1 <= i1; j1++)
        {
            for(int k1 = j; k1 <= l; k1++)
            {
                HashMapExt hashmapext = mapXY.get(j1, k1);
                if(hashmapext != null)
                {
                    for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
                    {
                        Actor actor = (Actor)entry.getKey();
                        int i2 = actor.getArmy();
                        if(i2 == 0 || i2 == i || !actor.isAlive() || current.containsKey(actor))
                            continue;
                        current.put(actor, null);
                        double d3 = actor.collisionR();
                        Point3d point3d1 = actor.pos.getAbsPoint();
                        double d5 = (d + d3) * (d + d3);
                        double d6 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y);
                        if(d6 > d5)
                            continue;
                        double d8 = point3d1.z - point3d.z;
                        if(d8 - d3 <= d2 && d8 + d3 >= d1 && !accumulator.add(actor, d6 + d8 * d8))
                        {
                            current.clear();
                            return;
                        }
                    }

                }
                List list = lstXY.get(j1, k1);
                if(list == null)
                    continue;
                int l1 = list.size();
                for(int j2 = 0; j2 < l1; j2++)
                {
                    Actor actor1 = (Actor)list.get(j2);
                    int k2 = actor1.getArmy();
                    if(k2 == 0 || k2 == i || !actor1.isAlive() || current.containsKey(actor1))
                        continue;
                    current.put(actor1, null);
                    double d4 = actor1.collisionR();
                    Point3d point3d2 = actor1.pos.getAbsPoint();
                    double d7 = (d + d4) * (d + d4);
                    double d9 = (point3d.x - point3d2.x) * (point3d.x - point3d2.x) + (point3d.y - point3d2.y) * (point3d.y - point3d2.y);
                    if(d9 > d7)
                        continue;
                    double d10 = point3d2.z - point3d.z;
                    if(d10 - d4 <= d2 && d10 + d4 >= d1 && !accumulator.add(actor1, d9 + d10 * d10))
                    {
                        current.clear();
                        return;
                    }
                }

            }

        }

        current.clear();
    }

    public ResultTrigger getEnemiesInCyl(Point2d triggerPoint, double radius, double altMin, double altMax, 
            int triggerArmy, int typeActivate, int actorsMin)
    {
        int count = 0;
        double asl = 0.0D;
        ArrayList listActorsAlreadyProcessed = new ArrayList();
        int mapGridXMin = (int)(triggerPoint.x - radius) / 96;
        int mapGridYMin = (int)(triggerPoint.y - radius) / 96;
        int mapGridXMax = (int)(triggerPoint.x + radius) / 96;
        int mapGripYMax = (int)(triggerPoint.y + radius) / 96;
        for(int mapGridY = mapGridYMin; mapGridY <= mapGripYMax; mapGridY++)
        {
            for(int mapGridX = mapGridXMin; mapGridX <= mapGridXMax; mapGridX++)
            {
                if(typeActivate != ActorTrigger.TRIGGERED_BY_STATIC_AIRCRAFTS_ONLY)
                { // We are not searching for static aircraft, so let's check all actors on the map.
                    HashMapExt mapGridActorList = mapXY.get(mapGridY, mapGridX);
                    if(mapGridActorList != null)
                    {
                        for(Map.Entry mapGridActorEntry = mapGridActorList.nextEntry(null); mapGridActorEntry != null; mapGridActorEntry = mapGridActorList.nextEntry(mapGridActorEntry))
                        {
                            Actor actor = (Actor)mapGridActorEntry.getKey();
                            int army = actor.getArmy();
                            if(army == 0 || army == triggerArmy || !actor.isAlive() || listActorsAlreadyProcessed.contains(actor))
                                continue;
                            if(typeActivate == ActorTrigger.TRIGGERED_BY_AI_AIRCRAFT_ONLY
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_HUMAN_AIRCRAFT_ONLY
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_MOVING_AIRCRAFTS_ONLY)
                            {
                                if(!(actor instanceof Aircraft))
                                    continue;
                                if(typeActivate != ActorTrigger.TRIGGERED_BY_MOVING_AIRCRAFTS_ONLY)
                                {
                                    boolean flag = ((Aircraft)actor).FM.isPlayers() || ((Aircraft)actor).isNetPlayer();
                                    if(typeActivate == ActorTrigger.TRIGGERED_BY_AI_AIRCRAFT_ONLY && flag
                                            || typeActivate == ActorTrigger.TRIGGERED_BY_HUMAN_AIRCRAFT_ONLY && !flag)
                                        continue;
                                }
                            } else
                            if(typeActivate == ActorTrigger.TRIGGERED_BY_GROUND_OBJECTS_ONLY && (actor instanceof Aircraft)
                                || typeActivate == ActorTrigger.TRIGGERED_BY_ARMOUR_ONLY && !(actor instanceof TankGeneric) && !(actor instanceof STank) && !(actor instanceof TgtTank)
                                || typeActivate == ActorTrigger.TRIGGERED_BY_ARTILLERY_ONLY && ((!(actor instanceof ArtilleryGeneric) && !(actor instanceof SArtillery) && !(actor instanceof TgtFlak)) || (actor instanceof TgtInfantry))
                                || typeActivate == ActorTrigger.TRIGGERED_BY_INFANTRY_ONLY && !(actor instanceof TgtInfantry)
                                || typeActivate == ActorTrigger.TRIGGERED_BY_SHIPS_ONLY && !(actor instanceof BigshipGeneric) && !(actor instanceof ShipGeneric)
                                || typeActivate == ActorTrigger.TRIGGERED_BY_TRAINS_ONLY && !(actor instanceof Wagon) && !(actor instanceof SWagon) && !(actor instanceof TgtTrain)
                                || typeActivate == ActorTrigger.TRIGGERED_BY_VEHICLES_ONLY && ((!(actor instanceof CarGeneric) && !(actor instanceof TgtVehicle)) || (actor instanceof TgtInfantry)))
                                continue;
                            Point3d actorPoint = actor.pos.getAbsPoint();
                            double actorAltMin = altMin < 1000D?actorPoint.z - Engine.land().HQ(actorPoint.x, actorPoint.y):actorPoint.z;
                            double actorAltMax = altMax < 1000D?actorPoint.z - Engine.land().HQ(actorPoint.x, actorPoint.y):actorPoint.z;
                            if(triggerPoint.distance(new Point2d(actorPoint.x, actorPoint.y)) < radius && actorAltMax <= altMax && actorAltMin >= altMin)
//                            if(triggerPoint.distance(new Point2d(actorPoint.x, actorPoint.y)) < radius && actorPoint.z <= altMax && actorPoint.z >= altMin)
                            {
                                count++;
                                listActorsAlreadyProcessed.add(actor);
                                asl += actorPoint.z;
                                if(count >= actorsMin)
                                    return new CollideEnv.ResultTrigger(true, asl / (double)count);
                            }
                        }

                    }
                }
                if(typeActivate != ActorTrigger.TRIGGERED_BY_AI_AIRCRAFT_ONLY && typeActivate != ActorTrigger.TRIGGERED_BY_HUMAN_AIRCRAFT_ONLY && typeActivate != ActorTrigger.TRIGGERED_BY_MOVING_AIRCRAFTS_ONLY)
                { // We are searching for things that could be static, let's walk through the list of statics.
                    List staticActorsList = lstXY.get(mapGridY, mapGridX);
                    if(staticActorsList != null)
                    {
                        for(int staticActorsIndex = 0; staticActorsIndex < staticActorsList.size(); staticActorsIndex++)
                        {
                            Actor actor = (Actor)staticActorsList.get(staticActorsIndex);
                            int army = actor.getArmy();
                            
                            if(army == 0 || army == triggerArmy || !actor.isAlive() || listActorsAlreadyProcessed.contains(actor))
                                continue;

                            if(typeActivate == ActorTrigger.TRIGGERED_BY_STATIC_AIRCRAFTS_ONLY && !(actor instanceof PlaneGeneric)
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_ARMOUR_ONLY && !(actor instanceof TankGeneric) && !(actor instanceof STank) && !(actor instanceof TgtTank)
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_ARTILLERY_ONLY && ((!(actor instanceof ArtilleryGeneric) && !(actor instanceof SArtillery) && !(actor instanceof TgtFlak)) || (actor instanceof TgtInfantry))
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_INFANTRY_ONLY && !(actor instanceof TgtInfantry)
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_SHIPS_ONLY && !(actor instanceof BigshipGeneric) && !(actor instanceof ShipGeneric)
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_TRAINS_ONLY && !(actor instanceof Wagon) && !(actor instanceof SWagon) && !(actor instanceof TgtTrain)
                                    || typeActivate == ActorTrigger.TRIGGERED_BY_VEHICLES_ONLY && ((!(actor instanceof CarGeneric) && !(actor instanceof TgtVehicle)) || (actor instanceof TgtInfantry)))
                                continue;
                            Point3d actorPoint = actor.pos.getAbsPoint();
                            if(triggerPoint.distance(new Point2d(actorPoint.x, actorPoint.y)) < radius && actorPoint.z <= altMax && actorPoint.z >= altMin)
                            {
                                count++;
                                listActorsAlreadyProcessed.add(actor);
                                asl += actorPoint.z;
                                if(count >= actorsMin)
                                    return new CollideEnv.ResultTrigger(true, asl / (double)count);
                            }
                        }
                    }
                }
            }
        }

        return new CollideEnv.ResultTrigger(false);
    }

    protected void changedPos(Actor actor, Point3d point3d, Point3d point3d1)
    {
        if(actor.collisionR() <= 32F)
        {
            int i = (int)point3d.x / 32;
            int k = (int)point3d.y / 32;
            int i1 = (int)point3d1.x / 32;
            int k1 = (int)point3d1.y / 32;
            if(i != i1 || k != k1)
            {
                remove(actor, i, k);
                add(actor, i1, k1);
            }
        } else
        {
            int j = (int)(point3d.x / 96D);
            int l = (int)(point3d.y / 96D);
            int j1 = (int)(point3d1.x / 96D);
            int l1 = (int)(point3d1.y / 96D);
            if(j != j1 || l != l1)
            {
                index.make(point3d, actor.collisionR());
                for(int i2 = 0; i2 < index.count; i2++)
                    mapXY.remove(index.y[i2], index.x[i2], actor);

                index.make(point3d1, actor.collisionR());
                for(int j2 = 0; j2 < index.count; j2++)
                    mapXY.put(index.y[j2], index.x[j2], actor, null);

            }
        }
    }

    protected void add(Actor actor)
    {
        if(actor.pos != null)
        {
            Point3d point3d = actor.pos.getCurrentPoint();
            if(actor.collisionR() <= 32F)
            {
                add(actor, (int)point3d.x / 32, (int)point3d.y / 32);
            } else
            {
                index.make(point3d, actor.collisionR());
                for(int i = 0; i < index.count; i++)
                    mapXY.put(index.y[i], index.x[i], actor, null);

            }
        }
    }

    protected void add(Actor actor, int i, int j)
    {
        index.make(i, j);
        for(int k = 0; k < index.count; k++)
            mapXY.put(index.y[k], index.x[k], actor, null);

    }

    protected void remove(Actor actor)
    {
        if(actor.pos != null)
        {
            Point3d point3d = actor.pos.getCurrentPoint();
            if(actor.collisionR() <= 32F)
            {
                remove(actor, (int)point3d.x / 32, (int)point3d.y / 32);
            } else
            {
                index.make(point3d, actor.collisionR());
                for(int i = 0; i < index.count; i++)
                    mapXY.remove(index.y[i], index.x[i], actor);

            }
        }
    }

    private void remove(Actor actor, int i, int j)
    {
        index.make(i, j);
        for(int k = 0; k < index.count; k++)
            mapXY.remove(index.y[k], index.x[k], actor);

    }

    protected void changedPosStatic(Actor actor, Point3d point3d, Point3d point3d1)
    {
        removeStatic(actor);
        addStatic(actor);
    }

    protected void addStatic(Actor actor)
    {
        if(actor.pos != null)
        {
            Point3d point3d = actor.pos.getCurrentPoint();
            index.make(point3d, actor.collisionR());
            for(int i = 0; i < index.count; i++)
                lstXY.put(index.y[i], index.x[i], actor);

        }
    }

    protected void removeStatic(Actor actor)
    {
        if(actor.pos != null)
        {
            Point3d point3d = actor.pos.getCurrentPoint();
            index.make(point3d, actor.collisionR());
            for(int i = 0; i < index.count; i++)
                lstXY.remove(index.y[i], index.x[i], actor);

        }
    }

    protected void resetGameClear()
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        mapXY.allValues(arraylist);
        for(int i = 0; i < arraylist.size(); i++)
        {
            HashMapExt hashmapext = (HashMapExt)arraylist.get(i);
            arraylist1.addAll(hashmapext.keySet());
            Engine.destroyListGameActors(arraylist1);
        }

        arraylist.clear();
        lstXY.allValues(arraylist);
        for(int j = 0; j < arraylist.size(); j++)
        {
            ArrayList arraylist2 = (ArrayList)arraylist.get(j);
            arraylist1.addAll(arraylist2);
            Engine.destroyListGameActors(arraylist1);
        }

        arraylist.clear();
    }

    protected void resetGameCreate()
    {
        clear();
    }

    protected void clear()
    {
        mapXY.clear();
    }

    protected CollideEnvXY()
    {
        bDoCollision = false;
        indexLineX = new int[100];
        indexLineY = new int[100];
        moved = new HashMapExt();
        current = new HashMapExt();
        _getLineP1 = new Point3d();
        _getLineRayHit = new Point3d();
        _getLineLandHit = new Point3d();
        p0 = new Point3d();
        normal = new Vector3d();
        mapXY = new HashMapXY16Hash(7);
        lstXY = new HashMapXY16List(7);
        index = new CollideEnvXYIndex();
    }

    public static final int SMALL_STEP = 32;
    public static final int STEP = 96;
    public static final float STEPF = 96F;
    public static final float STEPF_2 = 48F;
    public static float STATIC_HMAX = 50F;
    private static final float MAX_ENEMY_DIST = 6000F;
    private boolean bDoCollision;
    private int indexLineX[];
    private int indexLineY[];
    private double boundXmin;
    private double boundXmax;
    private double boundYmin;
    private double boundYmax;
    private double boundZmin;
    private double boundZmax;
    private HashMapExt moved;
    private HashMapExt current;
    private Actor _bulletActor;
    private String _bulletChunk;
    private double _bulletTickOffset;
    private boolean _bulletArcade;
    private AbstractCollection _getSphereLst;
    private Point3d _getSphereCenter;
    private double _getSphereR;
    private Point3d _getLineP0;
    private Point3d _getLineP1;
//    private double _getLineR2;
    private double _getLineLen2;
    private double _getLineDx;
    private double _getLineDy;
    private double _getLineDz;
    private boolean _getLineBOnlySphere;
    private double _getLineU;
    private Actor _getLineA;
    private Point3d _getLineRayHit;
    private Point3d _getLineLandHit;
    private Point3d _getFilteredCenter;
    private ActorFilter _getFilteredFilter;
    private double _getFilteredR;
    private Actor _current;
    private Point3d _currentP;
    private double _currentCollisionR;
    private Point3d _p;
    private Point3d p0;
    private Vector3d normal;
    private HashMapXY16Hash mapXY;
    private HashMapXY16List lstXY;
    private CollideEnvXYIndex index;

}
