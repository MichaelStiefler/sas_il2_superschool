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
            this.name = string;
            this.indx = i;
        }
    }

    private int targetColor(int i, boolean bool) {
        if (bool) return Builder.colorSelected();
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
        if (!Plugin.builder.isFreeView() && this.viewType.bChecked) {
            Actor actor = Plugin.builder.selectedActor();
            int i = this.allActors.size();
            for (int i_0_ = 0; i_0_ < i; i_0_++) {
                ActorTarget actortarget = (ActorTarget) this.allActors.get(i_0_);
                if (Actor.isValid(actortarget.getTarget()) && Plugin.builder.project2d(actortarget.pos.getAbsPoint(), this.p2d) && Plugin.builder.project2d(actortarget.getTarget().pos.getAbsPoint(), this.p2dt) && this.p2d.distance(this.p2dt) > 4.0) {
                    int i_1_ = this.targetColor(actortarget.importance, actortarget == actor);
                    this.line2XYZ[0] = (float) this.p2d.x;
                    this.line2XYZ[1] = (float) this.p2d.y;
                    this.line2XYZ[2] = 0.0F;
                    this.line2XYZ[3] = (float) this.p2dt.x;
                    this.line2XYZ[4] = (float) this.p2dt.y;
                    this.line2XYZ[5] = 0.0F;
                    Render.drawBeginLines(-1);
                    Render.drawLines(this.line2XYZ, 2, 1.0F, i_1_, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
                    Render.drawEnd();
                }
            }
        }
    }

    public void renderMap2DAfter() {
        if (!Plugin.builder.isFreeView() && this.viewType.bChecked) {
            Actor actor = Plugin.builder.selectedActor();
            int i = this.allActors.size();
            for (int i_2_ = 0; i_2_ < i; i_2_++) {
                ActorTarget actortarget = (ActorTarget) this.allActors.get(i_2_);
                if (Plugin.builder.project2d(actortarget.pos.getAbsPoint(), this.p2d)) {
                    int i_3_ = this.targetColor(actortarget.importance, actortarget == actor);
                    IconDraw.setColor(i_3_);
                    if (Actor.isValid(actortarget.getTarget()) && Plugin.builder.project2d(actortarget.getTarget().pos.getAbsPoint(), this.p2dt) && this.p2d.distance(this.p2dt) > 4.0)
                        Render.drawTile((float) (this.p2dt.x - Plugin.builder.conf.iconSize / 2), (float) (this.p2dt.y - Plugin.builder.conf.iconSize / 2), Plugin.builder.conf.iconSize, Plugin.builder.conf.iconSize, 0.0F, Plugin.targetIcon, i_3_, 0.0F,
                                1.0F, 1.0F, -1.0F);
                    IconDraw.render(actortarget, this.p2d.x, this.p2d.y);
                    if (actortarget.type == 3 || actortarget.type == 6 || actortarget.type == 1) {
                        actortarget.pos.getAbs(this.p3d);
                        this.p3d.x += actortarget.r;
                        if (Plugin.builder.project2d(this.p3d, this.p2dt)) {
                            double d = this.p2dt.x - this.p2d.x;
                            if (d > Plugin.builder.conf.iconSize / 3) this.drawCircle(this.p2d.x, this.p2d.y, d, i_3_);
                        }
                    }
                }
            }
        }
    }

    private void drawCircle(double d, double d_4_, double d_5_, int i) {
        int i_6_ = NCIRCLESEGMENTS;
        double d_7_ = 6.283185307179586 / i_6_;
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
        int i = this.allActors.size();
        if (i == 0) return true;
        int sectionIndex = sectfile.sectionAdd("Target");
        for (int i_11_ = 0; i_11_ < i; i_11_++) {
            ActorTarget actortarget = (ActorTarget) this.allActors.get(i_11_);
            String string = "";
            int i_12_ = 0;
            int i_13_ = 0;
            int i_14_ = 0;
            if (Actor.isValid(actortarget.target)) {
                if (actortarget.target instanceof PPoint) {
                    string = actortarget.target.getOwner().name();
                    i_12_ = ((Path) actortarget.target.getOwner()).pointIndx((PPoint) actortarget.target);
                } else string = actortarget.target.name();
                Point3d point3d = actortarget.target.pos.getAbsPoint();
                i_13_ = (int) point3d.x;
                i_14_ = (int) point3d.y;
            }

            // TODO: Added by |ZUTI|: houses targeting variable at the end
            sectfile.lineAdd(sectionIndex,
                    "" + actortarget.type + " " + actortarget.importance + " " + (actortarget.bTimeout ? "1 " : "0 ") + actortarget.timeout + " " + actortarget.destructLevel + (actortarget.bLanding ? 1 : 0) + " " + (int) actortarget.pos.getAbsPoint().x
                            + " " + (int) actortarget.pos.getAbsPoint().y + " " + actortarget.r + (string.length() > 0 ? " " + i_12_ + " " + string + " " + i_13_ + " " + i_14_ : "" + " " + (actortarget.zutiAllowHousesTargeting ? 1 : 0)));
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
                if (i_20_ < 0) i_20_ = 0;
                if (i_20_ > 100) i_20_ = 100;
                point3d.x = numbertokenizer.next(0);
                point3d.y = numbertokenizer.next(0);
                int i_22_ = numbertokenizer.next(0);
                if (i_17_ == 3 || i_17_ == 6 || i_17_ == 1) {
                    if (i_22_ < 2) i_22_ = 2;
                    if (i_22_ > 3000) i_22_ = 3000;
                }
                int i_23_ = numbertokenizer.next(0);
                String string = numbertokenizer.next((String) null);
                if (string != null && string.startsWith("Bridge")) string = " " + string;

                ActorTarget actortarget = this.insert(point3d, i_17_, string, i_23_, false);
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
        int i = this.allActors.size();
        for (int i_24_ = 0; i_24_ < i; i_24_++) {
            ActorTarget actortarget = (ActorTarget) this.allActors.get(i_24_);
            actortarget.destroy();
        }
        this.allActors.clear();
    }

    public void delete(Actor actor) {
        this.allActors.remove(actor);
        actor.destroy();
    }

    public void afterDelete() {
        int i = 0;
        while (i < this.allActors.size()) {
            ActorTarget actortarget = (ActorTarget) this.allActors.get(i);
            if (actortarget.target != null && !Actor.isValid(actortarget.target)) {
                actortarget.destroy();
                this.allActors.remove(i);
            } else i++;
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
                if (actortarget.type != 4) for (int index = 0; index < this.allActors.size(); index++) {
                    ActorTarget actortarget1 = (ActorTarget) this.allActors.get(index);
                    if (actortarget.type != actortarget1.type || !com.maddox.il2.engine.Actor.isValid(actortarget1.target)
                            || actortarget1.target != actortarget.target && (!(actortarget.target instanceof com.maddox.il2.builder.PPoint) || actortarget.target.getOwner() != actortarget1.target.getOwner()))
                        continue;

                    actortarget.destroy();
                    System.out.println("PlMisTarget - destroying actortarget!");
                    return null;
                }
            }
            Plugin.builder.align(actortarget);
            Property.set(actortarget, "builderSpawn", "");
            Property.set(actortarget, "builderPlugin", this);
            this.allActors.add(actortarget);
            if (flag) Plugin.builder.setSelected(actortarget);
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
        if (i == this.startComboBox1 && i_31_ >= 0 && i_31_ < this.item.length) this.insert(loc.getPoint(), this.item[i_31_].indx, null, 0, bool);
    }

    public void changeType() {
        Plugin.builder.setSelected(null);
    }

    private void updateView() {
        int i = this.allActors.size();
        for (int i_32_ = 0; i_32_ < i; i_32_++) {
            ActorTarget actortarget = (ActorTarget) this.allActors.get(i_32_);
            actortarget.drawing(this.viewType.bChecked);
        }
    }

    public void configure() {
        if (Plugin.getPlugin("Mission") == null) throw new RuntimeException("PlMisTarget: plugin 'Mission' not found");
        this.pluginMission = (PlMission) Plugin.getPlugin("Mission");
    }

    private void fillComboBox2(int i) {
        if (i == this.startComboBox1) {
            if (Plugin.builder.wSelect.curFilledType != i) {
                Plugin.builder.wSelect.curFilledType = i;
                Plugin.builder.wSelect.comboBox2.clear(false);
                for (int i_33_ = 0; i_33_ < this.item.length; i_33_++)
                    Plugin.builder.wSelect.comboBox2.add(Plugin.i18n(this.item[i_33_].name));
                Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
            }
            Plugin.builder.wSelect.comboBox2.setSelected(0, true, false);
            Plugin.builder.wSelect.setMesh(null, true);
        }
    }

    public void viewTypeAll(boolean bool) {
        this.viewType.bChecked = bool;
        this.updateView();
    }

    public String[] actorInfo(Actor actor) {
        ActorTarget actortarget = (ActorTarget) actor;
        switch (actortarget.importance) {
            case 0:
                this._actorInfo[0] = Plugin.i18n("Primary") + " " + Plugin.i18n(this.item[actortarget.type].name);
                break;
            case 1:
                this._actorInfo[0] = Plugin.i18n("Secondary") + " " + Plugin.i18n(this.item[actortarget.type].name);
                break;
            case 2:
                this._actorInfo[0] = Plugin.i18n("Secret") + " " + Plugin.i18n(this.item[actortarget.type].name);
                break;
        }
        if (Actor.isValid(actortarget.getTarget()) && actortarget.getTarget() instanceof PPoint) {
            Path path = (Path) actortarget.getTarget().getOwner();
            if (path instanceof PathAir) this._actorInfo[1] = ((PathAir) path).typedName;
            else if (path instanceof PathChief) this._actorInfo[1] = Property.stringValue(path, "i18nName", "");
            else this._actorInfo[1] = path.name();
        } else this._actorInfo[1] = null;
        return this._actorInfo;
    }

    public void syncSelector() {
        ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
        this.fillComboBox2(this.startComboBox1);
        Plugin.builder.wSelect.comboBox2.setSelected(actortarget.type, true, false);
        Plugin.builder.wSelect.tabsClient.addTab(1, this.tabTarget);
        this.wType.cap.set(Plugin.i18n(this.item[actortarget.type].name));
        float f = 3.0F;
        if (Actor.isValid(actortarget.getTarget()) && actortarget.getTarget() instanceof PPoint) {
            this.wTarget.showWindow();
            Path path = (Path) actortarget.getTarget().getOwner();
            if (path instanceof PathAir) this.wTarget.cap.set(((PathAir) path).typedName);
            else if (path instanceof PathChief) this.wTarget.cap.set(Property.stringValue(path, "i18nName", ""));
            else this.wTarget.cap.set(path.name());
            f += 2.0F;
        } else this.wTarget.hideWindow();
        if (actortarget.type == 3 || actortarget.type == 6 || actortarget.type == 7) this.wBTimeout.hideWindow();
        else {
            this.wBTimeout.showWindow();
            this.wBTimeout.setMetricPos(this.wBTimeout.metricWin.x, f);
        }
        this.wBTimeout.setChecked(actortarget.bTimeout, false);
        this.wLTimeout.setMetricPos(this.wLTimeout.metricWin.x, f);
        this.wTimeoutH.setEnable(actortarget.bTimeout);
        this.wTimeoutM.setEnable(actortarget.bTimeout);
        this.wTimeoutH.setMetricPos(this.wTimeoutH.metricWin.x, f);
        this.wTimeoutM.setMetricPos(this.wTimeoutM.metricWin.x, f);
        this.wTimeoutH.setValue("" + actortarget.timeout / 60 % 24, false);
        this.wTimeoutM.setValue("" + actortarget.timeout % 60, false);
        f += 2.0F;
        if (actortarget.type == 3 || actortarget.type == 6 || actortarget.type == 1) {
            this.wR.setPos(actortarget.r / 50, false);
            this.wR.showWindow();
            this.wR.setMetricPos(this.wR.metricWin.x, f);
            // TODO: Added by |ZUTI|
            // ---------------------------------------
            this.lZutiRadius1.setMetricPos(1.0F, f);
            this.lZutiRadius2.setMetricPos(17.0F, f);
            // ---------------------------------------
            f += 2.0F;

            // TODO: Added by |ZUTI|
            // ---------------------------------------
            this.lZutiDescription.hideWindow();
            this.lZutiRadius1.showWindow();
            this.lZutiRadius2.showWindow();

            if (actortarget.type == 6 || actortarget.type == 1) {
                this.wZutiAllowHousesTargeting.setChecked(actortarget.zutiAllowHousesTargeting, false);
                this.lzutiHouseTargeting.showWindow();
                this.wZutiAllowHousesTargeting.showWindow();
                this.lZutiDescription.showWindow();
                this.lZutiConditionsSummary.showWindow();
            } else {
                this.lzutiHouseTargeting.hideWindow();
                this.lzutiHouseTargeting.hideWindow();
                this.lZutiConditionsSummary.hideWindow();
                this.lZutiDescription.hideWindow();
            }
            // ---------------------------------------
        } else {
            this.wR.hideWindow();

            // TODO: Added by |ZUTI|
            // ------------------------------------------------
            this.lzutiHouseTargeting.hideWindow();
            this.wZutiAllowHousesTargeting.hideWindow();
            this.lZutiRadius1.hideWindow();
            this.lZutiRadius2.hideWindow();
            this.lZutiDescription.hideWindow();
            this.lZutiConditionsSummary.hideWindow();
            // ------------------------------------------------
        }
        if (actortarget.type == 3) {
            this.wBLanding.showWindow();
            this.wLLanding.showWindow();
            this.wBLanding.setMetricPos(this.wBLanding.metricWin.x, f);
            this.wLLanding.setMetricPos(this.wLLanding.metricWin.x, f);
            this.wBLanding.setChecked(actortarget.bLanding, false);
            f += 2.0F;
        } else {
            this.wBLanding.hideWindow();
            this.wLLanding.hideWindow();
        }
        this.wImportance.setMetricPos(this.wImportance.metricWin.x, f);
        this.wImportance.setSelected(actortarget.importance, true, false);
        f += 2.0F;
        if (actortarget.type == 3 || actortarget.type == 2 || actortarget.type == 7) {
            this.wLDestruct.hideWindow();
            this.wDestruct.hideWindow();
        } else {
            this.wLDestruct.showWindow();
            this.wDestruct.showWindow();
            this.wLDestruct.setMetricPos(this.wLDestruct.metricWin.x, f);
            f += 2.0F;
            this.wDestruct.setMetricPos(this.wDestruct.metricWin.x, f);
            f += 2.0F;
            int i;
            if (actortarget.destructLevel < 12) i = 0;
            else if (actortarget.destructLevel < 37) i = 1;
            else if (actortarget.destructLevel < 62) i = 2;
            else if (actortarget.destructLevel < 87) i = 3;
            else i = 4;
            this.wDestruct.setSelected(i, true, false);
            if (actortarget.type == 0 || actortarget.type == 1) {
                this.wDestruct.posEnable[0] = false;
                this.wDestruct.posEnable[4] = true;
            } else {
                this.wDestruct.posEnable[0] = true;
                this.wDestruct.posEnable[4] = false;
            }
        }
        this.wLArmy.setMetricPos(this.wLArmy.metricWin.x, f);
        f += 2.0F;
        this.wArmy.setMetricPos(this.wArmy.metricWin.x, f);
        if (Actor.isValid(Path.player)) {
            this.wArmy.setSelected(Path.player.getArmy() - 1, true, false);
            this.wArmy.bEnable = false;
        } else {
            this.wArmy.setSelected(PlMission.cur.missionArmy - 1, true, false);
            this.wArmy.bEnable = true;
        }
    }

    public void createGUI() {
        this.startComboBox1 = Plugin.builder.wSelect.comboBox1.size();
        Plugin.builder.wSelect.comboBox1.add(Plugin.i18n("tTarget"));
        Plugin.builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {
            public boolean notify(GWindow gwindow, int i, int i_35_) {
                int i_36_ = Plugin.builder.wSelect.comboBox1.getSelected();
                if (i_36_ >= 0 && i == 2) PlMisTarget.this.fillComboBox2(i_36_);
                return false;
            }
        });
        int i;
        for (i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; i >= 0; i--)
            if (this.pluginMission.viewBridge == Plugin.builder.mDisplayFilter.subMenu.getItem(i)) break;
        if (--i >= 0) {
            this.viewType = Plugin.builder.mDisplayFilter.subMenu.addItem(i, new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showTarget"), null) {
                public void execute() {
                    this.bChecked = !this.bChecked;
                    PlMisTarget.this.updateView();
                }
            });
            this.viewType.bChecked = true;
        }
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());
        this.tabTarget = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("tTarget"), gwindowdialogclient);
        gwindowdialogclient.addLabel(this.wType = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 15.0F, 1.3F, Plugin.i18n("lType"), null));
        gwindowdialogclient.addLabel(this.wTarget = new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 15.0F, 1.3F, Plugin.i18n("tTarget"), null));

        gwindowdialogclient.addControl(this.wBTimeout = new GWindowCheckBox(gwindowdialogclient, 1.0F, 5.0F, null) {
            public boolean notify(int i_41_, int i_42_) {
                if (i_41_ != 2) return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.bTimeout = this.isChecked();
                PlMisTarget.this.wTimeoutH.setEnable(actortarget.bTimeout);
                PlMisTarget.this.wTimeoutM.setEnable(actortarget.bTimeout);
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(this.wLTimeout = new GWindowLabel(gwindowdialogclient, 3.0F, 5.0F, 5.0F, 1.3F, Plugin.i18n("TimeOut"), null));
        gwindowdialogclient.addControl(this.wTimeoutH = new GWindowEditControl(gwindowdialogclient, 9.0F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i_47_, int i_48_) {
                if (i_47_ != 2) return false;
                PlMisTarget.this.getTimeOut();
                return false;
            }
        });
        gwindowdialogclient.addControl(this.wTimeoutM = new GWindowEditControl(gwindowdialogclient, 12.0F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i_53_, int i_54_) {
                if (i_53_ != 2) return false;
                PlMisTarget.this.getTimeOut();
                return false;
            }
        });

        // TODO: Added by |ZUTI|
        // ----------------------------------------------------------
        gwindowdialogclient.addLabel(this.lZutiRadius1 = new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, 6.0F, 1.3F, Plugin.i18n("mds.properties.radius"), null));
        gwindowdialogclient.addLabel(this.lZutiRadius2 = new GWindowLabel(gwindowdialogclient, 17.0F, 5.0F, 6.0F, 1.3F, " [500m]", null));
        // ----------------------------------------------------------
        gwindowdialogclient.addControl(this.wR = new GWindowHSliderInt(gwindowdialogclient, 0, 61, 11, 6.0F, 7.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.bSlidingNotify = true;
            }

            public boolean notify(int i_61_, int i_62_) {
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();

                // TODO: Added by |ZUTI|
                // ------------------------------------------
                if (actortarget != null) ZutiSupportMethods_Builder.setPlMisTargetLabels(PlMisTarget.this, actortarget, false);

                if (i_61_ != 2) return false;

                actortarget.r = this.pos() * 50;
                if (actortarget.r < 2) actortarget.r = 2;

                // TODO: Added by |ZUTI|: repeats here because radius is recalculated
                ZutiSupportMethods_Builder.setPlMisTargetLabels(PlMisTarget.this, actortarget, true);

                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addControl(this.wBLanding = new GWindowCheckBox(gwindowdialogclient, 1.0F, 9.0F, null) {
            public boolean notify(int i_65_, int i_66_) {
                if (i_65_ != 2) return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.bLanding = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(this.wLLanding = new GWindowLabel(gwindowdialogclient, 3.0F, 9.0F, 7.0F, 1.3F, Plugin.i18n("landing"), null));
        gwindowdialogclient.addControl(this.wImportance = new GWindowComboControl(gwindowdialogclient, 1.0F, 11.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add(Plugin.i18n("Primary"));
                this.add(Plugin.i18n("Secondary"));
                this.add(Plugin.i18n("Secret"));
            }

            public boolean notify(int i_70_, int i_71_) {
                if (i_70_ != 2) return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.importance = this.getSelected();
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(this.wLDestruct = new GWindowLabel(gwindowdialogclient, 1.0F, 13.0F, 12.0F, 1.3F, Plugin.i18n("DestructLevel"), null));
        gwindowdialogclient.addControl(this.wDestruct = new GWindowComboControl(gwindowdialogclient, 1.0F, 15.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add("0 %");
                this.add("25 %");
                this.add("50 %");
                this.add("75 %");
                this.add("100 %");
                boolean[] bools = new boolean[5];
                for (int i_75_ = 0; i_75_ < 5; i_75_++)
                    bools[i_75_] = true;
                this.posEnable = bools;
            }

            public boolean notify(int i_76_, int i_77_) {
                if (i_76_ != 2) return false;

                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.destructLevel = this.getSelected() * 25;
                PlMission.setChanged();

                // TODO: Added by |ZUTI|
                ZutiSupportMethods_Builder.setPlMisTargetLabels(PlMisTarget.this, actortarget, true);

                return false;
            }
        });
        gwindowdialogclient.addLabel(this.wLArmy = new GWindowLabel(gwindowdialogclient, 1.0F, 15.0F, 12.0F, 1.3F, Plugin.i18n("AppliesArmy"), null));
        gwindowdialogclient.addControl(this.wArmy = new GWindowComboControl(gwindowdialogclient, 1.0F, 17.0F, 10.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add(I18N.army(Army.name(1)));
                this.add(I18N.army(Army.name(2)));
            }

            public boolean notify(int i_81_, int i_82_) {
                if (i_81_ != 2) return false;
                PlMission.cur.missionArmy = this.getSelected() + 1;
                PlMission.setChanged();
                return false;
            }
        });

        // TODO: Added by |ZUTI|
        // ---------------------------------------------------------------------
        gwindowdialogclient.addLabel(this.lzutiHouseTargeting = new GWindowLabel(gwindowdialogclient, 1.0F, 18.0F, 20.0F, 1.3F, Plugin.i18n("mds.objectives.allowHouses"), null));
        gwindowdialogclient.addControl(this.wZutiAllowHousesTargeting = new GWindowCheckBox(gwindowdialogclient, 20.0F, 18.0F, null) {
            public boolean notify(int i_65_, int i_66_) {
                if (i_65_ != 2) return false;
                ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
                actortarget.zutiAllowHousesTargeting = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        gwindowdialogclient.addLabel(this.lZutiDescription = new GWindowLabel(gwindowdialogclient, 1.0F, 20.0F, 30.0F, 1.3F, Plugin.i18n("mds.objectives.objectsNr"), null));
        gwindowdialogclient.addLabel(this.lZutiConditionsSummary = new GWindowLabel(gwindowdialogclient, 1.0F, 22.0F, 40.0F, 1.3F, "---", null));
        // ---------------------------------------------------------------------
    }

    private void getTimeOut() {
        ActorTarget actortarget = (ActorTarget) Plugin.builder.selectedActor();
        String string = this.wTimeoutH.getValue();
        double d = 0.0;
        try {
            d = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d < 0.0) d = 0.0;
        if (d > 12.0) d = 12.0;
        string = this.wTimeoutM.getValue();
        double d_83_ = 0.0;
        try {
            d_83_ = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d_83_ < 0.0) d_83_ = 0.0;
        if (d_83_ > 59.0) d_83_ = 59.0;
        actortarget.timeout = (int) (d * 60.0 + d_83_);
        this.wTimeoutH.setValue("" + actortarget.timeout / 60 % 24, false);
        this.wTimeoutM.setValue("" + actortarget.timeout % 60, false);
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