package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_RRR extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS";
	public static final String SECTION_ID = "MDSSection_RRR";
	public Zuti_WResourcesManagement resourcesManagement_Red = null;
	public Zuti_WResourcesManagement resourcesManagement_Blue = null;
	
	private int zutiReload_OneMgCannonRearmSecond;
	private int zutiReload_OneBombFTankTorpedoeRearmSeconds;
	private int zutiReload_OneRocketRearmSeconds;
	private int zutiReload_GallonsLitersPerSecond;
	private int zutiReload_OneWeaponRepairSeconds;
	private int zutiReload_FlapsRepairSeconds;
	private boolean zutiReload_ReloadOnlyIfFuelTanksExist;
	private boolean zutiReload_ReloadOnlyIfAmmoBoxesExist;
	private boolean zutiReload_RepairOnlyIfWorkshopExist;
	private int zutiReload_EngineRepairSeconds;
	private int zutiReload_OneControlCableRepairSeconds;
	private int zutiReload_OneFuelOilTankRepairSeconds;
	private int zutiReload_LoadoutChangePenaltySeconds;
	private boolean zutiReload_OnlyHomeBaseSpecificLoadouts;
	private int zutiReload_CockpitRepairSeconds;
	
	private GWindowEditControl wZutiReload_OneMgCannonRearmSecond;
	private GWindowEditControl wZutiReload_OneBombFTankTorpedoeRearmSeconds;
	private GWindowEditControl wZutiReload_OneRocketRearmSeconds;
	private GWindowEditControl wZutiReload_GallonsLitersPerSecond;
	private GWindowEditControl wZutiReload_OneWeaponRepairSeconds;
	private GWindowEditControl wZutiReload_FlapsRepairSeconds;
	private GWindowCheckBox wZutiReload_ReloadOnlyIfFuelTanksExist;
	private GWindowCheckBox wZutiReload_ReloadOnlyIfAmmoBoxesExist;
	private GWindowCheckBox wZutiReload_RepairOnlyIfWorkshopExist;
	private GWindowEditControl wZutiReload_EngineRepairSeconds;
	private GWindowEditControl wZutiReload_CockpitRepairSeconds;
	private GWindowEditControl wZutiReload_OneControlCableRepairSeconds;
	private GWindowEditControl wZutiReload_OneFuelOilTankRepairSeconds;
	private GWindowEditControl wZutiReload_LoadoutChangePenaltySeconds;
	private GWindowCheckBox wZutiReload_OnlyHomeBaseSpecificLoadouts;
	
	public GWindowCheckBox wZutiReload_EnableResourcesManagement;
	
	private GWindowBoxSeparate 	bSeparate_Rearm;
	private GWindowLabel 		lSeparate_Rearm;
	private GWindowBoxSeparate 	bSeparate_Refuel;
	private GWindowLabel 		lSeparate_Refuel;
	private GWindowBoxSeparate 	bSeparate_Repair;
	private GWindowLabel 		lSeparate_Repair;
	private GWindowBoxSeparate 	bSeparate_Resources;
	private GWindowLabel 		lSeparate_Resources;
	
	public ZutiMDSSection_RRR()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 40.0F, 42.0F, true);
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
		title = Plugin.i18n("mds.section.RRR");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		gwindowdialogclient.addLabel(lSeparate_Rearm = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 4.0F, 1.6F, Plugin.i18n("mds.rearm"), null));
		bSeparate_Rearm = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 37.0F, 9.0F);
		bSeparate_Rearm.exclude = lSeparate_Rearm;
		
		//wZutiReload_OneMgCannonRearmSecond
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.mg"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 2.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_OneMgCannonRearmSecond = new GWindowEditControl(gwindowdialogclient, 11.0F, 2.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_OneRocketRearmSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.rocket"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 4.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_OneRocketRearmSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 4.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_OneBombFTankTorpedoeRearmSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 2.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.bomb"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 2.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_OneBombFTankTorpedoeRearmSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 2.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_LoadoutChangePenaltySeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 4.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.loadout"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 4.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_LoadoutChangePenaltySeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 4.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_OnlyHomeBaseSpecificLoadouts
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 36.0F, 1.3F, Plugin.i18n("mds.rearm.onlyHbLoadouts"), null));
		gwindowdialogclient.addControl(wZutiReload_OnlyHomeBaseSpecificLoadouts = new GWindowCheckBox(gwindowdialogclient, 35.0F, 6.0F, null)
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
		
		//wZutiReload_ReloadOnlyIfAmmoBoxesExist
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 36.0F, 1.3F, Plugin.i18n("mds.rearm.ammo"), null));
		gwindowdialogclient.addControl(wZutiReload_ReloadOnlyIfAmmoBoxesExist = new GWindowCheckBox(gwindowdialogclient, 35.0F, 8.0F, null)
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
					
		gwindowdialogclient.addLabel(lSeparate_Refuel = new GWindowLabel(gwindowdialogclient, 3.0F, 11.5F, 4.0F, 1.6F, Plugin.i18n("mds.refuel"), null));
		bSeparate_Refuel = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 12.0F, 37.0F, 5.0F);
		bSeparate_Refuel.exclude = lSeparate_Refuel;
		
		//wZutiReload_GallonsLitersPerSecond
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 10.0F, 1.3F, Plugin.i18n("mds.refuel.rate1"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 35.0F, 13.0F, 12.0F, 1.3F, Plugin.i18n("mds.refuel.rate2"), null));
		gwindowdialogclient.addControl(wZutiReload_GallonsLitersPerSecond = new GWindowEditControl(gwindowdialogclient, 31.5F, 13.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_ReloadOnlyIfFuelTanksExist
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 35.0F, 1.3F, Plugin.i18n("mds.refuel.fuelTanks"), null));
		gwindowdialogclient.addControl(wZutiReload_ReloadOnlyIfFuelTanksExist = new GWindowCheckBox(gwindowdialogclient, 35.0F, 15.0F, null)
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
		
		gwindowdialogclient.addLabel(lSeparate_Repair = new GWindowLabel(gwindowdialogclient, 3.0F, 18.5F, 4.0F, 1.6F, Plugin.i18n("mds.repair"), null));
		bSeparate_Repair = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 19.0F, 37.0F, 9.0F);
		bSeparate_Repair.exclude = lSeparate_Repair;
		
		//wZutiReload_EngineRepairSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 20.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.engine"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 20.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_EngineRepairSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 20.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_OneControlCableRepairSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 20.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.controlCable"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 20.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_OneControlCableRepairSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 20.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_FlapsRepairSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 22.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.flaps"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 22.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_FlapsRepairSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 22.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_OneWeaponRepairSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 22.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.mg"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 22.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_OneWeaponRepairSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 22.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_CockpitRepairSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 24.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.cockpit"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 24.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_CockpitRepairSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 24.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_OneFuelOilTankRepairSeconds
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 24.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.tank"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 24.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiReload_OneFuelOilTankRepairSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 24.0F, 3.0F, 1.3F, "")
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
		
		//wZutiReload_RepairOnlyIfWorkshopExist
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 26.0F, 35.0F, 1.3F, Plugin.i18n("mds.repair.workshop"), null));
		gwindowdialogclient.addControl(wZutiReload_RepairOnlyIfWorkshopExist = new GWindowCheckBox(gwindowdialogclient, 35.0F, 26.0F, null)
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
		
		//TODO: 
		gwindowdialogclient.addLabel(lSeparate_Resources = new GWindowLabel(gwindowdialogclient, 3.0F, 29.5F, 14.0F, 1.6F, Plugin.i18n("mds.RRR.resourcesManagement"), null));
		bSeparate_Resources = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 30.0F, 37.0F, 9.0F);
		bSeparate_Resources.exclude = lSeparate_Resources;
		
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 31.0F, 35.0F, 1.3F, Plugin.i18n("mds.RRR.enableResourcesMng"), null));
		gwindowdialogclient.addControl(wZutiReload_EnableResourcesManagement = new GWindowCheckBox(gwindowdialogclient, 35.0F, 31.0F, null)
		{
			public void preRender()
			{
				super.preRender();
			}

			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
				
				return false;
			}
		});
		//RED team resources
		gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 33.0F, 35.0F, 2.0F, Plugin.i18n("mds.RRR.setResourcesRed"), null)
		{
			public void preRender()
			{
				super.preRender();
				
				this.setEnable(wZutiReload_EnableResourcesManagement.isChecked());
				if( !this.isEnable() )
					resourcesManagement_Red = null;
			}
			
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				//TODO: Open new window here
				if( resourcesManagement_Red == null )
					resourcesManagement_Red = new Zuti_WResourcesManagement(1);
				
				if( resourcesManagement_Red.isVisible() )
					resourcesManagement_Red.hideWindow();
				else
				{
					resourcesManagement_Red.setTitle(Plugin.i18n("mds.RRR.setResourcesRed"));
					resourcesManagement_Red.countRRRObjects();
					resourcesManagement_Red.showWindow();
				}
				
				return true;
			}
		});
		//BLUE team resources
		gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 36.0F, 35.0F, 2.0F, Plugin.i18n("mds.RRR.setResourcesBlue"), null)
		{
			public void preRender()
			{
				super.preRender();
				
				this.setEnable(wZutiReload_EnableResourcesManagement.isChecked());
				if( !this.isEnable() )
					resourcesManagement_Blue = null;
			}
			
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				//TODO: Open new window here
				if( resourcesManagement_Blue == null )
					resourcesManagement_Blue = new Zuti_WResourcesManagement(2);
				
				if( resourcesManagement_Blue.isVisible() )
					resourcesManagement_Blue.hideWindow();
				else
				{
					resourcesManagement_Blue.setTitle(Plugin.i18n("mds.RRR.setResourcesBlue"));
					resourcesManagement_Blue.countRRRObjects();
					resourcesManagement_Blue.showWindow();
				}
				
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
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiReload_OneMgCannonRearmSecond.setValue(new Integer(zutiReload_OneMgCannonRearmSecond).toString(), false);
		wZutiReload_OneBombFTankTorpedoeRearmSeconds.setValue(new Integer(zutiReload_OneBombFTankTorpedoeRearmSeconds).toString(), false);
		wZutiReload_OneRocketRearmSeconds.setValue(new Integer(zutiReload_OneRocketRearmSeconds).toString(), false);
		wZutiReload_GallonsLitersPerSecond.setValue(new Integer(zutiReload_GallonsLitersPerSecond).toString(), false);
		wZutiReload_OneWeaponRepairSeconds.setValue(new Integer(zutiReload_OneWeaponRepairSeconds).toString(), false);
		wZutiReload_FlapsRepairSeconds.setValue(new Integer(zutiReload_FlapsRepairSeconds).toString(), false);
		wZutiReload_EngineRepairSeconds.setValue(new Integer(zutiReload_EngineRepairSeconds).toString(), false);
		wZutiReload_OneControlCableRepairSeconds.setValue(new Integer(zutiReload_OneControlCableRepairSeconds).toString(), false);
		wZutiReload_OneFuelOilTankRepairSeconds.setValue(new Integer(zutiReload_OneFuelOilTankRepairSeconds).toString(), false);
		wZutiReload_LoadoutChangePenaltySeconds.setValue(new Integer(zutiReload_LoadoutChangePenaltySeconds).toString(), false);
		wZutiReload_CockpitRepairSeconds.setValue(new Integer(zutiReload_CockpitRepairSeconds).toString(), false);
		
		wZutiReload_ReloadOnlyIfFuelTanksExist.setChecked(zutiReload_ReloadOnlyIfFuelTanksExist, false);
		wZutiReload_ReloadOnlyIfAmmoBoxesExist.setChecked(zutiReload_ReloadOnlyIfAmmoBoxesExist, false);
		wZutiReload_RepairOnlyIfWorkshopExist.setChecked(zutiReload_RepairOnlyIfWorkshopExist, false);
		wZutiReload_OnlyHomeBaseSpecificLoadouts.setChecked(zutiReload_OnlyHomeBaseSpecificLoadouts, false);
		
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiReload_OneMgCannonRearmSecond = Integer.parseInt(wZutiReload_OneMgCannonRearmSecond.getValue());
		zutiReload_OneBombFTankTorpedoeRearmSeconds = Integer.parseInt(wZutiReload_OneBombFTankTorpedoeRearmSeconds.getValue());
		zutiReload_OneRocketRearmSeconds = Integer.parseInt(wZutiReload_OneRocketRearmSeconds.getValue());
		zutiReload_GallonsLitersPerSecond = Integer.parseInt(wZutiReload_GallonsLitersPerSecond.getValue());
		zutiReload_OneWeaponRepairSeconds = Integer.parseInt(wZutiReload_OneWeaponRepairSeconds.getValue());
		zutiReload_FlapsRepairSeconds = Integer.parseInt(wZutiReload_FlapsRepairSeconds.getValue());
		zutiReload_EngineRepairSeconds = Integer.parseInt(wZutiReload_EngineRepairSeconds.getValue());
		zutiReload_OneControlCableRepairSeconds = Integer.parseInt(wZutiReload_OneControlCableRepairSeconds.getValue());
		zutiReload_OneFuelOilTankRepairSeconds = Integer.parseInt(wZutiReload_OneFuelOilTankRepairSeconds.getValue());
		zutiReload_LoadoutChangePenaltySeconds = Integer.parseInt(wZutiReload_LoadoutChangePenaltySeconds.getValue());
		zutiReload_CockpitRepairSeconds = Integer.parseInt(wZutiReload_CockpitRepairSeconds.getValue());
		
		zutiReload_ReloadOnlyIfFuelTanksExist = wZutiReload_ReloadOnlyIfFuelTanksExist.isChecked();
		zutiReload_ReloadOnlyIfAmmoBoxesExist = wZutiReload_ReloadOnlyIfAmmoBoxesExist.isChecked();
		zutiReload_RepairOnlyIfWorkshopExist = wZutiReload_RepairOnlyIfWorkshopExist.isChecked();
		zutiReload_OnlyHomeBaseSpecificLoadouts = wZutiReload_OnlyHomeBaseSpecificLoadouts.isChecked();
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiReload_OneMgCannonRearmSecond = sectfile.get(SECTION_ID, "ZutiReload_OneMgCannonRearmSecond", 10, 0, 99999);
			zutiReload_OneBombFTankTorpedoeRearmSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneBombFTankTorpedoeRearmSeconds", 25, 0, 99999);
			zutiReload_OneRocketRearmSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneRocketRearmSeconds", 20, 0, 99999);
			zutiReload_GallonsLitersPerSecond = sectfile.get(SECTION_ID, "ZutiReload_GallonsLitersPerSecond", 3, 0, 99999);
			zutiReload_OneWeaponRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneWeaponRepairSeconds", 3, 0, 99999);
			zutiReload_FlapsRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_FlapsRepairSeconds", 30, 0, 99999);
			zutiReload_EngineRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_EngineRepairSeconds", 90, 0, 99999);
			zutiReload_CockpitRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_CockpitRepairSeconds", 30, 0, 99999);
			zutiReload_OneControlCableRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneControlCableRepairSeconds", 15, 0, 99999);
			zutiReload_OneFuelOilTankRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneFuelOilTankRepairSeconds", 20, 0, 99999);
			zutiReload_LoadoutChangePenaltySeconds = sectfile.get(SECTION_ID, "ZutiReload_LoadoutChangePenaltySeconds", 30, 0, 99999);
			zutiReload_ReloadOnlyIfFuelTanksExist = false;
			if( sectfile.get(SECTION_ID, "ZutiReload_ReloadOnlyIfFuelTanksExist", 0, 0, 1) == 1 )
				zutiReload_ReloadOnlyIfFuelTanksExist = true;
			zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
			if( sectfile.get(SECTION_ID, "ZutiReload_ReloadOnlyIfAmmoBoxesExist", 0, 0, 1) == 1 )
				zutiReload_ReloadOnlyIfAmmoBoxesExist = true;
			zutiReload_RepairOnlyIfWorkshopExist = false;
			if( sectfile.get(SECTION_ID, "ZutiReload_RepairOnlyIfWorkshopExist", 0, 0, 1) == 1 )
				zutiReload_RepairOnlyIfWorkshopExist = true;
			zutiReload_OnlyHomeBaseSpecificLoadouts = true;
			if( sectfile.get(SECTION_ID, "ZutiReload_OnlyHomeBaseSpecificLoadouts", 1, 0, 1) == 0 )
				zutiReload_OnlyHomeBaseSpecificLoadouts = false;
			
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
			zutiReload_OneMgCannonRearmSecond = sectfile.get(OLD_MDS_ID, "ZutiReload_OneMgCannonRearmSecond", 10, 0, 99999);
			zutiReload_OneBombFTankTorpedoeRearmSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneBombFTankTorpedoeRearmSeconds", 25, 0, 99999);
			zutiReload_OneRocketRearmSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneRocketRearmSeconds", 20, 0, 99999);
			zutiReload_GallonsLitersPerSecond = sectfile.get(OLD_MDS_ID, "ZutiReload_GallonsLitersPerSecond", 3, 0, 99999);
			zutiReload_OneWeaponRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneWeaponRepairSeconds", 3, 0, 99999);
			zutiReload_FlapsRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_FlapsRepairSeconds", 30, 0, 99999);
			zutiReload_EngineRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_EngineRepairSeconds", 90, 0, 99999);
			zutiReload_CockpitRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_CockpitRepairSeconds", 30, 0, 99999);
			zutiReload_OneControlCableRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneControlCableRepairSeconds", 15, 0, 99999);
			zutiReload_OneFuelOilTankRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneFuelOilTankRepairSeconds", 20, 0, 99999);
			zutiReload_LoadoutChangePenaltySeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_LoadoutChangePenaltySeconds", 30, 0, 99999);
			zutiReload_ReloadOnlyIfFuelTanksExist = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiReload_ReloadOnlyIfFuelTanksExist", 0, 0, 1) == 1 )
				zutiReload_ReloadOnlyIfFuelTanksExist = true;
			zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiReload_ReloadOnlyIfAmmoBoxesExist", 0, 0, 1) == 1 )
				zutiReload_ReloadOnlyIfAmmoBoxesExist = true;
			zutiReload_RepairOnlyIfWorkshopExist = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiReload_RepairOnlyIfWorkshopExist", 0, 0, 1) == 1 )
				zutiReload_RepairOnlyIfWorkshopExist = true;
			zutiReload_OnlyHomeBaseSpecificLoadouts = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiReload_OnlyHomeBaseSpecificLoadouts", 1, 0, 1) == 0 )
				zutiReload_OnlyHomeBaseSpecificLoadouts = false;
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
			
			sectfile.lineAdd(sectionIndex, "ZutiReload_OneMgCannonRearmSecond", new Integer(zutiReload_OneMgCannonRearmSecond).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_OneBombFTankTorpedoeRearmSeconds", new Integer(zutiReload_OneBombFTankTorpedoeRearmSeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_OneRocketRearmSeconds", new Integer(zutiReload_OneRocketRearmSeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_GallonsLitersPerSecond", new Integer(zutiReload_GallonsLitersPerSecond).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_OneWeaponRepairSeconds", new Integer(zutiReload_OneWeaponRepairSeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_FlapsRepairSeconds", new Integer(zutiReload_FlapsRepairSeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_EngineRepairSeconds", new Integer(zutiReload_EngineRepairSeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_OneControlCableRepairSeconds", new Integer(zutiReload_OneControlCableRepairSeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_OneFuelOilTankRepairSeconds", new Integer(zutiReload_OneFuelOilTankRepairSeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_LoadoutChangePenaltySeconds", new Integer(zutiReload_LoadoutChangePenaltySeconds).toString());
			sectfile.lineAdd(sectionIndex, "ZutiReload_CockpitRepairSeconds", new Integer(zutiReload_CockpitRepairSeconds).toString());
			
			sectfile.lineAdd(sectionIndex, "ZutiReload_ReloadOnlyIfFuelTanksExist", ZutiSupportMethods.boolToInt(zutiReload_ReloadOnlyIfFuelTanksExist));
			sectfile.lineAdd(sectionIndex, "ZutiReload_ReloadOnlyIfAmmoBoxesExist", ZutiSupportMethods.boolToInt(zutiReload_ReloadOnlyIfAmmoBoxesExist));
			sectfile.lineAdd(sectionIndex, "ZutiReload_RepairOnlyIfWorkshopExist", ZutiSupportMethods.boolToInt(zutiReload_RepairOnlyIfWorkshopExist));
			sectfile.lineAdd(sectionIndex, "ZutiReload_OnlyHomeBaseSpecificLoadouts", ZutiSupportMethods.boolToInt(zutiReload_OnlyHomeBaseSpecificLoadouts));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}