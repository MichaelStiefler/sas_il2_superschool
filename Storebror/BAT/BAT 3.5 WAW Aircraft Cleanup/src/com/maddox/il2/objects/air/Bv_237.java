package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Bv_237 extends Bv_237X implements TypeFighter, TypeStormovik, TypeX4Carrier {

    public Bv_237() {
        Bv_237.prevWing = true;
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
    }

    public void onAircraftLoaded() {
        if (this.thisWeaponsName.startsWith("2 x Mk-103")) {
            this.hierMesh().chunkVisible("Gun01_D0", true);
            this.hierMesh().chunkVisible("Gun02_D0", true);
        } else {
            this.hierMesh().chunkVisible("Gun01_D0", false);
            this.hierMesh().chunkVisible("Gun02_D0", false);
        }
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public boolean        bToFire;
    private float         deltaAzimuth;
    private float         deltaTangage;
    public static boolean bChangedPit = false;
    public static boolean prevWing    = false;
    static {
        Class class1 = Bv_237.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bv-237");
        Property.set(class1, "meshName", "3DO/Plane/Bv_237/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1955.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bv237.fmd:Bv237_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBv_237.class });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
