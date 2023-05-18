package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.rts.Property;

public class G50bisA extends G50bis_xyz {
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            for (int i = 0; i < aobj.length; i++) {
                if (aobj[i] instanceof Bomb) {
                    this.hierMesh().chunkVisible("RackL_D0", true);
                    this.hierMesh().chunkVisible("RackR_D0", true);
                }
            }

        }
    }

    static {
        Class class1 = G50bisA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G.50");
        Property.set(class1, "meshName", "3DO/Plane/G-50bisA(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/G-50bisA(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1944.8F);
        Property.set(class1, "FlightModel", "FlightModels/G50bisA.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitG50.class });
        Property.set(class1, "LOSElevation", 0.98615F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
