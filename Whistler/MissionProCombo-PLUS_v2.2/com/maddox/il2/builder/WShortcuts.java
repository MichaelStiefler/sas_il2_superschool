package com.maddox.il2.builder;

import com.maddox.gwindow.*;
import com.maddox.rts.*;
import com.maddox.util.*;
import java.util.*;

public class WShortcuts extends GWindowFramed
{

    public void windowShown()
    {
        builder.mShortcuts.bChecked = true;
        super.windowShown();
    }

    public void windowHidden()
    {
        builder.mShortcuts.bChecked = false;
        super.windowHidden();
    }

    public void created()
    {
        bAlwaysOnTop = true;
        super.created();
        title = "FMB Controls";
//        float f = 13F;
        clientWindow = create(new GWindowDialogClient());
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;

//        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, f - 1.0F, 1.3F, "Insert", null));

//        boxA = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 15F, 20F, 25F);
        gwindowdialogclient.addLabel(sLabel0 = new GWindowLabel(gwindowdialogclient,  1.0F,  1.0F, 10F, 1.3F, "Land Toggle", null)); sLabel0.align = 0;
        gwindowdialogclient.addLabel(sLabel1 = new GWindowLabel(gwindowdialogclient,  1.0F,  2.5F, 10F, 1.3F, "Destruction Fill", null)); sLabel1.align = 0;
        gwindowdialogclient.addLabel(sLabel2 = new GWindowLabel(gwindowdialogclient,  1.0F,  4.0F, 10F, 1.3F, "Zoom Area", null)); sLabel2.align = 0;
        gwindowdialogclient.addLabel(sLabel3 = new GWindowLabel(gwindowdialogclient,  1.0F,  5.5F, 10F, 1.3F, "Group Select", null)); sLabel3.align = 0;
        gwindowdialogclient.addLabel(sLabel4 = new GWindowLabel(gwindowdialogclient,  1.0F,  7.0F, 10F, 1.3F, "Mouse Menu", null)); sLabel4.align = 0;

        gwindowdialogclient.addLabel(sLabel5 = new GWindowLabel(gwindowdialogclient,  1.0F,  9.5F, 10F, 1.3F, "Insert Object", null)); sLabel5.align = 0;
        gwindowdialogclient.addLabel(sLabel6 = new GWindowLabel(gwindowdialogclient,  1.0F, 11.0F, 10F, 1.3F, "Delete Object", null)); sLabel6.align = 0;
        gwindowdialogclient.addLabel(sLabel7 = new GWindowLabel(gwindowdialogclient,  1.0F, 12.5F, 10F, 1.3F, "Next Object", null)); sLabel7.align = 0;
        gwindowdialogclient.addLabel(sLabel8 = new GWindowLabel(gwindowdialogclient,  1.0F, 14.0F, 10F, 1.3F, "Prev Object", null)); sLabel8.align = 0;

        gwindowdialogclient.addLabel(sLabel9 = new GWindowLabel(gwindowdialogclient,  1.0F, 16.5F, 10F, 1.3F, "Rotate -/+ 5\260", null)); sLabel9.align = 0;
        gwindowdialogclient.addLabel(sLabel10 = new GWindowLabel(gwindowdialogclient, 1.0F, 18.0F, 10F, 1.3F, "Rotate -/+ 15\260", null)); sLabel10.align = 0;
        gwindowdialogclient.addLabel(sLabel11 = new GWindowLabel(gwindowdialogclient, 1.0F, 19.5F, 10F, 1.3F, "Rotate -/+ 30\260", null)); sLabel11.align = 0;
        gwindowdialogclient.addLabel(sLabel12 = new GWindowLabel(gwindowdialogclient, 1.0F, 21.0F, 10F, 1.3F, "Reset Yaw", null)); sLabel12.align = 0;
        gwindowdialogclient.addLabel(sLabel13 = new GWindowLabel(gwindowdialogclient, 1.0F, 22.5F, 10F, 1.3F, "Reset Pitch", null)); sLabel13.align = 0;

        gwindowdialogclient.addLabel(sLabel14 = new GWindowLabel(gwindowdialogclient, 1.0F, 25.0F, 10F, 1.3F, "2D/3D", null)); sLabel14.align = 0;
        gwindowdialogclient.addLabel(sLabel15 = new GWindowLabel(gwindowdialogclient, 1.0F, 26.5F, 10F, 1.3F, "Cursor/Object", null)); sLabel15.align = 0;
        gwindowdialogclient.addLabel(sLabel16 = new GWindowLabel(gwindowdialogclient, 1.0F, 28.0F, 10F, 1.3F, "3D Zoom", null)); sLabel16.align = 0;
        gwindowdialogclient.addLabel(sLabel17 = new GWindowLabel(gwindowdialogclient, 1.0F, 29.5F, 10F, 1.3F, "3D Move", null)); sLabel17.align = 0;
        gwindowdialogclient.addLabel(sLabel18 = new GWindowLabel(gwindowdialogclient, 1.0F, 31.0F, 10F, 1.3F, "Move Slow", null)); sLabel18.align = 0;
        gwindowdialogclient.addLabel(sLabel19 = new GWindowLabel(gwindowdialogclient, 1.0F, 32.5F, 10F, 1.3F, "Move Normal", null)); sLabel19.align = 0;
        gwindowdialogclient.addLabel(sLabel20 = new GWindowLabel(gwindowdialogclient, 1.0F, 34.0F, 10F, 1.3F, "Move Fast", null)); sLabel20.align = 0;
        gwindowdialogclient.addLabel(sLabel21 = new GWindowLabel(gwindowdialogclient, 1.0F, 35.5F, 10F, 1.3F, "Raise/Lower", null)); sLabel21.align = 0;
        gwindowdialogclient.addLabel(sLabel22 = new GWindowLabel(gwindowdialogclient, 1.0F, 37.0F, 10F, 1.3F, "Change Yaw", null)); sLabel22.align = 0;
        gwindowdialogclient.addLabel(sLabel23 = new GWindowLabel(gwindowdialogclient, 1.0F, 38.5F, 10F, 1.3F, "Change Roll", null)); sLabel23.align = 0;
        gwindowdialogclient.addLabel(sLabel24 = new GWindowLabel(gwindowdialogclient, 1.0F, 40.0F, 10F, 1.3F, "Change Pitch", null)); sLabel24.align = 0;

        gwindowdialogclient.addLabel(sLabel100 = new GWindowLabel(gwindowdialogclient, 10F,  1.0F, 10F, 1.3F, "F10", null)); sLabel100.align = 0;
        gwindowdialogclient.addLabel(sLabel101 = new GWindowLabel(gwindowdialogclient, 10F,  2.5F, 10F, 1.3F, "F+Mouse", null)); sLabel101.align = 0;
        gwindowdialogclient.addLabel(sLabel102 = new GWindowLabel(gwindowdialogclient, 10F,  4.0F, 10F, 1.3F, "Shift+LeftClick", null)); sLabel102.align = 0;
        gwindowdialogclient.addLabel(sLabel103 = new GWindowLabel(gwindowdialogclient, 10F,  5.5F, 10F, 1.3F, "Alt+LeftClick", null)); sLabel103.align = 0;
        gwindowdialogclient.addLabel(sLabel104 = new GWindowLabel(gwindowdialogclient, 10F,  7.0F, 10F, 1.3F, "RightClick", null)); sLabel104.align = 0;

        gwindowdialogclient.addLabel(sLabel105 = new GWindowLabel(gwindowdialogclient, 10F,  9.5F, 10F, 1.3F, "Insert", null)); sLabel105.align = 0;
        gwindowdialogclient.addLabel(sLabel106 = new GWindowLabel(gwindowdialogclient, 10F, 11.0F, 10F, 1.3F, "Delete", null)); sLabel106.align = 0;
        gwindowdialogclient.addLabel(sLabel107 = new GWindowLabel(gwindowdialogclient, 10F, 12.5F, 10F, 1.3F, "PageUp", null)); sLabel107.align = 0;
        gwindowdialogclient.addLabel(sLabel108 = new GWindowLabel(gwindowdialogclient, 10F, 14.0F, 10F, 1.3F, "PageDown", null)); sLabel108.align = 0;

        gwindowdialogclient.addLabel(sLabel109 = new GWindowLabel(gwindowdialogclient, 10F, 16.5F, 10F, 1.3F, "NumPad7/9", null)); sLabel109.align = 0;
        gwindowdialogclient.addLabel(sLabel110 = new GWindowLabel(gwindowdialogclient, 10F, 18.0F, 10F, 1.3F, "NumPad4/6", null)); sLabel110.align = 0;
        gwindowdialogclient.addLabel(sLabel111 = new GWindowLabel(gwindowdialogclient, 10F, 19.5F, 10F, 1.3F, "NumPad1/3", null)); sLabel111.align = 0;
        gwindowdialogclient.addLabel(sLabel112 = new GWindowLabel(gwindowdialogclient, 10F, 21.0F, 10F, 1.3F, "NumPad5", null)); sLabel112.align = 0;
        gwindowdialogclient.addLabel(sLabel113 = new GWindowLabel(gwindowdialogclient, 10F, 22.5F, 10F, 1.3F, "NumPad8", null)); sLabel113.align = 0;

        gwindowdialogclient.addLabel(sLabel114 = new GWindowLabel(gwindowdialogclient, 10F, 25.0F, 10F, 1.3F, "Enter", null)); sLabel114.align = 0;
        gwindowdialogclient.addLabel(sLabel115 = new GWindowLabel(gwindowdialogclient, 10F, 26.5F, 10F, 1.3F, "Tab/Backspace", null)); sLabel115.align = 0;
        gwindowdialogclient.addLabel(sLabel116 = new GWindowLabel(gwindowdialogclient, 10F, 28.0F, 10F, 1.3F, "LeftClick", null)); sLabel116.align = 0;
        gwindowdialogclient.addLabel(sLabel117 = new GWindowLabel(gwindowdialogclient, 10F, 29.5F, 10F, 1.3F, "RightClick", null)); sLabel117.align = 0;
        gwindowdialogclient.addLabel(sLabel118 = new GWindowLabel(gwindowdialogclient, 10F, 31.0F, 10F, 1.3F, "F1+RightClick", null)); sLabel118.align = 0;
        gwindowdialogclient.addLabel(sLabel119 = new GWindowLabel(gwindowdialogclient, 10F, 32.5F, 10F, 1.3F, "F2+RightClick", null)); sLabel119.align = 0;
        gwindowdialogclient.addLabel(sLabel120 = new GWindowLabel(gwindowdialogclient, 10F, 34.0F, 10F, 1.3F, "F3+RightClick", null)); sLabel120.align = 0;
        gwindowdialogclient.addLabel(sLabel121 = new GWindowLabel(gwindowdialogclient, 10F, 35.5F, 10F, 1.3F, "F4+RightClick", null)); sLabel121.align = 0;
        gwindowdialogclient.addLabel(sLabel122 = new GWindowLabel(gwindowdialogclient, 10F, 37.0F, 10F, 1.3F, "F5+RightClick", null)); sLabel122.align = 0;
        gwindowdialogclient.addLabel(sLabel123 = new GWindowLabel(gwindowdialogclient, 10F, 38.5F, 10F, 1.3F, "F6+RightClick", null)); sLabel123.align = 0;
        gwindowdialogclient.addLabel(sLabel124 = new GWindowLabel(gwindowdialogclient, 10F, 40.0F, 10F, 1.3F, "F7+RightClick", null)); sLabel124.align = 0;
    }

