package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Craters extends GWindowFramed {
    public static final String SECTION_ID = "MDSSection_Craters";

    private int                zutiCraters_Bombs250_Visibility;
    private int                zutiCraters_Bombs500_Visibility;
    private int                zutiCraters_Bombs1000_Visibility;
    private int                zutiCraters_Bombs2000_Visibility;
    private int                zutiCraters_Bombs5000_Visibility;
    private int                zutiCraters_Bombs9999_Visibility;

    private boolean            zutiCraters_Bombs250_SyncOnline;
    private boolean            zutiCraters_Bombs500_SyncOnline;
    private boolean            zutiCraters_Bombs1000_SyncOnline;
    private boolean            zutiCraters_Bombs2000_SyncOnline;
    private boolean            zutiCraters_Bombs5000_SyncOnline;
    private boolean            zutiCraters_Bombs9999_SyncOnline;
    private boolean            zutiCraters_OnlyAreaHits;

    private GWindowEditControl wZutiCraters_Bombs250_Visibility;
    private GWindowEditControl wZutiCraters_Bombs500_Visibility;
    private GWindowEditControl wZutiCraters_Bombs1000_Visibility;
    private GWindowEditControl wZutiCraters_Bombs2000_Visibility;
    private GWindowEditControl wZutiCraters_Bombs5000_Visibility;
    private GWindowEditControl wZutiCraters_Bombs9999_Visibility;

    private GWindowCheckBox    wZutiCraters_Bombs250_SyncOnline;
    private GWindowCheckBox    wZutiCraters_Bombs500_SyncOnline;
    private GWindowCheckBox    wZutiCraters_Bombs1000_SyncOnline;
    private GWindowCheckBox    wZutiCraters_Bombs2000_SyncOnline;
    private GWindowCheckBox    wZutiCraters_Bombs5000_SyncOnline;
    private GWindowCheckBox    wZutiCraters_Bombs9999_SyncOnline;
    private GWindowCheckBox    wZutiCraters_OnlyAreaHits;

    private GWindowBoxSeparate bSeparate_Craters;
    private GWindowLabel       lSeparate_Craters;

    public ZutiMDSSection_Craters() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 35.0F, 21.0F, true);
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
        this.title = Plugin.i18n("mds.section.craters");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        gwindowdialogclient.addLabel(this.lSeparate_Craters = new GWindowLabel(gwindowdialogclient, 3.0F, 2.5F, 13.0F, 1.6F, Plugin.i18n("mds.craters"), null));
        this.bSeparate_Craters = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 3.0F, 32.0F, 14.0F);
        this.bSeparate_Craters.exclude = this.lSeparate_Craters;

        // ------------------------------------------------------------------------------------------
        // wZutiCraters_OnlyAreaHits
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 1.0F, 30.0F, 1.3F, Plugin.i18n("mds.craters.areasOnly"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_OnlyAreaHits = new GWindowCheckBox(gwindowdialogclient, 31.0F, 1.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        // ------------------------------------------------------------------------------------------
        // wZutiMisc_Bombs250_Visibility
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 5.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.250"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs250_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 5.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 5.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));

        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 5.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs250_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 5.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        // ------------------------------------------------------------------------------------------
        // wZutiMisc_Bombs500_Visibility
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 7.0F, 17.0F, 1.3F, Plugin.i18n("mds.craters.500"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs500_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 7.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 7.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));

        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 7.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs500_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 7.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        // ------------------------------------------------------------------------------------------
        // wZutiMisc_Bombs1000_Visibility
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 9.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.1000"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs1000_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 9.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 9.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));

        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 9.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs1000_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 9.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        // ------------------------------------------------------------------------------------------
        // wZutiMisc_Bombs2000_Visibility
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 11.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.2000"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs2000_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 11.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 11.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));

        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 11.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs2000_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 11.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        // ------------------------------------------------------------------------------------------
        // wZutiMisc_Bombs5000_Visibility
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.5000"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs5000_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 13.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 13.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));

        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 13.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs5000_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 13.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        // ------------------------------------------------------------------------------------------
        // wZutiMisc_Bombs9999_Visibility
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 15.0F, 1.3F, Plugin.i18n("mds.craters.9999"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs9999_Visibility = new GWindowEditControl(gwindowdialogclient, 15.0F, 15.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 20.3F, 15.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));

        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 15.0F, 10.0F, 1.3F, Plugin.i18n("mds.craters.syncOnline"), null));
        gwindowdialogclient.addControl(this.wZutiCraters_Bombs9999_SyncOnline = new GWindowCheckBox(gwindowdialogclient, 31.0F, 15.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Craters.this.setMDSVariables();
                return false;
            }
        });
        // ------------------------------------------------------------------------------------------
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
        this.zutiCraters_Bombs250_Visibility = 80;
        this.zutiCraters_Bombs500_Visibility = 80;
        this.zutiCraters_Bombs1000_Visibility = 80;
        this.zutiCraters_Bombs2000_Visibility = 80;
        this.zutiCraters_Bombs5000_Visibility = 450;
        this.zutiCraters_Bombs9999_Visibility = 900;

        this.zutiCraters_Bombs250_SyncOnline = false;
        this.zutiCraters_Bombs500_SyncOnline = false;
        this.zutiCraters_Bombs1000_SyncOnline = false;
        this.zutiCraters_Bombs2000_SyncOnline = false;
        this.zutiCraters_Bombs5000_SyncOnline = false;
        this.zutiCraters_Bombs9999_SyncOnline = false;

        this.zutiCraters_OnlyAreaHits = true;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiCraters_Bombs250_Visibility.setValue(new Integer(this.zutiCraters_Bombs250_Visibility).toString(), false);
        this.wZutiCraters_Bombs500_Visibility.setValue(new Integer(this.zutiCraters_Bombs500_Visibility).toString(), false);
        this.wZutiCraters_Bombs1000_Visibility.setValue(new Integer(this.zutiCraters_Bombs1000_Visibility).toString(), false);
        this.wZutiCraters_Bombs2000_Visibility.setValue(new Integer(this.zutiCraters_Bombs2000_Visibility).toString(), false);
        this.wZutiCraters_Bombs5000_Visibility.setValue(new Integer(this.zutiCraters_Bombs5000_Visibility).toString(), false);
        this.wZutiCraters_Bombs9999_Visibility.setValue(new Integer(this.zutiCraters_Bombs9999_Visibility).toString(), false);

        this.wZutiCraters_Bombs250_SyncOnline.setChecked(this.zutiCraters_Bombs250_SyncOnline, false);
        this.wZutiCraters_Bombs500_SyncOnline.setChecked(this.zutiCraters_Bombs500_SyncOnline, false);
        this.wZutiCraters_Bombs1000_SyncOnline.setChecked(this.zutiCraters_Bombs1000_SyncOnline, false);
        this.wZutiCraters_Bombs2000_SyncOnline.setChecked(this.zutiCraters_Bombs2000_SyncOnline, false);
        this.wZutiCraters_Bombs5000_SyncOnline.setChecked(this.zutiCraters_Bombs5000_SyncOnline, false);
        this.wZutiCraters_Bombs9999_SyncOnline.setChecked(this.zutiCraters_Bombs9999_SyncOnline, false);

        this.wZutiCraters_OnlyAreaHits.setChecked(this.zutiCraters_OnlyAreaHits, false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiCraters_Bombs250_Visibility = Integer.parseInt(this.wZutiCraters_Bombs250_Visibility.getValue());
        this.zutiCraters_Bombs500_Visibility = Integer.parseInt(this.wZutiCraters_Bombs500_Visibility.getValue());
        this.zutiCraters_Bombs1000_Visibility = Integer.parseInt(this.wZutiCraters_Bombs1000_Visibility.getValue());
        this.zutiCraters_Bombs2000_Visibility = Integer.parseInt(this.wZutiCraters_Bombs2000_Visibility.getValue());
        this.zutiCraters_Bombs5000_Visibility = Integer.parseInt(this.wZutiCraters_Bombs5000_Visibility.getValue());
        this.zutiCraters_Bombs9999_Visibility = Integer.parseInt(this.wZutiCraters_Bombs9999_Visibility.getValue());

        this.zutiCraters_Bombs250_SyncOnline = this.wZutiCraters_Bombs250_SyncOnline.isChecked();
        this.zutiCraters_Bombs500_SyncOnline = this.wZutiCraters_Bombs500_SyncOnline.isChecked();
        this.zutiCraters_Bombs1000_SyncOnline = this.wZutiCraters_Bombs1000_SyncOnline.isChecked();
        this.zutiCraters_Bombs2000_SyncOnline = this.wZutiCraters_Bombs2000_SyncOnline.isChecked();
        this.zutiCraters_Bombs5000_SyncOnline = this.wZutiCraters_Bombs5000_SyncOnline.isChecked();
        this.zutiCraters_Bombs9999_SyncOnline = this.wZutiCraters_Bombs9999_SyncOnline.isChecked();

        this.zutiCraters_OnlyAreaHits = this.wZutiCraters_OnlyAreaHits.isChecked();

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiCraters_Bombs250_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs250_Visibility", 1, 1, 99999);
            this.zutiCraters_Bombs500_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs500_Visibility", 1, 1, 99999);
            this.zutiCraters_Bombs1000_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs1000_Visibility", 1, 1, 99999);
            this.zutiCraters_Bombs2000_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs2000_Visibility", 1, 1, 99999);
            this.zutiCraters_Bombs5000_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs5000_Visibility", 1, 1, 99999);
            this.zutiCraters_Bombs9999_Visibility = sectfile.get(SECTION_ID, "ZutiCraters_Bombs9999_Visibility", 1, 1, 99999);

            this.zutiCraters_Bombs250_SyncOnline = false;
            if (sectfile.get(SECTION_ID, "ZutiCraters_Bombs250_SyncOnline", 0, 0, 1) == 1) this.zutiCraters_Bombs250_SyncOnline = true;
            this.zutiCraters_Bombs500_SyncOnline = false;
            if (sectfile.get(SECTION_ID, "ZutiCraters_Bombs500_SyncOnline", 0, 0, 1) == 1) this.zutiCraters_Bombs500_SyncOnline = true;
            this.zutiCraters_Bombs1000_SyncOnline = false;
            if (sectfile.get(SECTION_ID, "ZutiCraters_Bombs1000_SyncOnline", 0, 0, 1) == 1) this.zutiCraters_Bombs1000_SyncOnline = true;
            this.zutiCraters_Bombs2000_SyncOnline = false;
            if (sectfile.get(SECTION_ID, "ZutiCraters_Bombs2000_SyncOnline", 0, 0, 1) == 1) this.zutiCraters_Bombs2000_SyncOnline = true;
            this.zutiCraters_Bombs5000_SyncOnline = false;
            if (sectfile.get(SECTION_ID, "ZutiCraters_Bombs5000_SyncOnline", 0, 0, 1) == 1) this.zutiCraters_Bombs5000_SyncOnline = true;
            this.zutiCraters_Bombs9999_SyncOnline = false;
            if (sectfile.get(SECTION_ID, "ZutiCraters_Bombs9999_SyncOnline", 0, 0, 1) == 1) this.zutiCraters_Bombs9999_SyncOnline = true;

            this.zutiCraters_OnlyAreaHits = true;
            if (sectfile.get(SECTION_ID, "ZutiCraters_OnlyAreaHits", 1, 0, 1) == 0) this.zutiCraters_OnlyAreaHits = false;

            this.setUIVariables();
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

            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs250_Visibility", new Float(this.zutiCraters_Bombs250_Visibility).toString());
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs500_Visibility", new Float(this.zutiCraters_Bombs500_Visibility).toString());
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs1000_Visibility", new Float(this.zutiCraters_Bombs1000_Visibility).toString());
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs2000_Visibility", new Float(this.zutiCraters_Bombs2000_Visibility).toString());
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs5000_Visibility", new Float(this.zutiCraters_Bombs5000_Visibility).toString());
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs9999_Visibility", new Float(this.zutiCraters_Bombs9999_Visibility).toString());

            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs250_SyncOnline", ZutiSupportMethods.boolToInt(this.zutiCraters_Bombs250_SyncOnline));
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs500_SyncOnline", ZutiSupportMethods.boolToInt(this.zutiCraters_Bombs500_SyncOnline));
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs1000_SyncOnline", ZutiSupportMethods.boolToInt(this.zutiCraters_Bombs1000_SyncOnline));
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs2000_SyncOnline", ZutiSupportMethods.boolToInt(this.zutiCraters_Bombs2000_SyncOnline));
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs5000_SyncOnline", ZutiSupportMethods.boolToInt(this.zutiCraters_Bombs5000_SyncOnline));
            sectfile.lineAdd(sectionIndex, "ZutiCraters_Bombs9999_SyncOnline", ZutiSupportMethods.boolToInt(this.zutiCraters_Bombs9999_SyncOnline));

            sectfile.lineAdd(sectionIndex, "ZutiCraters_OnlyAreaHits", ZutiSupportMethods.boolToInt(this.zutiCraters_OnlyAreaHits));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}