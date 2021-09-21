package com.maddox.il2.objects.air;

public abstract class SE_2PA123 extends P_35family {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.endsWith("4G")) {
            this.hierMesh().chunkVisible("barrelL_D0", true);
            this.hierMesh().chunkVisible("barrelR_D0", true);
            return;
        }
        if (this.thisWeaponsName.endsWith("2P")) {
            this.hierMesh().chunkVisible("RackL3_D0", true);
            this.hierMesh().chunkVisible("RackR3_D0", true);
            return;
        }
        if (this.thisWeaponsName.endsWith("4P")) {
            this.hierMesh().chunkVisible("RackL2_D0", true);
            this.hierMesh().chunkVisible("RackR2_D0", true);
            this.hierMesh().chunkVisible("RackL3_D0", true);
            this.hierMesh().chunkVisible("RackR3_D0", true);
            return;
        }
        if (this.thisWeaponsName.endsWith("6P")) {
            this.hierMesh().chunkVisible("RackL_D0", true);
            this.hierMesh().chunkVisible("RackR_D0", true);
            this.hierMesh().chunkVisible("RackL2_D0", true);
            this.hierMesh().chunkVisible("RackR2_D0", true);
            this.hierMesh().chunkVisible("RackL3_D0", true);
            this.hierMesh().chunkVisible("RackR3_D0", true);
            return;
        } else {
            return;
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask2a_D0", false);
        }
    }

}
