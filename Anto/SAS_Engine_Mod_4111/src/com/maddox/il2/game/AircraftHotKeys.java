/*Modified AircraftHotKeys class for the SAS Engine Mod*/

package com.maddox.il2.game;

import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.gui.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.*;
import com.maddox.sound.*;
import com.maddox.util.*;
import java.io.IOException;
import java.util.*;


public class AircraftHotKeys
{
	//TODO: Default Parameters
	// --------------------------------------------------------
	public static boolean bFirstHotCmd = false;
	private RealFlightModel FM;
	private boolean bPropAuto;
	private boolean bAfterburner;
	private float lastPower;
	private float lastProp;
	private float lastPower2;
	private float lastPower1;
	private float lastPower3;
	private float lastPower4;
	private float lastProp1;
	private float lastProp2;
	private float lastProp3;
	private float lastProp4;
	private float lastRadiator;
	private AircraftLH bAAircraft;
	private FlightModel baFM;
	private static final int MOVE_POWER1 = 15;
	private static final int MOVE_POWER2 = 16;
	private static final int MOVE_POWER3 = 17;
	private static final int MOVE_POWER4 = 18;
	private static final int MOVE_RADIATOR = 19;
	private static final int MOVE_PROP1 = 20;
	private static final int MOVE_PROP2 = 21;
	private static final int MOVE_PROP3 = 22;
	private static final int MOVE_PROP4 = 23;
	private static final int MOVE_ZOOM = 100;
	private boolean changeFovEnabled;
	private static final int BEACON_PLUS = 139;
	private static final int BEACON_MINUS = 140;
	private static final int AUX1_PLUS = 149;
	private static final int AUX1_MINUS = 150;
	private static final int AUX_A = 157;
	private static final int AUX_B = 158;
	private static final int HEAD_LEAN_FORWARD = 170;
	private static final int HEAD_LEAN_SIDE = 171;
	private static final int HEAD_RAISE = 172;
	private static final int MOVE_POWER = 1;
	private static final int MOVE_FLAPS = 2;
	private static final int MOVE_AILERON = 3;
	private static final int MOVE_ELEVATOR = 4;
	private static final int MOVE_RUDDER = 5;
	private static final int MOVE_BRAKE = 6;
	private static final int MOVE_STEP = 7;
	private static final int MOVE_TRIMHOR = 8;
	private static final int MOVE_TRIMVER = 9;
	private static final int MOVE_TRIMRUD = 10;
	private int cptdmg;
	private boolean useSmartAxisForPower;
	private boolean useSmartAxisForPitch;
	private static final int BRAKE = 0;
	private static final int RUDDER_LEFT = 1;
	private static final int RUDDER_RIGHT = 2;
	private static final int ELEVATOR_UP = 3;
	private static final int ELEVATOR_DOWN = 4;
	private static final int AILERON_LEFT = 5;
	private static final int AILERON_RIGHT = 6;
	private static final int RADIATOR = 7;
	private static final int GEAR = 9;
	private static final int GUNPODS = 15;
	private static final int WEAPON0 = 16;
	private static final int WEAPON1 = 17;
	private static final int WEAPON2 = 18;
	private static final int WEAPON3 = 19;
	private static final int WEAPON01 = 64;
	private static final int POWER_0 = 20;
	private static final int POWER_1 = 21;
	private static final int POWER_2 = 22;
	private static final int POWER_3 = 23;
	private static final int POWER_4 = 24;
	private static final int POWER_5 = 25;
	private static final int POWER_6 = 26;
	private static final int POWER_7 = 27;
	private static final int POWER_8 = 28;
	private static final int POWER_9 = 29;
	private static final int POWER_10 = 30;
	private static final int STEP_0 = 31;
	private static final int STEP_1 = 32;
	private static final int STEP_2 = 33;
	private static final int STEP_3 = 34;
	private static final int STEP_4 = 35;
	private static final int STEP_5 = 36;
	private static final int STEP_6 = 37;
	private static final int STEP_7 = 38;
	private static final int STEP_8 = 39;
	private static final int STEP_9 = 40;
	private static final int STEP_10 = 41;
	private static final int STEP_A = 42;
	private static final int AIRCRAFT_TRIM_V_0 = 43;
	private static final int AIRCRAFT_TRIM_V_PLUS = 44;
	private static final int AIRCRAFT_TRIM_V_MINUS = 45;
	private static final int AIRCRAFT_TRIM_H_0 = 46;
	private static final int AIRCRAFT_TRIM_H_PLUS = 47;
	private static final int AIRCRAFT_TRIM_H_MINUS = 48;
	private static final int AIRCRAFT_TRIM_R_0 = 49;
	private static final int AIRCRAFT_TRIM_R_PLUS = 50;
	private static final int AIRCRAFT_TRIM_R_MINUS = 51;
	private static final int FLAPS_NOTCH_UP = 52;
	private static final int FLAPS_NOTCH_DOWN = 53;
	private static final int GEAR_UP_MANUAL = 54;
	private static final int GEAR_DOWN_MANUAL = 55;
	private static final int RUDDER_LEFT_1 = 56;
	private static final int RUDDER_CENTRE = 57;
	private static final int RUDDER_RIGHT_1 = 58;
	private static final int POWER_PLUS_5 = 59;
	private static final int POWER_MINUS_5 = 60;
	private static final int BOOST = 61;
	private static final int AIRCRAFT_DROP_TANKS = 62;
	private static final int AIRCRAFT_TOGGLE_AIRBRAKE = 63;
	private static final int AIRCRAFT_TOGGLE_ENGINE = 70;
	private static final int AIRCRAFT_STABILIZER = 71;
	private static final int AIRCRAFT_TAILWHEELLOCK = 72;
	private static final int STEP_PLUS_5 = 73;
	private static final int STEP_MINUS_5 = 74;
	private static final int MIX_0 = 75;
	private static final int MIX_1 = 76;
	private static final int MIX_2 = 77;
	private static final int MIX_3 = 78;
	private static final int MIX_4 = 79;
	private static final int MIX_5 = 80;
	private static final int MIX_6 = 81;
	private static final int MIX_7 = 82;
	private static final int MIX_8 = 83;
	private static final int MIX_9 = 84;
	private static final int MIX_10 = 85;
	private static final int MIX_PLUS_20 = 86;
	private static final int MIX_MINUS_20 = 87;
	private static final int MAGNETO_PLUS_1 = 88;
	private static final int MAGNETO_MINUS_1 = 89;
	private static final int ENGINE_SELECT_ALL = 90;
	private static final int ENGINE_SELECT_NONE = 91;
	private static final int ENGINE_SELECT_LEFT = 92;
	private static final int ENGINE_SELECT_RIGHT = 93;
	private static final int ENGINE_SELECT_1 = 94;
	private static final int ENGINE_SELECT_2 = 95;
	private static final int ENGINE_SELECT_3 = 96;
	private static final int ENGINE_SELECT_4 = 97;
	private static final int ENGINE_SELECT_5 = 98;
	private static final int ENGINE_SELECT_6 = 99;
	private static final int ENGINE_SELECT_7 = 100;
	private static final int ENGINE_SELECT_8 = 101;
	private static final int ENGINE_TOGGLE_ALL = 102;
	private static final int ENGINE_TOGGLE_LEFT = 103;
	private static final int ENGINE_TOGGLE_RIGHT = 104;
	private static final int ENGINE_TOGGLE_1 = 105;
	private static final int ENGINE_TOGGLE_2 = 106;
	private static final int ENGINE_TOGGLE_3 = 107;
	private static final int ENGINE_TOGGLE_4 = 108;
	private static final int ENGINE_TOGGLE_5 = 109;
	private static final int ENGINE_TOGGLE_6 = 110;
	private static final int ENGINE_TOGGLE_7 = 111;
	private static final int ENGINE_TOGGLE_8 = 112;
	private static final int ENGINE_EXTINGUISHER = 113;
	private static final int ENGINE_FEATHER = 114;
	private static final int COMPRESSORSTEP_PLUS = 115;
	private static final int COMPRESSORSTEP_MINUS = 116;
	private static final int SIGHT_DIST_PLUS = 117;
	private static final int SIGHT_DIST_MINUS = 118;
	private static final int SIGHT_SIDE_RIGHT = 119;
	private static final int SIGHT_SIDE_LEFT = 120;
	private static final int SIGHT_ALT_PLUS = 121;
	private static final int SIGHT_ALT_MINUS = 122;
	private static final int SIGHT_SPD_PLUS = 123;
	private static final int SIGHT_SPD_MINUS = 124;
	private static final int SIGHT_AUTO_ONOFF = 125;
	private static final int AIRCRAFT_DOCK_UNDOCK = 126;
	private static final int WINGFOLD = 127;
	private static final int COCKPITDOOR = 128;
	private static final int AIRCRAFT_CARRIERHOOK = 129;
	private static final int AIRCRAFT_BRAKESHOE = 130;
	public static int hudLogPowerId;
	public static int hudLogWeaponId;
	private boolean bAutoAutopilot;
	private int switchToCockpitRequest;
	private HotKeyCmd cmdFov[];
	private static Object namedAll[] = new Object[1];
	private static TreeMap namedAircraft = new TreeMap();
	// --------------------------------------------------------

	//TODO: New Parameters
	// --------------------------------------------------------
	private boolean									bSpeedbarTAS				= false;
	private boolean									bSeparateGearUpDown			= false;
	private boolean									bSeparateHookUpDown			= false;
	private boolean									bSeparateRadiatorOpenClose	= false;
	private boolean									bMusicOn					= true;
	private boolean									bToggleMusic				= true;
	private int										iAirShowSmoke				= 0;
	private boolean									bAirShowSmokeEnhanced		= false;
	private boolean									bSideDoor					= true;
	private int										COCKPIT_DOOR				= 1;
	private int										SIDE_DOOR					= 2;
	private boolean									bAllowDumpFuel				= true;
	private boolean									bDumpFuel					= false;
	private boolean									bBombBayDoors				= true;
	private static final int BRAKE_RIGHT = 144;
	private static final int BRAKE_LEFT = 145;
	private int flapIndex = 0;
	private int varWingIndex = 0;
	// --------------------------------------------------------

	public AircraftHotKeys()
	{
		bPropAuto = true;
		bAfterburner = false;
		lastPower = -0.5F;
		lastProp = 1.5F;
		lastPower2 = -0.5F;
		lastPower1 = -0.5F;
		lastPower3 = -0.5F;
		lastPower4 = -0.5F;
		lastProp1 = 1.5F;
		lastProp2 = 1.5F;
		lastProp3 = 1.5F;
		lastProp4 = 1.5F;
		lastRadiator = -0.5F;
		changeFovEnabled = true;
		cptdmg = 1;
		useSmartAxisForPower = false;
		useSmartAxisForPitch = false;
		bAutoAutopilot = false;
		switchToCockpitRequest = -1;
		cmdFov = new HotKeyCmd[16];
		createPilotHotKeys();
		createPilotHotMoves();
		createGunnerHotKeys();
		createMiscHotKeys();
		create_MiscHotKeys();
		createViewHotKeys();
		createTimeHotKeys();
		createCommonHotKeys();
		if (Config.cur.ini.get("Mods", "SpeedbarTAS", 0) > 0)
			bSpeedbarTAS = true;
		if (Config.cur.ini.get("Mods", "SeparateGearUpDown", 0) > 0)
			bSeparateGearUpDown = true;
		if (Config.cur.ini.get("Mods", "SeparateHookUpDown", 0) > 0)
			bSeparateHookUpDown = true;
		if (Config.cur.ini.get("Mods", "SeparateRadiatorOpenClose", 0) > 0)
			bSeparateRadiatorOpenClose = true;
		if (Config.cur.ini.get("Mods", "ToggleMusic", 1) == 0)
			bToggleMusic = false;
		if (Config.cur.ini.get("Mods", "BombBayDoors", 1) == 0)
			bBombBayDoors = false;
		if (Config.cur.ini.get("Mods", "SideDoor", 1) == 0)
			bSideDoor = false;
		iAirShowSmoke = Config.cur.ini.get("Mods", "AirShowSmoke", 0);
		if (iAirShowSmoke < 1 || iAirShowSmoke > 3)
			iAirShowSmoke = 0;
		if (Config.cur.ini.get("Mods", "AirShowSmokeEnhanced", 0) > 0)
			bAirShowSmokeEnhanced = true;
		if (Config.cur.ini.get("Mods", "DumpFuel", 1) == 0)
			bAllowDumpFuel = false;
	}

	class HotKeyCmdFireMove extends HotKeyCmdMove
	{

		public void begin()
		{
			byte byte0 = ((byte)(name().charAt(0) != '-' ? 1 : -1));
			doCmdPilotMove(cmd, Joy.normal(byte0 * move()));
		}

		public boolean isDisableIfTimePaused()
		{
			return disableIfPaused;
		}

		int cmd;
		boolean disableIfPaused;

		public HotKeyCmdFireMove(String s, String s1, int i, int j)
		{
			super(true, s1, s);
			cmd = i;
			setRecordId(j);
			disableIfPaused = true;
		}

		public HotKeyCmdFireMove(String s, String s1, int i, int j, boolean flag)
		{
			this(s, s1, i, j);
			disableIfPaused = flag;
		}
	}

	class HotKeyCmdFire extends HotKeyCmd
	{

		public void begin()
		{
			doCmdPilot(cmd, true);
			time = Time.tick();
		}

		public void tick()
		{
			if(Time.tick() > time + 500L)
				doCmdPilotTick(cmd);
		}

		public boolean isTickInTime(boolean flag)
		{
			return !flag;
		}

		public void end()
		{
			doCmdPilot(cmd, false);
		}

		public boolean isDisableIfTimePaused()
		{
			return true;
		}

		int cmd;
		long time;

		public HotKeyCmdFire(String s, String s1, int i, int j)
		{
			super(true, s1, s);
			cmd = i;
			setRecordId(j);
		}
	}


	public boolean isAfterburner()
	{
		if(!Actor.isValid(World.getPlayerAircraft()))
			bAfterburner = false;
		return bAfterburner;
	}

	public void setAfterburner(boolean flag)
	{
		if(FM.EI.isSelectionHasControlAfterburner())
		{
			bAfterburner = flag;
			if(bAfterburner)
			{
				if((FM.actor instanceof Hurricane) || (FM.actor instanceof A6M) && !(FM.actor instanceof A6M7_62) && !(FM.actor instanceof A6M5C) || (FM.actor instanceof P_51) || (FM.actor instanceof SPITFIRE) || (FM.actor instanceof MOSQUITO) || (FM.actor instanceof TEMPEST))
					HUD.logRightBottom("BoostWepTP0");
				else
					if(FM.actor instanceof FW_190A5165ATA)
						HUD.logRightBottom("BoostWepTP6");
					else
						HUD.logRightBottom("BoostWepTP" + FM.EI.getFirstSelected().getAfterburnerType());
			} else
			{
				HUD.logRightBottom(null);
			}
		}
		FM.CT.setAfterburnerControl(bAfterburner);
	}

	public void setAfterburnerForAutoActivation(boolean flag)
	{
		bAfterburner = flag;
	}

	public boolean isPropAuto()
	{
		if(!Actor.isValid(World.getPlayerAircraft()))
			bPropAuto = false;
		return bPropAuto;
	}

	public void setPropAuto(boolean flag)
	{
		if(flag && !FM.EI.isSelectionAllowsAutoProp())
		{
			return;
		} else
		{
			bPropAuto = flag;
			return;
		}
	}

	public void resetGame()
	{
		FM = null;
		bAfterburner = false;
		bPropAuto = true;
		lastPower = -0.5F;
		lastPower1 = -0.5F;
		lastPower2 = -0.5F;
		lastPower3 = -0.5F;
		lastPower4 = -0.5F;
		lastProp = 1.5F;
		lastProp1 = 1.5F;
		lastProp2 = 1.5F;
		lastProp3 = 1.5F;
		lastProp4 = 1.5F;
		checkSmartControlsUse();
	}

	public void resetUser()
	{
		resetGame();
	}

	private boolean setPilot()
	{
		FM = null;
		if(!Actor.isAlive(World.getPlayerAircraft()))
			return false;
		if(World.isPlayerParatrooper())
			return false;
		if(World.isPlayerDead())
			return false;
		FlightModel flightmodel = World.getPlayerFM();
		if(flightmodel == null)
			return false;
		if(flightmodel instanceof RealFlightModel)
		{
			FM = (RealFlightModel)flightmodel;
			return FM.isRealMode();
		} else
		{
			return false;
		}
	}

	private boolean setBombAimerAircraft()
	{
		bAAircraft = null;
		if(!Actor.isAlive(World.getPlayerAircraft()))
			return false;
		if(World.isPlayerParatrooper())
			return false;
		if(World.isPlayerDead())
			return false;
		FlightModel flightmodel = World.getPlayerFM();
		if(flightmodel == null)
		{
			return false;
		} else
		{
			bAAircraft = (AircraftLH)flightmodel.actor;
			baFM = flightmodel;
			return true;
		}
	}

	private void setPowerControl(float f)
	{
		if(f < 0.0F)
			f = 0.0F;
		if(f > 1.1F)
			f = 1.1F;
		if((FM.actor instanceof FW_190A4) && f > 0.875F)
			f = 0.875F;
		lastPower = f;
		FM.CT.setPowerControl(f);
		hudPower(FM.CT.PowerControl);
	}

	private void setPowerControl(float f, int i)
	{
		if(f < 0.0F)
			f = 0.0F;
		if(f > 1.1F)
			f = 1.1F;
		if(i == 1 && (FM.actor instanceof FW_190A4) && f > 0.875F)
			f = 0.875F;
		FM.CT.setPowerControl(f, i - 1);
		if(i <= FM.EI.engines.length)
			hudPower(f);
	}

	private void setPropControl(float f, int i)
	{
		if(f < 0.0F)
			f = 0.0F;
		if(f > 1.0F)
			f = 1.0F;
		FM.CT.setStepControl(f, i - 1);
	}

