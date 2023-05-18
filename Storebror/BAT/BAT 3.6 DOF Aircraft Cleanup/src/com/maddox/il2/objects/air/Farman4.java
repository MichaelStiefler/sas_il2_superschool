package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Config;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Farman4 extends Scheme1 implements TypeScout, TypeBomber, TypeTransport {

    public Farman4() {
        this.oldProp = new int[2];
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.setDynamoOrient1(0.0F);
        this.setbDynamoRotary1(false);
        this.setRotorrpm(0);
        this.setPk(0);
    }

    protected void moveFlap(float f) {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorC_D0", 0.0F, 35F * f, 0.0F);
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
        this.setPk(Math.abs((int) (this.FM.Vwld.length() / 14D)));
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        int i = 0;
        int j = 0;
        int k = 1;
        if (this.oldProp[j] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(Farman4.Props[j][this.oldProp[j]])) {
                this.hierMesh().chunkVisible(Farman4.Props[0][this.oldProp[j]], false);
                this.oldProp[j] = i;
                this.hierMesh().chunkVisible(Farman4.Props[j][i], true);
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
        this.hierMesh().chunkSetAngles(Farman4.Props[j][0], 0.0F, 0.0F, this.propPos[j]);
        if (this.oldProp[k] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[k]) && this.hierMesh().isChunkVisible(Farman4.Props[k][this.oldProp[k]])) {
                this.hierMesh().chunkVisible(Farman4.Props[k][this.oldProp[k]], false);
                this.oldProp[k] = i;
                this.hierMesh().chunkVisible(Farman4.Props[k][i], true);
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
        this.hierMesh().chunkSetAngles(Farman4.Props[k][0], 0.0F, 0.0F, this.propPos[k]);
    }

    public float getDynamoOrient1() {
        return this.dynamoOrient1;
    }

    public void setDynamoOrient1(float f) {
        this.dynamoOrient1 = f;
    }

    public boolean isbDynamoRotary1() {
        return this.bDynamoRotary1;
    }

    public void setbDynamoRotary1(boolean flag) {
        this.bDynamoRotary1 = flag;
    }

    public int getRotorrpm() {
        return this.rotorrpm;
    }

    public void setRotorrpm(int i) {
        this.rotorrpm = i;
    }

    public int getPk() {
        return this.pk;
    }

    public void setPk(int i) {
        this.pk = i;
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float f) {
    }

    protected int                 oldProp[];
    protected static final String Props[][] = { { "Prop1_D0", "PropRot1_D0", "Prop1_D1" }, { "Prop2_D0", "PropRot2_D0", "Prop2_D1" } };
    private float                 dynamoOrient;
    private boolean               bDynamoRotary;
    private float                 dynamoOrient1;
    private boolean               bDynamoRotary1;
    private int                   rotorrpm;
    private int                   pk;

    static {
        Class class1 = Farman4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Farman4");
        Property.set(class1, "meshName", "3DO/Plane/Farman4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1918F);
        Property.set(class1, "yearExpired", 1926F);
        Property.set(class1, "FlightModel", "FlightModels/DOF.fmd:DOF_generic_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFarman4.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12" });
    }
}
