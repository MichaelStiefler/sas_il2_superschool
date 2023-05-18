package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class J1N1_F extends J1N1 implements TypeBomber {

    public J1N1_F() {
        this.bSightAutomation = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
        this.fSightCurReadyness = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.4F;
        if (this.fSightCurForwardAngle > 75F) {
            this.fSightCurForwardAngle = 75F;
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.4F;
        if (this.fSightCurForwardAngle < -15F) {
            this.fSightCurForwardAngle = -15F;
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.5D;
        if (this.thisWeaponsName.startsWith("1x")) {
            if (this.fSightCurSideslip > 40F) {
                this.fSightCurSideslip = 40F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + this.fSightCurSideslip);
        } else {
            if (this.fSightCurSideslip > 10F) {
                this.fSightCurSideslip = 10F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + this.fSightCurSideslip);
        }
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.5D;
        if (this.thisWeaponsName.startsWith("1x")) {
            if (this.fSightCurSideslip < -40F) {
                this.fSightCurSideslip = -40F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + this.fSightCurSideslip);
        } else {
            if (this.fSightCurSideslip < -10F) {
                this.fSightCurSideslip = -10F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + this.fSightCurSideslip);
        }
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 6000F) {
            this.fSightCurAltitude = 6000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
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
        if (this.fSightCurSpeed > 650F) {
            this.fSightCurSpeed = 650F;
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
        double d = (this.fSightCurSpeed / 3.6000000000000001D) * Math.sqrt(this.fSightCurAltitude * 0.20387359799999999D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.atan(d / this.fSightCurAltitude);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(this.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readFloat();
        this.fSightCurSideslip = netmsginput.readFloat();
    }

    public boolean bSightAutomation;
    public float   fSightCurDistance;
    public float   fSightCurForwardAngle;
    public float   fSightCurSideslip;
    public float   fSightCurAltitude;
    public float   fSightCurSpeed;
    public float   fSightCurReadyness;
    public float   fSightSetForwardAngle;
    static {
        Class class1 = J1N1_F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "J1N1F");
        Property.set(class1, "meshName", "3do/plane/J1N1F(Multi1)/F_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_ja", "3do/plane/J1N1F(ja)/F_hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-46-IIIKai.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJ1N1_S.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 11, 12, 0, 3, 3, 9, 9, 9, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn02", "_BombSpawn03", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_BombSpawn04" });
    }
}
