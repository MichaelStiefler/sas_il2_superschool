//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRearm_UnloadRockets
{
	private long startTime = 0;
	private float rocketsUnloadingTime = 0;
	private Aircraft playerAC = null;
	
	private BornPlace bornPlace = null;
	
	public ZutiRearm_UnloadRockets()
	{
		this.playerAC = World.getPlayerAircraft();
		this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(playerAC.FM);
		
		calculateRearmingTimeConsumption();
		
		HUD.log( "mds.unloadingRocketsTime", new java.lang.Object[]{new Integer(Math.round(rocketsUnloadingTime))} );
		
		rocketsUnloadingTime *= 1000;
		
		startTime = Time.current();
	}

	/**
	 * if -1 is returned abort execution
	 * @return
	 */
	public int updateTimer() 
	{
		if( !ZutiSupportMethods.allowRRR(playerAC) )
		{
			this.cancelTimer();
			return -1;
		}
		
		try
		{		
			if( rocketsUnloadingTime > -1 && Time.current()-startTime > rocketsUnloadingTime )
			{
				ZutiWeaponsManagement.returnRemainingAircraftRockets(playerAC, true);
				
				if( playerAC instanceof NetAircraft )
					ZutiSupportMethods_Air.sendNetAircraftRearmOrdinance(playerAC, 1, 0, null);
				
				String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
				String userLocation = ZutiSupportMethods.getPlayerLocation();
				ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), userData + " unloaded rockets at " + userLocation);
				
				rocketsUnloadingTime = -1;
								
				stopTimer();
				
				return -1;
			}
		}
		catch(Exception ex){ex.printStackTrace();}

		return 0;
	}
	
	private void calculateRearmingTimeConsumption()
	{
		rocketsUnloadingTime = ZutiWeaponsManagement.returnRemainingAircraftRockets(playerAC, false);
		
		if( bornPlace != null && bornPlace.zutiEnableResourcesManagement )
			rocketsUnloadingTime *= bornPlace.zutiOneRocketRearmSeconds;
		else
			rocketsUnloadingTime *= Mission.MDS_VARIABLES().zutiReload_OneRocketRearmSeconds;
		
		System.out.println("Calculated Rockets Unloading Time: " + rocketsUnloadingTime);
	}
	
	private void stopTimer()
	{
		com.maddox.il2.game.HUD.log("mds.unloadingRocketsDone");		
		System.out.println("Unloading o rockets done!");
	}
	
	public void cancelTimer()
	{
		com.maddox.il2.game.HUD.log("mds.unloadingRocketsAborted");
		System.out.println("Unloading of rockets aborted!!!");
	}	
}