    public void afterCreated()
    {
        super.afterCreated();
        resized();
        close(false);
    }

    public WShortcuts(Builder builder1, GWindow gwindow)
    {
        builder = builder1;
//        doNew(gwindow, 2.0F, 2.0F, 17F, 7F, true);
        doNew(gwindow, 50.0F, 2.0F, 19F, 44F, true);
    }

/*
    public void _enter()
    {
        fillItems();
    }
*/

/*
    private void fillItems()
    {
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        HotKeyEnv hotkeyenv = HotKeyEnv.env("builder");
        HashMapInt hashmapint = hotkeyenv.all();
        for(HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint.nextEntry(hashmapintentry))
        {
//            int k = hashmapintentry1.getKey();
//            String s1 = (String)(String)hashmapintentry1.getValue();
//            if("timeSpeedPause".equals(s1))
//                arraylist.add(new Integer(k));

            int key = hashmapintentry.getKey();
            arraylist1.add(new Integer(key));

            String control = (String)hashmapintentry.getValue();
            arraylist2.add(new String(control));
        }
    }
*/

    Builder builder;
//    GWindowBoxSeparate boxA;
    GWindowLabel sLabel0;
    GWindowLabel sLabel1;
    GWindowLabel sLabel2;
    GWindowLabel sLabel3;
    GWindowLabel sLabel4;
    GWindowLabel sLabel5;
    GWindowLabel sLabel6;
    GWindowLabel sLabel7;
    GWindowLabel sLabel8;
    GWindowLabel sLabel9;
    GWindowLabel sLabel10;
    GWindowLabel sLabel11;
    GWindowLabel sLabel12;
    GWindowLabel sLabel13;
    GWindowLabel sLabel14;
    GWindowLabel sLabel15;
    GWindowLabel sLabel16;
    GWindowLabel sLabel17;
    GWindowLabel sLabel18;
    GWindowLabel sLabel19;
    GWindowLabel sLabel20;
    GWindowLabel sLabel21;
    GWindowLabel sLabel22;
    GWindowLabel sLabel23;
    GWindowLabel sLabel24;
    GWindowLabel sLabel100;
    GWindowLabel sLabel101;
    GWindowLabel sLabel102;
    GWindowLabel sLabel103;
    GWindowLabel sLabel104;
    GWindowLabel sLabel105;
    GWindowLabel sLabel106;
    GWindowLabel sLabel107;
    GWindowLabel sLabel108;
    GWindowLabel sLabel109;
    GWindowLabel sLabel110;
    GWindowLabel sLabel111;
    GWindowLabel sLabel112;
    GWindowLabel sLabel113;
    GWindowLabel sLabel114;
    GWindowLabel sLabel115;
    GWindowLabel sLabel116;
    GWindowLabel sLabel117;
    GWindowLabel sLabel118;
    GWindowLabel sLabel119;
    GWindowLabel sLabel120;
    GWindowLabel sLabel121;
    GWindowLabel sLabel122;
    GWindowLabel sLabel123;
    GWindowLabel sLabel124;
}
