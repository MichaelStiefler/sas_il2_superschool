package com.maddox.il2.game;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.rts.Property;

public class ZutiPadObject
{
	public static Mat ARTILLERY_TANK_ICON = null;
	public static Mat AAA_ICON = null;
	public static Mat TRAIN_ICON = null;
	public static Mat AIRCRAFT_ICON = null;
	public static Mat SHIP_ICON = null;
	public static Mat CAR_ICON = null;
	public static Mat ROCKET_ICON = null;
	
	static
	{
		// TODO: Additional Icon Initialization moved to class constructor to avoid startup errors
//		ARTILLERY_TANK_ICON = Mat.New("icons/fieldgun.mat");
//		AAA_ICON = Mat.New("icons/antiaircraft.mat");
//		TRAIN_ICON = Mat.New("icons/Train.mat");
//		AIRCRAFT_ICON = Mat.New("icons/plane.mat");
//		SHIP_ICON = Mat.New("icons/shipDestroyer.mat");
//		CAR_ICON = Mat.New("icons/car.mat");
//		ROCKET_ICON = Mat.New("icons/objV1.mat");
	}
	
	private Actor actor;
	private Point3d livePos = null;
	private Point3d oldPos = null;
	private Point3d deadPos = new Point3d(-99999.99999D, -99999.99999D, 0.0D);
	private float azimut = 0F;
	private boolean isPlayerPlane = false;
	private boolean isPlayerArmyScout;
	private boolean visibleForPlayerArmy = false;
	private boolean scoutsAsRadar = false;
	private boolean refreshIntervalSet = false;
	
	private String name = null;
	
	/**
	 * 0 = Aircraft;
	 * 1 = Tank;
	 * 2 = AAA;
	 * 3 = Rocket;
	 * 4 = Ship;
	 * 5 = Trains, Cars
	 */
	public int type = -1;
	
