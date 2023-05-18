// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:19:26
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketFlare.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketFlare extends Rocket
{

    public RocketFlare()
    {
    }

    public void start(float f, int i)
    {
        float f1 = 30F;
        super.start(f1, i);
        super.speed.z = -20D;
        super.speed.x = 0.0D;
        super.speed.y = 0.0D;
        Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
        vector3d.x += World.Rnd().nextFloat_Dome(-2F, 2.0F);
        vector3d.y += World.Rnd().nextFloat_Dome(-1.2F, 1.2F);
        super.speed.add(vector3d);
        setSpeed(super.speed);
        Eff3DActor.New(this, null, new Loc(), 1.0F, "3do/Effects/Fireworks/Piropatron.eff", f1);
        Eff3DActor.New(this, null, new Loc(), 0.8F, "3do/effects/rocket/rocketsmokewhitestart.eff", f1);
        Engine.countermeasures().add(this);
    }

    protected void doExplosion(Actor actor, String s)
    {
        destroy();
        Engine.missiles().remove(this);
    }

    protected void doExplosionAir()
    {
    }

    static Orient Or = new Orient();
    private static Point3d p = new Point3d();
    private static Vector3d v = new Vector3d();

    static 
    {
    	Class class1 = com.maddox.il2.objects.weapons.RocketFlare.class;
        Property.set(class1, "mesh", "3do/arms/Piropatron/mono.sim");
        //Property.set(class1, "sprite", "");
        Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
        Property.set(class1, "smoke", "3do/effects/rocket/rocketsmokewhite.eff");
        Property.set(class1, "smokeStart", "3do/effects/rocket/rocketsmokewhitestart.eff");
        Property.set(class1, "smokeTile", "3do/effects/rocket/rocketsmokewhitetile.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 5F);
        Property.set(class1, "emitMax", 20F);
        //Property.set(class1, "sound", "");
        Property.set(class1, "timeLife", 1.5F);
        Property.set(class1, "timeFire", 0.3F);
        Property.set(class1, "force", 210F);
        Property.set(class1, "dragCoefficient", 0.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 0.01F);
        Property.set(class1, "radius", 0.01F);
        Property.set(class1, "kalibr", 0.03F);
        Property.set(class1, "massa", 3F);
        Property.set(class1, "massaEnd", 1.0F);
        Property.set(class1, "shotFreq", 3F);
    }
}