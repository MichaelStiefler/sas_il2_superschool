package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SiemensSchuckertD1 extends SiemensSchuckertS {

    public SiemensSchuckertD1() {
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
        Class class1 = SiemensSchuckertD1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SiemensSchuckertD1");
        Property.set(class1, "meshName", "3do/plane/SiemensSchuckertD1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1917F);
        Property.set(class1, "yearExpired", 1919F);
        Property.set(class1, "FlightModel", "FlightModels/SiemensSchuckertD1.fmd:SSD1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSSDI.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
