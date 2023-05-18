/*4.09m compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;
import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GSize;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowTable;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.il2.builder.ActorBorn;

public class Zuti_WHomeBaseCountries extends GWindowFramed
{
	private Table lstSelected;
	private Table lstAvailable;
	
	private GWindowButton bAdd;
	private GWindowButton bAddAll;
	private GWindowButton bRemove;
	private GWindowButton bRemoveAll;
	
	private ArrayList fullCountriesList = new ArrayList();
	private ActorBorn selectedActorBorn;
	
	//Modes: 0=general country selection; 1=red captured countries; 2=blue captured countries
	private int mode = 0;
	
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
	
	public Zuti_WHomeBaseCountries()
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
		
		if(lstSelected != null)
			lstSelected.resolutionChanged();
		if(lstAvailable != null)
			lstAvailable.resolutionChanged();
	}
	
	public void windowHidden()
	{
		super.windowHidden();
	}
	
	public void created()
	{
		bAlwaysOnTop = true;
		super.created();
		title = Plugin.i18n("mds.zCountries.title");
		clientWindow = create(new GWindowDialogClient()
		{
			public void resized()
			{
				super.resized();
				Zuti_WHomeBaseCountries.this.setSizes(this);
			}
		});
		com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient)clientWindow;
		
		lstSelected  = new Table(gwindowdialogclient, Plugin.i18n("mds.zCountries.selected"), 1.0F, 3.0F, 15.0F, 20.0F);
		lstAvailable = new Table(gwindowdialogclient, Plugin.i18n("mds.zCountries.available"), 23.0F, 3.0F, 15.0F, 20.0F);
		
		gwindowdialogclient.addControl(bAddAll = new GWindowButton(gwindowdialogclient, 17.0F, 3.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_addall")), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				lstSelected.lst.clear();
				addAllCountries();
				lstAvailable.lst.clear();
				
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
				
				int i = lstAvailable.selectRow;
				if (i < 0 || i >= lstAvailable.lst.size())
					return true;
				
				if ( !lstSelected.lst.contains(lstAvailable.lst.get(i)) )
				{
					lstSelected.lst.add(lstAvailable.lst.get(i));
					lstAvailable.lst.remove(i);
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
				
				lstSelected.lst.clear();
				fillAvailableCountries();
				
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
				
				int i = lstSelected.selectRow;
				if (i < 0 || i >= lstSelected.lst.size())
					return true;
				
				if( !lstAvailable.lst.contains(lstSelected.lst.get(i)) )
					lstAvailable.lst.add(lstSelected.lst.get(i));
				lstSelected.lst.remove(i);
				
				PlMission.setChanged();
				return true;
			}
		});
	}
	
	private void setSizes(GWindow gwindow)
	{
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
		bAddAll.setPosSize(f_173_, f_SizeY, f_171_, 2.0F * f_SizeY);
		bAdd.setPosSize(f_173_, f_SizeY + 2.0F * f_SizeY, f_171_, 2.0F * f_SizeY);
		bRemoveAll.setPosSize(f_173_, 2.0F * f_SizeY + 4.0F * f_SizeY, f_171_, 2.0F * f_SizeY);
		bRemove.setPosSize(f_173_, 2.0F * f_SizeY + 6.0F * f_SizeY, f_171_, 2.0F * f_SizeY);
		float f_174_ = (win_X - f_171_ - 4.0F * f_SizeY) / 2.0F;
		float f_175_ = win_Y - f_SizeY - 12.0F;
		lstAvailable.setPosSize(win_X - f_SizeY - f_174_, f_SizeY, f_174_, f_175_);
		lstSelected.setPosSize(f_SizeY, f_SizeY, f_174_, f_175_);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//Called from: PlMisBorn
	//Called from: PlMisBorn
	public void setSelectedCountries(ActorBorn actorBorn)
	{
		lstSelected.lst.clear();
		lstAvailable.lst.clear();
		
		selectedActorBorn = actorBorn;
		
		if( fullCountriesList == null || fullCountriesList.size() < 1 )
			fillCountries();
	
		fillAvailableCountries();
	
		switch( mode )
		{
			case 0:
			{
				if( selectedActorBorn.zutiHomeBaseCountries != null && selectedActorBorn.zutiHomeBaseCountries.size() > 0 )
				{
					//Transform IDs to names
					for( int i=0; i<selectedActorBorn.zutiHomeBaseCountries.size(); i++ )
					{
						try
						{
							String country = (String)selectedActorBorn.zutiHomeBaseCountries.get(i);
							if( lstAvailable.lst.contains( country ) )
								lstSelected.lst.add( country );
						}
						catch(Exception ex){}
					}
				}
				break;
			}
			case 1:
			{
				if( selectedActorBorn.zutiHomeBaseCapturedRedCountries != null && selectedActorBorn.zutiHomeBaseCapturedRedCountries.size() > 0 )
				{
					//Transform IDs to names
					for( int i=0; i<selectedActorBorn.zutiHomeBaseCapturedRedCountries.size(); i++ )
					{
						try
						{
							String country = (String)selectedActorBorn.zutiHomeBaseCapturedRedCountries.get(i);
							if( lstAvailable.lst.contains( country ) )
								lstSelected.lst.add( country );
						}
						catch(Exception ex){}
					}
				}
				break;
			}
			case 2:
			{
				if( selectedActorBorn.zutiHomeBaseCapturedBlueCountries != null && selectedActorBorn.zutiHomeBaseCapturedBlueCountries.size() > 0 )
				{
					//Transform IDs to names
					for( int i=0; i<selectedActorBorn.zutiHomeBaseCapturedBlueCountries.size(); i++ )
					{
						try
						{
							String country = (String)selectedActorBorn.zutiHomeBaseCapturedBlueCountries.get(i);
							if( lstAvailable.lst.contains( country ) )
								lstSelected.lst.add( country );
						}
						catch(Exception ex){}
					}
				}
				break;
			}
		}
		
		syncLists();
	}
	
	private void fillCountries()
	{
		java.util.ResourceBundle resCountry = java.util.ResourceBundle.getBundle("i18n/country", com.maddox.rts.RTSConf.cur.locale, com.maddox.rts.LDRres.loader());
		
		java.util.List list = com.maddox.il2.ai.Regiment.getAll();
        int k1 = list.size();
        for(int i = 0; i < k1; i++)
        {
            com.maddox.il2.ai.Regiment regiment = (com.maddox.il2.ai.Regiment)list.get(i);
			String branch = resCountry.getString(regiment.branch());
			if( !fullCountriesList.contains(branch) )
				fullCountriesList.add(branch);
		}
	}
	
	private void fillAvailableCountries()
	{
        for(int i = 0; i < fullCountriesList.size(); i++)
            lstAvailable.lst.add(fullCountriesList.get(i));
	}
	
	public void close(boolean flag)
	{
		super.close(flag);
		
		if( selectedActorBorn != null )
		{
			//Save selection
			switch(mode)
			{
				case 0:
				{
					if( selectedActorBorn.zutiHomeBaseCountries == null )
						selectedActorBorn.zutiHomeBaseCountries = new ArrayList();
				
					selectedActorBorn.zutiHomeBaseCountries.clear();
					for( int i=0; i<lstSelected.lst.size(); i++ )
						selectedActorBorn.zutiHomeBaseCountries.add( lstSelected.lst.get(i) );
						
					break;
				}
				case 1:
				{
					if( selectedActorBorn.zutiHomeBaseCapturedRedCountries == null )
						selectedActorBorn.zutiHomeBaseCapturedRedCountries = new ArrayList();
				
					selectedActorBorn.zutiHomeBaseCapturedRedCountries.clear();
					for( int i=0; i<lstSelected.lst.size(); i++ )
						selectedActorBorn.zutiHomeBaseCapturedRedCountries.add( lstSelected.lst.get(i) );
						
					break;
				}
				case 2:
				{
					if( selectedActorBorn.zutiHomeBaseCapturedBlueCountries == null )
						selectedActorBorn.zutiHomeBaseCapturedBlueCountries = new ArrayList();
				
					selectedActorBorn.zutiHomeBaseCapturedBlueCountries.clear();
					for( int i=0; i<lstSelected.lst.size(); i++ )
						selectedActorBorn.zutiHomeBaseCapturedBlueCountries.add( lstSelected.lst.get(i) );
						
					break;
				}
			}
		}
	}
	
	private void addAllCountries()
	{
		for (int i = 0; i < lstAvailable.lst.size(); i++)
		{			
			if ( !lstSelected.lst.contains(lstAvailable.lst.get(i)) )
				lstSelected.lst.add(lstAvailable.lst.get(i));
		}
	}
	
	private void syncLists()
	{
		for( int i=0; i<lstSelected.lst.size(); i++ )
			lstAvailable.lst.remove(lstSelected.lst.get(i));
	}
	
	//Called from PlMisBorn
	public void setTitle(String newTitle)
	{
		title = newTitle;
	}
	//Called from: PlMisBorn
	public void clearArrays()
	{
		if( lstAvailable.lst != null )
			lstAvailable.lst.clear();
		if( lstSelected.lst != null )
			lstSelected.lst.clear();
	}
	//Called from: PlMisBorn
	public void setMode(int inMode)
	{
		mode = inMode;
	}
}