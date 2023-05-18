package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.engine.*;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile, MissileInterceptable

public class RocketChaff extends Rocket
{

    public RocketChaff()
    {
    }

    public void start(float f, int i)
    {
        float f1 = 30F;
        super.start(f1, i);
        getOwner().getSpeed(speed);
        speed.z = -50D;
        setSpeed(speed);
        Engine.countermeasures().add(this);
    }

    protected void doExplosion(Actor actor, String s)
    {
        pos.getTime(Time.current(), p);
        Class class1 = getClass();
        float f = Property.floatValue(class1, "power", 1000F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        getSpeed(speed);
        Vector3f vector3f = new Vector3f(speed);
        vector3f.normalize();
        vector3f.scale(850F);
        MsgShot.send(actor, s, p, vector3f, M, getOwner(), (float)((double)(0.5F * M) * speed.lengthSquared()), 3, 0.0D);
        MsgExplosion.send(actor, s, p, getOwner(), M, f, i, f1);
        destroy();
        
//        Engine.missiles().remove(this);
    }

    protected void doExplosionAir()
    {
    }

    private static Point3d p = new Point3d();
    private static Vector3d v = new Vector3d();

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketChaff.class;
        Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
        Property.set(class1, "sprite", (Object)null);
        Property.set(class1, "flame", (Object)null);
        Property.set(class1, "smoke", (Object)null);
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 5F);
        Property.set(class1, "emitMax", 20F);
        Property.set(class1, "sound", (Object)null);
        Property.set(class1, "timeLife", 7F);
        Property.set(class1, "timeFire", 0.2F);
        Property.set(class1, "force", 20F);
        Property.set(class1, "dragCoefficient", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1E-005F);
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "kalibr", 0.001F);
        Property.set(class1, "massa", 3F);
        Property.set(class1, "massaEnd", 20000F);
        Property.set(class1, "shotFreq", 3F);

    }
}