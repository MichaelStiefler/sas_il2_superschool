// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 10.3.2016 21:28:21
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunGSh23Ls.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunVYas

public class MGunGSh23LsM extends MGunVYas
{

    public MGunGSh23LsM()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = null;
        gunproperties.fire = "3DO/Effects/GunFire/30mm/GunFire.eff";
        gunproperties.sprite = "3DO/Effects/GunFire/30mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
        gunproperties.sound = "weapon.GSH23";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1500F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.28F;
        gunproperties.shotFreqDeviation = 0.03F;
        gunproperties.shotFreq = 60F;
        gunproperties.traceFreq = 1;
        gunproperties.bEnablePause = true;
        gunproperties.bullets = 120;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.184F;
        gunproperties.bullet[0].kalibr = 0.000320F;
        gunproperties.bullet[0].speed = 700F;
        gunproperties.bullet[0].power = 0.0115F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 1.2F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd9002eff;
        gunproperties.bullet[0].timeLife = 8F;
        
        gunproperties.bullet[1].massa = 0.176F;
        gunproperties.bullet[1].kalibr = 0.0003184F;
        gunproperties.bullet[1].speed = 710F;
        gunproperties.bullet[1].power = 0.0115F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 1.0F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[1].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[1].traceColor = 0xd9002eff;
        gunproperties.bullet[1].timeLife = 8F;
        
        gunproperties.bullet[2].massa = 0.190F;
        gunproperties.bullet[2].kalibr = 0.0003184F;
        gunproperties.bullet[2].speed = 690F;
        gunproperties.bullet[2].power = 0.007F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.2F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[2].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[2].traceColor = 0xd9002eff;
        gunproperties.bullet[2].timeLife = 8F;
        
        gunproperties.bullet[3].massa = 0.184F;
        gunproperties.bullet[3].kalibr = 0.000320F;
        gunproperties.bullet[3].speed = 700F;
        gunproperties.bullet[3].power = 0.0115F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 1.2F;
        gunproperties.bullet[3].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[3].traceTrail = null;
        gunproperties.bullet[3].traceColor = 0xd9002eff;
        gunproperties.bullet[3].timeLife = 8F;
        return gunproperties;
    }
}