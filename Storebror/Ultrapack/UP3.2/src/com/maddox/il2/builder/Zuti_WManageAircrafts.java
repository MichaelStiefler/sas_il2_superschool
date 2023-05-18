/*4.09 compatible*/
package com.maddox.il2.builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.lang.StringBuffer;

import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GSize;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.game.Main;
import com.maddox.rts.LDRres;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.il2.builder.Zuti_WAircraftProperties;
import com.maddox.il2.game.ZutiAircraft;
import com.maddox.il2.objects.air.Scheme1;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeSailPlane;
import com.maddox.il2.objects.air.TypeScout;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.gwindow.GWindowFramed;

public class Zuti_WManageAircrafts extends GWindowFramed
{
	private Table lstAvailable;
	Table lstInReserve;
	GWindowButton bAddAll;
	GWindowButton bAdd;
	GWindowButton bRemAll;
	GWindowButton bRem;
	GWindowButton bModifyPlane;
	GWindowLabel lSeparate;
	GWindowBoxSeparate bSeparate;
	GWindowLabel lCountry;
	GWindowComboControl cCountry;
	static ArrayList lstCountry = new ArrayList();
	GWindowButton bCountryAdd;
	GWindowButton bCountryRem;
	GWindowLabel lYear;
	GWindowComboControl cYear;
	static ArrayList lstYear = new ArrayList();
	GWindowButton bYearAdd;
	GWindowButton bYearRem;
	GWindowLabel lType;
	GWindowComboControl cType;
	static ArrayList lstType = new ArrayList();
	GWindowButton bTypeAdd;
	GWindowButton bTypeRem;
	//Inserted ArrayList containing aircrafts
	private ArrayList airNames = null;
	private GWindowEditControl parentEditControl = null;
	private boolean enableAcModifications = false;
	private boolean offerOnlyFlyableAircraft = false;
	
	class Table extends GWindowTable
	{
		public ArrayList lst = new ArrayList();

		public int countRows()
		{
			return lst != null ? lst.size() : 0;
		}

