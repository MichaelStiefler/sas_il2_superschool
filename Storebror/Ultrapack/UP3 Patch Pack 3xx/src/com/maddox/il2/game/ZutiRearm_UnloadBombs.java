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

public class ZutiRearm_UnloadBombs
{
	private long startTime = 0;
	private float bombsUnloadingTime = 0;
	private Aircraft playerAC = null;
	
	private BornPlace bornPlace = null;
	
	public ZutiRearm_UnloadBombs()
	{
		this.playerAC = World.getPlayerAircraft();
		this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(playerAC.FM);
		
		calculateRearmingTimeConsumption();
		
		HUD.log( "mds.unloadingBombsTime", new java.lang.Object[]{new Integer(Math.round(bombsUnloadingTime))} );
		
		bombsUnloadingTime *= 1000;
		
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
			if( bombsUnloadingTime > -1 && Time.current()-startTime > bombsUnloadingTime )
			{
				ZutiWeaponsManagement.returnRemainingAircraftBombsAndCargoCrates(playerAC, true);
				
				if( playerAC instanceof NetAircraft )
					ZutiSupportMethods_Air.sendNetAircraftRearmOrdinance(playerAC, 2, -1, new int[]{0,0,0,0,0,0});
				
				String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
				String userLocation = ZutiSupportMethods.getPlayerLocation();
				ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), userData + " unloaded bombs at " + userLocation);
				
				bombsUnloadingTime = -1;
								
				stopTimer();
				
				return -1;
			}
		}
		catch(Exception ex){ex.printStackTrace();}

		return 0;
	}
	
	private void calculateRearmingTimeConsumption()
	{
		int[] bombs = ZutiWeaponsManagement.returnRemainingAircraftBombsAndCargoCrates(playerAC, false);
		for( int i=0; i<bombs.length; i++ )
		{
			bombsUnloadingTime += bombs[i];
		}		
		
		if( bornPlace != null && bornPlace.zutiEnableResourcesManagement )
			bombsUnloadingTime *= bornPlace.zutiOneRocketRearmSeconds;
		else
			bombsUnloadingTime *= Mission.MDS_VARIABLES().zutiReload_OneRocketRearmSeconds;
		
		System.out.println("Calculated Bombs Unloading Time: " + bombsUnloadingTime);
	}
	
	private void stopTimer()
	{
		com.maddox.il2.game.HUD.log("mds.unloadingBombsDone");		
		System.out.println("Unloading o bombs done!");
	}
	
	public void cancelTimer()
	{
		com.maddox.il2.game.HUD.log("mds.unloadingBombsAborted");
		System.out.println("Unloading of bombs aborted!!!");
	}	
}