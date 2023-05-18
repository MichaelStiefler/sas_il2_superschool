// Source File Name: MGunADEN30s.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.ai.RangeRandom;
import java.security.SecureRandom;

public class MGunADEN30s extends MGunAircraftGeneric {

  private static RangeRandom theRangeRandom;

  private void initRandom() {
    if (theRangeRandom != null) {
      return;
    }
    long lTime = System.currentTimeMillis();
    SecureRandom secRandom = new SecureRandom();
    secRandom.setSeed(lTime);
    long lSeed1 = (long) secRandom.nextInt();
    long lSeed2 = (long) secRandom.nextInt();
    long lSeed = (lSeed1 << 32) + lSeed2;
    theRangeRandom = new RangeRandom(lSeed);
  }

  private int nextRandomInt(int iMin, int iMax) {
    this.initRandom();
    return theRangeRandom.nextInt(iMin, iMax);
  }

  public void createdProperties() {
    super.createdProperties();
    this.initRandom();
    int iRandBullet = this.nextRandomInt(1, this.prop.bullet.length);
    for (int i = 0; i < iRandBullet; i++) {
      this.nextIndexBulletType();
    }
  }

  public GunProperties createProperties() {
    GunProperties gunproperties = super.createProperties();
    gunproperties.bCannon = false;
    gunproperties.bUseHookAsRel = true;
    gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
    gunproperties.fire = "3DO/Effects/GunFire/30mm/GunFire.eff";
    gunproperties.sprite = "3DO/Effects/GunFire/30mm/GunFlare.eff";
    gunproperties.smoke = "effects/smokes/MachineGun.eff";
    gunproperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
    gunproperties.sound = "weapon.MGun231";
    gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
    gunproperties.emitI = 2.5F;
    gunproperties.emitR = 1.5F;
    gunproperties.emitTime = 0.03F;
    gunproperties.aimMinDist = 10.0F;
    gunproperties.aimMaxDist = 1000.0F;
    gunproperties.weaponType = -1;
    gunproperties.maxDeltaAngle = 0.43F;
    gunproperties.shotFreq = 20F;
    gunproperties.traceFreq = 2;
    gunproperties.bullets = 50;
    gunproperties.bulletsCluster = 1;
    gunproperties.bullet = (new BulletProperties[]{new BulletProperties(), new BulletProperties(),
              new BulletProperties(), new BulletProperties(),
              new BulletProperties()});
    gunproperties.bullet[0].massa = 0.247F;
    gunproperties.bullet[0].kalibr = 0.000667F;
    gunproperties.bullet[0].speed = 790F;
    gunproperties.bullet[0].power = 0.046F;
    gunproperties.bullet[0].powerType = 0;
    gunproperties.bullet[0].powerRadius = 1.5F;
    gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
    gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
    gunproperties.bullet[0].traceColor = 0xd200ffff;
    gunproperties.bullet[0].timeLife = 3.3F;
    gunproperties.bullet[1].massa = 0.247F;
    gunproperties.bullet[1].kalibr = 0.000667F;
    gunproperties.bullet[1].speed = 790F;
    gunproperties.bullet[1].power = 0.054F;
    gunproperties.bullet[1].powerType = 0;
    gunproperties.bullet[1].powerRadius = 1.5F;
    gunproperties.bullet[1].traceMesh = null;
    gunproperties.bullet[1].traceTrail = null;
    gunproperties.bullet[1].traceColor = 0;
    gunproperties.bullet[1].timeLife = 3.3F;
    return gunproperties;
  }
}
