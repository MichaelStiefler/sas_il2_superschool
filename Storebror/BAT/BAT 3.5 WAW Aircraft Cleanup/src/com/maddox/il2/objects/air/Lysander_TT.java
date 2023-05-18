package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Lysander_TT extends LysanderX implements TypeBomber, TypeScout, TypeTransport {

    public Lysander_TT() {
        this.arrestor = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if ((World.cur().camouflage == 2) || (World.cur().camouflage == 6)) {
            this.hierMesh().chunkVisible("SandFilter_D0", true);
        }
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatLInner_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatLOuter_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatRInner_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatROuter_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
        }
        super.update(f);
    }

    protected void moveAileron(float f) {
        if (this.FM.CT.getWing() < 0.01F) {
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        }
    }

    protected void moveFlap1(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -60F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -60F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
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

    public static void moveGear(HierMesh hiermesh1, float f1) {
    }

    protected void moveGear(float f) {
        Lysander_TT.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (f > 66.5F) {
            f = 66.5F;
            this.FM.Gears.steerAngle = f;
        }
        if (f < -66.5F) {
            f = -66.5F;
            this.FM.Gears.steerAngle = f;
        }
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -60F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        this.FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + (shot.mass * 18.95F * 2.0F)));
        if (shot.chunkName.startsWith("Engine")) {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass) {
                this.FM.AS.hitEngine(shot.initiator, 0, 1);
            }
            if ((((Tuple3d) (Aircraft.v1)).z > 0.0D) && (World.Rnd().nextFloat() < 0.12F)) {
                this.FM.AS.setEngineDies(shot.initiator, 0);
                if (shot.mass > 0.1F) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 5);
                }
            }
            if ((((Tuple3d) (Aircraft.v1)).x < 0.10000000149011612D) && (World.Rnd().nextFloat() < 0.57F)) {
                this.FM.AS.hitOil(shot.initiator, 0);
            }
        }
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
            if ((((Tuple3d) (Aircraft.Pd)).z > 0.5D) && (shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                HUD.logCenter("H E A D S H O T");
            }
        } else if (shot.chunkName.startsWith("Pilot2")) {
            this.killPilot(shot.initiator, 1);
            if ((((Tuple3d) (Aircraft.Pd)).z > 0.5D) && (shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                HUD.logCenter("H E A D S H O T");
            }
        } else {
            if (shot.chunkName.startsWith("Turret")) {
                this.FM.turret[0].bIsOperable = false;
            }
            if ((this.FM.AS.astateEngineStates[0] == 4) && (World.Rnd().nextInt(0, 99) < 33)) {
                this.FM.setCapableOfBMP(false, shot.initiator);
            }
            super.msgShot(shot);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 34:
                return super.cutFM(35, j, actor);

            case 37:
                return super.cutFM(38, j, actor);

            case 35:
            case 36:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void doKillPilot(int i) {
        if (i == 1) {
            this.FM.turret[0].bIsOperable = false;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        if (f < -45F) {
            f = -45F;
            flag = false;
        }
        if (f > 45F) {
            f = 45F;
            flag = false;
        }
        if (f1 < -45F) {
            f1 = -45F;
            flag = false;
        }
        if (f1 > 20F) {
            f1 = 20F;
            flag = false;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float f) {
    }

    public float getArrestor() {
        return this.arrestor;
    }

    public void setArrestor(float arrestor) {
        this.arrestor = arrestor;
    }

    private float arrestor;

    static {
        Class class1 = Lysander_TT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Lizzie");
        Property.set(class1, "meshName", "3do/plane/Lysander_TT/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/Lysander3_SD.fmd:Lysander3_SD_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLysander_1.class, CockpitLysander_TT_Ob.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_MGUN03" });
    }
}
