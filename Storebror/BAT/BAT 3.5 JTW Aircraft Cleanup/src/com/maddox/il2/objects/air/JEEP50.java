package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class JEEP50 extends CAR {

    public JEEP50() {
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
                this.hierMesh().chunkVisible("Helm_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot6_D0", false);
                this.hierMesh().chunkVisible("Pilot6_D1", true);
                break;
        }
    }

    public void update(float f) {
        if (this.FM.AS.bIsAboutToBailout) {
            this.hierMesh().chunkVisible("Helm_D0", false);
        }
        this.FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
        super.update(f);
    }

    static {
        Class class1 = JEEP50.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Jeep");
        Property.set(class1, "meshName", "3do/plane/Jeep50cal/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Jeep50.fmd:WAGEN");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJeep.class, CockpitJeep_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 0, 0, 0, 0, 0, 1, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_CANNON01", "_ExternalBomb01" });
    }
}
