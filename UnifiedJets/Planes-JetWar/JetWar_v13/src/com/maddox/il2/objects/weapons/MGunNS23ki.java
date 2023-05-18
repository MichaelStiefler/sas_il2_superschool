// Source File Name: MGunNS23ki.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.ai.RangeRandom;
import java.security.SecureRandom;

public class MGunNS23ki extends MGunVYas {

  public MGunNS23ki() {
  }

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
    gunproperties.fireMesh = null;
    gunproperties.fire = "3DO/Effects/GunFire/30mm/GunFire.eff";
    gunproperties.sprite = "3DO/Effects/GunFire/30mm/GunFlare.eff";
    gunproperties.smoke = "effects/smokes/MachineGun.eff";
    gunproperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
    gunproperties.sound = "weapon.mgun_20_700";
    gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
    gunproperties.emitI = 2.5F;
    gunproperties.emitR = 1.5F;
    gunproperties.emitTime = 0.03F;
    gunproperties.aimMinDist = 10F;
    gunproperties.aimMaxDist = 1000F;
    gunproperties.weaponType = 3;
    gunproperties.maxDeltaAngle = 0.28F;
    gunproperties.shotFreqDeviation = 0.03F;
    gunproperties.shotFreq = 9.2F;
    gunproperties.traceFreq = 3;
    gunproperties.bullets = 120;
    gunproperties.bulletsCluster = 1;
    gunproperties.bullet = (new BulletProperties[]{
              new BulletProperties(), new BulletProperties(), new BulletProperties()
            });
    gunproperties.bullet[0].massa = 0.175F;
    gunproperties.bullet[0].kalibr = 0.0006245F;
    gunproperties.bullet[0].speed = 690F;
    gunproperties.bullet[0].power = 0.0105F;
    gunproperties.bullet[0].powerType = 0;
    gunproperties.bullet[0].powerRadius = 1.0F;
    gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
    gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
    gunproperties.bullet[0].traceColor = 0xd9002eff;
    gunproperties.bullet[0].timeLife = 4F;
    gunproperties.bullet[1].massa = 0.175F;
    gunproperties.bullet[1].kalibr = 0.0006245F;
    gunproperties.bullet[1].speed = 690F;
    gunproperties.bullet[1].power = 0.0105F;
    gunproperties.bullet[1].powerType = 0;
    gunproperties.bullet[1].powerRadius = 1.0F;
    gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
    gunproperties.bullet[1].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
    gunproperties.bullet[1].traceColor = 0xd9002eff;
    gunproperties.bullet[1].timeLife = 4F;
    gunproperties.bullet[2].massa = 0.175F;
    gunproperties.bullet[2].kalibr = 0.0006245F;
    gunproperties.bullet[2].speed = 690F;
    gunproperties.bullet[2].power = 0.006F;
    gunproperties.bullet[2].powerType = 0;
    gunproperties.bullet[2].powerRadius = 0.0F;
    gunproperties.bullet[2].traceMesh = null;
    gunproperties.bullet[2].traceTrail = null;
    gunproperties.bullet[2].traceColor = 0;
    gunproperties.bullet[2].timeLife = 4F;
    return gunproperties;
  }
}