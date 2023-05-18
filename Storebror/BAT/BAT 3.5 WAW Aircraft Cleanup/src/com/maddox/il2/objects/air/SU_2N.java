package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SU_2N extends Scheme1 implements TypeScout, TypeBomber, TypeStormovik {

    public SU_2N() {
        this.arrestor = 0.0F;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -37F * f, 0.0F);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -90F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2a_D0", 0.0F, -42F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -140F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2a_D0", 0.0F, -42F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -140F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void moveGear(float f) {
        SU_2N.moveGear(this.hierMesh(), f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstabl")) {
            this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) {
            this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", shot);
            }
            if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.1F)) {
                this.FM.AS.hitTank(shot.initiator, 0, 1);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", shot);
            }
            if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.1F)) {
                this.FM.AS.hitTank(shot.initiator, 1, 1);
            }
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", shot);
            }
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", shot);
            }
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xaronel")) {
            this.hitChunk("AroneL", shot);
        } else if (s.startsWith("xaroner")) {
            this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
            if ((this.getEnergyPastArmor(1.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.5F))) {
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                if (this.FM.AS.astateEngineStates[0] < 1) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                }
                if (World.Rnd().nextFloat() < (shot.power / 960000F)) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                }
                this.getEnergyPastArmor(25F, shot);
            }
        } else if (s.startsWith("xgearl")) {
            this.hitChunk("GearL2", shot);
        } else if (s.startsWith("xgearr")) {
            this.hitChunk("GearR2", shot);
        } else if (s.startsWith("xturret")) {
            if (s.startsWith("xturret1")) {
                this.FM.AS.setJamBullets(10, 0);
            }
            if (s.startsWith("xturret2")) {
                this.FM.AS.setJamBullets(11, 0);
            }
            if (s.startsWith("xturret3")) {
                this.FM.AS.setJamBullets(12, 0);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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
                if (f < -90F) {
                    f = -90F;
                    flag = false;
                }
                if (f > 90F) {
                    f = 90F;
                    flag = false;
                }
                if (f1 < -5F) {
                    f1 = -5F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
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
            case 1:
                this.FM.turret[0].setHealth(f);
                break;
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

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 11:
            case 19:
                this.hierMesh().chunkVisible("Wire_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.58F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.75F;
    }

    public void update(float f) {
        float f1 = Aircraft.cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -10F);
        for (int i = 1; i < 17; i++) {
            this.hierMesh().chunkSetAngles("cowlflap" + i + "_D0", 0.0F, f1, 0.0F);
        }

        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f2 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f2);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f3 = (-42F * this.FM.Gears.arrestorVSink) / 37F;
                if ((f3 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f3 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f3 = 0.0F;
                }
                if (f3 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f3));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f3));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
        super.update(f);
    }

    private float arrestor;

    static {
        Class class1 = SU_2N.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-2N");
        Property.set(class1, "meshName", "3DO/Plane/Su-2N/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Su-2N.fmd:SU_2N_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSU_2.class, CockpitSU_2_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02" });
    }
}
