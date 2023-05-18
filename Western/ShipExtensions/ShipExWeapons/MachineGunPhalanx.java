
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.*;


public class MachineGunPhalanx extends MGunAntiAirGeneric
{

    public MachineGunPhalanx()
    {
    }

    protected float Specify(GunProperties gunproperties)
    {
        gunproperties.aimMaxDist = 2000F;
        gunproperties.shotFreq = 50.0F;
        gunproperties.bulletsCluster = 1;
        gunproperties.sound = "weapon.Gau4";
        BulletProperties bulletproperties = gunproperties.bullet[0];
        bulletproperties.timeLife = 3F;
        bulletproperties.addExplTime = 0F;
        bulletproperties.power = 0.0072F;
        bulletproperties.powerType = 0;
        bulletproperties.powerRadius = 0.01F;
        bulletproperties.kalibr = 0.020F;
        bulletproperties.massa = 0.102F;
        bulletproperties.speed = 1030F;
        bulletproperties.traceMesh = null;
        bulletproperties.traceTrail = null;
        bulletproperties.traceColor = 0x00000000;
        return 42F;
    }

    public Bullet createNextBullet(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l)
    {
        return new BulletAntiAirSmallUSSR(vector3d, i, gungeneric, loc, vector3d1, l, explAddTimeT);
    }
}
