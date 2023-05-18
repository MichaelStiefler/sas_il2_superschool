package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_MapIcons extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS";
	public static final String SECTION_ID = "MDSSection_MapIcons";
	
	private boolean zutiIcons_ShowAircraft;
	private boolean zutiIcons_ShowGroundUnits;
	private boolean zutiIcons_StaticIconsIfNoRadar;
	private boolean zutiIcons_AircraftIconsWhite;
	private boolean zutiIcons_HideUnpopulatedAirstripsFromMinimap;
	private boolean zutiIcons_ShowRockets;
	private boolean zutiIcons_ShowPlayerAircraft;
	private boolean zutiIcons_MovingIcons;
	private boolean zutiIcons_ShowTargets;
	private int 	zutiIcons_IconsSize;
	private boolean zutiIcons_ShowNeutralHB;
	
	private GWindowCheckBox 	wZutiIcons_ShowAircraft;
	private GWindowCheckBox 	wZutiIcons_ShowGroundUnits;
	private GWindowCheckBox		wZutiIcons_ShowRockets;
	private GWindowCheckBox 	wZutiIcons_StaticIconsIfNoRadar;
	private GWindowCheckBox 	wZutiIcons_AircraftIconsWhite;
	private GWindowCheckBox 	wZutiIcons_HideUnpopulatedAirstripsFromMinimap;
	private GWindowCheckBox 	wZutiIcons_ShowPlayerAircraft;
	private GWindowCheckBox 	wZutiIcons_MovingIcons;
	private GWindowBoxSeparate 	bSeparate_TargetIcons;
	private GWindowLabel 		lSeparate_TargetIcons;
	private GWindowBoxSeparate 	bSeparate_Icons;
	private GWindowLabel 		lSeparate_Icons;
	private GWindowComboControl wZutiIcons_IconsSize;
	private GWindowCheckBox 	wZutiIcons_ShowNeutralHB;
	private GWindowCheckBox 	wZutiIcons_ShowTargets;
	
	private ZutiMDSSection_Radar mdsRadarReference;
	
	public ZutiMDSSection_MapIcons(ZutiMDSSection_Radar radarReference)
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 33.0F, 31.0F, true);
		bSizable = false;

		mdsRadarReference = radarReference;
	}
	
	public void afterCreated()
	{
		super.afterCreated();
		
		close(false);
	}
	
	public void windowShown()
	{
		setUIVariables();
		
		super.windowShown();
	}
	
	public void windowHidden()
	{
		super.windowHidden();
	}
	
	public void created()
	{
		initializeVariables();
		
		bAlwaysOnTop = true;
		super.created();
		title = Plugin.i18n("mds.section.mapIcons");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		gwindowdialogclient.addLabel(lSeparate_Icons = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 4.0F, 1.6F, Plugin.i18n("mds.icons.general"), null));
		bSeparate_Icons = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 30.0F, 17.0F);
		bSeparate_Icons.exclude = lSeparate_Icons;			
		
		//wZutiRadar_ShowAircraft
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showAc"), null));
		gwindowdialogclient.addControl(wZutiIcons_ShowAircraft = new GWindowCheckBox(gwindowdialogclient, 29.0F, 2.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShowRockets
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showRocket"), null));
		gwindowdialogclient.addControl(wZutiIcons_ShowRockets = new GWindowCheckBox(gwindowdialogclient, 29.0F, 4.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShowGroundUnits
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showGO"), null));
		gwindowdialogclient.addControl(wZutiIcons_ShowGroundUnits = new GWindowCheckBox(gwindowdialogclient, 29.0F, 6.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				
				if( mdsRadarReference != null )
					setEnable(mdsRadarReference.areScoutsRadars());
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiIcons_ShowNeutralHB
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showNeutral"), null));
		gwindowdialogclient.addControl(wZutiIcons_ShowNeutralHB = new GWindowCheckBox(gwindowdialogclient, 29.0F, 8.0F, null)
		{
			public void preRender()
			{
				super.preRender();
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});			
		
		//wZutiRadar_AircraftIconsWhite
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 10.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.allIconsWhite"), null));
		gwindowdialogclient.addControl(wZutiIcons_AircraftIconsWhite = new GWindowCheckBox(gwindowdialogclient, 29.0F, 10.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShowPlayerAircraft
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 12.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showPlayer"), null));
		gwindowdialogclient.addControl(wZutiIcons_ShowPlayerAircraft = new GWindowCheckBox(gwindowdialogclient, 29.0F, 12.0F, null)
		{
			public void preRender()
			{
				super.preRender();
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiIcons_IconsSize
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.iconSize"), null));
		gwindowdialogclient.addControl(wZutiIcons_IconsSize = new GWindowComboControl(gwindowdialogclient, 25.0F, 14.0F, 5.0F)
		{
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		wZutiIcons_IconsSize.setEditable(false);
		wZutiIcons_IconsSize.add(Plugin.i18n("08"));
		wZutiIcons_IconsSize.add(Plugin.i18n("12"));
		wZutiIcons_IconsSize.add(Plugin.i18n("16"));
		wZutiIcons_IconsSize.add(Plugin.i18n("20"));
		wZutiIcons_IconsSize.add(Plugin.i18n("24"));
		wZutiIcons_IconsSize.add(Plugin.i18n("28"));
		wZutiIcons_IconsSize.add(Plugin.i18n("32"));			
		
		//wZutiRadar_HideUnpopulatedAirstripsFromMinimap
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 16.0F, 29.0F, 1.3F, Plugin.i18n("mds.misc.hideAirfields"), null));
		gwindowdialogclient.addControl(wZutiIcons_HideUnpopulatedAirstripsFromMinimap = new GWindowCheckBox(gwindowdialogclient, 29.0F, 16.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		gwindowdialogclient.addLabel(lSeparate_TargetIcons = new GWindowLabel(gwindowdialogclient, 3.0F, 19.5F, 4.0F, 1.6F, Plugin.i18n("mds.targets"), null));
		bSeparate_TargetIcons = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 20.0F, 30.0F, 7.0F);
		bSeparate_TargetIcons.exclude = lSeparate_TargetIcons;
		
		//wZutiTargets_ShowTargets
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 21.0F, 27.0F, 1.3F, Plugin.i18n("mds.targets.renderTargets"), null));
		gwindowdialogclient.addControl(wZutiIcons_ShowTargets = new GWindowCheckBox(gwindowdialogclient, 29.0F, 21.0F, null)
		{
			public void preRender()
			{
				super.preRender();
			}

			public boolean notify(int i, int i_18_)
			{
				wZutiIcons_MovingIcons.setEnable(isChecked());
				
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_MovingIcons
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 23.0F, 27.0F, 1.3F, Plugin.i18n("mds.targets.moving"), null));
		gwindowdialogclient.addControl(wZutiIcons_MovingIcons = new GWindowCheckBox(gwindowdialogclient, 29.0F, 23.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				wZutiIcons_StaticIconsIfNoRadar.setEnable(isChecked());
			
				if (i != 2)
					return false;
				
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_StaticIconsIfNoRadar
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 25.0F, 27.0F, 1.3F, Plugin.i18n("mds.targets.stationary"), null));
		gwindowdialogclient.addControl(wZutiIcons_StaticIconsIfNoRadar = new GWindowCheckBox(gwindowdialogclient, 29.0F, 25.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
	}
	
	/**
	 * Close this window.
	 * @param flag: Set to false if you want windows to disappear.
	 */
	public void close(boolean flag)
	{
		super.close(flag);
	}
	
	/**
	 * Initializes MDS variables (sets default values).
	 */
	private void initializeVariables()
	{
		zutiIcons_MovingIcons	= false;
		zutiIcons_ShowTargets = true;
		zutiIcons_IconsSize	= 4;
		zutiIcons_ShowNeutralHB	= false;
		zutiIcons_ShowAircraft = false;
		zutiIcons_ShowGroundUnits = false;
		zutiIcons_StaticIconsIfNoRadar = false;
		zutiIcons_ShowRockets = false;
		zutiIcons_AircraftIconsWhite = false;
		zutiIcons_ShowPlayerAircraft = true;
		zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiIcons_MovingIcons.setChecked(zutiIcons_MovingIcons, false);
		wZutiIcons_ShowTargets.setChecked(zutiIcons_ShowTargets, false);
		wZutiIcons_IconsSize.setSelected(zutiIcons_IconsSize, true, false);
		wZutiIcons_ShowNeutralHB.setChecked(zutiIcons_ShowNeutralHB, false);
		
		wZutiIcons_ShowAircraft.setChecked(zutiIcons_ShowAircraft, false);
		wZutiIcons_ShowGroundUnits.setChecked(zutiIcons_ShowGroundUnits, false);
		wZutiIcons_ShowRockets.setChecked(zutiIcons_ShowRockets, false);
		wZutiIcons_StaticIconsIfNoRadar.setChecked(zutiIcons_StaticIconsIfNoRadar, false);
		wZutiIcons_AircraftIconsWhite.setChecked(zutiIcons_AircraftIconsWhite, false);
		wZutiIcons_ShowPlayerAircraft.setChecked(zutiIcons_ShowPlayerAircraft, false);
		wZutiIcons_HideUnpopulatedAirstripsFromMinimap.setChecked(zutiIcons_HideUnpopulatedAirstripsFromMinimap, false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiIcons_MovingIcons = wZutiIcons_MovingIcons.isChecked();
		zutiIcons_ShowTargets = wZutiIcons_ShowTargets.isChecked();
		zutiIcons_IconsSize = wZutiIcons_IconsSize.getSelected();
		zutiIcons_ShowNeutralHB = wZutiIcons_ShowNeutralHB.isChecked();

		zutiIcons_ShowAircraft = wZutiIcons_ShowAircraft.isChecked();
		zutiIcons_ShowGroundUnits = wZutiIcons_ShowGroundUnits.isChecked();
		zutiIcons_ShowRockets = wZutiIcons_ShowRockets.isChecked();
		zutiIcons_StaticIconsIfNoRadar = wZutiIcons_StaticIconsIfNoRadar.isChecked();
		zutiIcons_AircraftIconsWhite = wZutiIcons_AircraftIconsWhite.isChecked();
		zutiIcons_ShowPlayerAircraft = wZutiIcons_ShowPlayerAircraft.isChecked();
		zutiIcons_HideUnpopulatedAirstripsFromMinimap = wZutiIcons_HideUnpopulatedAirstripsFromMinimap.isChecked();
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiIcons_MovingIcons = false;
			if( sectfile.get(SECTION_ID, "ZutiIcons_MovingIcons", 0, 0, 1) == 1 )
				zutiIcons_MovingIcons = true;
			
			zutiIcons_StaticIconsIfNoRadar = false;
			if( sectfile.get(SECTION_ID, "ZutiRadar_StaticIconsIfNoRadar", 0, 0, 1) == 1 )
				zutiIcons_StaticIconsIfNoRadar = true;
				
			zutiIcons_ShowTargets = true;
			if( sectfile.get(SECTION_ID, "ZutiIcons_ShowTargets", 1, 0, 1) == 0 )
				zutiIcons_ShowTargets = false;
				
			zutiIcons_IconsSize = sectfile.get(SECTION_ID, "ZutiIcons_IconsSize", 4, 0, 6);
			
			zutiIcons_ShowNeutralHB = false;
			if( sectfile.get(SECTION_ID, "ZutiIcons_ShowNeutralHB", 0, 0, 1) == 1 )
				zutiIcons_ShowNeutralHB = true;
			
			zutiIcons_ShowAircraft = false;
			if( sectfile.get(SECTION_ID, "ZutiIcons_ShowAircraft", 0, 0, 1) == 1 )
				zutiIcons_ShowAircraft = true;
				
			zutiIcons_ShowGroundUnits = false;
			if( sectfile.get(SECTION_ID, "ZutiIcons_ShowGroundUnits", 0, 0, 1) == 1 )
				zutiIcons_ShowGroundUnits = true;
			
			zutiIcons_ShowRockets = false;
			if( sectfile.get(SECTION_ID, "ZutiIcons_ShowRockets", 0, 0, 1) == 1 )
				zutiIcons_ShowRockets = true;
			
			zutiIcons_AircraftIconsWhite = false;
			if( sectfile.get(SECTION_ID, "ZutiIcons_AircraftIconsWhite", 0, 0, 1) == 1 )
				zutiIcons_AircraftIconsWhite = true;
			
			zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
			if( sectfile.get(SECTION_ID, "ZutiIcons_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1 )
				zutiIcons_HideUnpopulatedAirstripsFromMinimap = true;
				
			zutiIcons_ShowPlayerAircraft = true;
			if( sectfile.get(SECTION_ID, "ZutiIcons_ShowPlayerAircraft", 1, 0, 1) == 0 )
				zutiIcons_ShowPlayerAircraft = false;
			
			if( sectfile.sectionIndex(OLD_MDS_ID) > -1 )
				loadVariablesFromOldSection(sectfile);
			
			setUIVariables();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadVariablesFromOldSection(SectFile sectfile)
	{
		try
		{
			zutiIcons_MovingIcons = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiTargets_MovingIcons", 0, 0, 1) == 1 )
				zutiIcons_MovingIcons = true;
			
			zutiIcons_StaticIconsIfNoRadar = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_StaticIconsIfNoRadar", 0, 0, 1) == 1 )
				zutiIcons_StaticIconsIfNoRadar = true;
				
			zutiIcons_ShowTargets = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiTargets_ShowTargets", 1, 0, 1) == 0 )
				zutiIcons_ShowTargets = false;
				
			zutiIcons_IconsSize = sectfile.get(OLD_MDS_ID, "ZutiIcons_IconsSize", 4, 0, 6);
			
			zutiIcons_ShowNeutralHB = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiIcons_ShowNeutralHB", 0, 0, 1) == 1 )
				zutiIcons_ShowNeutralHB = true;
			
			zutiIcons_ShowAircraft = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowAircraft", 0, 0, 1) == 1 )
				zutiIcons_ShowAircraft = true;
				
			zutiIcons_ShowGroundUnits = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowGroundUnits", 0, 0, 1) == 1 )
				zutiIcons_ShowGroundUnits = true;
			
			zutiIcons_ShowRockets = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowRockets", 0, 0, 1) == 1 )
				zutiIcons_ShowRockets = true;
			
			
			zutiIcons_AircraftIconsWhite = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_AircraftIconsWhite", 0, 0, 1) == 1 )
				zutiIcons_AircraftIconsWhite = true;
			
			zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1 )
				zutiIcons_HideUnpopulatedAirstripsFromMinimap = true;
				
			zutiIcons_ShowPlayerAircraft = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowPlayerAircraft", 1, 0, 1) == 0 )
				zutiIcons_ShowPlayerAircraft = false;
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Stores variables to corresponding MDS section in mission file.
	 * @param sectfile
	 */
	public void saveVariables(SectFile sectfile)
	{
		try
		{
			int sectionIndex = sectfile.sectionIndex(SECTION_ID);
			if(sectionIndex < 0)
				sectionIndex = sectfile.sectionAdd(SECTION_ID);
						
			sectfile.lineAdd(sectionIndex, "ZutiIcons_MovingIcons", ZutiSupportMethods.boolToInt(zutiIcons_MovingIcons));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowTargets", ZutiSupportMethods.boolToInt(zutiIcons_ShowTargets));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_IconsSize", new Integer(zutiIcons_IconsSize).toString());
			sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowNeutralHB", ZutiSupportMethods.boolToInt(zutiIcons_ShowNeutralHB));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowAircraft", ZutiSupportMethods.boolToInt(zutiIcons_ShowAircraft));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowGroundUnits", ZutiSupportMethods.boolToInt(zutiIcons_ShowGroundUnits));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowRockets", ZutiSupportMethods.boolToInt(zutiIcons_ShowRockets));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_StaticIconsIfNoRadar", ZutiSupportMethods.boolToInt(zutiIcons_StaticIconsIfNoRadar));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_AircraftIconsWhite", ZutiSupportMethods.boolToInt(zutiIcons_AircraftIconsWhite));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowPlayerAircraft", ZutiSupportMethods.boolToInt(zutiIcons_ShowPlayerAircraft));
			sectfile.lineAdd(sectionIndex, "ZutiIcons_HideUnpopulatedAirstripsFromMinimap", ZutiSupportMethods.boolToInt(zutiIcons_HideUnpopulatedAirstripsFromMinimap));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}