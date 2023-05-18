package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombTiBase2 extends Bomb {

    public void start() {
        super.start();
        this.t1 = Time.current() + (1000L * (long) Math.max(this.delayExplosion, 4.21F)) + World.Rnd().nextLong(-250L, 250L);
        this.charge = 0;
        this.setName("qqq");
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.t1 < Time.current()) {
            this.doFireContaineds();
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (this.t1 > Time.current()) {
            this.doFireContaineds();
        }
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds() {
        this.charge++;
        if (this.charge < 1.01D) {
            Actor actor = this.getOwner();
            if (Actor.isValid(actor)) {
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                Vector3d vector3d = new Vector3d();
                Vector3d vector3d1 = new Vector3d();
                Loc loc = new Loc();
                Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                this.pos.getCurrent(loc);
                this.findHook("_Spawn0" + this.charge).computePos(this, loc, loc1);
                this.getSpeed(vector3d1);
                for (int i = 0; i < 60; i++) {
                    loc1.get(point3d, orient);
                    orient.increment(World.Rnd().nextFloat(-85F, 85F), World.Rnd().nextFloat(-85.5F, 85.5F), World.Rnd().nextFloat(-0.5F, 0.5F));
                    vector3d.set(1.0D, 0.0D, 0.0D);
                    orient.transform(vector3d);
                    vector3d.scale(World.Rnd().nextDouble(5D, 12.2D));
                    vector3d.add(vector3d1);
                    Bomb bomb = this.spawnNewBomb();
                    bomb.pos.setUpdateEnable(true);
                    bomb.pos.setAbs(point3d, orient);
                    bomb.pos.reset();
                    bomb.start();
                    bomb.setOwner(actor, false, false, false);
                    bomb.setSpeed(vector3d);
                }

                this.t1 = Time.current() + 1000L;
            }
        }
    }

    protected Bomb spawnNewBomb() {
        return null;
    }

    protected static void initStatic(Class class1) {
        Property.set(class1, "mesh", "3DO/Arms/Flare/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 125F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }

    protected long t1;
    protected int  charge;
}
