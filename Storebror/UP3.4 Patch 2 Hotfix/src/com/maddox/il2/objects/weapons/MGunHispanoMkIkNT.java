package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunHispanoMkIkNT extends MGunAircraftGeneric {

    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
        gunproperties.smoke = null;
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MGunHispanoMkIs";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.12F;
        gunproperties.shotFreq = 13.33F;
        gunproperties.traceFreq = 0x1869f;
        gunproperties.bullets = 250;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] { new BulletProperties(), new BulletProperties() });
        gunproperties.bullet[0].massa = 0.129F;
        gunproperties.bullet[0].kalibr = 0.00032F;
        gunproperties.bullet[0].speed = 840F;
        gunproperties.bullet[0].power = 0.0084F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.27F;
        gunproperties.bullet[0].traceMesh = null;
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0;
        gunproperties.bullet[0].timeLife = 1.5F;
        gunproperties.bullet[1].massa = 0.124F;
        gunproperties.bullet[1].kalibr = 0.00026F;
        gunproperties.bullet[1].speed = 840F;
        gunproperties.bullet[1].power = 0.0054F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0;
        gunproperties.bullet[1].timeLife = 1.5F;
        return gunproperties;
    }
}
