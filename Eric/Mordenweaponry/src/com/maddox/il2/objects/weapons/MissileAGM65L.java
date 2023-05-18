package com.maddox.il2.objects.weapons;

import java.util.List;

import com.maddox.rts.Property;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.air.TypeLaserSpotter;

//import com.maddox.il2.objects.air.Mi24V;

public class MissileAGM65L extends Missile {

    public boolean interpolateStep()
    {  	
    	float f = Time.tickLenFs();
        float f1 = (float)getSpeed((Vector3d)null);
        f1 += (4000F - f1) * 0.1F * f;
        pos.getAbs(p, or);
        v.set(1.0D, 0.0D, 0.0D);
        or.transform(v);
        v.scale(f1);
        setSpeed(v);
        p.x += v.x * (double)f;
        p.y += v.y * (double)f;
        p.z += v.z * (double)f;
        if(isNet() && isNetMirror())
        {
            pos.setAbs(p, or);
            return false;
        }
        if(Actor.isValid(getOwner()))
        {
            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
            {
                Pilot pilot = (Pilot)fm;
                if(pilot.target_ground != null)
                    victim = pilot.target_ground;
                else
                if(pilot.target != null)
                    victim = pilot.target.actor;
                else
                    victim = null;
            } else
            {
            	checklaser();
                if(!laseron)
                    if(victim.isDestroyed())
                    {
                        victim = null;
                    } else
                    {
                        victim.pos.getAbs(pT);
                        pT.sub(p);
                        or.transformInv(pT);
                        if(pT.y > pT.x / 4D || pT.y < -pT.x / 4D || pT.z > pT.x / 4D || pT.z < -pT.x / 4D)
                            victim = null;
                    }
                if(Time.current() > tStart + 50000L && (getOwner() instanceof TypeX4Carrier) && ((TypeX4Carrier)fm.actor).typeX4CgetdeltaAzimuth() != ((TypeX4Carrier)fm.actor).typeX4CgetdeltaTangage())
                {
                    ((TypeX4Carrier)fm.actor).typeX4CResetControls();
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                }
            }
            if(laseron)
            {
            	pT.sub(p);
                or.transformInv(pT);
                float f2 = 0.1F;
                if(p.distance(pT) > 0.0D)
                {
                    if(pT.y > 0.10000000000000001D)
                        deltaAzimuth = -f2;
                    if(pT.y < -0.10000000000000001D)
                        deltaAzimuth = f2;
                    if(pT.z < -0.10000000000000001D)
                        deltaTangage = -f2;
                    if(pT.z > 0.10000000000000001D)
                        deltaTangage = f2;
                    or.increment(30F * f2 * deltaAzimuth, 30F * f2 * deltaTangage, 5520F * f);
                    deltaAzimuth = deltaTangage = 0.0F;
                }
            } else
            {
                or.increment(0.0F, 0.0F, 0.0F);
            }
        }
        pos.setAbs(p, or);
        return false;
    }
    
