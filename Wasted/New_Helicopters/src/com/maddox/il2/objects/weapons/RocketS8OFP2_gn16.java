// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10.02.2013 0:51:49
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketS5.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS8OFP2_gn16 extends Rocket
{

    public RocketS8OFP2_gn16()
    {
    }

    public void start(float f, int i)
    {
        super.start(f, i);
        super.noGDelay = -1L;
    }
    
    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketS8OFP2_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFAR.sim");
        Property.set(class1, "meshFly", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFARfly.sim");
        Property.set(class1, "sprite", (Object)null);
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/effects/rocket/rocketsmokewhitetile.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 2.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "timeLife", 5F);
        Property.set(class1, "timeFire", 0.88F);
        Property.set(class1, "force", 5500F);
        Property.set(class1, "power", 2.9F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.080F);
        Property.set(class1, "spinningStraightFactor", 1.5F);
        Property.set(class1, "maxDeltaAngle", 0.2F);
        Property.set(class1, "massa", 16.7F);
        Property.set(class1, "massaEnd", 13.53F);
        Property.set(class1, "friendlyName", "S-8");
        
    }
}