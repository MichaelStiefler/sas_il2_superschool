package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_EL_AZ38 extends Fuze_EL_AZ {

    protected int getProbableArmingMin() {
        if (this.releasedBombMode == 1) {
            return 3500;
        }
        if (this.releasedBombMode == 2) {
            return 3700;
        }
        return this.releasedBombMode != 3 ? 0 : 600;
    }

    protected int getProbableArmingMax() {
        if (this.releasedBombMode == 1) {
            return 6000;
        }
        if (this.releasedBombMode == 2) {
            return 5000;
        }
        return this.releasedBombMode != 3 ? 0 : 1000;
    }

    public float getDetonationDelay() {
        if (this.releasedBombMode == 1) {
            return 0.05F;
        }
        if (this.releasedBombMode == 2) {
            return 0.2F;
        }
        return this.releasedBombMode != 3 ? 0.0F : 5F;
    }

    static {
        Class class1 = Fuze_EL_AZ38.class;
        Property.set(class1, "type", 5);
        Property.set(class1, "armingTime", 1000);
        Property.set(class1, "fuzeSelectionMode", 1);
    }
}
