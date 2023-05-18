// Source File Name: FuelTank_TankC120galL.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankC120galL extends FuelTank {

  public FuelTank_TankC120galL() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankC120galL.class;
    Property.set(class1, "mesh", "3DO/Arms/F86_120galCombatTankL/mono.sim");
    Property.set(class1, "kalibr", 0.6F);
    Property.set(class1, "massa", 359.3F);
  }
}