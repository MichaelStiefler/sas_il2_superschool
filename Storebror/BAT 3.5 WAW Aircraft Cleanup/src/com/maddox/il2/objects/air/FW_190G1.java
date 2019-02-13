package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.weapons.FuelTank_Type_D;
import com.maddox.rts.Property;

public class FW_190G1 extends FW_190F implements TypeStormovik {

    public FW_190G1() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).M.massEmpty -= 81F;
        this.hierMesh().chunkVisible("7mmC_D0", false);
        this.hierMesh().chunkVisible("7mmCowl_D0", true);
        this.hierMesh().chunkVisible("Flap01_D0", true);
        this.hierMesh().chunkVisible("Flap04_D0", true);
        this.hierMesh().chunkVisible("Flap01Holed_D0", false);
        this.hierMesh().chunkVisible("Flap04Holed_D0", false);
        Object aobj[] = super.pos.getBaseAttached();
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
        if (((FlightModelMain) (super.FM)).CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    static {
        Class class1 = FW_190G1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-4(Beta)/hier_FG.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-4.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190FG1.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb07", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev11", "_ExternalBomb08", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb11", "_ExternalDev12" });
    }
}
