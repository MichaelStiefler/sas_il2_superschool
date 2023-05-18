// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 12.07.2017 21:03:21
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunYakB.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAircraftGeneric

public class MGunYakB extends MGunAircraftGeneric
{

    public MGunYakB()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = false;
        gunproperties.fireMesh = "3DO/Effects/GunFire/7mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/7mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MiniGun";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 5F;
        gunproperties.aimMaxDist = 3000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 1F;
        gunproperties.shotFreq = 40F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 1470;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.045F;
        gunproperties.bullet[0].kalibr = 0.0001209675F;
        gunproperties.bullet[0].speed = 810F;
        gunproperties.bullet[0].power = 0.0208F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/7mmGreen/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd200ff00;
        gunproperties.bullet[0].timeLife = 3F;
        gunproperties.bullet[1].massa = 0.045F;
        gunproperties.bullet[1].kalibr = 0.0001209675F;
        gunproperties.bullet[1].speed = 810F;
        gunproperties.bullet[1].power = 0.01F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.05F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/7mmGreen/mono.sim";
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0xd200ff00;
        gunproperties.bullet[1].timeLife = 3F;
        return gunproperties;
    }
}