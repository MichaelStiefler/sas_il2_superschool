package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class HS123 extends HS123xyz {

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    static {
        Class class1 = HS123.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hs-123");
        Property.set(class1, "meshName", "3DO/Plane/Hs-123/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Hs123.fmd");
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "cockpitClass", new Class[] { CockpitHS123.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev02", "_ExternalBomb05", "_ExternalDev05", "_ExternalDev06" });
        Aircraft.weaponsRegister(class1, "default", new String[] { "MGunMG17si 500", "MGunMG17si 500", null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(class1, "1xDroptanks+4xSC-50", new String[] { "MGunMG17si 500", "MGunMG17si 500", null, null, "PylonBombs 1", "PylonBombs 1", "FTGun_71 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "PylonB 1", null, null, null });
        Aircraft.weaponsRegister(class1, "2xMG-FF", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMGFFt 90", "MGunMGFFt 90", null, null, null, null, null, null, null, null, null, "PylonMG 1", "PylonMG 1" });
        Aircraft.weaponsRegister(class1, "1xDroptanks+4xSC-70", new String[] { "MGunMG17si 500", "MGunMG17si 500", null, null, "PylonBombs 1", "PylonBombs 1", "FTGun_71 1", "BombGunSC70 1", "BombGunSC70 1", "BombGunSC70 1", "BombGunSC70 1", "PylonB 1", null, null, null });
        Aircraft.weaponsRegister(class1, "1xSC-250+4xSC-50", new String[] { "MGunMG17si 500", "MGunMG17si 500", null, null, "PylonBombs 1", "PylonBombs 1", null, "BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "PylonB 1", "BombGunSC250 1", null, null });
        Aircraft.weaponsRegister(class1, "1xSC-250", new String[] { "MGunMG17si 500", "MGunMG17si 500", null, null, null, null, null, null, null, null, null, "PylonB 1", "BombGunSC250 1", null, null });
        Aircraft.weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
