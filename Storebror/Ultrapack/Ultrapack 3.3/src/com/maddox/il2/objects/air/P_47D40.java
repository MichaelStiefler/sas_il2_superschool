package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_47D40 extends P_47ModPackAceMakerGunsight implements TypeFighterAceMaker {

    public P_47D40() {
        this.hasRwr = true;
    }

    public void missionStarting() {
        super.missionStarting();
        if (this.FM.isStationedOnGround()) {
            this.FM.AS.setCockpitDoor(this.FM.actor, 1);
            this.FM.CT.cockpitDoorControl = 1.0F;
            printDebugMessage("*** Initial canopy state: " + (this.FM.CT.getCockpitDoor() == 1.0F ? "open" : "closed"));
        }
    }

    static {
        Class class1 = P_47D40.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47D-30(Multi1)/hier40.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47D-30(USA)/hier40.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47D-30.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D25.class });
        Property.set(class1, "LOSElevation", 1.1104F);

        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04",
                        "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03",
                        "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03" });
    }
}
