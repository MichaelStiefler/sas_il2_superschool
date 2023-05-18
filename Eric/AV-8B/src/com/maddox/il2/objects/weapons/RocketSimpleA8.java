// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 8/31/2013 9:33:37 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketSimpleA8.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketSimple

public class RocketSimpleA8 extends RocketSimple
{

    public RocketSimpleA8(Point3d point3d, Orient orient, Actor actor)
    {
        super(point3d, orient, actor);
    }
    
    public void start(float f)
    {
        float f1 = f-1F;
        super.start(f1);
        //Eff3DActor.New(this, null, new Loc(), 1.0F, "EFFECTS/Smokes/SmokeMissilessmall.eff", -1F);
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketSimpleA8.class;
        Property.set(class1, "mesh", "3DO/Arms/2-75inch/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        //Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        //Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 1.5F);
        Property.set(class1, "timeLife", 60F);
        Property.set(class1, "timeFire", 1.1F);
        Property.set(class1, "force", 2300F);
        Property.set(class1, "power", 10.8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.055F);
        Property.set(class1, "massa", 3.86F);
        Property.set(class1, "massaEnd", 3F);
    }
}