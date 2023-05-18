package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_AirDrops extends GWindowFramed {
    private static String      OLD_MDS_ID = "MDS";
    public static final String SECTION_ID = "MDSSection_AirDrops";

    private boolean            zutiDrop_OverFriendlyHomeBase;
    private boolean            zutiDrop_OverEnemyHomeBase;
    private boolean            zutiDrop_OverNeutralHomeBase;
    private boolean            zutiDrop_OverFriendlyFrictionArea;
    private boolean            zutiDrop_OverEnemyFrictionArea;
    private boolean            zutiDrop_OverDestroyGroundArea;
    private boolean            zutiDrop_OverDefenceGroundArea;
    private int                zutiDrop_MinHeight;
    private int                zutiDrop_MaxHeight;

    private GWindowCheckBox    wZutiDrop_OverFriendlyHomeBase;
    private GWindowCheckBox    wZutiDrop_OverEnemyHomeBase;
    private GWindowCheckBox    wZutiDrop_OverNeutralHomeBase;
    private GWindowCheckBox    wZutiDrop_OverFriendlyFrictionArea;
    private GWindowCheckBox    wZutiDrop_OverEnemyFrictionArea;
    private GWindowCheckBox    wZutiDrop_OverDestroyGroundArea;
    private GWindowCheckBox    wZutiDrop_OverDefenceGroundArea;
    private GWindowEditControl wZutiDrop_MinHeight;
    private GWindowEditControl wZutiDrop_MaxHeight;

    private GWindowBoxSeparate bSeparate_Misc;
    private GWindowLabel       lSeparate_Misc;
    private GWindowBoxSeparate bSeparate_DropHeight;
    private GWindowLabel       lSeparate_DropHeight;

    public ZutiMDSSection_AirDrops() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 31.0F, 26.5F, true);
        this.bSizable = false;
    }

    public void afterCreated() {
        super.afterCreated();

        this.close(false);
    }

    public void windowShown() {
        this.setUIVariables();

        super.windowShown();
    }

    public void windowHidden() {
        super.windowHidden();
    }

    public void created() {
        this.initializeVariables();

        this.bAlwaysOnTop = true;
        super.created();
        this.title = Plugin.i18n("mds.section.airDrops");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        gwindowdialogclient.addLabel(this.lSeparate_Misc = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 5.0F, 1.6F, Plugin.i18n("mds.airdrops"), null));
        this.bSeparate_Misc = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 28.0F, 15.0F);
        this.bSeparate_Misc.exclude = this.lSeparate_Misc;

        // wZutiDrop_OverFriendlyHomeBase
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 27.0F, 1.3F, Plugin.i18n("mds.airdrops.friendlyHB"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_OverFriendlyHomeBase = new GWindowCheckBox(gwindowdialogclient, 27.0F, 2.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
        // wZutiDrop_OverEnemyHomeBase
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.enemyHB"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_OverEnemyHomeBase = new GWindowCheckBox(gwindowdialogclient, 27.0F, 4.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
        // wZutiDrop_OverNeutralHomeBase
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.neutralHB"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_OverNeutralHomeBase = new GWindowCheckBox(gwindowdialogclient, 27.0F, 6.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
        // wZutiDrop_OverFriendlyFrictionArea
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.friendlyFA"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_OverFriendlyFrictionArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 8.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
        // wZutiDrop_OverEnemyFrictionArea
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 10.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.enemyFA"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_OverEnemyFrictionArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 10.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
        // wZutiDrop_OverDestroyGroundArea
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 12.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.destroyGA"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_OverDestroyGroundArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 12.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
        // wZutiDrop_OverDefenceGroundArea
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 22.0F, 1.3F, Plugin.i18n("mds.airdrops.defenceGA"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_OverDefenceGroundArea = new GWindowCheckBox(gwindowdialogclient, 27.0F, 14.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_DropHeight = new GWindowLabel(gwindowdialogclient, 3.0F, 17.5F, 10.0F, 1.6F, Plugin.i18n("mds.airdrops.altitude"), null));
        this.bSeparate_DropHeight = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 18.0F, 28.0F, 5.0F);
        this.bSeparate_DropHeight.exclude = this.lSeparate_DropHeight;

        // wZutiDrop_MinHeight
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 19.0F, 18.0F, 1.3F, Plugin.i18n("mds.airdrops.min"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 27.0F, 19.0F, 4.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_MinHeight = new GWindowEditControl(gwindowdialogclient, 21.5F, 19.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
        // wZutiDrop_MaxHeight
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 21.0F, 18.0F, 1.3F, Plugin.i18n("mds.airdrops.max"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 27.0F, 21.0F, 4.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient.addControl(this.wZutiDrop_MaxHeight = new GWindowEditControl(gwindowdialogclient, 21.5F, 21.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_AirDrops.this.setMDSVariables();
                return false;
            }
        });
    }

    /**
     * Close this window.
     *
     * @param flag: Set to false if you want windows to disappear.
     */
    public void close(boolean flag) {
        super.close(flag);
    }

    /**
     * Initializes MDS variables (sets default values).
     */
    private void initializeVariables() {
        this.zutiDrop_OverFriendlyHomeBase = false;
        this.zutiDrop_OverEnemyHomeBase = false;
        this.zutiDrop_OverNeutralHomeBase = false;
        this.zutiDrop_OverFriendlyFrictionArea = false;
        this.zutiDrop_OverEnemyFrictionArea = true;
        this.zutiDrop_OverDestroyGroundArea = true;
        this.zutiDrop_OverDefenceGroundArea = true;
        this.zutiDrop_MinHeight = 0;
        this.zutiDrop_MaxHeight = 10000;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiDrop_OverFriendlyHomeBase.setChecked(this.zutiDrop_OverFriendlyHomeBase, false);
        this.wZutiDrop_OverEnemyHomeBase.setChecked(this.zutiDrop_OverEnemyHomeBase, false);
        this.wZutiDrop_OverNeutralHomeBase.setChecked(this.zutiDrop_OverNeutralHomeBase, false);
        this.wZutiDrop_OverFriendlyFrictionArea.setChecked(this.zutiDrop_OverFriendlyFrictionArea, false);
        this.wZutiDrop_OverEnemyFrictionArea.setChecked(this.zutiDrop_OverEnemyFrictionArea, false);
        this.wZutiDrop_OverDestroyGroundArea.setChecked(this.zutiDrop_OverDestroyGroundArea, false);
        this.wZutiDrop_OverDefenceGroundArea.setChecked(this.zutiDrop_OverDefenceGroundArea, false);
        this.wZutiDrop_MinHeight.setValue(new Integer(this.zutiDrop_MinHeight).toString(), false);
        this.wZutiDrop_MaxHeight.setValue(new Integer(this.zutiDrop_MaxHeight).toString(), false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiDrop_OverFriendlyHomeBase = this.wZutiDrop_OverFriendlyHomeBase.isChecked();
        this.zutiDrop_OverEnemyHomeBase = this.wZutiDrop_OverEnemyHomeBase.isChecked();
        this.zutiDrop_OverNeutralHomeBase = this.wZutiDrop_OverNeutralHomeBase.isChecked();
        this.zutiDrop_OverFriendlyFrictionArea = this.wZutiDrop_OverFriendlyFrictionArea.isChecked();
        this.zutiDrop_OverEnemyFrictionArea = this.wZutiDrop_OverEnemyFrictionArea.isChecked();
        this.zutiDrop_OverDestroyGroundArea = this.wZutiDrop_OverDestroyGroundArea.isChecked();
        this.zutiDrop_OverDefenceGroundArea = this.wZutiDrop_OverDefenceGroundArea.isChecked();
        this.zutiDrop_MinHeight = Integer.parseInt(this.wZutiDrop_MinHeight.getValue());
        this.zutiDrop_MaxHeight = Integer.parseInt(this.wZutiDrop_MaxHeight.getValue());

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiDrop_OverFriendlyHomeBase = false;
            if (sectfile.get(SECTION_ID, "ZutiDrop_OverFriendlyHomeBase", 0, 0, 1) == 1) this.zutiDrop_OverFriendlyHomeBase = true;
            this.zutiDrop_OverEnemyHomeBase = false;
            if (sectfile.get(SECTION_ID, "ZutiDrop_OverEnemyHomeBase", 0, 0, 1) == 1) this.zutiDrop_OverEnemyHomeBase = true;
            this.zutiDrop_OverNeutralHomeBase = false;
            if (sectfile.get(SECTION_ID, "ZutiDrop_OverNeutralHomeBase", 0, 0, 1) == 1) this.zutiDrop_OverNeutralHomeBase = true;
            this.zutiDrop_OverFriendlyFrictionArea = false;
            if (sectfile.get(SECTION_ID, "ZutiDrop_OverFriendlyFrictionArea", 0, 0, 1) == 1) this.zutiDrop_OverFriendlyFrictionArea = true;
            this.zutiDrop_OverEnemyFrictionArea = true;
            if (sectfile.get(SECTION_ID, "ZutiDrop_OverEnemyFrictionArea", 1, 0, 1) == 0) this.zutiDrop_OverEnemyFrictionArea = false;
            this.zutiDrop_OverDestroyGroundArea = true;
            if (sectfile.get(SECTION_ID, "ZutiDrop_OverDestroyGroundArea", 1, 0, 1) == 0) this.zutiDrop_OverDestroyGroundArea = false;
            this.zutiDrop_OverDefenceGroundArea = true;
            if (sectfile.get(SECTION_ID, "ZutiDrop_OverDefenceGroundArea", 1, 0, 1) == 0) this.zutiDrop_OverDefenceGroundArea = false;
            this.zutiDrop_MinHeight = sectfile.get(SECTION_ID, "ZutiDrop_MinHeight", 0, 0, 99999);
            this.zutiDrop_MaxHeight = sectfile.get(SECTION_ID, "ZutiDrop_MaxHeight", 10000, 0, 99999);

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            this.zutiDrop_OverFriendlyHomeBase = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiDrop_OverFriendlyHomeBase", 0, 0, 1) == 1) this.zutiDrop_OverFriendlyHomeBase = true;
            this.zutiDrop_OverEnemyHomeBase = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiDrop_OverEnemyHomeBase", 0, 0, 1) == 1) this.zutiDrop_OverEnemyHomeBase = true;
            this.zutiDrop_OverNeutralHomeBase = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiDrop_OverNeutralHomeBase", 0, 0, 1) == 1) this.zutiDrop_OverNeutralHomeBase = true;
            this.zutiDrop_OverFriendlyFrictionArea = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiDrop_OverFriendlyFrictionArea", 0, 0, 1) == 1) this.zutiDrop_OverFriendlyFrictionArea = true;
            this.zutiDrop_OverEnemyFrictionArea = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiDrop_OverEnemyFrictionArea", 1, 0, 1) == 0) this.zutiDrop_OverEnemyFrictionArea = false;
            this.zutiDrop_OverDestroyGroundArea = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiDrop_OverDestroyGroundArea", 1, 0, 1) == 0) this.zutiDrop_OverDestroyGroundArea = false;
            this.zutiDrop_OverDefenceGroundArea = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiDrop_OverDefenceGroundArea", 1, 0, 1) == 0) this.zutiDrop_OverDefenceGroundArea = false;
            this.zutiDrop_MinHeight = sectfile.get(OLD_MDS_ID, "ZutiDrop_MinHeight", 0, 0, 99999);
            this.zutiDrop_MaxHeight = sectfile.get(OLD_MDS_ID, "ZutiDrop_MaxHeight", 10000, 0, 99999);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stores variables to corresponding MDS section in mission file.
     *
     * @param sectfile
     */
    public void saveVariables(SectFile sectfile) {
        try {
            int sectionIndex = sectfile.sectionIndex(SECTION_ID);
            if (sectionIndex < 0) sectionIndex = sectfile.sectionAdd(SECTION_ID);

            sectfile.lineAdd(sectionIndex, "ZutiDrop_OverFriendlyHomeBase", ZutiSupportMethods.boolToInt(this.zutiDrop_OverFriendlyHomeBase));
            sectfile.lineAdd(sectionIndex, "ZutiDrop_OverEnemyHomeBase", ZutiSupportMethods.boolToInt(this.zutiDrop_OverEnemyHomeBase));
            sectfile.lineAdd(sectionIndex, "ZutiDrop_OverNeutralHomeBase", ZutiSupportMethods.boolToInt(this.zutiDrop_OverNeutralHomeBase));
            sectfile.lineAdd(sectionIndex, "ZutiDrop_OverFriendlyFrictionArea", ZutiSupportMethods.boolToInt(this.zutiDrop_OverFriendlyFrictionArea));
            sectfile.lineAdd(sectionIndex, "ZutiDrop_OverEnemyFrictionArea", ZutiSupportMethods.boolToInt(this.zutiDrop_OverEnemyFrictionArea));
            sectfile.lineAdd(sectionIndex, "ZutiDrop_OverDestroyGroundArea", ZutiSupportMethods.boolToInt(this.zutiDrop_OverDestroyGroundArea));
            sectfile.lineAdd(sectionIndex, "ZutiDrop_OverDefenceGroundArea", ZutiSupportMethods.boolToInt(this.zutiDrop_OverDefenceGroundArea));
            sectfile.lineAdd(sectionIndex, "ZutiDrop_MinHeight", new Integer(this.zutiDrop_MinHeight).toString());
            sectfile.lineAdd(sectionIndex, "ZutiDrop_MaxHeight", new Integer(this.zutiDrop_MaxHeight).toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}