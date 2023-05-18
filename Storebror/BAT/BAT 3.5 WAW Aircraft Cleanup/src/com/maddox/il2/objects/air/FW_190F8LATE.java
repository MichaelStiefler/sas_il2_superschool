package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.FuelTank_Type_D;
import com.maddox.rts.Property;

public class FW_190F8LATE extends FW_190F implements TypeStormovik {

    public FW_190F8LATE() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        this.FM.M.massEmpty -= 0.0F;
        this.hierMesh().chunkVisible("Flap01_D0", true);
        this.hierMesh().chunkVisible("Flap04_D0", true);
        this.hierMesh().chunkVisible("Flap01Holed_D0", false);
        this.hierMesh().chunkVisible("Flap04Holed_D0", false);
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if (aobj[i] instanceof FuelTank_Type_D) {
                    this.hierMesh().chunkVisible("Flap01_D0", false);
                    this.hierMesh().chunkVisible("Flap04_D0", false);
                    this.hierMesh().chunkVisible("Flap01Holed_D0", true);
                    this.hierMesh().chunkVisible("Flap04Holed_D0", true);
                    break;
                }
                i++;
            } while (true);
        }
    }

    protected void moveGear(float f) {
        FW_190F.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    static {
        Class class1 = FW_190F8LATE.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3do/plane/Fw-190G-8/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-8.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190FG89.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb11", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb05", "_ExternalDev19", "_ExternalDev20" });
    }
}
