package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.BombGun500kgJ;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class D4Y3 extends D4Y_ten implements TypeDiveBomber, TypeBomber {

    public D4Y3() {
        this.flapps = 0.0F;
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 75F) {
            this.fSightCurForwardAngle = 75F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15F) {
            this.fSightCurForwardAngle = -15F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip++;
        if (this.fSightCurSideslip > 45F) {
            this.fSightCurSideslip = 45F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip--;
        if (this.fSightCurSideslip < -45F) {
            this.fSightCurSideslip = -45F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
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

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            D4Y3.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            D4Y3.bChangedPit = true;
        }
    }

    public void doWoundPilot(int i, float f) {
        super.doWoundPilot(i, f);
        if (this.FM.isPlayers()) {
            D4Y3.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (this.FM.isPlayers()) {
            D4Y3.bChangedPit = true;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.64F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][0] instanceof BombGun500kgJ)) {
            this.hierMesh().chunkVisible("Bay01_D0", false);
            this.hierMesh().chunkVisible("Bay02_D0", false);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -33F) {
                    f = -33F;
                    flag = false;
                }
                if (f > 33F) {
                    f = 33F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 62F) {
                    f1 = 62F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("Oil_D0", 0.0F, -22F * f1, 0.0F);
            for (int i = 1; i < 9; i++) {
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);
            }

        }
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister2_D0", false);
                this.hierMesh().chunkVisible("Blister3_D0", false);
                this.hierMesh().chunkVisible("Gunsite_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister2_D0", true);
                this.hierMesh().chunkVisible("Blister3_D0", true);
                this.hierMesh().chunkVisible("Gunsite_D0", true);
            }
        }
    }

    public static boolean bChangedPit = false;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurForwardAngle;
    public float          fSightSetForwardAngle;
    public float          fSightCurSideslip;
    private float         flapps;

    static {
        Class class1 = D4Y3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D4Y");
        Property.set(class1, "meshName", "3DO/Plane/D4Y3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/D4Y3(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/D4Y3.fmd:D4Y3_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitD4Y3.class, CockpitD4Y3_Bombardier.class, CockpitD4Y3_TGunner.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 9, 9, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn02", "_BombSpawn03", "_BombSpawn01", "_ExternalDev02", "_ExternalDev03", "_BombSpawn04", "_ExternalDev04", "_ExternalDev05" });
    }
}
