package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunDepthCharge450lbBarracuda extends BombGun {

    static {
        Class class1 = BombGunDepthCharge450lbBarracuda.class;
        Property.set(class1, "bulletClass", (Object) BombDepthCharge450lbBarracuda.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
