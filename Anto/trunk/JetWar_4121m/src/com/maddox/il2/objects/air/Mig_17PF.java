package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;

public class Mig_17PF extends Mig_17
implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeRadarGunsight
{
	private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
	private Sample smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
	private boolean hasChaff = false;
	private boolean hasFlare = false;
	private long lastChaffDeployed = 0L;
	private long lastFlareDeployed = 0L;
	private long lastCommonThreatActive = 0L;
	private long intervalCommonThreat = 1000L;
	private long lastRadarLockThreatActive = 0L;
	private long intervalRadarLockThreat = 1000L;
	private long lastMissileLaunchThreatActive = 0L;
	private long intervalMissileLaunchThreat = 1000L;

	public Mig_17PF()
	{
		this.guidedMissileUtils = new GuidedMissileUtils(this);
		this.smplSirena.setInfinite(true);
	}

	//<editor-fold defaultstate="collapsed" desc="G Forces">
	/**
	 * G-Force Resistance, Tolerance and Recovery parmeters.
	 * See TypeGSuit.GFactors Private fields implementation
	 * for further details.
	 */
	private static final float NEG_G_TOLERANCE_FACTOR = 1.0F;
	private static final float NEG_G_TIME_FACTOR = 1.0F;
	private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
	private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
	private static final float POS_G_TIME_FACTOR = 1.5F;
	private static final float POS_G_RECOVERY_FACTOR = 1.0F;

	public void getGFactors(GFactors theGFactors) {
		theGFactors.setGFactors(
				NEG_G_TOLERANCE_FACTOR,
				NEG_G_TIME_FACTOR,
				NEG_G_RECOVERY_FACTOR,
				POS_G_TOLERANCE_FACTOR,
				POS_G_TIME_FACTOR,
				POS_G_RECOVERY_FACTOR);
	}
	// </editor-fold>

	public long getChaffDeployed()
	{
		if (this.hasChaff) {
			return this.lastChaffDeployed;
		}
		return 0L;
	}

	public long getFlareDeployed() {
		if (this.hasFlare) {
			return this.lastFlareDeployed;
		}
		return 0L;
	}

