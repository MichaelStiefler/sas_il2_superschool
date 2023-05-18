// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 14.01.2020 2:22:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileAIM7Mhl_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.*;
import com.maddox.il2.objects.air.TypeHARM_carrier;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileAGM88C_gn16 extends Missile
{
	private com.maddox.il2.fm.FlightModel fm;
    static class SPAWN extends Missile.SPAWN
    {

    	
        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileAGM88C_gn16(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }



    
    public boolean interpolateStep()
    {
    	float f1 = Time.tickLenFs();
    	float f2 = (float)getSpeed(null);
    	
    	//Missile acceleration: does not work properly?
        
    	if(tStart+10000 > Time.current())
    		f2+=(28*f1);
    		//f2+=((640f-f2)*0.1f)*f1;
        if(tStart+40000 < Time.current())
        	f2+=((200f-f2)*0.1f)*f1;
        	
        
        
    	this.pos.getAbs(p, or);
    	v.set(1.0D, 0.0D, 0.0D);
    	or.transform(v);
    	v.scale(f2);
    	setSpeed(v);
    	p.x += v.x * f1;
    	p.y += v.y * f1;
    	p.z += v.z * f1;
    	if ((isNet()) && (isNetMirror()))
    	{
    		this.pos.setAbs(p, or);
    		return false;
    	}
    	
    	if (((getOwner() != com.maddox.il2.ai.World.getPlayerAircraft()) || (!((com.maddox.il2.fm.RealFlightModel)this.fm).isRealMode())) && ((this.fm instanceof com.maddox.il2.ai.air.Pilot)))
    	{
    		//TODO: AI targeting
    	}
    	else
    	{
    		/*if (this.victim.isDestroyed())
    		{
    		  this.victim = null;
    		}*/
    		
    		{
    			
    		checkTarget();
    		
    		
    		//Missile steering
    			pT.sub(p);
    			or.transformInv(pT);
    			if (pT.x > 0.0D && tStart + 2500L < Time.current() )
    			{
    			  if (pT.y > 0.1D*pT.x)
    			    this.deltaAzimuth = -1.0F;
    			  if (pT.y < -0.1D*pT.x)
    			    this.deltaAzimuth = 1.0F;
    			  if ((pT.z+loft.z) < -0.1D*pT.x)
    			    this.deltaTangage = -1.0F;
    			  if ((pT.z+loft.z) > 0.1D*pT.x)
    			    this.deltaTangage = 1.0F;
    			  or.increment(10.0F * f1 * this.deltaAzimuth, 10.0F * f1 * this.deltaTangage, 0.0F);
    			  or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
    			  this.deltaAzimuth = (this.deltaTangage = 0.0F);
    			}
    			else {
    		      or.increment(0.0F, 0.0F, 0.0F);
    		    }
    		}
    	}
    	this.pos.setAbs(p, or);

    	return false;
    }
    
    
    	public void checkTarget()
    	{
    		if(Actor.isValid(this.victim))
    		{
    		pT.set(this.victim.pos.getAbsPoint());
    		pT.z+=2.0;
    		}
    		
    		//TODO:LOFT   (target a point at target+added height)
    		float d = (float) GuidedMissileUtils.distanceBetween(this.victim, this);
    		float H = conversion(d,10000f,2500f);
    		loft.set(0,0,H);
    		//TODO:WAYPOINT    (first target a way point, then target radar)
    		//TODO:PRE-BRIEFED     (target a specific class in a designated area)
    		
    	}
    	



    	public float conversion(float a, float b, float c)
    	{
    		float result = 0;
    		if(a<b)result = 0;
    		else
    			if(a>=b)result = c;
    			
    		return result;
    	}
    
    public void start(float paramFloat, int paramInt)
    {
      Actor localActor = this.pos.base();
      if ((Actor.isValid(localActor)) && ((localActor instanceof com.maddox.il2.objects.air.Aircraft)) && (localActor.isNetMirror()))
      {
        destroy();
        return;
      }
      
      doStart(paramFloat);
    }
    
    private void doStart(float paramFloat)
    {
      super.start(-1.0F, 0);
      this.fm = ((com.maddox.il2.objects.air.Aircraft)getOwner()).FM;
      this.tStart = Time.current();
      this.pos.getAbs(p, or);
      or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
      this.pos.setAbs(p, or);
      this.ownerOfHarm = ((TypeHARM_carrier)getOwner());
      this.victim = this.ownerOfHarm.getHarmVictim();
      
      
    }
    
    
    
    public MissileAGM88C_gn16()
    {
    	super.victim = null;
    	this.fm = null;
    	this.tStart = 0L;
    	this.deltaAzimuth = 0.0F;
    	this.deltaTangage = 0.0F;
    	this.victim = null;
    }

    public MissileAGM88C_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
             this.victim = null;
             this.fm = null;
             this.tStart = 0L;
             this.pos.setAbs(point3d, orient);
             this.pos.reset();
             this.pos.setBase(actor, null, true);
             doStart(-1.0F);
             v.set(1.0D, 0.0D, 0.0D);
             orient.transform(v);
             v.scale(f);
             setSpeed(v);
             collide(false);
        
        
        
    }

    public boolean isVictim = false;
    public Actor VictimActor;
    public TypeHARM_carrier ownerOfHarm;

    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Vector3d v = new Vector3d();
    private static Point3d pT = new Point3d();
    private static Point3d loft = new Point3d();
    private long tStart;
    private float deltaAzimuth;
    private float deltaTangage;
    private Actor victim;

    static 
    {
        Class class1 = MissileAGM88C_gn16.class;
        Property.set(class1, "mesh", "3do/arms/AGM88_HARM_gn16/mono.sim");
        //Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        //Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSparrowFlame.sim");
        //Property.set(class1, "smoke", "3DO/Effects/Aircraft/TurboHWK109D.eff");
        //Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail2.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 300F);
        Property.set(class1, "timeFire", 10F);
        Property.set(class1, "force", 35750);
        Property.set(class1, "timeSustain", 30F);
        Property.set(class1, "forceSustain", 6750);
        //Property.set(class1, "forceT1", 0.5F);
        //Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "dragCoefficient", 0.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 66F);
        Property.set(class1, "radius", 25F);
        Property.set(class1, "proximityFuzeRadius", 0F);
        Property.set(class1, "kalibr", 0.25F);
        Property.set(class1, "massa", 355F);
        Property.set(class1, "massaEnd", 200F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 0);
        Property.set(class1, "sunRayAngle", 0.0F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 0L);
        Property.set(class1, "attackDecisionByAI", 2);
        Property.set(class1, "targetType", 16);
        Property.set(class1, "shotFreq", 0.01F);
        Property.set(class1, "groundTrackFactor", 0F);
        Property.set(class1, "flareLockTime", 0L);
        Property.set(class1, "trackDelay", 2500L);
        Property.set(class1, "failureRate", 10F);
        Property.set(class1, "maxLockGForce", 40F);
        Property.set(class1, "maxFOVfrom", 90F);
        Property.set(class1, "maxFOVto", 360F);
        Property.set(class1, "PkMaxFOVfrom", 90F);
        Property.set(class1, "PkMaxFOVto", 360F);
        Property.set(class1, "PkDistMin", 1000F);
        Property.set(class1, "PkDistOpt", 100000F);
        Property.set(class1, "PkDistMax", 150000F);
        Property.set(class1, "leadPercent", 0F);
        Property.set(class1, "maxGForce", 6F);
        Property.set(class1, "stepsForFullTurn", 10);
        Property.set(class1, "fxLock", (String)"aircraft.APR25S75EbandLock");
        Property.set(class1, "fxLockVolume", 1.0F);
        Property.set(class1, "fxNoLock", (String)null);
        Property.set(class1, "fxNoLockVolume", 1.0F);
        Property.set(class1, "smplLock", (String)"APR25S75EbandLock.wav");
        Property.set(class1, "smplNoLock", (String)null);
        Property.set(class1, "friendlyName", "AGM-88C");
        Spawn.add(class1, new SPAWN());
    }
}