	private void setPropControl(float f)
	{
		if(!World.cur().diffCur.ComplexEManagement)
			return;
		if(f < 0.0F)
			f = 0.0F;
		if(f > 1.0F)
			f = 1.0F;
		lastProp = f;
		if(!FM.EI.isSelectionAllowsAutoProp())
			bPropAuto = false;
		if(!bPropAuto)
		{
			FM.CT.setStepControlAuto(false);
			FM.CT.setStepControl(f);
		}
	}

	private void setMixControl(float f)
	{
		if(!World.cur().diffCur.ComplexEManagement)
			return;
		if(f < 0.0F)
			f = 0.0F;
		if(f > 1.2F)
			f = 1.2F;
		if(FM.EI.getFirstSelected() != null)
		{
			FM.EI.setMix(f);
			f = FM.EI.getFirstSelected().getControlMix();
			FM.CT.setMixControl(f);
			HUD.log(hudLogPowerId, "PropMix", new Object[] {
					new Integer(Math.round(FM.CT.getMixControl() * 100F))
			});
		}
	}

	private void setRadiatorControl(float f)
	{
		if(f < 0.0F)
			f = 0.0F;
		if(f > 1.0F)
			f = 1.0F;
		if(!FM.EI.isSelectionHasControlRadiator())
			return;
		if(FM.CT.getRadiatorControlAuto())
		{
			if(f > 0.8F)
				return;
			FM.CT.setRadiatorControlAuto(false, FM.EI);
			if(World.cur().diffCur.ComplexEManagement)
			{
				FM.CT.setRadiatorControl(f);
				HUD.log(hudLogPowerId, "RadiatorPercentage", new Object[] {
						new Integer(Math.round(FM.CT.getRadiatorControl() * 100F))
				});
			} else
			{
				FM.CT.setRadiatorControl(1.0F);
				HUD.log("RadiatorON");
			}
			return;
		}
		if(World.cur().diffCur.ComplexEManagement)
		{
			if(FM.actor instanceof MOSQUITO)
			{
				if(f > 0.5F)
				{
					FM.CT.setRadiatorControl(1.0F);
					HUD.log("RadiatorON");
				} else
				{
					FM.CT.setRadiatorControl(0.0F);
					HUD.log("RadiatorOFF");
				}
			} else
			{
				FM.CT.setRadiatorControl(f);
				HUD.log(hudLogPowerId, "RadiatorPercentage", new Object[] {
						new Integer(Math.round(FM.CT.getRadiatorControl() * 100F))
				});
			}
		} else
		{
			FM.CT.setRadiatorControlAuto(true, FM.EI);
			HUD.log("RadiatorOFF");
		}
	}

	private void hudPower(float f)
	{
		HUD.log(hudLogPowerId, "Power", new Object[] {
				new Integer(Math.round(f * 100F))
		});
	}

	private void hudWeapon(boolean flag, int i)
	{
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
		if(abulletemitter == null)
			return;
		int j = 0;
		do
		{
			if(j >= abulletemitter.length)
				break;
			if(abulletemitter[j] != null && abulletemitter[j].haveBullets())
			{
				flag1 = true;
				break;
			}
			j++;
		} while(true);
		if(!flag)
		{
			ForceFeedback.fxTriggerShake(i, false);
			return;
		}
		if(flag1)
			ForceFeedback.fxTriggerShake(i, true);
		else
			HUD.log(hudLogWeaponId, "OutOfAmmo");
	}

	private void doCmdPilot(int i, boolean flag)
	{
		if(!setBombAimerAircraft())
			return;
		if(flag)
			switch(i)
			{
			case 139: 
			case 140: 
			case 149: 
			case 150: 
				doCmdPilotTick(i);
				return;

			case 157: 
				bAAircraft.auxPressed(1);
				return;

			case 158: 
				bAAircraft.auxPressed(2);
				return;
			}
		if(!setPilot())
			return;
		Aircraft aircraft = (Aircraft)FM.actor;
		switch(i)
		{
		case 16: // '\020'
			FM.CT.WeaponControl[0] = flag;
			hudWeapon(flag, 0);
			break;

		case 17: // '\021'
			FM.CT.WeaponControl[1] = flag;
			hudWeapon(flag, 1);
			break;
		case 18:
			//TODO: Rocket hotkey modified
			FM.CT.WeaponControl[FM.CT.rocketHookSelected] = flag;
			hudWeapon(flag, FM.CT.rocketHookSelected);
			break;
		case 19:
			FM.CT.WeaponControl[3] = flag;
			hudWeapon(flag, 3);
			if ((aircraft instanceof TypeHasToKG) && FM.CT.Weapons[3] != null && (FM.CT.Weapons[3][0] instanceof TorpedoGun) && FM.CT.Weapons[3][0].haveBullets())
			{
				FM.AS.replicateGyroAngleToNet();
				FM.AS.replicateSpreadAngleToNet();
			}

			break;
		case 65:
			//TODO: Flare Hotkey
			FM.CT.WeaponControl[7] = flag;
			hudWeapon(flag, 7);
			break;
		case 66:
			//TODO: Chaff Hotkey
			FM.CT.WeaponControl[8] = flag;
			hudWeapon(flag, 8);
			break;

		case 64: // '@'
			FM.CT.WeaponControl[0] = flag;
			hudWeapon(flag, 0);
			FM.CT.WeaponControl[1] = flag;
			hudWeapon(flag, 1);
			break;

		case 73: // 'I'
			FM.CT.setElectricPropUp(flag);
			break;

		case 74: // 'J'
			FM.CT.setElectricPropDn(flag);
			break;
		}
		if(!flag)
		{
			switch(i)
			{
			default:
				break;

			case 71: // 'G'
				if((aircraft instanceof TypeBomber) || (aircraft instanceof DO_335))
				{
					FM.CT.StabilizerControl = !FM.CT.StabilizerControl;
					HUD.log("Stabilizer" + (FM.CT.StabilizerControl ? "On" : "Off"));
				}
				return;

			case 15: // '\017'
				if(!aircraft.isGunPodsExist())
					return;
				if(aircraft.isGunPodsOn())
				{
					aircraft.setGunPodsOn(false);
					HUD.log("GunPodsOff");
				} else
				{
					aircraft.setGunPodsOn(true);
					HUD.log("GunPodsOn");
				}
				return;

			case 1: // '\001'
			case 2: // '\002'
				if(!FM.CT.StabilizerControl)
					FM.CT.RudderControl = 0.0F;
				break;

			case 0: // '\0'
				FM.CT.BrakeControl = 0.0F;
				break;
				//TODO: PAS++
			case 144: // '\0'
				FM.CT.BrakeRightControl = 0.0F;
				break;

			case 145: // '\0'
				FM.CT.BrakeLeftControl = 0.0F;
				break;
				//PAS--
			case 3: // '\003'
			case 4: // '\004'
				if(!FM.CT.StabilizerControl)
					FM.CT.ElevatorControl = 0.0F;
				break;

			case 5: // '\005'
			case 6: // '\006'
				if(!FM.CT.StabilizerControl)
					FM.CT.AileronControl = 0.0F;
				break;

			case 54: // '6'
				if(FM.Gears.onGround() || FM.CT.GearControl <= 0.0F || !FM.Gears.isOperable() || FM.Gears.isHydroOperable())
					break;
				FM.CT.GearControl -= 0.02F;
				if(FM.CT.GearControl <= 0.0F)
				{
					FM.CT.GearControl = 0.0F;
					HUD.log("GearUp");
				}
				break;

			case 55: // '7'
				if(FM.Gears.onGround() || FM.CT.GearControl >= 1.0F || !FM.Gears.isOperable() || FM.Gears.isHydroOperable())
					break;
				FM.CT.GearControl += 0.02F;
				if(FM.CT.GearControl >= 1.0F)
				{
					FM.CT.GearControl = 1.0F;
					HUD.log("GearDown");
				}
				break;

			case 63: // '?'
				if(FM.CT.bHasAirBrakeControl)
				{
					FM.CT.AirBrakeControl = FM.CT.AirBrakeControl <= 0.5F ? 1.0F : 0.0F;
					HUD.log("Divebrake" + (FM.CT.AirBrakeControl != 0.0F ? "ON" : "OFF"));
				}
				break;
				//TODO:Drag chute control
			case 143:
				if (FM.CT.bHasDragChuteControl)
				{
					FM.CT.DragChuteControl = FM.CT.DragChuteControl <= 0.5F ? 1.0F : 0.0F;
					HUD.log("Drag Chute " + (FM.CT.DragChuteControl != 0.0F ? "Deployed" : "Released"));
				}
				break;
			case 136:
				//TODO:Placeholder for 'Deploy Refuelling Device'
				if (FM.CT.bHasRefuelControl)
				{
					FM.CT.RefuelControl = FM.CT.RefuelControl <= 0.5F ? 1.0F : 0.0F;
					HUD.log("Refuel" + (FM.CT.RefuelControl != 0.0F ? "ON" : "OFF"));
				}
				break;
			case 70: // 'F'
				if(World.cur().diffCur.SeparateEStart && FM.EI.getNumSelected() > 1 && FM.EI.getFirstSelected().getStage() == 0)
					return;
				FM.EI.toggle();
				break;

			case 126: // '~'
				if(!(FM.actor instanceof TypeDockable))
					break;
				if(((TypeDockable)FM.actor).typeDockableIsDocked())
					((TypeDockable)FM.actor).typeDockableAttemptDetach();
				else
					((TypeDockable)FM.actor).typeDockableAttemptAttach();
				break;
			}
			return;
		}
		label0:
			switch(i)
			{
			case 8: // '\b'
			case 10: // '\n'
			case 11: // '\013'
			case 12: // '\f'
			case 13: // '\r'
			case 14: // '\016'
			case 15: // '\017'
			case 16: // '\020'
			case 17: // '\021'
			case 18: // '\022'
			case 19: // '\023'
			case 63: // '?'
			case 64: // '@'
			case 65: // 'A'
			case 66: // 'B'
			case 67: // 'C'
			case 68: // 'D'
			case 69: // 'E'
			case 70: // 'F'
			case 71: // 'G'
			case 111: // 'o'
			case 112: // 'p'
			case 126: // '~'
			default:
				break;

				//TODO:
			case 7:
				if (bSeparateRadiatorOpenClose)
				{
					if (FM.EI.isSelectionHasControlRadiator())
					{
						if (FM.CT.getRadiatorControlAuto())
						{
							FM.CT.setRadiatorControlAuto(false, FM.EI);
							if (World.cur().diffCur.ComplexEManagement)
							{
								FM.CT.setRadiatorControl(0.0F);
								HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
							}
							else
							{
								FM.CT.setRadiatorControl(1.0F);
								HUD.log("RadiatorON");
							}
						}
						else if (World.cur().diffCur.ComplexEManagement)
						{
							if (FM.CT.getRadiatorControl() != 1.0F)
							{
								if (FM.actor instanceof MOSQUITO)
									FM.CT.setRadiatorControl(1.0F);
								else
									FM.CT.setRadiatorControl(FM.CT.getRadiatorControl() + 0.2F);
								HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
							}
						}
						else
						{
							FM.CT.setRadiatorControlAuto(true, FM.EI);
							HUD.log("RadiatorOFF");
						}
					}
				}
				else if (FM.EI.isSelectionHasControlRadiator())
				{
					if (FM.CT.getRadiatorControlAuto())
					{
						FM.CT.setRadiatorControlAuto(false, FM.EI);
						if (World.cur().diffCur.ComplexEManagement)
						{
							FM.CT.setRadiatorControl(0.0F);
							HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
						}
						else
						{
							FM.CT.setRadiatorControl(1.0F);
							HUD.log("RadiatorON");
						}
					}
					else if (World.cur().diffCur.ComplexEManagement)
					{
						if (FM.CT.getRadiatorControl() == 1.0F)
						{
							if (FM.EI.isSelectionAllowsAutoRadiator())
							{
								FM.CT.setRadiatorControlAuto(true, FM.EI);
								HUD.log("RadiatorOFF");
							}
							else
							{
								FM.CT.setRadiatorControl(0.0F);
								HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
							}
						}
						else
						{
							if (FM.actor instanceof MOSQUITO)
								FM.CT.setRadiatorControl(1.0F);
							else
								FM.CT.setRadiatorControl(FM.CT.getRadiatorControl() + 0.2F);
							HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
						}
					}
					else
					{
						FM.CT.setRadiatorControlAuto(true, FM.EI);
						HUD.log("RadiatorOFF");
					}
				}
				break;

			case 0: // '\0'
				if(FM.CT.bHasBrakeControl)
					FM.CT.BrakeControl = 1.0F;
				break;
				//TODO: PAS++
			case 144: // '\0'
				if(FM.CT.bHasBrakeControl)
					FM.CT.BrakeRightControl = 1.0F;
				break;

			case 145: // '\0'
				if(FM.CT.bHasBrakeControl)
					FM.CT.BrakeLeftControl = 1.0F;
				break;
				//PAS--
			case 3: // '\003'
				if(!FM.CT.StabilizerControl)
					FM.CT.ElevatorControl = -1F;
				break;

			case 4: // '\004'
				if(!FM.CT.StabilizerControl)
					FM.CT.ElevatorControl = 1.0F;
				break;

			case 5: // '\005'
				if(!FM.CT.StabilizerControl)
					FM.CT.AileronControl = -1F;
				break;

			case 6: // '\006'
				if(!FM.CT.StabilizerControl)
					FM.CT.AileronControl = 1.0F;
				break;

			case 72: // 'H'
				if(FM.CT.bHasLockGearControl)
				{
					FM.Gears.bTailwheelLocked = !FM.Gears.bTailwheelLocked;
					HUD.log("TailwheelLock" + (FM.Gears.bTailwheelLocked ? "ON" : "OFF"));
				}
				break;

			case 1: // '\001'
				if(!FM.CT.StabilizerControl)
					FM.CT.RudderControl = -1F;
				break;

			case 56: // '8'
				if(!FM.CT.StabilizerControl && FM.CT.RudderControl > -1F)
					FM.CT.RudderControl -= 0.1F;
				break;

			case 57: // '9'
				if(!FM.CT.StabilizerControl)
					FM.CT.RudderControl = 0.0F;
				break;

			case 2: // '\002'
				if(!FM.CT.StabilizerControl)
					FM.CT.RudderControl = 1.0F;
				break;

			case 58: // ':'
				if(!FM.CT.StabilizerControl && FM.CT.RudderControl < 1.0F)
					FM.CT.RudderControl += 0.1F;
				break;

			case 20: // '\024'
				setPowerControl(0.0F);
				break;

			case 21: // '\025'
				setPowerControl(0.1F);
				break;

			case 22: // '\026'
				setPowerControl(0.2F);
				break;

				//TODO:
			case 23:
				if (bSeparateRadiatorOpenClose)
				{
					if (FM.EI.isSelectionHasControlRadiator() && !FM.CT.getRadiatorControlAuto())
					{
						if (World.cur().diffCur.ComplexEManagement)
						{
							if (FM.CT.getRadiatorControl() == 0.0F)
							{
								if (FM.EI.isSelectionAllowsAutoRadiator())
								{
									FM.CT.setRadiatorControlAuto(true, FM.EI);
									HUD.log("RadiatorOFF");
								}
							}
							else
							{
								if (FM.actor instanceof MOSQUITO)
									FM.CT.setRadiatorControl(0.0F);
								else
									FM.CT.setRadiatorControl(FM.CT.getRadiatorControl() - 0.2F);
								HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
							}
						}
						else
						{
							FM.CT.setRadiatorControlAuto(true, FM.EI);
							HUD.log("RadiatorOFF");
						}
					}
				}
				else
					setPowerControl(0.3F);
				break;
			case 24: // '\030'
				setPowerControl(0.4F);
				break;

			case 25: // '\031'
				setPowerControl(0.5F);
				break;

			case 26: // '\032'
				setPowerControl(0.6F);
				break;

			case 27: // '\033'
				setPowerControl(0.7F);
				break;

			case 28: // '\034'
				setPowerControl(0.8F);
				break;

			case 29: // '\035'
				setPowerControl(0.9F);
				break;

			case 30: // '\036'
				setPowerControl(1.0F);
				break;
				//TODO: Bomb Bay Door
			case 131: // '\025'
				if(bBombBayDoors)
				{
					if(!FM.CT.bHasBayDoorControl)
						break;
					if(((FlightModelMain) (FM)).CT.BayDoorControl != 0.0F)
					{
						((FlightModelMain) (FM)).CT.BayDoorControl = 0.0F;
						HUD.log("BayDoorsClosed");
					} else
					{
						((FlightModelMain) (FM)).CT.BayDoorControl = 1.0F;
						HUD.log("BayDoorsOpen");
					}
				}
				break;
			case 132: // '\026'
				if (bAllowDumpFuel)
				{
					//TODO: Disabled for ModAct compatibility

					if (aircraft instanceof TypeFuelDump)
					{
						if (bDumpFuel){
							bDumpFuel = false;
							HUD.log(hudLogWeaponId, "Fuel Valve Closed");
						}
						else {
							bDumpFuel = true;
							HUD.log(hudLogWeaponId, "Fuel Valve Open");
						}
						FM.AS.setDumpFuelState(bDumpFuel);

					}
				}
				break;
			case 133:
				if (bToggleMusic)
				{
					if (bMusicOn)
					{
						CmdMusic.setCurrentVolume(0.0F);
						bMusicOn = false;
					}
					else
					{
						CmdMusic.setCurrentVolume(1.0F);
						bMusicOn = true;
					}
				}
				break;
			case 134:
				if (bSideDoor)
				{
					boolean bool_14_ = false;
					try
					{
						//TODO: Disabled for ModAct compatibility
						if ((Aircraft)FM.actor instanceof SPITFIRE)
							bool_14_ = true;
					}
					catch (Throwable throwable)
					{
						/* empty */
					}
					if (bool_14_)
					{
						FM.CT.setActiveDoor(SIDE_DOOR);
						if (FM.CT.cockpitDoorControl < 0.5F && FM.CT.getCockpitDoor() < 0.01F)
							FM.AS.setCockpitDoor(aircraft, 1);
						else if (FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.99F)
							FM.AS.setCockpitDoor(aircraft, 0);
					}
				}
				break;
			case 135: 
				// TODO: ++ Added Code for Net Replication ++
				// Shifted this case part to Controls class where it belongs
				if (FM.CT.toggleRocketHook()) HUD.log(hudLogWeaponId, FM.CT.rocketNameSelected + " Selected");
				// Function call to net replication code, see AircraftState class
				FM.AS.replicateRocketHookToNet(FM.CT.rocketHookSelected);
				// TODO: -- Added Code for Net Replication --
				break;

			case 137: 
				// TODO: ++ Added Code for Net Replication ++
				// Shifted this case part to Controls class where it belongs
				FM.CT.toggleWeaponFireMode(hudLogWeaponId);
				// Function call to net replication code, see AircraftState class
				FM.AS.replicateWeaponFireModeToNet(FM.CT.weaponFireMode);
				// TODO: -- Added Code for Net Replication --
				break;

			case 146: 
				// TODO: ++ Added Code for Net Replication ++
				// Shifted this case part to Controls class where it belongs
				FM.CT.toggleWeaponReleaseDelay(hudLogWeaponId);
				// Function call to net replication code, see AircraftState class
				FM.AS.replicateWeaponReleaseDelay(FM.CT.weaponReleaseDelay);
				// TODO: -- Added Code for Net Replication --
				break;
			case 138:
				//TODO:Placeholder for 'Toggle Radar Mode'
				if(aircraft instanceof TypeRadar)
				{
					((TypeRadar) aircraft).typeRadarToggleMode();
					toTrackSign(i);
					break;
				}

			case 139:
				//TODO:Placeholder for 'Radar Setting #1'
				if(aircraft instanceof TypeRadar)
				{
					((TypeRadar) aircraft).typeRadarRangePlus();
					toTrackSign(i);
					break;
				}
			case 140:
				//TODO:Placeholder for 'Radar Setting #2'
				if(aircraft instanceof TypeRadar)
				{
					((TypeRadar) aircraft).typeRadarRangeMinus();
					toTrackSign(i);
					break;
				}
				break;
			case 141:
				//TODO:Placeholder for 'Radar Setting #3'
				if(aircraft instanceof TypeRadar)
				{
					((TypeRadar) aircraft).typeRadarGainPlus();
					toTrackSign(i);
					break;
				}
				break;
			case 142:
				//TODO:Placeholder for 'Radar Setting #4'
				if(aircraft instanceof TypeRadar)
				{
					((TypeRadar) aircraft).typeRadarGainMinus();
					toTrackSign(i);
					break;
				}
				break;
			case 59: // ';'
				setPowerControl(FM.CT.PowerControl + 0.05F);
				break;

			case 60: // '<'
				setPowerControl(FM.CT.PowerControl - 0.05F);
				break;

			case 61: // '='
				setAfterburner(!bAfterburner);
				break;

			case 31: // '\037'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.0F);
				break;

			case 32: // ' '
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.1F);
				break;

			case 33: // '!'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.2F);
				break;

			case 34: // '"'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.3F);
				break;

			case 35: // '#'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.4F);
				break;

			case 36: // '$'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.5F);
				break;

			case 37: // '%'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.6F);
				break;

			case 38: // '&'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.7F);
				break;

			case 39: // '\''
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.8F);
				break;

