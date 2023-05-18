// Source File Name: FuelTank_200galR.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_200galR extends FuelTank {

  static {
    Class var_class = com.maddox.il2.objects.weapons.FuelTank_200galR.class;
    Property.set(var_class, "mesh", "3DO/Arms/200galL/mono.sim");
    Property.set(var_class, "kalibr", 0.6F);
    Property.set(var_class, "massa", 598.8F);
  }
}
