package com.maddox.il2.objects.air;

import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class F11CHawkII extends Avia_B5xx {

    public void update(float f) {
        super.update(f);
        float f3 = Atmosphere.temperature((float) this.FM.Loc.z) - 273.15F;
        float f4 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if (f4 < 0.0F) {
            f4 = 0.0F;
        }
        float f5 = (((this.FM.EI.engines[0].getControlRadiator() * f * f4) / (f4 + 50F)) * (this.FM.EI.engines[0].tWaterOut - f3)) / 256F;
        this.FM.EI.engines[0].tWaterOut -= f5;
    }

    public void moveWheelSink() {
    }

    static {
        Class class1 = F11CHawkII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F11CHawkII");
        Property.set(class1, "meshName", "3DO/Plane/F11CHawkII(Multi)/hierSea.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00s());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/F11C2HawkII.fmd");
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
        Property.set(class1, "cockpitClass", new Class[] { CockpitF11C.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
