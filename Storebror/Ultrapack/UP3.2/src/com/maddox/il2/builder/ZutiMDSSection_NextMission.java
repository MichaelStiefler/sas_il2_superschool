package com.maddox.il2.builder;

import com.maddox.gwindow.GFileFilter;
import com.maddox.gwindow.GFileFilterName;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFileOpen;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_NextMission extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS";
	public static final String SECTION_ID = "MDSSection_NextMission";
	
	private String zutiNextMission_RedWon;
	private String zutiNextMission_BlueWon;
	private String zutiNextMission_Difficulty;
	private int zutiNextMission_LoadDelay;
	private boolean zutiNextMission_Enable;
	
	private GWindowEditControl wZutiNextMission_RedWon;
	private GWindowEditControl wZutiNextMission_BlueWon;
	private GWindowEditControl wZutiNextMission_Difficulty;
	private GWindowEditControl wZutiNextMission_LoadDelay;
	private GWindowButton bZutiNextMission_RedWon;
	private GWindowButton bZutiNextMission_BlueWon;
	private GWindowButton bZutiNextMission_Difficulty;
	private GWindowCheckBox wZutiNextMission_Enable;
	
	public ZutiMDSSection_NextMission()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 44.0F, 14.0F, true);
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
		title = Plugin.i18n("mds.section.nextMission");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		//wZutiNextMission_Enable
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 43.0F, 1.3F, Plugin.i18n("mds.mission.enable"), null));
		gwindowdialogclient.addControl(wZutiNextMission_Enable = new GWindowCheckBox(gwindowdialogclient, 41.0F, 1.0F, null)
		{
			public void preRender()
			{
				super.preRender();
			}

			public boolean notify(int i, int i_18_)
			{					
				bZutiNextMission_RedWon.setEnable(isChecked());
				wZutiNextMission_RedWon.setEnable(isChecked());
				bZutiNextMission_BlueWon.setEnable(isChecked());
				wZutiNextMission_BlueWon.setEnable(isChecked());
				bZutiNextMission_Difficulty.setEnable(isChecked());
				wZutiNextMission_Difficulty.setEnable(isChecked());
				wZutiNextMission_LoadDelay.setEnable(isChecked());

				if (i != 2)
					return false;					
			
				setMDSVariables();
				return false;
			}
		});
		
		//bZutiNextMission_RedWon
		gwindowdialogclient.addControl(bZutiNextMission_RedWon = new GWindowButton(gwindowdialogclient, 1.0F, 3.0F, 9.0F, 1.3F, Plugin.i18n("mds.mission.red"), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
					
				GWindowFileOpen gwindowfileopen = new GWindowFileOpen((Plugin.builder.clientWindow.root), true, Plugin.i18n("mds.mission.redTitle"), "missions", (new GFileFilter[] { new GFileFilterName((Plugin.i18n("MissionFiles")), (new String[] { "*.mis" })) }))
				{
					public void result(String string)
					{
						if (string != null)
							wZutiNextMission_RedWon.setValue(string);
					}
				};
				//Set current file as last assigned file		
				gwindowfileopen.setSelectFile(wZutiNextMission_RedWon.getValue());
				
				return true;
			}
		});
		//wZutiNextMission_RedWon
		gwindowdialogclient.addControl(wZutiNextMission_RedWon = new GWindowEditControl(gwindowdialogclient, 11.0F, 3.0F, 31.0F, 1.3F, "")
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
		
		//bZutiNextMission_BlueWon
		gwindowdialogclient.addControl(bZutiNextMission_BlueWon = new GWindowButton(gwindowdialogclient, 1.0F, 5.0F, 9.0F, 1.3F, Plugin.i18n("mds.mission.blue"), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
					
				GWindowFileOpen gwindowfileopen = new GWindowFileOpen((Plugin.builder.clientWindow.root), true, Plugin.i18n("mds.mission.blueTitle"), "missions", (new GFileFilter[] { new GFileFilterName((Plugin.i18n("MissionFiles")), (new String[] { "*.mis" })) }))
				{
					public void result(String string)
					{
						if (string != null)
							wZutiNextMission_BlueWon.setValue(string);
					}
				};
				//Set current file as last assigned file		
				gwindowfileopen.setSelectFile(wZutiNextMission_BlueWon.getValue());
				
				return true;
			}
		});
		//wZutiNextMission_BlueWon
		gwindowdialogclient.addControl(wZutiNextMission_BlueWon = new GWindowEditControl(gwindowdialogclient, 11.0F, 5.0F, 31.0F, 1.3F, "")
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
		
		//bZutiNextMission_Difficulty
		gwindowdialogclient.addControl(bZutiNextMission_Difficulty = new GWindowButton(gwindowdialogclient, 1.0F, 7.0F, 9.0F, 1.3F, Plugin.i18n("mds.mission.difficulty"), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
					
				GWindowFileOpen gwindowfileopen = new GWindowFileOpen((Plugin.builder.clientWindow.root), true, Plugin.i18n("mds.mission.diffTitle"), "missions", (new GFileFilter[] { new GFileFilterName("Difficulty Files", (new String[] { "*.difficulty" })) }))
				{
					public void result(String string)
					{
						if (string != null)
							wZutiNextMission_Difficulty.setValue(string);
					}
				};
				//Set current file as last assigned file		
				gwindowfileopen.setSelectFile(wZutiNextMission_Difficulty.getValue());
				
				return true;
			}
		});
		//wZutiNextMission_Difficulty
		gwindowdialogclient.addControl(wZutiNextMission_Difficulty = new GWindowEditControl(gwindowdialogclient, 11.0F, 7.0F, 31.0F, 1.3F, "")
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
		
		//wZutiNextMission_LoadDelay
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, 43F, 1.3F, Plugin.i18n("mds.mission.delay"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 41.5F, 9.0F, 43F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wZutiNextMission_LoadDelay = new GWindowEditControl(gwindowdialogclient, 36.0F, 9.0F, 5.0F, 1.3F, "")
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
		zutiNextMission_RedWon 		= "";
		zutiNextMission_BlueWon 	= "";
		zutiNextMission_Difficulty	= "";
		zutiNextMission_LoadDelay 	= 60;

		zutiNextMission_Enable = false;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiNextMission_RedWon.setValue(zutiNextMission_RedWon, false);
		wZutiNextMission_BlueWon.setValue(zutiNextMission_BlueWon, false);
		wZutiNextMission_Difficulty.setValue(zutiNextMission_Difficulty, false);
		wZutiNextMission_LoadDelay.setValue(new Integer(zutiNextMission_LoadDelay).toString(), false);

		wZutiNextMission_Enable.setChecked(zutiNextMission_Enable, false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiNextMission_RedWon = wZutiNextMission_RedWon.getValue();
		zutiNextMission_BlueWon = wZutiNextMission_BlueWon.getValue();
		zutiNextMission_Difficulty = wZutiNextMission_Difficulty.getValue();
		zutiNextMission_LoadDelay = Integer.parseInt(wZutiNextMission_LoadDelay.getValue());
		zutiNextMission_Enable = wZutiNextMission_Enable.isChecked();
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiNextMission_RedWon = sectfile.get(SECTION_ID, "ZutiNextMission_RedWon", "");
			zutiNextMission_BlueWon = sectfile.get(SECTION_ID, "ZutiNextMission_BlueWon", "");
			zutiNextMission_Difficulty = sectfile.get(SECTION_ID, "ZutiNextMission_Difficulty", "");
			zutiNextMission_LoadDelay = sectfile.get(SECTION_ID, "ZutiNextMission_LoadDelay", 60, 60, 99999);
			zutiNextMission_Enable = false;
			if( sectfile.get(SECTION_ID, "ZutiNextMission_Enable", 0, 0, 1) == 1 )
				zutiNextMission_Enable = true;
			
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
			zutiNextMission_RedWon = sectfile.get(OLD_MDS_ID, "ZutiNextMission_RedWon", "");
			zutiNextMission_BlueWon = sectfile.get(OLD_MDS_ID, "ZutiNextMission_BlueWon", "");
			zutiNextMission_Difficulty = sectfile.get(OLD_MDS_ID, "ZutiNextMission_Difficulty", "");
			zutiNextMission_LoadDelay = sectfile.get(OLD_MDS_ID, "ZutiNextMission_LoadDelay", 60, 60, 99999);
			zutiNextMission_Enable = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiNextMission_Enable", 0, 0, 1) == 1 )
				zutiNextMission_Enable = true;
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
			
			sectfile.lineAdd(sectionIndex, "ZutiNextMission_RedWon", zutiNextMission_RedWon);
			sectfile.lineAdd(sectionIndex, "ZutiNextMission_BlueWon", zutiNextMission_BlueWon);
			sectfile.lineAdd(sectionIndex, "ZutiNextMission_Difficulty", zutiNextMission_Difficulty);
			sectfile.lineAdd(sectionIndex, "ZutiNextMission_LoadDelay", new Integer(zutiNextMission_LoadDelay).toString());
			sectfile.lineAdd(sectionIndex, "ZutiNextMission_Enable", ZutiSupportMethods.boolToInt(zutiNextMission_Enable));

			sectfile.lineAdd(sectionIndex, "ZutiNextMission_Enable", ZutiSupportMethods.boolToInt(zutiNextMission_Enable));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}