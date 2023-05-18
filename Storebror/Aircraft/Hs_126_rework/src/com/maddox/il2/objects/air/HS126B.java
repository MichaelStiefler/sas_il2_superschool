package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.AircraftTools;

public class HS126B extends HS126 {
    static {
        Class class1 = HS126B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "HS126B");
        Property.set(class1, "meshName", "3DO/Plane/HS-126B/hierSpats.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/HS-126B/hierSpats.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/He46.fmd:He46_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHe46.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02" });

        AircraftTools.weaponsRegister(class1, "default", new String[] { "MGunMG17si 500", null, "MGunMG15t 1500", null, null, null, null, null, null, null, null });
        AircraftTools.weaponsRegister(class1, "20x10Kg HE Bombs", new String[] { "MGunMG17si 500", null, "MGunMG15t 1500", null, "BombGun10kgCZ 10", "BombGun10kgCZ 10", null, null, null, null, null });
        AircraftTools.weaponsRegister(class1, "1x50Kg HE Bombs", new String[] { "MGunMG17si 500", null, "MGunMG15t 1500", "BombGunPuW50 1", null, null, null, null, null, "PylonHe46BombRack 1", null });
        AircraftTools.weaponsRegister(class1, "2x50Kg HE Bombs", new String[] { "MGunMG17si 500", null, "MGunMG15t 1500", null, null, null, null, "BombGunPuW50 1", "BombGunPuW50 1", "PylonHe46BombRack 1", "PylonHe46BombRack 1" });
        AircraftTools.weaponsRegister(class1, "1x100Kg HE Bombs", new String[] { "MGunMG17si 500", null, "MGunMG15t 1500", "BombGunPuW100 1", null, null, null, null, null, "PylonHe46BombRack 1", null });
        AircraftTools.weaponsRegister(class1, "2x100Kg HE Bombs", new String[] { "MGunMG17si 500", null, "MGunMG15t 1500", "BombGunPuW100 1", null, null, "BombGunPuW100 1", null, null, "PylonHe46BombRack 1", "PylonHe46BombRack 1" });
        AircraftTools.weaponsRegister(class1, "none", new String[] { "MGunMG17si 500", null, "MGunMG15t 1500", null, null, null, null, null, null, null, null });
    }
}
