package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class RocketRP3_Mk5_60 extends Rocket {

    static {
        Class class1 = RocketRP3_Mk5_60.class;
        Property.set(class1, "mesh", "3DO/Arms/RP3_Mk5_60/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "timeLife", 999.999F);
        Property.set(class1, "timeFire", 4F);
        Property.set(class1, "force", 1728F);
        Property.set(class1, "power", 30F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.127F);
        Property.set(class1, "massa", 43.4F);
        Property.set(class1, "massaEnd", 37.78F);
        Property.set(class1, "friendlyName", "RP-3");
    }
}
