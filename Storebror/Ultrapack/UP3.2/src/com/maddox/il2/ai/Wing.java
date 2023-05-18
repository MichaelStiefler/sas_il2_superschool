/*4.10.1 class*/
package com.maddox.il2.ai;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.MsgOwnerListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.SectFile;

public class Wing extends Chief implements MsgOwnerListener
{
    // TODO: +++ TD AI code backport from 4.13 +++
	public Aircraft[] airc;
	public Way way;
    // TODO: --- TD AI code backport from 4.13 ---
	private static Vector3d tmpSpeed = new Vector3d();
	private static Point3d tmpPoint0 = new Point3d();
	private static Point3d tmpPoint1 = new Point3d();
	private static Vector3d zeroSpeed = new Vector3d();
	private static Point3d pGround = new Point3d();
	private static Orient oGround = new Orient();
	private static Vector3d vGround = new Vector3d();
    // TODO: +++ TD AI code backport from 4.13 +++
    public boolean bOnlyAI;
    public boolean bCoopSpawnStationary;
	
    public Wing()
    {
        airc = new Aircraft[4];
        way = new Way();
        bOnlyAI = false;
        bCoopSpawnStationary = false;
    }
    // TODO: --- TD AI code backport from 4.13 ---
    
	public int aircReady()
	{
		int i = 0;
		for (int i_0_ = 0; i_0_ < airc.length; i_0_++)
		{
			if (Actor.isValid(airc[i_0_]))
				i++;
		}
		return i;
	}
	
	public int aircIndex(Aircraft aircraft)
	{
		String string = aircraft.name();
		char c = string.charAt(string.length() - 1);
		return c - '0';
	}
	
	public Regiment regiment()
	{
		return squadron().regiment();
	}
	
	public Squadron squadron()
	{
		return (Squadron)getOwner();
	}
	
	public int indexInSquadron()
	{
		char c = name().charAt(name().length() - 1);
		return c - '0';
	}
	
	public void msgOwnerDied(Actor actor)
	{
		if (!getDiedFlag())
		{
			for (int i = 0; i < airc.length; i++)
			{
				if (Actor.isValid(airc[i]) && airc[i].isAlive())
					return;
			}
			World.onActorDied(this, null);
		}
	}
	
	public void msgOwnerTaskComplete(Actor actor)
	{
		for (int i = 0; i < airc.length; i++)
		{
			if (Actor.isValid(airc[i]) && !airc[i].isTaskComplete())
				return;
		}
		World.onTaskComplete(this);
	}
	
	public void msgOwnerAttach(Actor actor)
	{
	}
	
	public void msgOwnerDetach(Actor actor)
	{
	}
	
	public void msgOwnerChange(Actor actor, Actor actor_1_)
	{
	}
	
	public void destroy()
	{
		for (int i = 0; i < airc.length; i++)
			airc[i] = null;
		super.destroy();
	}
	
	public Object getSwitchListener(Message message)
	{
		return this;
	}
	
	public void load(SectFile sectfile, String string, NetChannel netchannel) throws Exception
	{
		if (sectfile.sectionIndex(string) < 0)
		{
			destroy();
			throw new Exception("Mission: section '" + string + "' not found");
		}
		String string_2_ = sectfile.get(string, "Class", (String)null);
		if (string_2_ == null)
		{
			destroy();
			throw new Exception("Mission: in section '" + string + "' class aircraft not defined");
		}
		String string_3_ = string + "_Way";
		if (sectfile.sectionIndex(string_3_) < 0)
		{
			destroy();
			throw new Exception("Mission: section '" + string_3_ + "' not found");
		}
		if (Actor.getByName(string) != null)
		{
			destroy();
			throw new Exception("Mission: dublicate wing '" + string + "'");
		}
		try
		{
			setName(string);
			Squadron squadron = null;
			
			//TODO: Added by |ZUTI|: loading squadroon might fail because of a mess with custom country and regiments. If that happens, load NoNe as the default one!
			//-------------------------------------------------------------------------
			try{squadron = Squadron.New(string.substring(0, string.length() - 1));}
			catch(Exception ex)
			{
				System.out.println("Loading of squadron failed: " + ex.getMessage());
				System.out.println("Trying to load default one...");
				try
				{
				squadron = Squadron.New("NoNe");
				}
				catch(Exception ex1)
				{
					System.out.println("Failed to load default squadron: " + ex1.getMessage());
					System.out.println("Aborting to load current wing!");
					destroy();
					return;
				}
			}
			//-------------------------------------------------------------------------
			
			setOwner(squadron);
			squadron.wing[indexInSquadron()] = this;
			setArmy(squadron.getArmy());
			// TODO: +++ TD AI code backport from 4.13 +++
	        bOnlyAI = sectfile.get(string, "OnlyAI", 0, 0, 1) == 1;
            // TODO: --- TD AI code backport from 4.13 ---
			Aircraft.loadingCountry = squadron.regiment().country();
			int i = sectfile.get(string, "Planes", 1, 1, 4);
			
			//TODO: Added by |ZUTI|: check if wing is AI only or not
			//--------------------------------------------------------
			boolean isMulticrew = false;
			if( sectfile.get(string, "OnlyAI", 0, 0, 1) == 0 )
			{
				isMulticrew = true;
			}
			//--------------------------------------------------------
			
			way.load(sectfile, string_3_);
			for (int i_4_ = 0; i_4_ < i; i_4_++)
			{
				airc[i_4_] = Mission.cur().loadAir(sectfile, string_2_, string, string + i_4_, i_4_);
				airc[i_4_].setArmy(getArmy());
				airc[i_4_].checkTurretSkill();
				airc[i_4_].setOwner(this);
				if (airc[i_4_] == World.getPlayerAircraft())
					World.setPlayerRegiment();
				
				//TODO: Added by |ZUTI|
				//----------------------------------
				airc[i_4_].FM.AS.zutiSetMultiCrew(isMulticrew);
				//----------------------------------
				
				airc[i_4_].preparePaintScheme();
				airc[i_4_].prepareCamouflage();
				setPosAndSpeed(airc[i_4_], way);
			}
			setArmy(airc[0].getArmy());
			Formation.generate(airc);
			for (int i_5_ = 0; i_5_ < i; i_5_++)
				airc[i_5_].FM.AP.way = new Way(way);
			//TODO: Edited by |ZUTI|
			//if ((Mission.isSingle() || Mission.isCoop() && Mission.isServer()) && !NetMissionTrack.isPlaying())
			if ((Mission.isSingle() || (Mission.isDogfight() || Mission.isCoop() && Mission.isServer())) && !NetMissionTrack.isPlaying())
			{
				AirGroup airgroup = new AirGroup(airc[0].getSquadron(), way);
				for (int i_6_ = 0; i_6_ < i; i_6_++)
					airgroup.addAircraft(airc[i_6_]);
				
				AirGroupList.addAirGroup(War.Groups, airc[0].getArmy() - 1 & 0x1, airgroup);
			}
			Aircraft.loadingCountry = null;
		}
		catch (Exception exception)
		{
			Aircraft.loadingCountry = null;
			destroy();
			throw exception;
		}
		// TODO: +++ TD AI code backport from 4.13 +++
        if(way.first().waypointType == 4 || way.first().waypointType == 5)
            bCoopSpawnStationary = true;
        if(bOnlyAI)
            bCoopSpawnStationary = true;
		// TODO: --- TD AI code backport from 4.13 ---
	}
	
