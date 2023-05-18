package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.rts.Property;

public class P_11C extends P_11 {

    public P_11C() {
    }

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

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            P_11C.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            P_11C.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = P_11C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P.11");
        Property.set(class1, "meshName", "3DO/Plane/P-11c(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_pl", "3DO/Plane/P-11c/hier.him");
        Property.set(class1, "PaintScheme_pl", new PaintSchemeFCSPar01());
        Property.set(class1, "meshName_ro", "3DO/Plane/P-11c(Romanian)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1939.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-11c.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_11C.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
