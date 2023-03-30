package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.BombSC50;
import com.maddox.rts.Property;

public class BF_109E4B extends BF_109Ex {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) for (int i = 0; i < aobj.length; i++)
            if (aobj[i] instanceof BombSC50) {
                this.hierMesh().chunkVisible("Rack", false);
                this.hierMesh().chunkVisible("ETC50", true);
            }
    }

    static {
        Class class1 = BF_109E4B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3do/plane/Bf-109E-4B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109E-4B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Ex.class });
        Property.set(class1, "LOSElevation", 0.74985F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
