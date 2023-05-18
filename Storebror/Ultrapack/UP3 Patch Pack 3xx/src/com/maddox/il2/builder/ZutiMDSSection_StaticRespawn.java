package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_StaticRespawn extends GWindowFramed
{
	public static final String SECTION_ID = "RespawnTime";
	
	private int respawnTime_Bigship;
	private int respawnTime_Ship;
	private int respawnTime_Aeroanchored;
	private int respawnTime_Artillery;
	private int respawnTime_Searchlight;
	
	private GWindowEditControl wRespawnTime_Bigship;
	private GWindowEditControl wRespawnTime_Ship;
	private GWindowEditControl wRespawnTime_Aeroanchored;
	private GWindowEditControl wRespawnTime_Artillery;
	private GWindowEditControl wRespawnTime_Searchlight;
	
	public ZutiMDSSection_StaticRespawn()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 18.0F, 14.0F, true);
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
		title = Plugin.i18n("mds.section.staticsRespawn");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		//wRespawnTime_Bigship
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 9.0F, 1.3F, Plugin.i18n("mds.respawn.bigship"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15.5F, 1.0F, 9.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wRespawnTime_Bigship = new GWindowEditControl(gwindowdialogclient, 10.0F, 1.0F, 5.0F, 1.3F, "")
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
		
		//wRespawnTime_Ship
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 9.0F, 1.3F, Plugin.i18n("mds.respawn.ship"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15.5F, 3.0F, 9.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wRespawnTime_Ship = new GWindowEditControl(gwindowdialogclient, 10.0F, 3.0F, 5.0F, 1.3F, "")
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
		
		//wRespawnTime_Aeroanchored
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, 9.0F, 1.3F, Plugin.i18n("mds.respawn.aeroanchored"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15.5F, 5.0F, 9.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wRespawnTime_Aeroanchored = new GWindowEditControl(gwindowdialogclient, 10.0F, 5.0F, 5.0F, 1.3F, "")
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
		
		//wRespawnTime_Artillery
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, 9.0F, 1.3F, Plugin.i18n("mds.respawn.artillery"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15.5F, 7.0F, 9.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wRespawnTime_Artillery = new GWindowEditControl(gwindowdialogclient, 10.0F, 7.0F, 5.0F, 1.3F, "")
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
		
		//wRespawnTime_Searchlight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, 9.0F, 1.3F, Plugin.i18n("mds.respawn.searchlight"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15.5F, 9.0F, 9.0F, 1.3F, Plugin.i18n("mds.second"), null));
		gwindowdialogclient.addControl(wRespawnTime_Searchlight = new GWindowEditControl(gwindowdialogclient, 10.0F, 9.0F, 5.0F, 1.3F, "")
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
		respawnTime_Bigship 		= 1800;
		respawnTime_Ship 			= 1800;
		respawnTime_Aeroanchored 	= 1800;
		respawnTime_Artillery 		= 1800;
		respawnTime_Searchlight 	= 1800;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wRespawnTime_Bigship.setValue(new Integer(respawnTime_Bigship).toString(), false);
		wRespawnTime_Ship.setValue(new Integer(respawnTime_Ship).toString(), false);
		wRespawnTime_Aeroanchored.setValue(new Integer(respawnTime_Aeroanchored).toString(), false);
		wRespawnTime_Artillery.setValue(new Integer(respawnTime_Artillery).toString(), false);
		wRespawnTime_Searchlight.setValue(new Integer(respawnTime_Searchlight).toString(), false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		respawnTime_Bigship = Integer.parseInt(wRespawnTime_Bigship.getValue());
		respawnTime_Ship = Integer.parseInt(wRespawnTime_Ship.getValue());
		respawnTime_Aeroanchored = Integer.parseInt(wRespawnTime_Aeroanchored.getValue());
		respawnTime_Artillery = Integer.parseInt(wRespawnTime_Artillery.getValue());
		respawnTime_Searchlight = Integer.parseInt(wRespawnTime_Searchlight.getValue());
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			respawnTime_Bigship = sectfile.get(SECTION_ID, "Bigship", 1800, 0, 1200000);
			respawnTime_Ship = sectfile.get(SECTION_ID, "Ship", 1800, 0, 1200000);
			respawnTime_Aeroanchored = sectfile.get(SECTION_ID, "Aeroanchored", 1800, 0, 1200000);
			respawnTime_Artillery = sectfile.get(SECTION_ID, "Artillery", 1800, 0, 1200000);
			respawnTime_Searchlight = sectfile.get(SECTION_ID, "Searchlight", 1800, 0, 1200000);
			
			setUIVariables();
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
			
			sectfile.lineAdd(sectionIndex, "Bigship", new Integer(respawnTime_Bigship).toString());
			sectfile.lineAdd(sectionIndex, "Ship", new Integer(respawnTime_Ship).toString());
			sectfile.lineAdd(sectionIndex, "Aeroanchored", new Integer(respawnTime_Aeroanchored).toString());
			sectfile.lineAdd(sectionIndex, "Artillery", new Integer(respawnTime_Artillery).toString());
			sectfile.lineAdd(sectionIndex, "Searchlight", new Integer(respawnTime_Searchlight).toString());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}