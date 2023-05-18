package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_MapIcons extends GWindowFramed {
    private static String        OLD_MDS_ID = "MDS";
    public static final String   SECTION_ID = "MDSSection_MapIcons";

    private boolean              zutiIcons_ShowAircraft;
    private boolean              zutiIcons_ShowGroundUnits;
    private boolean              zutiIcons_StaticIconsIfNoRadar;
    private boolean              zutiIcons_AircraftIconsWhite;
    private boolean              zutiIcons_HideUnpopulatedAirstripsFromMinimap;
    private boolean              zutiIcons_ShowRockets;
    private boolean              zutiIcons_ShowPlayerAircraft;
    private boolean              zutiIcons_MovingIcons;
    private boolean              zutiIcons_ShowTargets;
    private int                  zutiIcons_IconsSize;
    private boolean              zutiIcons_ShowNeutralHB;

    private GWindowCheckBox      wZutiIcons_ShowAircraft;
    private GWindowCheckBox      wZutiIcons_ShowGroundUnits;
    private GWindowCheckBox      wZutiIcons_ShowRockets;
    private GWindowCheckBox      wZutiIcons_StaticIconsIfNoRadar;
    private GWindowCheckBox      wZutiIcons_AircraftIconsWhite;
    private GWindowCheckBox      wZutiIcons_HideUnpopulatedAirstripsFromMinimap;
    private GWindowCheckBox      wZutiIcons_ShowPlayerAircraft;
    private GWindowCheckBox      wZutiIcons_MovingIcons;
    private GWindowBoxSeparate   bSeparate_TargetIcons;
    private GWindowLabel         lSeparate_TargetIcons;
    private GWindowBoxSeparate   bSeparate_Icons;
    private GWindowLabel         lSeparate_Icons;
    private GWindowComboControl  wZutiIcons_IconsSize;
    private GWindowCheckBox      wZutiIcons_ShowNeutralHB;
    private GWindowCheckBox      wZutiIcons_ShowTargets;

    private ZutiMDSSection_Radar mdsRadarReference;

    public ZutiMDSSection_MapIcons(ZutiMDSSection_Radar radarReference) {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 33.0F, 31.0F, true);
        this.bSizable = false;

        this.mdsRadarReference = radarReference;
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
        this.title = Plugin.i18n("mds.section.mapIcons");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        gwindowdialogclient.addLabel(this.lSeparate_Icons = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 4.0F, 1.6F, Plugin.i18n("mds.icons.general"), null));
        this.bSeparate_Icons = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 30.0F, 17.0F);
        this.bSeparate_Icons.exclude = this.lSeparate_Icons;

        // wZutiRadar_ShowAircraft
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showAc"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_ShowAircraft = new GWindowCheckBox(gwindowdialogclient, 29.0F, 2.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShowRockets
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showRocket"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_ShowRockets = new GWindowCheckBox(gwindowdialogclient, 29.0F, 4.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShowGroundUnits
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showGO"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_ShowGroundUnits = new GWindowCheckBox(gwindowdialogclient, 29.0F, 6.0F, null) {
            public void preRender() {
                super.preRender();

                if (ZutiMDSSection_MapIcons.this.mdsRadarReference != null) this.setEnable(ZutiMDSSection_MapIcons.this.mdsRadarReference.areScoutsRadars());
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiIcons_ShowNeutralHB
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showNeutral"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_ShowNeutralHB = new GWindowCheckBox(gwindowdialogclient, 29.0F, 8.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_AircraftIconsWhite
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 10.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.allIconsWhite"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_AircraftIconsWhite = new GWindowCheckBox(gwindowdialogclient, 29.0F, 10.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_ShowPlayerAircraft
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 12.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.showPlayer"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_ShowPlayerAircraft = new GWindowCheckBox(gwindowdialogclient, 29.0F, 12.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiIcons_IconsSize
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 14.0F, 29.0F, 1.3F, Plugin.i18n("mds.icons.iconSize"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_IconsSize = new GWindowComboControl(gwindowdialogclient, 25.0F, 14.0F, 5.0F) {
            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });
        this.wZutiIcons_IconsSize.setEditable(false);
        this.wZutiIcons_IconsSize.add(Plugin.i18n("08"));
        this.wZutiIcons_IconsSize.add(Plugin.i18n("12"));
        this.wZutiIcons_IconsSize.add(Plugin.i18n("16"));
        this.wZutiIcons_IconsSize.add(Plugin.i18n("20"));
        this.wZutiIcons_IconsSize.add(Plugin.i18n("24"));
        this.wZutiIcons_IconsSize.add(Plugin.i18n("28"));
        this.wZutiIcons_IconsSize.add(Plugin.i18n("32"));

        // wZutiRadar_HideUnpopulatedAirstripsFromMinimap
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 16.0F, 29.0F, 1.3F, Plugin.i18n("mds.misc.hideAirfields"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_HideUnpopulatedAirstripsFromMinimap = new GWindowCheckBox(gwindowdialogclient, 29.0F, 16.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_TargetIcons = new GWindowLabel(gwindowdialogclient, 3.0F, 19.5F, 4.0F, 1.6F, Plugin.i18n("mds.targets"), null));
        this.bSeparate_TargetIcons = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 20.0F, 30.0F, 7.0F);
        this.bSeparate_TargetIcons.exclude = this.lSeparate_TargetIcons;

        // wZutiTargets_ShowTargets
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 21.0F, 27.0F, 1.3F, Plugin.i18n("mds.targets.renderTargets"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_ShowTargets = new GWindowCheckBox(gwindowdialogclient, 29.0F, 21.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                ZutiMDSSection_MapIcons.this.wZutiIcons_MovingIcons.setEnable(this.isChecked());

                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiTargets_MovingIcons
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 23.0F, 27.0F, 1.3F, Plugin.i18n("mds.targets.moving"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_MovingIcons = new GWindowCheckBox(gwindowdialogclient, 29.0F, 23.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                ZutiMDSSection_MapIcons.this.wZutiIcons_StaticIconsIfNoRadar.setEnable(this.isChecked());

                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
                return false;
            }
        });

        // wZutiRadar_StaticIconsIfNoRadar
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 25.0F, 27.0F, 1.3F, Plugin.i18n("mds.targets.stationary"), null));
        gwindowdialogclient.addControl(this.wZutiIcons_StaticIconsIfNoRadar = new GWindowCheckBox(gwindowdialogclient, 29.0F, 25.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_MapIcons.this.setMDSVariables();
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
        this.zutiIcons_MovingIcons = false;
        this.zutiIcons_ShowTargets = true;
        this.zutiIcons_IconsSize = 4;
        this.zutiIcons_ShowNeutralHB = false;
        this.zutiIcons_ShowAircraft = false;
        this.zutiIcons_ShowGroundUnits = false;
        this.zutiIcons_StaticIconsIfNoRadar = false;
        this.zutiIcons_ShowRockets = false;
        this.zutiIcons_AircraftIconsWhite = false;
        this.zutiIcons_ShowPlayerAircraft = true;
        this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiIcons_MovingIcons.setChecked(this.zutiIcons_MovingIcons, false);
        this.wZutiIcons_ShowTargets.setChecked(this.zutiIcons_ShowTargets, false);
        this.wZutiIcons_IconsSize.setSelected(this.zutiIcons_IconsSize, true, false);
        this.wZutiIcons_ShowNeutralHB.setChecked(this.zutiIcons_ShowNeutralHB, false);

        this.wZutiIcons_ShowAircraft.setChecked(this.zutiIcons_ShowAircraft, false);
        this.wZutiIcons_ShowGroundUnits.setChecked(this.zutiIcons_ShowGroundUnits, false);
        this.wZutiIcons_ShowRockets.setChecked(this.zutiIcons_ShowRockets, false);
        this.wZutiIcons_StaticIconsIfNoRadar.setChecked(this.zutiIcons_StaticIconsIfNoRadar, false);
        this.wZutiIcons_AircraftIconsWhite.setChecked(this.zutiIcons_AircraftIconsWhite, false);
        this.wZutiIcons_ShowPlayerAircraft.setChecked(this.zutiIcons_ShowPlayerAircraft, false);
        this.wZutiIcons_HideUnpopulatedAirstripsFromMinimap.setChecked(this.zutiIcons_HideUnpopulatedAirstripsFromMinimap, false);
    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiIcons_MovingIcons = this.wZutiIcons_MovingIcons.isChecked();
        this.zutiIcons_ShowTargets = this.wZutiIcons_ShowTargets.isChecked();
        this.zutiIcons_IconsSize = this.wZutiIcons_IconsSize.getSelected();
        this.zutiIcons_ShowNeutralHB = this.wZutiIcons_ShowNeutralHB.isChecked();

        this.zutiIcons_ShowAircraft = this.wZutiIcons_ShowAircraft.isChecked();
        this.zutiIcons_ShowGroundUnits = this.wZutiIcons_ShowGroundUnits.isChecked();
        this.zutiIcons_ShowRockets = this.wZutiIcons_ShowRockets.isChecked();
        this.zutiIcons_StaticIconsIfNoRadar = this.wZutiIcons_StaticIconsIfNoRadar.isChecked();
        this.zutiIcons_AircraftIconsWhite = this.wZutiIcons_AircraftIconsWhite.isChecked();
        this.zutiIcons_ShowPlayerAircraft = this.wZutiIcons_ShowPlayerAircraft.isChecked();
        this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = this.wZutiIcons_HideUnpopulatedAirstripsFromMinimap.isChecked();

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiIcons_MovingIcons = false;
            if (sectfile.get(SECTION_ID, "ZutiIcons_MovingIcons", 0, 0, 1) == 1) this.zutiIcons_MovingIcons = true;

            this.zutiIcons_StaticIconsIfNoRadar = false;
            if (sectfile.get(SECTION_ID, "ZutiRadar_StaticIconsIfNoRadar", 0, 0, 1) == 1) this.zutiIcons_StaticIconsIfNoRadar = true;

            this.zutiIcons_ShowTargets = true;
            if (sectfile.get(SECTION_ID, "ZutiIcons_ShowTargets", 1, 0, 1) == 0) this.zutiIcons_ShowTargets = false;

            this.zutiIcons_IconsSize = sectfile.get(SECTION_ID, "ZutiIcons_IconsSize", 4, 0, 6);

            this.zutiIcons_ShowNeutralHB = false;
            if (sectfile.get(SECTION_ID, "ZutiIcons_ShowNeutralHB", 0, 0, 1) == 1) this.zutiIcons_ShowNeutralHB = true;

            this.zutiIcons_ShowAircraft = false;
            if (sectfile.get(SECTION_ID, "ZutiIcons_ShowAircraft", 0, 0, 1) == 1) this.zutiIcons_ShowAircraft = true;

            this.zutiIcons_ShowGroundUnits = false;
            if (sectfile.get(SECTION_ID, "ZutiIcons_ShowGroundUnits", 0, 0, 1) == 1) this.zutiIcons_ShowGroundUnits = true;

            this.zutiIcons_ShowRockets = false;
            if (sectfile.get(SECTION_ID, "ZutiIcons_ShowRockets", 0, 0, 1) == 1) this.zutiIcons_ShowRockets = true;

            this.zutiIcons_AircraftIconsWhite = false;
            if (sectfile.get(SECTION_ID, "ZutiIcons_AircraftIconsWhite", 0, 0, 1) == 1) this.zutiIcons_AircraftIconsWhite = true;

            this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
            if (sectfile.get(SECTION_ID, "ZutiIcons_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1) this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = true;

            this.zutiIcons_ShowPlayerAircraft = true;
            if (sectfile.get(SECTION_ID, "ZutiIcons_ShowPlayerAircraft", 1, 0, 1) == 0) this.zutiIcons_ShowPlayerAircraft = false;

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            this.zutiIcons_MovingIcons = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiTargets_MovingIcons", 0, 0, 1) == 1) this.zutiIcons_MovingIcons = true;

            this.zutiIcons_StaticIconsIfNoRadar = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_StaticIconsIfNoRadar", 0, 0, 1) == 1) this.zutiIcons_StaticIconsIfNoRadar = true;

            this.zutiIcons_ShowTargets = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiTargets_ShowTargets", 1, 0, 1) == 0) this.zutiIcons_ShowTargets = false;

            this.zutiIcons_IconsSize = sectfile.get(OLD_MDS_ID, "ZutiIcons_IconsSize", 4, 0, 6);

            this.zutiIcons_ShowNeutralHB = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiIcons_ShowNeutralHB", 0, 0, 1) == 1) this.zutiIcons_ShowNeutralHB = true;

            this.zutiIcons_ShowAircraft = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowAircraft", 0, 0, 1) == 1) this.zutiIcons_ShowAircraft = true;

            this.zutiIcons_ShowGroundUnits = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowGroundUnits", 0, 0, 1) == 1) this.zutiIcons_ShowGroundUnits = true;

            this.zutiIcons_ShowRockets = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowRockets", 0, 0, 1) == 1) this.zutiIcons_ShowRockets = true;

            this.zutiIcons_AircraftIconsWhite = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_AircraftIconsWhite", 0, 0, 1) == 1) this.zutiIcons_AircraftIconsWhite = true;

            this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1) this.zutiIcons_HideUnpopulatedAirstripsFromMinimap = true;

            this.zutiIcons_ShowPlayerAircraft = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiRadar_ShowPlayerAircraft", 1, 0, 1) == 0) this.zutiIcons_ShowPlayerAircraft = false;

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

            sectfile.lineAdd(sectionIndex, "ZutiIcons_MovingIcons", ZutiSupportMethods.boolToInt(this.zutiIcons_MovingIcons));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowTargets", ZutiSupportMethods.boolToInt(this.zutiIcons_ShowTargets));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_IconsSize", new Integer(this.zutiIcons_IconsSize).toString());
            sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowNeutralHB", ZutiSupportMethods.boolToInt(this.zutiIcons_ShowNeutralHB));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowAircraft", ZutiSupportMethods.boolToInt(this.zutiIcons_ShowAircraft));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowGroundUnits", ZutiSupportMethods.boolToInt(this.zutiIcons_ShowGroundUnits));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowRockets", ZutiSupportMethods.boolToInt(this.zutiIcons_ShowRockets));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_StaticIconsIfNoRadar", ZutiSupportMethods.boolToInt(this.zutiIcons_StaticIconsIfNoRadar));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_AircraftIconsWhite", ZutiSupportMethods.boolToInt(this.zutiIcons_AircraftIconsWhite));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_ShowPlayerAircraft", ZutiSupportMethods.boolToInt(this.zutiIcons_ShowPlayerAircraft));
            sectfile.lineAdd(sectionIndex, "ZutiIcons_HideUnpopulatedAirstripsFromMinimap", ZutiSupportMethods.boolToInt(this.zutiIcons_HideUnpopulatedAirstripsFromMinimap));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}