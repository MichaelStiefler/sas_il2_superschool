package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunS24 extends RocketGun {

  public RocketGunS24() {
  }

  public void setRocketTimeLife(float f)
  {
      timeLife = -1F;
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketGunS24.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketS24.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 1F);
    Property.set(class1, "sound", "weapon.rocketgun_132");
  }
}