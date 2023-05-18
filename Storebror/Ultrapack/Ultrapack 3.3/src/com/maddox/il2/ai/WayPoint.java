/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.bridges.Bridge;

public class WayPoint {

    public WayPoint() {
        this.p = new Point3d();
        this.deltaZ = -1;
        this.groundTargetAlt = -1;
        this.bHadTarget = false;
        this.bTargetFinished = false;
        this.bRadioSilence = false;
        this.resetWayPointExtra();
    }

    public WayPoint(Point3f point3f) {
        this(point3f.x, point3f.y, point3f.z);
    }

    public WayPoint(Point3d point3d) {
        this((float) point3d.x, (float) point3d.y, (float) point3d.z);
    }

    public WayPoint(float f, float f1, float f2) {
        this.p = new Point3d();
        this.deltaZ = -1;
        this.groundTargetAlt = -1;
        this.bHadTarget = false;
        this.bTargetFinished = false;
        this.bRadioSilence = false;
        this.set(f, f1, f2, 83F);
    }

    public WayPoint(float f, float f1, float f2, float f3) {
        this.p = new Point3d();
        this.deltaZ = -1;
        this.groundTargetAlt = -1;
        this.bHadTarget = false;
        this.bTargetFinished = false;
        this.bRadioSilence = false;
        this.set(f, f1, f2, f3);
    }

    public float x() {
        return (float) this.p.x;
    }

    public float y() {
        return (float) this.p.y;
    }

    public float z() {
        return (float) this.p.z;
    }

    public void setTarget(String s) {
        if (s == null) this.bHadTarget = false;
        else this.bHadTarget = true;
        this.sTarget = s;
        this.target = null;
    }

    public boolean isTargetFinished() {
        this.getTarget();
        return this.bTargetFinished;
    }

    public String getTargetName() {
        return this.sTarget;
    }

    public Actor getTarget() {
        if (this.sTarget != null) {
            Actor actor = Actor.getByName(this.sTarget);
            if (actor != null) {
                this.target = actor;
                this.sTarget = null;
            }
        }
        if (this.target == null || this.target != null && !this.target.isAlive()) {
            this.bTargetFinished = true;
            this.target = null;
        }
        if (!this.bHadTarget) this.bTargetFinished = false;
        return this.target;
    }

    public Actor getTargetActorRandom() {
        Actor actor = this.getTarget();
        if (!Actor.isValid(actor) || !actor.isAlive()) return null;
        if (actor instanceof Chief || actor instanceof Bridge) {
            int i = actor.getOwnerAttachedCount();
            if (i < 1) return null;
            for (int j = 0; j < i; j++) {
                Actor actor1 = (Actor) actor.getOwnerAttached(World.Rnd().nextInt(0, i - 1));
                if (Actor.isValid(actor1) && actor1.isAlive()) return actor1;
            }

            for (int k = 0; k < i; k++) {
                Actor actor2 = (Actor) actor.getOwnerAttached(k);
                if (Actor.isValid(actor2) && actor2.isAlive()) return actor2;
            }

            return null;
        } else return actor;
    }

    public void set(float f, float f1, float f2) {
        this.p.set(f, f1, f2);
    }

    public void set(Point3f point3f) {
        this.p.set(point3f);
    }

    public void set(Point3d point3d) {
        this.p.set(point3d);
    }

    public void set(float f, float f1, float f2, float f3) {
        this.p.set(f, f1, f2);
        this.Speed = f3;
        this.resetWayPointExtra();
    }

    public void set(float f) {
        this.Speed = f;
    }

    public void setTimeout(int i) {
        this.timeout = i;
    }

