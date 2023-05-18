package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.rts.Property;

public class F_102A_Early extends F_102 {

    public F_102A_Early() {
        this.counter = 0;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "Early_";
    }

    public void rareAction(float f, boolean flag) {
        if ((this.counter++ % 5) == 0) {
            this.TrackingSystem(F_102.TRACKING_SYSTEM_M3, 20000, 600);
        }
        super.rareAction(f, flag);
    }

    private int counter;

    static {
        Class class1 = F_102A_Early.class;
        F_102.initCommon(class1);
        Property.set(class1, "meshName", "3DO/Plane/F-102A/hier102early.him");
    }
}
