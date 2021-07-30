package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;

public class RocketChaff_gn16 extends RocketChaff {

    public void start(float f, int i) {
        float f1 = 30F;
        super.start(f1, i);
        this.getOwner().getSpeed(this.speed);
        this.speed.x *= 0.974D + TrueRandom.nextDouble(0.0D, 0.008D);
        this.speed.z -= 7.94D + TrueRandom.nextDouble(0.0D, 0.2D);
        this.setSpeed(this.speed);
    }

    protected void doExplosion(Actor actor, String s) {
        this.destroy();
        Engine.countermeasures().remove(this);
    }

    protected void doExplosionAir() {
    }

    static {
        Class class1 = RocketChaff_gn16.class;
        Property.set(class1, "mesh", "3do/arms/Dummy_Transparent_gn16/mono.sim");
        Property.set(class1, "sprite", (Object) null);
        Property.set(class1, "flame", (Object) null);
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/ChaffSmoke_gn16.eff");
        Property.set(class1, "emitColor", new Color3f(0.01F, 0.01F, 0.01F));
        Property.set(class1, "emitLen", 0.0F);
        Property.set(class1, "emitMax", 0.0F);
        Property.set(class1, "sound", (Object) null);
        Property.set(class1, "timeLife", 7F);
        Property.set(class1, "timeFire", 1.0F);
        Property.set(class1, "force", 2.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1E-005F);
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 2.5F);
        Property.set(class1, "massaEnd", 2.0F);
        Property.set(class1, "friendlyName", "Chaff");
        Property.set(class1, "iconFar_shortClassName", "Chaff");
    }
}
