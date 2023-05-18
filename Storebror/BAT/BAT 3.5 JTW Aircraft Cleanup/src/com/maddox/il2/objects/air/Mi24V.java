package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Mi24V extends Scheme2a implements TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector {

    public Mi24V() {
        this.rocketHookSelected = 2;
        this.suka = new Loc();
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.rotorrpm = 0;
        this.obsLookTime = 0;
        this.obsLookAzimuth = 0.0F;
        this.obsLookElevation = 0.0F;
        this.obsAzimuth = 0.0F;
        this.obsElevation = 0.0F;
        this.obsAzimuthOld = 0.0F;
        this.obsElevationOld = 0.0F;
        this.obsMove = 0.0F;
        this.obsMoveTot = 0.0F;
        this.bObserverKilled = false;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
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
        this.fxSirena = this.newSound("aircraft.Sirena2", false);
        this.smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        this.smplSirena.setInfinite(true);
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
        long l1 = Time.current();
        if ((l1 - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l1;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l1 = Time.current();
        if ((l1 - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l1;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l1 = Time.current();
        if ((l1 - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l1;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    private boolean sirenaWarning() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if ((aircraft1 instanceof Aircraft) && (aircraft1.getArmy() != World.getPlayerArmy()) && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && (aircraft1 != World.getPlayerAircraft()) && (aircraft1.getSpeed(vector3d) > 20D)) {
            this.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().z;
            new String();
            new String();
            double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float) Math.atan2(d8, -d7);
            int k = (int) (Math.floor((int) f) - 90D);
            if (k < 0) {
                k += 360;
            }
            int i1 = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int j1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
            float f1 = 57.32484F * (float) Math.atan2(j1, d11);
            int k1 = (int) (Math.floor((int) f1) - 90D);
            if (k1 < 0) {
                k1 += 360;
            }
            int l1 = k1 - j;
            int i2 = (int) (Math.ceil((j1 * 3.2808399000000001D) / 100D) * 100D);
            if (i2 >= 5280) {
                i2 = (int) Math.floor(i2 / 5280);
            }
            this.bRadarWarning = (j1 <= 3000D) && (j1 >= 50D) && (l1 >= 195) && (l1 <= 345) && (Math.sqrt(i1 * i1) >= 120D);
            this.playSirenaWarning(this.bRadarWarning);
        } else {
            this.bRadarWarning = false;
            this.playSirenaWarning(this.bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag) {
        if (flag && !this.sirenaSoundPlaying) {
            this.fxSirena.play(this.smplSirena);
            this.sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else if (!flag && this.sirenaSoundPlaying) {
            this.fxSirena.cancel();
            this.sirenaSoundPlaying = false;
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.AS.bNavLightsOn) {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            this.pos.getAbs(point3d, orient);
            Mi24V.l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, this.findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }
        if (!this.bObserverKilled) {
            if (this.obsLookTime == 0) {
                this.obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                this.obsMoveTot = 1.0F + (World.Rnd().nextFloat() * 1.5F);
                this.obsMove = 0.0F;
                this.obsAzimuthOld = this.obsAzimuth;
                this.obsElevationOld = this.obsElevation;
                if (World.Rnd().nextFloat() > 0.8D) {
                    this.obsAzimuth = 0.0F;
                    this.obsElevation = 0.0F;
                } else {
                    this.obsAzimuth = (World.Rnd().nextFloat() * 140F) - 70F;
                    this.obsElevation = (World.Rnd().nextFloat() * 50F) - 20F;
                }
            } else {
                this.obsLookTime--;
            }
        }
    }

    protected void moveElevator(float f) {
        this.updateControlsVisuals();
    }

    private final void updateControlsVisuals() {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -7F * this.FM.CT.getElevator());
    }

    protected void moveAileron(float f) {
        this.updateAileron();
    }

    private final void updateAileron() {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -7F * this.FM.CT.getAileron(), 0.0F);
    }

    protected void moveFlap(float f) {
    }

    protected void moveRudder(float f) {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -90F);
        float f2 = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -85F);
        float f3 = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 85F);
        float f4 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, 70F, 0.0F) : Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, 70F);
        float f5 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, -70F, 0.0F) : Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -70F);
        Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
        float f7 = Aircraft.cvt(f, 0.1F, 0.8F, 0.0F, 90F);
        float f8 = Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90F);
        float f9 = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -140F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, f9);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f5, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", f8, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", f7, 0.0F, 0.0F);
    }

    protected void moveGear(float f) {
        Mi24V.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
        this.hierMesh().chunkSetAngles("Door1_D0", -90F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Door2_D0", 0.0F, -65F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -120F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 120F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 120F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -120F * f, 0.0F);
    }

    protected void moveFan(float f) {
        this.rotorrpm = Math.abs((int) ((this.FM.EI.engines[0].getw() * 0.025F) + (this.FM.Vwld.length() / 30D)));
        if (this.rotorrpm >= 1) {
            this.rotorrpm = 1;
        }
        if ((this.FM.EI.engines[0].getw() > 100F) && (this.FM.EI.engines[1].getw() > 100F)) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if ((this.FM.EI.engines[0].getw() < 100F) && (this.FM.EI.engines[1].getw() < 100F)) {
            this.hierMesh().chunkVisible("Prop1_D0", true);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop1_D1")) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if ((this.FM.EI.engines[0].getw() > 100F) && (this.FM.EI.engines[1].getw() > 100F)) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if ((this.FM.EI.engines[0].getw() < 100F) && (this.FM.EI.engines[1].getw() < 100F)) {
            this.hierMesh().chunkVisible("Prop2_D0", true);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop2_D1")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Tail1_CAP")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
            this.hierMesh().chunkVisible("Prop2_D1", false);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 100F) % 360F : (float) (this.dynamoOrient - (this.rotorrpm * 25D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop1_D0", -this.dynamoOrient, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, this.dynamoOrient);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        boolean flag1 = this instanceof Mi24V;
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.startsWith("xxarmorp")) {
                    int i = s.charAt(8) - 48;
                    switch (i) {
                        default:
                            break;

                        case 1:
                            this.getEnergyPastArmor(7.070000171661377D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            break;

                        case 2:
                        case 3:
                            this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                                this.doRicochet(shot);
                            }
                            break;

                        case 4:
                            if (point3d.x > -1.35D) {
                                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
                                shot.powerType = 0;
                                if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                                    this.doRicochet(shot);
                                }
                            } else {
                                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                            }
                            break;

                        case 5:
                        case 6:
                            this.getEnergyPastArmor(20.2D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                            if (shot.power > 0.0F) {
                                break;
                            }
                            if (Math.abs(Aircraft.v1.x) > 0.86599999666213989D) {
                                this.doRicochet(shot);
                            } else {
                                this.doRicochetBack(shot);
                            }
                            break;

                        case 7:
                            this.getEnergyPastArmor(20.2D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                            if (shot.power <= 0.0F) {
                                this.doRicochetBack(shot);
                            }
                            break;
                    }
                }
                if (s.startsWith("xxarmorc1")) {
                    this.getEnergyPastArmor(7.070000171661377D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                }
                if (s.startsWith("xxarmort1")) {
                    this.getEnergyPastArmor(6.059999942779541D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.1D)) && (this.getEnergyPastArmor(3.4D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxsparlm")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingLMid") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingLMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparrm")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingRMid") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingRMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparlo")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingLOut") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingLOut") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparro")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingROut") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingROut") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(6.5D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(6.5D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if (s.startsWith("xxeng")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    if ((this.getEnergyPastArmor(3.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.8F)) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                    }
                } else if (s.endsWith("gear")) {
                    if ((this.getEnergyPastArmor(4.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.01F, shot) > 0.0F) {
                        this.debuggunnery("Engine Module: Supercharger Disabled..");
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else if (s.endsWith("feed")) {
                    if ((this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.endsWith("fue1")) {
                    if (this.getEnergyPastArmor(0.89F, shot) > 0.0F) {
                        this.debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.endsWith("cyl1")) {
                    if ((this.getEnergyPastArmor(1.3F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        this.debuggunnery("Engine Module: Cylinders Assembly Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Operating..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                    if ((Math.abs(point3d.y) < 0.1379999965429306D) && (this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.startsWith("xxeng1mag")) {
                    int j = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + j + " Hit, Magneto " + j + " Disabled..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                } else if (s.startsWith("xxeng1oil") && (this.getEnergyPastArmor(0.5F, shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if (s.startsWith("xxw1")) {
                if (this.FM.AS.astateEngineStates[0] == 0) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else if (this.FM.AS.astateEngineStates[0] == 1) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.12F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    if (this.FM.AS.astateTankStates[k] == 0) {
                        this.debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 1);
                    } else if (this.FM.AS.astateTankStates[k] == 1) {
                        this.debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                        this.FM.AS.hitTank(shot.initiator, 2, 2);
                        this.debuggunnery("Fuel System: Fuel Tank " + k + " Pierced, State Shifted..");
                    }
                }
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if (s.startsWith("xxcannon")) {
                if (s.endsWith("01") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Left Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("02") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Right Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if (s.startsWith("xxammo")) {
                if (s.startsWith("xxammol1") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.startsWith("xxammor1") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                if (s.startsWith("xxammol2") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.startsWith("xxammor2") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if (s.startsWith("xxbomb") && (World.Rnd().nextFloat() < 0.00345F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                this.debuggunnery("Armament System: Bomb Payload Detonated..");
                this.FM.AS.hitTank(shot.initiator, 0, 10);
                this.FM.AS.hitTank(shot.initiator, 1, 10);
                this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if (s.startsWith("xxpnm") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)) {
                this.debuggunnery("Pneumo System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if (s.startsWith("xxhyd") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.startsWith("xxins")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            return;
        }
        if (s.startsWith("xcockpit") || s.startsWith("xblister")) {
            if (point3d.z > 0.473D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            } else if (point3d.y > 0.0D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            } else {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            }
        }
        if (s.startsWith("xcf")) {
            if (point3d.x < -1.94D) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else {
                if (point3d.x <= 1.342D) {
                    if ((point3d.z < -0.591D) || ((point3d.z > 0.40799999237060547D) && (point3d.x > 0.0D))) {
                        this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
                        if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                            this.doRicochet(shot);
                        }
                    } else {
                        this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                        if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                            this.doRicochet(shot);
                        }
                    }
                }
                if (this.chunkDamageVisible("CF") < 3) {
                    this.hitChunk("CF", shot);
                }
            }
        } else if (s.startsWith("xoil")) {
            if (point3d.z < -0.981D) {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochet(shot);
                }
            } else if ((point3d.x > 0.537D) || (point3d.x < -0.1D)) {
                this.getEnergyPastArmor(0.2D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochetBack(shot);
                }
            } else {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochet(shot);
                }
            }
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xeng")) {
            if (point3d.z > 0.159D) {
                this.getEnergyPastArmor((1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 1.335D) && (point3d.x < 2.386D) && (point3d.z > -0.06D) && (point3d.z < 0.064000000000000001D)) {
                this.getEnergyPastArmor(0.5D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 2.53D) && (point3d.x < 2.992D) && (point3d.z > -0.235D) && (point3d.z < 0.011D)) {
                this.getEnergyPastArmor(4.0399999618530273D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 2.559D) && (point3d.z < -0.595D)) {
                this.getEnergyPastArmor(4.0399999618530273D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 1.849D) && (point3d.x < 2.251D) && (point3d.z < -0.71D)) {
                this.getEnergyPastArmor(4.0399999618530273D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else if (point3d.x > 3.0030000000000001D) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            } else if (point3d.z < -0.60600000619888306D) {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
            }
            if ((Math.abs(Aircraft.v1.x) > 0.86599999666213989D) && ((shot.power <= 0.0F) || (World.Rnd().nextFloat() < 0.1F))) {
                this.doRicochet(shot);
            }
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner")) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if ((s.endsWith("2a") || s.endsWith("2b")) && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xturret")) {
            if (this.getEnergyPastArmor(0.25F, shot) > 0.0F) {
                this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else if (s.startsWith("xhelm")) {
            this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if (shot.power <= 0.0F) {
                this.doRicochetBack(shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i1;
            if (s.endsWith("a")) {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else {
                i1 = s.charAt(5) - 49;
            }
            this.hitFlesh(i1, shot, byte0);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                // fall through

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.bObserverKilled = true;
                // fall through

            default:
                return;
        }
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        if ((this.obsMove < this.obsMoveTot) && !this.bObserverKilled && !this.FM.AS.isPilotParatrooper(1)) {
            if ((this.obsMove < 0.2F) || (this.obsMove > (this.obsMoveTot - 0.2F))) {
                this.obsMove += 0.3D * f;
            } else if ((this.obsMove < 0.1F) || (this.obsMove > (this.obsMoveTot - 0.1F))) {
                this.obsMove += 0.15F;
            } else {
                this.obsMove += 1.2D * f;
            }
            this.obsLookAzimuth = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsAzimuthOld, this.obsAzimuth);
            this.obsLookElevation = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsElevationOld, this.obsElevation);
            this.hierMesh().chunkSetAngles("Head2_D0", 0.0F, this.obsLookAzimuth, this.obsLookElevation);
        }
        super.update(f);
        this.computeVerticalThrust();
        this.computeOrizzontalThrust();
        this.computeOrizzontalThrust2();
        this.computeHovering();
        this.computeEngine();
        if ((this == World.getPlayerAircraft()) && (this.FM.turret.length > 0) && (this.FM.AS.astatePilotStates[1] < 90) && this.FM.turret[0].bIsAIControlled && ((this.FM.getOverload() > 3F) || (this.FM.getOverload() < -0.7F))) {
            Voice.speakRearGunShake();
        }
        this.sirenaWarning();
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -60F) {
            af[0] = -60F;
            flag = false;
        } else if (af[0] > 60F) {
            af[0] = 60F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (af[1] < -80F) {
            af[1] = -80F;
            flag = false;
        }
        if (af[1] > 20F) {
            af[1] = 20F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 1.2F) && (f1 < 13.3F)) {
            return false;
        } else {
            return (f1 >= -3.1F) || (f1 <= -4.6F);
        }
    }

    public void computeVerticalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 210F, 1.0F, 0.2F);
        float f3 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 5000F, 1.0F, 0.5F);
        float f4 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 15F)) {
            f4 = 1.5F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 15F)) {
            f4 = 1.12F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && !flag) {
            f4 = 1.4F * f;
        }
        this.FM.producedAF.z += f4 * (10F * this.FM.M.mass) * (1.0F * f2) * (1.0F * f3);
    }

    public void computeOrizzontalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 100F, 1.0F, 0.0F);
        float f3 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && !flag) {
            f3 = 0.35F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (f1 > 5F) && flag) {
            f3 = 0.15F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void computeOrizzontalThrust2() {
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.0F) && (this.FM.getSpeedKMH() < 0.0F)) {
            f1 = 0.35F * f;
        }
        this.FM.producedAF.x += f1 * (10F * this.FM.M.mass);
    }

    public void computeHovering() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 100F, 1.0F, 0.0F);
        float f3 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && !flag && (f1 < 35F) && (this.FM.getSpeedKMH() > 12F) && this.FM.CT.StabilizerControl) {
            f3 = 0.4F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void computeEngine() {
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5)) {
            if (this.FM.EI.getPowerOutput() <= 0.0F) {

            }
        }
        if (f > 5D) {
            f1 = 18F;
        } else {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = ((0.139683F * f3) - (0.71746F * f2)) + (3.69524F * f);
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public BulletEmitter       Weapons[][];
    public int                 rocketHookSelected;
    private static Loc         l           = new Loc();
    private int                obsLookTime;
    private float              obsLookAzimuth;
    private float              obsLookElevation;
    private float              obsAzimuth;
    private float              obsElevation;
    private float              obsAzimuthOld;
    private float              obsElevationOld;
    private float              obsMove;
    private float              obsMoveTot;
    boolean                    bObserverKilled;
    public static boolean      bChangedPit = false;
    public Loc                 suka;
    private float              dynamoOrient;
    private boolean            bDynamoRotary;
    private int                rotorrpm;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean            bRadarWarning;
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
    private SoundFX            fxSirena;
    private Sample             smplSirena;
    private boolean            sirenaSoundPlaying;
    static {
        Class class1 = Mi24V.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-24V");
        Property.set(class1, "meshName", "3DO/Plane/Mi-24V/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HIND");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMi24.class, CockpitMi24_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 7, 7, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Flare01", "_Flare02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev11", "_ExternalDev12" });
    }
}
