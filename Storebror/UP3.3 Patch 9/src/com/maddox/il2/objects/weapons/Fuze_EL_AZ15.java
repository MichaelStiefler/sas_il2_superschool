package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_EL_AZ15 extends Fuze_EL_AZ {

    protected int getProbableArmingMin() {
        if (this.releasedBombMode == 1) {
            return 3700;
        }
        if (this.releasedBombMode == 2) {
            return 5700;
        }
        return this.releasedBombMode != 3 ? 0 : 900;
    }

    protected int getProbableArmingMax() {
        if (this.releasedBombMode == 1) {
            return 5600;
        }
        if (this.releasedBombMode == 2) {
            return 8700;
        }
        return this.releasedBombMode != 3 ? 0 : 1600;
    }

    public float getDetonationDelay() {
        if (this.releasedBombMode == 1) {
            return 0.0F;
        }
        if (this.releasedBombMode == 2) {
            return 0.05F;
        }
        return this.releasedBombMode != 3 ? 0.0F : 8F;
    }

    static {
        Class class1 = Fuze_EL_AZ15.class;
        Property.set(class1, "type", 4);
        Property.set(class1, "armingTime", 1000);
        Property.set(class1, "fuzeSelectionMode", 1);
        Property.set(class1, "dateStart", 0);
        Property.set(class1, "dateEnd", 0x1282cb5);
    }
}
