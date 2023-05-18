// Source File Name: RocketSURA_HE.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class RocketSURA_HE extends Rocket {

  public RocketSURA_HE() {
  }

  public void start(float f, int i) {
    setMesh("3DO/Arms/SURA-D_HE/mono_open.sim");
    super.start(f, i);
    speed.normalize();
    speed.scale(525D);
    noGDelay = -1L;
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketSURA_HE.class;
    Property.set(class1, "mesh", "3DO/Arms/SURA-D_HE/mono.sim");
    Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
    Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
    Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 1.0F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "radius", 60F);
    Property.set(class1, "timeLife", 999.999F);
    Property.set(class1, "timeFire", 1.2F);
    Property.set(class1, "force", 2142F);
    Property.set(class1, "power", 25F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.081F);
    Property.set(class1, "massa", 11.4F);
    Property.set(class1, "massaEnd", 8.4F);
  }
}
