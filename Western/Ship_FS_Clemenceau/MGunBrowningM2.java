
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAntiAirGeneric, BulletAntiAirSmallUSSR, Bullet

public class MGunBrowningM2 extends MGunAntiAirGeneric
{

    public MGunBrowningM2()
    {
    }

    protected float Specify(GunProperties gunproperties)
    {
        gunproperties.fireMesh = "3DO/Effects/GunFire/12mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/12mm/GunFlare.eff";
        gunproperties.smoke = null;
        gunproperties.aimMaxDist = 2500F;
        gunproperties.shotFreq = 5.0F;
        gunproperties.bulletsCluster = 1;
        gunproperties.sound = "weapon.mgun_15_dual_t";
        BulletProperties bulletproperties = gunproperties.bullet[0];
        bulletproperties.timeLife = 4.0F;
        bulletproperties.addExplTime = 1.5F;
        bulletproperties.power = 0.005F;
        bulletproperties.powerType = 1;
        bulletproperties.powerRadius = 15F;
        bulletproperties.kalibr = 0.0127F;
        bulletproperties.massa = 0.052F;
        bulletproperties.speed = 880F;
        bulletproperties.traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        bulletproperties.traceTrail = null;
        bulletproperties.traceColor = 0xb300ffff;
        bulletproperties = gunproperties.bullet[1];
        bulletproperties.timeLife = 4.0F;
        bulletproperties.power = 0.0F;
        bulletproperties.kalibr = 0.0127F;
        bulletproperties.massa = 0.052F;
        bulletproperties.speed = 880F;
        bulletproperties.traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        bulletproperties.traceTrail = null;
        bulletproperties.traceColor = 0xb300ffff;
        return 10F;
    }

    public Bullet createNextBullet(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l)
    {
        return new BulletAntiAirSmallUSSR(vector3d, i, gungeneric, loc, vector3d1, l, explAddTimeT);
    }
}