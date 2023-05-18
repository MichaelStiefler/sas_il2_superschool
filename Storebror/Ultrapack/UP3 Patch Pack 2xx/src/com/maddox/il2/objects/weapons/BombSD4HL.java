package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombSD4HL extends Bomb
{

    public BombSD4HL()
    {
        this.lStart = 0L;
    }

    protected boolean haveSound()
    {
        return index % 10 == 0;
    }
    
    public void interpolateTick() {
        super.interpolateTick();
        getSpeed(v3d);
        this.S = Aircraft.cvt(Time.current() - this.lStart, 0L, 2000L, this.bombS, this.bombS * 0.5F);
    }

    public void start() {
        super.start();
        this.lStart = Time.current();
        this.bombS = this.S;
    }

    protected void doExplosion(Actor actor, String s)
    {
//        System.out.println("BombSD4HL doExplosion(" + actor.getClass().getName() + ", " + s + ")");
        Class class1 = getClass();
        Point3d point3d = new Point3d();
        pos.getTime(Time.current(), point3d);
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        Explosions.AirFlak(point3d, 3);
        if (actor instanceof ActorLand) {
            MsgExplosion.send(actor, s, point3d, getOwner(), M * 100F, f * 10F, i, f1, 0);
            
            Actor target = NearestTargets.getEnemy(1, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(3, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(4, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(2, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(0, -1, point3d, f1, 0);
            if (target != null) MsgExplosion.send(target, "Body", point3d, getOwner(), M * 100F, f * 10F, i, f1, 0);
        }
        super.doExplosion(actor, s);
    }

    protected void doExplosion(Actor actor, String s, Point3d point3d)
    {
//        System.out.println("BombSD4HL doExplosion(" + actor.getClass().getName() + ", " + s + ", point3d)");
        Class class1 = getClass();
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        Explosions.AirFlak(point3d, 3);
        if (actor instanceof ActorLand) {
            MsgExplosion.send(actor, s, point3d, getOwner(), M * 100F, f * 10F, i, f1, 0);
            
            Actor target = NearestTargets.getEnemy(1, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(3, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(4, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(2, -1, point3d, f1, 0);
            if (target == null) target = NearestTargets.getEnemy(0, -1, point3d, f1, 0);
            if (target != null) MsgExplosion.send(target, "Body", point3d, getOwner(), M * 100F, f * 10F, i, f1, 0);
        }
        super.doExplosion(actor, s, point3d);
    }

    private static Vector3d v3d = new Vector3d();
    private long lStart;
    private float bombS;

    static 
    {
        Class class1 = BombSD4HL.class;
        Property.set(class1, "mesh", "3DO/Arms/SD-4HL/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 0.345384F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.35F);
        Property.set(class1, "massa", 4.264F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
