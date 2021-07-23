package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitB36Jets extends CockpitB36 {
    public CockpitB36Jets() {
        super("3DO/Cockpit/B36/hierD.him", "bf109");
    }
    
    public void reflectWorldToInstruments(float f) {
        super.reflectWorldToInstruments(f);
        for (int engineIndex=6; engineIndex < 10; engineIndex++)
            this.mesh.chunkSetAngles("power" + (engineIndex+1) + "L", 0.0F, 60F - 70F * this.interp(this.setNew.throttle[engineIndex], this.setOld.throttle[engineIndex], f), 0.0F);
    }

    static {
        Property.set(CockpitB36Jets.class, "normZN", 2.5F);
        Property.set(CockpitB36Jets.class, "normZNs", new float[] { 2.0F, 2.0F, 2.25F, 2.0F });
    }
}
