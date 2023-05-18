package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_HUD extends GWindowFramed {
    private static String      OLD_MDS_ID = "MDS";
    public static final String SECTION_ID = "MDSSection_HUD";

    private boolean            zutiHud_DisableHudStatistics;
    private boolean            zutiHud_ShowPilotNumber;
    private boolean            zutiHud_ShowPilotPing;
    private boolean            zutiHud_ShowPilotName;
    private boolean            zutiHud_ShowPilotScore;
    private boolean            zutiHud_ShowPilotArmy;
    private boolean            zutiHud_ShowPilotACDesignation;
    private boolean            zutiHud_ShowPilotACType;

    private GWindowCheckBox    wZutiHud_DisableHudStatistics;
    private GWindowCheckBox    wZutiHud_ShowPilotNumber;
    private GWindowCheckBox    wZutiHud_ShowPilotPing;
    private GWindowCheckBox    wZutiHud_ShowPilotName;
    private GWindowCheckBox    wZutiHud_ShowPilotScore;
    private GWindowCheckBox    wZutiHud_ShowPilotArmy;
    private GWindowCheckBox    wZutiHud_ShowPilotACDesignation;
    private GWindowCheckBox    wZutiHud_ShowPilotACType;

    public ZutiMDSSection_HUD() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 21.0F, 20.0F, true);
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
        this.title = Plugin.i18n("mds.section.hud");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.disable"), null));
        gwindowdialogclient.addControl(this.wZutiHud_DisableHudStatistics = new GWindowCheckBox(gwindowdialogclient, 17.0F, 1.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                ZutiMDSSection_HUD.this.wZutiHud_ShowPilotNumber.setEnable(!this.isChecked());
                ZutiMDSSection_HUD.this.wZutiHud_ShowPilotPing.setEnable(!this.isChecked());
                ZutiMDSSection_HUD.this.wZutiHud_ShowPilotName.setEnable(!this.isChecked());
                ZutiMDSSection_HUD.this.wZutiHud_ShowPilotScore.setEnable(!this.isChecked());
                ZutiMDSSection_HUD.this.wZutiHud_ShowPilotArmy.setEnable(!this.isChecked());
                ZutiMDSSection_HUD.this.wZutiHud_ShowPilotACDesignation.setEnable(!this.isChecked());
                ZutiMDSSection_HUD.this.wZutiHud_ShowPilotACType.setEnable(!this.isChecked());

                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
                return false;
            }
        });
        // wZutiHud_ShowPilotNumber
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.number"), null));
        gwindowdialogclient.addControl(this.wZutiHud_ShowPilotNumber = new GWindowCheckBox(gwindowdialogclient, 17.0F, 3.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
                return false;
            }
        });
        // wZutiHud_ShowPilotPing
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.ping"), null));
        gwindowdialogclient.addControl(this.wZutiHud_ShowPilotPing = new GWindowCheckBox(gwindowdialogclient, 17.0F, 5.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
                return false;
            }
        });
        // wZutiHud_ShowPilotName
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.name"), null));
        gwindowdialogclient.addControl(this.wZutiHud_ShowPilotName = new GWindowCheckBox(gwindowdialogclient, 17.0F, 7.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
                return false;
            }
        });
        // wZutiHud_ShowPilotScore
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.score"), null));
        gwindowdialogclient.addControl(this.wZutiHud_ShowPilotScore = new GWindowCheckBox(gwindowdialogclient, 17.0F, 9.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
                return false;
            }
        });
        // wZutiHud_ShowPilotArmy
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.army"), null));
        gwindowdialogclient.addControl(this.wZutiHud_ShowPilotArmy = new GWindowCheckBox(gwindowdialogclient, 17.0F, 11.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
                return false;
            }
        });
        // wZutiHud_ShowPilotACDesignation
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.designation"), null));
        gwindowdialogclient.addControl(this.wZutiHud_ShowPilotACDesignation = new GWindowCheckBox(gwindowdialogclient, 17.0F, 13.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
                return false;
            }
        });
        // wZutiHud_ShowPilotACType
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 15.0F, 16.0F, 1.3F, Plugin.i18n("mds.hud.type"), null));
        gwindowdialogclient.addControl(this.wZutiHud_ShowPilotACType = new GWindowCheckBox(gwindowdialogclient, 17.0F, 15.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_HUD.this.setMDSVariables();
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
        this.zutiHud_DisableHudStatistics = false;
        this.zutiHud_ShowPilotNumber = false;
        this.zutiHud_ShowPilotPing = true;
        this.zutiHud_ShowPilotName = true;
        this.zutiHud_ShowPilotScore = true;
        this.zutiHud_ShowPilotArmy = true;
        this.zutiHud_ShowPilotACDesignation = false;
        this.zutiHud_ShowPilotACType = true;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiHud_DisableHudStatistics.setChecked(this.zutiHud_DisableHudStatistics, false);
        this.wZutiHud_ShowPilotNumber.setChecked(this.zutiHud_ShowPilotNumber, false);
        this.wZutiHud_ShowPilotPing.setChecked(this.zutiHud_ShowPilotPing, false);
        this.wZutiHud_ShowPilotName.setChecked(this.zutiHud_ShowPilotName, false);
        this.wZutiHud_ShowPilotScore.setChecked(this.zutiHud_ShowPilotScore, false);
        this.wZutiHud_ShowPilotArmy.setChecked(this.zutiHud_ShowPilotArmy, false);
        this.wZutiHud_ShowPilotACDesignation.setChecked(this.zutiHud_ShowPilotACDesignation, false);
        this.wZutiHud_ShowPilotACType.setChecked(this.zutiHud_ShowPilotACType, false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiHud_DisableHudStatistics = this.wZutiHud_DisableHudStatistics.isChecked();
        this.zutiHud_ShowPilotNumber = this.wZutiHud_ShowPilotNumber.isChecked();
        this.zutiHud_ShowPilotPing = this.wZutiHud_ShowPilotPing.isChecked();
        this.zutiHud_ShowPilotName = this.wZutiHud_ShowPilotName.isChecked();
        this.zutiHud_ShowPilotScore = this.wZutiHud_ShowPilotScore.isChecked();
        this.zutiHud_ShowPilotArmy = this.wZutiHud_ShowPilotArmy.isChecked();
        this.zutiHud_ShowPilotACDesignation = this.wZutiHud_ShowPilotACDesignation.isChecked();
        this.zutiHud_ShowPilotACType = this.wZutiHud_ShowPilotACType.isChecked();

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiHud_DisableHudStatistics = false;
            if (sectfile.get(SECTION_ID, "ZutiHud_DisableHudStatistics", 0, 0, 1) == 1) this.zutiHud_DisableHudStatistics = true;
            this.zutiHud_ShowPilotNumber = false;
            if (sectfile.get(SECTION_ID, "ZutiHud_ShowPilotNumber", 0, 0, 1) == 1) this.zutiHud_ShowPilotNumber = true;
            this.zutiHud_ShowPilotPing = true;
            if (sectfile.get(SECTION_ID, "ZutiHud_ShowPilotPing", 1, 0, 1) == 0) this.zutiHud_ShowPilotPing = false;
            this.zutiHud_ShowPilotName = true;
            if (sectfile.get(SECTION_ID, "ZutiHud_ShowPilotName", 1, 0, 1) == 0) this.zutiHud_ShowPilotName = false;
            this.zutiHud_ShowPilotScore = true;
            if (sectfile.get(SECTION_ID, "ZutiHud_ShowPilotScore", 1, 0, 1) == 0) this.zutiHud_ShowPilotScore = false;
            this.zutiHud_ShowPilotArmy = true;
            if (sectfile.get(SECTION_ID, "ZutiHud_ShowPilotArmy", 1, 0, 1) == 0) this.zutiHud_ShowPilotArmy = false;
            this.zutiHud_ShowPilotACDesignation = false;
            if (sectfile.get(SECTION_ID, "ZutiHud_ShowPilotACDesignation", 0, 0, 1) == 1) this.zutiHud_ShowPilotACDesignation = true;
            this.zutiHud_ShowPilotACType = true;
            if (sectfile.get(SECTION_ID, "ZutiHud_ShowPilotACType", 1, 0, 1) == 0) this.zutiHud_ShowPilotACType = false;

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            this.zutiHud_DisableHudStatistics = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_DisableHudStatistics", 0, 0, 1) == 1) this.zutiHud_DisableHudStatistics = true;
            this.zutiHud_ShowPilotNumber = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotNumber", 0, 0, 1) == 1) this.zutiHud_ShowPilotNumber = true;
            this.zutiHud_ShowPilotPing = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotPing", 1, 0, 1) == 0) this.zutiHud_ShowPilotPing = false;
            this.zutiHud_ShowPilotName = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotName", 1, 0, 1) == 0) this.zutiHud_ShowPilotName = false;
            this.zutiHud_ShowPilotScore = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotScore", 1, 0, 1) == 0) this.zutiHud_ShowPilotScore = false;
            this.zutiHud_ShowPilotArmy = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotArmy", 1, 0, 1) == 0) this.zutiHud_ShowPilotArmy = false;
            this.zutiHud_ShowPilotACDesignation = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotACDesignation", 0, 0, 1) == 1) this.zutiHud_ShowPilotACDesignation = true;
            this.zutiHud_ShowPilotACType = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiHud_ShowPilotACType", 1, 0, 1) == 0) this.zutiHud_ShowPilotACType = false;
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

            sectfile.lineAdd(sectionIndex, "ZutiHud_DisableHudStatistics", ZutiSupportMethods.boolToInt(this.zutiHud_DisableHudStatistics));
            sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotNumber", ZutiSupportMethods.boolToInt(this.zutiHud_ShowPilotNumber));
            sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotPing", ZutiSupportMethods.boolToInt(this.zutiHud_ShowPilotPing));
            sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotName", ZutiSupportMethods.boolToInt(this.zutiHud_ShowPilotName));
            sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotScore", ZutiSupportMethods.boolToInt(this.zutiHud_ShowPilotScore));
            sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotArmy", ZutiSupportMethods.boolToInt(this.zutiHud_ShowPilotArmy));
            sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotACDesignation", ZutiSupportMethods.boolToInt(this.zutiHud_ShowPilotACDesignation));
            sectfile.lineAdd(sectionIndex, "ZutiHud_ShowPilotACType", ZutiSupportMethods.boolToInt(this.zutiHud_ShowPilotACType));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}