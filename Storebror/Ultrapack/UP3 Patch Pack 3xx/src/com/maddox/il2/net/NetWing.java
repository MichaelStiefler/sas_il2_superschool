/*4.10.1 class*/
package com.maddox.il2.net;

import com.maddox.il2.ai.Squadron;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;

public class NetWing extends Wing
{
	private int indexInSquadron;

	public int aircReady()
	{
		if (Actor.isValid(airc[0]))
			return 1;
		return 0;
	}

	public int aircIndex(Aircraft aircraft)
	{
		if (airc[0] == aircraft)
			return 0;
		return -1;
	}

	public int indexInSquadron()
	{
		return indexInSquadron;
	}

	public void setPlane(NetAircraft netaircraft, int i)
	{
		int army = netaircraft.getArmy();
		Aircraft aircraft = (Aircraft) netaircraft;
		airc[0] = aircraft;
		aircraft.setArmy(army);
		aircraft.setOwner(this);
		if (aircraft == World.getPlayerAircraft())
			World.setPlayerRegiment();
		aircraft.preparePaintScheme(i);
		aircraft.prepareCamouflage();
		
		//TODO: Added by |ZUTI|: manage dogfight aircraft
		//----------------------------------------------
		ZutiSupportMethods_Net.manageDogfightGroups(aircraft);
		//----------------------------------------------
	}

	public void destroy()
	{
		destroy(squadron());
		super.destroy();
	}

	public NetWing(Squadron squadron, int i)
	{
		indexInSquadron = i;
		setOwner(squadron);
		squadron.wing[i] = this;
		setArmy(squadron.getArmy());
	}
}