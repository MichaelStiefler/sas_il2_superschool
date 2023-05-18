package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Aero_A101 extends Aeroxyz {

    public Aero_A101() {
    }

    public void update(float f) {
        super.update(f);
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("Blister2_D0", true);
            }
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("Blister2_D1", false);
            this.hierMesh().chunkVisible("Blister2_D2", false);
        }
    }

    static {
        Class class1 = Aero_A101.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A101");
        Property.set(class1, "meshName", "3DO/Plane/Aero_A101/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/Aero_A101.fmd:Aero_A101_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAero_A101.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBombB1", "_ExternalBombB2", "_ExternalBombC1", "_ExternalBombC2", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb09", "_ExternalBomb10" });
    }
}
