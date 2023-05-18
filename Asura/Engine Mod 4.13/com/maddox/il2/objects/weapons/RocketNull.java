// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:20:07
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketNull.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketNull extends Rocket
{

//    public void start(float f, int i)
//    {
//        super.start(f, i);
//        drawing(false);
//    }

    protected void doExplosion(Actor actor1, String s1)
    {
    }

    protected void doExplosionAir()
    {
    }

    public RocketNull()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketNull.class;
        Property.set(class1, "mesh", "3DO/Arms/Null/mono.sim");
        Property.set(class1, "sprite", 0.0F);
        Property.set(class1, "flame", 0.0F);
        Property.set(class1, "smoke", 0.0F);
        Property.set(class1, "emitColor", new Color3f(0.0F, 0.0F, 0.0F));
        Property.set(class1, "emitLen", 0.0F);
        Property.set(class1, "emitMax", 0.0F);
        Property.set(class1, "sound", 0.0F);
        Property.set(class1, "radius", 0.1F);
        Property.set(class1, "timeLife", 999.999F);
        Property.set(class1, "timeFire", 0.0F);
        Property.set(class1, "force", 0.0F);
        Property.set(class1, "power", 0.01485F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.001F);
        Property.set(class1, "massa", 0.01485F);
        Property.set(class1, "massaEnd", 0.01485F);
    }
}