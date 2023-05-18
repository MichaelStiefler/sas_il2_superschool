package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class HS_123A1 extends HS_123 {

    public HS_123A1() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        int i = Mission.getMissionDate(true);
        if ((i > 0) && (i < 0x127e1b4)) {
            return "prewar_";
        } else {
            return "";
        }
    }

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

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    static {
        Class class1 = HS_123A1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hs-123");
        Property.set(class1, "meshName", "3DO/Plane/Hs-123A-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00s());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Hs-123.fmd");
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "LOSElevation", 0.66F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitHS_123A1.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_BombSpawn00", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_ExternalBomb00", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev00", "_ExternalDev01" });
    }
}
