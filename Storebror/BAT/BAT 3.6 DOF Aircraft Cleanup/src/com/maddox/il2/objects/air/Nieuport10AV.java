package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Nieuport10AV extends NieuportR {

    public Nieuport10AV() {
    }

    public void doMurderPilot(int i) {
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("Pilot1_D1", true);
        if (!this.FM.AS.bIsAboutToBailout) {
            this.hierMesh().chunkVisible("Gore1_D0", true);
        }
    }

    static {
        Class class1 = Nieuport10AV.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Nieuport10AV");
        Property.set(class1, "meshName", "3do/plane/Nieuport10AV/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/Nieuport10AV.fmd:Nieuport10_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitNieuport10AV1.class, CockpitNieuport10AV2.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON013", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON010", "_CANNON011", "_CANNON012" });
    }
}
