package com.maddox.il2.builder;

import com.maddox.gwindow.GFileFilter;
import com.maddox.gwindow.GFileFilterName;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFileOpen;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_NextMission extends GWindowFramed {
    private static String      OLD_MDS_ID = "MDS";
    public static final String SECTION_ID = "MDSSection_NextMission";

    private String             zutiNextMission_RedWon;
    private String             zutiNextMission_BlueWon;
    private String             zutiNextMission_Difficulty;
    private int                zutiNextMission_LoadDelay;
    private boolean            zutiNextMission_Enable;

    private GWindowEditControl wZutiNextMission_RedWon;
    private GWindowEditControl wZutiNextMission_BlueWon;
    private GWindowEditControl wZutiNextMission_Difficulty;
    private GWindowEditControl wZutiNextMission_LoadDelay;
    private GWindowButton      bZutiNextMission_RedWon;
    private GWindowButton      bZutiNextMission_BlueWon;
    private GWindowButton      bZutiNextMission_Difficulty;
    private GWindowCheckBox    wZutiNextMission_Enable;

    public ZutiMDSSection_NextMission() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 44.0F, 14.0F, true);
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
        this.title = Plugin.i18n("mds.section.nextMission");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        // wZutiNextMission_Enable
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 43.0F, 1.3F, Plugin.i18n("mds.mission.enable"), null));
        gwindowdialogclient.addControl(this.wZutiNextMission_Enable = new GWindowCheckBox(gwindowdialogclient, 41.0F, 1.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                ZutiMDSSection_NextMission.this.bZutiNextMission_RedWon.setEnable(this.isChecked());
                ZutiMDSSection_NextMission.this.wZutiNextMission_RedWon.setEnable(this.isChecked());
                ZutiMDSSection_NextMission.this.bZutiNextMission_BlueWon.setEnable(this.isChecked());
                ZutiMDSSection_NextMission.this.wZutiNextMission_BlueWon.setEnable(this.isChecked());
                ZutiMDSSection_NextMission.this.bZutiNextMission_Difficulty.setEnable(this.isChecked());
                ZutiMDSSection_NextMission.this.wZutiNextMission_Difficulty.setEnable(this.isChecked());
                ZutiMDSSection_NextMission.this.wZutiNextMission_LoadDelay.setEnable(this.isChecked());

                if (i != 2) return false;

                ZutiMDSSection_NextMission.this.setMDSVariables();
                return false;
            }
        });

        // bZutiNextMission_RedWon
        gwindowdialogclient.addControl(this.bZutiNextMission_RedWon = new GWindowButton(gwindowdialogclient, 1.0F, 3.0F, 9.0F, 1.3F, Plugin.i18n("mds.mission.red"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                GWindowFileOpen gwindowfileopen = new GWindowFileOpen(Plugin.builder.clientWindow.root, true, Plugin.i18n("mds.mission.redTitle"), "missions",
                        new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                    public void result(String string) {
                        if (string != null) ZutiMDSSection_NextMission.this.wZutiNextMission_RedWon.setValue(string);
                    }
                };
                // Set current file as last assigned file
                gwindowfileopen.setSelectFile(ZutiMDSSection_NextMission.this.wZutiNextMission_RedWon.getValue());

                return true;
            }
        });
        // wZutiNextMission_RedWon
        gwindowdialogclient.addControl(this.wZutiNextMission_RedWon = new GWindowEditControl(gwindowdialogclient, 11.0F, 3.0F, 31.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bDelayedNotify = true;
                this.bCanEdit = false;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_NextMission.this.setMDSVariables();
                return false;
            }
        });

        // bZutiNextMission_BlueWon
        gwindowdialogclient.addControl(this.bZutiNextMission_BlueWon = new GWindowButton(gwindowdialogclient, 1.0F, 5.0F, 9.0F, 1.3F, Plugin.i18n("mds.mission.blue"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                GWindowFileOpen gwindowfileopen = new GWindowFileOpen(Plugin.builder.clientWindow.root, true, Plugin.i18n("mds.mission.blueTitle"), "missions",
                        new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                    public void result(String string) {
                        if (string != null) ZutiMDSSection_NextMission.this.wZutiNextMission_BlueWon.setValue(string);
                    }
                };
                // Set current file as last assigned file
                gwindowfileopen.setSelectFile(ZutiMDSSection_NextMission.this.wZutiNextMission_BlueWon.getValue());

                return true;
            }
        });
        // wZutiNextMission_BlueWon
        gwindowdialogclient.addControl(this.wZutiNextMission_BlueWon = new GWindowEditControl(gwindowdialogclient, 11.0F, 5.0F, 31.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bDelayedNotify = true;
                this.bCanEdit = false;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_NextMission.this.setMDSVariables();
                return false;
            }
        });

        // bZutiNextMission_Difficulty
        gwindowdialogclient.addControl(this.bZutiNextMission_Difficulty = new GWindowButton(gwindowdialogclient, 1.0F, 7.0F, 9.0F, 1.3F, Plugin.i18n("mds.mission.difficulty"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                GWindowFileOpen gwindowfileopen = new GWindowFileOpen(Plugin.builder.clientWindow.root, true, Plugin.i18n("mds.mission.diffTitle"), "missions",
                        new GFileFilter[] { new GFileFilterName("Difficulty Files", new String[] { "*.difficulty" }) }) {
                    public void result(String string) {
                        if (string != null) ZutiMDSSection_NextMission.this.wZutiNextMission_Difficulty.setValue(string);
                    }
                };
                // Set current file as last assigned file
                gwindowfileopen.setSelectFile(ZutiMDSSection_NextMission.this.wZutiNextMission_Difficulty.getValue());

                return true;
            }
        });
        // wZutiNextMission_Difficulty
        gwindowdialogclient.addControl(this.wZutiNextMission_Difficulty = new GWindowEditControl(gwindowdialogclient, 11.0F, 7.0F, 31.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bDelayedNotify = true;
                this.bCanEdit = false;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_NextMission.this.setMDSVariables();
                return false;
            }
        });

        // wZutiNextMission_LoadDelay
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, 43F, 1.3F, Plugin.i18n("mds.mission.delay"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 41.5F, 9.0F, 43F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiNextMission_LoadDelay = new GWindowEditControl(gwindowdialogclient, 36.0F, 9.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_NextMission.this.setMDSVariables();
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
        this.zutiNextMission_RedWon = "";
        this.zutiNextMission_BlueWon = "";
        this.zutiNextMission_Difficulty = "";
        this.zutiNextMission_LoadDelay = 60;

        this.zutiNextMission_Enable = false;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiNextMission_RedWon.setValue(this.zutiNextMission_RedWon, false);
        this.wZutiNextMission_BlueWon.setValue(this.zutiNextMission_BlueWon, false);
        this.wZutiNextMission_Difficulty.setValue(this.zutiNextMission_Difficulty, false);
        this.wZutiNextMission_LoadDelay.setValue(new Integer(this.zutiNextMission_LoadDelay).toString(), false);

        this.wZutiNextMission_Enable.setChecked(this.zutiNextMission_Enable, false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiNextMission_RedWon = this.wZutiNextMission_RedWon.getValue();
        this.zutiNextMission_BlueWon = this.wZutiNextMission_BlueWon.getValue();
        this.zutiNextMission_Difficulty = this.wZutiNextMission_Difficulty.getValue();
        this.zutiNextMission_LoadDelay = Integer.parseInt(this.wZutiNextMission_LoadDelay.getValue());
        this.zutiNextMission_Enable = this.wZutiNextMission_Enable.isChecked();

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiNextMission_RedWon = sectfile.get(SECTION_ID, "ZutiNextMission_RedWon", "");
            this.zutiNextMission_BlueWon = sectfile.get(SECTION_ID, "ZutiNextMission_BlueWon", "");
            this.zutiNextMission_Difficulty = sectfile.get(SECTION_ID, "ZutiNextMission_Difficulty", "");
            this.zutiNextMission_LoadDelay = sectfile.get(SECTION_ID, "ZutiNextMission_LoadDelay", 60, 60, 99999);
            this.zutiNextMission_Enable = false;
            if (sectfile.get(SECTION_ID, "ZutiNextMission_Enable", 0, 0, 1) == 1) this.zutiNextMission_Enable = true;

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            this.zutiNextMission_RedWon = sectfile.get(OLD_MDS_ID, "ZutiNextMission_RedWon", "");
            this.zutiNextMission_BlueWon = sectfile.get(OLD_MDS_ID, "ZutiNextMission_BlueWon", "");
            this.zutiNextMission_Difficulty = sectfile.get(OLD_MDS_ID, "ZutiNextMission_Difficulty", "");
            this.zutiNextMission_LoadDelay = sectfile.get(OLD_MDS_ID, "ZutiNextMission_LoadDelay", 60, 60, 99999);
            this.zutiNextMission_Enable = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiNextMission_Enable", 0, 0, 1) == 1) this.zutiNextMission_Enable = true;
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

            sectfile.lineAdd(sectionIndex, "ZutiNextMission_RedWon", this.zutiNextMission_RedWon);
            sectfile.lineAdd(sectionIndex, "ZutiNextMission_BlueWon", this.zutiNextMission_BlueWon);
            sectfile.lineAdd(sectionIndex, "ZutiNextMission_Difficulty", this.zutiNextMission_Difficulty);
            sectfile.lineAdd(sectionIndex, "ZutiNextMission_LoadDelay", new Integer(this.zutiNextMission_LoadDelay).toString());
            sectfile.lineAdd(sectionIndex, "ZutiNextMission_Enable", ZutiSupportMethods.boolToInt(this.zutiNextMission_Enable));

            sectfile.lineAdd(sectionIndex, "ZutiNextMission_Enable", ZutiSupportMethods.boolToInt(this.zutiNextMission_Enable));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}