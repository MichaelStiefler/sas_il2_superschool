/*4.09m compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;
import java.util.List;

import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GSize;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.game.ZutiAircraft;

public class Zuti_WAircraftProperties extends GWindowFramed {
    public static final int     MAX_LOADOUTS = 9;
    private Table               lstSelectedLoadouts;
    private Table               lstAvailableLoadouts;

    private GWindowEditControl  wMaxAllowed;
    private GWindowComboControl wMaxFuelSelection;
    private GWindowButton       bAdd;
    private GWindowButton       bAddAll;
    private GWindowButton       bRemove;
    private GWindowButton       bRemoveAll;

    // Called from: PlMisBorn
    private ZutiAircraft zutiAircraft = null;

    class Table extends GWindowTable {
        public ArrayList lst = new ArrayList();

        public int countRows() {
            return this.lst != null ? this.lst.size() : 0;
        }

        public Object getValueAt(int i, int i_0_) {
            if (this.lst == null) return null;
            if (i < 0 || i >= this.lst.size()) return null;
            String string = (String) this.lst.get(i);
            return string;
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

    public Zuti_WAircraftProperties() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 45.0F, 30.0F, true);
        this.bSizable = true;
    }

    public void afterCreated() {
        super.afterCreated();

        this.close(false);
    }

    public void windowShown() {
        super.windowShown();

        if (this.lstSelectedLoadouts != null) this.lstSelectedLoadouts.resolutionChanged();
        if (this.lstAvailableLoadouts != null) this.lstAvailableLoadouts.resolutionChanged();
    }

    public void windowHidden() {
        super.windowHidden();
    }

    public void created() {
        this.bAlwaysOnTop = true;
        super.created();
        this.title = Plugin.i18n("mds.zLoadouts.title");
        this.clientWindow = this.create(new GWindowDialogClient() {
            public void resized() {
                super.resized();
                Zuti_WAircraftProperties.this.setSizes(this);
            }
        });
        com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient) this.clientWindow;

        // wMaxAllowed
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7.0F, 1.3F, Plugin.i18n("mds.zLoadouts.max"), null));
        gwindowdialogclient.addControl(this.wMaxAllowed = new GWindowEditControl(gwindowdialogclient, 10.0F, 1.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                this.setValue(new Integer(Zuti_WAircraftProperties.this.zutiAircraft.getNumberOfAIrcraft()).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                if (Zuti_WAircraftProperties.this.zutiAircraft != null) {
                    Zuti_WAircraftProperties.this.zutiAircraft.setNumberOfAircraft(Integer.parseInt(this.getValue()));
                    PlMission.setChanged();
                }
                return false;
            }
        });

        // wMaxFuelSelection
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 26.0F, 1.0F, 12.0F, 1.3F, Plugin.i18n("mds.zFuel.max"), null));
        gwindowdialogclient.addControl(this.wMaxFuelSelection = new GWindowComboControl(gwindowdialogclient, 38.0F, 1.0F, 5.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                this.setSelected(Zuti_WAircraftProperties.this.zutiAircraft.getMaxFuelSelection(), true, false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                if (Zuti_WAircraftProperties.this.zutiAircraft != null) {
                    Zuti_WAircraftProperties.this.zutiAircraft.setMaxFuelSelection(this.getSelected());
                    PlMission.setChanged();
                }
                return false;
            }
        });
        this.wMaxFuelSelection.add("10");
        this.wMaxFuelSelection.add("20");
        this.wMaxFuelSelection.add("30");
        this.wMaxFuelSelection.add("40");
        this.wMaxFuelSelection.add("50");
        this.wMaxFuelSelection.add("60");
        this.wMaxFuelSelection.add("70");
        this.wMaxFuelSelection.add("80");
        this.wMaxFuelSelection.add("90");
        this.wMaxFuelSelection.add("100");

        this.lstSelectedLoadouts = new Table(gwindowdialogclient, Plugin.i18n("mds.zLoadouts.selected"), 1.0F, 3.0F, 15.0F, 20.0F);
        this.lstAvailableLoadouts = new Table(gwindowdialogclient, Plugin.i18n("mds.zLoadouts.available"), 23.0F, 3.0F, 15.0F, 20.0F);

        gwindowdialogclient.addControl(this.bAddAll = new GWindowButton(gwindowdialogclient, 17.0F, 3.0F, 5.0F, 2.0F, Plugin.i18n("bplace_addall"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.clear();
                Zuti_WAircraftProperties.this.addAllWeaponOptions();
                // lstAvailable.lst.clear();

                PlMission.setChanged();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bAdd = new GWindowButton(gwindowdialogclient, 17.0F, 5.0F, 5.0F, 2.0F, Plugin.i18n("bplace_add"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                int i = Zuti_WAircraftProperties.this.lstAvailableLoadouts.selectRow;
                if (i < 0 || i >= Zuti_WAircraftProperties.this.lstAvailableLoadouts.lst.size()) return true;

                if (!Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.contains(Zuti_WAircraftProperties.this.lstAvailableLoadouts.lst.get(i)) && Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.size() < MAX_LOADOUTS) {
                    Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.add(Zuti_WAircraftProperties.this.lstAvailableLoadouts.lst.get(i));
                    Zuti_WAircraftProperties.this.lstAvailableLoadouts.lst.remove(i);
                }

                PlMission.setChanged();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bRemoveAll = new GWindowButton(gwindowdialogclient, 17.0F, 8.0F, 5.0F, 2.0F, Plugin.i18n("bplace_delall"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.clear();
                Zuti_WAircraftProperties.this.fillAvailableLoadouts();

                PlMission.setChanged();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bRemove = new GWindowButton(gwindowdialogclient, 17.0F, 10.0F, 5.0F, 2.0F, Plugin.i18n("bplace_del"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                int i = Zuti_WAircraftProperties.this.lstSelectedLoadouts.selectRow;
                if (i < 0 || i >= Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.size()) return true;

                if (!Zuti_WAircraftProperties.this.lstAvailableLoadouts.lst.contains(Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.get(i)))
                    Zuti_WAircraftProperties.this.lstAvailableLoadouts.lst.add(Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.get(i));
                Zuti_WAircraftProperties.this.lstSelectedLoadouts.lst.remove(i);

                PlMission.setChanged();
                return true;
            }
        });
    }

    private void setSizes(GWindow gwindow) {
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
        if (f_maxFont < f_add) f_maxFont = f_add;
        if (f_maxFont < f_delAll) f_maxFont = f_delAll;
        if (f_maxFont < f_del) f_maxFont = f_del;
        float f_171_ = f_SizeY + f_maxFont;
        f_maxFont += f_SizeY + 4.0F * f_SizeY + f_list_1 + 4.0F * f_SizeY + f_list_2 + 4.0F * f_SizeY;
        if (win_X < f_maxFont) win_X = f_maxFont;
        float f_172_ = 10.0F * f_SizeY + 10.0F * f_SizeY + 2.0F * f_SizeY;
        if (win_Y < f_172_) win_Y = f_172_;
        float f_173_ = (win_X - f_171_) / 2.0F;
        this.bAddAll.setPosSize(f_173_, f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
        this.bAdd.setPosSize(f_173_, f_SizeY + 2.0F * f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
        this.bRemoveAll.setPosSize(f_173_, 2.0F * f_SizeY + 4.0F * f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
        this.bRemove.setPosSize(f_173_, 2.0F * f_SizeY + 6.0F * f_SizeY + spaceFromTop, f_171_, 2.0F * f_SizeY);
        float f_174_ = (win_X - f_171_ - 4.0F * f_SizeY) / 2.0F;
        float f_175_ = win_Y - f_SizeY - spaceFromTop - 12.0F;
        this.lstAvailableLoadouts.setPosSize(win_X - f_SizeY - f_174_, f_SizeY + spaceFromTop, f_174_, f_175_);
        this.lstSelectedLoadouts.setPosSize(f_SizeY, f_SizeY + spaceFromTop, f_174_, f_175_);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Called from: PlMisBorn
    public void setAircraft(ZutiAircraft zac) {
        if (zac == null) return;

        System.out.println("EDITING ZAC NAME=" + zac.getAcName() + ", NR=" + zac.getNumberOfAIrcraft() + ", LDT=" + zac.getSelectedWeaponI18NNames());

        this.lstAvailableLoadouts.lst.clear();
        this.lstSelectedLoadouts.lst.clear();

        this.zutiAircraft = zac;

        this.assignSelectedWeaponsToTable(this.zutiAircraft.getSelectedWeaponI18NNames());
        this.wMaxAllowed.setValue(new Integer(this.zutiAircraft.getNumberOfAIrcraft()).toString());
        this.wMaxFuelSelection.setSelected(this.zutiAircraft.getMaxFuelSelection(), true, false);

        this.fillAvailableLoadouts();
        this.syncLists();
    }

    private void fillAvailableLoadouts() {
        this.lstAvailableLoadouts.lst.clear();
        ArrayList list = this.zutiAircraft.getWeaponI18NNames();
        if (list != null) for (int i = 0; i < list.size(); i++)
            this.lstAvailableLoadouts.lst.add(list.get(i));
    }

    private void assignSelectedWeaponsToTable(List list) {
        this.lstSelectedLoadouts.lst.clear();

        if (list == null) return;

        for (int i = 0; i < list.size(); i++)
            this.lstSelectedLoadouts.lst.add(list.get(i));
    }

    public void close(boolean flag) {
        super.close(flag);

        if (this.zutiAircraft != null) {
            this.zutiAircraft.setSelectedWeapons(this.lstSelectedLoadouts.lst);
            this.zutiAircraft.setNumberOfAircraft(new Integer(this.wMaxAllowed.getValue()).intValue());
        }
    }

    private void addAllWeaponOptions() {
        List remove = new ArrayList();
        for (int i = 0; i < this.lstAvailableLoadouts.lst.size(); i++) {
            if (!this.lstSelectedLoadouts.lst.contains(this.lstAvailableLoadouts.lst.get(i))) {
                this.lstSelectedLoadouts.lst.add(this.lstAvailableLoadouts.lst.get(i));
                remove.add(this.lstAvailableLoadouts.lst.get(i));
            }

            if (this.lstSelectedLoadouts.lst.size() >= MAX_LOADOUTS) break;
        }

        for (int i = 0; i < remove.size(); i++)
            this.lstAvailableLoadouts.lst.remove(remove.get(i));
    }

    private void syncLists() {
        for (int i = 0; i < this.lstSelectedLoadouts.lst.size(); i++)
            this.lstAvailableLoadouts.lst.remove(this.lstSelectedLoadouts.lst.get(i));
    }

    // Called from PlMisBorn
    public void setTitle(String newTitle) {
        this.title = Plugin.i18n("mds.zLoadouts.title") + " " + Plugin.i18n("mds.for") + " " + newTitle;
    }
}