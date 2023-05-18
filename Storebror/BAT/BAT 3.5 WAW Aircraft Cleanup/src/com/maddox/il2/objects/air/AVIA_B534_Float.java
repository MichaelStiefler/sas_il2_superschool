package com.maddox.il2.objects.air;

import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class AVIA_B534_Float extends Avia_B5xx implements TypeSailPlane, TypeScout {

    public AVIA_B534_Float() {
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
        Class class1 = AVIA_B534_Float.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AVIA_B534_Float");
        Property.set(class1, "meshName", "3DO/Plane/AVIA_B534_Float(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1950F);
        Property.set(class1, "FlightModel", "FlightModels/AVIA_B534_Float.fmd:AVIA_B534_Float_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAVIA_B534_Float.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
    }
}
