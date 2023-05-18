// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 21.08.2019 15:06:47
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StationarySAM.java

package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtSAM;
import com.maddox.il2.ai.ground.TypeSAM;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeJammer;
import com.maddox.il2.objects.air.TypeRadarWarningReceiver;
import com.maddox.il2.objects.air.TypeSemiRadar;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.il2.objects.vehicles.artillery.SWagon;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.SoundFX;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.vehicles.stationary:
//            StationaryGeneric, StationaryGenericSAM

public abstract class StationarySAM
{
    public static class SNR_75M extends StationaryGenericSAM
        implements TgtSAM, TgtFlak, AAA, TypeSAM, TypeSemiRadar
    {
    	
    	public void destroy()
    	{
    		int l = launcherQueue.size();
    		for(int i=0; i < l; i++)
    		{
    			S_75M localActor = (S_75M) launcherQueue.get(i);
    			localActor.target = null;
    		}
    		launcherQueue = new ArrayList();
    		
    		super.destroy();
    	}
        public void Target()
        {
        	if(!prepareCommandPoint())
        	{
        		lock = null;
        		TREVOGA = false;
        		return;
        	}
        	if(isAlive(this))
        	{
           
            if(lock == null)
            {
            	if(CommandPoint != null)
            	
            	{lock = getTarget();
            	
            	if(TREVOGA = false)
            	{
            		TREVOGA = ((S_75M_commandPoint)CommandPoint).isTREVOGA(this);
            		if(TREVOGA) launcherQueue = new ArrayList();
            	}
            	else
            		TREVOGA = ((S_75M_commandPoint)CommandPoint).isTREVOGA(this);
            	
            	}
            	else
            	{
                    List list = Engine.targets();
                    int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor contact = (Actor)list.get(j);
                    if((contact instanceof Aircraft) && contact.pos.getAbsPoint().distance(super.pos.getAbsPoint()) > 5000D && contact.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 65000D && ((Tuple3d) (contact.pos.getAbsPoint())).z - World.land().HQ(((Tuple3d) (super.pos.getAbsPoint())).x, ((Tuple3d) (super.pos.getAbsPoint())).y) > 200D && ((Tuple3d) (contact.pos.getAbsPoint())).z < 30000D && contact.getArmy() != getArmy() && !Landscape.rayHitHQ(super.pos.getAbsPoint(), contact.pos.getAbsPoint(), tmpp1) && ((SndAircraft) ((Aircraft)contact)).FM.getOverload() < 16F)
                        {lock = contact;        TREVOGA = true;      launcherQueue = new ArrayList();}
                }
            	}
            }
            
            
            if(lock != null && (((SndAircraft) ((Aircraft)lock)).FM.getOverload() > 16F || Landscape.rayHitHQ(super.pos.getAbsPoint(), lock.pos.getAbsPoint(), tmpp1) || !Actor.isValid(lock) || !(lock instanceof Aircraft) || lock.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 4500D || lock.pos.getAbsPoint().distance(super.pos.getAbsPoint()) > 100000D || ((Tuple3d) (lock.pos.getAbsPoint())).z - World.land().HQ(((Tuple3d) (super.pos.getAbsPoint())).x, ((Tuple3d) (super.pos.getAbsPoint())).y) < 100D || ((Tuple3d) (lock.pos.getAbsPoint())).z > 33000D))
                {lock = null; TREVOGA = false; waiter = 0; uplinkCommand = false; uplinkLive = false;}
            checkCountermeasure();
            if(lock != null)
            	pingRWR();
            if(!(launcherQueue.isEmpty())&&TREVOGA)prepareLaunchers();
            if(!(launcherQueue.isEmpty())&&TREVOGA)checkLauncherQueue();
        	}
        	else
        	{
        		TREVOGA = false;
        		lock = null;
        		return;
        	}
        	
        	
            if(Siren)resetSiren();
            }
        
        
        private void resetSiren()
        {
        	
        	if(Time.current() > SirenTimer + 5000)
        	{
        		Horn.cancel();
        		Siren = false;
        	}
        	
        }
        
        private void pingRWR()
        {
        	
        	if(lock instanceof TypeRadarWarningReceiver)
        		{
        		if(lock.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 30000 || uplinkLive)//uplinkLive)
            		((TypeRadarWarningReceiver)lock).myRadarLockYou(this, "aircraft.APR25S75EbandLock");
        		else
        			((TypeRadarWarningReceiver)lock).myRadarSearchYou(this, "aircraft.APR25S75EbandLOW");
        		//((TypeRadarWarningReceiver)lock).myRadarLockYou(this, "aircraft.APR25S75EbandLock");
        		
        		}
        	if(uplinkCommand){
        	guidanceUplink = Time.current();
        	if(guidanceUplink > uplinkTimer + 3000D)uplinkLive = true;

    		if(guidanceUplink - uplinkTimer > 30000 && uplinkCommand){uplinkCommand = false; uplinkLive = false;}
        	}
        }
        
        private void resetGuidanceTimer()
        {
        	uplinkTimer = Time.current();
        	uplinkCommand = true;
        }

