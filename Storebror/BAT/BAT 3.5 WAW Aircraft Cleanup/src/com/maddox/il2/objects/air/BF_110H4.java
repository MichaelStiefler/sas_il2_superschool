package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class BF_110H4 extends BF_110 implements TypeX4Carrier, TypeRadarLiSN2Carrier {

    public BF_110H4() {
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.radarGain = 50;
        this.radarMode = 0;
    }

    public void onAircraftLoaded() {
        this.FM.crew = 2;
        this.FM.AS.astatePilotFunctions[1] = 2;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            BF_110H4.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            BF_110H4.bChangedPit = true;
        }
    }

    public void typeX4CAdjSidePlus() {
        this.radarMode++;
        if (this.radarMode > 1) {
            this.radarMode = 0;
        }
    }

    public void typeX4CAdjSideMinus() {
        this.radarMode--;
        if (this.radarMode < 0) {
            this.radarMode = 1;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        this.radarGain += 10;
        if (this.radarGain > 100) {
            this.radarGain = 100;
        }
    }

    public void typeX4CAdjAttitudeMinus() {
        this.radarGain -= 10;
        if (this.radarGain < 0) {
            this.radarGain = 0;
        }
    }

    public void typeX4CResetControls() {
        this.radarGain = 50;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void setCurPilot(int i) {
    }

    public int getCurPilot() {
        return 1;
    }

    public int getRadarGain() {
        return this.radarGain;
    }

    public int getRadarMode() {
        return this.radarMode;
    }

    private float         deltaAzimuth;
    private float         deltaTangage;
    private int           radarGain;
    private int           radarMode;
    public static boolean bChangedPit = false;

    static {
        Class class1 = BF_110H4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf-110");
        Property.set(class1, "meshName", "3DO/Plane/Bf-110H-4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-110H-4.fmd:Bf-110H");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_110H4.class, CockpitBF_110NJ_RadarOp.class });
        Property.set(class1, "LOSElevation", 0.66895F);
        int ai[] = new int[5];
        ai[0] = 1;
        ai[1] = 1;
        ai[2] = 1;
        Aircraft.weaponTriggersRegister(class1, ai);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON03", "_CANNON04" });
    }
}
