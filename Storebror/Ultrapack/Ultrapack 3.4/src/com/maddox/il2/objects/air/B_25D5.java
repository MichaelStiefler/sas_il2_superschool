package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class B_25D5 extends B_25_Strafer implements TypeBomber, TypeStormovik, TypeStormovikArmored {

    public B_25D5() {
        this.bpos = 1.0F;
        this.bcurpos = 1.0F;
        this.btme = -1L;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
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

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                break;

            case 5:
                this.hierMesh().chunkVisible("Pilot6_D0", false);
                this.hierMesh().chunkVisible("HMask6_D0", false);
                this.hierMesh().chunkVisible("Pilot6_D1", true);
                break;
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster()) {
            if (this.bpos == 0.0F) {
                if (this.bcurpos > this.bpos) {
                    this.bcurpos -= 0.2F * f;
                    if (this.bcurpos < 0.0F) this.bcurpos = 0.0F;
                }
                this.resetYPRmodifier();
                Aircraft.xyz[1] = -0.31F + 0.31F * this.bcurpos;
                this.hierMesh().chunkSetLocate("Turret3A_D0", Aircraft.xyz, Aircraft.ypr);
            } else if (this.bpos == 1.0F) {
                if (this.bcurpos < this.bpos) {
                    this.bcurpos += 0.2F * f;
                    if (this.bcurpos > 1.0F) {
                        this.bcurpos = 1.0F;
                        this.bpos = 0.5F;
                        this.FM.turret[2].bIsOperable = true;
                    }
                }
                this.resetYPRmodifier();
                Aircraft.xyz[1] = -0.3F + 0.3F * this.bcurpos;
                this.hierMesh().chunkSetLocate("Turret3A_D0", Aircraft.xyz, Aircraft.ypr);
            }
            if (Time.current() > this.btme) {
                this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if (this.FM.turret[2].target == null) {
                    this.FM.turret[2].bIsOperable = false;
                    this.bpos = 0.0F;
                }
                if (this.FM.turret[1].target != null && this.FM.AS.astatePilotStates[4] < 90) this.bpos = 1.0F;
            }
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask4_D0", false);
            this.hierMesh().chunkVisible("HMask5_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            this.hierMesh().chunkVisible("HMask4_D0", this.hierMesh().isChunkVisible("Pilot4_D0"));
            this.hierMesh().chunkVisible("HMask5_D0", this.hierMesh().isChunkVisible("Pilot5_D0"));
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                return false;

            case 1:
                if (f1 < 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                if (f1 > 88F) {
                    f1 = 88F;
                    flag = false;
                }
                break;

            case 2:
                if (f1 < -88F) {
                    f1 = -88F;
                    flag = false;
                }
                if (f1 > 2.0F) {
                    f1 = 2.0F;
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

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 3:
                this.FM.turret[1].setHealth(f);
                break;

            case 4:
                this.FM.turret[2].setHealth(f);
                break;
        }
    }

    private float         bpos;
    private float         bcurpos;
    private long          btme;
    public static boolean bChangedPit = false;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;

    static {
        Class class1 = B_25D5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-25");
        Property.set(class1, "meshName", "3DO/Plane/B-25D-5(Multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03B25());
        Property.set(class1, "meshName_us", "3DO/Plane/B-25D-5(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar03B25());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/B-25D5-Strafer.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB25D5.class, CockpitB25D5_TGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 11, 11, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        weaponHooksRegister(class1,
                new String[] { "_MGUN17", "_MGUN18", "_MGUN19", "_MGUN20", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02",
                        "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_BombSpawn02", "_BombSpawn03", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn01",
                        "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02",
                        "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
