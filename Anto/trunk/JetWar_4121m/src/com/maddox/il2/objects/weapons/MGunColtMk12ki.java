// Source File Name: MGunColtMk12ki.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.ai.RangeRandom;
import java.security.SecureRandom;

public class MGunColtMk12ki extends MGunHispanoMkIs {

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

  public void init() {
    super.init();
    int iRandBullet = (this.nextRandomInt(0, Integer.MAX_VALUE / 2) % this.prop.bullet.length) + 1;
    for (int i = 0; i < iRandBullet; i++) {
      this.nextIndexBulletType();
    }
  }

  public GunProperties createProperties() {
    GunProperties gunproperties = super.createProperties();
    gunproperties.bCannon = false;
    gunproperties.bUseHookAsRel = true;
    gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
    gunproperties.fire = null;
    gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
    gunproperties.smoke = "effects/smokes/MachineGun.eff";
    gunproperties.shells = null;
    gunproperties.sound = "weapon.MGunHispanoMkIs";
    gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
    gunproperties.emitI = 10.0F;
    gunproperties.emitR = 3.0F;
    gunproperties.emitTime = 0.03F;
    gunproperties.aimMinDist = 10.0F;
    gunproperties.aimMaxDist = 1000.0F;
    gunproperties.weaponType = 3;
    gunproperties.maxDeltaAngle = 0.246F;
    gunproperties.shotFreqDeviation = 0.08F;
    gunproperties.shotFreq = 16.667F;
    gunproperties.traceFreq = 5;
    gunproperties.bullets = 250;
    gunproperties.bulletsCluster = 3;
    gunproperties.bullet = new BulletProperties[]{new BulletProperties(),
      new BulletProperties()};
    gunproperties.bullet[0].massa = 0.114F;
    gunproperties.bullet[0].kalibr = 3.2E-4F;
    gunproperties.bullet[0].speed = 1010.0F;
    gunproperties.bullet[0].power = 0.0104F;
    gunproperties.bullet[0].powerType = 0;
    gunproperties.bullet[0].powerRadius = 0.34F;
    gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
    gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
    gunproperties.bullet[0].traceColor = -771686401;
    gunproperties.bullet[0].timeLife = 1.5F;
    gunproperties.bullet[1].massa = 0.109F;
    gunproperties.bullet[1].kalibr = 2.4E-4F;
    gunproperties.bullet[1].speed = 1010.0F;
    gunproperties.bullet[1].power = 0.0052F;
    gunproperties.bullet[1].powerType = 0;
    gunproperties.bullet[1].powerRadius = 0.0F;
    gunproperties.bullet[1].traceMesh = null;
    gunproperties.bullet[1].traceTrail = null;
    gunproperties.bullet[1].traceColor = 0;
    gunproperties.bullet[1].timeLife = 1.5F;
    return gunproperties;
  }
}