			case 40: // '('
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(0.9F);
				break;

			case 41: // ')'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(1.0F);
				break;

			case 73: // 'I'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(lastProp + 0.05F);
				break;

			case 74: // 'J'
				if(FM.EI.isSelectionHasControlProp())
					setPropControl(lastProp - 0.05F);
				break;

			case 42: // '*'
				if(!FM.EI.isSelectionHasControlProp() || !World.cur().diffCur.ComplexEManagement)
					break;
				setPropAuto(!bPropAuto);
				if(bPropAuto)
				{
					HUD.log("PropAutoPitch");
					lastProp = FM.CT.getStepControl();
					FM.CT.setStepControlAuto(true);
				} else
				{
					FM.CT.setStepControlAuto(false);
					setPropControl(lastProp);
				}
				break;

			case 114:
				if (FM.EI.isSelectionHasControlFeather() && World.cur().diffCur.ComplexEManagement && FM.EI.getFirstSelected() != null)
					FM.EI.setFeather(FM.EI.getFirstSelected().getControlFeather() != 0 ? 0 : 1);
				break;

			case 75: // 'K'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.0F);
				break;

			case 76: // 'L'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.1F);
				break;

			case 77: // 'M'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.2F);
				break;

			case 78: // 'N'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.3F);
				break;

			case 79: // 'O'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.4F);
				break;

			case 80: // 'P'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.5F);
				break;

			case 81: // 'Q'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.6F);
				break;

			case 82: // 'R'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.7F);
				break;

			case 83: // 'S'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.8F);
				break;

			case 84: // 'T'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(0.9F);
				break;

			case 85: // 'U'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(1.0F);
				break;

			case 86: // 'V'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(FM.CT.getMixControl() + 0.2F);
				break;

			case 87: // 'W'
				if(FM.EI.isSelectionHasControlMix())
					setMixControl(FM.CT.getMixControl() - 0.2F);
				break;

				//TODO:
			case 89:
				if (!bSeparateGearUpDown)
				{
					if (FM.EI.isSelectionHasControlMagnetos() && FM.EI.getFirstSelected() != null && (FM.EI.getFirstSelected().getControlMagnetos() > 0))
					{
						FM.CT.setMagnetoControl(FM.EI.getFirstSelected().getControlMagnetos() - 1);
						HUD.log("MagnetoSetup" + FM.CT.getMagnetoControl());
					}
				}
				else if (FM.CT.bHasGearControl && !FM.Gears.onGround() && FM.Gears.isHydroOperable())
				{
					if (FM.CT.GearControl > 0.5F && FM.CT.getGear() > 0.99F)
					{
						FM.CT.GearControl = 0.0F;
						HUD.log("GearUp");
					}
					if (FM.Gears.isAnyDamaged())
						HUD.log("GearDamaged");
				}
				break;
			case 88:
				if (!bSeparateHookUpDown)
				{
					if (FM.EI.isSelectionHasControlMagnetos() && FM.EI.getFirstSelected() != null && (FM.EI.getFirstSelected().getControlMagnetos() < 3))
					{
						FM.CT.setMagnetoControl(FM.EI.getFirstSelected().getControlMagnetos() + 1);
						HUD.log("MagnetoSetup" + FM.CT.getMagnetoControl());
					}
				}
				else if (FM.CT.bHasArrestorControl && FM.CT.arrestorControl > 0.5F)
				{
					FM.AS.setArrestor(FM.actor, 0);
					HUD.log("HookUp");
				}
				break;
			case 116: // 't'
				if(FM.EI.isSelectionHasControlCompressor() && FM.EI.getFirstSelected() != null && World.cur().diffCur.ComplexEManagement)
				{
					FM.CT.setCompressorControl(FM.EI.getFirstSelected().getControlCompressor() - 1);
					HUD.log("CompressorSetup" + FM.CT.getCompressorControl());
				}
				break;

			case 115: // 's'
				if(FM.EI.isSelectionHasControlCompressor() && FM.EI.getFirstSelected() != null && World.cur().diffCur.ComplexEManagement)
				{
					FM.CT.setCompressorControl(FM.EI.getFirstSelected().getControlCompressor() + 1);
					HUD.log("CompressorSetup" + FM.CT.getCompressorControl());
				}
				break;

			case 90: // 'Z'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControlAll(true);
				HUD.log("EngineSelectAll");
				break;

			case 91: // '['
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControlAll(false);
				HUD.log("EngineSelectNone");
				break;

			case 92: // '\\'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControlAll(false);
				int ai[] = FM.EI.getSublist(FM.Scheme, 1);
				for(int l = 0; l < ai.length; l++)
					FM.EI.setCurControl(ai[l], true);

				HUD.log("EngineSelectLeft");
				break;

			case 93: // ']'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControlAll(false);
				int ai1[] = FM.EI.getSublist(FM.Scheme, 2);
				for(int i1 = 0; i1 < ai1.length; i1++)
					FM.EI.setCurControl(ai1[i1], true);

				HUD.log("EngineSelectRight");
				break;

			case 94: // '^'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControlAll(false);
				FM.EI.setCurControl(0, true);
				HUD.log("EngineSelect1");
				break;

			case 95: // '_'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControlAll(false);
				FM.EI.setCurControl(1, true);
				HUD.log("EngineSelect2");
				break;

			case 96: // '`'
				if(FM.Scheme == 0 || FM.Scheme == 1 || FM.EI.getNum() < 3)
					return;
				FM.EI.setCurControlAll(false);
				FM.EI.setCurControl(2, true);
				HUD.log("EngineSelect3");
				break;

			case 97: // 'a'
				if(FM.Scheme == 0 || FM.Scheme == 1 || FM.EI.getNum() < 4)
					return;
				FM.EI.setCurControlAll(false);
				FM.EI.setCurControl(3, true);
				HUD.log("EngineSelect4");
				break;

			case 98: // 'b'
				if(FM.Scheme == 0 || FM.Scheme == 1 || FM.EI.getNum() < 5)
					return;
				FM.EI.setCurControlAll(false);
				FM.EI.setCurControl(4, true);
				HUD.log("EngineSelect5");
				break;

			case 99: // 'c'
				if(FM.Scheme == 0 || FM.Scheme == 1 || FM.EI.getNum() < 6)
					return;
				FM.EI.setCurControlAll(false);
				FM.EI.setCurControl(5, true);
				HUD.log("EngineSelect6");
				break;
				//TODO: Next two cases added in to allow for toggling of engines 7 and 8
			case 100:
				if (FM.Scheme != 0 && FM.Scheme != 1 && FM.EI.getNum() >= 7)
				{
					FM.EI.setCurControlAll(false);
					FM.EI.setCurControl(6, true);
					HUD.log("EngineSelect7");
					break;
				}
				break;
			case 101:
				if (FM.Scheme != 0 && FM.Scheme != 1 && FM.EI.getNum() >= 8)
				{
					FM.EI.setCurControlAll(false);
					FM.EI.setCurControl(7, true);
					HUD.log("EngineSelect8");
					break;
				}
				break;
			case 102: // 'f'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				for(int j = 0; j < FM.EI.getNum(); j++)
					FM.EI.setCurControl(j, !FM.EI.getCurControl(j));

				HUD.log("EngineToggleAll");
				break;

			case 103: // 'g'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				int ai2[] = FM.EI.getSublist(FM.Scheme, 1);
				for(int j1 = 0; j1 < ai2.length; j1++)
					FM.EI.setCurControl(ai2[j1], !FM.EI.getCurControl(ai2[j1]));

				HUD.log("EngineToggleLeft");
				break;

			case 104: // 'h'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				int ai3[] = FM.EI.getSublist(FM.Scheme, 2);
				for(int k1 = 0; k1 < ai3.length; k1++)
					FM.EI.setCurControl(ai3[k1], !FM.EI.getCurControl(ai3[k1]));

				HUD.log("EngineToggleRight");
				break;

			case 105: // 'i'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControl(0, !FM.EI.getCurControl(0));
				HUD.log("EngineSelect1" + (FM.EI.getCurControl(0) ? "" : "OFF"));
				break;

			case 106: // 'j'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControl(1, !FM.EI.getCurControl(1));
				HUD.log("EngineSelect2" + (FM.EI.getCurControl(1) ? "" : "OFF"));
				break;

			case 107: // 'k'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControl(2, !FM.EI.getCurControl(2));
				HUD.log("EngineSelect3" + (FM.EI.getCurControl(2) ? "" : "OFF"));
				break;

			case 108: // 'l'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControl(3, !FM.EI.getCurControl(3));
				HUD.log("EngineSelect4" + (FM.EI.getCurControl(3) ? "" : "OFF"));
				break;

			case 109: // 'm'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControl(4, !FM.EI.getCurControl(4));
				HUD.log("EngineSelect5" + (FM.EI.getCurControl(4) ? "" : "OFF"));
				break;

			case 110: // 'n'
				if(FM.Scheme == 0 || FM.Scheme == 1)
					return;
				FM.EI.setCurControl(5, !FM.EI.getCurControl(5));
				HUD.log("EngineSelect6" + (FM.EI.getCurControl(5) ? "" : "OFF"));
				break;

			case 113: // 'q'
				if(!FM.EI.isSelectionHasControlExtinguisher())
					break;
				int k = 0;
				do
				{
					if(k >= FM.EI.getNum())
						break label0;
					if(FM.EI.getCurControl(k))
						FM.EI.engines[k].setExtinguisherFire();
					k++;
				} while(true);

				//TODO: Added flapIndex integer to following two keys to allow for custom flap settings
			case 53: // '5'
				if(FM.CT.bHasFlapsControl){
					SetFlapsHotKeys(1, FM);
					if(FM.CT.FlapStageMax != -1.0F && flapIndex < (FM.CT.nFlapStages-1))
						flapIndex++;
				}
				break;
			case 52: // '4'
				if(FM.CT.bHasFlapsControl){
					SetFlapsHotKeys(0, FM);
					if(FM.CT.FlapStageMax != -1.0F && flapIndex > 0)
						flapIndex--;
				}
				break;
				//TODO: New hotkeys for variable geometry/incidence wings
			case 54: // '5'
				if(FM.CT.bHasVarWingControl){
					SetVarWingHotKeys(0, FM);
					if(varWingIndex > 0)
						varWingIndex--;
				}
				break;
			case 55: // '4'
				if(FM.CT.bHasVarWingControl){
					SetVarWingHotKeys(1, FM);
					if(varWingIndex < (FM.CT.nVarWingStages-1))
						varWingIndex++;
				}
				break;
				//TODO: Blown Flaps
			case 160: 
				if(((FlightModelMain) (FM)).CT.BlownFlapsType != null && ((FlightModelMain) (FM)).CT.bHasBlownFlaps && ((FlightModelMain) (FM)).CT.FlapsControl > 0.0F)
				{
					((FlightModelMain) (FM)).CT.BlownFlapsControl = ((FlightModelMain) (FM)).CT.BlownFlapsControl > 0.5F ? 0.0F : 1.0F;
					HUD.log(((FlightModelMain) (FM)).CT.BlownFlapsType + (((FlightModelMain) (FM)).CT.BlownFlapsControl == 0.0F ? "OFF" : "ON"));
				}
				break;
			case 9: // '\t'
				if(!FM.CT.bHasGearControl || FM.Gears.onGround() || !FM.Gears.isHydroOperable())
					break;
				if(FM.CT.GearControl > 0.5F && FM.CT.getGear() > 0.99F)
				{
					FM.CT.GearControl = 0.0F;
					HUD.log("GearUp");
				} else
					if(FM.CT.GearControl < 0.5F && FM.CT.getGear() < 0.01F)
					{
						FM.CT.GearControl = 1.0F;
						HUD.log("GearDown");
					}
				if(FM.Gears.isAnyDamaged())
					HUD.log("GearDamaged");
				break;

			case 129: 
				if(!FM.CT.bHasArrestorControl)
					break;
				if(FM.CT.arrestorControl > 0.5F)
				{
					FM.AS.setArrestor(FM.actor, 0);
					HUD.log("HookUp");
				} else
				{
					FM.AS.setArrestor(FM.actor, 1);
					HUD.log("HookDown");
				}
				break;

			case 130: 
				if(!FM.canChangeBrakeShoe || (FM.actor instanceof TypeSailPlane) || (FM.actor instanceof HE_LERCHE3))
					break;
				if(FM.brakeShoe)
				{
					FM.brakeShoe = false;
					HUD.log("BrakeShoeOff");
				} else
				{
					FM.brakeShoe = true;
					HUD.log("BrakeShoeOn");
				}
				break;

			case 127: // '\177'
				if(!FM.CT.bHasWingControl)
					break;
				if(FM.CT.wingControl < 0.5F && FM.CT.getWing() < 0.01F)
				{
					FM.AS.setWingFold(aircraft, 1);
					HUD.log("WingFold");
					break;
				}
				if(FM.CT.wingControl > 0.5F && FM.CT.getWing() > 0.99F)
				{
					FM.AS.setWingFold(aircraft, 0);
					HUD.log("WingExpand");
				}
				break;

			case 128:
				if (FM.CT.bHasCockpitDoorControl)
				{
					if (bSideDoor)
					{
						boolean bool_21_ = false;
						try
						{
							//TODO: Disabled for ModAct compatibility
							if ((Aircraft)FM.actor instanceof SPITFIRE)
								bool_21_ = true;
						}
						catch (Throwable throwable)
						{
							/* empty */
						}
						if (bool_21_)
							FM.CT.setActiveDoor(COCKPIT_DOOR);
					}
					if (FM.CT.cockpitDoorControl < 0.5F && FM.CT.getCockpitDoor() < 0.01F)
					{
						FM.AS.setCockpitDoor(aircraft, 1);
						HUD.log("CockpitDoorOPN");
					}
					else if (FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.99F)
					{
						FM.AS.setCockpitDoor(aircraft, 0);
						HUD.log("CockpitDoorCLS");
					}
				}
				break;

			case 43: // '+'
				if(FM.CT.bHasElevatorTrim)
					FM.CT.setTrimElevatorControl(0.0F);
				break;

			case 44: // ','
				doCmdPilotTick(i);
				break;

			case 45: // '-'
				doCmdPilotTick(i);
				break;

			case 46: // '.'
				if(FM.CT.bHasAileronTrim)
					FM.CT.setTrimAileronControl(0.0F);
				break;

			case 47: // '/'
				doCmdPilotTick(i);
				break;

			case 48: // '0'
				doCmdPilotTick(i);
				break;

			case 49: // '1'
				if(FM.CT.bHasRudderTrim)
					FM.CT.setTrimRudderControl(0.0F);
				break;

			case 50: // '2'
				doCmdPilotTick(i);
				break;

			case 51: // '3'
				doCmdPilotTick(i);
				break;

