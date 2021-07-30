package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_EL_AZ28 extends Fuze_EL_AZ {

    protected int getProbableArmingMin() {
        if (this.releasedBombMode == 1) {
            return 1000;
        }
        return (this.releasedBombMode != 2) && (this.releasedBombMode != 3) ? 0 : 1400;
    }

    protected int getProbableArmingMax() {
        if (this.releasedBombMode == 1) {
            return 1500;
        }
        return (this.releasedBombMode != 2) && (this.releasedBombMode != 3) ? 0 : 2200;
    }

    public float getDetonationDelay() {
        if (this.releasedBombMode == 1) {
            return 0.0F;
        }
        return (this.releasedBombMode != 2) && (this.releasedBombMode != 3) ? 0.0F : 0.15F;
    }

    static {
        Class class1 = Fuze_EL_AZ28.class;
        Property.set(class1, "type", 4);
        Property.set(class1, "armingTime", 1000);
        Property.set(class1, "fuzeSelectionMode", 1);
    }
}
