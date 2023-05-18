// Source File Name: FuelTankGun_TankC120galR.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_120galR extends FuelTankGun {

  static {
    Class var_class = com.maddox.il2.objects.weapons.FuelTankGun_120galR.class;
    Property.set(var_class, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_120galR.class);
    Property.set(var_class, "bullets", 1);
    Property.set(var_class, "shotFreq", 1.0F);
    Property.set(var_class, "external", 1);
  }
}