	public ZutiPadObject(Actor inActor, boolean refreshIntervalSet)
	{
		try
		{
			if( inActor == null )
				return;
			
			if( ((Actor)World.getPlayerAircraft()) == inActor )
				isPlayerPlane = true;
			
			this.refreshIntervalSet = refreshIntervalSet;
			actor = inActor;
			livePos = actor.pos.getAbsPoint();
			oldPos = new Point3d(livePos.x, livePos.y, livePos.z);
			azimut = actor.pos.getAbsOrient().azimut();
			name = actor.name();
			isPlayerArmyScout = ZutiRadarObject.isPlayerArmyScout(inActor, ZutiSupportMethods.getPlayerArmy());
			scoutsAsRadar = Mission.MDS_VARIABLES().zutiRadar_ScoutsAsRadar;
			
			// TODO: Additional Icon Initialization moved to class constructor to avoid startup errors
			if (ARTILLERY_TANK_ICON == null) ARTILLERY_TANK_ICON = Mat.New("icons/fieldgun.mat");
			if (AAA_ICON == null) AAA_ICON = Mat.New("icons/antiaircraft.mat");
			if (TRAIN_ICON == null) TRAIN_ICON = Mat.New("icons/Train.mat");
			if (AIRCRAFT_ICON == null) AIRCRAFT_ICON = Mat.New("icons/plane.mat");
			if (SHIP_ICON == null) SHIP_ICON = Mat.New("icons/shipDestroyer.mat");
			if (CAR_ICON == null) CAR_ICON = Mat.New("icons/car.mat");
			if (ROCKET_ICON == null) ROCKET_ICON = Mat.New("icons/objV1.mat");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * When player changes army, current scout status might not be valid as array that holds
	 * zuti pad objects is not cleaned but just updated with new objects and updated by
	 * removing dead ones.
	 */
	public void updateScoutStatus()
	{
		isPlayerArmyScout = ZutiRadarObject.isPlayerArmyScout(actor, ZutiSupportMethods.getPlayerArmy());
	}
	
	/**
	 * Call this method with desired icon. If actor does not have icon assigned this method will
	 * assign it to it and you will see correct data/icon on your map screen. Available options:
	 * 
	 * 	Artillery	Mat.New(icons/fieldgun.mat),
		AAA			icons/antiaircraft.mat,
		Train	 	icons/Train.mat,
		Aircraft 	icons/plane.mat,
		Ship		icons/shipDestroyer.mat,
		Car			icons/car.mat,
		Rocket		icons/objV1.mat,
	 * @param iconString
	 */
	public void setIcon(Mat icon)
	{
		if( actor.icon == null )
		{
			actor.icon = icon;
		}
	}
	
	public void refreshPosition()
	{
		if( actor == null || livePos == null )
			return;
		
		oldPos = new Point3d(livePos.x, livePos.y, livePos.z);
		azimut = actor.pos.getAbsOrient().azimut();
	}
	
	public String getName()
	{
		return name;
	}
	
	/**
	 * If radar refresh interval is set to value greater than 0, return value is old position
	 * otherwise returned value represents live position. If owner is dead, position is set to 
	 * some distant coordinates.
	 * @return
	 */
	public Point3d getPosition()
	{
		if( !isAlive() )
			return deadPos;
		else if( refreshIntervalSet )
		{
			return oldPos;
		}
		else
		{
			return livePos;
		}
	}
	
	public float getAzimut()
	{
		return azimut;
	}
	public boolean isAlive()
	{
		if( actor == null )
			return false;
		
		if( actor instanceof RocketryRocket )
			return !((RocketryRocket)actor).isDamaged();
		
		return !actor.getDiedFlag();
	}
	public Actor getOwner()
	{
		return actor;
	}
	
	public boolean equals(Object o)
	{
		if( o instanceof ZutiPadObject )
		{	
			ZutiPadObject inKeyObject = (ZutiPadObject)o;
			if( this.actor.equals(inKeyObject.getOwner()) )
				return true;
		}
		
		return false;
	}
	
	public int hashCode()
	{
		return actor.hashCode();
	}
	
	/**
	 * Get pad object army.
	 * @return If -1 is returned, object owner is dead.
	 */
	public int getArmy()
	{
		if( isAlive() )
			return actor.getArmy();
		
		return -1;		
	}
	
	public boolean isGroundUnit()
	{
		if( actor == null )
			return false;
		
		return isGroundUnit(actor);
	}
	
	public Mat getIcon()
	{
		if (actor.icon != null)
			return actor.icon;
		
		java.lang.Class class1 = actor.getClass();
		//System.out.println("Icon - Class: " + class1.toString());
		com.maddox.il2.engine.Mat mat = (com.maddox.il2.engine.Mat) com.maddox.rts.Property.value(class1, "iconMat", null);
		//boolean flag = mat == null;
		if (mat == null)
		{
			String s = Property.stringValue(class1, "iconName", null);
			//System.out.println("Icon - Class value: " + s);
			if (s != null)
			{
				try{mat = Mat.New(s);}
				catch(Exception ex){ex.printStackTrace();}
			}
		}
		return mat;
	}
	//Called from: GUIPad
	public boolean isPlayerPlane()
	{
		return isPlayerPlane;
	}
	
	//Called from: HouseManager, GUIBriefing
	public static boolean isGroundUnit(Actor actor)
	{
		if( actor instanceof SndAircraft || actor instanceof RocketryRocket )
			return false;

		return true;
	}
	
	public boolean isVisibleForPlayerArmy()
	{
		if( isPlayerArmyScout && scoutsAsRadar )
			return true;
		else
			return visibleForPlayerArmy;
	}
	
	public void setVisibleForPlayerArmy(boolean value)
	{
		visibleForPlayerArmy = value;
	}
	
	public boolean isPlayerArmyScout()
	{
		return isPlayerArmyScout;
	}
}