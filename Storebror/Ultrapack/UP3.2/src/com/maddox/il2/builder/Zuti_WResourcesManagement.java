/*4.09m compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.maddox.JGP.Point3d;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowCellEdit;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.game.I18N;

public class Zuti_WResourcesManagement extends GWindowFramed
{
	private static float windowXSize = 70F;
	private static float windowYSize = 30F;
	
	public Map objectsMap = null;
	private ActorBorn actorBorn = null;
	public Table wTable;
	private int army;
	
	public class Table extends GWindowTable
	{
		public ArrayList items = new ArrayList();
		
		public int countRows()
		{
			try
			{
				if (items == null)
					return 0;
				
				return items.size();				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				return -1;
			}
		}
		
		public void renderCell(int rowId, int columnId, boolean bool, float x, float y)
		{
			try
			{
				setCanvasFont(0);
				if (bool)
				{
					setCanvasColorBLACK();
					draw(0.0F, 0.0F, x, y, lookAndFeel().regionWhite);
				}
				if (bool)
					setCanvasColorWHITE();
				else
					setCanvasColorBLACK();
				RRRItem item = null;
				if (rowId < items.size())
					item = (RRRItem)items.get(rowId);
				String string = null;
				int alignment = 1;
				switch (columnId)
				{
					case 0 :
						if (item != null)
						{
							alignment = 2;
							string = item.name;
						}
						break;
					case 1 :
						if (item != null)
						{
							string = new Long(item.count).toString();
							switch( Zuti_WResourcesManagement.this.army )
							{
								case 1:
									setCanvasColor(GColor.Red);
									break;
								case 2:
									setCanvasColor(GColor.Blue);
									break;
								default:
									setCanvasColor(GColor.Black);
									break;
							}
						}
						break;
					case 2 :
						if (item != null)
						{
							string = new Long(item.bullets).toString();
						}
						break;
					case 3 :
						if (item != null)
						{
							string = new Long(item.rockets).toString();
						}
						break;
					case 4 :
						if (item != null)
						{
							string = new Long(item.bomb250).toString();
						}
						break;
					case 5 :
						if (item != null)
						{
							string = new Long(item.bomb500).toString();
						}
						break;
					case 6 :
						if (item != null)
						{
							string = new Long(item.bomb1000).toString();
						}
						break;
					case 7 :
						if (item != null)
						{
							string = new Long(item.bomb2000).toString();
						}
						break;
					case 8 :
						if (item != null)
						{
							string = new Long(item.bomb5000).toString();
						}
						break;
					case 9 :
						if (item != null)
						{
							string = new Long(item.bomb9999).toString();
						}
						break;
					case 10 :
						if (item != null)
						{
							string = new Long(item.fuel).toString();
						}
						break;
					case 11 :
						if (item != null)
						{
							string = new Long(item.engines).toString();
						}
						break;
					case 12 :
						if (item != null)
						{
							string = new Long(item.repairKits).toString();
						}
						break;
				}
				
				if( rowId == this.items.size()-1 )
				{
					switch( Zuti_WResourcesManagement.this.army )
					{
						case 1:
							setCanvasColor(GColor.Red);
							break;
						case 2:
							setCanvasColor(GColor.Blue);
							break;
						default:
							setCanvasColor(GColor.Black);
							break;
					}
				}
				
				if (string != null)
					draw(0.0F, 0.0F, x, y, alignment, string);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		public boolean isCellEditable(int rowId, int columnId)
        {
			if( columnId > 1 && rowId < this.items.size()-1 )
				return true;
			
			return false;
        }

        public GWindowCellEdit getCellEdit(int rowId, int columnId)
        {
            GWindowCellEdit gwindowcelledit = super.getCellEdit(rowId, columnId);
            if(gwindowcelledit != null && (gwindowcelledit instanceof GWindowEditControl))
            {
                GWindowEditControl gwindoweditcontrol = (GWindowEditControl)gwindowcelledit;
                gwindoweditcontrol.bNumericOnly = true;
                gwindoweditcontrol.maxLength = 5;
            }
            return gwindowcelledit;
        }
        
        public Object getValueAt(int rowId, int columnId)
        {
            RRRItem item = (RRRItem)this.items.get(rowId);
            
            if( item == null )
            	return null;
            
            switch(columnId)
            {
	            case 0:
	                return item.name;
	            case 1:
	                return new Long(item.count);
	            case 2:
	                return new Long(item.bullets);
	            case 3:
	                return new Long(item.rockets);
	            case 4:
	                return new Long(item.bomb250);
	            case 5:
	                return new Long(item.bomb500);
	            case 6:
	                return new Long(item.bomb1000);
	            case 7:
	                return new Long(item.bomb2000);
	            case 8:
	                return new Long(item.bomb5000);
	            case 9:
	                return new Long(item.bomb9999);
	            case 10:
	                return new Long(item.fuel);
	            case 11:
	                return new Long(item.engines);
	            case 12:
	                return new Long(item.repairKits);
            }
            return null;
        }

        public void setValueAt(Object obj, int rowId, int columnId)
        {
            if(rowId < 0 || columnId < 0)
                return;
            if(rowId >= this.items.size())
                return;
            RRRItem item = (RRRItem)this.items.get(rowId);
                        
            long lValue = 0;
            if( columnId > 0)
            {
            	 if( Zuti_WResourcesManagement.this.objectsMap == null )
            		 Zuti_WResourcesManagement.this.objectsMap = new HashMap();
            	 
            	String value = (String)obj;
            	try{lValue = Long.parseLong(value);}
            	catch(Exception ex){}
            }

            if( ZutiSupportMethods_Builder.changeBombCargoParameters(item, columnId, lValue) )
            {
            	//We changed bomb cargo values, just update sum line and exit
            	insertSumLine();
            	return;
            }
            
            switch(columnId)
            {
	            case 0:
	                item.name = (String)obj;
	                break;
	            case 1:
	            {
	                item.count = lValue;
	                break;
	            }
	            case 2:
	            {
	                item.bullets = lValue;
	                Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_bullets", new Long(item.bullets));
	                break;
	            }
	            case 3:
	            {
	            	item.rockets = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_rockets", new Long(item.rockets));
	                break;
	            }
	            case 4:
	            {
	            	item.bomb250 = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_bomb250", new Long(item.bomb250));
	                break;
	            }
	            case 5:
	            {
	            	item.bomb500 = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_bomb500", new Long(item.bomb500));
	                break;
	            }
	            case 6:
	            {
	            	item.bomb1000 = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_bomb1000", new Long(item.bomb1000));
	                break;
	            }
	            case 7:
	            {
	            	item.bomb2000 = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_bomb2000", new Long(item.bomb2000));
	                break;
	            }
	            case 8:
	            {
	            	item.bomb5000 = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_bomb5000", new Long(item.bomb5000));
	                break;
	            }
	            case 9:
	            {
	            	item.bomb9999 = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_bomb9999", new Long(item.bomb9999));
	                break;
	            }
	            case 10:
	            {
	            	item.fuel = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_fuel", new Long(item.fuel));
	                break;
	            }
	            case 11:
	            {
	            	item.engines = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_engines", new Long(item.engines));
	                break;
	            }
	            case 12:
	            {
	            	item.repairKits = lValue;
	            	Zuti_WResourcesManagement.this.objectsMap.put(item.name + "_repairKits", new Long(item.repairKits));
	                break;
	            }
            }
            
            insertSumLine();
        }
		
		public void setSelect(int rowId, int columnId)
		{
			try
			{
				if (items != null && rowId >= 0 && rowId < items.size())
				{
					//Item item = (Item)items.get(rowId);
					//System.out.println("Selected item id = " + rowId + ", columnt1Value=");
				}
				super.setSelect(rowId, columnId);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}			
		}
		
		public void afterCreated()
		{
			try
			{
				super.afterCreated();
				bColumnsSizable = true;
				bSelecting = true;
				bSelectRow = true;
				addColumn(I18N.gui("mds.RRR.object"), null);
				addColumn(I18N.gui("mds.RRR.count"), null);
				addColumn(I18N.gui("mds.RRR.bullets"), null);
				addColumn(I18N.gui("mds.RRR.rockets"), null);
				addColumn(I18N.gui("mds.RRR.bomb250"), null);
				addColumn(I18N.gui("mds.RRR.bomb500"), null);
				addColumn(I18N.gui("mds.RRR.bomb1000"), null);
				addColumn(I18N.gui("mds.RRR.bomb2000"), null);
				addColumn(I18N.gui("mds.RRR.bomb5000"), null);
				addColumn(I18N.gui("mds.RRR.bomb9999"), null);
				addColumn(I18N.gui("mds.RRR.fuel"), null);
				addColumn(I18N.gui("mds.RRR.engine"), null);
				addColumn(I18N.gui("mds.RRR.repairKit"), null);
				vSB.scroll = rowHeight(0);
				getColumn(0).setRelativeDx(4.0F);
				getColumn(1).setRelativeDx(1.0F);
				getColumn(2).setRelativeDx(2.0F);
				getColumn(3).setRelativeDx(2.0F);
				getColumn(4).setRelativeDx(2.0F);
				getColumn(5).setRelativeDx(2.0F);
				getColumn(6).setRelativeDx(2.0F);
				getColumn(7).setRelativeDx(2.0F);
				getColumn(8).setRelativeDx(2.0F);
				getColumn(9).setRelativeDx(2.0F);
				getColumn(10).setRelativeDx(2.0F);
				getColumn(11).setRelativeDx(2.0F);
				getColumn(12).setRelativeDx(2.0F);
				alignColumns();
				wClient.bNotify = true;
				resized();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}			
		}
		
		public void resolutionChanged()
		{
			try
			{
				vSB.scroll = rowHeight(0);
				super.resolutionChanged();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		public Table(GWindow gwindow, float x, float y, float xSize, float ySize)
		{
			super(gwindow, x, y, xSize, ySize);
		}
	}
	
	public static class RRRItem
	{
		public String name;
		public long count;
		public long bullets;
		public long rockets;
		public long bomb250;
		public long bomb500;
		public long bomb1000;
		public long bomb2000;
		public long bomb5000;
		public long bomb9999;
		public long fuel;
		public long engines;
		public long repairKits;
		
		public String getFileLine()
		{
			StringBuffer sb = new StringBuffer();
			
			sb.append(name);sb.append(" ");
			sb.append(count);sb.append(" ");
			sb.append(bullets);sb.append(" ");
			sb.append(rockets);sb.append(" ");
			sb.append(bomb250);sb.append(" ");
			sb.append(bomb500);sb.append(" ");
			sb.append(bomb1000);sb.append(" ");
			sb.append(bomb2000);sb.append(" ");
			sb.append(bomb5000);sb.append(" ");
			sb.append(bomb9999);sb.append(" ");
			sb.append(fuel);sb.append(" ");
			sb.append(engines);sb.append(" ");
			sb.append(repairKits);
			
			return sb.toString();
		}
	}
	
	public Zuti_WResourcesManagement(int army)
	{
		this.army = army;
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, windowXSize, windowYSize, true);
		bSizable = false;
	}
	
	public void afterCreated()
	{
		super.afterCreated();
		
		close(false);
	}
	
	public void windowShown()
	{
		super.windowShown();
	}
	
	public void windowHidden()
	{
		super.windowHidden();
	}
	
	public void created()
	{
		bAlwaysOnTop = true;
		super.created();
		clientWindow = create(new GWindowDialogClient()
		{
			public void resized()
			{
				super.resized();
			}
		});
		GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
		wTable = new Table(gwindowdialogclient, 1F, 1F, windowXSize-2, windowYSize-4);
	}
	
	public void close(boolean flag)
	{
		super.close(flag);
	}
	
	//Called from PlMisBorn
	public void setTitle(String newTitle)
	{
		title = newTitle;
	}
	
	public void countRRRObjects()
	{
		wTable.items.clear();
		
		Map objects = null;
		if( actorBorn == null )
		{
			//For side resources
			objects = mergeMaps
			(
				ZutiSupportMethods_Builder.listRRRObjects(objectsMap, this.army),
				ZutiSupportMethods_Builder.listRRRMovingObjects(objectsMap, this.army)		
			);
		}
		else
		{
			//For home base resources
			Point3d point = actorBorn.pos.getAbsPoint();
			objects = mergeMaps
			(
				ZutiSupportMethods_Builder.listRRRObjects_ActorBorn(objectsMap, point.x, point.y, actorBorn.r, this.army),
				ZutiSupportMethods_Builder.listAircraftCarriers_ActorBorn(objectsMap, point.x, point.y, actorBorn.r)		
			);
			if( objects.size() > 0 )
				actorBorn.zutiEnableResourcesManagement = true;
		}
		//System.out.println("Army: " + this.army + ", objects: " + objects.size());
		Iterator iterator = objects.keySet().iterator();
		while( iterator.hasNext() )
		{
			RRRItem rrrItem = (RRRItem)objects.get((String)iterator.next());
			if( rrrItem != null )
			{
				wTable.items.add(rrrItem);
			}
		}
		
		wTable.items.add(null);
		wTable.items.add(null);
		
		insertSumLine();
	}
	
	private void insertSumLine()
	{
		RRRItem sumsItem = new RRRItem();
		sumsItem.name = I18N.gui("mds.RRR.sum");
		
		for( int i=0; i<wTable.items.size(); i++ )
		{
			RRRItem rrrItem = (RRRItem)wTable.items.get(i);
			if( rrrItem == null || rrrItem.name.equals(I18N.gui("mds.RRR.sum")) )
				continue;
			
			//Calculate sums
			sumsItem.count += rrrItem.count;
			sumsItem.bullets += rrrItem.bullets * rrrItem.count;
			sumsItem.rockets += rrrItem.rockets * rrrItem.count;
			sumsItem.bomb250 += rrrItem.bomb250 * rrrItem.count;
			sumsItem.bomb500 += rrrItem.bomb500 * rrrItem.count;
			sumsItem.bomb1000 += rrrItem.bomb1000 * rrrItem.count;
			sumsItem.bomb2000 += rrrItem.bomb2000 * rrrItem.count;
			sumsItem.bomb5000 += rrrItem.bomb5000 * rrrItem.count;
			sumsItem.bomb9999 += rrrItem.bomb9999 * rrrItem.count;
			sumsItem.fuel += rrrItem.fuel * rrrItem.count;
			sumsItem.engines += rrrItem.engines * rrrItem.count;
			sumsItem.repairKits += rrrItem.repairKits * rrrItem.count;
		}
		
		wTable.items.remove(wTable.items.size()-1);
		wTable.items.add(sumsItem);
	}
	
	public void setActorBorn(ActorBorn actorBorn)
	{
		if( actorBorn == null )
		{
			System.out.println("ZWRMngmt - ab can not be null!");
			return;
		}
		
		this.actorBorn = actorBorn;
		this.army = actorBorn.getArmy();
	}
	
	private Map mergeMaps(Map map1, Map map2)
	{
		Map mergedMap = new HashMap();
		
		String key = null;
		RRRItem value = null;

		Iterator iterator = null;
		
		if( map1 != null )
		{
			iterator = map1.keySet().iterator();
			while( iterator.hasNext() )
			{
				key = (String)iterator.next();
				value = (RRRItem)map1.get(key);
				
				if( !mergedMap.containsKey(key) )
				{
					mergedMap.put(key, value);
				}
				else
				{
					value.count++;
				}
			}
		}
		
		if( map2 != null )
		{
			iterator = map2.keySet().iterator();
			while( iterator.hasNext() )
			{
				key = (String)iterator.next();
				
				System.out.println("Key = " + key);
				
				value = (RRRItem)map2.get(key);
				
				if( !mergedMap.containsKey(key) )
				{
					mergedMap.put(key, value);
				}
				else
				{
					value.count++;
				}
			}
		}
		
		return mergedMap;
	}
}