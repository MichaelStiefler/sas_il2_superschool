package com.maddox.il2.builder;

import java.util.ArrayList;
import com.maddox.gwindow.GCaption;
import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.builder.PlMisRocket.Rocket;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Objectives extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS_Objectives";
	public static final String SECTION_ID = "MDSSection_Objectives";
	public static int CHIELF_TYPE_INCREMENT = -1;
	
	private boolean zutiObjectives_enable;
	private int zutiObjectives_redShipsS;
	private int zutiObjectives_redShipsM;
	private int zutiObjectives_redArtillery;
	private int zutiObjectives_redStationary;
	private int zutiObjectives_redArmor;
	private int zutiObjectives_redVehicles;
	private int zutiObjectives_redTrains;
	private int zutiObjectives_redAircraftsS;
	private int zutiObjectives_redAircraftsM;
	private int zutiObjectives_redRockets;
	private int zutiObjectives_redHomeBases;
	private int zutiobjectives_blueShipsS;
	private int zutiObjectives_blueShipsM;
	private int zutiobjectives_blueArtillery;
	private int zutiObjectives_blueStationary;
	private int zutiObjectives_blueArmor;
	private int zutiObjectives_blueVehicles;
	private int zutiObjectives_blueTrains;
	private int zutiObjectives_blueAircraftsS;
	private int zutiObjectives_blueAircraftsM;
	private int zutiObjectives_blueRockets;
	private int zutiObjectives_blueHomeBases;
	private int zutiObjectives_redInfantry;
	private int zutiObjectives_blueInfantry;
	
	private GWindowCheckBox wZutiObjectives_enable;
	private GWindowEditControl wZutiObjectives_redShipsS;
	private GWindowEditControl wZutiObjectives_redShipsM;
	private GWindowEditControl wZutiObjectives_redArtillery;
	private GWindowEditControl wZutiObjectives_redStationary;
	private GWindowEditControl wZutiObjectives_redArmor;
	private GWindowEditControl wZutiObjectives_redVehicles;
	private GWindowEditControl wZutiObjectives_redTrains;
	private GWindowEditControl wZutiObjectives_redAircraftsS;
	private GWindowEditControl wZutiObjectives_redAircraftsM;
	private GWindowEditControl wZutiObjectives_redRockets;
	private GWindowEditControl wZutiObjectives_redHomeBases;
	private GWindowEditControl wZutiObjectives_blueShipsS;
	private GWindowEditControl wZutiObjectives_blueShipsM;
	private GWindowEditControl wZutiObjectives_blueArtillery;
	private GWindowEditControl wZutiObjectives_blueStationary;
	private GWindowEditControl wZutiObjectives_blueArmor;
	private GWindowEditControl wZutiObjectives_blueVehicles;
	private GWindowEditControl wZutiObjectives_blueTrains;
	private GWindowEditControl wZutiObjectives_blueAircraftsS;
	private GWindowEditControl wZutiObjectives_blueAircraftsM;
	private GWindowEditControl wZutiObjectives_blueRockets;
	private GWindowEditControl wZutiObjectives_blueHomeBases;
	private GWindowEditControl wZutiObjectives_redInfantry;
	private GWindowEditControl wZutiObjectives_blueInfantry;
	private GWindowLabel lZutiObjectives_redMaxArmor;
	private GWindowLabel lZutiObjectives_redMaxTrains;
	private GWindowLabel lZutiObjectives_redMaxRockets;
	private GWindowLabel lZutiObjectives_redMaxShipsS;
	private GWindowLabel lZutiObjectives_redMaxShipsM;
	private GWindowLabel lZutiObjectives_redMaxArtillery;
	private GWindowLabel lZutiObjectives_redMaxVehicles;
	private GWindowLabel lZutiObjectives_redMaxStationary;
	private GWindowLabel lZutiObjectives_redMaxAircraftsS;
	private GWindowLabel lZutiObjectives_redMaxAircraftsM;
	private GWindowLabel lZutiObjectives_redMaxHomeBases;
	private GWindowLabel lZutiObjectives_blueMaxArmor;
	private GWindowLabel lZutiObjectives_blueMaxTrains;
	private GWindowLabel lZutiObjectives_blueMaxRockets;
	private GWindowLabel lZutiObjectives_blueMaxShipsS;
	private GWindowLabel lZutiObjectives_blueMaxShipsM;
	private GWindowLabel lZutiObjectives_blueMaxArtillery;
	private GWindowLabel lZutiObjectives_blueMaxVehicles;
	private GWindowLabel lZutiObjectives_blueMaxStationary;
	private GWindowLabel lZutiObjectives_blueMaxAircraftsS;
	private GWindowLabel lZutiObjectives_blueMaxAircraftsM;
	private GWindowLabel lZutiObjectives_blueMaxHomeBases;
	private GWindowLabel lZutiObjectives_redMaxInfantry;
	private GWindowLabel lZutiObjectives_blueMaxInfantry;
	
	private GWindowBoxSeparate 	bSeparate_redObjectives;
	private GWindowLabel 		lSeparate_redObjectives;
	private GWindowBoxSeparate 	bSeparate_blueObjectives;
	private GWindowLabel 		lSeparate_blueObjectives;
	
	public ZutiMDSSection_Objectives()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 46.0F, 35.0F, true);
		bSizable = false;
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
		title = Plugin.i18n("mds.section.objectives");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 45.0F, 1.3F, Plugin.i18n("mds.objectives.enable"), null));
		gwindowdialogclient.addControl(wZutiObjectives_enable = new GWindowCheckBox(gwindowdialogclient, 43.0F, 1.0F, null)
		{
			public void preRender()
			{
				super.preRender();
			}

			public boolean notify(int i, int i_18_)
			{
				//Disable static icons
				if( isChecked() )
				{
					wZutiObjectives_redShipsS.setEnable(true);
					wZutiObjectives_redShipsM.setEnable(true);
					wZutiObjectives_redArtillery.setEnable(true);
					wZutiObjectives_redStationary.setEnable(true);
					wZutiObjectives_redArmor.setEnable(true);
					wZutiObjectives_redVehicles.setEnable(true);
					wZutiObjectives_redTrains.setEnable(true);
					wZutiObjectives_redAircraftsS.setEnable(true);
					wZutiObjectives_redAircraftsM.setEnable(true);
					wZutiObjectives_redRockets.setEnable(true);
					wZutiObjectives_redInfantry.setEnable(true);
					wZutiObjectives_redHomeBases.setEnable(true);
					wZutiObjectives_blueShipsS.setEnable(true);
					wZutiObjectives_blueShipsM.setEnable(true);
					wZutiObjectives_blueArtillery.setEnable(true);
					wZutiObjectives_blueStationary.setEnable(true);
					wZutiObjectives_blueArmor.setEnable(true);
					wZutiObjectives_blueVehicles.setEnable(true);
					wZutiObjectives_blueTrains.setEnable(true);
					wZutiObjectives_blueAircraftsS.setEnable(true);
					wZutiObjectives_blueAircraftsM.setEnable(true);
					wZutiObjectives_blueRockets.setEnable(true);
					wZutiObjectives_blueInfantry.setEnable(true);
					wZutiObjectives_blueHomeBases.setEnable(true);
				}
				else
				{
					wZutiObjectives_redShipsS.setEnable(false);
					wZutiObjectives_redShipsM.setEnable(false);
					wZutiObjectives_redArtillery.setEnable(false);
					wZutiObjectives_redStationary.setEnable(false);
					wZutiObjectives_redArmor.setEnable(false);
					wZutiObjectives_redVehicles.setEnable(false);
					wZutiObjectives_redTrains.setEnable(false);
					wZutiObjectives_redAircraftsS.setEnable(false);
					wZutiObjectives_redAircraftsM.setEnable(false);
					wZutiObjectives_redRockets.setEnable(false);
					wZutiObjectives_redInfantry.setEnable(false);
					wZutiObjectives_redHomeBases.setEnable(false);
					wZutiObjectives_blueShipsS.setEnable(false);
					wZutiObjectives_blueShipsM.setEnable(false);
					wZutiObjectives_blueArtillery.setEnable(false);
					wZutiObjectives_blueStationary.setEnable(false);
					wZutiObjectives_blueArmor.setEnable(false);
					wZutiObjectives_blueVehicles.setEnable(false);
					wZutiObjectives_blueTrains.setEnable(false);
					wZutiObjectives_blueAircraftsS.setEnable(false);
					wZutiObjectives_blueAircraftsM.setEnable(false);
					wZutiObjectives_blueRockets.setEnable(false);
					wZutiObjectives_blueInfantry.setEnable(false);
					wZutiObjectives_blueHomeBases.setEnable(false);
				}
				
				if (i != 2)
					return false;
				
				setMDSVariables();
				return false;
			}
		});
		
		gwindowdialogclient.addLabel(lSeparate_redObjectives = new GWindowLabel(gwindowdialogclient, 3.0F, 3.5F, 13.0F, 1.6F, Plugin.i18n("mds.objectives.redSection"), null));
		bSeparate_redObjectives = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 4.0F, 21.0F, 25.0F);
		bSeparate_redObjectives.exclude = lSeparate_redObjectives;
		
		//wZutiTargets_redArmor
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 5.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.armor"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxArmor = new GWindowLabel(gwindowdialogclient, 18.0F, 5F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redArmor = new GWindowEditControl(gwindowdialogclient, 14.0F, 5.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redTrains
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 7.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.trains"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxTrains = new GWindowLabel(gwindowdialogclient, 18.0F, 7F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redTrains = new GWindowEditControl(gwindowdialogclient, 14.0F, 7.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redRockets
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 9.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.rockets"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxRockets = new GWindowLabel(gwindowdialogclient, 18.0F, 9F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redRockets = new GWindowEditControl(gwindowdialogclient, 14.0F, 9.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redVehicles
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 11.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.vehicles"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxVehicles = new GWindowLabel(gwindowdialogclient, 18.0F, 11F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redVehicles = new GWindowEditControl(gwindowdialogclient, 14.0F, 11.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redShipsM
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsM"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxShipsM = new GWindowLabel(gwindowdialogclient, 18.0F, 13F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redShipsM = new GWindowEditControl(gwindowdialogclient, 14.0F, 13.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redAircraftsM
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsM"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxAircraftsM = new GWindowLabel(gwindowdialogclient, 18.0F, 15F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redAircraftsM = new GWindowEditControl(gwindowdialogclient, 14.0F, 15.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redShipsS
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 17.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsS"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxShipsS = new GWindowLabel(gwindowdialogclient, 18.0F, 17F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redShipsS = new GWindowEditControl(gwindowdialogclient, 14.0F, 17.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redAircraftsS
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 19.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsS"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxAircraftsS = new GWindowLabel(gwindowdialogclient, 18.0F, 19F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redAircraftsS = new GWindowEditControl(gwindowdialogclient, 14.0F, 19.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redArtillery
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 21.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.artillery"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxArtillery = new GWindowLabel(gwindowdialogclient, 18.0F, 21F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redArtillery = new GWindowEditControl(gwindowdialogclient, 14.0F, 21.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_redStationary
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 23.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.stationary"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxStationary = new GWindowLabel(gwindowdialogclient, 18.0F, 23F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redStationary = new GWindowEditControl(gwindowdialogclient, 14.0F, 23.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiObjectives_redInfantry
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 25.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.infantry"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxInfantry = new GWindowLabel(gwindowdialogclient, 18.0F, 25F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redInfantry = new GWindowEditControl(gwindowdialogclient, 14.0F, 25.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiObjectives_redHomeBases
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 27.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.homebases"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_blueMaxHomeBases = new GWindowLabel(gwindowdialogclient, 18.0F, 27F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_redHomeBases = new GWindowEditControl(gwindowdialogclient, 14.0F, 27.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		gwindowdialogclient.addLabel(lSeparate_blueObjectives = new GWindowLabel(gwindowdialogclient, 25.0F, 3.5F, 11.0F, 1.6F, Plugin.i18n("mds.objectives.blueSection"), null));
		bSeparate_blueObjectives = new GWindowBoxSeparate(gwindowdialogclient, 23.0F, 4.0F, 21.0F, 25.0F);
		bSeparate_blueObjectives.exclude = lSeparate_blueObjectives;
		
		//wZutiTargets_blueArmor
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 5.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.armor"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxArmor = new GWindowLabel(gwindowdialogclient, 40.0F, 5.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueArmor = new GWindowEditControl(gwindowdialogclient, 36.0F, 5.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueTrains
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 7.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.trains"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxTrains = new GWindowLabel(gwindowdialogclient, 40.0F, 7.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueTrains = new GWindowEditControl(gwindowdialogclient, 36.0F, 7.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueRockets
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 9.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.vehicles"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxRockets = new GWindowLabel(gwindowdialogclient, 40.0F, 9.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueRockets = new GWindowEditControl(gwindowdialogclient, 36.0F, 9.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueVehicles
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 11.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.vehicles"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxVehicles = new GWindowLabel(gwindowdialogclient, 40.0F, 11.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueVehicles = new GWindowEditControl(gwindowdialogclient, 36.0F, 11.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueShipsM
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 13.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsM"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxShipsM = new GWindowLabel(gwindowdialogclient, 40.0F, 13.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueShipsM = new GWindowEditControl(gwindowdialogclient, 36.0F, 13.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueAircraftsM
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 15.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsM"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxAircraftsM = new GWindowLabel(gwindowdialogclient, 40.0F, 15.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueAircraftsM = new GWindowEditControl(gwindowdialogclient, 36.0F, 15.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueShipsS
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 17.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsS"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxShipsS = new GWindowLabel(gwindowdialogclient, 40.0F, 17.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueShipsS = new GWindowEditControl(gwindowdialogclient, 36.0F, 17.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueAircraftsS
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 19.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsS"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxAircraftsS = new GWindowLabel(gwindowdialogclient, 40.0F, 19.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueAircraftsS = new GWindowEditControl(gwindowdialogclient, 36.0F, 19.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueArtillery
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 21.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.artillery"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxArtillery = new GWindowLabel(gwindowdialogclient, 40.0F, 21.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueArtillery = new GWindowEditControl(gwindowdialogclient, 36.0F, 21.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiTargets_blueStationary
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 23.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.stationary"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxStationary = new GWindowLabel(gwindowdialogclient, 40.0F, 23.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueStationary = new GWindowEditControl(gwindowdialogclient, 36.0F, 23.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiObjectives_blueInfantry
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 25.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.infantry"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxInfantry = new GWindowLabel(gwindowdialogclient, 40.0F, 25.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueInfantry = new GWindowEditControl(gwindowdialogclient, 36.0F, 25.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		
		//wZutiObjectives_blueHomeBases
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 27.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.homebases"), null));
		gwindowdialogclient.addLabel(lZutiObjectives_redMaxHomeBases = new GWindowLabel(gwindowdialogclient, 40.0F, 27.0F, 11.0F, 1.6F, "[MAX]", null));
		gwindowdialogclient.addControl(wZutiObjectives_blueHomeBases = new GWindowEditControl(gwindowdialogclient, 36.0F, 27.0F, 3.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//bZutiObjectives_RefreshMax
		gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 1.0F, 30.0F, 43.0F, 2.0F, Plugin.i18n("mds.objectives.refreshMax"), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				//List all actors
				zutiListAllActors();		
				
				return true;
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
		zutiObjectives_enable			= false;
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
		zutiObjectives_redHomeBases		= 0;
		zutiObjectives_redInfantry		= 0;
		zutiobjectives_blueShipsS 		= 0;
		zutiObjectives_blueShipsM 		= 0;
		zutiobjectives_blueArtillery 	= 0;
		zutiObjectives_blueStationary 	= 0;
		zutiObjectives_blueArmor 		= 0;
		zutiObjectives_blueVehicles 	= 0;
		zutiObjectives_blueTrains 		= 0;
		zutiObjectives_blueAircraftsS 	= 0;
		zutiObjectives_blueAircraftsM 	= 0;
		zutiObjectives_blueRockets	 	= 0;
		zutiObjectives_blueHomeBases	= 0;
		zutiObjectives_blueInfantry		= 0;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiObjectives_enable.setChecked(zutiObjectives_enable, false);
		wZutiObjectives_redArmor.setValue(new Integer(zutiObjectives_redArmor).toString(), false);
		wZutiObjectives_redTrains.setValue(new Integer(zutiObjectives_redTrains).toString(), false);
		wZutiObjectives_redVehicles.setValue(new Integer(zutiObjectives_redVehicles).toString(), false);
		wZutiObjectives_redShipsM.setValue(new Integer(zutiObjectives_redShipsM).toString(), false);
		wZutiObjectives_redAircraftsM.setValue(new Integer(zutiObjectives_redAircraftsM).toString(), false);
		wZutiObjectives_redShipsS.setValue(new Integer(zutiObjectives_redShipsS).toString(), false);
		wZutiObjectives_redAircraftsS.setValue(new Integer(zutiObjectives_redAircraftsS).toString(), false);
		wZutiObjectives_redArtillery.setValue(new Integer(zutiObjectives_redArtillery).toString(), false);
		wZutiObjectives_redStationary.setValue(new Integer(zutiObjectives_redStationary).toString(), false);
		wZutiObjectives_redRockets.setValue(new Integer(zutiObjectives_redRockets).toString(), false);
		wZutiObjectives_redInfantry.setValue(new Integer(zutiObjectives_redInfantry).toString(), false);
		wZutiObjectives_redHomeBases.setValue(new Integer(zutiObjectives_redHomeBases).toString(), false);
		wZutiObjectives_blueArmor.setValue(new Integer(zutiObjectives_blueArmor).toString(), false);
		wZutiObjectives_blueTrains.setValue(new Integer(zutiObjectives_blueTrains).toString(), false);
		wZutiObjectives_blueVehicles.setValue(new Integer(zutiObjectives_blueVehicles).toString(), false);
		wZutiObjectives_blueShipsM.setValue(new Integer(zutiObjectives_blueShipsM).toString(), false);
		wZutiObjectives_blueAircraftsM.setValue(new Integer(zutiObjectives_blueAircraftsM).toString(), false);
		wZutiObjectives_blueShipsS.setValue(new Integer(zutiobjectives_blueShipsS).toString(), false);
		wZutiObjectives_blueAircraftsS.setValue(new Integer(zutiObjectives_blueAircraftsS).toString(), false);
		wZutiObjectives_blueArtillery.setValue(new Integer(zutiobjectives_blueArtillery).toString(), false);
		wZutiObjectives_blueStationary.setValue(new Integer(zutiObjectives_blueStationary).toString(), false);
		wZutiObjectives_blueRockets.setValue(new Integer(zutiObjectives_blueRockets).toString(), false);
		wZutiObjectives_blueHomeBases.setValue(new Integer(zutiObjectives_blueHomeBases).toString(), false);
		wZutiObjectives_blueInfantry.setValue(new Integer(zutiObjectives_blueInfantry).toString(), false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiObjectives_enable = wZutiObjectives_enable.isChecked();
		zutiObjectives_redArmor = Integer.parseInt(wZutiObjectives_redArmor.getValue());
		zutiObjectives_redTrains = Integer.parseInt(wZutiObjectives_redTrains.getValue());
		zutiObjectives_redVehicles = Integer.parseInt(wZutiObjectives_redVehicles.getValue());
		zutiObjectives_redShipsM = Integer.parseInt(wZutiObjectives_redShipsM.getValue());
		zutiObjectives_redAircraftsM = Integer.parseInt(wZutiObjectives_redAircraftsM.getValue());
		zutiObjectives_redShipsS = Integer.parseInt(wZutiObjectives_redShipsS.getValue());
		zutiObjectives_redAircraftsS = Integer.parseInt(wZutiObjectives_redAircraftsS.getValue());
		zutiObjectives_redArtillery = Integer.parseInt(wZutiObjectives_redArtillery.getValue());
		zutiObjectives_redStationary = Integer.parseInt(wZutiObjectives_redStationary.getValue());
		zutiObjectives_redRockets = Integer.parseInt(wZutiObjectives_redRockets.getValue());
		zutiObjectives_redInfantry = Integer.parseInt(wZutiObjectives_redInfantry.getValue());
		zutiObjectives_redHomeBases = Integer.parseInt(wZutiObjectives_redHomeBases.getValue());
		zutiObjectives_blueArmor = Integer.parseInt(wZutiObjectives_blueArmor.getValue());
		zutiObjectives_blueTrains = Integer.parseInt(wZutiObjectives_blueTrains.getValue());
		zutiObjectives_blueVehicles = Integer.parseInt(wZutiObjectives_blueVehicles.getValue());
		zutiObjectives_blueShipsM = Integer.parseInt(wZutiObjectives_blueShipsM.getValue());
		zutiObjectives_blueAircraftsM = Integer.parseInt(wZutiObjectives_blueAircraftsM.getValue());
		zutiobjectives_blueShipsS = Integer.parseInt(wZutiObjectives_blueShipsS.getValue());
		zutiObjectives_blueAircraftsS = Integer.parseInt(wZutiObjectives_blueAircraftsS.getValue());
		zutiobjectives_blueArtillery = Integer.parseInt(wZutiObjectives_blueArtillery.getValue());
		zutiObjectives_blueStationary = Integer.parseInt(wZutiObjectives_blueStationary.getValue());
		zutiObjectives_blueRockets = Integer.parseInt(wZutiObjectives_blueRockets.getValue());
		zutiObjectives_blueInfantry = Integer.parseInt(wZutiObjectives_blueInfantry.getValue());
		zutiObjectives_blueHomeBases = Integer.parseInt(wZutiObjectives_blueHomeBases.getValue());
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			int sectionId = sectfile.sectionIndex(SECTION_ID);
			if(sectionId < 0)
			{
				zutiObjectives_enable = false;
				loadVariablesFromOldSection(sectfile);
				return;
			}
			
			zutiObjectives_enable 			= true;
			zutiObjectives_redArmor 		= sectfile.get(SECTION_ID, "ZutiObjective_RedArmor", 0, 0, 99999);//, default, min, max)
			zutiObjectives_redTrains 		= sectfile.get(SECTION_ID, "zutiObjective_RedTrains", 0, 0, 99999);
			zutiObjectives_redVehicles 		= sectfile.get(SECTION_ID, "ZutiObjective_RedVehicles", 0, 0, 99999);
			zutiObjectives_redShipsM 		= sectfile.get(SECTION_ID, "ZutiObjectives_RedShipsM", 0, 0, 99999);
			zutiObjectives_redAircraftsM 	= sectfile.get(SECTION_ID, "ZutiObjective_RedAircraftM", 0, 0, 99999);
			zutiObjectives_redShipsS 		= sectfile.get(SECTION_ID, "ZutiObjective_RedShipsS", 0, 0, 99999);
			zutiObjectives_redAircraftsS 	= sectfile.get(SECTION_ID, "ZutiObjective_RedAircraftS", 0, 0, 99999);
			zutiObjectives_redArtillery 	= sectfile.get(SECTION_ID, "ZutiObjective_RedArtillery", 0, 0, 99999);
			zutiObjectives_redStationary 	= sectfile.get(SECTION_ID, "ZutiObjective_RedStationary", 0, 0, 99999);
			zutiObjectives_redRockets	 	= sectfile.get(SECTION_ID, "ZutiObjective_RedRockets", 0, 0, 99999);
			zutiObjectives_redInfantry	 	= sectfile.get(SECTION_ID, "ZutiObjective_RedInfantry", 0, 0, 99999);
			zutiObjectives_redHomeBases 	= sectfile.get(SECTION_ID, "ZutiObjective_RedHomeBases", 0, 0, 99999);
			
			zutiObjectives_blueArmor 		= sectfile.get(SECTION_ID, "ZutiObjective_BlueArmor", 0, 0, 99999);//, default, min, max)
			zutiObjectives_blueTrains 		= sectfile.get(SECTION_ID, "zutiObjective_BlueTrains", 0, 0, 99999);
			zutiObjectives_blueVehicles 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueVehicles", 0, 0, 99999);
			zutiObjectives_blueShipsM 		= sectfile.get(SECTION_ID, "ZutiObjectives_BlueShipsM", 0, 0, 99999);
			zutiObjectives_blueAircraftsM 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueAircraftM", 0, 0, 99999);
			zutiobjectives_blueShipsS 		= sectfile.get(SECTION_ID, "ZutiObjective_BlueShipsS", 0, 0, 99999);
			zutiObjectives_blueAircraftsS 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueAircraftS", 0, 0, 99999);
			zutiobjectives_blueArtillery 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueArtillery", 0, 0, 99999);
			zutiObjectives_blueStationary 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueStationary", 0, 0, 99999);
			zutiObjectives_blueRockets	 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueRockets", 0, 0, 99999);
			zutiObjectives_blueInfantry	 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueInfantry", 0, 0, 99999);
			zutiObjectives_blueHomeBases 	= sectfile.get(SECTION_ID, "ZutiObjective_BlueHomeBases", 0, 0, 99999);
		
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
			int sectionId = sectfile.sectionIndex(OLD_MDS_ID);
			if(sectionId < 0)
			{
				zutiObjectives_enable = false;
				return;
			}
			
			zutiObjectives_enable 			= true;
			zutiObjectives_redArmor 		= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedArmor", 0, 0, 99999);//, default, min, max)
			zutiObjectives_redTrains 		= sectfile.get(OLD_MDS_ID, "zutiObjective_RedTrains", 0, 0, 99999);
			zutiObjectives_redVehicles 		= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedVehicles", 0, 0, 99999);
			zutiObjectives_redShipsM 		= sectfile.get(OLD_MDS_ID, "ZutiObjectives_RedShipsM", 0, 0, 99999);
			zutiObjectives_redAircraftsM 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedAircraftM", 0, 0, 99999);
			zutiObjectives_redShipsS 		= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedShipsS", 0, 0, 99999);
			zutiObjectives_redAircraftsS 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedAircraftS", 0, 0, 99999);
			zutiObjectives_redArtillery 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedArtillery", 0, 0, 99999);
			zutiObjectives_redStationary 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedStationary", 0, 0, 99999);
			zutiObjectives_redRockets	 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedRockets", 0, 0, 99999);
			zutiObjectives_redInfantry	 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedInfantry", 0, 0, 99999);
			zutiObjectives_redHomeBases 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_RedHomeBases", 0, 0, 99999);
			
			zutiObjectives_blueArmor 		= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueArmor", 0, 0, 99999);//, default, min, max)
			zutiObjectives_blueTrains 		= sectfile.get(OLD_MDS_ID, "zutiObjective_BlueTrains", 0, 0, 99999);
			zutiObjectives_blueVehicles 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueVehicles", 0, 0, 99999);
			zutiObjectives_blueShipsM 		= sectfile.get(OLD_MDS_ID, "ZutiObjectives_BlueShipsM", 0, 0, 99999);
			zutiObjectives_blueAircraftsM 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueAircraftM", 0, 0, 99999);
			zutiobjectives_blueShipsS 		= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueShipsS", 0, 0, 99999);
			zutiObjectives_blueAircraftsS 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueAircraftS", 0, 0, 99999);
			zutiobjectives_blueArtillery 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueArtillery", 0, 0, 99999);
			zutiObjectives_blueStationary 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueStationary", 0, 0, 99999);
			zutiObjectives_blueRockets	 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueRockets", 0, 0, 99999);
			zutiObjectives_blueInfantry	 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueInfantry", 0, 0, 99999);
			zutiObjectives_blueHomeBases 	= sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueHomeBases", 0, 0, 99999);
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
		if( !zutiObjectives_enable )
		{
			return;
		}
		
		try
		{
			int sectionIndex = sectfile.sectionIndex(SECTION_ID);
			if(sectionIndex < 0)
				sectionIndex = sectfile.sectionAdd(SECTION_ID);
			
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedArmor", new Integer(zutiObjectives_redArmor).toString());
			sectfile.lineAdd(sectionIndex, "zutiObjective_RedTrains", new Integer(zutiObjectives_redTrains).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedVehicles", new Integer(zutiObjectives_redVehicles).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedShipsM", new Integer(zutiObjectives_redShipsM).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedAircraftM", new Integer(zutiObjectives_redAircraftsM).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedShipsS", new Integer(zutiObjectives_redShipsS).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedAircraftS", new Integer(zutiObjectives_redAircraftsS).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedArtillery", new Integer(zutiObjectives_redArtillery).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedStationary", new Integer(zutiObjectives_redStationary).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedRockets", new Integer(zutiObjectives_redRockets).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedInfantry", new Integer(zutiObjectives_redInfantry).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_RedHomeBases", new Integer(zutiObjectives_redHomeBases).toString());
			
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueArmor", new Integer(zutiObjectives_blueArmor).toString());
			sectfile.lineAdd(sectionIndex, "zutiObjective_BlueTrains", new Integer(zutiObjectives_blueTrains).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueVehicles", new Integer(zutiObjectives_blueVehicles).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueShipsM", new Integer(zutiObjectives_blueShipsM).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueAircraftM", new Integer(zutiObjectives_blueAircraftsM).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueShipsS", new Integer(zutiobjectives_blueShipsS).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueAircraftS", new Integer(zutiObjectives_blueAircraftsS).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueArtillery", new Integer(zutiobjectives_blueArtillery).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueStationary", new Integer(zutiObjectives_blueStationary).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueRockets", new Integer(zutiObjectives_blueRockets).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueInfantry", new Integer(zutiObjectives_blueInfantry).toString());
			sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueHomeBases", new Integer(zutiObjectives_blueHomeBases).toString());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void zutiListAllActors()
	{
		if( ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0 )
			ZutiMDSSection_Objectives.setChiefIdIncrement();
		System.out.println("Chiefs increment = " + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT);
		
		int redShipsS = 0;
		int redShipsM = 0;
		int redArtillery = 0;
		int redStationary = 0;
		int redArmor = 0;
		int redVehicles = 0;
		int redTrains = 0;
		int redAircraftsS = 0;
		int redAircraftsM = 0;
		int redRockets = 0;
		int redInfantry = 0;
		int redHomeBases = 0;
		int blueShipsS = 0;
		int blueShipsM = 0;
		int blueArtillery = 0;
		int blueStationary = 0;
		int blueArmor = 0;
		int blueVehicles = 0;
		int blueTrains = 0;
		int blueAircraftsS = 0;
		int blueAircraftsM = 0;
		int blueRockets = 0;
		int blueInfantry = 0;
		int blueHomeBases = 0;
		
		ArrayList plugins = Plugin.zutiGetAllActors();
		PathChief pc = null;
		PathAir pa = null;
		Actor actor = null;
		Object object = null;
		PlMisStatic pluginStatic = null;
		PlMisRocket pluginRocket = null;
		PlMisHouse pluginHouse = null;
		PlMisBorn pluginBorn = null;
		Object aobj[] = null;
		ArrayList actors = null;
		ActorBorn actorBorn = null;
		
		for( int i=0; i<plugins.size(); i++ )
		{
			object = (Object)plugins.get(i);
			System.out.println("PlMisBorn - " + object.getClass() + ", " + object.getClass().getSuperclass());
			
			if( object instanceof PlMisBorn )
			{
				pluginBorn = (PlMisBorn)object;
				actors = pluginBorn.allActors;
				for( int j=0; j<actors.size(); j++ )
				{
					actor = (Actor)actors.get(j);
					if( actor instanceof ActorBorn )
					{
						actorBorn = (ActorBorn)actor;
						if( actorBorn.zutiCanThisHomeBaseBeCaptured )
						{
							if( actorBorn.getArmy() == 1 )
								redHomeBases++;
							else if( actorBorn.getArmy() == 2 )
								blueHomeBases++;
						}
					}
				}
			}
			else if( object instanceof PlMisStatic )
			{
				pluginStatic = (PlMisStatic) object;
				actors = pluginStatic.allActors;
				
				for( int j=0; j<actors.size(); j++ )
				{
					actor = (Actor)actors.get(j);
					
					if( actor instanceof BigshipGeneric || actor instanceof ShipGeneric )
					{
						System.out.println("  PlMisBorn - Ship: " + actor.name() );
						
						if( actor.getArmy() == 1 )
							redShipsS++;
						if( actor.getArmy() == 2 )
							blueShipsS++;
					}
					else if( actor instanceof PlaneGeneric )
					{
						System.out.println("  PlMisBorn - Aircraft: " + actor.name() );
						
						if( actor.getArmy() == 1 )
							redAircraftsS++;
						if( actor.getArmy() == 2 )
							blueAircraftsS++;
					}
					else if( actor instanceof ArtilleryGeneric )
					{
						System.out.println("  PlMisBorn - Artillery: " + actor.name() );
						
						if( actor.getArmy() == 1 )
							redArtillery++;
						if( actor.getArmy() == 2 )
							blueArtillery++;
					}
					else if( actor instanceof StationaryGeneric )
					{
						System.out.println("  PlMisBorn - Stationary: " + actor.name() );
						
						if( actor.getArmy() == 1 )
							redStationary++;
						if( actor.getArmy() == 2 )
							blueStationary++;
					}
					else
					{
						
						System.out.println("  PlMisBorn - Object: " + actor.name() + ", " + actor.getClass().getSuperclass() );							
					}
				}
			}
			else if( object instanceof PlMisRocket )
			{
				pluginRocket = (PlMisRocket) object;
				actors = pluginRocket.allActors;
				for( int j=0; j<actors.size(); j++ )
				{
					actor = (Actor)actors.get(j);
					if( actor instanceof Rocket )
					{
						System.out.println("  PlMisBorn - Rocket: " + actor.name() );
						
						if( actor.getArmy() == 1 )
							redRockets++;
						if( actor.getArmy() == 2 )
							blueRockets++;
					}
				}
			}
			else if( object instanceof PlMisHouse )
			{
				pluginHouse = (PlMisHouse) object;
				actors = pluginHouse.allActors;
				for( int j=0; j<actors.size(); j++ )
				{
					actor = (Actor)actors.get(j);
					System.out.println("  PlMisBorn - actor: " + actor.name() + actor.getClass());
				}
			}
			else if( object instanceof PlMisAir)
			{
				aobj = Plugin.builder.pathes.getOwnerAttached();
		        for(int j = 0; j < aobj.length; j++)
		        {
		            actor = (Actor)aobj[j];
		            if( actor instanceof PathAir)
		            {
		            	pa = (PathAir)actor;

		            	System.out.println("  PlMisBorn - Aircraft(s): " + pa.planes);
		            	
		            	if( pa.getArmy() == 1 )
		            		redAircraftsM += pa.planes;
		            	if( pa.getArmy() == 2 )
		            		blueAircraftsM += pa.planes;
		            }
		        }
			}
			else if( object instanceof PlMisChief)
			{
				aobj = Plugin.builder.pathes.getOwnerAttached();
		        for(int j = 0; j < aobj.length; j++)
		        {
		            actor = (Actor)aobj[j];
		            if( actor instanceof PathChief)
		            {
		            	pc = (PathChief)actor;
		            	int pcType = pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT;
		            	switch( pcType )
		            	{
		            		case 0:
		            			//Infantry
		            			System.out.println("  PlMisBorn - Armor, count:" + pc.units.length);
		            			
		            			if( pc.getArmy() == 1 )
				            		redInfantry += pc.units.length;
				            	if( pc.getArmy() == 2 )
				            		blueInfantry += pc.units.length;
		            			break;
		            		case 1:
		            			//Armor
		            			System.out.println("  PlMisBorn - Armor, count:" + pc.units.length);
		            			
		            			if( pc.getArmy() == 1 )
				            		redArmor += pc.units.length;
				            	if( pc.getArmy() == 2 )
				            		blueArmor += pc.units.length;
		            			break;
		            		case 2:
		            			//Vehicle
		            			System.out.println("  PlMisBorn - Vehicle, count:" + pc.units.length);
		            			
		            			if( pc.getArmy() == 1 )
				            		redVehicles += pc.units.length;
				            	if( pc.getArmy() == 2 )
				            		blueVehicles += pc.units.length;
		            			break;
		            		case 3:
		            			//Train
		            			System.out.println("  PlMisBorn - Train, count:" + pc.units.length);
		            			
		            			if( pc.getArmy() == 1 )
				            		redTrains += pc.units.length;
				            	if( pc.getArmy() == 2 )
				            		blueTrains += pc.units.length;
		            			break;
		            		case 4:
		            		case 5:
		            			//Ship
		            			System.out.println("  PlMisBorn - Ship, count:" + pc.units.length);
		            			
		            			/*
		            			if( pc.getArmy() == 1 )
				            		redShipsM += pc.units.length;
				            	if( pc.getArmy() == 2 )
				            		blueShipsM += pc.units.length;
		            			break;
		            			*/
		            			if( pc.getArmy() == 1 )
				            		redShipsM++;
				            	if( pc.getArmy() == 2 )
				            		blueShipsM++;
		            			break;
		            	}
		            }
		        }
			}
		}
		
		lZutiObjectives_redMaxShipsS.cap = new GCaption(new Integer(redShipsS).toString());
		lZutiObjectives_blueMaxShipsS.cap = new GCaption(new Integer(blueShipsS).toString());
		lZutiObjectives_redMaxShipsM.cap = new GCaption(new Integer(redShipsM).toString());
		lZutiObjectives_blueMaxShipsM.cap = new GCaption(new Integer(blueShipsM).toString());
		lZutiObjectives_redMaxAircraftsS.cap = new GCaption(new Integer(redAircraftsS).toString());
		lZutiObjectives_blueMaxAircraftsS.cap = new GCaption(new Integer(blueAircraftsS).toString());
		lZutiObjectives_redMaxAircraftsM.cap = new GCaption(new Integer(redAircraftsM).toString());
		lZutiObjectives_blueMaxAircraftsM.cap = new GCaption(new Integer(blueAircraftsM).toString());
		lZutiObjectives_redMaxRockets.cap = new GCaption(new Integer(redRockets).toString());
		lZutiObjectives_blueMaxRockets.cap = new GCaption(new Integer(blueRockets).toString());
		lZutiObjectives_redMaxVehicles.cap = new GCaption(new Integer(redVehicles).toString());
		lZutiObjectives_redMaxHomeBases.cap = new GCaption(new Integer(redHomeBases).toString());
		lZutiObjectives_blueMaxVehicles.cap = new GCaption(new Integer(blueVehicles).toString());
		lZutiObjectives_redMaxArmor.cap = new GCaption(new Integer(redArmor).toString());
		lZutiObjectives_blueMaxArmor.cap = new GCaption(new Integer(blueArmor).toString());
		lZutiObjectives_redMaxStationary.cap = new GCaption(new Integer(redStationary).toString());
		lZutiObjectives_blueMaxStationary.cap = new GCaption(new Integer(blueStationary).toString());
		lZutiObjectives_redMaxArtillery.cap = new GCaption(new Integer(redArtillery).toString());
		lZutiObjectives_blueMaxArtillery.cap = new GCaption(new Integer(blueArtillery).toString());
		lZutiObjectives_redMaxTrains.cap = new GCaption(new Integer(redTrains).toString());
		lZutiObjectives_blueMaxTrains.cap = new GCaption(new Integer(blueTrains).toString());
		lZutiObjectives_blueMaxHomeBases.cap = new GCaption(new Integer(blueHomeBases).toString());
		lZutiObjectives_blueMaxInfantry.cap = new GCaption(new Integer(blueInfantry).toString());
		lZutiObjectives_redMaxInfantry.cap = new GCaption(new Integer(redInfantry).toString());
		
		System.out.println("-----------------------------------------------------------------");
		
		aobj = com.maddox.il2.builder.Plugin.builder.pathes.getOwnerAttached();
        for(int j = 0; j < aobj.length; j++)
        {
            actor = (Actor)aobj[j];
            System.out.println("PlMisBorn - " + actor.getClass() + ", " + actor.getClass().getSuperclass());
        }
	}
	
	/**
	 * Method returns integer increment that needs to be added to chief ID for objectives identifications.
	 * @return
	 */
	public static void setChiefIdIncrement()
	{		
		CHIELF_TYPE_INCREMENT = Config.cur.ini.get("game", "pcIncrement", 0, 0, 1); 
		//System.out.println("ZutiMDSection_Objectives - Chief Increment: " + CHIELF_TYPE_INCREMENT);
	}
}