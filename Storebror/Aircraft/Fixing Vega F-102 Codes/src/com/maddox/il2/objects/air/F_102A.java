package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_102A extends F_102 {

    public F_102A() {
        this.counter = 0;
    }

    public void rareAction(float f, boolean flag) {
        boolean targetTracked = false;
        if (this.counter++ % 5 == 0) {
            targetTracked = this.TrackingSystem(F_102.TRACKING_SYSTEM_M10, 25000, 600);
        }
        if (!targetTracked && this.counter++ % 12 == 3) { // Don't track twice, player can only see one tracking result anyway!
            this.TrackingSystem(F_102.TRACKING_SYSTEM_IRST, 50000, 15);
        }
        super.rareAction(f, flag);
    }
    

    private int counter;

    static {
        Class class1 = F_102A.class;
        F_102.initCommon(class1);
        Property.set(class1, "meshName", "3DO/Plane/F-102A/hier102late.him");
    }
}