        private void checkLauncherQueue()
        {
        	double time = Time.current();
        	if(time > launcher_timer + 10000D/* + waiter*/)
        	{
        		if(launcher_timer == 0){launcher_timer = time; return;}
        		
        		
        	if(launcher_indexer >= launcherQueue.size()) launcher_indexer = 0;
        	
        	try
        	{
        		if((S_75M) launcherQueue.get(launcher_indexer) != null)
        		{
        	S_75M localActor = (S_75M) launcherQueue.get(launcher_indexer);
        	if (localActor.PODGOTOVKA == 2)
        	{
        	
        	Siren = localActor.komanda_PUSK();
        	if(Siren)
        	{
        	Horn.start();
        	SirenTimer = Time.current();
        	}
        	}
        	else
        		launcher_indexer++;
        		}
        		else
        			launcher_indexer++;
        	//if(waiter < 200000)waiter += 20000D;
        	
        	}
        	catch(Exception exception)
	        {
	            exception.printStackTrace();
	        }
        	launcher_timer = time;
        	
        	}
        }
        
        //TODO:prepare command point;
        private boolean prepareCommandPoint()
        {
        	
        	if(CommandPoint != null)
        	{
        		if(((S_75M_commandPoint)CommandPoint).isAlive() && ((S_75M_commandPoint)CommandPoint).isPowered()) return true; else return false;
        	}
        	
        	List CPs = Engine.targets();
        	int i = CPs.size();
        	for(int j = 0; j<i; j++)
        	{
        	Actor commandPoint = (Actor)CPs.get(j);
        	if(commandPoint instanceof S_75M_commandPoint && commandPoint.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 500D)
        	{
        		if(((S_75M_commandPoint)commandPoint).getUsed())
        		{
        			continue;
        		}
        		CommandPoint = commandPoint;
        		((S_75M_commandPoint)CommandPoint).setUsed();
        		return true;
        	}
        	}
        	return false;
        }
        
        private Actor getTarget()
        {
        	if(CommandPoint == null)
        	return null;
        	else
        		return ((S_75M_commandPoint)CommandPoint).getDesignate(50000,  7000,  30000,  500,  360);
        }
        

        
        private void prepareLaunchers()
        {
        	
        	double time = Time.current();
        	if(time > prepare_timer + prepare_cooldown)
        	{
        		prepare_cooldown = 60000;
        	try
        	{   //System.out.println("Preparing launchers !");
        		PODGOTOVKA = 0;
        		int i = launcherQueue.size();
        		//System.out.println("No. of launchers: " + i);
        		for(int j = 0; j < i; j++)
        		{
        			//if(i<10)System.out.println("Checking launcher #" + j);
        			S_75M localActor = (S_75M) launcherQueue.get(j);
        			if(localActor.PODGOTOVKA == 1 || localActor.PODGOTOVKA == 2)PODGOTOVKA++;
                	
        		}
        		
        		for(int j = 0; j < i; j++)
        		{
        		
        		if((S_75M) launcherQueue.get(j) != null)
        		{
        	S_75M localActor = (S_75M) launcherQueue.get(j);
        	if(localActor.PODGOTOVKA == 0  && PODGOTOVKA <3)
        	{
        	localActor.prepareLauncher();
        	PODGOTOVKA++;
        	//System.out.println("Added preparation, launcher #" + j);
        	}
        		}
        		
        		}

        	}
        	catch(Exception exception)
	        {
	            exception.printStackTrace();
	        }
        	prepare_timer = time;
        	}
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
        public ArrayList launcherQueue;
        public int launcher_indexer;
        public double launcher_timer;
        public double waiter;
        private boolean TREVOGA;
        private double prepare_timer;
        private double prepare_cooldown;
        private int PODGOTOVKA;
        private boolean Siren;
        protected SoundFX Horn;
        protected double SirenTimer;
        
        private double guidanceUplink;
        private double uplinkTimer;
        private boolean uplinkLive;
        private boolean uplinkCommand;
        
        private boolean hasCommandPoint;
        private boolean checkCommandPoint;
        private Actor CommandPoint;

        protected float Rmin0;
        protected float Rmax0;
        protected float Hmin0;
        protected float Hmax0;
        protected float Rmin1;
        protected float Rmax1;
        protected float Hmin1;
        protected float Hmax1;
        

        public SNR_75M()
        {
            lock = null;
            launcherQueue = new ArrayList();
            launcher_indexer = 0;
            launcher_timer = 0;
            waiter = 0;
            TREVOGA = false;
            PODGOTOVKA = 0;
            prepare_cooldown = 0;
            Horn = newSound("objects.siren", false);
            guidanceUplink = 0;
            uplinkTimer = 0;
            uplinkLive = false;
            uplinkCommand = false;
            
            hasCommandPoint = false;
            checkCommandPoint = false;
            
            
            
            Rmin0 =  6000;
            Rmax0 = 50000;
            Hmin0 =   200;
            Hmax0 = 30000;
            Rmin1 =  5000;
            Rmax1 = 50000;
            Hmin1 =   100;
            Hmax1 = 33000;
        }
		public Actor getSemiActiveRadarLockedActor() {
			// TODO Auto-generated method stub
			return lock;
		}
		public Actor setSemiActiveRadarLockedActor(Actor paramActor) {
			// TODO Auto-generated method stub
			return null;
		}
		public boolean getSemiActiveRadarOn() {
			// TODO Auto-generated method stub
			return uplinkLive;
		}
		public boolean setSemiActiveRadarOn(boolean paramBoolean) {
			// TODO Auto-generated method stub
			return false;
		}
    }

