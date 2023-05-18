// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 01.09.2018 11:10:47
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketS8KOM.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS8KOM extends Rocket
{

    public RocketS8KOM()
    {
    }

    public void start(float f, int i)
    {
        setMesh("3DO/Arms/FFAR_275inch/mono_launched.sim");
        super.start(f, i);
    }



    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketS8KOM.class;
        Property.set(class1, "mesh", "3DO/Arms/FFAR_275inch/mono.sim");
        Property.set(class1, "sprite", (String)null);
        Property.set(class1, "flame", "3do/Effects/RocketKS1/RocketKS1Flame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketKS1/RocketKS1Smoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 2.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 0.2F);
        Property.set(class1, "timeLife", 60F);
        Property.set(class1, "timeFire", 2.1F);
        Property.set(class1, "force", 4000F);
        Property.set(class1, "power", 1.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.08F);
        Property.set(class1, "massa", 11.3F);
        Property.set(class1, "massaEnd", 6.5F);
        Property.set(class1, "friendlyName", "S-8");
    }
}