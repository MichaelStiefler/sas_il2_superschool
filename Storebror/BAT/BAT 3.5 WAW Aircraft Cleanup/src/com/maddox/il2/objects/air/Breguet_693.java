package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Breguet_693 extends Breguet693xyz implements TypeFighter, TypeStormovik {

    public Breguet_693() {
    }

    static {
        Class class1 = Breguet_693.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Breguet693");
        Property.set(class1, "meshName", "3DO/Plane/Breguet693(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/BeaufighterMk1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBreguett693.class });
        Property.set(class1, "LOSElevation", 0.84305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 9, 3, 3, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_MGUN09" });
    }
}
