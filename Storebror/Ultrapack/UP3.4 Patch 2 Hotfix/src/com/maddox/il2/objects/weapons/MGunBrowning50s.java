package com.maddox.il2.objects.weapons;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunBrowning50s extends MGunAircraftGeneric
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/12mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/12mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MGunBrowning50s";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = -1;
        gunproperties.maxDeltaAngle = 0.229F;
        gunproperties.shotFreq = 10F;
        gunproperties.traceFreq = 2;
        gunproperties.bullets = 500;
        gunproperties.bulletsCluster = 2;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.046F;
        gunproperties.bullet[0].kalibr = 0.0001181215F;
        gunproperties.bullet[0].speed = 870F;
        gunproperties.bullet[0].power = 0.0F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[0].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[0].traceColor = 0xf90000ff;
        gunproperties.bullet[0].timeLife = 6.5F;
        gunproperties.bullet[1].massa = 0.041F;
        gunproperties.bullet[1].kalibr = 0.0001185215F;
        gunproperties.bullet[1].speed = 900F;
        gunproperties.bullet[1].power = 0.0022F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0;
        gunproperties.bullet[1].timeLife = 6.3F;
        gunproperties.bullet[2].massa = 0.046F;
        gunproperties.bullet[2].kalibr = 0.0001181215F;
        gunproperties.bullet[2].speed = 870F;
        gunproperties.bullet[2].power = 0.0F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.0F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[2].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[2].traceColor = 0xf90000ff;
        gunproperties.bullet[2].timeLife = 6.5F;
        gunproperties.bullet[3].massa = 0.041F;
        gunproperties.bullet[3].kalibr = 0.0001185215F;
        gunproperties.bullet[3].speed = 900F;
        gunproperties.bullet[3].power = 0.0022F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 0.0F;
        gunproperties.bullet[3].traceMesh = null;
        gunproperties.bullet[3].traceTrail = null;
        gunproperties.bullet[3].traceColor = 0;
        gunproperties.bullet[3].timeLife = 6.3F;
        gunproperties.bullet[4].massa = 0.042F;
        gunproperties.bullet[4].kalibr = 0.0001182215F;
        gunproperties.bullet[4].speed = 820F;
        gunproperties.bullet[4].power = 0.001F;
        gunproperties.bullet[4].powerType = 0;
        gunproperties.bullet[4].powerRadius = 0.0F;
        gunproperties.bullet[4].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[4].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[4].traceColor = 0xf90000ff;
        gunproperties.bullet[4].timeLife = 6.5F;
        return gunproperties;
    }
}
