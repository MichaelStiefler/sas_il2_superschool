/*4.09 compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GSize;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.ZutiAircraft;
import com.maddox.il2.objects.air.Scheme1;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeSailPlane;
import com.maddox.il2.objects.air.TypeScout;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.rts.LDRres;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;

public class Zuti_WManageAircrafts extends GWindowFramed {
    private Table              lstAvailable;
    Table                      lstInReserve;
    GWindowButton              bAddAll;
    GWindowButton              bAdd;
    GWindowButton              bRemAll;
    GWindowButton              bRem;
    GWindowButton              bModifyPlane;
    GWindowLabel               lSeparate;
    GWindowBoxSeparate         bSeparate;
    GWindowLabel               lCountry;
    GWindowComboControl        cCountry;
    static ArrayList           lstCountry               = new ArrayList();
    GWindowButton              bCountryAdd;
    GWindowButton              bCountryRem;
    GWindowLabel               lYear;
    GWindowComboControl        cYear;
    static ArrayList           lstYear                  = new ArrayList();
    GWindowButton              bYearAdd;
    GWindowButton              bYearRem;
    GWindowLabel               lType;
    GWindowComboControl        cType;
    static ArrayList           lstType                  = new ArrayList();
    GWindowButton              bTypeAdd;
    GWindowButton              bTypeRem;
    // Inserted ArrayList containing aircrafts
    private ArrayList          airNames                 = null;
    private GWindowEditControl parentEditControl        = null;
    private boolean            enableAcModifications    = false;
    private boolean            offerOnlyFlyableAircraft = false;

    class Table extends GWindowTable {
        public ArrayList lst = new ArrayList();

        public int countRows() {
            return this.lst != null ? this.lst.size() : 0;
        }

        public Object getValueAt(int i, int i_0_) {
            if (this.lst == null) return null;
            if (i < 0 || i >= this.lst.size()) return null;

            ZutiAircraft zac = (ZutiAircraft) this.lst.get(i);
            return zac;
        }

        public void resolutionChanged() {
            this.vSB.scroll = this.rowHeight(0);
            super.resolutionChanged();
        }

        public Table(GWindow gwindow, String string, float f, float f_1_, float f_2_, float f_3_) {
            super(gwindow, f, f_1_, f_2_, f_3_);
            this.bColumnsSizable = false;
            this.addColumn(string, null);
            this.vSB.scroll = this.rowHeight(0);
            this.resized();
        }
    }

    /**
     * Enable or disable modify button
     *
     * @param value
     */
    public void enableAcModifications(boolean value) {
        this.enableAcModifications = value;
    }

    public void windowShown() {
        super.windowShown();
    }

    public void windowHidden() {
        super.windowHidden();
    }

    public void created() {
        this.bAlwaysOnTop = true;
        super.created();
        this.title = Plugin.i18n("mds.zAircrafts.title");
        this.clientWindow = this.create(new GWindowDialogClient() {
            public void resized() {
                super.resized();
                Zuti_WManageAircrafts.this.setAircraftSizes(this);
            }
        });
        com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient) this.clientWindow;

        this.lstAvailable = new Table(gwindowdialogclient, Plugin.i18n("bplace_planes"), 1.0F, 1.0F, 6.0F, 10.0F);
        this.lstInReserve = new Table(gwindowdialogclient, Plugin.i18n("bplace_list"), 14.0F, 1.0F, 6.0F, 10.0F);
        gwindowdialogclient.addControl(this.bAddAll = new GWindowButton(gwindowdialogclient, 8.0F, 1.0F, 5.0F, 2.0F, Plugin.i18n("bplace_addall"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                Zuti_WManageAircrafts.this.lstAvailable.lst.clear();
                Zuti_WManageAircrafts.this.addAllAircraft(Zuti_WManageAircrafts.this.lstAvailable.lst);
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bAdd = new GWindowButton(gwindowdialogclient, 8.0F, 3.0F, 5.0F, 2.0F, Plugin.i18n("bplace_add"), null) {
            public boolean notify(int i_68_, int i_69_) {
                if (i_68_ != 2) return false;

                int selectedId = Zuti_WManageAircrafts.this.lstInReserve.selectRow;
                if (selectedId < 0) selectedId = 0;
                if (selectedId >= Zuti_WManageAircrafts.this.lstInReserve.lst.size()) selectedId = Zuti_WManageAircrafts.this.lstInReserve.lst.size() - 1;

                if (Zuti_WManageAircrafts.this.lstAvailable.lst == null) Zuti_WManageAircrafts.this.lstAvailable.lst = new ArrayList();

                ZutiAircraft zac = (ZutiAircraft) Zuti_WManageAircrafts.this.lstInReserve.lst.get(selectedId);
                // System.out.println("SELECTED ZAC = " + zac.getAcName());
                Zuti_WManageAircrafts.this.airNames.add(zac);
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bRemAll = new GWindowButton(gwindowdialogclient, 8.0F, 6.0F, 5.0F, 2.0F, Plugin.i18n("bplace_delall"), null) {
            public boolean notify(int i_76_, int i_77_) {
                if (i_76_ != 2) return false;

                Zuti_WManageAircrafts.this.lstAvailable.lst.clear();
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bRem = new GWindowButton(gwindowdialogclient, 8.0F, 8.0F, 5.0F, 2.0F, Plugin.i18n("bplace_del"), null) {
            public boolean notify(int i_83_, int i_84_) {
                if (i_83_ != 2) return false;
                int i_85_ = Zuti_WManageAircrafts.this.lstAvailable.selectRow;
                if (i_85_ < 0 || i_85_ >= Zuti_WManageAircrafts.this.lstAvailable.lst.size()) return true;
                Zuti_WManageAircrafts.this.lstAvailable.lst.remove(i_85_);
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bModifyPlane = new GWindowButton(gwindowdialogclient, 8.0F, 12.0F, 5.0F, 2.0F, Plugin.i18n("mds.aircraft.modify"), null) {
            public void preRender() {
                super.preRender();
                this.setEnable(Zuti_WManageAircrafts.this.enableAcModifications);
            }

            public boolean notify(int i_83_, int i_84_) {
                if (i_83_ != 2) return false;

                if (Zuti_WManageAircrafts.this.lstAvailable.selectRow < 0 || Zuti_WManageAircrafts.this.lstAvailable.selectRow >= Zuti_WManageAircrafts.this.lstAvailable.lst.size()) return true;
                else {
                    ZutiAircraft zac = (ZutiAircraft) Zuti_WManageAircrafts.this.lstAvailable.lst.get(Zuti_WManageAircrafts.this.lstAvailable.selectRow);
                    Zuti_WAircraftProperties properties = new Zuti_WAircraftProperties();
                    properties.setAircraft(zac);
                    properties.setTitle(zac.getAcName());
                    properties.showWindow();
                }
                return true;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate = new GWindowLabel(gwindowdialogclient, 3.0F, 12.0F, 12.0F, 1.6F, " " + Plugin.i18n("bplace_cats") + " ", null));
        this.bSeparate = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 12.5F, 27.0F, 8.0F);
        this.bSeparate.exclude = this.lSeparate;
        gwindowdialogclient.addLabel(this.lCountry = new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 7.0F, 1.6F, Plugin.i18n("bplace_country"), null));
        gwindowdialogclient.addControl(this.cCountry = new GWindowComboControl(gwindowdialogclient, 9.0F, 14.0F, 7.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
                TreeMap treemap = new TreeMap();
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_89_ = 0; i_89_ < arraylist.size(); i_89_++) {
                    Class var_class = (Class) arraylist.get(i_89_);
                    if (Property.containsValue(var_class, "cockpitClass")) {
                        String string = Property.stringValue(var_class, "originCountry", null);
                        if (string != null) {
                            String string_90_;
                            try {
                                string_90_ = resourcebundle.getString(string);
                            } catch (Exception exception) {
                                continue;
                            }
                            treemap.put(string_90_, string);
                        }
                    }
                }
                Iterator iterator = treemap.keySet().iterator();
                while (iterator.hasNext()) {
                    String string = (String) iterator.next();
                    String string_91_ = (String) treemap.get(string);
                    Zuti_WManageAircrafts.lstCountry.add(string_91_);
                    this.add(string);
                }
                if (Zuti_WManageAircrafts.lstCountry.size() > 0) this.setSelected(0, true, false);
            }
        });
        gwindowdialogclient.addControl(this.bCountryAdd = new GWindowButton(gwindowdialogclient, 17.0F, 14.0F, 5.0F, 1.6F, Plugin.i18n("bplace_add"), null) {
            public boolean notify(int i_97_, int i_98_) {
                if (i_97_ != 2) return false;
                String string = (String) Zuti_WManageAircrafts.lstCountry.get(Zuti_WManageAircrafts.this.cCountry.getSelected());
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_99_ = 0; i_99_ < arraylist.size(); i_99_++) {
                    Class var_class = (Class) arraylist.get(i_99_);
                    if (Property.containsValue(var_class, "cockpitClass") && string.equals(Property.stringValue(var_class, "originCountry", null))) {
                        String string_100_ = Property.stringValue(var_class, "keyName");

                        ZutiAircraft zac = new ZutiAircraft();
                        zac.setAcName(string_100_);

                        if (!Zuti_WManageAircrafts.this.lstAvailable.lst.contains(zac)) Zuti_WManageAircrafts.this.lstAvailable.lst.add(zac);
                    }
                }
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bCountryRem = new GWindowButton(gwindowdialogclient, 22.0F, 14.0F, 5.0F, 1.6F, Plugin.i18n("bplace_del"), null) {
            public boolean notify(int i_106_, int i_107_) {
                if (i_106_ != 2) return false;
                String string = (String) Zuti_WManageAircrafts.lstCountry.get(Zuti_WManageAircrafts.this.cCountry.getSelected());
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_108_ = 0; i_108_ < arraylist.size(); i_108_++) {
                    Class var_class = (Class) arraylist.get(i_108_);
                    if (Property.containsValue(var_class, "cockpitClass") && string.equals(Property.stringValue(var_class, "originCountry", null))) {
                        String string_109_ = Property.stringValue(var_class, "keyName");
                        int i_110_ = Zuti_WManageAircrafts.this.lstAvailable.lst.indexOf(string_109_);
                        if (i_110_ >= 0) Zuti_WManageAircrafts.this.lstAvailable.lst.remove(i_110_);
                    }
                }
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addLabel(this.lYear = new GWindowLabel(gwindowdialogclient, 2.0F, 16.0F, 7.0F, 1.6F, Plugin.i18n("bplace_year"), null));
        gwindowdialogclient.addControl(this.cYear = new GWindowComboControl(gwindowdialogclient, 9.0F, 16.0F, 7.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                TreeMap treemap = new TreeMap();
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_114_ = 0; i_114_ < arraylist.size(); i_114_++) {
                    Class var_class = (Class) arraylist.get(i_114_);
                    if (Property.containsValue(var_class, "cockpitClass")) {
                        float f = Property.floatValue(var_class, "yearService", 0.0F);
                        if (f != 0.0F) treemap.put("" + (int) f, null);
                    }
                }
                Iterator iterator = treemap.keySet().iterator();
                while (iterator.hasNext()) {
                    String string = (String) iterator.next();
                    Zuti_WManageAircrafts.lstYear.add(string);
                    this.add(string);
                }
                if (Zuti_WManageAircrafts.lstYear.size() > 0) this.setSelected(0, true, false);
            }
        });
        gwindowdialogclient.addControl(this.bYearAdd = new GWindowButton(gwindowdialogclient, 17.0F, 16.0F, 5.0F, 1.6F, Plugin.i18n("bplace_add"), null) {
            public boolean notify(int i_120_, int i_121_) {
                if (i_120_ != 2) return false;
                String string = (String) Zuti_WManageAircrafts.lstYear.get(Zuti_WManageAircrafts.this.cYear.getSelected());
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_122_ = 0; i_122_ < arraylist.size(); i_122_++) {
                    Class var_class = (Class) arraylist.get(i_122_);
                    if (Property.containsValue(var_class, "cockpitClass") && string.equals("" + (int) Property.floatValue(var_class, "yearService", 0.0F))) {
                        String string_123_ = Property.stringValue(var_class, "keyName");

                        ZutiAircraft zac = new ZutiAircraft();
                        zac.setAcName(string_123_);

                        if (!Zuti_WManageAircrafts.this.lstAvailable.lst.contains(zac)) Zuti_WManageAircrafts.this.lstAvailable.lst.add(zac);
                    }
                }
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bYearRem = new GWindowButton(gwindowdialogclient, 22.0F, 16.0F, 5.0F, 1.6F, Plugin.i18n("bplace_del"), null) {
            public boolean notify(int i_129_, int i_130_) {
                if (i_129_ != 2) return false;
                String string = (String) Zuti_WManageAircrafts.lstYear.get(Zuti_WManageAircrafts.this.cYear.getSelected());
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_131_ = 0; i_131_ < arraylist.size(); i_131_++) {
                    Class var_class = (Class) arraylist.get(i_131_);
                    if (Property.containsValue(var_class, "cockpitClass") && string.equals("" + (int) Property.floatValue(var_class, "yearService", 0.0F))) {
                        String string_132_ = Property.stringValue(var_class, "keyName");
                        int i_133_ = Zuti_WManageAircrafts.this.lstAvailable.lst.indexOf(string_132_);
                        if (i_133_ >= 0) Zuti_WManageAircrafts.this.lstAvailable.lst.remove(i_133_);
                    }
                }
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addLabel(this.lType = new GWindowLabel(gwindowdialogclient, 2.0F, 18.0F, 7.0F, 1.6F, Plugin.i18n("bplace_category"), null));
        gwindowdialogclient.addControl(this.cType = new GWindowComboControl(gwindowdialogclient, 9.0F, 18.0F, 7.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                TreeMap treemap = new TreeMap();
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_137_ = 0; i_137_ < arraylist.size(); i_137_++) {
                    Class var_class = (Class) arraylist.get(i_137_);
                    if (Property.containsValue(var_class, "cockpitClass")) {
                        if (TypeStormovik.class.isAssignableFrom(var_class)) treemap.put(Plugin.i18n("bplace_sturm"), TypeStormovik.class);
                        if (TypeFighter.class.isAssignableFrom(var_class)) treemap.put(Plugin.i18n("bplace_fiter"), TypeFighter.class);
                        if (TypeBomber.class.isAssignableFrom(var_class)) treemap.put(Plugin.i18n("bplace_bomber"), TypeBomber.class);
                        if (TypeScout.class.isAssignableFrom(var_class)) treemap.put(Plugin.i18n("bplace_recon"), TypeScout.class);
                        if (TypeDiveBomber.class.isAssignableFrom(var_class)) treemap.put(Plugin.i18n("bplace_diver"), TypeDiveBomber.class);
                        if (TypeSailPlane.class.isAssignableFrom(var_class)) treemap.put(Plugin.i18n("bplace_sailer"), TypeSailPlane.class);
                        if (Scheme1.class.isAssignableFrom(var_class)) treemap.put(Plugin.i18n("bplace_single"), Scheme1.class);
                        else treemap.put(Plugin.i18n("bplace_multi"), null);
                    }
                }
                Iterator iterator = treemap.keySet().iterator();
                while (iterator.hasNext()) {
                    String string = (String) iterator.next();
                    Class var_class = (Class) treemap.get(string);
                    PlMisBorn.lstType.add(var_class);
                    this.add(string);
                }
                if (PlMisBorn.lstType.size() > 0) this.setSelected(0, true, false);
            }
        });
        gwindowdialogclient.addControl(this.bTypeAdd = new GWindowButton(gwindowdialogclient, 17.0F, 18.0F, 5.0F, 1.6F, Plugin.i18n("bplace_add"), null) {
            public boolean notify(int i_143_, int i_144_) {
                if (i_143_ != 2) return false;
                Class var_class = (Class) PlMisBorn.lstType.get(Zuti_WManageAircrafts.this.cType.getSelected());
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_145_ = 0; i_145_ < arraylist.size(); i_145_++) {
                    Class var_class_146_ = (Class) arraylist.get(i_145_);
                    if (Property.containsValue(var_class_146_, "cockpitClass")) {
                        if (var_class == null) {
                            if (Scheme1.class.isAssignableFrom(var_class_146_)) continue;
                        } else if (!var_class.isAssignableFrom(var_class_146_)) continue;
                        String string = Property.stringValue(var_class_146_, "keyName");

                        ZutiAircraft zac = new ZutiAircraft();
                        zac.setAcName(string);

                        if (!Zuti_WManageAircrafts.this.lstAvailable.lst.contains(zac)) Zuti_WManageAircrafts.this.lstAvailable.lst.add(zac);
                    }
                }
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bTypeRem = new GWindowButton(gwindowdialogclient, 22.0F, 18.0F, 5.0F, 1.6F, Plugin.i18n("bplace_del"), null) {
            public boolean notify(int i_152_, int i_153_) {
                if (i_152_ != 2) return false;
                Class var_class = (Class) PlMisBorn.lstType.get(Zuti_WManageAircrafts.this.cType.getSelected());
                ArrayList arraylist = Main.cur().airClasses;
                for (int i_154_ = 0; i_154_ < arraylist.size(); i_154_++) {
                    Class var_class_155_ = (Class) arraylist.get(i_154_);
                    if (Property.containsValue(var_class_155_, "cockpitClass")) {
                        if (var_class == null) {
                            if (Scheme1.class.isAssignableFrom(var_class_155_)) continue;
                        } else if (!var_class.isAssignableFrom(var_class_155_)) continue;
                        String string = Property.stringValue(var_class_155_, "keyName");
                        int i_156_ = Zuti_WManageAircrafts.this.lstAvailable.lst.indexOf(string);
                        if (i_156_ >= 0) Zuti_WManageAircrafts.this.lstAvailable.lst.remove(i_156_);
                    }
                }
                Zuti_WManageAircrafts.this.fillTabAircraft();
                return true;
            }
        });
    }

    private void fillTabAircraft() {
        int selectedRow = this.lstInReserve.selectRow;
        this.lstAvailable.lst = this.airNames;
        this.lstInReserve.lst.clear();

        // Fill inReserve list with all AC that exist
        this.addAllAircraft(this.lstInReserve.lst);

        // If airNames is not null, remove AC that are in it from inReserve list
        if (this.lstAvailable.lst != null && this.lstAvailable.lst.size() > 0) for (int x = 0; x < this.airNames.size(); x++)
            this.lstInReserve.lst.remove(this.airNames.get(x));

        this.lstInReserve.setSelect(selectedRow, 0);
        this.lstAvailable.resized();
        this.lstInReserve.resized();

        // TODO: Added by |ZUTI|: Sorting...
        // ----------------------------------------
        if (this.lstAvailable.lst != null) Collections.sort(this.lstAvailable.lst, new ZutiSupportMethods_Builder.ZutiAircraft_CompareByName());
        if (this.lstInReserve.lst != null) Collections.sort(this.lstInReserve.lst, new ZutiSupportMethods_Builder.ZutiAircraft_CompareByName());
        // ----------------------------------------
    }

    private void addAllAircraft(ArrayList arraylist) {
        ArrayList list = Main.cur().airClasses;
        for (int i = 0; i < list.size(); i++) {
            Class var_class = (Class) list.get(i);
            if (this.offerOnlyFlyableAircraft && !Property.containsValue(var_class, "cockpitClass")) continue;

            // TODO: Modified by |ZUTI|: just replaced original block
            // -----------------------------------------------------------------
            String zutiAcName = Property.stringValue(var_class, "keyName");
            int index = zutiAcName.indexOf("*");
            if (index < 0 || index > 1) {
                ZutiAircraft zac = new ZutiAircraft();
                zac.setAcName(zutiAcName);

                if (!arraylist.contains(zac)) arraylist.add(zac);
            }
            // -----------------------------------------------------------------
        }
    }

    private void setAircraftSizes(GWindow gwindow) {
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
        if (f_170_ < f_165_) f_170_ = f_165_;
        if (f_170_ < f_166_) f_170_ = f_166_;
        if (f_170_ < f_167_) f_170_ = f_167_;
        float f_171_ = f_163_ + f_170_;
        f_170_ += f_163_ + 4.0F * f_163_ + f_168_ + 4.0F * f_163_ + f_169_ + 4.0F * f_163_;
        if (f < f_170_) f = f_170_;
        float f_172_ = 10.0F * f_163_ + 10.0F * f_163_ + 2.0F * f_163_;
        if (f_162_ < f_172_) f_162_ = f_172_;
        float f_173_ = (f - f_171_) / 2.0F;
        this.bAddAll.setPosSize(f_173_, f_163_, f_171_, 2.0F * f_163_);
        this.bAdd.setPosSize(f_173_, f_163_ + 2.0F * f_163_, f_171_, 2.0F * f_163_);
        this.bRemAll.setPosSize(f_173_, 2.0F * f_163_ + 4.0F * f_163_, f_171_, 2.0F * f_163_);
        this.bRem.setPosSize(f_173_, 2.0F * f_163_ + 6.0F * f_163_, f_171_, 2.0F * f_163_);
        this.bModifyPlane.setPosSize(f_173_, 2.0F * f_163_ + 10.0F * f_163_, f_171_, 2.0F * f_163_);
        float f_174_ = (f - f_171_ - 4.0F * f_163_) / 2.0F;
        float f_175_ = f_162_ - 6.0F * f_163_ - 2.0F * f_163_ - 3.0F * f_163_;
        this.lstAvailable.setPosSize(f_163_, f_163_, f_174_, f_175_);
        this.lstInReserve.setPosSize(f - f_163_ - f_174_, f_163_, f_174_, f_175_);
        gfont.size(" " + Plugin.i18n("bplace_cats") + " ", gsize);
        f_174_ = gsize.dx;
        float f_176_ = f_163_ + f_175_;
        this.lSeparate.setPosSize(2.0F * f_163_, f_176_, f_174_, 2.0F * f_163_);
        this.bSeparate.setPosSize(f_163_, f_176_ + f_163_, f - 2.0F * f_163_, f_162_ - f_176_ - 2.0F * f_163_);
        gfont.size(Plugin.i18n("bplace_country"), gsize);
        float f_177_ = gsize.dx;
        gfont.size(Plugin.i18n("bplace_year"), gsize);
        if (f_177_ < gsize.dx) f_177_ = gsize.dx;
        gfont.size(Plugin.i18n("bplace_category"), gsize);
        if (f_177_ < gsize.dx) f_177_ = gsize.dx;
        f_171_ = 2.0F * f_163_ + f_165_ + f_167_;
        f_174_ = f - f_177_ - f_171_ - 6.0F * f_163_;
        float f_178_ = gwindow.lAF().getComboH();
        this.lCountry.setPosSize(2.0F * f_163_, f_176_ + 2.0F * f_163_, f_177_, 2.0F * f_163_);
        this.cCountry.setPosSize(2.0F * f_163_ + f_177_ + f_163_, f_176_ + 2.0F * f_163_ + (2.0F * f_163_ - f_178_) / 2.0F, f_174_, f_178_);
        this.bCountryAdd.setPosSize(f - 4.0F * f_163_ - f_167_ - f_165_, f_176_ + 2.0F * f_163_, f_163_ + f_165_, 2.0F * f_163_);
        this.bCountryRem.setPosSize(f - 3.0F * f_163_ - f_167_, f_176_ + 2.0F * f_163_, f_163_ + f_167_, 2.0F * f_163_);
        this.lYear.setPosSize(2.0F * f_163_, f_176_ + 4.0F * f_163_, f_177_, 2.0F * f_163_);
        this.cYear.setPosSize(2.0F * f_163_ + f_177_ + f_163_, f_176_ + 4.0F * f_163_ + (2.0F * f_163_ - f_178_) / 2.0F, f_174_, f_178_);
        this.bYearAdd.setPosSize(f - 4.0F * f_163_ - f_167_ - f_165_, f_176_ + 4.0F * f_163_, f_163_ + f_165_, 2.0F * f_163_);
        this.bYearRem.setPosSize(f - 3.0F * f_163_ - f_167_, f_176_ + 4.0F * f_163_, f_163_ + f_167_, 2.0F * f_163_);
        this.lType.setPosSize(2.0F * f_163_, f_176_ + 6.0F * f_163_, f_177_, 2.0F * f_163_);
        this.cType.setPosSize(2.0F * f_163_ + f_177_ + f_163_, f_176_ + 6.0F * f_163_ + (2.0F * f_163_ - f_178_) / 2.0F, f_174_, f_178_);
        this.bTypeAdd.setPosSize(f - 4.0F * f_163_ - f_167_ - f_165_, f_176_ + 6.0F * f_163_, f_163_ + f_165_, 2.0F * f_163_);
        this.bTypeRem.setPosSize(f - 3.0F * f_163_ - f_167_, f_176_ + 6.0F * f_163_, f_163_ + f_167_, 2.0F * f_163_);
    }

    public void afterCreated() {
        super.afterCreated();

        this.resized();
        this.close(false);
    }

    // Override default close method
    public void close(boolean flag) {
        if (this.parentEditControl != null) {
            StringBuffer sb = new StringBuffer();
            if (this.lstAvailable.lst != null) for (int i = 0; i < this.lstAvailable.lst.size(); i++) {
                sb.append(this.lstAvailable.lst.get(i));
                sb.append(" ");
            }

            this.parentEditControl.setValue(sb.toString());
            this.parentEditControl.notify(2, 0);

            com.maddox.il2.builder.PlMission.setChanged();
        }

        super.close(flag);
    }

    public Zuti_WManageAircrafts() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 40.0F, 40.0F, true);
        this.bSizable = true;
    }

    public void setParentEditControl(GWindowEditControl inParentEditControl, boolean onlyFlyableAc) {
        this.parentEditControl = inParentEditControl;
        this.offerOnlyFlyableAircraft = onlyFlyableAc;

        if (inParentEditControl != null) this.fillTabAircraft();

        if (this.airNames == null) this.airNames = new ArrayList();
    }

    public void clearAirNames() {
        this.airNames.clear();
        this.airNames = null;
    }

    /**
     * Set title for this GUI.
     *
     * @param newTitle
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Add aircraft as available.
     *
     * @param zac
     */
    public void addAircraft(ZutiAircraft zac) {
        if (this.airNames == null) this.airNames = new ArrayList();

        if (!this.airNames.contains(zac)) this.airNames.add(zac);
    }

    /**
     * Returns list of available aircraft.
     *
     * @return
     */
    public List getAircraft() {
        return this.airNames;
    }

    public void printAcList() {
        System.out.println("AC list - after adding new AC:");
        for (int i = 0; i < this.airNames.size(); i++) {
            ZutiAircraft zac = (ZutiAircraft) this.airNames.get(i);
            System.out.println("  ZAC NAME=" + zac.getAcName() + ", NR=" + zac.getNumberOfAIrcraft());
        }
        System.out.println("===========================");
    }
}