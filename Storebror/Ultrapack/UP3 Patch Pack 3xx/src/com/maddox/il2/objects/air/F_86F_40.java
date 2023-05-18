package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F_86F_40 extends F_86F
		implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

	public F_86F_40() {
		hasChaff = false;
		hasFlare = false;
		lastChaffDeployed = 0L;
		lastFlareDeployed = 0L;
		lastCommonThreatActive = 0L;
		intervalCommonThreat = 1000L;
		lastRadarLockThreatActive = 0L;
		intervalRadarLockThreat = 1000L;
		lastMissileLaunchThreatActive = 0L;
		intervalMissileLaunchThreat = 1000L;
		bToFire = false;
		guidedMissileUtils = new GuidedMissileUtils(this);
	}

	public GuidedMissileUtils getGuidedMissileUtils() {
		return this.guidedMissileUtils;
	}

	public long getChaffDeployed() {
		if (hasChaff)
			return lastChaffDeployed;
		else
			return 0L;
	}

	public long getFlareDeployed() {
		if (hasFlare)
			return lastFlareDeployed;
		else
			return 0L;
	}

	public void setCommonThreatActive() {
		long curTime = Time.current();
		if (curTime - lastCommonThreatActive > intervalCommonThreat) {
			lastCommonThreatActive = curTime;
			doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long curTime = Time.current();
		if (curTime - lastRadarLockThreatActive > intervalRadarLockThreat) {
			lastRadarLockThreatActive = curTime;
			doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		long curTime = Time.current();
		if (curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat) {
			lastMissileLaunchThreatActive = curTime;
			doDealMissileLaunchThreat();
		}
	}

	private void doDealCommonThreat() {
	}

	private void doDealRadarLockThreat() {
	}

	private void doDealMissileLaunchThreat() {
	}

	public void getGFactors(TypeGSuit.GFactors theGFactors) {
		theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR,
				POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (super.FM.isPlayers()) {
			this.FM.CT.bHasCockpitDoorControl = true;
			this.FM.CT.dvCockpitDoor = 0.5F;
		}
		this.guidedMissileUtils.onAircraftLoaded();
	}

	public void update(float f) {
		this.guidedMissileUtils.update();
		super.update(f);
		if (super.FM.getSpeed() > 5F) {
			moveSlats(f);
			super.bSlatsOff = false;
		} else {
			slatsOff();
		}
	}

	protected void moveSlats(float paramFloat) {
		resetYPRmodifier();
		Aircraft.xyz[0] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.15F);
		Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.1F);
		Aircraft.xyz[2] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.065F);
		hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
		hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.1F);
		hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
		hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void slatsOff() {
		if (!super.bSlatsOff) {
			resetYPRmodifier();
			Aircraft.xyz[0] = -0.15F;
			Aircraft.xyz[1] = 0.1F;
			Aircraft.xyz[2] = -0.065F;
			hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
			hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
			Aircraft.xyz[1] = -0.1F;
			hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
			hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
			super.bSlatsOff = true;
		}
	}

	private GuidedMissileUtils guidedMissileUtils;
	private boolean hasChaff;
	private boolean hasFlare;
	private long lastChaffDeployed;
	private long lastFlareDeployed;
	private long lastCommonThreatActive;
	private long intervalCommonThreat;
	private long lastRadarLockThreatActive;
	private long intervalRadarLockThreat;
	private long lastMissileLaunchThreatActive;
	private long intervalMissileLaunchThreat;
	private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
	private static final float NEG_G_TIME_FACTOR = 1.5F;
	private static final float NEG_G_RECOVERY_FACTOR = 1F;
	private static final float POS_G_TOLERANCE_FACTOR = 2F;
	private static final float POS_G_TIME_FACTOR = 2F;
	private static final float POS_G_RECOVERY_FACTOR = 2F;
	public boolean bToFire;

	static {
		Class localClass = F_86F_40.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "F-86");
		Property.set(localClass, "meshName", "3DO/Plane/F-86F(Multi1)/hierF40.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(localClass, "meshName_us", "3DO/Plane/F-86F(USA)/hierF40.him");
		Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(localClass, "yearService", 1949.9F);
		Property.set(localClass, "yearExpired", 1960.3F);
		Property.set(localClass, "FlightModel", "FlightModels/F-86F-40.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitF_86Flate.class });
		Property.set(localClass, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 9, 2, 2,
				9, 2, 2, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 });
		Aircraft.weaponHooksRegister(localClass,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01",
						"_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01",
						"_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07",
						"_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02",
						"_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13",
						"_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04",
						"_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07",
						"_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12",
						"_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17",
						"_ExternalRock18", "_ExternalBomb07" });
	}
}
