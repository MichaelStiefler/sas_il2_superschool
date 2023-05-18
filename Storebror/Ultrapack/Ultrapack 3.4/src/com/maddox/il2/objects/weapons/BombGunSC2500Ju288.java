package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSC2500Ju288 extends BombGun {

    public void setBombDelay(float f) {
        this.bombDelay = 0.0F;
        if (this.bomb != null) {
            this.bomb.delayExplosion = this.bombDelay;
        }
    }

    static {
        Class class1 = BombGunSC2500Ju288.class;
        Property.set(class1, "bulletClass", (Object) BombSC2500Ju288.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
