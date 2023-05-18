package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class AH1W_AI extends AH1W implements TypeScout, TypeTransport, TypeStormovik {

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (World.Sun().ToSun.z < -0.22F) {
            this.FM.AS.setNavLightsState(true);
        } else {
            this.FM.AS.setNavLightsState(false);
        }
    }

    public void update(float f) {
        Pilot pilot = (Pilot) this.FM;
        if ((pilot.get_maneuver() == 25) && this.FM.AP.way.isLandingOnShip() && (this.FM.Gears.nOfGearsOnGr >= 3)) {
            this.FM.CT.BrakeControl = 1.0F;
        }
        super.update(f);
    }

    public static boolean bChangedPit      = false;

    double[]              producedAfValues = { 1400D, 1600D, 3200D, 1000D, 1000D };

    static {
        Class class1 = AH1W_AI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AH1W_AI");
        Property.set(class1, "meshName", "3DO/Plane/AH1W/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1988.5F);
        Property.set(class1, "FlightModel", "FlightModels/AH1AI.fmd:AH1FM");
        Property.set(class1, "cockpitClass", new Class[] {});
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 1, 1, 3, 3, 3, 3, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 9, 9, 1, 1, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_MGUN03", "_MGUN04", "_ExternalDev07", "_ExternalDev08", "_MGUN05", "_MGUN06", "_Pylon01", "_Pylon02" });
    }
}
