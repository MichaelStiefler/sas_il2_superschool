package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Miscellaneous extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS";
	public static final String SECTION_ID = "MDSSection_Miscellaneous";
	
	private boolean zutiMisc_DisableAIRadioChatter;
	private boolean zutiMisc_DespawnAIPlanesAfterLanding;
	private boolean zutiMisc_HidePlayersCountOnHomeBase;
	private boolean zutiMisc_EnableReflyOnlyIfBailedOrDied;
	private boolean zutiMisc_DisableReflyForMissionDuration;
	private int zutiMisc_ReflyKIADelay;
	private int zutiMisc_MaxAllowedKIA;
	private float zutiMisc_ReflyKIADelayMultiplier;
	private boolean zutiMisc_EnableAIAirborneMulticrew;
	private boolean zutiMisc_EnableInstructor;
	private boolean zutiMisc_EnableTowerCommunications;
	private boolean zutiMisc_DisableVectoring;
	
	private GWindowCheckBox wZutiMisc_DisableAIRadioChatter;
	private GWindowCheckBox wZutiMisc_DespawnAIPlanesAfterLanding;
	private GWindowCheckBox wZutiMisc_HidePlayersCountOnHomeBase;
	private GWindowCheckBox wZutiMisc_EnableReflyOnlyIfBailedOrDied;
	private GWindowCheckBox wZutiMisc_DisableReflyForMissionDuration;
	private GWindowEditControl wZutiMisc_ReflyKIADelay;
	private GWindowEditControl wZutiMisc_MaxAllowedKIA;
	private GWindowEditControl wZutiMisc_ReflyKIADelayMultiplier;
	private GWindowCheckBox wZutiMisc_EnableAIAirborneMulticrew;
	private GWindowCheckBox wZutiMisc_EnableInstructor;
	private GWindowCheckBox wZutiRadar_EnableTowerCommunications;
	private GWindowCheckBox wZutiMisc_DisableVectoring;
	
	private GWindowBoxSeparate 	bSeparate_Misc;
	private GWindowLabel 		lSeparate_Misc;
	private GWindowBoxSeparate 	bSeparate_Refly;
	private GWindowLabel 		lSeparate_Refly;
	
	public ZutiMDSSection_Miscellaneous()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 37.0F, 32.5F, true);
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
		title = Plugin.i18n("mds.section.miscellaneous");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		gwindowdialogclient.addLabel(lSeparate_Misc = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 7.0F, 1.6F, Plugin.i18n("mds.misc"), null));
		bSeparate_Misc = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 34.0F, 15.0F);
		bSeparate_Misc.exclude = lSeparate_Misc;
		
		//wZutiRadar_EnableTowerCommunications
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.towerComms"), null));
		gwindowdialogclient.addControl(wZutiRadar_EnableTowerCommunications = new GWindowCheckBox(gwindowdialogclient, 33.0F, 2.0F, null)
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
		
		//wZutiMisc_DisableAIRadioChatter
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.disableAI"), null));
		gwindowdialogclient.addControl(wZutiMisc_DisableAIRadioChatter = new GWindowCheckBox(gwindowdialogclient, 33.0F, 4.0F, null)
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
		
		//wZutiMisc_DespawnAIPlanesAfterLanding
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.despawn"), null));
		gwindowdialogclient.addControl(wZutiMisc_DespawnAIPlanesAfterLanding = new GWindowCheckBox(gwindowdialogclient, 33.0F, 6.0F, null)
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
		
		//wZutiMisc_HidePlayersCountOnHomeBase
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.hideHBNumbers"), null));
		gwindowdialogclient.addControl(wZutiMisc_HidePlayersCountOnHomeBase = new GWindowCheckBox(gwindowdialogclient, 33.0F, 8.0F, null)
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
		
		//wZutiMisc_EnableAIAirborneMulticrew
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 10.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.enableACJoining"), null));
		gwindowdialogclient.addControl(wZutiMisc_EnableAIAirborneMulticrew = new GWindowCheckBox(gwindowdialogclient, 33.0F, 10.0F, null)
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
		
		//wZutiMisc_EnableInstructor
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 12.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.enableInstructor"), null));
		gwindowdialogclient.addControl(wZutiMisc_EnableInstructor = new GWindowCheckBox(gwindowdialogclient, 33.0F, 12.0F, null)
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
		
		//wZutiMisc_DisableVectoring
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.disableVectoring"), null));
		gwindowdialogclient.addControl(wZutiMisc_DisableVectoring = new GWindowCheckBox(gwindowdialogclient, 33.0F, 14.0F, null)
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
					
		gwindowdialogclient.addLabel(lSeparate_Refly = new GWindowLabel(gwindowdialogclient, 3.0F, 17.5F, 3.0F, 1.6F, Plugin.i18n("mds.refly"), null));
		bSeparate_Refly = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 18.0F, 34.0F, 11.0F);
		bSeparate_Refly.exclude = lSeparate_Refly;
		
		//wZutiMisc_EnableReflyOnlyIfBailedOrDied
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 19.0F, 33.0F, 1.3F, Plugin.i18n("mds.refly.disable"), null));
		gwindowdialogclient.addControl(wZutiMisc_EnableReflyOnlyIfBailedOrDied = new GWindowCheckBox(gwindowdialogclient, 33.0F, 19.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}
			
			public boolean notify(int i, int i_18_)
			{
				wZutiMisc_ReflyKIADelay.setEnable(isChecked());
				wZutiMisc_ReflyKIADelayMultiplier.setEnable(isChecked());
				wZutiMisc_MaxAllowedKIA.setEnable(isChecked());
			
				//Disable static icons
				if( isChecked() )
					wZutiMisc_DisableReflyForMissionDuration.setChecked(false, false);
				
				if (i != 2)
					return false;
				
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiMisc_ReflyKIADelay
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 21.0F, 33.0F, 1.3F, Plugin.i18n("mds.refly.delay"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 33.5F, 21.0F, 33.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiMisc_ReflyKIADelay = new GWindowEditControl(gwindowdialogclient, 29.0F, 21.0F, 4.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				setEnable(wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked());
			
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		//wZutiMisc_ReflyKIADelayMultiplier
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 23.0F, 24.0F, 1.3F, Plugin.i18n("mds.refly.KIA"), null));
		gwindowdialogclient.addControl(wZutiMisc_ReflyKIADelayMultiplier = new GWindowEditControl(gwindowdialogclient, 29.0F, 23.0F, 5.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				setEnable(wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked());
				
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		//wZutiMisc_MaxAllowedKIA
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 25.0F, 24.0F, 1.3F, Plugin.i18n("mds.refly.limit"), null));
		gwindowdialogclient.addControl(wZutiMisc_MaxAllowedKIA = new GWindowEditControl(gwindowdialogclient, 29.0F, 25.0F, 5.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public boolean notify(int i, int i_18_)
			{
				setEnable(wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked());
				
				if (i != 2)
					return false;
					
				setMDSVariables();
				return false;
			}
		});
		
		//wZutiMisc_DisableReflyForMissionDuration
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 27.0F, 33.0F, 1.3F, Plugin.i18n("mds.refly.disableMission"), null));
		gwindowdialogclient.addControl(wZutiMisc_DisableReflyForMissionDuration = new GWindowCheckBox(gwindowdialogclient, 33.0F, 27.0F, null)
		{
			public void preRender()
			{
				super.preRender();
				//setChecked(World.cur().isTimeOfDayConstant(), false);
			}

			public boolean notify(int i, int i_18_)
			{
				//Disable static icons
				if( isChecked() )
					wZutiMisc_EnableReflyOnlyIfBailedOrDied.setChecked(false, false);
			
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
		zutiMisc_EnableTowerCommunications	= true;
		zutiMisc_DisableVectoring = false;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiRadar_EnableTowerCommunications.setChecked(zutiMisc_EnableTowerCommunications, false);
		wZutiMisc_DisableAIRadioChatter.setChecked(zutiMisc_DisableAIRadioChatter, false);
		wZutiMisc_DespawnAIPlanesAfterLanding.setChecked(zutiMisc_DespawnAIPlanesAfterLanding, false);
		wZutiMisc_HidePlayersCountOnHomeBase.setChecked(zutiMisc_HidePlayersCountOnHomeBase, false);
		wZutiMisc_EnableReflyOnlyIfBailedOrDied.setChecked(zutiMisc_EnableReflyOnlyIfBailedOrDied, false);
		wZutiMisc_DisableReflyForMissionDuration.setChecked(zutiMisc_DisableReflyForMissionDuration, false);
		wZutiMisc_ReflyKIADelay.setValue(new Integer(zutiMisc_ReflyKIADelay).toString(), false);
		wZutiMisc_MaxAllowedKIA.setValue(new Integer(zutiMisc_MaxAllowedKIA).toString(), false);
		wZutiMisc_ReflyKIADelayMultiplier.setValue(new Float(zutiMisc_ReflyKIADelayMultiplier).toString(), false);
		wZutiMisc_EnableAIAirborneMulticrew.setChecked(zutiMisc_EnableAIAirborneMulticrew, false);
		wZutiMisc_EnableInstructor.setChecked(zutiMisc_EnableInstructor, false);
		wZutiMisc_DisableVectoring.setChecked(zutiMisc_DisableVectoring, false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiMisc_EnableTowerCommunications = wZutiRadar_EnableTowerCommunications.isChecked();
		zutiMisc_DisableAIRadioChatter = wZutiMisc_DisableAIRadioChatter.isChecked();
		zutiMisc_DespawnAIPlanesAfterLanding = wZutiMisc_DespawnAIPlanesAfterLanding.isChecked();
		zutiMisc_HidePlayersCountOnHomeBase = wZutiMisc_HidePlayersCountOnHomeBase.isChecked();
		zutiMisc_EnableReflyOnlyIfBailedOrDied = wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked();
		zutiMisc_DisableReflyForMissionDuration = wZutiMisc_DisableReflyForMissionDuration.isChecked();
		zutiMisc_ReflyKIADelay = Integer.parseInt(wZutiMisc_ReflyKIADelay.getValue());
		zutiMisc_MaxAllowedKIA = Integer.parseInt(wZutiMisc_MaxAllowedKIA.getValue());
		zutiMisc_ReflyKIADelayMultiplier = Float.parseFloat(wZutiMisc_ReflyKIADelayMultiplier.getValue());
		zutiMisc_EnableAIAirborneMulticrew = wZutiMisc_EnableAIAirborneMulticrew.isChecked();
		zutiMisc_EnableInstructor = wZutiMisc_EnableInstructor.isChecked();
		zutiMisc_DisableVectoring = wZutiMisc_DisableVectoring.isChecked();
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiMisc_EnableTowerCommunications = true;
			if( sectfile.get(SECTION_ID, "ZutiMisc_EnableTowerCommunications", 1, 0, 1) == 0 )
				zutiMisc_EnableTowerCommunications = false;
			zutiMisc_DisableAIRadioChatter = false;
			if( sectfile.get(SECTION_ID, "ZutiMisc_DisableAIRadioChatter", 0, 0, 1) == 1 )
				zutiMisc_DisableAIRadioChatter = true;
			zutiMisc_DespawnAIPlanesAfterLanding = true;
			if( sectfile.get(SECTION_ID, "ZutiMisc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0 )
				zutiMisc_DespawnAIPlanesAfterLanding = false;
			zutiMisc_HidePlayersCountOnHomeBase = false;
			if( sectfile.get(SECTION_ID, "ZutiMisc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1 )
				zutiMisc_HidePlayersCountOnHomeBase = true;
			zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
			if( sectfile.get(SECTION_ID, "ZutiMisc_EnableReflyOnlyIfBailedOrDied", 0, 0, 1) == 1 )
				zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
			zutiMisc_DisableReflyForMissionDuration = false;
			if( sectfile.get(SECTION_ID, "ZutiMisc_DisableReflyForMissionDuration", 0, 0, 1) == 1 )
				zutiMisc_DisableReflyForMissionDuration = true;
			zutiMisc_ReflyKIADelay = sectfile.get(SECTION_ID, "ZutiMisc_ReflyKIADelay", 15, 0, 99999);
			zutiMisc_MaxAllowedKIA = sectfile.get(SECTION_ID, "ZutiMisc_MaxAllowedKIA", 10, 1, 99999);
			zutiMisc_ReflyKIADelayMultiplier = sectfile.get(SECTION_ID, "ZutiMisc_ReflyKIADelayMultiplier", 2.0F, 1.0F, 99999.0F);
			zutiMisc_EnableAIAirborneMulticrew = false;
			if( sectfile.get(SECTION_ID, "ZutiMisc_EnableAIAirborneMulticrew", 0, 0, 1) == 1 )
				zutiMisc_EnableAIAirborneMulticrew = true;
			zutiMisc_EnableInstructor = false;
			if( sectfile.get(SECTION_ID, "ZutiMisc_EnableInstructor", 0, 0, 1) == 1 )
				zutiMisc_EnableInstructor = true;
			zutiMisc_DisableVectoring = false;
			if( sectfile.get(SECTION_ID, "ZutiMisc_DisableVectoring", 0, 0, 1) == 1 )
				zutiMisc_DisableVectoring = true;
			
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
			zutiMisc_EnableTowerCommunications = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiRadar_EnableTowerCommunications", 1, 0, 1) == 0 )
				zutiMisc_EnableTowerCommunications = false;
			zutiMisc_DisableAIRadioChatter = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_DisableAIRadioChatter", 0, 0, 1) == 1 )
				zutiMisc_DisableAIRadioChatter = true;
			zutiMisc_DespawnAIPlanesAfterLanding = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0 )
				zutiMisc_DespawnAIPlanesAfterLanding = false;
			zutiMisc_HidePlayersCountOnHomeBase = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1 )
				zutiMisc_HidePlayersCountOnHomeBase = true;
			zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_EnableReflyOnlyIfBailedOrDied", 0, 0, 1) == 1 )
				zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
			zutiMisc_DisableReflyForMissionDuration = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_DisableReflyForMissionDuration", 0, 0, 1) == 1 )
				zutiMisc_DisableReflyForMissionDuration = true;
			zutiMisc_ReflyKIADelay = sectfile.get(OLD_MDS_ID, "ZutiMisc_ReflyKIADelay", 15, 0, 99999);
			zutiMisc_MaxAllowedKIA = sectfile.get(OLD_MDS_ID, "ZutiMisc_MaxAllowedKIA", 10, 1, 99999);
			zutiMisc_ReflyKIADelayMultiplier = sectfile.get(OLD_MDS_ID, "ZutiMisc_ReflyKIADelayMultiplier", 2.0F, 1.0F, 99999.0F);
			zutiMisc_EnableAIAirborneMulticrew = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_EnableAIAirborneMulticrew", 0, 0, 1) == 1 )
				zutiMisc_EnableAIAirborneMulticrew = true;
			zutiMisc_EnableInstructor = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_EnableInstructor", 0, 0, 1) == 1 )
				zutiMisc_EnableInstructor = true;
			zutiMisc_DisableVectoring = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiMisc_DisableVectoring", 0, 0, 1) == 1 )
				zutiMisc_DisableVectoring = true;
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
			
			sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableTowerCommunications", ZutiSupportMethods.boolToInt(zutiMisc_EnableTowerCommunications));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_DisableAIRadioChatter", ZutiSupportMethods.boolToInt(zutiMisc_DisableAIRadioChatter));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_DespawnAIPlanesAfterLanding", ZutiSupportMethods.boolToInt(zutiMisc_DespawnAIPlanesAfterLanding));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_HidePlayersCountOnHomeBase", ZutiSupportMethods.boolToInt(zutiMisc_HidePlayersCountOnHomeBase));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableReflyOnlyIfBailedOrDied", ZutiSupportMethods.boolToInt(zutiMisc_EnableReflyOnlyIfBailedOrDied));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_DisableReflyForMissionDuration", ZutiSupportMethods.boolToInt(zutiMisc_DisableReflyForMissionDuration));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_ReflyKIADelay", new Integer(zutiMisc_ReflyKIADelay).toString());
			sectfile.lineAdd(sectionIndex, "ZutiMisc_MaxAllowedKIA", new Integer(zutiMisc_MaxAllowedKIA).toString());
			sectfile.lineAdd(sectionIndex, "ZutiMisc_ReflyKIADelayMultiplier", new Float(zutiMisc_ReflyKIADelayMultiplier).toString());
			sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableAIAirborneMulticrew", ZutiSupportMethods.boolToInt(zutiMisc_EnableAIAirborneMulticrew));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableInstructor", ZutiSupportMethods.boolToInt(zutiMisc_EnableInstructor));
			sectfile.lineAdd(sectionIndex, "ZutiMisc_DisableVectoring", ZutiSupportMethods.boolToInt(zutiMisc_DisableVectoring));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}