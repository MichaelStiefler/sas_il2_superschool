package com.maddox.il2.builder;

import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.rts.SectFile;

public class ZutiMDSSection_RRR extends GWindowFramed {
    private static String            OLD_MDS_ID               = "MDS";
    public static final String       SECTION_ID               = "MDSSection_RRR";
    public Zuti_WResourcesManagement resourcesManagement_Red  = null;
    public Zuti_WResourcesManagement resourcesManagement_Blue = null;

    private int                      zutiReload_OneMgCannonRearmSecond;
    private int                      zutiReload_OneBombFTankTorpedoeRearmSeconds;
    private int                      zutiReload_OneRocketRearmSeconds;
    private int                      zutiReload_GallonsLitersPerSecond;
    private int                      zutiReload_OneWeaponRepairSeconds;
    private int                      zutiReload_FlapsRepairSeconds;
    private boolean                  zutiReload_ReloadOnlyIfFuelTanksExist;
    private boolean                  zutiReload_ReloadOnlyIfAmmoBoxesExist;
    private boolean                  zutiReload_RepairOnlyIfWorkshopExist;
    private int                      zutiReload_EngineRepairSeconds;
    private int                      zutiReload_OneControlCableRepairSeconds;
    private int                      zutiReload_OneFuelOilTankRepairSeconds;
    private int                      zutiReload_LoadoutChangePenaltySeconds;
    private boolean                  zutiReload_OnlyHomeBaseSpecificLoadouts;
    private int                      zutiReload_CockpitRepairSeconds;

    private GWindowEditControl       wZutiReload_OneMgCannonRearmSecond;
    private GWindowEditControl       wZutiReload_OneBombFTankTorpedoeRearmSeconds;
    private GWindowEditControl       wZutiReload_OneRocketRearmSeconds;
    private GWindowEditControl       wZutiReload_GallonsLitersPerSecond;
    private GWindowEditControl       wZutiReload_OneWeaponRepairSeconds;
    private GWindowEditControl       wZutiReload_FlapsRepairSeconds;
    private GWindowCheckBox          wZutiReload_ReloadOnlyIfFuelTanksExist;
    private GWindowCheckBox          wZutiReload_ReloadOnlyIfAmmoBoxesExist;
    private GWindowCheckBox          wZutiReload_RepairOnlyIfWorkshopExist;
    private GWindowEditControl       wZutiReload_EngineRepairSeconds;
    private GWindowEditControl       wZutiReload_CockpitRepairSeconds;
    private GWindowEditControl       wZutiReload_OneControlCableRepairSeconds;
    private GWindowEditControl       wZutiReload_OneFuelOilTankRepairSeconds;
    private GWindowEditControl       wZutiReload_LoadoutChangePenaltySeconds;
    private GWindowCheckBox          wZutiReload_OnlyHomeBaseSpecificLoadouts;

    public GWindowCheckBox           wZutiReload_EnableResourcesManagement;

    private GWindowBoxSeparate       bSeparate_Rearm;
    private GWindowLabel             lSeparate_Rearm;
    private GWindowBoxSeparate       bSeparate_Refuel;
    private GWindowLabel             lSeparate_Refuel;
    private GWindowBoxSeparate       bSeparate_Repair;
    private GWindowLabel             lSeparate_Repair;
    private GWindowBoxSeparate       bSeparate_Resources;
    private GWindowLabel             lSeparate_Resources;