		public Object getValueAt(int i, int i_0_)
		{
			if (lst == null) return null;
			if (i < 0 || i >= lst.size()) return null;
			
			ZutiAircraft zac = (ZutiAircraft)lst.get(i);
			return zac;
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
	
	/**
	 * Enable or disable modify button
	 * @param value
	 */
	public void enableAcModifications(boolean value)
	{
		enableAcModifications = value;
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
		title = Plugin.i18n("mds.zAircrafts.title");
		clientWindow = create(new GWindowDialogClient()
		{
			public void resized()
			{
				super.resized();
				Zuti_WManageAircrafts.this.setAircraftSizes(this);
			}
		});
		com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient)clientWindow;
		
		lstAvailable = new Table(gwindowdialogclient, Plugin.i18n("bplace_planes"), 1.0F, 1.0F, 6.0F, 10.0F);
		lstInReserve = new Table(gwindowdialogclient, Plugin.i18n("bplace_list"), 14.0F, 1.0F, 6.0F, 10.0F);
		gwindowdialogclient.addControl(bAddAll = new GWindowButton(gwindowdialogclient, 8.0F, 1.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_addall")), null)
		{
			public boolean notify(int i_61_, int i_62_)
			{
				if (i_61_ != 2)
					return false;
				
				lstAvailable.lst.clear();
				Zuti_WManageAircrafts.this.addAllAircraft(lstAvailable.lst);
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addControl(bAdd = new GWindowButton(gwindowdialogclient, 8.0F, 3.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_add")), null)
		{
			public boolean notify(int i_68_, int i_69_)
			{
				if (i_68_ != 2)
					return false;

				int selectedId = lstInReserve.selectRow;
				if( selectedId < 0 )
					selectedId = 0;
				if( selectedId >= lstInReserve.lst.size() )
					selectedId = lstInReserve.lst.size()-1;
				
				if( lstAvailable.lst == null )
					lstAvailable.lst = new ArrayList();

				ZutiAircraft zac = (ZutiAircraft)lstInReserve.lst.get(selectedId);
				//System.out.println("SELECTED ZAC = " + zac.getAcName());
				airNames.add(zac);
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addControl(bRemAll = new GWindowButton(gwindowdialogclient, 8.0F, 6.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_delall")), null)
		{
			public boolean notify(int i_76_, int i_77_)
			{
				if (i_76_ != 2)
					return false;
				
				lstAvailable.lst.clear();
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addControl(bRem = new GWindowButton(gwindowdialogclient, 8.0F, 8.0F, 5.0F, 2.0F, (Plugin.i18n("bplace_del")), null)
		{
			public boolean notify(int i_83_, int i_84_)
			{
				if (i_83_ != 2) return false;
				int i_85_ = lstAvailable.selectRow;
				if (i_85_ < 0 || i_85_ >= lstAvailable.lst.size()) return true;
				lstAvailable.lst.remove(i_85_);
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addControl(bModifyPlane = new GWindowButton(gwindowdialogclient, 8.0F, 12.0F, 5.0F, 2.0F, Plugin.i18n("mds.aircraft.modify"), null)
		{			
			public void preRender()
			{
				super.preRender();
				setEnable(enableAcModifications);
			}
			public boolean notify(int i_83_, int i_84_)
			{
				if (i_83_ != 2)
					return false;
				
				if (lstAvailable.selectRow < 0 || lstAvailable.selectRow >= lstAvailable.lst.size())
					return true;
				else
				{
					ZutiAircraft zac = (ZutiAircraft)lstAvailable.lst.get(lstAvailable.selectRow);
					Zuti_WAircraftProperties properties = new Zuti_WAircraftProperties();
					properties.setAircraft( zac );
					properties.setTitle( zac.getAcName() );
					properties.showWindow();
				}
				return true;
			}
		});
		
		gwindowdialogclient.addLabel(lSeparate = new GWindowLabel(gwindowdialogclient, 3.0F, 12.0F, 12.0F, 1.6F, " " + Plugin.i18n("bplace_cats") + " ", null));
		bSeparate = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 12.5F, 27.0F, 8.0F);
		bSeparate.exclude = lSeparate;
		gwindowdialogclient.addLabel(lCountry = new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 7.0F, 1.6F, Plugin.i18n("bplace_country"), null));
		gwindowdialogclient.addControl(cCountry = new GWindowComboControl(gwindowdialogclient, 9.0F, 14.0F, 7.0F)
		{
			public void afterCreated()
			{
				super.afterCreated();
				setEditable(false);
				ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
				TreeMap treemap = new TreeMap();
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_89_ = 0; i_89_ < arraylist.size(); i_89_++)
				{
					Class var_class = (Class) arraylist.get(i_89_);
					if (Property.containsValue(var_class, "cockpitClass"))
					{
						String string = Property.stringValue(var_class, "originCountry", null);
						if (string != null)
						{
							String string_90_;
							try
							{
								string_90_ = resourcebundle.getString(string);
							}
							catch (Exception exception)
							{
								continue;
							}
							treemap.put(string_90_, string);
						}
					}
				}
				Iterator iterator = treemap.keySet().iterator();
				while (iterator.hasNext())
				{
					String string = (String) iterator.next();
					String string_91_ = (String) treemap.get(string);
					Zuti_WManageAircrafts.lstCountry.add(string_91_);
					add(string);
				}
				if (Zuti_WManageAircrafts.lstCountry.size() > 0) setSelected(0, true, false);
			}
		});
		gwindowdialogclient.addControl(bCountryAdd = new GWindowButton(gwindowdialogclient, 17.0F, 14.0F, 5.0F, 1.6F, (Plugin.i18n("bplace_add")), null)
		{
			public boolean notify(int i_97_, int i_98_)
			{
				if (i_97_ != 2) return false;
				String string = ((String) Zuti_WManageAircrafts.lstCountry.get(cCountry.getSelected()));
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_99_ = 0; i_99_ < arraylist.size(); i_99_++)
				{
					Class var_class = (Class) arraylist.get(i_99_);
					if (Property.containsValue(var_class, "cockpitClass") && string.equals(Property.stringValue(var_class, "originCountry", null)))
					{
						String string_100_ = Property.stringValue(var_class, "keyName");
						
						ZutiAircraft zac = new ZutiAircraft();
						zac.setAcName(string_100_);
						
						if (!lstAvailable.lst.contains(zac)) 
							lstAvailable.lst.add(zac);
					}
				}
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addControl(bCountryRem = new GWindowButton(gwindowdialogclient, 22.0F, 14.0F, 5.0F, 1.6F, (Plugin.i18n("bplace_del")), null)
		{
			public boolean notify(int i_106_, int i_107_)
			{
				if (i_106_ != 2) return false;
				String string = ((String) Zuti_WManageAircrafts.lstCountry.get(cCountry.getSelected()));
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_108_ = 0; i_108_ < arraylist.size(); i_108_++)
				{
					Class var_class = (Class) arraylist.get(i_108_);
					if (Property.containsValue(var_class, "cockpitClass") && string.equals(Property.stringValue(var_class, "originCountry", null)))
					{
						String string_109_ = Property.stringValue(var_class, "keyName");
						int i_110_ = lstAvailable.lst.indexOf(string_109_);
						if (i_110_ >= 0) lstAvailable.lst.remove(i_110_);
					}
				}
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addLabel(lYear = new GWindowLabel(gwindowdialogclient, 2.0F, 16.0F, 7.0F, 1.6F, Plugin.i18n("bplace_year"), null));
		gwindowdialogclient.addControl(cYear = new GWindowComboControl(gwindowdialogclient, 9.0F, 16.0F, 7.0F)
		{
			public void afterCreated()
			{
				super.afterCreated();
				setEditable(false);
				TreeMap treemap = new TreeMap();
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_114_ = 0; i_114_ < arraylist.size(); i_114_++)
				{
					Class var_class = (Class) arraylist.get(i_114_);
					if (Property.containsValue(var_class, "cockpitClass"))
					{
						float f = Property.floatValue(var_class, "yearService", 0.0F);
						if (f != 0.0F) treemap.put("" + (int) f, null);
					}
				}
				Iterator iterator = treemap.keySet().iterator();
				while (iterator.hasNext())
				{
					String string = (String) iterator.next();
					Zuti_WManageAircrafts.lstYear.add(string);
					add(string);
				}
				if (Zuti_WManageAircrafts.lstYear.size() > 0) setSelected(0, true, false);
			}
		});
		gwindowdialogclient.addControl(bYearAdd = new GWindowButton(gwindowdialogclient, 17.0F, 16.0F, 5.0F, 1.6F, (Plugin.i18n("bplace_add")), null)
		{
			public boolean notify(int i_120_, int i_121_)
			{
				if (i_120_ != 2) return false;
				String string = (String) Zuti_WManageAircrafts.lstYear.get(cYear.getSelected());
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_122_ = 0; i_122_ < arraylist.size(); i_122_++)
				{
					Class var_class = (Class) arraylist.get(i_122_);
					if (Property.containsValue(var_class, "cockpitClass") && string.equals("" + (int) (Property.floatValue(var_class, "yearService", 0.0F))))
					{
						String string_123_ = Property.stringValue(var_class, "keyName");
						
						ZutiAircraft zac = new ZutiAircraft();
						zac.setAcName(string_123_);
						
						if (!lstAvailable.lst.contains(zac)) 
							lstAvailable.lst.add(zac);
					}
				}
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addControl(bYearRem = new GWindowButton(gwindowdialogclient, 22.0F, 16.0F, 5.0F, 1.6F, (Plugin.i18n("bplace_del")), null)
		{
			public boolean notify(int i_129_, int i_130_)
			{
				if (i_129_ != 2) return false;
				String string = (String) Zuti_WManageAircrafts.lstYear.get(cYear.getSelected());
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_131_ = 0; i_131_ < arraylist.size(); i_131_++)
				{
					Class var_class = (Class) arraylist.get(i_131_);
					if (Property.containsValue(var_class, "cockpitClass") && string.equals("" + (int) (Property.floatValue(var_class, "yearService", 0.0F))))
					{
						String string_132_ = Property.stringValue(var_class, "keyName");
						int i_133_ = lstAvailable.lst.indexOf(string_132_);
						if (i_133_ >= 0) lstAvailable.lst.remove(i_133_);
					}
				}
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addLabel(lType = new GWindowLabel(gwindowdialogclient, 2.0F, 18.0F, 7.0F, 1.6F, Plugin.i18n("bplace_category"), null));
		gwindowdialogclient.addControl(cType = new GWindowComboControl(gwindowdialogclient, 9.0F, 18.0F, 7.0F)
		{
			public void afterCreated()
			{
				super.afterCreated();
				setEditable(false);
				TreeMap treemap = new TreeMap();
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_137_ = 0; i_137_ < arraylist.size(); i_137_++)
				{
					Class var_class = (Class) arraylist.get(i_137_);
					if (Property.containsValue(var_class, "cockpitClass"))
					{
                        if (TypeStormovik.class.isAssignableFrom(var_class))
                            treemap.put(Plugin.i18n("bplace_sturm"), TypeStormovik.class);
                        if (TypeFighter.class.isAssignableFrom(var_class))
                            treemap.put(Plugin.i18n("bplace_fiter"), TypeFighter.class);
                        if (TypeBomber.class.isAssignableFrom(var_class))
                            treemap.put(Plugin.i18n("bplace_bomber"), TypeBomber.class);
                        if (TypeScout.class.isAssignableFrom(var_class))
                            treemap.put(Plugin.i18n("bplace_recon"), TypeScout.class);
                        if (TypeDiveBomber.class.isAssignableFrom(var_class))
                            treemap.put(Plugin.i18n("bplace_diver"), TypeDiveBomber.class);
                        if (TypeSailPlane.class.isAssignableFrom(var_class))
                            treemap.put(Plugin.i18n("bplace_sailer"), TypeSailPlane.class);
                        if (Scheme1.class.isAssignableFrom(var_class))
                            treemap.put(Plugin.i18n("bplace_single"), Scheme1.class);
                        else
                            treemap.put(Plugin.i18n("bplace_multi"), null);
					}
				}
				Iterator iterator = treemap.keySet().iterator();
				while (iterator.hasNext())
				{
					String string = (String) iterator.next();
					Class var_class = (Class) treemap.get(string);
					PlMisBorn.lstType.add(var_class);
					add(string);
				}
				if (PlMisBorn.lstType.size() > 0) setSelected(0, true, false);
			}
		});
		gwindowdialogclient.addControl(bTypeAdd = new GWindowButton(gwindowdialogclient, 17.0F, 18.0F, 5.0F, 1.6F, (Plugin.i18n("bplace_add")), null)
		{
			public boolean notify(int i_143_, int i_144_)
			{
				if (i_143_ != 2) return false;
				Class var_class = (Class) PlMisBorn.lstType.get(cType.getSelected());
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_145_ = 0; i_145_ < arraylist.size(); i_145_++)
				{
					Class var_class_146_ = (Class) arraylist.get(i_145_);
					if (Property.containsValue(var_class_146_, "cockpitClass"))
					{
						if (var_class == null)
						{
							if (Scheme1.class.isAssignableFrom(var_class_146_)) continue;
						}
						else if (!var_class.isAssignableFrom(var_class_146_)) continue;
						String string = Property.stringValue(var_class_146_, "keyName");
						
						ZutiAircraft zac = new ZutiAircraft();
						zac.setAcName(string);
						
						if (!lstAvailable.lst.contains(zac)) 
							lstAvailable.lst.add(zac);
					}
				}
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
		gwindowdialogclient.addControl(bTypeRem = new GWindowButton(gwindowdialogclient, 22.0F, 18.0F, 5.0F, 1.6F, (Plugin.i18n("bplace_del")), null)
		{
			public boolean notify(int i_152_, int i_153_)
			{
				if (i_152_ != 2) return false;
				Class var_class = (Class) PlMisBorn.lstType.get(cType.getSelected());
				ArrayList arraylist = Main.cur().airClasses;
				for (int i_154_ = 0; i_154_ < arraylist.size(); i_154_++)
				{
					Class var_class_155_ = (Class) arraylist.get(i_154_);
					if (Property.containsValue(var_class_155_, "cockpitClass"))
					{
						if (var_class == null)
						{
							if (Scheme1.class.isAssignableFrom(var_class_155_)) continue;
						}
						else if (!var_class.isAssignableFrom(var_class_155_)) continue;
						String string = Property.stringValue(var_class_155_, "keyName");
						int i_156_ = lstAvailable.lst.indexOf(string);
						if (i_156_ >= 0) lstAvailable.lst.remove(i_156_);
					}
				}
				Zuti_WManageAircrafts.this.fillTabAircraft();
				return true;
			}
		});
	}
	
	private void fillTabAircraft()
	{
		int selectedRow = lstInReserve.selectRow;
		lstAvailable.lst = airNames;
		lstInReserve.lst.clear();
		
		//Fill inReserve list with all AC that exist
		addAllAircraft( lstInReserve.lst );
		
		//If airNames is not null, remove AC that are in it from inReserve list
		if( lstAvailable.lst != null && lstAvailable.lst.size() > 0 )
		{
			for( int x=0; x<airNames.size(); x++ )
			{
				lstInReserve.lst.remove( (ZutiAircraft)airNames.get(x) );
			}
		}
		
		lstInReserve.setSelect(selectedRow, 0);
		lstAvailable.resized();
		lstInReserve.resized();
		
		//TODO: Added by |ZUTI|: Sorting...
		//----------------------------------------
		if( lstAvailable.lst != null )
			Collections.sort(lstAvailable.lst, new ZutiSupportMethods_Builder.ZutiAircraft_CompareByName());
		if( lstInReserve.lst != null )
			Collections.sort(lstInReserve.lst, new ZutiSupportMethods_Builder.ZutiAircraft_CompareByName());
		//----------------------------------------
	}
	
	private void addAllAircraft(ArrayList arraylist)
	{
		ArrayList list = Main.cur().airClasses;
		for (int i = 0; i < list.size(); i++)
		{
			Class var_class = (Class) list.get(i);
			if( offerOnlyFlyableAircraft && !Property.containsValue(var_class, "cockpitClass") )
				continue;
			
			//TODO: Modified by |ZUTI|: just replaced original block
			//-----------------------------------------------------------------
			String zutiAcName = Property.stringValue(var_class, "keyName");
			int index = zutiAcName.indexOf("*");
			if( index < 0 || index > 1 )
			{
				ZutiAircraft zac = new ZutiAircraft();
				zac.setAcName( zutiAcName );
				
				if (!arraylist.contains(zac))
					arraylist.add(zac);
			}
			//-----------------------------------------------------------------
		}
	}

	private void setAircraftSizes(GWindow gwindow)
	{
		float f = gwindow.win.dx;
		float f_162_ = gwindow.win.dy;
		GFont gfont = gwindow.root.textFonts[0];
		float f_163_ = gwindow.lAF().metric();
		GSize gsize = new GSize();
		gfont.size(Plugin.i18n("bplace_addall"), gsize);
		float f_164_ = gsize.dx;
		gfont.size(Plugin.i18n("bplace_add"), gsize);
		float f_165_ = gsize.dx;
		gfont.size(Plugin.i18n("bplace_delall"), gsize);
		float f_166_ = gsize.dx;
		gfont.size(Plugin.i18n("bplace_del"), gsize);
		float f_167_ = gsize.dx;
		gfont.size(Plugin.i18n("bplace_planes"), gsize);
		float f_168_ = gsize.dx;
		gfont.size(Plugin.i18n("bplace_list"), gsize);
		float f_169_ = gsize.dx;
		float f_170_ = f_164_;
		if (f_170_ < f_165_)
			f_170_ = f_165_;
		if (f_170_ < f_166_)
			f_170_ = f_166_;
		if (f_170_ < f_167_)
			f_170_ = f_167_;
		float f_171_ = f_163_ + f_170_;
		f_170_ += (f_163_ + 4.0F * f_163_ + f_168_ + 4.0F * f_163_ + f_169_ + 4.0F * f_163_);
		if (f < f_170_)
			f = f_170_;
		float f_172_ = 10.0F * f_163_ + 10.0F * f_163_ + 2.0F * f_163_;
		if (f_162_ < f_172_)
			f_162_ = f_172_;
		float f_173_ = (f - f_171_) / 2.0F;
		bAddAll.setPosSize(f_173_, f_163_, f_171_, 2.0F * f_163_);
		bAdd.setPosSize(f_173_, f_163_ + 2.0F * f_163_, f_171_, 2.0F * f_163_);
		bRemAll.setPosSize(f_173_, 2.0F * f_163_ + 4.0F * f_163_, f_171_, 2.0F * f_163_);
		bRem.setPosSize(f_173_, 2.0F * f_163_ + 6.0F * f_163_, f_171_, 2.0F * f_163_);
		bModifyPlane.setPosSize(f_173_, 2.0F * f_163_ + 10.0F * f_163_, f_171_, 2.0F * f_163_);
		float f_174_ = (f - f_171_ - 4.0F * f_163_) / 2.0F;
		float f_175_ = f_162_ - 6.0F * f_163_ - 2.0F * f_163_ - 3.0F * f_163_;
		lstAvailable.setPosSize(f_163_, f_163_, f_174_, f_175_);
		lstInReserve.setPosSize(f - f_163_ - f_174_, f_163_, f_174_, f_175_);
		gfont.size(" " + Plugin.i18n("bplace_cats") + " ", gsize);
		f_174_ = gsize.dx;
		float f_176_ = f_163_ + f_175_;
		lSeparate.setPosSize(2.0F * f_163_, f_176_, f_174_, 2.0F * f_163_);
		bSeparate.setPosSize(f_163_, f_176_ + f_163_, f - 2.0F * f_163_, f_162_ - f_176_ - 2.0F * f_163_);
		gfont.size(Plugin.i18n("bplace_country"), gsize);
		float f_177_ = gsize.dx;
		gfont.size(Plugin.i18n("bplace_year"), gsize);
		if (f_177_ < gsize.dx)
			f_177_ = gsize.dx;
		gfont.size(Plugin.i18n("bplace_category"), gsize);
		if (f_177_ < gsize.dx)
			f_177_ = gsize.dx;
		f_171_ = 2.0F * f_163_ + f_165_ + f_167_;
		f_174_ = f - f_177_ - f_171_ - 6.0F * f_163_;
		float f_178_ = gwindow.lAF().getComboH();
		lCountry.setPosSize(2.0F * f_163_, f_176_ + 2.0F * f_163_, f_177_, 2.0F * f_163_);
		cCountry.setPosSize(2.0F * f_163_ + f_177_ + f_163_, (f_176_ + 2.0F * f_163_ + (2.0F * f_163_ - f_178_) / 2.0F), f_174_, f_178_);
		bCountryAdd.setPosSize(f - 4.0F * f_163_ - f_167_ - f_165_, f_176_ + 2.0F * f_163_, f_163_ + f_165_, 2.0F * f_163_);
		bCountryRem.setPosSize(f - 3.0F * f_163_ - f_167_, f_176_ + 2.0F * f_163_, f_163_ + f_167_, 2.0F * f_163_);
		lYear.setPosSize(2.0F * f_163_, f_176_ + 4.0F * f_163_, f_177_, 2.0F * f_163_);
		cYear.setPosSize(2.0F * f_163_ + f_177_ + f_163_, (f_176_ + 4.0F * f_163_ + (2.0F * f_163_ - f_178_) / 2.0F), f_174_, f_178_);
		bYearAdd.setPosSize(f - 4.0F * f_163_ - f_167_ - f_165_, f_176_ + 4.0F * f_163_, f_163_ + f_165_, 2.0F * f_163_);
		bYearRem.setPosSize(f - 3.0F * f_163_ - f_167_, f_176_ + 4.0F * f_163_, f_163_ + f_167_, 2.0F * f_163_);
		lType.setPosSize(2.0F * f_163_, f_176_ + 6.0F * f_163_, f_177_, 2.0F * f_163_);
		cType.setPosSize(2.0F * f_163_ + f_177_ + f_163_, (f_176_ + 6.0F * f_163_ + (2.0F * f_163_ - f_178_) / 2.0F), f_174_, f_178_);
		bTypeAdd.setPosSize(f - 4.0F * f_163_ - f_167_ - f_165_, f_176_ + 6.0F * f_163_, f_163_ + f_165_, 2.0F * f_163_);
		bTypeRem.setPosSize(f - 3.0F * f_163_ - f_167_, f_176_ + 6.0F * f_163_, f_163_ + f_167_, 2.0F * f_163_);
	}
	
	public void afterCreated()
	{
		super.afterCreated();
		
		resized();
		close(false);
	}
	
	//Override default close method
	public void close(boolean flag)
	{
		if( parentEditControl != null )
		{
			StringBuffer sb = new StringBuffer();
			if( lstAvailable.lst != null )
			{
				for( int i=0; i<lstAvailable.lst.size(); i++ )
				{
					sb.append(lstAvailable.lst.get(i));
					sb.append(" ");
				}
			}
			
			parentEditControl.setValue(sb.toString());
			parentEditControl.notify(2,0);
			
			com.maddox.il2.builder.PlMission.setChanged();
		}
		
		super.close(flag);
	}
	
	public Zuti_WManageAircrafts()
	{		
		doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 40.0F, 40.0F, true);
		bSizable = true;
	}
	
	public void setParentEditControl(GWindowEditControl inParentEditControl, boolean onlyFlyableAc)
	{
		parentEditControl = inParentEditControl;
		offerOnlyFlyableAircraft = onlyFlyableAc;
		
		if( inParentEditControl != null )
		{
			fillTabAircraft();
		}
		
		if( airNames == null )
			airNames = new ArrayList();
	}
	
	public void clearAirNames()
	{
		airNames.clear();
		airNames = null;
	}
	
	/**
	 * Set title for this GUI.
	 * @param newTitle
	 */
	public void setTitle(String newTitle)
	{
		title = newTitle;
	}
	
	/**
	 * Add aircraft as available.
	 * @param zac
	 */
	public void addAircraft(ZutiAircraft zac)
	{
		if( airNames == null )
			airNames = new ArrayList();
		
		if( !airNames.contains(zac) )
		{
			airNames.add(zac);
		}
	}
	
	/**
	 * Returns list of available aircraft.
	 * @return
	 */
	public List getAircraft()
	{
		return airNames;
	}
	
	public void printAcList()
	{
		System.out.println("AC list - after adding new AC:");
		for( int i=0; i<airNames.size(); i++ )
		{
			ZutiAircraft zac = (ZutiAircraft)airNames.get(i);
			System.out.println("  ZAC NAME=" + zac.getAcName() + ", NR=" + zac.getNumberOfAIrcraft());
		}
		System.out.println("===========================");
	}
}