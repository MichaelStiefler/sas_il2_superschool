package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbSAP_AN_M58 extends Bomb
{

    static 
    {
        Class class1 = Bomb500lbSAP_AN_M58.class;
        Property.set(class1, "mesh", "3do/arms/500lbSAP_AN_M58/mono.sim");
        Property.set(class1, "radius", 56F);
        Property.set(class1, "power", 72.6F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 214F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_M161.class, Fuze_AN_M101A2.class, Fuze_M113.class, Fuze_M116.class
        })));
    }
}
