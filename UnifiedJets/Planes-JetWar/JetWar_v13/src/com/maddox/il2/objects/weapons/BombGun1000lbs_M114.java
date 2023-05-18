// Source File Name: BombGun1000lbs_M114F.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun1000lbs_M114 extends BombGun {

  public BombGun1000lbs_M114() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.BombGun1000lbs_M114.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.Bomb1000lbs_M114.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 1.0F);
    Property.set(class1, "external", 1);
    Property.set(class1, "sound", "weapon.bombgun");
  }
}
