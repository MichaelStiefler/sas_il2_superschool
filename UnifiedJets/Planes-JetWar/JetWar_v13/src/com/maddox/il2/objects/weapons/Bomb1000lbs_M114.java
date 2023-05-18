// Source File Name: Bomb1000lbs_M114.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbs_M114 extends Bomb {

  public Bomb1000lbs_M114() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.Bomb1000lbs_M114.class;
    Property.set(class1, "mesh", "3DO/Arms/1000lbs_M114/mono.sim");
    Property.set(class1, "radius", 250F);
    Property.set(class1, "power", 275F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.30F);
    Property.set(class1, "massa", 505F);
    Property.set(class1, "sound", "weapon.bomb_big");
    Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
  }
}