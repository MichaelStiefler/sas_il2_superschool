// Source File Name: RocketS5.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class RocketS5 extends Rocket {

  public RocketS5() {
  }

  public void start(float f, int i) {
    setMesh("3DO/Arms/S5/mono_open.sim");
    super.start(f, i);
    speed.normalize();
    speed.scale(525D);
    noGDelay = -1L;
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketS5.class;
    Property.set(class1, "mesh", "3DO/Arms/S5/mono.sim");
    Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
    Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
    Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 1.0F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "radius", 12F);
    Property.set(class1, "timeLife", 60F);
    Property.set(class1, "timeFire", 1.1F);
    Property.set(class1, "force", 1300.0F);
    Property.set(class1, "power", 5F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.055F);
    Property.set(class1, "massa", 3.99F);
    Property.set(class1, "massaEnd", 3.0F);
  }
}