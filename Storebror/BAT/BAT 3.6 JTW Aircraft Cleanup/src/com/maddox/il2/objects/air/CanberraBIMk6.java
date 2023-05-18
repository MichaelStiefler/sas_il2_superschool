package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class CanberraBIMk6 extends Canberra implements TypeStormovik {

    public CanberraBIMk6() {
    }

    public void onAircraftLoaded() {
        if (!(this.getBulletEmitterByHookName("_CANNON01") instanceof GunEmpty)) {
            this.hierMesh().chunkVisible("BombBay_Short", true);
            this.hierMesh().chunkVisible("BombDoorLeft_Short", true);
            this.hierMesh().chunkVisible("BombDoorRight_Short", true);
            this.hierMesh().chunkVisible("GunPack", true);
            this.hierMesh().chunkVisible("BombBay", false);
            this.hierMesh().chunkVisible("BombDoorLeft", false);
            this.hierMesh().chunkVisible("BombDoorRight", false);
        }
    }

    static {
        Class class1 = CanberraBIMk6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Canberra B(I) Mk.6");
        Property.set(class1, "meshName", "3DO/Plane/Canberra_B.2(Multi1)/hier_B(I)6.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar1956());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/CanberraBMK6.fmd:CANBERRA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCanberraB62.class, CockpitCanberraBombardier.class, CockpitCanberraBombardier2.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_MGUN01", "_MGUN01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev10", "_ExternalDev11", "_ExternalRock23",
                "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalDev07", "_ExternalDev08", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_ExternalDev09", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36" });
    }
}
