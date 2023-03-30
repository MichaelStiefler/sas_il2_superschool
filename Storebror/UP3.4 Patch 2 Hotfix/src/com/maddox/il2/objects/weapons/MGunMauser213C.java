package com.maddox.il2.objects.weapons;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunMauser213C extends MGunAircraftGeneric
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MGunMG213s";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 2000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.28F;
        gunproperties.shotFreq = 20F;
        gunproperties.traceFreq = 5;
        gunproperties.bullets = 250;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.115F;
        gunproperties.bullet[0].kalibr = 0.00032F;
        gunproperties.bullet[0].speed = 1050F;
        gunproperties.bullet[0].power = 0.0036F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmBlue/mono.sim";
        gunproperties.bullet[0].traceTrail = "3DO/Effects/Tracers/TrailCurved.eff";
        gunproperties.bullet[0].traceColor = 0xd2ff0000;
        gunproperties.bullet[0].timeLife = 3.3F;
        gunproperties.bullet[1].massa = 0.115F;
        gunproperties.bullet[1].kalibr = 0.000404F;
        gunproperties.bullet[1].speed = 1050F;
        gunproperties.bullet[1].power = 0.0044F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.1F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmCyan/mono.sim";
        gunproperties.bullet[1].traceTrail = "3DO/Effects/Tracers/TrailCurved.eff";
        gunproperties.bullet[1].traceColor = 0xd2ffff00;
        gunproperties.bullet[1].timeLife = 3F;
        gunproperties.bullet[2].massa = 0.092F;
        gunproperties.bullet[2].kalibr = 0.000404F;
        gunproperties.bullet[2].speed = 1100F;
        gunproperties.bullet[2].power = 0.01395F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.2F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmCyan/mono.sim";
        gunproperties.bullet[2].traceTrail = "3DO/Effects/Tracers/TrailCurved.eff";
        gunproperties.bullet[2].traceColor = 0xd2ffff00;
        gunproperties.bullet[2].timeLife = 3F;
        gunproperties.bullet[3].massa = 0.092F;
        gunproperties.bullet[3].kalibr = 0.000404F;
        gunproperties.bullet[3].speed = 1100F;
        gunproperties.bullet[3].power = 0.01395F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 0.2F;
        gunproperties.bullet[3].traceMesh = "3do/effects/tracers/20mmCyan/mono.sim";
        gunproperties.bullet[3].traceTrail = "3DO/Effects/Tracers/TrailCurved.eff";
        gunproperties.bullet[3].traceColor = 0xd2ffff00;
        gunproperties.bullet[3].timeLife = 3F;
        gunproperties.bullet[4].massa = 0.092F;
        gunproperties.bullet[4].kalibr = 0.000404F;
        gunproperties.bullet[4].speed = 1100F;
        gunproperties.bullet[4].power = 0.01395F;
        gunproperties.bullet[4].powerType = 0;
        gunproperties.bullet[4].powerRadius = 0.2F;
        gunproperties.bullet[4].traceMesh = "3do/effects/tracers/20mmCyan/mono.sim";
        gunproperties.bullet[4].traceTrail = "3DO/Effects/Tracers/TrailCurved.eff";
        gunproperties.bullet[4].traceColor = 0xd2ffff00;
        gunproperties.bullet[4].timeLife = 3F;
        return gunproperties;
    }
}
