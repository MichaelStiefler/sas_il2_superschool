package com.maddox.il2.builder;

import java.util.StringTokenizer;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiMDSVariables;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Radar extends GWindowFramed {
    private static String         OLD_MDS_ID  = "MDS";
    public static final String    SECTION_ID  = "MDSSection_Radar";
    public static final String    RED_SCOUTS  = "MDS_Scouts_Red";
    public static final String    BLUE_SCOUTS = "MDS_Scouts_Blue";

    private Zuti_WManageAircrafts zuti_manageAircrafts;

    private int                   zutiRadar_RefreshInterval;
    private boolean               zutiRadar_ShipsAsRadar;
    private int                   zutiRadar_ShipRadar_MaxRange;
    private int                   zutiRadar_ShipRadar_MinHeight;
    private int                   zutiRadar_ShipRadar_MaxHeight;
    private int                   zutiRadar_ShipSmallRadar_MaxRange;
    private int                   zutiRadar_ShipSmallRadar_MinHeight;
    private int                   zutiRadar_ShipSmallRadar_MaxHeight;
    private boolean               zutiRadar_ScoutsAsRadar;
    private int                   zutiRadar_ScoutRadar_MaxRange;
    private int                   zutiRadar_ScoutRadar_DeltaHeight;
    private String                zutiRadar_ScoutRadarType_Red;
    private String                zutiRadar_ScoutRadarType_Blue;
    private int                   zutiRadar_ScoutGroundObjects_Alpha;
    private boolean               zutiRadar_ScoutCompleteRecon;

    private GWindowCheckBox       wZutiRadar_IsRadarInAdvancedMode;
    private GWindowEditControl    wZutiRadar_RefreshInterval;
    private GWindowCheckBox       wZutiRadar_ShipsAsRadar;
    private GWindowEditControl    wZutiRadar_ShipRadar_MaxRange;
    private GWindowEditControl    wZutiRadar_ShipRadar_MinHeight;
    private GWindowEditControl    wZutiRadar_ShipRadar_MaxHeight;
    private GWindowEditControl    wZutiRadar_ShipSmallRadar_MaxRange;
    private GWindowEditControl    wZutiRadar_ShipSmallRadar_MinHeight;
    private GWindowEditControl    wZutiRadar_ShipSmallRadar_MaxHeight;
    private GWindowCheckBox       wZutiRadar_ScoutsAsRadar;
    private GWindowEditControl    wZutiRadar_ScoutRadar_MaxRange;
    private GWindowEditControl    wZutiRadar_ScoutRadar_DeltaHeight;
    private GWindowEditControl    wZutiRadar_ScoutRadarType_Red;
    private GWindowEditControl    wZutiRadar_ScoutRadarType_Blue;
    private GWindowComboControl   wZutiRadar_ScoutGroundObjects_Alpha;
    private GWindowCheckBox       wZutiRadar_ScoutCompleteRecon;

    private GWindowBoxSeparate    bSeparate_General;
    private GWindowLabel          lSeparate_General;
    private GWindowBoxSeparate    bSeparate_Ships;
    private GWindowLabel          lSeparate_Ships;
    private GWindowBoxSeparate    bSeparate_Scouts;
    private GWindowLabel          lSeparate_Scouts;

    public ZutiMDSSection_Radar() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 47.0F, 34.0F, true);
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
        this.title = Plugin.i18n("mds.section.radar");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;

        gwindowdialogclient.addLabel(this.lSeparate_General = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 4.0F, 1.6F, Plugin.i18n("mds.section.radar.general"), null));
        this.bSeparate_General = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 44.0F, 5.0F);
        this.bSeparate_General.exclude = this.lSeparate_General;
        // wZutiRadar_IsRadarInAdvancedMode
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 42.0F, 1.3F, Plugin.i18n("mds.radar.rangeLimitation"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_IsRadarInAdvancedMode = new GWindowCheckBox(gwindowdialogclient, 42.0F, 2.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (ZutiMDSSection_Radar.this.wZutiRadar_ShipsAsRadar.isChecked()) {
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipRadar_MaxRange.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipRadar_MinHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipRadar_MaxHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipSmallRadar_MaxRange.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipSmallRadar_MinHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipSmallRadar_MaxHeight.setEnable(this.isChecked());
                }

                if (ZutiMDSSection_Radar.this.wZutiRadar_ScoutsAsRadar.isChecked()) {
                    ZutiMDSSection_Radar.this.wZutiRadar_ScoutRadar_MaxRange.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ScoutRadar_DeltaHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ScoutGroundObjects_Alpha.setEnable(this.isChecked());
                }

                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_RefreshInterval
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 40.0F, 1.3F, Plugin.i18n("mds.radar.refresh"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 4.0F, 4.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_RefreshInterval = new GWindowEditControl(gwindowdialogclient, 39.0F, 4.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_Ships = new GWindowLabel(gwindowdialogclient, 3.0F, 7.5F, 3.0F, 1.6F, Plugin.i18n("mds.section.radar.ships"), null));
        this.bSeparate_Ships = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 8.0F, 44.0F, 9.0F);
        this.bSeparate_Ships.exclude = this.lSeparate_Ships;
        // wZutiRadar_ShipsAsRadar
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 9.0F, 42.0F, 1.3F, Plugin.i18n("mds.radar.shipsAsRadars"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ShipsAsRadar = new GWindowCheckBox(gwindowdialogclient, 42.0F, 9.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (ZutiMDSSection_Radar.this.wZutiRadar_IsRadarInAdvancedMode.isChecked()) {
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipRadar_MaxRange.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipRadar_MinHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipRadar_MaxHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipSmallRadar_MaxRange.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipSmallRadar_MinHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ShipSmallRadar_MaxHeight.setEnable(this.isChecked());
                }

                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShipRadar_MaxRange
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 11.0F, 17.0F, 1.3F, Plugin.i18n("mds.radar.bigShipMax"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 11.0F, 17.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ShipRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 15.0F, 11.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShipRadar_MinHeight
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 17.0F, 1.3F, Plugin.i18n("mds.radar.bigShipMin"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 13.0F, 17.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ShipRadar_MinHeight = new GWindowEditControl(gwindowdialogclient, 15.0F, 13.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShipRadar_MaxHeight
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 17.0F, 1.3F, Plugin.i18n("mds.radar.bigShipMaxH"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 15.0F, 17.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ShipRadar_MaxHeight = new GWindowEditControl(gwindowdialogclient, 15.0F, 15.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // Small ships settings
        // wZutiRadar_ShipSmallRadar_MaxRange
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 11.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.smallShipMax"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 11.0F, 14.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ShipSmallRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 39.0F, 11.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShipSmallRadar_MinHeight
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 13F, 14.0F, 1.3F, Plugin.i18n("mds.radar.smallShipMin"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 13.0F, 14.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ShipSmallRadar_MinHeight = new GWindowEditControl(gwindowdialogclient, 39.0F, 13.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShipSmallRadar_MaxHeight
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 15.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.smallShipMaxH"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 15.0F, 14.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ShipSmallRadar_MaxHeight = new GWindowEditControl(gwindowdialogclient, 39.0F, 15.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_Scouts = new GWindowLabel(gwindowdialogclient, 3.0F, 18.5F, 4.0F, 1.6F, Plugin.i18n("mds.section.radar.scouts"), null));
        this.bSeparate_Scouts = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 19.0F, 44.0F, 11.0F);
        this.bSeparate_Scouts.exclude = this.lSeparate_Scouts;
        // wZutiRadar_ScoutsAsRadar
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 20.0F, 42.0F, 1.3F, Plugin.i18n("mds.radar.scoutsAsRadars"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ScoutsAsRadar = new GWindowCheckBox(gwindowdialogclient, 42.0F, 20.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (ZutiMDSSection_Radar.this.wZutiRadar_IsRadarInAdvancedMode.isChecked()) {
                    ZutiMDSSection_Radar.this.wZutiRadar_ScoutRadar_MaxRange.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ScoutRadar_DeltaHeight.setEnable(this.isChecked());
                    ZutiMDSSection_Radar.this.wZutiRadar_ScoutGroundObjects_Alpha.setEnable(this.isChecked());
                }

                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ScoutRadar_MaxRange
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 22.0F, 13.0F, 1.3F, Plugin.i18n("mds.radar.scoutAcScanMax"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 22.0F, 17.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ScoutRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 15.0F, 22.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ScoutRadar_DeltaHeight
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 24F, 13.0F, 1.3F, Plugin.i18n("mds.radar.scoutAcDelta"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19.5F, 24.0F, 17.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ScoutRadar_DeltaHeight = new GWindowEditControl(gwindowdialogclient, 15.0F, 24.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });
        // wZutiRadar_ScoutGroundObjects_Alpha
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 22.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.scoutGroundAlpha"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 43.5F, 22.0F, 14.0F, 1.3F, Plugin.i18n("mds.degrees"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ScoutGroundObjects_Alpha = new GWindowComboControl(gwindowdialogclient, 39.0F, 22.0F, 4.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEnable(false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });
        this.wZutiRadar_ScoutGroundObjects_Alpha.setEditable(false);
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("30"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("35"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("40"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("45"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("50"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("55"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("60"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("65"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("70"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("75"));
        this.wZutiRadar_ScoutGroundObjects_Alpha.add(Plugin.i18n("80"));

        // wZutiRadar_ScoutCompleteRecon
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.0F, 24.0F, 14.0F, 1.3F, Plugin.i18n("mds.radar.scoutCmplRecon"), null));
        gwindowdialogclient.addControl(this.wZutiRadar_ScoutCompleteRecon = new GWindowCheckBox(gwindowdialogclient, 42.0F, 24.0F, null) {
            public void afterCreated() {
                super.afterCreated();
            }

            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // bZutiRadar_ScoutRadarType_Red
        gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 26.0F, 9.0F, 1.3F, Plugin.i18n("mds.radar.scoutRed"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                if (ZutiMDSSection_Radar.this.zuti_manageAircrafts == null) ZutiMDSSection_Radar.this.zuti_manageAircrafts = new Zuti_WManageAircrafts();

                if (ZutiMDSSection_Radar.this.zuti_manageAircrafts.isVisible()) {
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.hideWindow();
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.clearAirNames();
                } else {
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.setTitle(Plugin.i18n("mds.radar.scoutRedTitle"));
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.setParentEditControl(ZutiMDSSection_Radar.this.wZutiRadar_ScoutRadarType_Red, false);
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.enableAcModifications(false);
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.showWindow();
                }
                return true;
            }
        });

        // wZutiRadar_ScoutRadarType_Red
        gwindowdialogclient.addControl(this.wZutiRadar_ScoutRadarType_Red = new GWindowEditControl(gwindowdialogclient, 12.0F, 26.0F, 32.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bDelayedNotify = true;
                this.bCanEdit = false;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
                return false;
            }
        });

        // bZutiRadar_ScoutRadarType_Blue
        gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 28.0F, 9.0F, 1.3F, Plugin.i18n("mds.radar.scoutBlue"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                if (ZutiMDSSection_Radar.this.zuti_manageAircrafts == null) ZutiMDSSection_Radar.this.zuti_manageAircrafts = new Zuti_WManageAircrafts();

                if (ZutiMDSSection_Radar.this.zuti_manageAircrafts.isVisible()) {
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.hideWindow();
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.clearAirNames();
                } else {
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.setTitle(Plugin.i18n("mds.radar.scoutBlueTitle"));
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.setParentEditControl(ZutiMDSSection_Radar.this.wZutiRadar_ScoutRadarType_Blue, false);
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.enableAcModifications(false);
                    ZutiMDSSection_Radar.this.zuti_manageAircrafts.showWindow();
                }
                return true;
            }
        });
        // wZutiRadar_ScoutRadarType_Blue
        gwindowdialogclient.addControl(this.wZutiRadar_ScoutRadarType_Blue = new GWindowEditControl(gwindowdialogclient, 12.0F, 28.0F, 32.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bDelayedNotify = true;
                this.bCanEdit = false;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Radar.this.setMDSVariables();
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
        this.zutiRadar_ShipsAsRadar = false;
        this.zutiRadar_ShipRadar_MaxRange = 100;
        this.zutiRadar_ShipRadar_MinHeight = 100;
        this.zutiRadar_ShipRadar_MaxHeight = 5000;
        this.zutiRadar_ShipSmallRadar_MaxRange = 25;
        this.zutiRadar_ShipSmallRadar_MinHeight = 0;
        this.zutiRadar_ShipSmallRadar_MaxHeight = 2000;
        this.zutiRadar_ScoutsAsRadar = false;
        this.zutiRadar_ScoutRadar_MaxRange = 2;
        this.zutiRadar_ScoutRadar_DeltaHeight = 1500;
        this.zutiRadar_ScoutRadarType_Red = "";
        this.zutiRadar_ScoutRadarType_Blue = "";
        this.zutiRadar_RefreshInterval = 0;
        this.zutiRadar_ScoutGroundObjects_Alpha = 5;
        this.zutiRadar_ScoutCompleteRecon = false;
        ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiRadar_IsRadarInAdvancedMode.setChecked(ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE, false);
        this.wZutiRadar_RefreshInterval.setValue(new Integer(this.zutiRadar_RefreshInterval).toString(), false);
        this.wZutiRadar_ShipsAsRadar.setChecked(this.zutiRadar_ShipsAsRadar, false);
        this.wZutiRadar_ShipRadar_MaxRange.setValue(new Integer(this.zutiRadar_ShipRadar_MaxRange).toString(), false);
        this.wZutiRadar_ShipRadar_MinHeight.setValue(new Integer(this.zutiRadar_ShipRadar_MinHeight).toString(), false);
        this.wZutiRadar_ShipRadar_MaxHeight.setValue(new Integer(this.zutiRadar_ShipRadar_MaxHeight).toString(), false);
        this.wZutiRadar_ShipSmallRadar_MaxRange.setValue(new Integer(this.zutiRadar_ShipSmallRadar_MaxRange).toString(), false);
        this.wZutiRadar_ShipSmallRadar_MinHeight.setValue(new Integer(this.zutiRadar_ShipSmallRadar_MinHeight).toString(), false);
        this.wZutiRadar_ShipSmallRadar_MaxHeight.setValue(new Integer(this.zutiRadar_ShipSmallRadar_MaxHeight).toString(), false);
        this.wZutiRadar_ScoutsAsRadar.setChecked(this.zutiRadar_ScoutsAsRadar, false);
        this.wZutiRadar_ScoutRadar_MaxRange.setValue(new Integer(this.zutiRadar_ScoutRadar_MaxRange).toString(), false);
        this.wZutiRadar_ScoutRadar_DeltaHeight.setValue(new Integer(this.zutiRadar_ScoutRadar_DeltaHeight).toString(), false);
        this.wZutiRadar_ScoutRadarType_Red.setValue(this.zutiRadar_ScoutRadarType_Red, false);
        this.wZutiRadar_ScoutRadarType_Blue.setValue(this.zutiRadar_ScoutRadarType_Blue, false);
        this.wZutiRadar_ScoutGroundObjects_Alpha.setSelected(this.zutiRadar_ScoutGroundObjects_Alpha - 1, true, false);
        this.wZutiRadar_ScoutCompleteRecon.setChecked(this.zutiRadar_ScoutCompleteRecon, false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = this.wZutiRadar_IsRadarInAdvancedMode.isChecked();
        this.zutiRadar_RefreshInterval = Integer.parseInt(this.wZutiRadar_RefreshInterval.getValue());
        this.zutiRadar_ShipsAsRadar = this.wZutiRadar_ShipsAsRadar.isChecked();
        this.zutiRadar_ShipRadar_MaxRange = Integer.parseInt(this.wZutiRadar_ShipRadar_MaxRange.getValue());
        this.zutiRadar_ShipRadar_MinHeight = Integer.parseInt(this.wZutiRadar_ShipRadar_MinHeight.getValue());
        this.zutiRadar_ShipRadar_MaxHeight = Integer.parseInt(this.wZutiRadar_ShipRadar_MaxHeight.getValue());
        this.zutiRadar_ShipSmallRadar_MaxRange = Integer.parseInt(this.wZutiRadar_ShipSmallRadar_MaxRange.getValue());
        this.zutiRadar_ShipSmallRadar_MinHeight = Integer.parseInt(this.wZutiRadar_ShipSmallRadar_MinHeight.getValue());
        this.zutiRadar_ShipSmallRadar_MaxHeight = Integer.parseInt(this.wZutiRadar_ShipSmallRadar_MaxHeight.getValue());
        this.zutiRadar_ScoutsAsRadar = this.wZutiRadar_ScoutsAsRadar.isChecked();
        this.zutiRadar_ScoutRadar_MaxRange = Integer.parseInt(this.wZutiRadar_ScoutRadar_MaxRange.getValue());
        this.zutiRadar_ScoutRadar_DeltaHeight = Integer.parseInt(this.wZutiRadar_ScoutRadar_DeltaHeight.getValue());
        this.zutiRadar_ScoutRadarType_Red = this.wZutiRadar_ScoutRadarType_Red.getValue();
        this.zutiRadar_ScoutRadarType_Blue = this.wZutiRadar_ScoutRadarType_Blue.getValue();
        this.zutiRadar_ScoutGroundObjects_Alpha = this.wZutiRadar_ScoutGroundObjects_Alpha.getSelected() + 1;
        this.zutiRadar_ScoutCompleteRecon = this.wZutiRadar_ScoutCompleteRecon.isChecked();

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiRadar_ShipsAsRadar = false;
            if (sectfile.get(SECTION_ID, "ZutiRadar_ShipsAsRadar", 0, 0, 1) == 1) this.zutiRadar_ShipsAsRadar = true;
            this.zutiRadar_ShipRadar_MaxRange = sectfile.get(SECTION_ID, "ZutiRadar_ShipRadar_MaxRange", 100, 1, 99999);
            this.zutiRadar_ShipRadar_MinHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipRadar_MinHeight", 100, 0, 99999);
            this.zutiRadar_ShipRadar_MaxHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipRadar_MaxHeight", 5000, 1000, 99999);
            this.zutiRadar_ShipSmallRadar_MaxRange = sectfile.get(SECTION_ID, "ZutiRadar_ShipSmallRadar_MaxRange", 25, 1, 99999);
            this.zutiRadar_ShipSmallRadar_MinHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipSmallRadar_MinHeight", 0, 0, 99999);
            this.zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get(SECTION_ID, "ZutiRadar_ShipSmallRadar_MaxHeight", 2000, 1000, 99999);

            this.zutiRadar_ScoutsAsRadar = false;
            if (sectfile.get(SECTION_ID, "ZutiRadar_ScoutsAsRadar", 0, 0, 1) == 1) this.zutiRadar_ScoutsAsRadar = true;
            this.zutiRadar_ScoutRadar_MaxRange = sectfile.get(SECTION_ID, "ZutiRadar_ScoutRadar_MaxRange", 2, 1, 99999);
            this.zutiRadar_ScoutRadar_DeltaHeight = sectfile.get(SECTION_ID, "ZutiRadar_ScoutRadar_DeltaHeight", 1500, 100, 99999);

            this.zutiRadar_ScoutCompleteRecon = false;
            if (sectfile.get(SECTION_ID, "ZutiRadar_ScoutCompleteRecon", 0, 0, 1) == 1) this.zutiRadar_ScoutCompleteRecon = true;

            this.zutiRadar_ScoutGroundObjects_Alpha = sectfile.get(SECTION_ID, "ZutiRadar_ScoutGroundObjects_Alpha", 5, 1, 11);// , default, min, max)
            this.zutiRadar_RefreshInterval = sectfile.get(SECTION_ID, "ZutiRadar_RefreshInterval", 0, 0, 99999);

            this.zutiLoadScouts_Red(sectfile);
            this.zutiLoadScouts_Blue(sectfile);

            ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
            if (sectfile.get(SECTION_ID, "ZutiRadar_SetRadarToAdvanceMode", 0, 0, 1) == 1) ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = true;

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            this.zutiRadar_ShipsAsRadar = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipsAsRadar", 0, 0, 1) == 1) this.zutiRadar_ShipsAsRadar = true;
            this.zutiRadar_ShipRadar_MaxRange = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipRadar_MaxRange", 100, 1, 99999);
            this.zutiRadar_ShipRadar_MinHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipRadar_MinHeight", 100, 0, 99999);
            this.zutiRadar_ShipRadar_MaxHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipRadar_MaxHeight", 5000, 1000, 99999);
            this.zutiRadar_ShipSmallRadar_MaxRange = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipSmallRadar_MaxRange", 25, 1, 99999);
            this.zutiRadar_ShipSmallRadar_MinHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipSmallRadar_MinHeight", 0, 0, 99999);
            this.zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ShipSmallRadar_MaxHeight", 2000, 1000, 99999);

            this.zutiRadar_ScoutsAsRadar = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutsAsRadar", 0, 0, 1) == 1) this.zutiRadar_ScoutsAsRadar = true;
            this.zutiRadar_ScoutRadar_MaxRange = sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutRadar_MaxRange", 2, 1, 99999);
            this.zutiRadar_ScoutRadar_DeltaHeight = sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutRadar_DeltaHeight", 1500, 100, 99999);

            this.zutiRadar_ScoutCompleteRecon = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutCompleteRecon", 0, 0, 1) == 1) this.zutiRadar_ScoutCompleteRecon = true;

            this.zutiRadar_ScoutGroundObjects_Alpha = sectfile.get(OLD_MDS_ID, "ZutiRadar_ScoutGroundObjects_Alpha", 5, 1, 11);// , default, min, max)
            this.zutiRadar_RefreshInterval = sectfile.get(OLD_MDS_ID, "ZutiRadar_RefreshInterval", 0, 0, 99999);

            this.zutiLoadScouts_Red(sectfile);
            this.zutiLoadScouts_Blue(sectfile);

            ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_SetRadarToAdvanceMode", 0, 0, 1) == 1) ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE = true;
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

            sectfile.lineAdd(sectionIndex, "ZutiRadar_RefreshInterval", new Integer(this.zutiRadar_RefreshInterval).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipsAsRadar", ZutiSupportMethods.boolToInt(this.zutiRadar_ShipsAsRadar));
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipRadar_MaxRange", new Integer(this.zutiRadar_ShipRadar_MaxRange).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipRadar_MinHeight", new Integer(this.zutiRadar_ShipRadar_MinHeight).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipRadar_MaxHeight", new Integer(this.zutiRadar_ShipRadar_MaxHeight).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipSmallRadar_MaxRange", new Integer(this.zutiRadar_ShipSmallRadar_MaxRange).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipSmallRadar_MinHeight", new Integer(this.zutiRadar_ShipSmallRadar_MinHeight).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ShipSmallRadar_MaxHeight", new Integer(this.zutiRadar_ShipSmallRadar_MaxHeight).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutsAsRadar", ZutiSupportMethods.boolToInt(this.zutiRadar_ScoutsAsRadar));
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutRadar_MaxRange", new Integer(this.zutiRadar_ScoutRadar_MaxRange).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutRadar_DeltaHeight", new Integer(this.zutiRadar_ScoutRadar_DeltaHeight).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutGroundObjects_Alpha", new Integer(this.zutiRadar_ScoutGroundObjects_Alpha).toString());
            sectfile.lineAdd(sectionIndex, "ZutiRadar_ScoutCompleteRecon", ZutiSupportMethods.boolToInt(this.zutiRadar_ScoutCompleteRecon));
            sectfile.lineAdd(sectionIndex, "ZutiRadar_SetRadarToAdvanceMode", ZutiSupportMethods.boolToInt(ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE));

            this.zutiSaveScouts_Red(sectfile);
            this.zutiSaveScouts_Blue(sectfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void zutiSaveScouts_Red(SectFile sectfile) {
        if (this.zutiRadar_ScoutRadarType_Red != null && this.zutiRadar_ScoutRadarType_Red.trim().length() > 0) {
            int sectionId = sectfile.sectionAdd(RED_SCOUTS);

            StringTokenizer stringtokenizer = new StringTokenizer(this.zutiRadar_ScoutRadarType_Red);

            while (stringtokenizer.hasMoreTokens())
                sectfile.lineAdd(sectionId, stringtokenizer.nextToken());
        }
    }

    private void zutiSaveScouts_Blue(SectFile sectfile) {
        if (this.zutiRadar_ScoutRadarType_Blue != null && this.zutiRadar_ScoutRadarType_Blue.trim().length() > 0) {
            int sectionId = sectfile.sectionAdd(BLUE_SCOUTS);

            StringTokenizer stringtokenizer = new StringTokenizer(this.zutiRadar_ScoutRadarType_Blue);

            while (stringtokenizer.hasMoreTokens())
                sectfile.lineAdd(sectionId, stringtokenizer.nextToken());
        }
    }

    private void zutiLoadScouts_Red(SectFile sectfile) {
        int index = sectfile.sectionIndex(RED_SCOUTS);
        if (index > -1) {
            this.zutiRadar_ScoutRadarType_Red = "";

            int lines = sectfile.vars(index);
            for (int i = 0; i < lines; i++) {
                String line = sectfile.line(index, i);
                StringTokenizer stringtokenizer = new StringTokenizer(line);
                String acName = null;

                while (stringtokenizer.hasMoreTokens()) {
                    acName = stringtokenizer.nextToken();
                    break;
                }
                if (acName != null) {
                    acName = acName.intern();
                    Class var_class = (Class) Property.value(acName, "airClass", null);

                    if (var_class != null && Property.containsValue(var_class, "cockpitClass")) // Add this ac to modified table for this home base
                        this.zutiRadar_ScoutRadarType_Red += acName + " ";
                }
            }
        }
    }

    private void zutiLoadScouts_Blue(SectFile sectfile) {
        int index = sectfile.sectionIndex(BLUE_SCOUTS);
        if (index > -1) {
            this.zutiRadar_ScoutRadarType_Blue = "";

            int lines = sectfile.vars(index);
            for (int i = 0; i < lines; i++) {
                String line = sectfile.line(index, i);
                StringTokenizer stringtokenizer = new StringTokenizer(line);
                String acName = null;

                while (stringtokenizer.hasMoreTokens()) {
                    acName = stringtokenizer.nextToken();
                    break;
                }
                if (acName != null) {
                    acName = acName.intern();
                    Class var_class = (Class) Property.value(acName, "airClass", null);

                    if (var_class != null && Property.containsValue(var_class, "cockpitClass")) // Add this ac to modified table for this home base
                        this.zutiRadar_ScoutRadarType_Blue += acName + " ";
                }
            }
        }
    }

    /**
     * Are scout planes performing radar role?
     *
     * @return
     */
    public boolean areScoutsRadars() {
        return this.zutiRadar_ScoutsAsRadar;
    }
}