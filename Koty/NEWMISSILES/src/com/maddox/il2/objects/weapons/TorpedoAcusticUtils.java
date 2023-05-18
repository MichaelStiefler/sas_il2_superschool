// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 30.03.2018 23:16:44
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TorpedoAcusticUtils.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import java.text.DecimalFormat;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class TorpedoAcusticUtils
{

    public TorpedoAcusticUtils(Actor owner)
    {
        angleActorPos = null;
        angleActorLoc = null;
        angleTargetPos = null;
        angleTargRayDir = null;
        angleNoseDir = null;
        distanceActorPos = null;
        distanceActorLoc = null;
        distanceTargetPos = null;
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        theOwner = null;
        trgtTorpedo = null;
        theOwner = owner;
        angleActorPos = new Point3d();
        angleActorLoc = new Loc();
        angleTargetPos = new Point3d();
        angleTargRayDir = new Vector3d();
        angleNoseDir = new Vector3d();
        distanceActorPos = new Point3d();
        distanceActorLoc = new Loc();
        distanceTargetPos = new Point3d();
    }

    public Actor getTorpedotarget()
    {
        return trgtTorpedo;
    }

    public void setTorpedotarget(Actor theTarget)
    {
        trgtTorpedo = theTarget;
    }

    private void HudLog(String logLine)
    {
        if((theOwner != World.getPlayerAircraft() || !((RealFlightModel)((SndAircraft) ((Aircraft)theOwner)).FM).isRealMode()) && (((SndAircraft) ((Aircraft)theOwner)).FM instanceof Pilot))
        {
            return;
        } else
        {
            HUD.log(logLine);
            return;
        }
    }

    public float Pk(Actor actorFrom, Actor actorTo)
    {
        float fPkRet = 0.0F;
        float fTemp = 0.0F;
        if(!(actorFrom instanceof Aircraft) || !(actorTo instanceof Aircraft))
            return fPkRet;
        float angleToTarget = angleBetween(actorFrom, actorTo);
        float angleFromTarget = 180F - angleBetween(actorTo, actorFrom);
        float distanceToTarget = (float)distanceBetween(actorFrom, actorTo);
        float gForce = ((SndAircraft) ((Aircraft)actorFrom)).FM.getOverload();
        fPkRet = 100F;
        if(distanceToTarget > 4500F || distanceToTarget < 400F || angleToTarget > 30F /*|| angleFromTarget > 70F*/ || gForce > 2.0F)
            return 0.0F;
        if(distanceToTarget > 1500F)
        {
            fTemp = distanceToTarget - 1500F;
            fTemp /= 3000F;
            fPkRet -= fTemp * fTemp * 20F;
        } else
        {
            fTemp = 1500F - distanceToTarget;
            fTemp /= 1100F;
            fPkRet -= fTemp * fTemp * 60F;
        }
        fTemp = angleToTarget / 30F;
        fPkRet -= fTemp * fTemp * 30F;
        fTemp = angleFromTarget / 70F;
        fPkRet -= fTemp * fTemp * 50F;
        fTemp = gForce / 2.0F;
        fPkRet -= fTemp * fTemp * 30F;
        if(fPkRet < 0.0F)
            fPkRet = 0.0F;
        return fPkRet;
    }

    public float angleBetween(Actor actorFrom, Actor actorTo)
    {
        float angleRetVal = 180.1F;
        if(!(actorFrom instanceof Aircraft) && !(actorFrom instanceof Bomb) && !(actorFrom instanceof ShipGeneric) && !(actorFrom instanceof BigshipGeneric) || !(actorTo instanceof Aircraft) && !(actorTo instanceof Bomb) && !(actorTo instanceof ShipGeneric) && !(actorTo instanceof BigshipGeneric))
        {
            return angleRetVal;
        } else
        {
            double angleDoubleTemp = 0.0D;
            actorFrom.pos.getAbs(angleActorLoc);
            angleActorLoc.get(angleActorPos);
            actorTo.pos.getAbs(angleTargetPos);
            angleTargRayDir.sub(angleTargetPos, angleActorPos);
            angleDoubleTemp = angleTargRayDir.length();
            angleTargRayDir.scale(1.0D / angleDoubleTemp);
            angleNoseDir.set(1.0D, 0.0D, 0.0D);
            angleActorLoc.transform(angleNoseDir);
            angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
            angleRetVal = Geom.RAD2DEG((float)Math.acos(angleDoubleTemp));
            return angleRetVal;
        }
    }

    public float angleActorBetween(Actor actorFrom, Actor actorTo)
    {
        float angleRetVal = 180.1F;
        double angleDoubleTemp = 0.0D;
        actorFrom.pos.getAbs(angleActorLoc);
        angleActorLoc.get(angleActorPos);
        actorTo.pos.getAbs(angleTargetPos);
        angleTargRayDir.sub(angleTargetPos, angleActorPos);
        angleDoubleTemp = angleTargRayDir.length();
        angleTargRayDir.scale(1.0D / angleDoubleTemp);
        angleNoseDir.set(1.0D, 0.0D, 0.0D);
        angleActorLoc.transform(angleNoseDir);
        angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float)Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    public double distanceBetween(Actor actorFrom, Actor actorTo)
    {
        double distanceRetVal = 99999.998999999996D;
        if(!Actor.isValid(actorFrom) || !Actor.isValid(actorTo))
        {
            return distanceRetVal;
        } else
        {
            actorFrom.pos.getAbs(distanceActorLoc);
            distanceActorLoc.get(distanceActorPos);
            actorTo.pos.getAbs(distanceTargetPos);
            distanceRetVal = distanceActorPos.distance(distanceTargetPos);
            return distanceRetVal;
        }
    }

    public Actor lookTorpedo(Actor actor, float maxFOVfrom, double maxDistance)
    {
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        double mintargetDistance = maxDistance + 1000D;
        Actor selectedActor = null;
        Point3f theSelectedActorOffset = new Point3f();
        if(!(actor instanceof Aircraft) && !(actor instanceof Bomb))
        {
            HUD.log("Actor invalid!");
            return selectedActor;
        }
        try
        {
            List list = Engine.targets();
            int k = list.size();
            for(int i1 = 0; i1 < k; i1++)
            {
                Actor theTarget1 = (Actor)list.get(i1);
                if((theTarget1 instanceof ShipGeneric) || (theTarget1 instanceof BigshipGeneric))
                {
                    targetDistance = distanceBetween(actor, theTarget1);
                    if(targetDistance <= maxDistance)
                    {
                        targetAngle = angleBetween(actor, theTarget1);
                        if(targetAngle <= maxFOVfrom && targetDistance < mintargetDistance)
                        {
                            mintargetDistance = targetDistance;
                            selectedActor = theTarget1;
                        }
                    }
                }
            }

        }
        catch(Exception e)
        {
            EventLog.type("Exception in selectedActor");
            EventLog.type(e.toString());
            EventLog.type(e.getMessage());
        }
        return selectedActor;
    }

    private static final float PK_MAX_ANGLE = 30F;
    private static final float PK_MAX_ANGLE_AFT = 70F;
    private static final float PK_MIN_DIST = 400F;
    private static final float PK_OPT_DIST = 1500F;
    private static final float PK_MAX_DIST = 4500F;
    private static final float PK_MAX_G = 2F;
    private Point3d angleActorPos;
    private Loc angleActorLoc;
    private Point3d angleTargetPos;
    private Vector3d angleTargRayDir;
    private Vector3d angleNoseDir;
    private Point3d distanceActorPos;
    private Loc distanceActorLoc;
    private Point3d distanceTargetPos;
    private DecimalFormat twoPlaces;
    private Actor theOwner;
    private Actor trgtTorpedo;
}