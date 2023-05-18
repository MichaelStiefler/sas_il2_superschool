package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunKh66 extends MissileGun implements RocketGunWithDelay {
  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketGunKh66.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.MissileKh66.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 0.25F);
    Property.set(class1, "sound", "weapon.rocketgun_132");
  }
}
