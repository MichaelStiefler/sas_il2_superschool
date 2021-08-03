package com.maddox.il2.engine;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtInfantry;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.ai.ground.TgtVehicle;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
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

public class CollideEnvXY extends CollideEnv {
    class CollideEnvXYIndex {

        public void make(int i, int j) {
            int k = i % 3;
            int l = i / 3;
            int i1 = j % 3;
            int j1 = j / 3;
            this.x[0] = l;
            this.y[0] = j1;
            switch (i1) {
                default:
                    break;

                case 0:
                    switch (k) {
                        case 0:
                            this.x[1] = l - 1;
                            this.y[1] = j1;
                            this.x[2] = l - 1;
                            this.y[2] = j1 - 1;
                            this.x[3] = l;
                            this.y[3] = j1 - 1;
                            this.count = 4;
                            break;

                        case 1:
                            this.x[1] = l;
                            this.y[1] = j1 - 1;
                            this.count = 2;
                            break;

                        case 2:
                            this.x[1] = l + 1;
                            this.y[1] = j1;
                            this.x[2] = l + 1;
                            this.y[2] = j1 - 1;
                            this.x[3] = l;
                            this.y[3] = j1 - 1;
                            this.count = 4;
                            break;
                    }
                    break;

                case 1:
                    switch (k) {
                        case 0:
                            this.x[1] = l - 1;
                            this.y[1] = j1;
                            this.count = 2;
                            break;

                        case 1:
                            this.count = 1;
                            break;

                        case 2:
                            this.x[1] = l + 1;
                            this.y[1] = j1;
                            this.count = 2;
                            break;
                    }
                    break;

                case 2:
                    switch (k) {
                        case 0:
                            this.x[1] = l - 1;
                            this.y[1] = j1;
                            this.x[2] = l - 1;
                            this.y[2] = j1 + 1;
                            this.x[3] = l;
                            this.y[3] = j1 + 1;
                            this.count = 4;
                            break;

                        case 1:
                            this.x[1] = l;
                            this.y[1] = j1 + 1;
                            this.count = 2;
                            break;

                        case 2:
                            this.x[1] = l + 1;
                            this.y[1] = j1;
                            this.x[2] = l + 1;
                            this.y[2] = j1 + 1;
                            this.x[3] = l;
                            this.y[3] = j1 + 1;
                            this.count = 4;
                            break;
                    }
                    break;
            }
        }

        public void make(Point3d point3d, float f) {
            int i = (int) ((point3d.x - f - 96D) / 96D);
            int j = (int) ((point3d.x + f + 96D) / 96D);
            int k = (int) ((point3d.y - f - 96D) / 96D);
            int l = (int) ((point3d.y + f + 96D) / 96D);
            int i1 = (j - i) + 1;
            int j1 = (l - k) + 1;
            this.count = i1 * j1;
            if (this.count > this.x.length) {
                this.x = new int[this.count];
                this.y = new int[this.count];
            }
            int k1 = 0;
            while (j1-- > 0) {
                int l1 = i1;
                int i2 = i;
                while (l1-- > 0) {
                    this.x[k1] = i2++;
                    this.y[k1++] = k;
                }
                k++;
            }
        }

        public int count;
        public int x[];
        public int y[];

        CollideEnvXYIndex() {
            this.x = new int[4];
            this.y = new int[4];
        }
    }

    public boolean isDoCollision() {
        return this.bDoCollision;
    }

    public static final double intersectPointSphere(double d, double d1, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d6 * d6;
        return d7 >= (((d - d3) * (d - d3)) + ((d1 - d4) * (d1 - d4)) + ((d2 - d5) * (d2 - d5))) ? 0.0D : -1D;
    }

    public static final double intersectLineSphere(double d, double d1, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        double d10 = d9 * d9;
        double d11 = d3 - d;
        double d12 = d4 - d1;
        double d13 = d5 - d2;
        double d14 = (d11 * d11) + (d12 * d12) + (d13 * d13);
        if (d14 < 0.000001D) {
            return d10 >= (((d - d6) * (d - d6)) + ((d1 - d7) * (d1 - d7)) + ((d2 - d8) * (d2 - d8))) ? 0.0D : -1D;
        }
        double d15 = (((d6 - d) * d11) + ((d7 - d1) * d12) + ((d8 - d2) * d13)) / d14;
        if ((d15 >= 0.0D) && (d15 <= 1.0D)) {
            double d16 = d + (d15 * d11);
            double d18 = d1 + (d15 * d12);
            double d20 = d2 + (d15 * d13);
            double d21 = ((d16 - d6) * (d16 - d6)) + ((d18 - d7) * (d18 - d7)) + ((d20 - d8) * (d20 - d8));
            double d22 = d10 - d21;
            if (d22 < 0.0D) {
                return -1D;
            }
            d15 -= Math.sqrt(d22 / d14);
            if (d15 < 0.0D) {
                d15 = 0.0D;
            }
            return d15;
        }
        double d17 = ((d3 - d6) * (d3 - d6)) + ((d4 - d7) * (d4 - d7)) + ((d5 - d8) * (d5 - d8));
        double d19 = ((d - d6) * (d - d6)) + ((d1 - d7) * (d1 - d7)) + ((d2 - d8) * (d2 - d8));
        if ((d17 <= d10) || (d19 <= d10)) {
            return d17 < d19 ? 1.0D : 0.0D;
        } else {
            return -1D;
        }
    }

    private void collidePoint() {
        this.makeBoundBox(this._p.x, this._p.y, this._p.z);
        int i = this.makeIndexLine(this._p);
        for (int j = 0; j < i; j++) {
            HashMapExt hashmapext = this.mapXY.get(this.indexLineY[j], this.indexLineX[j]);
            if (hashmapext != null) {
                for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                    Actor actor = (Actor) entry.getKey();
                    if ((this._current.getOwner() != actor) && Actor.isValid(actor) && !this.current.containsKey(actor)) {
                        this._collidePoint(actor);
                    }
                }

            }
        }

