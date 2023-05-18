package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class IAI_Kfir_C2 extends MIRAGE implements TypeBomber {

    public IAI_Kfir_C2() {
        this.APmode3 = false;
    }

    public void update(float f) {
        this.computeJ79J1E_AB();
        super.update(f);
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "Kfir_";
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

    public void computeJ79J1E_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 22600D;
        }
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.calculateMach() >= 1.1D)) {
            this.FM.producedAF.x -= 9000D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 19F) {
                f1 = 12F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                float f5 = f4 * f;
                f1 = ((((-0.000219756F * f5) + (0.0108394F * f4)) - (0.160409F * f3)) + (0.726606F * f2)) - (0.974738F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public boolean APmode3;

    static {
        Class class1 = IAI_Kfir_C2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Kfir");
        Property.set(class1, "meshName", "3DO/Plane/Kfir/Kfir.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1959F);
        Property.set(class1, "yearExpired", 1980F);
        Property.set(class1, "FlightModel", "FlightModels/IAI-Kfir_C2.fmd:MIRAGE_FM");
        Property.set(class1, "LOSElevation", 0.725F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIRAGE.class, CockpitMIRAGE_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_Rock01", "_Rock01", "_Rock02", "_Rock02", "_Rock03", "_Rock03", "_Rock04", "_Rock04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_Bomb05", "_Bomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_Bomb11", "_Bomb12", "_ExternalBomb13", "_ExternalBomb14", "_Bomb15", "_Bomb16", "_Bomb17", "_Bomb18", "_Bomb19", "_Bomb20", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_Bomb25", "_Bomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_Bomb31", "_Bomb32", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_Bomb39", "_Bomb40", "_ExternalBomb41" });
    }
}
