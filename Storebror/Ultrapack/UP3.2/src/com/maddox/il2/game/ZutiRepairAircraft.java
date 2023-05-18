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
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.fm.FlightModel;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRepairAircraft
{
	private long startTime = 1;
	private float repairTime = 0;
	private float flapsRepairTime = 0;
	private float weaponsRepairTime = 0;
	private float fuelOilTanksRepairTime = 0;
	private float cockpitRepairTime = 0;
	private float controlCablesRepairTime = 0;
	private Aircraft playerAC = null;
	private BornPlace bornPlace = null;

	public ZutiRepairAircraft(float repairPenalty, boolean rearmingInProgress)
	{
		this.playerAC = World.getPlayerAircraft();
		this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(playerAC.FM);
		
		calculateRepairingTimeConsumption(repairPenalty, rearmingInProgress);
		
		HUD.log( "mds.repairingTime", new java.lang.Object[]{new Integer(Math.round(repairTime))} );
		
		flapsRepairTime 		*= 1000;
		weaponsRepairTime 		*= 1000;
		fuelOilTanksRepairTime 	*= 1000;
		controlCablesRepairTime *= 1000;
		cockpitRepairTime 		*= 1000;
		repairTime				*= 1000;
		
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
			if( flapsRepairTime > -1 && Time.current()-startTime > flapsRepairTime )
			{
				playerAC.FM.CT.bHasFlapsControl = true;
				flapsRepairTime = -1;
				com.maddox.il2.game.HUD.log("mds.repairedFlaps");
			}
			if( weaponsRepairTime > -1 && Time.current()-startTime > weaponsRepairTime )
			{
				fixJammedMgsCannons();
				weaponsRepairTime = -1;
				com.maddox.il2.game.HUD.log("mds.repairedWeapons");
			}
			if( fuelOilTanksRepairTime > -1 && Time.current()-startTime > fuelOilTanksRepairTime )
			{
				repairFuelOilTanks();
				fuelOilTanksRepairTime = -1;
				com.maddox.il2.game.HUD.log("mds.repairedTanks");
			}
			if( controlCablesRepairTime > -1 && Time.current()-startTime > controlCablesRepairTime )
			{
				repairControlCables();
				controlCablesRepairTime = -1;
				com.maddox.il2.game.HUD.log("mds.repairedCables");
			}
			if( cockpitRepairTime > -1 && Time.current()-startTime > cockpitRepairTime )
			{
				repairCockpit();
				if( cockpitRepairTime > 0 )
					com.maddox.il2.game.HUD.log("mds.repairedCockpit");
				System.out.println("Cockpit(s) repaired!");
				cockpitRepairTime = -1;
			}
			if( Time.current()-startTime > repairTime )
			{
				playerAC.FM.AS.setCockpitState((com.maddox.il2.engine.Actor)playerAC, 0);
				
				//Collect earned points
				ZutiSupportMethods_AI.collectPoints();
				stopTimer();
				
				String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
				String userLocation = ZutiSupportMethods.getPlayerLocation();
				ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), userData + " repaired aircraft at " + userLocation);
								
				return -1;
			}
		}
		catch(Exception ex){ex.printStackTrace();}
		
		return 0;
	}
	
	private void calculateRepairingTimeConsumption(float repairTimePenalty, boolean rearmingInProgress)
	{
		calculateFlapsRepairTime();
		calculateWeaponsRepairTime(rearmingInProgress);
		calculateFuelOilTanksRepairTime();		
		calculateControlCablesRepairTime();
		calculateCockpitRepairTime();
		
		System.out.println("    Flaps Repair Time: " + flapsRepairTime);
		System.out.println("     Guns Repair Time: " + weaponsRepairTime);
		System.out.println("F/O Tanks Repair Time: " + fuelOilTanksRepairTime);
		System.out.println("  CCables Repair Time: " + controlCablesRepairTime);
		System.out.println("  Cockpit Repair Time: " + cockpitRepairTime);
		System.out.println("    Repairing Penalty: " + repairTimePenalty);
		
		flapsRepairTime 		= flapsRepairTime * repairTimePenalty;
		weaponsRepairTime 		= weaponsRepairTime * repairTimePenalty;
		fuelOilTanksRepairTime 	= fuelOilTanksRepairTime * repairTimePenalty;
		controlCablesRepairTime = controlCablesRepairTime * repairTimePenalty;
		cockpitRepairTime 		= cockpitRepairTime * repairTimePenalty;

		setTotalRepairTime();
		
		System.out.println("--------------------------------------");
		System.out.println("Calculated Repair Time: " + repairTime);
	}
	
	private void setTotalRepairTime()
	{
		repairTime = flapsRepairTime;
		if( weaponsRepairTime > repairTime )
			repairTime = weaponsRepairTime;
		if(fuelOilTanksRepairTime > repairTime)
			repairTime = fuelOilTanksRepairTime;
		if(controlCablesRepairTime > repairTime)
			repairTime = controlCablesRepairTime;
		if(cockpitRepairTime > repairTime)
			repairTime = cockpitRepairTime;
	}
	
	private void calculateFlapsRepairTime()
	{
		flapsRepairTime = 0;
		
		if( !playerAC.FM.CT.bHasFlapsControl )
		{
			flapsRepairTime = Mission.MDS_VARIABLES().zutiReload_FlapsRepairSeconds;
			
			if( bornPlace != null )
				flapsRepairTime = bornPlace.zutiFlapsRepairSeconds;
		}
	}
	
	private void calculateWeaponsRepairTime(boolean rearmingInProgress)
	{
		weaponsRepairTime = 0;
		
		//If rearming is in process, rearming process already unjamed the guns
		if( rearmingInProgress )
			return;
		
		com.maddox.il2.ai.BulletEmitter[][] weapons = playerAC.FM.CT.Weapons;
				
		for( int i=0; i<weapons.length; i++ )
		{
			try
			{
				for( int j=0; j<weapons[i].length; j++ )
				{
					if( weapons[i][j] instanceof GunGeneric )
					{
						if ( !((GunGeneric)weapons[i][j]).haveBullets() )
							weaponsRepairTime++;
					}
				}
			}
			catch(Exception ex){}
		}
		
		if( bornPlace == null )
			weaponsRepairTime = weaponsRepairTime * Mission.MDS_VARIABLES().zutiReload_OneWeaponRepairSeconds;
		else
			weaponsRepairTime = weaponsRepairTime * bornPlace.zutiOneWeaponRepairSeconds;
	}
	
	private void fixJammedMgsCannons()
	{
		com.maddox.il2.ai.BulletEmitter[][] weapons = playerAC.FM.CT.Weapons;
		Aircraft._WeaponSlot[] weaponSlots = Aircraft.getWeaponSlotsRegistered(playerAC.getClass(), ZutiSupportMethods_Air.getCurrentAircraftLoadoutName(playerAC));
		
		for( int i=0; i<weapons.length; i++ )
		{
			try
			{
				for( int j=0; j<weapons[i].length; j++ )
				{
					if( weapons[i][j] instanceof GunGeneric )
					{
						//fixing is not rearming - just load 5% of max allowed bullets per gun :D
						if ( !((GunGeneric)weapons[i][j]).haveBullets() )
						{
							GunGeneric gun = (GunGeneric)weapons[i][j];
							gun.loadBullets(0);
							gun.loadBullets( ((int)(0.05 * ZutiWeaponsManagement.getBulletsForWeapon(weapons[i][j].getClass(), weaponSlots))) );
						}
					}
				}
			}
			catch(Exception ex){}
		}
	}
		
	private void calculateFuelOilTanksRepairTime()
	{
		FlightModel fm = playerAC.FM;
		fuelOilTanksRepairTime = 0;
		
		for( int i=0; i<fm.AS.astateTankStates.length; i++ )
		{
			if(fm.AS.astateTankStates[i] > 0)
			{
				if( bornPlace == null )
					fuelOilTanksRepairTime += Mission.MDS_VARIABLES().zutiReload_OneFuelOilTankRepairSeconds;
				else
					fuelOilTanksRepairTime += bornPlace.zutiOneFuelOilTankRepairSeconds;
			}
		}
		
		for( int i=0; i<fm.AS.astateOilStates.length; i++ )
		{
			if(fm.AS.astateOilStates[i] > 0)
			{
				if( bornPlace == null )
					fuelOilTanksRepairTime += Mission.MDS_VARIABLES().zutiReload_OneFuelOilTankRepairSeconds;
				else
					fuelOilTanksRepairTime += bornPlace.zutiOneFuelOilTankRepairSeconds;
			}
		}
	}
	
	private void calculateControlCablesRepairTime()
	{
		FlightModel fm = playerAC.FM;
		controlCablesRepairTime = 0;
		
		if(!fm.CT.bHasAileronControl)
		{
			if( bornPlace == null )
				controlCablesRepairTime += Mission.MDS_VARIABLES().zutiReload_OneControlCableRepairSeconds;
			else
				controlCablesRepairTime += bornPlace.zutiOneControlCableRepairSeconds;
		}
		if(!fm.CT.bHasElevatorControl)
		{
			if( bornPlace == null )
				controlCablesRepairTime += Mission.MDS_VARIABLES().zutiReload_OneControlCableRepairSeconds;
			else
				controlCablesRepairTime += bornPlace.zutiOneControlCableRepairSeconds;
		}
		if(!fm.CT.bHasRudderControl)
		{
			if( bornPlace == null )
				controlCablesRepairTime += Mission.MDS_VARIABLES().zutiReload_OneControlCableRepairSeconds;
			else
				controlCablesRepairTime += bornPlace.zutiOneControlCableRepairSeconds;
		}
	}
	
	private void calculateCockpitRepairTime()
	{
		FlightModel fm = playerAC.FM;
		cockpitRepairTime = 0;
		
		if( fm.AS.astateCockpitState != 0 )
		{
			if( bornPlace == null )
				cockpitRepairTime = Mission.MDS_VARIABLES().zutiReload_CockpitRepairSeconds;
			else
				cockpitRepairTime = bornPlace.zutiCockpitRepairSeconds;
		}
	}
	
	private void repairFuelOilTanks()
	{
		FlightModel fm = playerAC.FM;

		for( int i=0; i<fm.AS.astateTankStates.length; i++ )
			fm.AS.repairTank(i);
		
		for( int i=0; i<fm.AS.astateOilStates.length; i++ )
			fm.AS.repairOil(i);
	}
	
	private void repairControlCables()
	{
		FlightModel fm = playerAC.FM;
		
		fm.CT.bHasAileronControl = true;
		fm.CT.bHasElevatorControl = true;
		fm.CT.bHasRudderControl = true;
	}
	
	private void repairCockpit()
	{
		for( int i=0; i<com.maddox.il2.game.Main3D.cur3D().cockpits.length; i++ )
		{
			ZutiSupportMethods_Air.restoreCockpit( Main3D.cur3D().cockpits[i] );
		}
	}
	
	private void stopTimer()
	{
		com.maddox.il2.game.HUD.log("mds.repairingDone");	
		System.out.println("AC repairs done!");
	}
	
	public void cancelTimer()
	{
		//Return repair kit
		ZutiSupportMethods_NetSend.returnRRRResources_RepairKit(1, playerAC.pos.getAbsPoint());
		
		com.maddox.il2.game.HUD.log("mds.repairingAborted");
		System.out.println("AC repairs aborted!!!");
	}
}