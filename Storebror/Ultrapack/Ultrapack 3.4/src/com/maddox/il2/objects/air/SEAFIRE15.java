// This file is part of the SAS IL-2 Sturmovik 1946
// Late Seafire Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
// www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/03/11

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SEAFIRE15 extends SeafireLate {

    public SEAFIRE15() {
    }

    public void moveArrestorHook(float paramFloat) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -57.0F * paramFloat, 0.0F);
        this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -12.0F * paramFloat, 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = (0.1385F * paramFloat);
        this.hierMesh().chunkSetLocate("Hook3_D0", Aircraft.xyz, Aircraft.ypr);
        this.arrestor = paramFloat;
    }

    static {
        Class class1 = SEAFIRE15.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SeafireMkXV_SAS(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1942.11F);
        Property.set(class1, "yearExpired", 1944.12F);
        Property.set(class1, "FlightModel", "FlightModels/SeafireXV.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSeafireLate.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 3, 9, 9, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev08", "_ExternalDev09" });
    }
}
