/*Modified Aircraft class for the SAS Engine Mod*/
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.*;
import com.maddox.il2.game.*;
import com.maddox.JGP.*;

// additional Imports for Engine Shake Mod
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Motor;
import java.util.Arrays;

public abstract class AircraftLH extends Aircraft {

	private static float fShakeThreshold = 0.2F;    // Shake Threshold, apply no shake if shake level would be lower than this value
	private static float fMaxShake = 0.4F;          // Maximum Shake Level
	private static float fStartupShakeLevel = 0.5F; // Max. Startup Shake Level in range 0.0F - 1.0F
	private float[] fEngineShakeLevel = null;       // Array of current shake levels per engine

	public static int hudLogCompassId = HUD.makeIdLog();
	public static boolean printCompassHeading = false;
	public boolean bWantBeaconKeys;
	
	public AircraftLH() {
		bWantBeaconKeys = false;
	}

	public void beaconPlus() {
		if (!bWantBeaconKeys
				|| Main.cur().mission.getBeacons(getArmy()) != null
				&& Main.cur().mission.getBeacons(getArmy()).size() == 0) {
			return;
		} else {
			FM.AS.beaconPlus();
			return;
		}
	}

	public void beaconMinus() {
		if (!bWantBeaconKeys
				|| Main.cur().mission.getBeacons(getArmy()) != null
				&& Main.cur().mission.getBeacons(getArmy()).size() == 0) {
			return;
		} else {
			FM.AS.beaconMinus();
			return;
		}
	}

	public void beaconSet(int i) {
		if (!bWantBeaconKeys
				|| Main.cur().mission.getBeacons(getArmy()) != null
				&& Main.cur().mission.getBeacons(getArmy()).size() == 0) {
			return;
		} else {
			FM.AS.setBeacon(i);
			return;
		}
	}

	public void auxPlus(int i) {
		switch (i) {
		case 1: // '\001'
			headingBug = headingBug + 1.0F;
			if (headingBug >= 360F)
				headingBug = 0.0F;
			if (printCompassHeading
					&& World.cur().diffCur.RealisticNavigationInstruments
					&& bWantBeaconKeys)
				HUD.log(hudLogCompassId, "CompassHeading", new Object[] { ""
						+ (int) headingBug });
			break;
		}
	}

	public void auxMinus(int i) {
		switch (i) {
		case 1: // '\001'
			headingBug = headingBug - 1.0F;
			if (headingBug < 0.0F)
				headingBug = 359F;
			if (printCompassHeading
					&& World.cur().diffCur.RealisticNavigationInstruments
					&& bWantBeaconKeys)
				HUD.log(hudLogCompassId, "CompassHeading", new Object[] { ""
						+ (int) headingBug });
			break;
		}
	}

	public void auxPressed(int i) {
		if (i == 1)
			FM.CT.dropExternalStores(true);
	}

	protected void hitFlesh(int i, Shot shot, int j) {
		int k = 0;
		int l = (int) (shot.power * 0.0035F * World.Rnd().nextFloat(0.5F, 1.5F));
		switch (j) {
		default:
			break;

		case 0: // '\0'
			if (World.Rnd().nextFloat() < 0.05F)
				return;
			if (shot.initiator == World.getPlayerAircraft()
					&& World.cur().isArcade())
				HUD.logCenter("H E A D S H O T");
			l *= 30;
			break;

		case 1: // '\001'
			if (World.Rnd().nextFloat() < 0.08F) {
				l *= 2;
				k = World.Rnd().nextInt(1, 15) * 8000;
				break;
			}
			boolean flag1 = World.Rnd().nextInt(0, 100 - l) <= 20;
			if (flag1)
				k = l / World.Rnd().nextInt(1, 10);
			break;

		case 2: // '\002'
			if (World.Rnd().nextFloat() < 0.015F) {
				k = World.Rnd().nextInt(1, 15) * 1000;
			} else {
				boolean flag3 = World.Rnd().nextInt(0, 100 - l) <= 10;
				if (flag3)
					k = l / World.Rnd().nextInt(1, 15);
			}
			l = (int) ((float) l / 1.5F);
			break;
		}
		debuggunnery("*** Pilot " + i + " hit for " + l + "% ("
				+ (int) shot.power + " J)");
		FM.AS.hitPilot(shot.initiator, i, l);
		if (World.cur().diffCur.RealisticPilotVulnerability) {
			if (k > 0)
				FM.AS.setBleedingPilot(shot.initiator, i, k);
			if (i == 0 && j > 0)
				FM.AS.woundedPilot(shot.initiator, j, l);
		}
		if (FM.AS.astatePilotStates[i] > 95 && j == 0)
			debuggunnery("*** Headshot!.");
	}
	
