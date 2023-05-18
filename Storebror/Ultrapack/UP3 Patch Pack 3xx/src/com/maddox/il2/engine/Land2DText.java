package com.maddox.il2.engine;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.maddox.rts.Destroy;
import com.maddox.rts.LDRres;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSReader;
import com.maddox.util.HashMapXY16List;
import com.maddox.util.NumberTokenizer;

public class Land2DText
    implements Destroy
{
    static class Item
    {

        void setLevels(int i)
        {
            commonFilds |= i & 7;
        }

        boolean isShowLevel(int i)
        {
            return (commonFilds & 1 << i) != 0;
        }

        void setFont(int i)
        {
            commonFilds |= (i & 3) << 3;
        }

        int font()
        {
            return commonFilds >> 3 & 3;
        }

        void setAlign(int i)
        {
            commonFilds |= (i & 3) << 5;
        }

        int align()
        {
            return commonFilds >> 5 & 3;
        }

        void setColor(int i)
        {
            commonFilds |= (i & 0x1f) << 7;
        }

        int color()
        {
            return commonFilds >> 7 & 0x1f;
        }

        void setW(int i)
        {
            commonFilds |= (i & 0x3ff) << 12;
        }

        int w()
        {
            return commonFilds >> 12 & 0x3ff;
        }

        void setH(int i)
        {
            commonFilds |= (i & 0x3ff) << 22;
        }

        int h()
        {
            return commonFilds >> 22 & 0x3ff;
        }

        public void computeSizes()
        {
            if(text == null || text.length() == 0)
                setW(0);
            else
                setW((int)TTFont.font[font()].width(text));
            setH(TTFont.font[font()].height());
        }

        public String text;
        public int commonFilds;
        public float x;
        public float y;

        public Item(float f, float f1, String s, int i, int j, int k, int l)
        {
            x = f;
            y = f1;
            text = s;
            setLevels(i);
            setFont(j);
            setAlign(k);
            setColor(l);
        }
    }


    public static int color(int i, int j, int k)
    {
        return i & 0xff | (j & 0xff) << 8 | (k & 0xff) << 16 | 0xff000000;
    }

    public boolean isShow()
    {
        return bShow;
    }

    public void show(boolean flag)
    {
        bShow = flag;
    }

    public void render()
    {
        if(!bShow || isDestroyed() || !(Render.currentCamera() instanceof CameraOrtho2D))
            return;
        CameraOrtho2D cameraortho2d = (CameraOrtho2D)Render.currentCamera();
        byte byte0 = 1;
        if(cameraortho2d.worldScale < 0.01D)
            byte0 = 0;
        else
        if(cameraortho2d.worldScale > 0.050000000000000003D)
            byte0 = 2;
        double d = cameraortho2d.worldXOffset;
        double d1 = cameraortho2d.worldYOffset;
        double d2 = d + (double)(cameraortho2d.right - cameraortho2d.left) / cameraortho2d.worldScale;
        double d3 = d1 + (double)(cameraortho2d.top - cameraortho2d.bottom) / cameraortho2d.worldScale;
        int i = (int)d / 10000;
        int j = ((int)d2 + 5000) / 10000;
        int k = (int)d1 / 10000;
        int l = ((int)d3 + 5000) / 10000;
        for(int i1 = k; i1 <= l; i1++)
        {
            for(int j1 = i; j1 <= j; j1++)
            {
                List list = lstXY.get(i1, j1);
                if(list == null)
                    continue;
                int k1 = list.size();
                for(int l1 = 0; l1 < k1; l1++)
                {
                    Item item = (Item)list.get(l1);
                    if(!item.isShowLevel(byte0))
                        continue;
                    float f = (float)(((double)item.x - d) * cameraortho2d.worldScale);
                    float f1 = (float)(((double)item.y - d1) * cameraortho2d.worldScale);
                    switch(item.align())
                    {
                    case 1: // '\001'
                        f -= item.w() / 2;
                        break;

                    case 2: // '\002'
                        f -= item.w();
                        break;
                    }
                    if(f <= cameraortho2d.right && f1 <= cameraortho2d.top && f + (float)item.w() >= cameraortho2d.left && f1 + (float)item.h() >= cameraortho2d.bottom)
                        TTFont.font[item.font()].output(color[item.color()], f, f1, 0.0F, item.text);
                }

            }

        }

    }

    public void load(String s)
    {
        if (this.lstXY != null) {
            this.lstXY.clear();
        }
        else {
            this.lstXY = new HashMapXY16List(7);
        }
        ResourceBundle bundle = null;
        try {
            final int lastIndex = s.lastIndexOf("/");
            if (lastIndex > 0) {
                final int lastIndex2 = s.lastIndexOf("/", lastIndex - 1);
                if (lastIndex2 > 0) {
                    bundle = ResourceBundle.getBundle("i18n/" + s.substring(lastIndex2 + 1, lastIndex), RTSConf.cur.locale, LDRres.loader());
                }
            }
        }
        catch (Exception ex2) {
            //ex2.printStackTrace(); // Don't report missing locales, it's normal that specific locales are missing.
        }
        String line = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new SFSReader(s));
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    bufferedReader.close();
                    this.lstXY.allValuesTrimToSize();
                    break;
                }
                // TODO: Patch Pack 107 avoid errors for empty texts.txt files or files with empty lines!
                if (line.trim().length() < 1) continue;
                // ---
                NumberTokenizer numberTokenizer = new NumberTokenizer(line);
                float n = numberTokenizer.next(0);
                float n2 = numberTokenizer.next(0);
                int next = numberTokenizer.next(7, 1, 7);
                int next2 = numberTokenizer.next(1, 0, 2);
                int next3 = numberTokenizer.next(1, 0, 2);
                int next4 = numberTokenizer.next(0, 0, 19);
                String s2 = numberTokenizer.nextToken("");
                int i;
                int n3;
                for (i = 0, n3 = s2.length() - 1; i < n3; ++i) {
                    if (s2.charAt(i) > ' ') {
                        break;
                    }
                }
                while (i < n3 && s2.charAt(n3) <= ' ') {
                    --n3;
                }
                if (i == n3) {
                    return;
                }
                if (i != 0 || n3 != s2.length() - 1) {
                    s2 = s2.substring(i, n3 + 1);
                }
                if (bundle != null) {
                    try {
                        s2 = bundle.getString(s2);
                    }
                    catch (Exception ex3) {}
                }
                Item item = new Item(n, n2, s2, next, next3, next2, next4);
                item.computeSizes();
                this.lstXY.put((int)n2 / 10000, (int)n / 10000, item);
            }
        }
        catch (Exception ex) {
            System.out.println("Land2DText load failed: " + ex.getMessage());
            ex.printStackTrace();
            System.out.println("Last line loaded was: " + line);
        }
    }

    public void contextResized()
    {
        if(isDestroyed())
            return;
        ArrayList arraylist = new ArrayList();
        lstXY.allValues(arraylist);
        for(int i = 0; i < arraylist.size(); i++)
        {
            ArrayList arraylist1 = (ArrayList)arraylist.get(i);
            for(int j = 0; j < arraylist1.size(); j++)
            {
                Item item = (Item)arraylist1.get(j);
                item.computeSizes();
            }

        }

        arraylist.clear();
    }

    public void clear()
    {
        if(isDestroyed())
        {
            return;
        } else
        {
            lstXY.clear();
            return;
        }
    }

    public void destroy()
    {
        if(isDestroyed())
        {
            return;
        } else
        {
            lstXY.clear();
            lstXY = null;
            return;
        }
    }

    public boolean isDestroyed()
    {
        return lstXY == null;
    }

    public Land2DText()
    {
        bShow = true;
    }

    public static final int STEP = 10000;
    public static final double LEVEL0_SCALE = 0.01D;
    public static final double LEVEL1_SCALE = 0.050000000000000003D;
    public static int color[];
    private boolean bShow;
    private HashMapXY16List lstXY;

    static 
    {
        color = new int[20];
        color[0] = color(0, 0, 0);
        color[1] = color(128, 0, 0);
        color[2] = color(0, 128, 0);
        color[3] = color(128, 128, 0);
        color[4] = color(0, 0, 128);
        color[5] = color(128, 0, 128);
        color[6] = color(0, 128, 128);
        color[7] = color(192, 192, 192);
        color[8] = color(192, 220, 192);
        color[9] = color(166, 202, 240);
        color[10] = color(255, 251, 240);
        color[11] = color(160, 160, 164);
        color[12] = color(128, 128, 128);
        color[13] = color(255, 0, 0);
        color[14] = color(0, 255, 0);
        color[15] = color(255, 255, 0);
        color[16] = color(0, 0, 255);
        color[17] = color(255, 0, 255);
        color[18] = color(0, 255, 255);
        color[19] = color(255, 255, 255);
    }
}
