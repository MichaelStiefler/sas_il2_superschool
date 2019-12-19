package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class G_55 extends G_55xyz {

    public G_55() {
    }

    public void onAircraftLoaded() {
        if (this == World.getPlayerAircraft()) {
            this.FM.CT.bHasCockpitDoorControl = false;
        }
    }

    static {
        Class class1 = G_55.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G.55");
        Property.set(class1, "meshName_it", "3DO/Plane/G-55(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "meshName", "3DO/Plane/G-55(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/G-55.fmd");
        Property.set(class1, "LOSElevation", 0.9119F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitMC_205.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03" });
    }
}