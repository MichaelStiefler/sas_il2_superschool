package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.FuelTankGun_Tank19;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Mig_25R extends Mig_19 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump {

    public Mig_25R() {
        this.fxSirena = this.newSound("aircraft.Sirena2", false);
        this.smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        this.guidedMissileUtils = null;
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
    }

    public float getFlowRate() {
        return Mig_25R.FlowRate;
    }

    public float getFuelReserve() {
        return Mig_25R.FuelReserve;
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
            int l1 = (int) (Math.ceil((i1 * 3.28084D) / 100D) * 100D);
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

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(Mig_25R.NEG_G_TOLERANCE_FACTOR, Mig_25R.NEG_G_TIME_FACTOR, Mig_25R.NEG_G_RECOVERY_FACTOR, Mig_25R.POS_G_TOLERANCE_FACTOR, Mig_25R.POS_G_TIME_FACTOR, Mig_25R.POS_G_RECOVERY_FACTOR);
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.droptank();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        if (this.thisWeaponsName.endsWith("P1")) {
            this.hierMesh().chunkVisible("PylonTL", true);
            this.hierMesh().chunkVisible("PylonTR", true);
        }
        if (this.thisWeaponsName.endsWith("P2")) {
            this.hierMesh().chunkVisible("PylonTL", true);
            this.hierMesh().chunkVisible("PylonTR", true);
            this.hierMesh().chunkVisible("PylonML", true);
            this.hierMesh().chunkVisible("PylonMR", true);
        }
        if (this.thisWeaponsName.endsWith("P3")) {
            this.hierMesh().chunkVisible("PylonML", true);
            this.hierMesh().chunkVisible("PylonMR", true);
        }
    }

    private final void doRemovedroptankL() {
        if (this.hierMesh().chunkFindCheck("DroptankL") != -1) {
            this.hierMesh().hideSubTrees("DroptankL");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("DroptankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemovedroptankR() {
        if (this.hierMesh().chunkFindCheck("DroptankR") != -1) {
            this.hierMesh().hideSubTrees("DroptankR");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("DroptankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private void droptank() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j].haveBullets() && (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_Tank19)) {
                        this.havedroptank = true;
                        this.hierMesh().chunkVisible("DroptankL", true);
                        this.hierMesh().chunkVisible("DroptankR", true);
                    }
                }

            }
        }

    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        this.sirenaWarning();
        super.update(f);
        if (this.havedroptank && !this.FM.CT.Weapons[9][1].haveBullets()) {
            this.havedroptank = false;
            this.doRemovedroptankL();
            this.doRemovedroptankR();
        }
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteF86/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(0.5F);
            this.chute.pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
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
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 17000D;
        }
        if ((this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x += 17000D;
        }
        if ((this.FM.getAltitude() > 10000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 3500D;
        }
        if ((this.FM.getAltitude() > 10000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 3500D;
        }
        if ((this.FM.getAltitude() > 10000F) && (this.calculateMach() >= 1.81D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 10000F) && (this.calculateMach() >= 1.81D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 11000F) && (this.FM.EI.engines[0].getThrustOutput() < 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
        if ((this.FM.getAltitude() > 11000F) && (this.FM.EI.engines[1].getThrustOutput() < 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
        if ((this.FM.getAltitude() > 12000F) && (this.calculateMach() >= 1.67D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 12000F) && (this.calculateMach() >= 1.67D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 12500F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 3000D;
        }
        if ((this.FM.getAltitude() > 12500F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 3000D;
        }
        if ((this.FM.getAltitude() > 12500F) && (this.FM.EI.engines[0].getThrustOutput() < 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
        if ((this.FM.getAltitude() > 12500F) && (this.FM.EI.engines[1].getThrustOutput() < 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
        if ((this.FM.getAltitude() > 12500F) && (this.calculateMach() >= 1.64D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 12500F) && (this.calculateMach() >= 1.64D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 13000F) && (this.calculateMach() >= 1.6D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 13000F) && (this.calculateMach() >= 1.6D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 13500F) && (this.calculateMach() >= 1.55D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 13500F) && (this.calculateMach() >= 1.55D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 14000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 3500D;
        }
        if ((this.FM.getAltitude() > 14000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 3500D;
        }
        if ((this.FM.getAltitude() > 14000F) && (this.calculateMach() >= 1.47D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 14000F) && (this.calculateMach() >= 1.47D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 14500F) && (this.calculateMach() >= 1.43D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 14500F) && (this.calculateMach() >= 1.43D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 15000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 3500D;
        }
        if ((this.FM.getAltitude() > 15000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 3500D;
        }
        if ((this.FM.getAltitude() > 15000F) && (this.calculateMach() >= 1.33D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 15000F) && (this.calculateMach() >= 1.33D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 15500F) && (this.calculateMach() >= 1.23D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 15500F) && (this.calculateMach() >= 1.23D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 15800F) && (this.calculateMach() >= 1.19D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 15800F) && (this.calculateMach() >= 1.19D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 16000F) && (this.calculateMach() >= 1.14D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 16000F) && (this.calculateMach() >= 1.14D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 16300F) && (this.calculateMach() >= 1.1D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 16300F) && (this.calculateMach() >= 1.1D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 8000D;
        }
        if ((this.FM.getAltitude() > 16000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 5200D;
        }
        if ((this.FM.getAltitude() > 16000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 5200D;
        }
        if ((this.FM.getAltitude() > 17000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 5500D;
        }
        if ((this.FM.getAltitude() > 17000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 5500D;
        }
        if ((this.FM.getAltitude() > 18000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 3900D;
        }
        if ((this.FM.getAltitude() > 18000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 3900D;
        }
        if ((this.FM.getAltitude() > 18800F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 5500D;
        }
        if ((this.FM.getAltitude() > 18800F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 5500D;
        }
        if ((this.FM.getAltitude() > 20000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 7000D;
        }
        if ((this.FM.getAltitude() > 20000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 7000D;
        }
        if ((this.FM.getAltitude() > 20000F) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 17000D;
        }
        if ((this.FM.getAltitude() > 20000F) && (this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 17000D;
        }
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
    public static float        FlowRate               = 10F;
    public static float        FuelReserve            = 1500F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;
    public boolean             bToFire;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    private boolean            havedroptank;

    static {
        Class class1 = Mig_25R.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mig-25R");
        Property.set(class1, "meshName", "3DO/Plane/Mig-25R/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/Mig-25.fmd:MIG25");
        Property.set(class1, "cockpitClass", new Class[] { com.maddox.il2.objects.air.CockpitMig_19.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 9, 9, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 9, 9, 9, 9, 3, 3, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", "_ExternalDev04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev05", "_ExternalDev06", "_ExternalRock21", "_ExternalRock22", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31",
                "_ExternalRock32", "_ExternalRock33", "_ExternalRock34" });
    }
}
