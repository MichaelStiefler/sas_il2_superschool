package com.maddox.il2.builder;

import java.util.StringTokenizer;
import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiMDSVariables;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Radar extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS";
	public static final String SECTION_ID = "MDSSection_Radar";
	public static final String RED_SCOUTS = "MDS_Scouts_Red";
	public static final String BLUE_SCOUTS = "MDS_Scouts_Blue";
	
	private Zuti_WManageAircrafts zuti_manageAircrafts;
	
	private int zutiRadar_RefreshInterval;
	private boolean zutiRadar_ShipsAsRadar;
	private int zutiRadar_ShipRadar_MaxRange;
	private int zutiRadar_ShipRadar_MinHeight;
	private int zutiRadar_ShipRadar_MaxHeight;
	private int zutiRadar_ShipSmallRadar_MaxRange;
	private int zutiRadar_ShipSmallRadar_MinHeight;
	private int zutiRadar_ShipSmallRadar_MaxHeight;
	private boolean zutiRadar_ScoutsAsRadar;
	private int zutiRadar_ScoutRadar_MaxRange;
	private int zutiRadar_ScoutRadar_DeltaHeight;
	private String zutiRadar_ScoutRadarType_Red;
	private String zutiRadar_ScoutRadarType_Blue;
	private int zutiRadar_ScoutGroundObjects_Alpha;
	private boolean zutiRadar_ScoutCompleteRecon;
	
	private GWindowCheckBox 	wZutiRadar_IsRadarInAdvancedMode;
	private GWindowEditControl 	wZutiRadar_RefreshInterval;
	private GWindowCheckBox 	wZutiRadar_ShipsAsRadar;
	private GWindowEditControl 	wZutiRadar_ShipRadar_MaxRange;
	private GWindowEditControl 	wZutiRadar_ShipRadar_MinHeight;
	private GWindowEditControl 	wZutiRadar_ShipRadar_MaxHeight;
	private GWindowEditControl 	wZutiRadar_ShipSmallRadar_MaxRange;
	private GWindowEditControl 	wZutiRadar_ShipSmallRadar_MinHeight;
	private GWindowEditControl 	wZutiRadar_ShipSmallRadar_MaxHeight;
	private GWindowCheckBox 	wZutiRadar_ScoutsAsRadar;
	private GWindowEditControl 	wZutiRadar_ScoutRadar_MaxRange;
	private GWindowEditControl 	wZutiRadar_ScoutRadar_DeltaHeight;
	private GWindowEditControl 	wZutiRadar_ScoutRadarType_Red;
	private GWindowEditControl 	wZutiRadar_ScoutRadarType_Blue;
	private GWindowComboControl wZutiRadar_ScoutGroundObjects_Alpha;
	private GWindowCheckBox		wZutiRadar_ScoutCompleteRecon;
	
	private GWindowBoxSeparate 	bSeparate_General;
	private GWindowLabel 		lSeparate_General;
	private GWindowBoxSeparate 	bSeparate_Ships;
	private GWindowLabel 		lSeparate_Ships;
	private GWindowBoxSeparate 	bSeparate_Scouts;
	private GWindowLabel 		lSeparate_Scouts;
	
	public ZutiMDSSection_Radar()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 47.0F, 34.0F, true);
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
		title = Plugin.i18n("mds.section.radar");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		
		gwindowdialogclient.addLabel(lSeparate_General = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 4.0F, 1.6F, Plugin.i18n("mds.section.radar.general"), null));
		bSeparate_General = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 44.0F, 5.0F);
		bSeparate_General.exclude = lSeparate_General;
		//wZutiRadar_IsRadarInAdvancedMode
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 42.0F, 1.3F, Plugin.i18n("mds.radar.rangeLimitation"), null));
		gwindowdialogclient.addControl(wZutiRadar_IsRadarInAdvancedMode = new GWindowCheckBox(gwindowdialogclient, 42.0F, 2.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if( wZutiRadar_ShipsAsRadar.isChecked() )
				{
					wZutiRadar_ShipRadar_MaxRange.setEnable(isChecked());
					wZutiRadar_ShipRadar_MinHeight.setEnable(isChecked());
					wZutiRadar_ShipRadar_MaxHeight.setEnable(isChecked());
					wZutiRadar_ShipSmallRadar_MaxRange.setEnable(isChecked());
					wZutiRadar_ShipSmallRadar_MinHeight.setEnable(isChecked());
					wZutiRadar_ShipSmallRadar_MaxHeight.setEnable(isChecked());
				}
				
				if( wZutiRadar_ScoutsAsRadar.isChecked() )
				{
					wZutiRadar_ScoutRadar_MaxRange.setEnable(isChecked());
					wZutiRadar_ScoutRadar_DeltaHeight.setEnable(isChecked());
					wZutiRadar_ScoutGroundObjects_Alpha.setEnable(isChecked());
				}
				
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_RefreshInterval
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 40.0F, 1.3F, Plugin.i18n("mds.radar.refresh"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 4.0F, 4.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiRadar_RefreshInterval = new GWindowEditControl(gwindowdialogclient, 39.0F, 4.0F, 4.0F, 1.3F, "")
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
		
		
		gwindowdialogclient.addLabel(lSeparate_Ships = new GWindowLabel(gwindowdialogclient, 3.0F, 7.5F, 3.0F, 1.6F, Plugin.i18n("mds.section.radar.ships"), null));
		bSeparate_Ships = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 8.0F, 44.0F, 9.0F);
		bSeparate_Ships.exclude = lSeparate_Ships;
		//wZutiRadar_ShipsAsRadar
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 9.0F, 42.0F, 1.3F, Plugin.i18n("mds.radar.shipsAsRadars"), null));
		gwindowdialogclient.addControl(wZutiRadar_ShipsAsRadar = new GWindowCheckBox(gwindowdialogclient, 42.0F, 9.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if( wZutiRadar_IsRadarInAdvancedMode.isChecked() )
				{
					wZutiRadar_ShipRadar_MaxRange.setEnable(isChecked());
					wZutiRadar_ShipRadar_MinHeight.setEnable(isChecked());
					wZutiRadar_ShipRadar_MaxHeight.setEnable(isChecked());
					wZutiRadar_ShipSmallRadar_MaxRange.setEnable(isChecked());
					wZutiRadar_ShipSmallRadar_MinHeight.setEnable(isChecked());
					wZutiRadar_ShipSmallRadar_MaxHeight.setEnable(isChecked());
				}
				
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShipRadar_MaxRange
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 11.0F, 17.0F, 1.3F, Plugin.i18n("mds.radar.bigShipMax"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 11.0F, 17.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
		gwindowdialogclient.addControl(wZutiRadar_ShipRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 15.0F, 11.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShipRadar_MinHeight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 17.0F, 1.3F, Plugin.i18n("mds.radar.bigShipMin"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 13.0F, 17.0F, 1.3F, Plugin.i18n("mds.meter"), null));
		gwindowdialogclient.addControl(wZutiRadar_ShipRadar_MinHeight = new GWindowEditControl(gwindowdialogclient, 15.0F, 13.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShipRadar_MaxHeight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 17.0F, 1.3F, Plugin.i18n("mds.radar.bigShipMaxH"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 15.0F, 17.0F, 1.3F, Plugin.i18n("mds.meter"), null));
		gwindowdialogclient.addControl(wZutiRadar_ShipRadar_MaxHeight = new GWindowEditControl(gwindowdialogclient, 15.0F, 15.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//Small ships settings
		//wZutiRadar_ShipSmallRadar_MaxRange
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 11.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.smallShipMax"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 11.0F, 14.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
		gwindowdialogclient.addControl(wZutiRadar_ShipSmallRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 39.0F, 11.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShipSmallRadar_MinHeight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 13F, 14.0F, 1.3F, Plugin.i18n("mds.radar.smallShipMin"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 13.0F, 14.0F, 1.3F, Plugin.i18n("mds.meter"), null));
		gwindowdialogclient.addControl(wZutiRadar_ShipSmallRadar_MinHeight = new GWindowEditControl(gwindowdialogclient, 39.0F, 13.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ShipSmallRadar_MaxHeight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 15.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.smallShipMaxH"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 15.0F, 14.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
		gwindowdialogclient.addControl(wZutiRadar_ShipSmallRadar_MaxHeight = new GWindowEditControl(gwindowdialogclient, 39.0F, 15.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		
		gwindowdialogclient.addLabel(lSeparate_Scouts = new GWindowLabel(gwindowdialogclient, 3.0F, 18.5F, 4.0F, 1.6F, Plugin.i18n("mds.section.radar.scouts"), null));
		bSeparate_Scouts = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 19.0F, 44.0F, 11.0F);
		bSeparate_Scouts.exclude = lSeparate_Scouts;		
		//wZutiRadar_ScoutsAsRadar
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 20.0F, 42.0F, 1.3F, Plugin.i18n("mds.radar.scoutsAsRadars"), null));
		gwindowdialogclient.addControl(wZutiRadar_ScoutsAsRadar = new GWindowCheckBox(gwindowdialogclient, 42.0F, 20.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				if( wZutiRadar_IsRadarInAdvancedMode.isChecked() )
				{
					wZutiRadar_ScoutRadar_MaxRange.setEnable(isChecked());
					wZutiRadar_ScoutRadar_DeltaHeight.setEnable(isChecked());
					wZutiRadar_ScoutGroundObjects_Alpha.setEnable(isChecked());
				}
			
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ScoutRadar_MaxRange
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 22.0F, 13.0F, 1.3F, Plugin.i18n("mds.radar.scoutAcScanMax"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 22.0F, 17.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
		gwindowdialogclient.addControl(wZutiRadar_ScoutRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 15.0F, 22.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiRadar_ScoutRadar_DeltaHeight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 24F, 13.0F, 1.3F, Plugin.i18n("mds.radar.scoutAcDelta"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 24.0F, 17.0F, 1.3F, Plugin.i18n("mds.meter"), null));
		gwindowdialogclient.addControl(wZutiRadar_ScoutRadar_DeltaHeight = new GWindowEditControl(gwindowdialogclient, 15.0F, 24.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		//wZutiRadar_ScoutGroundObjects_Alpha
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 22.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.scoutGroundAlpha"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 22.0F, 14.0F, 1.3F, Plugin.i18n("mds.degrees"), null));
		gwindowdialogclient.addControl(wZutiRadar_ScoutGroundObjects_Alpha = new GWindowComboControl(gwindowdialogclient, 39.0F, 22.0F, 4.0F)
		{
			public void afterCreated()
			{
				super.afterCreated();
				setEnable(false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		wZutiRadar_ScoutGroundObjects_Alpha.setEditable(false);
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("30"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("35"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("40"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("45"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("50"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("55"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("60"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("65"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("70"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("75"));
		wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("80"));
		
		//wZutiRadar_ScoutCompleteRecon
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 24.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.scoutCmplRecon"), null));
		gwindowdialogclient.addControl(wZutiRadar_ScoutCompleteRecon = new GWindowCheckBox(gwindowdialogclient, 42.0F, 24.0F, null)
		{
			public void afterCreated()
			{
				super.afterCreated();
			}
			
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
		
		//bZutiRadar_ScoutRadarType_Red
		gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 26.0F, 9.0F, 1.3F, Plugin.i18n("mds.radar.scoutRed"), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				if( zuti_manageAircrafts == null )
				{
					zuti_manageAircrafts = new Zuti_WManageAircrafts();
				}
				
				if (zuti_manageAircrafts.isVisible())
				{
					zuti_manageAircrafts.hideWindow();
					zuti_manageAircrafts.clearAirNames();
				}
				else
				{
					zuti_manageAircrafts.setTitle(Plugin.i18n("mds.radar.scoutRedTitle"));
					zuti_manageAircrafts.setParentEditControl(wZutiRadar_ScoutRadarType_Red, false);
					zuti_manageAircrafts.enableAcModifications(false);
					zuti_manageAircrafts.showWindow();
				}
				return true;
			}
		});
		
		//wZutiRadar_ScoutRadarType_Red
		gwindowdialogclient.addControl(wZutiRadar_ScoutRadarType_Red = new GWindowEditControl(gwindowdialogclient, 12.0F, 26.0F, 32.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = false;
				bDelayedNotify = true;
				bCanEdit = false;
			}
			
			public boolean notify(int i, int i_18_)
			{					
				if (i != 2)
					return false;
				
				setMDSVariables();
				return false;
			}
		});
		
		//bZutiRadar_ScoutRadarType_Blue
		gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 28.0F, 9.0F, 1.3F, Plugin.i18n("mds.radar.scoutBlue"), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				if( zuti_manageAircrafts == null )
				{
					zuti_manageAircrafts = new Zuti_WManageAircrafts();
				}
				
				if (zuti_manageAircrafts.isVisible())
				{
					zuti_manageAircrafts.hideWindow();
					zuti_manageAircrafts.clearAirNames();
				}
				else
				{
					zuti_manageAircrafts.setTitle(Plugin.i18n("mds.radar.scoutBlueTitle"));
					zuti_manageAircrafts.setParentEditControl(wZutiRadar_ScoutRadarType_Blue, false);
					zuti_manageAircrafts.enableAcModifications(false);
					zuti_manageAircrafts.showWindow();
				}
				return true;
			}
		});
		//wZutiRadar_ScoutRadarType_Blue
		gwindowdialogclient.addControl(wZutiRadar_ScoutRadarType_Blue = new GWindowEditControl(gwindowdialogclient, 12.0F, 28.0F, 32.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = false;
				bDelayedNotify = true;
				bCanEdit = false;
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
		zutiRadar_ScoutRadarType_Red = "";
		zutiRadar_ScoutRadarType_Blue = "";
		zutiRadar_RefreshInterval = 0;
		zutiRadar_ScoutGroundObjects_Alpha = 5;
		zutiRadar_ScoutCompleteRecon = false;
		ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiRadar_IsRadarInAdvancedMode.setChecked(ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE, false);
		wZutiRadar_RefreshInterval.setValue(new Integer(zutiRadar_RefreshInterval).toString(), false);
		wZutiRadar_ShipsAsRadar.setChecked(zutiRadar_ShipsAsRadar, false);
		wZutiRadar_ShipRadar_MaxRange.setValue(new Integer(zutiRadar_ShipRadar_MaxRange).toString(), false);
		wZutiRadar_ShipRadar_MinHeight.setValue(new Integer(zutiRadar_ShipRadar_MinHeight).toString(), false);
		wZutiRadar_ShipRadar_MaxHeight.setValue(new Integer(zutiRadar_ShipRadar_MaxHeight).toString(), false);
		wZutiRadar_ShipSmallRadar_MaxRange.setValue(new Integer(zutiRadar_ShipSmallRadar_MaxRange).toString(), false);
		wZutiRadar_ShipSmallRadar_MinHeight.setValue(new Integer(zutiRadar_ShipSmallRadar_MinHeight).toString(), false);
		wZutiRadar_ShipSmallRadar_MaxHeight.setValue(new Integer(zutiRadar_ShipSmallRadar_MaxHeight).toString(), false);
		wZutiRadar_ScoutsAsRadar.setChecked(zutiRadar_ScoutsAsRadar, false);
		wZutiRadar_ScoutRadar_MaxRange.setValue(new Integer(zutiRadar_ScoutRadar_MaxRange).toString(), false);
		wZutiRadar_ScoutRadar_DeltaHeight.setValue(new Integer(zutiRadar_ScoutRadar_DeltaHeight).toString(), false);
		wZutiRadar_ScoutRadarType_Red.setValue(zutiRadar_ScoutRadarType_Red, false);
		wZutiRadar_ScoutRadarType_Blue.setValue(zutiRadar_ScoutRadarType_Blue, false);
		wZutiRadar_ScoutGroundObjects_Alpha.setSelected(zutiRadar_ScoutGroundObjects_Alpha-1, true, false);
		wZutiRadar_ScoutCompleteRecon.setChecked(zutiRadar_ScoutCompleteRecon, false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = wZutiRadar_IsRadarInAdvancedMode.isChecked();
		zutiRadar_RefreshInterval = Integer.parseInt(wZutiRadar_RefreshInterval.getValue());
		zutiRadar_ShipsAsRadar = wZutiRadar_ShipsAsRadar.isChecked();
		zutiRadar_ShipRadar_MaxRange = Integer.parseInt(wZutiRadar_ShipRadar_MaxRange.getValue());
		zutiRadar_ShipRadar_MinHeight = Integer.parseInt(wZutiRadar_ShipRadar_MinHeight.getValue());
		zutiRadar_ShipRadar_MaxHeight = Integer.parseInt(wZutiRadar_ShipRadar_MaxHeight.getValue());
		zutiRadar_ShipSmallRadar_MaxRange = Integer.parseInt(wZutiRadar_ShipSmallRadar_MaxRange.getValue());
		zutiRadar_ShipSmallRadar_MinHeight = Integer.parseInt(wZutiRadar_ShipSmallRadar_MinHeight.getValue());
		zutiRadar_ShipSmallRadar_MaxHeight = Integer.parseInt(wZutiRadar_ShipSmallRadar_MaxHeight.getValue());
		zutiRadar_ScoutsAsRadar = wZutiRadar_ScoutsAsRadar.isChecked();
		zutiRadar_ScoutRadar_MaxRange = Integer.parseInt(wZutiRadar_ScoutRadar_MaxRange.getValue());
		zutiRadar_ScoutRadar_DeltaHeight = Integer.parseInt(wZutiRadar_ScoutRadar_DeltaHeight.getValue());
		zutiRadar_ScoutRadarType_Red = wZutiRadar_ScoutRadarType_Red.getValue();
		zutiRadar_ScoutRadarType_Blue = wZutiRadar_ScoutRadarType_Blue.getValue();				
		zutiRadar_ScoutGroundObjects_Alpha = wZutiRadar_ScoutGroundObjects_Alpha.getSelected()+1;
		zutiRadar_ScoutCompleteRecon = wZutiRadar_ScoutCompleteRecon.isChecked();
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiRadar_ShipsAsRadar = false;
			if( sectfile.get(SECTION_ID, "ZutiRadar_ShipsAsRadar", 0, 0, 1) == 1 )
				zutiRadar_ShipsAsRadar = true;
			zutiRadar_ShipRadar_MaxRange = sectfile.get(SECTION_ID, "ZutiRadar_ShipRadar_MaxRange", 100, 1, 99999);
			zutiRadar_ShipRadar_MinHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipRadar_MinHeight", 100, 0, 99999);
			zutiRadar_ShipRadar_MaxHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipRadar_MaxHeight", 5000, 1000, 99999);
			zutiRadar_ShipSmallRadar_MaxRange = sectfile.get(SECTION_ID, "ZutiRadar_ShipSmallRadar_MaxRange", 25, 1, 99999);
			zutiRadar_ShipSmallRadar_MinHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipSmallRadar_MinHeight", 0, 0, 99999);
			zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipSmallRadar_MaxHeight", 2000, 1000, 99999);	
			
			zutiRadar_ScoutsAsRadar = false;
			if( sectfile.get(SECTION_ID, "ZutiRadar_ScoutsAsRadar", 0, 0, 1) == 1 )
				zutiRadar_ScoutsAsRadar = true;
			zutiRadar_ScoutRadar_MaxRange = sectfile.get(SECTION_ID, "ZutiRadar_ScoutRadar_MaxRange", 2, 1, 99999);
			zutiRadar_ScoutRadar_DeltaHeight = sectfile.get(SECTION_ID, "ZutiRadar_ScoutRadar_DeltaHeight", 1500, 100, 99999);
			
			zutiRadar_ScoutCompleteRecon = false;
			if( sectfile.get(SECTION_ID, "ZutiRadar_ScoutCompleteRecon", 0, 0, 1) == 1 )
				zutiRadar_ScoutCompleteRecon = true;

			zutiRadar_ScoutGroundObjects_Alpha = sectfile.get(SECTION_ID, "ZutiRadar_ScoutGroundObjects_Alpha", 5, 1, 11);//, default, min, max)
			zutiRadar_RefreshInterval = sectfile.get(SECTION_ID, "ZutiRadar_RefreshInterval", 0, 0, 99999);
			
			zutiLoadScouts_Red(sectfile);
			zutiLoadScouts_Blue(sectfile);
			
			ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
			if( sectfile.get(SECTION_ID, "ZutiRadar_SetRadarToAdvanceMode", 0, 0, 1) == 1 )
				ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = true;
			
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
			zutiRadar_ShipsAsRadar = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipsAsRadar", 0, 0, 1) == 1 )
				zutiRadar_ShipsAsRadar = true;
			zutiRadar_ShipRadar_MaxRange = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipRadar_MaxRange", 100, 1, 99999);
			zutiRadar_ShipRadar_MinHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipRadar_MinHeight", 100, 0, 99999);
			zutiRadar_ShipRadar_MaxHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipRadar_MaxHeight", 5000, 1000, 99999);
			zutiRadar_ShipSmallRadar_MaxRange = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipSmallRadar_MaxRange", 25, 1, 99999);
			zutiRadar_ShipSmallRadar_MinHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipSmallRadar_MinHeight", 0, 0, 99999);
			zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipSmallRadar_MaxHeight", 2000, 1000, 99999);	
			
			zutiRadar_ScoutsAsRadar = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutsAsRadar", 0, 0, 1) == 1 )
				zutiRadar_ScoutsAsRadar = true;
			zutiRadar_ScoutRadar_MaxRange = sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutRadar_MaxRange", 2, 1, 99999);
			zutiRadar_ScoutRadar_DeltaHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutRadar_DeltaHeight", 1500, 100, 99999);
			
			zutiRadar_ScoutCompleteRecon = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutCompleteRecon", 0, 0, 1) == 1 )
				zutiRadar_ScoutCompleteRecon = true;
			
			zutiRadar_ScoutGroundObjects_Alpha = sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutGroundObjects_Alpha", 5, 1, 11);//, default, min, max)
			zutiRadar_RefreshInterval = sectfile.get(OLD_MDS_ID, "ZutiRadar_RefreshInterval", 0, 0, 99999);
			
			zutiLoadScouts_Red(sectfile);
			zutiLoadScouts_Blue(sectfile);
			
			ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_SetRadarToAdvanceMode", 0, 0, 1) == 1 )
				ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = true;
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
			
			sectfile.lineAdd(sectionIndex, "ZutiRadar_RefreshInterval", new Integer(zutiRadar_RefreshInterval).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipsAsRadar", ZutiSupportMethods.boolToInt(zutiRadar_ShipsAsRadar));
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipRadar_MaxRange", new Integer(zutiRadar_ShipRadar_MaxRange).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipRadar_MinHeight", new Integer(zutiRadar_ShipRadar_MinHeight).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipRadar_MaxHeight", new Integer(zutiRadar_ShipRadar_MaxHeight).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipSmallRadar_MaxRange", new Integer(zutiRadar_ShipSmallRadar_MaxRange).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipSmallRadar_MinHeight", new Integer(zutiRadar_ShipSmallRadar_MinHeight).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipSmallRadar_MaxHeight", new Integer(zutiRadar_ShipSmallRadar_MaxHeight).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutsAsRadar", ZutiSupportMethods.boolToInt(zutiRadar_ScoutsAsRadar));
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutRadar_MaxRange", new Integer(zutiRadar_ScoutRadar_MaxRange).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutRadar_DeltaHeight", new Integer(zutiRadar_ScoutRadar_DeltaHeight).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutGroundObjects_Alpha", new Integer(zutiRadar_ScoutGroundObjects_Alpha).toString());
			sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutCompleteRecon", ZutiSupportMethods.boolToInt(zutiRadar_ScoutCompleteRecon));
			sectfile.lineAdd(sectionIndex, "ZutiRadar_SetRadarToAdvanceMode", ZutiSupportMethods.boolToInt(ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE));
			
			zutiSaveScouts_Red(sectfile);
			zutiSaveScouts_Blue(sectfile);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void zutiSaveScouts_Red(SectFile sectfile)
	{
		if( zutiRadar_ScoutRadarType_Red != null && zutiRadar_ScoutRadarType_Red.trim().length() > 0 )
		{
			int sectionId = sectfile.sectionAdd(RED_SCOUTS);
			
			StringTokenizer stringtokenizer = new StringTokenizer(zutiRadar_ScoutRadarType_Red);
			
			while( stringtokenizer.hasMoreTokens() )
			{
				sectfile.lineAdd(sectionId, stringtokenizer.nextToken()); ;
			}
		}
	}
	
	private void zutiSaveScouts_Blue(SectFile sectfile)
	{
		if( zutiRadar_ScoutRadarType_Blue != null && zutiRadar_ScoutRadarType_Blue.trim().length() > 0 )
		{
			int sectionId = sectfile.sectionAdd(BLUE_SCOUTS);
			
			StringTokenizer stringtokenizer = new StringTokenizer(zutiRadar_ScoutRadarType_Blue);
			
			while( stringtokenizer.hasMoreTokens() )
			{
				sectfile.lineAdd(sectionId, stringtokenizer.nextToken()); ;
			}
		}
	}
	
	private void zutiLoadScouts_Red(SectFile sectfile)
	{
		int index = sectfile.sectionIndex(RED_SCOUTS);
		if( index > -1 )
		{
			zutiRadar_ScoutRadarType_Red = "";
			
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
					
					if (var_class != null && Property.containsValue(var_class, "cockpitClass"))
					{
						//Add this ac to modified table for this home base
						zutiRadar_ScoutRadarType_Red += acName + " ";
					}
				}
			}
		}
	}
	
	private void zutiLoadScouts_Blue(SectFile sectfile)
	{
		int index = sectfile.sectionIndex(BLUE_SCOUTS);
		if( index > -1 )
		{
			zutiRadar_ScoutRadarType_Blue = "";
			
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
					
					if (var_class != null && Property.containsValue(var_class, "cockpitClass"))
					{
						//Add this ac to modified table for this home base
						zutiRadar_ScoutRadarType_Blue += acName + " ";
					}
				}
			}
		}
	}
		
	/**
	 * Are scout planes performing radar role?
	 * @return
	 */
	public boolean areScoutsRadars()
	{
		return zutiRadar_ScoutsAsRadar;
	}
}