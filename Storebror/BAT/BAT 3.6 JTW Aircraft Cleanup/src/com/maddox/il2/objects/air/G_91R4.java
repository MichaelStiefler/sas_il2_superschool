package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.rts.Property;

public class G_91R4 extends G_91 implements TypeFastJet {

    public G_91R4() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "R4_";
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public void update(float f) {
        super.update(f);
    }

    public boolean bToFire;
    static {
        Class class1 = G_91R4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G.91");
        Property.set(class1, "meshName", "3DO/Plane/G.91/hierR4.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1958.9F);
        Property.set(class1, "yearExpired", 1980.3F);
        Property.set(class1, "FlightModel", "FlightModels/G.91R.fmd:G91FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitG_91.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04" });
    }
}
