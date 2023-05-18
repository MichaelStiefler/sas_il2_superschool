package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import com.maddox.il2.ai.World;
import com.maddox.il2.builder.ZutiMDSSection_AirDrops;
import com.maddox.il2.builder.ZutiMDSSection_Craters;
import com.maddox.il2.builder.ZutiMDSSection_HUD;
import com.maddox.il2.builder.ZutiMDSSection_MapIcons;
import com.maddox.il2.builder.ZutiMDSSection_Miscellaneous;
import com.maddox.il2.builder.ZutiMDSSection_NextMission;
import com.maddox.il2.builder.ZutiMDSSection_Objectives;
import com.maddox.il2.builder.ZutiMDSSection_RRR;
import com.maddox.il2.builder.ZutiMDSSection_Radar;
import com.maddox.il2.builder.ZutiMDSSection_StaticRespawn;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.builder.Zuti_WResourcesManagement.RRRItem;
import com.maddox.il2.fm.ZutiSupportMethods_FM;
import com.maddox.il2.gui.GUIPad;
import com.maddox.il2.gui.ZutiSupportMethods_GUI;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.effects.ZutiSupportMethods_Effects;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class ZutiMDSVariables
{
	public static boolean 	ZUTI_FRONT_ENABLE_HB_CAPTURING = true;
	public static boolean 	ZUTI_RADAR_IN_ADV_MODE = false;
	private static int[] 	ZUTI_ICON_SIZES = {	8,	//0
												12,	//1
												16,	//2
												20,	//3
												24,	//4
												28,	//5
												32,	//6
											};
	public static int 		ZUTI_ICON_SIZE = ZUTI_ICON_SIZES[4];
	
	public boolean zutiDrop_OverFriendlyHomeBase;
	public boolean zutiDrop_OverEnemyHomeBase;
	public boolean zutiDrop_OverNeutralHomeBase;
	public boolean zutiDrop_OverFriendlyFrictionArea;
	public boolean zutiDrop_OverEnemyFrictionArea;
	public boolean zutiDrop_OverDestroyGroundArea;
	public boolean zutiDrop_OverDefenceGroundArea;
	public int zutiDrop_MinHeight;
	public int zutiDrop_MaxHeight;
	
	public int zutiCraters_Bombs250_Visibility;
	public int zutiCraters_Bombs500_Visibility;
	public int zutiCraters_Bombs1000_Visibility;
	public int zutiCraters_Bombs2000_Visibility;
	public int zutiCraters_Bombs5000_Visibility;
	public int zutiCraters_Bombs9999_Visibility;
	public boolean zutiCraters_Bombs250_SyncOnline;
	public boolean zutiCraters_Bombs500_SyncOnline;
	public boolean zutiCraters_Bombs1000_SyncOnline;
	public boolean zutiCraters_Bombs2000_SyncOnline;
	public boolean zutiCraters_Bombs5000_SyncOnline;
	public boolean zutiCraters_Bombs9999_SyncOnline;
	public boolean zutiCraters_OnlyAreaHits;
	
	public boolean zutiHud_DisableHudStatistics;
	public boolean zutiHud_ShowPilotNumber;
	public boolean zutiHud_ShowPilotPing;
	public boolean zutiHud_ShowPilotName;
	public boolean zutiHud_ShowPilotScore;
	public boolean zutiHud_ShowPilotArmy;
	public boolean zutiHud_ShowPilotACDesignation;
	public boolean zutiHud_ShowPilotACType;
	
	public boolean zutiIcons_ShowAircraft;
	public boolean zutiIcons_ShowGroundUnits;
	public boolean zutiIcons_StaticIconsIfNoRadar;
	public boolean zutiIcons_AircraftIconsWhite;
	public boolean zutiIcons_HideUnpopulatedAirstripsFromMinimap;
	public boolean zutiIcons_ShowRockets;
	public boolean zutiIcons_ShowPlayerAircraft;
	public boolean zutiIcons_MovingIcons;
	public boolean zutiIcons_ShowTargets;
	public boolean zutiIcons_ShowNeutralHB;
	
	public boolean zutiMisc_DisableAIRadioChatter;
	public boolean zutiMisc_DespawnAIPlanesAfterLanding;
	public boolean zutiMisc_HidePlayersCountOnHomeBase;
	public boolean zutiMisc_EnableReflyOnlyIfBailedOrDied;
	public boolean zutiMisc_DisableReflyForMissionDuration;
	public boolean zutiMisc_DisableVectoring;
	public int zutiMisc_ReflyKIADelay;
	public int zutiMisc_MaxAllowedKIA;
	public float zutiMisc_ReflyKIADelayMultiplier;
	public boolean zutiMisc_EnableAIAirborneMulticrew;
	public boolean zutiMisc_EnableInstructor;
	public boolean zutiMisc_EnableTowerCommunications;
	
	public String zutiNextMission_RedWon;
	public String zutiNextMission_BlueWon;
	public String zutiNextMission_Difficulty;
	public int zutiNextMission_LoadDelay;
	public boolean zutiNextMission_Enable;
	
	public boolean zutiObjectives_enable;
	public boolean zutiObjectives_enableRed;
	public boolean zutiObjectives_enableBlue;
	public int zutiObjectives_redShipsS;
	public int zutiObjectives_redShipsM;
	public int zutiObjectives_redArtillery;
	public int zutiObjectives_redStationary;
	public int zutiObjectives_redArmor;
	public int zutiObjectives_redVehicles;
	public int zutiObjectives_redTrains;
	public int zutiObjectives_redAircraftsS;
	public int zutiObjectives_redAircraftsM;
	public int zutiObjectives_redRockets;
	public int zutiObjectives_redInfantry;
	public int zutiObjectives_redHomeBases;
	public int zutiObjectives_blueShipsS;
	public int zutiObjectives_blueShipsM;
	public int zutiObjectives_blueArtillery;
	public int zutiObjectives_blueStationary;
	public int zutiObjectives_blueArmor;
	public int zutiObjectives_blueVehicles;
	public int zutiObjectives_blueTrains;
	public int zutiObjectives_blueAircraftsS;
	public int zutiObjectives_blueAircraftsM;
	public int zutiObjectives_blueRockets;
	public int zutiObjectives_blueInfantry;
	public int zutiObjectives_blueHomeBases;
	
	public int zutiRadar_RefreshInterval;
	public boolean zutiRadar_ShipsAsRadar;
	public int zutiRadar_ShipRadar_MaxRange;
	public int zutiRadar_ShipRadar_MinHeight;
	public int zutiRadar_ShipRadar_MaxHeight;
	public int zutiRadar_ShipSmallRadar_MaxRange;
	public int zutiRadar_ShipSmallRadar_MinHeight;
	public int zutiRadar_ShipSmallRadar_MaxHeight;
	public boolean zutiRadar_ScoutsAsRadar;
	public int zutiRadar_ScoutRadar_MaxRange;
	public int zutiRadar_ScoutRadar_DeltaHeight;
	public int zutiRadar_ScoutGroundObjects_Alpha;
	public boolean zutiRadar_ScoutCompleteRecon;
	public boolean zutiRadar_EnableBigShip_Radar;
	public boolean zutiRadar_EnableSmallShip_Radar;
	public List scoutsRed;
	public List scoutsBlue;
	public boolean zutiRadar_PlayerSideHasRadars;
	
	public int zutiReload_OneMgCannonRearmSecond;
	public int zutiReload_OneBombFTankTorpedoeRearmSeconds;
	public int zutiReload_OneRocketRearmSeconds;
	public int zutiReload_GallonsLitersPerSecond;
	public int zutiReload_OneWeaponRepairSeconds;
	public int zutiReload_FlapsRepairSeconds;
	public boolean zutiReload_ReloadOnlyIfFuelTanksExist;
	public boolean zutiReload_ReloadOnlyIfAmmoBoxesExist;
	public boolean zutiReload_RepairOnlyIfWorkshopExist;
	public int zutiReload_EngineRepairSeconds;
	public int zutiReload_OneControlCableRepairSeconds;
	public int zutiReload_OneFuelOilTankRepairSeconds;
	public int zutiReload_LoadoutChangePenaltySeconds;
	public boolean zutiReload_OnlyHomeBaseSpecificLoadouts;
	public int zutiReload_CockpitRepairSeconds;

	public Map objectsMap_Red = null;
	public Map objectsMap_Blue = null;
	public boolean enabledResourcesManagement_BySide = false;
	public boolean enabledResourcesManagement_HomeBases = false;
	
	public long zutiBulletsSupply_Red = 0;
	public int[] zutiBombsSupply_Red = new int[]{0, 0, 0, 0, 0, 0};
	public long zutiRocketsSupply_Red = 0;
	public long zutiFuelSupply_Red = 0;
	public long zutiEnginesSupply_Red = 0;
	public long zutiRepairKitsSupply_Red = 0;
	
	public long zutiBulletsSupply_Blue = 0;
	public int[] zutiBombsSupply_Blue = new int[]{0, 0, 0, 0, 0, 0};
	public long zutiRocketsSupply_Blue = 0;
	public long zutiFuelSupply_Blue = 0;
	public long zutiEnginesSupply_Blue = 0;
	public long zutiRepairKitsSupply_Blue = 0;
	
	public int respawnTime_Bigship;
	public int respawnTime_Ship;
	public int respawnTime_Aeroanchored;
	public int respawnTime_Artillery;
	public int respawnTime_Searchlight;
	
	public void loadVariables(SectFile sectfile)
	{
		loadAirDropsVariables(sectfile);
		loadCratersVariables(sectfile);
		loadHUDVariables(sectfile);
		loadMapIconsVariables(sectfile);
		loadMiscellaneousVariables(sectfile);
		loadNextMissionVariables(sectfile);
		loadObjectivesVariables(sectfile);
		loadRadarVariables(sectfile);
		loadRRRVariables(sectfile);
		loadStaticRespawnVariables(sectfile);
		
		loadAOCVariables(sectfile);
		
		if( sectfile.sectionIndex("MDS") > -1 )
			loadOldMDS(sectfile);
	}
	
	private void loadAirDropsVariables(SectFile sectfile)
	{
		try
		{
			zutiDrop_OverFriendlyHomeBase = false;
			if( sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_OverFriendlyHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverFriendlyHomeBase = true;
			zutiDrop_OverEnemyHomeBase = false;
			if( sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_OverEnemyHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverEnemyHomeBase = true;
			zutiDrop_OverNeutralHomeBase = false;
			if( sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_OverNeutralHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverNeutralHomeBase = true;
			zutiDrop_OverFriendlyFrictionArea = false;
			if( sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_OverFriendlyFrictionArea", 0, 0, 1) == 1 )
				zutiDrop_OverFriendlyFrictionArea = true;
			zutiDrop_OverEnemyFrictionArea = true;
			if( sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_OverEnemyFrictionArea", 1, 0, 1) == 0 )
				zutiDrop_OverEnemyFrictionArea = false;
			zutiDrop_OverDestroyGroundArea = true;
			if( sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_OverDestroyGroundArea", 1, 0, 1) == 0 )
				zutiDrop_OverDestroyGroundArea = false;
			zutiDrop_OverDefenceGroundArea = true;
			if( sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_OverDefenceGroundArea", 1, 0, 1) == 0 )
				zutiDrop_OverDefenceGroundArea = false;
			zutiDrop_MinHeight = sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_MinHeight", 0, 0, 99999);
			zutiDrop_MaxHeight = sectfile.get(ZutiMDSSection_AirDrops.SECTION_ID, "ZutiDrop_MaxHeight", 10000, 0, 99999);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void loadCratersVariables(SectFile sectfile)
	{
		try
		{
			zutiCraters_Bombs250_Visibility = sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs250_Visibility", 80, 1, 99999);
			zutiCraters_Bombs500_Visibility = sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs500_Visibility", 80, 1, 99999);
			zutiCraters_Bombs1000_Visibility = sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs1000_Visibility", 80, 1, 99999);
			zutiCraters_Bombs2000_Visibility = sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs2000_Visibility", 80, 1, 99999);
			zutiCraters_Bombs5000_Visibility = sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs5000_Visibility", 450, 1, 99999);
			zutiCraters_Bombs9999_Visibility = sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs9999_Visibility", 900, 1, 99999);
			
			zutiCraters_Bombs250_SyncOnline = false;
			if( sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs250_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs250_SyncOnline = true;
			zutiCraters_Bombs500_SyncOnline = false;
			if( sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs500_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs500_SyncOnline = true;
			zutiCraters_Bombs1000_SyncOnline = false;
			if( sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs1000_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs1000_SyncOnline = true;
			zutiCraters_Bombs2000_SyncOnline = false;
			if( sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs2000_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs2000_SyncOnline = true;
			zutiCraters_Bombs5000_SyncOnline = false;
			if( sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs5000_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs5000_SyncOnline = true;
			zutiCraters_Bombs9999_SyncOnline = false;
			if( sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_Bombs9999_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs9999_SyncOnline = true;
			
			zutiCraters_OnlyAreaHits = true;
			if( sectfile.get(ZutiMDSSection_Craters.SECTION_ID, "ZutiCraters_OnlyAreaHits", 1, 0, 1) == 0 )
				zutiCraters_OnlyAreaHits = false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadHUDVariables(SectFile sectfile)
	{
		try
		{
			zutiHud_DisableHudStatistics = false;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_DisableHudStatistics", 0, 0, 1) == 1 )
				zutiHud_DisableHudStatistics = true;
			zutiHud_ShowPilotNumber = false;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_ShowPilotNumber", 0, 0, 1) == 1 )
				zutiHud_ShowPilotNumber = true;
			zutiHud_ShowPilotPing = true;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_ShowPilotPing", 1, 0, 1) == 0 )
				zutiHud_ShowPilotPing = false;
			zutiHud_ShowPilotName = true;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_ShowPilotName", 1, 0, 1) == 0 )
				zutiHud_ShowPilotName = false;
			zutiHud_ShowPilotScore = true;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_ShowPilotScore", 1, 0, 1) == 0 )
				zutiHud_ShowPilotScore = false;
			zutiHud_ShowPilotArmy = true;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_ShowPilotArmy", 1, 0, 1) == 0 )
				zutiHud_ShowPilotArmy = false;
			zutiHud_ShowPilotACDesignation = false;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_ShowPilotACDesignation", 0, 0, 1) == 1 )
				zutiHud_ShowPilotACDesignation = true;
			zutiHud_ShowPilotACType = true;
			if( sectfile.get(ZutiMDSSection_HUD.SECTION_ID, "ZutiHud_ShowPilotACType", 1, 0, 1) == 0 )
				zutiHud_ShowPilotACType = false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadMapIconsVariables(SectFile sectfile)
	{
		try
		{
			zutiIcons_MovingIcons = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_MovingIcons", 0, 0, 1) == 1 )
				zutiIcons_MovingIcons = true;
			
			zutiIcons_StaticIconsIfNoRadar = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiRadar_StaticIconsIfNoRadar", 0, 0, 1) == 1 )
				zutiIcons_StaticIconsIfNoRadar = true;
				
			zutiIcons_ShowTargets = true;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_ShowTargets", 1, 0, 1) == 0 )
				zutiIcons_ShowTargets = false;
				
			ZutiMDSVariables.ZUTI_ICON_SIZE = ZUTI_ICON_SIZES[sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_IconsSize", 4, 0, 6)];
			
			zutiIcons_ShowNeutralHB = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_ShowNeutralHB", 0, 0, 1) == 1 )
				zutiIcons_ShowNeutralHB = true;
			
			zutiIcons_ShowAircraft = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_ShowAircraft", 0, 0, 1) == 1 )
				zutiIcons_ShowAircraft = true;
				
			zutiIcons_ShowGroundUnits = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_ShowGroundUnits", 0, 0, 1) == 1 )
				zutiIcons_ShowGroundUnits = true;
			
			zutiIcons_ShowRockets = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_ShowRockets", 0, 0, 1) == 1 )
				zutiIcons_ShowRockets = true;
			
			zutiIcons_AircraftIconsWhite = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_AircraftIconsWhite", 0, 0, 1) == 1 )
				zutiIcons_AircraftIconsWhite = true;
			
			zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1 )
				zutiIcons_HideUnpopulatedAirstripsFromMinimap = true;
				
			zutiIcons_ShowPlayerAircraft = true;
			if( sectfile.get(ZutiMDSSection_MapIcons.SECTION_ID, "ZutiIcons_ShowPlayerAircraft", 1, 0, 1) == 0 )
				zutiIcons_ShowPlayerAircraft = false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadMiscellaneousVariables(SectFile sectfile)
	{
		try
		{
			zutiMisc_EnableTowerCommunications = true;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_EnableTowerCommunications", 1, 0, 1) == 0 )
				zutiMisc_EnableTowerCommunications = false;
			zutiMisc_DisableAIRadioChatter = false;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_DisableAIRadioChatter", 0, 0, 1) == 1 )
				zutiMisc_DisableAIRadioChatter = true;
			zutiMisc_DespawnAIPlanesAfterLanding = true;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0 )
				zutiMisc_DespawnAIPlanesAfterLanding = false;
			zutiMisc_HidePlayersCountOnHomeBase = false;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1 )
				zutiMisc_HidePlayersCountOnHomeBase = true;
			zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_EnableReflyOnlyIfBailedOrDied", 0, 0, 1) == 1 )
				zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
			zutiMisc_DisableReflyForMissionDuration = false;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_DisableReflyForMissionDuration", 0, 0, 1) == 1 )
				zutiMisc_DisableReflyForMissionDuration = true;
			this.zutiMisc_DisableVectoring = false;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_DisableVectoring", 0, 0, 1) == 1 )
				this.zutiMisc_DisableVectoring = true;
			zutiMisc_ReflyKIADelay = sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_ReflyKIADelay", 15, 0, 99999);
			zutiMisc_MaxAllowedKIA = sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_MaxAllowedKIA", 10, 1, 99999);
			zutiMisc_ReflyKIADelayMultiplier = sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_ReflyKIADelayMultiplier", 2.0F, 1.0F, 99999.0F);
			zutiMisc_EnableAIAirborneMulticrew = false;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_EnableAIAirborneMulticrew", 0, 0, 1) == 1 )
				zutiMisc_EnableAIAirborneMulticrew = true;
			zutiMisc_EnableInstructor = false;
			if( sectfile.get(ZutiMDSSection_Miscellaneous.SECTION_ID, "ZutiMisc_EnableInstructor", 0, 0, 1) == 1 )
				zutiMisc_EnableInstructor = true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadNextMissionVariables(SectFile sectfile)
	{
		try
		{
			zutiNextMission_RedWon = sectfile.get(ZutiMDSSection_NextMission.SECTION_ID, "ZutiNextMission_RedWon", "");
			zutiNextMission_BlueWon = sectfile.get(ZutiMDSSection_NextMission.SECTION_ID, "ZutiNextMission_BlueWon", "");
			zutiNextMission_Difficulty = sectfile.get(ZutiMDSSection_NextMission.SECTION_ID, "ZutiNextMission_Difficulty", "");
			zutiNextMission_LoadDelay = sectfile.get(ZutiMDSSection_NextMission.SECTION_ID, "ZutiNextMission_LoadDelay", 60, 60, 99999);
			zutiNextMission_Enable = false;
			if( sectfile.get(ZutiMDSSection_NextMission.SECTION_ID, "ZutiNextMission_Enable", 0, 0, 1) == 1 )
				zutiNextMission_Enable = true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadObjectivesVariables(SectFile sectfile)
	{
		try
		{
			int sectionId = sectfile.sectionIndex(ZutiMDSSection_Objectives.SECTION_ID);			
			if(sectionId > -1)
			{
				zutiObjectives_enableRed 		= true;
				zutiObjectives_enableBlue 		= true;
				zutiObjectives_enable 			= true;
				
				zutiObjectives_redArmor 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedArmor", 0, 0, 99999);//, default, min, max)
				zutiObjectives_redTrains 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "zutiObjective_RedTrains", 0, 0, 99999);
				zutiObjectives_redVehicles 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedVehicles", 0, 0, 99999);
				zutiObjectives_redShipsM 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjectives_RedShipsM", 0, 0, 99999);
				zutiObjectives_redAircraftsM 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedAircraftM", 0, 0, 99999);
				zutiObjectives_redShipsS 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedShipsS", 0, 0, 99999);
				zutiObjectives_redAircraftsS 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedAircraftS", 0, 0, 99999);
				zutiObjectives_redArtillery 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedArtillery", 0, 0, 99999);
				zutiObjectives_redStationary 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedStationary", 0, 0, 99999);
				zutiObjectives_redRockets	 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedRockets", 0, 0, 99999);
				zutiObjectives_redInfantry	 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedInfantry", 0, 0, 99999);
				zutiObjectives_redHomeBases 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_RedHomeBases", 0, 0, 99999);
				
				zutiObjectives_blueArmor 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueArmor", 0, 0, 99999);//, default, min, max)
				zutiObjectives_blueTrains 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "zutiObjective_BlueTrains", 0, 0, 99999);
				zutiObjectives_blueVehicles 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueVehicles", 0, 0, 99999);
				zutiObjectives_blueShipsM 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjectives_BlueShipsM", 0, 0, 99999);
				zutiObjectives_blueAircraftsM 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueAircraftM", 0, 0, 99999);
				zutiObjectives_blueShipsS 		= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueShipsS", 0, 0, 99999);
				zutiObjectives_blueAircraftsS 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueAircraftS", 0, 0, 99999);
				zutiObjectives_blueArtillery 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueArtillery", 0, 0, 99999);
				zutiObjectives_blueStationary 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueStationary", 0, 0, 99999);
				zutiObjectives_blueRockets	 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueRockets", 0, 0, 99999);
				zutiObjectives_blueInfantry	 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueInfantry", 0, 0, 99999);
				zutiObjectives_blueHomeBases 	= sectfile.get(ZutiMDSSection_Objectives.SECTION_ID, "ZutiObjective_BlueHomeBases", 0, 0, 99999);
				
				//If both armies "completed" their objectives at load time - they were not set at all
				if( ZutiSupportMethods.isMDSObjectivesCompleted(1) )
					zutiObjectives_enableRed = false;
				if( ZutiSupportMethods.isMDSObjectivesCompleted(2) )
					zutiObjectives_enableBlue = false;
				
				if( !zutiObjectives_enableRed && !zutiObjectives_enableBlue )
					zutiObjectives_enable = false;
			}
			else
			{
				zutiObjectives_enableRed = false;
				zutiObjectives_enableBlue = false;
				zutiObjectives_enable = false;
				return;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadRadarVariables(SectFile sectfile)
	{
		try
		{
			zutiRadar_ShipsAsRadar = false;
			if( sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ShipsAsRadar", 0, 0, 1) == 1 )
				zutiRadar_ShipsAsRadar = true;
			zutiRadar_ShipRadar_MaxRange = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ShipRadar_MaxRange", 100, 1, 99999);
			zutiRadar_ShipRadar_MinHeight = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ShipRadar_MinHeight", 100, 0, 99999);
			zutiRadar_ShipRadar_MaxHeight = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ShipRadar_MaxHeight", 5000, 1000, 99999);
			zutiRadar_ShipSmallRadar_MaxRange = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ShipSmallRadar_MaxRange", 25, 1, 99999);
			zutiRadar_ShipSmallRadar_MinHeight = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ShipSmallRadar_MinHeight", 0, 0, 99999);
			zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ShipSmallRadar_MaxHeight", 2000, 1000, 99999);	
			
			zutiRadar_ScoutsAsRadar = false;
			if( sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ScoutsAsRadar", 0, 0, 1) == 1 )
				zutiRadar_ScoutsAsRadar = true;
			zutiRadar_ScoutRadar_MaxRange = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ScoutRadar_MaxRange", 2, 1, 99999);
			zutiRadar_ScoutRadar_DeltaHeight = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ScoutRadar_DeltaHeight", 1500, 100, 99999);
			
			zutiRadar_ScoutCompleteRecon = false;
			if( sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ScoutCompleteRecon", 0, 0, 1) == 1 )
				zutiRadar_ScoutCompleteRecon = true;

			zutiRadar_ScoutGroundObjects_Alpha = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_ScoutGroundObjects_Alpha", 5, 1, 11);//, default, min, max)
			zutiRadar_RefreshInterval = sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_RefreshInterval", 0, 0, 99999);
			zutiRadar_RefreshInterval *= 1000; //so that we get milliseconds
			
			zutiLoadScouts_Red(sectfile);
			zutiLoadScouts_Blue(sectfile);
			
			ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
			if( sectfile.get(ZutiMDSSection_Radar.SECTION_ID, "ZutiRadar_SetRadarToAdvanceMode", 0, 0, 1) == 1 )
				ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = true;
			
			zutiSetShipRadars();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	private void zutiLoadScouts_Red(SectFile sectfile)
	{
		int index = sectfile.sectionIndex("MDS_Scouts_Red");
		if( index > -1 )
		{
			if( scoutsRed == null )
				scoutsRed = new ArrayList();
			scoutsRed.clear();
			
			int lines = sectfile.vars(index);
			for (int i=0; i<lines; i++)
			{
				String line = sectfile.line(index, i);
				StringTokenizer stringtokenizer = new StringTokenizer(line);
				String acName = null;
				
				while( stringtokenizer.hasMoreTokens() )
				{
					acName = stringtokenizer.nextToken();
					break;
				}
				if (acName != null)
				{
					acName = acName.intern();
					Class var_class = ((Class) Property.value(acName, "airClass", null));
					
					//if (var_class != null && Property.containsValue(var_class, "cockpitClass"))
					if (var_class != null)
					{
						//Add this ac to modified table for this home base
						scoutsRed.add(acName);
					}
				}
			}
		}
	}
	private void zutiLoadScouts_Blue(SectFile sectfile)
	{
		int index = sectfile.sectionIndex("MDS_Scouts_Blue");
		if( index > -1 )
		{
			if( scoutsBlue == null )
				scoutsBlue = new ArrayList();
			scoutsBlue.clear();
			
			int lines = sectfile.vars(index);
			for (int i=0; i<lines; i++)
			{
				String line = sectfile.line(index, i);
				StringTokenizer stringtokenizer = new StringTokenizer(line);
				String acName = null;
				
				while( stringtokenizer.hasMoreTokens() )
				{
					acName = stringtokenizer.nextToken();
					break;
				}
				if (acName != null)
				{
					acName = acName.intern();
					Class var_class = ((Class) Property.value(acName, "airClass", null));
					
					//if (var_class != null && Property.containsValue(var_class, "cockpitClass"))
					if (var_class != null)
					{
						//Add this ac to modified table for this home base
						scoutsBlue.add(acName);
					}
				}
			}
		}
	}
	private void zutiSetShipRadars()
	{
		zutiRadar_EnableBigShip_Radar = true;
		zutiRadar_EnableSmallShip_Radar = true;
		
		if( zutiRadar_ShipRadar_MaxHeight == 0 && zutiRadar_ShipRadar_MaxRange == 0 && zutiRadar_ShipRadar_MinHeight == 0 )
			zutiRadar_EnableBigShip_Radar = false;
		
		if( zutiRadar_ShipSmallRadar_MaxHeight == 0 && zutiRadar_ShipSmallRadar_MaxRange == 0 && zutiRadar_ShipSmallRadar_MinHeight == 0 )
			zutiRadar_EnableSmallShip_Radar = false;
	}
	
	private void loadRRRVariables(SectFile sectfile)
	{
		try
		{
			zutiReload_OneMgCannonRearmSecond = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_OneMgCannonRearmSecond", 10, 0, 99999);
			zutiReload_OneBombFTankTorpedoeRearmSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_OneBombFTankTorpedoeRearmSeconds", 25, 0, 99999);
			zutiReload_OneRocketRearmSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_OneRocketRearmSeconds", 20, 0, 99999);
			zutiReload_GallonsLitersPerSecond = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_GallonsLitersPerSecond", 3, 0, 99999);
			zutiReload_OneWeaponRepairSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_OneWeaponRepairSeconds", 3, 0, 99999);
			zutiReload_FlapsRepairSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_FlapsRepairSeconds", 30, 0, 99999);
			zutiReload_EngineRepairSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_EngineRepairSeconds", 90, 0, 99999);
			zutiReload_CockpitRepairSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_CockpitRepairSeconds", 30, 0, 99999);
			zutiReload_OneControlCableRepairSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_OneControlCableRepairSeconds", 15, 0, 99999);
			zutiReload_OneFuelOilTankRepairSeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_OneFuelOilTankRepairSeconds", 20, 0, 99999);
			zutiReload_LoadoutChangePenaltySeconds = sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_LoadoutChangePenaltySeconds", 30, 0, 99999);
			zutiReload_ReloadOnlyIfFuelTanksExist = false;
			if( sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_ReloadOnlyIfFuelTanksExist", 0, 0, 1) == 1 )
				zutiReload_ReloadOnlyIfFuelTanksExist = true;
			zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
			if( sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_ReloadOnlyIfAmmoBoxesExist", 0, 0, 1) == 1 )
				zutiReload_ReloadOnlyIfAmmoBoxesExist = true;
			zutiReload_RepairOnlyIfWorkshopExist = false;
			if( sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_RepairOnlyIfWorkshopExist", 0, 0, 1) == 1 )
				zutiReload_RepairOnlyIfWorkshopExist = true;
			zutiReload_OnlyHomeBaseSpecificLoadouts = true;
			if( sectfile.get(ZutiMDSSection_RRR.SECTION_ID, "ZutiReload_OnlyHomeBaseSpecificLoadouts", 1, 0, 1) == 0 )
				zutiReload_OnlyHomeBaseSpecificLoadouts = false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadStaticRespawnVariables(SectFile sectfile)
	{
		try
		{
			respawnTime_Bigship = sectfile.get(ZutiMDSSection_StaticRespawn.SECTION_ID, "Bigship", 1800, 0, 1200000);
			respawnTime_Ship = sectfile.get(ZutiMDSSection_StaticRespawn.SECTION_ID, "Ship", 1800, 0, 1200000);
			respawnTime_Aeroanchored = sectfile.get(ZutiMDSSection_StaticRespawn.SECTION_ID, "Aeroanchored", 1800, 0, 1200000);
			respawnTime_Artillery = sectfile.get(ZutiMDSSection_StaticRespawn.SECTION_ID, "Artillery", 1800, 0, 1200000);
			respawnTime_Searchlight = sectfile.get(ZutiMDSSection_StaticRespawn.SECTION_ID, "Searchlight", 1800, 0, 1200000);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadOldMDS(SectFile sectfile)
	{
		this.zutiNextMission_RedWon = sectfile.get("MDS", "ZutiNextMission_RedWon");
		this.zutiNextMission_BlueWon = sectfile.get("MDS", "ZutiNextMission_BlueWon");
		this.zutiNextMission_Difficulty = sectfile.get("MDS", "ZutiNextMission_Difficulty");
		this.zutiNextMission_LoadDelay = sectfile.get("MDS", "ZutiNextMission_LoadDelay", 60, 60, 99999);
		
		this.zutiIcons_MovingIcons = false;
		if( sectfile.get("MDS", "ZutiTargets_MovingIcons", 0, 0, 1) == 1 )
			this.zutiIcons_MovingIcons = true;
			
		this.zutiNextMission_Enable = false;
		if( sectfile.get("MDS", "ZutiNextMission_Enable", 0, 0, 1) == 1 )
			this.zutiNextMission_Enable = true;
		
		this.zutiIcons_ShowTargets = true;
		if( sectfile.get("MDS", "ZutiTargets_ShowTargets", 1, 0, 1) == 0 )
			this.zutiIcons_ShowTargets = false;
			
		this.zutiIcons_ShowNeutralHB = false;
		if( sectfile.get("MDS", "ZutiIcons_ShowNeutralHB", 0, 0, 1) == 1 )
			this.zutiIcons_ShowNeutralHB = true;
		
		this.zutiIcons_ShowAircraft = false;
		if( sectfile.get("MDS", "ZutiRadar_ShowAircraft", 0, 0, 1) == 1 )
			this.zutiIcons_ShowAircraft = true;
		
		this.zutiIcons_StaticIconsIfNoRadar = false;
		if( sectfile.get("MDS", "ZutiRadar_StaticIconsIfNoRadar", 0, 0, 1) == 1 )
			this.zutiIcons_StaticIconsIfNoRadar = true;
		
		this.zutiRadar_ShipsAsRadar = false;
		if( sectfile.get("MDS", "ZutiRadar_ShipsAsRadar", 0, 0, 1) == 1 )
			this.zutiRadar_ShipsAsRadar = true;
		this.zutiIcons_ShowGroundUnits = false;
		if( sectfile.get("MDS", "ZutiRadar_ShowGroundUnits", 0, 0, 1) == 1 )
			this.zutiIcons_ShowGroundUnits = true;
		this.zutiRadar_ShipRadar_MaxRange = sectfile.get("MDS", "ZutiRadar_ShipRadar_MaxRange", 100, 1, 99999);
		this.zutiRadar_ShipRadar_MinHeight = sectfile.get("MDS", "ZutiRadar_ShipRadar_MinHeight", 100, 0, 99999);
		this.zutiRadar_ShipRadar_MaxHeight = sectfile.get("MDS", "ZutiRadar_ShipRadar_MaxHeight", 5000, 1000, 99999);
		this.zutiRadar_ShipSmallRadar_MaxRange = sectfile.get("MDS", "ZutiRadar_ShipSmallRadar_MaxRange", 25, 1, 99999);
		this.zutiRadar_ShipSmallRadar_MinHeight = sectfile.get("MDS", "ZutiRadar_ShipSmallRadar_MinHeight", 0, 0, 99999);
		this.zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get("MDS", "ZutiRadar_ShipSmallRadar_MaxHeight", 2000, 1000, 99999);				
			
		this.zutiRadar_ScoutsAsRadar = false;
		if( sectfile.get("MDS", "ZutiRadar_ScoutsAsRadar", 0, 0, 1) == 1 )
			this.zutiRadar_ScoutsAsRadar = true;
		this.zutiRadar_ScoutRadar_MaxRange = sectfile.get("MDS", "ZutiRadar_ScoutRadar_MaxRange", 2, 1, 99999);
		this.zutiRadar_ScoutRadar_DeltaHeight = sectfile.get("MDS", "ZutiRadar_ScoutRadar_DeltaHeight", 1500, 100, 99999);
		
		this.zutiRadar_ScoutCompleteRecon = false;
		if( sectfile.get("MDS", "ZutiRadar_ScoutCompleteRecon", 0, 0, 1) == 1 )
			this.zutiRadar_ScoutCompleteRecon = true;
		
		zutiLoadScouts_Red(sectfile);
		zutiLoadScouts_Blue(sectfile);
		
		this.zutiIcons_ShowRockets = false;
		if( sectfile.get("MDS", "ZutiRadar_ShowRockets", 0, 0, 1) == 1 )
			this.zutiIcons_ShowRockets = true;
		
		this.zutiIcons_ShowPlayerAircraft = true;
		if( sectfile.get("MDS", "ZutiRadar_ShowPlayerAircraft", 1, 0, 1) == 0 )
			this.zutiIcons_ShowPlayerAircraft = false;
		
		this.zutiRadar_RefreshInterval = sectfile.get("MDS", "ZutiRadar_RefreshInterval", 0, 0, 99999) * 1000;//to get ms
		this.zutiRadar_RefreshInterval *= 1000; //we must have milliseconds
		this.zutiIcons_AircraftIconsWhite = false;
		if( sectfile.get("MDS", "ZutiRadar_AircraftIconsWhite", 0, 0, 1) == 1 )
			this.zutiIcons_AircraftIconsWhite = true;
		
		this.zutiMisc_EnableTowerCommunications = true;
		if( sectfile.get("MDS", "ZutiRadar_EnableTowerCommunications", 1, 0, 1) == 0 )
			this.zutiMisc_EnableTowerCommunications = false;	
		
		ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
		if( sectfile.get("MDS", "ZutiRadar_SetRadarToAdvanceMode", 0, 0, 1) == 1 )
			ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = true;
		
		this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
		if( sectfile.get("MDS", "ZutiRadar_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1 )
			this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = true;
		
		this.zutiRadar_ScoutGroundObjects_Alpha = sectfile.get("MDS", "ZutiRadar_ScoutGroundObjects_Alpha", 5, 1, 11);//, default, min, max)
		
		this.zutiHud_DisableHudStatistics = false;
		if( sectfile.get("MDS", "ZutiHud_DisableHudStatistics", 0, 0, 1) == 1 )
			this.zutiHud_DisableHudStatistics = true;
		this.zutiHud_ShowPilotNumber = false;
		if( sectfile.get("MDS", "ZutiHud_ShowPilotNumber", 0, 0, 1) == 1 )
			this.zutiHud_ShowPilotNumber = true;
		this.zutiHud_ShowPilotPing = true;
		if( sectfile.get("MDS", "ZutiHud_ShowPilotPing", 1, 0, 1) == 0 )
			this.zutiHud_ShowPilotPing = false;
		this.zutiHud_ShowPilotName = true;
		if( sectfile.get("MDS", "ZutiHud_ShowPilotName", 1, 0, 1) == 0 )
			this.zutiHud_ShowPilotName = false;
		this.zutiHud_ShowPilotScore = true;
		if( sectfile.get("MDS", "ZutiHud_ShowPilotScore", 1, 0, 1) == 0 )
			this.zutiHud_ShowPilotScore = false;
		this.zutiHud_ShowPilotArmy = true;
		if( sectfile.get("MDS", "ZutiHud_ShowPilotArmy", 1, 0, 1) == 0 )
			this.zutiHud_ShowPilotArmy = false;
		this.zutiHud_ShowPilotACDesignation = false;
		if( sectfile.get("MDS", "ZutiHud_ShowPilotACDesignation", 0, 0, 1) == 1 )
			this.zutiHud_ShowPilotACDesignation = true;
		this.zutiHud_ShowPilotACType = true;
		if( sectfile.get("MDS", "ZutiHud_ShowPilotACType", 1, 0, 1) == 0 )
			this.zutiHud_ShowPilotACType = false;
		
		this.zutiMisc_DisableAIRadioChatter = false;
		if( sectfile.get("MDS", "ZutiMisc_DisableAIRadioChatter", 0, 0, 1) == 1 )
			this.zutiMisc_DisableAIRadioChatter = true;
		this.zutiMisc_DespawnAIPlanesAfterLanding = true;
		if( sectfile.get("MDS", "ZutiMisc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0 )
			this.zutiMisc_DespawnAIPlanesAfterLanding = false;
		this.zutiMisc_HidePlayersCountOnHomeBase = false;
		if( sectfile.get("MDS", "ZutiMisc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1 )
			this.zutiMisc_HidePlayersCountOnHomeBase = true;
		this.zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
		if( sectfile.get("MDS", "ZutiMisc_EnableReflyOnlyIfBailedOrDied", 0, 0, 1) == 1 )
			this.zutiMisc_EnableReflyOnlyIfBailedOrDied = true;		
		this.zutiMisc_DisableReflyForMissionDuration = false;
		if( sectfile.get("MDS", "ZutiMisc_DisableReflyForMissionDuration", 0, 0, 1) == 1 )
			this.zutiMisc_DisableReflyForMissionDuration = true;
		this.zutiMisc_EnableAIAirborneMulticrew = false;
		if( sectfile.get("MDS", "ZutiMisc_EnableAIAirborneMulticrew", 0, 0, 1) == 1 )
			this.zutiMisc_EnableAIAirborneMulticrew = true;
		this.zutiMisc_DisableVectoring = false;
		if( sectfile.get("MDS", "ZutiMisc_DisableVectoring", 0, 0, 1) == 1 )
			this.zutiMisc_DisableVectoring = true;
		this.zutiMisc_EnableInstructor = false;
		if( sectfile.get("MDS", "ZutiMisc_EnableInstructor", 0, 0, 1) == 1 )
			this.zutiMisc_EnableInstructor = true;
		
		this.zutiMisc_ReflyKIADelay = sectfile.get("MDS", "ZutiMisc_ReflyKIADelay", 15, 0, 6000);
		this.zutiMisc_MaxAllowedKIA = sectfile.get("MDS", "ZutiMisc_MaxAllowedKIA", 10, 1, 600);
		this.zutiMisc_ReflyKIADelayMultiplier = sectfile.get("MDS", "ZutiMisc_ReflyKIADelayMultiplier", 2.0F, 1.0F, 600.0F);
		
		ZutiMDSVariables.ZUTI_ICON_SIZE = ZUTI_ICON_SIZES[sectfile.get("MDS", "Zuti_IconSize", 4, 0, 6)];
		
		this.zutiReload_OneMgCannonRearmSecond = sectfile.get("MDS", "ZutiReload_OneMgCannonRearmSecond", 10, 0, 99999);
		this.zutiReload_OneBombFTankTorpedoeRearmSeconds = sectfile.get("MDS", "ZutiReload_OneBombFTankTorpedoeRearmSeconds", 25, 0, 99999);
		this.zutiReload_OneRocketRearmSeconds = sectfile.get("MDS", "ZutiReload_OneRocketRearmSeconds", 20, 0, 99999);
		this.zutiReload_GallonsLitersPerSecond = sectfile.get("MDS", "ZutiReload_GallonsLitersPerSecond", 3, 0, 99999);
		this.zutiReload_OneWeaponRepairSeconds = sectfile.get("MDS", "ZutiReload_OneWeaponRepairSeconds", 3, 0, 99999);
		this.zutiReload_FlapsRepairSeconds = sectfile.get("MDS", "ZutiReload_FlapsRepairSeconds", 30, 0, 99999);
		this.zutiReload_EngineRepairSeconds = sectfile.get("MDS", "ZutiReload_EngineRepairSeconds", 90, 0, 99999);
		this.zutiReload_CockpitRepairSeconds = sectfile.get("MDS", "ZutiReload_CockpitRepairSeconds", 30, 0, 99999);
		this.zutiReload_OneControlCableRepairSeconds = sectfile.get("MDS", "ZutiReload_OneControlCableRepairSeconds", 15, 0, 99999);
		this.zutiReload_OneFuelOilTankRepairSeconds = sectfile.get("MDS", "ZutiReload_OneFuelOilTankRepairSeconds", 20, 0, 99999);
		this.zutiReload_LoadoutChangePenaltySeconds = sectfile.get("MDS", "ZutiReload_LoadoutChangePenaltySeconds", 30, 0, 99999);;
		
		this.zutiReload_ReloadOnlyIfFuelTanksExist = false;
		if( sectfile.get("MDS", "ZutiReload_ReloadOnlyIfFuelTanksExist", 0, 0, 1) == 1 )
			this.zutiReload_ReloadOnlyIfFuelTanksExist = true;
		this.zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
		if( sectfile.get("MDS", "ZutiReload_ReloadOnlyIfAmmoBoxesExist", 0, 0, 1) == 1 )
			this.zutiReload_ReloadOnlyIfAmmoBoxesExist = true;
		this.zutiReload_RepairOnlyIfWorkshopExist = false;
		if( sectfile.get("MDS", "ZutiReload_RepairOnlyIfWorkshopExist", 0, 0, 1) == 1 )
			this.zutiReload_RepairOnlyIfWorkshopExist = true;
		this.zutiReload_OnlyHomeBaseSpecificLoadouts = true;
		if( sectfile.get("MDS", "ZutiReload_OnlyHomeBaseSpecificLoadouts", 1, 0, 1) == 0 )
			this.zutiReload_OnlyHomeBaseSpecificLoadouts = false;
		
		this.zutiDrop_OverFriendlyHomeBase = false;
		if( sectfile.get("MDS", "ZutiDrop_OverFriendlyHomeBase", 0, 0, 1) == 1 )
			this.zutiDrop_OverFriendlyHomeBase = true;
		this.zutiDrop_OverEnemyHomeBase = false;
		if( sectfile.get("MDS", "ZutiDrop_OverEnemyHomeBase", 0, 0, 1) == 1 )
			this.zutiDrop_OverEnemyHomeBase = true;
		this.zutiDrop_OverNeutralHomeBase = false;
		if( sectfile.get("MDS", "ZutiDrop_OverNeutralHomeBase", 0, 0, 1) == 1 )
			this.zutiDrop_OverNeutralHomeBase = true;
		this.zutiDrop_OverFriendlyFrictionArea = false;
		if( sectfile.get("MDS", "ZutiDrop_OverFriendlyFrictionArea", 0, 0, 1) == 1 )
			this.zutiDrop_OverFriendlyFrictionArea = true;
		this.zutiDrop_OverEnemyFrictionArea = true;
		if( sectfile.get("MDS", "ZutiDrop_OverEnemyFrictionArea", 1, 0, 1) == 0 )
			this.zutiDrop_OverEnemyFrictionArea = false;
		this.zutiDrop_OverDestroyGroundArea = true;
		if( sectfile.get("MDS", "ZutiDrop_OverDestroyGroundArea", 1, 0, 1) == 0 )
			this.zutiDrop_OverDestroyGroundArea = false;
		this.zutiDrop_OverDefenceGroundArea = true;
		if( sectfile.get("MDS", "ZutiDrop_OverDefenceGroundArea", 1, 0, 1) == 0 )
			this.zutiDrop_OverDefenceGroundArea = false;
		this.zutiDrop_MinHeight = sectfile.get("MDS", "ZutiDrop_MinHeight", 0, 0, 99999);
		this.zutiDrop_MaxHeight = sectfile.get("MDS", "ZutiDrop_MaxHeight", 10000, 0, 99999);
	}
	
	public void resetVariables()
	{
		if( objectsMap_Blue != null )
			objectsMap_Blue.clear();
		if( objectsMap_Red != null )
			objectsMap_Red.clear();
		objectsMap_Blue = null;
		objectsMap_Red = null;
		enabledResourcesManagement_BySide = false;
		enabledResourcesManagement_HomeBases = false;
		
		zutiBulletsSupply_Red = 0;
		zutiBombsSupply_Red = new int[]{0, 0, 0, 0, 0, 0};
		zutiRocketsSupply_Red = 0;
		zutiFuelSupply_Red = 0;
		zutiEnginesSupply_Red = 0;
		zutiRepairKitsSupply_Red = 0;
		
		zutiBulletsSupply_Blue = 0;
		zutiBombsSupply_Blue = new int[]{0, 0, 0, 0, 0, 0};
		zutiRocketsSupply_Blue = 0;
		zutiFuelSupply_Blue = 0;
		zutiEnginesSupply_Blue = 0;
		zutiRepairKitsSupply_Blue = 0;
		
		ZUTI_ICON_SIZE = ZUTI_ICON_SIZES[4];
		ZUTI_FRONT_ENABLE_HB_CAPTURING = true;
		ZUTI_RADAR_IN_ADV_MODE = false;
		
		zutiDrop_OverFriendlyHomeBase = false; 
		zutiDrop_OverEnemyHomeBase = false; 
		zutiDrop_OverNeutralHomeBase = false; 
		zutiDrop_OverFriendlyFrictionArea = false; 
		zutiDrop_OverEnemyFrictionArea = true; 
		zutiDrop_OverDestroyGroundArea = true; 
		zutiDrop_OverDefenceGroundArea = true;
		zutiDrop_MinHeight = 0;
		zutiDrop_MaxHeight = 10000;
		
		zutiCraters_Bombs250_Visibility = 80;
		zutiCraters_Bombs500_Visibility = 80;
		zutiCraters_Bombs1000_Visibility = 80;
		zutiCraters_Bombs2000_Visibility = 80;
		zutiCraters_Bombs5000_Visibility = 450;
		zutiCraters_Bombs9999_Visibility = 900;
		
		zutiCraters_Bombs250_SyncOnline = false;
		zutiCraters_Bombs500_SyncOnline = false;
		zutiCraters_Bombs1000_SyncOnline = false;
		zutiCraters_Bombs2000_SyncOnline = false;
		zutiCraters_Bombs5000_SyncOnline = false;
		zutiCraters_Bombs9999_SyncOnline = false;
		
		zutiHud_DisableHudStatistics = false;
		zutiHud_ShowPilotNumber	= false;
		zutiHud_ShowPilotPing = true;
		zutiHud_ShowPilotName = true;
		zutiHud_ShowPilotScore = true;
		zutiHud_ShowPilotArmy = true;
		zutiHud_ShowPilotACDesignation = false;
		zutiHud_ShowPilotACType = true;
		
		zutiIcons_MovingIcons	= false;
		zutiIcons_ShowTargets = true;
		zutiIcons_ShowNeutralHB	= false;
		zutiIcons_ShowAircraft = false;
		zutiIcons_ShowGroundUnits = false;
		zutiIcons_StaticIconsIfNoRadar = false;
		zutiIcons_ShowRockets = false;
		zutiIcons_AircraftIconsWhite = false;
		zutiIcons_ShowPlayerAircraft = true;
		zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
		
		zutiMisc_DisableAIRadioChatter = false;
		zutiMisc_DespawnAIPlanesAfterLanding = true;
		zutiMisc_HidePlayersCountOnHomeBase = false;
		zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
		zutiMisc_DisableReflyForMissionDuration = false;
		zutiMisc_ReflyKIADelay = 15;
		zutiMisc_MaxAllowedKIA = 10;
		zutiMisc_ReflyKIADelayMultiplier = 2.0F;
		zutiMisc_EnableAIAirborneMulticrew = false;
		zutiMisc_EnableInstructor = false;
		zutiMisc_EnableTowerCommunications = true;

		zutiNextMission_Enable = false;
		zutiNextMission_RedWon 		= "";
		zutiNextMission_BlueWon 	= "";
		zutiNextMission_Difficulty	= "";
		zutiNextMission_LoadDelay 	= 60;
		
		zutiObjectives_enable			= false;
		zutiObjectives_enableRed 		= false;
		zutiObjectives_enableBlue		= false;
		zutiObjectives_redShipsS 		= 0;
		zutiObjectives_redShipsM 		= 0;
		zutiObjectives_redArtillery	 	= 0;
		zutiObjectives_redStationary	= 0;
		zutiObjectives_redArmor 		= 0;
		zutiObjectives_redVehicles 		= 0;
		zutiObjectives_redTrains 		= 0;
		zutiObjectives_redAircraftsS 	= 0;
		zutiObjectives_redAircraftsM 	= 0;
		zutiObjectives_redRockets	 	= 0;
		zutiObjectives_redInfantry	 	= 0;
		zutiObjectives_redHomeBases		= 0;
		zutiObjectives_blueShipsS 		= 0;
		zutiObjectives_blueShipsM 		= 0;
		zutiObjectives_blueArtillery 	= 0;
		zutiObjectives_blueStationary 	= 0;
		zutiObjectives_blueArmor 		= 0;
		zutiObjectives_blueVehicles 	= 0;
		zutiObjectives_blueTrains 		= 0;
		zutiObjectives_blueAircraftsS 	= 0;
		zutiObjectives_blueAircraftsM 	= 0;
		zutiObjectives_blueRockets	 	= 0;
		zutiObjectives_blueInfantry	 	= 0;
		zutiObjectives_blueHomeBases	= 0;
		
		zutiRadar_ShipsAsRadar = false;
		zutiRadar_ShipRadar_MaxRange = 100;
		zutiRadar_ShipRadar_MinHeight = 100;
		zutiRadar_ShipRadar_MaxHeight = 5000;
		zutiRadar_ShipSmallRadar_MaxRange = 25;
		zutiRadar_ShipSmallRadar_MinHeight = 0;
		zutiRadar_ShipSmallRadar_MaxHeight = 2000;
		zutiRadar_ScoutsAsRadar = false;
		zutiRadar_ScoutRadar_MaxRange = 2;
		zutiRadar_ScoutRadar_DeltaHeight = 1500;
		zutiRadar_RefreshInterval = 0;
		zutiRadar_ScoutGroundObjects_Alpha = 5;
		zutiRadar_ScoutCompleteRecon = false;
		zutiRadar_EnableBigShip_Radar = false;
		zutiRadar_EnableSmallShip_Radar = false;
		scoutsRed = new ArrayList();
		scoutsBlue = new ArrayList();
		zutiRadar_PlayerSideHasRadars = false;
		
		zutiReload_OneMgCannonRearmSecond = 10;
		zutiReload_OneBombFTankTorpedoeRearmSeconds = 25;
		zutiReload_OneRocketRearmSeconds = 20;
		zutiReload_GallonsLitersPerSecond = 3;
		zutiReload_OneWeaponRepairSeconds = 3;
		zutiReload_FlapsRepairSeconds = 30;
		zutiReload_EngineRepairSeconds = 90;
		zutiReload_CockpitRepairSeconds = 30;
		zutiReload_OneControlCableRepairSeconds = 15;
		zutiReload_OneFuelOilTankRepairSeconds = 20;
		zutiReload_LoadoutChangePenaltySeconds = 30;
		zutiReload_ReloadOnlyIfFuelTanksExist = false;
		zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
		zutiReload_RepairOnlyIfWorkshopExist = false;
		zutiReload_OnlyHomeBaseSpecificLoadouts = true;
		
		respawnTime_Bigship 		= 1800;
		respawnTime_Ship 			= 1800;
		respawnTime_Aeroanchored 	= 1800;
		respawnTime_Artillery 		= 1800;
		respawnTime_Searchlight 	= 1800;
		
		//In net game, reset net position when mission is loaded
    	if( NetEnv.host() != null )
    		((NetUser)NetEnv.host()).requestPlace(-1);
    	//Reset ZUTI_OBJECTS_ICONS_ARRAY
		GUIPad.resetClassVariables();
		//Reset ZutiSupportMethods class methods
		ZutiSupportMethods.resetClassVariables();
		ZutiSupportMethods_Air.resetClassVariables();
		ZutiSupportMethods_GUI.resetClassVariables();
		ZutiSupportMethods_FM.resetClassVariables();
		ZutiSupportMethods_Builder.resetClassVariables();
		ZutiSupportMethods_Multicrew.resetClassVariables();
		ZutiSupportMethods_Net.resetClassVariables();
		ZutiSupportMethods_ResourcesManagement.resetClassVariables();
		//Reset ZutiNetSendMethods class variables
		ZutiSupportMethods_NetSend.resetClassVariables();
		//Reset ZutiAcWithReleasedOrdinance class variables
		ZutiAcWithReleasedOrdinance.resetClassVariables();
		//Reset aircrafts crew map
		ZutiAircraftCrewManagement.resetMainMap();
		//Reset effects variables
		ZutiSupportMethods_Effects.resetClassVariables();
		
		//Reset last known server time to 0 after (new) mission is loaded.
		NetServerParams.ZUTI_LAST_SERVER_TIME = 0;
		
		//Start my interpolator
		new ZutiActor().startInterpolator();
	}

	/**
	 * Read resources file with specified rrr objects values.
	 * @param fileName
	 * @param printResults: if true console shows number of resources (per side or per base)
	 */
	public static void loadResources(SectFile sectfile, boolean printResults)
	{
		System.out.println("Loading RRR objects resources data...");
		RRRItem rrrItem = null;
		
		//Try loading resources for RED side
		int sectionId = sectfile.sectionIndex("RRR_Resources_Red");
		if (sectionId >= 0)
		{
			Mission.MDS_VARIABLES().enabledResourcesManagement_BySide = true;
			Mission.MDS_VARIABLES().objectsMap_Red = new HashMap();
			
			int vars = sectfile.vars(sectionId);
			for (int i = 0; i < vars; i++)
			{
				String line = sectfile.line(sectionId, i).trim();
				String name = line.substring(0, line.indexOf(" "));
				String remaining = line.substring(line.indexOf(" ")+1, line.length());
				
				//Load remaining values
				NumberTokenizer numbertokenizer = new NumberTokenizer(remaining);

				rrrItem = new RRRItem();
				rrrItem.name = name;
				rrrItem.count = numbertokenizer.next(0, 0, 100000);
				rrrItem.bullets = numbertokenizer.next(0, 0, 100000);
				rrrItem.rockets = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb250 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb500 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb1000 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb2000 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb5000 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb9999 = numbertokenizer.next(0, 0, 100000);
				rrrItem.fuel = numbertokenizer.next(0, 0, 100000);
				rrrItem.engines = numbertokenizer.next(0, 0, 100000);
				rrrItem.repairKits = numbertokenizer.next(0, 0, 100000);
				
				Mission.MDS_VARIABLES().objectsMap_Red.put(name, rrrItem);
				
				//Skip moving objects. They add resources once they reach final destination!
				if( ZutiSupportMethods.isMovingRRRObject(name, null))
					continue;
				
				//Ok, objects were loaded into has, now it's time that we update counters...
				Mission.MDS_VARIABLES().zutiBulletsSupply_Red += rrrItem.bullets*rrrItem.count;
				Mission.MDS_VARIABLES().zutiRocketsSupply_Red += rrrItem.rockets*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] += rrrItem.bomb250*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] += rrrItem.bomb500*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] += rrrItem.bomb1000*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] += rrrItem.bomb2000*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] += rrrItem.bomb5000*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] += rrrItem.bomb9999*rrrItem.count;
				Mission.MDS_VARIABLES().zutiFuelSupply_Red += rrrItem.fuel*rrrItem.count;
				Mission.MDS_VARIABLES().zutiEnginesSupply_Red += rrrItem.engines*rrrItem.count;
				Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red += rrrItem.repairKits*rrrItem.count;
				
				if( printResults )
				{
					ZutiSupportMethods_ResourcesManagement.printOutResourcesForSide(1);
				}
			}
		}
		
		//Try loading resources for BLUE side
		sectionId = sectfile.sectionIndex("RRR_Resources_Blue");
		if (sectionId >= 0)
		{
			Mission.MDS_VARIABLES().enabledResourcesManagement_BySide = true;
			Mission.MDS_VARIABLES().objectsMap_Blue = new HashMap();
			
			int vars = sectfile.vars(sectionId);
			for (int i = 0; i < vars; i++)
			{
				String line = sectfile.line(sectionId, i).trim();
				String name = line.substring(0, line.indexOf(" "));
				String remaining = line.substring(line.indexOf(" ")+1, line.length()).trim();
				
				//Load remaining values
				NumberTokenizer numbertokenizer = new NumberTokenizer(remaining);
				
				rrrItem = new RRRItem();
				rrrItem.name = name;
				rrrItem.count = numbertokenizer.next(0, 0, 100000);
				rrrItem.bullets = numbertokenizer.next(0, 0, 100000);
				rrrItem.rockets = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb250 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb500 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb1000 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb2000 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb5000 = numbertokenizer.next(0, 0, 100000);
				rrrItem.bomb9999 = numbertokenizer.next(0, 0, 100000);
				rrrItem.fuel = numbertokenizer.next(0, 0, 100000);
				rrrItem.engines = numbertokenizer.next(0, 0, 100000);
				rrrItem.repairKits = numbertokenizer.next(0, 0, 100000);
				
				Mission.MDS_VARIABLES().objectsMap_Blue.put(name, rrrItem);

				//Skip moving objects. They add resources once they reach final destination!
				if( ZutiSupportMethods.isMovingRRRObject(name, null))
					continue;
				
				//Ok, objects were loaded into has, now it's time that we update counters...
				Mission.MDS_VARIABLES().zutiBulletsSupply_Blue += rrrItem.bullets*rrrItem.count;
				Mission.MDS_VARIABLES().zutiRocketsSupply_Blue += rrrItem.rockets*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] += rrrItem.bomb250*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] += rrrItem.bomb500*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] += rrrItem.bomb1000*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] += rrrItem.bomb2000*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] += rrrItem.bomb5000*rrrItem.count;
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] += rrrItem.bomb9999*rrrItem.count;
				Mission.MDS_VARIABLES().zutiFuelSupply_Blue += rrrItem.fuel*rrrItem.count;
				Mission.MDS_VARIABLES().zutiEnginesSupply_Blue += rrrItem.engines*rrrItem.count;
				Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue += rrrItem.repairKits*rrrItem.count;
				
				if( printResults )
				{
					ZutiSupportMethods_ResourcesManagement.printOutResourcesForSide(2);
				}
			}
		}
		
		//Now, try loading born place related settings
		List list = World.cur().bornPlaces;
		if( list != null )
		{
			for( int i=0; i<list.size(); i++ )
			{
				BornPlace bp = (BornPlace)list.get(i);
				sectionId = sectfile.sectionIndex("BornPlace_Resources_" + (int)bp.place.x + "_" + (int)bp.place.y);
				if (sectionId >= 0)
				{
					Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases = true;
					bp.zutiEnableResourcesManagement = true;
					bp.objectsMap = new HashMap();
					
					int vars = sectfile.vars(sectionId);
					for (int j = 0; j < vars; j++)
					{
						String line = sectfile.line(sectionId, j).trim();
						String name = line.substring(0, line.indexOf(" "));
						String remaining = line.substring(line.indexOf(" ")+1, line.length()).trim();
						
						//Load remaining values
						NumberTokenizer numbertokenizer = new NumberTokenizer(remaining);
						
						rrrItem = new RRRItem();
						rrrItem.name = name;
						rrrItem.count = numbertokenizer.next(0, 0, 100000);
						rrrItem.bullets = numbertokenizer.next(0, 0, 100000);
						rrrItem.rockets = numbertokenizer.next(0, 0, 100000);
						rrrItem.bomb250 = numbertokenizer.next(0, 0, 100000);
						rrrItem.bomb500 = numbertokenizer.next(0, 0, 100000);
						rrrItem.bomb1000 = numbertokenizer.next(0, 0, 100000);
						rrrItem.bomb2000 = numbertokenizer.next(0, 0, 100000);
						rrrItem.bomb5000 = numbertokenizer.next(0, 0, 100000);
						rrrItem.bomb9999 = numbertokenizer.next(0, 0, 100000);
						rrrItem.fuel = numbertokenizer.next(0, 0, 100000);
						rrrItem.engines = numbertokenizer.next(0, 0, 100000);
						rrrItem.repairKits = numbertokenizer.next(0, 0, 100000);
						
						bp.objectsMap.put(name, rrrItem);

						//Ok, objects were loaded into has, now it's time that we update counters...
						bp.zutiBulletsSupply += rrrItem.bullets*rrrItem.count;
						bp.zutiRocketsSupply += rrrItem.rockets*rrrItem.count;
						bp.zutiBombsSupply[0] += rrrItem.bomb250*rrrItem.count;
						bp.zutiBombsSupply[1] += rrrItem.bomb500*rrrItem.count;
						bp.zutiBombsSupply[2] += rrrItem.bomb1000*rrrItem.count;
						bp.zutiBombsSupply[3] += rrrItem.bomb2000*rrrItem.count;
						bp.zutiBombsSupply[4] += rrrItem.bomb5000*rrrItem.count;
						bp.zutiBombsSupply[5] += rrrItem.bomb9999*rrrItem.count;
						bp.zutiFuelSupply += rrrItem.fuel*rrrItem.count;
						bp.zutiEnginesSupply += rrrItem.engines*rrrItem.count;
						bp.zutiRepairKitsSupply += rrrItem.repairKits*rrrItem.count;
					}

					if( printResults )
					{
						ZutiSupportMethods_ResourcesManagement.printOutResourcesForHomeBase(bp);
					}
				}
			}
		}
		
		System.out.println("Done.");
	}
	
	//Lutz mod
	//---------------------------------------------------------------------------------
	private int Zone;
    private float Xcenter[];
    private float Ycenter[];
    private float LenghtZone[];
    private float WidthZone[];
    private float Zmin[];
    private float Zmax[];
	    
	private void loadAOCVariables(SectFile sectfile)
	{
		Zone = 0;
		Xcenter = null;
		Ycenter = null;
		LenghtZone = null;
		WidthZone = null;
		Zmin = null;
		Zmax = null;
		
		String sectionName = "AOC_Trigger";
        int j = 0;
        String zoneId = "zone" + j;
        for(String s3 = sectfile.get(sectionName, zoneId, (String)null); s3 != null; s3 = sectfile.get(sectionName, zoneId, (String)null))
        {
            Zone++;
            ZutiSupportMethods_FM.ZoneDef(s3, j, Xcenter, Ycenter, LenghtZone, WidthZone, Zmin, Zmax);
            j++;
            zoneId = "zone" + j;
        }
	}
	
	public int getZone()
    {
        return Zone;
    }

    public float getXcenter(int i)
    {
        return Xcenter[i];
    }

    public float getYcenter(int i)
    {
        return Ycenter[i];
    }

    public float getLenghtZone(int i)
    {
        return LenghtZone[i];
    }

    public float getWidthZone(int i)
    {
        return WidthZone[i];
    }

    public float getZmin(int i)
    {
        return Zmin[i];
    }

    public float getZmax(int i)
    {
        return Zmax[i];
    }
	//---------------------------------------------------------------------------------
}