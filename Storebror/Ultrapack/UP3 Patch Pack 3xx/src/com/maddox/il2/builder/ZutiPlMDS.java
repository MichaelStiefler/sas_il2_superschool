package com.maddox.il2.builder;

import java.util.ArrayList;
import java.util.List;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

public class ZutiPlMDS extends Plugin {

    protected GWindowMenuItem              zuti_mMDS;
    protected ZutiMDSSection_Radar         mdsSection_Radar;
    protected ZutiMDSSection_MapIcons      mdsSection_Icons;
    protected ZutiMDSSection_RRR           mdsSection_RRR;
    protected ZutiMDSSection_Objectives    mdsSection_Objectives;
    protected ZutiMDSSection_NextMission   mdsSection_NextMission;
    protected ZutiMDSSection_HUD           mdsSection_HUD;
    protected ZutiMDSSection_Craters       mdsSection_Craters;
    protected ZutiMDSSection_AirDrops      mdsSection_AirDrops;
    protected ZutiMDSSection_Miscellaneous mdsSection_Miscellaneous;
    protected ZutiMDSSection_StaticRespawn mdsSection_StaticsRespawn;
    protected GWindowMenuItem              toggleSpawnPlaceIndicatorsMenuItem;
    protected GWindowMenuItem              toggleAirdromeInfrastructureMenuItem;

    protected static ArrayList             zutiModsLines                = null;
    protected ArrayList                    zutiAlternativeAirfieldsList = null;

