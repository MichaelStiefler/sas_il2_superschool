package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAircraftGeneric

public class MGunDHK extends MGunAircraftGeneric
{

    public MGunDHK()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = false;
        gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.mgun_07_1500";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 3000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.1F;
        gunproperties.shotFreq = 15F;
        gunproperties.traceFreq = 4;
        gunproperties.bullets = 150;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.052F;
        gunproperties.bullet[0].kalibr = 0.0001209675F;
        gunproperties.bullet[0].speed = 850F;
        gunproperties.bullet[0].power = 0.01F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmWhite/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd2ffffff;
        gunproperties.bullet[0].timeLife = 3F;
        gunproperties.bullet[1].massa = 0.052F;
        gunproperties.bullet[1].kalibr = 0.0001209675F;
        gunproperties.bullet[1].speed = 850F;
        gunproperties.bullet[1].power = 0.01F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.05F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmWhite/mono.sim";
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0xd2ffffff;
        gunproperties.bullet[1].timeLife = 3F;
        gunproperties.bullet[2].massa = 0.045F;
        gunproperties.bullet[2].kalibr = 0.0001209675F;
        gunproperties.bullet[2].speed = 850F;
        gunproperties.bullet[2].power = 0.002322F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.1F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmWhite/mono.sim";
        gunproperties.bullet[2].traceTrail = null;
        gunproperties.bullet[2].traceColor = 0xd2ffffff;
        gunproperties.bullet[2].timeLife = 2.0F;
        gunproperties.bullet[3].massa = 0.052F;
        gunproperties.bullet[3].kalibr = 0.0001209675F;
        gunproperties.bullet[3].speed = 850F;
        gunproperties.bullet[3].power = 0.01F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 0.1F;
        gunproperties.bullet[3].traceMesh = "3do/effects/tracers/20mmWhite/mono.sim";
        gunproperties.bullet[3].traceTrail = null;
        gunproperties.bullet[3].traceColor = 0xd2ffffff;
        gunproperties.bullet[3].timeLife = 3F;
        return gunproperties;
    }
}