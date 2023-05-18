// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:27:18 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAIM_9B.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeAIM9Carrier;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunAIM_9B extends RocketGun
{

    public RocketGunAIM_9B()
    {
    }

    public float bulletMassa()
    {
        return super.bulletMassa / 10F;
    }

    public void shots(int paramInt)
    {
        try
        {
            if(Actor.isValid(super.actor) && (super.actor instanceof Aircraft) && (super.actor instanceof TypeAIM9Carrier) && (Aircraft)super.actor == World.getPlayerAircraft() && ((RealFlightModel)((SndAircraft) ((Aircraft)super.actor)).FM).isRealMode() && ((TypeAIM9Carrier)super.actor).hasMissiles() && ((TypeAIM9Carrier)super.actor).getMissileLockState() == 0)
            {
                HUD.log("AIM-9B launch cancelled (disengaged)");
                return;
            }
        }
        catch(Exception exception)
        {
            HUD.log("AIM-9B launch cancelled (system error)");
        }
        super.shots(paramInt);
        if(paramInt > 0 && Actor.isValid(super.actor) && (super.actor instanceof TypeAIM9Carrier) && (World.cur().diffCur.Limited_Ammo || super.actor != World.getPlayerAircraft()))
            ((TypeAIM9Carrier)super.actor).shotMissile();
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAIM_9B.class;
        Property.set(class1, "bulletClass", com.maddox.il2.objects.weapons.RocketAIM9B.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
