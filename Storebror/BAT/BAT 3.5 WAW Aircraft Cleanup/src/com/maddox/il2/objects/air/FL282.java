package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FL282 extends FL282X implements TypeScout, TypeTransport, TypeStormovik, TypeBomber, TypeX4Carrier {

    public FL282() {
        FL282.bChangedPit = true;
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        FL282.prevWing = true;
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("Cardan", 0.0F, 15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Cardan1", 0.0F, 15F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("Cardan", 0.0F, 0.0F, 15F * f);
        this.hierMesh().chunkSetAngles("Cardan1", 0.0F, 0.0F, 15F * f);
    }

    protected void moveFlap(float f) {
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (FL282.prevWing) {
            this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
            this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, this.dynamoOrient, 0.0F);
            this.hierMesh().chunkSetAngles("Prop3_D0", 0.0F, -this.dynamoOrient, 0.0F);
        }
        super.moveFan(f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("Gunpods")) {
            this.hierMesh().chunkVisible("Pod_01", true);
            this.hierMesh().chunkVisible("Pod_02", true);
        } else {
            this.hierMesh().chunkVisible("Pod_01", false);
            this.hierMesh().chunkVisible("Pod_02", false);
        }
        if (this.thisWeaponsName.startsWith("Panzershrek")) {
            this.hierMesh().chunkVisible("Panzer1", true);
            this.hierMesh().chunkVisible("Panzer2", true);
        } else {
            this.hierMesh().chunkVisible("Panzer1", false);
            this.hierMesh().chunkVisible("Panzer2", false);
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
    private boolean       bDynamoOperational;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    public static boolean prevWing    = false;
    private int           pk;

    static {
        Class class1 = FL282.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Fl-282");
        Property.set(class1, "meshName", "3do/plane/FL282/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1960F);
        Property.set(class1, "FlightModel", "FlightModels/AuG.fmd:AUG_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFL282.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON010", "_CANNON011", "_CANNON012", "_ExternalDev07", "_ExternalRock07", "_ExternalRock08" });
    }
}
