package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitKI_45_HeiTei extends CockpitKI_45 {

    public CockpitKI_45_HeiTei() {
        super("3DO/Cockpit/Ki-45/hier.him");
    }

    static {
        Property.set(CockpitKI_45_HeiTei.class, "normZNs", new float[] { 0.7F, 0.7F, 0.8F, 0.7F });
    }
}
