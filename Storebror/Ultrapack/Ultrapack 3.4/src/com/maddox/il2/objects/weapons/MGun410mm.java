package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGun410mm extends MGunAircraftGeneric {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = true;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = null;
        gunproperties.fire = null;
        gunproperties.sprite = null;
        gunproperties.smoke = null;
        gunproperties.shells = null;
        gunproperties.sound = null;
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 100F;
        gunproperties.aimMaxDist = 5000F;
        gunproperties.weaponType = -1;
        gunproperties.maxDeltaAngle = 0.0F;
        gunproperties.shotFreq = 0.08333334F;
        gunproperties.traceFreq = 10000;
        gunproperties.bullets = 25;
        gunproperties.bulletsCluster = 10;
        gunproperties.bullet = (new BulletProperties[] { new BulletProperties() });
        gunproperties.bullet[0].massa = 0.01F;
        gunproperties.bullet[0].kalibr = 0.00500625F;
        gunproperties.bullet[0].speed = 610F;
        gunproperties.bullet[0].power = 50F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 50F;
        gunproperties.bullet[0].timeLife = 30F;
        return gunproperties;
    }
}
