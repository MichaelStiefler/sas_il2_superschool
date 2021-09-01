package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbAP_M52 extends Bomb
{

    static 
    {
        Class class1 = Bomb1000lbAP_M52.class;
        Property.set(class1, "mesh", "3do/arms/1000lbAP_M52/mono.sim");
        Property.set(class1, "radius", 31F);
        Property.set(class1, "power", 26.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.31F);
        Property.set(class1, "massa", 488.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AN_M102A1.class
        })));
    }
}
