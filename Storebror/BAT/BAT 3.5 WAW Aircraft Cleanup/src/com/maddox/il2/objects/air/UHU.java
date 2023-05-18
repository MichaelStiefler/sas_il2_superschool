package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class UHU extends UHUxyz implements TypeX4Carrier, TypeRadarLiSN2Carrier {

    public UHU() {
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.curPilot = 1;
        this.radarGain = 50;
        this.radarMode = 0;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.endsWith("R4")) {
            this.hierMesh().chunkVisible("MG_FF1", true);
            this.hierMesh().chunkVisible("MG_FF2", true);
        }
        if (this.thisWeaponsName.startsWith("SN2b")) {
            this.hierMesh().chunkVisible("RadarSN2b", true);
            this.hierMesh().chunkVisible("RadarSN2c", false);
        }
        if (this.thisWeaponsName.startsWith("SN2d")) {
            this.hierMesh().chunkVisible("RadarSN2d", true);
            this.hierMesh().chunkVisible("RadarSN2c", false);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void typeX4CAdjSidePlus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = 1.0F;
            return;
        }
        this.radarMode++;
        if (this.radarMode > 1) {
            this.radarMode = 0;
        }
    }

    public void typeX4CAdjSideMinus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = -1F;
            return;
        }
        this.radarMode--;
        if (this.radarMode < 0) {
            this.radarMode = 1;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        if (this.curPilot == 1) {
            this.deltaTangage = 1.0F;
            return;
        }
        this.radarGain += 10;
        if (this.radarGain > 100) {
            this.radarGain = 100;
        }
    }

    public void typeX4CAdjAttitudeMinus() {
        if (this.curPilot == 1) {
            this.deltaTangage = -1F;
            return;
        }
        this.radarGain -= 10;
        if (this.radarGain < 0) {
            this.radarGain = 0;
        }
    }

    public void typeX4CResetControls() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = this.deltaTangage = 0.0F;
            return;
        } else {
            this.radarGain = 50;
            return;
        }
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void setCurPilot(int theCurPilot) {
        this.curPilot = theCurPilot;
    }

    public int getCurPilot() {
        return this.curPilot;
    }

    public int getRadarGain() {
        return this.radarGain;
    }

    public int getRadarMode() {
        return this.radarMode;
    }

    private float deltaAzimuth;
    private float deltaTangage;
    private int   curPilot;
    private int   radarGain;
    private int   radarMode;

    static {
        Class class1 = UHU.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-219");
        Property.set(class1, "meshName", "3DO/Plane/UHU/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-219.fmd:Uhu_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitUHU.class, CockpitHe_219_Radar.class });
        Property.set(class1, "LOSElevation", 1.00705F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 0, 0, 0, 0, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev01", "_ExternalDev02" });
    }
}
