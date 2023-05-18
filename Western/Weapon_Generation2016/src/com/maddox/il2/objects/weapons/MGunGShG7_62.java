// USSR 4x 7.62mm gatling gun GShG-7.62 --- in GUV-8700 container for Mi-8/24 helicopters
// By western0221 on 23rd/Oct./2019

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAircraftGeneric

public class MGunGShG7_62 extends MGunAircraftGeneric
{

    public MGunGShG7_62()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/7mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/7mm/GunFlare.eff";
        gunproperties.smoke = "Effects/Smokes/MachineGunSmall.eff";
        gunproperties.shells = null;
        gunproperties.sound = "weapon.mgun_07_900";
        gunproperties.emitColor = new Color3f(0.6F, 0.4F, 0.2F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = 1;
        gunproperties.maxDeltaAngle = 0.25F;
        gunproperties.shotFreq = 45F;
        gunproperties.traceFreq = 9;
        gunproperties.bullets = 1500;
        gunproperties.bulletsCluster = 2;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.0128F;
        gunproperties.bullet[0].kalibr = 2.62122E-005F;
        gunproperties.bullet[0].speed = 830F;
        gunproperties.bullet[0].power = 0.0F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail2.eff";
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 1.8F;
        return gunproperties;
    }
}
