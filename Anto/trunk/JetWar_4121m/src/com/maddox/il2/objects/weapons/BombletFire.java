package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombletFire extends Bomb
{

    public BombletFire()
    {
    }

    public void start()
    {
        super.start();
        Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/NapalmFire.eff", 5F);
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        Point3d point3d = new Point3d();
        pos.getTime(Time.current(), point3d);
        Class class1 = getClass();
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 1.0F);
        MsgExplosion.send(actor, s1, point3d, getOwner(), M, f, i, f1);
        super.msgCollision(actor, s, s1);
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombletFire.class;
        Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
        Property.set(class1, "emitColor", new Color3f(1.0F, 0.5F, 0.5F));
        Property.set(class1, "emitLen", 20F);
        Property.set(class1, "emitMax", 0.5F);
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 2.0F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}