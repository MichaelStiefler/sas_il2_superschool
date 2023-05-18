package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class AH1_AI extends AH1 implements TypeScout, TypeTransport, TypeStormovik {

    public AH1_AI() {
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (World.Sun().ToSun.z < -0.22F) {
            this.FM.AS.setNavLightsState(true);
        } else {
            this.FM.AS.setNavLightsState(false);
        }
    }

    public static boolean bChangedPit = false;
    public Loc            suka;

    static {
        Class class1 = AH1_AI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "meshName", "3DO/Plane/AH1-Cobra(Multi1)/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Cobra AI");
        Property.set(class1, "FlightModel", "FlightModels/AH1AI.fmd:AH1FM");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "cockpitClass", new Class[] {});
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 9, 9, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12" });
    }
}
