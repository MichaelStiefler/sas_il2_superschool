package com.maddox.il2.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowHSliderInt;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuBarItem;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowRootMenu;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.World;
import com.maddox.il2.builder.Plugin;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.ActorSoundListener;
import com.maddox.il2.engine.Camera;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.ConsoleGL0;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Render;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.TimeSkip;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.BackgroundTask;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.sound.AudioDevice;
import com.maddox.util.HashMapExt;

public class GUIMenu
{
    
    public static class SelectableActor implements Comparable {

        private Actor actor;
        private int sortOrder;
        
        public static final int SORT_PLAYERAIRCRAFT = 0;
        public static final int SORT_PLAYERAIRCRAFT_NONAME = 1;
        public static final int SORT_OTHERAIRCRAFT = 2;
        public static final int SORT_CHIEF = 3;
        public static final int SORT_MOVING = 4;
        public static final int SORT_STATIONARY = 5;
        public static final int SORT_BUILDING = 6;
        public static final int SORT_OTHER = 7;
        
        public SelectableActor(Actor actor) {
            this.actor = actor;
            this.sortOrder = -1;
            if (this.actor instanceof Aircraft) {
                Aircraft thisAircraft = (Aircraft)this.actor;
                if (thisAircraft.netUser() != null) {
                    if (thisAircraft.netUser().shortName() != null) {
                        this.sortOrder = SORT_PLAYERAIRCRAFT;
                    } else {
                        this.sortOrder = SORT_PLAYERAIRCRAFT_NONAME;
                    }
                } else {
                    this.sortOrder = SORT_OTHERAIRCRAFT;
                }
            } else {
                if (this.actor instanceof Chief) this.sortOrder = SORT_CHIEF;
                else {
                    String thisName = Property.stringValue(this.actor.getClass(), "iconFar_shortClassName", null);
                    if (thisName != null) {
                        if (thisName.toLowerCase().endsWith("static")) this.sortOrder = SORT_STATIONARY;
                        else if (thisName.toLowerCase().endsWith("bld")) this.sortOrder = SORT_BUILDING;
                    }
                }
            }
            if (this.sortOrder == -1) {
                if (this.actor.pos instanceof ActorPosMove) this.sortOrder = SORT_MOVING;
                else this.sortOrder = SORT_OTHER;
            }
        }
        
        public String getName() {
            String name = Property.stringValue(this.actor.getClass(), "iconFar_shortClassName", null);
            if(name == null) name = getSimpleInnerName(this.actor.getClass()).trim();
            name = name.trim();
            
            if (name.toLowerCase().startsWith("com.maddox.il2")) name=name.substring(name.lastIndexOf("$") + 1);
            
            boolean hasNetUserName = false;
            
            if (this.actor instanceof Aircraft) {
                Aircraft thisAircraft = (Aircraft)this.actor;
                if (thisAircraft.netUser() != null) {
                    if (thisAircraft.netUser().shortName() != null) {
                        name = thisAircraft.netUser().shortName() + " (" + name + ")";
                        hasNetUserName = true;
                    }
                }
            }
            if(!hasNetUserName) {
                String actorName = this.actor.name().trim();
                if (actorName.toLowerCase().startsWith("com.maddox.il2")) actorName=actorName.substring(actorName.lastIndexOf("$") + 1);
                name = actorName + " (" + name + ")";
            }
            return name;
        }
        
        public int compareTo(Object o) {
            if (!(o instanceof SelectableActor)) return -1;
            SelectableActor selectableActor = (SelectableActor)o;
            if (this.actor == selectableActor.actor) return 0;
            if (this.sortOrder != selectableActor.sortOrder) return this.sortOrder<selectableActor.sortOrder?-1:1;
            
            if (this.actor instanceof Aircraft) {
                if (!(selectableActor.actor instanceof Aircraft)) return -1;
                Aircraft thisAircraft = (Aircraft)this.actor;
                Aircraft otherAircraft = (Aircraft)selectableActor.actor;
                if (thisAircraft.netUser() != null) {
                    if (otherAircraft.netUser() == null) return -1;
                    if (thisAircraft.netUser().shortName() != null) {
                        if (otherAircraft.netUser().shortName() == null) return -1;
                        return thisAircraft.netUser().shortName().compareTo(otherAircraft.netUser().shortName());
                    } else if (otherAircraft.netUser().shortName() != null) return 1;
                } else if (otherAircraft.netUser() != null) return 1;
            } else if (selectableActor.actor instanceof Aircraft) return 1;
            
            return this.getName().compareTo(selectableActor.getName());
        }
        
    }

    public GUIMenu()
    {
    }

    public static void initHotKeyESC()
    {
        String envName = "gui";
        HotKeyEnv.fromIni(envName, Config.cur.ini, "HotKey " + envName);
        HotKeyCmdEnv.addCmd(envName, new HotKeyCmd(true, "activate") {

            public void end()
            {
                GameState gamestate = Main.state();
                if(gamestate == null)
                    return;
                if(BackgroundTask.isExecuted())
                    return;
                if(GUI.pad.isActive())
                {
                    GUI.pad.leave(false);
                    return;
                }
                if(GUIMenu.isVisible())
                {
                    GUIMenu.doCloseMenu();
                    return;
                } else
                {
                    RTSConf.cur.hotKeyEnvs.endAllActiveCmd(false);
                    gamestate.doQuitMission();
                    return;
                }
            }

        }
);
    }

//    public static void initHotKeys()
//    {
//        String sc = "aircraftView";
//        HotKeyCmdEnv.setCurrentEnv(sc);
//        HotKeyCmdEnv.addCmd("aircraftView", new HotKeyCmd(true, "GameMenu") {
//
//            public void end()
//            {
//                if(!GUI.pad.isActive())
//                    GUIMenu.doToggleMenu();
//            }
//
//            public void created()
//            {
//            }
//
//        }
//);
//    }

