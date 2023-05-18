package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import java.io.File;
import java.util.*;

public class GUIRecordSelect extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == wPrev)
            {
                sCycle.setChecked(bCycle, false);
                sTimeCompression.setChecked(bManualTimeCompression, false);
                sViewControls.setChecked(bManualViewControls, false);
                Main3D.cur3D();
                sIcons.setChecked(Main3D.bOverrideIcons, false);
                Main3D.cur3D().viewSet_Load();
                Main.stateStack().pop();
            } else
            {
                if(gwindow == wPlay)
                {
                    bCycle = sCycle.isChecked();
                    bManualTimeCompression = sTimeCompression.isChecked();
                    bManualViewControls = sViewControls.isChecked();
                    Main3D.cur3D().setIconsOverride(sIcons.isChecked());
                    int k = wTable.selectRow;
                    if(k < 0 || k >= wTable.files.size())
                    {
                        return true;
                    } else
                    {
                        selectedFile = (String)wTable.files.get(k);
                        Main.stateStack().push(8);
                        return true;
                    }
                }
                if(gwindow == wDelete)
                {
                    int l = wTable.selectRow;
                    if(l < 0 || l >= wTable.files.size())
                    {
                        return true;
                    } else
                    {
                        new GWindowMessageBox(root, 20F, true, i18n("warning.Warning"), i18n("warning.DeleteFile"), 1, 0.0F) {

                            public void result(int i1)
                            {
                                if(i1 != 3)
                                    return;
                                int j1 = wTable.selectRow;
                                String s = (String)wTable.files.get(j1);
                                try
                                {
                                    File file = new File(HomePath.toFileSystemName("Records/" + s, 0));
                                    file.delete();
                                }
                                catch(Exception exception) { }
                                fillFiles();
                                if(j1 >= wTable.files.size())
                                    j1 = wTable.files.size() - 1;
                                if(j1 < 0)
                                {
                                    return;
                                } else
                                {
                                    wTable.setSelect(j1, 0);
                                    return;
                                }
                            }

                        }
;
                        return true;
                    }
                }
                if(gwindow == sViewMessages)
                {
                    Main3D.cur3D().hud.bDrawAllMessages = sViewMessages.isChecked();
                    bDrawAllMessages = sViewMessages.isChecked();
                }
            }
            return super.notify(gwindow, i, j);
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(464F), x1024(720F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(448F), y1024(372F), x1024(305F), 2.0F);
//            GUISeparate.draw(this, GColor.Gray, x1024(432F), y1024(32F), 2.0F, y1024(400F));
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(528F), y1024(34F), x1024(224F), y1024(48F), i18n("record.Cycle"), 2);
            draw(x1024(528F), y1024(99F), x1024(224F), y1024(48F), i18n("record.ManualTime"), 2);
            draw(x1024(528F), y1024(164F), x1024(224F), y1024(48F), i18n("record.ManualView"), 2);
            draw(x1024(528F), y1024(229F), x1024(224F), y1024(48F), i18n("record.InflightMessages"), 2);
            draw(x1024(528F), y1024(294F), x1024(224F), y1024(48F), i18n("record.RemoveIcons"), 2);
            draw(x1024(528F), y1024(394F), x1024(224F), y1024(48F), i18n("record.Delete"), 2);
            draw(x1024(96F), y1024(496F), x1024(208F), y1024(48F), 0, i18n("record.MainMenu"));
            draw(x1024(448F), y1024(496F), x1024(240F), y1024(48F), 2, i18n("record.Play"));
        }

        public void setPosSize()
        {
            set1024PosSize(128F, 112F, 784F, 576F);
            wPrev.setPosC(x1024(56F), y1024(520F));
            wPlay.setPosC(x1024(728F), y1024(520F));
            wDelete.setPosC(x1024(478F), y1024(418F));
            sCycle.setPosC(x1024(486F), y1024(57F));
            sTimeCompression.setPosC(x1024(486F), y1024(122F));
            sViewControls.setPosC(x1024(486F), y1024(187F));
            sViewMessages.setPosC(x1024(486F), y1024(252F));
            wTable.setPosSize(x1024(32F), y1024(32F), x1024(384F), y1024(400F));
            sIcons.setPosC(x1024(486F), y1024(317F));
        }


        public DialogClient()
        {
        }
    }

    public class Table extends GWindowTable
    {

        public int countRows()
        {
            return files == null ? 0 : files.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            setCanvasFont(0);
            if(flag)
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, lookAndFeel().regionWhite);
                setCanvasColorWHITE();
                draw(0.0F, 0.0F, f, f1, 0, (String)files.get(i));
            } else
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, 0, (String)files.get(i));
            }
        }

        public void afterCreated()
        {
            super.afterCreated();
            bColumnsSizable = false;
            addColumn(I18N.gui("record.TrackFiles"), null);
            vSB.scroll = rowHeight(0);
            resized();
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
        }

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(super.notify(gwindow, i, j))
            {
                return true;
            } else
            {
                notify(i, j);
                return false;
            }
        }

        public ArrayList files;

        public Table(GWindow gwindow)
        {
            super(gwindow);
            files = new ArrayList();
            bNotify = true;
            wClient.bNotify = true;
        }
    }


    public void _enter()
    {
        if(Mission.cur() != null && !Mission.cur().isDestroyed())
            Mission.cur().destroy();
        if(Main3D.cur3D().keyRecord != null)
            Main3D.cur3D().keyRecord.clearRecorded();
        bSaveManualTimeCompression = HotKeyEnv.isEnabled("timeCompression");
        bSaveManualViewControls = HotKeyEnv.isEnabled("aircraftView");
        sCycle.setChecked(bCycle, false);
        sTimeCompression.setChecked(bManualTimeCompression, false);
        sViewControls.setChecked(bManualViewControls, false);
        sViewMessages.setChecked(bDrawAllMessages, false);
        Main3D.cur3D();
        sIcons.setChecked(Main3D.bOverrideIcons, false);
        Main3D.cur3D().hud.bDrawAllMessages = bDrawAllMessages;
        fillFiles();
        client.activateWindow();
    }

    public void leavePop(GameState gamestate)
    {
        Main3D.cur3D().hud.bDrawAllMessages = true;
        Main3D.cur3D().setIconsOverride(false);
        World.cur().setUserCovers();
        super.leavePop(gamestate);
    }

    public void _leave()
    {
        HotKeyEnv.enable("timeCompression", bSaveManualTimeCompression);
        HotKeyEnv.enable("aircraftView", bSaveManualViewControls);
        HotKeyEnv.enable("HookView", bSaveManualViewControls);
        HotKeyEnv.enable("HeadMove", bSaveManualViewControls);
        HotKeyEnv.enable("PanView", bSaveManualViewControls);
        HotKeyEnv.enable("SnapView", bSaveManualViewControls);
        client.hideWindow();
    }

    public void fillFiles()
    {
        wTable.files.clear();
        File file = new File(HomePath.get(0), "Records");
        File afile[] = file.listFiles();
        if(afile != null && afile.length > 0)
        {
            for(int i = 0; i < afile.length; i++)
                if(!afile[i].isDirectory() && !afile[i].isHidden())
                    _scanMap.put(afile[i].getName(), null);

            for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wTable.files.add(iterator.next()));
//            if(_scanMap.size() > 0)
//                wTable.setSelect(0, 0);
            _scanMap.clear();
        }
        wTable.resized();
    }

    public GUIRecordSelect(GWindowRoot gwindowroot)
    {
        super(7);
        bCycle = false;
        bManualTimeCompression = false;
        bManualViewControls = false;
        bDrawAllMessages = true;
        bSaveManualTimeCompression = false;
        bSaveManualViewControls = false;
        _scanMap = new TreeMap();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("record.infoSelect");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wTable = new Table(dialogClient);
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        wPlay = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        wDelete = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        sCycle = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sTimeCompression = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sViewControls = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sViewMessages = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sIcons = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sIcons.setChecked(false, false);
        sCycle.setChecked(bCycle, false);
        sTimeCompression.setChecked(bManualTimeCompression, false);
        sViewControls.setChecked(bManualViewControls, false);
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public String selectedFile;
    public boolean bCycle;
    public boolean bManualTimeCompression;
    public boolean bManualViewControls;
    public boolean bDrawAllMessages;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton wPrev;
    public GUIButton wPlay;
    public GUIButton wDelete;
    public Table wTable;
    public GUISwitchBox3 sCycle;
    public GUISwitchBox3 sTimeCompression;
    public GUISwitchBox3 sViewControls;
    public GUISwitchBox3 sViewMessages;
    public GUISwitchBox3 sIcons;
    public boolean bSaveManualTimeCompression;
    public boolean bSaveManualViewControls;
    public TreeMap _scanMap;
}
