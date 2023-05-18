
package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B5N2 extends B5Nxyz implements TypeBomber {

    public B5N2() {
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        float f2 = -35F;
        if (f < 0.0F) {
            if (f < -20F) f2 = Aircraft.cvt(f, -41F, -20F, -35F, -15F);
            else f2 = Aircraft.cvt(f, -20F, -10F, -15F, -8F);
        } else if (f > 20F) f2 = Aircraft.cvt(f, 20F, 41F, -15F, -35F);
        else f2 = Aircraft.cvt(f, 10F, 20F, -8F, -15F);
        switch (i) {
            case 0: // '\0'
                if (f < -54F) {
                    f = -54F;
                    flag = false;
                }
                if (f > 54F) {
                    f = 54F;
                    flag = false;
                }
                if (f1 < f2) {
                    f1 = f2;
                    flag = false;
                }
                if (f1 > 55F) {
                    f1 = 55F;
                    flag = false;
                }
                if (f > -0.9F && f < 0.9F && f1 < 15.5F) flag = false;
                if (f > -32F && f < 32F && f1 < -8F && f1 > -15F) flag = false;
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 75F) this.fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15F) this.fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        if (super.thisWeaponsName.startsWith("1x91")) {
            this.fSightCurSideslip += 0.5D;
            if (this.fSightCurSideslip > 40F) this.fSightCurSideslip = 40F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoAngle", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
        } else {
            this.fSightCurSideslip++;
            if (this.fSightCurSideslip > 45F) this.fSightCurSideslip = 45F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
        }
    }

    public void typeBomberAdjSideslipMinus() {
        if (super.thisWeaponsName.startsWith("1x91")) {
            this.fSightCurSideslip -= 0.5D;
            if (this.fSightCurSideslip < -40F) this.fSightCurSideslip = -40F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoAngle", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
        } else {
            this.fSightCurSideslip--;
            if (this.fSightCurSideslip < -45F) this.fSightCurSideslip = -45F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
        }
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 6000F) this.fSightCurAltitude = 6000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 300F) this.fSightCurAltitude = 300F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 650F) this.fSightCurSpeed = 650F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) this.fSightCurSpeed = 50F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
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

    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;

    static {
        Class class1 = B5N2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B5N");
        Property.set(class1, "meshName", "3DO/Plane/B5N2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/B5N2(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/B5N2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB5N2.class, CockpitB5N2_Bombardier.class, CockpitB5N2_Gunner.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 3, 9, 3, 9, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev04",
                "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11" });
    }
}
