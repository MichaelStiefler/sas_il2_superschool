package com.maddox.il2.objects.air;

import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class C_97 extends B_29X implements TypeBomber, TypeTransport {

    public C_97() {
        this.APmode1 = false;
        this.APmode2 = false;
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 0.0F, 90F * f);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 0.0F, 90F * f);
        this.hierMesh().chunkSetAngles("Bay03_D0", 0.0F, 0.0F, -90F * f);
        this.hierMesh().chunkSetAngles("Bay04_D0", 0.0F, 0.0F, -90F * f);
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
        Class class1 = C_97.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "C_97");
        Property.set(class1, "meshName", "3DO/Plane/C_97(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/C-97.fmd:C97FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitC_97.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04" });
    }
}
