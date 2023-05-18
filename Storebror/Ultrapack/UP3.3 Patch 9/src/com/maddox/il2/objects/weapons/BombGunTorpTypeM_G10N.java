package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTorpTypeM_G10N extends TorpedoGun {

    public void setBombDelay(float paramFloat) {
        this.bombDelay = 0.0F;
        if (this.bomb != null) {
            this.bomb.delayExplosion = this.bombDelay;
        }
    }

    static {
        Class class1 = BombGunTorpTypeM_G10N.class;
        Property.set(class1, "bulletClass", (Object) BombTorpTypeM_G10N.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.1F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_torpedo");
    }
}
