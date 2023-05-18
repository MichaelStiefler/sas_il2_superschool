package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class ME_262A1A extends ME_262 {

    public ME_262A1A() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if ((this.getBulletEmitterByHookName("_ExternalBomb01") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalDev03") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalDev05") instanceof GunEmpty)) {
            this.hierMesh().chunkVisible("Pylon_D0", false);
        }
        if (this.getBulletEmitterByHookName("_CANNON07") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("MK103", false);
        } else {
            this.hierMesh().chunkVisible("MK103", true);
        }
        if (this.getBulletEmitterByHookName("_CANNON09") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("MK108", false);
        } else {
            this.hierMesh().chunkVisible("MK108", true);
        }
        super.onAircraftLoaded();
        this.FM.isPlayers();
    }

    static {
        Class class1 = ME_262A1A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me 262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262A-1a/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262A-1a.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 0, 0, 1, 1, 1, 1, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON10", "_ExternalDev05", "_ExternalDev06" });
    }
}
