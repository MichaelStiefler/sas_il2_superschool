
package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Squares;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGun9M114;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;



public class Mi24X extends Scheme2 implements TypeHelicopter, TypeStormovikArmored,
TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeLaserSpotter, TypeFighterAceMaker {

	public Mi24X()
	{
		bAPazimut = false;
		bAPkrentang = true;
		bAPalt = false;
		apALltPitch = 0.0F;
		apAzimut = 0.0F;
		apKren = 0.0F;
		apTang = 0.0F;
		forceTrim_x = 0.0D;
		forceTrim_y = 0.0D;
		forceTrim_z = 0.0D;
		getTrim = false;
		curAngleRotor = 0.0F;
		lastTimeFan = Time.current();
		rocketHookSelected = 2;
		suka = new Loc();
		obsLookTime = 0;
		obsLookAzimuth = 0.0F;
		obsLookElevation = 0.0F;
		obsAzimuth = 0.0F;
		obsElevation = 0.0F;
		obsAzimuthOld = 0.0F;
		obsElevationOld = 0.0F;
		obsMove = 0.0F;
		obsMoveTot = 0.0F;
		bObserverKilled = false;
		hasChaff = false;
		hasFlare = false;
		lastChaffDeployed = 0L;
		lastFlareDeployed = 0L;
		lastCommonThreatActive = 0L;
		intervalCommonThreat = 1000L;
		lastRadarLockThreatActive = 0L;
		intervalRadarLockThreat = 1000L;
		lastMissileLaunchThreatActive = 0L;
		intervalMissileLaunchThreat = 500L;
		guidedMissileUtils = new GuidedMissileUtils(this);
		curAngleRotor = 0.0F;
		lastTimeFan = Time.current();
		TailRotorDestroyed = false;
		laserOn = false;
		gndYaw = 0.0F;
		gndRoll = 0.0F;
		gndPitch = 0.0F;
		hover = false;
		landing = false;
		counter = 0;
		repMod = 0;
		missileLaunchInterval = 0L;
		victim = null;
		missilesList = new ArrayList();
		k14Mode = 0;
		k14WingspanType = 0;
		k14Distance = 200F;
		aPitch = 0.0F;
		aOldPitch = 0.0F;
		rotorRPM = 0.0D;
		tailRotorRPM = 0.0D;
		reductorRPM = 0.0D;
		engineRPM = 0.0D;
		sndTrim = this.newSound("cockpit.trimmer", false);
		sndProp = this.newSound("propeller.mi24", true);
		isAirstart = false;
		AltCheck = false;
		asTimer = Time.current() + 500L;
		aso2mode = 0;
		threatIsNear = false;
		hookDust = new Hook[4];
		hookDust[0] = this.findHook("_Dust00");
		hookDust[1] = this.findHook("_Dust01");
		hookDust[2] = this.findHook("_Dust02");
		hookDust[3] = this.findHook("_Dust03");
		dustLoc = new Loc();
		dustEff = new Eff3DActor[4];
		tV = new Vector3d[2];
		tV[0] = new Vector3d(0.0D, 0.0D, 0.0D);
		tV[1] = new Vector3d(0.0D, 0.0D, 0.0D);
		tP = new Point3d[5];
		tP[0] = new Point3d(0.0D, 0.0D, 0.0D);
		tP[1] = new Point3d(0.0D, 0.0D, 0.0D);
		tP[2] = new Point3d(0.0D, 0.0D, 0.0D);
		tP[3] = new Point3d(0.0D, 0.0D, 0.0D);
		tP[4] = new Point3d(0.0D, 0.0D, 0.0D);
		tOr = new Orientation(0.0F, 0.0F, 0.0F);
	}

	public void checkAirstart () {
		if (!AltCheck && FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y) > 20F) 
		{
			FM.EI.engines[0].doSetStage(6);
			FM.EI.engines[1].doSetStage(6);
			this.FM.EI.engines[0].setw((15000 * 3.141593F * 2.0F) / 60F);
			this.FM.EI.engines[1].setw((15000 * 3.141593F * 2.0F) / 60F);
			aPitch = 0.75F;
			engineRPM = 15000;
			reductorRPM = 228D;
			rotorRPM = 228D;
			FM.CT.setPowerControl(1.0F);
			AltCheck = true;
		}
	}

	public void rotorSound() {
		this.sndProp.setParent(this.getRootFX());
		this.sndProp.setPosition(this.FM.EI.engines[0].getEnginePos());
		this.sndProp.setControl(100, (float) rotorRPM);
		this.sndProp.setControl(108, aPitch);
	}

	public void dustEmit() {	
		for (int j = 0; j < FM.EI.getNum(); j++) {
			if (FM.Gears.clpEngineEff[j][0] != null) {
				Eff3DActor.finish(FM.Gears.clpEngineEff[j][0]);
				FM.Gears.clpEngineEff[j][0] = null;
			}
			if (FM.Gears.clpEngineEff[j][1] != null) {
				Eff3DActor.finish(FM.Gears.clpEngineEff[j][1]);
				FM.Gears.clpEngineEff[j][1] = null;
			}
		}
		float alt = FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
		float f = (float) (cvt(alt, 0F, 20F, TrueRandom.nextFloat(0.4F, 0.9F), 0.01F) * (rotorRPM / 240D));
		if (alt < 20F && rotorRPM > 80) {
			dustLoc.set(0.0D, alt + 3, 0.0D, 0.0F, 0.0F, 0.0F);
			hookDust[0].computePos(this, this.pos.getCurrent(), dustLoc);
			dustLoc.getPoint().z = Landscape.HQ_Air((float) dustLoc.getPoint().x, (float) dustLoc.getPoint().y);
			if (Engine.cur.land.isWater(dustLoc.getPoint().x, dustLoc.getPoint().y)) {
				dustEff[0] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				dustEff[0] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}
			hookDust[1].computePos(this, this.pos.getCurrent(), dustLoc);
			dustLoc.getPoint().z = Landscape.HQ_Air((float) dustLoc.getPoint().x, (float) dustLoc.getPoint().y);
			if (Engine.cur.land.isWater(dustLoc.getPoint().x, dustLoc.getPoint().y)) {
				dustEff[1] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				dustEff[1] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}
			hookDust[2].computePos(this, this.pos.getCurrent(), dustLoc);
			dustLoc.getPoint().z = Landscape.HQ_Air((float) dustLoc.getPoint().x, (float) dustLoc.getPoint().y);
			if (Engine.cur.land.isWater(dustLoc.getPoint().x, dustLoc.getPoint().y)) {
				dustEff[2] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				dustEff[2] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}
			hookDust[3].computePos(this, this.pos.getCurrent(), dustLoc);
			dustLoc.getPoint().z = Landscape.HQ_Air((float) dustLoc.getPoint().x, (float) dustLoc.getPoint().y);
			if (Engine.cur.land.isWater(dustLoc.getPoint().x, dustLoc.getPoint().y)) {
				dustEff[3] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				dustEff[3] = Eff3DActor.New(dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}
		} else {
			Eff3DActor.finish(dustEff[0]);
			Eff3DActor.finish(dustEff[1]);
			Eff3DActor.finish(dustEff[2]);
			Eff3DActor.finish(dustEff[3]);
		}
	}

	public void laserUpdate() {
		if (laserOn) {
			this.pos.getRender(this.pos.getCurrent());
			LaserHook[1] = new HookNamed(this, "_Laser1");
			LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			this.LaserHook[1].computePos(this, this.pos.getCurrent(), LaserLoc1);
			LaserLoc1.get(LaserP1);
			LaserLoc1.set(6000.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			this.LaserHook[1].computePos(this, this.pos.getCurrent(), LaserLoc1);
			LaserLoc1.get(LaserP2);
			Engine.land();
			if (Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL)) {
				LaserPL.z -= 0.95D;
				LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
				Mi24V.spot.set(LaserP2);
			}
		}
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
		intervalCommonThreat = aso2mode * 4000;
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
		if (bShotFlare) {
			long curTime = Time.current();
			if (curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat) {
				lastMissileLaunchThreatActive = curTime;
				doDealMissileLaunchThreat();
				lastFlareDeployed++;
				if (lastFlareDeployed > 2)
				{
					lastFlareDeployed = 0L;
					bShotFlare = false;
				}
			}
		}
	}

	private void doDealCommonThreat() {
		hasFlare = FM.CT.Weapons[7][0].haveBullets() && FM.CT.Weapons[7][1].haveBullets();
		if (hasFlare) {
			FM.CT.Weapons[7][0].shots(1);
			FM.CT.Weapons[7][1].shots(1);
		}
	}

	private void doDealRadarLockThreat() {
	}

	private void doDealMissileLaunchThreat() {
		hasFlare = FM.CT.Weapons[7][0].haveBullets() && FM.CT.Weapons[7][1].haveBullets();
		if (hasFlare) {
			FM.CT.Weapons[7][0].shots(1);
			FM.CT.Weapons[7][1].shots(1);
		}
	}

	private void OperatorLookout() {
		double d = Main3D.cur3D().land2D.worldOfsX() + this.pos.getAbsPoint().x;
		double d1 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().y;
		double d2 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().z;
		int i = (int) (-(this.pos.getAbsOrient().getYaw() - 90D));
		if (i < 0) {
			i = 360 + i;
		}
		float f1 = World.getTimeofDay();
		boolean flag = false;
		if (((f1 >= 0.0F) && (f1 <= 5F)) || ((f1 >= 21F) && (f1 <= 24F))) {
			flag = true;
		}
		int i1 = TrueRandom.nextInt(100);
		int i2 = 5 - FM.Skill;
		int i3 = i2 + TrueRandom.nextInt(i2);
		counter++;
		if (counter >= 7 + i3)
			counter = 0;
		//Missile look code:
		if (!FM.Gears.onGround()) 
		{
			List list = Engine.missiles();
			int j1 = list.size();
			for(int k1 = 0; k1 < j1; k1++)
			{
				Actor missile = (Actor)list.get(k1);
				if (missile.getSpeed(vector3dop) > 500D && missile.getArmy() != this.getArmy())
				{
					double d3 = Main3D.cur3D().land2D.worldOfsX() + missile.pos.getAbsPoint().x;
					double d4 = Main3D.cur3D().land2D.worldOfsY() + missile.pos.getAbsPoint().y;
					double d5 = Main3D.cur3D().land2D.worldOfsY() + missile.pos.getAbsPoint().z;
					double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
					String s = "";
					if ((d2 - d5 - 500D) >= 0.0D) {
						s = " LOW";
					}
					if (((d2 - d5) + 500D) < 0.0D) {
						s = " HIGH";
					}
					double d9 = d3 - d;
					double d10 = d4 - d1;
					float f = 57.32484F * (float) Math.atan2(d10, -d9);
					int j = (int) (Math.floor((int) f) - 90D);
					if (j < 0) {
						j = 360 + j;
					}
					int k = j - i;
					if (k < 0) {
						k = 360 + k;
					}
					int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
					if (l < 1) {
						l = 12;
					}
					double d11 = d - d3;
					double d12 = d1 - d4;
					double d13 = Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D;
					if (!flag && (d13 <= 3000D) && (d13 >= 20D) && (Math.sqrt(d8 * d8) <= 2000D)) {
						if (!isAI) {
							HUD.logCenter("MISSILE AT " + l + " O'CLOCK" + s + "!");
							Voice.speakDanger(this, 4);
							HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: ASO-2 Engaged!");
						}
						if (isAI) {
							((Maneuver) FM).set_maneuver(100);
						}
						bShotFlare = true;
					}
					if (flag && (d13 <= 1000D) && (d13 >= 20D) && (Math.sqrt(d8 * d8) <= 500D)) {
						if (!isAI) {
							HUD.logCenter("MISSILE AT " + l + " O'CLOCK" + s + "!");
							Voice.speakDanger(this, 4);
							HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: ASO-2 Engaged!");
						}
						if (isAI) {
							((Maneuver) FM).set_maneuver(100);
						}
						bShotFlare = true;
					}
				}
			}
		}
		//Operator Look Air:
		List targets = Engine.targets();
		int v = targets.size();
		for(int b = 0; b < v; b++) {
			Actor threat = (Actor)targets.get(b);
			double d3 = Main3D.cur3D().land2D.worldOfsX() + threat.pos.getAbsPoint().x;
			double d4 = Main3D.cur3D().land2D.worldOfsY() + threat.pos.getAbsPoint().y;
			double d5 = Main3D.cur3D().land2D.worldOfsY() + threat.pos.getAbsPoint().z;
			if (repMod == 7 && counter == 0) 
			{
				if ((threat instanceof Aircraft) && (threat.getArmy() != this.getArmy()) && !FM.Gears.onGround()) {
					double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
					String s = "";
					if ((d2 - d5 - 500D) >= 0.0D) {
						s = " low";
					}
					if (((d2 - d5) + 500D) < 0.0D) {
						s = " high";
					}
					double d9 = d3 - d;
					double d10 = d4 - d1;
					float f = 57.32484F * (float) Math.atan2(d10, -d9);
					int j = (int) (Math.floor((int) f) - 90D);
					if (j < 0) {
						j = 360 + j;
					}
					int k = j - i;
					if (k < 0) {
						k = 360 + k;
					}
					int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
					if (l < 1) {
						l = 12;
					}
					double d11 = d - d3;
					double d12 = d1 - d4;
					double d13 = Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D;
					String s1 = "Aircraft ";
					if (threat instanceof TypeFighter) {
						s1 = "Fighters ";
					}
					if (threat instanceof TypeBomber) {
						s1 = "Bombers ";
					}
					if (threat instanceof TypeHelicopter) {
						s1 = "Helicopters ";
					}
					if (!flag && (i1 <= 50 + i2) && (d13 <= 4000D) && (d13 >= 500D) && (Math.sqrt(d8 * d8) <= 2000D)) {
						if (!isAI) {
							HUD.training(s1 + "at " + l + " o'clock" + s + "!");
						}
					}
					if (flag && (i1 <= 5 + i2) && (d13 <= 1000D) && (d13 >= 100D) && (Math.sqrt(d8 * d8) <= 500D)) {
						if (!isAI) {
							HUD.training(s1 + "at " + l + " o'clock" + s + "!");
						}
					}
				}
			}
			//Operator Look Ground:
			if ((!isAI && (repMod >= 1 && repMod <= 6) && counter == 0) || (isAI)) 
			{
				if (Actor.isAlive(threat) && ((threat instanceof TankGeneric) || (threat instanceof Wagon) || (threat instanceof ArtilleryGeneric) || (threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric) || (threat instanceof CarGeneric)) && (threat.getArmy() != this.getArmy())) 
				{
					double d8 = (d2 - d5) * 2D;
					if (d8 > 6000D) {
						d8 = 6000D;
					}
					if (flag) {
						d8 = 1500D - (d2 - d5);
					}
					if (!flag && d8 < 2000 + FM.Skill * 100) {
						d8 = FM.Skill * 100 + 2000;
					}
					String s = "units";
					if (repMod == 1) {
						if (threat instanceof TankGeneric) {
							s = "armor";
						}
						if (threat instanceof ArtilleryGeneric) {
							s = "guns";
						}
						if (threat instanceof CarGeneric) {
							s = "vehicles";
						}
						if (threat instanceof Wagon) {
							s = "train";
						}
						if ((threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric)) {
							s = "ship";
							d8 *= 2D;
						}
					} else if (repMod == 2) {
						if (threat instanceof TankGeneric) {
							s = "armor";
						}
					} else if (repMod == 3) {
						if (threat instanceof ArtilleryGeneric) {
							s = "guns";
						}
					} else if (repMod == 4) {
						if (threat instanceof CarGeneric) {
							s = "vehicles";
						}
					} else if (repMod == 5) {
						if (threat instanceof Wagon) {
							s = "train";
						}
					} else if (repMod == 6) {
						if ((threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric)) {
							s = "ship";
							d8 *= 2D;
						}
					}
					double d9 = d3 - d;
					double d10 = d4 - d1;
					float f11 = 57.32484F * (float) Math.atan2(d10, -d9);
					double d11 = Math.floor((int) f11) - 90D;
					if (d11 < 0.0D) {
						d11 = 360D + d11;
					}
					int k = (int) (d11 - i);
					if (k < 0) {
						k = 360 + k;
					}
					int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
					if (l < 1) {
						l = 12;
					}
					double d12 = d - d3;
					double d13 = d1 - d4;
					double d14 = Math.ceil(Math.sqrt((d13 * d13) + (d12 * d12)));
					float angle = GuidedMissileUtils.angleActorBetween(this, threat);
					float distance = (float) GuidedMissileUtils.distanceBetween(this, threat);
					int i4 = (int) (Math.ceil(distance / 100D) * 100D);
					if (d14 <= d8) {
						if (!isAI) {
							HUD.training("Enemy " + s + " at " + l + " o'clock for " + i4 + " meters");
							if(!bManualFire) {
								if (l == 12)
								{
									if(angle < 10F + (FM.Skill * 10F) && distance > 1400F - (FM.Skill * 100) && distance < 2000F + (FM.Skill * 1000)) {
										if (Math.abs(FM.getOverload()) < 1 + FM.Skill) {
											HUD.training("Tracking " + s + " range " + i4 + " meters");
											victim = threat;
											Reflection.setInt(guidedMissileUtils, "engageMode", 1);
											doOperatorLaunchMissile();
										}
									}
								}
							}
						} else if (l == 12)
						{
							if(angle < 10F + (FM.Skill * 10F) && distance > 1400F - (FM.Skill * 100) && distance < 2000F + (FM.Skill * 1000)) {
								victim = threat;
								doLaunchMissileAI();
								threatIsNear = true;
							}
						}
					}
				}
			}
		}
	}

	public GuidedMissileUtils getGuidedMissileUtils() {
		return guidedMissileUtils;
	}

	protected void moveElevator(float f1) {
	}

	protected void moveAileron(float f1) {
	}

	protected void moveFlap(float f) {
	}

	protected void moveRudder(float f) {
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f_1_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -90F);
		float f_2_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -85F);
		float f_3_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 85F);
		float f_4_ = f <= 0.5F ? Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, 70F)
				: Aircraft.cvt(f, 0.8F, 1.0F, 70F, 0.0F);
		float f_5_ = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -70F)
				: Aircraft.cvt(f, 0.8F, 1.0F, -70F, 0.0F);
		float f_6_ = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
		float f_7_ = Aircraft.cvt(f, 0.1F, 0.8F, -7.0F, 90F);
		float f_8_ = Aircraft.cvt(f, 0.2F, 0.9F, 7.0F, -90F);
		float f_9_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -140F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, f_9_);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f_2_, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f_3_, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f_4_, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f_5_, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", f_8_, 7.0F, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", f_7_, -7.0F, 0.0F);
	}

	public void moveWheelSink() {
		resetYPRmodifier();
		hierMesh().chunkSetAngles("GearL22_D0", 0.0F, FM.Gears.gWheelSinking[0] * -90F, 0.0F);
		hierMesh().chunkSetAngles("GearR22_D0", 0.0F, FM.Gears.gWheelSinking[1] * 90F, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	public void moveSteering(float f) {
		float f_1_ = Aircraft.cvt(1.0F, 0.1F, 0.9F, 0.0F, -90F);
		if (FM.CT.GearControl > 0.1F) {
			if (f < -45F)
				hierMesh().chunkSetAngles("GearC2_D0", -45F, 0.0F, f_1_);
			else if (f > 45F) 
				hierMesh().chunkSetAngles("GearC2_D0", 45F, 0.0F, f_1_);
			else
				hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, f_1_);
		} else {
			hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
		}
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
		hierMesh().chunkSetAngles("Door1_D0", -90F * f, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Door2_D0", 0.0F, -65F * f, 0.0F);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	protected void moveBayDoor(float f) {
		hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -120F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 120F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 120F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -120F * f, 0.0F);
	}

	protected void moveFan(float f)
	{
		if (isAI)
		{    
			if(FM.EI.engines[0].getStage() > 5 && FM.EI.engines[1].getStage() > 5)
			{
				hierMesh().chunkVisible("Prop1_D0", false);
				hierMesh().chunkVisible("Prop2_D0", false);
				hierMesh().chunkVisible("PropRot1_D0", true);
				hierMesh().chunkVisible("PropRot2_D0", true);
			} else
			{
				hierMesh().chunkVisible("Prop1_D0", true);
				hierMesh().chunkVisible("Prop2_D0", true);
				hierMesh().chunkVisible("PropRot1_D0", false);
				hierMesh().chunkVisible("PropRot2_D0", false);
			}
		} else {
			if(rotorRPM > 100F)
			{
				hierMesh().chunkVisible("Prop1_D0", false);
				hierMesh().chunkVisible("Prop2_D0", false);
				hierMesh().chunkVisible("PropRot1_D0", true);
				hierMesh().chunkVisible("PropRot2_D0", true);
			} else
			{
				hierMesh().chunkVisible("Prop1_D0", true);
				hierMesh().chunkVisible("Prop2_D0", true);
				hierMesh().chunkVisible("PropRot1_D0", false);
				hierMesh().chunkVisible("PropRot2_D0", false);
			}
		}

		if(hierMesh().isChunkVisible("Prop1_D1"))
		{
			hierMesh().chunkVisible("Prop1_D0", false);
			hierMesh().chunkVisible("PropRot1_D0", false);
		}
		if(hierMesh().isChunkVisible("Prop2_D1"))
		{
			hierMesh().chunkVisible("Prop2_D0", false);
			hierMesh().chunkVisible("PropRot2_D0", false);
		}
		if(hierMesh().isChunkVisible("Tail1_CAP"))
		{
			hierMesh().chunkVisible("Prop2_D0", false);
			hierMesh().chunkVisible("PropRot2_D0", false);
			hierMesh().chunkVisible("Prop2_D1", false);
		}
		long curTime = Time.current();
		diffAngleRotor = (float) ((6D * rotorRPM * (curTime - lastTimeFan)) / 1000D);
		curAngleRotor += diffAngleRotor;
		diffAngleTailRotor = (float) ((6D * tailRotorRPM * (curTime - lastTimeFan)) / 1000D);
		curAngleTailRotor += diffAngleTailRotor;
		lastTimeFan = curTime;
		hierMesh().chunkSetAngles("Prop1_D0", -curAngleRotor % 360F, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, -curAngleTailRotor % 360F);
		if(TailRotorDestroyed)
		{
			hierMesh().chunkVisible("Prop2_D0", false);
			hierMesh().chunkVisible("PropRot2_D0", false);
			hierMesh().chunkVisible("Prop2_D1", true);
		}
	}

	private void tiltRotor(float f) {
		hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F,
				-((FlightModelMain) (super.FM)).CT.getElevator() / 10F);
		hierMesh().chunkSetAngles("AroneL_D0", 0.0F,
				-((FlightModelMain) (super.FM)).CT.getAileron() / 10F, 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d)
	{
		boolean flag1 = this instanceof Mi24X;
		if(s.startsWith("xx"))
		{
			if(s.startsWith("xxarmor"))
			{
				debuggunnery("Armor: Hit..");
				if(s.startsWith("xxarmorp"))
				{
					int i = s.charAt(8) - 48;
					switch(i)
					{
					default:
						break;

					case 1: // '\001'
					getEnergyPastArmor(7.070000171661377D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
					shot.powerType = 0;
					break;

					case 2: // '\002'
					case 3: // '\003'
						getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
						shot.powerType = 0;
						if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
							doRicochet(shot);
						break;

					case 4: // '\004'
						if(((Tuple3d) (point3d)).x > -1.3500000000000001D)
						{
							getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
							shot.powerType = 0;
							if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
								doRicochet(shot);
						} else
						{
							getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
						}
						break;

					case 5: // '\005'
					case 6: // '\006'
						getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
						if(shot.power > 0.0F)
							break;
						if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
							doRicochet(shot);
						else
							doRicochetBack(shot);
						break;

					case 7: // '\007'
						getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
						if(shot.power <= 0.0F)
							doRicochetBack(shot);
						break;
					}
				}
				if(s.startsWith("xxarmorc1"))
					getEnergyPastArmor(7.070000171661377D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
				if(s.startsWith("xxarmort1"))
					getEnergyPastArmor(6.059999942779541D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
				return;
			}
			if(s.startsWith("xxspar"))
			{
				debuggunnery("Spar Construction: Hit..");
				if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.10000000149011612D && getEnergyPastArmor(3.4000000953674316D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
				{
					debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
				}
				if(s.startsWith("xxsparlm"))
					if(flag1)
					{
						if(chunkDamageVisible("WingLMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
							nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
						}
					} else
						if(chunkDamageVisible("WingLMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
							nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
						}
				if(s.startsWith("xxsparrm"))
					if(flag1)
					{
						if(chunkDamageVisible("WingRMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
							nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
						}
					} else
						if(chunkDamageVisible("WingRMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
							nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
						}
				if(s.startsWith("xxsparlo"))
					if(flag1)
					{
						if(chunkDamageVisible("WingLOut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
							nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
						}
					} else
						if(chunkDamageVisible("WingLOut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
							nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
						}
				if(s.startsWith("xxsparro"))
					if(flag1)
					{
						if(chunkDamageVisible("WingROut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
							nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
						}
					} else
						if(chunkDamageVisible("WingROut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
						{
							debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
							nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
						}
				if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
				{
					debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
				}
				if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
				{
					debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
				}
				if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y + ((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
				{
					debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
			}
			if(s.startsWith("xxlock"))
			{
				debuggunnery("Lock Construction: Hit..");
				if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
					nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
				}
				if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
				}
				if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Lock Construction: AroneL Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
				}
				if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
				}
			}
			if(s.startsWith("xxeng"))
			{
				debuggunnery("Engine Module: Hit..");
				if(s.endsWith("prop"))
				{
					if(getEnergyPastArmor(3.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
						if(World.Rnd().nextFloat() < 0.5F)
						{
							debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
							((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 3);
						} else
						{
							debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
							((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 4);
						}
				} else
					if(s.endsWith("gear"))
					{
						if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
						{
							debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
							((FlightModelMain) (super.FM)).EI.engines[0].setEngineStuck(shot.initiator);
						}
					} else
						if(s.endsWith("supc"))
						{
							if(getEnergyPastArmor(0.01F, shot) > 0.0F)
							{
								debuggunnery("Engine Module: Supercharger Disabled..");
								((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 0);
							}
						} else
							if(s.endsWith("feed"))
							{
								if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
								{
									if(World.Rnd().nextFloat() < 0.1F)
									{
										debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
										((FlightModelMain) (super.FM)).EI.engines[0].setEngineStops(shot.initiator);
									}
									if(World.Rnd().nextFloat() < 0.05F)
									{
										debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
										((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
									}
									if(World.Rnd().nextFloat() < 0.1F)
									{
										debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
										((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
									}
								}
							} else
								if(s.endsWith("fue1"))
								{
									if(getEnergyPastArmor(0.89F, shot) > 0.0F)
									{
										debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
										((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 100);
									}
								} else
									if(s.endsWith("case"))
									{
										if(getEnergyPastArmor(2.2F, shot) > 0.0F)
										{
											if(World.Rnd().nextFloat() < shot.power / 175000F)
											{
												debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
												((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
											}
											if(World.Rnd().nextFloat() < shot.power / 50000F)
											{
												debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
												((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
											}
										}
										((FlightModelMain) (super.FM)).EI.engines[0].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
										debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
										getEnergyPastArmor(22.5F, shot);
									} else
										if(s.endsWith("cyl1"))
										{
											if(getEnergyPastArmor(1.3F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 1.75F)
											{
												((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
												debuggunnery("Engine Module: Cylinders Assembly Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Operating..");
												if(World.Rnd().nextFloat() < shot.power / 48000F)
												{
													debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
													((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
												}
												if(World.Rnd().nextFloat() < 0.01F)
												{
													debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
													((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
												}
												getEnergyPastArmor(22.5F, shot);
											}
											if(Math.abs(((Tuple3d) (point3d)).y) < 0.1379999965429306D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
											{
												if(World.Rnd().nextFloat() < 0.1F)
												{
													debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
													((FlightModelMain) (super.FM)).EI.engines[0].setEngineStops(shot.initiator);
												}
												if(World.Rnd().nextFloat() < 0.05F)
												{
													debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
													((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
												}
												if(World.Rnd().nextFloat() < 0.1F)
												{
													debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
													((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
												}
											}
										} else
											if(s.startsWith("xxeng1mag"))
											{
												int j = s.charAt(9) - 49;
												debuggunnery("Engine Module: Magneto " + j + " Hit, Magneto " + j + " Disabled..");
												((FlightModelMain) (super.FM)).EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
											} else
												if(s.startsWith("xxeng1oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
												{
													debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
													((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
												}
			}
			if(s.startsWith("xxw1"))
				if(((FlightModelMain) (super.FM)).AS.astateEngineStates[0] == 0)
				{
					debuggunnery("Engine Module: Water Radiator Pierced..");
					((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
					((FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 1);
				} else
					if(((FlightModelMain) (super.FM)).AS.astateEngineStates[0] == 1)
					{
						debuggunnery("Engine Module: Water Radiator Pierced..");
						((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
						((FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 2);
					}
			if(s.startsWith("xxtank"))
			{
				int k = s.charAt(6) - 49;
				if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
				{
					if(((FlightModelMain) (super.FM)).AS.astateTankStates[k] == 0)
					{
						debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
						((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
						((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, k, 1);
					} else
						if(((FlightModelMain) (super.FM)).AS.astateTankStates[k] == 1)
						{
							debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
							((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
							((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, k, 2);
						}
					if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
					{
						((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 2);
						debuggunnery("Fuel System: Fuel Tank " + k + " Pierced, State Shifted..");
					}
				}
			}
			if(s.startsWith("xxmgun"))
			{
				if(s.endsWith("01"))
				{
					debuggunnery("Armament System: Left Machine Gun: Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
				}
				if(s.endsWith("02"))
				{
					debuggunnery("Armament System: Right Machine Gun: Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
				}
				getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
			}
			if(s.startsWith("xxcannon"))
			{
				if(s.endsWith("01") && getEnergyPastArmor(0.25F, shot) > 0.0F)
				{
					debuggunnery("Armament System: Left Cannon: Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
				}
				if(s.endsWith("02") && getEnergyPastArmor(0.25F, shot) > 0.0F)
				{
					debuggunnery("Armament System: Right Cannon: Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
				}
				getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
			}
			if(s.startsWith("xxammo"))
			{
				if(s.startsWith("xxammol1") && World.Rnd().nextFloat() < 0.023F)
				{
					debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
				}
				if(s.startsWith("xxammor1") && World.Rnd().nextFloat() < 0.023F)
				{
					debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
				}
				if(s.startsWith("xxammol2") && World.Rnd().nextFloat() < 0.023F)
				{
					debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
				}
				if(s.startsWith("xxammor2") && World.Rnd().nextFloat() < 0.023F)
				{
					debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
					((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
				}
				getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
			}
			if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.00345F && ((FlightModelMain) (super.FM)).CT.Weapons[3] != null && ((FlightModelMain) (super.FM)).CT.Weapons[3][0].haveBullets())
			{
				debuggunnery("Armament System: Bomb Payload Detonated..");
				((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 10);
				((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 10);
				nextDMGLevels(3, 2, "CF_D0", shot.initiator);
			}
			if(s.startsWith("xxpnm") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
			{
				debuggunnery("Pneumo System: Disabled..");
				((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
			}
			if(s.startsWith("xxhyd") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
			{
				debuggunnery("Hydro System: Disabled..");
				((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
			}
			if(s.startsWith("xxins"))
				((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
			return;
		}
		if(s.startsWith("xcockpit") || s.startsWith("xblister"))
			if(((Tuple3d) (point3d)).z > 0.47299999999999998D)
				((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
			else
				if(((Tuple3d) (point3d)).y > 0.0D)
					((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
				else
					((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
		if(s.startsWith("xcf"))
		{
			if(((Tuple3d) (point3d)).x < -1.9399999999999999D)
			{
				if(chunkDamageVisible("Tail1") < 3)
					hitChunk("Tail1", shot);
			} else
			{
				if(((Tuple3d) (point3d)).x <= 1.3420000000000001D)
					if(((Tuple3d) (point3d)).z < -0.59099999999999997D || ((Tuple3d) (point3d)).z > 0.40799999237060547D && ((Tuple3d) (point3d)).x > 0.0D)
					{
						getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
						if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
							doRicochet(shot);
					} else
					{
						getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
						if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
							doRicochet(shot);
					}
				if(chunkDamageVisible("CF") < 3)
					hitChunk("CF", shot);
			}
		} else
			if(s.startsWith("xoil"))
			{
				if(((Tuple3d) (point3d)).z < -0.98099999999999998D)
				{
					getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
					if(shot.power <= 0.0F)
						doRicochet(shot);
				} else
					if(((Tuple3d) (point3d)).x > 0.53700000000000003D || ((Tuple3d) (point3d)).x < -0.10000000000000001D)
					{
						getEnergyPastArmor(0.20000000298023224D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
						if(shot.power <= 0.0F)
							doRicochetBack(shot);
					} else
					{
						getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
						if(shot.power <= 0.0F)
							doRicochet(shot);
					}
				if(chunkDamageVisible("CF") < 3)
					hitChunk("CF", shot);
			} else
				if(s.startsWith("xeng"))
				{
					if(((Tuple3d) (point3d)).z > 0.159D)
						getEnergyPastArmor((double)(1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
					else
						if(((Tuple3d) (point3d)).x > 1.335D && ((Tuple3d) (point3d)).x < 2.3860000000000001D && ((Tuple3d) (point3d)).z > -0.059999999999999998D && ((Tuple3d) (point3d)).z < 0.064000000000000001D)
							getEnergyPastArmor(0.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
						else
							if(((Tuple3d) (point3d)).x > 2.5299999999999998D && ((Tuple3d) (point3d)).x < 2.992D && ((Tuple3d) (point3d)).z > -0.23499999999999999D && ((Tuple3d) (point3d)).z < 0.010999999999999999D)
								getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
							else
								if(((Tuple3d) (point3d)).x > 2.5590000000000002D && ((Tuple3d) (point3d)).z < -0.59499999999999997D)
									getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
								else
									if(((Tuple3d) (point3d)).x > 1.849D && ((Tuple3d) (point3d)).x < 2.2509999999999999D && ((Tuple3d) (point3d)).z < -0.70999999999999996D)
										getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
									else
										if(((Tuple3d) (point3d)).x > 3.0030000000000001D)
											getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
										else
											if(((Tuple3d) (point3d)).z < -0.60600000619888306D)
												getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
											else
												getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
					if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
						doRicochet(shot);
					if(chunkDamageVisible("Engine1") < 2)
						hitChunk("Engine1", shot);
				} else
					if(s.startsWith("xtail"))
						hitChunk("Tail1", shot);
					else
						if(s.startsWith("xkeel"))
						{
							if(chunkDamageVisible("Keel1") < 2) {
								hitChunk("Keel1", shot);
								TailRotorDamage();
							}
						} else
							if(s.startsWith("xrudder") && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
							{
								if(chunkDamageVisible("Rudder1") < 1)
									hitChunk("Rudder1", shot);
							} else
								if(s.startsWith("xstab"))
								{
									if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
										hitChunk("StabL", shot);
									if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
										hitChunk("StabR", shot);
								} else
									if(s.startsWith("xvator"))
									{
										if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
											hitChunk("VatorL", shot);
										if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
											hitChunk("VatorR", shot);
									} else
										if(s.startsWith("xwing"))
										{
											if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
												hitChunk("WingLIn", shot);
											if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
												hitChunk("WingRIn", shot);
											if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
												hitChunk("WingLMid", shot);
											if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
												hitChunk("WingRMid", shot);
											if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
												hitChunk("WingLOut", shot);
											if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
												hitChunk("WingROut", shot);
										} else
											if(s.startsWith("xarone"))
											{
												if(s.startsWith("xaronel"))
													hitChunk("AroneL", shot);
												if(s.startsWith("xaroner"))
													hitChunk("AroneR", shot);
											} else
												if(s.startsWith("xgear"))
												{
													if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
													{
														debuggunnery("Hydro System: Disabled..");
														((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
													}
													if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
													{
														debuggunnery("Undercarriage: Stuck..");
														((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
													}
												} else
													if(s.startsWith("xturret"))
													{
														if(getEnergyPastArmor(0.25F, shot) > 0.0F)
														{
															debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
															((FlightModelMain) (super.FM)).AS.setJamBullets(10, 0);
															((FlightModelMain) (super.FM)).AS.setJamBullets(10, 1);
															getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
														}
													} else
														if(s.startsWith("xhelm"))
														{
															getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
															if(shot.power <= 0.0F)
																doRicochetBack(shot);
														} else
															if(s.startsWith("xpilot") || s.startsWith("xhead"))
															{
																byte byte0 = 0;
																int l;
																if(s.endsWith("a"))
																{
																	byte0 = 1;
																	l = s.charAt(6) - 49;
																} else
																	if(s.endsWith("b"))
																	{
																		byte0 = 2;
																		l = s.charAt(6) - 49;
																	} else
																	{
																		l = s.charAt(5) - 49;
																	}
																hitFlesh(l, shot, byte0);
															}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			// fall through

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("Head2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			bObserverKilled = true;
			// fall through

		default:
			return;
		}
	}

	private void TailRotorDamage() {
		int rnd = TrueRandom.nextInt(50);
		if(!TailRotorDestroyed && rnd == 1)
		{
			if (World.getPlayerAircraft() == this) {
				HUD.log(AircraftHotKeys.hudLogWeaponId, "Tail Rotor: Damaged!");
			}
			TailRotorDestroyed = true;
		}
	}
	
	//TODO: Below method is used to calculate precise AI movement during take off or landing and other operations; 
	// double v is used to declare speed to which AI should accelerate or brake;
	// double a is used to set the desired altitude at which AI should ascend or descend;
	// double d is used to set a distance during which all the above operations should happen.
	// float f is should be always set to the update(f); method float f to keep the correct speed increment. 
	
	public void blyat(double v, double a, double d, float f) {
		v = v / 3.6D;
//		double v1 = Math.abs(FM.Vwld.z - tV[0].z);
		if (v < this.tV[0].x) {
			this.tV[0].x -= ((this.tV[0].x - v) / d);
		} else {
			this.tV[0].x += ((v - this.tV[0].x) / d);
		}
		if(a < this.FM.Loc.z) {
			this.tV[0].z = -(this.tV[0].x * (a - this.FM.Loc.z) / d);
		} else {
			this.tV[0].z = (this.tV[0].x * (a - this.FM.Loc.z) / d);
		}
//		tV[0].x += v1;
		tV[1].interpolate(tV[0], 0.5F * f);
		Vector3d v3d = new Vector3d(tV[1]);
		this.pos.getAbsOrient().transform(v3d);
		this.FM.Vwld.set(v3d);
		FM.Vwld.z = tV[1].z;
		this.FM.Loc.x += FM.Vwld.x * (double) f;
		this.FM.Loc.y += FM.Vwld.y * (double) f;
		this.FM.Loc.z += FM.Vwld.z * (double) f;
	}
	
	//TODO: Stability method is used to calculate AI flight behavior. 	
	
	private void stability(float f) { 
		float avT = (FM.EI.engines[0].getControlThrottle() + FM.EI.engines[1].getControlThrottle()) / 2F;
        float alt = FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
//        if ((alt < 10F) && (this.FM.getSpeedKMH() < 60F) && (vector3d.z < -1D)) {
//            vector3d.z *= 0.9D;
//            this.setSpeed(vector3d);
//        }

        
        
        
        
		if(TailRotorDestroyed) this.FM.producedAM.z += 100000;
		this.pos.getAbs(localPoint3d1);
		Vector3d localVector3d = new Vector3d(FM.Vwld);


		FM.SensYaw = 0.5F; 
		FM.SensPitch = 0.3F;
		FM.SensRoll = 0.3F;
		if(((Maneuver) FM).get_maneuver() == 26 && alt < 10F && Time.current() < asTimer + 2500L) takeOff = true;
		if (takeOff){
			switch (takeOffStep) {
			case 0:
			{		
				if(FM.CT.PowerControl > 0.9 && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getStage() == 6 && FM.CT.cockpitDoorControl < 0.9) {
					Vector3d tmpV1 = new Vector3d();
					tmpV1.set(1.0D, 0.0D, 0.0D);
					FM.Or.transform(tmpV1);
					tmpV1.scale(20D);
					tP[0].set(FM.Loc);
					tP[0].add(tmpV1);
					tP[0].z += 10D;
					tmpV1.set(1.0D, 0.0D, 0.0D);
					FM.Or.transform(tmpV1);
					tmpV1.scale(70D);
					tP[1].set(FM.Loc);
					tP[1].add(tmpV1);
					tP[1].z += 20D;
					tmpV1.set(1.0D, 0.0D, 0.0D);
					FM.Or.transform(tmpV1);
					tmpV1.scale(500D);
					tP[2].set(FM.Loc);
					tP[2].add(tmpV1);
					tP[2].z += 30D;
					tOr.set(FM.Or);
					takeOffStep++;
				}
				break;
			}
			case 1:
			{
				Reflection.setBoolean(((Maneuver) FM), "callSuperUpdate", false);
				((Maneuver) FM).set_maneuver(66);
				((Maneuver) FM).setSpeedMode(11);
				FM.Vwld.x = 0D;
				FM.Vwld.y = 0D;
				FM.Vwld.z = 0.4D;
				FM.Loc.x += FM.Vwld.x * (double) f;
				FM.Loc.y += FM.Vwld.y * (double) f;
				FM.Loc.z += FM.Vwld.z * (double) f;
				tOr.setYPR(tOr.getYaw(), 0.0F, tOr.getRoll());
				FM.Or.interpolate(tOr, 0.2F * f);
				if (alt > 3.5F) takeOffStep++;
				break;
			}
			case 2:
			{
				Reflection.setBoolean(((Maneuver) FM), "callSuperUpdate", false);
				((Maneuver) FM).set_maneuver(66);
				((Maneuver) FM).setSpeedMode(11);
				tOr.setYPR(tOr.getYaw(), -4.0F, tOr.getRoll());
				FM.Or.interpolate(tOr, 0.2F * f);
				blyat(7, tP[0].z, pos.getAbsPoint().distance(tP[0]), f);
				if (pos.getAbsPoint().distance(tP[0]) < 3D || alt > 10) takeOffStep++;
				break;
			}
			case 3:
			{
				Reflection.setBoolean(((Maneuver) FM), "callSuperUpdate", false);
				((Maneuver) FM).set_maneuver(66);
				((Maneuver) FM).setSpeedMode(11);
				tOr.setYPR(tOr.getYaw(), -7.0F, tOr.getRoll());
				FM.Or.interpolate(tOr, 0.2F * f);
				blyat(40, tP[1].z, pos.getAbsPoint().distance(tP[1]), f);
				if (pos.getAbsPoint().distance(tP[1]) < 5 || alt > 20) takeOffStep++;
				break;
			}
			case 4:
			{
				Reflection.setBoolean(((Maneuver) FM), "callSuperUpdate", false);
				((Maneuver) FM).set_maneuver(66);
				((Maneuver) FM).setSpeedMode(11);
				tOr.setYPR(tOr.getYaw(), -4.0F, tOr.getRoll());
				FM.Or.interpolate(tOr, 0.1F * f);
				blyat(200, tP[2].z, pos.getAbsPoint().distance(tP[2]), f);
				if (pos.getAbsPoint().distance(tP[2]) < 20 || FM.getSpeedKMH() > 195F) {
					Reflection.setBoolean(((Maneuver) FM), "callSuperUpdate", true);
					((com.maddox.il2.ai.air.Maneuver) FM).unblock();
					takeOff = false;
				}
				break;
			}
			}
		}
		if (((Maneuver) FM).get_maneuver() == 25) 
		{
			landing = true;
		}
		if (landing) 
		{
			if (alt <= 17 && alt > 7)
			{
				FM.setCapableOfTaxiing(false);
				((Maneuver) FM).set_maneuver(66);
				FM.CT.ElevatorControl = 0.8F;
				localVector3d.x *= 0.995;
				localVector3d.y *= 0.995;
				localVector3d.z *= 0.1;
			}		
			if (alt <= 7)
			{
				((Maneuver) FM).setSpeedMode(8);	
				localVector3d.x *= 0.97;
				localVector3d.y *= 0.97;
				localVector3d.z *= 0.4;
				if (FM.Gears.nOfGearsOnGr > 2)
				{
					((Maneuver) FM).set_maneuver(66);
					FM.CT.BrakeControl = 1.0F;
					FM.EI.setEngineStops();
					MsgDestroy.Post(Time.current() + 12000L, this);
				}
			}
			setSpeed(localVector3d);
		}
		if (FM.getSpeedKMH() > 0.1F && !takeOff && !landing && !(((Maneuver) FM).get_maneuver() == 44) && !(((Maneuver) FM).get_maneuver() == 49) && !FM.isReadyToDie() && !FM.isTakenMortalDamage()) 
		{
	        Point3d point3d1 = new Point3d(0.0D, 0.0D, 0.0D);
	        point3d1.x = 0.0D - ((this.FM.Or.getTangage() / 10F) - (this.FM.CT.getElevator() * 2.5D));
	        point3d1.y = 0.0D - ((this.FM.Or.getKren() / 10F) - (this.FM.CT.getAileron() * 2.5D));
	        point3d1.z = 2D;
	        this.FM.EI.engines[0].setPropPos(point3d1);
	        this.FM.EI.engines[1].setPropPos(point3d1);
	        this.FM.producedAF.x += 15000D * avT;
	        this.FM.producedAF.y += 6000D * (-this.FM.CT.getAileron() * avT);
	        this.FM.producedAF.z += avT * 70000F;
	        float f1 = avT * Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 300F, 1.0F, 0.0F);
	        this.FM.Or.increment(f1 * (this.FM.CT.getRudder() + this.FM.CT.getTrimRudderControl()), f1 * (this.FM.CT.getElevator() + this.FM.CT.getTrimElevatorControl()), f1 * (this.FM.CT.getAileron() + this.FM.CT.getTrimAileronControl()));
			FM.Sq.dragParasiteCx += cvt(FM.getSpeedKMH(), 290F, 300F, 0.0F, 1.0F);
			if (FM.getVertSpeed() > 2.0) FM.CT.ElevatorControl = (FM.getSpeedKMH() - 180F - FM.Or.getTangage() * 10F - FM.getVertSpeed() * 5F) * 0.004F;
		}
		engineRPM = Math.sqrt((Math.pow(FM.EI.engines[0].getRPM(), 2D) + Math.pow(FM.EI.engines[1].getRPM(), 2D)) / 2D);
		reductorRPM = engineRPM * 0.016D;
		tailRotorRPM = engineRPM * 0.07413D;
		rotorRPM = reductorRPM;
	}
	
//	private void stability() { 
//		if(TailRotorDestroyed) this.FM.producedAM.z += 100000;
//		Vector3f eVect = new Vector3f();
//		eVect.x = 1.0F;
//		eVect.y = 0.0F;
//		eVect.z = 0.0F;
//		FM.EI.engines[0].setVector(eVect);
//		FM.EI.engines[1].setVector(eVect);
//
//		this.pos.getAbs(localPoint3d1);
//		Vector3d localVector3d = new Vector3d();
//		getSpeed(localVector3d);
//		float wfZ = 20000F;
//		float wfX = 5000F;
//		float afZ = 0.0F;
//		float afX = 0.0F;
//		float avT = (FM.EI.engines[0].getThrustOutput() + FM.EI.engines[1].getThrustOutput()) / 2F;
//		float alt = FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
//		Polares polares = (Polares)Reflection.getValue(FM, "Wing");
//		polares.Cy0_0 = cvt(alt, 30F, 50F, 5.5F, 0.55F);
//		polares.lineCyCoeff = cvt(alt, 30F, 50F, 0.29F, 0.19F);
//		polares.CyCritH_0 = cvt(alt, 30F, 50F, 5.9934692F, 1.1F);
//		FM.SensYaw = 0.5F; 
//		FM.SensPitch = 0.5F;
//		FM.SensRoll = 0.5F;
//
//		if (!tookOff) {
//			if (((com.maddox.il2.ai.air.Maneuver) FM).get_maneuver() == 26 && FM.getSpeed() > 0.1F ) 
//			{
//				wfZ = 0;
//				wfX = 0;
//				afZ = 150000;
//				afX = 0;
//				float altlim = 10.0F;
//				if (FM.Gears.nOfGearsOnGr > 2) {
//					gndYaw = FM.Or.getYaw();
//					gndRoll = FM.Or.getRoll();
//					gndPitch = FM.Or.getPitch();
//				}
//				FM.Or.set(-gndYaw, cvt(alt, 0.0F, altlim, gndPitch, gndPitch - 8.0F), gndRoll);
//				if (!hover)
//				{
//					float a = 0.0F;
//					if (alt < 7)
//						a = 7.0F;
//					if (alt > 7)
//						a = altlim;
//					float vertSpd = cvt(alt, 0.0F, a, 0.98F, 0.9F);
//					localVector3d.z *= vertSpd + World.cur().rnd.nextDouble(-0.05F, 0.05F);
//					localVector3d.x *= 0.9 + World.cur().rnd.nextDouble(-0.05F, 0.05F);
//					localVector3d.y *= 0.9 + World.cur().rnd.nextDouble(-0.05F, 0.05F);
//					setSpeed(localVector3d);
//					if (alt > altlim){
//						hover = true;
//					}
//				}
//				if (hover)
//				{
//					if (FM.getVertSpeed() < 0.0F) 
//					{
//						localVector3d.z = 0.0;
//						setSpeed(localVector3d);
//					}
//					if (FM.getVertSpeed() > 0.1F)
//					{
//						float verSPDlimit = cvt(this.FM.getVertSpeed(), 0.0F, 3.0F, 1.0F, 0.85F);
//						localVector3d.z *= verSPDlimit;
//						setSpeed(localVector3d);
//					}
//					if (FM.getSpeedKMH() > 80F) {
//						afZ = 0.0F;
//						afX = 0.0F;
//						((com.maddox.il2.ai.air.Maneuver) FM).unblock();
//						tookOff = true;
//						hover = false;
//					}
//				}
//			}
//		}
//		if (((com.maddox.il2.ai.air.Maneuver) FM).get_maneuver() == 25) 
//		{
//			landing = true;
//		}
//		if (landing) 
//		{
//			if (alt <= 17 && alt > 7)
//			{
//				FM.setCapableOfTaxiing(false);
//				((com.maddox.il2.ai.air.Maneuver) FM).set_maneuver(66);
//				FM.CT.ElevatorControl = 0.8F;
//				localVector3d.x *= 0.995;
//				localVector3d.y *= 0.995;
//				localVector3d.z *= 0.1;
//			}		
//			if (alt <= 7)
//			{
//				((com.maddox.il2.ai.air.Maneuver) FM).setSpeedMode(8);	
//				localVector3d.x *= 0.97;
//				localVector3d.y *= 0.97;
//				localVector3d.z *= 0.4;
//				if (FM.Gears.nOfGearsOnGr > 2)
//				{
//					((com.maddox.il2.ai.air.Maneuver) FM).set_maneuver(66);
//					FM.CT.BrakeControl = 1.0F;
//					FM.EI.setEngineStops();
//					MsgDestroy.Post(Time.current() + 12000L, this);
//				}
//			}
//			setSpeed(localVector3d);
//		}
//		if (FM.getSpeedKMH() > 0.1F && !landing && !(((Maneuver) FM).get_maneuver() == 44) && !(((Maneuver) FM).get_maneuver() == 49) && !FM.isReadyToDie() && !FM.isTakenMortalDamage()) {
//			FM.producedAF.z += avT * cvt(alt, 30F, 50F, 30000F, 0F) + afZ + avT * wfZ;
//			FM.producedAF.x += avT * cvt(alt, 30F, 50F, 5000F, 0F) + afX + avT * wfX;
//			if (FM.getSpeedKMH() >= 310)
//				FM.Sq.dragParasiteCx += 0.25F;
//			FM.Vwld.z *= FM.getVertSpeed() > 0.0 ? cvt((float) FM.getVertSpeed(), 1.0F, 10.0F, 1.0F, 0.95F) : 1.0;
//		}
//		engineRPM = Math.sqrt((Math.pow(FM.EI.engines[0].getRPM(), 2D) + Math.pow(FM.EI.engines[1].getRPM(), 2D)) / 2D);
//		reductorRPM = engineRPM * 0.016D;
//		tailRotorRPM = engineRPM * 0.07413D;
//		rotorRPM = reductorRPM;
//	}

	public void human(float f) {

		if (FM.EI.getCurControl(0) && !FM.EI.getCurControl(1)) FM.EI.engines[1].setControlProp(FM.EI.engines[0].getControlProp());
		if (FM.EI.getCurControl(1) && !FM.EI.getCurControl(0)) FM.EI.engines[0].setControlProp(FM.EI.engines[1].getControlProp());

		if(bAPkrentang) {
			if (FM.CT.getElevator() > 0.07F || FM.CT.getElevator() < -0.07F) apTang = -FM.Or.getTangage();
			if (FM.CT.getAileron() > 0.07F || FM.CT.getAileron() < -0.07F) apKren = -FM.Or.getKren();
			FM.CT.setTrimElevatorControl(cvt(-FM.Or.getTangage(), apTang - 7F, apTang + 7F, -0.2F, 0.2F));
			FM.CT.setTrimAileronControl(cvt(-FM.Or.getKren(), apKren - 15F, apKren + 15F, -0.2F, 0.2F));
		} else {
			FM.CT.setTrimElevatorControl(0.0F);
			FM.CT.setTrimAileronControl(0.0F);
		}
		float aileT = FM.CT.getAileron() + (float)forceTrim_x;
		float aile = aileT + FM.CT.getTrimAileronControl();
		if(aile > 1.0F)
			aile = 1.0F;
		if(aile < -1F)
			aile = -1F;
		float elevT = FM.CT.getElevator() + (float)forceTrim_y;
		float elev = elevT + FM.CT.getTrimElevatorControl();
		if(elev > 1.0F)
			elev = 1.0F;
		if(elev < -1F)
			elev = -1F;

		if(bAPazimut) {
			if (FM.CT.getRudder() > 0.07F || FM.CT.getRudder() < -0.07F) apAzimut = -FM.Or.getAzimut();
			FM.CT.setTrimRudderControl(cvt(-FM.Or.getAzimut(), apAzimut - 10F, apAzimut + 10F, -0.2F, 0.2F));
		} else {
			FM.CT.setTrimRudderControl(0.0F);
		}

		float ruddT = FM.CT.getRudder() +  + (float)forceTrim_z;
		float rudd = ruddT + FM.CT.getTrimRudderControl();
		if(rudd > 1.0F)
			rudd = 1.0F;
		if(rudd < -1F)
			rudd = -1F;
		if(getTrim)
		{
			forceTrim_x = aileT;
			forceTrim_y = elevT;
			forceTrim_z = ruddT;
			getTrim = false;
		}
		double Wx = FM.getW().x;
		double Wy = FM.getW().y;
		double Wz = FM.getW().z;
		if(bAPalt) {
			apALltPitch = (cvt(FM.getAltitude(), apAlt - 20F, apAlt + 20F, 0.2F, -0.2F));
		} else {
			apALltPitch = 0.0F;
		}
		float inPitch = (FM.EI.engines[0].getControlProp() + FM.EI.engines[1].getControlProp()) / 2.0F;
		float aPitchT = aOldPitch;
		if (aPitchT < inPitch) aPitchT += (0.007F - (rotorRPM / 120000D));
		if (aPitchT > inPitch) aPitchT -= (0.007F - (rotorRPM / 120000D));
		aOldPitch = aPitchT;
		aPitch = aPitchT + apALltPitch;
		if(aPitch > 1.0F) aPitch = 1.0F;
		Vector3d vFlow = super.FM.getVflow();
		double sinkRate = ((Tuple3d) (vFlow)).z;
		float falt = (float)(FM.getAltitude() - Landscape.HQ_Air((float) FM.Loc.x, (float) FM.Loc.y));
		float airDensity = Atmosphere.density((float)((Tuple3d) (FM.Loc)).z);
		double rotorSurface = 20D;
		double rotorSurface_cyclic = 10D;
		double tailRotorSurface = 1.3500000000000001D;
		double rotorCy = 1.3D;
		double rotorCx = 0.02D;
		double rotorLineCx = 0.00075 * (double)(aPitch * aPitch * 12F * 12F);
		double tailRotorLineCx = 0.00075D * (double)(rudd * rudd * 10F * 10F);
		double spdFlowCoef_0 = (double) cvt((float) vFlow.x, 5.5F, 22.2F, 1.0F, 1.3F);
		double altFlowCoef_1 = (double) cvt(falt, 2.0F, 12F, cvt((float) vFlow.x, 5.5F, 22.2F, 1.2F, 1.0F), 1.0F);
		double rotorCyDyn_0 = 0.0198D * altFlowCoef_1 * spdFlowCoef_0 * airDensity; //0.022D
		double rotorCyDyn_line = 0.0765D * altFlowCoef_1 * spdFlowCoef_0 * airDensity; //0.085D
		if (sinkRate < 0.0D && Math.abs(vFlow.x) < Math.abs(sinkRate)) {
			if (sinkRate < 3.0D) {
				((RealFlightModel)FM).producedShakeLevel += 0.005F * Math.abs(sinkRate);
				rotorCyDyn_0 -= Math.abs(sinkRate) * 0.001D;
				rotorCyDyn_line -= Math.abs(sinkRate) * 0.001D;
			}
		}
		double rotorDiameter = 17.3D;
		double tailRotorDiameter = 3.0D;
		float fAOA = this.FM.getAOA();
		float fuselageCxS = (5 - cvt((float) vFlow.x, 41.6F, 83.3F, 0F, 4.0F));
		float fuselageCyS = 20;
		double load = (FM.M.getFullMass() - FM.M.massEmpty) * airDensity;
		double bladeAOACx = aPitch > 0.7D - (load / 27000D) ? (load / 50000D) * ((aPitch - (0.7D  - (load / 27000D))) / (0.3D + (load / 27000))) * (rotorRPM / 240D) : 0D;
		double bladeCx = FM.Or.getTangage() > 0.0 ? ((vFlow.x + vFlow.z) * Math.abs(FM.Or.getTangage())) * 0.0001D : -(((vFlow.x + vFlow.z) * Math.abs(FM.Or.getTangage()) * 0.0001D) / 3);
		float flowRPM = cvt((float) vFlow.x, 0.0F, 27F, 0.0F, (float) ((200D - (double) (Math.abs(fAOA) * 3D)) - (sinkRate > 0D ? sinkRate * 12D : 0D)));
		engineRPM = Math.sqrt((Math.pow(FM.EI.engines[0].getRPM(), 2D) + Math.pow(FM.EI.engines[1].getRPM(), 2D)) / 2D);
		reductorRPM = engineRPM * 0.016D > flowRPM ? engineRPM * 0.016D > 228 ? 228 : engineRPM * 0.016D : flowRPM;
		//        HUD.training("flowRPM " + flowRPM);
		double tempRPM = reductorRPM * ((1.0D + bladeCx) - bladeAOACx);
		rotorRPM += rotorRPM < tempRPM ? 0.005 + (reductorRPM * 0.0007D) : -(0.005 + (reductorRPM * 0.0007D));
		if (rotorRPM > reductorRPM && rotorRPM < 240) {
			rotorRPM = reductorRPM;
		}
		double hubDirection_x = Math.toRadians(0.0D);
		double hubDirection_y = Math.toRadians(5D);
		double rotorHeight = 2D;
		double rotorSpeed = 6.2831853071795862D * (rotorDiameter / 2D) * 0.5D * (rotorRPM / 60D);
		double autoPitch = PitchAuto(-Wy);
		double autoRoll = RollAuto(Wx);
		double d_hubDirection_x = Math.toRadians(-((double)aile + autoRoll) * 2D);
		double d_hubDirection_y = Math.toRadians(((double)elev + autoPitch) * 5D);
		double rotorLift_dyn = 0.5D * (rotorCyDyn_0 + rotorCyDyn_line * 12D * (double)aPitch) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
		double rotorLift_moment_z = 0.5D * (rotorCx + rotorLineCx * 1.2D * (double)aPitch) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
		double rotorLift_moment_y = -(rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 5D * ((double)elev + autoPitch)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
		double rotorLift_moment_x = (rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 3D * ((double)aile + autoRoll)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
		rotorLift_moment_y += rotorLift_dyn * (rotorHeight * Math.sin(d_hubDirection_y));
		rotorLift_moment_x += rotorLift_dyn * (rotorHeight * Math.sin(d_hubDirection_x));  
		tailRotorRPM = engineRPM * 0.07413D;
		double tailRotorSpeed = 6.2831853071795862D * (tailRotorDiameter / 2D) * 0.5000000000000003D * (tailRotorRPM / 60D);
		double tailRotorLift_dyn = 0.5D * (rotorCyDyn_line * 10D * (double)rudd) * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
		double tailRotorLift_moment_y = 0.5D * (tailRotorDiameter / 2D) * 0.5000000000000003D * (rotorCx + tailRotorLineCx) * tailRotorSurface * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
		double tailRotorLift_moment_z = tailRotorLift_dyn * 10D;
		double rotateSpeed_z = Wz * (rotorDiameter / 2D) * 0.5D;
		double rotateSpeed_y = Wy * (rotorDiameter / 2D) * 0.5D;
		double rotateSpeed_x = Wx * (rotorDiameter / 2D) * 0.5D;
		double balanceMoment_x = (rotorDiameter / 2D) * 0.66000000000000003D * rotateSpeed_x * rotateSpeed_x * rotorSurface * (double)airDensity * rotorCy * 0.5D;
		if(rotateSpeed_x < 0.0D)
			balanceMoment_x = 0.0D - balanceMoment_x;
		double balanceMoment_y = (rotorDiameter / 2D) * 0.66000000000000003D * rotateSpeed_y * rotateSpeed_y * rotorSurface * (double)airDensity * rotorCy * 0.5D;
		if(rotateSpeed_y < 0.0D)
			balanceMoment_y = 0.0D - balanceMoment_y;
		double balanceMoment_z = 10D * rotateSpeed_z * rotateSpeed_z * tailRotorSurface * (double)airDensity * rotorCy * 0.5D;
		if(rotateSpeed_z < 0.0D)
			balanceMoment_z = 0.0D - balanceMoment_z;
		float antiSinkForce;
		if(sinkRate >= 0.0D)
			antiSinkForce = -(float)((rotorCy * rotorSurface + fuselageCyS) * (double)airDensity * sinkRate * sinkRate);
		else
			antiSinkForce = (float)((rotorCy * rotorSurface + fuselageCyS) * (double)airDensity * sinkRate * sinkRate);
		float headOnForce;
		double dragMoment_y;
		if(((Tuple3d) (vFlow)).x >= 0.0D)
		{
			headOnForce = -(float)(((rotorCx + rotorLineCx) * rotorSurface+fuselageCxS) * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
			dragMoment_y = -2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
		} else
		{
			headOnForce = (float)(((rotorCx + rotorLineCx) * rotorSurface+fuselageCxS) * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
			dragMoment_y = 2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
		}
		float sideForce;
		float tailRotorMoment;
		double dragMoment_x;
		if(((Tuple3d) (vFlow)).y >= 0.0D)
		{
			sideForce = -(float)(((rotorCx + rotorLineCx) * rotorSurface + fuselageCyS) * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
			tailRotorMoment = -(float)(rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
			dragMoment_x = 2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
		} else
		{
			sideForce = (float)(((rotorCx + rotorLineCx) * rotorSurface + fuselageCyS) * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
			tailRotorMoment = (float)(rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
			dragMoment_x = -2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
		}
		double rotorLift_3D_x = Math.sin(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
		double rotorLift_3D_y = Math.sin(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
		double rotorLift_3D_z = Math.cos(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
		float antiLiftForce;
		if(sinkRate >= 1.0D)
			antiLiftForce = (float)(0.5D * rotorCy * rotorSurface * (double)airDensity * sinkRate * sinkRate) * 20F;
		else
			antiLiftForce = 0.0F;
		FM.producedAF.x += ((double)headOnForce + rotorLift_3D_x) / 0.03 * f;
		FM.producedAF.y += ((double)sideForce + rotorLift_3D_y) / 0.03 * f;
		FM.producedAF.z += (((double)antiSinkForce + rotorLift_3D_z) - (double)antiLiftForce) / 0.03 * f;
		FM.producedAM.x += ((double)dragMoment_x - balanceMoment_x+ rotorLift_moment_x) / 0.03 * f;
		FM.producedAM.y += ((double)((dragMoment_y + tailRotorLift_moment_y) - balanceMoment_y) + rotorLift_moment_y) / 0.03 * f;
		FM.producedAM.z += !TailRotorDestroyed ? FM.producedAM.z += (tailRotorMoment - tailRotorLift_moment_z - balanceMoment_z + rotorLift_moment_z) / 0.03 * f : rotorRPM * 50000 + aPitch * 50000;
		FM.Vwld.z *= sinkRate > 0.0 ? (cvt((float) sinkRate, 1.0F, 10.0F, 1.0F, 0.95F)) / 0.03 * f : 1.0 / 0.03 * f;
		FM.Sq.dragParasiteCx += fAOA > 0.0F ? (fAOA / 5) / 0.03 * f : 0;

		rotateSpeed_z = 0;
		rotateSpeed_y = 0;
		rotateSpeed_x = 0;
		headOnForce = 0;
		sideForce = 0;

		if(rotorRPM > 0) {
			float shakeMe = 0F;
			float shakeRPM = ((float) ((rotorRPM / 2.4D) * 0.01D)) / 0.03F * f;
			if (vFlow.x < 22.2D && vFlow.x > 11.1D) {
				shakeMe = (cvt((float) vFlow.x, 11.1F, 22.2F, 0.03F, shakeRPM * 0.010F + (aPitch * shakeRPM) * 0.015F)) / 0.03F * f;
			} else if (vFlow.x < 11.1D) {
				shakeMe = (cvt((float) vFlow.x, 5.5F, 11.1F, shakeRPM * 0.010F + (aPitch * shakeRPM) * 0.015F, 0.03F)) / 0.03F * f;
			}
			((RealFlightModel)FM).producedShakeLevel += shakeMe;
		}
	}

	public double PitchAuto(double p) {
		p = -(p * 4);
		if (p >= 0.2)
			p = 0.2;
		if (p <= -0.2)
			p = -0.2;
		return p;
	}

	public double RollAuto(double k) {
		k = -(k * 4);
		if (k >= 0.2)
			k = 0.2;
		if (k <= -0.2)
			k = -0.2;
		return k;
	}

	public void auxPressed(int i) {
		super.auxPressed(i);
		if (i == 20) {
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Trimmer: Set");
			sndTrim.cancel();
			sndTrim.setParent(getRootFX());
			sndTrim.play(pos.getAbsPoint());
			getTrim = true;
		}
		if (i == 21) {
			forceTrim_x = 0.0D;
			forceTrim_y = 0.0D;
			forceTrim_z = 0.0D;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Trimmer: Reset");
			sndTrim.cancel();
			sndTrim.setParent(getRootFX());
			sndTrim.play(pos.getAbsPoint());
		}
		if (i == 22){
			repMod++;
			if(repMod > 7)
				repMod = 0;
			if (repMod == 0) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: None");
			if (repMod == 1) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For All Ground Targets");
			if (repMod == 2) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Armor");
			if (repMod == 3) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Guns");
			if (repMod == 4) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Vehicles");
			if (repMod == 5) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Train");
			if (repMod == 6) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Ship");
			if (repMod == 7) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For All Air Targets");
		}
		if (i == 23) {
			if (!bManualFire) {
				bManualFire = true;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "9K113 Fire Control: Pilot");

			} else if (bManualFire) {
				bManualFire = false;
				laserOn = false;
				Reflection.setInt(guidedMissileUtils, "engageMode", 1);
				HUD.log(AircraftHotKeys.hudLogWeaponId, "9K113 Fire Control: Operator");
			}
		}
		if (i == 24) {
			if(bManualFire) {
				if (!laserOn) {
					laserOn = true;
					laserLock = false;
					HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: On");

				} else if (laserOn) {
					laserOn = false;
					laserLock = false;
					HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: Off");
				}
			}
		}
		if (i == 26) {
			aso2mode++;
			if(aso2mode > 2)
				aso2mode = 0;
			if (aso2mode == 0) {
				HUD.log(AircraftHotKeys.hudLogWeaponId, "ASO-2V: Standby");
				intervalCommonThreat = 0L;
			}
			if (aso2mode == 1) { 
				HUD.log(AircraftHotKeys.hudLogWeaponId, "ASO-2V: 4 sec. interval");
			}
			if (aso2mode == 2) { 
				HUD.log(AircraftHotKeys.hudLogWeaponId, "ASO-2V: 8 sec. interval");
			}
		}
		if (i == 27) {
			if (!bAPazimut) {
				bAPazimut = true;
				apAzimut = -FM.Or.getAzimut();
				HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Heading: On");
			} else if (bAPazimut) {
				bAPazimut = false;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Heading: Off");
			}
		}
		if (i == 28) {
			if (!bAPalt) {
				bAPalt = true;
				apAlt = FM.getAltitude();
				HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Altitude: On");
			} else if (bAPalt) {
				bAPalt = false;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Altitude: Off");
			}
		}
		if (i == 29) {
			if (!bAPkrentang) {
				bAPkrentang = true;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Pitch + Roll: On");
			} else if (bAPkrentang) {
				bAPkrentang = false;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Pitch + Roll: Off");
			}
		}
	}

	private void checkAmmo()
	{
		missilesList.clear();
		for(int i = 0; i < FM.CT.Weapons.length; i++)
		{
			if(FM.CT.Weapons[i] == null)
				continue;
			for(int j = 0; j < FM.CT.Weapons[i].length; j++)
			{
				if(!FM.CT.Weapons[i][j].haveBullets())
					continue;
				if(FM.CT.Weapons[i][j] instanceof RocketGun9M114)
				{
					missilesList.add(FM.CT.Weapons[i][j]);
				}
			}        
		}

	}

	private void doOperatorLaunchMissile() {
		victim.pos.getAbs(point3dop);
		Mi24V.spot.set(point3dop);
		boolean isReady = (FM.getAOA() < (FM.Skill + 3)) && (FM.getOverload() < (FM.Skill + 1));
		if(missilesList.isEmpty()) {
			return;
		} else if (Actor.isAlive(victim) && Time.current() > missileLaunchInterval && isReady) {
			guidedMissileUtils.update();
			missileLaunchInterval = Time.current() + 20000L + (4 - FM.Skill) * 1000L;
			((RocketGun9M114)missilesList.remove(0)).shots(1);
			HUD.training("Missile Gone!");
			return;
		}   
	}

	private void doLaunchMissileAI() {
		if(isAI && !missilesList.isEmpty() && Time.current() > missileLaunchInterval)
		{
			if((((Maneuver) FM).get_maneuver() == 7 || ((Maneuver) FM).get_maneuver() == 43) && ((Maneuver) FM).target_ground != null)
			{
				((RocketGun9M114)missilesList.remove(0)).shots(1);
				missileLaunchInterval = Time.current() + 20000L + (4 - FM.Skill) * 1000L;
				Voice.speakAttackByRockets(this);
			}
		}
	}

	public boolean typeFighterAceMakerToggleAutomation()
	{
		k14Mode++;
		if(k14Mode > 3)
			k14Mode = 0;
		if(k14Mode == 0)
		{
			if(FM.actor == World.getPlayerAircraft())
				HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Grid");
		} else
			if(k14Mode == 1)
			{
				if(FM.actor == World.getPlayerAircraft())
					HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: S-8");
			} else
				if(k14Mode == 2)
				{
					if(FM.actor == World.getPlayerAircraft())
						HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: S-13");
				} else
					if(k14Mode == 3)
					{	
						if(FM.actor == World.getPlayerAircraft())
							HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Unguided Bomb");
					}
		return true;
	}

	public void typeFighterAceMakerAdjDistanceReset()
	{
	}

	public void typeFighterAceMakerAdjDistancePlus()
	{
	}

	public void typeFighterAceMakerAdjDistanceMinus()
	{
	}

	public void typeFighterAceMakerAdjSideslipReset()
	{
	}

	public void typeFighterAceMakerAdjSideslipPlus()
	{
	}

	public void typeFighterAceMakerAdjSideslipMinus()
	{
	}

	public void typeFighterAceMakerRangeFinder()
	{
		if(k14Mode == 1)
		{		
			Vector3d vTmp1 = new Vector3d();
			Point3d pTmp1 = new Point3d();
			Point3d pTmp2 = new Point3d();
			vTmp1.set(1.0D, 0.0D, 0.0D);
			FM.Or.transform(vTmp1);
			vTmp1.scale(6000.0D);
			pTmp1.set(FM.Loc);
			pTmp1.add(vTmp1);

			Engine.land();
			if (Landscape.rayHitHQ(FM.Loc, pTmp1, pTmp2)) {
				k14Distance = (float) pos.getAbsPoint().distance(pTmp2);
			}
		}
	}

	public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
			throws IOException
	{
		netmsgguaranted.writeByte(k14Mode);
		netmsgguaranted.writeByte(k14WingspanType);
		netmsgguaranted.writeFloat(k14Distance);
	}

	public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
			throws IOException
	{
		k14Mode = netmsginput.readByte();
		k14WingspanType = netmsginput.readByte();
		k14Distance = netmsginput.readFloat();
	}

	public void update(float f) {
		typeFighterAceMakerRangeFinder();
		if (Time.current() < asTimer) {
			checkAirstart();
		}
		if (isAI) stability(f); else human(f);
		rotorSound();
		dustEmit();
		setMissileLaunchThreatActive();
		laserUpdate();
		tiltRotor(f);
		guidedMissileUtils.update();
		if (obsMove < obsMoveTot && !bObserverKilled && !((FlightModelMain) (super.FM)).AS.isPilotParatrooper(1)) {
			if (obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
				obsMove += 0.29999999999999999D * (double) f;
			else if (obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
				obsMove += 0.15F;
			else
				obsMove += 1.2D * (double) f;
			obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
			obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
			hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
		}
		super.update(f);
	}

	public void rareAction(float f, boolean flag) {
		if (aso2mode > 0) {
			setCommonThreatActive();
		}
		if (isAI && !FM.Gears.onGround()) {
			Pilot pilot = (Pilot) FM;
			if((((Maneuver) FM).get_maneuver() == 7 || ((Maneuver) FM).get_maneuver() == 43) || ((Maneuver) FM).target_ground != null)
			{
				aso2mode = 1;
			} else if (threatIsNear) {
				aso2mode = 2;
			} else if (pilot != null && isInPlayerWing()) {
				if (pilot.Leader != null && (pilot.Leader instanceof RealFlightModel) || ((RealFlightModel) pilot.Leader).isRealMode()) {
					aso2mode = ((Mi24X)World.getPlayerAircraft()).aso2mode;
				}
			}
		}
		OperatorLookout();
		super.rareAction(f, flag);
		if (((FlightModelMain) (super.FM)).AS.bNavLightsOn) {
			Point3d point3d = new Point3d();
			Orient orient = new Orient();
			super.pos.getAbs(point3d, orient);
			l.set(point3d, orient);
			Eff3DActor eff3dactor = Eff3DActor.New(this, findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
			eff3dactor.postDestroy(Time.current() + 500L);
			LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
			lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
			lightpointactor.light.setEmit(1.0F, 3F);
			((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
		}
		if (!bObserverKilled)
			if (obsLookTime == 0) {
				obsLookTime = 2 + World.Rnd().nextInt(1, 3);
				obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
				obsMove = 0.0F;
				obsAzimuthOld = obsAzimuth;
				obsElevationOld = obsElevation;
				if ((double) World.Rnd().nextFloat() > 0.80000000000000004D) {
					obsAzimuth = 0.0F;
					obsElevation = 0.0F;
				} else {
					obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
					obsElevation = World.Rnd().nextFloat() * 50F - 20F;
				}
			} else {
				obsLookTime--;
			}
	}

	//TODO: onAircraftLoaded();
	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		checkAmmo();
		guidedMissileUtils.onAircraftLoaded();
		World.cur().diffCur.Engine_Overheat = false;
		if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))  		
		{
			isAI = false;
		} else {
			isAI = true;
			repMod = 1;
			Squares squares = (Squares)Reflection.getValue(FM, "Sq");
			squares.squareWing = 66.0F;
			squares.squareAilerons = 1.00F;
			squares.squareFlaps = 0.81F;
			squares.liftStab = 5.20F;
			squares.squareElevators = 2.80F;
			squares.liftKeel = 0.1F;
			squares.squareRudders = 2.50F;
			squares.liftWingLIn = (squares.liftWingRIn = 10.00F);
			squares.liftWingLMid = (squares.liftWingRMid = 10.00F);
			squares.liftWingLOut = (squares.liftWingROut = 10.00F);
			squares.dragFuselageCx = 0.06F;
			squares.dragAirbrakeCx = 1.00F;

			Vector3f eVect = new Vector3f();
			eVect.x = 1.0F; eVect.y = 0.0F; eVect.z = 0.0F;

			Point3d ePos = new Point3d();
			ePos.x = 1.5F; ePos.y = 0.0F; ePos.z = 0.0F;

			Point3d ePropPos = new Point3d();
			ePropPos.x = 1.0F; ePropPos.y = 0.0F; ePropPos.z = 0.0F;

			this.FM.EI.engines[0].setVector(eVect);
			this.FM.EI.engines[0].setPos(ePos);
			this.FM.EI.engines[0].setPropPos(ePropPos);

			Reflection.setFloat(FM.EI.engines[0], "tChangeSpeed", 0.000001F);
			Reflection.setFloat(FM.EI.engines[0], "thrustMax", 2200.0F);
			Reflection.setFloat(FM.EI.engines[0], "engineAcceleration", 1.0F);
			Reflection.setFloat(FM.EI.engines[0], "propReductor", 1.0F);

			this.FM.EI.engines[1].setVector(eVect);
			this.FM.EI.engines[1].setPos(ePos);
			this.FM.EI.engines[1].setPropPos(ePropPos);

			Reflection.setFloat(FM.EI.engines[1], "tChangeSpeed", 0.000001F);
			Reflection.setFloat(FM.EI.engines[1], "thrustMax", 2200.0F);
			Reflection.setFloat(FM.EI.engines[1], "engineAcceleration", 1.0F);
			Reflection.setFloat(FM.EI.engines[1], "propReductor", 1.0F);
			Polares polares = (Polares)Reflection.getValue(FM, "Wing");
			polares.AOA_crit = 10.0F;
			Reflection.setFloat(FM, "Vmin", 20.0F);
			Reflection.setFloat(FM, "Vmax", 280.0F);
			Reflection.setFloat(FM, "VmaxAllowed", 300.0F);
			Reflection.setFloat(FM, "VmaxH", 280.0F);
			Reflection.setFloat(FM, "HofVmax", 7900.0F);
			Reflection.setFloat(FM, "VminFLAPS", 20.0F);
			Reflection.setFloat(FM, "VmaxFLAPS", 280.0F);
			polares.Cy0_max = 7.15F;
			polares.FlapsMult = 1.0F;
			polares.FlapsAngSh = 4.0F;
			polares.lineCyCoeff = 0.19F;
			polares.AOAMinCx_Shift = 0.0F;
			polares.Cy0_0 = 0.8F;
			polares.AOACritH_0 = 10.0F;
			polares.AOACritL_0 = -6.0F;
			polares.CyCritH_0 = 0.9F;
			polares.CyCritL_0 = -0.7648107F;
			polares.parabCxCoeff_0 = 5.0E-4F;
			polares.CxMin_0 = 0.0125F;
			polares.Cy0_1 = 7.53F;
			polares.AOACritH_1 = 10.0F;
			polares.AOACritL_1 = -6.0F;
			polares.CyCritH_1 = 8.2791651F;
			polares.CyCritL_1 = -0.7F;
			polares.CxMin_1 = 0.15F;
			polares.parabCxCoeff_1 = 7.5E-5F;
			polares.parabAngle = 5.0F;
			polares.declineCoeff = 0.008F;
			polares.maxDistAng = 40.0F;
			FM.setGCenter(0.0F);
			FM.CT.bHasBrakeControl = true;
			FM.CT.bHasAirBrakeControl = false;
		}
	}

	protected static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte bt)
	{
		Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
		try
		{
			a_lweaponslot[0] = new Aircraft._WeaponSlot(11, "MGunYakB", 1470);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareLO56_gn16", 96);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareLO56_gn16", 96);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Default loadout Generator method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertB8V20AinConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8V20A_gn16", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8V20A_gn16", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8OFP2_gn16", 20);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8OFP2_gn16", 20);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in B-8V20A loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertB8V20AoutConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8V20A_gn16", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8V20A_gn16", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8OFP2_gn16", 20);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8OFP2_gn16", 20);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out B-8V20A loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertB13inConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in B-13 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertB13outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out B-13 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] Insert9M114Config(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon9M114", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon9M114", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGun9M114", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGun9M114", 1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGun9M114", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGun9M114", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 4x out 9M114 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertFAB250inConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in FAB-250 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertFAB250outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out FAB-250 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertFAB500inConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in FAB-500 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertFAB500outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out FAB-500 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertRBK250inConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in RBK-250 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertRBK250outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out RBK-250 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertRBK500inConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in RBK-500 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertRBK500outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out RBK-500 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertKMGU2inConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in KMGU2 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertKMGU2outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out KMGU2 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertSAB100inConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x in FAB-250 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	protected static Aircraft._WeaponSlot[] InsertSAB100outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
	{
		try
		{
			a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
		}
		catch(Exception exception) {
			System.out.println("Weapon register error - Mi-24V : Insert 2x out FAB-250 loadout method");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return a_lweaponslot;
	}

	private Orientation tOr;
	private Vector3d tV[];
	private Point3d tP[];
	private int takeOffStep;
	private Vector3d tmpAF;
	private Vector3d tmpAM;
	private Eff3DActor dustEff[];
	private Hook hookDust[];
	private Loc dustLoc;

	public boolean threatIsNear;
	public int aso2mode;

	public int k14Mode;
	public int k14WingspanType;
	public float k14Distance;

	protected SoundFX sndTrim;

	private boolean bAPazimut;
	private boolean bAPalt;
	private boolean bAPkrentang;

	private float apALltPitch;
	private float apAlt;
	private float apKren;
	private float apTang;
	private float apAzimut;

	private long missileLaunchInterval;
	private ArrayList missilesList;
	public Actor victim;
	private boolean bManualFire;
	private boolean bShotFlare;

	private int repMod;
	private int counter = 0;

	private Vector3d vector3dop = new Vector3d();
	private Point3d  point3dop  = new Point3d();

	private Point3d localPoint3d1 = new Point3d();
	private boolean landing;
	private boolean hover;
	private float gndYaw;
	private float gndRoll;
	private float gndPitch;

	private boolean takeOff;
	private boolean TailRotorDestroyed;
	public static Orient LaserOr = new Orient();

	public boolean laserOn;
	public boolean laserLock;

	public boolean FLIR;

	public BulletEmitter Weapons[][];
	public int rocketHookSelected;
	private static Loc l = new Loc();
	private int obsLookTime;
	private float obsLookAzimuth;
	private float obsLookElevation;
	private float obsAzimuth;
	private float obsElevation;
	private float obsAzimuthOld;
	private float obsElevationOld;
	private float obsMove;
	private float obsMoveTot;
	boolean bObserverKilled;
	public static boolean bChangedPit = false;
	public Loc suka;
	private GuidedMissileUtils guidedMissileUtils;

	public double forceTrim_x;
	public double forceTrim_y;
	public double forceTrim_z;
	public boolean getTrim;

	private Hook[] LaserHook = { null, null, null, null };

	private static Loc LaserLoc1 = new Loc();
	private static Point3d LaserP1 = new Point3d();
	private static Point3d LaserP2 = new Point3d();
	private static Point3d LaserPL = new Point3d();

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
	private float curAngleRotor;
	private float diffAngleRotor;
	private float curAngleTailRotor;
	private float diffAngleTailRotor;
	private long lastTimeFan;

	public float aPitch;
	public float aOldPitch;

	public double engineRPM;
	public double reductorRPM;
	public double rotorRPM;
	public double tailRotorRPM;

	protected SoundFX sndProp;

	public boolean isAI;
	private long asTimer;
	private boolean AltCheck;
	private boolean isAirstart;

	static 
	{
		Class class1 = com.maddox.il2.objects.air.Mi24X.class;
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
	}
}