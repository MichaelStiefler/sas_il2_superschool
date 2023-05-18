package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class H_75A3 extends P_36 {

    public H_75A3() {
        this.kangle = 0.0F;
        this.flapps = 0.0F;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals("fi")) {
            int i = Mission.getMissionDate(true);
            if (i > 0) {
                if (i < 0x1282fd5) {
                    return "early_";
                }
                if (i > 0x12855b9) {
                    return "late_";
                }
            }
            return "";
        }
        if (regiment.country().equals("fr")) {
            return "FR_";
        } else {
            return "generic_";
        }
    }

    public void update(float f) {
        if (Math.abs(this.flapps - this.kangle) > 0.01F) {
            this.flapps = this.kangle;
            for (int i = 1; i < 12; i++) {
                String s = "Water" + i + "_D0";
                this.hierMesh().chunkSetAngles(s, 0.0F, -10F * this.kangle, 0.0F);
            }

        }
        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        super.update(f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.indexOf("bombs") == -1) {
            this.hierMesh().chunkVisible("RackRIn", false);
            this.hierMesh().chunkVisible("RackROut", false);
            this.hierMesh().chunkVisible("RackLIn", false);
            this.hierMesh().chunkVisible("RackLOut", false);
        }
    }

    private float kangle;
    private float flapps;

    static {
        Class class1 = H_75A3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "H-75");
        Property.set(class1, "meshName", "3DO/Plane/Hawk75A-3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar07());
        Property.set(class1, "meshName_us", "3DO/Plane/Hawk75A-3(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_fi", "3DO/Plane/Hawk75A-3(Multi1)/Fi_hier.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFMPar07());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-36A-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHawk_75A3.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
    }
}