        double d = Engine.cur.land.Hmax(this._p.x, this._p.y);
        if (this.boundZmin < (d + CollideEnvXY.STATIC_HMAX)) {
            for (int k = 0; k < i; k++) {
                List list = this.lstXY.get(this.indexLineY[k], this.indexLineX[k]);
                if (list != null) {
                    int l = list.size();
                    for (int i1 = 0; i1 < l; i1++) {
                        Actor actor1 = (Actor) list.get(i1);
                        if ((this._current.getOwner() != actor1) && Actor.isValid(actor1) && !this.current.containsKey(actor1)) {
                            this._collidePoint(actor1);
                        }
                    }

                }
            }

        }
        if (this._current.isCollideOnLand() && ((this._p.z - d) <= 0.0D)) {
            double d1 = this._p.z - Engine.cur.land.HQ(this._p.x, this._p.y);
            if (d1 <= 0.0D) {
                long l1 = Time.tick();
                MsgCollision.post(l1, this._current, Engine.actorLand(), "<edge>", "Body");
            }
        }
    }

    private void _collidePoint(Actor actor) {
        double d = actor.collisionR();
        if (d > 0.0D) {
            Point3d point3d = actor.pos.getAbsPoint();
            double d1 = -1D;
            boolean flag = true;
            this.p0.set(this._p);
            if (actor.pos.isChanged()) {
                Point3d point3d1 = actor.pos.getCurrentPoint();
                if (this.collideBoundBox(point3d1.x, point3d1.y, point3d1.z, point3d.x, point3d.y, point3d.z, d)) {
                    this.p0.x += point3d.x - point3d1.x;
                }
                this.p0.y += point3d.y - point3d1.y;
                this.p0.z += point3d.z - point3d1.z;
                flag = (((this.p0.x - this._p.x) * (this.p0.x - this._p.x)) + ((this.p0.y - this._p.y) * (this.p0.y - this._p.y)) + ((this.p0.z - this._p.z) * (this.p0.z - this._p.z))) < 0.000001D;
                if (flag) {
                    d1 = CollideEnvXY.intersectPointSphere(this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
                } else {
                    d1 = CollideEnvXY.intersectLineSphere(this.p0.x, this.p0.y, this.p0.z, this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
                }
            } else if (this.collideBoundBox(point3d.x, point3d.y, point3d.z, d)) {
                d1 = CollideEnvXY.intersectPointSphere(this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
            }
            if ((d1 >= 0.0D) && MsgCollisionRequest.on(this._current, actor)) {
                String s = "Body";
                if (actor instanceof ActorMesh) {
                    Mesh mesh = ((ActorMesh) actor).mesh();
                    Loc loc = actor.pos.getAbs();
                    if (flag) {
                        d1 = mesh.detectCollisionPoint(loc, this._p) != 0 ? 0.0D : -1D;
                    } else {
                        d1 = mesh.detectCollisionLine(loc, this.p0, this._p);
                    }
                    if (d1 >= 0.0D) {
                        s = Mesh.collisionChunk(0);
                    }
                }
                if (d1 >= 0.0D) {
                    long l = Time.tick() + (long) (d1 * Time.tickLenFms());
                    if (l >= Time.tickNext()) {
                        l = Time.tickNext() - 1L;
                    }
                    MsgCollision.post2(l, this._current, actor, "<edge>", s);
                }
            }
        }
        this.current.put(actor, null);
    }

    private void collideLine() {
        this._currentP = this._current.pos.getCurrentPoint();
        this._p = this._current.pos.getAbsPoint();
        if ((((this._currentP.x - this._p.x) * (this._currentP.x - this._p.x)) + ((this._currentP.y - this._p.y) * (this._currentP.y - this._p.y)) + ((this._currentP.z - this._p.z) * (this._currentP.z - this._p.z))) < 0.000001D) {
            this.collidePoint();
            return;
        }
        this.makeBoundBox(this._currentP.x, this._currentP.y, this._currentP.z, this._p.x, this._p.y, this._p.z);
        int i = this.makeIndexLine(this._currentP, this._p);
        if (i == 0) {
            System.out.println("CollideEnvXY.collideLine: " + this._current + " very big step moved actor - IGNORED !!!");
        }
        for (int j = 0; j < i; j++) {
            HashMapExt hashmapext = this.mapXY.get(this.indexLineY[j], this.indexLineX[j]);
            if (hashmapext != null) {
                for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                    Actor actor = (Actor) entry.getKey();
                    if ((this._current.getOwner() != actor) && Actor.isValid(actor) && !this.current.containsKey(actor)) {
                        this._collideLine(actor);
                    }
                }

            }
        }

        double d = Engine.cur.land.Hmax(this._p.x, this._p.y);
        if (this.boundZmin < (d + CollideEnvXY.STATIC_HMAX)) {
            for (int k = 0; k < i; k++) {
                List list = this.lstXY.get(this.indexLineY[k], this.indexLineX[k]);
                if (list != null) {
                    int l = list.size();
                    for (int i1 = 0; i1 < l; i1++) {
                        Actor actor1 = (Actor) list.get(i1);
                        if ((this._current.getOwner() != actor1) && Actor.isValid(actor1) && !this.current.containsKey(actor1)) {
                            this._collideLine(actor1);
                        }
                    }

                }
            }

        }
        if (this._current.isCollideOnLand() && ((this._p.z - d) <= 0.0D)) {
            double d1 = this._p.z - Engine.cur.land.HQ(this._p.x, this._p.y);
            if (d1 <= 0.0D) {
                long l1 = Time.tick();
                double d2 = this._currentP.z - Engine.cur.land.HQ(this._currentP.x, this._currentP.y);
                if (d2 > 0.0D) {
                    double d3 = 1.0D + (d1 / (d2 - d1));
                    l1 += (long) (d3 * Time.tickLenFms());
                    if (l1 >= Time.tickNext()) {
                        l1 = Time.tickNext() - 1L;
                    }
                }
                MsgCollision.post(l1, this._current, Engine.actorLand(), "<edge>", "Body");
            }
        }
    }

    private void _collideLine(Actor actor) {
        Engine.cur.profile.collideLineAll++;
        double d = actor.collisionR();
        if (d > 0.0D) {
            Point3d point3d = actor.pos.getAbsPoint();
            double d1 = -1D;
            boolean flag = false;
            this.p0.set(this._currentP);
            if (actor.pos.isChanged()) {
                Point3d point3d1 = actor.pos.getCurrentPoint();
                if (this.collideBoundBox(point3d1.x, point3d1.y, point3d1.z, point3d.x, point3d.y, point3d.z, d)) {
                    this.p0.x += point3d.x - point3d1.x;
                    this.p0.y += point3d.y - point3d1.y;
                    this.p0.z += point3d.z - point3d1.z;
                    flag = (((this.p0.x - this._p.x) * (this.p0.x - this._p.x)) + ((this.p0.y - this._p.y) * (this.p0.y - this._p.y)) + ((this.p0.z - this._p.z) * (this.p0.z - this._p.z))) < 0.000001D;
                    if (flag) {
                        d1 = CollideEnvXY.intersectPointSphere(this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
                    } else {
                        d1 = CollideEnvXY.intersectLineSphere(this.p0.x, this.p0.y, this.p0.z, this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
                    }
                }
            } else if (this.collideBoundBox(point3d.x, point3d.y, point3d.z, d)) {
                d1 = CollideEnvXY.intersectLineSphere(this.p0.x, this.p0.y, this.p0.z, this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
            }
            if (d1 >= 0.0D) {
                Engine.cur.profile.collideLineSphere++;
                if (MsgCollisionRequest.on(this._current, actor)) {
                    String s = "Body";
                    if (actor instanceof ActorMesh) {
                        Mesh mesh = ((ActorMesh) actor).mesh();
                        Loc loc = actor.pos.getAbs();
                        if (flag) {
                            d1 = mesh.detectCollisionPoint(loc, this._p) != 0 ? 0.0D : -1D;
                        } else {
                            d1 = mesh.detectCollisionLine(loc, this.p0, this._p);
                        }
                        if (d1 >= 0.0D) {
                            s = Mesh.collisionChunk(0);
                        }
                    }
                    if (d1 >= 0.0D) {
                        Engine.cur.profile.collideLine++;
                        long l = Time.tick() + (long) (d1 * Time.tickLenFms());
                        if (l >= Time.tickNext()) {
                            l = Time.tickNext() - 1L;
                        }
                        MsgCollision.post2(l, this._current, actor, "<edge>", s);
                    }
                }
            }
        }
        this.current.put(actor, null);
    }

    private int makeIndexLine(Point3d point3d) {
        int i = (int) point3d.x / 96;
        int j = (int) point3d.y / 96;
        int k = 1;
        if ((this.indexLineX == null) || (k > this.indexLineX.length)) {
            this.indexLineX = new int[2 * k];
            this.indexLineY = new int[2 * k];
        }
        this.indexLineX[0] = i;
        this.indexLineY[0] = j;
        return k;
    }

    private int makeIndexLine(Point3d point3d, Point3d point3d1) {
        int i = (int) point3d.x / 96;
        int j = (int) point3d.y / 96;
        int k = Math.abs(((int) point3d1.x / 96) - i) + Math.abs(((int) point3d1.y / 96) - j) + 1;
        if (k > 100) {
            return 0;
        }
        this.indexLineX[0] = i;
        this.indexLineY[0] = j;
        if (k > 1) {
            byte byte0 = 1;
            if (point3d1.x < point3d.x) {
                byte0 = -1;
            }
            byte byte1 = 1;
            if (point3d1.y < point3d.y) {
                byte1 = -1;
            }
            if (Math.abs(point3d1.x - point3d.x) >= Math.abs(point3d1.y - point3d.y)) {
                double d = Math.abs(point3d.y % 96D);
                double d2 = (96D * (point3d1.y - point3d.y)) / Math.abs(point3d1.x - point3d.x);
                if (d2 >= 0.0D) {
                    for (int l = 1; l < k; l++) {
                        if (d < 96D) {
                            i += byte0;
                            d += d2;
                        } else {
                            j += byte1;
                            d -= 96D;
                        }
                        this.indexLineX[l] = i;
                        this.indexLineY[l] = j;
                    }

                } else {
                    for (int i1 = 1; i1 < k; i1++) {
                        if (d > 0.0D) {
                            i += byte0;
                            d += d2;
                        } else {
                            j += byte1;
                            d += 96D;
                        }
                        this.indexLineX[i1] = i;
                        this.indexLineY[i1] = j;
                    }

                }
            } else {
                double d1 = Math.abs(point3d.x % 96D);
                double d3 = (96D * (point3d1.x - point3d.x)) / Math.abs(point3d1.y - point3d.y);
                if (d3 >= 0.0D) {
                    for (int j1 = 1; j1 < k; j1++) {
                        if (d1 < 96D) {
                            j += byte1;
                            d1 += d3;
                        } else {
                            i += byte0;
                            d1 -= 96D;
                        }
                        this.indexLineX[j1] = i;
                        this.indexLineY[j1] = j;
                    }

                } else {
                    for (int k1 = 1; k1 < k; k1++) {
                        if (d1 > 0.0D) {
                            j += byte1;
                            d1 += d3;
                        } else {
                            i += byte0;
                            d1 += 96D;
                        }
                        this.indexLineX[k1] = i;
                        this.indexLineY[k1] = j;
                    }

                }
            }
        }
        return k;
    }

    private void collideInterface() {
        CollisionInterface collisioninterface = (CollisionInterface) this._current;
        if (!collisioninterface.collision_isEnabled()) {
            return;
        }
        this._currentP = this._current.pos.getCurrentPoint();
        this._p = this._current.pos.getAbsPoint();
        int i = (int) this._p.x / 96;
        int j = (int) this._p.y / 96;
        this._currentCollisionR = collisioninterface.collision_getCylinderR();
        this.makeBoundBox2D(this._currentP.x, this._currentP.y, this._p.x, this._p.y, this._currentCollisionR);
        HashMapExt hashmapext = this.mapXY.get(j, i);
        if (hashmapext != null) {
            for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                Actor actor = (Actor) entry.getKey();
                if (Actor.isValid(actor) && !this.current.containsKey(actor)) {
                    double d = actor.collisionR();
                    if (d > 0.0D) {
                        Point3d point3d = actor.pos.getAbsPoint();
                        if (actor.pos.isChanged()) {
                            Point3d point3d1 = actor.pos.getCurrentPoint();
                            if (this.collideBoundBox2D(point3d1.x, point3d1.y, point3d.x, point3d.y, d) && MsgCollisionRequest.on(this._current, actor)) {
                                collisioninterface.collision_processing(actor);
                            }
                        } else if (this.collideBoundBox2D(point3d.x, point3d.y, d) && MsgCollisionRequest.on(this._current, actor)) {
                            collisioninterface.collision_processing(actor);
                        }
                        this.current.put(actor, null);
                    }
                }
            }

        }
        this.current.clear();
    }

    private void collideSphere() {
        this._currentP = this._current.pos.getCurrentPoint();
        this._p = this._current.pos.getAbsPoint();
        int i = (int) this._p.x / 96;
        int j = (int) this._p.y / 96;
        this._currentCollisionR = this._current.collisionR();
        if (this._currentCollisionR <= 0.0D) {
            return;
        }
        this.makeBoundBox(this._p.x, this._p.y, this._p.z, this._currentCollisionR);
        if (this._currentCollisionR <= 32D) {
            HashMapExt hashmapext = this.mapXY.get(j, i);
            if (hashmapext != null) {
                for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                    Actor actor = (Actor) entry.getKey();
                    if (Actor.isValid(actor) && !this.current.containsKey(actor) && !this.moved.containsKey(actor)) {
                        this._collideSphere(actor);
                    }
                }

            }
        } else {
            this.index.make(this._p, (float) this._currentCollisionR);
            for (int k = 0; k < this.index.count; k++) {
                HashMapExt hashmapext1 = this.mapXY.get(this.index.y[k], this.index.x[k]);
                if (hashmapext1 != null) {
                    for (java.util.Map.Entry entry1 = hashmapext1.nextEntry(null); entry1 != null; entry1 = hashmapext1.nextEntry(entry1)) {
                        Actor actor1 = (Actor) entry1.getKey();
                        if (Actor.isValid(actor1) && !this.current.containsKey(actor1) && !this.moved.containsKey(actor1)) {
                            this._collideSphere(actor1);
                        }
                    }

                }
            }

        }
        double d = Engine.cur.land.Hmax(this._p.x, this._p.y);
        if (this.boundZmin < (d + CollideEnvXY.STATIC_HMAX)) {
            List list = this.lstXY.get(j, i);
            if (list != null) {
                int l = list.size();
                for (int i1 = 0; i1 < l; i1++) {
                    Actor actor2 = (Actor) list.get(i1);
                    if (Actor.isValid(actor2) && !this.current.containsKey(actor2) && !this.moved.containsKey(actor2)) {
                        this._collideSphere(actor2);
                    }
                }

            }
        }
        if (this._current.isCollideOnLand() && ((this._p.z - this._currentCollisionR - d) <= 0.0D)) {
            double d1 = this._p.z - this._currentCollisionR - Engine.cur.land.HQ(this._p.x, this._p.y);
            if (d1 <= 0.0D) {
                long l1 = Time.tick();
                if (this._current instanceof ActorMesh) {
                    Mesh mesh = ((ActorMesh) this._current).mesh();
                    Loc loc = this._current.pos.getAbs();
                    double d3 = Engine.cur.land.EQN(this._p.x, this._p.y, this.normal);
                    if (mesh.detectCollisionPlane(loc, this.normal, d3) >= 0.0F) {
                        MsgCollision.post(l1, this._current, Engine.actorLand(), Mesh.collisionChunk(0), "Body");
                    }
                } else {
                    double d2 = this._currentP.z - this._currentCollisionR - Engine.cur.land.HQ(this._currentP.x, this._currentP.y);
                    if (d2 > 0.0D) {
                        double d4 = 1.0D + (d1 / (d2 - d1));
                        l1 += (long) (d4 * Time.tickLenFms());
                        if (l1 >= Time.tickNext()) {
                            l1 = Time.tickNext() - 1L;
                        }
                    }
                    MsgCollision.post(l1, this._current, Engine.actorLand(), "Body", "Body");
                }
            }
        }
    }

    private void _collideSphere(Actor actor) {
        Engine.cur.profile.collideSphereAll++;
        double d = actor.collisionR();
        if (d > 0.0D) {
            Point3d point3d = actor.pos.getAbsPoint();
            if (this.collideBoundBox(point3d.x, point3d.y, point3d.z, d)) {
                double d1 = (this._currentCollisionR + d) * (this._currentCollisionR + d);
                double d2 = ((this._p.x - point3d.x) * (this._p.x - point3d.x)) + ((this._p.y - point3d.y) * (this._p.y - point3d.y)) + ((this._p.z - point3d.z) * (this._p.z - point3d.z));
                if (d2 <= d1) {
                    Engine.cur.profile.collideSphereSphere++;
                    if (MsgCollisionRequest.on(this._current, actor)) {
                        long l = Time.tick();
                        if ((this._current instanceof ActorMesh) && (actor instanceof ActorMesh)) {
                            Loc loc = this._current.pos.getAbs();
                            Loc loc1 = actor.pos.getAbs();
                            Mesh mesh = ((ActorMesh) this._current).mesh();
                            Mesh mesh1 = ((ActorMesh) actor).mesh();
                            if (mesh instanceof HierMesh) {
                                if (mesh1 instanceof HierMesh) {
                                    if (((HierMesh) mesh).detectCollision(loc, (HierMesh) mesh1, loc1) != 0) {
                                        Engine.cur.profile.collideSphere++;
                                        MsgCollision.post2(l, this._current, actor, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                                    }
                                } else if (((HierMesh) mesh).detectCollision(loc, mesh1, loc1) != 0) {
                                    Engine.cur.profile.collideSphere++;
                                    MsgCollision.post2(l, this._current, actor, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                                }
                            } else if (mesh1 instanceof HierMesh) {
                                if (((HierMesh) mesh1).detectCollision(loc1, mesh, loc) != 0) {
                                    Engine.cur.profile.collideSphere++;
                                    MsgCollision.post2(l, actor, this._current, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                                }
                            } else if (mesh.detectCollision(loc, mesh1, loc1) != 0) {
                                Engine.cur.profile.collideSphere++;
                                MsgCollision.post2(l, this._current, actor, Mesh.collisionChunk(0), Mesh.collisionChunk(1));
                            }
                        } else {
                            if (actor.pos.isChanged()) {
                                point3d = actor.pos.getCurrentPoint();
                            }
                            double d3 = ((this._currentP.x - point3d.x) * (this._currentP.x - point3d.x)) + ((this._currentP.y - point3d.y) * (this._currentP.y - point3d.y)) + ((this._currentP.z - point3d.z) * (this._currentP.z - point3d.z));
                            if (d3 > d1) {
                                d1 = Math.sqrt(d1);
                                d2 = Math.sqrt(d2);
                                d3 = Math.sqrt(d3);
                                double d4 = 1.0D - ((d1 - d2) / (d3 - d2));
                                l += (long) (d4 * Time.tickLenFms());
                                if (l >= Time.tickNext()) {
                                    l = Time.tickNext() - 1L;
                                }
                            }
                            Engine.cur.profile.collideSphere++;
                            MsgCollision.post2(l, this._current, actor, "Body", "Body");
                        }
                    }
                }
            }
        }
        this.current.put(actor, null);
    }

    private void makeBoundBox(double d, double d1, double d2, double d3, double d4, double d5) {
        if (d < d3) {
            this.boundXmin = d;
            this.boundXmax = d3;
        } else {
            this.boundXmin = d3;
            this.boundXmax = d;
        }
        if (d1 < d4) {
            this.boundYmin = d1;
            this.boundYmax = d4;
        } else {
            this.boundYmin = d4;
            this.boundYmax = d1;
        }
        if (d2 < d5) {
            this.boundZmin = d2;
            this.boundZmax = d5;
        } else {
            this.boundZmin = d5;
            this.boundZmax = d2;
        }
    }

    private void makeBoundBox2D(double d, double d1, double d2, double d3, double d4) {
        if (d < d2) {
            this.boundXmin = d - d4;
            this.boundXmax = d2 + d4;
        } else {
            this.boundXmin = d2 - d4;
            this.boundXmax = d + d4;
        }
        if (d1 < d3) {
            this.boundYmin = d1 - d4;
            this.boundYmax = d3 + d4;
        } else {
            this.boundYmin = d3 - d4;
            this.boundYmax = d1 + d4;
        }
    }

    private void makeBoundBox(double d, double d1, double d2, double d3) {
        this.boundXmin = d - d3;
        this.boundXmax = d + d3;
        this.boundYmin = d1 - d3;
        this.boundYmax = d1 + d3;
        this.boundZmin = d2 - d3;
        this.boundZmax = d2 + d3;
    }

    private void makeBoundBox(double d, double d1, double d2) {
        this.boundXmin = this.boundXmax = d;
        this.boundYmin = this.boundYmax = d1;
        this.boundZmin = this.boundZmax = d2;
    }

    private boolean collideBoundBox(double d, double d1, double d2, double d3) {
        if (((d2 + d3) < this.boundZmin) || ((d2 - d3) > this.boundZmax) || ((d + d3) < this.boundXmin) || ((d - d3) > this.boundXmax)) {
            return false;
        }
        if ((d1 + d3) < this.boundYmin) {
            return false;
        }
        return (d1 - d3) <= this.boundYmax;
    }

    private boolean collideBoundBox2D(double d, double d1, double d2) {
        if (((d + d2) < this.boundXmin) || ((d - d2) > this.boundXmax) || ((d1 + d2) < this.boundYmin)) {
            return false;
        }
        return (d1 - d2) <= this.boundYmax;
    }

    private boolean collideBoundBox(double d, double d1, double d2, double d3, double d4, double d5, double d6) {
        if (d2 < d5) {
            if (((d5 + d6) < this.boundZmin) || ((d2 - d6) > this.boundZmax)) {
                return false;
            }
        } else {
            if (((d2 + d6) < this.boundZmin) || ((d5 - d6) > this.boundZmax)) {
                return false;
            }
        }
        if (d < d3) {
            if (((d3 + d6) < this.boundXmin) || ((d - d6) > this.boundXmax)) {
                return false;
            }
        } else {
            if (((d + d6) < this.boundXmin) || ((d3 - d6) > this.boundXmax)) {
                return false;
            }
        }
        if (d1 < d4) {
            if (((d4 + d6) < this.boundYmin) || ((d1 - d6) > this.boundYmax)) {
                return false;
            }
        } else {
            if (((d1 + d6) < this.boundYmin) || ((d4 - d6) > this.boundYmax)) {
                return false;
            }
        }
        return true;
    }

    private boolean collideBoundBox2D(double d, double d1, double d2, double d3, double d4) {
        if (d < d2) {
            if (((d2 + d4) < this.boundXmin) || ((d - d4) > this.boundXmax)) {
                return false;
            }
        } else {
            if (((d + d4) < this.boundXmin) || ((d2 - d4) > this.boundXmax)) {
                return false;
            }
        }
        if (d1 < d3) {
            if (((d3 + d4) < this.boundYmin) || ((d1 - d4) > this.boundYmax)) {
                return false;
            }
        } else {
            if (((d1 + d4) < this.boundYmin) || ((d3 - d4) > this.boundYmax)) {
                return false;
            }
        }
        return true;
    }

    protected void doCollision(List list) {
        this.bDoCollision = true;
        int i = list.size();
        for (int j = 0; j < i; j++) {
            this._current = (Actor) list.get(j);
            if (Actor.isValid(this._current)) {
                if (this._current.isCollide()) {
                    if (this._current.isCollideAsPoint()) {
                        this.collideLine();
                    } else {
                        this.moved.put(this._current, null);
                        this.collideSphere();
                    }
                    this.current.clear();
                } else if (this._current instanceof CollisionInterface) {
                    this.collideInterface();
                }
            }
        }

        this.moved.clear();
        this._current = null;
        this.bDoCollision = false;
    }

    private void _bulletCollide(Actor actor, Actor actor1) {
        Engine.cur.profile.collideLineAll++;
        double d = actor.collisionR();
        if (d > 0.0D) {
            Point3d point3d = actor.pos.getAbsPoint();
            double d1 = -1D;
            boolean flag = false;
            this.p0.set(this._currentP);
            if (actor.pos.isChanged()) {
                Point3d point3d1 = actor.pos.getCurrentPoint();
                if (this.collideBoundBox(point3d1.x, point3d1.y, point3d1.z, point3d.x, point3d.y, point3d.z, d)) {
                    this.p0.x += point3d.x - point3d1.x;
                    this.p0.y += point3d.y - point3d1.y;
                    this.p0.z += point3d.z - point3d1.z;
                    flag = (((this.p0.x - this._p.x) * (this.p0.x - this._p.x)) + ((this.p0.y - this._p.y) * (this.p0.y - this._p.y)) + ((this.p0.z - this._p.z) * (this.p0.z - this._p.z))) < 0.000001D;
                    if (flag) {
                        d1 = CollideEnvXY.intersectPointSphere(this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
                    } else {
                        d1 = CollideEnvXY.intersectLineSphere(this.p0.x, this.p0.y, this.p0.z, this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
                    }
                }
            } else if (this.collideBoundBox(point3d.x, point3d.y, point3d.z, d)) {
                d1 = CollideEnvXY.intersectLineSphere(this.p0.x, this.p0.y, this.p0.z, this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d);
            }
            if (d1 >= 0.0D) {
                Engine.cur.profile.collideLineSphere++;
                String s = "Body";
                if (actor instanceof ActorMesh) {
                    Mesh mesh = ((ActorMesh) actor).mesh();
                    Loc loc = actor.pos.getAbs();
                    if (flag) {
                        d1 = mesh.detectCollisionPoint(loc, this._p) != 0 ? 0.0D : -1D;
                    } else {
                        d1 = mesh.detectCollisionLine(loc, this.p0, this._p);
                    }
                    if (d1 >= 0.0D) {
                        s = Mesh.collisionChunk(0);
                    }
                }
                if ((d1 >= 0.0D) && (d1 <= 1.0D)) {
                    Engine.cur.profile.collideLine++;
                    if ((this._bulletActor == null) || (d1 < this._bulletTickOffset)) {
                        this._bulletActor = actor;
                        this._bulletTickOffset = d1;
                        this._bulletChunk = s;
                        this._bulletArcade = false;
                    }
                } else if (this._bulletArcade && (actor.getArmy() != actor1.getArmy())) {
                    double d2;
                    if (actor.pos.isChanged()) {
                        if (flag) {
                            d2 = CollideEnvXY.intersectPointSphere(this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d / 2D);
                        } else {
                            d2 = CollideEnvXY.intersectLineSphere(this.p0.x, this.p0.y, this.p0.z, this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d / 2D);
                        }
                    } else {
                        d2 = CollideEnvXY.intersectLineSphere(this.p0.x, this.p0.y, this.p0.z, this._p.x, this._p.y, this._p.z, point3d.x, point3d.y, point3d.z, d / 2D);
                    }
                    if ((d2 >= 0.0D) && (d2 <= 1.0D)) {
                        Engine.cur.profile.collideLine++;
                        if ((this._bulletActor == null) || (d2 < this._bulletTickOffset)) {
                            this._bulletActor = actor;
                            this._bulletTickOffset = d2;
                            this._bulletChunk = "Body";
                        }
                    }
                }
            }
        }
    }

    private boolean bulletCollide(BulletGeneric bulletgeneric) {
        this._bulletArcade = (bulletgeneric.flags & 0x40000000) != 0;
        this._currentP = bulletgeneric.p0;
        this._p = bulletgeneric.p1;
        if ((((this._currentP.x - this._p.x) * (this._currentP.x - this._p.x)) + ((this._currentP.y - this._p.y) * (this._currentP.y - this._p.y)) + ((this._currentP.z - this._p.z) * (this._currentP.z - this._p.z))) < 0.000001D) {
            return false;
        }
        Actor actor = bulletgeneric.gunOwnerBody();
        this.makeBoundBox(this._currentP.x, this._currentP.y, this._currentP.z, this._p.x, this._p.y, this._p.z);
        int i = this.makeIndexLine(this._currentP, this._p);
        if (i == 0) {
            System.out.println("CollideEnvXY.doBulletMoveAndCollision: " + bulletgeneric + " very big step moved bullet - IGNORED !!!");
        }
        double d = Engine.cur.land.Hmax(this._p.x, this._p.y);
        this._bulletActor = null;
        int j = 0;
        do {
            if (j >= i) {
                break;
            }
            HashMapExt hashmapext = this.mapXY.get(this.indexLineY[j], this.indexLineX[j]);
            if (hashmapext != null) {
                for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                    Actor actor1 = (Actor) entry.getKey();
                    if ((actor != null) && (actor != actor1) && Actor.isValid(actor1)) {
                        this._bulletCollide(actor1, actor);
                    }
                }

            }
            if (this.boundZmin < (d + CollideEnvXY.STATIC_HMAX)) {
                List list = this.lstXY.get(this.indexLineY[j], this.indexLineX[j]);
                if (list != null) {
                    int k = list.size();
                    for (int i1 = 0; i1 < k; i1++) {
                        Actor actor2 = (Actor) list.get(i1);
                        if ((actor != null) && (actor != actor2) && Actor.isValid(actor2)) {
                            this._bulletCollide(actor2, actor);
                        }
                    }

                }
            }
            if (this._bulletActor != null) {
                break;
            }
            j++;
        } while (true);
        if ((this._p.z - d) <= 0.0D) {
            double d1 = this._p.z - Engine.cur.land.HQ(this._p.x, this._p.y);
            if (d1 <= 0.0D) {
                double d2 = 0.0D;
                double d3 = this._currentP.z - Engine.cur.land.HQ(this._currentP.x, this._currentP.y);
                if (d3 > 0.0D) {
                    d2 = 1.0D + (d1 / (d3 - d1));
                    if (d2 < 0.0D) {
                        d2 = 0.0D;
                    }
                    if (d2 > 1.0D) {
                        d2 = 1.0D;
                    }
                }
                if ((this._bulletActor == null) || (d2 < this._bulletTickOffset)) {
                    this._bulletActor = Engine.actorLand();
                    this._bulletTickOffset = d2;
                    this._bulletChunk = "Body";
                    this._bulletArcade = false;
                }
            }
        }
        if (this._bulletActor != null) {
            if (((bulletgeneric.flags & 0x40000000) != 0) && this._bulletArcade) {
                bulletgeneric.flags |= 0x2000;
            }
            long l = Time.tick() + (long) (this._bulletTickOffset * Time.tickLenFms());
            if (l >= Time.tickNext()) {
                l = Time.tickNext() - 1L;
            }
            MsgBulletCollision.post(l, this._bulletTickOffset, this._bulletActor, this._bulletChunk, bulletgeneric);
            return true;
        } else {
            return false;
        }
    }

    protected void doBulletMoveAndCollision() {
        BulletGeneric bulletgeneric = null;
        BulletGeneric bulletgeneric1 = Engine.cur.bulletList;
        long l = Time.current();
        float f = Time.tickLenFs();
        while (bulletgeneric1 != null) {
            if (l < bulletgeneric1.timeEnd) {
                if (!bulletgeneric1.bMoved) {
                    bulletgeneric1.move(f);
                    bulletgeneric1.flags &= 0xffffefff;
                }
                bulletgeneric1.bMoved = false;
            } else {
                bulletgeneric1.timeOut();
                bulletgeneric1.destroy();
            }
            if (bulletgeneric1.isDestroyed() || this.bulletCollide(bulletgeneric1)) {
                BulletGeneric bulletgeneric2 = bulletgeneric1;
                bulletgeneric1 = bulletgeneric1.nextBullet;
                if (bulletgeneric == null) {
                    Engine.cur.bulletList = bulletgeneric1;
                } else {
                    bulletgeneric.nextBullet = bulletgeneric1;
                }
                bulletgeneric2.nextBullet = null;
            } else {
                bulletgeneric = bulletgeneric1;
                bulletgeneric1 = bulletgeneric1.nextBullet;
            }
        }
        this._bulletActor = null;
    }

    public void getSphere(AbstractCollection abstractcollection, Point3d point3d, double d) {
        int i = (int) (point3d.x - d) / 96;
        int j = (int) (point3d.y - d) / 96;
        int k = (int) (point3d.x + d) / 96;
        int l = (int) (point3d.y + d) / 96;
        this._getSphereLst = abstractcollection;
        this._getSphereCenter = point3d;
        this._getSphereR = d;
        for (int i1 = j; i1 <= l; i1++) {
            for (int j1 = i; j1 <= k; j1++) {
                HashMapExt hashmapext = this.mapXY.get(i1, j1);
                if (hashmapext != null) {
                    for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                        Actor actor = (Actor) entry.getKey();
                        if (Actor.isValid(actor) && !this.current.containsKey(actor)) {
                            this._getSphere(actor);
                        }
                    }

                }
                List list = this.lstXY.get(i1, j1);
                if (list != null) {
                    int k1 = list.size();
                    for (int l1 = 0; l1 < k1; l1++) {
                        Actor actor1 = (Actor) list.get(l1);
                        if (Actor.isValid(actor1) && !this.current.containsKey(actor1)) {
                            this._getSphere(actor1);
                        }
                    }

                }
            }

        }

        this.current.clear();
    }

    private void _getSphere(Actor actor) {
        this.current.put(actor, null);
        double d = actor.collisionR();
        Point3d point3d = actor.pos.getAbsPoint();
        double d1 = (this._getSphereR + d) * (this._getSphereR + d);
        double d2 = ((this._getSphereCenter.x - point3d.x) * (this._getSphereCenter.x - point3d.x)) + ((this._getSphereCenter.y - point3d.y) * (this._getSphereCenter.y - point3d.y)) + ((this._getSphereCenter.z - point3d.z) * (this._getSphereCenter.z - point3d.z));
        if (d2 <= d1) {
            this._getSphereLst.add(actor);
        }
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, Actor actor, Point3d point3d2) {
        if ((((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z))) < 0.000001D) {
            return null;
        }
        this._getLineP1.set(point3d1);
        point3d1 = this._getLineP1;
        this._getLineP0 = point3d;
        int i = 0;
        do {
            i = this.makeIndexLine(point3d, point3d1);
            if (i > 0) {
                break;
            }
            point3d1.interpolate(point3d, point3d1, 0.5D);
        } while (true);
        if (Engine.cur.land.HQ(point3d.x, point3d.y) > point3d.z) {
            return null;
        }
        double d = point3d.z;
        if (d > point3d1.z) {
            d = point3d1.z;
        }
        this._getLineDx = point3d1.x - point3d.x;
        this._getLineDy = point3d1.y - point3d.y;
        this._getLineDz = point3d1.z - point3d.z;
        this._getLineLen2 = (this._getLineDx * this._getLineDx) + (this._getLineDy * this._getLineDy) + (this._getLineDz * this._getLineDz);
        this._getLineBOnlySphere = flag;
        int j = 0;
        do {
            if (j >= i) {
                break;
            }
            int k = this.indexLineX[j];
            int l = this.indexLineY[j];
            HashMapExt hashmapext = this.mapXY.get(l, k);
            if (hashmapext != null) {
                for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                    Actor actor1 = (Actor) entry.getKey();
                    if (Actor.isValid(actor1) && (actor1 != actor)) {
                        this._getLineIntersect(actor1);
                    }
                }

            }
            double d3 = Landscape.Hmax(k * 96 + 48F, l * 96 + 48F);
            if (d < (d3 + CollideEnvXY.STATIC_HMAX)) {
                List list = this.lstXY.get(l, k);
                if (list != null) {
                    int i1 = list.size();
                    for (int j1 = 0; j1 < i1; j1++) {
                        Actor actor3 = (Actor) list.get(j1);
                        if (Actor.isValid(actor3) && (actor3 != actor)) {
                            this._getLineIntersect(actor3);
                        }
                    }

                }
            }
            if (this._getLineA != null) {
                Actor actor2 = this._getLineA;
                this._getLineA = null;
                this._getLineP1.set(this._getLineRayHit);
                Engine.land();
                if (!Landscape.rayHitHQ(point3d, this._getLineP1, this._getLineLandHit)) {
                    if (point3d2 != null) {
                        point3d2.set(this._getLineRayHit);
                    }
                    return actor2;
                }
                break;
            }
            j++;
        } while (true);
        Engine.land();
        if (Landscape.rayHitHQ(point3d, this._getLineP1, this._getLineLandHit)) {
            if (point3d2 != null) {
                point3d2.set(this._getLineLandHit);
            }
            return Engine.actorLand();
        } else {
            return null;
        }
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, ActorFilter actorfilter, Point3d point3d2) {
        if ((((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z))) < 0.000001D) {
            return null;
        }
        this._getLineP1.set(point3d1);
        point3d1 = this._getLineP1;
        this._getLineP0 = point3d;
        int i = 0;
        do {
            i = this.makeIndexLine(point3d, point3d1);
            if (i > 0) {
                break;
            }
            point3d1.interpolate(point3d, point3d1, 0.5D);
        } while (true);
        if (Engine.cur.land.HQ(point3d.x, point3d.y) > point3d.z) {
            return null;
        }
        double d = point3d.z;
        if (d > point3d1.z) {
            d = point3d1.z;
        }
        this._getLineDx = point3d1.x - point3d.x;
        this._getLineDy = point3d1.y - point3d.y;
        this._getLineDz = point3d1.z - point3d.z;
        this._getLineLen2 = (this._getLineDx * this._getLineDx) + (this._getLineDy * this._getLineDy) + (this._getLineDz * this._getLineDz);
        this._getLineBOnlySphere = flag;
        int j = 0;
        do {
            if (j >= i) {
                break;
            }
            int k = this.indexLineX[j];
            int l = this.indexLineY[j];
            HashMapExt hashmapext = this.mapXY.get(l, k);
            if (hashmapext != null) {
                for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                    Actor actor = (Actor) entry.getKey();
                    if (Actor.isValid(actor) && actorfilter.isUse(actor, 0.0D)) {
                        this._getLineIntersect(actor);
                    }
                }

            }
            double d3 = Landscape.Hmax(k * 96 + 48F, l * 96 + 48F);
            if (d < (d3 + CollideEnvXY.STATIC_HMAX)) {
                List list = this.lstXY.get(l, k);
                if (list != null) {
                    int i1 = list.size();
                    for (int j1 = 0; j1 < i1; j1++) {
                        Actor actor2 = (Actor) list.get(j1);
                        if (Actor.isValid(actor2) && actorfilter.isUse(actor2, 0.0D)) {
                            this._getLineIntersect(actor2);
                        }
                    }

                }
            }
            if (this._getLineA != null) {
                Actor actor1 = this._getLineA;
                this._getLineA = null;
                this._getLineP1.set(this._getLineRayHit);
                if (actorfilter.isUse(Engine.actorLand(), 0.0D)) {
                    Engine.land();
                    if (Landscape.rayHitHQ(point3d, this._getLineP1, this._getLineLandHit)) {
                        break;
                    }
                }
                if (point3d2 != null) {
                    point3d2.set(this._getLineRayHit);
                }
                return actor1;
            }
            j++;
        } while (true);
        if (actorfilter.isUse(Engine.actorLand(), 0.0D)) {
            Engine.land();
            if (Landscape.rayHitHQ(point3d, this._getLineP1, this._getLineLandHit)) {
                if (point3d2 != null) {
                    point3d2.set(this._getLineLandHit);
                }
                return Engine.actorLand();
            }
        }
        return null;
    }

    private void _getLineIntersect(Actor actor) {
        Point3d point3d = actor.pos.getAbsPoint();
        double d = actor.collisionR();
        double d1 = d * d;
        double d2 = (((point3d.x - this._getLineP0.x) * this._getLineDx) + ((point3d.y - this._getLineP0.y) * this._getLineDy) + ((point3d.z - this._getLineP0.z) * this._getLineDz)) / this._getLineLen2;
        if ((d2 >= 0.0D) && (d2 <= 1.0D)) {
            double d3 = this._getLineP0.x + (d2 * this._getLineDx);
            double d5 = this._getLineP0.y + (d2 * this._getLineDy);
            double d7 = this._getLineP0.z + (d2 * this._getLineDz);
            double d8 = ((d3 - point3d.x) * (d3 - point3d.x)) + ((d5 - point3d.y) * (d5 - point3d.y)) + ((d7 - point3d.z) * (d7 - point3d.z));
            double d9 = d1 - d8;
            if (d9 < 0.0D) {
                d2 = -1D;
            } else {
                d2 -= Math.sqrt(d9 / this._getLineLen2);
                if (d2 < 0.0D) {
                    d2 = 0.0D;
                }
            }
        } else {
            double d4 = ((this._getLineP1.x - point3d.x) * (this._getLineP1.x - point3d.x)) + ((this._getLineP1.y - point3d.y) * (this._getLineP1.y - point3d.y)) + ((this._getLineP1.z - point3d.z) * (this._getLineP1.z - point3d.z));
            double d6 = ((this._getLineP0.x - point3d.x) * (this._getLineP0.x - point3d.x)) + ((this._getLineP0.y - point3d.y) * (this._getLineP0.y - point3d.y)) + ((this._getLineP0.z - point3d.z) * (this._getLineP0.z - point3d.z));
            if ((d4 <= d1) || (d6 <= d1)) {
                if (d4 < d6) {
                    d2 = 1.0D;
                } else {
                    d2 = 0.0D;
                }
            } else {
                d2 = -1D;
            }
        }
        if (d2 < 0.0D) {
            return;
        }
        if (!this._getLineBOnlySphere && (actor instanceof ActorMesh)) {
            Mesh mesh = ((ActorMesh) actor).mesh();
            Loc loc = actor.pos.getAbs();
            d2 = mesh.detectCollisionLine(loc, this._getLineP0, this._getLineP1);
            if (d2 < 0.0D) {
                return;
            }
        }
        if ((this._getLineA != null) && (d2 > this._getLineU)) {
            return;
        } else {
            this._getLineA = actor;
            this._getLineU = d2;
            this._getLineRayHit.interpolate(this._getLineP0, this._getLineP1, d2);
            return;
        }
    }

    public void getFiltered(AbstractCollection abstractcollection, Point3d point3d, double d, ActorFilter actorfilter) {
        int i = (int) (point3d.x - d) / 96;
        int j = (int) (point3d.y - d) / 96;
        int k = (int) (point3d.x + d) / 96;
        int l = (int) (point3d.y + d) / 96;
        this._getFilteredCenter = point3d;
        this._getFilteredFilter = actorfilter;
        this._getFilteredR = d;
        for (int i1 = j; i1 <= l; i1++) {
            for (int j1 = i; j1 <= k; j1++) {
                HashMapExt hashmapext = this.mapXY.get(i1, j1);
                if (hashmapext != null) {
                    for (java.util.Map.Entry entry1 = hashmapext.nextEntry(null); entry1 != null; entry1 = hashmapext.nextEntry(entry1)) {
                        Actor actor = (Actor) entry1.getKey();
                        if (Actor.isValid(actor) && !this.current.containsKey(actor) && !this.moved.containsKey(actor)) {
                            this._getFiltered(actor);
                        }
                    }

                }
                List list = this.lstXY.get(i1, j1);
                if (list != null) {
                    int k1 = list.size();
                    for (int l1 = 0; l1 < k1; l1++) {
                        Actor actor1 = (Actor) list.get(l1);
                        if (Actor.isValid(actor1) && !this.current.containsKey(actor1) && !this.moved.containsKey(actor1)) {
                            this._getFiltered(actor1);
                        }
                    }

                }
            }

        }

        if (abstractcollection != null) {
            for (java.util.Map.Entry entry = this.current.nextEntry(null); entry != null; entry = this.current.nextEntry(entry)) {
                abstractcollection.add(entry.getKey());
            }

        }
        this.current.clear();
        this.moved.clear();
    }

    private void _getFiltered(Actor actor) {
        double d = actor.collisionR();
        Point3d point3d = actor.pos.getAbsPoint();
        double d1 = (this._getFilteredR + d) * (this._getFilteredR + d);
        double d2 = ((this._getFilteredCenter.x - point3d.x) * (this._getFilteredCenter.x - point3d.x)) + ((this._getFilteredCenter.y - point3d.y) * (this._getFilteredCenter.y - point3d.y)) + ((this._getFilteredCenter.z - point3d.z) * (this._getFilteredCenter.z - point3d.z));
        if ((d2 <= d1) && this._getFilteredFilter.isUse(actor, d2)) {
            this.current.put(actor, null);
        } else {
            this.moved.put(actor, null);
        }
    }

    public void getNearestEnemies(Point3d point3d, double d, int i, Accumulator accumulator) {
        if (d >= 6000D) {
            d = 6000D;
        }
        int j = (int) (point3d.x - d) / 96;
        int k = (int) (point3d.y - d) / 96;
        int l = (int) (point3d.x + d) / 96;
        int i1 = (int) (point3d.y + d) / 96;
        for (int j1 = k; j1 <= i1; j1++) {
            for (int k1 = j; k1 <= l; k1++) {
                HashMapExt hashmapext = this.mapXY.get(j1, k1);
                if (hashmapext != null) {
                    for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                        Actor actor = (Actor) entry.getKey();
                        int i2 = actor.getArmy();
                        if ((i2 != 0) && (i2 != i) && actor.isAlive() && !this.current.containsKey(actor)) {
                            this.current.put(actor, null);
                            double d1 = actor.collisionR();
                            Point3d point3d1 = actor.pos.getAbsPoint();
                            double d3 = (d + d1) * (d + d1);
                            double d4 = ((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y)) + ((point3d.z - point3d1.z) * (point3d.z - point3d1.z));
                            if ((d4 <= d3) && !accumulator.add(actor, d4)) {
                                this.current.clear();
                                return;
                            }
                        }
                    }

                }
                List list = this.lstXY.get(j1, k1);
                if (list != null) {
                    int l1 = list.size();
                    for (int j2 = 0; j2 < l1; j2++) {
                        Actor actor1 = (Actor) list.get(j2);
                        int k2 = actor1.getArmy();
                        if ((k2 != 0) && (k2 != i) && actor1.isAlive() && !this.current.containsKey(actor1)) {
                            this.current.put(actor1, null);
                            double d2 = actor1.collisionR();
                            Point3d point3d2 = actor1.pos.getAbsPoint();
                            double d5 = (d + d2) * (d + d2);
                            double d6 = ((point3d.x - point3d2.x) * (point3d.x - point3d2.x)) + ((point3d.y - point3d2.y) * (point3d.y - point3d2.y)) + ((point3d.z - point3d2.z) * (point3d.z - point3d2.z));
                            if ((d6 <= d5) && !accumulator.add(actor1, d6)) {
                                this.current.clear();
                                return;
                            }
                        }
                    }

                }
            }

        }

        this.current.clear();
    }

    public void getNearestEnemiesCyl(Point3d point3d, double d, double d1, double d2, int i, Accumulator accumulator) {
        if (d >= 6000D) {
            d = 6000D;
        }
        int j = (int) (point3d.x - d) / 96;
        int k = (int) (point3d.y - d) / 96;
        int l = (int) (point3d.x + d) / 96;
        int i1 = (int) (point3d.y + d) / 96;
        for (int j1 = k; j1 <= i1; j1++) {
            for (int k1 = j; k1 <= l; k1++) {
                HashMapExt hashmapext = this.mapXY.get(j1, k1);
                if (hashmapext != null) {
                    for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                        Actor actor = (Actor) entry.getKey();
                        int i2 = actor.getArmy();
                        if ((i2 != 0) && (i2 != i) && actor.isAlive() && !this.current.containsKey(actor)) {
                            this.current.put(actor, null);
                            double d3 = actor.collisionR();
                            Point3d point3d1 = actor.pos.getAbsPoint();
                            double d5 = (d + d3) * (d + d3);
                            double d6 = ((point3d.x - point3d1.x) * (point3d.x - point3d1.x)) + ((point3d.y - point3d1.y) * (point3d.y - point3d1.y));
                            if (d6 <= d5) {
                                double d8 = point3d1.z - point3d.z;
                                if (((d8 - d3) <= d2) && ((d8 + d3) >= d1) && !accumulator.add(actor, d6 + (d8 * d8))) {
                                    this.current.clear();
                                    return;
                                }
                            }
                        }
                    }

                }
                List list = this.lstXY.get(j1, k1);
                if (list != null) {
                    int l1 = list.size();
                    for (int j2 = 0; j2 < l1; j2++) {
                        Actor actor1 = (Actor) list.get(j2);
                        int k2 = actor1.getArmy();
                        if ((k2 != 0) && (k2 != i) && actor1.isAlive() && !this.current.containsKey(actor1)) {
                            this.current.put(actor1, null);
                            double d4 = actor1.collisionR();
                            Point3d point3d2 = actor1.pos.getAbsPoint();
                            double d7 = (d + d4) * (d + d4);
                            double d9 = ((point3d.x - point3d2.x) * (point3d.x - point3d2.x)) + ((point3d.y - point3d2.y) * (point3d.y - point3d2.y));
                            if (d9 <= d7) {
                                double d10 = point3d2.z - point3d.z;
                                if (((d10 - d4) <= d2) && ((d10 + d4) >= d1) && !accumulator.add(actor1, d9 + (d10 * d10))) {
                                    this.current.clear();
                                    return;
                                }
                            }
                        }
                    }

                }
            }

        }

        this.current.clear();
    }

    public CollideEnv.ResultTrigger getEnemiesInCyl(Point2d point2d, double d, double d1, double d2, int i, int typeActivate, int avionMin) {
        int count = 0;
        double moySea = 0.0D;
        ArrayList listPass = new ArrayList();
        int j = (int) (point2d.x - d) / 96;
        int k = (int) (point2d.y - d) / 96;
        int l = (int) (point2d.x + d) / 96;
        int l1 = (int) (point2d.y + d) / 96;
        for (int j1 = k; j1 <= l1; j1++) {
            for (int k1 = j; k1 <= l; k1++) {
                if (typeActivate != 5) {
                    HashMapExt hashmapext = this.mapXY.get(j1, k1);
                    if (hashmapext != null) {
                        for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry)) {
                            Actor actor = (Actor) entry.getKey();
                            int i2 = actor.getArmy();
                            if ((i2 == 0) || (i2 == i) || !actor.isAlive() || listPass.contains(actor)) {
                                continue;
                            }
                            if ((typeActivate == 1) || (typeActivate == 2) || (typeActivate == 4)) {
                                if (!(actor instanceof Aircraft)) {
                                    continue;
                                }
                                if (typeActivate != 4) {
                                    boolean flag = ((SndAircraft) ((Aircraft) actor)).FM.isPlayers() || ((Aircraft) actor).isNetPlayer();
                                    if (((typeActivate == 1) && flag) || ((typeActivate == 2) && !flag)) {
                                        continue;
                                    }
                                }
                            } else if (((typeActivate == 3) && (actor instanceof Aircraft)) || ((typeActivate == 6) && !(actor instanceof TankGeneric) && !(actor instanceof STank) && !(actor instanceof TgtTank)) || ((typeActivate == 7) && ((!(actor instanceof ArtilleryGeneric) && !(actor instanceof SArtillery) && !(actor instanceof TgtFlak)) || (actor instanceof TgtInfantry))) || ((typeActivate == 8) && !(actor instanceof TgtInfantry)) || ((typeActivate == 9) && !(actor instanceof BigshipGeneric) && !(actor instanceof ShipGeneric)) || ((typeActivate == 10) && !(actor instanceof Wagon) && !(actor instanceof SWagon) && !(actor instanceof TgtTrain)) || ((typeActivate == 11) && ((!(actor instanceof CarGeneric) && !(actor instanceof TgtVehicle)) || (actor instanceof TgtInfantry)))) {
                                continue;
                            }
                            Point3d point3d1 = actor.pos.getAbsPoint();
                            if ((point2d.distance(new Point2d(point3d1.x, point3d1.y)) < d) && (point3d1.z <= d2) && (point3d1.z >= d1)) {
                                count++;
                                listPass.add(actor);
                                moySea += point3d1.z;
                                if (count >= avionMin) {
                                    return new CollideEnv.ResultTrigger(true, moySea / count);
                                }
                            }
                        }

                    }
                }
                if ((typeActivate != 1) && (typeActivate != 2) && (typeActivate != 4)) {
                    List list = this.lstXY.get(j1, k1);
                    if (list != null) {
                        int l10 = list.size();
                        for (int j2 = 0; j2 < l10; j2++) {
                            Actor actor = (Actor) list.get(j2);
                            int i2 = actor.getArmy();
                            if ((i2 != 0) && (i2 != i) && actor.isAlive() && !listPass.contains(actor) && ((typeActivate != 5) || (actor instanceof PlaneGeneric)) && ((typeActivate != 6) || (actor instanceof TankGeneric) || (actor instanceof STank) || (actor instanceof TgtTank)) && ((typeActivate != 7) || (((actor instanceof ArtilleryGeneric) || (actor instanceof SArtillery) || (actor instanceof TgtFlak)) && !(actor instanceof TgtInfantry))) && ((typeActivate != 8) || (actor instanceof TgtInfantry)) && ((typeActivate != 9) || (actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && ((typeActivate != 10) || (actor instanceof Wagon) || (actor instanceof SWagon) || (actor instanceof TgtTrain)) && ((typeActivate != 11) || (((actor instanceof CarGeneric) || (actor instanceof TgtVehicle)) && !(actor instanceof TgtInfantry)))) {
                                Point3d point3d1 = actor.pos.getAbsPoint();
                                if ((point2d.distance(new Point2d(point3d1.x, point3d1.y)) < d) && (point3d1.z <= d2) && (point3d1.z >= d1)) {
                                    count++;
                                    listPass.add(actor);
                                    moySea += point3d1.z;
                                    if (count >= avionMin) {
                                        return new CollideEnv.ResultTrigger(true, moySea / count);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }

        return new CollideEnv.ResultTrigger(false);
    }

    protected void changedPos(Actor actor, Point3d point3d, Point3d point3d1) {
        if (actor.collisionR() <= 32F) {
            int i = (int) point3d.x / 32;
            int k = (int) point3d.y / 32;
            int i1 = (int) point3d1.x / 32;
            int k1 = (int) point3d1.y / 32;
            if ((i != i1) || (k != k1)) {
                this.remove(actor, i, k);
                this.add(actor, i1, k1);
            }
        } else {
            int j = (int) (point3d.x / 96D);
            int l = (int) (point3d.y / 96D);
            int j1 = (int) (point3d1.x / 96D);
            int l1 = (int) (point3d1.y / 96D);
            if ((j != j1) || (l != l1)) {
                this.index.make(point3d, actor.collisionR());
                for (int i2 = 0; i2 < this.index.count; i2++) {
                    this.mapXY.remove(this.index.y[i2], this.index.x[i2], actor);
                }

                this.index.make(point3d1, actor.collisionR());
                for (int j2 = 0; j2 < this.index.count; j2++) {
                    this.mapXY.put(this.index.y[j2], this.index.x[j2], actor, null);
                }

            }
        }
    }

    protected void add(Actor actor) {
        if (actor.pos != null) {
            Point3d point3d = actor.pos.getCurrentPoint();
            if (actor.collisionR() <= 32F) {
                this.add(actor, (int) point3d.x / 32, (int) point3d.y / 32);
            } else {
                this.index.make(point3d, actor.collisionR());
                for (int i = 0; i < this.index.count; i++) {
                    this.mapXY.put(this.index.y[i], this.index.x[i], actor, null);
                }

            }
        }
    }

    protected void add(Actor actor, int i, int j) {
        this.index.make(i, j);
        for (int k = 0; k < this.index.count; k++) {
            this.mapXY.put(this.index.y[k], this.index.x[k], actor, null);
        }

    }

    protected void remove(Actor actor) {
        if (actor.pos != null) {
            Point3d point3d = actor.pos.getCurrentPoint();
            if (actor.collisionR() <= 32F) {
                this.remove(actor, (int) point3d.x / 32, (int) point3d.y / 32);
            } else {
                this.index.make(point3d, actor.collisionR());
                for (int i = 0; i < this.index.count; i++) {
                    this.mapXY.remove(this.index.y[i], this.index.x[i], actor);
                }

            }
        }
    }

    private void remove(Actor actor, int i, int j) {
        this.index.make(i, j);
        for (int k = 0; k < this.index.count; k++) {
            this.mapXY.remove(this.index.y[k], this.index.x[k], actor);
        }

    }

    protected void changedPosStatic(Actor actor, Point3d point3d, Point3d point3d1) {
        this.removeStatic(actor);
        this.addStatic(actor);
    }

    protected void addStatic(Actor actor) {
        if (actor.pos != null) {
            Point3d point3d = actor.pos.getCurrentPoint();
            this.index.make(point3d, actor.collisionR());
            for (int i = 0; i < this.index.count; i++) {
                this.lstXY.put(this.index.y[i], this.index.x[i], actor);
            }

        }
    }

    protected void removeStatic(Actor actor) {
        if (actor.pos != null) {
            Point3d point3d = actor.pos.getCurrentPoint();
            this.index.make(point3d, actor.collisionR());
            for (int i = 0; i < this.index.count; i++) {
                this.lstXY.remove(this.index.y[i], this.index.x[i], actor);
            }

        }
    }

    protected void resetGameClear() {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        this.mapXY.allValues(arraylist);
        for (int i = 0; i < arraylist.size(); i++) {
            HashMapExt hashmapext = (HashMapExt) arraylist.get(i);
            arraylist1.addAll(hashmapext.keySet());
            Engine.destroyListGameActors(arraylist1);
        }

        arraylist.clear();
        this.lstXY.allValues(arraylist);
        for (int j = 0; j < arraylist.size(); j++) {
            ArrayList arraylist2 = (ArrayList) arraylist.get(j);
            arraylist1.addAll(arraylist2);
            Engine.destroyListGameActors(arraylist1);
        }

        arraylist.clear();
    }

    protected void resetGameCreate() {
        this.clear();
    }

    protected void clear() {
        this.mapXY.clear();
    }

    protected CollideEnvXY() {
        this.bDoCollision = false;
        this.indexLineX = new int[100];
        this.indexLineY = new int[100];
        this.moved = new HashMapExt();
        this.current = new HashMapExt();
        this._getLineP1 = new Point3d();
        this._getLineRayHit = new Point3d();
        this._getLineLandHit = new Point3d();
        this.p0 = new Point3d();
        this.normal = new Vector3d();
        this.mapXY = new HashMapXY16Hash(7);
        this.lstXY = new HashMapXY16List(7);
        this.index = new CollideEnvXYIndex();
    }

    public static final int    SMALL_STEP  = 32;
    public static final int    STEP        = 96;
    public static final float  STEPF       = 96F;
    public static final float  STEPF_2     = 48F;
    public static float        STATIC_HMAX = 50F;
    private boolean            bDoCollision;
    private int                indexLineX[];
    private int                indexLineY[];
    private double             boundXmin;
    private double             boundXmax;
    private double             boundYmin;
    private double             boundYmax;
    private double             boundZmin;
    private double             boundZmax;
    private HashMapExt         moved;
    private HashMapExt         current;
    private Actor              _bulletActor;
    private String             _bulletChunk;
    private double             _bulletTickOffset;
    private boolean            _bulletArcade;
    private AbstractCollection _getSphereLst;
    private Point3d            _getSphereCenter;
    private double             _getSphereR;
    private Point3d            _getLineP0;
    private Point3d            _getLineP1;
    private double             _getLineLen2;
    private double             _getLineDx;
    private double             _getLineDy;
    private double             _getLineDz;
    private boolean            _getLineBOnlySphere;
    private double             _getLineU;
    private Actor              _getLineA;
    private Point3d            _getLineRayHit;
    private Point3d            _getLineLandHit;
    private Point3d            _getFilteredCenter;
    private ActorFilter        _getFilteredFilter;
    private double             _getFilteredR;
    private Actor              _current;
    private Point3d            _currentP;
    private double             _currentCollisionR;
    private Point3d            _p;
    private Point3d            p0;
    private Vector3d           normal;
    private HashMapXY16Hash    mapXY;
    private HashMapXY16List    lstXY;
    private CollideEnvXYIndex  index;

}
