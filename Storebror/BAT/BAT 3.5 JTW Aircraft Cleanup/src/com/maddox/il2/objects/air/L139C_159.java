package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class L139C_159 extends L39_C implements TypeFighter, TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

    public L139C_159() {
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
        gfactors.setGFactors(L139C_159.NEG_G_TOLERANCE_FACTOR, L139C_159.NEG_G_TIME_FACTOR, L139C_159.NEG_G_RECOVERY_FACTOR, L139C_159.POS_G_TOLERANCE_FACTOR, L139C_159.POS_G_TIME_FACTOR, L139C_159.POS_G_RECOVERY_FACTOR);
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
        if (Config.isUSE_RENDER()) {
            this.hierMesh().chunkVisible("Pilot2_D0", true);
        }
        this.hierMesh().chunkVisible("Head2_D0", true);
        this.turbo = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
        this.turbosmoke = Eff3DActor.New(this, this.findHook("_Engine1ES_01"), null, 1.0F, "3DO/Effects/Aircraft/GraySmallTSPD.eff", -1F);
        this.afterburner = Eff3DActor.New(this, this.findHook("_Engine1EF_02"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
        Eff3DActor.setIntesity(this.turbo, 0.0F);
        Eff3DActor.setIntesity(this.turbosmoke, 0.0F);
        Eff3DActor.setIntesity(this.afterburner, 0.0F);
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        super.update(f);
        this.trgtPk = this.getMissilePk();
        this.guidedMissileUtils.checkLockStatus();
        this.checkAIlaunchMissile();
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if ((this.FM.EI.engines[0].getPowerOutput() > 0.45F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.65F) {
                    if (this.FM.EI.engines[0].getPowerOutput() > 1.001F) {
                        this.FM.AS.setSootState(this, 0, 3);
                    } else {
                        this.FM.AS.setSootState(this, 0, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, 0, 1);
                }
            } else if ((this.FM.EI.engines[0].getPowerOutput() <= 0.45F) || (this.FM.EI.engines[0].getStage() < 6)) {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
    }

    public void doSetSootState(int i, int j) {
        switch (j) {
            case 0:
                Eff3DActor.setIntesity(this.turbo, 0.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 0.0F);
                Eff3DActor.setIntesity(this.afterburner, 0.0F);
                break;

            case 1:
                Eff3DActor.setIntesity(this.turbo, 0.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 1.0F);
                Eff3DActor.setIntesity(this.afterburner, 0.0F);
                break;

            case 2:
                Eff3DActor.setIntesity(this.turbo, 1.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 1.0F);
                Eff3DActor.setIntesity(this.afterburner, 0.0F);
                break;

            case 3:
                Eff3DActor.setIntesity(this.turbo, 1.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 1.0F);
                Eff3DActor.setIntesity(this.afterburner, 1.0F);
                break;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 100F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
    private float              trgtPk;
    private Actor              trgtAI;
    private Eff3DActor         turbo;
    private Eff3DActor         turbosmoke;
    private Eff3DActor         afterburner;
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
        Class class1 = L139C_159.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "L139C_159");
        Property.set(class1, "meshName", "3DO/Plane/L139C_159(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/L-39C.fmd:L39C");
        Property.set(class1, "cockpitClass", new Class[] { CockpitL39Main.class, CockpitL39Main_2.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 0, 0, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 9, 2, 2, 2, 2, 9, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalRock01", "_ExternalRock02", "_MGUN01", "_MGUN02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22" });
    }
}
