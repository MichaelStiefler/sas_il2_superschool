package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class RocketHYDRA extends RocketHVAR2 {
    static {
        Class class1 = RocketHYDRA.class;
        Property.set(class1, "mesh", "3DO/Arms/2-75inch/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 25F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_82");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "timeLife", 999.999F);
        Property.set(class1, "timeFire", 4F);
        Property.set(class1, "force", 800F);
        Property.set(class1, "power", 9.6F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.06985F);
        Property.set(class1, "massa", 11F);
        Property.set(class1, "massaEnd", 6F);
    }
}