			case 125: // '}'
				if(aircraft instanceof TypeBomber)
				{
					((TypeBomber)aircraft).typeBomberToggleAutomation();
					toTrackSign(i);
					break;
				}
				if(aircraft instanceof TypeDiveBomber)
				{
					((TypeDiveBomber)aircraft).typeDiveBomberToggleAutomation();
					toTrackSign(i);
					break;
				}
				if(aircraft instanceof TypeFighterAceMaker)
				{
					((TypeFighterAceMaker)aircraft).typeFighterAceMakerToggleAutomation();
					toTrackSign(i);
				}
				break;

			case 117: // 'u'
				doCmdPilotTick(i);
				break;

			case 118: // 'v'
				doCmdPilotTick(i);
				break;

			case 119: // 'w'
				doCmdPilotTick(i);
				break;

			case 120: // 'x'
				doCmdPilotTick(i);
				break;

			case 121: // 'y'
				doCmdPilotTick(i);
				break;

			case 122: // 'z'
				doCmdPilotTick(i);
				break;

			case 123: // '{'
				doCmdPilotTick(i);
				break;

			case 124: // '|'
				doCmdPilotTick(i);
				break;

			case 62: // '>'
				FM.CT.dropFuelTanks();
				break;
			}
	}

	private void doCmdPilotTick(int i)
	{
		if(!setBombAimerAircraft())
			return;
		switch(i)
		{
		case 149: 
			bAAircraft.auxPlus(1);
			toTrackSign(i);
			return;

		case 150: 
			bAAircraft.auxMinus(1);
			toTrackSign(i);
			return;

		case 139: 
			if(World.cur().diffCur.RealisticNavigationInstruments)
			{
				bAAircraft.beaconPlus();
				toTrackSign(i);
			}
			return;

		case 140: 
			if(World.cur().diffCur.RealisticNavigationInstruments)
			{
				bAAircraft.beaconMinus();
				toTrackSign(i);
			}
			return;
		}
		if(!setPilot())
			return;
		Aircraft aircraft = (Aircraft)FM.actor;
		switch(i)
		{
		default:
			break;

		case 44: // ','
			if(FM.CT.bHasElevatorTrim && FM.CT.getTrimElevatorControl() < 0.5F)
				FM.CT.setTrimElevatorControl(FM.CT.getTrimElevatorControl() + 0.00625F);
			break;

		case 45: // '-'
			if(FM.CT.bHasElevatorTrim && FM.CT.getTrimElevatorControl() > -0.5F)
				FM.CT.setTrimElevatorControl(FM.CT.getTrimElevatorControl() - 0.00625F);
			break;

		case 47: // '/'
			if(FM.CT.bHasAileronTrim && FM.CT.getTrimAileronControl() < 0.5F)
				FM.CT.setTrimAileronControl(FM.CT.getTrimAileronControl() + 0.00625F);
			break;

		case 48: // '0'
			if(FM.CT.bHasAileronTrim && FM.CT.getTrimAileronControl() > -0.5F)
				FM.CT.setTrimAileronControl(FM.CT.getTrimAileronControl() - 0.00625F);
			break;

		case 50: // '2'
			if(FM.CT.bHasRudderTrim && FM.CT.getTrimRudderControl() < 0.5F)
				FM.CT.setTrimRudderControl(FM.CT.getTrimRudderControl() + 0.00625F);
			break;

		case 51: // '3'
			if(FM.CT.bHasRudderTrim && FM.CT.getTrimRudderControl() > -0.5F)
				FM.CT.setTrimRudderControl(FM.CT.getTrimRudderControl() - 0.00625F);
			break;

		case 117: // 'u'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjDistancePlus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeFighterAceMaker)
			{
				((TypeFighterAceMaker)aircraft).typeFighterAceMakerAdjDistancePlus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeDiveBomber)
			{
				((TypeDiveBomber)aircraft).typeDiveBomberAdjDiveAnglePlus();
				toTrackSign(i);
			}
			break;

		case 118: // 'v'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjDistanceMinus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeFighterAceMaker)
			{
				((TypeFighterAceMaker)aircraft).typeFighterAceMakerAdjDistanceMinus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeDiveBomber)
			{
				((TypeDiveBomber)aircraft).typeDiveBomberAdjDiveAngleMinus();
				toTrackSign(i);
			}
			break;

		case 119: // 'w'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjSideslipPlus();
				toTrackSign(i);
			}
			if(aircraft instanceof TypeX4Carrier)
			{
				((TypeX4Carrier)aircraft).typeX4CAdjSidePlus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeFighterAceMaker)
			{
				((TypeFighterAceMaker)aircraft).typeFighterAceMakerAdjSideslipPlus();
				toTrackSign(i);
			}
			break;

		case 120: // 'x'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjSideslipMinus();
				toTrackSign(i);
			}
			if(aircraft instanceof TypeX4Carrier)
			{
				((TypeX4Carrier)aircraft).typeX4CAdjSideMinus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeFighterAceMaker)
			{
				((TypeFighterAceMaker)aircraft).typeFighterAceMakerAdjSideslipMinus();
				toTrackSign(i);
			}
			break;

		case 121: // 'y'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjAltitudePlus();
				toTrackSign(i);
			} else
				if(aircraft instanceof TypeDiveBomber)
				{
					((TypeDiveBomber)aircraft).typeDiveBomberAdjAltitudePlus();
					toTrackSign(i);
				}
			if(aircraft instanceof TypeX4Carrier)
			{
				((TypeX4Carrier)aircraft).typeX4CAdjAttitudePlus();
				toTrackSign(i);
			}
			break;

		case 122: // 'z'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjAltitudeMinus();
				toTrackSign(i);
			} else
				if(aircraft instanceof TypeDiveBomber)
				{
					((TypeDiveBomber)aircraft).typeDiveBomberAdjAltitudeMinus();
					toTrackSign(i);
				}
			if(aircraft instanceof TypeX4Carrier)
			{
				((TypeX4Carrier)aircraft).typeX4CAdjAttitudeMinus();
				toTrackSign(i);
			}
			break;

		case 123: // '{'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjSpeedPlus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeDiveBomber)
			{
				((TypeDiveBomber)aircraft).typeDiveBomberAdjVelocityPlus();
				toTrackSign(i);
			}
			break;

		case 124: // '|'
			if(aircraft instanceof TypeBomber)
			{
				((TypeBomber)aircraft).typeBomberAdjSpeedMinus();
				toTrackSign(i);
				break;
			}
			if(aircraft instanceof TypeDiveBomber)
			{
				((TypeDiveBomber)aircraft).typeDiveBomberAdjVelocityMinus();
				toTrackSign(i);
			}
			break;
		}
	}

	public void fromTrackSign(NetMsgInput netmsginput)
			throws IOException
			{
		if(!Actor.isAlive(World.getPlayerAircraft()))
			return;
		if(World.isPlayerParatrooper())
			return;
		if(World.isPlayerDead())
			return;
		if(World.getPlayerAircraft() instanceof TypeBomber)
		{
			TypeBomber typebomber = (TypeBomber)World.getPlayerAircraft();
			int i = netmsginput.readUnsignedShort();
			switch(i)
			{
			case 125: // '}'
				typebomber.typeBomberToggleAutomation();
				break;

			case 117: // 'u'
				typebomber.typeBomberAdjDistancePlus();
				break;

			case 118: // 'v'
				typebomber.typeBomberAdjDistanceMinus();
				break;

			case 119: // 'w'
				typebomber.typeBomberAdjSideslipPlus();
				break;

			case 120: // 'x'
				typebomber.typeBomberAdjSideslipMinus();
				break;

			case 121: // 'y'
				typebomber.typeBomberAdjAltitudePlus();
				break;

			case 122: // 'z'
				typebomber.typeBomberAdjAltitudeMinus();
				break;

			case 123: // '{'
				typebomber.typeBomberAdjSpeedPlus();
				break;

			case 124: // '|'
				typebomber.typeBomberAdjSpeedMinus();
				break;

			default:
				return;
			}
		}
		if(World.getPlayerAircraft() instanceof TypeDiveBomber)
		{
			TypeDiveBomber typedivebomber = (TypeDiveBomber)World.getPlayerAircraft();
			int j = netmsginput.readUnsignedShort();
			switch(j)
			{
			case 125: // '}'
				typedivebomber.typeDiveBomberToggleAutomation();
				break;

			case 121: // 'y'
				typedivebomber.typeDiveBomberAdjAltitudePlus();
				break;

			case 122: // 'z'
				typedivebomber.typeDiveBomberAdjAltitudeMinus();
				break;

			case 123: // '{'
			case 124: // '|'
			default:
				return;
			}
		}
		if(World.getPlayerAircraft() instanceof TypeFighterAceMaker)
		{
			TypeFighterAceMaker typefighteracemaker = (TypeFighterAceMaker)World.getPlayerAircraft();
			int k = netmsginput.readUnsignedShort();
			switch(k)
			{
			case 125: // '}'
				typefighteracemaker.typeFighterAceMakerToggleAutomation();
				break;

			case 117: // 'u'
				typefighteracemaker.typeFighterAceMakerAdjDistancePlus();
				break;

			case 118: // 'v'
				typefighteracemaker.typeFighterAceMakerAdjDistanceMinus();
				break;

			case 119: // 'w'
				typefighteracemaker.typeFighterAceMakerAdjSideslipPlus();
				break;

			case 120: // 'x'
				typefighteracemaker.typeFighterAceMakerAdjSideslipMinus();
				break;

			case 121: // 'y'
			case 122: // 'z'
			case 123: // '{'
			case 124: // '|'
			default:
				return;
			}
		}
			}

	private void toTrackSign(int i)
	{
		if(Main3D.cur3D().gameTrackRecord() != null)
			try
		{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(5);
				netmsgguaranted.writeShort(i);
				Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
		}
		catch(Exception exception) { }
	}

	private void doCmdPilotMove(int i, float f)
	{
		if(!setPilot())
			return;
		switch(i)
		{
		case 1: // '\001'
			float f1 = f * 0.55F + 0.55F;
			if(Math.abs(f1 - lastPower) >= 0.01F)
				setPowerControl(f1);
			break;

		case 7: // '\007'
			float f2 = f * 0.5F + 0.5F;
			if(Math.abs(f2 - lastProp) >= 0.02F && FM.EI.isSelectionHasControlProp())
				setPropControl(f2);
			break;

		case 2: // '\002'
			if(!FM.CT.bHasFlapsControl)
				break;
			if(!FM.CT.bHasFlapsControlRed)
			{
				FM.CT.FlapsControl = f * 0.5F + 0.5F;
				break;
			}
			if(f < 0.0F)
			{
				FM.CT.FlapsControl = 0.0F;
				HUD.log("FlapsRaised");
			} else
			{
				FM.CT.FlapsControl = 1.0F;
				HUD.log("FlapsLanding");
			}
			break;

		case 3: // '\003'
			if(!FM.CT.StabilizerControl)
				FM.CT.AileronControl = f;
			break;

		case 4: // '\004'
			if(!FM.CT.StabilizerControl)
				FM.CT.ElevatorControl = f;
			break;

		case 5: // '\005'
			if(!FM.CT.StabilizerControl)
				FM.CT.RudderControl = f;
			break;

		case 6: // '\006'
			if(FM.CT.bHasBrakeControl)
				FM.CT.BrakeControl = f * 0.5F + 0.5F;
			break;

		case 8: // '\b'
			if(FM.CT.bHasAileronTrim)
				FM.CT.setTrimAileronControl(f * 0.5F);
			break;

		case 9: // '\t'
			if(FM.CT.bHasElevatorTrim)
				FM.CT.setTrimElevatorControl(f * 0.5F);
			break;

		case 10: // '\n'
			if(FM.CT.bHasRudderTrim)
				FM.CT.setTrimRudderControl(f * 0.5F);
			break;

		case 100: // 'd'
			if(changeFovEnabled)
			{
				f = (f * 0.5F + 0.5F) * 60F + 30F;
				CmdEnv.top().exec("fov " + f);
			}
			break;

		case 15: // '\017'
			float f3 = f * 0.55F + 0.55F;
			if(Math.abs(f3 - lastPower1) < 0.01F)
				break;
			lastPower1 = f3;
			if(useSmartAxisForPower && FM.EI.engines.length == 3)
			{
				setPowerControl(f3, 1);
				float f8 = (lastPower1 + lastPower2) / 2.0F;
				setPowerControl(f8, 2);
				break;
			}
			if(useSmartAxisForPower && FM.EI.engines.length == 4)
			{
				setPowerControl(f3, 1);
				setPowerControl(f3, 2);
			} else
			{
				setPowerControl(f3, 1);
			}
			break;

		case 16: // '\020'
			float f4 = f * 0.55F + 0.55F;
			if(Math.abs(f4 - lastPower2) < 0.01F)
				break;
			lastPower2 = f4;
			if(useSmartAxisForPower && FM.EI.engines.length == 3)
			{
				setPowerControl(f4, 3);
				float f9 = (lastPower1 + lastPower2) / 2.0F;
				setPowerControl(f9, 2);
				break;
			}
			if(useSmartAxisForPower && FM.EI.engines.length == 4)
			{
				setPowerControl(f4, 3);
				setPowerControl(f4, 4);
			} else
			{
				setPowerControl(f4, 2);
			}
			break;

		case 17: // '\021'
			float f5 = f * 0.55F + 0.55F;
			if(Math.abs(f5 - lastPower3) >= 0.01F)
			{
				lastPower3 = f5;
				setPowerControl(f5, 3);
			}
			break;

		case 18: // '\022'
			float f6 = f * 0.55F + 0.55F;
			if(Math.abs(f6 - lastPower4) >= 0.01F)
			{
				lastPower4 = f6;
				setPowerControl(f6, 4);
			}
			break;

		case 19: // '\023'
			float f7 = f * 0.5F + 0.5F;
			if(Math.abs(f7 - lastRadiator) >= 0.02F)
			{
				lastRadiator = f7;
				setRadiatorControl(f7);
			}
			break;

		case 20: // '\024'
			float f10 = f * 0.5F + 0.5F;
			if(Math.abs(f10 - lastProp1) < 0.02F || 0 >= FM.EI.getNum() || !FM.EI.engines[0].isHasControlProp())
				break;
			if(useSmartAxisForPitch && FM.EI.engines.length == 3)
			{
				setPropControl(f10, 1);
				float f14 = (lastProp1 + lastProp2) / 2.0F;
				setPropControl(f14, 2);
				break;
			}
			if(useSmartAxisForPitch && FM.EI.engines.length == 4)
			{
				setPropControl(f10, 1);
				setPropControl(f10, 2);
			} else
			{
				setPropControl(f10, 1);
			}
			break;

		case 21: // '\025'
			float f11 = f * 0.5F + 0.5F;
			if(Math.abs(f11 - lastProp2) < 0.02F || 1 >= FM.EI.getNum() || !FM.EI.engines[1].isHasControlProp())
				break;
			if(useSmartAxisForPitch && FM.EI.engines.length == 3)
			{
				setPropControl(f11, 3);
				float f15 = (lastProp1 + lastProp2) / 2.0F;
				setPropControl(f15, 2);
				break;
			}
			if(useSmartAxisForPitch && FM.EI.engines.length == 4)
			{
				setPropControl(f11, 3);
				setPropControl(f11, 4);
			} else
			{
				setPropControl(f11, 2);
			}
			break;

		case 22: // '\026'
			float f12 = f * 0.5F + 0.5F;
			if(Math.abs(f12 - lastProp3) >= 0.02F && 2 < FM.EI.getNum() && FM.EI.engines[2].isHasControlProp())
				setPropControl(f12, 3);
			break;

		case 23: // '\027'
			float f13 = f * 0.5F + 0.5F;
			if(Math.abs(f13 - lastProp4) >= 0.02F && 3 < FM.EI.getNum() && FM.EI.engines[3].isHasControlProp())
				setPropControl(f13, 4);
			break;

		case 170: 
			HookPilot.cur().leanForwardMove(f);
			break;

		case 171: 
			HookPilot.cur().leanSideMove(f);
			break;

		case 172: 
			HookPilot.cur().raiseMove(f);
			break;

		default:
			return;
		}
	}

	public void createPilotHotMoves()
	{
		String s = "move";
		HotKeyCmdEnv.setCurrentEnv(s);
		HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey " + s);
		HotKeyCmdEnv hotkeycmdenv = HotKeyCmdEnv.currentEnv();
		HotKeyCmdEnv _tmp = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("01", "power", 1, 1));
		HotKeyCmdEnv _tmp1 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("02", "flaps", 2, 2));
		HotKeyCmdEnv _tmp2 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("03", "aileron", 3, 3));
		HotKeyCmdEnv _tmp3 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("04", "elevator", 4, 4));
		HotKeyCmdEnv _tmp4 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("05", "rudder", 5, 5));
		HotKeyCmdEnv _tmp5 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("06", "brakes", 6, 6));
		HotKeyCmdEnv _tmp6 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("07", "pitch", 7, 7));
		HotKeyCmdEnv _tmp7 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("08", "trimaileron", 8, 8));
		HotKeyCmdEnv _tmp8 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("09", "trimelevator", 9, 9));
		HotKeyCmdEnv _tmp9 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("10", "trimrudder", 10, 10));
		HotKeyCmdEnv _tmp10 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power", 1, 11));
		HotKeyCmdEnv _tmp11 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-flaps", 2, 12));
		HotKeyCmdEnv _tmp12 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-aileron", 3, 13));
		HotKeyCmdEnv _tmp13 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-elevator", 4, 14));
		HotKeyCmdEnv _tmp14 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-rudder", 5, 15));
		HotKeyCmdEnv _tmp15 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-brakes", 6, 16));
		HotKeyCmdEnv _tmp16 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-pitch", 7, 17));
		HotKeyCmdEnv _tmp17 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-trimaileron", 8, 18));
		HotKeyCmdEnv _tmp18 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-trimelevator", 9, 19));
		HotKeyCmdEnv _tmp19 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-trimrudder", 10, 20));
		HotKeyCmdEnv _tmp20 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("11", "zoom", 100, 30, true));
		HotKeyCmdEnv _tmp21 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-zoom", 100, 31, true));
		HotKeyCmdEnv _tmp22 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("15", "power1", 15, 32));
		HotKeyCmdEnv _tmp23 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power1", 15, 23));
		HotKeyCmdEnv _tmp24 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("16", "power2", 16, 34));
		HotKeyCmdEnv _tmp25 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power2", 16, 35));
		HotKeyCmdEnv _tmp26 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("17", "power3", 17, 170));
		HotKeyCmdEnv _tmp27 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power3", 17, 171));
		HotKeyCmdEnv _tmp28 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("18", "power4", 18, 172));
		HotKeyCmdEnv _tmp29 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power4", 18, 174));
		HotKeyCmdEnv _tmp30 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("25", "radiator", 19, 36, true));
		HotKeyCmdEnv _tmp31 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-radiator", 19, 37, true));
		HotKeyCmdEnv _tmp32 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("26", "prop1", 20, 38));
		HotKeyCmdEnv _tmp33 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop1", 20, 39));
		HotKeyCmdEnv _tmp34 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("27", "prop2", 21, 40));
		HotKeyCmdEnv _tmp35 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop2", 21, 41));
		HotKeyCmdEnv _tmp36 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("28", "prop3", 22, 175));
		HotKeyCmdEnv _tmp37 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop3", 22, 176));
		HotKeyCmdEnv _tmp38 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("29", "prop4", 23, 177));
		HotKeyCmdEnv _tmp39 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop4", 23, 178));
		HotKeyCmdEnv _tmp40 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("30", "LeanF", 170, 410, true));
		HotKeyCmdEnv _tmp41 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-LeanF", 170, 413, true));
		HotKeyCmdEnv _tmp42 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("31", "LeanS", 171, 411, true));
		HotKeyCmdEnv _tmp43 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-LeanS", 171, 414, true));
		HotKeyCmdEnv _tmp44 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("32", "Raise", 172, 412, true));
		HotKeyCmdEnv _tmp45 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-Raise", 172, 415, true));
	}

	private void createCommonHotKeys()
	{
		String s = "misc";
		HotKeyCmdEnv.setCurrentEnv(s);
		HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey pilot");
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ18", "BEACON_PLUS", 139, 359));
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ19", "BEACON_MINUS", 140, 360));
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ40", "AUX1_PLUS", 149, 369));
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ41", "AUX1_MINUS", 150, 370));
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ60", "AUX_A", 157, 377));
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ61", "AUX_B", 158, 378));
	}

	public void createPilotHotKeys()
	{
		String s = "pilot";
		HotKeyCmdEnv.setCurrentEnv(s);
		HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey " + s);
		HotKeyCmdEnv hotkeycmdenv = HotKeyCmdEnv.currentEnv();
		HotKeyCmdEnv _tmp = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic01", "ElevatorUp", 3, 103));
		HotKeyCmdEnv _tmp1 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic02", "ElevatorDown", 4, 104));
		HotKeyCmdEnv _tmp2 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic03", "AileronLeft", 5, 105));
		HotKeyCmdEnv _tmp3 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic04", "AileronRight", 6, 106));
		HotKeyCmdEnv _tmp4 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic05", "RudderLeft", 1, 101));
		HotKeyCmdEnv _tmp5 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic06", "RudderRight", 2, 102));
		HotKeyCmdEnv _tmp6 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic07", "Stabilizer", 71, 165));
		HotKeyCmdEnv _tmp7 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic08", "AIRCRAFT_RUDDER_LEFT_1", 56, 156));
		HotKeyCmdEnv _tmp8 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic09", "AIRCRAFT_RUDDER_CENTRE", 57, 157));
		HotKeyCmdEnv _tmp9 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic10", "AIRCRAFT_RUDDER_RIGHT_1", 58, 158));
		HotKeyCmdEnv _tmp10 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic11", "AIRCRAFT_TRIM_V_PLUS", 44, 144));
		HotKeyCmdEnv _tmp11 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic12", "AIRCRAFT_TRIM_V_0", 43, 143));
		HotKeyCmdEnv _tmp12 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic13", "AIRCRAFT_TRIM_V_MINUS", 45, 145));
		HotKeyCmdEnv _tmp13 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic14", "AIRCRAFT_TRIM_H_MINUS", 48, 148));
		HotKeyCmdEnv _tmp14 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic15", "AIRCRAFT_TRIM_H_0", 46, 146));
		HotKeyCmdEnv _tmp15 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic16", "AIRCRAFT_TRIM_H_PLUS", 47, 147));
		HotKeyCmdEnv _tmp16 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic17", "AIRCRAFT_TRIM_R_MINUS", 51, 151));
		HotKeyCmdEnv _tmp17 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic18", "AIRCRAFT_TRIM_R_0", 49, 149));
		HotKeyCmdEnv _tmp18 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic19", "AIRCRAFT_TRIM_R_PLUS", 50, 150));
		HotKeyCmdEnv _tmp19 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$1", "1basic20") {

		}
				);
		hudLogPowerId = HUD.makeIdLog();
		HotKeyCmdEnv _tmp20 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine01", "AIRCRAFT_TOGGLE_ENGINE", 70, 164));
		HotKeyCmdEnv _tmp21 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine02", "AIRCRAFT_POWER_PLUS_5", 59, 159));
		HotKeyCmdEnv _tmp22 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine03", "AIRCRAFT_POWER_MINUS_5", 60, 160));
		HotKeyCmdEnv _tmp23 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine04", "Boost", 61, 161));
		HotKeyCmdEnv _tmp24 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine05", "Power0", 20, 120));
		HotKeyCmdEnv _tmp25 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine06", "Power10", 21, 121));
		HotKeyCmdEnv _tmp26 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine07", "Power20", 22, 122));
		HotKeyCmdEnv _tmp27 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine08", "Power30", 23, 123));
		HotKeyCmdEnv _tmp28 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine09", "Power40", 24, 124));
		HotKeyCmdEnv _tmp29 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine10", "Power50", 25, 125));
		HotKeyCmdEnv _tmp30 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine11", "Power60", 26, 126));
		HotKeyCmdEnv _tmp31 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine12", "Power70", 27, 127));
		HotKeyCmdEnv _tmp32 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine13", "Power80", 28, 128));
		HotKeyCmdEnv _tmp33 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine14", "Power90", 29, 129));
		HotKeyCmdEnv _tmp34 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine15", "Power100", 30, 130));
		HotKeyCmdEnv _tmp35 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$2", "2engine16") {

		}
				);
		HotKeyCmdEnv _tmp36 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine17", "Step0", 31, 131));
		HotKeyCmdEnv _tmp37 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine18", "Step10", 32, 132));
		HotKeyCmdEnv _tmp38 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine19", "Step20", 33, 133));
		HotKeyCmdEnv _tmp39 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine20", "Step30", 34, 134));
		HotKeyCmdEnv _tmp40 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine21", "Step40", 35, 135));
		HotKeyCmdEnv _tmp41 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine22", "Step50", 36, 136));
		HotKeyCmdEnv _tmp42 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine23", "Step60", 37, 137));
		HotKeyCmdEnv _tmp43 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine24", "Step70", 38, 138));
		HotKeyCmdEnv _tmp44 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine25", "Step80", 39, 139));
		HotKeyCmdEnv _tmp45 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine26", "Step90", 40, 140));
		HotKeyCmdEnv _tmp46 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine27", "Step100", 41, 141));
		HotKeyCmdEnv _tmp47 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine28", "StepAuto", 42, 142));
		HotKeyCmdEnv _tmp48 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine29", "StepPlus5", 73, 290));
		HotKeyCmdEnv _tmp49 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine30", "StepMinus5", 74, 291));
		HotKeyCmdEnv _tmp50 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$3", "2engine31") {

		}
				);
		HotKeyCmdEnv _tmp51 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine32", "Mix0", 75, 292));
		HotKeyCmdEnv _tmp52 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine33", "Mix10", 76, 293));
		HotKeyCmdEnv _tmp53 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine34", "Mix20", 77, 294));
		HotKeyCmdEnv _tmp54 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine35", "Mix30", 78, 295));
		HotKeyCmdEnv _tmp55 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine36", "Mix40", 79, 296));
		HotKeyCmdEnv _tmp56 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine37", "Mix50", 80, 297));
		HotKeyCmdEnv _tmp57 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine38", "Mix60", 81, 298));
		HotKeyCmdEnv _tmp58 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine39", "Mix70", 82, 299));
		HotKeyCmdEnv _tmp59 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine40", "Mix80", 83, 300));
		HotKeyCmdEnv _tmp60 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine41", "Mix90", 84, 301));
		HotKeyCmdEnv _tmp61 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine42", "Mix100", 85, 302));
		HotKeyCmdEnv _tmp62 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine43", "MixPlus20", 86, 303));
		HotKeyCmdEnv _tmp63 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine44", "MixMinus20", 87, 304));
		HotKeyCmdEnv _tmp64 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$4", "2engine45") {

		}
				);
		HotKeyCmdEnv _tmp65 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine46", "MagnetoPlus", 88, 305));
		HotKeyCmdEnv _tmp66 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine47", "MagnetoMinus", 89, 306));
		HotKeyCmdEnv _tmp67 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$5", "2engine48") {

		}
				);
		HotKeyCmdEnv _tmp68 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine49", "CompressorPlus", 115, 334));
		HotKeyCmdEnv _tmp69 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine50", "CompressorMinus", 116, 335));
		HotKeyCmdEnv _tmp70 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$6", "2engine51") {

		}
				);
		HotKeyCmdEnv _tmp71 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine52", "EngineSelectAll", 90, 307));
		HotKeyCmdEnv _tmp72 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine53", "EngineSelectNone", 91, 318));
		HotKeyCmdEnv _tmp73 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine54", "EngineSelectLeft", 92, 316));
		HotKeyCmdEnv _tmp74 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine55", "EngineSelectRight", 93, 317));
		HotKeyCmdEnv _tmp75 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine56", "EngineSelect1", 94, 308));
		HotKeyCmdEnv _tmp76 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine57", "EngineSelect2", 95, 309));
		HotKeyCmdEnv _tmp77 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine58", "EngineSelect3", 96, 310));
		HotKeyCmdEnv _tmp78 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine59", "EngineSelect4", 97, 311));
		HotKeyCmdEnv _tmp79 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine60", "EngineSelect5", 98, 312));
		HotKeyCmdEnv _tmp80 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine61", "EngineSelect6", 99, 313));
		HotKeyCmdEnv _tmp81 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine62", "EngineSelect7", 100, 314));
		HotKeyCmdEnv _tmp82 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine63", "EngineSelect8", 101, 315));
		HotKeyCmdEnv _tmp83 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine64", "EngineToggleAll", 102, 319));
		HotKeyCmdEnv _tmp84 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine65", "EngineToggleLeft", 103, 328));
		HotKeyCmdEnv _tmp85 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine66", "EngineToggleRight", 104, 329));
		HotKeyCmdEnv _tmp86 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine67", "EngineToggle1", 105, 320));
		HotKeyCmdEnv _tmp87 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine68", "EngineToggle2", 106, 321));
		HotKeyCmdEnv _tmp88 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine69", "EngineToggle3", 107, 322));
		HotKeyCmdEnv _tmp89 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine70", "EngineToggle4", 108, 323));
		HotKeyCmdEnv _tmp90 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine71", "EngineToggle5", 109, 324));
		HotKeyCmdEnv _tmp91 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine72", "EngineToggle6", 110, 325));
		HotKeyCmdEnv _tmp92 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine73", "EngineToggle7", 111, 326));
		HotKeyCmdEnv _tmp93 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine74", "EngineToggle8", 112, 327));
		HotKeyCmdEnv _tmp94 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$7", "2engine75") {

		}
				);
		HotKeyCmdEnv _tmp95 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine76", "EngineExtinguisher", 113, 330));
		HotKeyCmdEnv _tmp96 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine77", "EngineFeather", 114, 333));
		HotKeyCmdEnv _tmp97 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$8", "2engine78") {

		}
				);
		HotKeyCmdEnv _tmp98 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced01", "AIRCRAFT_FLAPS_NOTCH_UP", 52, 152));
		HotKeyCmdEnv _tmp99 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced02", "AIRCRAFT_FLAPS_NOTCH_DOWN", 53, 153));
		HotKeyCmdEnv _tmp224 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced021", "Toggle Blown Flaps", 160, 429));
		HotKeyCmdEnv _tmp100 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced03", "Gear", 9, 109));
		HotKeyCmdEnv _tmp101 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced04", "AIRCRAFT_GEAR_UP_MANUAL", 54, 154));
		HotKeyCmdEnv _tmp102 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced05", "AIRCRAFT_GEAR_DOWN_MANUAL", 55, 155));
		HotKeyCmdEnv _tmp103 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced06", "Radiator", 7, 107));
		HotKeyCmdEnv _tmp104 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced07", "AIRCRAFT_TOGGLE_AIRBRAKE", 63, 163));
		HotKeyCmdEnv _tmp105 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced08", "Brake", 0, 100));
		//TODO: New brake keys
		HotKeyCmdEnv tmp220 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced081", "brakesleft", 145, 352));
		HotKeyCmdEnv tmp221 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced082", "brakesright", 144, 351));
		HotKeyCmdEnv _tmp106 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced09", "AIRCRAFT_TAILWHEELLOCK", 72, 166));
		HotKeyCmdEnv _tmp107 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced10", "AIRCRAFT_DROP_TANKS", 62, 162));
		//TODO: Edits here
		HotKeyCmdEnv _tmp177= hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced12", "Deploy Drag Chute", 143, 412));
		HotKeyCmdEnv _tmp178_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced13", "Deploy Refuelling Device", 136, 405));
		HotKeyCmdEnv _tmp222 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced131", "Decrease Wing Sweep/Incidence", 54, 154));
		HotKeyCmdEnv _tmp223 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced132", "Increase Wing Sweep/Incidence", 55, 155));
		HotKeyCmdEnv _tmp108  = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$9", "3advanced14")
		{
		});
		HotKeyCmdEnv _tmp109 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced15", "AIRCRAFT_DOCK_UNDOCK", 126, 346));
		HotKeyCmdEnv _tmp110 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced16", "WINGFOLD", 127, 347));
		HotKeyCmdEnv _tmp111 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced17", "AIRCRAFT_CARRIERHOOK", 129, 349));
		HotKeyCmdEnv _tmp112 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced18", "AIRCRAFT_BRAKESHOE", 130, 350));
		HotKeyCmdEnv _tmp113 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced19", "COCKPITDOOR", 128, 348));
		//TODO: Added by |ZUTI|: Bomb Bay Door mod keys
		//-----------------------------------------------------------------------------
		HotKeyCmdEnv _tmp185 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced20", "Open/Close Side Hatch ", 134, 403));
		HotKeyCmdEnv _tmp186 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced21", "Open/Close Bay Doors", 131, 400));
		HotKeyCmdEnv _tmp187 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced22", "Toggle Fuel Dump", 132, 401));
		//-----------------------------------------------------------------------------
		HotKeyCmdEnv _tmp114 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$10", "3advanced23") {

		}
				);
		hudLogWeaponId = HUD.makeIdLog();
		HotKeyCmdEnv _tmp115 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic0", "Weapon0", 16, 116));
		HotKeyCmdEnv _tmp116 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic1", "Weapon1", 17, 117));
		HotKeyCmdEnv _tmp117 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic2", "Weapon2", 18, 118));
		HotKeyCmdEnv _tmp118 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic3", "Weapon3", 19, 119));
		HotKeyCmdEnv _tmp119 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic4", "Weapon01", 64, 173));
		HotKeyCmdEnv _tmp120 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic5", "GunPods", 15, 115));
		//TODO: Added by Anto: New weapon keys
		//-----------------------------------------------------------------------------
		HotKeyCmdEnv _tmp195 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic6", "Deploy Flares", 65, 174));
		HotKeyCmdEnv _tmp196 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic7", "Deploy Chaff", 66, 175));
		HotKeyCmdEnv _tmp197 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic9", "Select Missile/Rocket", 135, 404));
		HotKeyCmdEnv _tmp198 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic91", "Weapon Salvo Size", 137, 406));
		HotKeyCmdEnv _tmp199 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic92", "Weapon Release Delay", 146, 353));
		//-----------------------------------------------------------------------------
		HotKeyCmdEnv _tmp121 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$+SIGHTCONTROLS", "4basic93") {

		}
				);
		HotKeyCmdEnv _tmp122 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced01", "SIGHT_AUTO_ONOFF", 125, 344));
		HotKeyCmdEnv _tmp123 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced02", "SIGHT_DIST_PLUS", 117, 336));
		HotKeyCmdEnv _tmp124 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced03", "SIGHT_DIST_MINUS", 118, 337));
		HotKeyCmdEnv _tmp125 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced04", "SIGHT_SIDE_RIGHT", 119, 338));
		HotKeyCmdEnv _tmp126 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced05", "SIGHT_SIDE_LEFT", 120, 339));
		HotKeyCmdEnv _tmp127 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced06", "SIGHT_ALT_PLUS", 121, 340));
		HotKeyCmdEnv _tmp128 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced07", "SIGHT_ALT_MINUS", 122, 341));
		HotKeyCmdEnv _tmp129 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced08", "SIGHT_SPD_PLUS", 123, 342));
		HotKeyCmdEnv _tmp130 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced09", "SIGHT_SPD_MINUS", 124, 343));
		//TODO: Added by Anto: New radar keys
		//-----------------------------------------------------------------------------
		HotKeyCmdEnv _tmp210 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced10", "Toggle Radar Mode", 138, 407));
		HotKeyCmdEnv _tmp211 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced11", "Increase Scan Range", 139, 408));
		HotKeyCmdEnv _tmp212 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced12", "Decrease Scan Range", 140, 409));
		HotKeyCmdEnv _tmp213 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced13", "Increase Radar Gain", 141, 410));
		HotKeyCmdEnv _tmp214 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced14", "Decrease Radar Gain", 142, 411));
		//-----------------------------------------------------------------------------
	}

	private CockpitGunner getActiveCockpitGuner()
	{
		if(!Actor.isAlive(World.getPlayerAircraft()))
			return null;
		if(World.isPlayerParatrooper())
			return null;
		if(World.isPlayerDead())
			return null;
		if(Main3D.cur3D().cockpits == null)
			return null;
		int i = World.getPlayerAircraft().FM.AS.astatePlayerIndex;
		for(int j = 0; j < Main3D.cur3D().cockpits.length; j++)
		{
			if(!(Main3D.cur3D().cockpits[j] instanceof CockpitGunner))
				continue;
			CockpitGunner cockpitgunner = (CockpitGunner)Main3D.cur3D().cockpits[j];
			if(i != cockpitgunner.astatePilotIndx() || !cockpitgunner.isRealMode())
				continue;
			Turret turret = cockpitgunner.aiTurret();
			if(!turret.bIsNetMirror)
				return cockpitgunner;
		}

		return null;
	}

	public void createGunnerHotKeys()
	{
		String s = "gunner";
		HotKeyCmdEnv.setCurrentEnv(s);
		HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey " + s);
		HotKeyCmdEnv.addCmd(s, new HotKeyCmdMouseMove(true, "Mouse") {

			public void created()
			{
				setRecordId(51);
				sortingName = null;
			}

			public boolean isDisableIfTimePaused()
			{
				return true;
			}

			public void move(int i, int j, int k)
			{
				CockpitGunner cockpitgunner = getActiveCockpitGuner();
				if(cockpitgunner == null)
				{
					return;
				} else
				{
					cockpitgunner.hookGunner().mouseMove(i, j, k);
					return;
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "Fire") {

			public void created()
			{
				setRecordId(52);
			}

			public boolean isDisableIfTimePaused()
			{
				return true;
			}

			private boolean isExistAmmo(CockpitGunner cockpitgunner)
			{
				FlightModel flightmodel = World.getPlayerFM();
				BulletEmitter abulletemitter[] = flightmodel.CT.Weapons[cockpitgunner.weaponControlNum()];
				if(abulletemitter == null)
					return false;
				for(int i = 0; i < abulletemitter.length; i++)
					if(abulletemitter[i] != null && abulletemitter[i].haveBullets())
						return true;

				return false;
			}

			public void begin()
			{
				coc = getActiveCockpitGuner();
				if(coc == null)
					return;
				if(isExistAmmo(coc))
					coc.hookGunner().gunFire(true);
				else
					HUD.log(AircraftHotKeys.hudLogWeaponId, "OutOfAmmo");
			}

			public void end()
			{
				if(coc == null)
					return;
				if(Actor.isValid(coc))
					coc.hookGunner().gunFire(false);
				coc = null;
			}

			CockpitGunner coc;


			{
				coc = null;
			}
		}
				);
	}

	public boolean isAutoAutopilot()
	{
		return bAutoAutopilot;
	}

	public void setAutoAutopilot(boolean flag)
	{
		bAutoAutopilot = flag;
	}

	public static boolean isCockpitRealMode(int i)
	{
		if(Main3D.cur3D().cockpits[i] instanceof CockpitPilot)
		{
			RealFlightModel realflightmodel = (RealFlightModel)World.getPlayerFM();
			return realflightmodel.isRealMode();
		}
		if(Main3D.cur3D().cockpits[i] instanceof CockpitGunner)
		{
			CockpitGunner cockpitgunner = (CockpitGunner)Main3D.cur3D().cockpits[i];
			return cockpitgunner.isRealMode();
		} else
		{
			return false;
		}
	}

	public static void setCockpitRealMode(int i, boolean flag)
	{
		if(Main3D.cur3D().cockpits[i] instanceof CockpitPilot)
		{
			if(Mission.isNet())
				return;
			RealFlightModel realflightmodel = (RealFlightModel)World.getPlayerFM();
			if(realflightmodel.get_maneuver() == 44)
				return;
			if(realflightmodel.isRealMode() == flag)
				return;
			if(realflightmodel.isRealMode())
				Main3D.cur3D().aircraftHotKeys.bAfterburner = false;
			realflightmodel.CT.resetControl(0);
			realflightmodel.CT.resetControl(1);
			realflightmodel.CT.resetControl(2);
			realflightmodel.EI.setCurControlAll(true);
			realflightmodel.setRealMode(flag);
			HUD.log("PilotAI" + (realflightmodel.isRealMode() ? "OFF" : "ON"));
		} else
			if(Main3D.cur3D().cockpits[i] instanceof CockpitGunner)
			{
				CockpitGunner cockpitgunner = (CockpitGunner)Main3D.cur3D().cockpits[i];
				if(cockpitgunner.isRealMode() == flag)
					return;
				cockpitgunner.setRealMode(flag);
				if(!NetMissionTrack.isPlaying())
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if(World.isPlayerGunner())
						aircraft.netCockpitAuto(World.getPlayerGunner(), i, !cockpitgunner.isRealMode());
					else
						aircraft.netCockpitAuto(aircraft, i, !cockpitgunner.isRealMode());
				}
				FlightModel flightmodel = World.getPlayerFM();
				AircraftState _tmp = flightmodel.AS;
				String s = AircraftState.astateHUDPilotHits[flightmodel.AS.astatePilotFunctions[cockpitgunner.astatePilotIndx()]];
				HUD.log(s + (cockpitgunner.isRealMode() ? "AIOFF" : "AION"));
			}
	}

	private boolean isMiscValid()
	{
		if(!Actor.isAlive(World.getPlayerAircraft()))
			return false;
		if(World.isPlayerParatrooper())
			return false;
		if(World.isPlayerDead())
			return false;
		return Mission.isPlaying();
	}

    public void createMiscHotKeys()
    {
        String s = "misc";
        HotKeyCmdEnv.setCurrentEnv(s);
        HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey " + s);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "autopilot", "00") {

            public void created()
            {
                setRecordId(270);
            }

            public void begin()
            {
                if(!isMiscValid())
                    return;
                if(Main3D.cur3D().isDemoPlaying())
                    return;
                if(World.getPlayerFM().AS.isPilotDead(Main3D.cur3D().cockpitCur.astatePilotIndx()))
                    return;
                int j = Main3D.cur3D().cockpitCurIndx();
                if(AircraftHotKeys.isCockpitRealMode(j))
                    new MsgAction(true, new Integer(j)) {

                        public void doAction(Object obj)
                        {
                            int k = ((Integer)obj).intValue();
                            HotKeyCmd.exec("misc", "cockpitRealOff" + k);
                        }

                    }
;
                else
                    new MsgAction(true, new Integer(j)) {

                        public void doAction(Object obj)
                        {
                            int k = ((Integer)obj).intValue();
                            HotKeyCmd.exec("misc", "cockpitRealOn" + k);
                        }

                    }
;
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "autopilotAuto", "01") {

            public void begin()
            {
                if(!isMiscValid())
                    return;
                if(Main3D.cur3D().isDemoPlaying())
                {
                    return;
                } else
                {
                    new MsgAction(true) {

                        public void doAction()
                        {
                            HotKeyCmd.exec("misc", "autopilotAuto_");
                        }

                    }
;
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "autopilotAuto_", null) {

            public void created()
            {
                setRecordId(271);
                HotKeyEnv.currentEnv().remove(sName);
            }

            public void begin()
            {
                if(!isMiscValid())
                {
                    return;
                } else
                {
                    setAutoAutopilot(!isAutoAutopilot());
                    HUD.log("AutopilotAuto" + (isAutoAutopilot() ? "ON" : "OFF"));
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "target_", null) {

            public void created()
            {
                setRecordId(278);
                HotKeyEnv.currentEnv().remove(sName);
            }

            public void begin()
            {
                Actor actor = null;
                if(Main3D.cur3D().isDemoPlaying())
                    actor = Selector._getTrackArg0();
                else
                    actor = HookPilot.cur().getEnemy();
                Selector.setTarget(Selector.setCurRecordArg0(actor));
            }

        }
);
        for(int i = 0; i < 10; i++)
        {
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitRealOn" + i, null) {

                public void created()
                {
                    indx = Character.getNumericValue(name().charAt(name().length() - 1)) - Character.getNumericValue('0');
                    setRecordId(500 + indx);
                    HotKeyEnv.currentEnv().remove(sName);
                }

                public void begin()
                {
                    if(!isMiscValid())
                    {
                        return;
                    } else
                    {
                        AircraftHotKeys.setCockpitRealMode(indx, true);
                        return;
                    }
                }

                int indx;

            }
);
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitRealOff" + i, null) {

                public void created()
                {
                    indx = Character.getNumericValue(name().charAt(name().length() - 1)) - Character.getNumericValue('0');
                    setRecordId(510 + indx);
                    HotKeyEnv.currentEnv().remove(sName);
                }

                public void begin()
                {
                    if(!isMiscValid())
                    {
                        return;
                    } else
                    {
                        AircraftHotKeys.setCockpitRealMode(indx, false);
                        return;
                    }
                }

                int indx;

            }
);
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitEnter" + i, null) {

                public void created()
                {
                    indx = Character.getNumericValue(name().charAt(name().length() - 1)) - Character.getNumericValue('0');
                    setRecordId(520 + indx);
                    HotKeyEnv.currentEnv().remove(sName);
                }

                public void begin()
                {
                    if(!isMiscValid())
                        return;
                    if(Main3D.cur3D().cockpits != null && indx < Main3D.cur3D().cockpits.length)
                    {
                        World.getPlayerAircraft().FM.AS.astatePlayerIndex = Main3D.cur3D().cockpits[indx].astatePilotIndx();
                        if(!NetMissionTrack.isPlaying())
                        {
                            Aircraft aircraft = World.getPlayerAircraft();
                            if(World.isPlayerGunner())
                                aircraft.netCockpitEnter(World.getPlayerGunner(), indx);
                            else
                                aircraft.netCockpitEnter(aircraft, indx);
                        }
                    }
                }

                int indx;

            }
);
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitLeave" + i, null) {

                public void created()
                {
                    indx = Character.getNumericValue(name().charAt(name().length() - 1)) - Character.getNumericValue('0');
                    setRecordId(530 + indx);
                    HotKeyEnv.currentEnv().remove(sName);
                }

                public void begin()
                {
                    if(!isMiscValid())
                        return;
                    if(Main3D.cur3D().cockpits != null && indx < Main3D.cur3D().cockpits.length && (Main3D.cur3D().cockpits[indx] instanceof CockpitGunner) && AircraftHotKeys.isCockpitRealMode(indx))
                        ((CockpitGunner)Main3D.cur3D().cockpits[indx]).hookGunner().gunFire(false);
                }

                int indx;

            }
);
        }

        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ejectPilot", "02") {

            public void created()
            {
                setRecordId(272);
            }

            public void begin()
            {
                if(!isMiscValid())
                    return;
                if(World.isPlayerGunner())
                    return;
                if(!(World.getPlayerFM() instanceof RealFlightModel))
                    return;
                RealFlightModel realflightmodel = (RealFlightModel)World.getPlayerFM();
                if(!realflightmodel.isRealMode())
                    return;
                if(realflightmodel.AS.bIsAboutToBailout)
                    return;
                if(!realflightmodel.AS.bIsEnableToBailout)
                {
                    return;
                } else
                {
                    AircraftState.bCheckPlayerAircraft = false;
                    ((Aircraft)realflightmodel.actor).hitDaSilk();
                    AircraftState.bCheckPlayerAircraft = true;
                    Voice.cur().SpeakBailOut[realflightmodel.actor.getArmy() - 1 & 1][((Aircraft)realflightmodel.actor).aircIndex()] = (int)(Time.current() / 60000L) + 1;
                    new MsgAction(true) {

                        public void doAction()
                        {
                            if(!Main3D.cur3D().isDemoPlaying() || !HotKeyEnv.isEnabled("aircraftView"))
                                HotKeyCmd.exec("aircraftView", "OutsideView");
                        }

                    }
;
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitDim", "03") {

            public void created()
            {
                setRecordId(274);
            }

            public void begin()
            {
                if(Main3D.cur3D().isViewOutside())
                    return;
                if(!isMiscValid())
                    return;
                if(!Actor.isValid(Main3D.cur3D().cockpitCur))
                {
                    return;
                } else
                {
                    Main3D.cur3D().cockpitCur.doToggleDim();
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitLight", "04") {

            public void created()
            {
                setRecordId(275);
            }

            public void begin()
            {
                if(Main3D.cur3D().isViewOutside())
                    return;
                if(!isMiscValid())
                    return;
                if(!Actor.isValid(Main3D.cur3D().cockpitCur))
                {
                    return;
                } else
                {
                    Main3D.cur3D().cockpitCur.doToggleLight();
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toggleNavLights", "05") {

            public void created()
            {
                setRecordId(331);
            }

            public void begin()
            {
                if(!isMiscValid())
                    return;
                FlightModel flightmodel = World.getPlayerFM();
                if(flightmodel == null)
                    return;
                boolean flag = flightmodel.AS.bNavLightsOn;
                flightmodel.AS.setNavLightsState(!flightmodel.AS.bNavLightsOn);
                if(!flag && !flightmodel.AS.bNavLightsOn)
                {
                    return;
                } else
                {
                    HUD.log("NavigationLights" + (flightmodel.AS.bNavLightsOn ? "ON" : "OFF"));
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toggleLandingLight", "06") {

            public void created()
            {
                setRecordId(345);
            }

            public void begin()
            {
                if(!isMiscValid())
                    return;
                FlightModel flightmodel = World.getPlayerFM();
                if(flightmodel == null)
                    return;
                boolean flag = flightmodel.AS.bLandingLightOn;
                flightmodel.AS.setLandingLightState(!flightmodel.AS.bLandingLightOn);
                if(!flag && !flightmodel.AS.bLandingLightOn)
                {
                    return;
                } else
                {
                    HUD.log("LandingLight" + (flightmodel.AS.bLandingLightOn ? "ON" : "OFF"));
                    EventLog.onToggleLandingLight(flightmodel.actor, flightmodel.AS.bLandingLightOn);
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toggleSmokes", "07") {

            public void created()
            {
                setRecordId(273);
            }

            public void begin()
            {
                if(!isMiscValid())
                    return;
                FlightModel flightmodel = World.getPlayerFM();
                if(flightmodel == null)
                {
                    return;
                } else
                {
                    flightmodel.AS.setAirShowState(!flightmodel.AS.bShowSmokesOn);
                    EventLog.onToggleSmoke(flightmodel.actor, flightmodel.AS.bShowSmokesOn);
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "pad", "08") {

            public void end()
            {
                int j = Main.state().id();
                boolean flag = j == 5 || j == 29 || j == 63 || j == 49 || j == 50 || j == 42 || j == 43;
                if(GUI.pad.isActive())
                    GUI.pad.leave(!flag);
                else
                if(flag && !Main3D.cur3D().guiManager.isMouseActive())
                    GUI.pad.enter(false);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "padFull", "09") {

            public void end()
            {
                int j = Main.state().id();
                boolean flag = j == 5 || j == 29 || j == 63 || j == 49 || j == 50 || j == 42 || j == 43;
                if(GUI.pad.isActive())
                    GUI.pad.leave(!flag);
                else
                if(flag && !Main3D.cur3D().guiManager.isMouseActive())
                    GUI.pad.enter(true);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "chat", "10") {

            public void end()
            {
                GUI.chatActivate();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "onlineRating", "11") {

            public void begin()
            {
                Main3D.cur3D().hud.startNetStat();
            }

            public void end()
            {
                Main3D.cur3D().hud.stopNetStat();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "onlineRatingPage", "12") {

            public void end()
            {
                Main3D.cur3D().hud.pageNetStat();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "showPositionHint", "13") {

            public void begin()
            {
                HUD.setDrawSpeed((HUD.drawSpeed() + 1) % 4);
            }

            public void created()
            {
                setRecordId(277);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "iconTypes", "14") {

            public void end()
            {
                Main3D.cur3D().changeIconTypes();
            }

            public void created()
            {
                setRecordId(279);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "showMirror", "15") {

            public void end()
            {
                Main3D.cur3D().viewMirror = (Main3D.cur3D().viewMirror + 1) % 3;
            }

            public void created()
            {
                setRecordId(280);
            }

        }
);
    }

	public void create_MiscHotKeys()
	{
		String s = "$$$misc";
		HotKeyCmdEnv.setCurrentEnv(s);
		HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey " + s);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "quickSaveNetTrack", "01") {

			public void end()
			{
				GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
				if(guiwindowmanager.isKeyboardActive())
					return;
				if(NetMissionTrack.isQuickRecording())
					NetMissionTrack.stopRecording();
				else
					NetMissionTrack.startQuickRecording();
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "radioMuteKey", "02") {

			public void begin()
			{
				AudioDevice.setPTT(true);
			}

			public void end()
			{
				AudioDevice.setPTT(false);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "radioChannelSwitch", "03") {

			public void end()
			{
				if(GUI.chatDlg == null)
					return;
				if(Main.cur().chat == null)
					return;
				if(GUI.chatDlg.mode() == 2)
					return;
				if(RadioChannel.tstLoop)
					return;
				if(!AudioDevice.npFlags.get(0))
					return;
				if(NetMissionTrack.isPlaying())
					return;
				NetUser netuser = (NetUser)NetEnv.host();
				String s1 = null;
				String s2 = null;
				if(netuser.isRadioPrivate())
				{
					s1 = "radio NONE";
					s2 = "radioNone";
				} else
					if(netuser.isRadioArmy())
					{
						s1 = "radio NONE";
						s2 = "radioNone";
					} else
						if(netuser.isRadioCommon())
						{
							if(netuser.getArmy() != 0)
							{
								s1 = "radio ARMY";
								s2 = "radioArmy";
							} else
							{
								s1 = "radio NONE";
								s2 = "radioNone";
							}
						} else
							if(netuser.isRadioNone())
							{
								s1 = "radio COMMON";
								s2 = "radioCommon";
							}
				System.out.println(RTSConf.cur.console.getPrompt() + s1);
				RTSConf.cur.console.getEnv().exec(s1);
				RTSConf.cur.console.addHistoryCmd(s1);
				RTSConf.cur.console.curHistoryCmd = -1;
				if(!Time.isPaused())
					HUD.log(s2);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "soundMuteKey", "04") {

			public void end()
			{
				AudioDevice.toggleMute();
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("05", "Toggle In-Flight Music", 133, 402));
	}

	private void switchToAIGunner()
	{
		if(!Main3D.cur3D().isDemoPlaying() && (Main3D.cur3D().cockpitCur instanceof CockpitGunner) && Main3D.cur3D().isViewOutside() && isAutoAutopilot())
		{
			CockpitGunner cockpitgunner = (CockpitGunner)Main3D.cur3D().cockpitCur;
			if(cockpitgunner.isRealMode())
				new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx())) {

				public void doAction(Object obj)
				{
					int i = ((Integer)obj).intValue();
					HotKeyCmd.exec("misc", "cockpitRealOff" + i);
				}

			}
			;
		}
	}

	private boolean isValidCockpit(int i)
	{
		if(!Actor.isValid(World.getPlayerAircraft()))
			return false;
		if(!Mission.isPlaying())
			return false;
		if(World.isPlayerParatrooper())
			return false;
		if(Main3D.cur3D().cockpits == null)
			return false;
		if(i >= Main3D.cur3D().cockpits.length)
			return false;
		if(World.getPlayerAircraft().isUnderWater())
			return false;
		Cockpit cockpit = Main3D.cur3D().cockpits[i];
		if(!cockpit.isEnableFocusing())
			return false;
		int j = cockpit.astatePilotIndx();
		if(World.getPlayerFM().AS.isPilotParatrooper(j))
			return false;
		if(World.getPlayerFM().AS.isPilotDead(j))
			return false;
		if(Mission.isNet())
		{
			if(Mission.isCoop())
			{
				if(World.isPlayerGunner())
				{
					if(cockpit instanceof CockpitPilot)
						return false;
				} else
					if(cockpit instanceof CockpitPilot)
						return true;
				if(Time.current() == 0L)
					return false;
				if(Main3D.cur3D().isDemoPlaying())
					return true;
				return !Actor.isValid(World.getPlayerAircraft().netCockpitGetDriver(i)) || World.isPlayerDead();
			}
			return Mission.isDogfight();
		} else
		{
			return true;
		}
	}

	private void switchToCockpit(int i)
	{
		if(Mission.isCoop() && (Main3D.cur3D().cockpits[i] instanceof CockpitGunner) && !Main3D.cur3D().isDemoPlaying() && !World.isPlayerDead())
		{
			Object obj = World.getPlayerAircraft();
			if(World.isPlayerGunner())
				obj = World.getPlayerGunner();
			Actor actor = World.getPlayerAircraft().netCockpitGetDriver(i);
			if(obj != actor)
				if(Actor.isValid(actor))
				{
					return;
				} else
				{
					switchToCockpitRequest = i;
					World.getPlayerAircraft().netCockpitDriverRequest(((Actor) (obj)), i);
					return;
				}
		}
		doSwitchToCockpit(i);
	}

	public void netSwitchToCockpit(int i)
	{
		if(Main3D.cur3D().isDemoPlaying())
			return;
		if(i == switchToCockpitRequest)
			new MsgAction(true, new Integer(i)) {

			public void doAction(Object obj)
			{
				int j = ((Integer)obj).intValue();
				HotKeyCmd.exec("aircraftView", "cockpitSwitch" + j);
			}

		}
		;
	}

	private void doSwitchToCockpit(int i)
	{
		Selector.setCurRecordArg0(World.getPlayerAircraft());
		if(!World.isPlayerDead() && !World.isPlayerParatrooper() && !Main3D.cur3D().isDemoPlaying())
		{
			boolean flag = true;
			if((Main3D.cur3D().cockpitCur instanceof CockpitPilot) && (Main3D.cur3D().cockpits[i] instanceof CockpitPilot))
				flag = false;
			if(flag && isAutoAutopilot())
				new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx())) {

				public void doAction(Object obj)
				{
					int j = ((Integer)obj).intValue();
					HotKeyCmd.exec("misc", "cockpitRealOff" + j);
				}

			}
			;
			new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx())) {

				public void doAction(Object obj)
				{
					int j = ((Integer)obj).intValue();
					HotKeyCmd.exec("misc", "cockpitLeave" + j);
				}

			}
			;
			new MsgAction(true, new Integer(i)) {

				public void doAction(Object obj)
				{
					int j = ((Integer)obj).intValue();
					HotKeyCmd.exec("misc", "cockpitEnter" + j);
				}

			}
			;
			if(flag && isAutoAutopilot())
				new MsgAction(true, new Integer(i)) {

				public void doAction(Object obj)
				{
					int j = ((Integer)obj).intValue();
					HotKeyCmd.exec("misc", "cockpitRealOn" + j);
				}

			}
			;
		}
		Main3D.cur3D().cockpitCur.focusLeave();
		Main3D.cur3D().cockpitCur = Main3D.cur3D().cockpits[i];
		Main3D.cur3D().cockpitCur.focusEnter();
	}

	private int nextValidCockpit()
	{
		int i = Main3D.cur3D().cockpitCurIndx();
		if(i < 0)
			return -1;
		int j = Main3D.cur3D().cockpits.length;
		if(j < 2)
			return -1;
		for(int k = 0; k < j - 1; k++)
		{
			int l = (i + k + 1) % j;
			if(isValidCockpit(l))
				return l;
		}

		return -1;
	}

	public void setEnableChangeFov(boolean flag)
	{
		for(int i = 0; i < cmdFov.length; i++)
			cmdFov[i].enable(flag);

		changeFovEnabled = flag;
	}

	public void createViewHotKeys()
	{
		String s = "aircraftView";
		HotKeyCmdEnv.setCurrentEnv(s);
		HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey " + s);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "changeCockpit", "0") {

			public void begin()
			{
				int j = nextValidCockpit();
				if(j < 0)
				{
					return;
				} else
				{
					new MsgAction(true, new Integer(j)) {

						public void doAction(Object obj)
						{
							int k = ((Integer)obj).intValue();
							HotKeyCmd.exec("aircraftView", "cockpitSwitch" + k);
						}

					}
					;
					return;
				}
			}

		}
				);
		for(int i = 0; i < 10; i++)
		{
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitView" + i, "0" + i) {

				public void created()
				{
					indx = Character.getNumericValue(name().charAt(name().length() - 1)) - Character.getNumericValue('0');
				}

				public void begin()
				{
					if(!isValidCockpit(indx))
					{
						return;
					} else
					{
						new MsgAction(true, new Integer(indx)) {

							public void doAction(Object obj)
							{
								int j = ((Integer)obj).intValue();
								HotKeyCmd.exec("aircraftView", "cockpitSwitch" + j);
							}

						}
						;
						return;
					}
				}

				int indx;

			}
					);
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitSwitch" + i, null) {

				public void created()
				{
					indx = Character.getNumericValue(name().charAt(name().length() - 1)) - Character.getNumericValue('0');
					setRecordId(230 + indx);
					HotKeyEnv.currentEnv().remove(sName);
				}

				public void begin()
				{
					if(Main3D.cur3D().cockpitCurIndx() == indx && !Main3D.cur3D().isViewOutside())
					{
						return;
					} else
					{
						switchToCockpit(indx);
						return;
					}
				}

				int indx;

			}
					);
		}

		HotKeyCmdEnv.addCmd(cmdFov[0] = new HotKeyCmd(true, "fov90", "11") {

			public void begin()
			{
				CmdEnv.top().exec("fov 90");
			}

			public void created()
			{
				setRecordId(216);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[1] = new HotKeyCmd(true, "fov85", "12") {

			public void begin()
			{
				CmdEnv.top().exec("fov 85");
			}

			public void created()
			{
				setRecordId(244);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[2] = new HotKeyCmd(true, "fov80", "13") {

			public void begin()
			{
				CmdEnv.top().exec("fov 80");
			}

			public void created()
			{
				setRecordId(243);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[3] = new HotKeyCmd(true, "fov75", "14") {

			public void begin()
			{
				CmdEnv.top().exec("fov 75");
			}

			public void created()
			{
				setRecordId(242);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[4] = new HotKeyCmd(true, "fov70", "15") {

			public void begin()
			{
				CmdEnv.top().exec("fov 70");
			}

			public void created()
			{
				setRecordId(215);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[5] = new HotKeyCmd(true, "fov65", "16") {

			public void begin()
			{
				CmdEnv.top().exec("fov 65");
			}

			public void created()
			{
				setRecordId(241);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[6] = new HotKeyCmd(true, "fov60", "17") {

			public void begin()
			{
				CmdEnv.top().exec("fov 60");
			}

			public void created()
			{
				setRecordId(240);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[7] = new HotKeyCmd(true, "fov55", "18") {

			public void begin()
			{
				CmdEnv.top().exec("fov 55");
			}

			public void created()
			{
				setRecordId(229);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[8] = new HotKeyCmd(true, "fov50", "19") {

			public void begin()
			{
				CmdEnv.top().exec("fov 50");
			}

			public void created()
			{
				setRecordId(228);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[9] = new HotKeyCmd(true, "fov45", "20") {

			public void begin()
			{
				CmdEnv.top().exec("fov 45");
			}

			public void created()
			{
				setRecordId(227);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[10] = new HotKeyCmd(true, "fov40", "21") {

			public void begin()
			{
				CmdEnv.top().exec("fov 40");
			}

			public void created()
			{
				setRecordId(226);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[11] = new HotKeyCmd(true, "fov35", "22") {

			public void begin()
			{
				CmdEnv.top().exec("fov 35");
			}

			public void created()
			{
				setRecordId(225);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[12] = new HotKeyCmd(true, "fov30", "23") {

			public void begin()
			{
				CmdEnv.top().exec("fov 30");
			}

			public void created()
			{
				setRecordId(214);
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[13] = new HotKeyCmd(true, "fovSwitch", "24") {

			public void begin()
			{
				float f = (Main3D.FOVX - 30F) * (Main3D.FOVX - 30F);
				float f1 = (Main3D.FOVX - 70F) * (Main3D.FOVX - 70F);
				float f2 = (Main3D.FOVX - 90F) * (Main3D.FOVX - 90F);
				byte byte0 = 0;
				if(f <= f1)
					byte0 = 70;
				else
					if(f1 <= f2)
						byte0 = 90;
					else
						byte0 = 30;
				new MsgAction(true, new Integer(byte0)) {

					public void doAction(Object obj)
					{
						int j = ((Integer)obj).intValue();
						HotKeyCmd.exec("aircraftView", "fov" + j);
					}

				}
				;
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[14] = new HotKeyCmd(true, "fovInc", "25") {

			public void begin()
			{
				int j = ((int)((double)Main3D.FOVX + 2.5D) / 5) * 5;
				if(j < 30)
					j = 30;
				if(j > 85)
					j = 85;
				j += 5;
				new MsgAction(true, new Integer(j)) {

					public void doAction(Object obj)
					{
						int k = ((Integer)obj).intValue();
						HotKeyCmd.exec("aircraftView", "fov" + k);
					}

				}
				;
			}

		}
				);
		HotKeyCmdEnv.addCmd(cmdFov[15] = new HotKeyCmd(true, "fovDec", "26") {

			public void begin()
			{
				int j = ((int)((double)Main3D.FOVX + 2.5D) / 5) * 5;
				if(j < 35)
					j = 35;
				if(j > 90)
					j = 90;
				j -= 5;
				new MsgAction(true, new Integer(j)) {

					public void doAction(Object obj)
					{
						int k = ((Integer)obj).intValue();
						HotKeyCmd.exec("aircraftView", "fov" + k);
					}

				}
				;
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "CockpitView", "27") {

			public void begin()
			{
				if(!Actor.isValid(World.getPlayerAircraft()))
					return;
				if(World.isPlayerParatrooper())
					return;
				if(World.getPlayerAircraft().isUnderWater())
					return;
				Main3D.cur3D().setViewInside();
				Selector.setCurRecordArg0(World.getPlayerAircraft());
				if(!Main3D.cur3D().isDemoPlaying() && World.getPlayerAircraft().netCockpitGetDriver(Main3D.cur3D().cockpitCurIndx()) == null)
					new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx())) {

					public void doAction(Object obj)
					{
						int j = ((Integer)obj).intValue();
						HotKeyCmd.exec("misc", "cockpitEnter" + j);
					}

				}
				;
				if(!Main3D.cur3D().isDemoPlaying() && !Main3D.cur3D().isViewOutside() && isAutoAutopilot() && !AircraftHotKeys.isCockpitRealMode(Main3D.cur3D().cockpitCurIndx()))
					new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx())) {

					public void doAction(Object obj)
					{
						int j = ((Integer)obj).intValue();
						HotKeyCmd.exec("misc", "cockpitRealOn" + j);
					}

				}
				;
			}

			public void created()
			{
				setRecordId(212);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "CockpitShow", "28") {

			public void created()
			{
				setRecordId(213);
			}

			public void begin()
			{
				if(World.cur().diffCur.Cockpit_Always_On)
					return;
				if(Main3D.cur3D().isViewOutside())
					return;
				if(!(Main3D.cur3D().cockpitCur instanceof CockpitPilot))
					return;
				if(Main3D.cur3D().isViewInsideShow())
				{
					Main3D.cur3D().hud.bDrawDashBoard = true;
					Main3D.cur3D().setViewInsideShow(false);
					Main3D.cur3D().cockpitCur.setEnableRenderingBall(true);
				} else
					if(Main3D.cur3D().hud.bDrawDashBoard && Main3D.cur3D().cockpitCur.isEnableRenderingBall())
						Main3D.cur3D().cockpitCur.setEnableRenderingBall(false);
					else
						if(Main3D.cur3D().hud.bDrawDashBoard && !Main3D.cur3D().cockpitCur.isEnableRenderingBall())
						{
							Main3D.cur3D().hud.bDrawDashBoard = false;
							Main3D.cur3D().cockpitCur.setEnableRenderingBall(true);
						} else
							if(Main3D.cur3D().isEnableRenderingCockpit() && Main3D.cur3D().cockpitCur.isEnableRenderingBall())
								Main3D.cur3D().cockpitCur.setEnableRenderingBall(false);
							else
								if(Main3D.cur3D().isEnableRenderingCockpit() && !Main3D.cur3D().cockpitCur.isEnableRenderingBall())
								{
									Main3D.cur3D().setEnableRenderingCockpit(false);
								} else
								{
									Main3D.cur3D().setEnableRenderingCockpit(true);
									Main3D.cur3D().setViewInsideShow(true);
								}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OutsideView", "29") {

			public void created()
			{
				setRecordId(205);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews && !Aircraft.isPlayerTaxing())
					return;
				Object obj = World.getPlayerAircraft();
				Selector.setCurRecordArg0(((Actor) (obj)));
				if(!Actor.isValid(((Actor) (obj))))
					obj = getViewActor();
				if(Actor.isValid(((Actor) (obj))))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFlow10(((Actor) (obj)), false);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextView", "30") {

			public void created()
			{
				setRecordId(206);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				Actor actor = nextViewActor(false);
				if(Actor.isValid(actor))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFlow10(actor, false);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextViewEnemy", "31") {

			public void created()
			{
				setRecordId(207);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				Actor actor = nextViewActor(true);
				if(Actor.isValid(actor))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFlow10(actor, false);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OutsideViewFly", "32") {

			public void created()
			{
				setRecordId(200);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				Actor actor = getViewActor();
				if(Actor.isValid(actor))
				{
					if(actor == World.getPlayerAircraft() && World.cur().diffCur.NoOwnPlayerViews)
						return;
					if(!(actor instanceof ActorViewPoint) && !(actor instanceof BigshipGeneric))
					{
						boolean flag = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFly(actor);
						if(flag)
							switchToAIGunner();
					}
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockView", "33") {

			public void created()
			{
				setRecordId(217);
			}

			public void begin()
			{
				VisCheck.playerVisibilityCheck(World.getPlayerAircraft(), true, 1.0F);
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
					return;
				if(Main3D.cur3D().isViewPadlock())
				{
					Main3D.cur3D().setViewEndPadlock();
					Selector.setCurRecordArg1(aircraft);
				} else
				{
					if(AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
						Main3D.cur3D().setViewInside();
					Main3D.cur3D().setViewPadlock(false, false);
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewFriend", "34") {

			public void created()
			{
				setRecordId(218);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
					return;
				if(Main3D.cur3D().isViewPadlock())
				{
					Main3D.cur3D().setViewEndPadlock();
					Selector.setCurRecordArg1(aircraft);
				} else
				{
					if(AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
						Main3D.cur3D().setViewInside();
					Main3D.cur3D().setViewPadlock(true, false);
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewGround", "35") {

			public void created()
			{
				setRecordId(221);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
					return;
				if(Main3D.cur3D().isViewPadlock())
				{
					Main3D.cur3D().setViewEndPadlock();
					Selector.setCurRecordArg1(aircraft);
				} else
				{
					if(AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
						Main3D.cur3D().setViewInside();
					Main3D.cur3D().setViewPadlock(false, true);
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewFriendGround", "36") {

			public void created()
			{
				setRecordId(222);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
					return;
				if(Main3D.cur3D().isViewPadlock())
				{
					Main3D.cur3D().setViewEndPadlock();
					Selector.setCurRecordArg1(aircraft);
				} else
				{
					if(AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
						Main3D.cur3D().setViewInside();
					Main3D.cur3D().setViewPadlock(true, true);
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewNext", "37") {

			public void created()
			{
				setRecordId(223);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
					return;
				if(AircraftHotKeys.bFirstHotCmd)
				{
					Main3D.cur3D().setViewInside();
					if(Actor.isValid(Main3D.cur3D().cockpitCur) && Main3D.cur3D().cockpitCur.existPadlock())
						Main3D.cur3D().cockpitCur.startPadlock(Selector._getTrackArg1());
				} else
					if(Main3D.cur3D().isViewPadlock())
						Main3D.cur3D().setViewNextPadlock(true);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewPrev", "38") {

			public void created()
			{
				setRecordId(224);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
					return;
				if(AircraftHotKeys.bFirstHotCmd)
				{
					Main3D.cur3D().setViewInside();
					if(Actor.isValid(Main3D.cur3D().cockpitCur) && Main3D.cur3D().cockpitCur.existPadlock())
						Main3D.cur3D().cockpitCur.startPadlock(Selector._getTrackArg1());
				} else
					if(Main3D.cur3D().isViewPadlock())
						Main3D.cur3D().setViewNextPadlock(false);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewForward", "39") {

			public void created()
			{
				setRecordId(220);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
				{
					return;
				} else
				{
					Main3D.cur3D().setViewPadlockForward(true);
					return;
				}
			}

			public void end()
			{
				if(World.cur().diffCur.No_Padlock)
					return;
				Aircraft aircraft = World.getPlayerAircraft();
				if(!Actor.isValid(aircraft) || World.isPlayerDead() || World.isPlayerParatrooper())
				{
					return;
				} else
				{
					Main3D.cur3D().setViewPadlockForward(false);
					return;
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyAir", "40") {

			public void created()
			{
				setRecordId(203);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews)
					return;
				if(World.cur().diffCur.No_Padlock)
					return;
				Actor actor = getViewActor();
				if(Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewEnemy(actor, false, false);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewFriendAir", "41") {

			public void created()
			{
				setRecordId(198);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews)
					return;
				if(World.cur().diffCur.No_Padlock)
					return;
				Actor actor = getViewActor();
				if(Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFriend(actor, false, false);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyDirectAir", "42") {

			public void created()
			{
				setRecordId(201);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews)
					return;
				if(World.cur().diffCur.No_Padlock)
					return;
				Actor actor = getViewActor();
				if(Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewEnemy(actor, true, false);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyGround", "43") {

			public void created()
			{
				setRecordId(204);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews)
					return;
				if(World.cur().diffCur.No_Padlock)
					return;
				Actor actor = getViewActor();
				if(Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewEnemy(actor, false, true);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewFriendGround", "44") {

			public void created()
			{
				setRecordId(199);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews)
					return;
				if(World.cur().diffCur.No_Padlock)
					return;
				Actor actor = getViewActor();
				if(Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFriend(actor, false, true);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyDirectGround", "45") {

			public void created()
			{
				setRecordId(202);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews)
					return;
				if(World.cur().diffCur.No_Padlock)
					return;
				Actor actor = getViewActor();
				if(Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewEnemy(actor, true, true);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OutsideViewFollow", "46") {

			public void created()
			{
				setRecordId(208);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				if(World.cur().diffCur.NoOwnPlayerViews)
					return;
				Object obj = World.getPlayerAircraft();
				Selector.setCurRecordArg0(((Actor) (obj)));
				if(!Actor.isValid(((Actor) (obj))))
					obj = getViewActor();
				if(Actor.isValid(((Actor) (obj))))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFlow10(((Actor) (obj)), true);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextViewFollow", "47") {

			public void created()
			{
				setRecordId(209);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				Actor actor = nextViewActor(false);
				if(Actor.isValid(actor))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFlow10(actor, true);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextViewEnemyFollow", "48") {

			public void created()
			{
				setRecordId(210);
			}

			public void begin()
			{
				if(World.cur().diffCur.No_Outside_Views)
					return;
				Actor actor = nextViewActor(true);
				if(Actor.isValid(actor))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFlow10(actor, true);
					if(flag)
						switchToAIGunner();
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitAim", "49") {

			public void created()
			{
				setRecordId(276);
			}

			public void begin()
			{
				if(Main3D.cur3D().isViewOutside())
					return;
				if(!isMiscValid())
					return;
				if(!Actor.isValid(Main3D.cur3D().cockpitCur))
					return;
				if(Main3D.cur3D().cockpitCur.isToggleUp())
				{
					return;
				} else
				{
					Main3D.cur3D().cockpitCur.doToggleAim(!Main3D.cur3D().cockpitCur.isToggleAim());
					return;
				}
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitUp", "50") {

			public void created()
			{
				setRecordId(281);
			}

			public void begin()
			{
				if(Main3D.cur3D().isViewOutside())
					return;
				if(!isMiscValid())
					return;
				if(!Actor.isValid(Main3D.cur3D().cockpitCur))
					return;
				if(Main3D.cur3D().cockpitCur.isToggleAim())
					return;
				if(!World.getPlayerFM().CT.bHasCockpitDoorControl)
					return;
				if(!Main3D.cur3D().cockpitCur.isToggleUp() && (World.getPlayerFM().CT.cockpitDoorControl < 0.5F || World.getPlayerFM().CT.getCockpitDoor() < 0.99F))
				{
					return;
				} else
				{
					Main3D.cur3D().cockpitCur.doToggleUp(!Main3D.cur3D().cockpitCur.isToggleUp());
					return;
				}
			}

		}
				);
	}

	public void createTimeHotKeys()
	{
		String s = "timeCompression";
		HotKeyCmdEnv.setCurrentEnv(s);
		HotKeyEnv.fromIni(s, Config.cur.ini, "HotKey " + s);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedUp", "0") {

			public void begin()
			{
				if(TimeSkip.isDo())
					return;
				if(Time.isEnableChangeSpeed())
				{
					float f = Time.nextSpeed() * 2.0F;
					if(f <= 8F)
					{
						Time.setSpeed(f);
						showTimeSpeed(f);
					}
				}
			}

			public void created()
			{
				setRecordId(25);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedNormal", "1") {

			public void begin()
			{
				if(TimeSkip.isDo())
					return;
				if(Time.isEnableChangeSpeed())
				{
					Time.setSpeed(1.0F);
					showTimeSpeed(1.0F);
				}
			}

			public void created()
			{
				setRecordId(24);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedDown", "2") {

			public void begin()
			{
				if(TimeSkip.isDo())
					return;
				if(Time.isEnableChangeSpeed())
				{
					float f = Time.nextSpeed() / 2.0F;
					if(f >= 0.25F)
					{
						Time.setSpeed(f);
						showTimeSpeed(f);
					}
				}
			}

			public void created()
			{
				setRecordId(26);
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedPause", "3") {

			public void begin()
			{
			}

		}
				);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSkip", "4") {

			public void begin()
			{
				if(TimeSkip.isDo())
					Main3D.cur3D().timeSkip.stop();
				else
					Main3D.cur3D().timeSkip.start();
			}

		}
				);
	}

	private void showTimeSpeed(float f)
	{
		int i = Math.round(f * 4F);
		switch(i)
		{
		case 4: // '\004'
			Main3D.cur3D().hud._log(0, "TimeSpeedNormal");
			break;

		case 8: // '\b'
			Main3D.cur3D().hud._log(0, "TimeSpeedUp2");
			break;

		case 16: // '\020'
			Main3D.cur3D().hud._log(0, "TimeSpeedUp4");
			break;

		case 32: // ' '
			Main3D.cur3D().hud._log(0, "TimeSpeedUp8");
			break;

		case 2: // '\002'
			Main3D.cur3D().hud._log(0, "TimeSpeedDown2");
			break;

		case 1: // '\001'
			Main3D.cur3D().hud._log(0, "TimeSpeedDown4");
			break;
		}
	}

	private Actor getViewActor()
	{
		if(Selector.isEnableTrackArgs())
			return Selector.setCurRecordArg0(Selector.getTrackArg0());
		Actor actor = Main3D.cur3D().viewActor();
		if(isViewed(actor))
			return Selector.setCurRecordArg0(actor);
		else
			return Selector.setCurRecordArg0(World.getPlayerAircraft());
	}

	private boolean isValidViewActor(Actor actor, boolean flag)
	{
		if((actor instanceof Regiment) || (actor instanceof Bridge) || (actor instanceof BridgeSegment))
			return false;
		if(actor instanceof Aircraft)
		{
			if(actor == World.getPlayerAircraft())
			{
				if(World.cur().diffCur.NoOwnPlayerViews)
					return false;
			} else
			{
				if(World.cur().diffCur.NoAircraftViews)
					return false;
				if(World.cur().diffCur.NoFriendlyViews && !flag)
					return false;
				if(World.cur().diffCur.NoEnemyViews && flag)
					return false;
			}
			return true;
		}
		if((actor instanceof BigshipGeneric) && ((BigshipGeneric)actor).getAirport() != null)
		{
			if(World.cur().diffCur.NoSeaUnitViews)
				return false;
			if(World.cur().diffCur.NoFriendlyViews && !flag)
				return false;
			return !World.cur().diffCur.NoEnemyViews || !flag;
		}
		if(actor instanceof Paratrooper)
		{
			if(World.cur().noParaTrooperViews)
				return false;
			if(World.cur().diffCur.NoFriendlyViews && !flag)
				return false;
			return !World.cur().diffCur.NoEnemyViews || !flag;
		}
		if(actor instanceof ActorViewPoint)
		{
			ActorViewPoint actorviewpoint = (ActorViewPoint)actor;
			if(actorviewpoint.getArmy() == 0)
				return true;
			int i = World.getPlayerAircraft().getArmy();
			if(actorviewpoint.getArmy() == i && World.cur().diffCur.NoFriendlyViews)
				return false;
			return actorviewpoint.getArmy() == i || !World.cur().diffCur.NoEnemyViews;
		} else
		{
			return false;
		}
	}

	private Actor nextViewActor(boolean flag)
	{
		if(Selector.isEnableTrackArgs())
			return Selector.setCurRecordArg0(Selector.getTrackArg0());
		int i = World.getPlayerArmy();
		namedAircraft.clear();
		Actor actor = Main3D.cur3D().viewActor();
		if(isViewed(actor))
			namedAircraft.put(actor.name(), null);
		for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
		{
			Actor actor1 = (Actor)entry.getValue();
			if(!isValidViewActor(actor1, flag) || !Actor.isValid(actor1) || actor1 == actor)
				continue;
			if(flag)
			{
				if(actor1.getArmy() != i)
					namedAircraft.put(actor1.name(), null);
				continue;
			}
			if(actor1.getArmy() == i)
				namedAircraft.put(actor1.name(), null);
		}

		if(namedAircraft.size() == 0)
			return Selector.setCurRecordArg0(null);
		if(!isViewed(actor))
			return Selector.setCurRecordArg0((Actor)Engine.name2Actor().get((String)namedAircraft.firstKey()));
		if(namedAircraft.size() == 1 && isViewed(actor))
			return Selector.setCurRecordArg0(null);
		namedAll = namedAircraft.keySet().toArray(namedAll);
		int j = 0;
		for(String s = actor.name(); namedAll[j] != null && !s.equals(namedAll[j]); j++);
		if(namedAll[j] == null)
			return Selector.setCurRecordArg0(null);
		j++;
		if(namedAll.length == j || namedAll[j] == null)
			j = 0;
		return Selector.setCurRecordArg0((Actor)Engine.name2Actor().get((String)namedAll[j]));
	}

	private boolean isViewed(Actor actor)
	{
		if(!Actor.isValid(actor))
			return false;
		else
			return (actor instanceof Aircraft) || (actor instanceof Paratrooper) || (actor instanceof ActorViewPoint) || (actor instanceof BigshipGeneric) && ((BigshipGeneric)actor).getAirport() != null;
	}

	private void checkSmartControlsUse()
	{
		useSmartAxisForPower = false;
		useSmartAxisForPitch = false;
		if(!World.cur().useSmartAxis)
			return;
		boolean aflag[] = {
				false, false, false, false
		};
		boolean aflag1[] = {
				false, false, false, false
		};
		String as[] = UserCfg.nameHotKeyEnvs;
		for(int i = 0; i < as.length; i++)
		{
			HotKeyEnv hotkeyenv = HotKeyEnv.env(as[i]);
			HashMapInt hashmapint = hotkeyenv.all();
			for(HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint.nextEntry(hashmapintentry))
			{
				int j = hashmapintentry.getKey();
				String s = (String)(String)hashmapintentry.getValue();
				if(s.startsWith("-"))
					s = s.substring(1);
				if(s.startsWith("power") && s.length() == 6)
				{
					int k = (new Integer(s.substring(5))).intValue();
					if(k >= 1 && k <= 4)
						aflag[k - 1] = true;
				}
				if(!s.startsWith("prop") || s.length() != 5)
					continue;
				int l = (new Integer(s.substring(4))).intValue();
				if(l >= 1 && l <= 4)
					aflag1[l - 1] = true;
			}

		}

		if(aflag[0] && aflag[1] && !aflag[2] && !aflag[3])
			useSmartAxisForPower = true;
		if(aflag1[0] && aflag1[1] && !aflag1[2] && !aflag1[3])
			useSmartAxisForPitch = true;
	}

	// TODO: Custom flap settings. Parameters are defined in the flight model and allow for numerous different flap settings.
	public boolean SetFlapsHotKeys (int direction, RealFlightModel RFM)
	{
		int i = direction % 2;	// even number >> 0 - flaps up, odd number >> 1 - flaps down
		switch (i) {
		case 0:
			if(!RFM.CT.bHasFlapsControlRed)
			{
				if(RFM.CT.FlapStage != null && RFM.CT.FlapStageMax != -1.0F){
					if(RFM.CT.FlapsControl > RFM.CT.FlapStage[flapIndex])
					{
						RFM.CT.FlapsControl = RFM.CT.FlapStage[flapIndex];
						HUD.log("Flaps: "+(RFM.CT.FlapStage[flapIndex]*RFM.CT.FlapStageMax)+" deg.");
						break;
					}
					if(RFM.CT.FlapsControl >  0.0F)
					{
						RFM.CT.FlapsControl = 0.0F;
						HUD.log("FlapsRaised");
					}
					break;
				}
				else{
					if(RFM.CT.FlapsControl > 0.33F)
					{
						RFM.CT.FlapsControl = 0.33F;
						HUD.log("FlapsTakeOff");
						break;
					}
					if(RFM.CT.FlapsControl > 0.2F)
					{
						RFM.CT.FlapsControl = 0.2F;
						HUD.log("FlapsCombat");
						break;
					}
					if(RFM.CT.FlapsControl > 0.0F)
					{
						RFM.CT.FlapsControl = 0.0F;
						HUD.log("FlapsRaised");
					}
					break;
				}
			}

			if(RFM.CT.FlapsControl > 0.5F)
			{
				RFM.CT.FlapsControl = 0.0F;
				HUD.log("FlapsRaised");
			}
			break;

		case 1:
			if(!RFM.CT.bHasFlapsControlRed)
			{
				if(RFM.CT.FlapStage != null && RFM.CT.FlapStageMax != -1.0F){
					if(RFM.CT.FlapsControl < RFM.CT.FlapStage[flapIndex])
					{
						RFM.CT.FlapsControl = RFM.CT.FlapStage[flapIndex];
						HUD.log("Flaps: "+(RFM.CT.FlapStage[flapIndex]*RFM.CT.FlapStageMax)+" deg.");
						break;
					}

					if(RFM.CT.FlapsControl <  1.0F)
					{
						RFM.CT.FlapsControl = 1.0F;
						HUD.log("Flaps: "+RFM.CT.FlapStageMax+" deg.");
					}
					break;
				}
				else{
					if(RFM.CT.FlapsControl < 0.2F)
					{
						RFM.CT.FlapsControl = 0.2F;
						HUD.log("FlapsCombat");
						break;
					}
					if(RFM.CT.FlapsControl < 0.33F)
					{
						RFM.CT.FlapsControl = 0.33F;
						HUD.log("FlapsTakeOff");
						break;
					}
					if(RFM.CT.FlapsControl < 1.0F)
					{
						RFM.CT.FlapsControl = 1.0F;
						HUD.log("FlapsLanding");
					}
					break;
				}
			}
			if(RFM.CT.FlapsControl  < 0.5F)
			{
				RFM.CT.FlapsControl = 1.0F;
				HUD.log("FlapsLanding");
			}
			break;
		default:
			break;
		}
		return true;
	}
	// PAS--

	// TODO: Custom flap settings. Parameters are defined in the flight model and allow for numerous different flap settings.
	public boolean SetVarWingHotKeys (int direction, RealFlightModel RFM)
	{
		int i = direction % 2;	// even number >> 0 - flaps up, odd number >> 1 - flaps down
		switch (i) {
		case 0:
			if(RFM.CT.VarWingStage != null && RFM.CT.VarWingStageMax != -1.0F){
				if(RFM.CT.VarWingControl > RFM.CT.VarWingStage[varWingIndex])
				{
					RFM.CT.VarWingControl = RFM.CT.VarWingStage[varWingIndex];
					HUD.log("Wings: "+(RFM.CT.VarWingStage[varWingIndex]*RFM.CT.VarWingStageMax)+" deg.");
					break;
				}
				if(RFM.CT.VarWingControl >  0.0F)
				{
					RFM.CT.VarWingControl = 0.0F;
					HUD.log("Wings Retracted");
				}
				break;
			}
		case 1:
			if(RFM.CT.VarWingStage != null && RFM.CT.VarWingStageMax != -1.0F){
				if(RFM.CT.VarWingControl < RFM.CT.VarWingStage[varWingIndex])
				{
					RFM.CT.VarWingControl = RFM.CT.VarWingStage[varWingIndex];
					HUD.log("Wings: "+(RFM.CT.VarWingStage[varWingIndex]*RFM.CT.VarWingStageMax)+" deg.");
					break;
				}

				if(RFM.CT.VarWingControl <  1.0F)
				{
					RFM.CT.VarWingControl = 1.0F;
					HUD.log("Wings: "+RFM.CT.VarWingStageMax+" deg.");
				}
				break;
			}
		default:
			break;
		}
		return true;
	}
}