package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class BombABKP extends Bomb {

    public BombABKP() {
        this.bursted = false;
    }

    public void start() {
        super.start();
//        setStartDelayedExplosion(true);
        if (/*fuze == null || */this.delayExplosion == 0.0F) {
            this.t1 = Time.current() + 3000L;
        } else {
            this.t1 = Time.current() + (long) (1000F * this.delayExplosion);
        }
    }

    public void interpolateTick() {
        super.interpolateTick();
        if ((this.t1 < Time.current()) && !this.bursted) {
            this.doFireContaineds();
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        super.msgCollision(actor, s, s1);
        this.destroy();
    }

    protected void doFireContaineds() {
    }

    protected void doFireContaineds(int i, Class class1, Class class2, Class class3, Class class4) {
        this.bursted = true;
        Actor actor = null;
        if (Actor.isValid(this.getOwner())) {
            actor = this.getOwner();
        }
        Point3d point3d = new Point3d(this.pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        this.getSpeed(vector3d);
        Bomb bomb;
        try {
            bomb = (Bomb) class2.newInstance();
        } catch (Exception exception) {
            return;
        }
        bomb.pos.setUpdateEnable(true);
        bomb.pos.setAbs(point3d, this.pos.getAbsOrient());
        bomb.pos.reset();
        bomb.start();
        bomb.setOwner(actor, false, false, false);
        vector3d.add(3D, 0.0D, -0.7D);
        bomb.setSpeed(vector3d);
        Bomb bomb1;
        try {
            bomb1 = (Bomb) class3.newInstance();
        } catch (Exception exception1) {
            return;
        }
        bomb1.pos.setUpdateEnable(true);
        bomb1.pos.setAbs(point3d, this.pos.getAbsOrient());
        bomb1.pos.reset();
        bomb1.start();
        bomb1.setOwner(actor, false, false, false);
        vector3d.add(3D, 0.0D, -0.7D);
        bomb1.setSpeed(vector3d);
        Bomb bomb2;
        try {
            bomb2 = (Bomb) class4.newInstance();
        } catch (Exception exception2) {
            return;
        }
        bomb2.pos.setUpdateEnable(true);
        bomb2.pos.setAbs(point3d, this.pos.getAbsOrient());
        bomb2.pos.reset();
        bomb2.start();
        bomb2.setOwner(actor, false, false, false);
        vector3d.add(3D, 0.0D, -0.7D);
        bomb2.setSpeed(vector3d);
        for (int j = 0; j < i; j++) {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            this.getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-20D, 20D), World.Rnd().nextDouble(-20D, 20D), World.Rnd().nextDouble(-20D, 20D));
            Bomb bomb3;
            try {
                bomb3 = (Bomb) class1.newInstance();
            } catch (InstantiationException instantiationexception) {
                return;
            } catch (IllegalAccessException illegalaccessexception) {
                return;
            }
            bomb3.pos.setUpdateEnable(true);
            bomb3.pos.setAbs(point3d, orient);
            bomb3.pos.reset();
            bomb3.start();
            bomb3.setOwner(actor, false, false, false);
            bomb3.setSpeed(vector3d);
        }

        String s = Property.stringValue(this.getClass(), "mesh_tail", null);
        this.setMesh(MeshShared.get(s));
    }

    protected boolean bursted;
    private long      t1;
}
