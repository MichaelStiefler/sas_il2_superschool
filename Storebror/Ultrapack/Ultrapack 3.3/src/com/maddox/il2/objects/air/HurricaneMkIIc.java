package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class HurricaneMkIIc extends Hurricane implements TypeFighter, TypeStormovik {

    public HurricaneMkIIc() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_CANNON01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("CannonL_D0", false);
            this.hierMesh().chunkVisible("CannonR_D0", false);
        }
        if (this.getBulletEmitterByHookName("_ExternalDev01") instanceof GunEmpty && this.getBulletEmitterByHookName("_ExternalBomb01") instanceof GunEmpty) this.hierMesh().chunkVisible("PylonL_D0", false);
        if (this.getBulletEmitterByHookName("_ExternalDev02") instanceof GunEmpty && this.getBulletEmitterByHookName("_ExternalBomb02") instanceof GunEmpty) this.hierMesh().chunkVisible("PylonR_D0", false);
        if (World.cur().camouflage == 2) this.hierMesh().chunkVisible("filter", true);
        else this.hierMesh().chunkVisible("filter", false);
    }

    static {
        Class class1 = HurricaneMkIIc.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hurri");
        Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIIc(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/HurricaneMkII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHURRII.class });
        Property.set(class1, "LOSElevation", 0.66895F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01",
                        "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock06", "_ExternalRock06", "_ExternalRock05", "_ExternalRock05", "_ExternalRock04", "_ExternalRock04", "_ExternalRock07",
                        "_ExternalRock07", "_ExternalRock08", "_ExternalRock08" });
    }
}
