// Source File Name: MissileGun.java
// Author:           Storebror
package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeGuidedMissileCarrier;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class MissileGun extends RocketGun {
    public static int hudMissileGunId        = 0;
    private boolean   engineWarmupRunning    = false;
    private long      engineWarmupTime       = 0L;
    private long      shotFrequency          = 500L;
//	private long lastMissileLaunched = 0L;
    private int       shotsAfterEngineWarmup = 0;
    private String    theMissileName         = null;
    private boolean   isValidForNetMirrors   = false;

    public float bulletMassa() {
        return this.bulletMassa / 10F;
    }

    public void checkPendingWeaponRelease() {
        // com.maddox.il2.game.HUD.log("checkPendingWeaponRelease");
        // System.out.println("checkPendingWeaponRelease engineWarmupRunning=" + this.engineWarmupRunning);
        if (!this.engineWarmupRunning) return;
        Missile theMissile = (Missile) this.rocket;
        if (theMissile == null) {
            this.engineWarmupRunning = false;
            return;
        }
        if (Time.current() > theMissile.getStartTime() + this.engineWarmupTime) this.shots(this.shotsAfterEngineWarmup);
        else theMissile.runupEngine();
    }

    public Missile getMissile() {
        return (Missile) this.rocket;
    }

    public boolean isShots() {
//		System.out.println("MissileGun isShots=" + this.bExecuted);
        return this.bExecuted;
    }

    public boolean isEngineWarmupRunning() {
        return this.engineWarmupRunning;
    }

    public void shots(int numMissilesToShoot) {
        this.shots(numMissilesToShoot, this.isValidForNetMirrors);
    }

    public void shots(int numMissilesToShoot, boolean isValidForNetMirrors) {
//	    System.out.println("MissileGun 1 shots(" + numMissilesToShoot + ") class=" + this.getClass().getName() + ", hash=" + this.hashCode() + ", actor.isNetMirror=" + this.actor.isNetMirror() + ", isValidForNetMirrors=" + isValidForNetMirrors + ", actor=" + this.actor.getClass().getName());
//		Exception e = new Exception("shots");
//		e.printStackTrace();
        this.isValidForNetMirrors = isValidForNetMirrors;
        if (numMissilesToShoot == 0) {
            super.shots(numMissilesToShoot); // TODO: This is necessary to re-enable Missiles in case of unlimited ammo
            this.engineWarmupRunning = false;
            this.shotsAfterEngineWarmup = 0;
            return;
        }

        if (hudMissileGunId == 0) hudMissileGunId = HUD.makeIdLog();
        Missile theMissile = (Missile) this.rocket;
        if (this.theMissileName == null) if (theMissile != null) {
            Class theMissileClass = theMissile.getClass();
            this.theMissileName = Property.stringValue(theMissileClass, "friendlyName", "Missile");
            this.shotFrequency = (long) (1000.0F * Property.floatValue(theMissileClass, "shotFreq", 0.5F));
            this.engineWarmupTime = Property.longValue(theMissileClass, "engineDelayTime", 0L) * -1L;
        }

//		if (this.actor.isNet() && this.actor.isNetMirror() && !isValidForNetMirrors) {
//			if (this.engineWarmupTime > 0L) {
//				if (Time.current() < lastMissileLaunched + 5000L) {
//					System.out.println("MissileGun shots launch cancelled for NetMirror (1)");
//					return;
//				}
//			} else {
//				System.out.println("MissileGun shots launch cancelled for NetMirror (2)");
//				return;
//			}
//		}
//		lastMissileLaunched = Time.current();

        if (isValidForNetMirrors) {
            this.engineWarmupRunning = false;
            this.shotsAfterEngineWarmup = 0;
        } else {
            if (this.actor.isNetMirror()) return;
//			if (numMissilesToShoot == 0) {
//				super.shots(numMissilesToShoot); // TODO: This is necessary to re-enable Missiles in case of unlimited ammo
//				this.engineWarmupRunning = false;
//				this.shotsAfterEngineWarmup = 0;
//				return;
//			}

            try {
                if (Actor.isValid(this.actor) && this.actor instanceof Aircraft && this.actor instanceof TypeGuidedMissileCarrier) {
                    Aircraft aircraft = (Aircraft) this.actor;
                    GuidedMissileUtils guidedMissileUtils = ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils();
                    if (aircraft == World.getPlayerAircraft() && ((RealFlightModel) aircraft.FM).isRealMode() && guidedMissileUtils.hasMissiles()) {
                        if (guidedMissileUtils.getMissileLockState() == 0) {
                            GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId, this.theMissileName + " launch cancelled (disengaged)");
                            return;
                        }
                        long shotFrequency = this.shotFrequency;
                        if (aircraft.FM.CT.getRocketFireMode() == Controls.fullSalvo) shotFrequency = -100L;
                        if (Time.current() < guidedMissileUtils.getStartLastMissile() + shotFrequency) {
                            GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId, this.theMissileName + " launch cancelled (missile not ready yet)");
                            return;
                        }
                    }
                    // else {
                    // if (this.actor.isNetMirror()) {
                    // if (((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().getMissileLockState() == 0) {
                    // // missile disengaged.
                    // return;
                    // }
                    // }
                    // }
                }
            } catch (Exception exception) {
                GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId, this.theMissileName + " launch cancelled (system error)");
                return;
            }
            if (theMissile == null) {
                this.engineWarmupRunning = false;
                return;
            }
            if (this.engineWarmupRunning) if (Time.current() < theMissile.getStartTime() + this.engineWarmupTime) {
                GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId, this.theMissileName + " launch cancelled (engine warmup running)");
                return;
            }
            // System.out.println("" + theMissile.getClass().getName() + " engineDelayTime=" + this.engineWarmupTime + ", engineWarmupRunning=" + this.engineWarmupRunning);
            if (this.engineWarmupTime > 0L) // System.out.println("MissileGun 2 shots(" + numMissilesToShoot + ") class=" + this.getClass().getName() + ", hash=" + this.hashCode());
                if (!this.engineWarmupRunning) {
                    // System.out.println("MissileGun 3 shots(" + numMissilesToShoot + ") class=" + this.getClass().getName() + ", hash=" + this.hashCode());

                    if (this.actor.isNetMaster()) ZutiSupportMethods_Air.sendNetAircraftMissileEngineStart(this.actor, this);

                    theMissile.startEngine();
                    theMissile.setStartTime();
                    if (this.engineWarmupTime > 1000L) GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId, this.theMissileName + " engine starting");
                    this.engineWarmupRunning = true;
                    this.shotsAfterEngineWarmup = numMissilesToShoot;
                    return;
                }
        }
