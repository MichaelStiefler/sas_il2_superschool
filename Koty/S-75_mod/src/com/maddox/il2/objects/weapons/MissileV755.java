// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 21.08.2019 15:17:22
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileV755.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.vehicles.stationary.StationarySAM;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;

import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MissileSAM, Rocket

public class MissileV755 extends MissileSAM
{
    static class SPAWN extends MissileSAM.SPAWN
    {

        public void doSpawn(Actor paramActor, NetChannel paramNetChannel, int paramInt, Point3d paramPoint3d, Orient paramOrient, float paramFloat, Actor radartarget)
        {
            new MissileV755(paramActor, paramNetChannel, paramInt, paramPoint3d, paramOrient, paramFloat, radartarget);
        }

        SPAWN()
        {
        }
    }


    public MissileV755(Actor paramActor, NetChannel paramNetChannel, int paramInt, Point3d paramPoint3d, Orient paramOrient, float paramFloat, Actor radartarget)
    {
        MissileSAMInit(paramActor, paramNetChannel, paramInt, paramPoint3d, paramOrient, paramFloat, radartarget);
        radar = paramActor;
        checkMesh = true;
        //radar = null;
        P_fuze = 300;
        timerLaunch = Time.current();
    }
    

    

    public boolean interpolateStep()
    {
    	float overG = ((float)super.getSpeed(null)/700f)*6.5f;
    	if (overG>6.5f) overG = 6.5f;
    	super.setMaxG(overG);
        if(radar == null || !Actor.isValid(radar) || ((com.maddox.il2.objects.vehicles.stationary.StationarySAM.SNR_75M)radar).getRadarContact() == null || !Actor.isValid(((com.maddox.il2.objects.vehicles.stationary.StationarySAM.SNR_75M)radar).getRadarContact()))
            super.victim = null;
        if(radar != null && Actor.isValid(radar))
        {
            //double rnd = TrueRandom.nextFloat(0.0F, 100F);
            //double rndDist = TrueRandom.nextFloat(0.0F, 200F);
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if((actor instanceof Aircraft) && /*rnd < 20D && */super.pos.getAbsPoint().distance(actor.pos.getAbsPoint()) <= 100D && actor.getArmy() != getArmy())
                {
                	if(super.pos.getAbsPoint().distance(actor.pos.getAbsPoint()) <= P_fuze) P_fuze = (float) super.pos.getAbsPoint().distance(actor.pos.getAbsPoint());
                	if(super.pos.getAbsPoint().distance(actor.pos.getAbsPoint()) > P_fuze)
                	{
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                	}
                }
            }
            if(super.victim != null)
            if((super.victim.pos.getAbsPoint()).z - World.land().HQ(((Tuple3d) (super.victim.pos.getAbsPoint())).x, ((Tuple3d) (super.victim.pos.getAbsPoint())).y) < 1500) super.victim = null;

            if(super.pos.getAbsPoint().z - World.land().HQ(super.pos.getAbsPoint().x, super.pos.getAbsPoint().y)<=0){
            	super.doExplosionAir();
                postDestroy();
                collide(false);
                drawing(false);
            }
            if(Time.current() > timerLaunch+2000 && checkMesh == true){
            	super.setMesh("3do/SAM/Dvina/V-755/missile_.sim");
            	super.missileMass -= 900;
        	checkMesh = false; 
        	Vector3d speed = new Vector3d(); 
        	super.getSpeed(speed);
        	Orient orient = super.pos.getAbsOrient();
        	Point3d point = super.pos.getAbsPoint();
        	//new BombBooster(this, speed, orient, point);
            }
        }
        
        super.interpolateStep();
        return true;
    }

    public MissileV755()
    {
    	
    }

    private boolean checkMesh;
    public Actor radar;
    public float P_fuze;
    public double timerLaunch;

    static 
    {
        Class localClass = MissileV755.class;
        Property.set(localClass, "mesh", "3do/SAM/Dvina/V-755/missile.sim");
        Property.set(localClass, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(localClass, "flame", "3do/Effects/RocketSidewinder/RocketSparrowFlame.sim");
        Property.set(localClass, "smoke", "3do/Effects/RocketSidewinder/RocketSparrowSmokeBlack.eff");
        Property.set(localClass, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail2.eff");
        //Property.set(localClass, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        //Property.set(localClass, "flame", "3do/effects/misil/mono.sim");
        //Property.set(localClass, "smoke", "3DO/Effects/Tracers/Piropatron/Smokeflare.eff");
        Property.set(localClass, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(localClass, "emitLen", 60F);
        Property.set(localClass, "emitMax", 1.0F);
        Property.set(localClass, "sound", "weapon.rocketSAM");
        Property.set(localClass, "radius", 244F);//244
        Property.set(localClass, "timeLife", 65F);
        Property.set(localClass, "timeFire", 2F);
        Property.set(localClass, "force", 245250F);
        Property.set(localClass, "timeSustain", 22F);
        Property.set(localClass, "forceSustain", 30400F);
        //Property.set(localClass, "forceT1", 0.5F);
        //Property.set(localClass, "forceP1", 0.0F);
        Property.set(localClass, "dragCoefficient", 0.30F);
        Property.set(localClass, "dragCoefficientTurn", 0.60F);
        Property.set(localClass, "power", 268f);//134F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.65F);
        Property.set(localClass, "massa", 2160F);
        Property.set(localClass, "massaEnd", 1900F);
        Property.set(localClass, "stepMode", 1);
        Property.set(localClass, "launchType", 1);
        Property.set(localClass, "detectorType", 2);
        Property.set(localClass, "sunRayAngle", 0.0F);
        Property.set(localClass, "multiTrackingCapable", 1);
        Property.set(localClass, "canTrackSubs", 0);
        Property.set(localClass, "engineDelayTime", 0L);
        Property.set(localClass, "targetType", 1);
        Property.set(localClass, "shotFreq", 0.01F);
        Property.set(localClass, "groundTrackFactor", 0.0F);
        Property.set(localClass, "flareLockTime", 500L);
        Property.set(localClass, "trackDelay", 2000L);
        Property.set(localClass, "failureRate", 0F);
        Property.set(localClass, "maxLockGForce", 6F);
        Property.set(localClass, "maxFOVfrom", 360F);
        Property.set(localClass, "maxFOVto", 360F);
        Property.set(localClass, "PkDistMax", 60000F);
        Property.set(localClass, "leadPercent", 50F);
        Property.set(localClass, "maxGForce", 6F);
        Property.set(localClass, "stepsForFullTurn", 4);
        Property.set(localClass, "friendlyName", "SA-2");
        Spawn.add(localClass, new SPAWN());
    }
}