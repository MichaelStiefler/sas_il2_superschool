// Source File Name: Bomb1000lbsMC.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbsMC extends Bomb {

  public Bomb1000lbsMC() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.Bomb1000lbsMC.class;
    Property.set(class1, "mesh", "3DO/Arms/1000lbsMC/mono.sim");
    Property.set(class1, "radius", 250F);
    Property.set(class1, "power", 275F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.30F);
    Property.set(class1, "massa", 505F);
    Property.set(class1, "sound", "weapon.bomb_big");
  }
}