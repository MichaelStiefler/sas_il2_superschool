// Source File Name: Bomb500lbsMC.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbsMC extends Bomb {

  public Bomb500lbsMC() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.Bomb500lbsMC.class;
    Property.set(class1, "mesh", "3DO/Arms/500lbsMC/mono.sim");
    Property.set(class1, "radius", 170F);
    Property.set(class1, "power", 180F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.30F);
    Property.set(class1, "massa", 312F);
    Property.set(class1, "sound", "weapon.bomb_big");
  }
}