/* 4.10.1 class */
package com.maddox.il2.builder;

import java.io.File;

import com.maddox.gwindow.GFileFilter;
import com.maddox.gwindow.GFileFilterName;
import com.maddox.gwindow.GNotifyListener;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFileBox;
import com.maddox.gwindow.GWindowFileBoxExec;
import com.maddox.gwindow.GWindowFileOpen;
import com.maddox.gwindow.GWindowFileSaveAs;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRootMenu;
import com.maddox.gwindow.GWindowTabDialogClient;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Land2DText;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.Statics;
import com.maddox.rts.HomePath;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

public class PlMission extends Plugin {
    protected static PlMission cur;
    protected int              missionArmy   = 1;
    private int                cloudType     = 0;
    private float              cloudHeight   = 1000.0F;
    private float              windDirection = 0.0F;
    private float              windVelocity  = 0.0F;
    private int                gust          = 0;
    private int                turbulence    = 0;
    private int                day           = 15;
    private int                month         = World.land().config.month;
    private int                year          = 1940;
    private String[]           _yearKey      = { "1930", "1931", "1932", "1933", "1934", "1935", "1936", "1937", "1938", "1939", "1940", "1941", "1942", "1943", "1944", "1945", "1946", "1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954",
            "1955", "1956", "1957", "1958", "1959", "1960" };
    private String[]           _dayKey       = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
    private String[]           _monthKey     = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    private boolean            bChanged      = false;
    private String             missionFileName;
    private boolean            bReload       = false;
    PlMapLoad                  pluginMapLoad;
    WConditions                wConditions;
    GWindowMenuItem            mConditions;
    GWindowMenuItem            viewBridge;
    GWindowMenuItem            viewRunaway;
    GWindowMenuItem            viewName;
    GWindowMenuItem            viewTime;
    GWindowMenuItem[]          viewArmy;
    private String             lastOpenFile;
    private GWindowMessageBox  _loadMessageBox;
    private String             _loadFileName;

    class WConditions extends GWindowFramed {
        GWindowEditControl  wTimeH;
        GWindowEditControl  wTimeM;
        GWindowComboControl wCloudType;
        GWindowEditControl  wCloudHeight;
        GWindowCheckBox     wTimeFix;
        GWindowCheckBox     wWeaponFix;
        GWindowComboControl wYear;
        GWindowComboControl wDay;
        GWindowComboControl wMonth;
        GWindowEditControl  wWindDirection;
        GWindowEditControl  wWindVelocity;
        GWindowComboControl wGust;
        GWindowComboControl wTurbulence;
        GWindowBoxSeparate  boxWindTable;
        GWindowLabel        wLabel0;
        GWindowLabel        wLabel1;
        GWindowLabel        wLabel2;
        GWindowLabel        wLabel3;
        GWindowLabel        wLabel4;
        GWindowLabel        wLabel5;
        GWindowLabel        wLabel6;
        GWindowLabel        wLabel7;
        GWindowLabel        wLabel8;
        GWindowLabel        wLabel9;
        GWindowLabel        wLabel10;
        GWindowLabel        wLabel00;
        GWindowLabel        wLabel11;
        GWindowLabel        wLabel22;
        GWindowLabel        wLabel33;
        GWindowLabel        wLabel44;
        GWindowLabel        wLabel55;
        GWindowLabel        wLabel66;
        GWindowLabel        wLabel77;
        GWindowLabel        wLabel88;
        GWindowLabel        wLabel99;
        GWindowLabel        wLabel1010;

        public void windowShown() {
            PlMission.this.mConditions.bChecked = true;
            super.windowShown();
        }

        public void windowHidden() {
            PlMission.this.mConditions.bChecked = false;
            super.windowHidden();
        }

