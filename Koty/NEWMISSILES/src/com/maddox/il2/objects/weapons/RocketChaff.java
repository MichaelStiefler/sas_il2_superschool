// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 01.04.2019 0:01:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketChaff_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketChaff

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
        speed.x *= 0.97399999999999998D + TrueRandom.nextDouble(0.0D, 0.0080000000000000002D);
        speed.z -= 7.9000000000000004D + TrueRandom.nextDouble(0.0D, 0.20000000000000001D);
        setSpeed(speed);
    }

    protected void doExplosion(Actor actor, String s)
    {
        destroy();
        Engine.countermeasures().remove(this);
    }

    protected void doExplosionAir()
    {
    }



    private static Point3d p = new Point3d();
    private static Vector3d v = new Vector3d();

    static 
    {
        Class class1 = RocketChaff.class;
        Property.set(class1, "mesh", "3do/arms/Dummy_Transparent_gn16/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/ChaffSmoke_gn16.eff");
        Property.set(class1, "emitColor", new Color3f(0.01F, 0.01F, 0.01F));
        Property.set(class1, "emitLen", 0.0F);
        Property.set(class1, "emitMax", 0.0F);
        Property.set(class1, "sound", (Object)null);
        Property.set(class1, "timeLife", 7F);
        Property.set(class1, "timeFire", 1.0F);
        Property.set(class1, "force", 2.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1E-005F);
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 2.5F);
        Property.set(class1, "massaEnd", 2.0F);
        Property.set(class1, "friendlyName", "Chaff");
        Property.set(class1, "iconFar_shortClassName", "Chaff");
    }
}