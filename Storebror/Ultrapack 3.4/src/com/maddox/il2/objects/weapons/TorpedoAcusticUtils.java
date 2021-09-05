package com.maddox.il2.objects.weapons;

import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;

public class TorpedoAcusticUtils {

    public TorpedoAcusticUtils(Actor owner) {
        this.angleActorPos = null;
        this.angleActorLoc = null;
        this.angleTargetPos = null;
        this.angleTargRayDir = null;
        this.angleNoseDir = null;
        this.distanceActorPos = null;
        this.distanceActorLoc = null;
        this.distanceTargetPos = null;
        this.trgtTorpedo = null;
        this.angleActorPos = new Point3d();
        this.angleActorLoc = new Loc();
        this.angleTargetPos = new Point3d();
        this.angleTargRayDir = new Vector3d();
        this.angleNoseDir = new Vector3d();
        this.distanceActorPos = new Point3d();
        this.distanceActorLoc = new Loc();
        this.distanceTargetPos = new Point3d();
    }

    public Actor getTorpedotarget() {
        return this.trgtTorpedo;
    }

    public void setTorpedotarget(Actor theTarget) {
        this.trgtTorpedo = theTarget;
    }

    public float Pk(Actor actorFrom, Actor actorTo) {
        float fPkRet = 0.0F;
        float fTemp = 0.0F;
        if (!(actorFrom instanceof Aircraft) || !(actorTo instanceof Aircraft)) {
            return fPkRet;
        }
        float angleToTarget = this.angleBetween(actorFrom, actorTo);
        float angleFromTarget = 180F - this.angleBetween(actorTo, actorFrom);
        float distanceToTarget = (float) this.distanceBetween(actorFrom, actorTo);
        float gForce = ((Aircraft) actorFrom).FM.getOverload();
        fPkRet = 100F;
        if ((distanceToTarget > TorpedoAcusticUtils.PK_MAX_DIST) || (distanceToTarget < TorpedoAcusticUtils.PK_MIN_DIST) || (angleToTarget > TorpedoAcusticUtils.PK_MAX_ANGLE) || (angleFromTarget > TorpedoAcusticUtils.PK_MAX_ANGLE_AFT) || (gForce > 2.0F)) {
            return 0.0F;
        }
        if (distanceToTarget > TorpedoAcusticUtils.PK_OPT_DIST) {
            fTemp = distanceToTarget - TorpedoAcusticUtils.PK_OPT_DIST;
            fTemp /= 3000F;
            fPkRet -= fTemp * fTemp * 20F;
        } else {
            fTemp = TorpedoAcusticUtils.PK_OPT_DIST - distanceToTarget;
            fTemp /= 1100F;
            fPkRet -= fTemp * fTemp * 60F;
        }
        fTemp = angleToTarget / TorpedoAcusticUtils.PK_MAX_ANGLE;
        fPkRet -= fTemp * fTemp * TorpedoAcusticUtils.PK_MAX_ANGLE;
        fTemp = angleFromTarget / TorpedoAcusticUtils.PK_MAX_ANGLE_AFT;
        fPkRet -= fTemp * fTemp * 50F;
        fTemp = gForce / TorpedoAcusticUtils.PK_MAX_G;
        fPkRet -= fTemp * fTemp * TorpedoAcusticUtils.PK_MAX_ANGLE;
        if (fPkRet < 0.0F) {
            fPkRet = 0.0F;
        }
        return fPkRet;
    }

