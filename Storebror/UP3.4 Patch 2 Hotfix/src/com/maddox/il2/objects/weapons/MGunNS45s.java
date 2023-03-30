package com.maddox.il2.objects.weapons;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunNS45s extends MGunAircraftGeneric
{
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
        gunproperties.sound = "weapon.MGunNS45s";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = -1;
        gunproperties.maxDeltaAngle = 0.21F;
        gunproperties.shotFreq = 4.166667F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 250;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 1.065F;
        gunproperties.bullet[0].kalibr = 0.0015795F;
        gunproperties.bullet[0].speed = 850F;
        gunproperties.bullet[0].power = 0.095F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 3F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 4F;
        gunproperties.bullet[1].massa = 1.065F;
        gunproperties.bullet[1].kalibr = 0.0015795F;
        gunproperties.bullet[1].speed = 850F;
        gunproperties.bullet[1].power = 0.095F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 3F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[1].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[1].traceColor = 0xd200ffff;
        gunproperties.bullet[1].timeLife = 4F;
        return gunproperties;
    }
}