//		System.out.println("MissileGun 5 shots(" + numMissilesToShoot + ") class=" + this.getClass().getName() + ", hash=" + this.hashCode());
        this.engineWarmupRunning = false;
        this.bExecuted = false;
        super.shots(numMissilesToShoot);
        if (Actor.isValid(this.actor) && this.actor instanceof TypeGuidedMissileCarrier) ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().setStartLastMissile(Time.current());
        if (this.engineWarmupTime > 1000L) GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId, this.theMissileName + " released");
        // if (!(NetMissionTrack.isPlaying() || this.actor.isNetMirror() /* Mission.isNet() */)) {
        if (numMissilesToShoot > 0 && Actor.isValid(this.actor) && this.actor instanceof TypeGuidedMissileCarrier && (World.cur().diffCur.Limited_Ammo || this.actor != World.getPlayerAircraft())) // System.out.println("MissileGun 6 shots(" +
                                                                                                                                                                                                    // numMissilesToShoot + ") class=" +
                                                                                                                                                                                                    // this.getClass().getName() + ", hash=" +
                                                                                                                                                                                                    // this.hashCode());
            ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().shotMissile(this);
        // }
        this.isValidForNetMirrors = false;
    }

    public void netStartEngine() {
//    	System.out.println("MissileGun netStartEngine() " + this.hashCode());
        Missile theMissile = (Missile) this.rocket;
        if (theMissile == null) return;
        theMissile.startEngine();
        theMissile.setStartTime();
        this.engineWarmupRunning = true;
        this.shotsAfterEngineWarmup = 0;
    }

    public void loadBullets(int numBulletsToLoad) {
//		System.out.println("MissileGun 1 loadBullets(" + numBulletsToLoad + ") class=" + this.getClass().getName() + ", hash=" + this.hashCode());
//		Exception e = new Exception("loadBullets");
//		e.printStackTrace();
        super.loadBullets(numBulletsToLoad);
//		if (numBulletsToLoad != 0 && this.actor instanceof TypeGuidedMissileCarrier) { ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().onAircraftLoaded(); }
        if (this.actor instanceof TypeGuidedMissileCarrier) ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().onAircraftLoaded();
    }

}
