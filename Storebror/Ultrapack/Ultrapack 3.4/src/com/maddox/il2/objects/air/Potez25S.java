package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Potez25S extends Potez25xyz {
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("default")) {
            this.hierMesh().chunkVisible("BombDoorS_D0", true);
            this.hierMesh().chunkVisible("BombBayS_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("12x10KgHE")) {
            this.hierMesh().chunkVisible("BombDoorS_D0", false);
            this.hierMesh().chunkVisible("BombBayS_D0", true);
            return;
        } else {
            return;
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
            }
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister1_D2", false);
        }
    }

    static {
        Class class1 = Potez25S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Potez-25");
        Property.set(class1, "meshName", "3DO/Plane/Potez_25/hierS.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/Potez25S.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPotez_25TOE.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb47", "_ExternalBomb48", "_ExternalBomb49", "_ExternalBomb50", "_ExternalBomb51", "_ExternalBomb52", "_ExternalBomb53", "_ExternalBomb54", "_ExternalBomb55", "_ExternalBomb56", "_ExternalBomb57", "_ExternalBomb58" });
    }
}
