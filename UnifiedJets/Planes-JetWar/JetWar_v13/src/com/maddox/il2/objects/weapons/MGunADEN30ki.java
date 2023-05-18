// Source File Name: MGunADEN30ki.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunADEN30ki extends MGunADEN30s {

  public GunProperties createProperties() {
    GunProperties gunproperties = super.createProperties();
    gunproperties.bUseHookAsRel = true;
    gunproperties.shells = null;
    gunproperties.shotFreq = 20F;
    gunproperties.maxDeltaAngle = 0.43F;
    gunproperties.shotFreqDeviation = 0.02F;
    return gunproperties;
  }
}
