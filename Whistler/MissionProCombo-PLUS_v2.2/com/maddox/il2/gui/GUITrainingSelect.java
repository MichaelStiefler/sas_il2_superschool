package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUITrainingSelect extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == wPrev)
            {
                Main3D.cur3D().viewSet_Load();
                Main.stateStack().pop();
            } else
            if(gwindow == wPlay)
            {
                int k = wTable.selectRow;
                if(k < 0 || k >= wTable.tracks.size())
                {
                    return true;
                } else
                {
                    selectedTrack = (String)wTable.tracks.get(k);
                    Main.stateStack().push(57);
                    return true;
                }
            }
            return super.notify(gwindow, i, j);
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(464F), x1024(720F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(96F), y1024(496F), x1024(208F), y1024(48F), 0, i18n("training.MainMenu"));
            draw(x1024(448F), y1024(496F), x1024(240F), y1024(48F), 2, i18n("record.Play"));
        }

        public void setPosSize()
        {
            set1024PosSize(128F, 112F, 784F, 576F);
            wPrev.setPosC(x1024(56F), y1024(520F));
            wPlay.setPosC(x1024(728F), y1024(520F));
            wTable.setPosSize(x1024(32F), y1024(32F), x1024(720F), y1024(400F));
        }

        public DialogClient()
        {
        }
    }

    public class Table extends GWindowTable
    {

        public int countRows()
        {
            return tracks == null ? 0 : tracks.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            setCanvasFont(0);
            if(flag)
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, lookAndFeel().regionWhite);
                setCanvasColorWHITE();
                draw(0.0F, 0.0F, f, f1, 0, (String)names.get(i));
            } else
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, 0, (String)names.get(i));
            }
        }

        public void afterCreated()
        {
            super.afterCreated();
            bColumnsSizable = false;
            addColumn(I18N.gui("training.Tracks"), null);
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

        public ArrayList tracks;
        public ArrayList names;

        public Table(GWindow gwindow)
        {
            super(gwindow);
            tracks = new ArrayList();
            names = new ArrayList();
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
        fillTracks();
        client.activateWindow();
    }

    public void leavePop(GameState gamestate)
    {
        World.cur().setUserCovers();
        super.leavePop(gamestate);
    }

    public void _leave()
    {
        client.hideWindow();
    }

    public void fillTracks()
    {
        wTable.tracks.clear();
        SectFile sectfile = new SectFile("Training/all.ini", 0);
        int i = sectfile.sectionIndex("all");
        if(i >= 0)
        {
            ResourceBundle resourcebundle = null;
            try
            {
                resourcebundle = ResourceBundle.getBundle("Training/all", RTSConf.cur.locale);
            }
            catch(Exception exception) { }
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                String s = sectfile.line(i, k);
                wTable.tracks.add(s);
                try
                {
                    wTable.names.add(resourcebundle.getString(s));
                }
                catch(Exception exception1)
                {
                    wTable.names.add(s);
                }
            }

//            if(j > 0)
//                wTable.setSelect(0, 0);
        }
        wTable.resized();
    }

    public GUITrainingSelect(GWindowRoot gwindowroot)
    {
        super(56);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("training.infoSelect");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wTable = new Table(dialogClient);
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        wPlay = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public String selectedTrack;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton wPrev;
    public GUIButton wPlay;
    public Table wTable;
}
