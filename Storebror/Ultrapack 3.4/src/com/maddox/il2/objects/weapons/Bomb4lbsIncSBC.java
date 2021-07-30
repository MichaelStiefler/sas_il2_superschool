package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class Bomb4lbsIncSBC extends Bomb {

    protected boolean haveSound() {
        return false;
    }

    protected void doExplosion(Actor actor, String s, Point3d point3d) {
        if (World.Rnd().nextFloat() > 0.1F) {
            Class class1 = Bomb4lbsIncSBC.class;
            Property.set(class1, "power", 10F);
            Property.set(class1, "powerType", 0);
            Property.set(class1, "kalibr", 0.042F);
            Property.set(class1, "massa", 10F);
            super.doExplosion(actor, s, point3d);
            Property.set(class1, "power", 0.91F);
            Property.set(class1, "powerType", 2);
            Property.set(class1, "kalibr", 0.042F);
            Property.set(class1, "massa", 1.81F);
        } else super.doExplosion(actor, s, point3d);
    }

    static {
        Class class1 = Bomb4lbsIncSBC.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb4lbsIncSBC/mono.sim");
        Property.set(class1, "power", 0.91F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.042F);
        Property.set(class1, "massa", 1.81F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "randomOrient", 1);
    }
}
