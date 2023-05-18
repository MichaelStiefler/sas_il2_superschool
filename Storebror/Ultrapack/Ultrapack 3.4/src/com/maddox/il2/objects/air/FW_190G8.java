package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.FuelTank_Type_D;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW_190G8 extends FW_190F_BASE {

    public FW_190G8() {
        this.fuel_tank = 0;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("7mmC_D0", false);
            this.hierMesh().chunkVisible("7mmCowl_D0", true);
            this.FM.M.massEmpty -= 58F;
        } else this.FM.M.massEmpty -= 24F;
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
        Class class1 = FW_190G8.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3do/plane/Fw-190A-8(Beta)/hier_G8.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190G-8 (Ultrapack).fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A8.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb07",
                        "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb11", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb04",
                        "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb05", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev19", "_ExternalDev20" });
    }
}
