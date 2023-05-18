/* 4.10.1 class */
package com.maddox.il2.builder;

import java.util.ArrayList;
import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.gwindow.GNotifyListener;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowHSliderInt;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowTabDialogClient;
import com.maddox.il2.ai.Army;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.il2.game.I18N;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class PlMisTarget extends Plugin {
    protected ArrayList        allActors       = new ArrayList();
    Item[]                     item            = { new Item("Destroy", 0), new Item("DestroyGround", 1), new Item("DestroyBridge", 2), new Item("Inspect", 3), new Item("Escort", 4), new Item("Defence", 5), new Item("DefenceGround", 6),
            new Item("DefenceBridge", 7) };
    private float[]            line2XYZ        = new float[6];
    private Point2d            p2d             = new Point2d();
    private Point2d            p2dt            = new Point2d();
    private Point3d            p3d             = new Point3d();
    private static final int   NCIRCLESEGMENTS = 48;
    private static float[]     _circleXYZ      = new float[144];
    private PlMission          pluginMission;
    private int                startComboBox1;
    private GWindowMenuItem    viewType;
    private String[]           _actorInfo      = new String[2];
    GWindowTabDialogClient.Tab tabTarget;
    GWindowLabel               wType;
    GWindowLabel               wTarget;
    GWindowCheckBox            wBTimeout;
    GWindowLabel               wLTimeout;
    GWindowEditControl         wTimeoutH;
    GWindowEditControl         wTimeoutM;
    GWindowHSliderInt          wR;
    GWindowCheckBox            wBLanding;
    GWindowLabel               wLLanding;
    GWindowComboControl        wImportance;
    GWindowLabel               wLDestruct;
    GWindowComboControl        wDestruct;
    GWindowLabel               wLArmy;
    GWindowComboControl        wArmy;

    static class Item {
        public String name;
        public int    indx;

        public Item(String string, int i) {
            name = string;
            indx = i;
        }
    }

    private int targetColor(int i, boolean bool) {
        if (bool)
            return Builder.colorSelected();
        switch (i) {
            case 0:
                return -1;
            case 1:
                return -16711936;
            case 2:
                return -8454144;
            default:
                return 0;
        }
    }

    public void renderMap2DBefore() {
        if (!Plugin.builder.isFreeView() && viewType.bChecked) {
            Actor actor = Plugin.builder.selectedActor();
            int i = allActors.size();
            for (int i_0_ = 0; i_0_ < i; i_0_++) {
                ActorTarget actortarget = (ActorTarget) allActors.get(i_0_);
                if (Actor.isValid(actortarget.getTarget()) && Plugin.builder.project2d(actortarget.pos.getAbsPoint(), p2d) && Plugin.builder.project2d(actortarget.getTarget().pos.getAbsPoint(), p2dt) && p2d.distance(p2dt) > 4.0) {
                    int i_1_ = targetColor(actortarget.importance, actortarget == actor);
                    line2XYZ[0] = (float) p2d.x;
                    line2XYZ[1] = (float) p2d.y;
                    line2XYZ[2] = 0.0F;
                    line2XYZ[3] = (float) p2dt.x;
                    line2XYZ[4] = (float) p2dt.y;
                    line2XYZ[5] = 0.0F;
                    Render.drawBeginLines(-1);
                    Render.drawLines(line2XYZ, 2, 1.0F, i_1_, (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND), 3);
                    Render.drawEnd();
                }
            }
        }
    }

    public void renderMap2DAfter() {
        if (!Plugin.builder.isFreeView() && viewType.bChecked) {
            Actor actor = Plugin.builder.selectedActor();
            int i = allActors.size();
            for (int i_2_ = 0; i_2_ < i; i_2_++) {
                ActorTarget actortarget = (ActorTarget) allActors.get(i_2_);
                if (Plugin.builder.project2d(actortarget.pos.getAbsPoint(), p2d)) {
                    int i_3_ = targetColor(actortarget.importance, actortarget == actor);
                    IconDraw.setColor(i_3_);
                    if (Actor.isValid(actortarget.getTarget()) && Plugin.builder.project2d(actortarget.getTarget().pos.getAbsPoint(), p2dt) && p2d.distance(p2dt) > 4.0)
                        Render.drawTile((float) (p2dt.x - (double) ((Plugin.builder.conf.iconSize) / 2)), (float) (p2dt.y - (double) ((Plugin.builder.conf.iconSize) / 2)), (float) Plugin.builder.conf.iconSize, (float) Plugin.builder.conf.iconSize, 0.0F,
                                Plugin.targetIcon, i_3_, 0.0F, 1.0F, 1.0F, -1.0F);
                    IconDraw.render(actortarget, p2d.x, p2d.y);
                    if (actortarget.type == 3 || actortarget.type == 6 || actortarget.type == 1) {
                        actortarget.pos.getAbs(p3d);
                        p3d.x += (double) actortarget.r;
                        if (Plugin.builder.project2d(p3d, p2dt)) {
                            double d = p2dt.x - p2d.x;
                            if (d > (double) (Plugin.builder.conf.iconSize / 3)) {
                                drawCircle(p2d.x, p2d.y, d, i_3_);
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawCircle(double d, double d_4_, double d_5_, int i) {
        int i_6_ = NCIRCLESEGMENTS;
        double d_7_ = 6.283185307179586 / (double) i_6_;
        double d_8_ = 0.0;
        for (int i_9_ = 0; i_9_ < i_6_; i_9_++) {
            _circleXYZ[i_9_ * 3 + 0] = (float) (d + d_5_ * Math.cos(d_8_));
            _circleXYZ[i_9_ * 3 + 1] = (float) (d_4_ + d_5_ * Math.sin(d_8_));
            _circleXYZ[i_9_ * 3 + 2] = 0.0F;
            d_8_ += d_7_;
        }
        Render.drawBeginLines(-1);
        Render.drawLines(_circleXYZ, i_6_, 1.0F, i, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 4);
        Render.drawEnd();
    }

    public boolean save(SectFile sectfile) {
        int i = allActors.size();
        if (i == 0)
            return true;
        int sectionIndex = sectfile.sectionAdd("Target");
        for (int i_11_ = 0; i_11_ < i; i_11_++) {
            ActorTarget actortarget = (ActorTarget) allActors.get(i_11_);
            String string = "";
            int i_12_ = 0;
            int i_13_ = 0;
            int i_14_ = 0;
            if (Actor.isValid(actortarget.target)) {
                if (actortarget.target instanceof PPoint) {
                    string = actortarget.target.getOwner().name();
                    i_12_ = ((Path) actortarget.target.getOwner()).pointIndx((PPoint) actortarget.target);
                } else
                    string = actortarget.target.name();
                Point3d point3d = actortarget.target.pos.getAbsPoint();
                i_13_ = (int) point3d.x;
                i_14_ = (int) point3d.y;
            }

            // TODO: Added by |ZUTI|: houses targeting variable at the end
            sectfile.lineAdd(sectionIndex,
                    ("" + actortarget.type + " " + actortarget.importance + " " + (actortarget.bTimeout ? "1 " : "0 ") + actortarget.timeout + " " + actortarget.destructLevel + (actortarget.bLanding ? 1 : 0) + " " + (int) actortarget.pos.getAbsPoint().x
                            + " " + (int) actortarget.pos.getAbsPoint().y + " " + actortarget.r + (string.length() > 0 ? (" " + i_12_ + " " + string + " " + i_13_ + " " + i_14_) : "" + " " + (actortarget.zutiAllowHousesTargeting ? 1 : 0))));
        }
        return true;
    }

    public void load(SectFile sectfile) {
        int i = sectfile.sectionIndex("Target");
        if (i >= 0) {
            int i_15_ = sectfile.vars(i);
            Point3d point3d = new Point3d();
            for (int i_16_ = 0; i_16_ < i_15_; i_16_++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_16_));
                int i_17_ = numbertokenizer.next(0, 0, 7);
                int i_18_ = numbertokenizer.next(0, 0, 2);
                boolean bool = numbertokenizer.next(0) == 1;
                int i_19_ = numbertokenizer.next(0, 0, 720);
                int i_20_ = numbertokenizer.next(0);
                boolean bool_21_ = (i_20_ & 0x1) == 1;
                i_20_ /= 10;
                if (i_20_ < 0)
                    i_20_ = 0;
                if (i_20_ > 100)
                    i_20_ = 100;
                point3d.x = (double) numbertokenizer.next(0);
                point3d.y = (double) numbertokenizer.next(0);
                int i_22_ = numbertokenizer.next(0);
                if (i_17_ == 3 || i_17_ == 6 || i_17_ == 1) {
                    if (i_22_ < 2)
                        i_22_ = 2;
                    if (i_22_ > 3000)
                        i_22_ = 3000;
                }
                int i_23_ = numbertokenizer.next(0);
                String string = numbertokenizer.next((String) null);
                if (string != null && string.startsWith("Bridge"))
                    string = " " + string;

                ActorTarget actortarget = insert(point3d, i_17_, string, i_23_, false);
                if (actortarget != null) {
                    actortarget.importance = i_18_;
                    actortarget.bTimeout = bool;
                    actortarget.timeout = i_19_;
                    actortarget.r = i_22_;
                    actortarget.bLanding = bool_21_;
                    actortarget.destructLevel = i_20_;
                }
            }
        }
    }

    public void deleteAll() {
        int i = allActors.size();
        for (int i_24_ = 0; i_24_ < i; i_24_++) {
            ActorTarget actortarget = (ActorTarget) allActors.get(i_24_);
            actortarget.destroy();
        }
        allActors.clear();
    }

    public void delete(Actor actor) {
        allActors.remove(actor);
        actor.destroy();
    }

    public void afterDelete() {
        int i = 0;
        while (i < allActors.size()) {
            ActorTarget actortarget = (ActorTarget) allActors.get(i);
            if (actortarget.target != null && !Actor.isValid(actortarget.target)) {
                actortarget.destroy();
                allActors.remove(i);
            } else
                i++;
        }
    }

    private ActorTarget insert(Point3d point3d, int i, String s, int j, boolean flag) {
        // TODO: Method was reconstructed by |ZUTI|. Issues are not excluded
        try {
            ActorTarget actortarget = new ActorTarget(point3d, i, s, j);

            // DestroyGround(type = 1), DefenceGround(type = 6) and Escort(type = 3) can not be attached to specific targets (3 can, but is valid even if it is not)
            if (actortarget.type != 1 && actortarget.type != 6 && actortarget.type != 3) {
                if (!com.maddox.il2.engine.Actor.isValid(actortarget.target)) {
                    System.out.println("PlMisTarget - actortarget not valid!");
                    return null;
                }

                // Escort target has problem with below for loop, so, don't perform it for that one
                if (actortarget.type != 4) {
                    for (int index = 0; index < allActors.size(); index++) {
                        ActorTarget actortarget1 = (ActorTarget) allActors.get(index);
                        if (actortarget.type != actortarget1.type || !com.maddox.il2.engine.Actor.isValid(actortarget1.target)
                                || actortarget1.target != actortarget.target && (!(actortarget.target instanceof com.maddox.il2.builder.PPoint) || actortarget.target.getOwner() != actortarget1.target.getOwner()))
                            continue;

                        actortarget.destroy();
                        System.out.println("PlMisTarget - destroying actortarget!");
                        return null;
                    }
                }
            }
            Plugin.builder.align(actortarget);
            Property.set(actortarget, "builderSpawn", "");
            Property.set(actortarget, "builderPlugin", this);
            allActors.add(actortarget);
            if (flag)
                Plugin.builder.setSelected(actortarget);
            PlMission.setChanged();
            return actortarget;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void insert(Loc loc, boolean bool) {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int i_31_ = Plugin.builder.wSelect.comboBox2.getSelected();
        if (i == startComboBox1 && (i_31_ >= 0 && i_31_ < item.length))
            insert(loc.getPoint(), item[i_31_].indx, null, 0, bool);
    }

    public void changeType() {
        Plugin.builder.setSelected(null);
    }

    private void updateView() {
        int i = allActors.size();
        for (int i_32_ = 0; i_32_ < i; i_32_++) {
            ActorTarget actortarget = (ActorTarget) allActors.get(i_32_);
            actortarget.drawing(viewType.bChecked);
        }
    }

    public void configure() {
        if (Plugin.getPlugin("Mission") == null)
            throw new RuntimeException("PlMisTarget: plugin 'Mission' not found");
        pluginMission = (PlMission) Plugin.getPlugin("Mission");
    }

    private void fillComboBox2(int i) {
        if (i == startComboBox1) {
            if (Plugin.builder.wSelect.curFilledType != i) {
                Plugin.builder.wSelect.curFilledType = i;
                Plugin.builder.wSelect.comboBox2.clear(false);
                for (int i_33_ = 0; i_33_ < item.length; i_33_++)
                    Plugin.builder.wSelect.comboBox2.add(Plugin.i18n(item[i_33_].name));
                Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
            }
            Plugin.builder.wSelect.comboBox2.setSelected(0, true, false);
            Plugin.builder.wSelect.setMesh(null, true);
        }
    }

    public void viewTypeAll(boolean bool) {
        viewType.bChecked = bool;
        updateView();
    }

    public String[] actorInfo(Actor actor) {
        ActorTarget actortarget = (ActorTarget) actor;
        switch (actortarget.importance) {
            case 0:
                _actorInfo[0] = (Plugin.i18n("Primary") + " " + Plugin.i18n(item[actortarget.type].name));
                break;
            case 1:
                _actorInfo[0] = (Plugin.i18n("Secondary") + " " + Plugin.i18n(item[actortarget.type].name));
                break;
            case 2:
                _actorInfo[0] = (Plugin.i18n("Secret") + " " + Plugin.i18n(item[actortarget.type].name));
                break;
        }
        if (Actor.isValid(actortarget.getTarget()) && actortarget.getTarget() instanceof PPoint) {
            Path path = (Path) actortarget.getTarget().getOwner();
            if (path instanceof PathAir)
                _actorInfo[1] = ((PathAir) path).typedName;
            else if (path instanceof PathChief)
                _actorInfo[1] = Property.stringValue(path, "i18nName", "");
            else
                _actorInfo[1] = path.name();
        } else
            _actorInfo[1] = null;
        return _actorInfo;
    }

    public void syncSelector() {
        ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
        fillComboBox2(startComboBox1);
        Plugin.builder.wSelect.comboBox2.setSelected(actortarget.type, true, false);
        Plugin.builder.wSelect.tabsClient.addTab(1, tabTarget);
        wType.cap.set(Plugin.i18n(item[actortarget.type].name));
        float f = 3.0F;
        if (Actor.isValid(actortarget.getTarget()) && actortarget.getTarget() instanceof PPoint) {
            wTarget.showWindow();
            Path path = (Path) actortarget.getTarget().getOwner();
            if (path instanceof PathAir)
                wTarget.cap.set(((PathAir) path).typedName);
            else if (path instanceof PathChief)
                wTarget.cap.set(Property.stringValue(path, "i18nName", ""));
            else
                wTarget.cap.set(path.name());
            f += 2.0F;
        } else
            wTarget.hideWindow();
        if (actortarget.type == 3 || actortarget.type == 6 || actortarget.type == 7)
            wBTimeout.hideWindow();
        else {
            wBTimeout.showWindow();
            wBTimeout.setMetricPos(wBTimeout.metricWin.x, f);
        }
        wBTimeout.setChecked(actortarget.bTimeout, false);
        wLTimeout.setMetricPos(wLTimeout.metricWin.x, f);
        wTimeoutH.setEnable(actortarget.bTimeout);
        wTimeoutM.setEnable(actortarget.bTimeout);
        wTimeoutH.setMetricPos(wTimeoutH.metricWin.x, f);
        wTimeoutM.setMetricPos(wTimeoutM.metricWin.x, f);
        wTimeoutH.setValue("" + actortarget.timeout / 60 % 24, false);
        wTimeoutM.setValue("" + actortarget.timeout % 60, false);
        f += 2.0F;
        if (actortarget.type == 3 || actortarget.type == 6 || actortarget.type == 1) {
            wR.setPos(actortarget.r / 50, false);
            wR.showWindow();
            wR.setMetricPos(wR.metricWin.x, f);
            // TODO: Added by |ZUTI|
            // ---------------------------------------
            lZutiRadius1.setMetricPos(1.0F, f);
            lZutiRadius2.setMetricPos(17.0F, f);
            // ---------------------------------------
            f += 2.0F;

            // TODO: Added by |ZUTI|
            // ---------------------------------------
            lZutiDescription.hideWindow();
            lZutiRadius1.showWindow();
            lZutiRadius2.showWindow();

            if (actortarget.type == 6 || actortarget.type == 1) {
                wZutiAllowHousesTargeting.setChecked(actortarget.zutiAllowHousesTargeting, false);
                lzutiHouseTargeting.showWindow();
                wZutiAllowHousesTargeting.showWindow();
                lZutiDescription.showWindow();
                lZutiConditionsSummary.showWindow();
            } else {
                lzutiHouseTargeting.hideWindow();
                lzutiHouseTargeting.hideWindow();
                lZutiConditionsSummary.hideWindow();
                lZutiDescription.hideWindow();
            }
            // ---------------------------------------
        } else {
            wR.hideWindow();

            // TODO: Added by |ZUTI|
            // ------------------------------------------------
            lzutiHouseTargeting.hideWindow();
            wZutiAllowHousesTargeting.hideWindow();
            lZutiRadius1.hideWindow();
            lZutiRadius2.hideWindow();
            lZutiDescription.hideWindow();
            lZutiConditionsSummary.hideWindow();
            // ------------------------------------------------
        }
        if (actortarget.type == 3) {
            wBLanding.showWindow();
            wLLanding.showWindow();
            wBLanding.setMetricPos(wBLanding.metricWin.x, f);
            wLLanding.setMetricPos(wLLanding.metricWin.x, f);
            wBLanding.setChecked(actortarget.bLanding, false);
            f += 2.0F;
        } else {
            wBLanding.hideWindow();
            wLLanding.hideWindow();
        }
        wImportance.setMetricPos(wImportance.metricWin.x, f);
        wImportance.setSelected(actortarget.importance, true, false);
        f += 2.0F;
        if (actortarget.type == 3 || actortarget.type == 2 || actortarget.type == 7) {
            wLDestruct.hideWindow();
            wDestruct.hideWindow();
        } else {
            wLDestruct.showWindow();
            wDestruct.showWindow();
            wLDestruct.setMetricPos(wLDestruct.metricWin.x, f);
            f += 2.0F;
            wDestruct.setMetricPos(wDestruct.metricWin.x, f);
            f += 2.0F;
            int i;
            if (actortarget.destructLevel < 12)
                i = 0;
            else if (actortarget.destructLevel < 37)
                i = 1;
            else if (actortarget.destructLevel < 62)
                i = 2;
            else if (actortarget.destructLevel < 87)
                i = 3;
            else
                i = 4;
            wDestruct.setSelected(i, true, false);
            if (actortarget.type == 0 || actortarget.type == 1) {
                wDestruct.posEnable[0] = false;
                wDestruct.posEnable[4] = true;
            } else {
                wDestruct.posEnable[0] = true;
                wDestruct.posEnable[4] = false;
            }
        }
        wLArmy.setMetricPos(wLArmy.metricWin.x, f);
        f += 2.0F;
        wArmy.setMetricPos(wArmy.metricWin.x, f);
        if (Actor.isValid(Path.player)) {
            wArmy.setSelected(Path.player.getArmy() - 1, true, false);
            wArmy.bEnable = false;
        } else {
            wArmy.setSelected(PlMission.cur.missionArmy - 1, true, false);
            wArmy.bEnable = true;
        }
    }

    public void createGUI() {
        startComboBox1 = Plugin.builder.wSelect.comboBox1.size();
        Plugin.builder.wSelect.comboBox1.add(Plugin.i18n("tTarget"));
        Plugin.builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {
            public boolean notify(GWindow gwindow, int i, int i_35_) {
                int i_36_ = Plugin.builder.wSelect.comboBox1.getSelected();
                if (i_36_ >= 0 && i == 2)
                    PlMisTarget.this.fillComboBox2(i_36_);
                return false;
            }
        });
        int i;
        for (i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; i >= 0; i--) {
            if (pluginMission.viewBridge == Plugin.builder.mDisplayFilter.subMenu.getItem(i))
                break;
        }
        if (--i >= 0) {
            viewType = Plugin.builder.mDisplayFilter.subMenu.addItem(i, new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showTarget"), null) {
                public void execute() {
                    bChecked = !bChecked;
                    PlMisTarget.this.updateView();
                }
            });
            viewType.bChecked = true;
        }
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());
        tabTarget = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("tTarget"), gwindowdialogclient);
        gwindowdialogclient.addLabel(wType = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 15.0F, 1.3F, Plugin.i18n("lType"), null));
        gwindowdialogclient.addLabel(wTarget = new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 15.0F, 1.3F, Plugin.i18n("tTarget"), null));

        gwindowdialogclient.addControl(wBTimeout = new GWindowCheckBox(gwindowdialogclient, 1.0F, 5.0F, null) {
            public boolean notify(int i_41_, int i_42_) {
                if (i_41_ != 2)
                    return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.bTimeout = isChecked();
                wTimeoutH.setEnable(actortarget.bTimeout);
                wTimeoutM.setEnable(actortarget.bTimeout);
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLTimeout = new GWindowLabel(gwindowdialogclient, 3.0F, 5.0F, 5.0F, 1.3F, Plugin.i18n("TimeOut"), null));
        gwindowdialogclient.addControl(wTimeoutH = new GWindowEditControl(gwindowdialogclient, 9.0F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_47_, int i_48_) {
                if (i_47_ != 2)
                    return false;
                PlMisTarget.this.getTimeOut();
                return false;
            }
        });
        gwindowdialogclient.addControl(wTimeoutM = new GWindowEditControl(gwindowdialogclient, 12.0F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_53_, int i_54_) {
                if (i_53_ != 2)
                    return false;
                PlMisTarget.this.getTimeOut();
                return false;
            }
        });

        // TODO: Added by |ZUTI|
        // ----------------------------------------------------------
        gwindowdialogclient.addLabel(lZutiRadius1 = new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, 6.0F, 1.3F, Plugin.i18n("mds.properties.radius"), null));
        gwindowdialogclient.addLabel(lZutiRadius2 = new GWindowLabel(gwindowdialogclient, 17.0F, 5.0F, 6.0F, 1.3F, " [500m]", null));
        // ----------------------------------------------------------
        gwindowdialogclient.addControl(wR = new GWindowHSliderInt(gwindowdialogclient, 0, 61, 11, 6.0F, 7.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                bSlidingNotify = true;
            }

            public boolean notify(int i_61_, int i_62_) {
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();

                // TODO: Added by |ZUTI|
                // ------------------------------------------
                if (actortarget != null) {
                    ZutiSupportMethods_Builder.setPlMisTargetLabels(PlMisTarget.this, actortarget, false);
                }
                // ------------------------------------------

                if (i_61_ != 2)
                    return false;

                actortarget.r = pos() * 50;
                if (actortarget.r < 2)
                    actortarget.r = 2;

                // TODO: Added by |ZUTI|: repeats here because radius is recalculated
                ZutiSupportMethods_Builder.setPlMisTargetLabels(PlMisTarget.this, actortarget, true);

                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addControl(wBLanding = new GWindowCheckBox(gwindowdialogclient, 1.0F, 9.0F, null) {
            public boolean notify(int i_65_, int i_66_) {
                if (i_65_ != 2)
                    return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.bLanding = isChecked();
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLLanding = new GWindowLabel(gwindowdialogclient, 3.0F, 9.0F, 7.0F, 1.3F, Plugin.i18n("landing"), null));
        gwindowdialogclient.addControl(wImportance = new GWindowComboControl(gwindowdialogclient, 1.0F, 11.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("Primary"));
                add(Plugin.i18n("Secondary"));
                add(Plugin.i18n("Secret"));
            }

            public boolean notify(int i_70_, int i_71_) {
                if (i_70_ != 2)
                    return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.importance = getSelected();
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLDestruct = new GWindowLabel(gwindowdialogclient, 1.0F, 13.0F, 12.0F, 1.3F, Plugin.i18n("DestructLevel"), null));
        gwindowdialogclient.addControl(wDestruct = new GWindowComboControl(gwindowdialogclient, 1.0F, 15.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                setEditable(false);
                add("0 %");
                add("25 %");
                add("50 %");
                add("75 %");
                add("100 %");
                boolean[] bools = new boolean[5];
                for (int i_75_ = 0; i_75_ < 5; i_75_++)
                    bools[i_75_] = true;
                posEnable = bools;
            }

            public boolean notify(int i_76_, int i_77_) {
                if (i_76_ != 2)
                    return false;

                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.destructLevel = getSelected() * 25;
                PlMission.setChanged();

                // TODO: Added by |ZUTI|
                ZutiSupportMethods_Builder.setPlMisTargetLabels(PlMisTarget.this, actortarget, true);

                return false;
            }
        });
        gwindowdialogclient.addLabel(wLArmy = new GWindowLabel(gwindowdialogclient, 1.0F, 15.0F, 12.0F, 1.3F, Plugin.i18n("AppliesArmy"), null));
        gwindowdialogclient.addControl(wArmy = new GWindowComboControl(gwindowdialogclient, 1.0F, 17.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                setEditable(false);
                add(I18N.army(Army.name(1)));
                add(I18N.army(Army.name(2)));
            }

            public boolean notify(int i_81_, int i_82_) {
                if (i_81_ != 2)
                    return false;
                PlMission.cur.missionArmy = getSelected() + 1;
                PlMission.setChanged();
                return false;
            }
        });

        // TODO: Added by |ZUTI|
        // ---------------------------------------------------------------------
        gwindowdialogclient.addLabel(lzutiHouseTargeting = new GWindowLabel(gwindowdialogclient, 1.0F, 18.0F, 20.0F, 1.3F, Plugin.i18n("mds.objectives.allowHouses"), null));
        gwindowdialogclient.addControl(wZutiAllowHousesTargeting = new GWindowCheckBox(gwindowdialogclient, 20.0F, 18.0F, null) {
            public boolean notify(int i_65_, int i_66_) {
                if (i_65_ != 2)
                    return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.zutiAllowHousesTargeting = isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        gwindowdialogclient.addLabel(lZutiDescription = new GWindowLabel(gwindowdialogclient, 1.0F, 20.0F, 30.0F, 1.3F, Plugin.i18n("mds.objectives.objectsNr"), null));
        gwindowdialogclient.addLabel(lZutiConditionsSummary = new GWindowLabel(gwindowdialogclient, 1.0F, 22.0F, 40.0F, 1.3F, "---", null));
        // ---------------------------------------------------------------------
    }

    private void getTimeOut() {
        ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
        String string = wTimeoutH.getValue();
        double d = 0.0;
        try {
            d = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d < 0.0)
            d = 0.0;
        if (d > 12.0)
            d = 12.0;
        string = wTimeoutM.getValue();
        double d_83_ = 0.0;
        try {
            d_83_ = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d_83_ < 0.0)
            d_83_ = 0.0;
        if (d_83_ > 59.0)
            d_83_ = 59.0;
        actortarget.timeout = (int) (d * 60.0 + d_83_);
        wTimeoutH.setValue("" + actortarget.timeout / 60 % 24, false);
        wTimeoutM.setValue("" + actortarget.timeout % 60, false);
        PlMission.setChanged();
    }

    static {
        Property.set(PlMisTarget.class, "name", "MisTarget");
    }

    // TODO: |ZUTI| variables
    // ----------------------------------------------------------------------
    protected GWindowLabel    lzutiHouseTargeting;
    protected GWindowCheckBox wZutiAllowHousesTargeting;
    protected GWindowLabel    lZutiRadius1;
    protected GWindowLabel    lZutiRadius2;
    protected GWindowLabel    lZutiDescription;
    protected GWindowLabel    lZutiConditionsSummary;
    protected ActorTarget     zutiTempActorTarget            = null;
    protected int             zutiObjectsCoveredByTargetArea = 0;
    // ----------------------------------------------------------------------
}