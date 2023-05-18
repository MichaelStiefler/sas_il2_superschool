// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 02.10.2020 3:10:04
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketS25_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS25_gn16 extends Rocket
{

    public RocketS25_gn16()
    {
    }



    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketS25_gn16.class;
        Property.set(class1, "mesh", "3do/arms/S25_Rocket_gn16/S25_fold.sim");
        Property.set(class1, "meshFly", "3do/arms/S25_Rocket_gn16/S25.sim");
        Property.set(class1, "sprite", "3do/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/Effects/RocketS25/RocketS25Flame.sim");
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/TurboHWK109D.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 98F);
        Property.set(class1, "timeLife", 120F);
        Property.set(class1, "timeFire", 5F);
        Property.set(class1, "force", 1400F);
        Property.set(class1, "power", 175F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 480F);
        Property.set(class1, "massaEnd", 260F);
        Property.set(class1, "spinningStraightFactor", 1.8F);
        Property.set(class1, "friendlyName", "S-25");
    }
}