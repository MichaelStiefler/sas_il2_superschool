package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.Reflection;

public class MIG_23BN extends MIG_23 {

    public MIG_23BN() {
        this.APmode3 = false;
    }

    public void update(float f) {
        super.update(f);
        this.computeR29B_300_AB();
        this.computeSpeedLimiter();
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "MiG23BN_";
    }

    public void computeSpeedLimiter() {
        Polares polares = (Polares) Reflection.getValue(this.FM, "Wing");
        if (this.calculateMach() > 1.71D) {
            polares.CxMin_0 = 0.031F;
        }
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 22) {
            if (!this.APmode3) {
                this.APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
                this.FM.AP.setWayPoint(true);
            } else if (this.APmode3) {
                this.APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
                this.FM.AP.setWayPoint(false);
                this.FM.CT.AileronControl = 0.0F;
                this.FM.CT.ElevatorControl = 0.0F;
                this.FM.CT.RudderControl = 0.0F;
            }
        }
    }

    public void computeR29B_300_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 28670D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 19.5D) {
                f1 = 16F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (((0.000896693F * f4) - (0.0216893F * f3)) + (0.146818F * f2)) - (0.443943F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public boolean APmode3;

    static {
        Class class1 = MIG_23BN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-23");
        Property.set(class1, "meshName", "3DO/Plane/MiG-23/hierMiG23BN.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1972F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-23BN.fmd:MIG23FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_23BN.class, CockpitMIG_23Bombardier.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 7, 8 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Gun01", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalDev01", "_Rock09", "_ExternalRock10", "_Rock11", "_ExternalRock12", "_Rock13", "_ExternalRock14", "_Rock15", "_ExternalRock16", "_Dev02", "_Dev03", "_Dev04", "_Dev05", "_Dev06", "_Dev07", "_Dev08", "_Dev09", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_Dev10", "_Dev11", "_Gun02", "_Gun03", "_Gun04", "_Gun05", "_Dev12", "_Dev13", "_Dev14", "_Dev15", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_Dev16", "_Dev17", "_Dev18", "_Dev19", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26",
                "_ExternalDev20", "_ExternalDev21", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22" });
    }
}
