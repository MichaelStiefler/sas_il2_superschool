package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTorpMk34homing extends BombGunTorpMk34 {

    static {
        Class class1 = BombGunTorpMk34homing.class;
        Property.set(class1, "bulletClass", (Object) BombTorpMk34homing.class);
    }
}