    public float angleBetween(Actor actorFrom, Actor actorTo) {
        float angleRetVal = 180.1F;
        if ((!(actorFrom instanceof Aircraft) && !(actorFrom instanceof Bomb) && !(actorFrom instanceof ShipGeneric) && !(actorFrom instanceof BigshipGeneric)) || (!(actorTo instanceof Aircraft) && !(actorTo instanceof Bomb) && !(actorTo instanceof ShipGeneric) && !(actorTo instanceof BigshipGeneric))) {
            return angleRetVal;
        } else {
            double angleDoubleTemp = 0.0D;
            actorFrom.pos.getAbs(this.angleActorLoc);
            this.angleActorLoc.get(this.angleActorPos);
            actorTo.pos.getAbs(this.angleTargetPos);
            this.angleTargRayDir.sub(this.angleTargetPos, this.angleActorPos);
            angleDoubleTemp = this.angleTargRayDir.length();
            this.angleTargRayDir.scale(1.0D / angleDoubleTemp);
            this.angleNoseDir.set(1.0D, 0.0D, 0.0D);
            this.angleActorLoc.transform(this.angleNoseDir);
            angleDoubleTemp = this.angleNoseDir.dot(this.angleTargRayDir);
            angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
            return angleRetVal;
        }
    }

    public float angleActorBetween(Actor actorFrom, Actor actorTo) {
        float angleRetVal = 180.1F;
        double angleDoubleTemp = 0.0D;
        actorFrom.pos.getAbs(this.angleActorLoc);
        this.angleActorLoc.get(this.angleActorPos);
        actorTo.pos.getAbs(this.angleTargetPos);
        this.angleTargRayDir.sub(this.angleTargetPos, this.angleActorPos);
        angleDoubleTemp = this.angleTargRayDir.length();
        this.angleTargRayDir.scale(1.0D / angleDoubleTemp);
        this.angleNoseDir.set(1.0D, 0.0D, 0.0D);
        this.angleActorLoc.transform(this.angleNoseDir);
        angleDoubleTemp = this.angleNoseDir.dot(this.angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    public double distanceBetween(Actor actorFrom, Actor actorTo) {
        double distanceRetVal = 99999.999D;
        if (!Actor.isValid(actorFrom) || !Actor.isValid(actorTo)) {
            return distanceRetVal;
        } else {
            actorFrom.pos.getAbs(this.distanceActorLoc);
            this.distanceActorLoc.get(this.distanceActorPos);
            actorTo.pos.getAbs(this.distanceTargetPos);
            distanceRetVal = this.distanceActorPos.distance(this.distanceTargetPos);
            return distanceRetVal;
        }
    }

    public Actor lookTorpedo(Actor actor, float maxFOVfrom, double maxDistance) {
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        double mintargetDistance = maxDistance + 1000D;
        Actor selectedActor = null;
        if (!(actor instanceof Aircraft) && !(actor instanceof Bomb)) {
            HUD.log("Actor invalid!");
            return selectedActor;
        }
        try {
            List list = Engine.targets();
            int k = list.size();
            for (int i1 = 0; i1 < k; i1++) {
                Actor theTarget1 = (Actor) list.get(i1);
                if ((theTarget1 instanceof ShipGeneric) || (theTarget1 instanceof BigshipGeneric)) {
                    targetDistance = this.distanceBetween(actor, theTarget1);
                    if (targetDistance <= maxDistance) {
                        targetAngle = this.angleBetween(actor, theTarget1);
                        if ((targetAngle <= maxFOVfrom) && (targetDistance < mintargetDistance)) {
                            mintargetDistance = targetDistance;
                            selectedActor = theTarget1;
                        }
                    }
                }
            }

        } catch (Exception e) {
            EventLog.type("Exception in selectedActor");
            EventLog.type(e.toString());
            EventLog.type(e.getMessage());
        }
        return selectedActor;
    }

    private static final float PK_MAX_ANGLE     = 30F;
    private static final float PK_MAX_ANGLE_AFT = 70F;
    private static final float PK_MIN_DIST      = 400F;
    private static final float PK_OPT_DIST      = 1500F;
    private static final float PK_MAX_DIST      = 4500F;
    private static final float PK_MAX_G         = 2F;
    private Point3d            angleActorPos;
    private Loc                angleActorLoc;
    private Point3d            angleTargetPos;
    private Vector3d           angleTargRayDir;
    private Vector3d           angleNoseDir;
    private Point3d            distanceActorPos;
    private Loc                distanceActorLoc;
    private Point3d            distanceTargetPos;
    private Actor              trgtTorpedo;
}
