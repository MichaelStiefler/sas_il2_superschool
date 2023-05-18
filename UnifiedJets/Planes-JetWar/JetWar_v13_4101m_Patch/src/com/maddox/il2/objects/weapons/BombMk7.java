// Source File Name: BombMk7.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk7 extends Bomb {

  public BombMk7() {
  }

  public void start() {
    setMesh("3DO/Arms/Mk7/mono_open.sim");
    super.start();
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.BombMk7.class;
    Property.set(class1, "mesh", "3DO/Arms/Mk7/mono.sim");
    Property.set(class1, "radius", 3200F);
    Property.set(class1, "power", 8000000F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 1.0F);
    Property.set(class1, "massa", 764F);
    Property.set(class1, "sound", "weapon.bomb_big");
    Property.set(class1, "newEffect", 1);
    Property.set(class1, "nuke", 1);
  }
}