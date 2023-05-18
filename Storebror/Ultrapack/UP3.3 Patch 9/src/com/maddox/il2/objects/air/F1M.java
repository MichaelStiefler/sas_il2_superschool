
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class F1M extends F1Mxyz {

    public F1M() {
        F1M.bChangedPit = true;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            F1M.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            F1M.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = F1M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F1M2A");
        Property.set(class1, "meshName", "3DO/Plane/F1M2A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/F1M2A(Ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar01());
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/F1M.fmd");
        Property.set(class1, "LOSElevation", 1.0728F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitF1M2_P.class, CockpitF1M2_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_MGUN03" });
    }
}
