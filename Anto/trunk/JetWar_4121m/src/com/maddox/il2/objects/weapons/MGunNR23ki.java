// Source File Name: MGunNR23ki.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunNR23ki extends MGunNS23ki {

  public MGunNR23ki() {
  }

  public GunProperties createProperties() {
    GunProperties gunproperties = super.createProperties();
    gunproperties.shotFreq = 13.3F;
    gunproperties.bullet = (new BulletProperties[]{
              new BulletProperties(), new BulletProperties(), new BulletProperties()
            });
    gunproperties.bullet[0].massa = 0.175F;
    gunproperties.bullet[0].kalibr = 0.0006245F;
    gunproperties.bullet[0].speed = 690F;
    gunproperties.bullet[0].power = 0.0115F;
    gunproperties.bullet[0].powerType = 0;
    gunproperties.bullet[0].powerRadius = 1.0F;
    gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
    gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
    gunproperties.bullet[0].traceColor = 0xd9002eff;
    gunproperties.bullet[0].timeLife = 4F;
    gunproperties.bullet[1].massa = 0.175F;
    gunproperties.bullet[1].kalibr = 0.0006245F;
    gunproperties.bullet[1].speed = 690F;
    gunproperties.bullet[1].power = 0.0115F;
    gunproperties.bullet[1].powerType = 0;
    gunproperties.bullet[1].powerRadius = 1.0F;
    gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
    gunproperties.bullet[1].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
    gunproperties.bullet[1].traceColor = 0xd9002eff;
    gunproperties.bullet[1].timeLife = 4F;
    gunproperties.bullet[2].massa = 0.175F;
    gunproperties.bullet[2].kalibr = 0.0006245F;
    gunproperties.bullet[2].speed = 690F;
    gunproperties.bullet[2].power = 0.007F;
    gunproperties.bullet[2].powerType = 0;
    gunproperties.bullet[2].powerRadius = 0.0F;
    gunproperties.bullet[2].traceMesh = null;
    gunproperties.bullet[2].traceTrail = null;
    gunproperties.bullet[2].traceColor = 0;
    gunproperties.bullet[2].timeLife = 4F;
    return gunproperties;
  }
}