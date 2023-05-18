package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class JEEP extends CAR {

    public JEEP() {
    }

    public void update(float f) {
        if (this.FM.AS.bIsAboutToBailout) this.hierMesh().chunkVisible("Helm_D0", false);
        this.FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
        super.update(f);
    }

    static {
        Class class1 = JEEP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Jeep");
        Property.set(class1, "meshName", "3do/plane/Jeep/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Jeep.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJeep.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
