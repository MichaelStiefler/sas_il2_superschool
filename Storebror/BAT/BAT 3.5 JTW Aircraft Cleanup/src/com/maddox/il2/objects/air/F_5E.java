package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU118_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU7_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERmd_gn16;
import com.maddox.il2.objects.weapons.Pylon_USTER_gn16;
import com.maddox.il2.objects.weapons.SkinnableOrdnance;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class F_5E extends F_5 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane {

    public F_5E() {
        this.guidedMissileUtils = null;
        this.fxSirena = this.newSound("aircraft.F4warning", false);
        this.smplSirena = new Sample("sample.F4warning.wav", 256, 65535);
        this.sirenaSoundPlaying = false;
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
        this.removeChuteTimer = -1L;
        this.smplSirena.setInfinite(true);
        this.engineSFX = null;
        this.engineSTimer = 0x98967f;
        this.counter = 0;
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
        this.bOpenAuxIntakeL = false;
        this.bOpenAuxIntakeR = false;
    }

    public float getFlowRate() {
        return F_5E.FlowRate;
    }

    public float getFuelReserve() {
        return F_5E.FuelReserve;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -85F), 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(false);
        }
        this.moveWingFold(this.hierMesh(), f);
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

    public static final double Rnd(double d, double d1) {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    private boolean sirenaWarning() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if (World.getPlayerAircraft() == null) {
            return false;
        }
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
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
            float f1 = 57.32484F * (float) Math.atan2(i1, d11);
            int j1 = (int) (Math.floor((int) f1) - 90D);
            if (j1 < 0) {
                j1 += 360;
            }
            int k1 = j1 - j;
            int l1 = (int) (Math.ceil((i1 * 3.2808399000000001D) / 100D) * 100D);
            if (l1 >= 5280) {
                l1 = (int) Math.floor(l1 / 5280);
            }
            this.bRadarWarning = (i1 <= 3000D) && (i1 >= 50D) && (k1 >= 195) && (k1 <= 345) && (Math.sqrt(l * l) >= 120D);
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
            HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
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
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        if (this.FM.CT.Weapons[0] != null) {
        }
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        this.sirenaWarning();
        this.computeJ85GE21B_AB();
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
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
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteF86/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(0.8F);
            this.chute.pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
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
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 14.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatL_Out", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
            this.hierMesh().chunkSetAngles("SlatR_Out", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
        }
    }

    public void rareAction(float f, boolean flag) {
        if ((this.raretimer != Time.current()) && (this == World.getPlayerAircraft())) {
            this.counter++;
            if ((this.counter++ % 5) == 0) {
                this.AN_APQ157();
            }
        }
        super.rareAction(f, flag);
        this.raretimer = Time.current();
        this.checkAuxIntake();
    }

    private boolean AN_APQ157() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof F_5E) {
            flag1 = true;
        }
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D)) {
                this.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                Engine.land();
                String s = "level with us";
                if ((d2 - d6 - 300D) >= 0.0D) {
                    s = "below us";
                }
                if (((d2 - d6) + 300D) <= 0.0D) {
                    s = "above us";
                }
                if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                    s = "slightly below";
                }
                if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) {
                    s = "slightly above";
                }
                new String();
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                int k = (int) (Math.floor((int) f) - 90D);
                if (k < 0) {
                    k += 360;
                }
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((random.nextInt(20) - 10F) / 100F) + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 18000F;
                float f3 = f2;
                if (d3 < 1200D) {
                    f3 = (float) (d3 * 0.8D * 3D);
                }
                int j1 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f1) / 10D) * 10D);
                if (j1 > f2) {
                    j1 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                }
                float f4 = 57.32484F * (float) Math.atan2(j1, d7);
                int k1 = (int) (Math.floor((int) f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int) f2;
                if (j1 < f2) {
                    if (j1 > 1150) {
                        i2 = (int) (Math.ceil(j1 / 900D) * 900D);
                    } else {
                        i2 = (int) (Math.ceil(j1 / 500D) * 500D);
                    }
                }
                int j2 = l + i1;
                int k2 = j2;
                if (k2 < 0) {
                    k2 += 360;
                }
                float f5 = (float) (f3 + (Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * (f3 * 0.25D)));
                int l2 = (int) (f5 * Math.cos(Math.toRadians(l1)));
                if (k2 < 5) {
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                }
                if ((k2 > 25) && (k2 <= 35)) {
                }
                if ((k2 > 35) && (k2 <= 45)) {
                }
                if ((k2 > 45) && (k2 <= 60)) {
                }
                if (k2 > 355) {
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                }
                if ((k2 < 335) && (k2 >= 325)) {
                }
                if ((k2 < 325) && (k2 >= 315)) {
                }
                if ((k2 < 345) && (k2 >= 300)) {
                }
                if (l1 < -10) {
                }
                if ((l1 >= -10) && (l1 <= -5)) {
                }
                if ((l1 > -5) && (l1 < 5)) {
                }
                if ((l1 <= 10) && (l1 >= 5)) {
                }
                if (l1 > 10) {
                }
                String s3 = "  ";
                if (k2 < 5) {
                    s3 = "dead ahead, ";
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                    s3 = "right by 5\260, ";
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                    s3 = "right by 10\260, ";
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                    s3 = "right by 15\260, ";
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                    s3 = "right by 20\260, ";
                }
                if ((k2 > 25) && (k2 <= 35)) {
                    s3 = "right by 30\260, ";
                }
                if ((k2 > 35) && (k2 <= 45)) {
                    s3 = "right by 40\260, ";
                }
                if ((k2 > 45) && (k2 <= 60)) {
                    s3 = "off our right, ";
                }
                if (k2 > 355) {
                    s3 = "dead ahead, ";
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                    s3 = "left by 5\260, ";
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                    s3 = "left by 10\260, ";
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                    s3 = "left by 15\260, ";
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                    s3 = "left by 20\260, ";
                }
                if ((k2 < 335) && (k2 >= 325)) {
                    s3 = "left by 30\260, ";
                }
                if ((k2 < 325) && (k2 >= 315)) {
                    s3 = "left by 40\260, ";
                }
                if ((k2 < 345) && (k2 >= 300)) {
                    s3 = "off our left, ";
                }
                if (((double) j1 <= (double) l2) && (j1 > 300D) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 60D)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APQ-157: Contact " + s3 + s + ", " + i2 + "m");
                    this.freq = 1;
                } else {
                    this.freq = 7;
                }
                this.setTimer(this.freq);
            }
        }

        return true;
    }

    public void setTimer(int i) {
        Random random = new Random();
        this.Timer1 = (float) (random.nextInt(i) * 0.1D);
        this.Timer2 = (float) (random.nextInt(i) * 0.1D);
    }

    public void resetTimer(float f) {
        this.Timer1 = f;
        this.Timer2 = f;
    }

    private void checkAuxIntake() {
        if (this.FM.EI.engines[0].getStage() > 5) {
            if (this.FM.getSpeedKMH() > 470F) {
                this.bOpenAuxIntakeL = false;
            } else if (this.FM.getSpeedKMH() < 435F) {
                this.bOpenAuxIntakeL = true;
            }
        } else {
            this.bOpenAuxIntakeL = false;
        }
        if (this.FM.EI.engines[1].getStage() > 5) {
            if (this.FM.getSpeedKMH() > 470F) {
                this.bOpenAuxIntakeR = false;
            } else if (this.FM.getSpeedKMH() < 435F) {
                this.bOpenAuxIntakeR = true;
            }
        } else {
            this.bOpenAuxIntakeR = false;
        }
        this.hierMesh().chunkVisible("IntakeL", this.bOpenAuxIntakeL);
        this.hierMesh().chunkVisible("IntakeR", this.bOpenAuxIntakeR);
    }

    public void computeJ85GE21B_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 5120D;
        }
        if ((this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x += 5120D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() == 6)) {
            if (f > 16F) {
                f1 = 5F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((((7F * f4) / 7488F) - ((77F * f3) / 3744F)) + ((329F * f2) / 2496F)) - ((49F * f) / 208F);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    private void checkChangeWeaponColors() {
        com.maddox.il2.engine.Mat mat = this.hierMesh().material(this.hierMesh().materialFind("Gloss1D0o"));
        com.maddox.il2.engine.Mat mat1 = this.hierMesh().material(this.hierMesh().materialFind("Gloss1D0p"));
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16) {
                        ((Pylon_USTER_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_USMERmd_gn16) {
                        ((Pylon_USMERmd_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16) {
                        ((Pylon_LAU10_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16) {
                        ((Pylon_LAU10_Cap_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16) {
                        ((Pylon_LAU7_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16) {
                        ((Pylon_LAU118_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof SkinnableOrdnance) {
                        ((SkinnableOrdnance) this.FM.CT.Weapons[i][j]).matPlane(mat, mat1);
                    }
                }

            }
        }

    }

    public void missionStarting() {
        super.missionStarting();
        this.checkChangeWeaponColors();
    }

    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX            fxSirena;
    private Sample             smplSirena;
    private boolean            sirenaSoundPlaying;
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
    public static float        FlowRate    = 10F;
    public static float        FuelReserve = 1500F;
    public boolean             bToFire;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    private float              arrestor;
    protected SoundFX          engineSFX;
    protected int              engineSTimer;
    public float               Timer1;
    public float               Timer2;
    private int                freq;
    private int                counter;
    public boolean             bOpenAuxIntakeL;
    public boolean             bOpenAuxIntakeR;
    private long               raretimer;

    static {
        Class class1 = F_5E.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-5E");
        Property.set(class1, "meshName", "3DO/Plane/F-5E/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/F-5E.fmd:F5");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_5E.class, CockpitF_5Bombardier.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 2, 2, 2, 2, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 1, 1, 9, 9, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalTank01", "_ExternalTank02", "_ExternalTank03", "_Dev01", "_Dev02", "_RocketLeft", "_RocketRight", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb06", "_ExternalBomb07", "_Bomb08", "_Bomb09", "_Bomb10", "_Bomb11", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_Dev05", "_ExternalBomb12", "_Bomb13", "_Bomb14", "_ExternalBomb15", "_ExternalBomb16", "_Bomb17", "_Bomb18", "_Bomb19", "_Bomb20", "_Dev06", "_Dev07", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev08", "_ExternalDev09", "_ExternalBomb21", "_ExternalBomb22", "_Bomb23", "_Bomb24", "_Bomb25", "_Bomb26", "_Bomb27", "_Bomb28", "_Bomb29", "_Bomb30", "_Bomb31", "_Bomb32", "_Dev10", "_Dev11", "_CANNON03" });
    }
}
