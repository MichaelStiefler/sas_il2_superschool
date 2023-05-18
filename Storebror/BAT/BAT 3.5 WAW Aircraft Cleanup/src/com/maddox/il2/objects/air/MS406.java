package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class MS406 extends MS400X {

    public MS406() {
        this.flapps = 0.0F;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals("fi")) {
            int i = Mission.getMissionDate(true);
            if (i > 0) {
                if (i < 0x1282df2) {
                    return "winterwar_";
                }
                if (i < 0x1282fd5) {
                    return "early_";
                }
            }
        }
        return "";
    }

    public void update(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.2F, 0.0F);
        if (Math.abs(this.flapps - Aircraft.xyz[1]) > 0.01F) {
            this.flapps = Aircraft.xyz[1];
            this.hierMesh().chunkSetLocate("OilRad_D0", Aircraft.xyz, Aircraft.ypr);
        }
        super.update(f);
    }

    private float flapps;

    static {
        Class class1 = MS406.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Morane");
        Property.set(class1, "meshNameDemo", "3DO/Plane/MS406(fi)/hier.him");
        Property.set(class1, "meshName_fi", "3DO/Plane/MS406(fi)/hier.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName", "3DO/Plane/MS406(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/MS406.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMS406.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }
}
