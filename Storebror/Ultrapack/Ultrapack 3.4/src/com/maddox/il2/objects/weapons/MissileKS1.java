package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class MissileKS1 extends Missile implements MissileInterceptable {

    static class SPAWN extends Missile.SPAWN {
        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
            new MissileKS1(actor, netchannel, i, point3d, orient, f);
        }
    }

    public MissileKS1() {
    }

    public MissileKS1(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    // private float victimOffsetZ = Float.MIN_VALUE;
    private float cruiseAltitude        = Float.MIN_VALUE;
    private float smoothDampeningFactor = 1F;
    private int   altOffsetSign         = 0;
    private int   flightPhase           = 0;
    Vector3d      speedVector           = new Vector3d();

    public boolean computeSpecialStepBefore() {

        if (!Missile.actorHasPos(this.getVictim())) // System.out.println("victim invalid!");
            return true;
//			return false;
        float victimDistance = (float) GuidedMissileUtils.distanceBetween(this, this.getVictim());
//		if (victimOffsetZ == Float.MIN_VALUE) {
//			victimOffsetZ = this.getVictimOffsetPoint3f().z;
////			System.out.println("victim offset = " + victimOffsetZ);
//		}
        if (this.cruiseAltitude == Float.MIN_VALUE) if (Missile.actorHasPos(this.getOwner())) {
            this.cruiseAltitude = (float) this.getOwner().pos.getAbsPoint().z;
            if (this.cruiseAltitude < 2000F) this.cruiseAltitude = 2000F;
            if (this.cruiseAltitude > 10000F) this.cruiseAltitude = 10000F;
//				System.out.println("cruise altitude = " + cruiseAltitude);
        }
        float alt = (float) this.pos.getAbsPoint().z;
        if (victimDistance < 2500F) {
//			System.out.println("distance=" + victimDistance + ", offset z=" + 5F + ", alt=" + alt);
//			this.setVictimOffsetZ(this.victimOffsetZ);
            this.setVictimOffsetZ(5F);
            return true;
        }
        float offsetZ = 2000F;
        if (victimDistance < 20000F) {
            if (this.flightPhase != 2) {
                this.flightPhase = 2;
                this.smoothDampeningFactor = 1F;
                this.altOffsetSign = 0;
            }
            offsetZ = 400F;
            if (victimDistance > 5000F) {
                float altOffset = alt - offsetZ;
                if (altOffset > 20F) {
                    if (this.altOffsetSign != 1) {
                        if (this.altOffsetSign != 0) this.smoothDampeningFactor *= 2F;
                        this.altOffsetSign = 1;
                    }
                    offsetZ -= Math.min(1F, altOffset / 1500F) * (victimDistance - alt) / this.smoothDampeningFactor;
                } else if (altOffset < -20F) {
                    if (this.altOffsetSign != -1) {
                        if (this.altOffsetSign != 0) this.smoothDampeningFactor *= 2F;
                        this.altOffsetSign = -1;
                    }
                    offsetZ += Math.min(1F, -altOffset / 1500F) * (victimDistance - alt) / this.smoothDampeningFactor;
                }
            }
        } else {
            if (this.flightPhase != 1) {
                this.flightPhase = 1;
                this.smoothDampeningFactor = 1F;
                this.altOffsetSign = 0;
            }
            offsetZ = this.cruiseAltitude;
            if (victimDistance > 30000F) {
                float altOffset = alt - offsetZ;
                if (altOffset > 100F) {
                    if (this.altOffsetSign != 1) {
                        if (this.altOffsetSign != 0) this.smoothDampeningFactor *= 2F;
                        this.altOffsetSign = 1;
                    }
                    offsetZ -= Math.min(1F, altOffset / 1500F) * (victimDistance - alt) / this.smoothDampeningFactor;
                } else if (altOffset < -100F) {
                    if (this.altOffsetSign != -1) {
                        if (this.altOffsetSign != 0) this.smoothDampeningFactor *= 2F;
                        this.altOffsetSign = -1;
                    }
                    offsetZ += Math.min(1F, -altOffset / 1500F) * (victimDistance - alt) / this.smoothDampeningFactor;
                }
            }
        }
//		System.out.println("distance=" + victimDistance + ", offset z=" + offsetZ + ", alt=" + alt);
        this.setVictimOffsetZ(offsetZ);

        return true;
    }

    public boolean computeSpecialStepAfter() {
        float turnStepMax = this.getTurnStepMax();
        if (turnStepMax == 0F) return true;
        float roll = this.getMissileOrient().getKren();
        while (roll > 180F)
            roll -= 360F;
        while (roll < -180F)
            roll += 360F;

        float curTurn = this.getDeltaAzimuth();
        float targetRoll = curTurn / turnStepMax;
        if (targetRoll > 1F) targetRoll = 1F;
        if (targetRoll < -1F) targetRoll = -1F;
        targetRoll *= 45F;

        float newRoll = (roll * 2F + targetRoll) / 3F;
//		System.out.println("Failstate" + this.getFailState() + ", speed=" + (this.getSpeed(null) * 3.6F) + ", turnStepMax=" + turnStepMax + ", curTurn=" + curTurn + ", targetRoll=" + targetRoll + ", Roll=" + this.getMissileOrient().getKren() + ", newRoll=" + newRoll);
        this.getMissileOrient().setYPR(this.getMissileOrient().getYaw(), this.getMissileOrient().getPitch(), -newRoll);

        float speed = (float) this.getSpeed(this.speedVector) * 3.6F;
        float machOne = MissilePhysics.getMachForAlt((float) this.pos.getAbsPoint().z);
        if (speed > machOne * 0.95F) {
            this.speedVector.scale(machOne * 0.95F / speed);
            this.setSpeed(this.speedVector);
        }

        return true;
    }

    static {
        GuidedMissileUtils.parseMissilePropertiesFile(MissileKS1.class);
        Spawn.add(MissileKS1.class, new SPAWN());
    }
}
