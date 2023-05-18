// Source File Name: Bomb750lbsM117.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb750lbsM117 extends Bomb {

  public Bomb750lbsM117() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.Bomb750lbsM117.class;
    Property.set(class1, "mesh", "3DO/Arms/750lbsBombM117/mono.sim");
    Property.set(class1, "radius", 200F);
    Property.set(class1, "power", 225F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.30F);
    Property.set(class1, "massa", 340F);
    Property.set(class1, "sound", "weapon.bomb_mid");
  }
}