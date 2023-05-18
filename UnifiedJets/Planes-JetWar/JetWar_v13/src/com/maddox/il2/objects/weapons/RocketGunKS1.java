package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunKS1 extends MissileGun implements RocketGunWithDelay
{
  static
  {
    Class class1 = RocketGunKS1.class;
    Property.set(class1, "bulletClass", (Object)MissileKS1.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 2.0F);
    Property.set(class1, "sound", "weapon.bombgun");
    Property.set(class1, "dateOfUse", 19450401);
  }
}