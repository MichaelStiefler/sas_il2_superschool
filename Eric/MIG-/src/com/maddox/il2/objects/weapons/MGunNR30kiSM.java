// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 5/16/2012 8:31:49 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunRBackfire.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAircraftGeneric

public class MGunNR30kiSM extends MGunAircraftGeneric
{

    public MGunNR30kiSM()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = true;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = null;
        gunproperties.fire = "3DO/Effects/GunFire/45mm/GunFire.eff";
        gunproperties.sprite = null;
        gunproperties.smoke = "effects/smokes/CannonTank.eff";
        gunproperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
        gunproperties.sound = "weapon.NS30";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 20F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1200F;
        gunproperties.weaponType = -1;
        gunproperties.maxDeltaAngle = 0.33F;
        gunproperties.shotFreq = 25.00F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 50;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.33F;
        gunproperties.bullet[0].kalibr = 0.000601F;
        gunproperties.bullet[0].speed = 950F;
        gunproperties.bullet[0].power = 0.046F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 1.5F;
        gunproperties.bullet[0].traceMesh = null;
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0;
        gunproperties.bullet[0].timeLife = 5F;
        gunproperties.bullet[1].massa = 0.33F;
        gunproperties.bullet[1].kalibr = 0.000601F;
        gunproperties.bullet[1].speed = 950F;
        gunproperties.bullet[1].power = 0.046F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 1.5F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0;
        gunproperties.bullet[1].timeLife = 5F;
        gunproperties.bullet[2].massa = 0.33F;
        gunproperties.bullet[2].kalibr = 0.000601F;
        gunproperties.bullet[2].speed = 950F;
        gunproperties.bullet[2].power = 0.046F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 1.5F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[2].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[2].traceColor = 0xd200ffff;
        gunproperties.bullet[2].timeLife = 5F;
        gunproperties.bullet[3].massa = 0.33F;
        gunproperties.bullet[3].kalibr = 0.000601F;
        gunproperties.bullet[3].speed = 950F;
        gunproperties.bullet[3].power = 0.046F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 1.5F;
        gunproperties.bullet[3].traceMesh = null;
        gunproperties.bullet[3].traceTrail = null;
        gunproperties.bullet[3].traceColor = 0;
        gunproperties.bullet[3].timeLife = 5F;
        gunproperties.bullet[4].massa = 0.33F;
        gunproperties.bullet[4].kalibr = 0.000601F;
        gunproperties.bullet[4].speed = 950F;
        gunproperties.bullet[4].power = 0.046F;
        gunproperties.bullet[4].powerType = 0;
        gunproperties.bullet[4].powerRadius = 1.5F;
        gunproperties.bullet[4].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[4].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[4].traceColor = 0xd200ffff;
        gunproperties.bullet[4].timeLife = 5F;
        return gunproperties;
    }
}