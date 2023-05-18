// Source File Name: Missile.java
// Author:	Storebror
// Edit:	western0221 on 21th/Feb./2019
package com.maddox.il2.objects.weapons;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeGuidedMissileCarrier;
import com.maddox.il2.objects.air.TypeHARM_carrier;
import com.maddox.il2.objects.air.TypeLaserDesignator;
import com.maddox.il2.objects.air.TypeSACLOS;
import com.maddox.il2.objects.air.TypeSemiRadar;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.ObjState;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;

public class Missile extends Rocket
	implements MsgExplosionListener, MsgShotListener {

	class Master extends ActorNet implements NetUpdate {

		NetMsgFiltered out;

		Orient theMissileOrient = new Orient();

		Point3d theMissilePoint3d = new Point3d();

		public Master(Actor actor) {
			super(actor);
			this.out = new NetMsgFiltered();
		}

		public void msgNetNewChannel(NetChannel netchannel) {
			if (!Actor.isValid(this.actor())) return;
			try {
				if (netchannel.isMirrored(this)) return;
				if (netchannel.userState == 0) {
					NetMsgSpawn netmsgspawn = this.actor().netReplicate(netchannel);
					if (netmsgspawn != null) {
						this.postTo(netchannel, netmsgspawn);
						this.actor().netFirstUpdate(netchannel);
					}
				}
			} catch (Exception exception) {
				NetObj.printDebug(exception);
			}
		}

		public boolean netInput(NetMsgInput netmsginput) throws IOException {
			return false;
		}

		public void netUpdate() {
			try {
				this.out.unLockAndClear();
				this.actor().pos.getAbs(this.theMissilePoint3d, this.theMissileOrient);
				this.out.writeFloat((float) this.theMissilePoint3d.x);
				this.out.writeFloat((float) this.theMissilePoint3d.y);
				this.out.writeFloat((float) this.theMissilePoint3d.z);
				this.theMissileOrient.wrap();
				int i = (int) ((this.theMissileOrient.getYaw() * 32000F) / 180F);
				int j = (int) ((this.theMissileOrient.tangage() * 32000F) / 90F);
				int k = (int) ((this.theMissileOrient.getKren() * 32000F) / 90F);
				this.out.writeShort(i);
				this.out.writeShort(j);
				this.out.writeShort(k);
				this.post(Time.current(), this.out);
			} catch (Exception exception) {
				NetObj.printDebug(exception);
			}
		}
	}

	class Mirror extends ActorNet {

		NetMsgFiltered out;

		Orient theMissileOrient = new Orient();

		Point3d theMissilePoint3d = new Point3d();

		public Mirror(Actor actor, NetChannel netchannel, int i) {
			super(actor, netchannel, i);
			this.out = new NetMsgFiltered();
		}

		public void msgNetNewChannel(NetChannel netchannel) {
			if (!Actor.isValid(this.actor())) return;
			if (netchannel.isMirrored(this)) return;
			try {
				if (netchannel.userState == 0) {
					NetMsgSpawn netmsgspawn = this.actor().netReplicate(netchannel);
					if (netmsgspawn != null) {
						this.postTo(netchannel, netmsgspawn);
						this.actor().netFirstUpdate(netchannel);
					}
				}
			} catch (Exception exception) {
				NetObj.printDebug(exception);
			}
			return;
		}

		public boolean netInput(NetMsgInput netmsginput) throws IOException {
			if (netmsginput.isGuaranted()) return false;
			if (this.isMirrored()) {
				this.out.unLockAndSet(netmsginput, 0);
				this.postReal(Message.currentTime(true), this.out);
			}
			this.theMissilePoint3d.set(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());

			int i = netmsginput.readShort();
			int j = netmsginput.readShort();
			int k = netmsginput.readShort();
			float f = -((i * 180F) / 32000F);
			float f1 = (j * 90F) / 32000F;
			float f2 = (k * 90F) / 32000F;
			this.theMissileOrient.set(f, f1, f2);
			this.actor().pos.setAbs(this.theMissilePoint3d, this.theMissileOrient);
			return true;
		}
	}

	/**
	 * Helper Class for a chained list of active Nav Lights for a particular Missile.
	 * This class is necessary since missiles can have different numbers of active Nav Lights.
	 */
	private class MissileNavLight {
		public MissileNavLight nextNavLight; // Pointer to the next chain element
		public Eff3DActor theNavLight; // The Nav Light Object in the Chain

		public MissileNavLight(Eff3DActor theEff3DActor) { // Element constructor.
			this.theNavLight = theEff3DActor;
			this.nextNavLight = null;
		}
	}

	static class SPAWN implements NetSpawn {

		SPAWN() {
		}

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		}

		public void netSpawn(int i, NetMsgInput netmsginput) {
			NetObj netobj;
			netobj = netmsginput.readNetObj();
			if (netobj == null) return;
			try {
				Actor actor = (Actor) netobj.superObj();
				Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
				Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
				float f = netmsginput.readFloat();
				this.doSpawn(actor, netmsginput.channel(), i, point3d, orient, f);
				if (actor instanceof TypeGuidedMissileCarrier) {
					// EventLog.type("netSpawn 1");
					((TypeGuidedMissileCarrier) actor).getGuidedMissileUtils().shotMissile();
				}
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
			return;
		}
	}

	public Missile() {
		this.targetPoint3d = new Point3d();
		this.targetPoint3dAbs = new Point3d();
		this.trajectoryVector3d = new Vector3d();
		this.victimSpeed = new Vector3d();
		this.victimOffsetOrient = new Orient();
		this.victimOffsetPoint3f = new Point3f();
		this.MissileInit();
		return;
	}

	public Missile(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}

	public float AttackMaxDistance() {
		return this.attackMaxDistance;
	}

	public int chooseBulletType(BulletProperties abulletproperties[]) {
		if (abulletproperties.length == 1) return 0;
		if (abulletproperties.length <= 0) return -1;
		if (abulletproperties[0].power <= 0.0F) return 1;
		if (abulletproperties[1].power <= 0.0F) return 0;
		if (abulletproperties[0].powerType == 1) return 0;
		if (abulletproperties[1].powerType == 1) return 1;
		if (abulletproperties[0].powerType == 0) return 0;
		if (abulletproperties[1].powerType == 0) return 1;
		return abulletproperties[0].powerType != 2 ? 0 : 1;
	}

	public int chooseShotpoint(BulletProperties bulletproperties) {
		return (this instanceof MissileInterceptable) ? 0 : -1;
	}

	private boolean computeFuzeState() {
		this.deltaAzimuth = this.deltaTangage = 0.0F;
		this.pos.setAbs(this.missilePoint3d, this.missileOrient);
		if (Time.current() > this.startTime + (2 * this.trackDelay)) {
			if (Actor.isValid(this.victim)) {
				Point3d futureVictimPos = new Point3d();
				this.victim.pos.getTime(Time.tickNext(), futureVictimPos);
				Point3d futureMissilePos = new Point3d();
				this.pos.getTime(Time.tickNext(), futureMissilePos);
				float f2 = (float) this.missilePoint3d.distance(this.victim.pos.getAbsPoint());
				float f2Future = (float) futureMissilePos.distance(futureVictimPos); // - 5F;
				if (f2Future < 0F) {
					f2Future = 0F;
				}
				if (((this.victim instanceof Aircraft) || (this.victim instanceof RocketryRocket) || (this.victim instanceof MissileInterceptable)) && (f2Future > f2 * 5F || f2 > this.previousDistance) && this.previousDistance != 1000F) {
					if ( f2 > 0F && f2 < this.missileProximityFuzeRadius) {
						if ( bLogDetail ) {
							ArrayList lst = new ArrayList();
							Point3d point3d = new Point3d();
							this.pos.getAbs(point3d);
							Engine.collideEnv().getSphere(lst, point3d, this.missileProximityFuzeRadius * 2F);
							System.out.println("Missile(" + actorString(this) + ") - entering doExplosionAir(); - f2=" + f2 + " < FuzeRadius=" + this.missileProximityFuzeRadius + " , lst.size()=" + lst.size() + " , lst=" + lst);
						}
						// Time.setPause(true); // Test detonation distance!
						// HUD.log("MD:" + twoPlaces.format(f2) + "/" + twoPlaces.format(f2Future) + "/" + twoPlaces.format(previousDistance));
						this.doExplosionAir();
						this.postDestroy();
						this.collide(false);
						this.drawing(false);
					}
					// if (this.getSpeed(null) > victim.getSpeed(null)) {
					// victim = null;
					// }
				}
				this.previousDistance = f2;
			} else {
				this.previousDistance = 1000F;
			}
		}
		this.safeVictimOffset.set(this.victimOffsetPoint3f);
		if (!Actor.isValid(this.getOwner())) {
			if ( bLogDetail )
				System.out.println("Missile(" + actorString(this) + ") - entering doExplosionAir(); - Because of owner(" + actorString(this.getOwner()) + ") becomes not valid.");
			this.doExplosionAir();
			this.postDestroy();
			this.collide(false);
			this.drawing(false);
			return false;
		} else return false;

	}

	private float computeMissileAccelleration() {
		this.victimOffsetPoint3f.set(this.safeVictimOffset);
		// float tickLen = Time.tickLenFs();
		float missileSpeed = (float) this.getSpeed(null);
		if (this.getFailState() == FAIL_TYPE_ENGINE) {
			this.rocketMotorOperationTime = 0.0F;
			this.rocketMotorSustainedOperationTime = 0.0F;
		}
		this.pos.getAbs(this.missilePoint3d, this.missileOrient);
		this.missileOrient.wrap();

		float theForce = this.missileForce;
		float millisecondsFromStart = Time.current() - this.startTime;

		if (millisecondsFromStart > this.rocketMotorOperationTime + this.rocketMotorSustainedOperationTime) {
			if (!this.noSmokeFlameSustain && (flameActive || smokeActive || spriteActive)) {
				this.flameActive = false;
				this.smokeActive = false;
				this.spriteActive = false;
				this.endSmoke();
			}
			if (this.noSmokeFlameSustain && this.playingSustainMotorSound) {
				this.stopSounds();
				this.playingSustainMotorSound = false;
			}
			if (this.playingMotorSound) {
				this.stopSounds();
				this.playingMotorSound = false;
			}
			if (!this.showingTrailSmoke && this.effSmokeTrail != null) {
				this.afterFireEngineTrail();
				this.showingTrailSmoke = true;
			}
			this.missileMass = this.massaEnd;
			this.missileForce = 0.0F;
		} else if (millisecondsFromStart > this.rocketMotorOperationTime) {
			if (this.noSmokeFlameSustain && (flameActive || smokeActive || spriteActive)) {
				this.flameActive = false;
				this.smokeActive = false;
				this.spriteActive = false;
				this.endSmoke();
			}

			theForce = this.missileSustainedForce;

			if (this.waitingMeshSustain) {
				setMesh(MeshShared.get(this.mshSustain));
				this.waitingMeshSustain = false;
			}
			if (!this.noSmokeFlameSustain && !this.showingSustainSmokeFlame && this.effSmokeSustain != null) {
				this.flameActive = false;
				this.smokeActive = false;
				this.spriteActive = false;
				this.endSmoke();
				this.sustainEngine();
				this.showingSustainSmokeFlame = true;
			}
			if (this.noSoundSustain && this.playingMotorSound) {
				this.stopSounds();
				this.playingMotorSound = false;
			}
			if (!this.noSmokeFlameSustain && !this.playingSustainMotorSound && this.soundNameSustain != null && this.playingMotorSound) {
				this.stopSounds();
				this.playingMotorSound = false;
			}
			if (!this.playingSustainMotorSound && this.soundNameSustain != null) {
				this.newSound(this.soundNameSustain, true);
				this.playingSustainMotorSound = true;
			}
		} else {
			if (this.missileForceRunUpTime > 0.001F) {
				if (millisecondsFromStart < this.missileForceRunUpTime) {
					float runUpTimeFactor = millisecondsFromStart / this.missileForceRunUpTime;
					if (runUpTimeFactor > 1.0F)
						runUpTimeFactor = 1.0F;

					this.setAllSmokeIntensities(runUpTimeFactor);
					this.setAllSpriteIntensities(runUpTimeFactor);
					theForce *= (this.initialMissileForce + ((100.0F - this.initialMissileForce) * runUpTimeFactor)) / 100.0F;
				}
			}
			this.missileMass -= this.massLossPerTick;
		}
		float forceAzimuth = MissilePhysics.getGForce(missileSpeed, this.oldDeltaAzimuth / Time.tickLenFs());
		float forceTangage = MissilePhysics.getGForce(missileSpeed, this.oldDeltaTangage / Time.tickLenFs());
		float turnForce = (float) Math.sqrt((forceAzimuth * forceAzimuth) + (forceTangage * forceTangage)) * MissilePhysics.G_CONST * this.missileMass * ((float) Math.sqrt(2.0D) - 1.0F);
		turnForce *= this.dragCoefficientTurn;

		float resForce = (float) Math.sqrt(Math.abs((theForce * theForce) - (turnForce * turnForce)));
		if (turnForce > theForce) {
			resForce *= -1F;
		}
		float accelForce = resForce - MissilePhysics.getDragInGravity(this.frontSquare, this.dragCoefficient, (float) this.missilePoint3d.z, missileSpeed, this.missileOrient.getTangage(), this.missileMass);
		float accelleration = accelForce / this.missileMass;
		missileSpeed += accelleration * Time.tickLenFs();

		if (missileSpeed < 3F) {
			this.failState = FAIL_TYPE_WARHEAD; // let missile detonate when speed is too low
		}

		this.trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
		if ((Time.current() < this.startTime + this.trackDelay) && (this.launchType == Missile.LAUNCH_TYPE_DROP)) // recover from launch pitch even if there's no target.
		{
			this.dropFlightPathOrient.transform(this.trajectoryVector3d);
		} else {
			this.missileOrient.transform(this.trajectoryVector3d);
		}
		this.trajectoryVector3d.scale(missileSpeed);
		this.setSpeed(this.trajectoryVector3d);
		this.missilePoint3d.x += this.trajectoryVector3d.x * Time.tickLenFs();
		this.missilePoint3d.y += this.trajectoryVector3d.y * Time.tickLenFs();
		this.missilePoint3d.z += this.trajectoryVector3d.z * Time.tickLenFs();
		if ((this.isNet()) && (this.isNetMirror())) {
			this.pos.setAbs(this.missilePoint3d, this.missileOrient);
			return -1F;
		}
		if (this.getFailState() == FAIL_TYPE_WARHEAD) {
			if ( bLogDetail )
				System.out.println("Missile(" + actorString(this) + ") - entering doExplosionAir(); - Because of getFailState() == FAIL_TYPE_WARHEAD.");
			this.doExplosionAir();
			this.postDestroy();
			this.collide(false);
			this.drawing(false);
			return -1F;
		}
		return missileSpeed;
	}

	private void computeMissilePath(float missileSpeed, float hTurn, float vTurn, float azimuth, float tangage) {
		// System.out.println("computeMissilePath(" + missileSpeed + ", " + hTurn + ", " + vTurn + ", " + azimuth + ", " + tangage + ")");
		// if (Time.current() > this.startTime + this.trackDelay) {
		float turnStepMax = MissilePhysics.getDegPerSec(missileSpeed, this.maxG) * Time.tickLenFs(); // turn limit, higher altitude means less turn capability (app. 1/10th from ground
		// to 10km)
		float turnStepMaxSpeedFactor = 1F;
		float turnStepMaxAltitudeFactor = MissilePhysics.getAirDensityFactor((float) this.missilePoint3d.z);
		if (missileSpeed < 340F) { // Missile slower than Mach 1, control surfaces effectivity decreases
			turnStepMaxSpeedFactor = missileSpeed / 340F;
		} else {
			turnStepMaxAltitudeFactor *= Math.sqrt(missileSpeed / 340F);
			if (turnStepMaxAltitudeFactor > 1F) turnStepMaxAltitudeFactor = 1F;
		}

		turnStepMax *= turnStepMaxSpeedFactor * turnStepMaxAltitudeFactor;

		float newTurnDiffMax = turnStepMax / this.stepsForFullTurn; // turn rate change limit, smoothen the turns.
		if (newTurnDiffMax > this.turnDiffMax) {
			this.turnDiffMax = (this.turnDiffMax * this.stepsForFullTurn + newTurnDiffMax) / (this.stepsForFullTurn + 1.0F);
		}

		if (this.getFailState() == FAIL_TYPE_IVAN) {
			if (this.ivanTimeLeft < Time.tickLenFs()) {
				if (TrueRandom.nextFloat() < 0.5F) {
					if (TrueRandom.nextFloat() < 0.5F) {
						this.deltaAzimuth = turnStepMax;
					} else {
						this.deltaAzimuth = -turnStepMax;
					}
					this.deltaTangage = TrueRandom.nextFloat(-turnStepMax, turnStepMax);
				} else {
					if (TrueRandom.nextFloat() < 0.5F) {
						this.deltaTangage = turnStepMax;
					} else {
						this.deltaTangage = -turnStepMax;
					}
					this.deltaAzimuth = TrueRandom.nextFloat(-turnStepMax, turnStepMax);
				}
				this.ivanTimeLeft = TrueRandom.nextFloat(IVAN_TIME_MIN, IVAN_TIME_MAX);
			} else {
				this.deltaAzimuth = this.oldDeltaAzimuth;
				this.deltaTangage = this.oldDeltaTangage;
				this.ivanTimeLeft -= Time.tickLenFs();
				if (this.ivanTimeLeft < Time.tickLenFs()) {
					this.failState = FAIL_TYPE_NONE;
					this.ivanTimeLeft = 0.0F;
				}
			}
		} else if (this.getFailState() == FAIL_TYPE_MIRROR) {
			if (!this.bMirrorValueSet || RndB(0.02F)) {
				if (RndB(0.5F)) {
					this.deltaAzimuth = turnStepMax;
				} else {
					this.deltaAzimuth = -turnStepMax;
				}
				if (RndB(0.5F)) {
					this.deltaTangage = turnStepMax;
				} else {
					this.deltaTangage = -turnStepMax;
				}
				this.bMirrorValueSet = true;
				this.mirrorAzimuth = this.deltaAzimuth;
				this.mirrorTangage = this.deltaTangage;
			} else {
				if (RndB(0.9F)) this.deltaAzimuth = this.mirrorAzimuth;
				if (RndB(0.9F)) this.deltaTangage = this.mirrorTangage;
			}
		} else if (this.getFailState() == FAIL_TYPE_CONTROL_BLOCKED) {
			this.deltaAzimuth = this.oldDeltaAzimuth;
			this.deltaTangage = this.oldDeltaTangage;
		} else {

			if (this.stepMode == STEP_MODE_HOMING) {
				if (this.targetPoint3d.x > -10D) { // don't track if target has been passed
					this.deltaAzimuth = -azimuth;
					if (this.deltaAzimuth > turnStepMax) {
						this.deltaAzimuth = turnStepMax; // limit turn
					}
					if (this.deltaAzimuth < -turnStepMax) {
						this.deltaAzimuth = -turnStepMax; // limit turn
					}
					this.deltaTangage = tangage;
					if (this.deltaTangage > turnStepMax) {
						this.deltaTangage = turnStepMax; // limit turn
					}
					if (this.deltaTangage < -turnStepMax) {
						this.deltaTangage = -turnStepMax; // limit turn
					}
				}
				if (Math.abs(this.oldDeltaAzimuth - this.deltaAzimuth) > this.turnDiffMax) // limit turn rate change
				{
					if (this.oldDeltaAzimuth < this.deltaAzimuth) {
						this.deltaAzimuth = this.oldDeltaAzimuth + this.turnDiffMax;
					} else {
						this.deltaAzimuth = this.oldDeltaAzimuth - this.turnDiffMax;
					}
				}
				if (Math.abs(this.oldDeltaTangage - this.deltaTangage) > this.turnDiffMax) // limit turn rate change
				{
					if (this.oldDeltaTangage < this.deltaTangage) {
						this.deltaTangage = this.oldDeltaTangage + this.turnDiffMax;
					} else {
						this.deltaTangage = this.oldDeltaTangage - this.turnDiffMax;
					}
				}

			} else if (this.stepMode == STEP_MODE_BEAMRIDER) {

				do {
					if (Math.abs(azimuth) > 90) {
						break; // Target outside Radar coverage
					}
					if (Math.abs(tangage) > 90) {
						break; // Target outside Radar coverage
					}

					if ((hTurn * this.oldDeltaAzimuth) < 0.0F) {
						this.deltaAzimuth = hTurn * this.turnDiffMax;
					} else {
						this.deltaAzimuth = this.oldDeltaAzimuth + (hTurn * this.turnDiffMax);
						if (this.deltaAzimuth < -turnStepMax) {
							this.deltaAzimuth = -turnStepMax;
						}
						if (this.deltaAzimuth > turnStepMax) {
							this.deltaAzimuth = turnStepMax;
						}
					}

					if ((vTurn * this.oldDeltaTangage) < 0.0F) {
						this.deltaTangage = vTurn * this.turnDiffMax;
					} else {
						this.deltaTangage = this.oldDeltaTangage + (vTurn * this.turnDiffMax);
						if (this.deltaTangage < -turnStepMax) {
							this.deltaTangage = -turnStepMax;
						}
						if (this.deltaTangage > turnStepMax) {
							this.deltaTangage = turnStepMax;
						}
					}

				} while (false);
			}
			this.oldDeltaAzimuth = this.deltaAzimuth;
			this.oldDeltaTangage = this.deltaTangage;
			// System.out.println("computeMissilePath oDA=" + this.oldDeltaAzimuth + ", oDT=" + this.oldDeltaTangage + ", tDM=" + this.turnDiffMax + ", tSM=" + turnStepMax);
		}
		this.missileOrient.increment(this.deltaAzimuth, this.deltaTangage, 0.0F);
		this.missileOrient.setYPR(this.missileOrient.getYaw(), this.missileOrient.getPitch(), this.getRoll());
		// } else {// in the first second after launch, recover from possible launch pitch.
		// this.computeNoTrackPath();
		// }
	}

	private void computeNoTrackPath() {
		if (this.launchType == Missile.LAUNCH_TYPE_DROP) {
			// float launchYaw = (float) this.getLaunchYaw();
			// float launchPitch = (float) this.getLaunchPitch();
			// float launchRoll = this.getRoll();
			// float launchTimeFactor = (float)this.getLaunchTimeFactor();
			// System.out.println(launchTimeFactor+";"+launchYaw+";"+launchPitch+";"+launchRoll+";"+this.launchKren);

			// this.dropFlightPathOrient.setYPR((float) this.getLaunchYaw(), (float) this.getLaunchPitch(), this.getRoll());
			// float smoothingFactor = 0.0F;
			// float orYPR[] = new float[3];
			// float orFPYPR[] = new float[3];
			// this.missileOrient.getYPR(orYPR);
			// this.oldDeltaAzimuth = orYPR[0];
			// this.oldDeltaTangage = orYPR[1];
			// this.dropFlightPathOrient.getYPR(orFPYPR);
			// for (int i = 0; i < 2; i++) {
			// orYPR[i] = (orYPR[i] * smoothingFactor + orFPYPR[i]) / (smoothingFactor + 1.0F);
			// }
			// // this.missileOrient.setYPR(orYPR[0], orYPR[1], orYPR[2]);
			// this.missileOrient.setYPR(orYPR[0], orYPR[1], this.getRoll());
			// this.oldDeltaAzimuth = orYPR[0] - this.oldDeltaAzimuth;
			// this.oldDeltaTangage = orYPR[1] - this.oldDeltaTangage;

			this.dropFlightPathOrient.setYPR((float) this.getLaunchYaw(), (float) this.getLaunchPitch(), this.getRoll());
			this.missileOrient.setYPR(this.dropFlightPathOrient.getYaw(), this.dropFlightPathOrient.getPitch(), this.getRoll());

			// this.dropFlightPathOrient.setYPR(launchYaw, launchPitch, launchRoll);
			// this.missileOrient.setYPR(launchYaw, launchPitch, launchRoll);
			// this.oldDeltaAzimuth = launchYaw - this.oldDeltaAzimuth;
			// this.oldDeltaTangage = launchPitch - this.oldDeltaTangage;
		} else // fly straight if no launch pitch.
		{
			this.missileOrient.setYPR(this.missileOrient.getYaw(), this.missileOrient.getPitch(), this.getRoll());
		}
		// this.deltaAzimuth = this.deltaTangage = this.oldDeltaAzimuth = this.oldDeltaTangage = 0.0F;
	}

	private void createAdditionalFlames(int stage) {
		this.flames = new Actor[this.exhausts];
		this.flames[0] = this.flame;
		if (stage == 0 && this.simFlame == null) return;
		if (stage == 1 && this.simFlameSustain == null) return;
		Hook theHook = null;
		for (int i = 1; i < this.exhausts; i++) {
			theHook = this.findHook("_SMOKE" + i);
			this.flames[i] = new ActorSimpleMesh((stage == 0) ? this.simFlame : this.simFlameSustain);
			if (this.flames[i] != null) {
				((ActorSimpleMesh) this.flames[i]).mesh().setScale(1.0F);
				this.flames[i].pos.setBase(this, theHook, false);
				this.flames[i].pos.changeHookToRel();
				this.flames[i].pos.resetAsBase();
				if (Config.isUSE_RENDER())
					this.flames[i].drawing(true);
			}
		}
	}

	private void createAdditionalSmokes(int stage) {
		this.smokes = new Eff3DActor[this.exhausts];
		this.smokes[0] = this.smoke;
		if (stage == 0 && this.effSmoke == null) return;
		if (stage == 1 && this.effSmokeSustain == null) return;
		if (stage == 2 && this.effSmokeTrail == null) return;
		Hook theHook = null;
		String ssm = null;
		switch(stage) {
		default:
		case 0:
			ssm = this.effSmoke;
			break;
		case 1:
			ssm = this.effSmokeSustain;
			break;
		case 2:
			ssm = this.effSmokeTrail;
			break;
		}
		for (int i = 1; i < this.exhausts; i++) {
			theHook = this.findHook("_SMOKE" + i);
			if (theHook == null) {
				this.smokes[i] = null;
				continue;
			}
			this.smokes[i] = Eff3DActor.New(this, theHook, null, 1.0F, ssm, -1F);
			if (this.smokes[i] != null) {
				this.smokes[i].pos.changeHookToRel();
			}
		}
	}

	private void createAdditionalSprites(int stage) {
		this.sprites = new Eff3DActor[this.exhausts];
		this.sprites[0] = this.sprite;
		if (stage == 0 && this.effSprite == null) return;
		if (stage == 1 && this.effSpriteSustain == null) return;
		Hook theHook = null;
		for (int i = 1; i < this.exhausts; i++) {
			theHook = this.findHook("_SMOKE" + i);
			if (theHook == null) {
				this.sprites[i] = null;
				continue;
			}
			this.sprites[i] = Eff3DActor.New(this, theHook, null, this.missileKalibr, (stage == 0) ? this.effSprite : this.effSpriteSustain, -1F);
			if (this.sprites[i] != null) {
				this.sprites[i].pos.changeHookToRel();
			}
		}
	}

	/**
	 * Creates The Nav Lights according to Base Hook Name and Effect Name
	 *
	 * @param theNavLightHookName
	 * @param theEffectName
	 */
	private void createNamedNavLights(String theNavLightHookName, String theEffectName) {
		int numNavLights = this.getNumNavLights(theNavLightHookName);
		if (numNavLights == 0) return;
		Hook theHook = null;
		for (int i = 0; i < numNavLights; i++) {
			if (i == 0) {
				theHook = this.findHook(theNavLightHookName);
			} else {
				theHook = this.findHook(theNavLightHookName + i);
			}
			MissileNavLight theNavLight = new MissileNavLight(Eff3DActor.New(this, theHook, null, 1.0F, theEffectName, -1.0F));
			if (this.firstNavLight == null) {
				this.firstNavLight = theNavLight;
				this.lastNavLight = theNavLight;
			} else {
				this.lastNavLight.nextNavLight = theNavLight;
				this.lastNavLight = theNavLight;
			}
		}
	}

	/**
	 * Creates Nav Lights according to the missile's mesh.
	 * Three Nav Light Types exist:
	 * "_NavLightR" : Red Flare
	 * "_NavLightG" : Green Flare
	 * "_NavLightW" : White Flare (very bright one)
	 * "_NavLightP" : Black Ball (currently useless)
	 *
	 * First Nav Light has no index, following Nav Lights start with Index "1".
	 *
	 * Example: A Green and a Red Nav Light are set through following Hooks:
	 * _NavLightG
	 * _NavLightR
	 *
	 * Example2: 2 Green Flares and 3 Black Balls are set through following Hooks:
	 * _NavLightG
	 * _NavLightG1
	 * _NavLightP
	 * _NavLightP1
	 * _NavLightP2
	 */
	private void createNavLights() {
		this.createNamedNavLights("_NavLightR", "3DO/Effects/Fireworks/FlareRed.eff");
		this.createNamedNavLights("_NavLightG", "3DO/Effects/Fireworks/FlareGreen.eff");
		this.createNamedNavLights("_NavLightW", "3DO/Effects/Fireworks/FlareWhite.eff");
		this.createNamedNavLights("_NavLightP", "3DO/Effects/Fireworks/PhosfourousBall.eff");
	}

	public void destroy() {
		if (this.isNet() && this.isNetMirror()) {
			this.doExplosionAir();
		}
		this.endNavLights();
		this.flameActive = false;
		this.smokeActive = false;
		this.spriteActive = false;
		this.showingTrailSmoke = false;
		this.endSmoke();
		this.victim = null;
		this.lockingVictim = false;
		this.startTime = 0L;
		this.previousDistance = 1000F;
		if (this.myActiveMissile != null) {
			GuidedMissileUtils.removeActiveMissile(this.myActiveMissile);
			this.myActiveMissile = null;
		}
		if (this instanceof MissileInterceptable) {
			Engine.targets().remove(this);
		}
		// TODO: For RWR
		Engine.missiles().remove(this);
		super.destroy();
	}

	private void doStart(float f) {
		// super.start(-1F, 0);
		if (this.waitingMeshFly) {
			setMesh(MeshShared.get(this.mshFly));
			this.waitingMeshFly = false;
		}
		this.startMissile(-1F, 0);
		this.setMissileEffects();
		this.setMissileStartParams();
		if (this.isNet() && this.isNetMirror()) return;
		this.setMissileDropParams();
		this.setMissileVictim();
		this.myActiveMissile = new ActiveMissile(this, this.getOwner(), this.victim, (Actor.isValid(this.getOwner())) ? this.getOwner().getArmy() : Integer.MAX_VALUE, (Actor.isValid(this.victim)) ? this.victim.getArmy() : Integer.MAX_VALUE, this.ownerIsAI());
		GuidedMissileUtils.addActiveMissile(this.myActiveMissile);
		this.startEngineDone();
		
		//TODO: reset missile FOV
		
				
			if(this.maxFOVfrom_real!=0.0F)  //Checks if missile has two different FOV's in code
			{
				System.out.println(
						"Missile FOV changing to: "
				        + this.maxFOVfrom_real 
				        + "° /from: " 
				        + this.maxFOVfrom    + "°"
					);  //Feedback in log
			this.maxFOVfrom = this.maxFOVfrom_real; //FOV changed to post-launch FOV
			
			}
			else
			{
				System.out.println(
						"Missile FOV not changed !!! "
				        + this.maxFOVfrom    + "°"
					);  //Feedback in log
			}
		if ( bLogDetail && Actor.isValid(this) ) System.out.println("Missile(" + actorString(this) + ") - doStart(f), recording new ActiveMissile(): getOwner()=" + actorString(this.getOwner()) + "; " + (Actor.isValid(this.getOwner()) ? ("Valid getOwner().getArmy()=" + getOwner().getArmy()) : "Invalid") + " - collisionR()=" + this.mesh().collisionR() + " , visibilityR()=" + this.mesh().visibilityR());
		if ( bLogDetail && Actor.isValid(this) ) iStatusCur_getArmy |= Integer.parseInt("0000000000000000000010000000000", 2);
/*		if (this.victim == null)
			System.out.println("Owner " + this.getOwner().hashCode() + " Active Missile added for victim=null");
		else
			System.out.println("Owner " + this.getOwner().hashCode() + " Active Missile added for victim=" + this.victim.hashCode() + " (" + this.victim.getClass().getName() + ")");
		System.out.println("Active Missile added for victim " + this.victim.hashCode());
	*/}

	private void endAllFlame() {
		if (this.exhausts < 2) {
			if (this.flame != null) {
				ObjState.destroy(this.flame);
			}
		} else if (this.flames != null) { // SAS Engine Mod 2.6 Hotfix: Check if no flames have been defined.
			for (int i = 0; i < this.exhausts; i++) {
				if (this.flames[i] == null) {
					continue;
				}
				ObjState.destroy(this.flames[i]);
			}
		}
		if (this.light != null) {
			this.light.light.setEmit(0.0F, 1.0F);
		}
		this.stopSounds();
		this.endedFlame = true;
		this.bRocketFiring = false;
	}

	private void endAllSmoke() {
		if (this.exhausts < 2) {
			if (this.smoke != null) {
				Eff3DActor.finish(this.smoke);
			}
		} else if (this.smokes != null) { // SAS Engine Mod 2.6 Hotfix: Check if no smokes have been defined.
			for (int i = 0; i < this.exhausts; i++) {
				if (this.smokes[i] == null) {
					continue;
				}
				Eff3DActor.finish(this.smokes[i]);
			}
		}
		this.endedSmoke = true;
	}

	private void endAllSprites() {
		if (this.exhausts < 2) {
			if (this.sprite != null) {
				Eff3DActor.finish(this.sprite);
			}
		} else if (this.sprites != null) { // SAS Engine Mod 2.6 Hotfix: Check if no sprites have been defined.
			for (int i = 0; i < this.exhausts; i++) {
				if (this.sprites[i] == null) {
					continue;
				}
				Eff3DActor.finish(this.sprites[i]);
			}
		}
		this.endedSprite = true;
	}

	/**
	 * Finish all Nav Lights in the chained list.
	 * No action required if there are no Nav Lights.
	 */
	private void endNavLights() {
		MissileNavLight theNavLight = this.firstNavLight;
		while (theNavLight != null) {
			Eff3DActor.finish(theNavLight.theNavLight);
			theNavLight = theNavLight.nextNavLight;
		}
	}

	// private void initRandom() {
	// if (theRangeRandom != null)
	// return;
	// long lTime = System.currentTimeMillis();
	// SecureRandom secRandom = new SecureRandom();
	// secTrueRandom.setSeed(lTime);
	// long lSeed1 = (long) secTrueRandom.nextInt();
	// long lSeed2 = (long) secTrueRandom.nextInt();
	// long lSeed = (lSeed1 << 32) + lSeed2;
	// theRangeRandom = new RangeRandom(lSeed);
	//
	// }
	//
	// private float nextFloat(float fMin, float fMax) {
	// this.initRandom();
	// return theRangeTrueRandom.nextFloat(fMin, fMax);
	// }
	//
	// private float nextFloat() {
	// this.initRandom();
	// return theRangeTrueRandom.nextFloat();
	// }
	//
	// private int nextInt(int iMin, int iMax) {
	// this.initRandom();
	// return theRangeTrueRandom.nextInt(iMin, iMax);
	// }

	protected void endSmoke() {
		if (!this.smokeActive && !this.endedSmoke) {
			this.endAllSmoke();
		}
		if (!this.spriteActive && !this.endedSprite) {
			this.endAllSprites();
		}
		if (!this.flameActive && !this.endedFlame) {
			this.endAllFlame();
		}
	}

	public int getArmy() {
		if ( bLogDetail ) {
			iStatusCur_getArmy |= Integer.parseInt("0000000000000000000000000000010", 2);
			if (!Actor.isValid(this.getOwner())) iStatusCur_getArmy |= Integer.parseInt("0000000000000000000000000000100", 2);
			else iStatusCur_getArmy &= Integer.parseInt("1111111111111111111111111111011", 2);

			if (Actor.isValid(this) && iStatusCur_getArmy != iStatusPrev_getArmy) System.out.println("Missile(" + actorString(this) + ") - getArmy(): getOwner()=" + actorString(this.getOwner()) + "; " + (Actor.isValid(this.getOwner()) ? ("Valid getOwner().getArmy()=" + getOwner().getArmy()) : "Invalid") + " , iStatusCur_getArmy=" + iStatusCur_getArmy);

			iStatusPrev_getArmy = iStatusCur_getArmy;
		}

		if (Actor.isValid(this.getOwner())) return this.getOwner().getArmy();
		return 0;
	}

	private int getFailState() {
		if (this.timeToFailure == 0L) return FAIL_TYPE_NONE;
		long millisecondsFromStart = Time.current() - this.startTime;
		if (millisecondsFromStart < this.timeToFailure) return FAIL_TYPE_NONE;
		if (this.myActiveMissile != null) {
			GuidedMissileUtils.removeActiveMissile(this.myActiveMissile);
			this.myActiveMissile = null;
		}
		if (this.failState == FAIL_TYPE_ELECTRONICS) {
			float fRand = TrueRandom.nextFloat();
			if (fRand < 0.01F) return FAIL_TYPE_WARHEAD;
			if (fRand < 0.02F) return FAIL_TYPE_REFLEX;
			if (fRand < 0.2F) return FAIL_TYPE_MIRROR;
			if (fRand < 0.5F) return FAIL_TYPE_CONTROL_BLOCKED;
			return FAIL_TYPE_IVAN;
		}
		return this.failState;
	}

	private FlightModel getFM() {
		if (!this.ownerIsAircraft()) return null;
		return ((Aircraft) this.getOwner()).FM;
	}

	public double getLaunchPitch() {
		double launchPitchTimeFactor = this.getLaunchTimeFactor();
		// double theLaunchPitch = 2.0D * (Math.cos(launchPitchTimeFactor + 0.4D) - 1.02D + launchPitchTimeFactor / 5.0D);
		double theLaunchPitch = 2.0D * (Math.cos(launchPitchTimeFactor + 0.4D) - 2.1759949269D + launchPitchTimeFactor / 5.0D);
		theLaunchPitch *= Math.cos(this.launchKren);
		theLaunchPitch += this.launchPitch;
		while (theLaunchPitch > 180F)
			theLaunchPitch -= 360F;
		while (theLaunchPitch < -180F)
			theLaunchPitch += 360F;
		return theLaunchPitch;
	}

	public double getLaunchTimeFactor() {
		return (Time.current() - this.startTime) / (double) this.trackDelay * 6.0D;
	}

	public double getLaunchYaw() {
		double launchYawTimeFactor = this.getLaunchTimeFactor();
		// double theLaunchYaw = 2.0D * (Math.cos(launchYawTimeFactor + 0.4D) - 1.02D + launchYawTimeFactor / 5.0D);
		double theLaunchYaw = 2.0D * (Math.cos(launchYawTimeFactor + 0.4D) - 2.1759949269D + launchYawTimeFactor / 5.0D);
		theLaunchYaw *= Math.sin(this.launchKren);
		theLaunchYaw += this.launchYaw;
		while (theLaunchYaw > 180F)
			theLaunchYaw -= 360F;
		while (theLaunchYaw < -180F)
			theLaunchYaw += 360F;
		return theLaunchYaw;
	}

	private void getMissileProperties() {
		Class localClass = super.getClass();
		this.MyotkaFOV = Property.floatValue(localClass, "typeMyotka", 0);
		this.launchType = Property.intValue(localClass, "launchType", 0);
		this.stepMode = Property.intValue(localClass, "stepMode", 0);
		this.maxFOVfrom = Property.floatValue(localClass, "maxFOVfrom", 180F);
		this.maxFOVfrom_real = Property.floatValue(localClass, "maxFOVfrom_real", 0F);
		this.maxLaunchG = Property.floatValue(localClass, "maxLockGForce", 99.9F);
		this.maxG = Property.floatValue(localClass, "maxGForce", 12F);
		this.stepsForFullTurn = Property.floatValue(localClass, "stepsForFullTurn", 10);
		this.rocketMotorOperationTime = Property.floatValue(localClass, "timeFire", 2.2F) * 1000F;
		this.missileTimeLife = Property.floatValue(localClass, "timeLife", 30F) * 1000F;
		this.rocketMotorSustainedOperationTime = Property.floatValue(localClass, "timeSustain", 0.0F) * 1000F;
		super.timeFire = (long) (this.rocketMotorOperationTime + this.rocketMotorSustainedOperationTime);
		super.timeLife = (long) this.missileTimeLife;
		this.engineDelayTime = Property.longValue(this.getClass(), "engineDelayTime", 0L);
		this.missileForce = Property.floatValue(localClass, "force", 18712F);
		this.missileSustainedForce = Property.floatValue(localClass, "forceSustain", 0F);
		this.leadPercent = Property.floatValue(localClass, "leadPercent", 0.0F);
		this.missileKalibr = Property.floatValue(localClass, "kalibr", 0.2F);
		this.missileMass = Property.floatValue(localClass, "massa", 86.2F);
		this.massaEnd = Property.floatValue(localClass, "massaEnd", 80.0F);
		this.sunRayAngle = Property.floatValue(localClass, "sunRayAngle", 0.0F);
		this.groundTrackFactor = Property.floatValue(localClass, "groundTrackFactor", 0.0F);
		this.flareLockTime = Property.longValue(localClass, "flareLockTime", 1000L);
		this.trackDelay = Property.longValue(localClass, "trackDelay", 1000L);
		this.frontSquare = 3.141592653589793F * this.missileKalibr * this.missileKalibr / 4.0F;
		this.missileForceRunUpTime = Property.floatValue(localClass, "forceT1", 0.0F) * 1000F;
		this.initialMissileForce = Property.floatValue(localClass, "forceP1", 0.0F);
		this.dragCoefficient = Property.floatValue(localClass, "dragCoefficient", 0.3F);
		this.dragCoefficientTurn = Property.floatValue(localClass, "dragCoefficientTurn", this.dragCoefficient);
		this.attackMaxDistance = Property.floatValue(localClass, "PkDistMax", 5000F);
		this.exhausts = this.getNumExhausts();
		this.effSmoke = Property.stringValue(localClass, "smoke", null);
		this.effSprite = Property.stringValue(localClass, "sprite", null);
		this.simFlame = Property.stringValue(localClass, "flame", null);
		float failureRate = Property.floatValue(localClass, "failureRate", 10.0F);
		this.noSmokeFlameSustain = (Property.intValue(localClass, "noSmokeFlameSustain", 0) == 1);
		this.noSoundSustain = (Property.intValue(localClass, "noSoundSustain", 0) == 1);
		this.soundNameSustain = Property.stringValue(localClass, "soundSustain", null);
		this.effSmokeSustain = Property.stringValue(localClass, "smokeSustain", null);
		this.effSpriteSustain = Property.stringValue(localClass, "spriteSustain", null);
		this.simFlameSustain = Property.stringValue(localClass, "flameSustain", null);
		this.effSmokeTrail = Property.stringValue(localClass, "smokeTrail", null);
		this.mshFly = Property.stringValue(localClass, "meshFly", null);
		this.mshDamage = Property.stringValue(localClass, "meshDamage", null);
		if (this.mshFly != null)
			this.waitingMeshFly = true;
		this.mshSustain = Property.stringValue(localClass, "meshSustain", null);
		if (this.mshSustain != null)
			this.waitingMeshSustain = true;
		if (TrueRandom.nextFloat(0, 100.0F) < failureRate) {
			this.setFailState();
			float randFail = TrueRandom.nextFloat();
			long baseFailTime = this.trackDelay;
			if (this.failState == FAIL_TYPE_WARHEAD) {
				baseFailTime += baseFailTime;
			}
			this.timeToFailure = baseFailTime + (long) ((this.missileTimeLife - baseFailTime) * randFail);
		} else {
			this.failState = FAIL_TYPE_NONE;
			this.timeToFailure = 0L;
		}
		if (this.rocketMotorOperationTime > 0.0F) {
			this.massLossPerTick = ((this.missileMass - this.massaEnd) / (this.rocketMotorOperationTime / 1000F / Time.tickConstLenFs()));
		} else {
			this.massLossPerTick = 0.0F;
		}
		this.iDetectorType = Property.intValue(localClass, "detectorType", DETECTOR_TYPE_MANUAL);
		if (this.iDetectorType == DETECTOR_TYPE_LASER) this.bLaserHoming = true;
		else this.bLaserHoming = false;
		if (this.iDetectorType == DETECTOR_TYPE_SACLOS) this.bSACLOSHoming = true;
		else this.bSACLOSHoming = false;
		this.lTargetType = Property.longValue(localClass, "targetType", TARGET_AIR);
		this.missileProximityFuzeRadius = Property.floatValue(localClass, "proximityFuzeRadius", 50.0F);
		// When proximityFuzeRadius = 0F set, means no Proximity Fuze. So, working as Contact Fuze or Time Fuze (reaching timeLife).
		this.soundNameRadarPW = Property.stringValue(localClass, "soundRadarPW", null);

		this.bRealisticRadarSelect = Config.cur.ini.get("Mods", "RealisticRadarSelect", 0) != 0;
		int iLog = Config.cur.ini.get("Mods", "GuidedMissileDebugLog", 0);
		if ((iLog & 128) == 128) bLogDetail = true;
		if ((iLog & 256) == 256) bLogDetailDamage = true;

		if (this instanceof MissileInterceptable) {
			// damage parameters borrowing from House.class -- Body=FuelSmall
			this.damage_MAT_TYPE = MAT_STEEL;
			this.damage_EXPL_TYPE = 0;
			this.damage_EFF_BODY_MATERIAL = 2;
			this.damage_PANZER = 0.001F;
			this.damage_MIN_TNT = 0.2F * (this.damage_PANZER / 0.002F);
			this.damage_MAX_TNT = this.damage_MIN_TNT * 1.7F;
			this.damage_PROBAB_DEATH_WHEN_EXPLOSION = 0.04F;  // make 4x bigger from 0.01F of house -- Body=FuelSmall
			this.flags &= 0xffffffdf;  // make moving collide enabled
		} else {
			// almost Immortal
			this.damage_MAT_TYPE = MAT_BRICK;
			this.damage_EXPL_TYPE = 4;
			this.damage_EFF_BODY_MATERIAL = 4;
			this.damage_PANZER = 50F;
			this.damage_MIN_TNT = 300F * (this.damage_PANZER / 0.48F);
			this.damage_MAX_TNT = this.damage_MIN_TNT * 1.7F;
			this.damage_PROBAB_DEATH_WHEN_EXPLOSION = 0.001F;
		}
	}

	private int getNumExhausts() {
		if (this.mesh.hookFind("_SMOKE") == -1) return 0;
		int retVal = 1;
		while (this.mesh.hookFind("_SMOKE" + retVal) != -1) {
			retVal++;
		}
		return retVal;
	}

	/**
	 * Counts Number of Nav Lights for Base Hook Name
	 *
	 * @param theNavLightHookName
	 * @return Number of Nav Lights for Base Hook Name
	 */
	private int getNumNavLights(String theNavLightHookName) {
		if (this.mesh.hookFind(theNavLightHookName) == -1) return 0;
		int retVal = 1;
		while (this.mesh.hookFind(theNavLightHookName + retVal) != -1) {
			retVal++;
		}
		return retVal;
	}

	public float getRoll() {
		if ((Time.current() - this.releaseTime) > this.trackDelay) return 0F;
		float fRollCalcAbscissa = ((float) (Time.current() - this.startTime) / (float) this.trackDelay) * (float) Math.PI;
		float fRollCalcOrdinate = ((float) Math.cos(fRollCalcAbscissa) + 1F) * 0.5F;
		float fRet = 360F - (fRollCalcOrdinate * (360F - this.oldRoll));
		while (fRet > 180F)
			fRet -= 360F;
		while (fRet < -180F)
			fRet += 360F;
		return fRet;
	}

	public boolean getShotpointOffset(int i, Point3d point3d) {
		if (i != 0) return false;
		if (point3d != null) {
			point3d.set(0.0D, 0.0D, 0.0D);
		}
		return true;
	}

	public long getStartTime() {
		return this.startTime;
	}

	// TODO: For Countermeasures
	public Actor getMissileTarget() {
		return this.victim;
	}

	public Point3d getMissileTargetPoint3d() {
		return this.targetPoint3d;
	}

	public Point3d getMissileTargetPoint3dAbs() {
		return this.targetPoint3dAbs;
	}

	public boolean getLockStatus() {
		return this.lockingVictim;
	}

	public int getDetectorType() {
		return this.iDetectorType;
	}

	public long getTargetType() {
		return this.lTargetType;
	}

	public boolean getRocketFiring() {
		return this.bRocketFiring;
	}

	public String getSoundstringRadarPW() {
		return this.soundNameRadarPW;
	}

	public int HitbyMask() {
		return -1;
	}

	public boolean interpolateStep() {
		if (this.engineDelayTime > 0L) {
			if (!this.engineRunning) {
				if (Time.current() > this.releaseTime + this.engineDelayTime) {
					this.startEngine();
				}
				this.startTime = Time.current();
			}
		}
		if (Time.current() > this.startTime + this.trackDelay) {
			if (this.isSunTracking() || this.isGroundTracking()) {
				this.victim = null;
			}
			if (this.iDetectorType == DETECTOR_TYPE_RADAR_HOMING && this.victim != null) {
				if (!Actor.isValid(this.getOwner())) {
					this.victim = null;
				} else {
					FlightModelMain ownerfm = ((Aircraft)this.getOwner()).FM;
					if (this.getOwner() instanceof TypeSemiRadar &&
						(((ownerfm instanceof RealFlightModel)) && (((RealFlightModel) ownerfm).isRealMode()) && (ownerfm instanceof Pilot))) {
			//			if (/*bRealisticRadarSelect && */(!((TypeSemiRadar) this.getOwner()).getSemiActiveRadarOn() || ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarLockedActor() != this.victim)) {
				//			this.lockingVictim = false;
					//		this.victim = null;
						//} else {
//							this.victim = ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarLockedActor();
	//						this.lockingVictim = true;
		//				}
						
						
						if( ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarOn() &&  ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarLockedActor() != null )
						{
							this.victim = ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarLockedActor();
							this.lockingVictim = true;
						}
						else{
							this.lockingVictim = false;
							this.victim = null;
						}
						
					} else {
						if (GuidedMissileUtils.angleBetween(this.getOwner(), this.victim) > 45.0F ||
							GuidedMissileUtils.pitchBetween(this.getOwner(), this.victim) > 45.0F) {
							this.lockingVictim = false;
						} else {
							this.lockingVictim = true;
						}
					}
				}
			}
		}
		// float fSpeed = (float) this.getSpeed((Vector3d) null) * 3.6F;
		// if (fSpeed > this.fMaxSpeed) {
		// this.fMaxSpeed = fSpeed;
		// }
		// HUD.training("" + this.twoPlaces.format(fSpeed) + " / " + this.twoPlaces.format(this.fMaxSpeed));
		switch (this.stepMode) {
		case STEP_MODE_HOMING: {
			return this.stepTargetHoming();
		}
		case STEP_MODE_BEAMRIDER: {
			return this.stepBeamRider();
		}
		default:
			break;
		}
		return true;
	}

	private boolean isGroundTracking() {
		if (this.groundTrackFactor == 0.0F) return false; // No Ground Clutter Tracking possible
		this.pos.getAbs(this.missilePoint3d, this.missileOrient);
		this.missileOrient.wrap();
		float ttG = this.missileOrient.getTangage() * -1.0F;
		float missileAlt = (float) (this.missilePoint3d.z - Engine.land().HQ_Air(this.missilePoint3d.x, this.missilePoint3d.y));
		missileAlt /= 1000.0F;
		float groundFactor = ttG / (missileAlt * missileAlt);
		long lTimeCurrent = Time.current();
		if (this.lastTimeNoFlare == 0L) {
			this.lastTimeNoFlare = lTimeCurrent;
		}
		if (groundFactor > this.groundTrackFactor) {
			if (lTimeCurrent < this.lastTimeNoFlare + this.flareLockTime) return false;
			return true;
		}
		this.lastTimeNoFlare = lTimeCurrent;
		return false;
	}

	public boolean isReleased() {
		return (this.releaseTime != 0L);
	}

	private boolean isSunTracking() {
		if (this.sunRayAngle == 0.0F) return false; // No Sun Ray Tracking possible

		float sunAngle = GuidedMissileUtils.angleBetween(this, World.Sun().ToSun);

		if (sunAngle < this.sunRayAngle) return true;
		return false;
	}

	public final void MissileInit() {
		this.victim = null;
		this.lockingVictim = false;
		this.startTime = 0L;
		this.previousDistance = 1000F;
		this.deltaAzimuth = 0.0F;
		this.deltaTangage = 0.0F;
		this.oldDeltaAzimuth = 0.0F;
		this.oldDeltaTangage = 0.0F;
		this.endedFlame = false;
		this.endedSmoke = false;
		this.endedSprite = false;
		this.flameActive = true;
		this.smokeActive = true;
		this.spriteActive = true;
		this.getMissileProperties();
	}

	public final void MissileInit(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		if (Actor.isValid(actor)) {
			if (!(Actor.isValid(this.getOwner()))) {
				this.setOwner(actor);
			}
		}
		this.victim = null;
		this.lockingVictim = false;
		this.startTime = 0L;
		this.previousDistance = 1000F;
		this.oldDeltaAzimuth = 0.0F;
		this.oldDeltaTangage = 0.0F;
		this.endedFlame = false;
		this.endedSmoke = false;
		this.endedSprite = false;
		this.flameActive = true;
		this.smokeActive = true;
		this.spriteActive = true;
		this.net = new Mirror(this, netchannel, i);
		this.pos.setAbs(point3d, orient);
		this.pos.reset();
		this.pos.setBase(actor, null, true);
		this.trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
		orient.transform(this.trajectoryVector3d);
		this.trajectoryVector3d.scale(f);
		this.setSpeed(this.trajectoryVector3d);
		this.collide(false);
		this.getMissileProperties();
	}

	public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
		NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
		netmsgspawn.writeNetObj(this.getOwner().net);
		Point3d point3d = this.pos.getAbsPoint();
		netmsgspawn.writeFloat((float) point3d.x);
		netmsgspawn.writeFloat((float) point3d.y);
		netmsgspawn.writeFloat((float) point3d.z);
		Orient orient = this.pos.getAbsOrient();
		netmsgspawn.writeFloat(orient.azimut());
		netmsgspawn.writeFloat(orient.tangage());
		netmsgspawn.writeFloat(orient.kren());
		float f = (float) this.getSpeed(null);
		netmsgspawn.writeFloat(f);
		return netmsgspawn;
	}

	private boolean ownerIsAI() {
		if (this.getFM() == null) return true;
		if ((this.getOwner() != World.getPlayerAircraft() || !((RealFlightModel) this.getFM()).isRealMode()) && (this.getFM() instanceof Pilot)) return true;
		return false;
	}

	private boolean ownerIsAircraft() {
		if (this.getOwner() instanceof Aircraft) return true;
		return false;
	}

	public void runupEngine() {
		float millisecondsFromStart = Time.current() - this.startTime;
		float runUpTimeFactor = millisecondsFromStart / this.missileForceRunUpTime;
		if (runUpTimeFactor > 1.0F) {
			runUpTimeFactor = 1.0F;
		}
		this.setAllSmokeIntensities(runUpTimeFactor);
		this.setAllSpriteIntensities(runUpTimeFactor);
	}

	private void setAllSmokeIntensities(float theIntensity) {
		if (!this.engineRunning) return;
		if (this.exhausts < 2) {
			if (this.smoke != null) {
				Eff3DActor.setIntesity(this.smoke, theIntensity);
			}
		} else if (this.smokes != null) { // SAS Engine Mod 2.6 Hotfix: Check if no smokes have been defined.
			for (int i = 0; i < this.exhausts; i++) {
				if (this.smokes[i] == null) {
					continue;
				}
				Eff3DActor.setIntesity(this.smokes[i], theIntensity);
			}
		}
	}

	private void setAllSpriteIntensities(float theIntensity) {
		if (!this.engineRunning) return;
		if (this.exhausts < 2) {
			if (this.sprite != null) {
				Eff3DActor.setIntesity(this.sprite, theIntensity);
			}
		} else if (this.sprites != null) { // SAS Engine Mod 2.6 Hotfix: Check if no sprites have been defined.
			for (int i = 0; i < this.exhausts; i++) {
				if (this.sprites[i] == null) {
					continue;
				}
				Eff3DActor.setIntesity(this.sprites[i], theIntensity);
			}
		}
	}

	private void setFailState() {
		if (this.failState == FAIL_TYPE_NONE) {
			this.failState = TrueRandom.nextInt(1, FAIL_TYPE_NUMBER);
		}
	}

	private void setMissileDropParams() {
		switch (this.launchType) {
		default:
		case LAUNCH_TYPE_STRAIGHT: // simply ignite and leave rail
			this.oldRoll = this.missileOrient.getRoll();
			break;
		case LAUNCH_TYPE_QUICK: // "swoosh" off rocket rail
			this.missileBaseSpeed += 20.0F;
			this.trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
			this.missileOrient.transform(this.trajectoryVector3d);
			this.trajectoryVector3d.scale(this.missileBaseSpeed);
			this.setSpeed(this.trajectoryVector3d);
			// this.launchKren = Math.toRadians(this.missileOrient.getKren());
			this.launchKren = Math.toRadians(this.missileOrient.getRoll());
			this.launchYaw = this.missileOrient.getYaw();
			this.launchPitch = this.missileOrient.getPitch();
			this.oldRoll = this.missileOrient.getRoll();
			this.missileOrient.setYPR((float) this.launchYaw, (float) this.launchPitch, this.oldRoll);
			this.pos.setAbs(this.missilePoint3d, this.missileOrient);
			break;
		case LAUNCH_TYPE_DROP: // drop pattern
			// start with -6° pitch if launched from Aircraft
			// this.launchKren = Math.toRadians(this.missileOrient.getKren());
			this.launchKren = Math.toRadians(this.getOwner().pos.getAbsOrient().getRoll());
			if (this.ownerIsAircraft()) {
				this.launchYaw = this.missileOrient.getYaw() - (this.getFM().getAOA() * (float) Math.sin(this.launchKren));
				this.launchPitch = this.missileOrient.getPitch() - (this.getFM().getAOA() * (float) Math.cos(this.launchKren));
				this.oldRoll = this.missileOrient.getRoll();
			} else { // if not launched from Aircraft, start in owner's current direction
				this.launchYaw = this.getOwner().pos.getCurrentOrient().getYaw();
				this.launchPitch = this.getOwner().pos.getCurrentOrient().getPitch();
				this.oldRoll = this.getOwner().pos.getCurrentOrient().getRoll();
			}

			this.dropFlightPathOrient.setYPR((float) this.launchYaw + (0.5F * (float) Math.sin(this.launchKren)), (float) this.launchPitch - (0.5F * (float) Math.cos(this.launchKren)), this.oldRoll);

			// this.or.setYPR(
			// (float) this.launchYaw,
			// (float) this.launchPitch,
			// fOldRoll);
			// pos.setAbs(this.p, this.or);
			break;
		}
	}

	public void setMissileEffects() {
		this.firstNavLight = null;
		this.lastNavLight = null;
		if (Config.isUSE_RENDER()) {
			this.createNavLights();
		}
	}

	private void setMissileStartParams() {
		this.previousDistance = 1000.0F;
		this.pos.getRelOrient().transformInv(this.speed);
		this.speed.y *= 3.0D;
		this.speed.z *= 3.0D;
		this.speed.x -= 198.0D;
		this.pos.getRelOrient().transform(this.speed);
		this.setStartTime();
		this.pos.getAbs(this.missilePoint3d, this.missileOrient);

		this.missileBaseSpeed = (float) this.getSpeed((Vector3d) null);
	}

	public void update (float f)
	{
		if(this.iDetectorType == DETECTOR_TYPE_RADAR_HOMING && this.getOwner() instanceof TypeSemiRadar)
		{
			if( ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarOn() && ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarLockedActor() != null)
			{
				this.victim = ((TypeSemiRadar) this.getOwner()).getSemiActiveRadarLockedActor();
				this.lockingVictim = true;
				return;
			}
			else{
				this.lockingVictim = false;
				this.victim = null;
				return;
			}
		}
	}
	private void setMissileVictim() {
		this.victim = null;
		this.lockingVictim = false;
//:TODO
		/*if (iDetectorType == 6 || iDetectorType == 5)
		{
			Actor owner = this.getOwner();
	    	this.victim = ((TypeHARM_carrier)owner).getHarmVictim();
	    	//this.loft_mode = ((TypeHARM_carrier)owner).getHarmLOFT();
	    	//if(this.loft_mode > 100)this.loft_mode=100; if(this.loft_mode<0)this.loft_mode=0;
	    	this.HARM_MODE = new String();
	    	this.HARM_MODE = ((TypeHARM_carrier)owner).getHarmMode();
	    	this.HARM_WP = new Point3d();
	    	this.HARM_WP = ((TypeHARM_carrier)owner).getHarmWP();
	    	((TypeHARM_carrier)owner).setNewHARMVictim();
	    	this.lockingVictim = true;
	    	//this.lockingVictim = true;
	    	System.out.println(
					"Setting missile victim"
				);
	    	this.targetPoint3dAbs = null;
	    	this.targetPoint3dAbs = this.victim.pos.getAbsPoint();

			return;
		}*/
		
		if (bLaserHoming) {
			if (!this.ownerIsAI()) {
				if (this.getFM().getOverload() > this.maxLaunchG) {
					this.targetPoint3d = null;
					this.targetPoint3dAbs = null;
					this.laserOwner = null;
					return;
				}
			}
			if (this.getOwner() instanceof TypeGuidedMissileCarrier && this.getOwner() instanceof TypeLaserDesignator && (((TypeLaserDesignator) this.getOwner()).getLaserOn())) {
				this.targetPoint3dAbs = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTargetPos();
				this.laserOwner = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTargetPosOwner();
				this.victimOffsetPoint3f = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTargetOffset();

				if (this.targetPoint3dAbs == null || this.laserOwner == null || this.victimOffsetPoint3f == null)
					return;

				this.safeVictimOffset.set(this.victimOffsetPoint3f);
				this.targetPoint3d.set(this.targetPoint3dAbs);
				this.targetPoint3d.sub(this.missilePoint3d); // relative Position to target
			}
			return;
		}

		if (bSACLOSHoming) {
			if (!this.ownerIsAI()) {
				if (this.getFM().getOverload() > this.maxLaunchG) {
					this.targetPoint3d = null;
					this.targetPoint3dAbs = null;
					this.saclosOwner = null;
					return;
				}
			}
			if ((this.getOwner() instanceof TypeGuidedMissileCarrier) && (this.getOwner() instanceof TypeSACLOS) && (((TypeSACLOS) this.getOwner()).getSACLOSenabled())) {
				this.targetPoint3dAbs = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTargetPos();
				this.saclosOwner = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTargetPosOwner();
				this.victimOffsetPoint3f = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTargetOffset();

				if (this.targetPoint3dAbs == null || this.saclosOwner == null || this.victimOffsetPoint3f == null)
					return;

				this.safeVictimOffset.set(this.victimOffsetPoint3f);
				this.targetPoint3d.set(this.targetPoint3dAbs);
				this.targetPoint3d.sub(this.missilePoint3d); // relative Position to target
			}
			return;
		}

		try {
			if (this.ownerIsAI()) {
				if (this.getOwner() instanceof TypeGuidedMissileCarrier) {
					this.victim = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTarget();
					if (GuidedMissileUtils.distanceBetween(this.getOwner(), this.victim) < 1500F && this.getOwner().getArmy() == this.victim.getArmy()) {
						// to avoid AI friendly fire
						this.victim = null;
					} else {
						this.lockingVictim = true;
						this.victimOffsetPoint3f = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTargetOffset();
						this.safeVictimOffset.set(this.victimOffsetPoint3f);
					}
				} else if (this.getOwner() instanceof TypeFighter) {
					if (this.getFM() != null) {
						this.victim = ((Pilot) this.getFM()).target.actor;
						this.lockingVictim = true;
					}
				}
			} else {
				if (this.getFM() != null) {
					if (this.getFM().getOverload() > this.maxLaunchG) {
						this.victim = null;
						return;
					}
				}
				
				if (this.getOwner() instanceof TypeGuidedMissileCarrier) {
					this.victim = ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileTarget();
					this.lockingVictim = true;
					this.safeVictimOffset.set(this.victimOffsetPoint3f);
				} else {
					this.victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
					if (this.victim == null) {
						this.victim = Main3D.cur3D().getViewPadlockEnemy();
						this.lockingVictim = true;
					}
				}
			}
		} catch (Exception exception) {	}
		
	}

	private void setSmokeSpriteFlames(int stage) {
		if (this.exhausts > 1) {
			if (this.smoke != null) {
				this.createAdditionalSmokes(stage);
			}
			if (this.sprite != null) {
				this.createAdditionalSprites(stage);
			}
			if (this.flame != null) {
				this.createAdditionalFlames(stage);
			}
		}
	}

	public void setStartTime() {
		// EventLog.type("setStartTime 1");
		if (this.startTimeIsSet) return;
		this.startTime = Time.current();
		this.startTimeIsSet = true;
		// EventLog.type("setStartTime 2");
	}

	public void start(float f) {
		this.start(f, 0);
	}

	public void start(float f, int paramInt) {
		Actor actor = this.pos.base();
		try {
			if (Actor.isValid(actor)) {
				if (actor.isNetMirror()) {
					this.destroy();
					return;
				}
				this.net = new Master(this);
			}
		} catch (Exception exception) {
			GuidedMissileUtils.LocalLog(this.getOwner(), "Missile launch cancelled (system error):" + exception.getMessage());
			this.destroy();
		}
		this.doStart(f);
	}

	public void startEngine() {
		if (this.engineRunning)
			return;
		// EventLog.type("startEngine");

		Class localClass = this.getClass();

		Hook localHook = null;
		String str = this.effSprite;
		if (str != null) {
			if (localHook == null) {
				localHook = this.findHook("_SMOKE");
			}
			this.sprite = Eff3DActor.New(this, localHook, null, 1.0F, str, -1.0F);
			if (this.sprite != null) {
				this.sprite.pos.changeHookToRel();
			}
		}
		str = this.simFlame;
		if (str != null) {
			if (localHook == null) {
				localHook = this.findHook("_SMOKE");
			}
			this.flame = new ActorSimpleMesh(str);
			if (this.flame != null) {
				((ActorSimpleMesh) this.flame).mesh().setScale(1.0F);
				this.flame.pos.setBase(this, localHook, false);
				this.flame.pos.changeHookToRel();
				this.flame.pos.resetAsBase();
				if (Config.isUSE_RENDER())
					this.flame.drawing(true);
			}
			this.bRocketFiring = true;
		}
		str = this.effSmoke;
		if (str != null) {
			if (localHook == null) {
				localHook = this.findHook("_SMOKE");
			}
			this.smoke = Eff3DActor.New(this, localHook, null, 1.0F, str, -1.0F);
			if (this.smoke != null) {
				this.smoke.pos.changeHookToRel();
			}
		}
		setSmokeSpriteFlames(0);
		this.soundName = Property.stringValue(localClass, "sound", null);
		if (this.soundName != null) {
			this.newSound(this.soundName, true);
			playingMotorSound = true;
		}
		this.engineRunning = true;
	}

	public void startEngineDone() {
		this.releaseTime = Time.current();
		// TODO: For RWR and intercept
		// "MissileInterceptable" is also recorded as a target,
		// able to shoot down by AAMs or SAMs.
		// Other missiles are recorded only as missiles,
		// possible only countermeasure -- no shooting down.
		if (this instanceof MissileInterceptable) {
			Engine.targets().add(this);
			setName(this.getOwner().name() + "_" + ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().getMissileName() + this.hashCode());
		}

		Engine.missiles().add(this);
	}

	public void sustainEngine() {
		// EventLog.type("sustainEngine");

		Class localClass = this.getClass();

		Hook localHook = null;
		String str = this.effSpriteSustain;
		if (str != null) {
			if (localHook == null) {
				localHook = this.findHook("_SMOKE");
			}
			this.sprite = Eff3DActor.New(this, localHook, null, 1.0F, str, -1.0F);
			if (this.sprite != null) {
				this.sprite.pos.changeHookToRel();
				this.spriteActive = true;
			}
		}
		str = this.simFlameSustain;
		if (str != null) {
			if (localHook == null) {
				localHook = this.findHook("_SMOKE");
			}
			this.flame = new ActorSimpleMesh(str);
			if (this.flame != null) {
				((ActorSimpleMesh) this.flame).mesh().setScale(1.0F);
				this.flame.pos.setBase(this, localHook, false);
				this.flame.pos.changeHookToRel();
				this.flame.pos.resetAsBase();
				this.flameActive = true;
				if (Config.isUSE_RENDER())
					this.flame.drawing(true);
			}
			this.bRocketFiring = true;
		}
		str = this.effSmokeSustain;
		if (str != null) {
			if (localHook == null) {
				localHook = this.findHook("_SMOKE");
			}
			this.smoke = Eff3DActor.New(this, localHook, null, 1.0F, str, -1.0F);
			if (this.smoke != null) {
				this.smoke.pos.changeHookToRel();
				this.smokeActive = true;
			}
		}
		setSmokeSpriteFlames(1);
	}

	public void afterFireEngineTrail() {
		// EventLog.type("afterFireEngineTrail");

		Class localClass = this.getClass();

		Hook localHook = null;
		String str = this.effSmokeTrail;
		if (str != null) {
			if (localHook == null) {
				localHook = this.findHook("_SMOKE");
			}
			this.smoke = Eff3DActor.New(this, localHook, null, 1.0F, str, -1.0F);
			if (this.smoke != null) {
				this.smoke.pos.changeHookToRel();
				this.smokeActive = true;
			}
		}
		setSmokeSpriteFlames(2);
	}

	public void startMissile(float paramFloat, int paramInt) {
		Class localClass = this.getClass();
		float f1 = Property.floatValue(localClass, "kalibr", 0.082F);
		if (paramFloat <= 0.0F) {
			paramFloat = Property.floatValue(localClass, "timeLife", 45.0F);
		}

		float f2 = -1.0F + 2.0F * TrueRandom.nextFloat();
		f2 *= f2 * f2;
		float f3 = -1.0F + 2.0F * TrueRandom.nextFloat();
		f3 *= f3 * f3;

		this.init(f1, Property.floatValue(localClass, "massa", 6.8F), Property.floatValue(localClass, "massaEnd", 2.52F), Property.floatValue(localClass, "timeFire", 4.0F) / (1.0F + 0.1F * f2), Property.floatValue(localClass, "force", 500.0F)
				* (1.0F + 0.1F * f2), paramFloat + f3 * 0.1F);

		this.setOwner(this.pos.base(), false, false, false);
		this.pos.setBase(null, null, true);
		this.pos.setAbs(this.pos.getCurrent());

		this.pos.getAbs(Aircraft.tmpOr);

		float f4 = 0.68F * Property.floatValue(localClass, "maxDeltaAngle", 3.0F);

		f2 = -1.0F + 2.0F * TrueRandom.nextFloat();
		f3 = -1.0F + 2.0F * TrueRandom.nextFloat();

		f2 *= f2 * f2 * f4;
		f3 *= f3 * f3 * f4;

		Aircraft.tmpOr.increment(f2, f3, 0.0F);

		this.pos.setAbs(Aircraft.tmpOr);

		this.pos.getRelOrient().transformInv(this.speed);

		this.speed.z /= 3.0D;
		this.speed.x += 200.0D;
		this.pos.getRelOrient().transform(this.speed);
		this.collide(true);
		this.interpPut(new Interpolater(), null, Time.current(), null);

		if (this.getOwner() == World.getPlayerAircraft()) {
			World.cur().scoreCounter.rocketsFire += 1;
		}
		if (!Config.isUSE_RENDER()) {
			if (this.engineDelayTime <= 0L)
				this.bRocketFiring = true;
			return;
		}

		if (this.engineDelayTime <= 0L)
			this.startEngine();

		this.light = new LightPointActor(new LightPointWorld(), new Point3d());
		this.light.light.setColor((Color3f) Property.value(localClass, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
		this.light.light.setEmit(Property.floatValue(localClass, "emitMax", 1.0F), Property.floatValue(localClass, "emitLen", 50.0F));
		this.draw.lightMap().put("light", this.light);

	}

	public boolean stepBeamRider() {
		float missileSpeed = this.computeMissileAccelleration();
		if (missileSpeed == -1F) return false;

		Actor myOwner = this.getOwner();
		if (this.victim != null) {
			if (GuidedMissileUtils.angleBetween(myOwner, this.victim) > this.maxFOVfrom) {
				this.victim = null;
			}
		}
		if (Time.current() < this.startTime + this.trackDelay) {
			this.computeNoTrackPath();
		} else if (this.victim != null) {
			if (myOwner != null) {

				float hTurn = 0.0F;
				float vTurn = 0.0F;

				Point3d pointAC = new Point3d();
				pointAC.set(myOwner.pos.getAbsPoint());
				Orient orientAC = new Orient();
				orientAC.set(myOwner.pos.getAbsOrient());
				Point3d pointTarget = new Point3d();
				pointTarget.set(this.victim.pos.getAbsPoint());
				Vector3d vectorOffset = new Vector3d();
				Orient orientTarget = new Orient();
				orientTarget.set(this.victim.pos.getAbsOrient());
				vectorOffset.set(this.victimOffsetPoint3f); // take victim Offset into account
				orientTarget.transform(vectorOffset); // take victim Offset into account
				pointTarget.add(vectorOffset); // take victim Offset into account

				Point3d pointMissile = new Point3d();
				pointMissile.set(this.pos.getAbsPoint());

				Point3d pointMissileAft = new Point3d();
				pointMissileAft.set(pointMissile);
				Orient orientMissileAft = new Orient();
				orientMissileAft.set(this.pos.getAbsOrient());
				Point3d pointACAft = new Point3d();
				pointACAft.set(pointAC);

				pointTarget.sub(pointAC);
				orientAC.transformInv(pointTarget);
				float targetAzimuth = (float) Math.toDegrees(Math.atan(-pointTarget.y / pointTarget.x));
				float targetElevation = (float) Math.toDegrees(Math.atan(pointTarget.z / pointTarget.x));
				if (this.getFailState() == FAIL_TYPE_REFLEX) {
					targetAzimuth += 180F;
					targetElevation += 180F;
					if (targetAzimuth > 180F) {
						targetAzimuth = 180F - targetAzimuth;
					}
					if (targetElevation > 180F) {
						targetElevation = 180F - targetElevation;
					}
				}

				pointMissile.sub(pointAC);
				orientAC.transformInv(pointMissile);
				float missileAzimuth = (float) Math.toDegrees(Math.atan(-pointMissile.y / pointMissile.x));
				float missileElevation = (float) Math.toDegrees(Math.atan(pointMissile.z / pointMissile.x));

				float missileOffsetAzimuth = missileAzimuth - targetAzimuth;
				float missileOffsetElevation = missileElevation - targetElevation;

				pointACAft.sub(pointMissileAft);
				orientMissileAft.transformInv(pointACAft);
				float missileAzimuthAft = (float) Math.toDegrees(Math.atan(-pointACAft.y / pointACAft.x));
				float missileElevationAft = (float) Math.toDegrees(Math.atan(pointACAft.z / pointACAft.x));

				float missileTrackOffsetAzimuth = missileOffsetAzimuth - missileAzimuthAft;
				float missileTrackOffsetElevation = missileOffsetElevation - missileElevationAft;

				float closingFactor = -5.0F;
				float maxClosing = 60.0F;
				float fastClosingMax = 3.0F;

				float turnNormal = 1.0F;
				float turnQuick = 1.5F;
				float turnSharp = 2.0F;

				if (missileOffsetAzimuth < 0) { // left of beam
					if (missileTrackOffsetAzimuth < 0) { // heading left
						hTurn = turnSharp; // turn right sharp
					} else if (missileTrackOffsetAzimuth > maxClosing) { // heading right more than 45° towards beam
						hTurn = -turnNormal; // turn left
					} else if ((missileTrackOffsetAzimuth > fastClosingMax) && (missileTrackOffsetAzimuth > (closingFactor * missileOffsetAzimuth))) { // fast closing in on beam from left
						hTurn = -turnQuick; // turn left quick
					} else {
						hTurn = turnNormal; // turn right
					}
				} else { // right of beam
					if (missileTrackOffsetAzimuth > 0) { // heading right
						hTurn = -turnSharp; // turn left sharply
					} else if (missileTrackOffsetAzimuth < -maxClosing) { // heading left more than 45° towards beam
						hTurn = turnNormal; // turn right
					} else if ((missileTrackOffsetAzimuth < -fastClosingMax) && (missileTrackOffsetAzimuth < (closingFactor * missileOffsetAzimuth))) { // fast closing in on beam from right
						hTurn = turnQuick; // turn right quick
					} else {
						hTurn = -turnNormal; // turn left
					}
				}

				if (missileOffsetElevation < 0) { // below beam
					if (missileTrackOffsetElevation < 0) { // heading down
						vTurn = turnSharp; // turn up sharp
					} else if (missileTrackOffsetElevation > maxClosing) { // heading up more than 45° towards beam
						vTurn = -turnNormal; // turn down
					} else if ((missileTrackOffsetElevation > fastClosingMax) && (missileTrackOffsetElevation > (closingFactor * missileOffsetElevation))) { // fast closing in on beam from below
						vTurn = -turnQuick; // turn down quick
					} else {
						vTurn = turnNormal; // turn up
					}
				} else { // above beam
					if (missileTrackOffsetElevation > 0) { // heading up
						vTurn = -turnSharp; // turn down sharp
					} else if (missileTrackOffsetElevation < -maxClosing) { // heading down more than 45° towards beam
						vTurn = turnNormal; // turn up
					} else if ((missileTrackOffsetElevation < -fastClosingMax) && (missileTrackOffsetElevation < (closingFactor * missileOffsetElevation))) { // fast closing in on beam from above
						vTurn = turnQuick; // turn up quick
					} else {
						vTurn = -turnNormal; // turn down
					}
				}

				this.computeMissilePath(missileSpeed, hTurn, vTurn, targetAzimuth, targetElevation);

			}
		} // else {
		// if ((Time.current() < this.startTime + this.trackDelay) && (this.launchType == Missile.LAUNCH_TYPE_DROP)) // recover from launch pitch even if there's no target.
		// {
		// this.computeNoTrackPath();
		// } else { // fly straight on after 1 sec. if no target available.
		// this.missileOrient.setYPR(this.missileOrient.getYaw(), this.missileOrient.getPitch(), this.getRoll());
		// }
		// }

		return this.computeFuzeState();
	}

	public boolean stepTargetHoming() {
		float missileSpeed = this.computeMissileAccelleration();
		if (missileSpeed == -1F) return false;

		if (Time.current() < this.startTime + this.trackDelay) {
			this.computeNoTrackPath();
		} else if (this.victim != null && !this.lockingVictim) {
			this.computeNoTrackPath();
		} else if (this.victim != null) {
			this.checkChaffFlareLock();
			// System.out.println("stepTargetHoming victim=" + this.victim.getClass().getName());
			this.victim.pos.getAbs(this.targetPoint3d, this.victimOffsetOrient);
			// Calculate future victim position

			// IR or IMAGE homing cannot chase the victim behind clouds
			if ((this.iDetectorType == DETECTOR_TYPE_INFRARED || this.iDetectorType == DETECTOR_TYPE_IMAGE_EOTV || this.iDetectorType == DETECTOR_TYPE_IMAGE_IR) &&
				Main.cur().clouds != null &&
				Main.cur().clouds.getVisibility(this.victim.pos.getAbsPoint(), this.pos.getAbsPoint()) < 0.4F) {
				this.computeNoTrackPath();
				return this.computeFuzeState();
			}

			this.victim.getSpeed(this.victimSpeed); // target movement vector
			double victimDistance = GuidedMissileUtils.distanceBetween(this, this.victim); // distance missile -> target
			double theVictimSpeed = this.victimSpeed.length(); // target speed
			if (theVictimSpeed > 10D) {
				double speedRel = missileSpeed / theVictimSpeed; // relation missile speed / target speed
				double gamma = (GuidedMissileUtils.angleActorBetween(this.victim, this)); // angle offset missile vector -> target vector
				double alpha = Geom.RAD2DEG((float) (Math.asin(Math.sin(Geom.DEG2RAD((float) gamma)) / speedRel))); // angle offset target vector -> target interception path vector
				double beta = 180.0D - gamma - alpha; // angle offset missile vector -> target interception path vector
				double victimAdvance = victimDistance * Math.sin(Geom.DEG2RAD((float) alpha)) / Math.sin(Geom.DEG2RAD((float) beta)); // track made good by target until impact
				victimAdvance -= 5.0D; // impact point 10m aft of engine (track exhaust).
				double timeToTarget = victimAdvance / theVictimSpeed; // time until calculated impact

				this.victimSpeed.scale(timeToTarget * (this.leadPercent / 100.0F));
				this.targetPoint3d.add(this.victimSpeed);
			}
			/*
			double speed = this.getSpeed(null);
	    	if (speed <100) speed = 100;
	    	double distance = GuidedMissileUtils.distanceBetween(this, victim);
	    	if (distance < 0) distance = 0;
	    	loft = distance/speed;
	    	loft = 0.5*9.81*loft*loft*(loft_mode);
	    	if((loft/distance) > 0.35D) loft = distance*0.35;
	    	
			if(loft > 0) this.targetPoint3d.z += loft; 
			*/
			Orient orientTarget = new Orient();
			orientTarget.set(this.victim.pos.getAbsOrient());
			this.trajectoryVector3d.set(this.victimOffsetPoint3f); // take victim Offset into account
			orientTarget.transform(this.trajectoryVector3d); // take victim Offset into account
			this.targetPoint3d.add(this.trajectoryVector3d); // take victim Offset into account

			this.targetPoint3d.sub(this.missilePoint3d); // relative Position to target
			this.missileOrient.transformInv(this.targetPoint3d); // set coordinate system according to A/C POV

			// Calculate angle to target.
			// This is required in order to respect the IR Seeker FOV.
			float angleAzimuth = (float) Math.toDegrees(Math.atan(this.targetPoint3d.y / this.targetPoint3d.x));
			float angleTangage = (float) Math.toDegrees(Math.atan(this.targetPoint3d.z / this.targetPoint3d.x));

			if (this.getFailState() == FAIL_TYPE_REFLEX) {
				angleAzimuth += 180F;
				angleTangage += 180F;
				if (angleAzimuth > 180F) {
					angleAzimuth = 180F - angleAzimuth;
				}
				if (angleTangage > 180F) {
					angleTangage = 180F - angleTangage;
				}
			}

			this.computeMissilePath(missileSpeed, 0.0F, 0.0F, angleAzimuth, angleTangage);
		} else if (bLaserHoming) {
			double targetDistance = 0.0D;
			float targetAngle = 0.0F;
			float targetBait = 0.0F;
			float maxTargetBait = 0.0F;

			if (laserOwner == null) {
				while ((this.getOwner() instanceof TypeLaserDesignator) && (((TypeLaserDesignator) this.getOwner()).getLaserOn())) {
					Point3d point3d = new Point3d();
					point3d.set(((TypeLaserDesignator) this.getOwner()).getLaserSpot());
					if (point3d == null)
						break;
					if (Main.cur().clouds != null &&
						(   Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F
						 || Main.cur().clouds.getVisibility(point3d, this.getOwner().pos.getAbsPoint()) < 1.0F))
						break;
					targetDistance = this.pos.getAbsPoint().distance(point3d);
					if (targetDistance > attackMaxDistance)
						break;
					targetAngle = GuidedMissileUtils.angleBetween(this, point3d);
					if (targetAngle > this.maxFOVfrom)
						break;
					laserOwner = this.getOwner();
					break;
				}
				if (laserOwner == null) {
					List list = Engine.targets();
					int i = list.size();
					for (int j = 0; j < i; j++) {
						Actor theOwner1 = (Actor)list.get(j);
						if ((theOwner1 instanceof TypeLaserDesignator) && ((TypeLaserDesignator) theOwner1).getLaserOn() && theOwner1.getArmy() == this.getOwner().getArmy()) {
							Point3d point3d = new Point3d();
							point3d = ((TypeLaserDesignator)theOwner1).getLaserSpot();
							if (point3d == null)
								break;
							// Not target about objects behind of clouds from the missile's seeker.
							if (Main.cur().clouds != null &&
								(   Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F)
								 || Main.cur().clouds.getVisibility(point3d, theOwner1.pos.getAbsPoint()) < 1.0F)
								continue;
							targetDistance = theOwner1.pos.getAbsPoint().distance(point3d);
							if (targetDistance > attackMaxDistance)
								continue;
							targetAngle = GuidedMissileUtils.angleBetween(theOwner1, point3d);
							if (targetAngle > maxFOVfrom)
								continue;

							targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);

							if (targetBait <= maxTargetBait)
								continue;

							maxTargetBait = targetBait;
							laserOwner = theOwner1;
						}
					}
				}
			}
			if (laserOwner != null) {
				if (((TypeLaserDesignator) laserOwner).getLaserOn()) {
					Point3d point3d = new Point3d();
					point3d.set(((TypeLaserDesignator) laserOwner).getLaserSpot());

					if (point3d != null) {
						this.targetPoint3d.set(point3d);
						this.targetPoint3d.sub(this.missilePoint3d); // relative Position to target
						this.missileOrient.transformInv(this.targetPoint3d); // set coordinate system according to A/C POV

						// Calculate angle to target.
						float angleAzimuth = (float) Math.toDegrees(Math.atan(this.targetPoint3d.y / this.targetPoint3d.x));
						float angleTangage = (float) Math.toDegrees(Math.atan(this.targetPoint3d.z / this.targetPoint3d.x));

						if (this.getFailState() == FAIL_TYPE_REFLEX) {
							angleAzimuth += 180F;
							angleTangage += 180F;
							if (angleAzimuth > 180F) {
								angleAzimuth = 180F - angleAzimuth;
							}
							if (angleTangage > 180F) {
								angleTangage = 180F - angleTangage;
							}
						}

						if (Main.cur().clouds == null ||
							(   Main.cur().clouds.getVisibility(this.targetPoint3d, this.pos.getAbsPoint()) >= 0.99F)
							 && Main.cur().clouds.getVisibility(this.targetPoint3d, laserOwner.pos.getAbsPoint()) >= 0.99F) {
							this.computeMissilePath(missileSpeed, 0.0F, 0.0F, angleAzimuth, angleTangage);
						}
					}
				}
			}
		} else if (bSACLOSHoming) {
			double targetDistance = 0.0D;
			float targetAngle = 0.0F;
			float targetBait = 0.0F;
			float maxTargetBait = 0.0F;

			if (saclosOwner == null) {
				while ((this.getOwner() instanceof TypeSACLOS) && (((TypeSACLOS) this.getOwner()).getSACLOSenabled())) {
					Point3d point3d = new Point3d();
					point3d.set(((TypeSACLOS) this.getOwner()).getSACLOStarget());
					if (point3d == null)
						break;
					if (Main.cur().clouds != null &&
						(   Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F
						 || Main.cur().clouds.getVisibility(point3d, this.getOwner().pos.getAbsPoint()) < 1.0F))
						break;
					targetDistance = this.pos.getAbsPoint().distance(point3d);
					if (targetDistance > attackMaxDistance)
						break;
					targetAngle = GuidedMissileUtils.angleBetween(this, point3d);
					if (targetAngle > this.maxFOVfrom)
						break;
					saclosOwner = this.getOwner();
					break;
				}
			}
			if (saclosOwner != null) {
				if (((TypeSACLOS) saclosOwner).getSACLOSenabled()) {
					Point3d point3d = new Point3d();
					point3d.set(((TypeSACLOS) saclosOwner).getSACLOStarget());

					if (point3d != null) {
						this.targetPoint3d.set(point3d);
						this.targetPoint3d.sub(this.missilePoint3d); // relative Position to target
						this.missileOrient.transformInv(this.targetPoint3d); // set coordinate system according to A/C POV

						// Calculate angle to target.
						float angleAzimuth = (float) Math.toDegrees(Math.atan(this.targetPoint3d.y / this.targetPoint3d.x));
						float angleTangage = (float) Math.toDegrees(Math.atan(this.targetPoint3d.z / this.targetPoint3d.x));

						if (this.getFailState() == FAIL_TYPE_REFLEX) {
							angleAzimuth += 180F;
							angleTangage += 180F;
							if (angleAzimuth > 180F) {
								angleAzimuth = 180F - angleAzimuth;
							}
							if (angleTangage > 180F) {
								angleTangage = 180F - angleTangage;
							}
						}

						if (Main.cur().clouds == null ||
							(   Main.cur().clouds.getVisibility(saclosOwner.pos.getAbsPoint(), this.pos.getAbsPoint()) >= 0.99F)
							 && Main.cur().clouds.getVisibility(this.targetPoint3d, saclosOwner.pos.getAbsPoint()) >= 0.99F) {
							this.computeMissilePath(missileSpeed, 0.0F, 0.0F, angleAzimuth, angleTangage);
						}
					}
				}
			}
		}

		return this.computeFuzeState();

	}

	public int WeaponsMask() {
		return -1;
	}

	private void checkChaffFlareLock() {
		if ((this.victim instanceof RocketFlare) || (this.victim instanceof RocketChaff))
			return;

		List theCountermeasures = null;
		try {
			theCountermeasures = Engine.countermeasures();
		} catch (Exception e) { }

		if (theCountermeasures == null) return;
//		Random random = new Random();
		int lockTime = TrueRandom.nextInt((int)flareLockTime + 1000); //  World.rnd().nextInt((int) (flareLockTime + 1000));
		double flareDistance = 0.0D;
//		double victim1Distance = 0.0D;
		int counterMeasureSize = theCountermeasures.size();
		for (int counterMeasureIndex = 0; counterMeasureIndex < counterMeasureSize; counterMeasureIndex++) {
			Actor flarechaff = (Actor) theCountermeasures.get(counterMeasureIndex);
			flareDistance = GuidedMissileUtils.distanceBetween(this, flarechaff);
//			victim1Distance = GuidedMissileUtils.distanceBetween(this, victim);
			if (this.iDetectorType == DETECTOR_TYPE_INFRARED && flarechaff instanceof RocketFlare && flareLockTime < lockTime && flareDistance < 200D && GuidedMissileUtils.angleActorBetween(victim, this) > 30F)
				this.victim = flarechaff;
			else if ((this.iDetectorType == DETECTOR_TYPE_RADAR_HOMING || this.iDetectorType == DETECTOR_TYPE_RADAR_BEAMRIDING || this.iDetectorType == DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE) && flarechaff instanceof RocketChaff && flareLockTime < lockTime && flareDistance < 500D && GuidedMissileUtils.angleActorBetween(victim, this) > 30F)
				this.victim = flarechaff;
			else if (this.iDetectorType == DETECTOR_TYPE_IMAGE_IR && flarechaff instanceof RocketFlare && flareLockTime < lockTime && flareDistance < 500D && GuidedMissileUtils.angleActorBetween(victim, this) > 30F && TrueRandom.nextFloat() < 0.1F)
				this.victim = flarechaff;
		}
	}

	private static final void InitTablesOfEnergyToKill() {
		PenetrateEnergyToKill = new float[4][][];
		PenetrateThickness = new float[4][];
		PenetrateThickness[0] = (new float[] { 0.025F, 0.15F });
		PenetrateEnergyToKill[0] = (new float[][] {
			new float[] {  23400F, 131400F,  252000F },
			new float[] { 131400F, 369000F, 1224000F }
		});
		PenetrateThickness[1] = (new float[] { 0.12F, 0.24F });
		PenetrateEnergyToKill[1] = (new float[][] {
			new float[] { 131400F,  252000F, 3295500F },
			new float[] { 369000F, 3295500F, 5120000F }
		});
		PenetrateThickness[2] = (new float[] { 0.002F, 0.008F });
		PenetrateEnergyToKill[2] = (new float[][] {
			new float[] {  511.225F,  2453.88F, 23400F },
			new float[] { 2453.88F,  10140F,	23400F }
		});
		PenetrateThickness[3] = (new float[] { 0.025F, 0.15F });
		PenetrateEnergyToKill[3] = (new float[][] {
			new float[] {  23400F, 131400F,  252000F },
			new float[] { 131400F, 369000F, 1224000F }
		});
		for (int i = 0; i < 4; i++) {
			if (Math.abs(PenetrateThickness[i][0] - PenetrateThickness[i][1]) < 0.001F)
				internalerrror(1);
			for (int j = 0; j <= 1; j++) {
				float af[] = PenetrateEnergyToKill[i][j];
				if (af[1] - af[0] < 0.001F || af[2] - af[1] < 0.001F)
					internalerrror(2);
			}

			float f = Math.min(PenetrateEnergyToKill[i][0][0], PenetrateEnergyToKill[i][1][0]);
			float f1 = Math.max(PenetrateEnergyToKill[i][0][2], PenetrateEnergyToKill[i][1][2]);
			for (int k = 0; k <= 100; k++) {
				float f2 = f + ((f1 - f) * (float)k) / 100F;
				float f3 = ComputeProbabOfPenetrateKill(i, 0, f2);
				float f4 = ComputeProbabOfPenetrateKill(i, 1, f2);
				if (f3 < f4) {
					System.out.println(i + " i,e0,e1,e:" + k + " " + f + " " + f1 + " " + f2 + " prob0,1: " + f3 + " " + f4);
					internalerrror(3);
				}
			}

		}

	}

	private static final float ComputeProbabOfPenetrateKill(int i, int j, float f) {
		float af[] = PenetrateEnergyToKill[i][j];
		float f1;
		float f2;
		if (f < af[1]) {
			if (f < af[0])
				return 0.0F;
			f1 = 0.2F / (af[1] - af[0]);
			f2 = 0.1F - af[0] * f1;
			if ( bLogDetailDamage ) System.out.println("ComputeProbabOfPenetrateKill(i="+ i + ", j=" + j + ", f=" + f + ") - if (f < af[1]) - f1=" + f1 + ", f2=" + f2);
		} else {
			if (f >= af[2])
				return 1.0F;
			f1 = 0.7F / (af[2] - af[1]);
			f2 = 0.3F - af[1] * f1;
			if ( bLogDetailDamage ) System.out.println("ComputeProbabOfPenetrateKill(i="+ i + ", j=" + j + ", f=" + f + ") - if (f => af[1]) - f1=" + f1 + ", f2=" + f2);
		}
		if ( bLogDetailDamage ) System.out.println("ComputeProbabOfPenetrateKill(i="+ i + ", j=" + j + ", f=" + f + ") - return=" + (f * f1 + f2));
		return f * f1 + f2;
	}

	private final float ComputeProbabOfPenetrateKill(float f, int i) {
		if (i <= 0) return 0.0F;

		float f1 = ComputeProbabOfPenetrateKill(this.damage_MAT_TYPE, 0, f);
		float f2 = ComputeProbabOfPenetrateKill(this.damage_MAT_TYPE, 1, f);
		float af[] = PenetrateThickness[this.damage_MAT_TYPE];
		float f3 = (f2 - f1) / (af[1] - af[0]);
		float f4 = f1 - af[0] * f3;
		float f5 = this.damage_PANZER * f3 + f4;

		if (f5 < 0.1F) f5 = 0.0F;
		else if (f5 >= 1.0F) f5 = 1.0F;
		else if (i > 1) f5 = 1.0F - (float)Math.pow(1.0F - f5, i);

		if ( bLogDetailDamage ) System.out.println("ComputeProbabOfPenetrateKill(f=" + f + " ,i=" + i + ") - f1=" + f1 + ", f2=" + f2 + ", f3=" + f3 + ", f4=" + f4 + ", return=f5=" + f5);
		return f5;
	}

	public void msgShot(Shot shot) {
		if ( bLogDetailDamage ) System.out.println("Missile(" + actorString(this) + ") - msgShot(){} - shot.power=" + shot.power);
		shot.bodyMaterial = this.damage_EFF_BODY_MATERIAL;
		if (!isAlive()) return;
		if (shot.power <= 0.0F) return;

		if (this.damage_MAT_TYPE == MAT_FLESH) {
			if (shot.powerType == 1) {
				die(shot.initiator, true);
				return;
			}
			if (shot.v.length() < 20D) {
				return;
			} else {
				die(shot.initiator, true);
				return;
			}
		}
		if (shot.powerType == 1) {
			if (this.damage_MAT_TYPE != MAT_STEEL) return;
			float f = shot.power * RndF(0.75F, 1.15F);
			float f2 = 0.256F * (float)Math.sqrt(Math.sqrt(f));
			if (this.damage_PANZER > f2) {
				return;
			} else {
				die(shot.initiator, true);
				return;
			}
		}
		float f1 = ComputeProbabOfPenetrateKill(shot.power, 1);
		if (!RndB(f1)) {
			return;
		} else {
			die(shot.initiator, true);
			return;
		}
	}

	public void msgExplosion(Explosion explosion) {
		if ( bLogDetailDamage || bLogDetail ) System.out.println("Missile(" + actorString(this) + ") - msgExplosion(){} - explosion.power=" + explosion.power + " - receivedPower=" + explosion.receivedPower(this) + " - powerType=" + explosion.powerType);
		if (!isAlive()) return;
		if (explosion.power <= 0.0F) return;

		if (this.damage_MAT_TYPE == MAT_FLESH) {
			if (Explosion.killable(this, explosion.receivedTNT_1meter(this), 0.005F, 0.1F, 0.0F))
				die(explosion.initiator, true);
			return;
		}
		Explosion _tmp = explosion;
		if (explosion.powerType == 1) {
			float af[] = new float[6];
			mesh().getBoundBox(af);
			pos.getAbs(ppp);
			ppp.x = (ppp.x - (double)af[0]) + (double)(af[3] - af[0]);
			ppp.y = (ppp.y - (double)af[1]) + (double)(af[4] - af[1]);
			ppp.z = (ppp.z - (double)af[2]) + (double)(af[5] - af[2]);
			float af1[] = new float[2];
			explosion.computeSplintersHit(ppp, mesh().collisionR(), 0.7F, af1);
			float f = 0.015F * af1[1] * af1[1] * 0.5F;
			float f1 = ComputeProbabOfPenetrateKill(f, (int)(af1[0] + 0.5F));
			if ( bLogDetailDamage || bLogDetail) System.out.println("msgExplosion(){} - if(powerType == 1) - af1[0]=" + af1[0] + " , af1[1]=" + af1[1] + " , ProbabOfKill=" + f1);
			if (RndB(f1))
				die(explosion.initiator, true);
			return;
		}
		if (explosion.powerType == 0) {
			if (Explosion.killable(this, explosion.receivedPower(this), this.damage_MIN_TNT, this.damage_MAX_TNT, this.damage_PROBAB_DEATH_WHEN_EXPLOSION))
				die(explosion.initiator, true);
			return;
		}
		if (this.damage_MAT_TYPE == MAT_BRICK) {
			return;
		} else {
			die(explosion.initiator, true);
			return;
		}
	}

	private void die(Actor actor, boolean flag) {
		if ( bLogDetailDamage ) System.out.println("Missile(" + actorString(this) + ") - die(actor=" + actorString(actor) + ", flag=" + flag + "){}");
		if (!isAlive()) return;
		this.timeToFailure = 1000L;
		if (RndB(0.04F)) this.failState = FAIL_TYPE_WARHEAD;
		else if (Time.current() - this.startTime < this.rocketMotorOperationTime + this.rocketMotorSustainedOperationTime) {
			if (RndB(0.25F)) this.failState = FAIL_TYPE_ENGINE;
			else if (this.failState == FAIL_TYPE_NONE) {
				if (RndB(0.7F)) this.failState = FAIL_TYPE_MIRROR;
				else this.failState = FAIL_TYPE_CONTROL_BLOCKED;
			}
		}
		else if (this.failState == FAIL_TYPE_NONE || this.failState == FAIL_TYPE_ENGINE) {
			if (RndB(0.7F)) this.failState = FAIL_TYPE_MIRROR;
			else this.failState = FAIL_TYPE_CONTROL_BLOCKED;
		}
		if (this.mshDamage != null)
			setMesh(MeshShared.get(this.mshDamage));

		if ( bLogDetailDamage ) System.out.println("Missile(" + actorString(this) + ") - die() - new failState=" + this.failState);
		if ( bLogDetailDamage ) System.out.println("die() - old Yaw=" + this.missileOrient.getYaw() + " , old Pitch=" + this.missileOrient.getPitch());
		this.missileOrient.setYPR(this.missileOrient.getYaw() + RndF(-5.0F, 5.0F), this.missileOrient.getPitch() + RndF(-5.0F, 5.0F), this.getRoll());
		if ( bLogDetailDamage ) System.out.println("die() - new Yaw=" + this.missileOrient.getYaw() + " , new Pitch=" + this.missileOrient.getPitch());
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		if ( bLogDetail )
			System.out.println("Missile(" + actorString(this) + ") - entering msgCollisionRequest(actor, aflag[])");
		super.msgCollisionRequest(actor, aflag);
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if ( bLogDetail )
			System.out.println("Missile(" + actorString(this) + ") - entering msgCollision(actor, s, s1)");
		super.msgCollision(actor, s, s1);
	}

	private static final boolean RndB(float f) {
		return TrueRandom.nextFloat(0.0F, 1.0F) < f;
	}

	private static final float RndF(float f, float f1) {
		return TrueRandom.nextFloat(f, f1);
	}

	private static final void internalerrror(int i) {
		System.out.println("*** Internal error #" + i + " in Missile damage database ***");
		throw new RuntimeException("Can't initialize Missile");
	}

	private static String actorString(Actor actor) {
		if (!Actor.isValid(actor)) return "(InvalidActor)";
		String s;
		try {
			s = actor.getClass().getName();
		} catch(Exception e) {
			System.out.println("Missile - actorString(): Cannot resolve class name of " + actor);
			return "(NoClassnameActor)";
		}
		int i = s.lastIndexOf('.');
		String strSection = s.substring(i + 1);
		strSection =  strSection + '@' + Integer.toHexString(actor.hashCode());
		return strSection;
	}

	protected static final int DETECTOR_TYPE_MANUAL = 0;
	protected static final int DETECTOR_TYPE_INFRARED = 1;
	protected static final int DETECTOR_TYPE_RADAR_HOMING = 2;
	protected static final int DETECTOR_TYPE_RADAR_BEAMRIDING = 3;
	protected static final int DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE = 4;
	protected static final int DETECTOR_TYPE_ANTI_RADIATION = 5;
	protected static final int DETECTOR_TYPE_ANTI_RADIATION_LOCREC = 6;
	protected static final int DETECTOR_TYPE_IMAGE_EOTV = 7;
	protected static final int DETECTOR_TYPE_IMAGE_IR = 8;
	protected static final int DETECTOR_TYPE_LASER = 9;
	protected static final int DETECTOR_TYPE_SACLOS = 10;
	protected static final int FAIL_TYPE_CONTROL_BLOCKED = 6;
	protected static final int FAIL_TYPE_ELECTRONICS = 1;
	protected static final int FAIL_TYPE_ENGINE = 3;
	protected static final int FAIL_TYPE_IVAN = 5;
	protected static final int FAIL_TYPE_MIRROR = 2;
	protected static final int FAIL_TYPE_NONE = 0;
	protected static final int FAIL_TYPE_NUMBER = 7;
	protected static final int FAIL_TYPE_REFLEX = 4;
	protected static final int FAIL_TYPE_WARHEAD = 7;
	private static final float IVAN_TIME_MAX = 2.0F;
	private static final float IVAN_TIME_MIN = 1.0F;
	protected static final int LAUNCH_TYPE_DROP = 2;
	protected static final int LAUNCH_TYPE_QUICK = 1;
	protected static final int LAUNCH_TYPE_STRAIGHT = 0;
	protected static final int STEP_MODE_BEAMRIDER = 1;
	protected static final int STEP_MODE_HOMING = 0;
	protected static final int TARGET_AIR = 0x0001;
	protected static final int TARGET_GROUND = 0x0010;
	protected static final int TARGET_LOCATE = 0x0020;
	protected static final int TARGET_SHIP = 0x0100;
	protected static final int ATTACK_DECISION_BY_AI_NO = 0;
	protected static final int ATTACK_DECISION_BY_AI_YES = 1;
	protected static final int ATTACK_DECISION_BY_AI_WAYPOINT = 2;
	private float deltaAzimuth = 0.0F;
	private float deltaTangage = 0.0F;
	private float dragCoefficient = 0.3F;
	private float dragCoefficientTurn = 0.3F;
	private Orient dropFlightPathOrient = new Orient();
	private String effSmoke = null;
	private String effSprite = null;
	private boolean endedFlame = false;
	private boolean endedSmoke = false;
	private boolean endedSprite = false;
	private long engineDelayTime = 0L;
	private boolean engineRunning = false;
	private int exhausts = 1;
	private int failState = FAIL_TYPE_NONE;
	private MissileNavLight firstNavLight = null; // First Nav Light Element in the chained list of Nav Lights.
	private boolean flameActive = true;
	private Actor[] flames = null;
	private long flareLockTime = 1000L;
	// private float fMaxSpeed = 0.0F;
	private float frontSquare = 1.0F;
	private float groundTrackFactor = 0.0F;
	private float initialMissileForce = 0F;
	private float ivanTimeLeft = 0.0F;
	private MissileNavLight lastNavLight = null; // Last Nav Light Element in the chained list of Nav Lights.
	private long lastTimeNoFlare = 0L;
	private double launchKren = 0.0D;
	private double launchPitch = 0.0D;
	private int launchType = 0;
	private double launchYaw = 0.0D;
	private float leadPercent = 0.0F; // 0 means tail chasing, 100 means full lead tracking
	private float massaEnd = 86.2F;
	private float massLossPerTick = 0F;
	private float maxFOVfrom = 0.0F;
	private float maxFOVfrom_real = 0.0F;
	private float maxG = 12F; // maximum G-Force during flight
	private float maxLaunchG = 2.0F;
	private float missileBaseSpeed = 0.0F;
	private float missileForce = 18712F;
	private float missileForceRunUpTime = 0F;
	private float missileKalibr = 0.2F;
	private float missileMass = 86.2F;
	private Orient missileOrient = new Orient();
	private Point3d missilePoint3d = new Point3d();
	private float missileSustainedForce = 0F;
	private float missileTimeLife = 30F;
	private ActiveMissile myActiveMissile = null;
	private float oldDeltaAzimuth;
	private float oldDeltaTangage;
	private float oldRoll = 0F;
	private float previousDistance = 0.0F;
	private long releaseTime = 0L;
	private float rocketMotorOperationTime = 2.2F;
	private float rocketMotorSustainedOperationTime = 0F;
	private Vector3d safeVictimOffset = new Vector3d();
	private String simFlame = null;
	private boolean smokeActive = true;
	private Eff3DActor[] smokes = null;
	private boolean spriteActive = true;
	private Eff3DActor[] sprites = null;
	private boolean noSmokeFlameSustain = false;
	private boolean playingMotorSound = false;
	private boolean noSoundSustain = false;
	private String soundNameSustain = null;
	private boolean playingSustainMotorSound = false;
	private String effSmokeSustain = null;
	private String effSpriteSustain = null;
	private String simFlameSustain = null;
	private boolean showingSustainSmokeFlame = false;
	private String effSmokeTrail = null;
	private boolean showingTrailSmoke = false;
	private String mshFly = null;
	private boolean waitingMeshFly = false;
	private String mshSustain = null;
	private boolean waitingMeshSustain = false;
	private String mshDamage = null;
	private long startTime = 0L;
	private boolean startTimeIsSet = false;
	private int iDetectorType = 0;
	private long lTargetType = 0L;
	private int stepMode = 0;
	private float stepsForFullTurn = 10; // update steps for maximum control surface output, higher value means slower reaction and smoother flight, lower value means higher agility
	private float sunRayAngle = 0.0F;
	private Point3d targetPoint3d = null;
	private Point3d targetPoint3dAbs = null;
	private long timeToFailure = 0L;
	private long trackDelay = 1000L;
	private Vector3d trajectoryVector3d = null;
	private float turnDiffMax = 0F;
	private float attackMaxDistance = 5000F;
	private float missileProximityFuzeRadius = 50F;
	private String soundNameRadarPW = null;
	DecimalFormat twoPlaces = new DecimalFormat("+000.00;-000.00"); // only required for debugging

	protected Actor victim = null;
	private boolean lockingVictim = false;
	
	/**TODO Myotka*/
	public float Myotka_x;
	public float Myotka_y;
	public int MyotkaMode;
	public Actor MyotkaActor;
	public float MyotkaFOV;
	
	private double loft;
	private float loft_mode;
    private String HARM_MODE;
    private Point3d HARM_WP;

	
	// private static RangeRandom theRangeRandom;

	private Orient victimOffsetOrient = null;
	private Point3f victimOffsetPoint3f = null;
	private Vector3d victimSpeed = null;
	private boolean bRocketFiring = false;

	private boolean bLaserHoming = false;
	private Actor laserOwner = null;
	private boolean bSACLOSHoming = false;
	private Actor saclosOwner = null;

	private boolean bRealisticRadarSelect = false;

	private static final int MAT_WOOD = 0;
	private static final int MAT_BRICK = 1;
	private static final int MAT_STEEL = 2;
	private static final int MAT_FLESH = 3;
	private static final int N_MAT_TYPES = 4;
	private static float PenetrateEnergyToKill[][][] = (float[][][])null;
	private static float PenetrateThickness[][] = (float[][])null;
	private static final float KinEnergy_4 = 511.225F;
	private static final float KinEnergy_7_62 = 2453.88F;
	private static final float KinEnergy_12_7 = 10140F;
	private static final float KinEnergy_20 = 23400F;
	private static final float KinEnergy_37 = 131400F;
	private static final float KinEnergy_45 = 252000F;
	private static final float KinEnergy_50 = 369000F;
	private static final float KinEnergy_75 = 1224000F;
	private static final float KinEnergy_100 = 3295500F;
	private static final float KinEnergy_203 = 5120000F;
	private int damage_MAT_TYPE = -1;
	private int damage_EXPL_TYPE = -1;
	private int damage_EFF_BODY_MATERIAL = -1;
	private float damage_PANZER = 0.001F;
	private float damage_MIN_TNT = 0.07F;
	private float damage_MAX_TNT = 0.071F;
	private float damage_PROBAB_DEATH_WHEN_EXPLOSION = 0.4F;
	private static Point3d ppp = new Point3d();
	private boolean bMirrorValueSet = false;
	private float mirrorAzimuth = 0.0F;
	private float mirrorTangage = 0.0F;
	

	static
	{
		InitTablesOfEnergyToKill();
	}

	private static boolean bLogDetail = false;
	private static boolean bLogDetailDamage = false;
	private int iStatusCur_getArmy = 0;
	private int iStatusPrev_getArmy = 0;

}