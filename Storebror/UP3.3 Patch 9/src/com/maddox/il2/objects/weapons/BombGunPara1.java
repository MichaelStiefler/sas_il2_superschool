package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunPara1 extends BombGun {

    static {
        Class class1 = BombGunPara1.class;
        Property.set(class1, "bulletClass", (Object) BombPara1.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.03F);
    }
}
