package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_APUV_M extends Fuze {

    public float getDetonationDelay() {
        if ((this.getOwnerBomb() != null) && (this.getOwnerBomb().getRnd().nextFloat() < 0.1F)) {
            return 0.0F;
        } else {
            return super.getDetonationDelay();
        }
    }

    static {
        Class class1 = Fuze_APUV_M.class;
        Property.set(class1, "type", 0);
        Property.set(class1, "airTravelToArm", 610F);
        Property.set(class1, "fixedDelay", new float[] { 0.0F, 0.3F });
        Property.set(class1, "dateStart", 0x12853c5);
        Property.set(class1, "dateEnd", 0x128a1e5);
    }
}
