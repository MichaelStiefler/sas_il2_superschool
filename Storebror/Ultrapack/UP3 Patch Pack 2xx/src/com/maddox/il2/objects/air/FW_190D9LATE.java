package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class FW_190D9LATE extends FW_190 implements TypeFighterAceMaker {
    public int    k14Mode;
    public int    k14WingspanType;
    public float  k14Distance;
    private float kangle;

    public FW_190D9LATE() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200.0f;
        this.kangle = 0.0f;
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        ++this.k14Mode;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    // -------------------------------------------------------------------------------------------------------
    // TODO: skylla: gyro-gunsight distance HUD log (for details please see P_51D25NA.class):

    public void typeFighterAceMakerAdjDistancePlus() {
        this.adjustK14AceMakerDistance(+10.0f);
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.adjustK14AceMakerDistance(-10.0f);
    }

    private void adjustK14AceMakerDistance(float f) {
        this.k14Distance += f;
        if (this.k14Distance > 1000.0f) {
            this.k14Distance = 1000.0f;
        } else if (this.k14Distance < 160.0f) {
            this.k14Distance = 160.0f;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int) (this.k14Distance) + "m");
    }
    /*
     * public void typeFighterAceMakerAdjDistancePlus() {
     * this.k14Distance += 10.0f;
     * if (this.k14Distance > 800.0f) {
     * this.k14Distance = 800.0f;
     * }
     * HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
     * }
     * 
     * public void typeFighterAceMakerAdjDistanceMinus() {
     * this.k14Distance -= 10.0f;
     * if (this.k14Distance < 200.0f) {
     * this.k14Distance = 200.0f;
     * }
     * HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
     * }
     */

    // Allied plane wingspans (approximately):
    public void typeFighterAceMakerAdjSideslipPlus() {
        try {
            this.adjustAceMakerSideSlip(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        try {
            this.adjustAceMakerSideSlip(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustAceMakerSideSlip(int i) throws IOException {
        if (!((i == 1) || (i == -1))) {
            throw new IOException("Wrong input value! Only +1 and -1 allowed!");
        }
        this.k14WingspanType += i;
        String s = "Wingspan Selected: ";
        String s1 = "Yak-3/Yak-9/La-5/P-39/MiG-3";
        String s2 = "B-24";

        switch (this.k14WingspanType) {
            // case 0: s += s1; break; //like Bf-109
            case 1:
                s += "P-51/P-47/P-80/Spitfire/Typhoon/Hurricane";
                break; // like Fw-190
            case 2:
                s += "P-38";
                break; // like Ju-87
            case 3:
                s += "Mosquito/IL-2/Beaufighter";
                break; // like Me-210
            case 4:
                s += "A-20/Pe-2";
                break; // like Do-217
            case 5:
                s += "20m";
                break; // like Ju-88
            case 6:
                s += "B-25/A-26";
                break; // like Ju-188
            case 7:
                s += "DC-3";
                break; // like Ju-52
            case 8:
                s += "B-17/Halifax/Lancaster";
                break; // like He-177
            case 9:
                s += s2;
                break; // like Fw-200
            case 10:
                this.adjustAceMakerSideSlip(-1);
                s += s2;
                break;
            case -1:
                this.adjustAceMakerSideSlip(1);
                s += s1;
                break;
            default:
                this.k14WingspanType = 0;
                s += s1;
                break;
        }

        HUD.log(AircraftHotKeys.hudLogWeaponId, s);
    }

    /*
     * old code:
     * public void typeFighterAceMakerAdjSideslipPlus() {
     * --this.k14WingspanType;
     * if (this.k14WingspanType < 0) {
     * this.k14WingspanType = 0;
     * }
     * HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
     * }
     * 
     * public void typeFighterAceMakerAdjSideslipMinus() {
     * ++this.k14WingspanType;
     * if (this.k14WingspanType > 9) {
     * this.k14WingspanType = 9;
     * }
     * HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
     * }
     */
    // -------------------------------------------------------------------------------------------------------

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerReplicateToNet(final NetMsgGuaranted netMsgGuaranted) throws IOException {
        netMsgGuaranted.writeByte(this.k14Mode);
        netMsgGuaranted.writeByte(this.k14WingspanType);
        netMsgGuaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(final NetMsgInput netMsgInput) throws IOException {
        this.k14Mode = netMsgInput.readByte();
        this.k14WingspanType = netMsgInput.readByte();
        this.k14Distance = netMsgInput.readFloat();
    }

    public static void moveGear(final HierMesh hierMesh, final float n) {
        hierMesh.chunkSetAngles("GearL2_D0", 0.0f, 77.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR2_D0", 0.0f, 77.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearL3_D0", 0.0f, 157.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR3_D0", 0.0f, 157.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearC99_D0", 20.0f * n, 0.0f, 0.0f);
        hierMesh.chunkSetAngles("GearC2_D0", 0.0f, 0.0f, 0.0f);
        final float max = Math.max(-n * 1500.0f, -94.0f);
        hierMesh.chunkSetAngles("GearL5_D0", 0.0f, -max, 0.0f);
        hierMesh.chunkSetAngles("GearR5_D0", 0.0f, -max, 0.0f);
    }

    protected void moveGear(final float n) {
        moveGear(this.hierMesh(), n);
    }

    public void moveSteering(final float n) {
        if (this.FM.CT.getGear() < 0.98f) {
            return;
        }
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0f, -n, 0.0f);
    }

    public void update(final float n) {
        for (int i = 1; i < 13; ++i) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0f, -10.0f * this.kangle, 0.0f);
        }
        this.kangle = (0.95f * this.kangle) + (0.05f * this.FM.EI.engines[0].getControlRadiator());
        super.update(n);
    }

    static {
        final Class var_class = FW_190D9LATE.class;
        new SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "FW190");
        Property.set(var_class, "meshName", "3DO/Plane/Fw-190D-9(Beta)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(var_class, "yearService", 1944.6f);
        Property.set(var_class, "yearExpired", 1948.0f);
        Property.set(var_class, "FlightModel", "FlightModels/Fw-190D-9Late.fmd");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitFW_190D9LATE.class });
        Property.set(var_class, "LOSElevation", 0.764106f);
        Aircraft.weaponTriggersRegister(var_class, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON04" });
        Aircraft.weaponsRegister(var_class, "default", new String[] { "MGunMG131si 750", "MGunMG131si 750", "MGunMG15120MGs 250", "MGunMG15120MGs 250" });
        Aircraft.weaponsRegister(var_class, "none", new String[] { null, null, null, null });
    }
}
