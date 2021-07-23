package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMine_MineJI_G10N extends BombGun {

    public void setBombDelay(float f) {
        super.bombDelay = 0.0F;
        if (super.bomb != null) {
            super.bomb.delayExplosion = super.bombDelay;
        }
    }

    static {
        Class class1 = BombGunMine_MineJI_G10N.class;
        Property.set(class1, "bulletClass", (Object) BombMine_MineJI_G10N.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_torpedo");
    }
}
