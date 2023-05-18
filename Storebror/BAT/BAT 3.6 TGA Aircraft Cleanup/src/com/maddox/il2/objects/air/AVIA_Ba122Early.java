package com.maddox.il2.objects.air;

import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class AVIA_Ba122Early extends Avia_B122Early {

    public AVIA_Ba122Early() {
    }

    public void update(float f) {
        super.update(f);
        float f1 = Atmosphere.temperature((float) this.FM.Loc.z) - 273.15F;
        float f2 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f3 = (((this.FM.EI.engines[0].getControlRadiator() * f * f2) / (f2 + 50F)) * (this.FM.EI.engines[0].tWaterOut - f1)) / 256F;
        this.FM.EI.engines[0].tWaterOut -= f3;
    }

    static {
        Class class1 = AVIA_Ba122Early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ba-122Early");
        Property.set(class1, "meshName", "3DO/Plane/AviaBa-122Early/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00s());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/AviaBa-122.fmd:Ba122");
        Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
        Property.set(class1, "cockpitClass", new Class[] { CockpitAVIA_Ba122Early.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Clip11" });
    }
}
