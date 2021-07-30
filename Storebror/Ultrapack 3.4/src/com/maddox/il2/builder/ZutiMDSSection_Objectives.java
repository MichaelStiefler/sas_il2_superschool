package com.maddox.il2.builder;

import java.util.ArrayList;

import com.maddox.gwindow.GCaption;
import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.builder.PlMisRocket.Rocket;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_Objectives extends GWindowFramed {
    private static String      OLD_MDS_ID            = "MDS_Objectives";
    public static final String SECTION_ID            = "MDSSection_Objectives";
    public static int          CHIELF_TYPE_INCREMENT = -1;

    private boolean            zutiObjectives_enable;
    private int                zutiObjectives_redShipsS;
    private int                zutiObjectives_redShipsM;
    private int                zutiObjectives_redArtillery;
    private int                zutiObjectives_redStationary;
    private int                zutiObjectives_redArmor;
    private int                zutiObjectives_redVehicles;
    private int                zutiObjectives_redTrains;
    private int                zutiObjectives_redAircraftsS;
    private int                zutiObjectives_redAircraftsM;
    private int                zutiObjectives_redRockets;
    private int                zutiObjectives_redHomeBases;
    private int                zutiobjectives_blueShipsS;
    private int                zutiObjectives_blueShipsM;
    private int                zutiobjectives_blueArtillery;
    private int                zutiObjectives_blueStationary;
    private int                zutiObjectives_blueArmor;
    private int                zutiObjectives_blueVehicles;
    private int                zutiObjectives_blueTrains;
    private int                zutiObjectives_blueAircraftsS;
    private int                zutiObjectives_blueAircraftsM;
    private int                zutiObjectives_blueRockets;
    private int                zutiObjectives_blueHomeBases;
    private int                zutiObjectives_redInfantry;
    private int                zutiObjectives_blueInfantry;

    private GWindowCheckBox    wZutiObjectives_enable;
    private GWindowEditControl wZutiObjectives_redShipsS;
    private GWindowEditControl wZutiObjectives_redShipsM;
    private GWindowEditControl wZutiObjectives_redArtillery;
    private GWindowEditControl wZutiObjectives_redStationary;
    private GWindowEditControl wZutiObjectives_redArmor;
    private GWindowEditControl wZutiObjectives_redVehicles;
    private GWindowEditControl wZutiObjectives_redTrains;
    private GWindowEditControl wZutiObjectives_redAircraftsS;
    private GWindowEditControl wZutiObjectives_redAircraftsM;
    private GWindowEditControl wZutiObjectives_redRockets;
    private GWindowEditControl wZutiObjectives_redHomeBases;
    private GWindowEditControl wZutiObjectives_blueShipsS;
    private GWindowEditControl wZutiObjectives_blueShipsM;
    private GWindowEditControl wZutiObjectives_blueArtillery;
    private GWindowEditControl wZutiObjectives_blueStationary;
    private GWindowEditControl wZutiObjectives_blueArmor;
    private GWindowEditControl wZutiObjectives_blueVehicles;
    private GWindowEditControl wZutiObjectives_blueTrains;
    private GWindowEditControl wZutiObjectives_blueAircraftsS;
    private GWindowEditControl wZutiObjectives_blueAircraftsM;
    private GWindowEditControl wZutiObjectives_blueRockets;
    private GWindowEditControl wZutiObjectives_blueHomeBases;
    private GWindowEditControl wZutiObjectives_redInfantry;
    private GWindowEditControl wZutiObjectives_blueInfantry;
    private GWindowLabel       lZutiObjectives_redMaxArmor;
    private GWindowLabel       lZutiObjectives_redMaxTrains;
    private GWindowLabel       lZutiObjectives_redMaxRockets;
    private GWindowLabel       lZutiObjectives_redMaxShipsS;
    private GWindowLabel       lZutiObjectives_redMaxShipsM;
    private GWindowLabel       lZutiObjectives_redMaxArtillery;
    private GWindowLabel       lZutiObjectives_redMaxVehicles;
    private GWindowLabel       lZutiObjectives_redMaxStationary;
    private GWindowLabel       lZutiObjectives_redMaxAircraftsS;
    private GWindowLabel       lZutiObjectives_redMaxAircraftsM;
    private GWindowLabel       lZutiObjectives_redMaxHomeBases;
    private GWindowLabel       lZutiObjectives_blueMaxArmor;
    private GWindowLabel       lZutiObjectives_blueMaxTrains;
    private GWindowLabel       lZutiObjectives_blueMaxRockets;
    private GWindowLabel       lZutiObjectives_blueMaxShipsS;
    private GWindowLabel       lZutiObjectives_blueMaxShipsM;
    private GWindowLabel       lZutiObjectives_blueMaxArtillery;
    private GWindowLabel       lZutiObjectives_blueMaxVehicles;
    private GWindowLabel       lZutiObjectives_blueMaxStationary;
    private GWindowLabel       lZutiObjectives_blueMaxAircraftsS;
    private GWindowLabel       lZutiObjectives_blueMaxAircraftsM;
    private GWindowLabel       lZutiObjectives_blueMaxHomeBases;
    private GWindowLabel       lZutiObjectives_redMaxInfantry;
    private GWindowLabel       lZutiObjectives_blueMaxInfantry;

    private GWindowBoxSeparate bSeparate_redObjectives;
    private GWindowLabel       lSeparate_redObjectives;
    private GWindowBoxSeparate bSeparate_blueObjectives;
    private GWindowLabel       lSeparate_blueObjectives;

    public ZutiMDSSection_Objectives() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 46.0F, 35.0F, true);
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
        this.title = Plugin.i18n("mds.section.objectives");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 45.0F, 1.3F, Plugin.i18n("mds.objectives.enable"), null));
        gwindowdialogclient.addControl(this.wZutiObjectives_enable = new GWindowCheckBox(gwindowdialogclient, 43.0F, 1.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                // Disable static icons
                if (this.isChecked()) {
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redShipsS.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redShipsM.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redArtillery.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redStationary.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redArmor.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redVehicles.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redTrains.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redAircraftsS.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redAircraftsM.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redRockets.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redInfantry.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redHomeBases.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueShipsS.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueShipsM.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueArtillery.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueStationary.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueArmor.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueVehicles.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueTrains.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueAircraftsS.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueAircraftsM.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueRockets.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueInfantry.setEnable(true);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueHomeBases.setEnable(true);
                } else {
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redShipsS.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redShipsM.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redArtillery.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redStationary.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redArmor.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redVehicles.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redTrains.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redAircraftsS.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redAircraftsM.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redRockets.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redInfantry.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_redHomeBases.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueShipsS.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueShipsM.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueArtillery.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueStationary.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueArmor.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueVehicles.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueTrains.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueAircraftsS.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueAircraftsM.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueRockets.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueInfantry.setEnable(false);
                    ZutiMDSSection_Objectives.this.wZutiObjectives_blueHomeBases.setEnable(false);
                }

                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_redObjectives = new GWindowLabel(gwindowdialogclient, 3.0F, 3.5F, 13.0F, 1.6F, Plugin.i18n("mds.objectives.redSection"), null));
        this.bSeparate_redObjectives = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 4.0F, 21.0F, 25.0F);
        this.bSeparate_redObjectives.exclude = this.lSeparate_redObjectives;

        // wZutiTargets_redArmor
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 5.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.armor"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxArmor = new GWindowLabel(gwindowdialogclient, 18.0F, 5F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redArmor = new GWindowEditControl(gwindowdialogclient, 14.0F, 5.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redTrains
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 7.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.trains"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxTrains = new GWindowLabel(gwindowdialogclient, 18.0F, 7F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redTrains = new GWindowEditControl(gwindowdialogclient, 14.0F, 7.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redRockets
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 9.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.rockets"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxRockets = new GWindowLabel(gwindowdialogclient, 18.0F, 9F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redRockets = new GWindowEditControl(gwindowdialogclient, 14.0F, 9.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redVehicles
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 11.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.vehicles"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxVehicles = new GWindowLabel(gwindowdialogclient, 18.0F, 11F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redVehicles = new GWindowEditControl(gwindowdialogclient, 14.0F, 11.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redShipsM
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsM"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxShipsM = new GWindowLabel(gwindowdialogclient, 18.0F, 13F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redShipsM = new GWindowEditControl(gwindowdialogclient, 14.0F, 13.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redAircraftsM
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsM"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxAircraftsM = new GWindowLabel(gwindowdialogclient, 18.0F, 15F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redAircraftsM = new GWindowEditControl(gwindowdialogclient, 14.0F, 15.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redShipsS
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 17.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsS"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxShipsS = new GWindowLabel(gwindowdialogclient, 18.0F, 17F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redShipsS = new GWindowEditControl(gwindowdialogclient, 14.0F, 17.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redAircraftsS
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 19.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsS"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxAircraftsS = new GWindowLabel(gwindowdialogclient, 18.0F, 19F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redAircraftsS = new GWindowEditControl(gwindowdialogclient, 14.0F, 19.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redArtillery
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 21.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.artillery"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxArtillery = new GWindowLabel(gwindowdialogclient, 18.0F, 21F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redArtillery = new GWindowEditControl(gwindowdialogclient, 14.0F, 21.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_redStationary
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 23.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.stationary"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxStationary = new GWindowLabel(gwindowdialogclient, 18.0F, 23F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redStationary = new GWindowEditControl(gwindowdialogclient, 14.0F, 23.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiObjectives_redInfantry
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 25.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.infantry"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxInfantry = new GWindowLabel(gwindowdialogclient, 18.0F, 25F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redInfantry = new GWindowEditControl(gwindowdialogclient, 14.0F, 25.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiObjectives_redHomeBases
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 27.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.homebases"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_blueMaxHomeBases = new GWindowLabel(gwindowdialogclient, 18.0F, 27F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_redHomeBases = new GWindowEditControl(gwindowdialogclient, 14.0F, 27.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_blueObjectives = new GWindowLabel(gwindowdialogclient, 25.0F, 3.5F, 11.0F, 1.6F, Plugin.i18n("mds.objectives.blueSection"), null));
        this.bSeparate_blueObjectives = new GWindowBoxSeparate(gwindowdialogclient, 23.0F, 4.0F, 21.0F, 25.0F);
        this.bSeparate_blueObjectives.exclude = this.lSeparate_blueObjectives;

        // wZutiTargets_blueArmor
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 5.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.armor"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxArmor = new GWindowLabel(gwindowdialogclient, 40.0F, 5.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueArmor = new GWindowEditControl(gwindowdialogclient, 36.0F, 5.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueTrains
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 7.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.trains"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxTrains = new GWindowLabel(gwindowdialogclient, 40.0F, 7.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueTrains = new GWindowEditControl(gwindowdialogclient, 36.0F, 7.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueRockets
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 9.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.vehicles"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxRockets = new GWindowLabel(gwindowdialogclient, 40.0F, 9.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueRockets = new GWindowEditControl(gwindowdialogclient, 36.0F, 9.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueVehicles
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 11.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.vehicles"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxVehicles = new GWindowLabel(gwindowdialogclient, 40.0F, 11.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueVehicles = new GWindowEditControl(gwindowdialogclient, 36.0F, 11.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueShipsM
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 13.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsM"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxShipsM = new GWindowLabel(gwindowdialogclient, 40.0F, 13.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueShipsM = new GWindowEditControl(gwindowdialogclient, 36.0F, 13.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueAircraftsM
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 15.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsM"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxAircraftsM = new GWindowLabel(gwindowdialogclient, 40.0F, 15.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueAircraftsM = new GWindowEditControl(gwindowdialogclient, 36.0F, 15.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueShipsS
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 17.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.shipsS"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxShipsS = new GWindowLabel(gwindowdialogclient, 40.0F, 17.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueShipsS = new GWindowEditControl(gwindowdialogclient, 36.0F, 17.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueAircraftsS
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 19.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.aircraftsS"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxAircraftsS = new GWindowLabel(gwindowdialogclient, 40.0F, 19.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueAircraftsS = new GWindowEditControl(gwindowdialogclient, 36.0F, 19.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueArtillery
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 21.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.artillery"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxArtillery = new GWindowLabel(gwindowdialogclient, 40.0F, 21.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueArtillery = new GWindowEditControl(gwindowdialogclient, 36.0F, 21.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_blueStationary
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 23.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.stationary"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxStationary = new GWindowLabel(gwindowdialogclient, 40.0F, 23.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueStationary = new GWindowEditControl(gwindowdialogclient, 36.0F, 23.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiObjectives_blueInfantry
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 25.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.infantry"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxInfantry = new GWindowLabel(gwindowdialogclient, 40.0F, 25.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueInfantry = new GWindowEditControl(gwindowdialogclient, 36.0F, 25.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // wZutiObjectives_blueHomeBases
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24.0F, 27.0F, 24.0F, 1.3F, Plugin.i18n("mds.objectives.homebases"), null));
        gwindowdialogclient.addLabel(this.lZutiObjectives_redMaxHomeBases = new GWindowLabel(gwindowdialogclient, 40.0F, 27.0F, 11.0F, 1.6F, "[MAX]", null));
        gwindowdialogclient.addControl(this.wZutiObjectives_blueHomeBases = new GWindowEditControl(gwindowdialogclient, 36.0F, 27.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_Objectives.this.setMDSVariables();
                return false;
            }
        });

        // bZutiObjectives_RefreshMax
        gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 1.0F, 30.0F, 43.0F, 2.0F, Plugin.i18n("mds.objectives.refreshMax"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                // List all actors
                ZutiMDSSection_Objectives.this.zutiListAllActors();

                return true;
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
        this.zutiObjectives_enable = false;
        this.zutiObjectives_redShipsS = 0;
        this.zutiObjectives_redShipsM = 0;
        this.zutiObjectives_redArtillery = 0;
        this.zutiObjectives_redStationary = 0;
        this.zutiObjectives_redArmor = 0;
        this.zutiObjectives_redVehicles = 0;
        this.zutiObjectives_redTrains = 0;
        this.zutiObjectives_redAircraftsS = 0;
        this.zutiObjectives_redAircraftsM = 0;
        this.zutiObjectives_redRockets = 0;
        this.zutiObjectives_redHomeBases = 0;
        this.zutiObjectives_redInfantry = 0;
        this.zutiobjectives_blueShipsS = 0;
        this.zutiObjectives_blueShipsM = 0;
        this.zutiobjectives_blueArtillery = 0;
        this.zutiObjectives_blueStationary = 0;
        this.zutiObjectives_blueArmor = 0;
        this.zutiObjectives_blueVehicles = 0;
        this.zutiObjectives_blueTrains = 0;
        this.zutiObjectives_blueAircraftsS = 0;
        this.zutiObjectives_blueAircraftsM = 0;
        this.zutiObjectives_blueRockets = 0;
        this.zutiObjectives_blueHomeBases = 0;
        this.zutiObjectives_blueInfantry = 0;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiObjectives_enable.setChecked(this.zutiObjectives_enable, false);
        this.wZutiObjectives_redArmor.setValue(new Integer(this.zutiObjectives_redArmor).toString(), false);
        this.wZutiObjectives_redTrains.setValue(new Integer(this.zutiObjectives_redTrains).toString(), false);
        this.wZutiObjectives_redVehicles.setValue(new Integer(this.zutiObjectives_redVehicles).toString(), false);
        this.wZutiObjectives_redShipsM.setValue(new Integer(this.zutiObjectives_redShipsM).toString(), false);
        this.wZutiObjectives_redAircraftsM.setValue(new Integer(this.zutiObjectives_redAircraftsM).toString(), false);
        this.wZutiObjectives_redShipsS.setValue(new Integer(this.zutiObjectives_redShipsS).toString(), false);
        this.wZutiObjectives_redAircraftsS.setValue(new Integer(this.zutiObjectives_redAircraftsS).toString(), false);
        this.wZutiObjectives_redArtillery.setValue(new Integer(this.zutiObjectives_redArtillery).toString(), false);
        this.wZutiObjectives_redStationary.setValue(new Integer(this.zutiObjectives_redStationary).toString(), false);
        this.wZutiObjectives_redRockets.setValue(new Integer(this.zutiObjectives_redRockets).toString(), false);
        this.wZutiObjectives_redInfantry.setValue(new Integer(this.zutiObjectives_redInfantry).toString(), false);
        this.wZutiObjectives_redHomeBases.setValue(new Integer(this.zutiObjectives_redHomeBases).toString(), false);
        this.wZutiObjectives_blueArmor.setValue(new Integer(this.zutiObjectives_blueArmor).toString(), false);
        this.wZutiObjectives_blueTrains.setValue(new Integer(this.zutiObjectives_blueTrains).toString(), false);
        this.wZutiObjectives_blueVehicles.setValue(new Integer(this.zutiObjectives_blueVehicles).toString(), false);
        this.wZutiObjectives_blueShipsM.setValue(new Integer(this.zutiObjectives_blueShipsM).toString(), false);
        this.wZutiObjectives_blueAircraftsM.setValue(new Integer(this.zutiObjectives_blueAircraftsM).toString(), false);
        this.wZutiObjectives_blueShipsS.setValue(new Integer(this.zutiobjectives_blueShipsS).toString(), false);
        this.wZutiObjectives_blueAircraftsS.setValue(new Integer(this.zutiObjectives_blueAircraftsS).toString(), false);
        this.wZutiObjectives_blueArtillery.setValue(new Integer(this.zutiobjectives_blueArtillery).toString(), false);
        this.wZutiObjectives_blueStationary.setValue(new Integer(this.zutiObjectives_blueStationary).toString(), false);
        this.wZutiObjectives_blueRockets.setValue(new Integer(this.zutiObjectives_blueRockets).toString(), false);
        this.wZutiObjectives_blueHomeBases.setValue(new Integer(this.zutiObjectives_blueHomeBases).toString(), false);
        this.wZutiObjectives_blueInfantry.setValue(new Integer(this.zutiObjectives_blueInfantry).toString(), false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiObjectives_enable = this.wZutiObjectives_enable.isChecked();
        this.zutiObjectives_redArmor = Integer.parseInt(this.wZutiObjectives_redArmor.getValue());
        this.zutiObjectives_redTrains = Integer.parseInt(this.wZutiObjectives_redTrains.getValue());
        this.zutiObjectives_redVehicles = Integer.parseInt(this.wZutiObjectives_redVehicles.getValue());
        this.zutiObjectives_redShipsM = Integer.parseInt(this.wZutiObjectives_redShipsM.getValue());
        this.zutiObjectives_redAircraftsM = Integer.parseInt(this.wZutiObjectives_redAircraftsM.getValue());
        this.zutiObjectives_redShipsS = Integer.parseInt(this.wZutiObjectives_redShipsS.getValue());
        this.zutiObjectives_redAircraftsS = Integer.parseInt(this.wZutiObjectives_redAircraftsS.getValue());
        this.zutiObjectives_redArtillery = Integer.parseInt(this.wZutiObjectives_redArtillery.getValue());
        this.zutiObjectives_redStationary = Integer.parseInt(this.wZutiObjectives_redStationary.getValue());
        this.zutiObjectives_redRockets = Integer.parseInt(this.wZutiObjectives_redRockets.getValue());
        this.zutiObjectives_redInfantry = Integer.parseInt(this.wZutiObjectives_redInfantry.getValue());
        this.zutiObjectives_redHomeBases = Integer.parseInt(this.wZutiObjectives_redHomeBases.getValue());
        this.zutiObjectives_blueArmor = Integer.parseInt(this.wZutiObjectives_blueArmor.getValue());
        this.zutiObjectives_blueTrains = Integer.parseInt(this.wZutiObjectives_blueTrains.getValue());
        this.zutiObjectives_blueVehicles = Integer.parseInt(this.wZutiObjectives_blueVehicles.getValue());
        this.zutiObjectives_blueShipsM = Integer.parseInt(this.wZutiObjectives_blueShipsM.getValue());
        this.zutiObjectives_blueAircraftsM = Integer.parseInt(this.wZutiObjectives_blueAircraftsM.getValue());
        this.zutiobjectives_blueShipsS = Integer.parseInt(this.wZutiObjectives_blueShipsS.getValue());
        this.zutiObjectives_blueAircraftsS = Integer.parseInt(this.wZutiObjectives_blueAircraftsS.getValue());
        this.zutiobjectives_blueArtillery = Integer.parseInt(this.wZutiObjectives_blueArtillery.getValue());
        this.zutiObjectives_blueStationary = Integer.parseInt(this.wZutiObjectives_blueStationary.getValue());
        this.zutiObjectives_blueRockets = Integer.parseInt(this.wZutiObjectives_blueRockets.getValue());
        this.zutiObjectives_blueInfantry = Integer.parseInt(this.wZutiObjectives_blueInfantry.getValue());
        this.zutiObjectives_blueHomeBases = Integer.parseInt(this.wZutiObjectives_blueHomeBases.getValue());

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            int sectionId = sectfile.sectionIndex(SECTION_ID);
            if (sectionId < 0) {
                this.zutiObjectives_enable = false;
                this.loadVariablesFromOldSection(sectfile);
                return;
            }

            this.zutiObjectives_enable = true;
            this.zutiObjectives_redArmor = sectfile.get(SECTION_ID, "ZutiObjective_RedArmor", 0, 0, 99999);// , default, min, max)
            this.zutiObjectives_redTrains = sectfile.get(SECTION_ID, "zutiObjective_RedTrains", 0, 0, 99999);
            this.zutiObjectives_redVehicles = sectfile.get(SECTION_ID, "ZutiObjective_RedVehicles", 0, 0, 99999);
            this.zutiObjectives_redShipsM = sectfile.get(SECTION_ID, "ZutiObjectives_RedShipsM", 0, 0, 99999);
            this.zutiObjectives_redAircraftsM = sectfile.get(SECTION_ID, "ZutiObjective_RedAircraftM", 0, 0, 99999);
            this.zutiObjectives_redShipsS = sectfile.get(SECTION_ID, "ZutiObjective_RedShipsS", 0, 0, 99999);
            this.zutiObjectives_redAircraftsS = sectfile.get(SECTION_ID, "ZutiObjective_RedAircraftS", 0, 0, 99999);
            this.zutiObjectives_redArtillery = sectfile.get(SECTION_ID, "ZutiObjective_RedArtillery", 0, 0, 99999);
            this.zutiObjectives_redStationary = sectfile.get(SECTION_ID, "ZutiObjective_RedStationary", 0, 0, 99999);
            this.zutiObjectives_redRockets = sectfile.get(SECTION_ID, "ZutiObjective_RedRockets", 0, 0, 99999);
            this.zutiObjectives_redInfantry = sectfile.get(SECTION_ID, "ZutiObjective_RedInfantry", 0, 0, 99999);
            this.zutiObjectives_redHomeBases = sectfile.get(SECTION_ID, "ZutiObjective_RedHomeBases", 0, 0, 99999);

            this.zutiObjectives_blueArmor = sectfile.get(SECTION_ID, "ZutiObjective_BlueArmor", 0, 0, 99999);// , default, min, max)
            this.zutiObjectives_blueTrains = sectfile.get(SECTION_ID, "zutiObjective_BlueTrains", 0, 0, 99999);
            this.zutiObjectives_blueVehicles = sectfile.get(SECTION_ID, "ZutiObjective_BlueVehicles", 0, 0, 99999);
            this.zutiObjectives_blueShipsM = sectfile.get(SECTION_ID, "ZutiObjectives_BlueShipsM", 0, 0, 99999);
            this.zutiObjectives_blueAircraftsM = sectfile.get(SECTION_ID, "ZutiObjective_BlueAircraftM", 0, 0, 99999);
            this.zutiobjectives_blueShipsS = sectfile.get(SECTION_ID, "ZutiObjective_BlueShipsS", 0, 0, 99999);
            this.zutiObjectives_blueAircraftsS = sectfile.get(SECTION_ID, "ZutiObjective_BlueAircraftS", 0, 0, 99999);
            this.zutiobjectives_blueArtillery = sectfile.get(SECTION_ID, "ZutiObjective_BlueArtillery", 0, 0, 99999);
            this.zutiObjectives_blueStationary = sectfile.get(SECTION_ID, "ZutiObjective_BlueStationary", 0, 0, 99999);
            this.zutiObjectives_blueRockets = sectfile.get(SECTION_ID, "ZutiObjective_BlueRockets", 0, 0, 99999);
            this.zutiObjectives_blueInfantry = sectfile.get(SECTION_ID, "ZutiObjective_BlueInfantry", 0, 0, 99999);
            this.zutiObjectives_blueHomeBases = sectfile.get(SECTION_ID, "ZutiObjective_BlueHomeBases", 0, 0, 99999);

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            int sectionId = sectfile.sectionIndex(OLD_MDS_ID);
            if (sectionId < 0) {
                this.zutiObjectives_enable = false;
                return;
            }

            this.zutiObjectives_enable = true;
            this.zutiObjectives_redArmor = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedArmor", 0, 0, 99999);// , default, min, max)
            this.zutiObjectives_redTrains = sectfile.get(OLD_MDS_ID, "zutiObjective_RedTrains", 0, 0, 99999);
            this.zutiObjectives_redVehicles = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedVehicles", 0, 0, 99999);
            this.zutiObjectives_redShipsM = sectfile.get(OLD_MDS_ID, "ZutiObjectives_RedShipsM", 0, 0, 99999);
            this.zutiObjectives_redAircraftsM = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedAircraftM", 0, 0, 99999);
            this.zutiObjectives_redShipsS = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedShipsS", 0, 0, 99999);
            this.zutiObjectives_redAircraftsS = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedAircraftS", 0, 0, 99999);
            this.zutiObjectives_redArtillery = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedArtillery", 0, 0, 99999);
            this.zutiObjectives_redStationary = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedStationary", 0, 0, 99999);
            this.zutiObjectives_redRockets = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedRockets", 0, 0, 99999);
            this.zutiObjectives_redInfantry = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedInfantry", 0, 0, 99999);
            this.zutiObjectives_redHomeBases = sectfile.get(OLD_MDS_ID, "ZutiObjective_RedHomeBases", 0, 0, 99999);

            this.zutiObjectives_blueArmor = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueArmor", 0, 0, 99999);// , default, min, max)
            this.zutiObjectives_blueTrains = sectfile.get(OLD_MDS_ID, "zutiObjective_BlueTrains", 0, 0, 99999);
            this.zutiObjectives_blueVehicles = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueVehicles", 0, 0, 99999);
            this.zutiObjectives_blueShipsM = sectfile.get(OLD_MDS_ID, "ZutiObjectives_BlueShipsM", 0, 0, 99999);
            this.zutiObjectives_blueAircraftsM = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueAircraftM", 0, 0, 99999);
            this.zutiobjectives_blueShipsS = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueShipsS", 0, 0, 99999);
            this.zutiObjectives_blueAircraftsS = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueAircraftS", 0, 0, 99999);
            this.zutiobjectives_blueArtillery = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueArtillery", 0, 0, 99999);
            this.zutiObjectives_blueStationary = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueStationary", 0, 0, 99999);
            this.zutiObjectives_blueRockets = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueRockets", 0, 0, 99999);
            this.zutiObjectives_blueInfantry = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueInfantry", 0, 0, 99999);
            this.zutiObjectives_blueHomeBases = sectfile.get(OLD_MDS_ID, "ZutiObjective_BlueHomeBases", 0, 0, 99999);
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
        if (!this.zutiObjectives_enable) return;

        try {
            int sectionIndex = sectfile.sectionIndex(SECTION_ID);
            if (sectionIndex < 0) sectionIndex = sectfile.sectionAdd(SECTION_ID);

            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedArmor", new Integer(this.zutiObjectives_redArmor).toString());
            sectfile.lineAdd(sectionIndex, "zutiObjective_RedTrains", new Integer(this.zutiObjectives_redTrains).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedVehicles", new Integer(this.zutiObjectives_redVehicles).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedShipsM", new Integer(this.zutiObjectives_redShipsM).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedAircraftM", new Integer(this.zutiObjectives_redAircraftsM).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedShipsS", new Integer(this.zutiObjectives_redShipsS).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedAircraftS", new Integer(this.zutiObjectives_redAircraftsS).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedArtillery", new Integer(this.zutiObjectives_redArtillery).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedStationary", new Integer(this.zutiObjectives_redStationary).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedRockets", new Integer(this.zutiObjectives_redRockets).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedInfantry", new Integer(this.zutiObjectives_redInfantry).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_RedHomeBases", new Integer(this.zutiObjectives_redHomeBases).toString());

            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueArmor", new Integer(this.zutiObjectives_blueArmor).toString());
            sectfile.lineAdd(sectionIndex, "zutiObjective_BlueTrains", new Integer(this.zutiObjectives_blueTrains).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueVehicles", new Integer(this.zutiObjectives_blueVehicles).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueShipsM", new Integer(this.zutiObjectives_blueShipsM).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueAircraftM", new Integer(this.zutiObjectives_blueAircraftsM).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueShipsS", new Integer(this.zutiobjectives_blueShipsS).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueAircraftS", new Integer(this.zutiObjectives_blueAircraftsS).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueArtillery", new Integer(this.zutiobjectives_blueArtillery).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueStationary", new Integer(this.zutiObjectives_blueStationary).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueRockets", new Integer(this.zutiObjectives_blueRockets).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueInfantry", new Integer(this.zutiObjectives_blueInfantry).toString());
            sectfile.lineAdd(sectionIndex, "ZutiObjective_BlueHomeBases", new Integer(this.zutiObjectives_blueHomeBases).toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void zutiListAllActors() {
        if (ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0) ZutiMDSSection_Objectives.setChiefIdIncrement();
        System.out.println("Chiefs increment = " + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT);

        int redShipsS = 0;
        int redShipsM = 0;
        int redArtillery = 0;
        int redStationary = 0;
        int redArmor = 0;
        int redVehicles = 0;
        int redTrains = 0;
        int redAircraftsS = 0;
        int redAircraftsM = 0;
        int redRockets = 0;
        int redInfantry = 0;
        int redHomeBases = 0;
        int blueShipsS = 0;
        int blueShipsM = 0;
        int blueArtillery = 0;
        int blueStationary = 0;
        int blueArmor = 0;
        int blueVehicles = 0;
        int blueTrains = 0;
        int blueAircraftsS = 0;
        int blueAircraftsM = 0;
        int blueRockets = 0;
        int blueInfantry = 0;
        int blueHomeBases = 0;

        ArrayList plugins = Plugin.zutiGetAllActors();
        PathChief pc = null;
        PathAir pa = null;
        Actor actor = null;
        Object object = null;
        PlMisStatic pluginStatic = null;
        PlMisRocket pluginRocket = null;
        PlMisHouse pluginHouse = null;
        PlMisBorn pluginBorn = null;
        Object aobj[] = null;
        ArrayList actors = null;
        ActorBorn actorBorn = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            System.out.println("PlMisBorn - " + object.getClass() + ", " + object.getClass().getSuperclass());

            if (object instanceof PlMisBorn) {
                pluginBorn = (PlMisBorn) object;
                actors = pluginBorn.allActors;
                for (int j = 0; j < actors.size(); j++) {
                    actor = (Actor) actors.get(j);
                    if (actor instanceof ActorBorn) {
                        actorBorn = (ActorBorn) actor;
                        if (actorBorn.zutiCanThisHomeBaseBeCaptured) if (actorBorn.getArmy() == 1) redHomeBases++;
                        else if (actorBorn.getArmy() == 2) blueHomeBases++;
                    }
                }
            } else if (object instanceof PlMisStatic) {
                pluginStatic = (PlMisStatic) object;
                actors = pluginStatic.allActors;

                for (int j = 0; j < actors.size(); j++) {
                    actor = (Actor) actors.get(j);

                    if (actor instanceof BigshipGeneric || actor instanceof ShipGeneric) {
                        System.out.println("  PlMisBorn - Ship: " + actor.name());

                        if (actor.getArmy() == 1) redShipsS++;
                        if (actor.getArmy() == 2) blueShipsS++;
                    } else if (actor instanceof PlaneGeneric) {
                        System.out.println("  PlMisBorn - Aircraft: " + actor.name());

                        if (actor.getArmy() == 1) redAircraftsS++;
                        if (actor.getArmy() == 2) blueAircraftsS++;
                    } else if (actor instanceof ArtilleryGeneric) {
                        System.out.println("  PlMisBorn - Artillery: " + actor.name());

                        if (actor.getArmy() == 1) redArtillery++;
                        if (actor.getArmy() == 2) blueArtillery++;
                    } else if (actor instanceof StationaryGeneric) {
                        System.out.println("  PlMisBorn - Stationary: " + actor.name());

                        if (actor.getArmy() == 1) redStationary++;
                        if (actor.getArmy() == 2) blueStationary++;
                    } else System.out.println("  PlMisBorn - Object: " + actor.name() + ", " + actor.getClass().getSuperclass());
                }
            } else if (object instanceof PlMisRocket) {
                pluginRocket = (PlMisRocket) object;
                actors = pluginRocket.allActors;
                for (int j = 0; j < actors.size(); j++) {
                    actor = (Actor) actors.get(j);
                    if (actor instanceof Rocket) {
                        System.out.println("  PlMisBorn - Rocket: " + actor.name());

                        if (actor.getArmy() == 1) redRockets++;
                        if (actor.getArmy() == 2) blueRockets++;
                    }
                }
            } else if (object instanceof PlMisHouse) {
                pluginHouse = (PlMisHouse) object;
                actors = pluginHouse.allActors;
                for (int j = 0; j < actors.size(); j++) {
                    actor = (Actor) actors.get(j);
                    System.out.println("  PlMisBorn - actor: " + actor.name() + actor.getClass());
                }
            } else if (object instanceof PlMisAir) {
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];
                    if (actor instanceof PathAir) {
                        pa = (PathAir) actor;

                        System.out.println("  PlMisBorn - Aircraft(s): " + pa.planes);

                        if (pa.getArmy() == 1) redAircraftsM += pa.planes;
                        if (pa.getArmy() == 2) blueAircraftsM += pa.planes;
                    }
                }
            } else if (object instanceof PlMisChief) {
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];
                    if (actor instanceof PathChief) {
                        pc = (PathChief) actor;
                        int pcType = pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT;
                        switch (pcType) {
                            case 0:
                                // Infantry
                                System.out.println("  PlMisBorn - Armor, count:" + pc.units.length);

                                if (pc.getArmy() == 1) redInfantry += pc.units.length;
                                if (pc.getArmy() == 2) blueInfantry += pc.units.length;
                                break;
                            case 1:
                                // Armor
                                System.out.println("  PlMisBorn - Armor, count:" + pc.units.length);

                                if (pc.getArmy() == 1) redArmor += pc.units.length;
                                if (pc.getArmy() == 2) blueArmor += pc.units.length;
                                break;
                            case 2:
                                // Vehicle
                                System.out.println("  PlMisBorn - Vehicle, count:" + pc.units.length);

                                if (pc.getArmy() == 1) redVehicles += pc.units.length;
                                if (pc.getArmy() == 2) blueVehicles += pc.units.length;
                                break;
                            case 3:
                                // Train
                                System.out.println("  PlMisBorn - Train, count:" + pc.units.length);

                                if (pc.getArmy() == 1) redTrains += pc.units.length;
                                if (pc.getArmy() == 2) blueTrains += pc.units.length;
                                break;
                            case 4:
                            case 5:
                                // Ship
                                System.out.println("  PlMisBorn - Ship, count:" + pc.units.length);

                                /*
                                 * if( pc.getArmy() == 1 ) redShipsM += pc.units.length; if( pc.getArmy() == 2 ) blueShipsM += pc.units.length; break;
                                 */
                                if (pc.getArmy() == 1) redShipsM++;
                                if (pc.getArmy() == 2) blueShipsM++;
                                break;
                        }
                    }
                }
            }
        }

        this.lZutiObjectives_redMaxShipsS.cap = new GCaption(new Integer(redShipsS).toString());
        this.lZutiObjectives_blueMaxShipsS.cap = new GCaption(new Integer(blueShipsS).toString());
        this.lZutiObjectives_redMaxShipsM.cap = new GCaption(new Integer(redShipsM).toString());
        this.lZutiObjectives_blueMaxShipsM.cap = new GCaption(new Integer(blueShipsM).toString());
        this.lZutiObjectives_redMaxAircraftsS.cap = new GCaption(new Integer(redAircraftsS).toString());
        this.lZutiObjectives_blueMaxAircraftsS.cap = new GCaption(new Integer(blueAircraftsS).toString());
        this.lZutiObjectives_redMaxAircraftsM.cap = new GCaption(new Integer(redAircraftsM).toString());
        this.lZutiObjectives_blueMaxAircraftsM.cap = new GCaption(new Integer(blueAircraftsM).toString());
        this.lZutiObjectives_redMaxRockets.cap = new GCaption(new Integer(redRockets).toString());
        this.lZutiObjectives_blueMaxRockets.cap = new GCaption(new Integer(blueRockets).toString());
        this.lZutiObjectives_redMaxVehicles.cap = new GCaption(new Integer(redVehicles).toString());
        this.lZutiObjectives_redMaxHomeBases.cap = new GCaption(new Integer(redHomeBases).toString());
        this.lZutiObjectives_blueMaxVehicles.cap = new GCaption(new Integer(blueVehicles).toString());
        this.lZutiObjectives_redMaxArmor.cap = new GCaption(new Integer(redArmor).toString());
        this.lZutiObjectives_blueMaxArmor.cap = new GCaption(new Integer(blueArmor).toString());
        this.lZutiObjectives_redMaxStationary.cap = new GCaption(new Integer(redStationary).toString());
        this.lZutiObjectives_blueMaxStationary.cap = new GCaption(new Integer(blueStationary).toString());
        this.lZutiObjectives_redMaxArtillery.cap = new GCaption(new Integer(redArtillery).toString());
        this.lZutiObjectives_blueMaxArtillery.cap = new GCaption(new Integer(blueArtillery).toString());
        this.lZutiObjectives_redMaxTrains.cap = new GCaption(new Integer(redTrains).toString());
        this.lZutiObjectives_blueMaxTrains.cap = new GCaption(new Integer(blueTrains).toString());
        this.lZutiObjectives_blueMaxHomeBases.cap = new GCaption(new Integer(blueHomeBases).toString());
        this.lZutiObjectives_blueMaxInfantry.cap = new GCaption(new Integer(blueInfantry).toString());
        this.lZutiObjectives_redMaxInfantry.cap = new GCaption(new Integer(redInfantry).toString());

        System.out.println("-----------------------------------------------------------------");

        aobj = com.maddox.il2.builder.Plugin.builder.pathes.getOwnerAttached();
        for (int j = 0; j < aobj.length; j++) {
            actor = (Actor) aobj[j];
            System.out.println("PlMisBorn - " + actor.getClass() + ", " + actor.getClass().getSuperclass());
        }
    }

    /**
     * Method returns integer increment that needs to be added to chief ID for objectives identifications.
     *
     * @return
     */
    public static void setChiefIdIncrement() {
        CHIELF_TYPE_INCREMENT = Config.cur.ini.get("game", "pcIncrement", 0, 0, 1);
        // System.out.println("ZutiMDSection_Objectives - Chief Increment: " + CHIELF_TYPE_INCREMENT);
    }
}