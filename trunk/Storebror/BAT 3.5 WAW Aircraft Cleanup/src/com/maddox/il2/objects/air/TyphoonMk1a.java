package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class TyphoonMk1a extends TEMPEST {

    public TyphoonMk1a() {
    }

    public void onAircraftLoaded() {
        ((FlightModelMain) (super.FM)).EI.engines[0].doSetKillControlAfterburner();
        super.onAircraftLoaded();
    }

    static Class class$com$maddox$il2$objects$air$TyphoonMk1a;
    static Class class$com$maddox$il2$objects$air$CockpitTYPHOON1a;

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Typhoon");
        Property.set(class1, "meshName", "3DO/Plane/TyphoonMkIa(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/TyphoonMkIa(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Typhoon1B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTYPHOON1B.class });
        Property.set(class1, "LOSElevation", 0.93655F);
        Aircraft.weaponTriggersRegister(class1, new int[12]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12" });
    }
}