    public void set(WayPoint waypoint) {
        this.set(waypoint.getP());
        this.set(waypoint.getV());
        this.sTarget = waypoint.sTarget;
        this.target = waypoint.target;
        this.Action = waypoint.Action;
        this.timeout = waypoint.timeout;
        this.bTargetFinished = waypoint.bTargetFinished;
        this.bHadTarget = waypoint.bHadTarget;
        this.bRadioSilence = waypoint.bRadioSilence;
        this.waypointType = waypoint.waypointType;
        this.cycles = waypoint.cycles;
        this.orient = waypoint.orient;
        this.baseSize = waypoint.baseSize;
        if (this.baseSize < 2) this.baseSize = 2;
        this.delayTimer = waypoint.delayTimer;
        if (this.delayTimer < 0) this.delayTimer = 0;
        this.takeoffSpacing = waypoint.takeoffSpacing;
        if (this.takeoffSpacing > -5 && this.takeoffSpacing < 0) this.takeoffSpacing = -5;
        if (this.takeoffSpacing < 5 && this.takeoffSpacing >= 0) this.takeoffSpacing = 5;
        this.altDifference = waypoint.altDifference;
        this.formation = waypoint.formation;
    }

    public Point3d getP() {
        return this.p;
    }

    public void getP(Point3f point3f) {
        point3f.set(this.p);
    }

    public void getP(Point3d point3d) {
        point3d.set(this.p);
    }

    public float getV() {
        return this.Speed;
    }

    public static Vector3f vector(WayPoint waypoint, WayPoint waypoint1) {
        V1.x = (float) (waypoint1.p.x - waypoint.p.x);
        V1.y = (float) (waypoint1.p.y - waypoint.p.y);
        V1.z = (float) (waypoint1.p.z - waypoint.p.z);
        return V1;
    }

    public boolean isRadioSilence() {
        return this.bRadioSilence;
    }

    public void setRadioSilence(boolean flag) {
        this.bRadioSilence = flag;
    }

    public void resetWayPointExtra() {
        this.waypointType = 0;
        this.cycles = 0;
        this.orient = 0.0F;
        this.delayTimer = 0;
        this.takeoffSpacing = 20;
        this.ignoreAlt = 0;
        this.baseSize = 5;
        this.targetTrigger = 0;
        this.altDifference = 0;
        this.formation = 0;
    }

    public static final int NORMFLY                 = 0;
    public static final int TAKEOFF                 = 1;
    public static final int LANDING                 = 2;
    public static final int GATTACK                 = 3;
    private Point3d         p;
    public int              deltaZ;
    public int              groundTargetAlt;
    public float            Speed;
    public int              Action;
    public int              timeout;
    private boolean         bHadTarget;
    protected String        sTarget;
    protected Actor         target;
    protected boolean       bTargetFinished;
    protected boolean       bRadioSilence;
    public int              waypointType;
    public static final int WP_TAKEOFF_NORMAL_DELAY = 1;
    public static final int WP_TAKEOFF_PAIRS        = 2;
    public static final int WP_TAKEOFF_LINE         = 3;
    public static final int WP_TAKEOFF_TAXI         = 4;
    public static final int WP_TAKEOFF_TAXI_FSP     = 5;
    public static final int WP_LANDING_RIGHT        = 101;
    public static final int WP_LANDING_SHORTLEFT    = 102;
    public static final int WP_LANDING_SHORTRIGHT   = 103;
    public static final int WP_LANDING_STRAIGHTIN   = 104;
    public static final int WP_NORMFLY_CAP_TRIANGLE = 401;
    public static final int WP_NORMFLY_CAP_SQUARE   = 402;
    public static final int WP_NORMFLY_CAP_PENTAGON = 403;
    public static final int WP_NORMFLY_CAP_HEXAGON  = 404;
    public static final int WP_NORMFLY_CAP_RANDOM   = 405;
    public static final int WP_NORMFLY_CAP_ONPATROL = 406;
    public static final int WP_NORMFLY_ART_SPOT     = 407;
    public int              cycles;
    public float            orient;
    public int              baseSize;
    public int              altDifference;
    public int              targetTrigger;
    public int              delayTimer;
    public int              takeoffSpacing;
    public int              ignoreAlt;
    public int              formation;
    private static Vector3f V1                      = new Vector3f();

}
