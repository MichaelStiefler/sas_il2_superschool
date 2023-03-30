package com.maddox.il2.objects.weapons;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunMG131s extends MGunAircraftGeneric
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
        gunproperties.sound = "weapon.MGunMG131s";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.12F;
        gunproperties.shotFreq = 14F;
        gunproperties.traceFreq = 2;
        gunproperties.bullets = 300;
        gunproperties.bulletsCluster = 2;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.0354F;
        gunproperties.bullet[0].kalibr = 0.00011F;
        gunproperties.bullet[0].speed = 750F;
        gunproperties.bullet[0].power = 0.0012F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.03F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmBlue/mono.sim";
        gunproperties.bullet[0].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[0].traceColor = 0xd2ff0000;
        gunproperties.bullet[0].timeLife = 3F;
        gunproperties.bullet[1].massa = 0.0354F;
        gunproperties.bullet[1].kalibr = 0.00011F;
        gunproperties.bullet[1].speed = 750F;
        gunproperties.bullet[1].power = 0.0012F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.03F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmGreenBlue/mono.sim";
        gunproperties.bullet[1].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[1].traceColor = 0xd294c476;
        gunproperties.bullet[1].timeLife = 3F;
        return gunproperties;
    }
}
