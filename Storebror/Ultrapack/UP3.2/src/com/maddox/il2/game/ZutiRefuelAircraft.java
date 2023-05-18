//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRefuelAircraft
{
	private long startTime = 0;
	private long secondInterval = 0;
	private float refuelTime = 0;
	private float calculatedRefuelRate = 0.0F;
	private Aircraft playerAC = null;
	private BornPlace bornPlace = null;
	
	private float fuelAtStart = 0.0F;
	private float fuelToFill = 0.0F;
	private float loadedFuel = 0.0F;

	public ZutiRefuelAircraft(float refuelPenalty, float fuelToFill)
	{
		this.fuelToFill = fuelToFill;
		this.playerAC = World.getPlayerAircraft();
		this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(playerAC.FM);
				
		calculateRearmingTimeConsumption(refuelPenalty);
		
		HUD.log( "mds.refuelingTime", new java.lang.Object[]{new Integer(Math.round(refuelTime))} );
		
		startTime = Time.current();
		secondInterval = startTime;
		refuelTime = refuelTime * 1000;
		
		loadedFuel = 0F;
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
			
			sendLogData();
			
			return -1;
		}
		
		try
		{
			//Refuel each second
			if( Time.current()-secondInterval > 1000  )
			{
				zutiRefuelAircraft(calculatedRefuelRate);
				secondInterval = Time.current();
				
				if( Time.current()-startTime > refuelTime )
				{
					ZutiSupportMethods_AI.collectPoints();
					stopTimer();
					
					sendLogData();
					
					return -1;
				}
			}
		}
		catch(Exception ex){ex.printStackTrace();}
		
		return 0;
	}
	
	private void calculateRearmingTimeConsumption(float refuelTimePenalty)
	{
		int refuelingRate = Mission.MDS_VARIABLES().zutiReload_GallonsLitersPerSecond;
		
		if( bornPlace != null )
		{
			refuelingRate = bornPlace.zutiGallonsLitersPerSecond;
			System.out.println("-== Refueling with HB specific RRR settings! ==-");
		}
		
		fuelAtStart = playerAC.FM.M.fuel;
		System.out.println(" Current Fuel: " + fuelAtStart);
		System.out.println("  Target Fuel: " + fuelToFill);
		System.out.println("         Rate: " + refuelingRate + " L/s");
		System.out.println("Penalty Multi: x" + refuelTimePenalty);
		
		float refuelTimeNoPenalty = fuelToFill / refuelingRate;
		refuelTime = refuelTimeNoPenalty * refuelTimePenalty;
		calculatedRefuelRate = refuelTimeNoPenalty * refuelingRate / refuelTime;
		if( calculatedRefuelRate < 0 )
			calculatedRefuelRate = 0;
		System.out.println("--------------------------------------");
		System.out.println("Calculated refuel rate: " + calculatedRefuelRate + " L/s");
		System.out.println("Calculated refuel time: " + Math.round(refuelTime) + "s.");
	}
	
	private void stopTimer()
	{
		HUD.log("mds.refuelingStatus", new java.lang.Object[]{new Integer(Math.round(loadedFuel)), new Integer(Math.round(playerAC.FM.M.fuel))} );
		System.out.println("Refueling Done. Loaded >" + loadedFuel +"kg< of new fuel. Total: " + playerAC.FM.M.fuel + "kg.");
	}
	
	public void cancelTimer()
	{
		float unusedFuel = (fuelToFill - (playerAC.FM.M.fuel - fuelAtStart));
		System.out.println("Not used fuel: " + unusedFuel + "kg");
		
		//Return unused fuel
		ZutiSupportMethods_NetSend.returnRRRResources_Fuel(unusedFuel, playerAC.pos.getAbsPoint());
		
		if( World.getPlayerAircraft() != null )
			HUD.log("mds.refuelingStatus", new java.lang.Object[]{new Integer(Math.round(loadedFuel)), new Integer(Math.round(playerAC.FM.M.fuel))} );
		else
			HUD.log("mds.refuelingAborted" );
			
		System.out.println("Refueling aborted. Loaded >" + loadedFuel +"kg< of new fuel. Total: " + playerAC.FM.M.fuel + "kg.");
	}
	
	private void zutiRefuelAircraft(float fuel)
	{
		if( playerAC.FM.M.fuel + fuel <= playerAC.FM.M.maxFuel )
		{
			playerAC.FM.M.fuel += fuel;
			
			loadedFuel += fuel;
		}
		
		//System.out.println("Fuel tanks status: " + playerAC.FM.M.fuel);
	}
	
	private void sendLogData()
	{
		String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
		String userLocation = ZutiSupportMethods.getPlayerLocation();
		ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), userData + " refueled aircraft to >" + playerAC.FM.M.fuel + "kg< at " + userLocation);
	}
}