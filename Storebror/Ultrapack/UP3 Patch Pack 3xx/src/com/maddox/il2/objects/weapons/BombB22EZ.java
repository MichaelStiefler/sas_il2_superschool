package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombB22EZ extends Bomb
{

    public BombB22EZ()
    {
        this.lStart = 0L;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void interpolateTick() {
        super.interpolateTick();
        getSpeed(v3d);
        this.S = Aircraft.cvt(Time.current() - this.lStart, 0L, 1500L, this.bombS, this.bombS * 0.2F);
    }

    public void start() {
        super.start();
        this.lStart = Time.current();
        this.bombS = this.S;
    }

    protected void doExplosion(Actor actor, String s)
    {
//        System.out.println("BombSD2A doExplosion(" + actor.getClass().getName() + ", " + s + ")");
        Class class1 = getClass();
        Point3d point3d = new Point3d();
        pos.getTime(Time.current(), point3d);
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        Explosions.AirFlak(point3d, 3);
        if (actor instanceof ActorLand) {
            if (World.Rnd().nextBoolean())
                MsgExplosion.send(actor, s, point3d, getOwner(), M * 100F, f * 10F, i, f1*25F, 0);
            else
                MsgExplosion.send(actor, s, point3d, getOwner(), M * 100F, f * 10F, 1, f1*25F, 0);
        }
        super.doExplosion(actor, s);
    }

    protected void doExplosion(Actor actor, String s, Point3d point3d)
    {
//        System.out.println("BombSD2A doExplosion(" + actor.getClass().getName() + ", " + s + ")");
        Class class1 = getClass();
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        Explosions.AirFlak(point3d, 3);
        if (actor instanceof ActorLand) {
            if (World.Rnd().nextBoolean())
                MsgExplosion.send(actor, s, point3d, getOwner(), M * 100F, f * 10F, i, f1*25F, 0);
            else
                MsgExplosion.send(actor, s, point3d, getOwner(), M * 100F, f * 10F, 1, f1*25F, 0);
        }
        super.doExplosion(actor, s, point3d);
    }

    private static Vector3d v3d = new Vector3d();
    private long lStart;
    private float bombS;

    static 
    {
        Class class1 = BombB22EZ.class;
        Property.set(class1, "mesh", "3DO/Arms/B22EZ/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.6F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.254F);
        Property.set(class1, "massa", 2.2F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
    }
}
