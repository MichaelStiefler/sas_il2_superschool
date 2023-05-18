package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CAC_Sabre_Mk31 extends F_86F implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

	public CAC_Sabre_Mk31() {
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
		this.oldthrl = -1F;
		this.curthrl = -1F;
		this.bToFire = false;
		this.guidedMissileUtils = new GuidedMissileUtils(this);
		this.engineSurgeDamage = 0.0F;
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
		long curTime = Time.current();
		if (curTime - this.lastCommonThreatActive > this.intervalCommonThreat) {
			this.lastCommonThreatActive = curTime;
			this.doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long curTime = Time.current();
		if (curTime - this.lastRadarLockThreatActive > this.intervalRadarLockThreat) {
			this.lastRadarLockThreatActive = curTime;
			this.doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		long curTime = Time.current();
		if (curTime - this.lastMissileLaunchThreatActive > this.intervalMissileLaunchThreat) {
			this.lastMissileLaunchThreatActive = curTime;
			this.doDealMissileLaunchThreat();
		}
	}

	private void doDealCommonThreat() {
	}

	private void doDealRadarLockThreat() {
	}

	private void doDealMissileLaunchThreat() {
	}

	public void getGFactors(TypeGSuit.GFactors theGFactors) {
		theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (this.FM.isPlayers()) {
			this.FM.CT.bHasCockpitDoorControl = true;
			this.FM.CT.dvCockpitDoor = 0.5F;
		}
		this.guidedMissileUtils.onAircraftLoaded();
	}

	public void engineSurge(float f) {
		if (this.FM.AS.isMaster()) {
			if (this.curthrl == -1F) {
				this.curthrl = this.oldthrl = this.FM.EI.engines[0].getControlThrottle();
			} else {
				this.curthrl = this.FM.EI.engines[0].getControlThrottle();
				if (this.curthrl < 1.05F) {
					if ((this.curthrl - this.oldthrl) / f > 10F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F) {
						if (this.FM.actor == World.getPlayerAircraft()) { HUD.log("Compressor Stall!"); }
						this.playSound("weapon.MGunMk108s", true);
						this.engineSurgeDamage += 0.01D * (this.FM.EI.engines[0].getRPM() / 1000F);
						this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
						if (World.Rnd().nextFloat() < 0.05F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) { this.FM.AS.hitEngine(this, 0, 100); }
						if (World.Rnd().nextFloat() < 0.05F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) { this.FM.EI.engines[0].setEngineDies(this); }
					}
					if ((this.curthrl - this.oldthrl) / f < -10F && (this.curthrl - this.oldthrl) / f > -100F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6) {
						this.playSound("weapon.MGunMk108s", true);
						this.engineSurgeDamage += 0.001D * (this.FM.EI.engines[0].getRPM() / 1000F);
						this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
						if (World.Rnd().nextFloat() < 0.4F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
							if (this.FM.actor == World.getPlayerAircraft()) { HUD.log("Engine Flameout!"); }
							this.FM.EI.engines[0].setEngineStops(this);
						} else if (this.FM.actor == World.getPlayerAircraft()) { HUD.log("Compressor Stall!"); }
					}
				}
				this.oldthrl = this.curthrl;
			}
		}
		if (this.FM.CT.WeaponControl[1] && this.FM.EI.engines[0].getStage() == 6 && this.FM.CT.Weapons[0][0].countBullets() != 0 && World.Rnd().nextFloat() < 0.005F) {
			this.playSound("weapon.MGunMk108s", true);
			this.engineSurgeDamage += 0.001D * (this.FM.EI.engines[0].getRPM() / 1000F);
			this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
			if (World.Rnd().nextFloat() < 0.2F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
				if (this.FM.actor == World.getPlayerAircraft()) { HUD.log("Engine Flameout!"); }
				this.FM.EI.engines[0].setEngineStops(this);
			} else if (this.FM.actor == World.getPlayerAircraft()) { HUD.log("Compressor Stall!"); }
		}
	}

	public void update(float f) {
		this.guidedMissileUtils.update();
		super.update(f);
		this.engineSurge(f);
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
	public boolean             bToFire;
	private float              oldthrl;
	private float              curthrl;
	private float              engineSurgeDamage;
	private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
	private static final float NEG_G_TIME_FACTOR      = 1.5F;
	private static final float NEG_G_RECOVERY_FACTOR  = 1F;
	private static final float POS_G_TOLERANCE_FACTOR = 2F;
	private static final float POS_G_TIME_FACTOR      = 2F;
	private static final float POS_G_RECOVERY_FACTOR  = 2F;

	static {
		Class localClass = CAC_Sabre_Mk31.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "CAC_Sabre");
		Property.set(localClass, "meshName_gb", "3DO/Plane/CAC_Sabre_Mk32(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme_gb", new PaintSchemeFMPar1956());
		Property.set(localClass, "meshName", "3DO/Plane/CAC_Sabre_Mk32(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(localClass, "yearService", 1949.9F);
		Property.set(localClass, "yearExpired", 1960.3F);
		Property.set(localClass, "FlightModel", "FlightModels/CAC_Sabre_Mk31.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitF_86Flate.class });
		Property.set(localClass, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 9, 2, 2, 9, 2, 2, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9 });
		Aircraft.weaponHooksRegister(localClass,
				new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07",
						"_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03",
						"_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11",
						"_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23",
						"_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", "_ExternalDev20", "_ExternalDev21", "_ExternalDev22", "_ExternalDev23", "_ExternalDev24", "_ExternalRock27",
						"_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39",
						"_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalDev25", "_ExternalDev26", "_ExternalDev27", "_ExternalDev28", "_ExternalDev29", "_ExternalDev30", "_ExternalDev31", "_ExternalDev32" });
	}
}
