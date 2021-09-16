package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunLKk42s extends MGunAircraftGeneric {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/12mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/12mm/GunFlare.eff";
        gunproperties.smoke = null;
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MGunBrowning50s";
        gunproperties.emitColor = new Color3f(0.6F, 0.4F, 0.2F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = -1;
        gunproperties.maxDeltaAngle = 0.17F;
        gunproperties.shotFreq = 12.4F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 400;
        gunproperties.bulletsCluster = 2;
        gunproperties.bullet = (new BulletProperties[] { new BulletProperties(), new BulletProperties() });
        gunproperties.bullet[0].massa = 0.04206F;
        gunproperties.bullet[0].kalibr = 0.00013F;
        gunproperties.bullet[0].speed = 888F;
        gunproperties.bullet[0].power = 0.00314F;
        gunproperties.bullet[0].powerType = 2;
        gunproperties.bullet[0].powerRadius = 0.002F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[0].traceTrail = "Effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[0].traceColor = 0xf90000ff;
        gunproperties.bullet[0].timeLife = 8F;
        gunproperties.bullet[1].massa = 0.04653F;
        gunproperties.bullet[1].kalibr = 0.00013F;
        gunproperties.bullet[1].speed = 885F;
        gunproperties.bullet[1].power = 0.00325F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0;
        gunproperties.bullet[1].timeLife = 8F;
        return gunproperties;
    }
}
