package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class Anasal extends Anatra {

    public Anasal() {
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1L_D0", 0.0F, 15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1R_D0", 0.0F, 15F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -18F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLD_D0", 0.0F, 18F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLU_D0", 0.0F, 18F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -18F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRD_D0", 0.0F, 18F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRU_D0", 0.0F, 18F * f, 0.0F);
    }

    protected void moveFlap(float f1) {
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
    }

    public void update(float f) {
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER() && (this.FM.CT.PowerControl > 0.95F) && (this.FM.EI.engines[0].getRPM() > 400F)) {
            this.FM.AS.setSootState(this, 0, 1);
        } else {
            this.FM.AS.setSootState(this, 0, 0);
        }
        super.update(f);
    }

    protected void moveFan(float f) {
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        int i = 0;
        int j = 0;
        int k = 1;
        if (this.oldProp[j] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(Anasal.Props[j][this.oldProp[j]])) {
                this.hierMesh().chunkVisible(Anasal.Props[0][this.oldProp[j]], false);
                this.oldProp[j] = i;
                this.hierMesh().chunkVisible(Anasal.Props[j][i], true);
            }
        }
        if (i == 0) {
            this.propPos[j] = (this.propPos[j] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
        } else {
            float f1 = 57.3F * this.FM.EI.engines[0].getw();
            f1 %= 2880F;
            f1 /= 2880F;
            if (f1 <= 0.5F) {
                f1 *= 2.0F;
            } else {
                f1 = (f1 * 2.0F) - 2.0F;
            }
            f1 *= 1200F;
            this.propPos[j] = (this.propPos[j] + (f1 * f)) % 360F;
        }
        this.hierMesh().chunkSetAngles(Anasal.Props[j][0], 0.0F, 0.0F, this.propPos[j]);
        if (this.oldProp[k] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[k]) && this.hierMesh().isChunkVisible(Anasal.Props[k][this.oldProp[k]])) {
                this.hierMesh().chunkVisible(Anasal.Props[k][this.oldProp[k]], false);
                this.oldProp[k] = i;
                this.hierMesh().chunkVisible(Anasal.Props[k][i], true);
            }
        }
        if (i == 0) {
            this.propPos[k] = (this.propPos[k] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
        } else {
            float f2 = 57.3F * this.FM.EI.engines[0].getw();
            f2 %= 2880F;
            f2 /= 2880F;
            if (f2 <= 0.5F) {
                f2 *= 2.0F;
            } else {
                f2 = (f2 * 2.0F) - 2.0F;
            }
            f2 *= 1200F;
            this.propPos[k] = (this.propPos[k] + (f2 * f)) % 360F;
        }
        this.hierMesh().chunkSetAngles(Anasal.Props[k][0], 0.0F, 0.0F, this.propPos[k]);
    }

    protected int                 oldProp[] = { 0, 0 };
    protected static final String Props[][] = { { "Prop1_D0", "PropRot1_D0", "Prop1_D1" }, { "Prop2_D0", "PropRot2_D0", "Prop2_D1" } };
    private float                 dynamoOrient;
    private boolean               bDynamoRotary;

    static {
        Class class1 = Anasal.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Anasal");
        Property.set(class1, "meshName", "3DO/Plane/Anasal(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1918F);
        Property.set(class1, "yearExpired", 1926F);
        Property.set(class1, "FlightModel", "FlightModels/Anasal.fmd:Anasal_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAnasal.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01", "_BombSpawn02" });
    }
}