	public void setCommonThreatActive()
	{
		long curTime = Time.current();
		if (curTime - this.lastCommonThreatActive > this.intervalCommonThreat) {
			this.lastCommonThreatActive = curTime;
			doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long curTime = Time.current();
		if (curTime - this.lastRadarLockThreatActive > this.intervalRadarLockThreat) {
			this.lastRadarLockThreatActive = curTime;
			doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		long curTime = Time.current();
		if (curTime - this.lastMissileLaunchThreatActive > this.intervalMissileLaunchThreat) {
			this.lastMissileLaunchThreatActive = curTime;
			doDealMissileLaunchThreat();
		}
	}

	private void doDealCommonThreat()
	{
	}

	private void doDealRadarLockThreat()
	{
	}

	private void doDealMissileLaunchThreat()
	{
	}

	public GuidedMissileUtils getGuidedMissileUtils() {
		return this.guidedMissileUtils;
	}

	public void onAircraftLoaded()
	{
		super.onAircraftLoaded();
		if (this.FM.isPlayers()) {
			this.FM.CT.bHasCockpitDoorControl = true;
			this.FM.CT.dvCockpitDoor = 0.5F;
		}
		this.guidedMissileUtils.onAircraftLoaded();
	}

	public void doEjectCatapult() {
		new MsgAction(false, this) {

			public void doAction(Object paramObject) {
				Aircraft localAircraft = (Aircraft) paramObject;
				if (Actor.isValid(localAircraft)) {
					Loc localLoc1 = new Loc();
					Loc localLoc2 = new Loc();
					Vector3d localVector3d = new Vector3d(0.0, 0.0, 10.0);
					HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
					localAircraft.pos.getAbs(localLoc2);
					localHookNamed.computePos(localAircraft, localLoc2,
							localLoc1);
					localLoc1.transform(localVector3d);
					localVector3d.x += localAircraft.FM.Vwld.x;
					localVector3d.y += localAircraft.FM.Vwld.y;
					localVector3d.z += localAircraft.FM.Vwld.z;
					new EjectionSeat(8, localLoc1, localVector3d,
							localAircraft);
				}
			}
		};
		this.hierMesh().chunkVisible("Seat_D0", false);
		FM.setTakenMortalDamage(true, null);
		FM.CT.WeaponControl[0] = false;
		FM.CT.WeaponControl[1] = false;
		FM.CT.bHasAileronControl = false;
		FM.CT.bHasRudderControl = false;
		FM.CT.bHasElevatorControl = false;
	}

	public void update(float f1) {
		this.guidedMissileUtils.update();
		super.update(f1);
		typeFighterAceMakerRangeFinder();
		if ((this.FM.AS.isMaster()) && (Config.isUSE_RENDER())) {
			if ((this.FM.EI.engines[0].getThrustOutput() > 0.5F) && (this.FM.EI.engines[0].getStage() == 6)) {
				if (this.FM.EI.engines[0].getThrustOutput()> 1.001F)
					this.FM.AS.setSootState(this, 0, 5);
				else
					this.FM.AS.setSootState(this, 0, 3);
			}
			else {
				this.FM.AS.setSootState(this, 0, 0);
			}
		}
	}

	static
	{
		Class var_class = Mig_17PF.class;
		new NetAircraft.SPAWN(var_class);
		Property.set(var_class, "iconFar_shortClassName", "MiG-17PF");
		Property.set(var_class, "meshName_ru", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_ru", new PaintSchemeFCSPar1956());
		Property.set(var_class, "meshName_sk", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_sk", new PaintSchemeFMPar1956());
		Property.set(var_class, "meshName_ro", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_ro", new PaintSchemeFMPar1956());
		Property.set(var_class, "meshName_hu", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme_hu", new PaintSchemeFMPar1956());
		Property.set(var_class, "meshName", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
		Property.set(var_class, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(var_class, "yearService", 1952.11F);
		Property.set(var_class, "yearExpired", 1960.3F);
		Property.set(var_class, "FlightModel", "FlightModels/MiG-17PF.fmd");
		Property.set(var_class, "cockpitClass", new Class[] { 
				CockpitMig_17PF.class, CockpitMig_17Radar.class
		});

		Property.set(var_class, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(var_class, new int[] { 
				0, 0, 0, 9, 9, 9, 9, 9, 9, 2, 
				2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 
				2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 
				9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2 });

		Aircraft.weaponHooksRegister(var_class, new String[] { 
				"_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", 
				"_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev07", "_ExternalDev08", "_ExternalRock05", 
				"_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", 
				"_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", 
				"_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", 
				"_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalDev17", 
				"_ExternalDev13", "_ExternalDev14", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", 
				"_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", 
				"_ExternalRock57", "_ExternalRock58", "_ExternalRock59", "_ExternalRock60", "_ExternalRock61", "_ExternalRock62", "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", 
				"_ExternalRock67", "_ExternalRock68", "_ExternalRock69", "_ExternalRock70" });

		Aircraft.weaponsRegister(var_class, "default", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG_Cannons 1" });

		Aircraft.weaponsRegister(var_class, "2xdrops", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", "FTGunL 1", "FTGunR 1", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG_Cannons 1" });

		Aircraft.weaponsRegister(var_class, "2xORO57", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", null, null, "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG_Cannons 1" });

		Aircraft.weaponsRegister(var_class, "2xORO57+2xdrops", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", "FTGunL 1", "FTGunR 1", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", null, null, "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG_Cannons 1" });

		Aircraft.weaponsRegister(var_class, "4xORO57", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5 1", "RocketGunS5 1", "PylonMiG_Cannons 1" });

		Aircraft.weaponsRegister(var_class, "4xORO57+2xdrops", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", "FTGunL 1", "FTGunR 1", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", 
				"RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5Salvo 1", "RocketGunS5 1", "RocketGunS5 1", "PylonMiG_Cannons 1" });

		Aircraft.weaponsRegister(var_class, "2xMARS2", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG_Cannons 1", 
				"PylonMARS2 1", "PylonMARS2 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

		Aircraft.weaponsRegister(var_class, "2xMARS2+2xdrops", new String[] { 
				"MGunNR23ki 100", "MGunNR23ki 100", "MGunNR23ki 100", "FTGunL 1", "FTGunR 1", 
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG_Cannons 1", 
				"PylonMARS2 1", "PylonMARS2 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

		Aircraft.weaponsRegister(var_class, "4xK5M", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", null, null, "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunK5M 1", 
				"RocketGunNull 1", "RocketGunK5M 1", "RocketGunNull 1", "RocketGunK5M 1", "RocketGunNull 1", "RocketGunK5M 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "4xK5M+2xdrops", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", "FTGunL 1", "FTGunR 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunK5M 1", 
				"RocketGunNull 1", "RocketGunK5M 1", "RocketGunNull 1", "RocketGunK5M 1", "RocketGunNull 1", "RocketGunK5M 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "4xK5Mf", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", null, null, "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunK5Mf 1", 
				"RocketGunNull 1", "RocketGunK5Mf 1", "RocketGunNull 1", "RocketGunK5Mf 1", "RocketGunNull 1", "RocketGunK5Mf 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "4xK5Mf+2xdrops", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", "FTGunL 1", "FTGunR 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunK5Mf 1", 
				"RocketGunNull 1", "RocketGunK5Mf 1", "RocketGunNull 1", "RocketGunK5Mf 1", "RocketGunNull 1", "RocketGunK5Mf 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "4xR55", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", null, null, "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunR55 1", 
				"RocketGunNull 1", "RocketGunR55 1", "RocketGunNull 1", "RocketGunR55 1", "RocketGunNull 1", "RocketGunR55 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "4xR55+2xdrops", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", "FTGunL 1", "FTGunR 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunR55 1", 
				"RocketGunNull 1", "RocketGunR55 1", "RocketGunNull 1", "RocketGunR55 1", "RocketGunNull 1", "RocketGunR55 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "4xR55f", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", null, null, "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunR55f 1", 
				"RocketGunNull 1", "RocketGunR55f 1", "RocketGunNull 1", "RocketGunR55f 1", "RocketGunNull 1", "RocketGunR55f 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "4xR55f+2xdrops", new String[] { 
				"MGunNull 1", "MGunNull 1", "MGunNull 1", "FTGunL 1", "FTGunR 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "PylonK5M 1", "RocketGunR55f 1", 
				"RocketGunNull 1", "RocketGunR55f 1", "RocketGunNull 1", "RocketGunR55f 1", "RocketGunNull 1", "RocketGunR55f 1", "RocketGunNull 1" });

		Aircraft.weaponsRegister(var_class, "none", new String[94]);
	}
}