// Source File Name: MGunBrowningM3.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.ai.RangeRandom;
import java.security.SecureRandom;

public class MGunBrowningM3 extends MGunBrowning50APIT {

  private static RangeRandom theRangeRandom;

  public MGunBrowningM3() {
  }

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
    gunproperties.bUseHookAsRel = true;
    gunproperties.shells = null;
    gunproperties.shotFreq = 20F;
    gunproperties.traceFreq = 2;
    gunproperties.maxDeltaAngle = 0.229F;
    gunproperties.shotFreqDeviation = 0.03F;
    gunproperties.bulletsCluster = 2;
    gunproperties.emitI = 8F;
    gunproperties.emitR = 1.5F;
    return gunproperties;
  }

}