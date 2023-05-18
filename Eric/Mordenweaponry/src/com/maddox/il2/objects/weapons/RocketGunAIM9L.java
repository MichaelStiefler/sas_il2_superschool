// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/5/2012 9:19:02 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAIM9L.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.*;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MissileGun, Missile, Rocket

public class RocketGunAIM9L extends MissileGun
implements RocketGunWithDelay
{

    public RocketGunAIM9L()
    {
    }

    public void updateHook(String s)
    {
        if(((Missile)super.rocket).isReleased())
            return;
        Class class1 = getClass();
        bullets(Property.intValue(class1, "bullets", 1));
        super.hook = (HookNamed)super.actor.findHook(s);
        try
        {
            super.rocket.destroy();
            super.rocket = (Rocket)super.bulletClass.newInstance();
            ((Actor) (super.rocket)).pos.setBase(super.actor, super.hook, false);
            ((Actor) (super.rocket)).pos.changeHookToRel();
            ((Actor) (super.rocket)).pos.resetAsBase();
            super.rocket.visibilityAsBase(true);
            ((Actor) (super.rocket)).pos.setUpdateEnable(false);
        }
        catch(Exception exception) { }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAIM9L.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileAIM9L.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}