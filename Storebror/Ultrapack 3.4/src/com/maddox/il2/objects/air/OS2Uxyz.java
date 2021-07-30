package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Property;

public class OS2Uxyz extends Scheme1 {

    public OS2Uxyz() {
        this.bChangedExts = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) bChangedPit = true;
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
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Head3_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
    }

    public void moveSteering(float f) {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && World.cur().camouflage == 1) this.FM.CT.bHasBrakeControl = false;
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) super.moveFan(f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        super.hitBone(s, shot, point3d);
        if (s.startsWith("xgearc")) this.hitChunk("GearC2", shot);
        if (s.startsWith("xgearl")) this.hitChunk("GearL2", shot);
        if (s.startsWith("xgearr")) this.hitChunk("GearR2", shot);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        if (f > 0.0F) {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -24.5F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -24.5F * f, 0.0F);
        }
    }

    protected void moveAileron(float f) {
        if (f > 0.0F) {
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -16F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -28.5F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -16F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -28.5F * f, 0.0F);
        }
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -30F * f, 0.0F);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass) this.FM.AS.hitTank(shot.initiator, 0, (int) (1.0F + shot.mass * 18.95F * 2.0F));
        if (shot.chunkName.startsWith("WingRMid") && World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass) this.FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + shot.mass * 18.95F * 2.0F));
        if (shot.chunkName.startsWith("Engine")) {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass) this.FM.AS.hitEngine(shot.initiator, 0, 1);
            if (Aircraft.v1.z > 0.0D && World.Rnd().nextFloat() < 0.12F) {
                this.FM.AS.setEngineDies(shot.initiator, 0);
                if (shot.mass > 0.1F) this.FM.AS.hitEngine(shot.initiator, 0, 5);
            }
            if (Aircraft.v1.x < 0.1D && World.Rnd().nextFloat() < 0.57F) this.FM.AS.hitOil(shot.initiator, 0);
        }
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
            if (Aircraft.Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
        } else {
            if (this.FM.AS.astateEngineStates[0] == 4 && World.Rnd().nextInt(0, 99) < 33) this.FM.setCapableOfBMP(false, shot.initiator);
            super.msgShot(shot);
        }
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = f1 * -0.45F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        this.hierMesh().chunkSetLocate("Water_D0", Aircraft.xyz, Aircraft.ypr);
        super.update(f);
        if (this.bChangedExts) this.doFixBellyDoor();
        if (this.gunnerAiming) {
            if (this.gunnerAnimation < 1.0D) {
                this.gunnerAnimation += 0.025F;
                this.moveGunner();
            }
        } else if (this.gunnerAnimation > 0.0D) {
            this.gunnerAnimation -= 0.025F;
            this.moveGunner();
        }
    }

    public void doFixBellyDoor() {
        this.hierMesh().chunkVisible("CF1_D0", this.hierMesh().isChunkVisible("CF_D0"));
        this.hierMesh().chunkVisible("CF1_D1", this.hierMesh().isChunkVisible("CF_D1"));
        this.hierMesh().chunkVisible("CF1_D2", this.hierMesh().isChunkVisible("CF_D2"));
        this.hierMesh().chunkVisible("CF1_D3", this.hierMesh().isChunkVisible("CF_D3"));
        this.hierMesh().chunkVisible("Engine11_D0", this.hierMesh().isChunkVisible("Engine1_D0"));
        this.hierMesh().chunkVisible("Engine11_D1", this.hierMesh().isChunkVisible("Engine1_D1"));
        this.hierMesh().chunkVisible("Engine11_D2", this.hierMesh().isChunkVisible("Engine1_D2"));
        this.bChangedExts = false;
    }

    private void moveGunner() {
        if (this.gunnerDead || this.gunnerEjected) return;
        if (this.gunnerAnimation > 0.5D) {
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Head2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkSetAngles("Pilot2_D0", (this.gunnerAnimation - 0.5F) * 360F - 180F, 0.0F, 0.0F);
        } else if (this.gunnerAnimation > 0.25D) {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = (this.gunnerAnimation - 0.5F) * 0.5F;
            Aircraft.ypr[0] = 180F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            this.hierMesh().chunkSetLocate("Pilot2_D0", Aircraft.xyz, Aircraft.ypr);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Head2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Blister99_D0", false);
            this.hierMesh().chunkVisible("Blister101_D0", false);
        } else {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = this.gunnerAnimation * 0.5F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = this.gunnerAnimation * -110F;
            this.hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Head2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", true);
            this.hierMesh().chunkVisible("Blister99_D0", true);
            this.hierMesh().chunkVisible("Blister101_D0", true);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        Actor actor = War.GetNearestEnemy(this, 16, 7000F);
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        boolean flag1 = this.FM.CT.Weapons[10] != null && this.FM.CT.Weapons[10][0].haveBullets() || this.FM.CT.Weapons[10] != null && this.FM.CT.Weapons[10][1].haveBullets();
        if (!flag1) this.FM.turret[0].bIsOperable = false;
        if (flag1 && (actor != null && !(actor instanceof BridgeSegment) || aircraft != null)) {
            if (!this.gunnerAiming) this.gunnerAiming = true;
        } else if (this.gunnerAiming) this.gunnerAiming = false;
        if ((actor != null && !(actor instanceof BridgeSegment) || aircraft != null) && this.FM.CT.getCockpitDoor() < 0.01F) this.FM.AS.setCockpitDoor(this, 1);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 1.05F);
        this.hierMesh().chunkSetLocate("Blister3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -0.91F);
        this.hierMesh().chunkSetLocate("Blister77_D0", Aircraft.xyz, Aircraft.ypr);
//        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -1.82F);
//        hierMesh().chunkSetLocate("Blister22_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -1.82F);
        this.hierMesh().chunkSetLocate("Blister99_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public boolean        bChangedExts;
    public static boolean bChangedPit = false;
    float                 radPos;
    float                 suspR;
    float                 suspL;
    private boolean       gunnerAiming;
    private float         gunnerAnimation;
    private boolean       gunnerDead;
    private boolean       gunnerEjected;

    static {
        Class class1 = OS2Uxyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
