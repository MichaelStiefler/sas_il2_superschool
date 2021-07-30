package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.FuelTank_Type_D;
import com.maddox.rts.Property;

public class FW_190F3 extends FW_190F_BASE {

    public FW_190F3() {
        this.fuel_tank = 0;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.M.massEmpty += 124F;
        this.hierMesh().chunkVisible("Flap01_D0", true);
        this.hierMesh().chunkVisible("Flap04_D0", true);
        this.hierMesh().chunkVisible("Flap01Holed_D0", false);
        this.hierMesh().chunkVisible("Flap04Holed_D0", false);
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) for (int i = 0; i < aobj.length; i++)
            if (aobj[i] instanceof FuelTank_Type_D) this.fuel_tank++;
        if (this.fuel_tank > 1) {
            this.hierMesh().chunkVisible("Flap01_D0", false);
            this.hierMesh().chunkVisible("Flap04_D0", false);
            this.hierMesh().chunkVisible("Flap01Holed_D0", true);
            this.hierMesh().chunkVisible("Flap04Holed_D0", true);
        }
    }

    private int fuel_tank;

    static {
        Class class1 = FW_190F3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-5(Beta)/hier_FG.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190F-3 (Ultrapack).fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A5.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02",
                        "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb06", "_ExternalBomb06",
                        "_ExternalBomb07", "_ExternalBomb07", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb05", "_ExternalBomb05", "_ExternalDev11", "_ExternalBomb10", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb11", "_ExternalBomb08",
                        "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb09" });
    }
}
