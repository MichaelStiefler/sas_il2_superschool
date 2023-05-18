package com.maddox.il2.objects.air;

import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class C_97K extends B_29X implements TypeBomber, TypeTransport {

    public C_97K() {
        this.APmode1 = false;
        this.APmode2 = false;
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            if (!this.APmode1) {
                this.APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else if (this.APmode1) {
                this.APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        }
        if (i == 21) {
            if (!this.APmode2) {
                this.APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else if (this.APmode2) {
                this.APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
        }
    }

    public boolean APmode1;
    public boolean APmode2;

    static {
        Class class1 = C_97K.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "C-97");
        Property.set(class1, "meshName", "3DO/Plane/C_97K(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/C-97.fmd:C97FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitC_97.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 14, 14, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02" });
    }
}
