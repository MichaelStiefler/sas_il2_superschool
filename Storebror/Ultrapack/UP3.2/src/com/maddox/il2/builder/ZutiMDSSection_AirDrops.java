package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_AirDrops extends GWindowFramed
{
	private static String OLD_MDS_ID = "MDS";
	public static final String SECTION_ID = "MDSSection_AirDrops";
	
	private boolean zutiDrop_OverFriendlyHomeBase;
	private boolean zutiDrop_OverEnemyHomeBase;
	private boolean zutiDrop_OverNeutralHomeBase;
	private boolean zutiDrop_OverFriendlyFrictionArea;
	private boolean zutiDrop_OverEnemyFrictionArea;
	private boolean zutiDrop_OverDestroyGroundArea;
	private boolean zutiDrop_OverDefenceGroundArea;
	private int zutiDrop_MinHeight;
	private int zutiDrop_MaxHeight;
	
	private GWindowCheckBox wZutiDrop_OverFriendlyHomeBase;
	private GWindowCheckBox wZutiDrop_OverEnemyHomeBase;
	private GWindowCheckBox wZutiDrop_OverNeutralHomeBase;
	private GWindowCheckBox wZutiDrop_OverFriendlyFrictionArea;
	private GWindowCheckBox wZutiDrop_OverEnemyFrictionArea;
	private GWindowCheckBox wZutiDrop_OverDestroyGroundArea;
	private GWindowCheckBox wZutiDrop_OverDefenceGroundArea;
	private GWindowEditControl wZutiDrop_MinHeight;
	private GWindowEditControl wZutiDrop_MaxHeight;
	
	private GWindowBoxSeparate 	bSeparate_Misc;
	private GWindowLabel 		lSeparate_Misc;
	private GWindowBoxSeparate 	bSeparate_DropHeight;
	private GWindowLabel 		lSeparate_DropHeight;
	
	public ZutiMDSSection_AirDrops()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 31.0F, 26.5F, true);
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
		title = Plugin.i18n("mds.section.airDrops");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		gwindowdialogclient.addLabel(lSeparate_Misc = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 5.0F, 1.6F, Plugin.i18n("mds.airdrops"), null));
		bSeparate_Misc = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 28.0F, 15.0F);
		bSeparate_Misc.exclude = lSeparate_Misc;
		
		//wZutiDrop_OverFriendlyHomeBase
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 27.0F, 1.3F, Plugin.i18n("mds.airdrops.friendlyHB"), null));
		gwindowdialogclient.addControl(wZutiDrop_OverFriendlyHomeBase = new GWindowCheckBox(gwindowdialogclient, 27.0F, 2.0F, null)
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
		//wZutiDrop_OverEnemyHomeBase
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.enemyHB"), null));
		gwindowdialogclient.addControl(wZutiDrop_OverEnemyHomeBase = new GWindowCheckBox(gwindowdialogclient, 27.0F, 4.0F, null)
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
		//wZutiDrop_OverNeutralHomeBase
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.neutralHB"), null));
		gwindowdialogclient.addControl(wZutiDrop_OverNeutralHomeBase = new GWindowCheckBox(gwindowdialogclient, 27.0F, 6.0F, null)
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
		//wZutiDrop_OverFriendlyFrictionArea
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.friendlyFA"), null));
		gwindowdialogclient.addControl(wZutiDrop_OverFriendlyFrictionArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 8.0F, null)
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
		//wZutiDrop_OverEnemyFrictionArea
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 10.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.enemyFA"), null));
		gwindowdialogclient.addControl(wZutiDrop_OverEnemyFrictionArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 10.0F, null)
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
		//wZutiDrop_OverDestroyGroundArea
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 12.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.destroyGA"), null));
		gwindowdialogclient.addControl(wZutiDrop_OverDestroyGroundArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 12.0F, null)
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
		//wZutiDrop_OverDefenceGroundArea
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.defenceGA"), null));
		gwindowdialogclient.addControl(wZutiDrop_OverDefenceGroundArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 14.0F, null)
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
		
		gwindowdialogclient.addLabel(lSeparate_DropHeight = new GWindowLabel(gwindowdialogclient, 3.0F, 17.5F, 10.0F, 1.6F, Plugin.i18n("mds.airdrops.altitude"), null));
		bSeparate_DropHeight = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 18.0F, 28.0F, 5.0F);
		bSeparate_DropHeight.exclude = lSeparate_DropHeight;
		
		//wZutiDrop_MinHeight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 19.0F, 18.0F, 1.3F, Plugin.i18n("mds.airdrops.min"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 27.0F, 19.0F, 4.0F, 1.3F, Plugin.i18n("mds.meter"), null));
		gwindowdialogclient.addControl(wZutiDrop_MinHeight = new GWindowEditControl(gwindowdialogclient, 21.5F, 19.0F, 5.0F, 1.3F, "")
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
		//wZutiDrop_MaxHeight
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 21.0F, 18.0F, 1.3F, Plugin.i18n("mds.airdrops.max"), null));
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 27.0F, 21.0F, 4.0F, 1.3F, Plugin.i18n("mds.meter"), null));
		gwindowdialogclient.addControl(wZutiDrop_MaxHeight = new GWindowEditControl(gwindowdialogclient, 21.5F, 21.0F, 5.0F, 1.3F, "")
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
		zutiDrop_OverFriendlyHomeBase = false; 
		zutiDrop_OverEnemyHomeBase = false; 
		zutiDrop_OverNeutralHomeBase = false; 
		zutiDrop_OverFriendlyFrictionArea = false; 
		zutiDrop_OverEnemyFrictionArea = true; 
		zutiDrop_OverDestroyGroundArea = true; 
		zutiDrop_OverDefenceGroundArea = true;
		zutiDrop_MinHeight = 0;
		zutiDrop_MaxHeight = 10000;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiDrop_OverFriendlyHomeBase.setChecked(zutiDrop_OverFriendlyHomeBase, false);
		wZutiDrop_OverEnemyHomeBase.setChecked(zutiDrop_OverEnemyHomeBase, false);
		wZutiDrop_OverNeutralHomeBase.setChecked(zutiDrop_OverNeutralHomeBase, false);
		wZutiDrop_OverFriendlyFrictionArea.setChecked(zutiDrop_OverFriendlyFrictionArea, false);
		wZutiDrop_OverEnemyFrictionArea.setChecked(zutiDrop_OverEnemyFrictionArea, false);
		wZutiDrop_OverDestroyGroundArea.setChecked(zutiDrop_OverDestroyGroundArea, false);
		wZutiDrop_OverDefenceGroundArea.setChecked(zutiDrop_OverDefenceGroundArea, false);
		wZutiDrop_MinHeight.setValue(new Integer(zutiDrop_MinHeight).toString(), false);
		wZutiDrop_MaxHeight.setValue(new Integer(zutiDrop_MaxHeight).toString(), false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiDrop_OverFriendlyHomeBase = wZutiDrop_OverFriendlyHomeBase.isChecked();
		zutiDrop_OverEnemyHomeBase = wZutiDrop_OverEnemyHomeBase.isChecked();
		zutiDrop_OverNeutralHomeBase = wZutiDrop_OverNeutralHomeBase.isChecked();
		zutiDrop_OverFriendlyFrictionArea = wZutiDrop_OverFriendlyFrictionArea.isChecked();
		zutiDrop_OverEnemyFrictionArea = wZutiDrop_OverEnemyFrictionArea.isChecked();
		zutiDrop_OverDestroyGroundArea = wZutiDrop_OverDestroyGroundArea.isChecked();
		zutiDrop_OverDefenceGroundArea = wZutiDrop_OverDefenceGroundArea.isChecked();
		zutiDrop_MinHeight = Integer.parseInt(wZutiDrop_MinHeight.getValue());
		zutiDrop_MaxHeight = Integer.parseInt(wZutiDrop_MaxHeight.getValue());
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiDrop_OverFriendlyHomeBase = false;
			if( sectfile.get(SECTION_ID, "ZutiDrop_OverFriendlyHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverFriendlyHomeBase = true;
			zutiDrop_OverEnemyHomeBase = false;
			if( sectfile.get(SECTION_ID, "ZutiDrop_OverEnemyHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverEnemyHomeBase = true;
			zutiDrop_OverNeutralHomeBase = false;
			if( sectfile.get(SECTION_ID, "ZutiDrop_OverNeutralHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverNeutralHomeBase = true;
			zutiDrop_OverFriendlyFrictionArea = false;
			if( sectfile.get(SECTION_ID, "ZutiDrop_OverFriendlyFrictionArea", 0, 0, 1) == 1 )
				zutiDrop_OverFriendlyFrictionArea = true;
			zutiDrop_OverEnemyFrictionArea = true;
			if( sectfile.get(SECTION_ID, "ZutiDrop_OverEnemyFrictionArea", 1, 0, 1) == 0 )
				zutiDrop_OverEnemyFrictionArea = false;
			zutiDrop_OverDestroyGroundArea = true;
			if( sectfile.get(SECTION_ID, "ZutiDrop_OverDestroyGroundArea", 1, 0, 1) == 0 )
				zutiDrop_OverDestroyGroundArea = false;
			zutiDrop_OverDefenceGroundArea = true;
			if( sectfile.get(SECTION_ID, "ZutiDrop_OverDefenceGroundArea", 1, 0, 1) == 0 )
				zutiDrop_OverDefenceGroundArea = false;
			zutiDrop_MinHeight = sectfile.get(SECTION_ID, "ZutiDrop_MinHeight", 0, 0, 99999);
			zutiDrop_MaxHeight = sectfile.get(SECTION_ID, "ZutiDrop_MaxHeight", 10000, 0, 99999);
			
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
			zutiDrop_OverFriendlyHomeBase = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiDrop_OverFriendlyHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverFriendlyHomeBase = true;
			zutiDrop_OverEnemyHomeBase = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiDrop_OverEnemyHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverEnemyHomeBase = true;
			zutiDrop_OverNeutralHomeBase = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiDrop_OverNeutralHomeBase", 0, 0, 1) == 1 )
				zutiDrop_OverNeutralHomeBase = true;
			zutiDrop_OverFriendlyFrictionArea = false;
			if( sectfile.get(OLD_MDS_ID, "ZutiDrop_OverFriendlyFrictionArea", 0, 0, 1) == 1 )
				zutiDrop_OverFriendlyFrictionArea = true;
			zutiDrop_OverEnemyFrictionArea = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiDrop_OverEnemyFrictionArea", 1, 0, 1) == 0 )
				zutiDrop_OverEnemyFrictionArea = false;
			zutiDrop_OverDestroyGroundArea = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiDrop_OverDestroyGroundArea", 1, 0, 1) == 0 )
				zutiDrop_OverDestroyGroundArea = false;
			zutiDrop_OverDefenceGroundArea = true;
			if( sectfile.get(OLD_MDS_ID, "ZutiDrop_OverDefenceGroundArea", 1, 0, 1) == 0 )
				zutiDrop_OverDefenceGroundArea = false;
			zutiDrop_MinHeight = sectfile.get(OLD_MDS_ID, "ZutiDrop_MinHeight", 0, 0, 99999);
			zutiDrop_MaxHeight = sectfile.get(OLD_MDS_ID, "ZutiDrop_MaxHeight", 10000, 0, 99999);
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
			
			sectfile.lineAdd(sectionIndex, "ZutiDrop_OverFriendlyHomeBase", ZutiSupportMethods.boolToInt(zutiDrop_OverFriendlyHomeBase));
			sectfile.lineAdd(sectionIndex, "ZutiDrop_OverEnemyHomeBase", ZutiSupportMethods.boolToInt(zutiDrop_OverEnemyHomeBase));
			sectfile.lineAdd(sectionIndex, "ZutiDrop_OverNeutralHomeBase", ZutiSupportMethods.boolToInt(zutiDrop_OverNeutralHomeBase));
			sectfile.lineAdd(sectionIndex, "ZutiDrop_OverFriendlyFrictionArea", ZutiSupportMethods.boolToInt(zutiDrop_OverFriendlyFrictionArea));
			sectfile.lineAdd(sectionIndex, "ZutiDrop_OverEnemyFrictionArea", ZutiSupportMethods.boolToInt(zutiDrop_OverEnemyFrictionArea));
			sectfile.lineAdd(sectionIndex, "ZutiDrop_OverDestroyGroundArea", ZutiSupportMethods.boolToInt(zutiDrop_OverDestroyGroundArea));
			sectfile.lineAdd(sectionIndex, "ZutiDrop_OverDefenceGroundArea", ZutiSupportMethods.boolToInt(zutiDrop_OverDefenceGroundArea));
			sectfile.lineAdd(sectionIndex, "ZutiDrop_MinHeight", new Integer(zutiDrop_MinHeight).toString());
			sectfile.lineAdd(sectionIndex, "ZutiDrop_MaxHeight", new Integer(zutiDrop_MaxHeight).toString());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}