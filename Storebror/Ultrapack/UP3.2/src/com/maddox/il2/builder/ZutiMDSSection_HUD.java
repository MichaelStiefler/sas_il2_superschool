package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_HUD extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS";
	public static final String SECTION_ID = "MDSSection_HUD";
	
	private boolean zutiHud_DisableHudStatistics;
	private boolean zutiHud_ShowPilotNumber;
	private boolean zutiHud_ShowPilotPing;
	private boolean zutiHud_ShowPilotName;
	private boolean zutiHud_ShowPilotScore;
	private boolean zutiHud_ShowPilotArmy;
	private boolean zutiHud_ShowPilotACDesignation;
	private boolean zutiHud_ShowPilotACType;
	
	private GWindowCheckBox wZutiHud_DisableHudStatistics;
	private GWindowCheckBox wZutiHud_ShowPilotNumber;
	private GWindowCheckBox wZutiHud_ShowPilotPing;
	private GWindowCheckBox wZutiHud_ShowPilotName;
	private GWindowCheckBox wZutiHud_ShowPilotScore;
	private GWindowCheckBox wZutiHud_ShowPilotArmy;
	private GWindowCheckBox wZutiHud_ShowPilotACDesignation;
	private GWindowCheckBox wZutiHud_ShowPilotACType;
	
	public ZutiMDSSection_HUD()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 21.0F, 20.0F, true);
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
		title = Plugin.i18n("mds.section.hud");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.disable"), null));
		gwindowdialogclient.addControl(wZutiHud_DisableHudStatistics = new GWindowCheckBox(gwindowdialogclient, 17.0F, 1.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				wZutiHud_ShowPilotNumber.setEnable(!isChecked());
				wZutiHud_ShowPilotPing.setEnable(!isChecked());
				wZutiHud_ShowPilotName.setEnable(!isChecked());
				wZutiHud_ShowPilotScore.setEnable(!isChecked());
				wZutiHud_ShowPilotArmy.setEnable(!isChecked());
				wZutiHud_ShowPilotACDesignation.setEnable(!isChecked());
				wZutiHud_ShowPilotACType.setEnable(!isChecked());
				
				if (i != 2)
					return false;
				
				setMDSVariables();
				return false;
			}
		});
		//wZutiHud_ShowPilotNumber
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.number"), null));
		gwindowdialogclient.addControl(wZutiHud_ShowPilotNumber = new GWindowCheckBox(gwindowdialogclient, 17.0F, 3.0F, null)
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
		//wZutiHud_ShowPilotPing
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.ping"), null));
		gwindowdialogclient.addControl(wZutiHud_ShowPilotPing = new GWindowCheckBox(gwindowdialogclient, 17.0F, 5.0F, null)
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
		//wZutiHud_ShowPilotName
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.name"), null));
		gwindowdialogclient.addControl(wZutiHud_ShowPilotName = new GWindowCheckBox(gwindowdialogclient, 17.0F, 7.0F, null)
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
		//wZutiHud_ShowPilotScore
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.score"), null));
		gwindowdialogclient.addControl(wZutiHud_ShowPilotScore = new GWindowCheckBox(gwindowdialogclient, 17.0F, 9.0F, null)
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
		//wZutiHud_ShowPilotArmy
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.army"), null));
		gwindowdialogclient.addControl(wZutiHud_ShowPilotArmy = new GWindowCheckBox(gwindowdialogclient, 17.0F, 11.0F, null)
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
		//wZutiHud_ShowPilotACDesignation
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.designation"), null));
		gwindowdialogclient.addControl(wZutiHud_ShowPilotACDesignation = new GWindowCheckBox(gwindowdialogclient, 17.0F, 13.0F, null)
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
		//wZutiHud_ShowPilotACType
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 15.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.type"), null));
		gwindowdialogclient.addControl(wZutiHud_ShowPilotACType = new GWindowCheckBox(gwindowdialogclient, 17.0F, 15.0F, null)
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
		zutiHud_DisableHudStatistics = false;
		zutiHud_ShowPilotNumber	= false;
		zutiHud_ShowPilotPing = true;
		zutiHud_ShowPilotName = true;
		zutiHud_ShowPilotScore = true;
		zutiHud_ShowPilotArmy = true;
		zutiHud_ShowPilotACDesignation = false;
		zutiHud_ShowPilotACType = true;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiHud_DisableHudStatistics.setChecked(zutiHud_DisableHudStatistics, false);
		wZutiHud_ShowPilotNumber.setChecked(zutiHud_ShowPilotNumber, false);
		wZutiHud_ShowPilotPing.setChecked(zutiHud_ShowPilotPing, false);
		wZutiHud_ShowPilotName.setChecked(zutiHud_ShowPilotName, false);
		wZutiHud_ShowPilotScore.setChecked(zutiHud_ShowPilotScore, false);
		wZutiHud_ShowPilotArmy.setChecked(zutiHud_ShowPilotArmy, false);
		wZutiHud_ShowPilotACDesignation.setChecked(zutiHud_ShowPilotACDesignation, false);
		wZutiHud_ShowPilotACType.setChecked(zutiHud_ShowPilotACType, false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiHud_DisableHudStatistics = wZutiHud_DisableHudStatistics.isChecked();
		zutiHud_ShowPilotNumber = wZutiHud_ShowPilotNumber.isChecked();
		zutiHud_ShowPilotPing = wZutiHud_ShowPilotPing.isChecked();
		zutiHud_ShowPilotName = wZutiHud_ShowPilotName.isChecked();
		zutiHud_ShowPilotScore = wZutiHud_ShowPilotScore.isChecked();
		zutiHud_ShowPilotArmy = wZutiHud_ShowPilotArmy.isChecked();
		zutiHud_ShowPilotACDesignation = wZutiHud_ShowPilotACDesignation.isChecked();
		zutiHud_ShowPilotACType = wZutiHud_ShowPilotACType.isChecked();
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiHud_DisableHudStatistics = false;
			if( sectfile.get(SECTION_ID, "ZutiHud_DisableHudStatistics", 0, 0, 1) == 1 )
				zutiHud_DisableHudStatistics = true;
			zutiHud_ShowPilotNumber = false;
			if( sectfile.get(SECTION_ID, "ZutiHud_ShowPilotNumber", 0, 0, 1) == 1 )
				zutiHud_ShowPilotNumber = true;
			zutiHud_ShowPilotPing = true;
			if( sectfile.get(SECTION_ID, "ZutiHud_ShowPilotPing", 1, 0, 1) == 0 )
				zutiHud_ShowPilotPing = false;
			zutiHud_ShowPilotName = true;
			if( sectfile.get(SECTION_ID, "ZutiHud_ShowPilotName", 1, 0, 1) == 0 )
				zutiHud_ShowPilotName = false;
			zutiHud_ShowPilotScore = true;
			if( sectfile.get(SECTION_ID, "ZutiHud_ShowPilotScore", 1, 0, 1) == 0 )
				zutiHud_ShowPilotScore = false;
			zutiHud_ShowPilotArmy = true;
			if( sectfile.get(SECTION_ID, "ZutiHud_ShowPilotArmy", 1, 0, 1) == 0 )
				zutiHud_ShowPilotArmy = false;
			zutiHud_ShowPilotACDesignation = false;
			if( sectfile.get(SECTION_ID, "ZutiHud_ShowPilotACDesignation", 0, 0, 1) == 1 )
				zutiHud_ShowPilotACDesignation = true;
			zutiHud_ShowPilotACType = true;
			if( sectfile.get(SECTION_ID, "ZutiHud_ShowPilotACType", 1, 0, 1) == 0 )
				zutiHud_ShowPilotACType = false;

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
			zutiHud_DisableHudStatistics = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_DisableHudStatistics", 0, 0, 1) == 1 )
				zutiHud_DisableHudStatistics = true;
			zutiHud_ShowPilotNumber = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotNumber", 0, 0, 1) == 1 )
				zutiHud_ShowPilotNumber = true;
			zutiHud_ShowPilotPing = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotPing", 1, 0, 1) == 0 )
				zutiHud_ShowPilotPing = false;
			zutiHud_ShowPilotName = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotName", 1, 0, 1) == 0 )
				zutiHud_ShowPilotName = false;
			zutiHud_ShowPilotScore = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotScore", 1, 0, 1) == 0 )
				zutiHud_ShowPilotScore = false;
			zutiHud_ShowPilotArmy = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotArmy", 1, 0, 1) == 0 )
				zutiHud_ShowPilotArmy = false;
			zutiHud_ShowPilotACDesignation = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotACDesignation", 0, 0, 1) == 1 )
				zutiHud_ShowPilotACDesignation = true;
			zutiHud_ShowPilotACType = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotACType", 1, 0, 1) == 0 )
				zutiHud_ShowPilotACType = false;
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
			
			sectfile.lineAdd(sectionIndex, "ZutiHud_DisableHudStatistics", ZutiSupportMethods.boolToInt(zutiHud_DisableHudStatistics));
			sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotNumber", ZutiSupportMethods.boolToInt(zutiHud_ShowPilotNumber));
			sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotPing", ZutiSupportMethods.boolToInt(zutiHud_ShowPilotPing));
			sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotName", ZutiSupportMethods.boolToInt(zutiHud_ShowPilotName));
			sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotScore", ZutiSupportMethods.boolToInt(zutiHud_ShowPilotScore));
			sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotArmy", ZutiSupportMethods.boolToInt(zutiHud_ShowPilotArmy));
			sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotACDesignation", ZutiSupportMethods.boolToInt(zutiHud_ShowPilotACDesignation));
			sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotACType", ZutiSupportMethods.boolToInt(zutiHud_ShowPilotACType));
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}