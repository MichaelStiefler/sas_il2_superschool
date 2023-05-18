// Source File Name: BombGunMk7.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk7 extends BombGun {

  public BombGunMk7() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.BombGunMk7.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombMk7.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 0.05F);
    Property.set(class1, "external", 1);
    Property.set(class1, "sound", "weapon.bombgun");
    Property.set(class1, "newEffect", 1);
  }
}