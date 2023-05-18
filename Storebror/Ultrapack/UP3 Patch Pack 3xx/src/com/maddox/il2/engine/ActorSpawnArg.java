/*4.10.1 class*/
package com.maddox.il2.engine;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Cmd;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class ActorSpawnArg
{
	public String name;
	public boolean armyExist;
	public int army;
	public Point3d point;
	public Orient orient;
	public Actor baseActor;
	public String hookName;
	public Actor ownerActor;
	public String iconName;
	public String meshName;
	public String matName;
	public String paramFileName;
	public boolean sizeExist;
	public float size;
	public boolean timeLenExist;
	public float timeLen;
	public boolean timeNativeExist;
	public boolean timeNative;
	public boolean typeExist;
	public int type;
	public String path;
	public String target;
	public String acoustic;
	public String sound;
	public String preload;
	public Color3f color3f;
	public float[] light;
	public boolean Z0Exist;
	public float Z0;
	public String FM;
	public int FM_Type;
	public NetChannel netChannel;
	public int netIdRemote;
	public String weapons;
	public float fuel;
	public Vector3d speed;
	public int skill;
	public boolean bPlayer;
	public boolean bornPlaceExist;
	public int bornPlace;
	public boolean stayPlaceExist;
	public int stayPlace;
	public boolean bNumberOn = true;
	public String rawData;
	public String country;
	private static Loc lempty = new Loc();
	private static ActorPosMoveInit apos = new ActorPosMoveInit();
	
	//TODO: Added by |ZUTI|
	//-------------------------------------------
	public boolean bZutiMultiCrew = false;
	public boolean bZutiMultiCrewAnytime = false;
	//-------------------------------------------
	
	public ActorSpawnArg()
	{
	}
	
	public ActorSpawnArg(ActorSpawnArg actorspawnarg_0_)
	{
		name = actorspawnarg_0_.name;
		armyExist = actorspawnarg_0_.armyExist;
		army = actorspawnarg_0_.army;
		point = actorspawnarg_0_.point;
		orient = actorspawnarg_0_.orient;
		baseActor = actorspawnarg_0_.baseActor;
		hookName = actorspawnarg_0_.hookName;
		ownerActor = actorspawnarg_0_.ownerActor;
		iconName = actorspawnarg_0_.iconName;
		meshName = actorspawnarg_0_.meshName;
		matName = actorspawnarg_0_.matName;
		paramFileName = actorspawnarg_0_.paramFileName;
		sizeExist = actorspawnarg_0_.sizeExist;
		size = actorspawnarg_0_.size;
		timeLenExist = actorspawnarg_0_.timeLenExist;
		timeLen = actorspawnarg_0_.timeLen;
		timeNativeExist = actorspawnarg_0_.timeNativeExist;
		timeNative = actorspawnarg_0_.timeNative;
		typeExist = actorspawnarg_0_.typeExist;
		type = actorspawnarg_0_.type;
		path = actorspawnarg_0_.path;
		target = actorspawnarg_0_.target;
		acoustic = actorspawnarg_0_.acoustic;
		sound = actorspawnarg_0_.sound;
		preload = actorspawnarg_0_.preload;
		color3f = actorspawnarg_0_.color3f;
		light = actorspawnarg_0_.light;
		Z0 = actorspawnarg_0_.Z0;
		Z0Exist = actorspawnarg_0_.Z0Exist;
		FM = actorspawnarg_0_.FM;
		FM_Type = actorspawnarg_0_.FM_Type;
		netChannel = actorspawnarg_0_.netChannel;
		netIdRemote = actorspawnarg_0_.netIdRemote;
		weapons = actorspawnarg_0_.weapons;
		fuel = actorspawnarg_0_.fuel;
		speed = actorspawnarg_0_.speed;
		skill = actorspawnarg_0_.skill;
		bPlayer = actorspawnarg_0_.bPlayer;
		bornPlaceExist = actorspawnarg_0_.bornPlaceExist;
		bornPlace = actorspawnarg_0_.bornPlace;
		stayPlaceExist = actorspawnarg_0_.stayPlaceExist;
		stayPlace = actorspawnarg_0_.stayPlace;
		bNumberOn = actorspawnarg_0_.bNumberOn;
		rawData = actorspawnarg_0_.rawData;
		country = actorspawnarg_0_.country;
		
		//TODO: Added by |ZUTI|
		//------------------------------------------------------------
		bZutiMultiCrew = actorspawnarg_0_.bZutiMultiCrew;
		bZutiMultiCrewAnytime = actorspawnarg_0_.bZutiMultiCrewAnytime;
		//------------------------------------------------------------
	}
	
	public void clear()
	{
		name = null;
		armyExist = false;
		army = 0;
		point = null;
		orient = null;
		baseActor = null;
		hookName = null;
		ownerActor = null;
		iconName = null;
		meshName = null;
		matName = null;
		paramFileName = null;
		sizeExist = false;
		timeLenExist = false;
		timeNativeExist = false;
		typeExist = false;
		path = null;
		target = null;
		acoustic = null;
		sound = null;
		preload = null;
		color3f = null;
		light = null;
		Z0 = 0.0F;
		Z0Exist = false;
		FM = null;
		FM_Type = 0;
		netChannel = null;
		netIdRemote = 0;
		weapons = null;
		fuel = 100.0F;
		speed = null;
		skill = 1;
		bPlayer = false;
		bornPlaceExist = false;
		bornPlace = -1;
		stayPlaceExist = false;
		stayPlace = -1;
		bNumberOn = true;
		rawData = null;
		country = null;
		
		//TODO: Added by |ZUTI|
		//------------------------------------------------------------
		bZutiMultiCrew = false;
		bZutiMultiCrewAnytime = false;
		//------------------------------------------------------------
	}
	
	public Actor set(Actor actor)
	{
		if (name != null)
			actor.setName(name);
		if (armyExist)
			actor.setArmy(army);
		if (baseActor != null)
		{
			Hook hook = null;
			if (hookName != null)
				hook = baseActor.findHook(hookName);
			actor.pos.setBase(baseActor, hook, false);
			if (point != null && orient != null)
				actor.pos.setRel(point, orient);
			else if (point != null)
				actor.pos.setRel(point);
			else if (orient != null)
				actor.pos.setRel(orient);
			else
				actor.pos.setRel(lempty);
			actor.pos.reset();
		}
		else
		{
			if (point != null && orient != null)
				actor.pos.setAbs(point, orient);
			else if (point != null)
				actor.pos.setAbs(point);
			else if (orient != null)
				actor.pos.setAbs(orient);
			else
				actor.pos.setAbs(lempty);
			actor.pos.reset();
		}
		if (ownerActor != null)
			actor.setOwner(ownerActor);
		if (speed != null)
		{
			actor.setSpeed(speed);
			if (actor.pos != null)
				actor.pos.reset();
		}
		if (iconName != null)
		{
			try
			{
				actor.icon = Mat.New(iconName);
			}
			catch (Exception exception)
			{
				/* empty */
			}
			if (actor.icon == null)
				ERR_SOFT("Icon : " + iconName + " not loaded");
		}
		else
			IconDraw.create(actor);
		if (bPlayer && actor instanceof Aircraft)
			World.setPlayerAircraft((Aircraft)actor);
		return actor;
	}
	
	public Actor setStationary(Actor actor)
	{
		if (name != null)
			actor.setName(name);
		if (!armyExist)
			throw new ActorException(actor.getClass().getName() + ": missing army");
		actor.setArmy(army);
		if (point == null || orient == null)
			throw new ActorException(actor.getClass().getName() + ": missing pos or orient");
		actor.pos.setAbs(point, orient);
		actor.pos.reset();
		if (iconName != null)
		{
			try
			{
				actor.icon = Mat.New(iconName);
			}
			catch (Exception exception)
			{
				/* empty */
			}
			if (actor.icon == null)
				ERR_SOFT(actor.getClass().getName() + ": icon '" + iconName + "' not loaded");
		}
		else
			IconDraw.create(actor);
		return actor;
	}
	
	public Actor setStationaryNoIcon(Actor actor)
	{
		if (name != null)
			actor.setName(name);
		if (!armyExist)
			throw new ActorException(actor.getClass().getName() + ": missing army");
		actor.setArmy(army);
		if (point == null || orient == null)
			throw new ActorException(actor.getClass().getName() + ": missing pos or orient");
		actor.pos.setAbs(point, orient);
		actor.pos.reset();
		return actor;
	}
	
	public Actor setNameOwnerIcon(Actor actor)
	{
		if (name != null)
			actor.setName(name);
		if (armyExist)
			actor.setArmy(army);
		if (ownerActor != null)
			actor.setOwner(ownerActor);
		if (iconName != null)
		{
			try
			{
				actor.icon = Mat.New(iconName);
			}
			catch (Exception exception)
			{
				/* empty */
			}
			if (actor.icon == null)
				ERR_SOFT("Icon : " + iconName + " not loaded");
		}
		else
			IconDraw.create(actor);
		return actor;
	}
	
	public Loc getAbsLoc()
	{
		if (baseActor != null)
		{
			Hook hook = null;
			if (hookName != null)
				hook = baseActor.findHook(hookName);
			apos.setBase(baseActor, hook, false);
			if (point != null && orient != null)
				apos.setRel(point, orient);
			else if (point != null)
				apos.setRel(point);
			else if (orient != null)
				apos.setRel(orient);
			else
				apos.setRel(lempty);
		}
		else if (point != null && orient != null)
			apos.setAbs(point, orient);
		else if (point != null)
			apos.setAbs(point);
		else if (orient != null)
			apos.setAbs(orient);
		else
			apos.setAbs(lempty);
		Loc loc = new Loc();
		apos.getAbs(loc);
		apos.setBase(null, null, false);
		return loc;
	}
	
	public void ERR_HARD(String string)
	{
		if (Cmd.ERR_HARD)
			System.err.println("spawn " + Spawn.getLastClassName() + ": " + string);
	}
	
	public void ERR_SOFT(String string)
	{
		if (Cmd.ERR_SOFT)
			System.err.println("spawn " + Spawn.getLastClassName() + ": " + string);
	}
	
	public boolean isNoExistHARD(Object object, String string)
	{
		if (object == null)
		{
			ERR_HARD(string);
			return true;
		}
		return false;
	}
	
	public void INFO_HARD(String string)
	{
		if (Cmd.INFO_HARD)
			System.out.println(string);
	}
	
	public void INFO_SOFT(String string)
	{
		if (Cmd.INFO_SOFT)
			System.out.println(string);
	}
}