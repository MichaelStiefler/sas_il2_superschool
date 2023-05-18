package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class SuperTucano extends TucanoV2 implements TypeFighter, TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

    public SuperTucano() {
        this.guidedMissileUtils = null;
        this.trgtPk = 0.0F;
        this.trgtAI = null;
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
        this.rocketsList = new ArrayList();
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
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

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(SuperTucano.NEG_G_TOLERANCE_FACTOR, SuperTucano.NEG_G_TIME_FACTOR, SuperTucano.NEG_G_RECOVERY_FACTOR, SuperTucano.POS_G_TOLERANCE_FACTOR, SuperTucano.POS_G_TIME_FACTOR, SuperTucano.POS_G_RECOVERY_FACTOR);
//    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
//    private static final float NEG_G_TIME_FACTOR = 1.5F;
//    private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
//    private static final float POS_G_TOLERANCE_FACTOR = 2.0F;
//    private static final float POS_G_TIME_FACTOR = 2.0F;
//    private static final float POS_G_RECOVERY_FACTOR = 2.0F;
    }

    public Actor getMissileTarget() {
        return this.guidedMissileUtils.getMissileTarget();
    }

    public Point3f getMissileTargetOffset() {
        return new Point3f(this.guidedMissileUtils.getSelectedActorOffset());
    }

    public boolean hasMissiles() {
        return !this.rocketsList.isEmpty();
    }

    public void shotMissile() {
        if (this.hasMissiles()) {
            this.rocketsList.remove(0);
        }
    }

    public int getMissileLockState() {
        return this.guidedMissileUtils.getMissileLockState();
    }

    private float getMissilePk() {
        float f = 0.0F;
        this.guidedMissileUtils.setMissileTarget(this.guidedMissileUtils.lookForGuidedMissileTarget(((Interpolate) (this.FM)).actor, this.guidedMissileUtils.getMaxPOVfrom(), this.guidedMissileUtils.getMaxPOVto(), this.guidedMissileUtils.getPkMaxDist()));
        if (Actor.isValid(this.guidedMissileUtils.getMissileTarget())) {
            f = this.guidedMissileUtils.Pk(((Interpolate) (this.FM)).actor, this.guidedMissileUtils.getMissileTarget());
        }
        return f;
    }

    private void checkAIlaunchMissile() {
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !(this.FM instanceof Pilot)) {
            return;
        }
        if (this.rocketsList.isEmpty()) {
            return;
        }
        Pilot pilot = (Pilot) this.FM;
        if (((pilot.get_maneuver() == 27) || (pilot.get_maneuver() == 62) || (pilot.get_maneuver() == 63)) && (((Maneuver) (pilot)).target != null)) {
            this.trgtAI = ((Interpolate) (((Maneuver) (pilot)).target)).actor;
            if (!Actor.isValid(this.trgtAI) || !(this.trgtAI instanceof Aircraft)) {
                return;
            }
            this.bToFire = false;
            if ((this.trgtPk > 25F) && Actor.isValid(this.guidedMissileUtils.getMissileTarget()) && (this.guidedMissileUtils.getMissileTarget() instanceof Aircraft) && (this.guidedMissileUtils.getMissileTarget().getArmy() != ((Interpolate) (this.FM)).actor.getArmy()) && (Time.current() > (this.tX4Prev + 10000L))) {
                this.bToFire = true;
                this.tX4Prev = Time.current();
                this.shootRocket();
                this.bToFire = false;
            }
        }
    }

    public void shootRocket() {
        if (this.rocketsList.isEmpty()) {
            return;
        } else {
            ((RocketGun) this.rocketsList.get(0)).shots(1);
            return;
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.rocketsList.clear();
        this.guidedMissileUtils.createMissileList(this.rocketsList);
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        super.update(f);
        this.trgtPk = this.getMissilePk();
        this.guidedMissileUtils.checkLockStatus();
        this.checkAIlaunchMissile();
    }

    private GuidedMissileUtils guidedMissileUtils;
    private float              trgtPk;
    private Actor              trgtAI;
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;
    private ArrayList          rocketsList;
    public boolean             bToFire;
    private long               tX4Prev;

    static {
        Class class1 = SuperTucano.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SuperTucano");
        Property.set(class1, "meshName", "3DO/Plane/AT29SuperTucano(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/SuperTucano.fmd:SuperTucano_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSuperTucanoF.class, CockpitSuperTucanoR.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalDev07", "_ExternalDev08", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18" });
    }
}
