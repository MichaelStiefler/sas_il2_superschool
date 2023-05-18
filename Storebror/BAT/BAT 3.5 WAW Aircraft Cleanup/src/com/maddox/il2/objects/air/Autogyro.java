package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class Autogyro extends AutogyroX implements TypeScout, TypeTransport, TypeStormovik, TypeBomber {

    public Autogyro() {
        Autogyro.bChangedPit = true;
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        Autogyro.prevWing = true;
    }

    protected void moveRudder(float f) {
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("Cardan", 0.0F, 15F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("Cardan", 0.0F, 0.0F, 15F * f);
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
        if (Autogyro.prevWing) {
            this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
            this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, this.dynamoOrient, 0.0F);
        }
        super.moveFan(f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, -110F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, 110F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            Autogyro.prevWing = true;
        } else {
            Autogyro.prevWing = false;
            this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, 0.0F);
        }
        this.moveWingFold(this.hierMesh(), f);
    }

    public static boolean bChangedPit = false;
    private boolean       bDynamoOperational;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    public static boolean prevWing    = false;
    private int           pk;

    static {
        Class class1 = Autogyro.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AuG");
        Property.set(class1, "meshName", "3do/plane/Autogyro(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1960F);
        Property.set(class1, "FlightModel", "FlightModels/AuG.fmd:AUG_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAutogyro.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON010", "_CANNON011", "_CANNON012", "_ExternalDev07" });
    }
}
