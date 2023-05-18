package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.PylonWB151;
import com.maddox.il2.objects.weapons.PylonWB20;
import com.maddox.il2.objects.weapons.PylonWB81A;
import com.maddox.il2.objects.weapons.PylonWB81B;
import com.maddox.rts.Property;

public class JU_87D3 extends JU_87 implements TypeStormovik {

    public JU_87D3() {
        this.bDynamoLOperational = true;
        this.bDynamoROperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (i == 33 || i == 34 || i == 9) {
            this.hierMesh().chunkVisible("GearL3_D0", false);
            this.hierMesh().chunkVisible("GearL3Rot_D0", false);
            this.bDynamoLOperational = false;
        }
        if (i == 36 || i == 37 || i == 10) {
            this.hierMesh().chunkVisible("GearR3_D0", false);
            this.hierMesh().chunkVisible("GearR3Rot_D0", false);
            this.bDynamoROperational = false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveFan(float f) {
        this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
        if (this.pk >= 1) this.pk = 1;
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            if (this.bDynamoLOperational) {
                this.hierMesh().chunkVisible("GearL3_D0", !this.bDynamoRotary);
                this.hierMesh().chunkVisible("GearL3Rot_D0", this.bDynamoRotary);
            }
            if (this.bDynamoROperational) {
                this.hierMesh().chunkVisible("GearR3_D0", !this.bDynamoRotary);
                this.hierMesh().chunkVisible("GearR3Rot_D0", this.bDynamoRotary);
            }
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - this.FM.Vwld.length() * 1.5444015264511108D) % 360F;
        this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, this.dynamoOrient, 0.0F);
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, this.dynamoOrient + 12.5F, 0.0F);
        super.moveFan(f);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
    }

    public void update(float f) {
        for (int i = 1; i < 5; i++)
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, 15F - 30F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);

        super.update(f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) break;
                if (aobj[i] instanceof PylonWB81B || aobj[i] instanceof PylonWB81A || aobj[i] instanceof PylonWB20 || aobj[i] instanceof PylonWB151) {
                    this.FM.M.massEmpty += 190F;
                    break;
                }
                i++;
            } while (true);
        }
    }

    private boolean bDynamoLOperational;
    private boolean bDynamoROperational;
    private float   dynamoOrient;
    private boolean bDynamoRotary;
    private int     pk;

    static {
        Class class1 = JU_87D3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87D-3j.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87D-3/hier_D1.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87D3.class, CockpitJU_87D3_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb05",
                        "_ExternalBomb06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_CANNON03",
                        "_CANNON04", "_CANNON05", "_CANNON06", "_MGUN17", "_MGUN18", "_MGUN19", "_MGUN20", "_MGUN21", "_MGUN22", "_MGUN23", "_MGUN24", "_MGUN25", "_MGUN26", "_MGUN27", "_MGUN28", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON10",
                        "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04" });
    }
}
