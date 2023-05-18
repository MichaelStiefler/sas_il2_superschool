//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.game.order.ZutiSupportMethods_GameOrder;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.rts.Time;

public class ZutiChangeLoadout
{
	private long startTime = 0;
	private float maxTime = 0;
	private int loadoutId = 0;
	private float originalPenalty = 0;
	private Aircraft playerAC = null;
	private BornPlace bornPlace = null;
	private String selectedLoadout = "";

	public ZutiChangeLoadout(float penalty, int inLoadoutId, BornPlace bp)
	{
		bornPlace = bp;
		loadoutId = inLoadoutId;
		originalPenalty = penalty;
		playerAC = World.getPlayerAircraft();
		
		try
		{
			if( playerAC.FM.CT.wingControl > 0 )
			{
				com.maddox.il2.game.HUD.log("mds.unfoldWings");
				return;
			}
			
			if( bornPlace != null )
			{
				maxTime = bornPlace.zutiLoadoutChangePenaltySeconds * originalPenalty;
				System.out.println("-== Loadout changes with HB specific RRR settings! ==-");
			}
			else
				maxTime = Mission.MDS_VARIABLES().zutiReload_LoadoutChangePenaltySeconds * originalPenalty;
			
			String currentAcName = com.maddox.rts.Property.stringValue(((Aircraft)playerAC).getClass(), "keyName");		
			
			selectedLoadout = ZutiSupportMethods.getSelectedLoadoutForAircraft(bornPlace, currentAcName, playerAC.FM.Loc.x, playerAC.FM.Loc.y, loadoutId, World.getPlayerArmy(), false);
			if( selectedLoadout.trim().length() < 1 )
			{
				com.maddox.il2.game.HUD.log("mds.unknownLoadout");		
				return;
			}

			//First, unload all ammo and return it to resources pool
			ZutiWeaponsManagement.returnRemainingAircraftResources(playerAC, null);
						
			HUD.log( "mds.loadoutChanging", new java.lang.Object[]{new Integer(Math.round(maxTime))} );		
			//Clear all ammo
			ZutiWeaponsManagement.unloadLoadedWeapons(playerAC);
			//Clear old pylons
			ZutiWeaponsManagement.removePylons(playerAC);
			//System.out.println("ZutiTimer_ChangeLoadout preparations complete!");
			
			ZutiWeaponsManagement.preloadLoadOptions( playerAC, loadoutId, selectedLoadout );
		
			if( playerAC instanceof NetAircraft )
				ZutiSupportMethods_Air.sendNetAircraftLoadoutChange(playerAC, loadoutId, selectedLoadout);
			
			maxTime *= 1000;
			
			startTime = Time.current();
		}
		catch(Exception ex){}
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
			if( Time.current()-startTime > maxTime )
			{
				ZutiWeaponsManagement.changeLoadout(playerAC, selectedLoadout, true);
			
				//Start rearming this new laodout setup
				long bullets = ZutiSupportMethods_GameOrder.calculateBulletsToReload(playerAC);
				ZutiSupportMethods_NetSend.requestBullets(bullets);
				
				long rockets = ZutiSupportMethods_GameOrder.calculateRocketsToReload(playerAC);
				ZutiSupportMethods_NetSend.requestRockets(rockets);
				
				int[] bombs = ZutiSupportMethods_GameOrder.getBombsCount(playerAC);
				ZutiSupportMethods_NetSend.requestBombs(bombs);
				
				//Collect earned points
				ZutiSupportMethods_AI.collectPoints();
				this.stopTimer();
				
				return -1;
			}
		}
		catch(Exception ex){ex.printStackTrace();}
	
		return 0;
	}
	
	private void stopTimer()
	{
		com.maddox.il2.game.HUD.log("mds.loadoutChanged");
		System.out.println("Loadout changed!");
	}
	
	public void cancelTimer()
	{
		com.maddox.il2.game.HUD.log("mds.loadoutAborted");	
		System.out.println("Loadout changing aborted!!!");
	}
}