	// Additional "update" function. This function gets called appr. every 30ms for each aircraft.
	public void update(float f) {
		super.update(f); // call parent class update method first to assure that the inheritance chain is kept intact.
		if(!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel)) return; // don't shake for non-player aircraft.
		
		if (this.fEngineShakeLevel == null) this.fEngineShakeLevel = new float[this.FM.EI.getNum()]; // initialize damaged engines array if necessary.
		
		Arrays.fill(this.fEngineShakeLevel, 0.0F); // initialize Engine Shake Level Array
		
		for (int i=0; i<this.FM.EI.getNum(); i++) { // check each engine's stage
			if (this.FM.EI.engines[i].getType() > Motor._E_TYPE_RADIAL) continue; // shake applies to piston engines only
			if (this.FM.EI.engines[i].getStage() < Motor._E_STAGE_CATCH_UP) continue; // engine is not running, no shake
			if (this.FM.EI.engines[i].getStage() > Motor._E_STAGE_NOMINAL) {
				if (this.FM.EI.engines[i].getRPM() == 0.0F)	continue; // engine is not running, no shake
			}
			if (this.FM.EI.engines[i].getStage() < Motor._E_STAGE_CATCH_FIRE)
				this.fEngineShakeLevel[i] = fStartupShakeLevel * (float)(this.FM.EI.engines[i].getStage()) / 5.0F; // engine is starting, set specified startup shake level
			else {
				this.fEngineShakeLevel[i] = 1.0F - this.FM.EI.engines[i].getReadyness(); // engine is running, set shake level according to damage
				if (this.fEngineShakeLevel[i] < fShakeThreshold) this.fEngineShakeLevel[i] = 0.0F; // only take shake levels into account which exceed the threshold shake setting
			}

			// Shake whole aircraft, not just inside the cockpit.
			if (this.fEngineShakeLevel[i] == 0.0F) continue; // don't apply aircraft shake force vector if there's no shake to apply
			Point3f theEnginePos = this.FM.EI.engines[i].getEnginePos(); // get engine position
			Vector3f theEngineShake = new Vector3f(World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F)); // generate random shake force vector
			float fShakeFactor = (float)Math.pow(this.FM.M.massEmpty, 0.3F) / (float)this.FM.EI.getNum() * 10000F * fMaxShake; // calculate shake force, must fit for aircraft weight etc.
			theEngineShake.scale(this.fEngineShakeLevel[i] * fShakeFactor); // scale random shake vector accordingly
			Vector3f theEngineMomentum = new Vector3f(); // instantiate the damage shake moment
			theEngineMomentum.cross(theEnginePos, theEngineShake); // damage shake momentum is the vector's cartesian product between engine pos and random shake force
			this.FM.producedAM.x += theEngineMomentum.x; // apply x-axis turn momentum only
		}
		
		float fTotalShake = 0.0F; // aggregated shake level of all engines
		int iShakeWeightFactor = 1 << (this.FM.EI.getNum() - 1); // weighted shake, engine with highest shake level counts most
		if (this.FM.EI.getNum() == 1) { // single engine aircraft, the engine's shake level equals total shake level
			fTotalShake = this.fEngineShakeLevel[0];
		} else { // multi engine aircraft, take all shake level into account (weighted)
			Arrays.sort(fEngineShakeLevel); // sort shake level array (sorts in ascending order)
			for (int i=this.FM.EI.getNum()-1; i>=0; i--) { // go through the list of engine shake levels in descending order
				if (this.fEngineShakeLevel[i] == 0.0F) break; // no more engine shake? exit loop.
				fTotalShake += this.fEngineShakeLevel[i] * (float)iShakeWeightFactor; // add weighted total shake
				iShakeWeightFactor >>= 1; // reduce weight factor by 2
			}
			fTotalShake /= (float)((1 << this.FM.EI.getNum()) - 1); // adjust total shake to 0.0F through 1.0F again.
		}
		if (((RealFlightModel)this.FM).producedShakeLevel < fTotalShake * fMaxShake) // if in-cockpit shake is stronger already, do nothing.
			((RealFlightModel)this.FM).producedShakeLevel = fTotalShake * fMaxShake; // apply shake level according to max shake level setting.
	}
}
