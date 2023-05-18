/*4.09m compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowTable;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.il2.builder.PlMapLoad.Land;

public class Zuti_WMapsList extends GWindowFramed
{
	private Table lstMaps;
	private GWindowButton bLoadMap;
	private PlMapLoad plMapLoad;
	
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
	
	public Zuti_WMapsList()
	{
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 25.0F, 40.0F, true);
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
		
		if(lstMaps != null)
			lstMaps.resolutionChanged();
	}
	
	public void windowHidden()
	{
		super.windowHidden();
	}
	
	public void created()
	{
		bAlwaysOnTop = true;
		super.created();
		title = Plugin.i18n("mds.loadMaps.windowTitle");
		clientWindow = create(new GWindowDialogClient()
		{
			public void resized()
			{
				super.resized();
				Zuti_WMapsList.this.setSizes(this);
			}
		});
		com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient)clientWindow;
		
		lstMaps  = new Table(gwindowdialogclient, Plugin.i18n("mds.loadMaps.listTitle"), 1.0F, 3.0F, 15.0F, 20.0F);
		
		gwindowdialogclient.addControl(bLoadMap = new GWindowButton(gwindowdialogclient, 17.0F, 8.0F, 5.0F, 2.0F, (Plugin.i18n("mds.loadMaps.loadMap")), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				loadMap();
				return true;
			}
		});
	}
	
	private void setSizes(GWindow gwindow)
	{
		float win_X = gwindow.win.dx;
		float win_Y = gwindow.win.dy;
		
		lstMaps.setPosSize(5, 5, win_X - 10, win_Y - 40);
		bLoadMap.setPosSize(5, win_Y - 25, win_X - 10, 20);
	}
	
	//----------------------------------------------------------------------
	private void loadMap()
	{		
		if( this.plMapLoad != null )
		{
			this.plMapLoad.zutiExecute( lstMaps.selectRow );
		}
	}
	
	/**
	 * Close window.
	 */
	public void close(boolean flag)
	{
		super.close(flag);
	}
	
	/**
	 * Set window title
	 * @param newTitle
	 */
	public void setTitle(String newTitle)
	{
		title = Plugin.i18n("mds.loadMaps.windowTitle") + " - " + newTitle;
	}
	
	/**
	 * Clear maps list.
	 */
	public void clearMaps()
	{
		if( lstMaps.lst != null )
			lstMaps.lst.clear();
	}
	
	/**
	 * Load all know maps into a list.
	 * @param lands
	 */
	public void loadMaps(ArrayList lands)
	{
		int size = lands.size();
		for (int i = 0; i < size; i++)
		{
			Land land = (Land)lands.get(i);
			this.lstMaps.lst.add(land.i18nName);
		}
	}
	
	/**
	 * Set map loading plugin.
	 * @param mapLoad
	 */
	public void setPlMapLoad(PlMapLoad mapLoad)
	{
		this.plMapLoad = mapLoad;
	}
}