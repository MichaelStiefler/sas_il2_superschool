package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Ki232 extends DO_335 implements TypeX4Carrier, TypeGuidedBombCarrier {

    public Ki232() {
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

    public void onAircraftLoaded() {
        if (this.thisWeaponsName.startsWith("Type3_Rockets_Schrage_Musik")) {
            this.hierMesh().chunkVisible("RocketArray_D0", true);
        } else {
            this.hierMesh().chunkVisible("RocketArray_D0", false);
        }
    }

    public boolean typeGuidedBombCgetIsGuiding() {
        return false;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag) {
    }

    public boolean typeGuidedBombCisMasterAlive() {
        return false;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag) {
    }

    public boolean        bToFire;
    private float         deltaAzimuth;
    private float         deltaTangage;
    public static boolean bChangedPit = false;
    public static boolean prevWing    = false;
    static {
        Class class1 = Ki232.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-232");
        Property.set(class1, "meshName", "3DO/Plane/Ki-232/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ki232.fmd:Ki232_FM");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_Ki232.class });
        Property.set(class1, "LOSElevation", 1.00705F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN02", "_MGUN03", "_MGUN01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17" });
    }
}