    private static boolean setTimeSpeed(float f)
    {
        if(TimeSkip.isDo())
            return false;
        if(!Time.isEnableChangeSpeed())
            return false;
        Time.setSpeed(f);
        sp = Math.round(f * 32F);
        HotKeyCmdEnv.showTimeSpeed(f);
//        switch(sp)
//        {
//        case 4:
//            HUD.log(0, "TimeSpeedNormal");
//            break;
//
//        case 8:
//            HUD.log(0, "TimeSpeedUp2");
//            break;
//
//        case 16: 
//            HUD.log(0, "TimeSpeedUp4");
//            break;
//
//        case 32:
//            HUD.log(0, "TimeSpeedUp8");
//            break;
//
//        case 2:
//            HUD.log(0, "TimeSpeedDown2");
//            break;
//
//        case 1:
//            HUD.log(0, "TimeSpeedDown4");
//            break;
//        }
        doUpdateMenu();
        return true;
    }

    public static boolean isVisible()
    {
        return gameMenu != null && gameMenu.menuBar.isVisible();
    }

    public static void doToggleMenu()
    {
        if(gameMenu != null)
        {
            if(gameMenu.menuBar.isVisible())
            {
                doCloseMenu();
            } else
            {
                doUpdateMenu();
                doShowMenu();
            }
            return;
        } else
        {
            doUpdateMenu();
            doShowMenu();
            return;
        }
    }

    public static void doCloseMenu()
    {
        if(gameMenu.menuBar != null)
        {
            gameMenu.menuBar.hideWindow();
            gameMenu.menuBar.close(true);
        }
        if(gameMenu.statusBar != null)
        {
            gameMenu.statusBar.hideWindow();
            gameMenu.statusBar.close(true);
        }
        GUI.unActivate();
        if (restoreChatOnClose) GUI.chatDlg.showWindow();
    }

    public static void doShowMenu()
    {
        gameMenu.menuBar.showWindow();
        if(Config.cur.bStBar)
            gameMenu.statusBar.showWindow();
        else
            gameMenu.statusBar.hideWindow();
        if(!Main3D.cur3D().guiManager.isMouseActive()) {
            GUI.activate(true, false);
            if (GUI.chatDlg.isVisible()) {
                restoreChatOnClose = true;
                GUI.chatDlg.hideWindow();
            }
        }
    }

