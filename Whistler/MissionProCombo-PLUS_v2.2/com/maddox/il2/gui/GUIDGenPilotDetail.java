package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.game.*;
import com.maddox.il2.game.campaign.Campaign;
import com.maddox.rts.*;
import com.maddox.util.UnicodeTo8bit;
import java.io.*;
import java.util.ArrayList;

public class GUIDGenPilotDetail extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bBack)
            {
                Main.stateStack().pop();
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(96F), y1024(658F), x1024(288F), y1024(48F), 0, i18n("camps.Back"));
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(624F), x1024(960F), 2.0F);
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bBack.setPosC(x1024(56F), y1024(682F));
            wTable.set1024PosSize(32F, 32F, 960F, 560F);
        }

        public DialogClient()
        {
        }
    }

    public class Table extends GWindowTable
    {

        private void computeHeight(int i)
        {
            Event event = (Event)events.get(i);
            event.h = (int)(fnt.height * 1.2F);
            float f = fnt.height - fnt.descender;
            int j = (int)((float)computeLines(event.mission, 0, event.mission.length(), c[2]) * f);
            if(event.h < j)
                event.h = j;
            j = 0;
            for(int k = 0; k < event.actions.size(); k++)
            {
                String s = (String)event.actions.get(k);
                j += (int)((float)computeLines(s, 0, s.length(), c[4]) * f);
            }

            if(event.h < j)
                event.h = j;
        }

        private void computeHeights()
        {
            root.C.font = fnt;
            for(int i = 0; i < 5; i++)
                c[i] = (int)((com.maddox.gwindow.GWindowTable.Column)columns.get(i)).win.dx;

            int j = events.size();
            for(int k = 0; k < j; k++)
                computeHeight(k);

        }

        public float rowHeight(int i)
        {
            if(_events == null)
                return super.rowHeight(i);
            if(i >= events.size())
                return 0.0F;
            if(events.size() == 0)
                return 0.0F;
            boolean flag = false;
            for(int j = 0; j < 5; j++)
                if((float)c[j] != ((com.maddox.gwindow.GWindowTable.Column)columns.get(j)).win.dx)
                    flag = true;

            if(((Event)events.get(i)).h == 0)
                flag = true;
            if(flag)
                computeHeights();
            return (float)(((Event)events.get(i)).h + 2);
        }

        public float fullClientHeight()
        {
            if(_events == null)
                return super.fullClientHeight();
            int i = 0;
            int j = countRows();
            for(int k = 0; k < j; k++)
                i = (int)((float)i + rowHeight(k));

            return (float)i;
        }

        public int countRows()
        {
            if(_events == null)
                return 0;
            else
                return events.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            if(i > 0)
                GUISeparate.draw(this, myBrass, 0.0F, 0.0F, f, 1.0F);
            setCanvasColorBLACK();
            root.C.font = fnt;
            Event event = (Event)events.get(i);
            String s = null;
            int k = 0;
            float f2 = fnt.height - fnt.descender;
            switch(j)
            {
            default:
                break;

            case 0: // '\0'
                s = event.date;
                k = 0;
                break;

            case 1: // '\001'
                s = event.plane;
                k = 0;
                break;

            case 2: // '\002'
                s = event.mission;
                drawLines(0.0F, 2.0F, s, 0, s.length(), c[2], f2);
                return;

            case 3: // '\003'
                if(event.flightTime == null)
                    s = "";
                else
                    s = event.flightTime;
                k = 1;
                break;

            case 4: // '\004'
                if(event.actions.size() > 0)
                {
                    int l = 2;
                    for(int i1 = 0; i1 < event.actions.size(); i1++)
                    {
                        s = (String)event.actions.get(i1);
                        l += (int)((float)drawLines(0.0F, l, s, 0, s.length(), c[4], f2) * f2);
                    }

                }
                return;
            }
            setCanvasColorBLACK();
            draw(0.0F, 2.0F, f, (int)(fnt.height * 1.2F) - 2, k, s);
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

        public void afterCreated()
        {
            super.afterCreated();
//            fnt = GFont.New("courSmall");
            fnt = root.textFonts[0];
            bColumnsSizable = true;
            bSelecting = false;
            bSelectRow = false;
            addColumn(I18N.gui("dgendetail.Date"), null);
            addColumn(I18N.gui("dgendetail.Plane"), null);
            addColumn(I18N.gui("dgendetail.Mission"), null);
            addColumn(I18N.gui("dgendetail.FlightTime"), null);
            addColumn(I18N.gui("dgendetail.Notes"), null);
            vSB.scroll = rowHeight(0);
            getColumn(0).setRelativeDx(10F);
            getColumn(1).setRelativeDx(10F);
            getColumn(2).setRelativeDx(20F);
            getColumn(3).setRelativeDx(10F);
            getColumn(4).setRelativeDx(20F);
            alignColumns();
            bNotify = true;
            wClient.bNotify = true;
            resized();
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
        }

        private ArrayList _events;
        private int c[];
        private GFont fnt;
        private GColor myBrass;

        public Table(GWindow gwindow)
        {
            super(gwindow);
            _events = events;
            c = new int[5];
            myBrass = new GColor(99, 89, 74);
        }
    }

    private static class Event
    {

        public String date;
        public String plane;
        public String flightTime;
        public String mission;
        public ArrayList actions;
        public int h;

        private Event()
        {
            actions = new ArrayList();
        }

    }


    public void _enter()
    {
        loadEvents();
        client.activateWindow();
        wTable.resized();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    private void loadEvents()
    {
        try
        {
            roster = (GUIDGenRoster)GameState.get(65);
            Campaign campaign = Main.cur().campaign;
            String s = "missions/campaign/" + campaign.branch() + "/" + campaign.missionsDir() + "/logbook.dat";
            BufferedReader bufferedreader = new BufferedReader(new SFSReader(s, RTSConf.charEncoding));
            events.clear();
            for(Event event = null; (event = loadEvent(bufferedreader)) != null;)
                events.add(event);

            bufferedreader.close();
        }
        catch(Exception exception)
        {
            System.out.println("Squadron file load failed: " + exception.getMessage());
            exception.printStackTrace();
            Main.stateStack().pop();
        }
    }

    private Event loadEvent(BufferedReader bufferedreader)
        throws IOException
    {
        Event event = new Event();
        boolean flag;
label0:
        do
        {
            String s;
            int i;
            do
            {
                if(!bufferedreader.ready())
                    break label0;
                s = bufferedreader.readLine();
                if(s == null)
                    break label0;
                i = s.length();
            } while(i == 0);
            flag = false;
            if(s.startsWith("DATE:"))
                event.date = roster.readArgStr(UnicodeTo8bit.load(s, false));
            else
            if(s.startsWith("PLANE:"))
            {
                event.plane = roster.readArgStr(s);
                try
                {
                    Class class1 = ObjIO.classForName("air." + event.plane);
                    String s1 = Property.stringValue(class1, "keyName", null);
                    if(s1 != null)
                        event.plane = I18N.plane(s1);
                }
                catch(Throwable throwable) { }
            } else
            if(s.startsWith("MISSION:"))
                event.mission = roster.readArgStr(UnicodeTo8bit.load(s, false));
            else
            if(s.startsWith("EVENT:"))
                event.actions.add(roster.readArgStr(UnicodeTo8bit.load(s, false)));
            else
            if(s.startsWith("FLIGHT TIME:"))
            {
                event.flightTime = roster.readArgStr(s);
                flag = true;
            }
        } while(!flag);
        if(event.date == null || event.plane == null || event.mission == null)
            return null;
        else
            return event;
    }

    public GUIDGenPilotDetail(GWindowRoot gwindowroot)
    {
        super(67);
        events = new ArrayList();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("dgendetail.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wTable = new Table(dialogClient);
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public Table wTable;
    public GUIButton bBack;
    private ArrayList events;
    private GUIDGenRoster roster;
}
