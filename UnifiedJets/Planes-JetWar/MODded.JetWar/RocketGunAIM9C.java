// Source File Name: RocketGunAIM9D.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM9C extends MissileGun implements RocketGunWithDelay {

static {
    Class class1 = com.maddox.il2.objects.weapons.RocketGunAIM9C.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.MissileAIM9C.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 0.25F);
    Property.set(class1, "sound", "weapon.rocketgun_132");
  }
}
