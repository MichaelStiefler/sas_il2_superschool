package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunHo3s extends MGunAircraftGeneric {

	public GunProperties createProperties() {
		GunProperties gunproperties = super.createProperties();
		gunproperties.bCannon = false;
		gunproperties.bUseHookAsRel = true;
		gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
		gunproperties.fire = null;
		gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
		gunproperties.smoke = "effects/smokes/MachineGun.eff";
		gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
		gunproperties.sound = "weapon.mgun_20_700";
		gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
		gunproperties.emitI = 10F;
		gunproperties.emitR = 3F;
		gunproperties.emitTime = 0.03F;
		gunproperties.aimMinDist = 10F;
		gunproperties.aimMaxDist = 3000F;
		gunproperties.weaponType = 3;
		gunproperties.maxDeltaAngle = 0.12F;
		gunproperties.shotFreq = 6.666667F;
		gunproperties.traceFreq = 1;
		gunproperties.bullets = 150;
		gunproperties.bulletsCluster = 1;
		gunproperties.bullet = new BulletProperties[] { new BulletProperties() };
		gunproperties.bullet[0].massa = 0.115F;
		gunproperties.bullet[0].kalibr = 0.000404F;
		gunproperties.bullet[0].speed = 820F;
		gunproperties.bullet[0].power = 0.003872F;
		gunproperties.bullet[0].powerType = 0;
		gunproperties.bullet[0].powerRadius = 0.1F;
		gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
		gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
		gunproperties.bullet[0].traceColor = 0xd200ffff;
		gunproperties.bullet[0].timeLife = 3F;
		return gunproperties;
	}
}
