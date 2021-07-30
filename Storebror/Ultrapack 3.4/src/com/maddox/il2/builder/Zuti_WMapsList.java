/*4.09m compatible*/
package com.maddox.il2.builder;

import java.util.ArrayList;

import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.builder.PlMapLoad.Land;

public class Zuti_WMapsList extends GWindowFramed {
    private Table         lstMaps;
    private GWindowButton bLoadMap;
    private PlMapLoad     plMapLoad;

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

    public Zuti_WMapsList() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 25.0F, 40.0F, true);
        this.bSizable = true;
    }

    public void afterCreated() {
        super.afterCreated();

        this.close(false);
    }

    public void windowShown() {
        super.windowShown();

        if (this.lstMaps != null) this.lstMaps.resolutionChanged();
    }

    public void windowHidden() {
        super.windowHidden();
    }

    public void created() {
        this.bAlwaysOnTop = true;
        super.created();
        this.title = Plugin.i18n("mds.loadMaps.windowTitle");
        this.clientWindow = this.create(new GWindowDialogClient() {
            public void resized() {
                super.resized();
                Zuti_WMapsList.this.setSizes(this);
            }
        });
        com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient) this.clientWindow;

        this.lstMaps = new Table(gwindowdialogclient, Plugin.i18n("mds.loadMaps.listTitle"), 1.0F, 3.0F, 15.0F, 20.0F);

        gwindowdialogclient.addControl(this.bLoadMap = new GWindowButton(gwindowdialogclient, 17.0F, 8.0F, 5.0F, 2.0F, Plugin.i18n("mds.loadMaps.loadMap"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                Zuti_WMapsList.this.loadMap();
                return true;
            }
        });
    }

    private void setSizes(GWindow gwindow) {
        float win_X = gwindow.win.dx;
        float win_Y = gwindow.win.dy;

        this.lstMaps.setPosSize(5, 5, win_X - 10, win_Y - 40);
        this.bLoadMap.setPosSize(5, win_Y - 25, win_X - 10, 20);
    }

    // ----------------------------------------------------------------------
    private void loadMap() {
        if (this.plMapLoad != null) this.plMapLoad.zutiExecute(this.lstMaps.selectRow);
    }

    /**
     * Close window.
     */
    public void close(boolean flag) {
        super.close(flag);
    }

    /**
     * Set window title
     *
     * @param newTitle
     */
    public void setTitle(String newTitle) {
        this.title = Plugin.i18n("mds.loadMaps.windowTitle") + " - " + newTitle;
    }

    /**
     * Clear maps list.
     */
    public void clearMaps() {
        if (this.lstMaps.lst != null) this.lstMaps.lst.clear();
    }

    /**
     * Load all know maps into a list.
     *
     * @param lands
     */
    public void loadMaps(ArrayList lands) {
        int size = lands.size();
        for (int i = 0; i < size; i++) {
            Land land = (Land) lands.get(i);
            this.lstMaps.lst.add(land.i18nName);
        }
    }

    /**
     * Set map loading plugin.
     *
     * @param mapLoad
     */
    public void setPlMapLoad(PlMapLoad mapLoad) {
        this.plMapLoad = mapLoad;
    }
}