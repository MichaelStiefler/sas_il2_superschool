package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class RocketS8OFP2_gn16 extends Rocket {

    public void start(float f, int i) {
        super.start(f, i);
        this.noGDelay = -1L;
    }

    static {
        Class class1 = RocketS8OFP2_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFAR.sim");
        Property.set(class1, "meshFly", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFARfly.sim");
        Property.set(class1, "sprite", (String) null);
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/effects/rocket/rocketsmokewhitetile.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 2.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "timeLife", 5F);
        Property.set(class1, "timeFire", 0.88F);
        Property.set(class1, "force", 5500F);
        Property.set(class1, "power", 2.9F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.08F);
        Property.set(class1, "spinningStraightFactor", 1.5F);
        Property.set(class1, "maxDeltaAngle", 0.2F);
        Property.set(class1, "massa", 16.7F);
        Property.set(class1, "massaEnd", 13.53F);
        Property.set(class1, "friendlyName", "S-8");
    }
}
