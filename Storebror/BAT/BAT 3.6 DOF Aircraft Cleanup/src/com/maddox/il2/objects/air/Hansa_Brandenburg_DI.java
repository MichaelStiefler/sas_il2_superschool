package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Hansa_Brandenburg_DI extends Hansa {

    public Hansa_Brandenburg_DI() {
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;
        }
    }

    static {
        Class class1 = Hansa_Brandenburg_DI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hansa");
        Property.set(class1, "meshName", "3do/plane/Hansa_Brandenburg_DI/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/Hansa_Brandenburg_DI.fmd:Brandenburg_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHansa_Brandenburg_DI.class });
        Aircraft.weaponTriggersRegister(class1, new int[1]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
