package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_177 extends KI_61 implements TypeFighter, TypeStormovik, TypeX4Carrier {

    public KI_177() {
        KI_177.prevWing = true;
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
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
        Class class1 = KI_177.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-177");
        Property.set(class1, "meshName", "3DO/Plane/Ki-177/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1955.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-177.fmd:Ki-177_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_61.class, CockpitKI_Bombardier.class });
        Property.set(class1, "LOSElevation", 0.81055F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9, 1, 1, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_MGUN05", "_MGUN06", "_ExternalDev03", "_ExternalBomb03" });
    }
}
