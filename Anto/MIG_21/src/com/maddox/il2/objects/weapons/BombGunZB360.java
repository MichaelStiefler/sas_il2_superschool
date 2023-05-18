package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunZB360 extends BombGun {

  public BombGunZB360() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.BombGunZB360.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombZB360.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 1F);
    Property.set(class1, "external", 1);
    Property.set(class1, "sound", "weapon.bombgun");
  }
}