// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 21.08.2019 15:06:47
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StationarySAM.java

package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtSAM;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;

import java.util.List;

// Referenced classes of package com.maddox.il2.objects.vehicles.stationary:
//            StationaryGeneric, StationaryGenericSAM

public abstract class S_75SAM
{
    public static class SNR_75M extends S75_GenericSAM
        implements TgtSAM, TgtFlak, AAA
    {
    	

        public void Target()
        {
            rndRange = TrueRandom.nextDouble(0.0D, 8000D);
            rndAlt = TrueRandom.nextDouble(0.0D, 400D);
            List list = Engine.targets();
            int i = list.size();
            if(lock == null)
            {
                for(int j = 0; j < i; j++)
                {
                    Actor contact = (Actor)list.get(j);
                    if((contact instanceof Aircraft) && contact.pos.getAbsPoint().distance(super.pos.getAbsPoint()) > 8000D && contact.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 26000D + rndRange && ((Tuple3d) (contact.pos.getAbsPoint())).z - World.land().HQ(((Tuple3d) (super.pos.getAbsPoint())).x, ((Tuple3d) (super.pos.getAbsPoint())).y) > 900D - rndAlt && ((Tuple3d) (contact.pos.getAbsPoint())).z < 27000D && contact.getArmy() != getArmy() && !Landscape.rayHitHQ(super.pos.getAbsPoint(), contact.pos.getAbsPoint(), tmpp1) && ((SndAircraft) ((Aircraft)contact)).FM.getOverload() < 4F)
                        lock = contact;
                }

            }
            if(lock != null && (((SndAircraft) ((Aircraft)lock)).FM.getOverload() > 16F || Landscape.rayHitHQ(super.pos.getAbsPoint(), lock.pos.getAbsPoint(), tmpp1) || !Actor.isValid(lock) || !(lock instanceof Aircraft) || lock.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 7000D || lock.pos.getAbsPoint().distance(super.pos.getAbsPoint()) > 43000D || ((Tuple3d) (lock.pos.getAbsPoint())).z - World.land().HQ(((Tuple3d) (super.pos.getAbsPoint())).x, ((Tuple3d) (super.pos.getAbsPoint())).y) < 900D - rndAlt || ((Tuple3d) (lock.pos.getAbsPoint())).z > 30000D))
                lock = null;
            checkCountermeasure();
        }

        private void checkCountermeasure()
        {
            if(lock != null)
            {
                List theCountermeasures = Engine.countermeasures();
                int lockTime = TrueRandom.nextInt(1000);
                int counterMeasureSize = theCountermeasures.size();
                for(int counterMeasureIndex = 0; counterMeasureIndex < counterMeasureSize; counterMeasureIndex++)
                {
                    Actor flarechaff = (Actor)theCountermeasures.get(counterMeasureIndex);
                    double flareDistance = GuidedMissileUtils.distanceBetween(lock, flarechaff);
                    if((flarechaff instanceof RocketChaff) && 500 < lockTime && flareDistance < 500D)
                        lock = null;
                }

            }
        }

        public Actor getRadarContact()
        {
            return lock;
        }

        private static Point3d tmpp1 = new Point3d();
        public double rndRange;
        public double rndAlt;
        public Actor lock;


        public SNR_75M()
        {
        	
            lock = null;
        }
    }

