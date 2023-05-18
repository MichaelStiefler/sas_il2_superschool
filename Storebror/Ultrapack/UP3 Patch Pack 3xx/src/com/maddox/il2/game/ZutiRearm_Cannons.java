//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRearm_Cannons
{
	public static int NUMBER_OF_GUNS = 0;
	private long startTime = 0;
	private float gunsRearmTime = 0;
	private Aircraft playerAC = null;
	private long bullets = 0;
	
	private BornPlace bornPlace = null;
	
	public ZutiRearm_Cannons(float rearmPenalty, long bullets)
	{
		this.bullets = bullets;
		this.playerAC = World.getPlayerAircraft();
		this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(playerAC.FM);
		
		if( playerAC.FM.CT.wingControl > 0 )
		{
			com.maddox.il2.game.HUD.log("mds.unfoldWings");
			return;
		}
	
		calculateRearmingTimeConsumption(rearmPenalty);
		
		HUD.log( "mds.rearmingMgsTime", new java.lang.Object[]{new Integer(Math.round(gunsRearmTime))} );
		
		gunsRearmTime *= 1000;
		
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
			if( gunsRearmTime > -1 && Time.current()-startTime > gunsRearmTime )
			{
				//If everything is set to 0, then don't show a message that separate weapon was reloaded...
				ZutiWeaponsManagement.rearmMGs_Cannons(playerAC, this.bullets);
				
				//Don't need to report bullets, they don't show :)
				//if( playerAC instanceof NetAircraft )
				//	ZutiSupportMethods_Air.sendNetAircraftRearmOrdinance(playerAC, 0, this.bullets, null);
				
				String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
				String userLocation = ZutiSupportMethods.getPlayerLocation();
				ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), userData + " rearmed guns at " + userLocation);
				
				gunsRearmTime = -1;
				
				//Collect earned points
				ZutiSupportMethods_AI.collectPoints();
				//Reset processing of cargo drops since player rearmed the plane and had to land first (and survive :D)
				ZutiWeaponsManagement.ZUTI_PROCESS_CARGO_DROPS = true;
				
				stopTimer();
				
				return -1;
			}
		}
		catch(Exception ex){ex.printStackTrace();}

		return 0;
	}
	
	private void calculateRearmingTimeConsumption(float rearmPenalty)
	{
		int numberOfGuns = 0;
		com.maddox.il2.ai.BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;
		
		for( int i=0; i<weapons.length; i++ )
		{
			try
			{
				for( int j=0; j<weapons[i].length; j++ )
				{
					if( weapons[i][j] instanceof GunGeneric )
					{
						numberOfGuns++;
					}
				}
			}
			catch(Exception ex){}
		}

		if( bornPlace == null)
			gunsRearmTime = Mission.MDS_VARIABLES().zutiReload_OneMgCannonRearmSecond * numberOfGuns;
		else
			gunsRearmTime = bornPlace.zutiOneMgCannonRearmSecond * numberOfGuns;
		
		if( this.bullets == 0 )
			gunsRearmTime = 0;
		
		//System.out.println("Received Bullets: " + bullets);
		System.out.println("  Number of guns: " + numberOfGuns);
		System.out.println(" Guns Rearm time: " + gunsRearmTime);
		System.out.println("Rearming Penalty: " + rearmPenalty);

		gunsRearmTime = gunsRearmTime * rearmPenalty;
		
		System.out.println("--------------------------------------");
		System.out.println("Calculated Rearm Time: " + gunsRearmTime);
	}
	
	private int countLoadedBullets()
	{
		int counter = 0;
		com.maddox.il2.ai.BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;
		
		for( int i=0; i<weapons.length; i++ )
		{
			try
			{
				for( int j=0; j<weapons[i].length; j++ )
				{
					//Covers bombs, fuel tanks, torpedoes
					if( weapons[i][j] instanceof GunGeneric )
					{
						counter += ((GunGeneric)weapons[i][j]).countBullets();
					}
				}
			}
			catch(Exception ex){}
		}
		
		return counter;
	}
	
	private void stopTimer()
	{
		int loadedBullets = countLoadedBullets();
		HUD.log("mds.rearmingMgsDone", new java.lang.Object[]{new Integer(loadedBullets)} );
		System.out.println("Rearming done! Loaded >" + loadedBullets +"< bullets.");
	}
	
	public void cancelTimer()
	{
		//System.out.println("Not used bullets: " + this.bullets);
		
		//Return unused fuel
		ZutiSupportMethods_NetSend.returnRRRResources_Bullets(this.bullets, playerAC.pos.getAbsPoint());
		
		com.maddox.il2.game.HUD.log("mds.rearmingMgsAborted");
		System.out.println("Rearming aborted!!!");
	}	
}