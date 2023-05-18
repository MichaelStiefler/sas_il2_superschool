package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Craters extends GWindowFramed
{
	public static final String SECTION_ID = "MDSSection_Craters";
	
	private int zutiCraters_Bombs250_Visibility;
	private int zutiCraters_Bombs500_Visibility;
	private int zutiCraters_Bombs1000_Visibility;
	private int zutiCraters_Bombs2000_Visibility;
	private int zutiCraters_Bombs5000_Visibility;
	private int zutiCraters_Bombs9999_Visibility;
	
	private boolean zutiCraters_Bombs250_SyncOnline;
	private boolean zutiCraters_Bombs500_SyncOnline;
	private boolean zutiCraters_Bombs1000_SyncOnline;
	private boolean zutiCraters_Bombs2000_SyncOnline;
	private boolean zutiCraters_Bombs5000_SyncOnline;
	private boolean zutiCraters_Bombs9999_SyncOnline;
	private boolean zutiCraters_OnlyAreaHits;
	
	private GWindowEditControl wZutiCraters_Bombs250_Visibility;
	private GWindowEditControl wZutiCraters_Bombs500_Visibility;
	private GWindowEditControl wZutiCraters_Bombs1000_Visibility;
	private GWindowEditControl wZutiCraters_Bombs2000_Visibility;
	private GWindowEditControl wZutiCraters_Bombs5000_Visibility;
	private GWindowEditControl wZutiCraters_Bombs9999_Visibility;
	
	private GWindowCheckBox 	wZutiCraters_Bombs250_SyncOnline;
	private GWindowCheckBox 	wZutiCraters_Bombs500_SyncOnline;
	private GWindowCheckBox 	wZutiCraters_Bombs1000_SyncOnline;
	private GWindowCheckBox 	wZutiCraters_Bombs2000_SyncOnline;
	private GWindowCheckBox 	wZutiCraters_Bombs5000_SyncOnline;
	private GWindowCheckBox 	wZutiCraters_Bombs9999_SyncOnline;
	private GWindowCheckBox 	wZutiCraters_OnlyAreaHits;
	
	private GWindowBoxSeparate 	bSeparate_Craters;
	private GWindowLabel 		lSeparate_Craters;
	
	public ZutiMDSSection_Craters()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 35.0F, 21.0F, true);
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
		title = Plugin.i18n("mds.section.craters");
		clientWindow = create(new GWindowDialogClient());
		
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		gwindowdialogclient.addLabel(lSeparate_Craters = new GWindowLabel(gwindowdialogclient, 3.0F, 2.5F, 13.0F, 1.6F, Plugin.i18n("mds.craters"), null));
		bSeparate_Craters = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 3.0F, 32.0F, 14.0F);
		bSeparate_Craters.exclude = lSeparate_Craters;
		
		//------------------------------------------------------------------------------------------
		//wZutiCraters_OnlyAreaHits
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 1.0F, 30.0F, 1.3F, Plugin.i18n("mds.craters.areasOnly"), null));
		gwindowdialogclient.addControl(wZutiCraters_OnlyAreaHits = new GWindowCheckBox(gwindowdialogclient, 31.0F, 1.0F, null)
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
		//------------------------------------------------------------------------------------------
		//wZutiMisc_Bombs250_Visibility
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 5.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.250"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs250_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 5.0F, 5.0F, 1.3F, "")
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
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 5.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 5.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs250_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 5.0F, null)
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
		//------------------------------------------------------------------------------------------
		//wZutiMisc_Bombs500_Visibility
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 7.0F, 17.0F, 1.3F, Plugin.i18n("mds.craters.500"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs500_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 7.0F, 5.0F, 1.3F, "")
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
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 7.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 7.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs500_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 7.0F, null)
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
		//------------------------------------------------------------------------------------------
		//wZutiMisc_Bombs1000_Visibility
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 9.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.1000"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs1000_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 9.0F, 5.0F, 1.3F, "")
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
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 9.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 9.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs1000_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 9.0F, null)
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
		//------------------------------------------------------------------------------------------
		//wZutiMisc_Bombs2000_Visibility
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 11.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.2000"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs2000_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 11.0F, 5.0F, 1.3F, "")
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
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 11.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 11.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs2000_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 11.0F, null)
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
		//------------------------------------------------------------------------------------------
		//wZutiMisc_Bombs5000_Visibility
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.5000"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs5000_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 13.0F, 5.0F, 1.3F, "")
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
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 13.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 13.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs5000_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 13.0F, null)
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
		//------------------------------------------------------------------------------------------
		//wZutiMisc_Bombs9999_Visibility
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.9999"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs9999_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 15.0F, 5.0F, 1.3F, "")
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
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 15.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
		
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 15.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
		gwindowdialogclient.addControl(wZutiCraters_Bombs9999_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 15.0F, null)
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
		//------------------------------------------------------------------------------------------
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
		
		zutiCraters_OnlyAreaHits = true;
	}
	
	/**
	 * Reads MDS variables and sets their values to UI objects.
	 */
	private void setUIVariables()
	{
		wZutiCraters_Bombs250_Visibility.setValue(new Integer(zutiCraters_Bombs250_Visibility).toString(), false);
		wZutiCraters_Bombs500_Visibility.setValue(new Integer(zutiCraters_Bombs500_Visibility).toString(), false);
		wZutiCraters_Bombs1000_Visibility.setValue(new Integer(zutiCraters_Bombs1000_Visibility).toString(), false);
		wZutiCraters_Bombs2000_Visibility.setValue(new Integer(zutiCraters_Bombs2000_Visibility).toString(), false);
		wZutiCraters_Bombs5000_Visibility.setValue(new Integer(zutiCraters_Bombs5000_Visibility).toString(), false);
		wZutiCraters_Bombs9999_Visibility.setValue(new Integer(zutiCraters_Bombs9999_Visibility).toString(), false);
		
		wZutiCraters_Bombs250_SyncOnline.setChecked(zutiCraters_Bombs250_SyncOnline, false);
		wZutiCraters_Bombs500_SyncOnline.setChecked(zutiCraters_Bombs500_SyncOnline, false);
		wZutiCraters_Bombs1000_SyncOnline.setChecked(zutiCraters_Bombs1000_SyncOnline, false);
		wZutiCraters_Bombs2000_SyncOnline.setChecked(zutiCraters_Bombs2000_SyncOnline, false);
		wZutiCraters_Bombs5000_SyncOnline.setChecked(zutiCraters_Bombs5000_SyncOnline, false);
		wZutiCraters_Bombs9999_SyncOnline.setChecked(zutiCraters_Bombs9999_SyncOnline, false);
		
		wZutiCraters_OnlyAreaHits.setChecked(zutiCraters_OnlyAreaHits, false);
	}
	
	/**
	 * Reads UI objects values and stores them to MDS variables.
	 */
	private void setMDSVariables()
	{
		zutiCraters_Bombs250_Visibility = Integer.parseInt(wZutiCraters_Bombs250_Visibility.getValue());
		zutiCraters_Bombs500_Visibility = Integer.parseInt(wZutiCraters_Bombs500_Visibility.getValue());
		zutiCraters_Bombs1000_Visibility = Integer.parseInt(wZutiCraters_Bombs1000_Visibility.getValue());
		zutiCraters_Bombs2000_Visibility = Integer.parseInt(wZutiCraters_Bombs2000_Visibility.getValue());
		zutiCraters_Bombs5000_Visibility = Integer.parseInt(wZutiCraters_Bombs5000_Visibility.getValue());
		zutiCraters_Bombs9999_Visibility = Integer.parseInt(wZutiCraters_Bombs9999_Visibility.getValue());
	
		zutiCraters_Bombs250_SyncOnline = wZutiCraters_Bombs250_SyncOnline.isChecked();
		zutiCraters_Bombs500_SyncOnline = wZutiCraters_Bombs500_SyncOnline.isChecked();
		zutiCraters_Bombs1000_SyncOnline = wZutiCraters_Bombs1000_SyncOnline.isChecked();
		zutiCraters_Bombs2000_SyncOnline = wZutiCraters_Bombs2000_SyncOnline.isChecked();
		zutiCraters_Bombs5000_SyncOnline = wZutiCraters_Bombs5000_SyncOnline.isChecked();
		zutiCraters_Bombs9999_SyncOnline = wZutiCraters_Bombs9999_SyncOnline.isChecked();
		
		zutiCraters_OnlyAreaHits = wZutiCraters_OnlyAreaHits.isChecked();
		
		PlMission.setChanged();
	}
	
	/**
	 * Reads variables from specified mission file and corresponding MDS section.
	 */
	public void loadVariables(SectFile sectfile)
	{
		try
		{
			zutiCraters_Bombs250_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs250_Visibility", 1, 1, 99999);
			zutiCraters_Bombs500_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs500_Visibility", 1, 1, 99999);
			zutiCraters_Bombs1000_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs1000_Visibility", 1, 1, 99999);
			zutiCraters_Bombs2000_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs2000_Visibility", 1, 1, 99999);
			zutiCraters_Bombs5000_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs5000_Visibility", 1, 1, 99999);
			zutiCraters_Bombs9999_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs9999_Visibility", 1, 1, 99999);
			
			zutiCraters_Bombs250_SyncOnline = false;
			if( sectfile.get(SECTION_ID, "ZutiCraters_Bombs250_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs250_SyncOnline = true;
			zutiCraters_Bombs500_SyncOnline = false;
			if( sectfile.get(SECTION_ID, "ZutiCraters_Bombs500_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs500_SyncOnline = true;
			zutiCraters_Bombs1000_SyncOnline = false;
			if( sectfile.get(SECTION_ID, "ZutiCraters_Bombs1000_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs1000_SyncOnline = true;
			zutiCraters_Bombs2000_SyncOnline = false;
			if( sectfile.get(SECTION_ID, "ZutiCraters_Bombs2000_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs2000_SyncOnline = true;
			zutiCraters_Bombs5000_SyncOnline = false;
			if( sectfile.get(SECTION_ID, "ZutiCraters_Bombs5000_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs5000_SyncOnline = true;
			zutiCraters_Bombs9999_SyncOnline = false;
			if( sectfile.get(SECTION_ID, "ZutiCraters_Bombs9999_SyncOnline", 0, 0, 1) == 1 )
				zutiCraters_Bombs9999_SyncOnline = true;
			
			zutiCraters_OnlyAreaHits = true;
			if( sectfile.get(SECTION_ID, "ZutiCraters_OnlyAreaHits", 1, 0, 1) == 0 )
				zutiCraters_OnlyAreaHits = false;
			
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
			
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs250_Visibility", new Float(zutiCraters_Bombs250_Visibility).toString());
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs500_Visibility", new Float(zutiCraters_Bombs500_Visibility).toString());
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs1000_Visibility", new Float(zutiCraters_Bombs1000_Visibility).toString());
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs2000_Visibility", new Float(zutiCraters_Bombs2000_Visibility).toString());
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs5000_Visibility", new Float(zutiCraters_Bombs5000_Visibility).toString());
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs9999_Visibility", new Float(zutiCraters_Bombs9999_Visibility).toString());
			
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs250_SyncOnline", ZutiSupportMethods.boolToInt(zutiCraters_Bombs250_SyncOnline));
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs500_SyncOnline", ZutiSupportMethods.boolToInt(zutiCraters_Bombs500_SyncOnline));
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs1000_SyncOnline", ZutiSupportMethods.boolToInt(zutiCraters_Bombs1000_SyncOnline));
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs2000_SyncOnline", ZutiSupportMethods.boolToInt(zutiCraters_Bombs2000_SyncOnline));
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs5000_SyncOnline", ZutiSupportMethods.boolToInt(zutiCraters_Bombs5000_SyncOnline));
			sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs9999_SyncOnline", ZutiSupportMethods.boolToInt(zutiCraters_Bombs9999_SyncOnline));
			
			sectfile.lineAdd(sectionIndex, "ZutiCraters_OnlyAreaHits", ZutiSupportMethods.boolToInt(zutiCraters_OnlyAreaHits));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}