    public static class S_75M extends S75_GenericSAM
        implements TgtSAM, TgtFlak, AAA
    {

    	public void update(float f)
    	{
    		
    		
    	}
        public void Target()
        {
        	
        	if (radar == null)
        	{
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                radar = (Actor)list.get(j);
                if((radar instanceof SNR_75M) && radar.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 2000D && radar.getArmy() == getArmy())
                {
                	
                }
                
                
            }
        	}
        	else
        	{

        		if(isValid(radar))
                target = ((SNR_75M)radar).getRadarContact();
        		double time = Time.current();
            	time -= timeFormer;
            	if(!launched && target != null) launcherAiming((float)time);
            	
                if(!launched && target != null && Actor.isValid(target) && launcher_ready)
                {
                    Point3d localPoint3d = new Point3d();
                    Orient localOrient = new Orient();
                    Vector3d localVector3d = new Vector3d();
                    super.pos.getAbs(localPoint3d, localOrient);
                    localVector3d.sub(target.pos.getAbsPoint(), super.pos.getAbsPoint());
                    super.pos.getAbsOrient().transformInv(localVector3d);
                    float f = 57.32484F * (float)Math.atan2(((Tuple3d) (localVector3d)).y, -((Tuple3d) (localVector3d)).x);
                    int k = (int)f + 180;
                    if(k > 360)
                        k -= 360;
                    if(k > 300 && k < 360 || k < 60)
                    {
                        int rnd = TrueRandom.nextInt(0, 100);
                        if(rnd < 50)
                        {
                            localOrient.setYPR(localOrient.getYaw(), localOrient.getPitch() + 45F, localOrient.getRoll());
                            localPoint3d.z += 5D;
                            netchannel = null;
                            
                            //super.setMesh("3do/SAM/SM-63/hier2.him");
                            super.hierMesh().chunkVisible("Dvina", false);
                            
                            launched = true;
                            MissileV755 missilev755 = new MissileV755(radar, netchannel, 1, localPoint3d, localOrient, 100F, target);
                        }
                    }
                }
        	}

        }
        
        public void initiateCooldown()
        {
        	
        }
        
        public void launcherAiming(float f)
        {
        	Point3d point3d = radar.pos.getAbsPoint();
            Orient orient = super.pos.getAbsOrient();
        	Vector3d vector3d = new Vector3d();
            vector3d.set(point3d);
            Point3d point3d1 = new Point3d();
            point3d1.set(target.pos.getAbsPoint());
            point3d1.sub(point3d);
            orient.transformInv(point3d1);
            
            float traverse = (float) Math.toDegrees(Math.atan2(point3d1.y, point3d1.x));
            float range = (float) Math.sqrt((point3d1.y * point3d1.y) + (point3d1.x * point3d1.x));
            if(range < 1000) range = 1000;
            float elevation = (float)Math.toDegrees(Math.atan(point3d1.z / range));
            launcher_lay[0] = traverse-launcher_lay_real[0];
            if(launcher_lay[0] < -180) launcher_lay[0]+=360;
            else{if(launcher_lay[0]>180) launcher_lay[0]-=360;}
            launcher_lay[1] = elevation-launcher_lay_real[1];
            
            if(launcher_lay[0]>(launcher_speed[0]*f)) launcher_lay[0] = launcher_speed[0]*f;
            else{if(launcher_lay[0]<(-launcher_speed[0]*f)) launcher_lay[0] = -launcher_speed[0]*f;}
            

            if(launcher_lay[1]>(launcher_speed[1]*f)) launcher_lay[1] = launcher_speed[1]*f;
            else{if(launcher_lay[1]<(-launcher_speed[1]*f)) launcher_lay[1] = -launcher_speed[1]*f;}
            
            launcher_lay_real[0]+=launcher_lay[0];
            launcher_lay_real[1]+=launcher_lay[1];
            
            if(elevation>launcher_limit[2])elevation = launcher_limit[2];
            if(elevation<launcher_limit[0])elevation = launcher_limit[0];
            super.hierMesh().chunkSetAngles("Head_SM63", traverse, 0f, 0f);
            super.hierMesh().chunkSetAngles("Gun_SM63", 0f, elevation, 0f);
            System.out.println("Dvina lay: " + traverse + "/" + elevation);
            
        }

        public Actor getMissileTarget()
        {
            return target;
        }

        private boolean launched;
        public double rndRange;
        public double rndAlt;
        public Actor target;
        private Actor radar;
        public NetChannel netchannel;
        private boolean launcher_ready;
        private boolean launcher_aimed;
        private float[] launcher_limit;
        private float[] launcher_speed;
        private float[] launcher_lay;
        private float[] launcher_lay_real;
        private double timeFormer;

        public S_75M()
        {
        	launcher_limit = new float[]{-5f,0f,85f};
        	launcher_speed = new float[]{16f,16f};
        	launcher_lay = new float[]{0f,0f};
        	launcher_lay_real = new float[]{0f,0f};
        	launcher_ready = false;
            target = null;
            launched = false;
            timeFormer = Time.current();
        }
    }


    public S_75SAM()
    {
    }

    static 
    {
        new ArtilleryGeneric.SPAWN(SNR_75M.class);
        new ArtilleryGeneric.SPAWN(S_75M.class);
    }
}