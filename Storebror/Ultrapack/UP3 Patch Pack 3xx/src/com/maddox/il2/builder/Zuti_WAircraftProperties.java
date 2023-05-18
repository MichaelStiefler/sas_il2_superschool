/*4.09m compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;
import java.util.List;
import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GSize;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowTable;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.il2.game.ZutiAircraft;

public class Zuti_WAircraftProperties extends GWindowFramed
{
	public static final int MAX_LOADOUTS = 9;
	private Table lstSelectedLoadouts;
	private Table lstAvailableLoadouts;
	
	private GWindowEditControl wMaxAllowed;
	private GWindowComboControl wMaxFuelSelection;
	private GWindowButton bAdd;
	private GWindowButton bAddAll;
	private GWindowButton bRemove;
	private GWindowButton bRemoveAll;
	
	//Called from: PlMisBorn
	private ZutiAircraft zutiAircraft = null;
	
	class Table extends GWindowTable
	{
		public ArrayList lst = new ArrayList();

		public int countRows()
		{
			return lst != null ? lst.size() : 0;
		}

		public Object getValueAt(int i, int i_0_)
		{
			if (lst == null) 
				return null;
			if (i < 0 || i >= lst.size())
				return null;
			String string = (String) lst.get(i);
			return string;
		}

		public void resolutionChanged()
		{
			vSB.scroll = rowHeight(0);
			super.resolutionChanged();
		}

		public Table(GWindow gwindow, String string, float f, float f_1_, float f_2_, float f_3_)
		{
			super(gwindow, f, f_1_, f_2_, f_3_);
			bColumnsSizable = false;
			addColumn(string, null);
			vSB.scroll = rowHeight(0);
			resized();
		}
	}
	
	public Zuti_WAircraftProperties()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 45.0F, 30.0F, true);
		bSizable = true;
	}
	
	public void afterCreated()
	{
		super.afterCreated();
		
		close(false);
	}
	
	public void windowShown()
	{
		super.windowShown();
		
		if(lstSelectedLoadouts != null)
			lstSelectedLoadouts.resolutionChanged();
		if(lstAvailableLoadouts != null)
			lstAvailableLoadouts.resolutionChanged();
	}
	
	public void windowHidden()
	{
		super.windowHidden();
	}
	
	public void created()
	{
		bAlwaysOnTop = true;
		super.created();
		title = Plugin.i18n("mds.zLoadouts.title");
		clientWindow = create(new GWindowDialogClient()
		{
			public void resized()
			{
				super.resized();
				Zuti_WAircraftProperties.this.setSizes(this);
			}
		});
		com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient)clientWindow;
		
		//wMaxAllowed
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7.0F, 1.3F, Plugin.i18n("mds.zLoadouts.max"), null));
		gwindowdialogclient.addControl(wMaxAllowed = new GWindowEditControl(gwindowdialogclient, 10.0F, 1.0F, 5.0F, 1.3F, "")
		{
			public void afterCreated()
			{
				super.afterCreated();
				bNumericOnly = true;
				bDelayedNotify = true;
			}
			
			public void preRender()
			{
				if( getValue().trim().length() > 0 )
					return;
				
				super.preRender();
				setValue(new Integer(zutiAircraft.getNumberOfAIrcraft()).toString(), false);
			}

			public boolean notify(int i_52_, int i_53_)
			{
				if (i_52_ != 2)
					return false;
				
				if( zutiAircraft != null )
				{
					zutiAircraft.setNumberOfAircraft( Integer.parseInt(getValue()) );
					PlMission.setChanged();
				}
				return false;
			}
		});
		
		//wMaxFuelSelection
		gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 26.0F, 1.0F, 12.0F, 1.3F, Plugin.i18n("mds.zFuel.max"), null));
		gwindowdialogclient.addControl(wMaxFuelSelection = new GWindowComboControl(gwindowdialogclient, 38.0F, 1.0F, 5.0F)
		{
			public void afterCreated()
			{
				super.afterCreated();
				setEditable(false);
			}
			
			public void preRender()
			{
				if( getValue().trim().length() > 0 )
					return;
				
				super.preRender();
				setSelected(zutiAircraft.getMaxFuelSelection(), true, false);
			}

			public boolean notify(int i_52_, int i_53_)
			{
				if (i_52_ != 2)
					return false;
				
				if( zutiAircraft != null )
				{
					zutiAircraft.setMaxFuelSelection( this.getSelected() );
					PlMission.setChanged();
				}
				return false;
			}
		});
		wMaxFuelSelection.add("10");
		wMaxFuelSelection.add("20");
		wMaxFuelSelection.add("30");
		wMaxFuelSelection.add("40");
		wMaxFuelSelection.add("50");
		wMaxFuelSelection.add("60");
		wMaxFuelSelection.add("70");
		wMaxFuelSelection.add("80");
		wMaxFuelSelection.add("90");
		wMaxFuelSelection.add("100");
		
		lstSelectedLoadouts  = new Table(gwindowdialogclient, Plugin.i18n("mds.zLoadouts.selected"), 1.0F, 3.0F, 15.0F, 20.0F);
		lstAvailableLoadouts = new Table(gwindowdialogclient, Plugin.i18n("mds.zLoadouts.available"), 23.0F, 3.0F, 15.0F, 20.0F);
		
		gwindowdialogclient.addControl(bAddAll = new GWindowButton(gwindowdialogclient, 17.0F, 3.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_addall")), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				lstSelectedLoadouts.lst.clear();
				addAllWeaponOptions();
				//lstAvailable.lst.clear();
				
				PlMission.setChanged();
				return true;
			}
		});
		gwindowdialogclient.addControl(bAdd = new GWindowButton(gwindowdialogclient, 17.0F, 5.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_add")), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				int i = lstAvailableLoadouts.selectRow;
				if (i < 0 || i >= lstAvailableLoadouts.lst.size())
					return true;
				
				if ( !lstSelectedLoadouts.lst.contains(lstAvailableLoadouts.lst.get(i)) && lstSelectedLoadouts.lst.size() < MAX_LOADOUTS )
				{
					lstSelectedLoadouts.lst.add(lstAvailableLoadouts.lst.get(i));
					lstAvailableLoadouts.lst.remove(i);
				}
				
				PlMission.setChanged();
				return true;
			}
		});
		gwindowdialogclient.addControl(bRemoveAll = new GWindowButton(gwindowdialogclient, 17.0F, 8.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_delall")), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				lstSelectedLoadouts.lst.clear();
				fillAvailableLoadouts();
				
				PlMission.setChanged();
				return true;
			}
		});
		gwindowdialogclient.addControl(bRemove = new GWindowButton(gwindowdialogclient, 17.0F, 10.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_del")), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				int i = lstSelectedLoadouts.selectRow;
				if (i < 0 || i >= lstSelectedLoadouts.lst.size())
					return true;
				
				if( !lstAvailableLoadouts.lst.contains(lstSelectedLoadouts.lst.get(i)) )
					lstAvailableLoadouts.lst.add(lstSelectedLoadouts.lst.get(i));
				lstSelectedLoadouts.lst.remove(i);
				
				PlMission.setChanged();
				return true;
			}
		});
	}
	
	private void setSizes(GWindow gwindow)
	{
		float spaceFromTop = 30.0F;
		float win_X = gwindow.win.dx;
		float win_Y = gwindow.win.dy;
		GFont gfont = gwindow.root.textFonts[0];
		float f_SizeY = gwindow.lAF().metric();
		GSize gsize = new GSize();
		gfont.size(Plugin.i18n("bplace_addall"), gsize);
		float f_addAll = gsize.dx;
		gfont.size(Plugin.i18n("bplace_add"), gsize);
		float f_add = gsize.dx;
		gfont.size(Plugin.i18n("bplace_delall"), gsize);
		float f_delAll = gsize.dx;
		gfont.size(Plugin.i18n("bplace_del"), gsize);
		float f_del = gsize.dx;
		gfont.size(Plugin.i18n("bplace_planes"), gsize);
		float f_list_1 = gsize.dx;
		gfont.size(Plugin.i18n("bplace_list"), gsize);
		float f_list_2 = gsize.dx;
		float f_maxFont = f_addAll;
		if (f_maxFont < f_add)
			f_maxFont = f_add;
		if (f_maxFont < f_delAll)
			f_maxFont = f_delAll;
		if (f_maxFont < f_del)
			f_maxFont = f_del;
		float f_171_ = f_SizeY + f_maxFont;
		f_maxFont += (f_SizeY + 4.0F * f_SizeY + f_list_1 + 4.0F * f_SizeY + f_list_2 + 4.0F * f_SizeY);
		if (win_X < f_maxFont)
			win_X = f_maxFont;
		float f_172_ = 10.0F * f_SizeY + 10.0F * f_SizeY + 2.0F * f_SizeY;
		if (win_Y < f_172_)
			win_Y = f_172_;
		float f_173_ = (win_X - f_171_) / 2.0F;
		bAddAll.setPosSize(f_173_, f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
		bAdd.setPosSize(f_173_, f_SizeY + 2.0F * f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
		bRemoveAll.setPosSize(f_173_, 2.0F * f_SizeY + 4.0F * f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
		bRemove.setPosSize(f_173_, 2.0F * f_SizeY + 6.0F * f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
		float f_174_ = (win_X - f_171_ - 4.0F * f_SizeY) / 2.0F;
		float f_175_ = win_Y - f_SizeY - spaceFromTop - 12.0F;
		lstAvailableLoadouts.setPosSize(win_X - f_SizeY - f_174_, f_SizeY + spaceFromTop, f_174_, f_175_);
		lstSelectedLoadouts.setPosSize(f_SizeY, f_SizeY + spaceFromTop, f_174_, f_175_);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//Called from: PlMisBorn
	public void setAircraft(ZutiAircraft zac)
	{
		if( zac == null )
			return;
		
		System.out.println("EDITING ZAC NAME=" + zac.getAcName() + ", NR=" + zac.getNumberOfAIrcraft() + ", LDT=" + zac.getSelectedWeaponI18NNames());
		
		lstAvailableLoadouts.lst.clear();
		lstSelectedLoadouts.lst.clear();
		
		zutiAircraft = zac;
	
		assignSelectedWeaponsToTable(zutiAircraft.getSelectedWeaponI18NNames());
		wMaxAllowed.setValue(new Integer(zutiAircraft.getNumberOfAIrcraft()).toString());
		wMaxFuelSelection.setSelected(zutiAircraft.getMaxFuelSelection(), true, false);
		
		fillAvailableLoadouts();
		syncLists();
	}
	
	private void fillAvailableLoadouts()
	{
		lstAvailableLoadouts.lst.clear();
		ArrayList list = zutiAircraft.getWeaponI18NNames();
		if( list != null )
		{
			for( int i=0; i<list.size(); i++ )
				lstAvailableLoadouts.lst.add( list.get(i) );
		}
	}
	
	private void assignSelectedWeaponsToTable(List list)
	{
		lstSelectedLoadouts.lst.clear();
		
		if( list == null )
			return;
		
		for(int i=0; i<list.size(); i++)
		{
			lstSelectedLoadouts.lst.add(list.get(i));
		}
	}
	
	public void close(boolean flag)
	{
		super.close(flag);
		
		if( zutiAircraft != null )
		{
			zutiAircraft.setSelectedWeapons(lstSelectedLoadouts.lst);
			zutiAircraft.setNumberOfAircraft(new Integer(wMaxAllowed.getValue()).intValue());
		}
	}
	private void addAllWeaponOptions()
	{
		List remove = new ArrayList();
		for (int i = 0; i < lstAvailableLoadouts.lst.size(); i++)
		{			
			if ( !lstSelectedLoadouts.lst.contains(lstAvailableLoadouts.lst.get(i)) )
			{
				lstSelectedLoadouts.lst.add(lstAvailableLoadouts.lst.get(i));
				remove.add(lstAvailableLoadouts.lst.get(i));
			}
			
			if( lstSelectedLoadouts.lst.size() >= MAX_LOADOUTS )
				break;
		}
		
		for( int i=0; i<remove.size(); i++ )
		{
			lstAvailableLoadouts.lst.remove(remove.get(i));
		}
	}
	private void syncLists()
	{
		for( int i=0; i<lstSelectedLoadouts.lst.size(); i++ )
			lstAvailableLoadouts.lst.remove(lstSelectedLoadouts.lst.get(i));
	}
	//Called from PlMisBorn
	public void setTitle(String newTitle)
	{
		title = Plugin.i18n("mds.zLoadouts.title") + " " + Plugin.i18n("mds.for") + " " + newTitle;
	}
}