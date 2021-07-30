package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_EL_AZ25 extends Fuze_EL_AZ {

    protected int getProbableArmingMin() {
        if (this.releasedBombMode == 1) {
            return 1900;
        }
        if (this.releasedBombMode == 2) {
            return 1900;
        }
        return this.releasedBombMode != 3 ? 0 : 700;
    }

    protected int getProbableArmingMax() {
        if (this.releasedBombMode == 1) {
            return 3500;
        }
        if (this.releasedBombMode == 2) {
            return 3600;
        }
        return this.releasedBombMode != 3 ? 0 : 800;
    }

    public float getDetonationDelay() {
        if (this.releasedBombMode == 1) {
            return 0.0F;
        }
        if (this.releasedBombMode == 2) {
            return 0.08F;
        }
        return this.releasedBombMode != 3 ? 0.0F : 14F;
    }

    static {
        Class class1 = Fuze_EL_AZ25.class;
        Property.set(class1, "type", 4);
        Property.set(class1, "armingTime", 1000);
        Property.set(class1, "fuzeSelectionMode", 1);
        Property.set(class1, "dateStart", 0x1282cb5);
        Property.set(class1, "dateEnd", 0x1310655);
    }
}
