// Source File Name: MissileGun.java
// Author:           Storebror
package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeGuidedMissileCarrier;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class MissileGun extends RocketGun {
	public static int hudMissileGunId = 0;
	private boolean engineWarmupRunning = false;
	private long engineWarmupTime = 0L;
	private long shotFrequency = 500L;
	private int shotsAfterEngineWarmup = 0;
	private String theMissileName = null;

	public float bulletMassa() {
		return this.bulletMassa / 10F;
	}

	public void checkPendingWeaponRelease() {
		// com.maddox.il2.game.HUD.log("checkPendingWeaponRelease");
		if (this.engineWarmupRunning) {
			Missile theMissile = (Missile) this.rocket;
			if (theMissile == null) {
				this.engineWarmupRunning = false;
				return;
			}
			if (Time.current() > theMissile.getStartTime() + this.engineWarmupTime) {
				this.shots(this.shotsAfterEngineWarmup);
			} else {
				theMissile.runupEngine();
			}
		}
		return;
	}

	public Missile getMissile() {
		return (Missile) this.rocket;
	}

	public void shots(int paramInt) {
		if (paramInt == 0)
			return;
//		Exception testException = new Exception("MissileGun Test");
//		testException.printStackTrace();

		if (hudMissileGunId == 0) {
			hudMissileGunId = HUD.makeIdLog();
		}
		Missile theMissile = (Missile) this.rocket;
		if (this.theMissileName == null) {
			if (theMissile != null) {
				this.theMissileName = Property.stringValue(theMissile.getClass(), "friendlyName", "Missile");
				this.shotFrequency = (long) (1000.0F * Property.floatValue(theMissile.getClass(), "shotFreq", 0.5F));
			}
		}
		// System.out.println("MissileGun shots 1");
		try {
			if (Actor.isValid(this.actor) && (this.actor instanceof Aircraft)
					&& (this.actor instanceof TypeGuidedMissileCarrier)
					&& (Aircraft) this.actor == World.getPlayerAircraft()
					&& ((RealFlightModel) ((SndAircraft) ((Aircraft) this.actor)).FM).isRealMode()
					&& ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().hasMissiles()
					&& (((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().getMissileLockState() == 0)) {
				GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId,
						this.theMissileName + " launch cancelled (disengaged)");
				return;
			}
			if (Actor.isValid(this.actor) && (this.actor instanceof Aircraft)
					&& (this.actor instanceof TypeGuidedMissileCarrier)
					&& (Aircraft) this.actor == World.getPlayerAircraft()
					&& ((RealFlightModel) ((SndAircraft) ((Aircraft) this.actor)).FM).isRealMode()
					&& ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().hasMissiles()
					&& (Time.current() < ((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils()
							.getStartLastMissile() + this.shotFrequency)) {
				GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId,
						this.theMissileName + " launch cancelled (missile not ready yet)");
				return;
			}
		} catch (Exception exception) {
			GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId,
					this.theMissileName + " launch cancelled (system error)");
			return;
		}
		if (theMissile == null) {
			// System.out.println("MissileGun shots 0");
			this.engineWarmupRunning = false;
			return;
		}
		if (this.engineWarmupRunning) {
			if (Time.current() < theMissile.getStartTime() + this.engineWarmupTime) {
				GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId,
						this.theMissileName + " launch cancelled (engine warmup running)");
				return;
			}
		}
		this.engineWarmupTime = Property.longValue(theMissile.getClass(), "engineDelayTime", 0L) * -1L;
		if (this.engineWarmupTime > 0L) {
			// System.out.println("MissileGun shots 2");
			if (!this.engineWarmupRunning) {
				// System.out.println("MissileGun shots 3");
				theMissile.startEngine();
				theMissile.setStartTime();
				if (this.engineWarmupTime > 1000L) {
					GuidedMissileUtils.LocalLog(this.actor, hudMissileGunId, this.theMissileName + " engine starting");
				}
				this.engineWarmupRunning = true;
				this.shotsAfterEngineWarmup = paramInt;
				return;
			}
		}
		// System.out.println("MissileGun shots 4");
		this.engineWarmupRunning = false;
		this.bExecuted = false;
		super.shots(paramInt);
		if (Actor.isValid(super.actor) && (this.actor instanceof TypeGuidedMissileCarrier)) {
			((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().setStartLastMissile(Time.current());
		}
		if (this.engineWarmupTime > 1000L) {
			GuidedMissileUtils.LocalLog(super.actor, hudMissileGunId, this.theMissileName + " released");
		}
		if (!(NetMissionTrack.isPlaying() || this.actor.isNetMirror() /* Mission.isNet() */)) {
			if ((paramInt > 0) && Actor.isValid(super.actor) && (this.actor instanceof TypeGuidedMissileCarrier)
					&& (World.cur().diffCur.Limited_Ammo || (this.actor != World.getPlayerAircraft()))) {
				((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().shotMissile();
			}
		}
	}

	public void loadBullets(int i) {
		super.loadBullets(i);
		if (this.actor instanceof TypeGuidedMissileCarrier) {
			((TypeGuidedMissileCarrier) this.actor).getGuidedMissileUtils().onAircraftLoaded();
		}
	}

}
