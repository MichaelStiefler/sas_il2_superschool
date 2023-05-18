// Source File Name: BombGunMk12.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk12 extends BombGun {

  public BombGunMk12() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.BombGunMk12.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombMk12.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 0.05F);
    Property.set(class1, "external", 1);
    Property.set(class1, "sound", "weapon.bombgun");
    Property.set(class1, "newEffect", 1);
  }
}
