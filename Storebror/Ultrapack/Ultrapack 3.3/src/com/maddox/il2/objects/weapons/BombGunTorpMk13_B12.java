package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTorpMk13_B12 extends TorpedoGun {

    public void setBombDelay(float f) {
        this.bombDelay = 0.0F;
        if (this.bomb != null) this.bomb.delayExplosion = this.bombDelay;
    }

    static {
        Class class1 = BombGunTorpMk13_B12.class;
        Property.set(class1, "bulletClass", (Object) BombTorpMk13_B12.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.1F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_torpedo");
    }
}
