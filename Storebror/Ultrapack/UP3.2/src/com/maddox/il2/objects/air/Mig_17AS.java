package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Mig_17AS extends Mig_17 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

	public Mig_17AS() {
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
		this.bToFire = false;
		this.guidedMissileUtils = new GuidedMissileUtils(this);
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

	public void update(float f) {
		this.guidedMissileUtils.update();
		super.update(f);
		this.typeFighterAceMakerRangeFinder();
		if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
			if (this.FM.EI.engines[0].getPowerOutput() > 0.5F && this.FM.EI.engines[0].getStage() == 6) {
				if (this.FM.EI.engines[0].getPowerOutput() > 0.5F) {
					if (this.FM.EI.engines[0].getPowerOutput() > 1.001F) {
						this.FM.AS.setSootState(this, 0, 5);
					} else {
						this.FM.AS.setSootState(this, 0, 3);
					}
				}
			} else {
				this.FM.AS.setSootState(this, 0, 0);
			}
		}
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
	private static final float NEG_G_TOLERANCE_FACTOR = 1F;
	private static final float NEG_G_TIME_FACTOR      = 1F;
	private static final float NEG_G_RECOVERY_FACTOR  = 1F;
	private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
	private static final float POS_G_TIME_FACTOR      = 1.5F;
	private static final float POS_G_RECOVERY_FACTOR  = 1F;
	public boolean             bToFire;

	static {
		Class var_class = Mig_17AS.class;
		new NetAircraft.SPAWN(var_class);
		Property.set(var_class, "iconFar_shortClassName", "MiG-17AS");
		Property.set(var_class, "meshName_ru", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_ru", new PaintSchemeFCSPar1956());
		Property.set(var_class, "meshName_sk", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_sk", new PaintSchemeFMPar1956());
		Property.set(var_class, "meshName_ro", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_ro", new PaintSchemeFMPar1956());
		Property.set(var_class, "meshName_hu", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_hu", new PaintSchemeFMPar1956());
		Property.set(var_class, "meshName", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(var_class, "yearService", 1952.11F);
		Property.set(var_class, "yearExpired", 1960.3F);
		Property.set(var_class, "FlightModel", "FlightModels/MiG-17F.fmd");
		Property.set(var_class, "cockpitClass", new Class[] { CockpitMig_17.class });
		Property.set(var_class, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(var_class, new int[] { 1, 0, 0, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(var_class,
				new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02",
						"_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev07", "_ExternalDev08", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalDev09", "_ExternalDev10",
						"_ExternalDev11", "_ExternalDev12", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16",
						"_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28",
						"_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalDev13", "_ExternalDev14",
						"_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50",
						"_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58", "_ExternalRock59", "_ExternalRock60", "_ExternalRock61", "_ExternalRock62",
						"_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", "_ExternalRock68", "_ExternalRock69", "_ExternalRock70", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb01", "_ExternalBomb01",
						"_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04" });
	}
}