    public void createGUI() {
        try {
            mdsSection_Radar = new ZutiMDSSection_Radar();
            mdsSection_Icons = new ZutiMDSSection_MapIcons(mdsSection_Radar);
            mdsSection_RRR = new ZutiMDSSection_RRR();
            mdsSection_Objectives = new ZutiMDSSection_Objectives();
            mdsSection_NextMission = new ZutiMDSSection_NextMission();
            mdsSection_HUD = new ZutiMDSSection_HUD();
            mdsSection_Craters = new ZutiMDSSection_Craters();
            mdsSection_AirDrops = new ZutiMDSSection_AirDrops();
            mdsSection_Miscellaneous = new ZutiMDSSection_Miscellaneous();
            mdsSection_StaticsRespawn = new ZutiMDSSection_StaticRespawn();

            GWindowMenu mdsMenu = Plugin.builder.mZutiMds.subMenu;

            mdsMenu.addItem(0, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.radar"), "Manage Radar Settings") {
                public void execute() {
                    if (mdsSection_Radar == null)
                        return;

                    if (mdsSection_Radar.isVisible())
                        mdsSection_Radar.hideWindow();
                    else
                        mdsSection_Radar.showWindow();
                }
            });
            mdsMenu.addItem(1, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.mapIcons"), "Manage Map Icons Settings") {
                public void execute() {
                    if (mdsSection_Icons == null)
                        return;

                    if (mdsSection_Icons.isVisible())
                        mdsSection_Icons.hideWindow();
                    else
                        mdsSection_Icons.showWindow();
                }
            });
            mdsMenu.addItem(2, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.RRR"), "Manage Map RRR Settings") {
                public void execute() {
                    if (mdsSection_RRR == null)
                        return;

                    if (mdsSection_RRR.isVisible())
                        mdsSection_RRR.hideWindow();
                    else
                        mdsSection_RRR.showWindow();
                }
            });
            mdsMenu.addItem(3, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.objectives"), "Manage Map Objectives Settings") {
                public void execute() {
                    if (mdsSection_Objectives == null)
                        return;

                    if (mdsSection_Objectives.isVisible())
                        mdsSection_Objectives.hideWindow();
                    else
                        mdsSection_Objectives.showWindow();
                }
            });
            mdsMenu.addItem(4, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.nextMission"), "Manage Map Next Mission Settings") {
                public void execute() {
                    if (mdsSection_NextMission == null)
                        return;

                    if (mdsSection_NextMission.isVisible())
                        mdsSection_NextMission.hideWindow();
                    else
                        mdsSection_NextMission.showWindow();
                }
            });
            mdsMenu.addItem(5, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.hud"), "Manage Map HUD Settings") {
                public void execute() {
                    if (mdsSection_HUD == null)
                        return;

                    if (mdsSection_HUD.isVisible())
                        mdsSection_HUD.hideWindow();
                    else
                        mdsSection_HUD.showWindow();
                }
            });
            mdsMenu.addItem(6, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.craters"), "Manage Map Craters Settings") {
                public void execute() {
                    if (mdsSection_Craters == null)
                        return;

                    if (mdsSection_Craters.isVisible())
                        mdsSection_Craters.hideWindow();
                    else
                        mdsSection_Craters.showWindow();
                }
            });
            mdsMenu.addItem(7, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.airDrops"), "Manage Map Air Drops Settings") {
                public void execute() {
                    if (mdsSection_AirDrops == null)
                        return;

                    if (mdsSection_AirDrops.isVisible())
                        mdsSection_AirDrops.hideWindow();
                    else
                        mdsSection_AirDrops.showWindow();
                }
            });
            mdsMenu.addItem(8, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.miscellaneous"), "Manage Map Miscellaneous Settings") {
                public void execute() {
                    if (mdsSection_Miscellaneous == null)
                        return;

                    if (mdsSection_Miscellaneous.isVisible())
                        mdsSection_Miscellaneous.hideWindow();
                    else
                        mdsSection_Miscellaneous.showWindow();
                }
            });
            mdsMenu.addItem("-", null);
            mdsMenu.addItem(10, new GWindowMenuItem(mdsMenu, Plugin.i18n("mds.section.staticsRespawn"), "Manage Map Stationary Objects Respawn Settings") {
                public void execute() {
                    if (mdsSection_StaticsRespawn == null)
                        return;

                    if (mdsSection_StaticsRespawn.isVisible())
                        mdsSection_StaticsRespawn.hideWindow();
                    else
                        mdsSection_StaticsRespawn.showWindow();
                }
            });
            mdsMenu.addItem("-", null);
            mdsMenu.addItem(toggleSpawnPlaceIndicatorsMenuItem = new GWindowMenuItem(mdsMenu, (Plugin.i18n("mds.section.toggleSpawnPlaces")), "Show or hide spawn place holders indicating where spawn places are") {
                public void execute() {
                    World world = World.cur();
                    if (world == null || world.airdrome == null || world.airdrome.stay == null) {
                        new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, Plugin.i18n("mds.section.error"), (Plugin.i18n("mds.section.noSpawnPlaces")), 4, 0.0F);
                        return;
                    }

                    this.bChecked = !this.bChecked;

                    if (bChecked) {
                        ZutiSupportMethods_Builder.loadSpawnPlaceMarkers(world.airdrome.stay);
                    } else {
                        ZutiSupportMethods_Builder.removeSpawnPlaceMarkers();
                    }
                }
            });
            mdsMenu.addItem(toggleAirdromeInfrastructureMenuItem = new GWindowMenuItem(mdsMenu, (Plugin.i18n("mds.section.toggleAirdromeInfrastructure")), "Show or hide airdrome infrastructure") {
                public void execute() {
                    this.bChecked = !this.bChecked;
                    ZutiSupportMethods_Builder.showAirportPoints(this.bChecked);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void load(SectFile sectfile) {
        if (toggleSpawnPlaceIndicatorsMenuItem != null)
            toggleSpawnPlaceIndicatorsMenuItem.bChecked = false;
        if (toggleAirdromeInfrastructureMenuItem != null)
            toggleAirdromeInfrastructureMenuItem.bChecked = false;

        if (mdsSection_Radar != null)
            mdsSection_Radar.loadVariables(sectfile);
        if (mdsSection_Icons != null)
            mdsSection_Icons.loadVariables(sectfile);
        if (mdsSection_RRR != null)
            mdsSection_RRR.loadVariables(sectfile);
        if (mdsSection_NextMission != null)
            mdsSection_NextMission.loadVariables(sectfile);
        if (mdsSection_HUD != null)
            mdsSection_HUD.loadVariables(sectfile);
        if (mdsSection_Craters != null)
            mdsSection_Craters.loadVariables(sectfile);
        if (mdsSection_AirDrops != null)
            mdsSection_AirDrops.loadVariables(sectfile);
        if (mdsSection_Miscellaneous != null)
            mdsSection_Miscellaneous.loadVariables(sectfile);
        if (mdsSection_StaticsRespawn != null)
            mdsSection_StaticsRespawn.loadVariables(sectfile);

        loadAlternativeAirfields(sectfile);

        // Store Mods section entries
        int modsIndex = sectfile.sectionIndex("Mods");
        if (modsIndex > 0) {
            zutiModsLines = new ArrayList();
            int varsCount = sectfile.vars(modsIndex);
            for (int i = 0; i < varsCount; i++) {
                zutiModsLines.add(sectfile.line(modsIndex, i));
            }
        }

        // Load resources settings
        ZutiSupportMethods_Builder.loadResources(mdsSection_RRR, sectfile);
    }

    public boolean save(SectFile sectfile) {
        try {
            // TODO: Added by |ZUTI|: Create sect file for RRR Resources
            // ---------------------------------------------------------
            if (ZutiSupportMethods_Builder.resourcesManagementEnabled(mdsSection_RRR)) {
                if (mdsSection_RRR != null && mdsSection_RRR.resourcesManagement_Red != null && mdsSection_RRR.resourcesManagement_Red.wTable != null)
                    ZutiSupportMethods_Builder.saveRRRObjectsResourcesForSide(mdsSection_RRR.resourcesManagement_Red.wTable.items, sectfile, 1);

                if (mdsSection_RRR != null && mdsSection_RRR.resourcesManagement_Blue != null && mdsSection_RRR.resourcesManagement_Blue.wTable != null)
                    ZutiSupportMethods_Builder.saveRRRObjectsResourcesForSide(mdsSection_RRR.resourcesManagement_Blue.wTable.items, sectfile, 2);

                List bornPlaces = ZutiSupportMethods_Builder.getPlMisBornActorsList();
                if (bornPlaces != null) {
                    for (int i = 0; i < bornPlaces.size(); i++)
                        ZutiSupportMethods_Builder.saveRRRObjectsResources((ActorBorn) bornPlaces.get(i), sectfile);
                }
            }
            // ----------------------------------------------------------

            if (mdsSection_StaticsRespawn != null)
                mdsSection_StaticsRespawn.saveVariables(sectfile);
            if (mdsSection_Radar != null)
                mdsSection_Radar.saveVariables(sectfile);
            if (mdsSection_Icons != null)
                mdsSection_Icons.saveVariables(sectfile);
            if (mdsSection_RRR != null)
                mdsSection_RRR.saveVariables(sectfile);
            if (mdsSection_NextMission != null)
                mdsSection_NextMission.saveVariables(sectfile);
            if (mdsSection_HUD != null)
                mdsSection_HUD.saveVariables(sectfile);
            if (mdsSection_Craters != null)
                mdsSection_Craters.saveVariables(sectfile);
            if (mdsSection_AirDrops != null)
                mdsSection_AirDrops.saveVariables(sectfile);
            if (mdsSection_Miscellaneous != null)
                mdsSection_Miscellaneous.saveVariables(sectfile);

            saveAlternativeAirfields(sectfile);

            // Save Mods section entries
            if (zutiModsLines != null) {
                int zi = zutiModsLines.size();
                int zi_13_ = sectfile.sectionIndex("Mods");
                if (zi_13_ < 0)
                    zi_13_ = sectfile.sectionAdd("Mods");
                if (zutiModsLines != null) {
                    for (int i_14_ = 0; i_14_ < zi; i_14_++) {
                        sectfile.lineAdd(zi_13_, "  " + (String) zutiModsLines.get(i_14_));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void mapLoaded() {
        freeResources();
    }

    public void freeResources() {
        if (mdsSection_Radar != null && mdsSection_Radar.isVisible())
            mdsSection_Radar.hideWindow();
        if (mdsSection_Icons != null && mdsSection_Icons.isVisible())
            mdsSection_Icons.hideWindow();
        if (mdsSection_RRR != null && mdsSection_RRR.isVisible())
            mdsSection_RRR.hideWindow();
        if (mdsSection_Objectives != null && mdsSection_Objectives.isVisible())
            mdsSection_Objectives.hideWindow();
        if (mdsSection_NextMission != null && mdsSection_NextMission.isVisible())
            mdsSection_NextMission.hideWindow();
        if (mdsSection_HUD != null && mdsSection_HUD.isVisible())
            mdsSection_HUD.hideWindow();
        if (mdsSection_Craters != null && mdsSection_Craters.isVisible())
            mdsSection_Craters.hideWindow();
        if (mdsSection_AirDrops != null && mdsSection_AirDrops.isVisible())
            mdsSection_AirDrops.hideWindow();
        if (mdsSection_Miscellaneous != null && mdsSection_Miscellaneous.isVisible())
            mdsSection_Miscellaneous.hideWindow();
        if (mdsSection_StaticsRespawn != null && mdsSection_StaticsRespawn.isVisible())
            mdsSection_StaticsRespawn.hideWindow();
    }

    /**
     * Method searches for alternative airfield section in mission and saves it's values.
     * 
     * @param plMission
     * @param sectfile
     */
    private void loadAlternativeAirfields(SectFile sectfile) {
        if (zutiAlternativeAirfieldsList == null)
            zutiAlternativeAirfieldsList = new ArrayList();
        zutiAlternativeAirfieldsList.clear();

        try {
            int s = sectfile.sectionIndex(ZutiSupportMethods_Builder.ALTERNATIVE_AIRFIELDS);
            if (s > 0) {
                int j = sectfile.vars(s);
                for (int k = 0; k < j; k++) {
                    String line = sectfile.line(s, k);

                    if (line.length() <= 0)
                        continue;

                    zutiAlternativeAirfieldsList.add(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method saves alternative airfield section in mission.
     * 
     * @param plMission
     * @param sectfile
     */
    private void saveAlternativeAirfields(SectFile sectfile) {
        if (zutiAlternativeAirfieldsList == null || zutiAlternativeAirfieldsList.size() < 1)
            return;

        try {
            int sectionId = sectfile.sectionIndex(ZutiSupportMethods_Builder.ALTERNATIVE_AIRFIELDS);
            if (sectionId < 0)
                sectionId = sectfile.sectionAdd(ZutiSupportMethods_Builder.ALTERNATIVE_AIRFIELDS);

            for (int i = 0; i < zutiAlternativeAirfieldsList.size(); i++) {
                sectfile.lineAdd(sectionId, (String) zutiAlternativeAirfieldsList.get(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static {
        Property.set(ZutiPlMDS.class, "name", "ZutiMDS");
    }
}