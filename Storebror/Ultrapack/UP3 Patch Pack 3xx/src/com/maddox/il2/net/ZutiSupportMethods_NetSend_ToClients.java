package com.maddox.il2.net;

import java.io.IOException;
import java.util.List;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.ZutiSupportMethods_FM;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiAircraftCrew;
import com.maddox.il2.game.ZutiAircraftCrewManagement;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.game.ZutiSupportMethods_ResourcesManagement;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;

public class ZutiSupportMethods_NetSend_ToClients
{
	/**
	 * AC released it's bomb load. This method is only executed BY THE SERVER!
	 * @param acName
	 * @param bool
	 * @param hasBombBayDoors
	 */
	public static void bombardierReleasedOrdinance_ToClient(String acName, boolean bool, boolean hasBombBayDoors)
	{
		try
		{
			if( acName == null )
				return;
			
			Aircraft ac = (Aircraft)Actor.getByName(acName);
			if( ac == null )
				return;
			
			if( acName.endsWith("_0") )
			{
				//Aircraft is controlled by user. Must forward bomb release data to aircraft owner.
				if( ac.FM instanceof RealFlightModel )
				{
					//Server is live plane owner and can release bombs!
					ac.FM.CT.bHasBayDoors = hasBombBayDoors;
					ac.FM.CT.WeaponControl[3] = bool;
					//System.out.println("ZutiNetSendMethods - bombardierReleasedOrdinance_ToClient executed for AC >" + acName + "<.");
				}
				else
				{
					//Forward data to rightful owner
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.BOMBARDIER_RELEASED_ORDINANCE);
					netmsgguaranted.write255(acName);
					netmsgguaranted.writeBoolean(bool);
					netmsgguaranted.writeBoolean(hasBombBayDoors);
					NetUser netuser = ZutiSupportMethods.getNetUser( acName.substring(0, acName.lastIndexOf("_0")) );
					if( netuser != null )
					{
						NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
						//System.out.println("ZutiNetSendMethods - bombardierReleasedOrdinance_ToClient forwarded to LIVE AC owner >" + netuser.uniqueName() + "<.");
					}
				}
			}
			else
			{
				//Aircraft is AI controlled
				List hosts = NetEnv.hosts();
				NetUser netuser = null;
				
				for( int i=0; i<hosts.size(); i++ )
				{
					netuser = (NetUser)hosts.get(i);
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.BOMBARDIER_RELEASED_ORDINANCE);
					netmsgguaranted.write255(acName);
					netmsgguaranted.writeBoolean(bool);
					netmsgguaranted.writeBoolean(hasBombBayDoors);
					
					NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
				}
				
				//All AI is controlled by the server and this is server - release bombs.
				ac.FM.CT.bHasBayDoors = hasBombBayDoors;
				ZutiSupportMethods_FM.executeDropBombs(ac.FM);
				//System.out.println("ZutiNetSendMethods - bombardierReleasedOrdinance_ToClient executed on AC >" + acName + "<.");
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * AC changed its bomb bay doors status. This method is only executed BY THE SERVER!
	 * @param acName
	 * @param status
	 */
	public static void bombBayDoorsStatus_ToClient(String acName, int status)
	{
		try
		{
			if( acName == null )
				return;
			
			Aircraft ac = (Aircraft)Actor.getByName(acName);
			if( ac == null )
				return;
			
			if( acName.endsWith("_0") )
			{
				//Aircraft is controlled by user. Must forward bomb release data to aircraft owner.
				if( ac.FM instanceof RealFlightModel )
				{
					//Server is live plane owner and can change bomb bay doors!
					//21 = event ID for bomb bay doors
					Main3D.cur3D().aircraftHotKeys.doCmdPilot( 21, true );
					//System.out.println("ZutiNetSendMethods - bombBayDoorsStatus_ToClient executed for AC >" + acName + "<.");
				}
				else
				{
					//Forward data to rightful owner
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.BOMB_BAY_DOORS_STATUS);
					netmsgguaranted.write255(acName);
					netmsgguaranted.writeByte(status);
					NetUser netuser = ZutiSupportMethods.getNetUser( acName.substring(0, acName.lastIndexOf("_0")) );
					if( netuser != null )
					{
						NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
						//System.out.println("ZutiNetSendMethods - bombBayDoorsStatus_ToClient forwarded to LIVE AC owner >" + netuser.uniqueName() + "<.");
					}
				}
			}
			else
			{
				//Aircraft is AI controlled
				List hosts = NetEnv.hosts();
				NetUser netuser = null;
				
				for( int i=0; i<hosts.size(); i++ )
				{
					netuser = (NetUser)hosts.get(i);
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.BOMB_BAY_DOORS_STATUS);
					netmsgguaranted.write255(acName);
					netmsgguaranted.writeByte(status);
					
					NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
				}
				
				//All AI is controled by the server and this is server - change bomb bay doors status.
				ac.FM.CT.BayDoorControl = status;
				//System.out.println("ZutiNetSendMethods - bombBayDoorsStatus_ToClient executed on AC >" + acName + "<.");
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method forwards bombardier instruments data to those users that are riding the same aircraft.
	 * @param sender
	 * @param netmsginput
	 */
	public static void bombardierInstrumentsChanged_ToClient(NetUser sender, String acName, int distance, int sideslip, int altitude, int speed, int gunsightAutomation)
	{
		try
		{
			if( sender == null || acName == null )
				return;
			
			NetMsgGuaranted netmsgguaranted = null;
			
			NetUser netuser = null;
			ZutiAircraftCrew positions = ZutiAircraftCrewManagement.getAircraftCrew(acName);
			if( positions != null )
			{
				List crew = positions.getCrewList();
				for( int i=0; i<crew.size(); i++ )
				{
					netuser = ZutiSupportMethods.getNetUser( (String)crew.get(i) );
					if( netuser != null && !netuser.uniqueName().equals(sender.uniqueName()) )
					{
						netmsgguaranted = new NetMsgGuaranted();
						netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.BOMBARDIER_INSTRUMENTS_CHANGED);
						
						netmsgguaranted.write255(acName);
						netmsgguaranted.writeByte(distance);
						netmsgguaranted.writeByte(sideslip);
						netmsgguaranted.writeByte(altitude);
						netmsgguaranted.writeByte(speed);
						netmsgguaranted.writeByte(gunsightAutomation);
						
						NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
						
						//System.out.println("ZutiNetSendMethods - bombardierInstrumentsChanged_Client data forwarded to user >" + netuser.uniqueName() + "<.");
					}
				}
			}
			
			//System.out.println("ZutiNetSendMethods - bombardierInstrumentsChanged_Client data processed.");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method is executed by server to inform remaining crew members that their comrade
	 * scored some victory. They all should share that benefit (or loss, if that member hit
	 * friendly unit).
	 * @param sender
	 * @param netmsginput
	 */
	public static void syncCrewScore_ToClient(NetUser sender, String acName, String actorName, boolean damagedOnly, double explosionPower)
	{
		try
		{
			NetMsgGuaranted netmsgguaranted = null;
			NetUser netuser = null;

			if( sender == null || acName == null || actorName == null )
				return;
			
			Actor actor = Actor.getByName(actorName);
			
			if( actor != null )
			{
				ZutiAircraftCrew positions = ZutiAircraftCrewManagement.getAircraftCrew(acName);
				if( positions != null )
				{
					List crew = positions.getCrewList();
					for( int i=0; i<crew.size(); i++ )
					{
						netuser = ZutiSupportMethods.getNetUser( (String)crew.get(i) );

						if( netuser != null && netuser.masterChannel() != null )
						{
							netmsgguaranted = new NetMsgGuaranted();
							netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.SYNC_CREW_SCORE);
							
							netmsgguaranted.write255(acName);
							netmsgguaranted.write255(actorName);
							netmsgguaranted.writeBoolean(damagedOnly);
							netmsgguaranted.writeDouble(explosionPower);
							
							NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
							
							//System.out.println("ZutiNetSendMethods - syncCrewScore_Client data forwarded to user >" + netuser.uniqueName() + "<.");
						}
					}	
				}
			}
			
			//System.out.println("ZutiNetSendMethods - syncCrewScore_Client data processed.");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Forward this message to the crew or received user aircraft.
	 * @param sender
	 * @param netmsginput
	 */
	public static void transferControls_ToClient(NetUser sender, String targetUser)
	{		
		try
		{
			if( sender == null || targetUser == null || sender.uniqueName().equals(targetUser) )
				return;
			
			NetMsgGuaranted netmsgguaranted = null;
			NetUser netuser = null;
			
			ZutiAircraftCrew positions = ZutiAircraftCrewManagement.getAircraftCrew(sender.findAircraft().name());
			if( positions != null )
			{
				List crew = positions.getCrewList();
				for( int i=0; i<crew.size(); i++ )
				{
					netuser = ZutiSupportMethods.getNetUser( (String)crew.get(i) );
					
					netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.TRANSFER_CONTROLS);
					netmsgguaranted.write255(targetUser);

					NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
					
					//System.out.println("ZutiNetSendMethods - transferControls_Client parameter >" + username + "< executed for user >" + crewMember.uniqueName() + "<.");
				}
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Server received info from acting pilot/instructor about moving flight controls.
	 * @param sender
	 * @param netmsginput
	 */
	public static void aircraftControlsMoved_ToClient(String acName, int eventId, float controlState)
	{		
		try
		{
			if( acName == null )
				return;
			
			acName = acName.substring(0, acName.lastIndexOf("_0"));
			NetUser netuser = ZutiSupportMethods.getNetUser(acName);
			
			if( netuser == null )
				return;
			
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.AIRCRAFT_CONTROLS_MOVED);
			netmsgguaranted.writeByte(eventId);
			netmsgguaranted.writeFloat(controlState);

			NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			
			//System.out.println("ZutiNetSendMethods - aircraftControlsMove_Client parameters sent to >" + username + "<.");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Server received information that acting pilot/instructor changed aircraft related control (flaps,...).
	 * If host is also plane owner, nothing is dorwarded.
	 * @param sender
	 * @param netmsginput
	 */
	public static void aircraftControlsChanged_ToClient(String acName, int eventId, boolean weaponControl)
	{		
		try
		{
			if( acName == null )
				return;
			
			acName = acName.substring(0, acName.lastIndexOf("_0"));
			
			//Is host also aircraft owner? Received aircraft name should be ending with _0 that is indicating
			//that aircraft is controlled by live user. We should remove that string beforehand.
			if( ((NetUser)NetEnv.host()).uniqueName().equals(acName) )
			{
				//We are... execute control change and exit.
				Main3D.cur3D().aircraftHotKeys.doCmdPilot(eventId, weaponControl);
				return;
			}
			
			NetUser netuser = ZutiSupportMethods.getNetUser(acName);
			
			if( netuser == null )
				return;

			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.AIRCRAFT_CONTROLS_CHANGED);
			netmsgguaranted.writeByte(eventId);
			netmsgguaranted.writeBoolean(weaponControl);

			if( netuser != null )
			{
				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
				//System.out.println("ZutiNetSendMethods - aircraftControlsChanged_Client parameters sent to >" + username + "<.");
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Forward log message to all clients.
	 * @param Message
	 */
	public static void logMessage_ToClient(String message)
	{
		try
		{
			if( message == null )
				return;
			
			List hosts = NetEnv.hosts();
			NetUser netuser = null;
			
			for( int i=0; i<hosts.size(); i++ )
			{
				netuser = (NetUser)hosts.get(i);
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.LOG_MESSAGE);
				netmsgguaranted.write255(message);
				
				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Calculate available fuel amount for born place that player parked on and forward
	 * that info back to the player.
	 * @param fuel
	 * @param user
	 */
	public static void refuelAircraft_ToClient(float fuel, String user)
	{
		try
		{
			if( user == null || Mission.isSingle() )
			{
				//RRR works for single missions also! Start Refueling process.
				Aircraft userAircraft = World.getPlayerAircraft();
				Point3d pos = userAircraft.pos.getAbsPoint();
				fuel = ZutiSupportMethods_ResourcesManagement.getFuelForPlayer(fuel, pos.x, pos.y);
				ZutiSupportMethods_FM.startRefueling(userAircraft, fuel);
				
				return;
			}
			Aircraft userAircraft = (Aircraft)Actor.getByName(user + "_0");
			if( userAircraft == null )
			{
				System.out.println("refuelAircraft_ToClient - User aircraft not identified!");
				return;
			}	
			
			Point3d pos = userAircraft.pos.getAbsPoint();
			fuel = ZutiSupportMethods_ResourcesManagement.getFuelForPlayer(fuel, pos.x, pos.y);
			if( ((NetUser)NetEnv.host()).uniqueName().equals(user) )
			{
				//We are server and target user. Start Refueling process
				ZutiSupportMethods_FM.startRefueling(userAircraft, fuel);
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				NetUser netuser = ZutiSupportMethods.getNetUser(user);
				if( netuser == null )
					return;

				//Notify user to start refueling process
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.RRR_REFUEL);
				netmsgguaranted.writeFloat(fuel);

				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Calculate available bullets amount for born place that player parked on and forward
	 * that info back to the player.
	 * @param bullets
	 * @param user
	 */
	public static void reloadGuns_ToClient(long bullets, String user)
	{		
		try
		{
			if( user == null || Mission.isSingle() )
			{
				//RRR works for single missions also! Start Refueling process.
				Aircraft userAircraft = World.getPlayerAircraft();
				Point3d pos = userAircraft.pos.getAbsPoint();
				bullets = ZutiSupportMethods_ResourcesManagement.getBulletsForPlayer(bullets, pos.x, pos.y);
				ZutiSupportMethods_FM.startRearming_Cannons(userAircraft, bullets);
				
				return;
			}
			Aircraft userAircraft = (Aircraft)Actor.getByName(user + "_0");
			if( userAircraft == null )
			{
				System.out.println("reloadGuns_ToClient - User aircraft not identified!");
				return;
			}	
			
			Point3d pos = userAircraft.pos.getAbsPoint();
			bullets = ZutiSupportMethods_ResourcesManagement.getBulletsForPlayer(bullets, pos.x, pos.y);
			if( ((NetUser)NetEnv.host()).uniqueName().equals(user) )
			{
				//We are server and target user. Start Refueling process
				ZutiSupportMethods_FM.startRearming_Cannons(userAircraft, bullets);
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				NetUser netuser = ZutiSupportMethods.getNetUser(user);
				if( netuser == null )
					return;

				//Notify user to start refueling process
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.RRR_BULLETS);
				netmsgguaranted.writeLong(bullets);

				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Calculate available bombs amount for born place that player parked on and forward
	 * that info back to the player.
	 * @param bullets
	 * @param user
	 */
	public static void reloadBombs_ToClient(int[] bombs, String user)
	{		
		try
		{
			if( user == null || Mission.isSingle() )
			{
				//RRR works for single missions also! Start Refueling process.
				Aircraft userAircraft = World.getPlayerAircraft();
				Point3d pos = userAircraft.pos.getAbsPoint();
				bombs = ZutiSupportMethods_ResourcesManagement.getBombsForPlayer(bombs, pos.x, pos.y);
				ZutiSupportMethods_FM.startRearming_Bombs(userAircraft, bombs);
				
				return;
			}
			Aircraft userAircraft = (Aircraft)Actor.getByName(user + "_0");
			if( userAircraft == null )
			{
				System.out.println("reloadBombs_ToClient - User aircraft not identified!");
				return;
			}	
			
			Point3d pos = userAircraft.pos.getAbsPoint();
			bombs = ZutiSupportMethods_ResourcesManagement.getBombsForPlayer(bombs, pos.x, pos.y);
			if( ((NetUser)NetEnv.host()).uniqueName().equals(user) )
			{
				//We are server and target user. Start Refueling process
				ZutiSupportMethods_FM.startRearming_Bombs(userAircraft, bombs);
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				NetUser netuser = ZutiSupportMethods.getNetUser(user);
				if( netuser == null )
					return;

				//Notify user to start refueling process
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.RRR_BOMBS);
				netmsgguaranted.writeInt(bombs[0]);
				netmsgguaranted.writeInt(bombs[1]);
				netmsgguaranted.writeInt(bombs[2]);
				netmsgguaranted.writeInt(bombs[3]);
				netmsgguaranted.writeInt(bombs[4]);
				netmsgguaranted.writeInt(bombs[5]);
				netmsgguaranted.write255(user);

				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Calculate available rockets amount for born place that player parked on and forward
	 * that info back to the player.
	 * @param bullets
	 * @param user
	 */
	public static void reloadRockets_ToClient(long rockets, String user)
	{		
		try
		{
			if( user == null || Mission.isSingle() )
			{
				//RRR works for single missions also! Start Refueling process.
				Aircraft userAircraft = World.getPlayerAircraft();
				Point3d pos = userAircraft.pos.getAbsPoint();
				rockets = ZutiSupportMethods_ResourcesManagement.getRocketsForPlayer(rockets, pos.x, pos.y);
				ZutiSupportMethods_FM.startRearming_Rockets(userAircraft, rockets);
				
				return;
			}
			Aircraft userAircraft = (Aircraft)Actor.getByName(user + "_0");
			if( userAircraft == null )
			{
				System.out.println("reloadRockets_ToClient - User aircraft not identified!");
				return;
			}	
			
			Point3d pos = userAircraft.pos.getAbsPoint();
			rockets = ZutiSupportMethods_ResourcesManagement.getRocketsForPlayer(rockets, pos.x, pos.y);
			if( ((NetUser)NetEnv.host()).uniqueName().equals(user) )
			{
				//We are server and target user. Start Refueling process
				ZutiSupportMethods_FM.startRearming_Rockets(userAircraft, rockets);
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				NetUser netuser = ZutiSupportMethods.getNetUser(user);
				if( netuser == null )
					return;

				//Notify user to start refueling process
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.RRR_ROCKETS);
				netmsgguaranted.writeLong(rockets);

				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Check if there are any engines left and report it back to the client.
	 * that info back to the player.
	 * @param engines
	 * @param engineId
	 * @param user
	 */
	public static void repairEngine_ToClient(int engines, int engineId, String user)
	{		
		try
		{
			if( user == null || Mission.isSingle() )
			{
				//RRR works for single missions also! Start Refueling process.
				Aircraft userAircraft = World.getPlayerAircraft();
				Point3d pos = userAircraft.pos.getAbsPoint();
				engines = (int)ZutiSupportMethods_ResourcesManagement.getEnginesForPlayer(engines, pos.x, pos.y);
				ZutiSupportMethods_FM.startEngineRepairing(userAircraft, engines, engineId);
				
				return;
			}
			Aircraft userAircraft = (Aircraft)Actor.getByName(user + "_0");
			if( userAircraft == null )
			{
				System.out.println("repairEngine_ToClient - User aircraft not identified!");
				return;
			}	
			
			Point3d pos = userAircraft.pos.getAbsPoint();
			engines = (int)ZutiSupportMethods_ResourcesManagement.getEnginesForPlayer(engines, pos.x, pos.y);
			if( ((NetUser)NetEnv.host()).uniqueName().equals(user) )
			{
				//We are server and target user. Start Refueling process
				ZutiSupportMethods_FM.startEngineRepairing(userAircraft, engines, engineId);
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				NetUser netuser = ZutiSupportMethods.getNetUser(user);
				if( netuser == null )
					return;

				//Notify user to start refueling process
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.RRR_ENGINE);
				netmsgguaranted.writeInt(engines);
				netmsgguaranted.writeInt(engineId);
				netmsgguaranted.write255(user);

				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Check if there are any repair kits left and report it back to the client.
	 * @param user
	 */
	public static void repairKit_ToClient(long requestedKits, String user)
	{		
		try
		{
			if( user == null || Mission.isSingle() )
			{
				//RRR works for single missions also! Start Refueling process.
				Aircraft userAircraft = World.getPlayerAircraft();
				Point3d pos = userAircraft.pos.getAbsPoint();
				requestedKits = ZutiSupportMethods_ResourcesManagement.getRepairKitsForPlayer(requestedKits, pos.x, pos.y);
				if( requestedKits <= 0 )
					ZutiSupportMethods_FM.startRepairing(userAircraft);
				
				return;
			}
			Aircraft userAircraft = (Aircraft)Actor.getByName(user + "_0");
			if( userAircraft == null )
			{
				System.out.println("repairEngine_ToClient - User aircraft not identified!");
				return;
			}	
			
			Point3d pos = userAircraft.pos.getAbsPoint();
			requestedKits = ZutiSupportMethods_ResourcesManagement.getRepairKitsForPlayer(requestedKits, pos.x, pos.y);
			if( requestedKits <= 0 )
				return;
			
			if( ((NetUser)NetEnv.host()).uniqueName().equals(user) )
			{
				//We are server and target user. Start Refueling process
				ZutiSupportMethods_FM.startRepairing(userAircraft);
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				NetUser netuser = ZutiSupportMethods.getNetUser(user);
				if( netuser == null )
					return;

				//Notify user to start refueling process
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.RRR_REPAIR_KIT);
				netmsgguaranted.writeLong(requestedKits);
				netmsgguaranted.write255(user);

				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Call this method to sync used resources by player spawned plane with the server resources.
	 * @param bullets
	 * @param bombs
	 * @param rockets
	 * @param fuel
	 * @param user
	 */
	public static void spawnResources_ToClient(long bullets, int[] bombs, long rockets, float fuel, long cargo, String user)
	{
		try
		{
			int[] tmpBombs = ZutiSupportMethods.cloneIntegerArray(bombs);
			
			if( user == null || Mission.isSingle() )
			{
			 // +++ TODO: Storebror: null Checks added +++
			    if (World.getPlayerAircraft() == null) {
	                System.out.println("spawnResources_ToClient - User and Player Aircraft are both null!");
	                return;
			    }
			 // --- TODO: Storebror: null Checks added ---
				Point3d pos = World.getPlayerAircraft().pos.getAbsPoint();
				long availableBullets = ZutiSupportMethods_ResourcesManagement.getBulletsForPlayer(bullets, pos.x, pos.y);
				int[] availableBombs = ZutiSupportMethods_ResourcesManagement.getBombsForPlayer(tmpBombs, pos.x, pos.y);
				long availableRockets = ZutiSupportMethods_ResourcesManagement.getRocketsForPlayer(rockets, pos.x, pos.y);
				long availableFuel = ZutiSupportMethods_ResourcesManagement.getFuelForPlayer(fuel, pos.x, pos.y);
				long availableCargo = ZutiSupportMethods_ResourcesManagement.getCargoForPlayer(cargo, pos.x, pos.y);
				
				bullets -= availableBullets;
				bombs[0] -= availableBombs[0];
				bombs[1] -= availableBombs[1];
				bombs[2] -= availableBombs[2];
				bombs[3] -= availableBombs[3];
				bombs[4] -= availableBombs[4];
				bombs[5] -= availableBombs[5];
				rockets -= availableRockets;
				fuel -= availableFuel;
				cargo -= availableCargo;
				
				//RRR works for single missions also! Update current ac resources.
				ZutiSupportMethods_FM.updateSpawnResources(bullets, bombs, rockets, fuel, cargo);
				return;
			}
			
			Aircraft userAircraft = (Aircraft)Actor.getByName(user + "_0");
			if( userAircraft == null )
			{
				System.out.println("spawnResources_ToClient - User aircraft not identified!");
				return;
			}
			
			Point3d pos = userAircraft.pos.getAbsPoint();
			long availableBullets = ZutiSupportMethods_ResourcesManagement.getBulletsForPlayer(bullets, pos.x, pos.y);			
			int[] availableBombs = ZutiSupportMethods_ResourcesManagement.getBombsForPlayer(tmpBombs, pos.x, pos.y);
			long availableRockets = ZutiSupportMethods_ResourcesManagement.getRocketsForPlayer(rockets, pos.x, pos.y);
			long availableFuel = ZutiSupportMethods_ResourcesManagement.getFuelForPlayer(fuel, pos.x, pos.y);
			long availableCargo = ZutiSupportMethods_ResourcesManagement.getCargoForPlayer(cargo, pos.x, pos.y);
						
			bullets -= availableBullets;
			bombs[0] -= availableBombs[0];
			bombs[1] -= availableBombs[1];
			bombs[2] -= availableBombs[2];
			bombs[3] -= availableBombs[3];
			bombs[4] -= availableBombs[4];
			bombs[5] -= availableBombs[5];
			rockets -= availableRockets;
			fuel -= availableFuel;
			cargo -= availableCargo;
			
			if( ((NetUser)NetEnv.host()).uniqueName().equals(user) )
			{
				//We are server and target user. Start updating process
				ZutiSupportMethods_FM.updateSpawnResources(bullets, bombs, rockets, fuel, cargo);
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				NetUser netuser = ZutiSupportMethods.getNetUser(user);
				if( netuser == null )
				{
					System.out.println("ZSM_NS - NetUser >" + user + "< not found!");
					return;
				}
				//Notify user to start refueling process
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.RRR_SPAWN_RESOURCES);
				
				//Add bullets information to the stream
				netmsgguaranted.writeLong(bullets);
				
				//Add rockets information to the stream
				netmsgguaranted.writeLong(rockets);
				
				//Add bomb information to the stream
				netmsgguaranted.writeInt(bombs[0]);
				netmsgguaranted.writeInt(bombs[1]);
				netmsgguaranted.writeInt(bombs[2]);
				netmsgguaranted.writeInt(bombs[3]);
				netmsgguaranted.writeInt(bombs[4]);
				netmsgguaranted.writeInt(bombs[5]);
				
				//Add fuel information to the stream
				netmsgguaranted.writeFloat(fuel);
				
				//Add cargo information to the stream
				netmsgguaranted.writeLong(cargo);
				
				//Add user information to the stream
				netmsgguaranted.write255(user);
				//System.out.println("SENT TO USER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method sends appropriate data to client about his requested aircraft.
	 * @param acName
	 * @param bornPlaceId
	 * @param client
	 */
	public static void aircraftAvailability_ToClient(String acName, int bornPlaceId, NetUser client)
	{
		if( client == null )
			return;
		try
		{
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.AIRCRAFT_AVAILABILITY);
			netmsgguaranted.write255(acName);
			
			if( ZutiSupportMethods_Air.getAircraft( acName, bornPlaceId) != null )
				netmsgguaranted.writeInt( 1 );
			else
				netmsgguaranted.writeInt( -1 );
			
			NetEnv.host().postTo(client.masterChannel(), netmsgguaranted);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Report stay place to requesting client.
	 * @param user
	 */
	public static void carrierStayPlace_ToClient(NetUser user)
	{
		try
		{
			int spawnPlace = ZutiSupportMethods_Net.getStayPlace(user);
			
			if( ((NetUser)NetEnv.host()).equals(user) )
			{
				//We are server and target user.
				((NetUser)NetEnv.host()).zutiSetAirdomeStay(spawnPlace);
				
			}
			else if( Main.cur().netServerParams.isMaster() )
			{
				if( user == null )
					return;

				//Notify user about free carrier spawn place
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.CARRIER_SPAWN_PLACE);
				netmsgguaranted.writeInt(spawnPlace);

				NetEnv.host().postTo(user.masterChannel(), netmsgguaranted);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Forward UPDATE_NET_PLACE message for users. Skip sender as he already did that.
	 * @param sender
	 */
	public static void updateNetPlace_ToClients(NetUser sender)
	{
		try
		{
			List hosts = NetEnv.hosts();
			NetUser netuser = null;
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(ZutiSupportMethods_NetSend.UPDATE_NET_PLACE);
			
			for( int i=0; i<hosts.size(); i++ )
			{
				netuser = (NetUser)hosts.get(i);
				
				if( netuser.uniqueName().equals(sender.uniqueName()) )
					continue;
				
				// +++ TODO: Storebror TEST: Net Check +++
//				NetEnv.host().postTo(netuser.masterChannel(), netmsgguaranted);
                NetEnv.host().postTo(netuser.masterChannel(), new NetMsgGuaranted(ZutiSupportMethods_NetSend.UPDATE_NET_PLACE));
                // --- TODO: Storebror TEST: Net Check ---
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}