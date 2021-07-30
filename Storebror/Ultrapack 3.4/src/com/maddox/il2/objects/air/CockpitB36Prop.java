package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitB36Prop extends CockpitB36 {
    public CockpitB36Prop() {
        super("3DO/Cockpit/B36/hier.him", "bf109");
    }

    static {
        Property.set(CockpitB36Prop.class, "normZN", 2.5F);
        Property.set(CockpitB36Prop.class, "normZNs", new float[] { 2.0F, 2.0F, 2.25F, 2.0F });
    }
}
