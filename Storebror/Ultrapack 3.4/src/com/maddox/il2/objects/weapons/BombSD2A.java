package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombSD2A extends Bomb {

    public BombSD2A() {
        this.lStart = 0L;
    }

    protected boolean haveSound() {
        return this.index % 10 == 0;
    }

    private float rotation = 0F;

    public void interpolateTick() {
        super.interpolateTick();
        this.rotation += Aircraft.cvt(Time.current() - this.lStart, 0L, 500L, 0F, -30F);
        this.rotation %= 360F;
        Orient or = this.pos.getAbsOrient();
        or.setYPR(or.getYaw(), or.getTangage(), this.rotation);
        this.pos.setAbs(or);
        this.S = Aircraft.cvt(Time.current() - this.lStart, 0L, 500L, this.bombS, this.bombS * 40F);
    }

    public void start() {
        super.start();
        this.lStart = Time.current();
        this.bombS = this.S;
    }

    protected void doExplosion(Actor actor, String s) {
        Class class1 = this.getClass();
        Point3d point3d = new Point3d();
        this.pos.getTime(Time.current(), point3d);
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        Explosions.AirFlak(point3d, 3);
        if (actor instanceof ActorLand) MsgExplosion.send(actor, s, point3d, this.getOwner(), this.M * 100F, f * 10F, i, f1, 0);
        super.doExplosion(actor, s);
    }

    protected void doExplosion(Actor actor, String s, Point3d point3d) {
        Class class1 = this.getClass();
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        Explosions.AirFlak(point3d, 3);
        if (actor instanceof ActorLand) MsgExplosion.send(actor, s, point3d, this.getOwner(), this.M * 100F, f * 10F, i, f1, 0);
        super.doExplosion(actor, s, point3d);
    }

    private long  lStart;
    private float bombS;

    static {
        Class class1 = BombSD2A.class;
        Property.set(class1, "mesh", "3DO/Arms/SD-2A/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 0.2126F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 2.0284F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
    }
}
