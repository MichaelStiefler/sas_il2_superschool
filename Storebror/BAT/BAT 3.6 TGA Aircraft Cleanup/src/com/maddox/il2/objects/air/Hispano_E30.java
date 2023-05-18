package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Hispano_E30 extends Biplanexyz {

    public Hispano_E30() {
    }

    public void update(float f) {
        super.update(f);
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("AroneL_D0", false);
                this.hierMesh().chunkVisible("AroneR_D0", false);
                this.hierMesh().chunkVisible("StrutsL_D0", false);
                this.hierMesh().chunkVisible("StrutsR_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("AroneL_D0", true);
                this.hierMesh().chunkVisible("AroneR_D0", true);
                this.hierMesh().chunkVisible("StrutsL_D0", true);
                this.hierMesh().chunkVisible("StrutsR_D0", true);
            }
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("AroneL_D1", false);
            this.hierMesh().chunkVisible("AroneR_D1", false);
            this.hierMesh().chunkVisible("StrutsL_D1", false);
            this.hierMesh().chunkVisible("StrutsR_D1", false);
            this.hierMesh().chunkVisible("AroneL_D2", false);
            this.hierMesh().chunkVisible("AroneR_D2", false);
            this.hierMesh().chunkVisible("StrutsL_D2", false);
            this.hierMesh().chunkVisible("StrutsR_D2", false);
        }
    }

    static {
        Class class1 = Hispano_E30.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "E-30");
        Property.set(class1, "meshName", "3DO/Plane/Hispano_E30/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/HispanoE30.fmd:Parasol3_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHispano_E30.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
