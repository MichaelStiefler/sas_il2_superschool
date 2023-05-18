// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 06.01.2019 15:19:10
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketS24.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS24 extends Rocket
{

    public RocketS24()
    {
    }



    static 
    {
        Class class1 = RocketS24.class;
        Property.set(class1, "mesh", "3DO/Arms/S-24/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/TurboHWK109D.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 75F);
        Property.set(class1, "timeLife", 120F);
        Property.set(class1, "timeFire", 1.1F);
        Property.set(class1, "force", 87590F);
        Property.set(class1, "power", 40F);//125
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.0576F);
        Property.set(class1, "massa", 235F);
        Property.set(class1, "massaEnd", 163F);
        Property.set(class1, "friendlyName", "S-24");
    }
}