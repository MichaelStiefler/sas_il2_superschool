package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class BombRRAB extends Bomb {

    public BombRRAB() {
        this.bursted = false;
    }

    public void start() {
        super.start();
        String s = Property.stringValue(this.getClass(), "mesh_released", null);
        this.setMesh(MeshShared.get(s));
//		this.setStartDelayedExplosion(true);
        this.t1 = Time.current() + 1000L * (long) Math.max(this.delayExplosion, 3F) + World.Rnd().nextLong(-250L, 250L);
    }

    public void interpolateTick() {
        super.interpolateTick();
        // if(rotatingSpeed > 30F && !bursted)
        if (this.t1 < Time.current() && !this.bursted) this.doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        super.msgCollision(actor, s, s1);
        this.destroy();
    }

    protected void doFireContaineds() {
    }

    protected void doFireContaineds(int i, int j, Class class1, Class class2) {
        this.bursted = true;
        Actor actor = null;
        if (Actor.isValid(this.getOwner())) actor = this.getOwner();
        Point3d point3d = new Point3d(this.pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        Point3d point3d1 = new Point3d();
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
        float f = 0.0F;
        int k = 0;
        float f5 = 0.15F;
        float f6 = 0.6F;
        if (class2.getName().indexOf("BombRRAB1Nose") > 0) {
            f6 = 0.7F;
            f5 = 2.0F;
        } else if (class2.getName().indexOf("BombRRAB2Nose") > 0) {
            f6 = 0.65F;
            f5 = 0.175F;
        }
        int l = i / j + 1;
        double d = 2D * Math.PI / l;
        for (int i1 = 0; i1 < i; i1++) {
            float f2 = (float) Math.sin(k * d);
            float f4 = (float) Math.cos(k * d);
            k++;
            if (i1 % l == 0) {
                f -= f6;
                k = 0;
            }
            orient.set(this.pos.getAbsOrient().getYaw() + World.Rnd().nextFloat(-5F, 5F), this.pos.getAbsOrient().getPitch() + World.Rnd().nextFloat(-5F, 5F), 0.0F);
            this.getSpeed(vector3d);
            vector3d1.set(World.Rnd().nextDouble(-12D, 12D), f2 * -8F + World.Rnd().nextFloat(-3F, 3F), f4 * -8F + World.Rnd().nextFloat(-3F, 3F));
            this.pos.getAbsOrient().transform(vector3d1);
            vector3d.add(vector3d1);
            Bomb bomb1;
            try {
                bomb1 = (Bomb) class1.newInstance();
            } catch (InstantiationException instantiationexception) {
                return;
            } catch (IllegalAccessException illegalaccessexception) {
                return;
            }
            bomb1.pos.setUpdateEnable(true);
            point3d1.set(f * f5 - 1.0F, f2 * f5, f4 + 1.5F);
            orient.set(this.pos.getAbsOrient());
            orient.setRoll(0.0F);
            orient.transform(point3d1);
            point3d.set(this.pos.getAbsPoint());
            point3d.add(point3d1);
            bomb1.pos.setAbs(point3d, orient);
            bomb1.pos.reset();
            bomb1.start();
            bomb1.setOwner(actor, false, false, false);
            bomb1.setSpeed(vector3d);
        }

        String s = Property.stringValue(this.getClass(), "mesh_tail", null);
        this.setMesh(MeshShared.get(s));
    }

    private long      t1;
    protected boolean bursted;
}
