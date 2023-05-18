package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class P_24F extends P_24 {

    public P_24F() {
    }

    protected void update(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxmgun")) {
            if (s.endsWith("01")) {
                Aircraft.debugprintln(this, "*** Machine Gun: Disabled..");
                this.FM.AS.setJamBullets(0, 0);
            }
            if (s.endsWith("02")) {
                Aircraft.debugprintln(this, "*** Cannon: Disabled..");
                this.FM.AS.setJamBullets(0, 1);
            }
            if (s.endsWith("03")) {
                Aircraft.debugprintln(this, "*** Cannon: Disabled..");
                this.FM.AS.setJamBullets(1, 0);
            }
            if (s.endsWith("04")) {
                Aircraft.debugprintln(this, "*** Machine Gun: Disabled..");
                this.FM.AS.setJamBullets(1, 1);
            }
            this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
        }
    }

    static {
        Class class1 = P_24F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P.24");
        Property.set(class1, "meshName", "3DO/Plane/P-24F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/P-24F.fmd:PZL24");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_24.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
