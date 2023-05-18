/*4.09m compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;

import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GSize;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowTable;

public class Zuti_WHomeBaseCountries extends GWindowFramed {
    private Table         lstSelected;
    private Table         lstAvailable;

    private GWindowButton bAdd;
    private GWindowButton bAddAll;
    private GWindowButton bRemove;
    private GWindowButton bRemoveAll;

    private ArrayList     fullCountriesList = new ArrayList();
    private ActorBorn     selectedActorBorn;

    // Modes: 0=general country selection; 1=red captured countries; 2=blue captured countries
    private int mode = 0;

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

    public Zuti_WHomeBaseCountries() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 45.0F, 30.0F, true);
        this.bSizable = true;
    }

    public void afterCreated() {
        super.afterCreated();

        this.close(false);
    }

    public void windowShown() {
        super.windowShown();

        if (this.lstSelected != null) this.lstSelected.resolutionChanged();
        if (this.lstAvailable != null) this.lstAvailable.resolutionChanged();
    }

    public void windowHidden() {
        super.windowHidden();
    }

    public void created() {
        this.bAlwaysOnTop = true;
        super.created();
        this.title = Plugin.i18n("mds.zCountries.title");
        this.clientWindow = this.create(new GWindowDialogClient() {
            public void resized() {
                super.resized();
                Zuti_WHomeBaseCountries.this.setSizes(this);
            }
        });
        com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient) this.clientWindow;

        this.lstSelected = new Table(gwindowdialogclient, Plugin.i18n("mds.zCountries.selected"), 1.0F, 3.0F, 15.0F, 20.0F);
        this.lstAvailable = new Table(gwindowdialogclient, Plugin.i18n("mds.zCountries.available"), 23.0F, 3.0F, 15.0F, 20.0F);

        gwindowdialogclient.addControl(this.bAddAll = new GWindowButton(gwindowdialogclient, 17.0F, 3.0F, 5.0F, 2.0F, Plugin.i18n("bplace_addall"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                Zuti_WHomeBaseCountries.this.lstSelected.lst.clear();
                Zuti_WHomeBaseCountries.this.addAllCountries();
                Zuti_WHomeBaseCountries.this.lstAvailable.lst.clear();

                PlMission.setChanged();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bAdd = new GWindowButton(gwindowdialogclient, 17.0F, 5.0F, 5.0F, 2.0F, Plugin.i18n("bplace_add"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                int i = Zuti_WHomeBaseCountries.this.lstAvailable.selectRow;
                if (i < 0 || i >= Zuti_WHomeBaseCountries.this.lstAvailable.lst.size()) return true;

                if (!Zuti_WHomeBaseCountries.this.lstSelected.lst.contains(Zuti_WHomeBaseCountries.this.lstAvailable.lst.get(i))) {
                    Zuti_WHomeBaseCountries.this.lstSelected.lst.add(Zuti_WHomeBaseCountries.this.lstAvailable.lst.get(i));
                    Zuti_WHomeBaseCountries.this.lstAvailable.lst.remove(i);
                }

                PlMission.setChanged();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bRemoveAll = new GWindowButton(gwindowdialogclient, 17.0F, 8.0F, 5.0F, 2.0F, Plugin.i18n("bplace_delall"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                Zuti_WHomeBaseCountries.this.lstSelected.lst.clear();
                Zuti_WHomeBaseCountries.this.fillAvailableCountries();

                PlMission.setChanged();
                return true;
            }
        });
        gwindowdialogclient.addControl(this.bRemove = new GWindowButton(gwindowdialogclient, 17.0F, 10.0F, 5.0F, 2.0F, Plugin.i18n("bplace_del"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                int i = Zuti_WHomeBaseCountries.this.lstSelected.selectRow;
                if (i < 0 || i >= Zuti_WHomeBaseCountries.this.lstSelected.lst.size()) return true;

                if (!Zuti_WHomeBaseCountries.this.lstAvailable.lst.contains(Zuti_WHomeBaseCountries.this.lstSelected.lst.get(i))) Zuti_WHomeBaseCountries.this.lstAvailable.lst.add(Zuti_WHomeBaseCountries.this.lstSelected.lst.get(i));
                Zuti_WHomeBaseCountries.this.lstSelected.lst.remove(i);

                PlMission.setChanged();
                return true;
            }
        });
    }

    private void setSizes(GWindow gwindow) {
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
        this.bAddAll.setPosSize(f_173_, f_SizeY, f_171_, 2.0F * f_SizeY);
        this.bAdd.setPosSize(f_173_, f_SizeY + 2.0F * f_SizeY, f_171_, 2.0F * f_SizeY);
        this.bRemoveAll.setPosSize(f_173_, 2.0F * f_SizeY + 4.0F * f_SizeY, f_171_, 2.0F * f_SizeY);
        this.bRemove.setPosSize(f_173_, 2.0F * f_SizeY + 6.0F * f_SizeY, f_171_, 2.0F * f_SizeY);
        float f_174_ = (win_X - f_171_ - 4.0F * f_SizeY) / 2.0F;
        float f_175_ = win_Y - f_SizeY - 12.0F;
        this.lstAvailable.setPosSize(win_X - f_SizeY - f_174_, f_SizeY, f_174_, f_175_);
        this.lstSelected.setPosSize(f_SizeY, f_SizeY, f_174_, f_175_);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Called from: PlMisBorn
    // Called from: PlMisBorn
    public void setSelectedCountries(ActorBorn actorBorn) {
        this.lstSelected.lst.clear();
        this.lstAvailable.lst.clear();

        this.selectedActorBorn = actorBorn;

        if (this.fullCountriesList == null || this.fullCountriesList.size() < 1) this.fillCountries();

        this.fillAvailableCountries();

        switch (this.mode) {
            case 0: {
                if (this.selectedActorBorn.zutiHomeBaseCountries != null && this.selectedActorBorn.zutiHomeBaseCountries.size() > 0) // Transform IDs to names
                    for (int i = 0; i < this.selectedActorBorn.zutiHomeBaseCountries.size(); i++)
                    try {
                        String country = (String) this.selectedActorBorn.zutiHomeBaseCountries.get(i);
                        if (this.lstAvailable.lst.contains(country)) this.lstSelected.lst.add(country);
                    } catch (Exception ex) {}
                break;
            }
            case 1: {
                if (this.selectedActorBorn.zutiHomeBaseCapturedRedCountries != null && this.selectedActorBorn.zutiHomeBaseCapturedRedCountries.size() > 0) // Transform IDs to names
                    for (int i = 0; i < this.selectedActorBorn.zutiHomeBaseCapturedRedCountries.size(); i++)
                    try {
                        String country = (String) this.selectedActorBorn.zutiHomeBaseCapturedRedCountries.get(i);
                        if (this.lstAvailable.lst.contains(country)) this.lstSelected.lst.add(country);
                    } catch (Exception ex) {}
                break;
            }
            case 2: {
                if (this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries != null && this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries.size() > 0) // Transform IDs to names
                    for (int i = 0; i < this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries.size(); i++)
                    try {
                        String country = (String) this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries.get(i);
                        if (this.lstAvailable.lst.contains(country)) this.lstSelected.lst.add(country);
                    } catch (Exception ex) {}
                break;
            }
        }

        this.syncLists();
    }

    private void fillCountries() {
        java.util.ResourceBundle resCountry = java.util.ResourceBundle.getBundle("i18n/country", com.maddox.rts.RTSConf.cur.locale, com.maddox.rts.LDRres.loader());

        java.util.List list = com.maddox.il2.ai.Regiment.getAll();
        int k1 = list.size();
        for (int i = 0; i < k1; i++) {
            com.maddox.il2.ai.Regiment regiment = (com.maddox.il2.ai.Regiment) list.get(i);
            String branch = resCountry.getString(regiment.branch());
            if (!this.fullCountriesList.contains(branch)) this.fullCountriesList.add(branch);
        }
    }

    private void fillAvailableCountries() {
        for (int i = 0; i < this.fullCountriesList.size(); i++)
            this.lstAvailable.lst.add(this.fullCountriesList.get(i));
    }

    public void close(boolean flag) {
        super.close(flag);

        if (this.selectedActorBorn != null) // Save selection
            switch (this.mode) {
            case 0: {
                if (this.selectedActorBorn.zutiHomeBaseCountries == null) this.selectedActorBorn.zutiHomeBaseCountries = new ArrayList();

                this.selectedActorBorn.zutiHomeBaseCountries.clear();
                for (int i = 0; i < this.lstSelected.lst.size(); i++)
                    this.selectedActorBorn.zutiHomeBaseCountries.add(this.lstSelected.lst.get(i));

                break;
            }
            case 1: {
                if (this.selectedActorBorn.zutiHomeBaseCapturedRedCountries == null) this.selectedActorBorn.zutiHomeBaseCapturedRedCountries = new ArrayList();

                this.selectedActorBorn.zutiHomeBaseCapturedRedCountries.clear();
                for (int i = 0; i < this.lstSelected.lst.size(); i++)
                    this.selectedActorBorn.zutiHomeBaseCapturedRedCountries.add(this.lstSelected.lst.get(i));

                break;
            }
            case 2: {
                if (this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries == null) this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries = new ArrayList();

                this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries.clear();
                for (int i = 0; i < this.lstSelected.lst.size(); i++)
                    this.selectedActorBorn.zutiHomeBaseCapturedBlueCountries.add(this.lstSelected.lst.get(i));

                break;
            }
            }
    }

    private void addAllCountries() {
        for (int i = 0; i < this.lstAvailable.lst.size(); i++)
            if (!this.lstSelected.lst.contains(this.lstAvailable.lst.get(i))) this.lstSelected.lst.add(this.lstAvailable.lst.get(i));
    }

    private void syncLists() {
        for (int i = 0; i < this.lstSelected.lst.size(); i++)
            this.lstAvailable.lst.remove(this.lstSelected.lst.get(i));
    }

    // Called from PlMisBorn
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    // Called from: PlMisBorn
    public void clearArrays() {
        if (this.lstAvailable.lst != null) this.lstAvailable.lst.clear();
        if (this.lstSelected.lst != null) this.lstSelected.lst.clear();
    }

    // Called from: PlMisBorn
    public void setMode(int inMode) {
        this.mode = inMode;
    }
}