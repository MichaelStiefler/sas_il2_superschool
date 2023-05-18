package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class IliaMuromV extends IliaMurom {

    public IliaMuromV() {
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -40F) {
                    f = -40F;
                }
                if (f > 40F) {
                    f = 40F;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -35F) {
                    f = -35F;
                }
                if (f > 35F) {
                    f = 35F;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -35F) {
                    f = -35F;
                }
                if (f > 35F) {
                    f = 35F;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            for (int i = 0; i < 4; i++) {
                if ((this.FM.AS.astateEngineStates[i] > 3) && (this.FM.EI.engines[i].getReadyness() < 0.1F)) {
                    this.FM.AS.repairEngine(i);
                }
            }

            for (int j = 0; j < 4; j++) {
                if ((this.FM.AS.astateTankStates[j] > 3) && (this.FM.AS.astatePilotStates[4] < 50F) && (this.FM.AS.astatePilotStates[7] < 50F) && (World.Rnd().nextFloat() < 0.1F)) {
                    this.FM.AS.repairTank(j);
                }
            }

        }
    }

    public void update(float f) {
        super.update(f);
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if ((explosion.chunkName != null) && (explosion.chunkName.startsWith("Wing") || explosion.chunkName.startsWith("Tail")) && explosion.chunkName.endsWith("D3") && (explosion.power < 0.014F)) {
            return;
        } else {
            super.msgExplosion(explosion);
            return;
        }
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
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 50F;
        if (this.fSightCurAltitude > 5000F) {
            this.fSightCurAltitude = 5000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 50F;
        if (this.fSightCurAltitude < 300F) {
            this.fSightCurAltitude = 300F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 350F) {
            this.fSightCurSpeed = 350F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) {
            this.fSightCurSpeed = 50F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;

    static {
        Class class1 = IliaMuromV.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ilia");
        Property.set(class1, "meshName", "3DO/Plane/IliaMuromV/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/IliaMV.fmd:IliaMV_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitIliaMuromV.class, CockpitIliaMuromV_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_BombSpawn01", "_BombSpawn02" });
    }
}
