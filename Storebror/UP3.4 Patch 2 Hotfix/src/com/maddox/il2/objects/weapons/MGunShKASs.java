package com.maddox.il2.objects.weapons;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunShKASs extends MGunAircraftGeneric
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/7mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/7mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/SmallGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MGunShKASs";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = 1;
        gunproperties.maxDeltaAngle = 0.31F;
        gunproperties.shotFreq = 27F;
        gunproperties.traceFreq = 2;
        gunproperties.bullets = 750;
        gunproperties.bulletsCluster = 2;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.0092F;
        gunproperties.bullet[0].kalibr = 2.900001E-005F;
        gunproperties.bullet[0].speed = 825F;
        gunproperties.bullet[0].power = 0.0002F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/7mmGreen/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd200ff00;
        gunproperties.bullet[0].timeLife = 2.2F;
        gunproperties.bullet[1].massa = 0.0096F;
        gunproperties.bullet[1].kalibr = 2.900001E-005F;
        gunproperties.bullet[1].speed = 826F;
        gunproperties.bullet[1].power = 0.0005F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0xd200ff00;
        gunproperties.bullet[1].timeLife = 2.2F;
        gunproperties.bullet[2].massa = 0.0092F;
        gunproperties.bullet[2].kalibr = 2.900001E-005F;
        gunproperties.bullet[2].speed = 825F;
        gunproperties.bullet[2].power = 0.00178F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.03F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/7mmGreen/mono.sim";
        gunproperties.bullet[2].traceTrail = null;
        gunproperties.bullet[2].traceColor = 0xd200ff00;
        gunproperties.bullet[2].timeLife = 2.2F;
        return gunproperties;
    }
}
