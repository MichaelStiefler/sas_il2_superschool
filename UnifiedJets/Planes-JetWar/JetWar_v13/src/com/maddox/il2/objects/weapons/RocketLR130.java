// Source File Name: RocketLR130.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class RocketLR130 extends Rocket {

  public RocketLR130() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketLR130.class;
    Property.set(class1, "mesh", "3DO/Arms/LR130/mono.sim");
    Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
    Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
    Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 1.0F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "radius", 60F);
    Property.set(class1, "timeLife", 999.999F);
    Property.set(class1, "timeFire", 1.2F);
    Property.set(class1, "force", 1620F);
    Property.set(class1, "power", 35F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.13F);
    Property.set(class1, "massa", 48.3F);
    Property.set(class1, "massaEnd", 8.4F);
  }
}
