package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Su_15 extends Su_X implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector {

    public Su_15() {
        this.guidedMissileUtils = null;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public void setCommonThreatActive() {
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.bHasSK1Seat = false;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 1) {
            this.k14Mode = 0;
        }
        if (this.k14Mode == 0) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD:_Fixed");
            }
        } else if (this.k14Mode == 1) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD:_Fixed+Gyro");
            } else if ((this.k14Mode == 2) && (((Interpolate) (this.FM)).actor == World.getPlayerAircraft())) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD:_Gyro");
            }
        }
        return true;
    }

    public void update(float f) {
        super.update(f);
        if (Config.isUSE_RENDER()) {
            if (!this.FM.AS.bIsAboutToBailout) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null) && Main3D.cur3D().cockpits[0].cockpitDimControl) {
                    this.hierMesh().chunkVisible("Head1_D0", false);
                    this.hierMesh().chunkVisible("Glass_Head1_D0", true);
                } else {
                    this.hierMesh().chunkVisible("Head1_D0", true);
                    this.hierMesh().chunkVisible("Glass_Head1_D0", false);
                }
            } else {
                this.hierMesh().chunkVisible("Glass_Head1_D0", false);
            }
        }
        this.setAfterburner();
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteMiG21/mono.sim");
            this.chute.collide(true);
            this.chute.pos.setRel(new Point3d(-5.9D, 0.0D, 0.6D), new Orient(0.0F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                    this.chute.pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                }
                this.chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute.destroy();
        }
        this.guidedMissileUtils.update();
    }

    private void setAfterburner() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 19500D;
        }
        if ((this.FM.getAltitude() > 1000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 2000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 3000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 4000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 5000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 6000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 7000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 8000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 9000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 500D;
        }
        if ((this.FM.getAltitude() > 10000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 2200D;
        }
        if ((this.FM.getAltitude() > 12000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1100D;
        }
        if ((this.FM.getAltitude() > 13000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1200D;
        }
        if ((this.FM.getAltitude() > 14000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1300D;
        }
        if ((this.FM.getAltitude() > 14500F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1350D;
        }
        if ((this.FM.getAltitude() > 15000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1400D;
        }
        if ((this.FM.getAltitude() > 15500F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1450D;
        }
        if ((this.FM.getAltitude() > 17000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1700D;
        }
        if ((this.FM.getAltitude() > 17800F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1950D;
        }
        if ((this.FM.getAltitude() > 18200F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 2800D;
        }
        if ((this.FM.getAltitude() > 20000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.92F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 9000D;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -140F);
        float f2 = Math.max(-f * 1500F, -100F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -f2);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, f2);
        hiermesh.chunkSetAngles("GearTelescopeL", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearTelescopeR", 0.0F, 115F * f, 0.0F);
    }

    protected void moveGear(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        this.hierMesh().chunkSetLocate("GearL33_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        this.hierMesh().chunkSetLocate("GearR33_D0", Aircraft.xyz, Aircraft.ypr);
        Su_15.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.4F, 0.0F, 0.3F);
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 50F * f, 0.0F);
        this.resetYPRmodifier();
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, -0.5F);
        Aircraft.xyz[0] = f;
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, -0.5F);
        Aircraft.xyz[0] = f;
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.99D) {
            this.hierMesh().chunkSetAngles("GearC_D0", 0.0F * f, 0.0F, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("GearC_D0", 30F * f, 0.0F, 0.0F);
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 60D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    private GuidedMissileUtils guidedMissileUtils;
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    public float               Timer1;
    public float               Timer2;
    private int                freq;
    protected boolean          bHasSK1Seat;

    static {
        Class class1 = Su_15.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-15");
        Property.set(class1, "meshName", "3DO/Plane/Su-15/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1954.9F);
        Property.set(class1, "yearExpired", 1988.3F);
        Property.set(class1, "FlightModel", "FlightModels/Su-9B.fmd:Sukhoi_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSu_15.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 9, 9, 2, 2, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 7, 7, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Flare01", "_Flare02", "_ExternalDev02", "_ExternalDev01", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalRock09", "_ExternalRock10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock11", "_ExternalRock22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev13", "_ExternalDev14", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10",
                "_ExternalBomb11", "_ExternalBomb12", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_CANNON01", "_CANNON02", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46" });
    }
}
