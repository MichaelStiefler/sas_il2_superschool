package com.maddox.il2.objects.air;

import java.util.ArrayList;

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
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.FuelTankGun_PTB490;
import com.maddox.il2.objects.weapons.FuelTankGun_PTB800L;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.PylonGP9;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class MIG_21_93 extends MIG_21 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeFuelDump, TypeThreatDetector {

    public MIG_21_93() {
        this.dosel = true;
        this.dosel2 = true;
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
        this.backfireList = new ArrayList();
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public float getFlowRate() {
        return MIG_21_93.FlowRate;
    }

    public float getFuelReserve() {
        return MIG_21_93.FuelReserve;
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
        this.checkAmmo();
        this.bHasSK1Seat = false;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        this.guidedMissileUtils.onAircraftLoaded();
        if ((this.getBulletEmitterByHookName("_ExternalDev01") instanceof FuelTankGun_PTB490) || (this.getBulletEmitterByHookName("_ExternalDev01") instanceof FuelTankGun_PTB800L)) {
            this.pylonOccupied_DT = true;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx / 2.0F;
        }
        if (this.getBulletEmitterByHookName("_ExternalDev17") instanceof PylonGP9) {
            this.pylonOccupied_Py = true;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx / 2.0F;
        }
        if (this.pylonOccupied_DT || this.pylonOccupied_Py) {
            this.pylonOccupied = true;
        }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 7) {
            this.k14Mode = 0;
        }
        if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
            switch (this.k14Mode) {
                case 0:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed");
                    break;

                case 1:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + Gsh-23 AA");
                    break;

                case 2:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + GSh-23 AG");
                    break;

                case 3:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + S-5 AG");
                    break;

                case 4:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + S-24 AG");
                    break;

                case 5:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + Bomb AG");
                    break;

                case 6:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Missile");
                    break;

                case 7:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Off");
                    break;
            }
        }
        return true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers() && this.FM.Gears.onGround() && this.dosel && !this.FM.AS.bIsAboutToBailout) {
            this.FM.CT.cockpitDoorControl = 0.0F;
            this.FM.CT.getCockpitDoor();
            this.dosel = false;
            this.dosel2 = false;
        } else if (!this.FM.isPlayers() && this.FM.Gears.onGround() && !this.FM.AS.bIsAboutToBailout) {
            if (this.FM.EI.engines[0].getRPM() < 100F) {
                this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
                this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if ((this.FM.CT.cockpitDoorControl != 0.0F) && !this.FM.AS.bIsAboutToBailout) {
            if (!this.FM.Gears.onGround()) {
                this.FM.CT.cockpitDoorControl = 0.0F;
                this.FM.CT.forceCockpitDoor(0.0F);
                World.cur();
                if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                    HUD.log("CockpitDoorCLS");
                }
            }
            if ((f1 > 56F) && this.FM.Gears.onGround()) {
                this.FM.CT.cockpitDoorControl = 0.0F;
                if ((this.FM.CT.cockpitDoorControl < 0.1F) && (this.FM.CT.getCockpitDoor() < 0.1F)) {
                    this.FM.CT.cockpitDoorControl = 0.0F;
                    this.FM.CT.forceCockpitDoor(0.0F);
                }
                World.cur();
                if ((((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) && this.dosel2) {
                    HUD.log("CockpitDoorCLS");
                }
            }
            this.dosel2 = true;
        }
        if (this.pylonOccupied && (this.airBrake_State < 0.0015D) && this.pylonOccupied_DT && !this.getBulletEmitterByHookName("_ExternalDev01").haveBullets()) {
            this.pylonOccupied = false;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx * 2.0F;
        }
        this.computeR25_300_AB();
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
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteMiG21bis/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(0.5F);
            this.chute.pos.setRel(new Point3d(-7D, 0.0D, 0.6D), new Orient(0.0F, 90F, 0.0F));
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

    private void checkAmmo() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j].haveBullets() && (this.FM.CT.Weapons[i][j] instanceof RocketGunFlare)) {
                        this.backfireList.add(this.FM.CT.Weapons[i][j]);
                    }
                }

            }
        }

    }

    public void backFire() {
        if (this.backfireList.isEmpty()) {
            return;
        } else {
            ((RocketGunFlare) this.backfireList.remove(0)).shots(3);
            return;
        }
    }

    public void computeR25_300_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 25900D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 19.55D) {
                f1 = 25F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                f1 = ((-0.000579744F * f3) + (0.109187F * f2)) - (0.63425F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -140F);
        float f2 = Math.max(-f * 1500F, -100F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -f2);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, f2);
        hiermesh.chunkSetAngles("GearC_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearTelescopeL", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearTelescopeR", 0.0F, 115F * f, 0.0F);
    }

    protected void moveGear(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.34F);
        this.hierMesh().chunkSetLocate("GearL33_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.34F);
        this.hierMesh().chunkSetLocate("GearR33_D0", Aircraft.xyz, Aircraft.ypr);
        MIG_21_93.moveGear(this.hierMesh(), f);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("AirbrakeL", -35F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("AirbrakeR", 35F * f, 0.0F, 0.0F);
        if (!this.pylonOccupied) {
            this.hierMesh().chunkSetAngles("AirbrakeRear", 0.0F, 40F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AirbrakeTelescope", 0.0F, -40F * f, 0.0F);
        }
        this.airBrake_State = f;
    }

    public void moveCockpitDoor(float f) {
        if (this.FM.Gears.onGround() && (this.FM.getSpeed() < 56F)) {
            this.resetYPRmodifier();
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if (Config.isUSE_RENDER()) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                }
                this.setDoorSnd(f);
            }
        }
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(8, loc, vector3d, aircraft);
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

    private float              airBrake_State;
    private boolean            pylonOccupied;
    private boolean            pylonOccupied_DT;
    private boolean            pylonOccupied_Py;
    public float               Timer1;
    public float               Timer2;
    private int                freq;
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
    public static float        FlowRate    = 10F;
    public static float        FuelReserve = 450F;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    protected boolean          bHasBoosters;
    protected long             boosterFireOutTime;
    protected boolean          bHasSK1Seat;
    private boolean            dosel;
    private boolean            dosel2;
    private ArrayList          backfireList;

    static {
        Class class1 = MIG_21_93.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21-93/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21bis.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_21_93.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 2, 2, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 9, 9, 2, 2, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 7, 7, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Flare01", "_Flare02", "_ExternalDev02", "_ExternalDev01", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalRock09", "_ExternalRock10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock11", "_ExternalRock22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev13", "_ExternalDev14", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Rock29", "_Rock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_Bomb09", "_Bomb10", "_Bomb11", "_Bomb12", "_Rock35", "_Rock36", "_Rock37", "_Rock38", "_Rock39", "_Rock40", "_Rock41", "_Rock42", "_CANNON01", "_CANNON02",
                "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalBomb13", "_ExternalBomb14", "_Rock43", "_Rock44", "_Rock45", "_Rock46", "_Rock47", "_Rock48", "_Rock49", "_Rock50" });
    }
}
