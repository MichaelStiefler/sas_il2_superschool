package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTorpFIDOhoming extends BombGunTorpFIDO {

    static {
        Class class1 = BombGunTorpFIDOhoming.class;
        Property.set(class1, "bulletClass", (Object) BombTorpFIDOhoming.class);
    }
}
