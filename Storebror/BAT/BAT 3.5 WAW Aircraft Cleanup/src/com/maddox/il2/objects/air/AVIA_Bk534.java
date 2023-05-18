package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class AVIA_Bk534 extends Avia_Bk5xx {

    public AVIA_Bk534() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals("sk")) {
            int i = Mission.getMissionDate(true);
            if (i > 0) {
                if (i < 0x127e1b5) {
                    return "CSR_";
                }
                if (i < 0x1280929) {
                    return "SKvsPL_";
                }
            }
        }
        return "";
    }

    public void update(float f) {
        super.update(f);
        float f1 = Atmosphere.temperature((float) this.FM.Loc.z) - 273.15F;
        float f2 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f3 = (((this.FM.EI.engines[0].getControlRadiator() * f * f2) / (f2 + 50F)) * (this.FM.EI.engines[0].tWaterOut - f1)) / 256F;
        this.FM.EI.engines[0].tWaterOut -= f3;
    }

    static {
        Class class1 = AVIA_Bk534.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bk-534");
        Property.set(class1, "meshName_sk", "3DO/Plane/AviaBk-534(sk)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar00s());
        Property.set(class1, "meshName_de", "3DO/Plane/AviaBk-534(de)/hier.him");
        Property.set(class1, "PaintScheme_de", new PaintSchemeFMPar00s());
        Property.set(class1, "meshName", "3DO/Plane/AviaBk-534/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00s());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1950F);
        Property.set(class1, "FlightModel", "FlightModels/AviaBk-534.fmd:Bk534");
        Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
        Property.set(class1, "cockpitClass", new Class[] { CockpitAVIA_Bk534.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
    }
}
