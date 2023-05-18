package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class JU_88C6NJ extends JU_88C6NEW implements TypeFighter, TypeBNZFighter, TypeScout, TypeX4Carrier, TypeRadarLiSN2Carrier {

    public JU_88C6NJ() {
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.curPilot = 1;
        this.radarGain = 50;
        this.radarMode = 0;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            JU_88C6NJ.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            JU_88C6NJ.bChangedPit = true;
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
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
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;
        }
    }

    public void typeX4CAdjSidePlus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = 1.0F;
            return;
        }
        if (this.curPilot == 2) {
            this.radarMode++;
            if (this.radarMode > 1) {
                this.radarMode = 0;
            }
            return;
        } else {
            return;
        }
    }

    public void typeX4CAdjSideMinus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = -1F;
            return;
        }
        if (this.curPilot == 2) {
            this.radarMode--;
            if (this.radarMode < 0) {
                this.radarMode = 1;
            }
            return;
        } else {
            return;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        if (this.curPilot == 1) {
            this.deltaTangage = 1.0F;
            return;
        }
        if (this.curPilot == 2) {
            this.radarGain += 10;
            if (this.radarGain > 100) {
                this.radarGain = 100;
            }
            return;
        } else {
            return;
        }
    }

    public void typeX4CAdjAttitudeMinus() {
        if (this.curPilot == 1) {
            this.deltaTangage = -1F;
            return;
        }
        if (this.curPilot == 2) {
            this.radarGain -= 10;
            if (this.radarGain < 0) {
                this.radarGain = 0;
            }
            return;
        } else {
            return;
        }
    }

    public void typeX4CResetControls() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = this.deltaTangage = 0.0F;
            return;
        }
        if (this.curPilot == 2) {
            this.radarGain = 50;
            return;
        } else {
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

    private float         deltaAzimuth;
    private float         deltaTangage;
    private int           curPilot;
    private int           radarGain;
    private int           radarMode;
    public static boolean bChangedPit = false;

    static {
        Class class1 = JU_88C6NJ.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88C-6Night/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88C-6mod.fmd:JU88C6");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88C6NJ.class, CockpitJU_88C6NJ_Radar.class, CockpitJU_88C6NJ_RGunner.class });
        Property.set(class1, "LOSElevation", 1.0976F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 0, 0, 0, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_MG1701", "_MG1702", "_MG1703", "_MGFF01", "_MGFF02", "_MGFF03" });
    }
}
