package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class RocketWfrGr21 extends Rocket {
    private static final float mass   = 470.0F;
    private static final float power  = 100.0F;
    private static final float radius = 580.0F;
    private static final int   times  = 10;

    protected void doExplosionAir() {
        super.doExplosionAir();
        this.pos.getTime(Time.current(), p);
        Class localClass = this.getClass();
//    float f1 = Property.floatValue(localClass, "power", 1000.0F);
        int i = Property.intValue(localClass, "powerType", 0);
//    float f2 = Property.floatValue(localClass, "radius", 150.0F);
        for (int j = 0; j < times; j++)
            MsgExplosion.send(null, null, p, this.getOwner(), mass, power, i, radius);

        Explosions.AirFlak(p, 0);
    }

    private static Point3d p = new Point3d();

    static {
        Class class1 = RocketWfrGr21.class;
        Property.set(class1, "mesh", "3DO/Arms/WfrGr21Rocket/mono.sim");
        Property.set(class1, "sprite", "3do/effects/rocket/firesprite.eff");
        Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
        Property.set(class1, "smoke", "3do/effects/rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50.0F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 790.0F);
        Property.set(class1, "timeLife", 20.0F);
        Property.set(class1, "timeFire", 1.8F);
        Property.set(class1, "force", 1720.0F);
        Property.set(class1, "power", 40.8F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.21F);
        Property.set(class1, "massa", 110.0F);
        Property.set(class1, "massaEnd", 91.6F);
    }
}
