package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class SPITFIREFR14E_CW extends SPITFIREFR14E implements TypeFighterAceMaker, TypeBNZFighter {

    public SPITFIREFR14E_CW() {
    }

    static {
        Class class1 = SPITFIREFR14E_CW.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireFRXIVE(Multi1)/hierCW.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName", "3DO/Plane/SpitfireFRXIVE(GB)/hierCW.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Spitfire-F-XIV-G65-21.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSpitFR14E.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev08", "_ExternalDev01", "_ExternalBomb01" });
    }
}
