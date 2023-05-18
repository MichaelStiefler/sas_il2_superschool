// Based on Hotfix by SAS~Skylla from 22 Sep 2015
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunPhBall extends BombGun {

    public void setBombDelay(float var1) {
        super.bombDelay = 0.0F;
        if (super.bomb != null)
            super.bomb.delayExplosion = super.bombDelay;
    }

    public void loadBullets(int i) {
        if (i != 0)
            i = 13;
        super.loadBullets(i);
    }

    static {
        Class class1 = BombGunPhBall.class;
        Property.set(class1, "bulletClass", (Object) BombPhBall.class);
        Property.set(class1, "bullets", 32);
        Property.set(class1, "shotFreq", 8F);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun_phball");
    }
}
