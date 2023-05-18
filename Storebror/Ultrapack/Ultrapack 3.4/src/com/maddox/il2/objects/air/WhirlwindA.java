// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~S3
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/10/17

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class WhirlwindA extends Whirlwind implements TypeFighter, TypeStormovik {

    static {
        Class class1 = WhirlwindA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Whirlwind");
        Property.set(class1, "meshName", "3DO/Plane/Whirlwind(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Whirlwind.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitWhirlwind.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1,
                new int[] { 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05",
                        "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02",
                        "_ExtSBC2L01", "_ExtSBC2L02", "_ExtSBC2L03", "_ExtSBC2L04", "_ExtSBC2L05", "_ExtSBC2L06", "_ExtSBC2L07", "_ExtSBC2L07", "_ExtSBC2L07", "_ExtSBC2R01", "_ExtSBC2R02", "_ExtSBC2R03", "_ExtSBC2R04", "_ExtSBC2R05", "_ExtSBC2R06",
                        "_ExtSBC2R07", "_ExtSBC3L01", "_ExtSBC3L02", "_ExtSBC3L03", "_ExtSBC3L04", "_ExtSBC3L05", "_ExtSBC3L06", "_ExtSBC3L07", "_ExtSBC3L08", "_ExtSBC3L09", "_ExtSBC3L10", "_ExtSBC3L11", "_ExtSBC3L12", "_ExtSBC3L12", "_ExtSBC3L12",
                        "_ExtSBC3R01", "_ExtSBC3R02", "_ExtSBC3R03", "_ExtSBC3R04", "_ExtSBC3R05", "_ExtSBC3R06", "_ExtSBC3R07", "_ExtSBC3R08", "_ExtSBC3R09", "_ExtSBC3R10", "_ExtSBC3R11", "_ExtSBC3R12", "_ExternalDev01", "_ExternalDev02",
                        "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06" });
    }
}
