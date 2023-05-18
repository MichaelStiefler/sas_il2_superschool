package com.maddox.il2.objects.weapons;

import com.maddox.il2.objects.air.A_37;
import com.maddox.rts.Property;

public class FuelTank_Tank100gal_A37 extends FuelTank {
	
	public void visibilityAsBase(boolean flag) {
        super.visibilityAsBase(flag);
        this.drawing(false);
    }

    public void start() {
        super.start();
        this.drawing(false);
        if (!(this.getOwner() instanceof A_37)) {
            return;
        }
        A_37 a = (A_37) this.getOwner();
        if (a.FM.getOverload() < 0.0F) {
            return;
        }
        a.dropTanks();
        this.destroy();
    }
	
    static {
        Class localClass = FuelTank_Tank100gal_A37.class;
        Property.set(localClass, "mesh", "3DO/arms/100gal_A37/mono.sim");
        Property.set(localClass, "kalibr", 0.46F);
        Property.set(localClass, "massa", 300.0F);
    }
}
