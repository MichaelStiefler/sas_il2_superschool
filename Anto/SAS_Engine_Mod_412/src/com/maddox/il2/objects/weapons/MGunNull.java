// Source File Name: MGunNull.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunNull extends MGunNullGeneric {

  public MGunNull() {
  }

  public GunProperties createProperties() {
    GunProperties gunproperties = super.createProperties();
    gunproperties.bCannon = false;
    gunproperties.bUseHookAsRel = true;
    gunproperties.fireMesh = null;
    gunproperties.fire = null;
    gunproperties.sprite = null;
    gunproperties.smoke = null;
    gunproperties.shells = null;
    gunproperties.sound = null;
    gunproperties.emitColor = null;
    gunproperties.emitI = 0F;
    gunproperties.emitR = 0F;
    gunproperties.emitTime = 00F;
    gunproperties.aimMinDist = 0F;
    gunproperties.aimMaxDist = 0F;
    gunproperties.weaponType = 3;
    gunproperties.maxDeltaAngle = 0F;
    gunproperties.shotFreq = 0F;
    gunproperties.traceFreq = 0;
    gunproperties.bullets = Integer.MAX_VALUE;
    gunproperties.bulletsCluster = 1;
    gunproperties.bullet = (new BulletProperties[]{new BulletProperties()});
    gunproperties.bullet[0].massa = 0F;
    gunproperties.bullet[0].kalibr = 0F;
    gunproperties.bullet[0].speed = 0F;
    gunproperties.bullet[0].power = 0F;
    gunproperties.bullet[0].powerType = 0;
    gunproperties.bullet[0].powerRadius = 0F;
    gunproperties.bullet[0].traceMesh = null;
    gunproperties.bullet[0].traceTrail = null;
    gunproperties.bullet[0].traceColor = 0;
    gunproperties.bullet[0].timeLife = 0F;
    return gunproperties;
  }
}
