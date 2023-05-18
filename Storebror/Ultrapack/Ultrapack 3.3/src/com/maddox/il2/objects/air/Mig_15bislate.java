package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class Mig_15bislate extends Mig_15F {

    public void update(float f) {
        super.update(f);
        if (this.FM.getSpeedKMH() > 1100F) {
            this.FM.CT.AirBrakeControl = 1.0F;
            World.cur();
        }
    }

    static {
        Class class1 = Mig_15bislate.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-15");
        Property.set(class1, "meshName_ru", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar1956());
        Property.set(class1, "meshName_sk", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_ro", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_hu", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1951.4F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-15bis.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMig_15F.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 0, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02" });
    }
}