    private void checklaser()
    {
        laseron = false;
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if((actor instanceof TypeLaserSpotter) && actor.pos.getAbsPoint().distance(pos.getAbsPoint()) < 20000D && actor == World.getPlayerAircraft())
            {
                Point3d point3d = new Point3d();
                TypeLaserSpotter _tmp = (TypeLaserSpotter)actor;
                point3d = TypeLaserSpotter.spot;
                if(pos.getAbsPoint().distance(point3d) >= 15000D);
                pT.set(point3d);
                laseron = true;
            }
        }

    }
	
  static class SPAWN extends Missile.SPAWN {

    public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
      new MissileAGM65L(actor, netchannel, i, point3d, orient, f);
    }
  }

  public MissileAGM65L(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
    this.MissileInit(actor, netchannel, i, point3d, orient, f);
    fm = null;
    tStart = 0L;
    pos.setAbs(point3d, orient);
    pos.reset();
    pos.setBase(actor, (Hook)null, true);
    doStart(-1F);
    v.set(1.0D, 0.0D, 0.0D);
    orient.transform(v);
    v.scale(f);
    setSpeed(v);
    collide(false);
  }

  public MissileAGM65L() {
      victim = null;
      fm = null;
      tStart = 0L;
      deltaAzimuth = 0.0F;
      deltaTangage = 0.0F;
      victim = null;
  }
  
  public void start(float f, int i)
  {
      Actor actor = pos.base();
      if(Actor.isValid(actor) && (actor instanceof Aircraft))
      {
          if(actor.isNetMirror())
          {
              destroy();
              return;
          }
      }
      doStart(f);
  }

  private void doStart(float f)
  {
      super.start(-1F, 0);
      fm = ((Aircraft)getOwner()).FM;
      tStart = Time.current();
      pos.getAbs(p, or);
      or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
      pos.setAbs(p, or);
  }

  
  private boolean laseron;
  private FlightModel fm;
  private static Orient or = new Orient();
  private static Point3d p = new Point3d();
  private static Point3d pT = new Point3d();
  private static Vector3d v = new Vector3d();
  private long tStart;
  private float deltaAzimuth;
  private float deltaTangage;
  //private Actor victim;

  static {
    Class class1 = com.maddox.il2.objects.weapons.MissileAGM65L.class;
    Property.set(class1, "mesh", "3do/arms/AGM65L/mono.sim");
    Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
    Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
    Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeBlack_missiles.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 0.4F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "timeLife", 60F);
    Property.set(class1, "timeFire", 40.8F);
    Property.set(class1, "force", 30000F);
    Property.set(class1, "forceT1", 2F);
    Property.set(class1, "forceP1", 0.0F);
    Property.set(class1, "forceT2", 10.0F);
    Property.set(class1, "forceP2", 50F);
    Property.set(class1, "dragCoefficient", 0.15F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "power", 20.52F);
    Property.set(class1, "radius", 34.93F);
    Property.set(class1, "kalibr", 0.8F);
    Property.set(class1, "massa", 204F);
    Property.set(class1, "massaEnd", 125F);
    Property.set(class1, "stepMode", 0);
    Property.set(class1, "launchType", 0);
    Property.set(class1, "detectorType", 4);
    Property.set(class1, "sunRayAngle", 0.0F);
    Property.set(class1, "multiTrackingCapable", 0);
    Property.set(class1, "canTrackSubs", 0);
    Property.set(class1, "minPkForAI", 25F);
    Property.set(class1, "timeForNextLaunchAI", 5000L);
    Property.set(class1, "engineDelayTime", 0L);
    Property.set(class1, "attackDecisionByAI", 0);
    Property.set(class1, "targetType", 16);
    Property.set(class1, "shotFreq", 1.01F);
    Property.set(class1, "groundTrackFactor", 16F);
    Property.set(class1, "flareLockTime", 1000L);
    Property.set(class1, "trackDelay", 200L);
    Property.set(class1, "failureRate", 10F);
    Property.set(class1, "maxLockGForce", 15F);
    Property.set(class1, "maxFOVfrom", 30F);
    Property.set(class1, "maxFOVto", 360F);
    Property.set(class1, "PkMaxFOVfrom", 25F);
    Property.set(class1, "PkMaxFOVto", 360F);
    Property.set(class1, "PkDistMin", 1000F);
    Property.set(class1, "PkDistOpt", 3000F);
    Property.set(class1, "PkDistMax", 10000F);
    Property.set(class1, "leadPercent", 70F);
    Property.set(class1, "maxGForce", 12F);
    Property.set(class1, "stepsForFullTurn", 12);
    Property.set(class1, "fxLock", (String)null);
    Property.set(class1, "fxNoLock", (String)null);
    Property.set(class1, "smplLock", (String)null);
    Property.set(class1, "smplNoLock", (String)null);
    Property.set(class1, "friendlyName", "AGM-65E");
    Spawn.add(class1, new SPAWN());
  }
}
