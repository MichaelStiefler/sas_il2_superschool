package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class U_2VEX2_TF1 extends BEO_WW1_2 implements TypeStormovik {

    public U_2VEX2_TF1() {
    }

    public void doMurderPilot(int i) {
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("Pilot1_D1", true);
        if (!this.FM.AS.bIsAboutToBailout) {
            this.hierMesh().chunkVisible("Gore1_D0", true);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][0] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) || ((this.FM.CT.Weapons[2] != null) && (this.FM.CT.Weapons[2][0] != null) && this.FM.CT.Weapons[2][this.FM.CT.Weapons[2].length - 1].haveBullets())) {
            this.hierMesh().chunkVisible("LP_Pylons1", true);
            this.hierMesh().chunkVisible("LP_Pylons2", true);
            this.hierMesh().chunkVisible("LP_Pylons3", true);
            this.hierMesh().chunkVisible("LP_Pylons4", true);
            this.hierMesh().chunkVisible("LP_Pylons5", true);
            this.hierMesh().chunkVisible("LP_Pylons6", true);
            this.hierMesh().chunkVisible("LP_Pylons7", true);
            this.hierMesh().chunkVisible("LP_Pylons8", true);
        } else {
            this.hierMesh().chunkVisible("LP_Pylons1", false);
            this.hierMesh().chunkVisible("LP_Pylons2", false);
            this.hierMesh().chunkVisible("LP_Pylons3", false);
            this.hierMesh().chunkVisible("LP_Pylons4", false);
            this.hierMesh().chunkVisible("LP_Pylons5", false);
            this.hierMesh().chunkVisible("LP_Pylons6", false);
            this.hierMesh().chunkVisible("LP_Pylons7", false);
            this.hierMesh().chunkVisible("LP_Pylons8", false);
        }
    }

    static {
        Class class1 = U_2VEX2_TF1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Scout2_TF1");
        Property.set(class1, "meshName", "3do/plane/U-2VEX2_TF1(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2VSLBs21b2.fmd:U2scout2_1B2_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitU2VSEX2_NEW_C.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 2, 2, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb34", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb33" });
    }
}
