package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class KI_15_II extends Scheme1 implements TypeScout, TypeBomber, TypeStormovik {

    public KI_15_II() {
        this.pilot2kill = false;
        this.BlisTurOpen = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            KI_15_II.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            KI_15_II.bChangedPit = true;
        }
    }

    protected void moveFlap(float f) {
        float f1 = -30F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
        KI_15_II.moveGear(this.hierMesh(), f);
    }

    public void rareAction(float f, boolean flag) {
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if (!this.pilot2kill && (aircraft != null) && !this.FM.AS.bIsAboutToBailout) {
            this.hierMesh().chunkVisible("Turret1B_D0", true);
            this.hierMesh().chunkVisible("Turret1C_D0", true);
            this.hierMesh().chunkVisible("TurretCG_D0", true);
            this.hierMesh().chunkVisible("Gundown_D0", false);
            this.hierMesh().chunkVisible("Down1C_D0", false);
            this.hierMesh().chunkVisible("DownCG_D0", false);
            this.BlisTurOpen = true;
        }
        if (!this.pilot2kill && (aircraft == null) && !this.FM.AS.bIsAboutToBailout && this.BlisTurOpen) {
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret1C_D0", false);
            this.hierMesh().chunkVisible("TurretCG_D0", false);
            this.hierMesh().chunkVisible("Gundown_D0", true);
            this.hierMesh().chunkVisible("Down1C_D0", true);
            this.hierMesh().chunkVisible("DownCG_D0", true);
            this.BlisTurOpen = false;
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("HMask1_D0", false);
        this.hierMesh().chunkVisible("Pilot1_D1", false);
        this.hierMesh().chunkVisible("Pilot2_D0", false);
        this.hierMesh().chunkVisible("HMask2_D0", false);
        this.hierMesh().chunkVisible("Turret1C_D0", false);
        this.hierMesh().chunkVisible("TurretCG_D0", false);
        this.hierMesh().chunkVisible("Gundown_D0", false);
        this.hierMesh().chunkVisible("Down1C_D0", false);
        this.hierMesh().chunkVisible("DownCG_D0", false);
        this.hierMesh().chunkVisible("Turret1B_D0", true);
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
                this.FM.turret[0].bIsOperable = false;
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("Turret1B_D0", true);
                this.hierMesh().chunkVisible("Turret1C_D0", true);
                this.hierMesh().chunkVisible("TurretCG_D0", true);
                this.hierMesh().chunkVisible("Gundown_D0", false);
                this.hierMesh().chunkVisible("Down1C_D0", false);
                this.hierMesh().chunkVisible("DownCG_D0", false);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else {
                i = s.charAt(5) - 49;
            }
            this.hitFlesh(i, shot, byte0);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -19F) {
                    f = -19F;
                    flag = false;
                }
                if (f > 19F) {
                    f = 19F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 35F) {
                    f1 = 35F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public static boolean bChangedPit = false;
    private boolean       pilot2kill;
    private boolean       BlisTurOpen;

    static {
        Class class1 = KI_15_II.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-15");
        Property.set(class1, "meshName", "3DO/Plane/Ki-15-II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ki15II.fmd:Ki15_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKi15I.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev06", "_ExternalDev07" });
    }
}
