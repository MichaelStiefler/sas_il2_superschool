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

public class BombAB500 extends Bomb
{

    public BombAB500()
    {
        this.bombletsTotal = this.bombletsLeft = 74;
        this.fireTime = 1000;
        this.fireContainedStarted = false;
        this.minFirePerTick = (int)((float)this.bombletsLeft * (float)Time.tickLen() / (float)this.fireTime); 
    }

    public void start()
    {
        super.start();
        t1 = Time.current() + (long)Math.max(this.armingTime, 1000L) + World.Rnd().nextLong(-200L, 200L);
        t2 = t1 + this.fireTime;
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(t1 < Time.current())
            doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(t1 > Time.current() && isFuseArmed())
            doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds()
    {
        if (!this.fireContainedStarted) {
            Explosions.AirFlak(pos.getAbsPoint(), 1);
            this.fireContainedStarted = true;
        }
        if(t2 < Time.current() || this.bombletsLeft < 1) {
            this.doFireContaineds(this.bombletsLeft);
            Explosions.AirFlak(pos.getAbsPoint(), 1);
            postDestroy();
            return;
        }
        int numToFire = this.bombletsLeft - (int)((float)this.bombletsTotal * (float)(t2 - Time.current()) / (float)this.fireTime);
        if (numToFire < this.minFirePerTick) numToFire = this.minFirePerTick;
        this.doFireContaineds(numToFire);
    }

    private void doFireContaineds(int numToFire)
    {
        if (numToFire > this.bombletsLeft) numToFire = this.bombletsLeft;
        if (numToFire == 0) return;
        Actor actor = (Actor.isValid(getOwner()))?getOwner():null;
        Point3d point3d = new Point3d();
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        Loc loc = new Loc();
        pos.getCurrent(loc);
        getSpeed(vector3d1);
        while (numToFire-- > 0) {
            loc.get(point3d, orient);
            orient.increment(World.Rnd().nextFloat(-180F, 180F), World.Rnd().nextFloat(-180F, 0F), World.Rnd().nextFloat(180F, -180F));
            vector3d.set(1.0D, 0.0D, 0.0D);
            orient.transform(vector3d);
            vector3d.scale(World.Rnd().nextDouble(3D, 7D));
            vector3d.add(vector3d1);
            BombSD4HL bombsd4hl = new BombSD4HL();
            bombsd4hl.pos.setUpdateEnable(true);
            bombsd4hl.pos.setAbs(point3d, orient);
            bombsd4hl.pos.reset();
            bombsd4hl.delayExplosion = 0F; // Hollow Charge Bombs don't have no delayed explosion, that's by design.
            bombsd4hl.start();
            bombsd4hl.setOwner(actor, false, false, false);
            bombsd4hl.setSpeed(vector3d);
            if(bombletsLeft-- % 4 == 0) Eff3DActor.New(bombsd4hl, null, null, 3F, "effects/Smokes/SmokeBlack_BuletteTrail.eff", 30F);
        }
    }

    private long t1;
    private long t2;
    private int bombletsLeft;
    private int bombletsTotal;
    private int minFirePerTick;
    private long fireTime;
    private boolean fireContainedStarted;

    static 
    {
        Class class1 = BombAB500.class;
        Property.set(class1, "mesh", "3DO/Arms/AB-500/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.18F);
        Property.set(class1, "massa", 335.336F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
