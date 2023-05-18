package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Miscellaneous extends GWindowFramed {
    private static String      OLD_MDS_ID = "MDS";
    public static final String SECTION_ID = "MDSSection_Miscellaneous";

    private boolean            zutiMisc_DisableAIRadioChatter;
    private boolean            zutiMisc_DespawnAIPlanesAfterLanding;
    private boolean            zutiMisc_HidePlayersCountOnHomeBase;
    private boolean            zutiMisc_EnableReflyOnlyIfBailedOrDied;
    private boolean            zutiMisc_DisableReflyForMissionDuration;
    private int                zutiMisc_ReflyKIADelay;
    private int                zutiMisc_MaxAllowedKIA;
    private float              zutiMisc_ReflyKIADelayMultiplier;
    private boolean            zutiMisc_EnableAIAirborneMulticrew;
    private boolean            zutiMisc_EnableInstructor;
    private boolean            zutiMisc_EnableTowerCommunications;
    private boolean            zutiMisc_DisableVectoring;

    private GWindowCheckBox    wZutiMisc_DisableAIRadioChatter;
    private GWindowCheckBox    wZutiMisc_DespawnAIPlanesAfterLanding;
    private GWindowCheckBox    wZutiMisc_HidePlayersCountOnHomeBase;
    private GWindowCheckBox    wZutiMisc_EnableReflyOnlyIfBailedOrDied;
    private GWindowCheckBox    wZutiMisc_DisableReflyForMissionDuration;
    private GWindowEditControl wZutiMisc_ReflyKIADelay;
    private GWindowEditControl wZutiMisc_MaxAllowedKIA;
    private GWindowEditControl wZutiMisc_ReflyKIADelayMultiplier;
    private GWindowCheckBox    wZutiMisc_EnableAIAirborneMulticrew;
    private GWindowCheckBox    wZutiMisc_EnableInstructor;
    private GWindowCheckBox    wZutiRadar_EnableTowerCommunications;
    private GWindowCheckBox    wZutiMisc_DisableVectoring;

    private GWindowBoxSeparate bSeparate_Misc;
    private GWindowLabel       lSeparate_Misc;
    private GWindowBoxSeparate bSeparate_Refly;
    private GWindowLabel       lSeparate_Refly;

    public ZutiMDSSection_Miscellaneous() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 37.0F, 32.5F, true);
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
        this.title = Plugin.i18n("mds.section.miscellaneous");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        gwindowdialogclient.addLabel(this.lSeparate_Misc = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 7.0F, 1.6F, Plugin.i18n("mds.misc"), null));
        this.bSeparate_Misc = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 34.0F, 15.0F);
        this.bSeparate_Misc.exclude = this.lSeparate_Misc;

        // wZutiRadar_EnableTowerCommunications
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.towerComms"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_EnableTowerCommunications = new GWindowCheckBox(gwindowdialogclient, 33.0F, 2.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_DisableAIRadioChatter
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.disableAI"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_DisableAIRadioChatter = new GWindowCheckBox(gwindowdialogclient, 33.0F, 4.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_DespawnAIPlanesAfterLanding
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.despawn"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_DespawnAIPlanesAfterLanding = new GWindowCheckBox(gwindowdialogclient, 33.0F, 6.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_HidePlayersCountOnHomeBase
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.hideHBNumbers"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_HidePlayersCountOnHomeBase = new GWindowCheckBox(gwindowdialogclient, 33.0F, 8.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_EnableAIAirborneMulticrew
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 10.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.enableACJoining"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_EnableAIAirborneMulticrew = new GWindowCheckBox(gwindowdialogclient, 33.0F, 10.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_EnableInstructor
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 12.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.enableInstructor"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_EnableInstructor = new GWindowCheckBox(gwindowdialogclient, 33.0F, 12.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_DisableVectoring
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 33.0F, 1.3F, Plugin.i18n("mds.misc.disableVectoring"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_DisableVectoring = new GWindowCheckBox(gwindowdialogclient, 33.0F, 14.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_Refly = new GWindowLabel(gwindowdialogclient, 3.0F, 17.5F, 3.0F, 1.6F, Plugin.i18n("mds.refly"), null));
        this.bSeparate_Refly = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 18.0F, 34.0F, 11.0F);
        this.bSeparate_Refly.exclude = this.lSeparate_Refly;

        // wZutiMisc_EnableReflyOnlyIfBailedOrDied
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 19.0F, 33.0F, 1.3F, Plugin.i18n("mds.refly.disable"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_EnableReflyOnlyIfBailedOrDied = new GWindowCheckBox(gwindowdialogclient, 33.0F, 19.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                ZutiMDSSection_Miscellaneous.this.wZutiMisc_ReflyKIADelay.setEnable(this.isChecked());
                ZutiMDSSection_Miscellaneous.this.wZutiMisc_ReflyKIADelayMultiplier.setEnable(this.isChecked());
                ZutiMDSSection_Miscellaneous.this.wZutiMisc_MaxAllowedKIA.setEnable(this.isChecked());

                // Disable static icons
                if (this.isChecked()) ZutiMDSSection_Miscellaneous.this.wZutiMisc_DisableReflyForMissionDuration.setChecked(false, false);

                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_ReflyKIADelay
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 21.0F, 33.0F, 1.3F, Plugin.i18n("mds.refly.delay"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 33.5F, 21.0F, 33.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_ReflyKIADelay = new GWindowEditControl(gwindowdialogclient, 29.0F, 21.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                this.setEnable(ZutiMDSSection_Miscellaneous.this.wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked());

                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });
        // wZutiMisc_ReflyKIADelayMultiplier
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 23.0F, 24.0F, 1.3F, Plugin.i18n("mds.refly.KIA"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_ReflyKIADelayMultiplier = new GWindowEditControl(gwindowdialogclient, 29.0F, 23.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                this.setEnable(ZutiMDSSection_Miscellaneous.this.wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked());

                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });
        // wZutiMisc_MaxAllowedKIA
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 25.0F, 24.0F, 1.3F, Plugin.i18n("mds.refly.limit"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_MaxAllowedKIA = new GWindowEditControl(gwindowdialogclient, 29.0F, 25.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                this.setEnable(ZutiMDSSection_Miscellaneous.this.wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked());

                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
                return false;
            }
        });

        // wZutiMisc_DisableReflyForMissionDuration
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 27.0F, 33.0F, 1.3F, Plugin.i18n("mds.refly.disableMission"), null));
        gwindowdialogclient.addControl(this.wZutiMisc_DisableReflyForMissionDuration = new GWindowCheckBox(gwindowdialogclient, 33.0F, 27.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                // Disable static icons
                if (this.isChecked()) ZutiMDSSection_Miscellaneous.this.wZutiMisc_EnableReflyOnlyIfBailedOrDied.setChecked(false, false);

                if (i != 2) return false;

                ZutiMDSSection_Miscellaneous.this.setMDSVariables();
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
        this.zutiMisc_DisableAIRadioChatter = false;
        this.zutiMisc_DespawnAIPlanesAfterLanding = true;
        this.zutiMisc_HidePlayersCountOnHomeBase = false;
        this.zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
        this.zutiMisc_DisableReflyForMissionDuration = false;
        this.zutiMisc_ReflyKIADelay = 15;
        this.zutiMisc_MaxAllowedKIA = 10;
        this.zutiMisc_ReflyKIADelayMultiplier = 2.0F;
        this.zutiMisc_EnableAIAirborneMulticrew = false;
        this.zutiMisc_EnableInstructor = false;
        this.zutiMisc_EnableTowerCommunications = true;
        this.zutiMisc_DisableVectoring = false;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiRadar_EnableTowerCommunications.setChecked(this.zutiMisc_EnableTowerCommunications, false);
        this.wZutiMisc_DisableAIRadioChatter.setChecked(this.zutiMisc_DisableAIRadioChatter, false);
        this.wZutiMisc_DespawnAIPlanesAfterLanding.setChecked(this.zutiMisc_DespawnAIPlanesAfterLanding, false);
        this.wZutiMisc_HidePlayersCountOnHomeBase.setChecked(this.zutiMisc_HidePlayersCountOnHomeBase, false);
        this.wZutiMisc_EnableReflyOnlyIfBailedOrDied.setChecked(this.zutiMisc_EnableReflyOnlyIfBailedOrDied, false);
        this.wZutiMisc_DisableReflyForMissionDuration.setChecked(this.zutiMisc_DisableReflyForMissionDuration, false);
        this.wZutiMisc_ReflyKIADelay.setValue(new Integer(this.zutiMisc_ReflyKIADelay).toString(), false);
        this.wZutiMisc_MaxAllowedKIA.setValue(new Integer(this.zutiMisc_MaxAllowedKIA).toString(), false);
        this.wZutiMisc_ReflyKIADelayMultiplier.setValue(new Float(this.zutiMisc_ReflyKIADelayMultiplier).toString(), false);
        this.wZutiMisc_EnableAIAirborneMulticrew.setChecked(this.zutiMisc_EnableAIAirborneMulticrew, false);
        this.wZutiMisc_EnableInstructor.setChecked(this.zutiMisc_EnableInstructor, false);
        this.wZutiMisc_DisableVectoring.setChecked(this.zutiMisc_DisableVectoring, false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiMisc_EnableTowerCommunications = this.wZutiRadar_EnableTowerCommunications.isChecked();
        this.zutiMisc_DisableAIRadioChatter = this.wZutiMisc_DisableAIRadioChatter.isChecked();
        this.zutiMisc_DespawnAIPlanesAfterLanding = this.wZutiMisc_DespawnAIPlanesAfterLanding.isChecked();
        this.zutiMisc_HidePlayersCountOnHomeBase = this.wZutiMisc_HidePlayersCountOnHomeBase.isChecked();
        this.zutiMisc_EnableReflyOnlyIfBailedOrDied = this.wZutiMisc_EnableReflyOnlyIfBailedOrDied.isChecked();
        this.zutiMisc_DisableReflyForMissionDuration = this.wZutiMisc_DisableReflyForMissionDuration.isChecked();
        this.zutiMisc_ReflyKIADelay = Integer.parseInt(this.wZutiMisc_ReflyKIADelay.getValue());
        this.zutiMisc_MaxAllowedKIA = Integer.parseInt(this.wZutiMisc_MaxAllowedKIA.getValue());
        this.zutiMisc_ReflyKIADelayMultiplier = Float.parseFloat(this.wZutiMisc_ReflyKIADelayMultiplier.getValue());
        this.zutiMisc_EnableAIAirborneMulticrew = this.wZutiMisc_EnableAIAirborneMulticrew.isChecked();
        this.zutiMisc_EnableInstructor = this.wZutiMisc_EnableInstructor.isChecked();
        this.zutiMisc_DisableVectoring = this.wZutiMisc_DisableVectoring.isChecked();

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiMisc_EnableTowerCommunications = true;
            if (sectfile.get(SECTION_ID, "ZutiMisc_EnableTowerCommunications", 1, 0, 1) == 0) this.zutiMisc_EnableTowerCommunications = false;
            this.zutiMisc_DisableAIRadioChatter = false;
            if (sectfile.get(SECTION_ID, "ZutiMisc_DisableAIRadioChatter", 0, 0, 1) == 1) this.zutiMisc_DisableAIRadioChatter = true;
            this.zutiMisc_DespawnAIPlanesAfterLanding = true;
            if (sectfile.get(SECTION_ID, "ZutiMisc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0) this.zutiMisc_DespawnAIPlanesAfterLanding = false;
            this.zutiMisc_HidePlayersCountOnHomeBase = false;
            if (sectfile.get(SECTION_ID, "ZutiMisc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1) this.zutiMisc_HidePlayersCountOnHomeBase = true;
            this.zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
            if (sectfile.get(SECTION_ID, "ZutiMisc_EnableReflyOnlyIfBailedOrDied", 0, 0, 1) == 1) this.zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
            this.zutiMisc_DisableReflyForMissionDuration = false;
            if (sectfile.get(SECTION_ID, "ZutiMisc_DisableReflyForMissionDuration", 0, 0, 1) == 1) this.zutiMisc_DisableReflyForMissionDuration = true;
            this.zutiMisc_ReflyKIADelay = sectfile.get(SECTION_ID, "ZutiMisc_ReflyKIADelay", 15, 0, 99999);
            this.zutiMisc_MaxAllowedKIA = sectfile.get(SECTION_ID, "ZutiMisc_MaxAllowedKIA", 10, 1, 99999);
            this.zutiMisc_ReflyKIADelayMultiplier = sectfile.get(SECTION_ID, "ZutiMisc_ReflyKIADelayMultiplier", 2.0F, 1.0F, 99999.0F);
            this.zutiMisc_EnableAIAirborneMulticrew = false;
            if (sectfile.get(SECTION_ID, "ZutiMisc_EnableAIAirborneMulticrew", 0, 0, 1) == 1) this.zutiMisc_EnableAIAirborneMulticrew = true;
            this.zutiMisc_EnableInstructor = false;
            if (sectfile.get(SECTION_ID, "ZutiMisc_EnableInstructor", 0, 0, 1) == 1) this.zutiMisc_EnableInstructor = true;
            this.zutiMisc_DisableVectoring = false;
            if (sectfile.get(SECTION_ID, "ZutiMisc_DisableVectoring", 0, 0, 1) == 1) this.zutiMisc_DisableVectoring = true;

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            this.zutiMisc_EnableTowerCommunications = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_EnableTowerCommunications", 1, 0, 1) == 0) this.zutiMisc_EnableTowerCommunications = false;
            this.zutiMisc_DisableAIRadioChatter = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_DisableAIRadioChatter", 0, 0, 1) == 1) this.zutiMisc_DisableAIRadioChatter = true;
            this.zutiMisc_DespawnAIPlanesAfterLanding = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0) this.zutiMisc_DespawnAIPlanesAfterLanding = false;
            this.zutiMisc_HidePlayersCountOnHomeBase = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1) this.zutiMisc_HidePlayersCountOnHomeBase = true;
            this.zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_EnableReflyOnlyIfBailedOrDied", 0, 0, 1) == 1) this.zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
            this.zutiMisc_DisableReflyForMissionDuration = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_DisableReflyForMissionDuration", 0, 0, 1) == 1) this.zutiMisc_DisableReflyForMissionDuration = true;
            this.zutiMisc_ReflyKIADelay = sectfile.get(OLD_MDS_ID, "ZutiMisc_ReflyKIADelay", 15, 0, 99999);
            this.zutiMisc_MaxAllowedKIA = sectfile.get(OLD_MDS_ID, "ZutiMisc_MaxAllowedKIA", 10, 1, 99999);
            this.zutiMisc_ReflyKIADelayMultiplier = sectfile.get(OLD_MDS_ID, "ZutiMisc_ReflyKIADelayMultiplier", 2.0F, 1.0F, 99999.0F);
            this.zutiMisc_EnableAIAirborneMulticrew = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_EnableAIAirborneMulticrew", 0, 0, 1) == 1) this.zutiMisc_EnableAIAirborneMulticrew = true;
            this.zutiMisc_EnableInstructor = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_EnableInstructor", 0, 0, 1) == 1) this.zutiMisc_EnableInstructor = true;
            this.zutiMisc_DisableVectoring = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiMisc_DisableVectoring", 0, 0, 1) == 1) this.zutiMisc_DisableVectoring = true;
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

            sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableTowerCommunications", ZutiSupportMethods.boolToInt(this.zutiMisc_EnableTowerCommunications));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_DisableAIRadioChatter", ZutiSupportMethods.boolToInt(this.zutiMisc_DisableAIRadioChatter));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_DespawnAIPlanesAfterLanding", ZutiSupportMethods.boolToInt(this.zutiMisc_DespawnAIPlanesAfterLanding));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_HidePlayersCountOnHomeBase", ZutiSupportMethods.boolToInt(this.zutiMisc_HidePlayersCountOnHomeBase));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableReflyOnlyIfBailedOrDied", ZutiSupportMethods.boolToInt(this.zutiMisc_EnableReflyOnlyIfBailedOrDied));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_DisableReflyForMissionDuration", ZutiSupportMethods.boolToInt(this.zutiMisc_DisableReflyForMissionDuration));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_ReflyKIADelay", new Integer(this.zutiMisc_ReflyKIADelay).toString());
            sectfile.lineAdd(sectionIndex, "ZutiMisc_MaxAllowedKIA", new Integer(this.zutiMisc_MaxAllowedKIA).toString());
            sectfile.lineAdd(sectionIndex, "ZutiMisc_ReflyKIADelayMultiplier", new Float(this.zutiMisc_ReflyKIADelayMultiplier).toString());
            sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableAIAirborneMulticrew", ZutiSupportMethods.boolToInt(this.zutiMisc_EnableAIAirborneMulticrew));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_EnableInstructor", ZutiSupportMethods.boolToInt(this.zutiMisc_EnableInstructor));
            sectfile.lineAdd(sectionIndex, "ZutiMisc_DisableVectoring", ZutiSupportMethods.boolToInt(this.zutiMisc_DisableVectoring));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}