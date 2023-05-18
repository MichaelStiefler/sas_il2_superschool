// Source File Name: FuelTankGun_200galR.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_200galR extends FuelTankGun {

  static {
    Class var_class = com.maddox.il2.objects.weapons.FuelTankGun_200galR.class;
    Property.set(var_class, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_200galR.class);
    Property.set(var_class, "bullets", 1);
    Property.set(var_class, "shotFreq", 0.25F);
    Property.set(var_class, "external", 1);
  }
}