    public static void doUpdateMenu()
    {
        if(!bHotKeyESC)
        {
            initHotKeyESC();
            bHotKeyESC = true;
        }
        if(Main3D.cur3D() == null)
            return;
        gwindowroot = Main3D.cur3D().guiManager.root;
        if(gameMenu == null) {
            GWindowRootMenu rootWindow = new GWindowRootMenu();
            rootWindow.textFonts[0] = GFont.New("arial8");
            rootWindow.textFonts[1] = GFont.New("arialb8");
            gameMenu = (GWindowRootMenu)gwindowroot.create(rootWindow);
        } else {
            gameMenu.menuBar.items.clear();
        }
        gameMenu.statusBar.defaultHelp = null;
        
        mMission = gameMenu.menuBar.addItem(Plugin.i18n("Mission   "), Plugin.i18n("Mission Related Options"));
        mMission.subMenu = (GWindowMenu)mMission.create(new GWindowMenu());
        mMission.subMenu.close(false);
        mMission.subMenu.addItem(new GWindowMenuItem(mMission.subMenu, Plugin.i18n("Time Skip"), Plugin.i18n("Skip Mission Time...")) {

            public void created()
            {
                super.bChecked = TimeSkip.isDo();
            }

            public void execute()
            {
                if(TimeSkip.isDo())
                    Main3D.cur3D().timeSkip.stop();
                else
                    Main3D.cur3D().timeSkip.start();
                super.bChecked = TimeSkip.isDo();
            }

        }
).bEnable = !NetMissionTrack.isPlaying() && !Mission.isCoop() && !Mission.isNet();
        int itemDy = (int)mMission.subMenu.getItem(0).win.dy;
        mMission.subMenu.addItem(new GWindowMenuItem(mMission.subMenu, Plugin.i18n("Pause/Play"), Plugin.i18n("Pause/Play Current Mission...")) {

            public void created()
            {
                super.bChecked = Time.isPaused();
            }

            public void execute()
            {
                GUIMenu.doCloseMenu();
                if(TimeSkip.isDo())
                    return;
                if(Time.isEnableChangePause())
                {
                    Time.setPause(!Time.isPaused());
                    if(Config.cur.isSoundUse())
                        if(Time.isPaused())
                            AudioDevice.soundsOff();
                        else
                            AudioDevice.soundsOn();
                }
                GUIMenu.doCloseMenu();
            }

        }
).bEnable = !Mission.isCoop() && !Mission.isNet();
        mMission.subMenu.addItem(new GWindowMenuItem(mMission.subMenu, Plugin.i18n("Time Speed"), Plugin.i18n("Relative Speed of Playing")) {

            public void created()
            {
                sp = (int)(0.5F + Time.speed() * 32F);
                super.subMenu = (GWindowMenu)create(new GWindowMenu());
                super.subMenu.close(false);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "256x", "256 Times the Normal Time (256 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 8192)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(256F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "128x", "128 Times the Normal Time (128 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 4096)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(128F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "64x", "64 Times the Normal Time (64 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 2048)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(64F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "32x", "32 Times the Normal Time (32 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 1024)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(32F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "16x", "16 Times the Normal Time (16 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 512)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(16F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "8x", "8 Times the Normal Time (8 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 256)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(8F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "4x", "4 Times the Normal Time (4 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 128)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(4F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "2x", "2 Times the Normal Time (2 secs of Mission in 1 sec)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 64)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(2F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "Normal (1x)", "Normal Time (neither Speed Up nor Slow Down)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 32)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(1F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "1/2x", "One Half Time the Normal Time (1 sec of Mission in 2 secs)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 16)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(0.5F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "1/4x", "One Quarter the Normal Time (1 sec of Mission in 4 secs)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 8)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(0.25F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "1/8x", "One Eigth the Normal Time (1 sec of Mission in 8 secs)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 4)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(0.125F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "1/16x", "One Sixteenth the Normal Time (1 sec of Mission in 16 secs)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 2)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(0.0625F);
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, "1/32x", "One Thirtyseconds the Normal Time (1 sec of Mission in 32 secs)...") {

                    public void created()
                    {
                        if(GUIMenu.sp == 1)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        GUIMenu.setTimeSpeed(0.03125F);
                    }

                }
);
            }

        }
).bEnable = !Mission.isCoop() && !Mission.isNet();
        mMission.subMenu.addItem(new GWindowMenuItem(mMission.subMenu, Plugin.i18n("Bail Out"), Plugin.i18n("Bail Out from Plane...")) {

            public void execute()
            {
                HotKeyCmd.exec("misc", "ejectPilot");
            }

        }
);
        mMission.subMenu.addItem("-", null);
        mMission.subMenu.addItem(new GWindowMenuItem(mMission.subMenu, Plugin.i18n("Brief"), Plugin.i18n("Current Mission Information...")) {

            public void execute()
            {
                String header = "Notice";
                String text = "Original Brief of this Mission:\n\n";
                try
                {
                    String s = Main.cur().currentMissionFile.fileName();
                    int i = s.length() - 1;
                    do
                    {
                        if(i <= 0)
                            break;
                        char c = s.charAt(i);
                        if(c == '\\' || c == '/')
                            break;
                        if(c == '.')
                        {
                            s = s.substring(0, i);
                            break;
                        }
                        i--;
                    } while(true);
                    ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
                    text = text + resourcebundle.getString("Description");
                }
                catch(Exception exception)
                {
                    text = text + "No Text Description Found";
                }
                new GWindowMessageBox(Main3D.cur3D().guiManager.root, 40F, true, header, text, 3, 0.0F);
                GUI.activate(true, false);
            }

        }
);
        mMission.subMenu.addItem("-", null);
        mMission.subMenu.addItem(new GWindowMenuItem(mMission.subMenu, Plugin.i18n("Cmd Console"), Plugin.i18n("Show/Hide Java Command Console...")) {

            public void execute()
            {
                ConsoleGL0.activate(!ConsoleGL0.isActive());
            }

        }
);
        mMission.subMenu.addItem("-", null);
        mMission.subMenu.addItem(new GWindowMenuItem(mMission.subMenu, Plugin.i18n("Quit"), Plugin.i18n("Quit Current Mission to Main Menu...")) {

            public void execute()
            {
                GUIMenu.doCloseMenu();
                int j = Main.state().id();
                boolean flag = j == 5 || j == 29 || j == 63 || j == 49 || j == 50 || j == 42 || j == 43;
                if(GUI.pad.isActive())
                    GUI.pad.leave(!flag);
                Main.state().doQuitMission();
            }

        }
);
        if(HotKeyEnv.isEnabled("aircraftView"))
        {
            mView = gameMenu.menuBar.addItem(Plugin.i18n("View   "), Plugin.i18n("View Objects and Planes"));
            mView.subMenu = (GWindowMenu)mView.create(new GWindowMenu());
            mView.subMenu.close(false);
            
            ArrayList viewActors[] = new ArrayList[4];
            for (int actorType = 0; actorType < 4; actorType++)
                viewActors[actorType] = new ArrayList();
            
            HashMapExt actorMap = Engine.name2Actor();
            for(Entry entry = actorMap.nextEntry(null); entry != null; entry = actorMap.nextEntry(entry)) {
                final Actor actor = (Actor)entry.getValue();
                if(actor == null || actor.name() == null || actor.name().length() == 0) continue;
                if(actor.pos == null || actor.name().charAt(0) == ' ' || actor instanceof Camera || actor instanceof Render || actor instanceof ActorSoundListener || actor instanceof ActorLand) continue;
                if(!Actor.isValid(actor)) continue;
                if(actor instanceof Aircraft)
                    if(actor.getArmy() == World.getPlayerArmy()) viewActors[0].add(new SelectableActor(actor)); else viewActors[1].add(new SelectableActor(actor));
                else
                    if(actor.getArmy() == World.getPlayerArmy()) viewActors[2].add(new SelectableActor(actor)); else viewActors[3].add(new SelectableActor(actor));
            }
            
            int maxActorsPerItem = (int)gwindowroot.win.dy / itemDy - 1; //(int)mView.subMenu.getItem(0).win.dy - 1;
            String[] menuCaption = {
                    "View Friend Plane",
                    "View Enemy (or Neutral) Plane",
                    "View Friend Actor",
                    "View Enemy (or Neutral) Actor"
                    };
            String[] menuHelp = {
                    "View from outside Planes of your same Country or Army",
                    "View from outside Enemy (or Neutral) Planes",
                    "View from outside Actors of your same Country or Army",
                    "View from outside Enemy (or Neutral) Actors"
                    };
            
            for (int actorType = 0; actorType < 4; actorType++) {
                switch (actorType) {
                    case 0:
                    case 2:
                        if (!Main3D.cur3D().aircraftHotKeys.viewAllowed(Main3D.cur3D().aircraftHotKeys.isbExtViewFriendly())) {
                            mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, menuCaption[actorType], menuHelp[actorType]){}).bEnable=false;
                            continue;
                        }
                    case 1:
                    case 3:
                        if (!Main3D.cur3D().aircraftHotKeys.viewAllowed(Main3D.cur3D().aircraftHotKeys.isbExtPadlockEnemy())) {
                            mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, menuCaption[actorType], menuHelp[actorType]){}).bEnable=false;
                            continue;
                        }
                }
                
