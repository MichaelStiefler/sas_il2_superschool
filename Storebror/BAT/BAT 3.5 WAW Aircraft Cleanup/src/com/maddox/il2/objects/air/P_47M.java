package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Property;

public class P_47M extends P_47ModPackAceMakerGunsight {

    public P_47M() {
    }

    public void missionStarting() {
        super.missionStarting();
        if (this.FM.isStationedOnGround()) {
            this.FM.AS.setCockpitDoor(((Interpolate) (this.FM)).actor, 1);
            this.FM.CT.cockpitDoorControl = 1.0F;
            P_47ModPack.printDebugMessage("*** Initial canopy state: " + (this.FM.CT.getCockpitDoor() != 1.0F ? "closed" : "open"));
        }
    }

    static {
        Class class1 = P_47M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47M(Multi1)/hierM.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47M(USA)/hierM.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47M.fmd:P47Pack_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D25.class });
        Property.set(class1, "LOSElevation", 1.1104F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb01" });
    }
}
