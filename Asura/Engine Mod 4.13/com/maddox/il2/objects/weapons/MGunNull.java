// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:18:15
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunNull.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunNullGeneric

public class MGunNull extends MGunNullGeneric
{

    public MGunNull()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = null;
        gunproperties.fire = null;
        gunproperties.sprite = null;
        gunproperties.smoke = null;
        gunproperties.shells = null;
        gunproperties.sound = null;
        gunproperties.emitColor = null;
        gunproperties.emitI = 0.0F;
        gunproperties.emitR = 0.0F;
        gunproperties.emitTime = 0.0F;
        gunproperties.aimMinDist = 0.0F;
        gunproperties.aimMaxDist = 0.0F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.0F;
        gunproperties.shotFreq = 0.0F;
        gunproperties.traceFreq = 0;
        gunproperties.bullets = 0x7fffffff;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.0F;
        gunproperties.bullet[0].kalibr = 0.0F;
        gunproperties.bullet[0].speed = 0.0F;
        gunproperties.bullet[0].power = 0.0F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = null;
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0;
        gunproperties.bullet[0].timeLife = 0.0F;
        return gunproperties;
    }
}