                Collections.sort(viewActors[actorType]);
                int actorIndex = 0;
                GWindowMenuItem menuItem = null;
                Iterator iter = viewActors[actorType].iterator();
                while (iter.hasNext()) {
                    
                    final SelectableActor selectableActor = (SelectableActor)iter.next();
                    //System.out.println("viewActors[" + actorType + "] = " + ((SelectableActor)iter.next()).getName());
                    if (actorIndex % maxActorsPerItem == 0) menuItem = mView.subMenu.addItem(
                            new GWindowMenuItem(
                                    mView.subMenu,
                                    menuCaption[actorType] + (actorIndex==0?"...":"(" + ((actorIndex / maxActorsPerItem) + 1) + ")"),
                                    menuHelp[actorType]) {
                                public void created()
                                {
                                    super.subMenu = (GWindowMenu)create(new GWindowMenu());
                                    super.subMenu.close(false);
                                }
                            }
                       );
                    
                    final int finalActorType = actorType;
                    
                    menuItem.subMenu.addItem(
                            actorIndex % maxActorsPerItem,
                            new GWindowMenuItem(menuItem.subMenu, selectableActor.getName(), selectableActor.actor.name()) {

                        public void created()
                        {
                            super.mouseCursor = 4;
                        }

                        public void execute()
                        {
                            System.out.println("* Trying to switch View to '" + selectableActor.getName() + "' " + (finalActorType < 2?"Plane!":"Actor!"));
                            doViewActor(selectableActor);
                            GUIMenu.doCloseMenu();
                        }

                    }
                    );
                    
                    if (actorType == 0) menuItem.subMenu.getItem(actorIndex % maxActorsPerItem).bChecked = selectableActor.actor == World.getPlayerAircraft();

                    actorIndex++;
                    
                }
            }
            
        }
        mOptions = gameMenu.menuBar.addItem(Plugin.i18n("Options   "), Plugin.i18n("Set Game and MOD Options..."));
        mOptions.subMenu = (GWindowMenu)mOptions.create(new GWindowMenu());
        mOptions.subMenu.close(false);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, Plugin.i18n("HUD Units"), Plugin.i18n("System of Units used in the HUD...")) {

            public void created()
            {
                super.subMenu = (GWindowMenu)create(new GWindowMenu());
                super.subMenu.close(false);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, Plugin.i18n("International"), Plugin.i18n("Il-2 game Stock Units")) {

                    public void created()
                    {
                        if(HUD.drawSpeed() == 1)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        HUD.setDrawSpeed(1);
                        GUIMenu.doCloseMenu();
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, Plugin.i18n("Imperial"), Plugin.i18n("British typical UK Units")) {

                    public void created()
                    {
                        if(HUD.drawSpeed() == 2)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        HUD.setDrawSpeed(2);
                        GUIMenu.doCloseMenu();
                    }

                }
);
                super.subMenu.addItem(new GWindowMenuItem(super.subMenu, Plugin.i18n("U.S."), Plugin.i18n("United States Units")) {

                    public void created()
                    {
                        if(HUD.drawSpeed() == 3)
                            super.bChecked = true;
                    }

                    public void execute()
                    {
                        HUD.setDrawSpeed(3);
                        GUIMenu.doCloseMenu();
                    }

                }
);
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "-", null));
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, Plugin.i18n("Show FPS"), Plugin.i18n("Show 'Frames Per Second' Rate in the HUD...")) {

            public void created()
            {
                super.bChecked = Config.cur.bShowFPS;
            }

            public void execute()
            {
                if(!Config.cur.bShowFPS)
                {
                    CmdEnv.top().exec("fps START SHOW");
                    Config.cur.bShowFPS = true;
                } else
                {
                    CmdEnv.top().exec("fps STOP SHOW");
                    Config.cur.bShowFPS = false;
                }
                super.bChecked = Config.cur.bShowFPS;
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Show Status Bar", "View always the Status Bar where this information is displayed") {

            public void created()
            {
                super.bChecked = Config.cur.bStBar;
            }

            public void execute()
            {
                Config.cur.bStBar = !Config.cur.bStBar;
                super.bChecked = Config.cur.bStBar;
                if(Config.cur.bStBar)
                    GUIMenu.gameMenu.statusBar.showWindow();
                else
                    GUIMenu.gameMenu.statusBar.hideWindow();
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Enable Gunner Inertia", "Apply Inertia to Gunner Movement") {

            public void created()
            {
                super.bChecked = Config.cur.inertiaGunnerEnabled;
            }

            public void execute()
            {
                Config.cur.inertiaGunnerEnabled = !Config.cur.inertiaGunnerEnabled;
                super.bChecked = Config.cur.inertiaGunnerEnabled;
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Set Gunner Inertia Factor", "Set factor for Gunner Inertia") {

            public void created()
            {
            }

            public void execute()
            {
                new DlgGunnerInertiaFactor(Main3D.cur3D().guiManager.root);
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Enable Pilot Soft View", "Soften Pilot Head Movement") {

            public void created()
            {
                super.bChecked = Config.cur.inertiaCockpitEnabled;
            }

            public void execute()
            {
                Config.cur.inertiaCockpitEnabled = !Config.cur.inertiaCockpitEnabled;
                super.bChecked = Config.cur.inertiaCockpitEnabled;
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Set Pilot View Smoothing Factor", "Set smoothing value for Pilot View") {

            public void created()
            {
            }

            public void execute()
            {
                new DlgPilotSoftViewFactor(Main3D.cur3D().guiManager.root);
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Enable External Soft View", "Soften External View Movement") {

            public void created()
            {
                super.bChecked = Config.cur.inertiaExternalEnabled;
            }

            public void execute()
            {
                Config.cur.inertiaExternalEnabled = !Config.cur.inertiaExternalEnabled;
                super.bChecked = Config.cur.inertiaExternalEnabled;
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Set External View Smoothing Factor", "Set smoothing value for External View") {

            public void created()
            {
            }

            public void execute()
            {
                new DlgExternalSoftViewFactor(Main3D.cur3D().guiManager.root);
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Enable Follow Inertia", "Apply Inertia to Following Actor Movement") {

            public void created()
            {
                super.bChecked = Config.cur.inertiaFollowEnabled;
            }

            public void execute()
            {
                Config.cur.inertiaFollowEnabled = !Config.cur.inertiaFollowEnabled;
                super.bChecked = Config.cur.inertiaFollowEnabled;
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "Set Follow Inertia Factors", "Set factors for Following Actor Movement Inertia") {

            public void created()
            {
            }

            public void execute()
            {
                new DlgFollowInertiaFactors(Main3D.cur3D().guiManager.root);
            }

        }
);
        mOptions.subMenu.addItem(new GWindowMenuItem(mOptions.subMenu, "External TIR", "Use TIR on External Views") {

            public void created()
            {
                super.bChecked = Config.cur.externalTrackIREnabled;
            }

            public void execute()
            {
                Config.cur.externalTrackIREnabled = !Config.cur.externalTrackIREnabled;
                super.bChecked = Config.cur.externalTrackIREnabled;
            }

        }
);
    }
    

    
    public static class DlgFollowInertiaFactors extends GWindowFramed
    {
        
        private final static int valueMin = 0;
        private final static int valueMid = 499;
        private final static int valueMax = 999;
        
        private final static float configMin = 0.001F;
        private final static float configMid = 0.01F;
        private final static float configMax = 1.0F;
        
        private final static float v2cExp = 2.0F;
        
        public void doOk()
        {
            Config.cur.inertiaFollowPositionValue = valueToConfig(hs1.pos());
            Config.cur.inertiaFollowAngleValue = valueToConfig(hs2.pos());
        }

        public void doCancel()
        {
        }

        public void doDefault()
        {
            hs1.setPos(configToValue(configMid), true);
            hs2.setPos(configToValue(configMid), true);
        }
        
        public int configToValue(float config) {
            if (config < 0.001F) config = 0.001F;
            if (config > 1F) config = 1F;
            
            if (config > configMid) {
                return (int)(valueMid + (float)(valueMax-valueMid) * (float)Math.pow((float)(config-configMid) / (float)(configMax-configMid), 1F/v2cExp));
            } else {
                return (int)(valueMid - (float)(valueMid-valueMin) * (float)Math.pow((float)(configMid-config) / (float)(configMid-configMin), 1F/v2cExp));
            }
        }
        
        public float valueToConfig(int value) {
            if (value < 0) value = 0;
            if (value > 999) value = 999;
            if (value > valueMid) {
                return configMid + (configMax-configMid) * (float)Math.pow((float)(value-valueMid) / (float)(valueMax-valueMid), v2cExp);
            } else {
                return configMid - (configMid-configMin) * (float)Math.pow((float)(valueMid-value) / (float)(valueMid-valueMin), v2cExp);
            }
        }

        public void setValue1(int value) {
            labelValue1.cap.caption = df.format(valueToConfig(value));
        }
        
        public void setValue2(int value) {
            labelValue2.cap.caption = df.format(valueToConfig(value));
        }

        public void afterCreated()
        {
            clientWindow = create(new GWindowDialogClient() {

                public boolean notify(GWindow gwindow, int i, int j)
                {
                    if(i != 2)
                        return super.notify(gwindow, i, j);
                    if(gwindow == bOk) {
                        doOk();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bCancel) {
                        doCancel();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bDefault) {
                        doDefault();
                        return true;
                    } else {
                        return super.notify(gwindow, i, j);
                    }
                }
                
            }
);
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
            gwindowdialogclient.addLabel(labelCaption1 = new GWindowLabel(gwindowdialogclient, 3.5F, 1.0F, 15F, 1.5F, "Position Inertia Value", null));
            gwindowdialogclient.addLabel(labelMin1 = new GWindowLabel(gwindowdialogclient, 0F, 2.5F, 3F, 1.5F, "Min", null));
            gwindowdialogclient.addLabel(labelMax1 = new GWindowLabel(gwindowdialogclient, 19.0F, 2.5F, 3F, 1.5F, "Max", null));
            gwindowdialogclient.addLabel(labelValue1 = new GWindowLabel(gwindowdialogclient, 8.5F, 4.0F, 5F, 1.5F, df.format(configMid), null));
            labelCaption1.align = GWindow.ALIGN_CENTER;
            labelMin1.align = GWindow.ALIGN_RIGHT;
            labelMax1.align = GWindow.ALIGN_LEFT;
            labelValue1.align = GWindow.ALIGN_CENTER;
            gwindowdialogclient.addControl(hs1 = new GWindowHSliderInt(clientWindow) {
                public boolean notify(int i, int j)
                {
                    setValue1(pos());
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }
            });
            hs1.setRange(0, 1000, configToValue(Config.cur.inertiaFollowPositionValue));
            hs1.setEnable(true);
            hs1.set1024PosSize(48F, 45F, 200F, 32F);
            
            
            gwindowdialogclient.addLabel(labelCaption2 = new GWindowLabel(gwindowdialogclient, 3.5F, 6.0F, 15F, 1.5F, "Angle Inertia Value", null));
            gwindowdialogclient.addLabel(labelMin2 = new GWindowLabel(gwindowdialogclient, 0F, 7.5F, 3F, 1.5F, "Min", null));
            gwindowdialogclient.addLabel(labelMax2 = new GWindowLabel(gwindowdialogclient, 19.0F, 7.5F, 3F, 1.5F, "Max", null));
            gwindowdialogclient.addLabel(labelValue2 = new GWindowLabel(gwindowdialogclient, 8.5F, 9.0F, 5F, 1.5F, df.format(configMid), null));
            labelCaption2.align = GWindow.ALIGN_CENTER;
            labelMin2.align = GWindow.ALIGN_RIGHT;
            labelMax2.align = GWindow.ALIGN_LEFT;
            labelValue2.align = GWindow.ALIGN_CENTER;
            gwindowdialogclient.addControl(hs2 = new GWindowHSliderInt(clientWindow) {
                public boolean notify(int i, int j)
                {
                    setValue2(pos());
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }
            });
            hs2.setRange(0, 1000, configToValue(Config.cur.inertiaFollowAngleValue));
            hs2.setEnable(true);
            hs2.set1024PosSize(48F, 135F, 200F, 32F);

            
            
            gwindowdialogclient.addDefault(bOk = new GWindowButton(gwindowdialogclient, 1F, 11F, 6F, 2.0F, I18N.gui("netns.Ok"), null));
            gwindowdialogclient.addControl(bDefault = new GWindowButton(gwindowdialogclient, 8F, 11F, 6F, 2.0F, I18N.gui("neta.Default"), null));
            gwindowdialogclient.addEscape(bCancel = new GWindowButton(gwindowdialogclient, 15F, 11F, 6F, 2.0F, I18N.gui("netns.Cancel"), null));
            super.afterCreated();
            resized();
            gameMenu.statusBar.defaultHelp = "Factors for Following Actor Movement Inertia, smaller values mean more inertia applied";
            showModal();
        }

        GWindowButton bOk;
        GWindowButton bCancel;
        GWindowButton bDefault;
        GWindowLabel labelValue1;
        GWindowLabel labelCaption1;
        GWindowLabel labelMin1;
        GWindowLabel labelMax1;
        GWindowHSliderInt hs1;
        GWindowLabel labelValue2;
        GWindowLabel labelCaption2;
        GWindowLabel labelMin2;
        GWindowLabel labelMax2;
        GWindowHSliderInt hs2;
        DecimalFormat df = new DecimalFormat("0.000");

        public DlgFollowInertiaFactors(GWindow gwindow)
        {
            bSizable = false;
            title = "Follow Inertia Factors";
            float f = 22F;
            float f1 = 16F;
            float f2 = gwindow.win.dx / gwindow.lookAndFeel().metric();
            float f3 = gwindow.win.dy / gwindow.lookAndFeel().metric();
            float f4 = (f2 - f) / 2.0F;
            float f5 = (f3 - f1) / 2.0F;
            
            doNew(gwindow, f4, f5, f, f1, true);
            bringToFront();
            activateWindow();
        }
    }

    
    
    

    
    
    
    
    
    
    public static class DlgGunnerInertiaFactor extends GWindowFramed
    {
        
        private final static int valueMin = 0;
        private final static int valueMid = 499;
        private final static int valueMax = 999;
        
        private final static float configMin = 0.001F;
        private final static float configMid = 0.01F;
        private final static float configMax = 1.0F;
        
        private final static float v2cExp = 2.0F;
        
        public void doOk()
        {
            Config.cur.inertiaGunnerValue = valueToConfig(hs.pos());
        }

        public void doCancel()
        {
        }

        public void doDefault()
        {
            hs.setPos(configToValue(configMid), true);
        }
        
        public int configToValue(float config) {
            if (config < 0.001F) config = 0.001F;
            if (config > 1F) config = 1F;
            
            if (config > configMid) {
                return (int)(valueMid + (float)(valueMax-valueMid) * (float)Math.pow((float)(config-configMid) / (float)(configMax-configMid), 1F/v2cExp));
            } else {
                return (int)(valueMid - (float)(valueMid-valueMin) * (float)Math.pow((float)(configMid-config) / (float)(configMid-configMin), 1F/v2cExp));
            }
        }
        
        public float valueToConfig(int value) {
            if (value < 0) value = 0;
            if (value > 999) value = 999;
            if (value > valueMid) {
                return configMid + (configMax-configMid) * (float)Math.pow((float)(value-valueMid) / (float)(valueMax-valueMid), v2cExp);
            } else {
                return configMid - (configMid-configMin) * (float)Math.pow((float)(valueMid-value) / (float)(valueMid-valueMin), v2cExp);
            }
        }

        public void setValue(int value) {
            labelValue.cap.caption = df.format(valueToConfig(value));
        }
        
        public void afterCreated()
        {
            clientWindow = create(new GWindowDialogClient() {

                public boolean notify(GWindow gwindow, int i, int j)
                {
                    if(i != 2)
                        return super.notify(gwindow, i, j);
                    if(gwindow == bOk) {
                        doOk();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bCancel) {
                        doCancel();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bDefault) {
                        doDefault();
                        return true;
                    } else {
                        return super.notify(gwindow, i, j);
                    }
                }
                
            }
);
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
            gwindowdialogclient.addLabel(labelCaption = new GWindowLabel(gwindowdialogclient, 3.5F, 1.0F, 15F, 1.5F, "Inertia Value", null));
            gwindowdialogclient.addLabel(labelMin = new GWindowLabel(gwindowdialogclient, 0F, 2.5F, 3F, 1.5F, "Min", null));
            gwindowdialogclient.addLabel(labelMax = new GWindowLabel(gwindowdialogclient, 19.0F, 2.5F, 3F, 1.5F, "Max", null));
            gwindowdialogclient.addLabel(labelValue = new GWindowLabel(gwindowdialogclient, 8.5F, 4.0F, 5F, 1.5F, df.format(configMid), null));
            labelCaption.align = GWindow.ALIGN_CENTER;
            labelMin.align = GWindow.ALIGN_RIGHT;
            labelMax.align = GWindow.ALIGN_LEFT;
            labelValue.align = GWindow.ALIGN_CENTER;
            gwindowdialogclient.addControl(hs = new GWindowHSliderInt(clientWindow) {
                public boolean notify(int i, int j)
                {
                    setValue(pos());
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }
            });
            hs.setRange(0, 1000, configToValue(Config.cur.inertiaGunnerValue));
            hs.setEnable(true);
            hs.set1024PosSize(48F, 45F, 200F, 32F);
            gwindowdialogclient.addDefault(bOk = new GWindowButton(gwindowdialogclient, 1F, 6F, 6F, 2.0F, I18N.gui("netns.Ok"), null));
            gwindowdialogclient.addControl(bDefault = new GWindowButton(gwindowdialogclient, 8F, 6F, 6F, 2.0F, I18N.gui("neta.Default"), null));
            gwindowdialogclient.addEscape(bCancel = new GWindowButton(gwindowdialogclient, 15F, 6F, 6F, 2.0F, I18N.gui("netns.Cancel"), null));
            super.afterCreated();
            resized();
            gameMenu.statusBar.defaultHelp = "Gunner Inertia Factor, smaller value means more inertia applied";
            showModal();
        }

        GWindowButton bOk;
        GWindowButton bCancel;
        GWindowButton bDefault;
        GWindowLabel labelValue;
        GWindowLabel labelCaption;
        GWindowLabel labelMin;
        GWindowLabel labelMax;
        GWindowHSliderInt hs;
        DecimalFormat df = new DecimalFormat("0.000");

        public DlgGunnerInertiaFactor(GWindow gwindow)
        {
            bSizable = false;
            title = "Gunner Inertia Factor";
            float f = 22F;
            float f1 = 12F;
            float f2 = gwindow.win.dx / gwindow.lookAndFeel().metric();
            float f3 = gwindow.win.dy / gwindow.lookAndFeel().metric();
            float f4 = (f2 - f) / 2.0F;
            float f5 = (f3 - f1) / 2.0F;
            
            doNew(gwindow, f4, f5, f, f1, true);
            bringToFront();
            activateWindow();
        }
    }

    
    
    

    
    
    
    
    
    public static class DlgPilotSoftViewFactor extends GWindowFramed
    {
        
        private final static int valueMin = 0;
        private final static int valueMid = 499;
        private final static int valueMax = 999;
        
        private final static float configMin = 0.01F;
        private final static float configMid = 0.075F;
        private final static float configMax = 1.0F;
        
        private final static float v2cExp = 2.0F;
        
        public void doOk()
        {
            Config.cur.inertiaCockpitValue = valueToConfig(hs.pos());
        }

        public void doCancel()
        {
        }

        public void doDefault()
        {
            hs.setPos(configToValue(configMid), true);
        }
        
        public int configToValue(float config) {
            if (config < 0.001F) config = 0.001F;
            if (config > 1F) config = 1F;
            
            if (config > configMid) {
                return (int)(valueMid + (float)(valueMax-valueMid) * (float)Math.pow((float)(config-configMid) / (float)(configMax-configMid), 1F/v2cExp));
            } else {
                return (int)(valueMid - (float)(valueMid-valueMin) * (float)Math.pow((float)(configMid-config) / (float)(configMid-configMin), 1F/v2cExp));
            }
        }
        
        public float valueToConfig(int value) {
            if (value < 0) value = 0;
            if (value > 999) value = 999;
            if (value > valueMid) {
                return configMid + (configMax-configMid) * (float)Math.pow((float)(value-valueMid) / (float)(valueMax-valueMid), v2cExp);
            } else {
                return configMid - (configMid-configMin) * (float)Math.pow((float)(valueMid-value) / (float)(valueMid-valueMin), v2cExp);
            }
        }

        public void setValue(int value) {
            labelValue.cap.caption = df.format(valueToConfig(value));
        }
        
        public void afterCreated()
        {
            clientWindow = create(new GWindowDialogClient() {

                public boolean notify(GWindow gwindow, int i, int j)
                {
                    if(i != 2)
                        return super.notify(gwindow, i, j);
                    if(gwindow == bOk) {
                        doOk();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bCancel) {
                        doCancel();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bDefault) {
                        doDefault();
                        return true;
                    } else {
                        return super.notify(gwindow, i, j);
                    }
                }
                
            }
);
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
            gwindowdialogclient.addLabel(labelCaption = new GWindowLabel(gwindowdialogclient, 3.5F, 1.0F, 15F, 1.5F, "Smoothing Value", null));
            gwindowdialogclient.addLabel(labelMin = new GWindowLabel(gwindowdialogclient, 0F, 2.5F, 3F, 1.5F, "Min", null));
            gwindowdialogclient.addLabel(labelMax = new GWindowLabel(gwindowdialogclient, 19.0F, 2.5F, 3F, 1.5F, "Max", null));
            gwindowdialogclient.addLabel(labelValue = new GWindowLabel(gwindowdialogclient, 8.5F, 4.0F, 5F, 1.5F, df.format(configMid), null));
            labelCaption.align = GWindow.ALIGN_CENTER;
            labelMin.align = GWindow.ALIGN_RIGHT;
            labelMax.align = GWindow.ALIGN_LEFT;
            labelValue.align = GWindow.ALIGN_CENTER;
            gwindowdialogclient.addControl(hs = new GWindowHSliderInt(clientWindow) {
                public boolean notify(int i, int j)
                {
                    setValue(pos());
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }
            });
            hs.setRange(0, 1000, configToValue(Config.cur.inertiaCockpitValue));
            hs.setEnable(true);
            hs.set1024PosSize(48F, 45F, 200F, 32F);
            gwindowdialogclient.addDefault(bOk = new GWindowButton(gwindowdialogclient, 1F, 6F, 6F, 2.0F, I18N.gui("netns.Ok"), null));
            gwindowdialogclient.addControl(bDefault = new GWindowButton(gwindowdialogclient, 8F, 6F, 6F, 2.0F, I18N.gui("neta.Default"), null));
            gwindowdialogclient.addEscape(bCancel = new GWindowButton(gwindowdialogclient, 15F, 6F, 6F, 2.0F, I18N.gui("netns.Cancel"), null));
            super.afterCreated();
            resized();
            gameMenu.statusBar.defaultHelp = "Smoothing Value for Pilot View, smaller value means smoother movement";
            showModal();
        }

        GWindowButton bOk;
        GWindowButton bCancel;
        GWindowButton bDefault;
        GWindowLabel labelValue;
        GWindowLabel labelCaption;
        GWindowLabel labelMin;
        GWindowLabel labelMax;
        GWindowHSliderInt hs;
        DecimalFormat df = new DecimalFormat("0.000");

        public DlgPilotSoftViewFactor(GWindow gwindow)
        {
            bSizable = false;
            title = "Pilot View Smoothing Factor";
            float f = 22F;
            float f1 = 12F;
            float f2 = gwindow.win.dx / gwindow.lookAndFeel().metric();
            float f3 = gwindow.win.dy / gwindow.lookAndFeel().metric();
            float f4 = (f2 - f) / 2.0F;
            float f5 = (f3 - f1) / 2.0F;
            
            doNew(gwindow, f4, f5, f, f1, true);
            bringToFront();
            activateWindow();
        }
    }

    
    
    
    
    
    
    public static class DlgExternalSoftViewFactor extends GWindowFramed
    {
        
        private final static int valueMin = 0;
        private final static int valueMid = 499;
        private final static int valueMax = 999;
        
        private final static float configMin = 0.001F;
        private final static float configMid = 0.075F;
        private final static float configMax = 1.0F;
        
        private final static float v2cExp = 2.0F;
        
        public void doOk()
        {
            Config.cur.inertiaExternalValue = valueToConfig(hs.pos());
        }

        public void doCancel()
        {
        }

        public void doDefault()
        {
            hs.setPos(configToValue(configMid), true);
        }
        
        public int configToValue(float config) {
            if (config < 0.001F) config = 0.001F;
            if (config > 1F) config = 1F;
            
            if (config > configMid) {
                return (int)(valueMid + (float)(valueMax-valueMid) * (float)Math.pow((float)(config-configMid) / (float)(configMax-configMid), 1F/v2cExp));
            } else {
                return (int)(valueMid - (float)(valueMid-valueMin) * (float)Math.pow((float)(configMid-config) / (float)(configMid-configMin), 1F/v2cExp));
            }
        }
        
        public float valueToConfig(int value) {
            if (value < 0) value = 0;
            if (value > 999) value = 999;
            if (value > valueMid) {
                return configMid + (configMax-configMid) * (float)Math.pow((float)(value-valueMid) / (float)(valueMax-valueMid), v2cExp);
            } else {
                return configMid - (configMid-configMin) * (float)Math.pow((float)(valueMid-value) / (float)(valueMid-valueMin), v2cExp);
            }
        }

        public void setValue(int value) {
            labelValue.cap.caption = df.format(valueToConfig(value));
        }
        
        public void afterCreated()
        {
            clientWindow = create(new GWindowDialogClient() {

                public boolean notify(GWindow gwindow, int i, int j)
                {
                    if(i != 2)
                        return super.notify(gwindow, i, j);
                    if(gwindow == bOk) {
                        doOk();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bCancel) {
                        doCancel();
                        close(false);
                        gameMenu.statusBar.defaultHelp = null;
                        return true;
                    } else if(gwindow == bDefault) {
                        doDefault();
                        return true;
                    } else {
                        return super.notify(gwindow, i, j);
                    }
                }
                
            }
);
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
            gwindowdialogclient.addLabel(labelCaption = new GWindowLabel(gwindowdialogclient, 3.5F, 1.0F, 15F, 1.5F, "Smoothing Value", null));
            gwindowdialogclient.addLabel(labelMin = new GWindowLabel(gwindowdialogclient, 0F, 2.5F, 3F, 1.5F, "Min", null));
            gwindowdialogclient.addLabel(labelMax = new GWindowLabel(gwindowdialogclient, 19.0F, 2.5F, 3F, 1.5F, "Max", null));
            gwindowdialogclient.addLabel(labelValue = new GWindowLabel(gwindowdialogclient, 8.5F, 4.0F, 5F, 1.5F, df.format(configMid), null));
            labelCaption.align = GWindow.ALIGN_CENTER;
            labelMin.align = GWindow.ALIGN_RIGHT;
            labelMax.align = GWindow.ALIGN_LEFT;
            labelValue.align = GWindow.ALIGN_CENTER;
            gwindowdialogclient.addControl(hs = new GWindowHSliderInt(clientWindow) {
                public boolean notify(int i, int j)
                {
                    setValue(pos());
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }
            });
            hs.setRange(0, 1000, configToValue(Config.cur.inertiaExternalValue));
            hs.setEnable(true);
            hs.set1024PosSize(48F, 45F, 200F, 32F);
            gwindowdialogclient.addDefault(bOk = new GWindowButton(gwindowdialogclient, 1F, 6F, 6F, 2.0F, I18N.gui("netns.Ok"), null));
            gwindowdialogclient.addControl(bDefault = new GWindowButton(gwindowdialogclient, 8F, 6F, 6F, 2.0F, I18N.gui("neta.Default"), null));
            gwindowdialogclient.addEscape(bCancel = new GWindowButton(gwindowdialogclient, 15F, 6F, 6F, 2.0F, I18N.gui("netns.Cancel"), null));
            super.afterCreated();
            resized();
            gameMenu.statusBar.defaultHelp = "Smoothing Value for External View, smaller value means smoother movement";
            showModal();
        }

        GWindowButton bOk;
        GWindowButton bCancel;
        GWindowButton bDefault;
        GWindowLabel labelValue;
        GWindowLabel labelCaption;
        GWindowLabel labelMin;
        GWindowLabel labelMax;
        GWindowHSliderInt hs;
        DecimalFormat df = new DecimalFormat("0.000");

        public DlgExternalSoftViewFactor(GWindow gwindow)
        {
            bSizable = false;
            title = "External View Smoothing Factor";
            float f = 22F;
            float f1 = 12F;
            float f2 = gwindow.win.dx / gwindow.lookAndFeel().metric();
            float f3 = gwindow.win.dy / gwindow.lookAndFeel().metric();
            float f4 = (f2 - f) / 2.0F;
            float f5 = (f3 - f1) / 2.0F;
            
            doNew(gwindow, f4, f5, f, f1, true);
            bringToFront();
            activateWindow();
        }
    }

    
    public static String getSimpleName(Class class1) {
        return class1.getName().substring(class1.getName().lastIndexOf(".") + 1);
    }


    public static String getSimpleInnerName(Class class1) {
        String simpleName = getSimpleName(class1);
        return simpleName.substring(simpleName.lastIndexOf("$") + 1);
    }

    public static void doViewActor(SelectableActor selectableActor)
    {
        Actor actor1 = Actor.getByName("camera");
        if(actor1 == null)
            System.out.println("* Camera named 'camera' not found!");
        if(selectableActor.actor != null)
        {
            if(selectableActor.actor.pos == null)
                System.out.println("* Actor '" + selectableActor.getName() + "' doesn't have a position!");
            Main3D.cur3D().setViewFlow10(selectableActor.actor, false);
            Class class1 = selectableActor.actor.getClass();
            String name = Property.stringValue(class1, "iconFar_shortClassName", null);
            if(name == null) {
                name = getSimpleInnerName(class1);
            }
            HUD.log("Viewing Actor '"+ selectableActor.getName() + "'.");
        }
    }
    
    private static boolean bHotKeyESC = false;
    private static GWindowRootMenu gameMenu;
    private static GWindowRoot gwindowroot;
    private static GWindowMenuBarItem mMission;
    private static GWindowMenuBarItem mView;
    private static GWindowMenuBarItem mOptions;
    static int sp = (int)(0.5F + Time.speedReal() * 32F);
    private static boolean restoreChatOnClose = false;
}