        public void created() {
            this.bAlwaysOnTop = true;
            super.created();
            this.title = Plugin.i18n("MissionConditions");
            float f = 13.0F;
            this.clientWindow = this.create(new GWindowTabDialogClient());
            GWindowTabDialogClient gwindowtabdialogclient = (GWindowTabDialogClient) this.clientWindow;
            GWindowDialogClient gwindowdialogclient;
            gwindowtabdialogclient.addTab(Plugin.i18n("weather"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, f - 1.0F, 1.3F, Plugin.i18n("Weather"), null));
            gwindowdialogclient.addControl(this.wCloudType = new GWindowComboControl(gwindowdialogclient, f, 1.0F, 8.0F) {
                public boolean notify(int i, int i_4_) {
                    if (i == 2) WConditions.this.getCloudType();
                    return super.notify(i, i_4_);
                }
            });
            this.wCloudType.setEditable(false);
            this.wCloudType.add(Plugin.i18n("Clear"));
            this.wCloudType.add(Plugin.i18n("Good"));
            this.wCloudType.add(Plugin.i18n("Hazy"));
            this.wCloudType.add(Plugin.i18n("Poor"));
            this.wCloudType.add(Plugin.i18n("Blind"));
            this.wCloudType.add(Plugin.i18n("Rain/Snow"));
            this.wCloudType.add(Plugin.i18n("Thunder"));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, f - 1.0F, 1.3F, Plugin.i18n("CloudHeight"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 3.0F, 2.0F, 1.3F, Plugin.i18n("[m]"), null));
            gwindowdialogclient.addControl(this.wCloudHeight = new GWindowEditControl(gwindowdialogclient, f, 3.0F, 5.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    this.bNumericOnly = true;
                    this.bDelayedNotify = true;
                }

                public boolean notify(int i, int i_10_) {
                    if (i == 2) WConditions.this.getCloudHeight();
                    return super.notify(i, i_10_);
                }
            });
            this.wCloudHeight.setEditable(true);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13.0F, 11.0F, 1.6F, Plugin.i18n("WindTable"), null));
            this.boxWindTable = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 15.0F, 20.0F, 25.0F);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 16.0F, 9.0F, 1.3F, Plugin.i18n("Altitude[m]"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 10.0F, 16.0F, 9.0F, 1.3F, Plugin.i18n("WindSpeed[m/s]"), null));
            gwindowdialogclient.addLabel(this.wLabel0 = new GWindowLabel(gwindowdialogclient, 1.0F, 18.0F, 5.0F, 1.3F, "10", null));
            this.wLabel0.align = 2;
            gwindowdialogclient.addLabel(this.wLabel1 = new GWindowLabel(gwindowdialogclient, 1.0F, 20.0F, 5.0F, 1.3F, "1000", null));
            this.wLabel1.align = 2;
            gwindowdialogclient.addLabel(this.wLabel2 = new GWindowLabel(gwindowdialogclient, 1.0F, 22.0F, 5.0F, 1.3F, "2000", null));
            this.wLabel2.align = 2;
            gwindowdialogclient.addLabel(this.wLabel3 = new GWindowLabel(gwindowdialogclient, 1.0F, 24.0F, 5.0F, 1.3F, "3000", null));
            this.wLabel3.align = 2;
            gwindowdialogclient.addLabel(this.wLabel4 = new GWindowLabel(gwindowdialogclient, 1.0F, 26.0F, 5.0F, 1.3F, "4000", null));
            this.wLabel4.align = 2;
            gwindowdialogclient.addLabel(this.wLabel5 = new GWindowLabel(gwindowdialogclient, 1.0F, 28.0F, 5.0F, 1.3F, "5000", null));
            this.wLabel5.align = 2;
            gwindowdialogclient.addLabel(this.wLabel6 = new GWindowLabel(gwindowdialogclient, 1.0F, 30.0F, 5.0F, 1.3F, "6000", null));
            this.wLabel6.align = 2;
            gwindowdialogclient.addLabel(this.wLabel7 = new GWindowLabel(gwindowdialogclient, 1.0F, 32.0F, 5.0F, 1.3F, "7000", null));
            this.wLabel7.align = 2;
            gwindowdialogclient.addLabel(this.wLabel8 = new GWindowLabel(gwindowdialogclient, 1.0F, 34.0F, 5.0F, 1.3F, "8000", null));
            this.wLabel8.align = 2;
            gwindowdialogclient.addLabel(this.wLabel9 = new GWindowLabel(gwindowdialogclient, 1.0F, 36.0F, 5.0F, 1.3F, "9000", null));
            this.wLabel9.align = 2;
            gwindowdialogclient.addLabel(this.wLabel10 = new GWindowLabel(gwindowdialogclient, 1.0F, 38.0F, 5.0F, 1.3F, "10000", null));
            this.wLabel10.align = 2;
            gwindowdialogclient.addLabel(this.wLabel00 = new GWindowLabel(gwindowdialogclient, 10.0F, 18.0F, 5.0F, 1.3F, "", null));
            this.wLabel00.align = 2;
            gwindowdialogclient.addLabel(this.wLabel11 = new GWindowLabel(gwindowdialogclient, 10.0F, 20.0F, 5.0F, 1.3F, "", null));
            this.wLabel11.align = 2;
            gwindowdialogclient.addLabel(this.wLabel22 = new GWindowLabel(gwindowdialogclient, 10.0F, 22.0F, 5.0F, 1.3F, "", null));
            this.wLabel22.align = 2;
            gwindowdialogclient.addLabel(this.wLabel33 = new GWindowLabel(gwindowdialogclient, 10.0F, 24.0F, 5.0F, 1.3F, "", null));
            this.wLabel33.align = 2;
            gwindowdialogclient.addLabel(this.wLabel44 = new GWindowLabel(gwindowdialogclient, 10.0F, 26.0F, 5.0F, 1.3F, "", null));
            this.wLabel44.align = 2;
            gwindowdialogclient.addLabel(this.wLabel55 = new GWindowLabel(gwindowdialogclient, 10.0F, 28.0F, 5.0F, 1.3F, "", null));
            this.wLabel55.align = 2;
            gwindowdialogclient.addLabel(this.wLabel66 = new GWindowLabel(gwindowdialogclient, 10.0F, 30.0F, 5.0F, 1.3F, "", null));
            this.wLabel66.align = 2;
            gwindowdialogclient.addLabel(this.wLabel77 = new GWindowLabel(gwindowdialogclient, 10.0F, 32.0F, 5.0F, 1.3F, "", null));
            this.wLabel77.align = 2;
            gwindowdialogclient.addLabel(this.wLabel88 = new GWindowLabel(gwindowdialogclient, 10.0F, 34.0F, 5.0F, 1.3F, "", null));
            this.wLabel88.align = 2;
            gwindowdialogclient.addLabel(this.wLabel99 = new GWindowLabel(gwindowdialogclient, 10.0F, 36.0F, 5.0F, 1.3F, "", null));
            this.wLabel99.align = 2;
            gwindowdialogclient.addLabel(this.wLabel1010 = new GWindowLabel(gwindowdialogclient, 10.0F, 38.0F, 5.0F, 1.3F, "", null));
            this.wLabel1010.align = 2;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, f - 1.0F, 1.3F, Plugin.i18n("WindDirection"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 5.0F, 7.0F, 1.3F, Plugin.i18n("[deg]"), null));
            gwindowdialogclient.addControl(this.wWindDirection = new GWindowEditControl(gwindowdialogclient, f, 5.0F, 5.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    this.bNumericOnly = true;
                    this.bDelayedNotify = true;
                }

                public boolean notify(int i, int i_16_) {
                    if (i == 2) WConditions.this.getWindDirection();
                    return super.notify(i, i_16_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, f - 1.0F, 1.3F, Plugin.i18n("WindVelocity"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 7.0F, 7.0F, 1.3F, Plugin.i18n("[m/s]"), null));
            gwindowdialogclient.addControl(this.wWindVelocity = new GWindowEditControl(gwindowdialogclient, f, 7.0F, 5.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    this.bNumericOnly = true;
                    this.bDelayedNotify = true;
                }

                public boolean notify(int i, int i_22_) {
                    if (i == 2) WConditions.this.getWindVelocity();
                    return super.notify(i, i_22_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, f - 1.0F, 1.3F, Plugin.i18n("Gust"), null));
            gwindowdialogclient.addControl(this.wGust = new GWindowComboControl(gwindowdialogclient, f, 9.0F, 8.0F) {
                public boolean notify(int i, int i_27_) {
                    if (i == 2) WConditions.this.getGust();
                    return super.notify(i, i_27_);
                }
            });
            this.wGust.setEditable(false);
            this.wGust.add(Plugin.i18n("None"));
            this.wGust.add(Plugin.i18n("Low"));
            this.wGust.add(Plugin.i18n("Moderate"));
            this.wGust.add(Plugin.i18n("Strong"));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11.0F, f - 1.0F, 1.3F, Plugin.i18n("Turbulence"), null));
            gwindowdialogclient.addControl(this.wTurbulence = new GWindowComboControl(gwindowdialogclient, f, 11.0F, 8.0F) {
                public boolean notify(int i, int i_32_) {
                    if (i == 2) WConditions.this.getTurbulence();
                    return super.notify(i, i_32_);
                }
            });
            this.wTurbulence.setEditable(false);
            this.wTurbulence.add(Plugin.i18n("None"));
            this.wTurbulence.add(Plugin.i18n("Low"));
            this.wTurbulence.add(Plugin.i18n("Moderate"));
            this.wTurbulence.add(Plugin.i18n("Strong"));
            this.wTurbulence.add(Plugin.i18n("VeryStrong"));
            gwindowtabdialogclient.addTab(Plugin.i18n("Season"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            f = 10.0F;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, f - 1.0F, 1.3F, Plugin.i18n("Time"), null));
            gwindowdialogclient.addControl(this.wTimeH = new GWindowEditControl(gwindowdialogclient, f, 1.0F, 2.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    this.bNumericOnly = true;
                    this.bDelayedNotify = true;
                }

                public boolean notify(int i, int i_38_) {
                    if (i != 2) return false;
                    WConditions.this.getTime();
                    return false;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 2.15F, 1.0F, 1.0F, 1.3F, ":", null));
            gwindowdialogclient.addControl(this.wTimeM = new GWindowEditControl(gwindowdialogclient, f + 2.5F, 1.0F, 2.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    this.bNumericOnly = true;
                    this.bDelayedNotify = true;
                }

                public boolean notify(int i, int i_44_) {
                    if (i != 2) return false;
                    WConditions.this.getTime();
                    return false;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, f - 1.0F, 1.3F, Plugin.i18n("Day"), null));
            gwindowdialogclient.addControl(this.wDay = new GWindowComboControl(gwindowdialogclient, f, 3.0F, 5.0F) {
                public boolean notify(int i, int i_49_) {
                    if (i == 2) WConditions.this.getDay();
                    return super.notify(i, i_49_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, f - 1.0F, 1.3F, Plugin.i18n("Month"), null));
            gwindowdialogclient.addControl(this.wMonth = new GWindowComboControl(gwindowdialogclient, f, 5.0F, 5.0F) {
                public boolean notify(int i, int i_54_) {
                    if (i == 2) WConditions.this.getMonth();
                    return super.notify(i, i_54_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, f - 1.0F, 1.3F, Plugin.i18n("Year"), null));
            gwindowdialogclient.addControl(this.wYear = new GWindowComboControl(gwindowdialogclient, f, 7.0F, 5.0F) {
                public boolean notify(int i, int i_59_) {
                    if (i == 2) WConditions.this.getYear();
                    return super.notify(i, i_59_);
                }
            });
            this.wDay.setEditable(false);
            this.wYear.setEditable(false);
            this.wMonth.setEditable(false);
            for (int i = 0; i < PlMission.this._dayKey.length; i++)
                this.wDay.add(PlMission.this._dayKey[i]);
            for (int i = 0; i < PlMission.this._monthKey.length; i++)
                this.wMonth.add(PlMission.this._monthKey[i]);
            for (int i = 0; i < PlMission.this._yearKey.length; i++)
                this.wYear.add(PlMission.this._yearKey[i]);
            gwindowtabdialogclient.addTab(Plugin.i18n("misc"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 12.0F, 1.3F, Plugin.i18n("timeLocked"), null));
            gwindowdialogclient.addControl(this.wTimeFix = new GWindowCheckBox(gwindowdialogclient, 14.0F, 1.0F, null) {
                public void preRender() {
                    super.preRender();
                    this.setChecked(World.cur().isTimeOfDayConstant(), false);
                }

                public boolean notify(int i, int i_63_) {
                    if (i != 2) return false;
                    World.cur().setTimeOfDayConstant(this.isChecked());
                    setChanged();
                    return false;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 12.0F, 1.3F, Plugin.i18n("weaponsLocked"), null));
            gwindowdialogclient.addControl(this.wWeaponFix = new GWindowCheckBox(gwindowdialogclient, 14.0F, 3.0F, null) {
                public void preRender() {
                    super.preRender();
                    this.setChecked(World.cur().isWeaponsConstant(), false);
                }

                public boolean notify(int i, int i_67_) {
                    if (i != 2) return false;
                    World.cur().setWeaponsConstant(this.isChecked());
                    setChanged();
                    return false;
                }
            });
        }

        public void update() {
            float f = World.getTimeofDay();
            int i = (int) f % 24;
            int i_68_ = (int) (60.0F * (f - (int) f));
            this.wTimeH.setValue("" + i, false);
            this.wTimeM.setValue("" + i_68_, false);
            this.wCloudType.setSelected(PlMission.this.cloudType, true, false);
            int i_69_ = (int) PlMission.this.cloudHeight;
            this.wCloudHeight.setValue("" + i_69_, false);
            this.wTimeFix.setChecked(World.cur().isTimeOfDayConstant(), false);
            this.wWeaponFix.setChecked(World.cur().isWeaponsConstant(), false);
            this.wDay.setValue("" + PlMission.this.day, false);
            this.wMonth.setValue("" + PlMission.this.month, false);
            this.wYear.setValue("" + PlMission.this.year, false);
            this.wWindDirection.setValue("" + PlMission.this.windDirection, false);
            this.wWindVelocity.setValue("" + PlMission.this.windVelocity, false);
            int i_70_ = PlMission.this.gust;
            int i_71_ = PlMission.this.turbulence;
            if (PlMission.this.gust > 0) i_70_ = (PlMission.this.gust - 6) / 2;
            this.wGust.setSelected(i_70_, true, false);
            if (PlMission.this.turbulence > 0) i_71_ = PlMission.this.turbulence - 2;
            this.wTurbulence.setSelected(i_71_, true, false);
            this.calcWindTable(PlMission.this.cloudType, PlMission.this.cloudHeight, PlMission.this.windVelocity);
        }

        public void calcWindTable(int i, float f, float f_72_) {
            float f_73_ = f_72_;
            if (f_73_ == 0.0F) f_73_ = 0.25F + this.wCloudType.getSelected() * this.wCloudType.getSelected() * 0.12F;
            float f_74_ = f + 300.0F;
            float f_75_ = f_74_ / 2.0F;
            float f_76_ = f_73_ * f_75_ / 3000.0F + f_73_;
            float f_77_ = f_73_ * (f_74_ - f_75_) / 9000.0F + f_76_;
            int[] is = new int[11];
            for (int i_78_ = 0; i_78_ <= 10; i_78_++) {
                int i_79_ = i_78_ * 1000;
                if (i_79_ > f_74_) f_73_ = f_77_ + (i_79_ - f_74_) * f_73_ / 18000.0F;
                // TODO: Implement 4.10.1 Codechanges +++
                else
                    // TODO: Implement 4.10.1 Codechanges ---
                    if (i_79_ > f_75_) f_73_ = f_76_ + (i_79_ - f_75_) * f_73_ / 9000.0F;
                    else if (i_79_ > 10.0F) f_73_ += f_73_ * i_79_ / 3000.0F;
                if (!(i_79_ <= 10.0F)) {
                    /* empty */
                }
                is[i_78_] = (int) f_73_;
            }
            this.wLabel00.cap.set("" + is[0]);
            this.wLabel11.cap.set("" + is[1]);
            this.wLabel22.cap.set("" + is[2]);
            this.wLabel33.cap.set("" + is[3]);
            this.wLabel44.cap.set("" + is[4]);
            this.wLabel55.cap.set("" + is[5]);
            this.wLabel66.cap.set("" + is[6]);
            this.wLabel77.cap.set("" + is[7]);
            this.wLabel88.cap.set("" + is[8]);
            this.wLabel99.cap.set("" + is[9]);
            this.wLabel1010.cap.set("" + is[10]);
        }

        public void getTime() {
            String string = this.wTimeH.getValue();
            double d = 0.0;
            try {
                d = Double.parseDouble(string);
            } catch (Exception exception) {
                /* empty */
            }
            if (d < 0.0) d = 0.0;
            if (d > 23.0) d = 23.0;
            string = this.wTimeM.getValue();
            double d_80_ = 0.0;
            try {
                d_80_ = Double.parseDouble(string);
            } catch (Exception exception) {
                /* empty */
            }
            if (d_80_ < 0.0) d_80_ = 0.0;
            if (d_80_ >= 60.0) d_80_ = 59.0;
            float f = (float) (d + d_80_ / 60.0);
            if ((int) (f * 60.0F) != (int) (World.getTimeofDay() * 60.0F)) {
                World.setTimeofDay(f);
                if (Plugin.builder.isLoadedLandscape()) World.land().cubeFullUpdate();
            }
            setChanged();
            this.update();
        }

        public void getCloudType() {
            PlMission.this.cloudType = this.wCloudType.getSelected();
            Mission.setCloudsType(PlMission.this.cloudType);
            setChanged();
            this.update();
        }

        public void getCloudHeight() {
            try {
                PlMission.this.cloudHeight = Float.parseFloat(this.wCloudHeight.getValue());
            } catch (Exception exception) {
                /* empty */
            }
            if (PlMission.this.cloudHeight < 300.0F) PlMission.this.cloudHeight = 300.0F;
            if (PlMission.this.cloudHeight > 5000.0F) PlMission.this.cloudHeight = 5000.0F;
            Mission.setCloudsHeight(PlMission.this.cloudHeight);
            setChanged();
            this.update();
        }

        public void getYear() {
            PlMission.this.year = Integer.parseInt(this.wYear.getValue());
            Mission.setYear(PlMission.this.year);
            setChanged();
        }

        public void getDay() {
            PlMission.this.day = Integer.parseInt(this.wDay.getValue());
            Mission.setDay(PlMission.this.day);
            setChanged();
        }

        public void getMonth() {
            PlMission.this.month = Integer.parseInt(this.wMonth.getValue());
            Mission.setMonth(PlMission.this.month);
            setChanged();
        }

        public void getWindDirection() {
            try {
                PlMission.this.windDirection = Float.parseFloat(this.wWindDirection.getValue());
            } catch (Exception exception) {
                /* empty */
            }
            if (PlMission.this.windDirection < 0.0F) PlMission.this.windDirection = 0.0F;
            if (PlMission.this.windDirection >= 360.0F) PlMission.this.windDirection = 0.0F;
            Mission.setWindDirection(PlMission.this.windDirection);
            setChanged();
            this.update();
        }

        public void getWindVelocity() {
            try {
                PlMission.this.windVelocity = Float.parseFloat(this.wWindVelocity.getValue());
            } catch (Exception exception) {
                /* empty */
            }
            if (PlMission.this.windVelocity > 15.0F) PlMission.this.windVelocity = 15.0F;
            if (PlMission.this.windVelocity < 0.0F) PlMission.this.windVelocity = 0.0F;
            Mission.setWindVelocity(PlMission.this.windVelocity);
            setChanged();
            this.update();
        }

        public void getGust() {
            PlMission.this.gust = this.wGust.getSelected();
            if (PlMission.this.gust > 0) PlMission.this.gust = PlMission.this.gust * 2 + 6;
            float f = PlMission.this.gust * 1.0F;
            Mission.setGust(f);
            setChanged();
        }

        public void getTurbulence() {
            PlMission.this.turbulence = this.wTurbulence.getSelected();
            if (PlMission.this.turbulence > 0) incTurbulence(PlMission.this, 2);
            float f = PlMission.this.turbulence * 1.0F;
            Mission.setTurbulence(f);
            setChanged();
        }

        public void afterCreated() {
            super.afterCreated();
            this.resized();
            this.close(false);
        }

        public WConditions() {
            this.doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 23.0F, 45.0F, true);
            this.bSizable = true;
        }
    }

    class DlgFileConfirmSave extends GWindowFileBoxExec {
        GWindowFileBox box;
        boolean        bClose = true;

        public boolean isCloseBox() {
            return this.bClose;
        }

        public void exec(GWindowFileBox gwindowfilebox, String string) {
            this.box = gwindowfilebox;
            this.bClose = true;
            if (string == null || this.box.files.size() == 0) this.box.endExec();
            else {
                int i = string.lastIndexOf("/");
                if (i >= 0) string = string.substring(i + 1);
                for (int i_222_ = 0; i_222_ < this.box.files.size(); i_222_++) {
                    String string_223_ = ((File) this.box.files.get(i_222_)).getName();
                    if (string.compareToIgnoreCase(string_223_) == 0) {
                        new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, I18N.gui("warning.Warning"), I18N.gui("warning.ReplaceFile"), 1, 0.0F) {
                            public void result(int i_229_) {
                                if (i_229_ != 3) DlgFileConfirmSave.this.bClose = false;
                                DlgFileConfirmSave.this.box.endExec();
                            }
                        };
                        return;
                    }
                }
                this.box.endExec();
            }
        }
    }

    static class GWindowMenuItemArmy extends GWindowMenuItem {
        int army;

        public GWindowMenuItemArmy(GWindowMenu gwindowmenu, String string, String string_230_, int i) {
            super(gwindowmenu, string, string_230_);
            this.army = i;
        }
    }

    public static void setChanged() {
        if (cur != null) cur.bChanged = true;
    }

    public static boolean isChanged() {
        if (cur != null) return cur.bChanged;
        return false;
    }

    public static String missionFileName() {
        if (cur == null) return null;
        return cur.missionFileName;
    }

    public static void doMissionReload() {
        if (cur != null && cur.bReload) {
            cur.bReload = false;
            cur.doLoadMission("missions/" + cur.missionFileName);
        }
    }

    public boolean load(String string) {
        builder.deleteAll();
        SectFile sectfile = new SectFile(string, 0);
        int i = sectfile.sectionIndex("MAIN");
        if (i < 0) {
            builder.tipErr("MissionLoad: '" + string + "' - section MAIN not found");
            return false;
        }
        int i_231_ = sectfile.varIndex(i, "MAP");
        if (i_231_ < 0) {
            builder.tipErr("MissionLoad: '" + string + "' - in section MAIN line MAP not found");
            return false;
        }
        String string_232_ = sectfile.value(i, i_231_);
        PlMapLoad.Land land = PlMapLoad.getLandForFileName(string_232_);
        if (land == PlMapLoad.getLandLoaded()) {
            World.cur().statics.restoreAllBridges();
            World.cur().statics.restoreAllHouses();
        } else if (!this.pluginMapLoad.mapLoad(land)) {
            builder.tipErr("MissionLoad: '" + string + "' - tirrain '" + string_232_ + "' not loaded");
            return false;
        }
        int i_233_ = sectfile.get("MAIN", "TIMECONSTANT", 0, 0, 1);
        World.cur().setTimeOfDayConstant(i_233_ == 1);
        World.setTimeofDay(sectfile.get("MAIN", "TIME", 12.0F, 0.0F, 23.99F));
        int i_234_ = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
        World.cur().setWeaponsConstant(i_234_ == 1);
        String string_235_ = sectfile.get("MAIN", "player", (String) null);
        Path.playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
        this.missionArmy = sectfile.get("MAIN", "army", 1, 1, 2);
        this.year = sectfile.get("SEASON", "Year", 1940, 1930, 1960);
        this.month = sectfile.get("SEASON", "Month", World.land().config.month, 1, 12);
        this.day = sectfile.get("SEASON", "Day", 15, 1, 31);
        this.windDirection = sectfile.get("WEATHER", "WindDirection", 0.0F, 0.0F, 359.99F);
        this.windVelocity = sectfile.get("WEATHER", "WindSpeed", 0.0F, 0.0F, 15.0F);
        this.gust = sectfile.get("WEATHER", "Gust", 0, 0, 12);
        this.turbulence = sectfile.get("WEATHER", "Turbulence", 0, 0, 6);
        this.cloudType = sectfile.get("MAIN", "CloudType", 0, 0, 6);
        this.cloudHeight = sectfile.get("MAIN", "CloudHeight", 1000.0F, 300.0F, 5000.0F);
        Mission.createClouds(this.cloudType, this.cloudHeight);
        if (Main3D.cur3D().clouds != null) Main3D.cur3D().clouds.setShow(false);
        Main3D.cur3D().spritesFog.setShow(false);
        this.wConditions.update();
        Plugin.doLoad(sectfile);
        if (string_235_ != null) {
            Object[] objects = builder.pathes.getOwnerAttached();
            for (int i_236_ = 0; i_236_ < objects.length; i_236_++) {
                Path path = (Path) objects[i_236_];
                if (string_235_.equals(path.name())) {
                    if (!((PathAir) path).bOnlyAI) {
                        Path.player = path;
                        this.missionArmy = path.getArmy();
                    }
                    break;
                }
            }
        }
        builder.repaint();
        this.bChanged = false;
        return true;
    }

    public boolean save(String string) {
        if (PlMapLoad.getLandLoaded() == null) {
            builder.tipErr("MissionSave: tirrain not selected");
            return false;
        }
        SectFile sectfile = new SectFile();
        sectfile.setFileName(string);
        int i = sectfile.sectionAdd("MAIN");
        sectfile.lineAdd(i, "MAP", PlMapLoad.mapFileName());
        sectfile.lineAdd(i, "TIME", "" + World.getTimeofDay());
        if (World.cur().isTimeOfDayConstant()) sectfile.lineAdd(i, "TIMECONSTANT", "1");
        if (World.cur().isWeaponsConstant()) sectfile.lineAdd(i, "WEAPONSCONSTANT", "1");
        sectfile.lineAdd(i, "CloudType", "" + this.cloudType);
        sectfile.lineAdd(i, "CloudHeight", "" + this.cloudHeight);
        if (Actor.isValid(Path.player)) {
            sectfile.lineAdd(i, "player", Path.player.name());
            if (Path.playerNum >= ((PathAir) Path.player).planes) Path.playerNum = 0;
        } else Path.playerNum = 0;
        sectfile.lineAdd(i, "army", "" + this.missionArmy);
        sectfile.lineAdd(i, "playerNum", "" + Path.playerNum);
        int i_237_ = sectfile.sectionAdd("SEASON");
        sectfile.lineAdd(i_237_, "Year", "" + this.year);
        sectfile.lineAdd(i_237_, "Month", "" + this.month);
        sectfile.lineAdd(i_237_, "Day", "" + this.day);
        int i_238_ = sectfile.sectionAdd("WEATHER");
        sectfile.lineAdd(i_238_, "WindDirection", "" + this.windDirection);
        sectfile.lineAdd(i_238_, "WindSpeed", "" + this.windVelocity);
        sectfile.lineAdd(i_238_, "Gust", "" + this.gust);
        sectfile.lineAdd(i_238_, "Turbulence", "" + this.turbulence);
        if (!Plugin.doSave(sectfile)) return false;
        sectfile.saveFile(string);
        this.bChanged = false;
        return true;
    }

    public void mapLoaded() {
        if (builder.isLoadedLandscape()) {
            String string = "maps/" + PlMapLoad.mapFileName();
            SectFile sectfile = new SectFile(string);
            int i = sectfile.sectionIndex("static");
            if (i >= 0 && sectfile.vars(i) > 0) {
                String string_239_ = sectfile.var(i, 0);
                Statics.load(HomePath.concatNames(string, string_239_), PlMapLoad.bridgeActors);
            }
            int i_240_ = sectfile.sectionIndex("text");
            if (i_240_ >= 0 && sectfile.vars(i_240_) > 0) {
                String string_241_ = sectfile.var(i_240_, 0);
                if (Main3D.cur3D().land2DText == null) Main3D.cur3D().land2DText = new Land2DText();
                else Main3D.cur3D().land2DText.clear();
                Main3D.cur3D().land2DText.load(HomePath.concatNames(string, string_241_));
            }
            Statics.trim();
            Landscape landscape = World.land();
            if (landscape != null) {
                /* empty */
            }
            if (Landscape.isExistMeshs()) for (int i_242_ = 0; i_242_ < PathFind.tShip.sy; i_242_++)
                for (int i_243_ = 0; i_243_ < PathFind.tShip.sx; i_243_++) {
                    if (landscape != null) {
                        /* empty */
                    }
                    if (Landscape.isExistMesh(i_243_, PathFind.tShip.sy - i_242_ - 1)) {
                        PathFind.tShip.I(i_243_, i_242_, PathFind.tShip.intI(i_243_, i_242_) | 0x8);
                        PathFind.tNoShip.I(i_243_, i_242_, PathFind.tNoShip.intI(i_243_, i_242_) | 0x8);
                    }
                }
            Mission.createClouds(this.cloudType, this.cloudHeight);
            if (Main3D.cur3D().clouds != null) Main3D.cur3D().clouds.setShow(false);
            Main3D.cur3D().spritesFog.setShow(false);
            this.wConditions.update();
        }
    }

    public void configure() {
        builder.bMultiSelect = false;
        if (getPlugin("MapLoad") == null) throw new RuntimeException("PlMission: plugin 'MapLoad' not found");
        this.pluginMapLoad = (PlMapLoad) getPlugin("MapLoad");
    }

    void _viewTypeAll(boolean bool) {
        Plugin.doViewTypeAll(bool);
        this.viewBridge(bool);
        this.viewRunaway(bool);
        this.viewName.bChecked = builder.conf.bShowName = bool;
        this.viewTime.bChecked = builder.conf.bShowTime = bool;
        for (int i = 0; i < builder.conf.bShowArmy.length; i++)
            this.viewArmy[i].bChecked = builder.conf.bShowArmy[i] = bool;
        if (!bool) builder.setSelected(null);
    }

    void viewBridge(boolean bool) {
        builder.conf.bViewBridge = bool;
        this.viewBridge.bChecked = builder.conf.bViewBridge;
    }

    void viewBridge() {
        this.viewBridge(!builder.conf.bViewBridge);
    }

    void viewRunaway(boolean bool) {
        builder.conf.bViewRunaway = bool;
        this.viewRunaway.bChecked = builder.conf.bViewRunaway;
    }

    void viewRunaway() {
        this.viewRunaway(!builder.conf.bViewRunaway);
    }

    public static void checkShowCurrentArmy() {
        Actor actor = builder.selectedPath();
        if (actor == null) actor = builder.selectedActor();
        if (actor != null) {
            int i = actor.getArmy();
            if (!builder.conf.bShowArmy[i]) builder.setSelected(null);
        }
    }

    private String checkMisExtension(String string) {
        if (!string.toLowerCase().endsWith(".mis")) return string + ".mis";
        return string;
    }

    public void createGUI() {
        builder.mDisplayFilter.subMenu.addItem("-", null);
        this.viewBridge = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showBridge"), i18n("TIPshowBridge")) {
            public void execute() {
                PlMission.this.viewBridge();
            }
        });
        this.viewBridge.bChecked = builder.conf.bViewBridge;
        this.viewRunaway = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showRunway"), i18n("TIPshowRunway")) {
            public void execute() {
                PlMission.this.viewRunaway();
            }
        });
        this.viewRunaway.bChecked = builder.conf.bViewRunaway;
        builder.mDisplayFilter.subMenu.addItem("-", null);
        this.viewName = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showName"), i18n("TIPshowName")) {
            public void execute() {
                this.bChecked = Plugin.builder.conf.bShowName = !Plugin.builder.conf.bShowName;
            }
        });
        this.viewName.bChecked = builder.conf.bShowName;
        this.viewTime = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showTime"), i18n("TIPshowTime")) {
            public void execute() {
                this.bChecked = Plugin.builder.conf.bShowTime = !Plugin.builder.conf.bShowTime;
            }
        });
        this.viewTime.bChecked = builder.conf.bShowTime;
        this.viewArmy = new GWindowMenuItemArmy[Builder.armyAmount()];
        for (int i = 0; i < Builder.armyAmount(); i++) {
            this.viewArmy[i] = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItemArmy(builder.mDisplayFilter.subMenu, i18n("showArmy") + " " + I18N.army(Army.name(i)), i18n("TIPshowArmy") + " " + I18N.army(Army.name(i)), i) {
                public void execute() {
                    this.bChecked = Plugin.builder.conf.bShowArmy[this.army] = !Plugin.builder.conf.bShowArmy[this.army];
                    checkShowCurrentArmy();
                }
            });
            this.viewArmy[i].bChecked = builder.conf.bShowArmy[i];
        }
        builder.mDisplayFilter.subMenu.addItem("-", null);
        builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("&ShowAll"), i18n("TIPShowAll")) {
            public void execute() {
                PlMission.this._viewTypeAll(true);
            }
        });
        builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("&HideAll"), i18n("TIPHideAll")) {
            public void execute() {
                PlMission.this._viewTypeAll(false);
            }
        });
        builder.mFile.subMenu.addItem(1, new GWindowMenuItem(builder.mFile.subMenu, i18n("Load"), i18n("TIPLoad")) {
            public void execute() {
                PlMission.this.doDlgLoadMission();
            }
        });
        builder.mFile.subMenu.addItem(2, new GWindowMenuItem(builder.mFile.subMenu, i18n("Save"), i18n("TIPSaveAs")) {
            public void execute() {
                if (PlMission.this.missionFileName != null) PlMission.this.save("missions/" + PlMission.this.missionFileName);
                else {
                    GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(this.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                        public void result(String string) {
                            if (string != null) {
                                string = PlMission.this.checkMisExtension(string);
                                PlMission.this.missionFileName = string;
                                ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
                                PlMission.this.lastOpenFile = string;
                                PlMission.this.save("missions/" + string);
                            }
                        }
                    };
                    gwindowfilesaveas.exec = new DlgFileConfirmSave();
                    if (PlMission.this.lastOpenFile != null) gwindowfilesaveas.setSelectFile(PlMission.this.lastOpenFile);
                }
            }
        });
        builder.mFile.subMenu.addItem(3, new GWindowMenuItem(builder.mFile.subMenu, i18n("SaveAs"), i18n("TIPSaveAs")) {
            public void execute() {
                GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(this.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                    public void result(String string) {
                        if (string != null) {
                            string = PlMission.this.checkMisExtension(string);
                            PlMission.this.missionFileName = string;
                            ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
                            PlMission.this.lastOpenFile = string;
                            PlMission.this.save("missions/" + string);
                        }
                    }
                };
                gwindowfilesaveas.exec = new DlgFileConfirmSave();
                if (PlMission.this.lastOpenFile != null) gwindowfilesaveas.setSelectFile(PlMission.this.lastOpenFile);
            }
        });
        builder.mFile.subMenu.addItem(4, new GWindowMenuItem(builder.mFile.subMenu, i18n("Play"), i18n("TIPPlay")) {
            public void execute() {
                if (Plugin.builder.isLoadedLandscape()) if (isChanged() || PlMission.this.missionFileName == null) {
                    if (PlMission.this.missionFileName != null) {
                        if (PlMission.this.save("missions/" + PlMission.this.missionFileName)) PlMission.this.playMission();
                    } else {
                        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(this.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                            public void result(String string) {
                                if (string != null) {
                                    string = PlMission.this.checkMisExtension(string);
                                    PlMission.this.missionFileName = string;
                                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
                                    PlMission.this.lastOpenFile = string;
                                    if (PlMission.this.save("missions/" + string)) PlMission.this.playMission();
                                }
                            }
                        };
                        gwindowfilesaveas.exec = new DlgFileConfirmSave();
                        if (PlMission.this.lastOpenFile != null) gwindowfilesaveas.setSelectFile(PlMission.this.lastOpenFile);
                    }
                } else PlMission.this.playMission();
            }
        });
        builder.mFile.subMenu.bNotify = true;
        builder.mFile.subMenu.addNotifyListener(new GNotifyListener() {
            public boolean notify(GWindow gwindow, int i, int i_274_) {
                if (i != 13) return false;
                Plugin.builder.mFile.subMenu.getItem(2).bEnable = Plugin.builder.isLoadedLandscape();
                Plugin.builder.mFile.subMenu.getItem(3).bEnable = Plugin.builder.isLoadedLandscape();
                Plugin.builder.mFile.subMenu.getItem(4).bEnable = Plugin.builder.isLoadedLandscape();
                return false;
            }
        });
        this.mConditions = builder.mConfigure.subMenu.addItem(0, new GWindowMenuItem(builder.mConfigure.subMenu, i18n("&Conditions"), i18n("TIPConditions")) {
            public void execute() {
                if (PlMission.this.wConditions.isVisible()) PlMission.this.wConditions.hideWindow();
                else PlMission.this.wConditions.showWindow();
            }
        });

        builder.mEdit.subMenu.addItem(0, "-", null);
        this.wConditions = new WConditions();
        this.wConditions.update();
    }

    private void doLoadMission(String string) {
        this._loadFileName = string;
        this._loadMessageBox = new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("StandBy"), i18n("LoadingMission"), 4, 0.0F);
        new MsgAction(72, 0.0) {
            public void doAction() {
                PlMission.this.load(PlMission.this._loadFileName);
                PlMission.this._loadMessageBox.close(false);
            }
        };
    }

    private void playMission() {
        Main.cur().currentMissionFile = new SectFile("missions/" + this.missionFileName, 0);
        this.bReload = true;
        Main.stateStack().push(4);
    }

    private void doDlgLoadMission() {
        if (!isChanged()) this._doDlgLoadMission();
        else new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("LoadMission"), i18n("ConfirmExitMsg"), 1, 0.0F) {
            public void result(int i) {
                if (i == 3) {
                    if (PlMission.this.missionFileName != null) {
                        PlMission.this.save("missions/" + PlMission.this.missionFileName);
                        PlMission.this._doDlgLoadMission();
                    } else {
                        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(this.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                            public void result(String string) {
                                if (string != null) {
                                    string = PlMission.this.checkMisExtension(string);
                                    PlMission.this.missionFileName = string;
                                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
                                    PlMission.this.lastOpenFile = string;
                                    PlMission.this.save("missions/" + string);
                                }
                                PlMission.this._doDlgLoadMission();
                            }
                        };
                        gwindowfilesaveas.exec = new DlgFileConfirmSave();
                        if (PlMission.this.lastOpenFile != null) gwindowfilesaveas.setSelectFile(PlMission.this.lastOpenFile);
                    }
                } else PlMission.this._doDlgLoadMission();
            }
        };
    }

    private void _doDlgLoadMission() {
        GWindowFileOpen gwindowfileopen = new GWindowFileOpen(builder.clientWindow.root, true, i18n("LoadMission"), "missions", new GFileFilter[] { new GFileFilterName(i18n("MissionFiles"), new String[] { "*.mis" }) }) {
            public void result(String string) {
                if (string != null) {
                    PlMission.this.missionFileName = string;
                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
                    PlMission.this.lastOpenFile = string;
                    PlMission.this.doLoadMission("missions/" + string);
                }
            }
        };
        if (this.lastOpenFile != null) gwindowfileopen.setSelectFile(this.lastOpenFile);
    }

    public boolean exitBuilder() {
        if (!isChanged()) return true;
        new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("ConfirmExit"), i18n("ConfirmExitMsg"), 1, 0.0F) {
            public void result(int i) {
                if (i == 3) {
                    if (PlMission.this.missionFileName != null) {
                        PlMission.this.save("missions/" + PlMission.this.missionFileName);
                        Plugin.builder.doMenu_FileExit();
                    } else {
                        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(this.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                            public void result(String string) {
                                if (string != null) {
                                    string = PlMission.this.checkMisExtension(string);
                                    PlMission.this.missionFileName = string;
                                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
                                    PlMission.this.lastOpenFile = string;
                                    PlMission.this.save("missions/" + string);
                                }
                                PlMission.this.bChanged = false;
                                Plugin.builder.doMenu_FileExit();
                            }
                        };
                        gwindowfilesaveas.exec = new DlgFileConfirmSave();
                        if (PlMission.this.lastOpenFile != null) gwindowfilesaveas.setSelectFile(PlMission.this.lastOpenFile);
                    }
                } else {
                    PlMission.this.bChanged = false;
                    Plugin.builder.doMenu_FileExit();
                }
            }
        };
        return false;
    }

    public void loadNewMap() {
        if (!this.bChanged) {
            this.missionFileName = null;
            ((GWindowRootMenu) builder.clientWindow.root).statusBar.setDefaultHelp(this.missionFileName);
            ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
        } else new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("SaveMission"), i18n("ConfirmExitMsg"), 1, 0.0F) {
            public void result(int i) {
                if (i == 3) {
                    if (PlMission.this.missionFileName != null) {
                        PlMission.this.save("missions/" + PlMission.this.missionFileName);
                        ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
                    } else {
                        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(this.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                            public void result(String string) {
                                if (string != null) {
                                    string = PlMission.this.checkMisExtension(string);
                                    PlMission.this.missionFileName = string;
                                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
                                    PlMission.this.lastOpenFile = string;
                                    PlMission.this.save("missions/" + string);
                                }
                                PlMission.this.bChanged = false;
                                ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
                            }
                        };
                        gwindowfilesaveas.exec = new DlgFileConfirmSave();
                        if (PlMission.this.lastOpenFile != null) gwindowfilesaveas.setSelectFile(PlMission.this.lastOpenFile);
                    }
                } else {
                    PlMission.this.bChanged = false;
                    ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
                }
                PlMission.this.missionFileName = null;
                ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(PlMission.this.missionFileName);
            }
        };
    }

    public void freeResources() {
        if (this.wConditions.isVisible()) this.wConditions.hideWindow();
        if (!this.bReload) {
            this.missionFileName = null;
            ((GWindowRootMenu) builder.clientWindow.root).statusBar.setDefaultHelp(this.missionFileName);
        }
    }

    public PlMission() {
        cur = this;
    }

    static int incTurbulence(PlMission plmission, int i) {
        return plmission.turbulence += i;
    }

    static {
        Property.set(PlMission.class, "name", "Mission");
    }
}