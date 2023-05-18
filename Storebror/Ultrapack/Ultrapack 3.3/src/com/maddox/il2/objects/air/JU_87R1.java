package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class JU_87R1 extends JU_87 {

    public JU_87R1() {
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (i == 36 || i == 37 || i == 10) {
            this.hierMesh().chunkVisible("GearR3_D0", false);
            this.hierMesh().chunkVisible("GearR3Rot_D0", false);
            this.bDynamoOperational = false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) this.pk = 1;
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("GearR3_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("GearR3Rot_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - this.FM.Vwld.length() * 1.5444015264511108D) % 360F;
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
    }

    public void update(float f) {
        for (int i = 1; i < 9; i++)
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, 0.0F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);

        super.update(f);
    }

    private boolean bDynamoOperational;
    private float   dynamoOrient;
    private boolean bDynamoRotary;
    private int     pk;

    static {
        Class class1 = JU_87R1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87R-1.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87B-1/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87B2.class, CockpitJU_87B2_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1938.9F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb04",
                "_ExternalBomb05", "_ExternalDev01", "_ExternalDev02" });
    }
}
