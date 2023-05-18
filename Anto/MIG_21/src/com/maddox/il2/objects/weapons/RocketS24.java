package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class RocketS24 extends Rocket {

  public RocketS24() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketS24.class;
    Property.set(class1, "mesh", "3DO/Arms/S-24/mono.sim");
    Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
    Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
    Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 1.0F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "radius", 75F);
    Property.set(class1, "timeLife", 120F);
    Property.set(class1, "timeFire", 5F);
    Property.set(class1, "force", 1300.0F);
    Property.set(class1, "power", 125F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.24F);
    Property.set(class1, "massa", 235F);
    Property.set(class1, "massaEnd", 200F);
  }
}