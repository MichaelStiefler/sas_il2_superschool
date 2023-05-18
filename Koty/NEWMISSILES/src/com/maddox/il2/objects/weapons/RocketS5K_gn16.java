// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 02.10.2020 3:09:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketS5K_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS5K_gn16 extends Rocket
{

    public RocketS5K_gn16()
    {
    }



    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketS5K_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/S5_Rocket_gn16/mono.sim");
        Property.set(class1, "meshFly", "3DO/Arms/S5_Rocket_gn16/mono_open.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 0.5F);
        Property.set(class1, "timeLife", 15F);
        Property.set(class1, "timeFire", 0.75F);
        Property.set(class1, "force", 8300F);
        Property.set(class1, "power", 1.13F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.057F);
        Property.set(class1, "massa", 3.65F);
        Property.set(class1, "massaEnd", 2.76F);
        Property.set(class1, "friendlyName", "S5K");
    }
}