    public static class S_75M_commandPoint extends StationaryGenericSAM
    implements TgtSAM, TgtFlak, SWagon
    {
    	
    	
    	public void Target()
    	{
    		
    	}
    	private boolean isUsed;
    	private Actor transformer;
    	private Actor designate;
    	private Actor EWR;
    	
    	public void setUsed()
    	{
    		isUsed = true;
    		connectPower();
    	}
    	public boolean getUsed()
    	{
    		return isUsed;
    	}
    	
        public boolean isTREVOGA(Actor actor)
        {
        	if(EWR != null)
        	return((EarlyWarningRadar)EWR).getTREVOGA(actor, 100000);    //TODO: nullPointerException
        	else
        	return false;
        }
    	
    	public void connectPower()
    	{
    		List list = Engine.targets();
    		int i = list.size();
    				for(int j = 0; j<i; j++)
    				{
    					Actor t = (Actor)list.get(j);
    					if(t instanceof S_75M_transformer && t.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 100D)
    					{
    						if(((S_75M_transformer)t).isUsed)continue;
    						transformer = t;
    						((S_75M_transformer)transformer).setUsed();
    						break;
    					}
    				}
    				connectSurveillance();
    	}
    	
    	public Actor getDesignate(float Rmax, float Rmin, float Hmax, float Hmin, float aspect)
    	{
    		if(EWR == null)
    		return null;
    		else
    			return ((EarlyWarningRadar)EWR).getTargetPlain(Rmax,  Rmin,  Hmax,  Hmin,  aspect);
    	}
    	
    	public void connectSurveillance()
    	{
    		List list = Engine.targets();
    		int i = list.size();
    				for(int j = 0; j<i; j++)
    				{
    					Actor t = (Actor)list.get(j);
    					if(t instanceof EarlyWarningRadar && t.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 200D)
    					{
    						if(((EarlyWarningRadar)t).isUsed)continue;
    						EWR = t;
    						((EarlyWarningRadar)EWR).setUsed();
    						break;
    					}
    				}
    	}
    	
    	public boolean isPowered()
    	{
    		if(transformer == null)return false;
    		if(((S_75M_transformer)transformer).isPowered())
    		return this.isAlive();
    		else return false;
    	}
    	
    	public S_75M_commandPoint()
    	{
    		isUsed = false;
    		transformer = null;
    		designate = null;
    		EWR = null;
    		//TargetContact tc = new TargetContact();
    	}
    }
    
    public static class S_75M_transformer extends StationaryGenericSAM
    implements TgtSAM, TgtFlak, SWagon
    {
    	private boolean isUsed;
    	private Actor[] generator;
    	
    	public void setUsed()
    	{
    		isUsed = true;
    		connectPower();
    	}
    	public boolean getUsed()
    	{
    		return isUsed;
    	}
    	
    	public boolean isPowered()
    	{
    		if(generator[0] == null || generator[1] == null || generator[2] == null)return false;
    		if(generator[0].isAlive() && generator[1].isAlive() && generator[2].isAlive())return this.isAlive();else return false;
    		
    	}
    	
    	public void connectPower()
    	{
    		int k = 0;
    		List list = Engine.targets();
    		int i = list.size();
    				for(int j = 0; j<i; j++)
    				{
    					Actor t = (Actor)list.get(j);
    					if(t instanceof S_75M_generator && t.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 50D)
    					{
    						if(k>2)break;
    						if(((S_75M_generator)t).isUsed)continue;
    						generator[k] = t;
    						((S_75M_generator)generator[k]).setUsed();

    						k++;
    					}
    				}
    	}
    	
    	public S_75M_transformer()
    	{
    		isUsed = false;
    		generator = new Actor[]{null,null,null};
    	}
    }
    
    public static class S_75M_generator extends StationaryGenericSAM
    implements TgtSAM, TgtFlak, SWagon
    {
    	private boolean isUsed;
    	public void setUsed()
    	{
    		isUsed = true;
    	}
    	public S_75M_generator()
    	{
    		isUsed = false;
    	}
    }
    
