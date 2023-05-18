package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class OV_10 extends B_25 implements TypeStormovik, TypeScout {

    public OV_10() {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;
        }
    }

    protected void moveFlap(float f) {
        float f1 = 55F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, f1);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            OV_10.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            OV_10.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;

    static {
        Class class1 = OV_10.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-26");
        Property.set(class1, "meshName", "3DO/Plane/OV10/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03B25());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/OV10.fmd:OV_10");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_63C.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        int ai[] = new int[73];
        ai[1] = 3;
        ai[2] = 3;
        ai[3] = 3;
        ai[4] = 3;
        ai[5] = 3;
        ai[6] = 3;
        ai[7] = 3;
        ai[8] = 3;
        ai[9] = 2;
        ai[10] = 2;
        ai[11] = 2;
        ai[12] = 2;
        ai[13] = 2;
        ai[14] = 2;
        ai[15] = 2;
        ai[16] = 2;
        ai[17] = 2;
        ai[18] = 2;
        ai[19] = 2;
        ai[20] = 2;
        ai[21] = 2;
        ai[22] = 2;
        ai[23] = 2;
        ai[24] = 2;
        ai[25] = 2;
        ai[26] = 2;
        ai[27] = 2;
        ai[28] = 2;
        ai[29] = 2;
        ai[30] = 2;
        ai[31] = 2;
        ai[32] = 2;
        ai[33] = 2;
        ai[34] = 2;
        ai[35] = 2;
        ai[36] = 2;
        ai[37] = 2;
        ai[38] = 2;
        ai[39] = 2;
        ai[40] = 2;
        ai[41] = 2;
        ai[42] = 2;
        ai[43] = 2;
        ai[44] = 2;
        ai[45] = 2;
        ai[46] = 2;
        ai[47] = 2;
        ai[48] = 2;
        ai[49] = 2;
        ai[50] = 2;
        ai[51] = 2;
        ai[52] = 2;
        ai[53] = 2;
        ai[54] = 2;
        ai[55] = 2;
        ai[56] = 2;
        ai[57] = 2;
        ai[58] = 2;
        ai[59] = 2;
        ai[60] = 2;
        ai[61] = 2;
        ai[62] = 2;
        ai[63] = 2;
        ai[64] = 2;
        ai[65] = 9;
        ai[66] = 9;
        ai[67] = 9;
        ai[68] = 9;
        Aircraft.weaponTriggersRegister(class1, ai);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42",
                "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05" });
    }
}
