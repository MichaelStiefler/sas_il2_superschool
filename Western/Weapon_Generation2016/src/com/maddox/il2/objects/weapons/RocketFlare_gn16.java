
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;
import com.maddox.sas1946.il2.util.TrueRandom;


public class RocketFlare_gn16 extends RocketFlare
{

    public RocketFlare_gn16()
    {
    }

    public void start(float f, int i)
    {
        float f1 = 30F;
        super.start(f1, i);
        getOwner().getSpeed(speed);
        speed.x *= (0.969D + TrueRandom.nextDouble(0.00D, 0.012D));
        speed.y += TrueRandom.nextDouble(-0.8D, 0.8D);
        speed.z -= (7.8D + TrueRandom.nextDouble(0.0D, 0.4D));
        setSpeed(speed);
//        Eff3DActor.New(this, null, new Loc(), 1.0F, "3do/Effects/Fireworks/Piropatron.eff", f1);
//        Eff3DActor.New(this, null, new Loc(), 0.8F, "3do/Effects/Tracers/Piropatron/Smokeflare.eff", f1);
    }

    protected void doExplosion(Actor actor, String s)
    {
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir()
    {
    }

    private static Point3d p = new Point3d();
    private static Vector3d v = new Vector3d();

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketFlare_gn16.class;
        Property.set(class1, "mesh", "3do/arms/Piropatron/mono.sim");
        Property.set(class1, "sprite", (Object)null);
        Property.set(class1, "flame", (Object)null);
        Property.set(class1, "smoke", (Object)null);
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 5F);
        Property.set(class1, "emitMax", 20F);
        Property.set(class1, "sound", (Object)null);
        Property.set(class1, "timeLife", 5F);
        Property.set(class1, "timeFire", 0.001F);
        Property.set(class1, "force", 2F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1E-005F);
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 3F);
        Property.set(class1, "massaEnd", 3F);
        Property.set(class1, "friendlyName", "Flare");
        Property.set(class1, "iconFar_shortClassName", "Flare");
    }
}