    public static class EarlyWarningRadar extends StationaryGenericSAM
    implements TgtSAM, TgtFlak, AAA
    {
    	private float Power;
    	private float RangeH0;
    	private float RangeH1;
    	private float RangeR0;
    	private float RangeR1;
    	private float IFOV;
    	private float IFOVh;
    	private float FOV;
    	private float PolarizationFilter;
    	private float RPM;
    	private long radar_timer;
    	private long radar_timer1;
    	private float radar_azimuth;
    	private int radar_direction;
    	private ArrayList all_targets;
    	private ArrayList all_targets_;
    	private boolean isUsed;
    	

    	
    	public void Target()
    	{
    		if(!this.isAlive())return;
    	
    		if(Time.current()> radar_timer + (60000/RPM))
    		{
    			all_targets_ = new ArrayList();
    			int iterator = all_targets.size();
    			for(int j = 0; j < iterator; j++)
    			{
    				Actor local_actor = (Actor)all_targets.get(j);
    				all_targets_.add(local_actor);
    			}
    			all_targets  = new ArrayList();

    		List list = Engine.targets();
    		int i = list.size();
    		Actor tgt;
    		
    		for(int j=0; j<i; j++)
    		{
    			if(list.get(j) instanceof Aircraft)
    			{
    				tgt = (Actor)list.get(j);
    				float dist = (float) tgt.pos.getAbsPoint().distance(this.pos.getAbsPoint());
    				float alt = (float) (tgt.pos.getAbsPoint().z - this.pos.getAbsPoint().z);
    				if(dist < (RangeR1*1.8f) /*&& Math.toDegrees(Math.atan(alt/dist)) < FOV*/ && tgt instanceof TypeRadarWarningReceiver)
    				{
    					if(!checkGroundLOS(tgt.pos.getAbsPoint(), this.pos.getAbsPoint()))
    						
    					((TypeRadarWarningReceiver)tgt).myRadarSearchYou((Actor)this, null);
    				}
    			}
    		}
    		radar_timer = Time.current();
    		}
    		
    		radarScan();
    		radarMovement();
    	}
    	
    	public Actor getTargetPlain(float Rmax, float Rmin, float Hmax, float Hmin, float aspect)   //TODO:check if asked
    	{
    		//System.out.println("getTargetPlain called");
    		if(all_targets!=null)
    		{
    		int i1 = all_targets.size();
    		for(int j = 0; j<i1; j++)
    		{
    			Actor local_actor = (Actor)all_targets.get(j);
    			if(local_actor.getArmy() != this.getArmy())
    			{
    			float distance = (float) local_actor.pos.getAbsPoint().distance(this.pos.getAbsPoint());
    			float height = (float) (local_actor.pos.getAbsPoint().z - this.pos.getAbsPoint().z);
    			float checkAspect = GuidedMissileUtils.angleBetween(local_actor, this);
    			if(distance < Rmax && distance > Rmin && height > Hmin && height < Hmax) 
    			{
    				//System.out.println("returnTarget"); 
    				return local_actor;
    				}
    			}
    		}
    		}
    		
    		if(all_targets_!=null)
    		{
    		int i2 = all_targets_.size();
    		for(int j = 0; j<i2; j++)
    		{
    			Actor local_actor = (Actor)all_targets_.get(j);
    			if(local_actor.getArmy() != this.getArmy())
    			{
    			float distance = (float) local_actor.pos.getAbsPoint().distance(this.pos.getAbsPoint());
    			float height = (float) (local_actor.pos.getAbsPoint().z - this.pos.getAbsPoint().z);
    			float checkAspect = GuidedMissileUtils.angleBetween(local_actor, this);
    			if(distance < Rmax && distance > Rmin && height > Hmin && height < Hmax)
    			{
    				//System.out.println("returnTarget_"); 
    				return local_actor;
    				}
    			}
    		}
    		}
    		return null;
    	}
    	public void getTargetJammer()
    	{
    		
    	}
    	
    	public void setUsed()
    	{
    		isUsed = true;
    	}
    	
    	private void radarScan()
    	{
    		if(Time.current() > radar_timer1 + 30)
    		{
    		List list = Engine.targets();
    		int i = list.size();
    		Actor tgt;
    		
    		
				double noise = noiseCheck(radar_azimuth);
		    	noise += noiseCheckBroad(radar_azimuth);
		    	double tresholdCheck =  16*Power/((Math.pow((RangeR1*1000)*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
		    	if (noise < tresholdCheck) noise = tresholdCheck;
    		
    		for(int j=0; j<i; j++)
    		{
    			if(list.get(j) instanceof Aircraft)
    			{
    				tgt = (Actor)list.get(j);
    				float dist = (float) tgt.pos.getAbsPoint().distance(this.pos.getAbsPoint());
    				float alt = (float) (tgt.pos.getAbsPoint().z - this.pos.getAbsPoint().z);
    				Point3d tgtPoint = new Point3d();
    				tgt.pos.getAbs(tgtPoint);
    				tgtPoint.sub(this.pos.getAbsPoint());
    				Orient tgtOrient = new Orient();
    				tgtOrient.transform(tgtPoint);
    				double tgtAzimuth = (Math.atan2(tgtPoint.y, tgtPoint.x));
    				tgtAzimuth -= radar_azimuth;
    				if(tgtAzimuth > 180)tgtAzimuth-=360; else if(tgtAzimuth < -180)tgtAzimuth+=360;
    				if(dist < RangeR1 && Math.toDegrees(Math.atan(alt/dist)) < FOV &&  tgtAzimuth < (IFOVh/2) &&  tgtAzimuth >  - (IFOVh/2))
    				{
    					
    					
    					
    					if(!all_targets.contains(tgt))
    					{
    						float ECCM_RCS = 16;
    						if(tgt instanceof TypeJammer)ECCM_RCS = ((TypeJammer)tgt).getJammerRCS(this);
    						double radar_return = ECCM_RCS*Power/((Math.pow(dist*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
    						if(radar_return > noise)
    						all_targets.add(tgt);
    					}
    				}
    			}	
    		}
    		radar_timer1 = Time.current();
    		}
    		radar_azimuth -= (Time.tickLenFs()*RPM*6)*radar_direction;
    		if(radar_azimuth >= 180)radar_azimuth = -180;
    		if(radar_azimuth <= -180)radar_azimuth = 180;
    	}
    	
    	private void radarMovement()
    	{
    		super.hierMesh().chunkSetAngles("Dish", radar_azimuth, 0f, 0f);
    	}
    	
    	private float noiseCheck(float x)
    	{
    		float backgroundNoise = 0;
    		Point3d point3d = ((Actor) (this)).pos.getAbsPoint();
            Orient orient = ((Actor) (this)).pos.getAbsOrient();
            List list = Engine.targets();
            int j = list.size();
            for(int k = 0; k < j; k++)
            {
            	Actor actor = (Actor)list.get(k);
                if(actor instanceof TypeJammer)
                {
                	float Jam = ((TypeJammer) actor).getJammerFlood('A', this);
                	if (Jam == 0)continue;
                	Point3d tgtPoint = new Point3d();
    				actor.pos.getAbs(tgtPoint);
    				tgtPoint.sub(this.pos.getAbsPoint());
    				Orient tgtOrient = new Orient();
    				tgtOrient.transform(tgtPoint);
    				double tgtAzimuth = (Math.atan2(tgtPoint.y, tgtPoint.x));
    				tgtAzimuth -= radar_azimuth;
    				if(tgtAzimuth > 180)tgtAzimuth-=360; else if(tgtAzimuth < -180)tgtAzimuth+=360;
    				
    				float dist = (float) actor.pos.getAbsPoint().distance(this.pos.getAbsPoint());
    				float alt = (float) (actor.pos.getAbsPoint().z - this.pos.getAbsPoint().z);
    	if(dist > 500 )
        {
      	  
      	  
      		  
      		  
    		
      		  double ECM_power = Jam/(4*3.1415926D*Math.pow(this.distance(actor), 2));
      		
      		  
      			  
      			  
                    
                    	
                    	float sidelobePower = 1f;
                    	if(Math.toDegrees(Math.atan(alt/dist)) < FOV*sidelobePower && tgtAzimuth < (IFOVh/2)*sidelobePower &&  tgtAzimuth >  - (IFOVh/2)*sidelobePower)
                    	{
                    		
                    		backgroundNoise += ECM_power;
                    	}
      		  
      		  
      	  
        			}
                }
            }
            
            if(PolarizationFilter < 1)PolarizationFilter=1;
    		return (backgroundNoise/PolarizationFilter);   //polarization bias
    	}
    	private float noiseCheckBroad(float x)
    	{
    		float backgroundNoise = 0;
    		Point3d point3d = ((Actor) (this)).pos.getAbsPoint();
            Orient orient = ((Actor) (this)).pos.getAbsOrient();
            List list = Engine.targets();
            int j = list.size();
            for(int k = 0; k < j; k++)
            {
            	Actor actor = (Actor)list.get(k);
                if(actor instanceof TypeJammer)
                {
                	float Jam = ((TypeJammer) actor).getJammerSpecial(3, 'A', this);
                	if (Jam == 0)continue;
                	Point3d tgtPoint = new Point3d();
    				actor.pos.getAbs(tgtPoint);
    				tgtPoint.sub(this.pos.getAbsPoint());
    				Orient tgtOrient = new Orient();
    				tgtOrient.transform(tgtPoint);
    				double tgtAzimuth = (Math.atan2(tgtPoint.y, tgtPoint.x));
    				tgtAzimuth -= radar_azimuth;
    				if(tgtAzimuth > 180)tgtAzimuth-=360; else if(tgtAzimuth < -180)tgtAzimuth+=360;
    				
    				float dist = (float) actor.pos.getAbsPoint().distance(this.pos.getAbsPoint());
    				float alt = (float) (actor.pos.getAbsPoint().z - this.pos.getAbsPoint().z);
    	if(dist > 500 )
        {
      	  
      	  
      		  
      		  
    		
      		  double ECM_power = Jam/(4*3.1415926D*Math.pow(this.distance(actor), 2));
      		
      		  
      			  
      			  
                    
                    	for(int i=1; i<9; i++)
                    	{
                    	float sidelobePower = i;
                    	if(Math.toDegrees(Math.atan(alt/dist)) < FOV*sidelobePower && tgtAzimuth < (IFOVh/2)*sidelobePower &&  tgtAzimuth >  - (IFOVh/2)*sidelobePower)
                    	{
                    		
                    		backgroundNoise += ECM_power;
                    	}
                    	}
      		  
      	  
        			}
                }
            }
            
            if(PolarizationFilter < 1)PolarizationFilter=1;
    		return (backgroundNoise/PolarizationFilter);   //polarization bias
    	}
    	
    	public boolean getTREVOGA (Actor actor, float range)
    	{
    		if(actor == null)return false;
    		if(all_targets!=null)
    		{
    		int i1 = all_targets.size();
    		for(int j = 0; j<i1; j++)
    		{
    			Actor local_actor = (Actor)all_targets.get(j);
    			if(local_actor.pos.getAbsPoint().distance(actor.pos.getAbsPoint()) < range)return true;
    		}
    		}
    		 if(all_targets_!=null)
    		 {

    	    		int i2 = all_targets_.size();
    		for(int j = 0; j<i2; j++)
    		{
    			Actor local_actor = (Actor)all_targets_.get(j);
    			if(local_actor.pos.getAbsPoint().distance(actor.pos.getAbsPoint()) < range)return true;
    		}
    		 }
    		
    		return false;
    	}
    	
    	public EarlyWarningRadar()
    	{
    		Power   = 1000000f;
    		RangeH0 = 300f;
    		RangeH1 = 25000f;
    		RangeR0 = 1000f;
    		RangeR1 = 150000f;
    		IFOV    = 4f;
    		IFOVh   = 4f;
    		FOV     = 45f;
    		PolarizationFilter = 0f;
    		RPM     = 1f;
    		all_targets = new ArrayList();
    		radar_direction = 1;
    		radar_azimuth = 0;
    	}
    }
    
    public static boolean checkGroundLOS(Point3d A, Point3d B)
    {
        Point3d point3d = new Point3d(A);
        Point3d point3d1 = new Point3d(B);
        Point3d point3d2 = new Point3d();
        if(Landscape.rayHitHQ(point3d, point3d1, point3d2))
            return false;
        else
            return true;
    }
    
    public static class P12_Radar extends EarlyWarningRadar
    {
    	public P12_Radar()
    	{
    		super.Power = 180000;
    		super.RPM = 12;
    		super.IFOV = 45;
    		super.IFOVh = 4;
    		super.PolarizationFilter = 2;
    	}
    }
    
    public static class S_75M extends StationaryGenericSAM
        implements TgtSAM, TgtFlak, AAA
    {
    	/*public void destroy()
    	{
    		if(radar!=null && Actor.isValid(radar) && Actor.isAlive(radar))
    		{
    			((SNR_75M)radar).launcherQueue.remove(this);
    			
    		}
    		super.destroy();
    	}*/

        public void Target()
        {
        	if(radar != null){if(!((Actor)radar).isAlive())return;}
        	
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                radar = (Actor)list.get(j);
                if((radar instanceof SNR_75M) && radar.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 2000D && radar.getArmy() == getArmy())
                {
                    target = ((SNR_75M)radar).getRadarContact();
                    if(!((SNR_75M)radar).launcherQueue.contains(this)||((SNR_75M)radar).launcherQueue.isEmpty())((SNR_75M)radar).launcherQueue.add(this);
                    break;
                }
            }
            
            double time = Time.current();
        	time -= timeFormer;
        	timeFormer = Time.current();
        	
        	if(PODGOTOVKA == 1)LauncherPreparation();
        	if(PODGOTOVKA == 2)LauncherLifespan();
        	if(komanda_PUSK)IGNITION_countdown();
        	
        	
        	if(!launched && target != null && isValid(radar) && (PODGOTOVKA == 1 || PODGOTOVKA == 2)) launcherAiming((float)time);
        	if(launched || target == null) launcherReset((float)time);
        	
            
        	
        	
        	if(!launched && target != null && Actor.isValid(radar) &&  Actor.isValid(target) && IGNITION && targetRHcheck() && PODGOTOVKA == 2)
            {
                Point3d localPoint3d = new Point3d();
                Orient localOrient = new Orient();
                Vector3d localVector3d = new Vector3d();
                super.pos.getAbs(localPoint3d, localOrient);
                localVector3d.sub(target.pos.getAbsPoint(), super.pos.getAbsPoint());
                super.pos.getAbsOrient().transformInv(localVector3d);
                /*float f = 57.32484F * (float)Math.atan2(((Tuple3d) (localVector3d)).y, -((Tuple3d) (localVector3d)).x);
                int k = (int)f + 180;
                if(k > 360)
                    k -= 360;
                if(k > 300 && k < 360 || k < 60)*/
                {
                	//((SNR_75M)radar).launcher_indexer++;
                	//((SNR_75M)radar).waiter += 20000;
                    //int rnd = TrueRandom.nextInt(0, 100);
                    //if(rnd < 50)
                    {
                        localOrient.setYPR(localOrient.getYaw()+launcher_lay_real[0], localOrient.getPitch()+launcher_lay_real[1], localOrient.getRoll());
                        localPoint3d.z += 5D;
                        netchannel = null;
                        //super.setMesh("3do/SAM/S-75/hier2.him");
                        super.hierMesh().chunkVisible("Dvina", false);
                        launched = true;
                        IGNITION = false;
                        PODGOTOVKA = 3;
                        ((SNR_75M)radar).launcherQueue.remove(this);
                        PUSK(localPoint3d,localOrient);
                        
                    }
                }
            }

        }
        
        public void prepareLauncher()
        {
        	//System.out.println("Preparing a launcher.");
        	prepare_timer = Time.current();
        	PODGOTOVKA = 1;
        }
        private void LauncherPreparation()
        {
        	
        	double time = Time.current();
        	if(time > prepare_timer + t0)
        	{
        		//System.out.println("Launcher ready.");
        		PODGOTOVKA = 2;
        	}
        }
        private void LauncherLifespan()
        {
        	
        	double time = Time.current();
        			if(time > prepare_timer + t1)
        			{
        				PODGOTOVKA = 0;
        			}
        }
        
        public boolean targetRHcheck()
        {
        	
        	if(target==null || !isValid(target))return false;
        	if(!launcher_aimed[2]) return false;
        	
        	
        	float tgt_altitude = (float) (((Tuple3d) (target.pos.getAbsPoint())).z - World.land().HQ(((Tuple3d) (super.pos.getAbsPoint())).x, ((Tuple3d) (super.pos.getAbsPoint())).y));
        	float tgt_range = (float) super.pos.getAbsPoint().distance(target.pos.getAbsPoint());
        	if(tgt_altitude<Hmax && tgt_altitude>Hmin && tgt_range < Rmax && tgt_range > Rmin)return true;
        	else
        	return false;
        }
        
        
        
        private void PUSK(Point3d point, Orient localOrient)
        {
        	MissileV755 missilev755;
        	MissileV755U missilev755u;
        	MissileV755U_low missilev755ulow;
        	
        	((SNR_75M)radar).resetGuidanceTimer();
        	
        	if(MissileType == "20D")
        	{
        	missilev755 = new MissileV755(radar, netchannel, 1, point, localOrient, 10F, target);
        	System.out.println("Launching 20D.");
        	}
        	if(MissileType == "20DS")
        	{
        		float tgt_altitude = (float) (((Tuple3d) (target.pos.getAbsPoint())).z - World.land().HQ(((Tuple3d) (radar.pos.getAbsPoint())).x, ((Tuple3d) (radar.pos.getAbsPoint())).y));
        		if(tgt_altitude>1500)
            	{missilev755u = new MissileV755U(radar, netchannel, 1, point, localOrient, 10F, target); System.out.println("Launching 20DS HIGH.");}
        		else
        		{missilev755ulow = new MissileV755U_low(radar, netchannel, 1, point, localOrient, 10F, target); System.out.println("Launching 20DS LOW.");}
        	}
        	
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
            //if(elevation < 10f)elevation = 10f;
            //if(elevation > 65f)elevation = 65f;
            
            double timer = Time.current();
            double dT = launcher_lay_t - timer;
            float d_traverse = 0f;
            if(dT <= 0) 
            {
            	launcher_azimuth_last = traverse;
            	launcher_lay_t = timer;
            }
            else
            {
            	d_traverse = traverse-launcher_azimuth_last;
            	d_traverse/=dT;
            	d_traverse*=4000;
            }
            
            launcher_lay[0] = (traverse+d_traverse)-launcher_lay_real[0];
            if(launcher_lay[0] < -180) launcher_lay[0]+=360;
            else{if(launcher_lay[0]>180) launcher_lay[0]-=360;}
            launcher_lay[1] = elevation-launcher_lay_real[1];
            
            
            
            if(launcher_lay[0]>(launcher_speed[0]*f/1000)) launcher_lay[0] = launcher_speed[0]*f/1000;
            else{if(launcher_lay[0]<(-launcher_speed[0]*f/1000)) launcher_lay[0] = -launcher_speed[0]*f/1000; else launcher_aimed[0]=true;}
            

            if(launcher_lay[1]>(launcher_speed[1]*f/1000)) launcher_lay[1] = launcher_speed[1]*f/1000;
            else{if(launcher_lay[1]<(-launcher_speed[1]*f/1000)) launcher_lay[1] = -launcher_speed[1]*f/1000; else launcher_aimed[1]=true;}
            
            launcher_lay_real[0]+=launcher_lay[0];
            launcher_lay_real[1]+=launcher_lay[1];
            
        	if(launcher_lay_real[1]>launcher_limit[2]){launcher_lay_real[1] = launcher_limit[2]; launcher_aimed[1]=true;}
            if(launcher_lay_real[1]<launcher_limit[0]){launcher_lay_real[1] = launcher_limit[0]; launcher_aimed[1]=true;}
            
            super.hierMesh().chunkSetAngles("Head_SM63", launcher_lay_real[0], 0f, 0f);
            super.hierMesh().chunkSetAngles("Gun_SM63", 0f, launcher_lay_real[1], 0f);
            if(launcher_aimed[0] && launcher_aimed[1]){
            	launcher_aimed[2]=true;//((SNR_75M)radar).launcherQueue.add(this);
            	launcher_aimed[0]=false;
            	launcher_aimed[1]=false;
            }
            else
            {
            	launcher_aimed[2]=false;
            	launcher_aimed[0]=false;
            	launcher_aimed[1]=false;
            }
            
            
        }

        public void launcherReset(float f){
        	launcher_lay[0] = launcher_lay_reset[0]-launcher_lay_real[0];
            if(launcher_lay[0] < -180) launcher_lay[0]+=360;
            else{if(launcher_lay[0]>180) launcher_lay[0]-=360;}
            launcher_lay[1] = launcher_lay_reset[1]-launcher_lay_real[1];
            
            if(launcher_lay[0]>(launcher_speed[0]*f/1000)) launcher_lay[0] = launcher_speed[0]*f/1000;
            else{if(launcher_lay[0]<(-launcher_speed[0]*f/1000)) launcher_lay[0] = -launcher_speed[0]*f/1000;}
            

            if(launcher_lay[1]>(launcher_speed[1]*f/1000)) launcher_lay[1] = launcher_speed[1]*f/1000;
            else{if(launcher_lay[1]<(-launcher_speed[1]*f/1000)) launcher_lay[1] = -launcher_speed[1]*f/1000;}
            
            launcher_lay_real[0]+=launcher_lay[0];
            launcher_lay_real[1]+=launcher_lay[1];
            
            if(launcher_lay_real[1]>launcher_limit[2])launcher_lay_real[1] = launcher_limit[2];
            if(launcher_lay_real[1]<launcher_limit[0])launcher_lay_real[1] = launcher_limit[0];
            super.hierMesh().chunkSetAngles("Head_SM63", launcher_lay_real[0], 0f, 0f);
            super.hierMesh().chunkSetAngles("Gun_SM63", 0f, launcher_lay_real[1], 0f);
        }
        public Actor getMissileTarget()
        {
            return target;
        }
        
        public boolean komanda_PUSK()
        {
        	
        	komanda_PUSK = targetRHcheck();
        	if(komanda_PUSK){ PUSK_counter = Time.current();
        	System.out.println("P U S K");}
        	return komanda_PUSK;
        }
        
        public void IGNITION_countdown()
        {
        	if(Time.current()>PUSK_counter + 4000)
        	{
        		IGNITION = true;
        		komanda_PUSK = false;
        	}
        }
        

        private boolean launched;
        public double rndRange;
        public double rndAlt;
        public Actor target;
        protected Actor radar;
        public NetChannel netchannel;
        private boolean komanda_PUSK;
        private boolean IGNITION;
        private double PUSK_counter;
        private boolean[] launcher_aimed;
        private float[] launcher_limit;
        private float[] launcher_speed;
        private float[] launcher_lay;
        private float[] launcher_lay_real;
        private float[] launcher_lay_reset;
        private double launcher_lay_t;
        private float launcher_azimuth_last;
        private double timeFormer;
        private float Rmin;
        private float Rmax;
        private float Hmin;
        private float Hmax;
        private double prepare_timer;
        private int PODGOTOVKA;
        protected double t0;
        protected double t1;
        protected String MissileType;

        public S_75M()
        {
            target = null;
            launched = false;
            
            launcher_limit = new float[]{-5f,20f,65f};
            launcher_lay_reset = new float[]{0,10};
        	launcher_speed = new float[]{10f,10f};
        	launcher_lay = new float[]{0f,0f};
        	launcher_lay_real = new float[]{0f,0f};
        	launcher_aimed = new boolean[]{false,false,false};
            timeFormer = Time.current();
            Rmin = 7000;
            Rmax = 40000;
            Hmin = 3000;
            Hmax = 30000;
            PODGOTOVKA = 0;
            t0 = 90000;
            t1 = 1200000;
            MissileType = "20D";
            launcher_lay_t = Time.current();
        }
    }

    public static class S_75Ma extends S_75M
    {
    	public S_75Ma()
    	{
    		super.Rmin = 7000;
    		super.Rmax = 45000;
    		super.Hmin = 200;
    		super.Hmax = 30000;
    		super.MissileType = "20DS";
    	}
    }
    
    public static class S_75Mb extends S_75M
    {
    	public S_75Mb()
    	{
    		super.Rmin = 7000;
    		super.Rmax = 45000;
    		super.Hmin = 200;
    		super.Hmax = 30000;
    		t0 = 22000;
    		t1 = 300000;
    		super.MissileType = "20DS";
    	}
    }

    public StationarySAM()
    {
    }

    static 
    {
        new StationaryGeneric.SPAWN(SNR_75M.class);
        new StationaryGeneric.SPAWN(S_75M.class);
        new StationaryGeneric.SPAWN(S_75Ma.class);
        new StationaryGeneric.SPAWN(S_75Mb.class);
        new StationaryGeneric.SPAWN(S_75M_commandPoint.class);
        new StationaryGeneric.SPAWN(S_75M_transformer.class);
        new StationaryGeneric.SPAWN(S_75M_generator.class);
        new StationaryGeneric.SPAWN(P12_Radar.class);
        
        
    }
}