package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombAB1000 extends Bomb {

    public BombAB1000() {
        this.bombletsTotal = this.bombletsLeft = 372;
        this.fireTime = 5000;
        this.fireContainedStarted = false;
        this.minFirePerTick = (int) ((float) this.bombletsLeft * (float) Time.tickLen() / this.fireTime);
    }

    public void start() {
        super.start();
        this.t1 = Time.current() + Math.max(this.armingTime, 1500L) + World.Rnd().nextLong(-250L, 250L);
        this.t2 = this.t1 + this.fireTime;
        this.charge = 0;
        this.setName("qqq");
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.t1 < Time.current()) this.doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (this.t1 > Time.current() && this.isFuseArmed()) this.doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds() {
        if (!this.fireContainedStarted) {
            Explosions.AirFlak(this.pos.getAbsPoint(), 1);
            this.fireContainedStarted = true;
        }
        if (this.t2 < Time.current() || this.bombletsLeft < 1) {
            this.doFireContaineds(this.bombletsLeft);
            Explosions.AirFlak(this.pos.getAbsPoint(), 1);
            this.postDestroy();
            return;
        }
        int numToFire = this.bombletsLeft - (int) ((float) this.bombletsTotal * (float) (this.t2 - Time.current()) / this.fireTime);
        if (numToFire < this.minFirePerTick) numToFire = this.minFirePerTick;
        this.doFireContaineds(numToFire);
    }

    private void doFireContaineds(int numToFire) {
        if (numToFire > this.bombletsLeft) numToFire = this.bombletsLeft;
        if (numToFire <= 0) return;
        int chargeOld = this.charge;
        this.charge = 1 + (this.bombletsTotal - this.bombletsLeft) / 74;
        if (this.charge > 5) this.charge = 5;
        if (chargeOld != this.charge) Explosions.AirFlak(this.pos.getAbsPoint(), 1);
        Actor actor = Actor.isValid(this.getOwner()) ? this.getOwner() : null;
        Point3d point3d = new Point3d();
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        Loc loc = new Loc();
        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        this.pos.getCurrent(loc);
        this.findHook("_Spawn0" + this.charge).computePos(this, loc, loc1);
        this.getSpeed(vector3d1);
        while (numToFire-- > 0) {
            loc1.get(point3d, orient);
            orient.increment(World.Rnd().nextBoolean() ? World.Rnd().nextFloat(-10F, -170F) : World.Rnd().nextFloat(10F, 170F), World.Rnd().nextFloat(-20F, 20F), World.Rnd().nextFloat(-180F, 180F));
            vector3d.set(1.0D, 0.0D, 0.0D);
            orient.transform(vector3d);
            vector3d.scale(World.Rnd().nextDouble(3D, 20D));
            vector3d.add(vector3d1);
            BombB22EZ bombb22ez = new BombB22EZ();
            bombb22ez.pos.setUpdateEnable(true);
            bombb22ez.pos.setAbs(point3d, orient);
            bombb22ez.pos.reset();
            bombb22ez.delayExplosion = this.delayExplosion;
            bombb22ez.start();
            bombb22ez.setOwner(actor, false, false, false);
            bombb22ez.setSpeed(vector3d);
            if (this.bombletsLeft-- % 4 == 0) Eff3DActor.New(bombb22ez, null, null, 3F, "effects/Smokes/SmokeBlack_BuletteTrail.eff", 30F);
        }
    }

    private long    t1;
    private int     charge;
    private long    t2;
    private int     bombletsLeft;
    private int     bombletsTotal;
    private int     minFirePerTick;
    private long    fireTime;
    private boolean fireContainedStarted;

    static {
        Class class1 = BombAB1000.class;
        Property.set(class1, "mesh", "3DO/Arms/AB-1000/mono.sim");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.6604F);
        Property.set(class1, "massa", 905.3F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
