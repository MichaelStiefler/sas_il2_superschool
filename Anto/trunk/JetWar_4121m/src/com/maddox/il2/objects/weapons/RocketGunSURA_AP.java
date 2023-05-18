// Source File Name: RocketGunSURA_AP.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunSURA_AP extends RocketGun {

  public RocketGunSURA_AP() {
  }

  public void setRocketTimeLife(float f) {
    timeLife = -1F;
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketGunSURA_AP.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketSURA_AP.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 1.0F);
    Property.set(class1, "sound", "weapon.rocketgun_132");
  }
}
