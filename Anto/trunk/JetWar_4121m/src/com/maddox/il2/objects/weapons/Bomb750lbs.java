// Source File Name: Bomb750lbs.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb750lbs extends Bomb {

  public Bomb750lbs() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.Bomb750lbs.class;
    Property.set(class1, "mesh", "3DO/Arms/750lbsBomb/mono.sim");
    Property.set(class1, "radius", 200F);
    Property.set(class1, "power", 225F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.32F);
    Property.set(class1, "massa", 340F);
    Property.set(class1, "sound", "weapon.bomb_mid");
    Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
  }
}