	private void setPosAndSpeed(Aircraft aircraft, Way way)
	{
		if (way.size() == 1)
		{
			WayPoint waypoint = way.get(0);
			waypoint.getP(tmpPoint0);
			aircraft.pos.setAbs(tmpPoint0);
			tmpSpeed.set((double)waypoint.Speed, 0.0, 0.0);
			aircraft.setSpeed(tmpSpeed);
		}
		else
		{
			WayPoint waypoint = way.get(0);
			WayPoint waypoint_7_ = way.get(1);
			waypoint.getP(tmpPoint0);
			waypoint_7_.getP(tmpPoint1);
			tmpSpeed.sub(tmpPoint1, tmpPoint0);
			tmpSpeed.normalize();
			Actor._tmpOrient.setAT0(tmpSpeed);
			tmpSpeed.scale((double)waypoint.Speed);
			aircraft.pos.setAbs(tmpPoint0, Actor._tmpOrient);
			aircraft.setSpeed(tmpSpeed);
		}
		aircraft.pos.reset();
	}
	
	public void setOnAirport()
	{
		WayPoint waypoint = way.get(0);
		waypoint.getP(tmpPoint0);
		if (waypoint.Action == 1)
		{
			boolean bool = false;
			if (waypoint.sTarget != null)
			{
				Actor actor = waypoint.getTarget();
				if (actor != null && actor instanceof BigshipGeneric)
				{
					AirportCarrier airportcarrier = ((BigshipGeneric)actor).getAirport();
					if (Actor.isValid(airportcarrier))
					{
						airportcarrier.setTakeoff(tmpPoint0, airc);
						bool = true;
					}
				}
			}
			if (!bool)
			{
				Airport airport = Airport.nearest(tmpPoint0, getArmy(), 3);
				if (airport != null)
				{
					double d = airport.pos.getAbsPoint().distance(tmpPoint0);
					if (d < 1250.0)
						airport.setTakeoff(tmpPoint0, airc);
					else
						setonground();
				}
				else
					setonground();
			}
		}
		else
		{
			for (int i = 0; i < 4; i++)
			{
				if (Actor.isValid(airc[i]))
				{
					if (airc[i] == World.getPlayerAircraft())
					{
						airc[i].FM.EI.setCurControlAll(true);
						airc[i].FM.EI.setEngineRunning();
						airc[i].FM.CT.setPowerControl(0.75F);
					}
					airc[i].FM.setStationedOnGround(false);
					airc[i].FM.setWasAirborne(true);
				}
			}
		}
	}
	
	private void setonground()
	{
		for (int i = 0; i < airc.length; i++)
		{
			if (airc[i] != null)
			{
				airc[i].pos.getAbs(pGround, oGround);
				pGround.z = (World.land().HQ(pGround.x, pGround.y) + (double)airc[i].FM.Gears.H);
				Engine.land().N(pGround.x, pGround.y, vGround);
				oGround.orient(vGround);
				oGround.increment(0.0F, airc[i].FM.Gears.Pitch, 0.0F);
				airc[i].setOnGround(pGround, oGround, zeroSpeed);
			}
		}
	}
}