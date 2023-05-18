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
            mConditions.bChecked = true;
            super.windowShown();
        }

        public void windowHidden() {
            mConditions.bChecked = false;
            super.windowHidden();
        }

        public void created() {
            bAlwaysOnTop = true;
            super.created();
            title = Plugin.i18n("MissionConditions");
            float f = 13.0F;
            clientWindow = create(new GWindowTabDialogClient());
            GWindowTabDialogClient gwindowtabdialogclient = (GWindowTabDialogClient) clientWindow;
            GWindowDialogClient gwindowdialogclient;
            gwindowtabdialogclient.addTab(Plugin.i18n("weather"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, f - 1.0F, 1.3F, Plugin.i18n("Weather"), null));
            gwindowdialogclient.addControl(wCloudType = new GWindowComboControl(gwindowdialogclient, f, 1.0F, 8.0F) {
                public boolean notify(int i, int i_4_) {
                    if (i == 2)
                        getCloudType();
                    return super.notify(i, i_4_);
                }
            });
            wCloudType.setEditable(false);
            wCloudType.add(Plugin.i18n("Clear"));
            wCloudType.add(Plugin.i18n("Good"));
            wCloudType.add(Plugin.i18n("Hazy"));
            wCloudType.add(Plugin.i18n("Poor"));
            wCloudType.add(Plugin.i18n("Blind"));
            wCloudType.add(Plugin.i18n("Rain/Snow"));
            wCloudType.add(Plugin.i18n("Thunder"));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, f - 1.0F, 1.3F, Plugin.i18n("CloudHeight"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 3.0F, 2.0F, 1.3F, Plugin.i18n("[m]"), null));
            gwindowdialogclient.addControl(wCloudHeight = new GWindowEditControl(gwindowdialogclient, f, 3.0F, 5.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    bNumericOnly = true;
                    bDelayedNotify = true;
                }

                public boolean notify(int i, int i_10_) {
                    if (i == 2)
                        getCloudHeight();
                    return super.notify(i, i_10_);
                }
            });
            wCloudHeight.setEditable(true);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13.0F, 11.0F, 1.6F, Plugin.i18n("WindTable"), null));
            boxWindTable = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 15.0F, 20.0F, 25.0F);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3.0F, 16.0F, 9.0F, 1.3F, Plugin.i18n("Altitude[m]"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 10.0F, 16.0F, 9.0F, 1.3F, Plugin.i18n("WindSpeed[m/s]"), null));
            gwindowdialogclient.addLabel(wLabel0 = new GWindowLabel(gwindowdialogclient, 1.0F, 18.0F, 5.0F, 1.3F, "10", null));
            wLabel0.align = 2;
            gwindowdialogclient.addLabel(wLabel1 = new GWindowLabel(gwindowdialogclient, 1.0F, 20.0F, 5.0F, 1.3F, "1000", null));
            wLabel1.align = 2;
            gwindowdialogclient.addLabel(wLabel2 = new GWindowLabel(gwindowdialogclient, 1.0F, 22.0F, 5.0F, 1.3F, "2000", null));
            wLabel2.align = 2;
            gwindowdialogclient.addLabel(wLabel3 = new GWindowLabel(gwindowdialogclient, 1.0F, 24.0F, 5.0F, 1.3F, "3000", null));
            wLabel3.align = 2;
            gwindowdialogclient.addLabel(wLabel4 = new GWindowLabel(gwindowdialogclient, 1.0F, 26.0F, 5.0F, 1.3F, "4000", null));
            wLabel4.align = 2;
            gwindowdialogclient.addLabel(wLabel5 = new GWindowLabel(gwindowdialogclient, 1.0F, 28.0F, 5.0F, 1.3F, "5000", null));
            wLabel5.align = 2;
            gwindowdialogclient.addLabel(wLabel6 = new GWindowLabel(gwindowdialogclient, 1.0F, 30.0F, 5.0F, 1.3F, "6000", null));
            wLabel6.align = 2;
            gwindowdialogclient.addLabel(wLabel7 = new GWindowLabel(gwindowdialogclient, 1.0F, 32.0F, 5.0F, 1.3F, "7000", null));
            wLabel7.align = 2;
            gwindowdialogclient.addLabel(wLabel8 = new GWindowLabel(gwindowdialogclient, 1.0F, 34.0F, 5.0F, 1.3F, "8000", null));
            wLabel8.align = 2;
            gwindowdialogclient.addLabel(wLabel9 = new GWindowLabel(gwindowdialogclient, 1.0F, 36.0F, 5.0F, 1.3F, "9000", null));
            wLabel9.align = 2;
            gwindowdialogclient.addLabel(wLabel10 = new GWindowLabel(gwindowdialogclient, 1.0F, 38.0F, 5.0F, 1.3F, "10000", null));
            wLabel10.align = 2;
            gwindowdialogclient.addLabel(wLabel00 = new GWindowLabel(gwindowdialogclient, 10.0F, 18.0F, 5.0F, 1.3F, "", null));
            wLabel00.align = 2;
            gwindowdialogclient.addLabel(wLabel11 = new GWindowLabel(gwindowdialogclient, 10.0F, 20.0F, 5.0F, 1.3F, "", null));
            wLabel11.align = 2;
            gwindowdialogclient.addLabel(wLabel22 = new GWindowLabel(gwindowdialogclient, 10.0F, 22.0F, 5.0F, 1.3F, "", null));
            wLabel22.align = 2;
            gwindowdialogclient.addLabel(wLabel33 = new GWindowLabel(gwindowdialogclient, 10.0F, 24.0F, 5.0F, 1.3F, "", null));
            wLabel33.align = 2;
            gwindowdialogclient.addLabel(wLabel44 = new GWindowLabel(gwindowdialogclient, 10.0F, 26.0F, 5.0F, 1.3F, "", null));
            wLabel44.align = 2;
            gwindowdialogclient.addLabel(wLabel55 = new GWindowLabel(gwindowdialogclient, 10.0F, 28.0F, 5.0F, 1.3F, "", null));
            wLabel55.align = 2;
            gwindowdialogclient.addLabel(wLabel66 = new GWindowLabel(gwindowdialogclient, 10.0F, 30.0F, 5.0F, 1.3F, "", null));
            wLabel66.align = 2;
            gwindowdialogclient.addLabel(wLabel77 = new GWindowLabel(gwindowdialogclient, 10.0F, 32.0F, 5.0F, 1.3F, "", null));
            wLabel77.align = 2;
            gwindowdialogclient.addLabel(wLabel88 = new GWindowLabel(gwindowdialogclient, 10.0F, 34.0F, 5.0F, 1.3F, "", null));
            wLabel88.align = 2;
            gwindowdialogclient.addLabel(wLabel99 = new GWindowLabel(gwindowdialogclient, 10.0F, 36.0F, 5.0F, 1.3F, "", null));
            wLabel99.align = 2;
            gwindowdialogclient.addLabel(wLabel1010 = new GWindowLabel(gwindowdialogclient, 10.0F, 38.0F, 5.0F, 1.3F, "", null));
            wLabel1010.align = 2;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, f - 1.0F, 1.3F, Plugin.i18n("WindDirection"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 5.0F, 7.0F, 1.3F, Plugin.i18n("[deg]"), null));
            gwindowdialogclient.addControl(wWindDirection = new GWindowEditControl(gwindowdialogclient, f, 5.0F, 5.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    bNumericOnly = true;
                    bDelayedNotify = true;
                }

                public boolean notify(int i, int i_16_) {
                    if (i == 2)
                        getWindDirection();
                    return super.notify(i, i_16_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, f - 1.0F, 1.3F, Plugin.i18n("WindVelocity"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 7.0F, 7.0F, 1.3F, Plugin.i18n("[m/s]"), null));
            gwindowdialogclient.addControl(wWindVelocity = new GWindowEditControl(gwindowdialogclient, f, 7.0F, 5.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    bNumericOnly = true;
                    bDelayedNotify = true;
                }

                public boolean notify(int i, int i_22_) {
                    if (i == 2)
                        getWindVelocity();
                    return super.notify(i, i_22_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, f - 1.0F, 1.3F, Plugin.i18n("Gust"), null));
            gwindowdialogclient.addControl(wGust = new GWindowComboControl(gwindowdialogclient, f, 9.0F, 8.0F) {
                public boolean notify(int i, int i_27_) {
                    if (i == 2)
                        getGust();
                    return super.notify(i, i_27_);
                }
            });
            wGust.setEditable(false);
            wGust.add(Plugin.i18n("None"));
            wGust.add(Plugin.i18n("Low"));
            wGust.add(Plugin.i18n("Moderate"));
            wGust.add(Plugin.i18n("Strong"));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11.0F, f - 1.0F, 1.3F, Plugin.i18n("Turbulence"), null));
            gwindowdialogclient.addControl(wTurbulence = new GWindowComboControl(gwindowdialogclient, f, 11.0F, 8.0F) {
                public boolean notify(int i, int i_32_) {
                    if (i == 2)
                        getTurbulence();
                    return super.notify(i, i_32_);
                }
            });
            wTurbulence.setEditable(false);
            wTurbulence.add(Plugin.i18n("None"));
            wTurbulence.add(Plugin.i18n("Low"));
            wTurbulence.add(Plugin.i18n("Moderate"));
            wTurbulence.add(Plugin.i18n("Strong"));
            wTurbulence.add(Plugin.i18n("VeryStrong"));
            gwindowtabdialogclient.addTab(Plugin.i18n("Season"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            f = 10.0F;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, f - 1.0F, 1.3F, Plugin.i18n("Time"), null));
            gwindowdialogclient.addControl(wTimeH = new GWindowEditControl(gwindowdialogclient, f, 1.0F, 2.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    bNumericOnly = true;
                    bDelayedNotify = true;
                }

                public boolean notify(int i, int i_38_) {
                    if (i != 2)
                        return false;
                    getTime();
                    return false;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 2.15F, 1.0F, 1.0F, 1.3F, ":", null));
            gwindowdialogclient.addControl(wTimeM = new GWindowEditControl(gwindowdialogclient, f + 2.5F, 1.0F, 2.0F, 1.3F, "") {
                public void afterCreated() {
                    super.afterCreated();
                    bNumericOnly = true;
                    bDelayedNotify = true;
                }

                public boolean notify(int i, int i_44_) {
                    if (i != 2)
                        return false;
                    getTime();
                    return false;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, f - 1.0F, 1.3F, Plugin.i18n("Day"), null));
            gwindowdialogclient.addControl(wDay = new GWindowComboControl(gwindowdialogclient, f, 3.0F, 5.0F) {
                public boolean notify(int i, int i_49_) {
                    if (i == 2)
                        getDay();
                    return super.notify(i, i_49_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, f - 1.0F, 1.3F, Plugin.i18n("Month"), null));
            gwindowdialogclient.addControl(wMonth = new GWindowComboControl(gwindowdialogclient, f, 5.0F, 5.0F) {
                public boolean notify(int i, int i_54_) {
                    if (i == 2)
                        getMonth();
                    return super.notify(i, i_54_);
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, f - 1.0F, 1.3F, Plugin.i18n("Year"), null));
            gwindowdialogclient.addControl(wYear = new GWindowComboControl(gwindowdialogclient, f, 7.0F, 5.0F) {
                public boolean notify(int i, int i_59_) {
                    if (i == 2)
                        getYear();
                    return super.notify(i, i_59_);
                }
            });
            wDay.setEditable(false);
            wYear.setEditable(false);
            wMonth.setEditable(false);
            for (int i = 0; i < _dayKey.length; i++)
                wDay.add(_dayKey[i]);
            for (int i = 0; i < _monthKey.length; i++)
                wMonth.add(_monthKey[i]);
            for (int i = 0; i < _yearKey.length; i++)
                wYear.add(_yearKey[i]);
            gwindowtabdialogclient.addTab(Plugin.i18n("misc"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 12.0F, 1.3F, Plugin.i18n("timeLocked"), null));
            gwindowdialogclient.addControl(wTimeFix = new GWindowCheckBox(gwindowdialogclient, 14.0F, 1.0F, null) {
                public void preRender() {
                    super.preRender();
                    setChecked(World.cur().isTimeOfDayConstant(), false);
                }

                public boolean notify(int i, int i_63_) {
                    if (i != 2)
                        return false;
                    World.cur().setTimeOfDayConstant(isChecked());
                    setChanged();
                    return false;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 12.0F, 1.3F, Plugin.i18n("weaponsLocked"), null));
            gwindowdialogclient.addControl(wWeaponFix = new GWindowCheckBox(gwindowdialogclient, 14.0F, 3.0F, null) {
                public void preRender() {
                    super.preRender();
                    setChecked(World.cur().isWeaponsConstant(), false);
                }

                public boolean notify(int i, int i_67_) {
                    if (i != 2)
                        return false;
                    World.cur().setWeaponsConstant(isChecked());
                    setChanged();
                    return false;
                }
            });
        }

        public void update() {
            float f = World.getTimeofDay();
            int i = (int) f % 24;
            int i_68_ = (int) (60.0F * (f - (float) (int) f));
            wTimeH.setValue("" + i, false);
            wTimeM.setValue("" + i_68_, false);
            wCloudType.setSelected(cloudType, true, false);
            int i_69_ = (int) cloudHeight;
            wCloudHeight.setValue("" + i_69_, false);
            wTimeFix.setChecked(World.cur().isTimeOfDayConstant(), false);
            wWeaponFix.setChecked(World.cur().isWeaponsConstant(), false);
            wDay.setValue("" + day, false);
            wMonth.setValue("" + month, false);
            wYear.setValue("" + year, false);
            wWindDirection.setValue("" + windDirection, false);
            wWindVelocity.setValue("" + windVelocity, false);
            int i_70_ = gust;
            int i_71_ = turbulence;
            if (gust > 0)
                i_70_ = (gust - 6) / 2;
            wGust.setSelected(i_70_, true, false);
            if (turbulence > 0)
                i_71_ = turbulence - 2;
            wTurbulence.setSelected(i_71_, true, false);
            calcWindTable(cloudType, cloudHeight, windVelocity);
        }

        public void calcWindTable(int i, float f, float f_72_) {
            float f_73_ = f_72_;
            if (f_73_ == 0.0F)
                f_73_ = 0.25F + (float) (wCloudType.getSelected() * wCloudType.getSelected()) * 0.12F;
            float f_74_ = f + 300.0F;
            float f_75_ = f_74_ / 2.0F;
            float f_76_ = f_73_ * f_75_ / 3000.0F + f_73_;
            float f_77_ = f_73_ * (f_74_ - f_75_) / 9000.0F + f_76_;
            int[] is = new int[11];
            for (int i_78_ = 0; i_78_ <= 10; i_78_++) {
                int i_79_ = i_78_ * 1000;
                if ((float) i_79_ > f_74_)
                    f_73_ = f_77_ + ((float) i_79_ - f_74_) * f_73_ / 18000.0F;
                //TODO: Implement 4.10.1 Codechanges +++
                else
                //TODO: Implement 4.10.1 Codechanges ---
                if ((float) i_79_ > f_75_)
                    f_73_ = f_76_ + ((float) i_79_ - f_75_) * f_73_ / 9000.0F;
                else if ((float) i_79_ > 10.0F)
                    f_73_ += f_73_ * (float) i_79_ / 3000.0F;
                if (!((float) i_79_ <= 10.0F)) {
                    /* empty */
                }
                is[i_78_] = (int) f_73_;
            }
            wLabel00.cap.set("" + is[0]);
            wLabel11.cap.set("" + is[1]);
            wLabel22.cap.set("" + is[2]);
            wLabel33.cap.set("" + is[3]);
            wLabel44.cap.set("" + is[4]);
            wLabel55.cap.set("" + is[5]);
            wLabel66.cap.set("" + is[6]);
            wLabel77.cap.set("" + is[7]);
            wLabel88.cap.set("" + is[8]);
            wLabel99.cap.set("" + is[9]);
            wLabel1010.cap.set("" + is[10]);
        }

        public void getTime() {
            String string = wTimeH.getValue();
            double d = 0.0;
            try {
                d = Double.parseDouble(string);
            } catch (Exception exception) {
                /* empty */
            }
            if (d < 0.0)
                d = 0.0;
            if (d > 23.0)
                d = 23.0;
            string = wTimeM.getValue();
            double d_80_ = 0.0;
            try {
                d_80_ = Double.parseDouble(string);
            } catch (Exception exception) {
                /* empty */
            }
            if (d_80_ < 0.0)
                d_80_ = 0.0;
            if (d_80_ >= 60.0)
                d_80_ = 59.0;
            float f = (float) (d + d_80_ / 60.0);
            if ((int) (f * 60.0F) != (int) (World.getTimeofDay() * 60.0F)) {
                World.setTimeofDay(f);
                if (Plugin.builder.isLoadedLandscape())
                    World.land().cubeFullUpdate();
            }
            setChanged();
            update();
        }

        public void getCloudType() {
            cloudType = wCloudType.getSelected();
            Mission.setCloudsType(cloudType);
            setChanged();
            update();
        }

        public void getCloudHeight() {
            try {
                cloudHeight = Float.parseFloat(wCloudHeight.getValue());
            } catch (Exception exception) {
                /* empty */
            }
            if (cloudHeight < 300.0F)
                cloudHeight = 300.0F;
            if (cloudHeight > 5000.0F)
                cloudHeight = 5000.0F;
            Mission.setCloudsHeight(cloudHeight);
            setChanged();
            update();
        }

        public void getYear() {
            year = Integer.parseInt(wYear.getValue());
            Mission.setYear(year);
            setChanged();
        }

        public void getDay() {
            day = Integer.parseInt(wDay.getValue());
            Mission.setDay(day);
            setChanged();
        }

        public void getMonth() {
            month = Integer.parseInt(wMonth.getValue());
            Mission.setMonth(month);
            setChanged();
        }

        public void getWindDirection() {
            try {
                windDirection = Float.parseFloat(wWindDirection.getValue());
            } catch (Exception exception) {
                /* empty */
            }
            if (windDirection < 0.0F)
                windDirection = 0.0F;
            if (windDirection >= 360.0F)
                windDirection = 0.0F;
            Mission.setWindDirection(windDirection);
            setChanged();
            update();
        }

        public void getWindVelocity() {
            try {
                windVelocity = Float.parseFloat(wWindVelocity.getValue());
            } catch (Exception exception) {
                /* empty */
            }
            if (windVelocity > 15.0F)
                windVelocity = 15.0F;
            if (windVelocity < 0.0F)
                windVelocity = 0.0F;
            Mission.setWindVelocity(windVelocity);
            setChanged();
            update();
        }

        public void getGust() {
            gust = wGust.getSelected();
            if (gust > 0)
                gust = gust * 2 + 6;
            float f = (float) gust * 1.0F;
            Mission.setGust(f);
            setChanged();
        }

        public void getTurbulence() {
            turbulence = wTurbulence.getSelected();
            if (turbulence > 0)
                incTurbulence(PlMission.this, 2);
            float f = (float) turbulence * 1.0F;
            Mission.setTurbulence(f);
            setChanged();
        }

        public void afterCreated() {
            super.afterCreated();
            resized();
            close(false);
        }

        public WConditions() {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 23.0F, 45.0F, true);
            bSizable = true;
        }
    }

    class DlgFileConfirmSave extends GWindowFileBoxExec {
        GWindowFileBox box;
        boolean        bClose = true;

        public boolean isCloseBox() {
            return bClose;
        }

        public void exec(GWindowFileBox gwindowfilebox, String string) {
            box = gwindowfilebox;
            bClose = true;
            if (string == null || box.files.size() == 0)
                box.endExec();
            else {
                int i = string.lastIndexOf("/");
                if (i >= 0)
                    string = string.substring(i + 1);
                for (int i_222_ = 0; i_222_ < box.files.size(); i_222_++) {
                    String string_223_ = ((File) box.files.get(i_222_)).getName();
                    if (string.compareToIgnoreCase(string_223_) == 0) {
                        new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, I18N.gui("warning.Warning"), I18N.gui("warning.ReplaceFile"), 1, 0.0F) {
                            public void result(int i_229_) {
                                if (i_229_ != 3)
                                    bClose = false;
                                box.endExec();
                            }
                        };
                        return;
                    }
                }
                box.endExec();
            }
        }
    }

    static class GWindowMenuItemArmy extends GWindowMenuItem {
        int army;

        public GWindowMenuItemArmy(GWindowMenu gwindowmenu, String string, String string_230_, int i) {
            super(gwindowmenu, string, string_230_);
            army = i;
        }
    }

    public static void setChanged() {
        if (cur != null)
            cur.bChanged = true;
    }

    public static boolean isChanged() {
        if (cur != null)
            return cur.bChanged;
        return false;
    }

    public static String missionFileName() {
        if (cur == null)
            return null;
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
        } else if (!pluginMapLoad.mapLoad(land)) {
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
        missionArmy = sectfile.get("MAIN", "army", 1, 1, 2);
        year = sectfile.get("SEASON", "Year", 1940, 1930, 1960);
        month = sectfile.get("SEASON", "Month", World.land().config.month, 1, 12);
        day = sectfile.get("SEASON", "Day", 15, 1, 31);
        windDirection = sectfile.get("WEATHER", "WindDirection", 0.0F, 0.0F, 359.99F);
        windVelocity = sectfile.get("WEATHER", "WindSpeed", 0.0F, 0.0F, 15.0F);
        gust = sectfile.get("WEATHER", "Gust", 0, 0, 12);
        turbulence = sectfile.get("WEATHER", "Turbulence", 0, 0, 6);
        cloudType = sectfile.get("MAIN", "CloudType", 0, 0, 6);
        cloudHeight = sectfile.get("MAIN", "CloudHeight", 1000.0F, 300.0F, 5000.0F);
        Mission.createClouds(cloudType, cloudHeight);
        if (Main3D.cur3D().clouds != null)
            Main3D.cur3D().clouds.setShow(false);
        Main3D.cur3D().spritesFog.setShow(false);
        wConditions.update();
        Plugin.doLoad(sectfile);
        if (string_235_ != null) {
            Object[] objects = builder.pathes.getOwnerAttached();
            for (int i_236_ = 0; i_236_ < objects.length; i_236_++) {
                Path path = (Path) objects[i_236_];
                if (string_235_.equals(path.name())) {
                    if (!((PathAir) path).bOnlyAI) {
                        Path.player = path;
                        missionArmy = path.getArmy();
                    }
                    break;
                }
            }
        }
        builder.repaint();
        bChanged = false;
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
        if (World.cur().isTimeOfDayConstant())
            sectfile.lineAdd(i, "TIMECONSTANT", "1");
        if (World.cur().isWeaponsConstant())
            sectfile.lineAdd(i, "WEAPONSCONSTANT", "1");
        sectfile.lineAdd(i, "CloudType", "" + cloudType);
        sectfile.lineAdd(i, "CloudHeight", "" + cloudHeight);
        if (Actor.isValid(Path.player)) {
            sectfile.lineAdd(i, "player", Path.player.name());
            if (Path.playerNum >= ((PathAir) Path.player).planes)
                Path.playerNum = 0;
        } else
            Path.playerNum = 0;
        sectfile.lineAdd(i, "army", "" + missionArmy);
        sectfile.lineAdd(i, "playerNum", "" + Path.playerNum);
        int i_237_ = sectfile.sectionAdd("SEASON");
        sectfile.lineAdd(i_237_, "Year", "" + year);
        sectfile.lineAdd(i_237_, "Month", "" + month);
        sectfile.lineAdd(i_237_, "Day", "" + day);
        int i_238_ = sectfile.sectionAdd("WEATHER");
        sectfile.lineAdd(i_238_, "WindDirection", "" + windDirection);
        sectfile.lineAdd(i_238_, "WindSpeed", "" + windVelocity);
        sectfile.lineAdd(i_238_, "Gust", "" + gust);
        sectfile.lineAdd(i_238_, "Turbulence", "" + turbulence);
        if (!Plugin.doSave(sectfile))
            return false;
        sectfile.saveFile(string);
        bChanged = false;
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
                if (Main3D.cur3D().land2DText == null)
                    Main3D.cur3D().land2DText = new Land2DText();
                else
                    Main3D.cur3D().land2DText.clear();
                Main3D.cur3D().land2DText.load(HomePath.concatNames(string, string_241_));
            }
            Statics.trim();
            Landscape landscape = World.land();
            if (landscape != null) {
                /* empty */
            }
            if (Landscape.isExistMeshs()) {
                for (int i_242_ = 0; i_242_ < PathFind.tShip.sy; i_242_++) {
                    for (int i_243_ = 0; i_243_ < PathFind.tShip.sx; i_243_++) {
                        if (landscape != null) {
                            /* empty */
                        }
                        if (Landscape.isExistMesh(i_243_, (PathFind.tShip.sy - i_242_ - 1))) {
                            PathFind.tShip.I(i_243_, i_242_, (PathFind.tShip.intI(i_243_, i_242_) | 0x8));
                            PathFind.tNoShip.I(i_243_, i_242_, (PathFind.tNoShip.intI(i_243_, i_242_) | 0x8));
                        }
                    }
                }
            }
            Mission.createClouds(cloudType, cloudHeight);
            if (Main3D.cur3D().clouds != null)
                Main3D.cur3D().clouds.setShow(false);
            Main3D.cur3D().spritesFog.setShow(false);
            wConditions.update();
        }
    }

    public void configure() {
        builder.bMultiSelect = false;
        if (getPlugin("MapLoad") == null)
            throw new RuntimeException("PlMission: plugin 'MapLoad' not found");
        pluginMapLoad = (PlMapLoad) getPlugin("MapLoad");
    }

    void _viewTypeAll(boolean bool) {
        Plugin.doViewTypeAll(bool);
        viewBridge(bool);
        viewRunaway(bool);
        viewName.bChecked = builder.conf.bShowName = bool;
        viewTime.bChecked = builder.conf.bShowTime = bool;
        for (int i = 0; i < builder.conf.bShowArmy.length; i++)
            viewArmy[i].bChecked = builder.conf.bShowArmy[i] = bool;
        if (!bool)
            builder.setSelected(null);
    }

    void viewBridge(boolean bool) {
        builder.conf.bViewBridge = bool;
        viewBridge.bChecked = builder.conf.bViewBridge;
    }

    void viewBridge() {
        viewBridge(!builder.conf.bViewBridge);
    }

    void viewRunaway(boolean bool) {
        builder.conf.bViewRunaway = bool;
        viewRunaway.bChecked = builder.conf.bViewRunaway;
    }

    void viewRunaway() {
        viewRunaway(!builder.conf.bViewRunaway);
    }

    public static void checkShowCurrentArmy() {
        Actor actor = builder.selectedPath();
        if (actor == null)
            actor = builder.selectedActor();
        if (actor != null) {
            int i = actor.getArmy();
            if (!builder.conf.bShowArmy[i])
                builder.setSelected(null);
        }
    }

    private String checkMisExtension(String string) {
        if (!string.toLowerCase().endsWith(".mis"))
            return string + ".mis";
        return string;
    }

    public void createGUI() {
        builder.mDisplayFilter.subMenu.addItem("-", null);
        viewBridge = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showBridge"), i18n("TIPshowBridge")) {
            public void execute() {
                viewBridge();
            }
        });
        viewBridge.bChecked = builder.conf.bViewBridge;
        viewRunaway = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showRunway"), i18n("TIPshowRunway")) {
            public void execute() {
                viewRunaway();
            }
        });
        viewRunaway.bChecked = builder.conf.bViewRunaway;
        builder.mDisplayFilter.subMenu.addItem("-", null);
        viewName = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showName"), i18n("TIPshowName")) {
            public void execute() {
                bChecked = Plugin.builder.conf.bShowName = !Plugin.builder.conf.bShowName;
            }
        });
        viewName.bChecked = builder.conf.bShowName;
        viewTime = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(builder.mDisplayFilter.subMenu, i18n("showTime"), i18n("TIPshowTime")) {
            public void execute() {
                bChecked = Plugin.builder.conf.bShowTime = !Plugin.builder.conf.bShowTime;
            }
        });
        viewTime.bChecked = builder.conf.bShowTime;
        viewArmy = new GWindowMenuItemArmy[Builder.armyAmount()];
        for (int i = 0; i < Builder.armyAmount(); i++) {
            viewArmy[i] = builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItemArmy(builder.mDisplayFilter.subMenu, i18n("showArmy") + " " + I18N.army(Army.name(i)), i18n("TIPshowArmy") + " " + I18N.army(Army.name(i)), i) {
                public void execute() {
                    bChecked = Plugin.builder.conf.bShowArmy[army] = !Plugin.builder.conf.bShowArmy[army];
                    checkShowCurrentArmy();
                }
            });
            viewArmy[i].bChecked = builder.conf.bShowArmy[i];
        }
        builder.mDisplayFilter.subMenu.addItem("-", null);
        builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem((builder.mDisplayFilter.subMenu), (i18n("&ShowAll")), (i18n("TIPShowAll"))) {
            public void execute() {
                _viewTypeAll(true);
            }
        });
        builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem((builder.mDisplayFilter.subMenu), (i18n("&HideAll")), (i18n("TIPHideAll"))) {
            public void execute() {
                _viewTypeAll(false);
            }
        });
        builder.mFile.subMenu.addItem(1, new GWindowMenuItem((builder.mFile.subMenu), i18n("Load"), i18n("TIPLoad")) {
            public void execute() {
                PlMission.this.doDlgLoadMission();
            }
        });
        builder.mFile.subMenu.addItem(2, new GWindowMenuItem((builder.mFile.subMenu), i18n("Save"), (i18n("TIPSaveAs"))) {
            public void execute() {
                if (missionFileName != null)
                    save("missions/" + missionFileName);
                else {
                    GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                        public void result(String string) {
                            if (string != null) {
                                string = PlMission.this.checkMisExtension(string);
                                missionFileName = string;
                                ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                                lastOpenFile = string;
                                save("missions/" + string);
                            }
                        }
                    };
                    gwindowfilesaveas.exec = new DlgFileConfirmSave();
                    if (lastOpenFile != null)
                        gwindowfilesaveas.setSelectFile(lastOpenFile);
                }
            }
        });
        builder.mFile.subMenu.addItem(3, new GWindowMenuItem((builder.mFile.subMenu), i18n("SaveAs"), (i18n("TIPSaveAs"))) {
            public void execute() {
                GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, Plugin.i18n("SaveMission"), "missions", (new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) })) {
                    public void result(String string) {
                        if (string != null) {
                            string = PlMission.this.checkMisExtension(string);
                            missionFileName = string;
                            ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                            lastOpenFile = string;
                            save("missions/" + string);
                        }
                    }
                };
                gwindowfilesaveas.exec = new DlgFileConfirmSave();
                if (lastOpenFile != null)
                    gwindowfilesaveas.setSelectFile(lastOpenFile);
            }
        });
        builder.mFile.subMenu.addItem(4, new GWindowMenuItem((builder.mFile.subMenu), i18n("Play"), i18n("TIPPlay")) {
            public void execute() {
                if (Plugin.builder.isLoadedLandscape()) {
                    if (isChanged() || missionFileName == null) {
                        if (missionFileName != null) {
                            if (save("missions/" + missionFileName))
                                PlMission.this.playMission();
                        } else {
                            GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                                public void result(String string) {
                                    if (string != null) {
                                        string = PlMission.this.checkMisExtension(string);
                                        missionFileName = string;
                                        ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                                        lastOpenFile = string;
                                        if (save("missions/" + string))
                                            PlMission.this.playMission();
                                    }
                                }
                            };
                            gwindowfilesaveas.exec = new DlgFileConfirmSave();
                            if (lastOpenFile != null)
                                gwindowfilesaveas.setSelectFile(lastOpenFile);
                        }
                    } else
                        PlMission.this.playMission();
                }
            }
        });
        builder.mFile.subMenu.bNotify = true;
        builder.mFile.subMenu.addNotifyListener(new GNotifyListener() {
            public boolean notify(GWindow gwindow, int i, int i_274_) {
                if (i != 13)
                    return false;
                Plugin.builder.mFile.subMenu.getItem(2).bEnable = Plugin.builder.isLoadedLandscape();
                Plugin.builder.mFile.subMenu.getItem(3).bEnable = Plugin.builder.isLoadedLandscape();
                Plugin.builder.mFile.subMenu.getItem(4).bEnable = Plugin.builder.isLoadedLandscape();
                return false;
            }
        });
        mConditions = builder.mConfigure.subMenu.addItem(0, new GWindowMenuItem(builder.mConfigure.subMenu, i18n("&Conditions"), i18n("TIPConditions")) {
            public void execute() {
                if (wConditions.isVisible())
                    wConditions.hideWindow();
                else
                    wConditions.showWindow();
            }
        });

        builder.mEdit.subMenu.addItem(0, "-", null);
        wConditions = new WConditions();
        wConditions.update();
    }

    private void doLoadMission(String string) {
        _loadFileName = string;
        _loadMessageBox = new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("StandBy"), i18n("LoadingMission"), 4, 0.0F);
        new MsgAction(72, 0.0) {
            public void doAction() {
                load(_loadFileName);
                _loadMessageBox.close(false);
            }
        };
    }

    private void playMission() {
        Main.cur().currentMissionFile = new SectFile("missions/" + missionFileName, 0);
        bReload = true;
        Main.stateStack().push(4);
    }

    private void doDlgLoadMission() {
        if (!isChanged())
            _doDlgLoadMission();
        else
            new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("LoadMission"), i18n("ConfirmExitMsg"), 1, 0.0F) {
                public void result(int i) {
                    if (i == 3) {
                        if (missionFileName != null) {
                            save("missions/" + missionFileName);
                            PlMission.this._doDlgLoadMission();
                        } else {
                            GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                                public void result(String string) {
                                    if (string != null) {
                                        string = PlMission.this.checkMisExtension(string);
                                        missionFileName = string;
                                        ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                                        lastOpenFile = string;
                                        save("missions/" + string);
                                    }
                                    PlMission.this._doDlgLoadMission();
                                }
                            };
                            gwindowfilesaveas.exec = new DlgFileConfirmSave();
                            if (lastOpenFile != null)
                                gwindowfilesaveas.setSelectFile(lastOpenFile);
                        }
                    } else
                        PlMission.this._doDlgLoadMission();
                }
            };
    }

    private void _doDlgLoadMission() {
        GWindowFileOpen gwindowfileopen = new GWindowFileOpen((builder.clientWindow.root), true, (i18n("LoadMission")), "missions", (new GFileFilter[] { new GFileFilterName((i18n("MissionFiles")), (new String[] { "*.mis" })) })) {
            public void result(String string) {
                if (string != null) {
                    missionFileName = string;
                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                    lastOpenFile = string;
                    PlMission.this.doLoadMission("missions/" + string);
                }
            }
        };
        if (lastOpenFile != null)
            gwindowfileopen.setSelectFile(lastOpenFile);
    }

    public boolean exitBuilder() {
        if (!isChanged())
            return true;
        new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("ConfirmExit"), i18n("ConfirmExitMsg"), 1, 0.0F) {
            public void result(int i) {
                if (i == 3) {
                    if (missionFileName != null) {
                        save("missions/" + missionFileName);
                        Plugin.builder.doMenu_FileExit();
                    } else {
                        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                            public void result(String string) {
                                if (string != null) {
                                    string = PlMission.this.checkMisExtension(string);
                                    missionFileName = string;
                                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                                    lastOpenFile = string;
                                    save("missions/" + string);
                                }
                                bChanged = false;
                                Plugin.builder.doMenu_FileExit();
                            }
                        };
                        gwindowfilesaveas.exec = new DlgFileConfirmSave();
                        if (lastOpenFile != null)
                            gwindowfilesaveas.setSelectFile(lastOpenFile);
                    }
                } else {
                    bChanged = false;
                    Plugin.builder.doMenu_FileExit();
                }
            }
        };
        return false;
    }

    public void loadNewMap() {
        if (!bChanged) {
            missionFileName = null;
            ((GWindowRootMenu) builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
            ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
        } else
            new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("SaveMission"), i18n("ConfirmExitMsg"), 1, 0.0F) {
                public void result(int i) {
                    if (i == 3) {
                        if (missionFileName != null) {
                            save("missions/" + missionFileName);
                            ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
                        } else {
                            GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] { "*.mis" }) }) {
                                public void result(String string) {
                                    if (string != null) {
                                        string = PlMission.this.checkMisExtension(string);
                                        missionFileName = string;
                                        ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                                        lastOpenFile = string;
                                        save("missions/" + string);
                                    }
                                    bChanged = false;
                                    ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
                                }
                            };
                            gwindowfilesaveas.exec = new DlgFileConfirmSave();
                            if (lastOpenFile != null)
                                gwindowfilesaveas.setSelectFile(lastOpenFile);
                        }
                    } else {
                        bChanged = false;
                        ((PlMapLoad) Plugin.getPlugin("MapLoad")).guiMapLoad();
                    }
                    missionFileName = null;
                    ((GWindowRootMenu) Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                }
            };
    }

    public void freeResources() {
        if (wConditions.isVisible())
            wConditions.hideWindow();
        if (!bReload) {
            missionFileName = null;
            ((GWindowRootMenu) builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
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