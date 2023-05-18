package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class BlenheimS1 extends BLENHEIM {

    public BlenheimS1() {
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayR1_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayR2_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayL1_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayL2_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 0.0F, 90F * f);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 0.0F, -90F * f);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 0.0F, -90F * f);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, 0.0F, 90F * f);
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
        if (this.fSightCurAltitude > 10000F) {
            this.fSightCurAltitude = 10000F;
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
        if (this.fSightCurSpeed > 520F) {
            this.fSightCurSpeed = 520F;
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

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("2x500lbs")) {
            this.hierMesh().chunkVisible("Bay1_D0", true);
            this.hierMesh().chunkVisible("Bay2_D0", true);
            this.hierMesh().chunkVisible("Bay3_D0", true);
            this.hierMesh().chunkVisible("Bay4_D0", true);
            this.hierMesh().chunkVisible("BayR1_D0", false);
            this.hierMesh().chunkVisible("BayR2_D0", false);
            this.hierMesh().chunkVisible("BayL1_D0", false);
            this.hierMesh().chunkVisible("BayL2_D0", false);
            this.hierMesh().chunkVisible("BayRF1_D0", true);
            this.hierMesh().chunkVisible("BayRF2_D0", true);
            this.hierMesh().chunkVisible("BayLF1_D0", true);
            this.hierMesh().chunkVisible("BayLF2_D0", true);
            this.hierMesh().chunkVisible("RackFinBL", false);
            return;
        }
        if (this.thisWeaponsName.equals("4x250lbs")) {
            this.hierMesh().chunkVisible("Bay1_D0", true);
            this.hierMesh().chunkVisible("Bay2_D0", true);
            this.hierMesh().chunkVisible("Bay3_D0", true);
            this.hierMesh().chunkVisible("Bay4_D0", true);
            this.hierMesh().chunkVisible("BayR1_D0", false);
            this.hierMesh().chunkVisible("BayR2_D0", false);
            this.hierMesh().chunkVisible("BayL1_D0", false);
            this.hierMesh().chunkVisible("BayL2_D0", false);
            this.hierMesh().chunkVisible("BayRF1_D0", true);
            this.hierMesh().chunkVisible("BayRF2_D0", true);
            this.hierMesh().chunkVisible("BayLF1_D0", true);
            this.hierMesh().chunkVisible("BayLF2_D0", true);
            this.hierMesh().chunkVisible("RackFinBL", false);
            return;
        }
        if (this.thisWeaponsName.equals("4x250lbs+8x40lbsF")) {
            this.hierMesh().chunkVisible("Bay1_D0", true);
            this.hierMesh().chunkVisible("Bay2_D0", true);
            this.hierMesh().chunkVisible("Bay3_D0", true);
            this.hierMesh().chunkVisible("Bay4_D0", true);
            this.hierMesh().chunkVisible("RackFinBL", false);
            return;
        }
        if (this.thisWeaponsName.equals("4x120lbs+8x40lbsPara")) {
            this.hierMesh().chunkVisible("Bay1_D0", true);
            this.hierMesh().chunkVisible("Bay2_D0", true);
            this.hierMesh().chunkVisible("Bay3_D0", true);
            this.hierMesh().chunkVisible("Bay4_D0", true);
            this.hierMesh().chunkVisible("RackFinBL", false);
            return;
        }
        if (this.thisWeaponsName.equals("4x250lbs+8x30lbsInc")) {
            this.hierMesh().chunkVisible("Bay1_D0", true);
            this.hierMesh().chunkVisible("Bay2_D0", true);
            this.hierMesh().chunkVisible("Bay3_D0", true);
            this.hierMesh().chunkVisible("Bay4_D0", true);
            this.hierMesh().chunkVisible("RackFinBL", false);
            return;
        }
        if (this.thisWeaponsName.equals("2xSBC250_20lbsPara+2x250lbs+8x30lbsInc")) {
            this.hierMesh().chunkVisible("Bay1_D0", false);
            this.hierMesh().chunkVisible("Bay2_D0", true);
            this.hierMesh().chunkVisible("Bay3_D0", false);
            this.hierMesh().chunkVisible("Bay4_D0", true);
            this.hierMesh().chunkVisible("RackFinBL", false);
            return;
        }
        if (this.thisWeaponsName.equals("4xSBC250_20lbs+8x30lbsInc")) {
            this.hierMesh().chunkVisible("Bay1_D0", false);
            this.hierMesh().chunkVisible("Bay2_D0", false);
            this.hierMesh().chunkVisible("Bay3_D0", false);
            this.hierMesh().chunkVisible("Bay4_D0", false);
            this.hierMesh().chunkVisible("RackFinBL", false);
            return;
        } else {
            return;
        }
    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;

    static {
        Class class1 = BlenheimS1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Blenheim");
        Property.set(class1, "meshName", "3DO/Plane/BlenheimMkI(Multi1)/hierF.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Blenheim_MkI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBLENHEIM1.class, CockpitBlenheimS1_Bombardier.class, CockpitBLENHEIM1_TGunner.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_ExternalDev03", "_ExternalDev04",
                "_ExternalBomb39", "_ExternalBomb40", "_ExternalBomb41", "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalBomb49", "_ExternalBomb50", "_ExternalBomb51", "_ExternalBomb52", "_ExternalBomb53", "_ExternalBomb54", "_ExternalBomb55", "_ExternalBomb56", "_ExternalBomb57", "_ExternalBomb58", "_ExternalBomb59", "_ExternalBomb60", "_ExternalBomb61", "_ExternalBomb62" });
    }
}
