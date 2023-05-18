// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 02.01.2020 13:08:47
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketFlareLO56_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketFlare_gn16, Rocket

public class RocketFlareLO56_gn16 extends RocketFlare_gn16
{

    public RocketFlareLO56_gn16()
    {
    }

    public void start(float f, int i)
    {
        float f1 = 30F;
        super.start(f1, i);
        getOwner().getSpeed(super.speed);
        setSpeed(super.speed);
        if(Config.isUSE_RENDER())
        {
            Loc loc = new Loc();
            loc.set(0.0D, 0.0D, 0.0D, 180F, 0.0F, 0.0F);
            eff1 = Eff3DActor.New(this, null, loc, 0.8F, "3DO/Effects/Tracers/GuidedRocket/White.eff", f1);
        }
        t1 = Time.current() + 5000L;
        Engine.countermeasures().add(this);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(t1 + TrueRandom.nextLong(2000L) < Time.current())
            destroy();
    }

    public void destroy()
    {
        if(Config.isUSE_RENDER())
            Eff3DActor.finish(eff1);
        super.destroy();
    }

    protected void doExplosion(Actor actor, String s)
    {
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir()
    {
    }

    private long t1;
    private Eff3DActor eff1;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketFlareLO56_gn16.class;
        Property.set(class1, "mesh", "3do/arms/Piropatron/mono.sim");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 5F);
        Property.set(class1, "emitMax", 20F);
        Property.set(class1, "timeLife", 8.0F);
        Property.set(class1, "timeFire", 0.001F);
        Property.set(class1, "force", 500F);
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