package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class G_55A extends G_55xyz {

    public G_55A() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("default") || this.thisWeaponsName.endsWith("default")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("default+50kg") || this.thisWeaponsName.endsWith("default+50kg")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("default+100kg") || this.thisWeaponsName.endsWith("default+100kg")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("default+DT150") || this.thisWeaponsName.endsWith("default+DT150")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("4x12,7mm") || this.thisWeaponsName.endsWith("4x12,7mm")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", true);
            this.hierMesh().chunkVisible("BredaWL_D0", true);
            return;
        }
        if (this.thisWeaponsName.startsWith("4x12,7mm+50kg") || this.thisWeaponsName.endsWith("4x12,7mm+50kg")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", true);
            this.hierMesh().chunkVisible("BredaWL_D0", true);
            return;
        }
        if (this.thisWeaponsName.startsWith("4x12,7mm+100kg") || this.thisWeaponsName.endsWith("4x12,7mm+100kg")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", true);
            this.hierMesh().chunkVisible("BredaWL_D0", true);
            return;
        }
        if (this.thisWeaponsName.startsWith("4x12,7mm+DT150") || this.thisWeaponsName.endsWith("4x12,7mm+DT150")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", true);
            this.hierMesh().chunkVisible("BredaWL_D0", true);
            return;
        }
        if (this.thisWeaponsName.startsWith("2x12,7+3x20") || this.thisWeaponsName.endsWith("2x12,7+3x20")) {
            this.hierMesh().chunkVisible("WGunL_D0", true);
            this.hierMesh().chunkVisible("WGunR_D0", true);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("2x12,7+3x20+50kg") || this.thisWeaponsName.endsWith("2x12,7+3x20+50kg")) {
            this.hierMesh().chunkVisible("WGunL_D0", true);
            this.hierMesh().chunkVisible("WGunR_D0", true);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("2x12,7+3x20+100kg") || this.thisWeaponsName.endsWith("2x12,7+3x20+100kg")) {
            this.hierMesh().chunkVisible("WGunL_D0", true);
            this.hierMesh().chunkVisible("WGunR_D0", true);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("2x12,7+3x20+DT150") || this.thisWeaponsName.endsWith("2x12,7+3x20+DT150")) {
            this.hierMesh().chunkVisible("WGunL_D0", true);
            this.hierMesh().chunkVisible("WGunR_D0", true);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        }
        if (this.thisWeaponsName.startsWith("none") || this.thisWeaponsName.endsWith("none")) {
            this.hierMesh().chunkVisible("WGunL_D0", false);
            this.hierMesh().chunkVisible("WGunR_D0", false);
            this.hierMesh().chunkVisible("BredaWR_D0", false);
            this.hierMesh().chunkVisible("BredaWL_D0", false);
            return;
        } else {
            return;
        }
    }

    static {
        Class class1 = G_55A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G.55");
        Property.set(class1, "meshName_it", "3DO/Plane/G-55A(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "meshName", "3DO/Plane/G-55A(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1959.5F);
        Property.set(class1, "FlightModel", "FlightModels/G-55-late.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMC_205.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
    }
}
