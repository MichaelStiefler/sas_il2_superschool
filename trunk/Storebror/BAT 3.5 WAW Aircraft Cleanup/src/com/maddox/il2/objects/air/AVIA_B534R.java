package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class AVIA_B534R extends Avia_B5xx {

    public AVIA_B534R() {
    }

    public void update(float f) {
        super.update(f);
        float f1 = Atmosphere.temperature((float) ((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z) - 273.15F;
        float f2 = Pitot.Indicator((float) ((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z, super.FM.getSpeedKMH());
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f3 = (((((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator() * f * f2) / (f2 + 50F)) * (((FlightModelMain) (super.FM)).EI.engines[0].tWaterOut - f1)) / 256F;
        ((FlightModelMain) (super.FM)).EI.engines[0].tWaterOut -= f3;
    }

    static {
        Class class1 = AVIA_B534R.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-534");
        Property.set(class1, "meshName", "3DO/Plane/AviaB-534R/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00s());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1950F);
        Property.set(class1, "FlightModel", "FlightModels/AviaB-534.fmd");
        Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
        Property.set(class1, "cockpitClass", new Class[] { CockpitAVIA_B534R.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
    }
}
