package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.MsgCollision;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Torpedo extends Bomb {

	public void msgCollision(Actor actor, String s, String s1) {
		Class class1 = this.getClass();
		this.armingTime = (long) Property.floatValue(class1, "armingTime", 1.0F) * 1000L;
		this.timeTravelInWater = Time.current() - this.timeHitWater;
		this.Other = actor;
		this.OtherChunk = s1;
		if (this instanceof BombTorpFFF && ((BombTorpFFF) this).bOnChute2) {
			((BombTorpFFF) this).bOnChute2 = false;
			if (!(actor instanceof ActorLand)) {
				this.doExplosion(actor, s1);
				return;
			}
			if (World.land().isWater(P.x, P.y)) {
				return;
			} else {
				this.doExplosion(actor, s1);
				return;
			}
		}
		if (actor instanceof ActorLand) {
			if (this.flow) {
				this.doExplosion(actor, s1);
				return;
			}
			this.surface();
			if (World.land().isWater(P.x, P.y)) { return; }
		}
		if (this.timeTravelInWater < this.armingTime && !(actor instanceof ActorLand)) {
			if (this.getOwner() instanceof Aircraft) {
				Aircraft aircraft = (Aircraft) this.getOwner();
				if (aircraft.FM.isPlayers()) { HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoDidNotArm"); }
			}
			this.destroy();
		} else if (this.hasHitLand || !this.flow) {
			Explosions.Explode10Kg_Land(P, 2.0F, 2.0F);
		} else {
			if (this.getOwner() instanceof Aircraft) {
				Aircraft aircraft1 = (Aircraft) this.getOwner();
				if (aircraft1.FM.isPlayers()) { HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoHit"); }
			}
			this.doExplosion(actor, s1);
		}
	}

	private void surface() {
		Class class1 = this.getClass();
		boolean explodeOnEntry = false;
		this.travelTime = (long) Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
		this.impactAngleMin = Property.floatValue(class1, "impactAngleMin", 1.0F);
		this.impactAngleMax = Property.floatValue(class1, "impactAngleMax", 1.0F);
		this.impactSpeed = Property.floatValue(class1, "impactSpeed", 1.0F);
		this.timeHitWater = Time.current();
		if (this instanceof BombTorpFFF1) {
			this.turnSpeed = 0.12F;
			float f = (float) spd.length();
			float f3 = 5.2F / f;
			spd.scale(f3);
			this.setSpeed(spd);
		} else if (this instanceof BombTorpFFF) {
			this.turnSpeed = 0.1F;
			float f1 = (float) spd.length();
			float f4 = 5F / f1;
			spd.scale(f4);
			this.setSpeed(spd);
		} else if (this instanceof BombParaTorp) {
			this.turnSpeed = 0.1F;
			float f2 = (float) spd.length();
			float f5 = 5F / f2;
			spd.scale(f5);
			this.setSpeed(spd);
		}
		this.pos.getAbs(P, Or);
		this.flow = true;
		this.getSpeed(spd);
		if (World.land().isWater(P.x, P.y)) {
			if (spd.z < -0.12D) { Explosions.RS82_Water(P, 4F, 1.0F); }
			double curImpactSpeed = spd.length();
			Aircraft ownerAircraft = null;
			boolean checkEntryParams = false;
			if (this.getOwner() instanceof Aircraft && !(this instanceof BombParaTorp)) {
				checkEntryParams = true;
				ownerAircraft = (Aircraft) this.getOwner();
				// TODO: Storebror: Added ownerAircraft null check
				if (ownerAircraft != null && (ownerAircraft.FM.isPlayers() || ownerAircraft.isNetPlayer())) {
					checkEntryParams = true;
				} else {
					this.impactAngleMin *= 0.8F;
					this.impactAngleMax *= 1.2F;
					this.impactSpeed *= 1.2F;
				}
			}

			// TODO: Storebror: +++++ New Impact Check with "smooth" envelope limits +++++
			printDebugMessage("Torp Limits... speedLimit=" + this.speedLimit + ", angleLimitLow=" + this.angleLimitLow + ", angleLimitHigh=" + this.angleLimitHigh);
			double sinImpactAngle = -1D;
			if (curImpactSpeed > 0.001D) { sinImpactAngle = spd.z / curImpactSpeed; }
			float impactAngle = 180F * (float) Math.abs(Math.asin(sinImpactAngle)) / (float) Math.PI;
			if (ownerAircraft != null && ownerAircraft.FM.isPlayers()) { Main3D.cur3D().hud.logTorpedoImpact((float) curImpactSpeed, impactAngle); }
			if (curImpactSpeed > this.impactSpeed && checkEntryParams) {
				boolean failed = true;
				if (ownerAircraft != null && ownerAircraft.FM.isPlayers()/* && !NetMissionTrack.isPlaying() */) {
					float impactSpeedExceedFactor = (float) curImpactSpeed / this.impactSpeed - 1.0F;
					if (this.speedLimit > impactSpeedExceedFactor * impactSpeedExceedFactor) { failed = false; }
				}
				if (failed) {
					if (ownerAircraft != null && ownerAircraft.FM.isPlayers()) { HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoBrokenOnEntryIntoWater"); }
					explodeOnEntry = true;
					this.destroy();
				}
			} else {
				boolean failed = false;
				if (checkEntryParams && (impactAngle > this.impactAngleMax || sinImpactAngle < -0.99D)) {
					failed = true;
					if (ownerAircraft != null && ownerAircraft.FM.isPlayers()/* && !NetMissionTrack.isPlaying() */) {
						float impactAngleExceedFactor = impactAngle / this.impactAngleMax - 1.0F;
						if (this.angleLimitHigh > impactAngleExceedFactor * impactAngleExceedFactor) { failed = false; }
					}
				}
				if (checkEntryParams && impactAngle < this.impactAngleMin) {
					failed = true;
					if (ownerAircraft != null && ownerAircraft.FM.isPlayers()/* && !NetMissionTrack.isPlaying() */) {
						float impactAngleExceedFactor = impactAngle / this.impactAngleMin;
						if (this.angleLimitLow * this.angleLimitLow < impactAngleExceedFactor) { failed = false; }
					}
				}
				if (failed) {
					if (ownerAircraft != null && ownerAircraft.FM.isPlayers()) { HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoFailedEntryIntoWater"); }
					explodeOnEntry = true;
					this.destroy();
				}
			}
//            if (curImpactSpeed > (double) impactSpeed && checkEntryParams) {
//                if (ownerAircraft != null && ownerAircraft.FM.isPlayers())
//                    HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoBrokenOnEntryIntoWater");
//                explodeOnEntry = true;
//                destroy();
//            } else {
//                if (curImpactSpeed > 0.001D)
//                    curImpactSpeed = spd.z / spd.length();
//                else
//                    curImpactSpeed = -1D;
//                float impactAngle = (180F * (float) Math.abs(Math.asin(curImpactSpeed))) / (float)Math.PI;
//                if (checkEntryParams && (impactAngle > impactAngleMax || curImpactSpeed < -0.99D)) {
//                    if (ownerAircraft != null && ownerAircraft.FM.isPlayers())
//                        HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoFailedEntryIntoWater");
//                    explodeOnEntry = true;
//                    destroy();
//                }
//                if (checkEntryParams && impactAngle < impactAngleMin) {
//                    if (ownerAircraft != null && ownerAircraft.FM.isPlayers())
//                        HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoFailedEntryIntoWater");
//                    explodeOnEntry = true;
//                    destroy();
//                }
//            }
			// TODO: Storebror: ----- New Impact Check with "smooth" envelope limits -----

		} else {
			explodeOnEntry = true;
			this.hasHitLand = true;
			this.destroy();
		}
		if (!explodeOnEntry) {
			if (this.getOwner() instanceof TypeHasToKG) {
				Aircraft aircraft = (Aircraft) this.getOwner();
				this.gyroTargetAngle = aircraft.FM.AS.getGyroAngle();
				this.spreadAngle = aircraft.FM.AS.getSpreadAngle();
				int i = Property.intValue(class1, "spreadDirection", 0);
				this.gyroTargetAngle += this.spreadAngle / 2.0F * i;
			}
			this.getSpeed(spd);
			spd.z = 0.0D;
			this.setSpeed(spd);
			P.z = 0.0D;
			float af[] = new float[3];
			Or.getYPR(af);
			Or.setYPR(af[0], 0.0F, af[2]);
			this.pos.setAbs(P, Or);
			this.flags &= 0xffffffbf;
			this.drawing(false);
			if (this instanceof BombParaTorp) {
				Eff3DActor.New(this, null, null, 0.5F, "3DO/Effects/Tracers/533mmTorpedo/CirclingLine.eff", -1F);
			} else {
				Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/Line.eff", -1F);
				Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/wave.eff", -1F);
			}
		}
	}

	public void interpolateTick() {
		float f = Time.tickLenFs();
		this.pos.getAbs(P);
		if (P.z <= -0.1D) { this.surface(); }
		if (!this.flow) {
			Ballistics.update(this, this.M, this.S);
		} else {
			this.getSpeed(spd);
			if (spd.length() > this.velocity) {
				spd.scale(0.99D);
			} else if (spd.length() < this.velocity) { spd.scale(1.01D); }
			this.setSpeed(spd);
			this.pos.getAbs(P);
			float f1 = this.gyroTargetAngle - this.gyroAngle;
			if (this instanceof BombTorpFFF1) {
				this.turnSpeed -= 7.8E-006F;
				if (this.turnSpeed < 0.0084F) { this.turnSpeed = 0.0084F; }
				f1 = -0.5F;
			} else if (this instanceof BombParaTorp) {
				this.turnSpeed -= 7E-006F;
				if (this.turnSpeed < 0.008F) { this.turnSpeed = 0.008F; }
				f1 = 0.5F;
			}
			if (f1 != 0.0F) {
				if (f1 < 0.0F) {
					this.gyroAngle = this.gyroAngle - this.turnSpeed;
				} else if (f1 > 0.0F) { this.gyroAngle = this.gyroAngle + this.turnSpeed; }
				float f2 = -(float) Math.toRadians(this.gyroAngle);
				float f3 = (float) Math.cos(Math.abs(f2));
				float f4 = (float) Math.sin(f2);
				float f5 = (float) (spd.x * f3 - spd.y * f4);
				float f6 = (float) (spd.x * f4 + spd.y * f3);
				spd.x = f5;
				spd.y = f6;
			}
			P.x += spd.x * f;
			P.y += spd.y * f;
			this.pos.setAbs(P);
			if (Time.current() > this.started + this.travelTime || !World.land().isWater(P.x, P.y)) { this.sendexplosion(); }
		}
		this.updateSound();
	}

	private void sendexplosion() {
		MsgCollision.post(Time.current(), this, this.Other, null, this.OtherChunk);
	}

	public void start() {
		Class class1 = this.getClass();
		this.init(Property.floatValue(class1, "kalibr", 1.0F), Property.floatValue(class1, "massa", 1.0F));
		this.started = Time.current();
		this.velocity = Property.floatValue(class1, "velocity", 1.0F);
		this.travelTime = (long) Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
		this.setOwner(this.pos.base(), false, false, false);
		this.pos.setBase(null, null, true);
		this.pos.setAbs(this.pos.getCurrent());
		this.getSpeed(spd);
		this.pos.getAbs(P, Or);
		Vector3d vector3d = new Vector3d(Property.floatValue(class1, "startingspeed", 0.0F), 0.0D, 0.0D);
		Or.transform(vector3d);
		spd.add(vector3d);
		this.setSpeed(spd);
		this.collide(true);
		this.interpPut(new Bomb.Interpolater(), null, Time.current(), null);
		this.drawing(true);
		if (Property.containsValue(class1, "emitColor")) {
			LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
			lightpointactor.light.setColor((Color3f) Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
			lightpointactor.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
			this.draw.lightMap().put("light", lightpointactor);
		}
		this.sound = this.newSound(Property.stringValue(class1, "sound", null), false);
		if (this.sound != null) { this.sound.play(); }
	}

	// TODO: Storebror: Torpedo Failure Rate Replication
	// ------------------------------------
	public void setLimits(float speedLimit, float angleLimitLow, float angleLimitHigh) {
		printDebugMessage("Torpedo setLimits(" + speedLimit + ", " + angleLimitLow + ", " + angleLimitHigh + ")");
		this.speedLimit = speedLimit;
		this.angleLimitLow = angleLimitLow;
		this.angleLimitHigh = angleLimitHigh;
	}
	// ------------------------------------

	public Torpedo() {
		this.gyroAngle = 0.0F;
		this.gyroTargetAngle = 0.0F;
		this.turnSpeed = 0.1F;
		this.spreadAngle = 0;
		this.hasHitLand = false;
		this.doLandExplosion = false;
	}

	Actor           Other;
	String          OtherChunk;
	String          ThisChunk;
	boolean         flow;
	private float   velocity;
	private long    travelTime;
	private long    started;
	private float   gyroAngle;
	private float   gyroTargetAngle;
	private float   turnSpeed;
	private int     spreadAngle;
	private long    timeHitWater;
	private long    timeTravelInWater;
	private float   impactAngleMin;
	private float   impactAngleMax;
	private float   impactSpeed;
	private float   armingTime;
	boolean         hasHitLand;
	boolean         doLandExplosion;
	static Vector3d spd = new Vector3d();
	static Orient   Or  = new Orient();
	static Point3d  P   = new Point3d();

	// TODO: Storebror: Torpedo Failure Rate Replication
	// ------------------------------------
	private float speedLimit;
	private float angleLimitLow;
	private float angleLimitHigh;
	// ------------------------------------

	private static int       debugLevel    = Integer.MIN_VALUE;
	private static final int DEBUG_DEFAULT = 0;

	private static int curDebugLevel() {
		if (debugLevel == Integer.MIN_VALUE) { debugLevel = Config.cur.ini.get("Mods", "DEBUG_TORPEDO", DEBUG_DEFAULT); }
		return debugLevel;
	}

	public static void printDebugMessage(String theMessage) {
		if (curDebugLevel() == 0) { return; }
		System.out.println(theMessage);
	}
}
