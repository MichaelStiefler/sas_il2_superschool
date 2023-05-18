package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class TA_B4000 extends U_2 implements TypeStormovik {

    public TA_B4000() {
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
        }
        if (shot.chunkName.startsWith("Pilot2")) {
            this.killPilot(shot.initiator, 1);
        }
        if (shot.chunkName.startsWith("Engine") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        super.msgShot(shot);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -25F) {
            af[0] = -25F;
            flag = false;
        } else if (af[0] > 25F) {
            af[0] = 25F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (f < 10F) {
            if (af[1] < -5F) {
                af[1] = -5F;
                flag = false;
            }
        } else if (af[1] < -15F) {
            af[1] = -15F;
            flag = false;
        }
        if (af[1] > 35F) {
            af[1] = 35F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 2.0F) && (f1 < 17F)) {
            return false;
        }
        if (f1 > -5F) {
            return true;
        }
        if (f1 > -12F) {
            f1 += 12F;
            return f > (12F + (f1 * 2.571429F));
        } else {
            f1 = -f1;
            return f > f1;
        }
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLrod_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRrod_D0", 0.0F, -35F * f, 0.0F);
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TA_B4000.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TA_B4000.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = TA_B4000.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TA_B4000");
        Property.set(class1, "meshName", "3DO/Plane/TA_B4000/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1967F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitU2UT.class, CockpitU2VN.class });
        Property.set(class1, "FlightModel", "FlightModels/U-2VS.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN00", "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_BombSpawn01" });
    }
}
