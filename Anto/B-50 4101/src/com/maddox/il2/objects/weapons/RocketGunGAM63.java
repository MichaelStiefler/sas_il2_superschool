package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunGAM63 extends MissileGun implements RocketGunWithDelay
{
  static
  {
    Class class1 = RocketGunGAM63.class;
    Property.set(class1, "bulletClass", (Object)MissileGAM63.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 2.0F);
    Property.set(class1, "sound", "weapon.bombgun");
    Property.set(class1, "dateOfUse", 1956);
  }
}