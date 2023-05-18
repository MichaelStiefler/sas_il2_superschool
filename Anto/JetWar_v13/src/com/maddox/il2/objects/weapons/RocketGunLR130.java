// Source File Name: RocketGunLR130.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunLR130 extends RocketGun {

  public RocketGunLR130() {
  }

  public void setRocketTimeLife(float f) {
    timeLife = -1F;
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketGunLR130.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketLR130.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 1.0F);
    Property.set(class1, "sound", "weapon.rocketgun_132");
  }
}