    public ZutiMDSSection_RRR() {
        this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 40.0F, 42.0F, true);
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
        this.title = Plugin.i18n("mds.section.RRR");
        this.clientWindow = this.create(new GWindowDialogClient());

        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
        gwindowdialogclient.addLabel(this.lSeparate_Rearm = new GWindowLabel(gwindowdialogclient, 3.0F, 0.5F, 4.0F, 1.6F, Plugin.i18n("mds.rearm"), null));
        this.bSeparate_Rearm = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 37.0F, 9.0F);
        this.bSeparate_Rearm.exclude = this.lSeparate_Rearm;

        // wZutiReload_OneMgCannonRearmSecond
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 2.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.mg"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 2.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_OneMgCannonRearmSecond = new GWindowEditControl(gwindowdialogclient, 11.0F, 2.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_OneRocketRearmSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 4.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.rocket"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 4.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_OneRocketRearmSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 4.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_OneBombFTankTorpedoeRearmSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 2.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.bomb"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 2.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_OneBombFTankTorpedoeRearmSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 2.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_LoadoutChangePenaltySeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 4.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.loadout"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 4.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_LoadoutChangePenaltySeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 4.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_OnlyHomeBaseSpecificLoadouts
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 6.0F, 36.0F, 1.3F, Plugin.i18n("mds.rearm.onlyHbLoadouts"), null));
        gwindowdialogclient.addControl(this.wZutiReload_OnlyHomeBaseSpecificLoadouts = new GWindowCheckBox(gwindowdialogclient, 35.0F, 6.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_ReloadOnlyIfAmmoBoxesExist
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 8.0F, 36.0F, 1.3F, Plugin.i18n("mds.rearm.ammo"), null));
        gwindowdialogclient.addControl(this.wZutiReload_ReloadOnlyIfAmmoBoxesExist = new GWindowCheckBox(gwindowdialogclient, 35.0F, 8.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_Refuel = new GWindowLabel(gwindowdialogclient, 3.0F, 11.5F, 4.0F, 1.6F, Plugin.i18n("mds.refuel"), null));
        this.bSeparate_Refuel = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 12.0F, 37.0F, 5.0F);
        this.bSeparate_Refuel.exclude = this.lSeparate_Refuel;

        // wZutiReload_GallonsLitersPerSecond
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 13.0F, 10.0F, 1.3F, Plugin.i18n("mds.refuel.rate1"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 35.0F, 13.0F, 12.0F, 1.3F, Plugin.i18n("mds.refuel.rate2"), null));
        gwindowdialogclient.addControl(this.wZutiReload_GallonsLitersPerSecond = new GWindowEditControl(gwindowdialogclient, 31.5F, 13.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_ReloadOnlyIfFuelTanksExist
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 15.0F, 35.0F, 1.3F, Plugin.i18n("mds.refuel.fuelTanks"), null));
        gwindowdialogclient.addControl(this.wZutiReload_ReloadOnlyIfFuelTanksExist = new GWindowCheckBox(gwindowdialogclient, 35.0F, 15.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lSeparate_Repair = new GWindowLabel(gwindowdialogclient, 3.0F, 18.5F, 4.0F, 1.6F, Plugin.i18n("mds.repair"), null));
        this.bSeparate_Repair = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 19.0F, 37.0F, 9.0F);
        this.bSeparate_Repair.exclude = this.lSeparate_Repair;

        // wZutiReload_EngineRepairSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 20.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.engine"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 20.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_EngineRepairSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 20.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_OneControlCableRepairSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 20.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.controlCable"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 20.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_OneControlCableRepairSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 20.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_FlapsRepairSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 22.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.flaps"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 22.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_FlapsRepairSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 22.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_OneWeaponRepairSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 22.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.mg"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 22.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_OneWeaponRepairSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 22.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_CockpitRepairSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 24.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.cockpit"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 14.5F, 24.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_CockpitRepairSeconds = new GWindowEditControl(gwindowdialogclient, 11.0F, 24.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_OneFuelOilTankRepairSeconds
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.0F, 24.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.tank"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 36.5F, 24.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient.addControl(this.wZutiReload_OneFuelOilTankRepairSeconds = new GWindowEditControl(gwindowdialogclient, 33.0F, 24.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // wZutiReload_RepairOnlyIfWorkshopExist
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 26.0F, 35.0F, 1.3F, Plugin.i18n("mds.repair.workshop"), null));
        gwindowdialogclient.addControl(this.wZutiReload_RepairOnlyIfWorkshopExist = new GWindowCheckBox(gwindowdialogclient, 35.0F, 26.0F, null) {
            public void preRender() {
                super.preRender();
                // setChecked(World.cur().isTimeOfDayConstant(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ZutiMDSSection_RRR.this.setMDSVariables();
                return false;
            }
        });

        // TODO:
        gwindowdialogclient.addLabel(this.lSeparate_Resources = new GWindowLabel(gwindowdialogclient, 3.0F, 29.5F, 14.0F, 1.6F, Plugin.i18n("mds.RRR.resourcesManagement"), null));
        this.bSeparate_Resources = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 30.0F, 37.0F, 9.0F);
        this.bSeparate_Resources.exclude = this.lSeparate_Resources;

        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 31.0F, 35.0F, 1.3F, Plugin.i18n("mds.RRR.enableResourcesMng"), null));
        gwindowdialogclient.addControl(this.wZutiReload_EnableResourcesManagement = new GWindowCheckBox(gwindowdialogclient, 35.0F, 31.0F, null) {
            public void preRender() {
                super.preRender();
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                return false;
            }
        });
        // RED team resources
        gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 33.0F, 35.0F, 2.0F, Plugin.i18n("mds.RRR.setResourcesRed"), null) {
            public void preRender() {
                super.preRender();

                this.setEnable(ZutiMDSSection_RRR.this.wZutiReload_EnableResourcesManagement.isChecked());
                if (!this.isEnable()) ZutiMDSSection_RRR.this.resourcesManagement_Red = null;
            }

            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                // TODO: Open new window here
                if (ZutiMDSSection_RRR.this.resourcesManagement_Red == null) ZutiMDSSection_RRR.this.resourcesManagement_Red = new Zuti_WResourcesManagement(1);

                if (ZutiMDSSection_RRR.this.resourcesManagement_Red.isVisible()) ZutiMDSSection_RRR.this.resourcesManagement_Red.hideWindow();
                else {
                    ZutiMDSSection_RRR.this.resourcesManagement_Red.setTitle(Plugin.i18n("mds.RRR.setResourcesRed"));
                    ZutiMDSSection_RRR.this.resourcesManagement_Red.countRRRObjects();
                    ZutiMDSSection_RRR.this.resourcesManagement_Red.showWindow();
                }

                return true;
            }
        });
        // BLUE team resources
        gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, 2.0F, 36.0F, 35.0F, 2.0F, Plugin.i18n("mds.RRR.setResourcesBlue"), null) {
            public void preRender() {
                super.preRender();

                this.setEnable(ZutiMDSSection_RRR.this.wZutiReload_EnableResourcesManagement.isChecked());
                if (!this.isEnable()) ZutiMDSSection_RRR.this.resourcesManagement_Blue = null;
            }

            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                // TODO: Open new window here
                if (ZutiMDSSection_RRR.this.resourcesManagement_Blue == null) ZutiMDSSection_RRR.this.resourcesManagement_Blue = new Zuti_WResourcesManagement(2);

                if (ZutiMDSSection_RRR.this.resourcesManagement_Blue.isVisible()) ZutiMDSSection_RRR.this.resourcesManagement_Blue.hideWindow();
                else {
                    ZutiMDSSection_RRR.this.resourcesManagement_Blue.setTitle(Plugin.i18n("mds.RRR.setResourcesBlue"));
                    ZutiMDSSection_RRR.this.resourcesManagement_Blue.countRRRObjects();
                    ZutiMDSSection_RRR.this.resourcesManagement_Blue.showWindow();
                }

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
        this.zutiReload_OneMgCannonRearmSecond = 10;
        this.zutiReload_OneBombFTankTorpedoeRearmSeconds = 25;
        this.zutiReload_OneRocketRearmSeconds = 20;
        this.zutiReload_GallonsLitersPerSecond = 3;
        this.zutiReload_OneWeaponRepairSeconds = 3;
        this.zutiReload_FlapsRepairSeconds = 30;
        this.zutiReload_EngineRepairSeconds = 90;
        this.zutiReload_CockpitRepairSeconds = 30;
        this.zutiReload_OneControlCableRepairSeconds = 15;
        this.zutiReload_OneFuelOilTankRepairSeconds = 20;
        this.zutiReload_LoadoutChangePenaltySeconds = 30;
        this.zutiReload_ReloadOnlyIfFuelTanksExist = false;
        this.zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
        this.zutiReload_RepairOnlyIfWorkshopExist = false;
        this.zutiReload_OnlyHomeBaseSpecificLoadouts = true;
    }

    /**
     * Reads MDS variables and sets their values to UI objects.
     */
    private void setUIVariables() {
        this.wZutiReload_OneMgCannonRearmSecond.setValue(new Integer(this.zutiReload_OneMgCannonRearmSecond).toString(), false);
        this.wZutiReload_OneBombFTankTorpedoeRearmSeconds.setValue(new Integer(this.zutiReload_OneBombFTankTorpedoeRearmSeconds).toString(), false);
        this.wZutiReload_OneRocketRearmSeconds.setValue(new Integer(this.zutiReload_OneRocketRearmSeconds).toString(), false);
        this.wZutiReload_GallonsLitersPerSecond.setValue(new Integer(this.zutiReload_GallonsLitersPerSecond).toString(), false);
        this.wZutiReload_OneWeaponRepairSeconds.setValue(new Integer(this.zutiReload_OneWeaponRepairSeconds).toString(), false);
        this.wZutiReload_FlapsRepairSeconds.setValue(new Integer(this.zutiReload_FlapsRepairSeconds).toString(), false);
        this.wZutiReload_EngineRepairSeconds.setValue(new Integer(this.zutiReload_EngineRepairSeconds).toString(), false);
        this.wZutiReload_OneControlCableRepairSeconds.setValue(new Integer(this.zutiReload_OneControlCableRepairSeconds).toString(), false);
        this.wZutiReload_OneFuelOilTankRepairSeconds.setValue(new Integer(this.zutiReload_OneFuelOilTankRepairSeconds).toString(), false);
        this.wZutiReload_LoadoutChangePenaltySeconds.setValue(new Integer(this.zutiReload_LoadoutChangePenaltySeconds).toString(), false);
        this.wZutiReload_CockpitRepairSeconds.setValue(new Integer(this.zutiReload_CockpitRepairSeconds).toString(), false);

        this.wZutiReload_ReloadOnlyIfFuelTanksExist.setChecked(this.zutiReload_ReloadOnlyIfFuelTanksExist, false);
        this.wZutiReload_ReloadOnlyIfAmmoBoxesExist.setChecked(this.zutiReload_ReloadOnlyIfAmmoBoxesExist, false);
        this.wZutiReload_RepairOnlyIfWorkshopExist.setChecked(this.zutiReload_RepairOnlyIfWorkshopExist, false);
        this.wZutiReload_OnlyHomeBaseSpecificLoadouts.setChecked(this.zutiReload_OnlyHomeBaseSpecificLoadouts, false);

    }

    /**
     * Reads UI objects values and stores them to MDS variables.
     */
    private void setMDSVariables() {
        this.zutiReload_OneMgCannonRearmSecond = Integer.parseInt(this.wZutiReload_OneMgCannonRearmSecond.getValue());
        this.zutiReload_OneBombFTankTorpedoeRearmSeconds = Integer.parseInt(this.wZutiReload_OneBombFTankTorpedoeRearmSeconds.getValue());
        this.zutiReload_OneRocketRearmSeconds = Integer.parseInt(this.wZutiReload_OneRocketRearmSeconds.getValue());
        this.zutiReload_GallonsLitersPerSecond = Integer.parseInt(this.wZutiReload_GallonsLitersPerSecond.getValue());
        this.zutiReload_OneWeaponRepairSeconds = Integer.parseInt(this.wZutiReload_OneWeaponRepairSeconds.getValue());
        this.zutiReload_FlapsRepairSeconds = Integer.parseInt(this.wZutiReload_FlapsRepairSeconds.getValue());
        this.zutiReload_EngineRepairSeconds = Integer.parseInt(this.wZutiReload_EngineRepairSeconds.getValue());
        this.zutiReload_OneControlCableRepairSeconds = Integer.parseInt(this.wZutiReload_OneControlCableRepairSeconds.getValue());
        this.zutiReload_OneFuelOilTankRepairSeconds = Integer.parseInt(this.wZutiReload_OneFuelOilTankRepairSeconds.getValue());
        this.zutiReload_LoadoutChangePenaltySeconds = Integer.parseInt(this.wZutiReload_LoadoutChangePenaltySeconds.getValue());
        this.zutiReload_CockpitRepairSeconds = Integer.parseInt(this.wZutiReload_CockpitRepairSeconds.getValue());

        this.zutiReload_ReloadOnlyIfFuelTanksExist = this.wZutiReload_ReloadOnlyIfFuelTanksExist.isChecked();
        this.zutiReload_ReloadOnlyIfAmmoBoxesExist = this.wZutiReload_ReloadOnlyIfAmmoBoxesExist.isChecked();
        this.zutiReload_RepairOnlyIfWorkshopExist = this.wZutiReload_RepairOnlyIfWorkshopExist.isChecked();
        this.zutiReload_OnlyHomeBaseSpecificLoadouts = this.wZutiReload_OnlyHomeBaseSpecificLoadouts.isChecked();

        PlMission.setChanged();
    }

    /**
     * Reads variables from specified mission file and corresponding MDS section.
     */
    public void loadVariables(SectFile sectfile) {
        try {
            this.zutiReload_OneMgCannonRearmSecond = sectfile.get(SECTION_ID, "ZutiReload_OneMgCannonRearmSecond", 10, 0, 99999);
            this.zutiReload_OneBombFTankTorpedoeRearmSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneBombFTankTorpedoeRearmSeconds", 25, 0, 99999);
            this.zutiReload_OneRocketRearmSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneRocketRearmSeconds", 20, 0, 99999);
            this.zutiReload_GallonsLitersPerSecond = sectfile.get(SECTION_ID, "ZutiReload_GallonsLitersPerSecond", 3, 0, 99999);
            this.zutiReload_OneWeaponRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneWeaponRepairSeconds", 3, 0, 99999);
            this.zutiReload_FlapsRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_FlapsRepairSeconds", 30, 0, 99999);
            this.zutiReload_EngineRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_EngineRepairSeconds", 90, 0, 99999);
            this.zutiReload_CockpitRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_CockpitRepairSeconds", 30, 0, 99999);
            this.zutiReload_OneControlCableRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneControlCableRepairSeconds", 15, 0, 99999);
            this.zutiReload_OneFuelOilTankRepairSeconds = sectfile.get(SECTION_ID, "ZutiReload_OneFuelOilTankRepairSeconds", 20, 0, 99999);
            this.zutiReload_LoadoutChangePenaltySeconds = sectfile.get(SECTION_ID, "ZutiReload_LoadoutChangePenaltySeconds", 30, 0, 99999);
            this.zutiReload_ReloadOnlyIfFuelTanksExist = false;
            if (sectfile.get(SECTION_ID, "ZutiReload_ReloadOnlyIfFuelTanksExist", 0, 0, 1) == 1) this.zutiReload_ReloadOnlyIfFuelTanksExist = true;
            this.zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
            if (sectfile.get(SECTION_ID, "ZutiReload_ReloadOnlyIfAmmoBoxesExist", 0, 0, 1) == 1) this.zutiReload_ReloadOnlyIfAmmoBoxesExist = true;
            this.zutiReload_RepairOnlyIfWorkshopExist = false;
            if (sectfile.get(SECTION_ID, "ZutiReload_RepairOnlyIfWorkshopExist", 0, 0, 1) == 1) this.zutiReload_RepairOnlyIfWorkshopExist = true;
            this.zutiReload_OnlyHomeBaseSpecificLoadouts = true;
            if (sectfile.get(SECTION_ID, "ZutiReload_OnlyHomeBaseSpecificLoadouts", 1, 0, 1) == 0) this.zutiReload_OnlyHomeBaseSpecificLoadouts = false;

            if (sectfile.sectionIndex(OLD_MDS_ID) > -1) this.loadVariablesFromOldSection(sectfile);

            this.setUIVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVariablesFromOldSection(SectFile sectfile) {
        try {
            this.zutiReload_OneMgCannonRearmSecond = sectfile.get(OLD_MDS_ID, "ZutiReload_OneMgCannonRearmSecond", 10, 0, 99999);
            this.zutiReload_OneBombFTankTorpedoeRearmSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneBombFTankTorpedoeRearmSeconds", 25, 0, 99999);
            this.zutiReload_OneRocketRearmSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneRocketRearmSeconds", 20, 0, 99999);
            this.zutiReload_GallonsLitersPerSecond = sectfile.get(OLD_MDS_ID, "ZutiReload_GallonsLitersPerSecond", 3, 0, 99999);
            this.zutiReload_OneWeaponRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneWeaponRepairSeconds", 3, 0, 99999);
            this.zutiReload_FlapsRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_FlapsRepairSeconds", 30, 0, 99999);
            this.zutiReload_EngineRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_EngineRepairSeconds", 90, 0, 99999);
            this.zutiReload_CockpitRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_CockpitRepairSeconds", 30, 0, 99999);
            this.zutiReload_OneControlCableRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneControlCableRepairSeconds", 15, 0, 99999);
            this.zutiReload_OneFuelOilTankRepairSeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_OneFuelOilTankRepairSeconds", 20, 0, 99999);
            this.zutiReload_LoadoutChangePenaltySeconds = sectfile.get(OLD_MDS_ID, "ZutiReload_LoadoutChangePenaltySeconds", 30, 0, 99999);
            this.zutiReload_ReloadOnlyIfFuelTanksExist = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiReload_ReloadOnlyIfFuelTanksExist", 0, 0, 1) == 1) this.zutiReload_ReloadOnlyIfFuelTanksExist = true;
            this.zutiReload_ReloadOnlyIfAmmoBoxesExist = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiReload_ReloadOnlyIfAmmoBoxesExist", 0, 0, 1) == 1) this.zutiReload_ReloadOnlyIfAmmoBoxesExist = true;
            this.zutiReload_RepairOnlyIfWorkshopExist = false;
            if (sectfile.get(OLD_MDS_ID, "ZutiReload_RepairOnlyIfWorkshopExist", 0, 0, 1) == 1) this.zutiReload_RepairOnlyIfWorkshopExist = true;
            this.zutiReload_OnlyHomeBaseSpecificLoadouts = true;
            if (sectfile.get(OLD_MDS_ID, "ZutiReload_OnlyHomeBaseSpecificLoadouts", 1, 0, 1) == 0) this.zutiReload_OnlyHomeBaseSpecificLoadouts = false;
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

            sectfile.lineAdd(sectionIndex, "ZutiReload_OneMgCannonRearmSecond", new Integer(this.zutiReload_OneMgCannonRearmSecond).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_OneBombFTankTorpedoeRearmSeconds", new Integer(this.zutiReload_OneBombFTankTorpedoeRearmSeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_OneRocketRearmSeconds", new Integer(this.zutiReload_OneRocketRearmSeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_GallonsLitersPerSecond", new Integer(this.zutiReload_GallonsLitersPerSecond).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_OneWeaponRepairSeconds", new Integer(this.zutiReload_OneWeaponRepairSeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_FlapsRepairSeconds", new Integer(this.zutiReload_FlapsRepairSeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_EngineRepairSeconds", new Integer(this.zutiReload_EngineRepairSeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_OneControlCableRepairSeconds", new Integer(this.zutiReload_OneControlCableRepairSeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_OneFuelOilTankRepairSeconds", new Integer(this.zutiReload_OneFuelOilTankRepairSeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_LoadoutChangePenaltySeconds", new Integer(this.zutiReload_LoadoutChangePenaltySeconds).toString());
            sectfile.lineAdd(sectionIndex, "ZutiReload_CockpitRepairSeconds", new Integer(this.zutiReload_CockpitRepairSeconds).toString());

            sectfile.lineAdd(sectionIndex, "ZutiReload_ReloadOnlyIfFuelTanksExist", ZutiSupportMethods.boolToInt(this.zutiReload_ReloadOnlyIfFuelTanksExist));
            sectfile.lineAdd(sectionIndex, "ZutiReload_ReloadOnlyIfAmmoBoxesExist", ZutiSupportMethods.boolToInt(this.zutiReload_ReloadOnlyIfAmmoBoxesExist));
            sectfile.lineAdd(sectionIndex, "ZutiReload_RepairOnlyIfWorkshopExist", ZutiSupportMethods.boolToInt(this.zutiReload_RepairOnlyIfWorkshopExist));
            sectfile.lineAdd(sectionIndex, "ZutiReload_OnlyHomeBaseSpecificLoadouts", ZutiSupportMethods.boolToInt(this.zutiReload_OnlyHomeBaseSpecificLoadouts));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}