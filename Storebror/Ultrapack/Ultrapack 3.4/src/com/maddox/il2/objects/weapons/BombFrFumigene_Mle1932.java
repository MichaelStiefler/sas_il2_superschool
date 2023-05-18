package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.ActorLand;
import com.maddox.rts.Property;

public class BombFrFumigene_Mle1932 extends Bomb {
    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand) {
            super.msgCollision(actor, s, s1);
            Eff3DActor.New(this, null, new Loc(), 0.7F, "3DO/Effects/Fireworks/Fumigene_New.eff", 1200F);
        }
    }

    static {
        Class class1 = BombFrFumigene_Mle1932.class;
        Property.set(class1, "mesh", "3do/arms/BombFrFumigene_Mle1932/mono.sim");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.089F);
        Property.set(class1, "massa", 8.8F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
