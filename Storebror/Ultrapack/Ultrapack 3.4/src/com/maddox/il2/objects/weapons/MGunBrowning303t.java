package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.*;

public class MGunBrowning303t extends MGunAircraftGeneric
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = false;
        gunproperties.fireMesh = "3DO/Effects/GunFire/7mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/7mm/GunFlare.eff";
        gunproperties.smoke = null;
        gunproperties.shells = null;
        gunproperties.sound = "weapon.mgun_07_900";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = 1;
        gunproperties.maxDeltaAngle = 0.42F;
        gunproperties.shotFreq = 16.66667F;
        gunproperties.traceFreq = 3;
        gunproperties.bullets = 500;
        gunproperties.bulletsCluster = 2;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.01066849F;
        gunproperties.bullet[0].kalibr = 4.442131E-005F;
        gunproperties.bullet[0].speed = 835F;
        gunproperties.bullet[0].power = 0.0018F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[0].traceTrail = "Effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[0].traceColor = 0xd90000ff;
        gunproperties.bullet[0].timeLife = 2.5F;
        gunproperties.bullet[1].massa = 0.01066849F;
        gunproperties.bullet[1].kalibr = 4.442131E-005F;
        gunproperties.bullet[1].speed = 835F;
        gunproperties.bullet[1].power = 0.0F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0;
        gunproperties.bullet[1].timeLife = 1.0F;
        gunproperties.bullet[2].massa = 0.01066849F;
        gunproperties.bullet[2].kalibr = 4.442131E-005F;
        gunproperties.bullet[2].speed = 835F;
        gunproperties.bullet[2].power = 0.0F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.0F;
        gunproperties.bullet[2].traceMesh = null;
        gunproperties.bullet[2].traceTrail = null;
        gunproperties.bullet[2].traceColor = 0;
        gunproperties.bullet[2].timeLife = 1.0F;
        gunproperties.bullet[3].massa = 0.01066849F;
        gunproperties.bullet[3].kalibr = 4.442131E-005F;
        gunproperties.bullet[3].speed = 835F;
        gunproperties.bullet[3].power = 0.0018F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 0.0F;
        gunproperties.bullet[3].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[3].traceTrail = "Effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[3].traceColor = 0xd90000ff;
        gunproperties.bullet[3].timeLife = 2.5F;
        gunproperties.bullet[4].massa = 0.01066849F;
        gunproperties.bullet[4].kalibr = 4.442131E-005F;
        gunproperties.bullet[4].speed = 835F;
        gunproperties.bullet[4].power = 0.0018F;
        gunproperties.bullet[4].powerType = 0;
        gunproperties.bullet[4].powerRadius = 0.0F;
        gunproperties.bullet[4].traceMesh = null;
        gunproperties.bullet[4].traceTrail = null;
        gunproperties.bullet[4].traceColor = 0;
        gunproperties.bullet[4].timeLife = 1.0F;
        gunproperties.bullet[5].massa = 0.01066849F;
        gunproperties.bullet[5].kalibr = 4.442131E-005F;
        gunproperties.bullet[5].speed = 835F;
        gunproperties.bullet[5].power = 0.0018F;
        gunproperties.bullet[5].powerType = 0;
        gunproperties.bullet[5].powerRadius = 0.0F;
        gunproperties.bullet[5].traceMesh = null;
        gunproperties.bullet[5].traceTrail = null;
        gunproperties.bullet[5].traceColor = 0;
        gunproperties.bullet[5].timeLife = 1.0F;
        return gunproperties;
    }
}
