package com.maddox.gwindow;

import com.maddox.rts.HomePath;
import com.maddox.rts.SectFile;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class GWindowFileBox extends GWindowFramed
{
    public class Client extends GWindowDialogClient
    {

        public GSize getMinSize(GSize gsize)
        {
            gsize.dx = M(minFullDX);
            gsize.dy = M(minFullDY);
            return gsize;
        }

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i == 2)
            {
                if(gwindow == wDir)
                {
                    scanFiles();
                    return true;
                }
                if(gwindow == wCFilter)
                {
                    scanFiles();
                    return true;
                }
                if(gwindow == wOk)
                {
                    if(wEdit != null)
                        doResult(wEdit.getValue());
                    else
                        doResult(null);
                    return true;
                }
                if(gwindow == wCancel)
                {
                    doResult(null);
                    return true;
                }
            }
            if(gwindow == wEdit && i == 10 && j == 10)
            {
                if(wEdit != null)
                    doResult(wEdit.getValue());
                else
                    doResult(null);
                return true;
            } else
            {
                return false;
            }
        }

        public Client()
        {
        }
    }

    public class Area extends GWindowDialogControl
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(gwindow == scroll && i == 2)
            {
                firstView = rows * (int)scroll.pos();
                return true;
            } else
            {
                return false;
            }
        }

        public void mouseDoubleClick(int i, float f, float f1)
        {
            if(i == 0)
            {
                int j = find(f, f1);
                if(j >= 0)
                {
                    selected = j;
                    File file = (File)files.get(selected);
                    wEdit.setValue(file.getName());
                    doResult(j);
                }
            }
        }

        public void mouseButton(int i, boolean flag, float f, float f1)
        {
            super.mouseButton(i, flag, f, f1);
            if(i == 0 && flag)
            {
                int j = find(f, f1);
                if(j >= 0)
                {
                    selected = j;
                    File file = (File)files.get(selected);
                    wEdit.setValue(file.getName());
                }
            }
        }

        public int find(float f, float f1)
        {
            GFont gfont = root.textFonts[0];
            f -= look.bevelDOWN.L.dx + (float)(spaceDX / 2);
            f1 -= look.bevelDOWN.T.dy + (float)(spaceDY / 2);
            int i = (int)(f / (maxDX + (float)spaceDX));
            int j = (int)(f1 / (gfont.height + (float)spaceDY));
            int k = j + i * rows + firstView;
            if(k >= files.size())
                return -1;
            else
                return k;
        }

        public void updateFiles(boolean flag)
        {
            if(flag)
            {
                firstView = 0;
                selected = -1;
            }
            GFont gfont = root.textFonts[0];
            maxDX = 0.0F;
            for(int i = 0; i < files.size(); i++)
            {
                File file = (File)files.get(i);
                GSize gsize = gfont.size(file.getName());
                if(gsize.dx > maxDX)
                    maxDX = gsize.dx;
            }

            float f = gfont.height + (float)spaceDY;
            GRegion gregion = getClientRegion();
            rows = (int)(gregion.dy / f);
            cols = files.size() / rows;
            if(files.size() % rows > 0)
                cols++;
            if((float)cols * maxDX + (float)((cols - 1) * spaceDX) + (float)(spaceDX / 2) > gregion.dx)
            {
                scroll.setSize(gregion.dx, look.getHScrollBarW());
                scroll.setPos(gregion.x, (gregion.y + gregion.dy) - look.getHScrollBarW());
                rows = (int)((gregion.dy - look.getHScrollBarW()) / f);
                cols = files.size() / rows;
                if(files.size() % rows > 0)
                    cols++;
                colsVisible = (int)(((gregion.dx - (float)(spaceDX / 2)) + (float)spaceDX) / (maxDX + (float)spaceDX));
                if(colsVisible == 0)
                    colsVisible = 1;
                firstView = (firstView / rows) * rows;
                if(cols - firstView / rows < colsVisible)
                    firstView = (cols - colsVisible) * rows;
                scroll.setRange(0.0F, cols, colsVisible, 1.0F, firstView / rows);
                scroll.showWindow();
            } else
            {
                colsVisible = cols;
                scroll.hideWindow();
                firstView = 0;
            }
        }

        public void render()
        {
            setCanvasColorWHITE();
            GWin95LookAndFeel gwin95lookandfeel = (GWin95LookAndFeel)lookAndFeel();
            draw(gwin95lookandfeel.bevelDOWN.L.dx, gwin95lookandfeel.bevelDOWN.T.dy, win.dx - gwin95lookandfeel.bevelDOWN.R.dx - gwin95lookandfeel.bevelDOWN.L.dx, win.dy - gwin95lookandfeel.bevelDOWN.B.dy - gwin95lookandfeel.bevelDOWN.T.dy, gwin95lookandfeel.elements, 5F, 17F, 1.0F, 1.0F);
            setCanvasColorBLACK();
            GFont gfont = root.textFonts[0];
            setCanvasFont(0);
            float f = gwin95lookandfeel.bevelDOWN.L.dx + (float)(spaceDX / 2);
            float f1 = gwin95lookandfeel.bevelDOWN.T.dy + (float)(spaceDY / 2);
            float f2 = gfont.height + (float)spaceDY;
            for(int i = 0; i < colsVisible + 1; i++)
            {
                for(int j = 0; j < rows; j++)
                {
                    int k = j + i * rows + firstView;
                    if(k >= files.size())
                        break;
                    float f3 = f + (float)i * (maxDX + (float)spaceDX);
                    float f4 = f1 + (float)j * f2;
                    File file = (File)files.get(k);
                    if(k == selected)
                    {
                        draw(f3 - (float)(spaceDX / 2), f4 - (float)(spaceDY / 2), maxDX + (float)spaceDX, f2, gwin95lookandfeel.elements, 5F, 17F, 1.0F, 1.0F);
                        setCanvasColorWHITE();
                        draw(f3, f4, file.getName());
                        setCanvasColorBLACK();
                    } else
                    {
                        draw(f3, f4, file.getName());
                    }
                }

            }

            setCanvasColorWHITE();
            gwin95lookandfeel.drawBevel(this, 0.0F, 0.0F, win.dx, win.dy, gwin95lookandfeel.bevelDOWN, gwin95lookandfeel.elements, false);
        }

        public void resized()
        {
            super.resized();
            updateFiles(false);
        }

        public void created()
        {
            super.created();
            look = (GWin95LookAndFeel)lookAndFeel();
            bEnableDoubleClick[0] = true;
        }

        public void afterCreated()
        {
            super.afterCreated();
            scroll = new GWindowHScrollBar(this);
            scroll.hideWindow();
        }

        public GRegion getClientRegion(GRegion gregion, float f)
        {
            gregion.x = look.bevelDOWN.L.dx + f;
            gregion.y = look.bevelDOWN.T.dy + f;
            gregion.dx = win.dx - gregion.x - look.bevelDOWN.R.dx - f;
            gregion.dy = win.dy - gregion.y - look.bevelDOWN.B.dy - f;
            return gregion;
        }

        public GWindowHScrollBar scroll;
        public GWin95LookAndFeel look;
        public int spaceDY;
        public int spaceDX;
        float maxDX;
        int cols;
        int rows;
        int firstView;
        int selected;
        int colsVisible;

        public Area(GWindow gwindow)
        {
            super(gwindow);
            spaceDY = 4;
            spaceDX = 6;
            selected = -1;
        }
    }

    public class Separate extends GWindow
    {

        public void mouseMove(float f, float f1)
        {
            super.mouseMove(f, f1);
            if(isMouseCaptured())
            {
                float f2 = root.mouseStep.dx;
                float f3 = m(f2);
                dirDX += f3;
                checkSizes();
                computeWin();
            }
        }

        public void mouseButton(int i, boolean flag, float f, float f1)
        {
            super.mouseButton(i, flag, f, f1);
            if(i == 0)
                if(flag)
                {
                    if(!isMouseCaptured())
                        mouseCapture(true);
                } else
                if(isMouseCaptured())
                    mouseCapture(false);
        }

        public Separate(GWindow gwindow)
        {
            super(gwindow);
            mouseCursor = 11;
        }
    }

    public class Label extends GWindow
    {

        public void render()
        {
            setCanvasColorWHITE();
            GWin95LookAndFeel gwin95lookandfeel = (GWin95LookAndFeel)lookAndFeel();
            gwin95lookandfeel.drawBevel(this, 0.0F, 0.0F, win.dx, win.dy, gwin95lookandfeel.bevelDOWN, gwin95lookandfeel.elements, true);
            if(title != null)
            {
                setCanvasColorBLACK();
                setCanvasFont(0);
                GFont gfont = root.textFonts[0];
                float f = (win.dy - gwin95lookandfeel.bevelDOWN.TL.dy - gwin95lookandfeel.bevelDOWN.BL.dy - gfont.height) / 2.0F;
                draw(gwin95lookandfeel.bevelDOWN.TL.dx + M(0.5F), gwin95lookandfeel.bevelDOWN.TL.dy + f, win.dx - M(0.5F) - gwin95lookandfeel.bevelDOWN.TL.dx - gwin95lookandfeel.bevelDOWN.TR.dx, gfont.height, 0, title);
            }
        }

        public String title;

        public Label(GWindow gwindow, String s)
        {
            super(gwindow);
            title = s;
        }
    }


    public void result(String s)
    {
        System.out.println("FileBox result: " + s);
    }

    public void setSelectFile(String s)
    {
        setSelectFile(s, bIncludeHomeNameToResult, false);
    }

    public void setSelectFile(String s, boolean flag)
    {
        setSelectFile(s, flag, false);
    }

    public void setSelectFile(String s, boolean flag, boolean flag1)
    {
        int i = s.lastIndexOf('/');
        if(i < 0)
            i = s.lastIndexOf('\\');
        String s1 = "";
        if(i >= 0)
        {
            if(i < s.length())
                s1 = s.substring(i + 1);
            s = s.substring(0, i);
        } else
        {
            s1 = s;
            s = null;
            wEdit.setValue(s1, false);
            if(flag1)
                doResult(s1);
            return;
        }
        GTreePath gtreepath = modelDir.strToPath(s, flag);
        if(gtreepath == null)
            return;
        wDir.setSelect(gtreepath);
        wEdit.setValue(s1, false);
        if(flag1)
            doResult(s1);
    }

    public String makeResultName(String s)
    {
        if(wDir.selectPath == null)
            return s;
        String s1 = modelDir.pathToStr(wDir.selectPath, bIncludeHomeNameToResult);
        if(s1 != null && s1.length() > 0)
            return s1 + "/" + s;
        else
            return s;
    }

    public void endExec()
    {
        if(exec == null)
        {
            close(false);
            return;
        }
        if(exec.isCloseBox())
        {
            iResult = -2;
            close(false);
            if(exec.isReturnResult())
                result(resultFileName);
        } else
        {
            iResult = -2;
            if(exec.isChangedBox())
                scanFiles();
            if(bModal)
                showModal();
        }
    }

    public void doResult(String s)
    {
        if(s != null)
            resultFileName = makeResultName(s);
        else
            resultFileName = null;
        if(exec != null)
        {
            exec.exec(this, resultFileName);
            return;
        } else
        {
            iResult = -2;
            close(false);
            result(resultFileName);
            return;
        }
    }

    public void doResult(int i)
    {
        if(i >= 0)
        {
            File file = (File)files.get(i);
            resultFileName = makeResultName(file.getName());
        } else
        {
            resultFileName = null;
        }
        if(exec != null)
        {
            exec.exec(this, resultFileName);
            return;
        } else
        {
            iResult = -2;
            close(false);
            result(resultFileName);
            return;
        }
    }

    public void close(boolean flag)
    {
        super.close(flag);
        if(iResult != -2)
        {
            iResult = -2;
            result(null);
        }
    }

    protected void scanFiles()
    {
        files.clear();
        if(wDir == null || wDir.selectPath == null)
        {
            wArea.updateFiles(true);
            return;
        }
        File file = (File)wDir.selectPath.getLastPathComponent();
        File afile[] = file.listFiles();
        if(afile == null || afile.length == 0)
        {
            wArea.updateFiles(true);
            return;
        }
        for(int i = 0; i < afile.length; i++)
        {
            if(filter != null && wCFilter != null)
            {
                GFileFilter gfilefilter = filter[wCFilter.getSelected()];
                if(afile[i].isFile() && !afile[i].isHidden() && gfilefilter.accept(afile[i]))
                    _scanMap.put(afile[i].getName(), afile[i]);
                continue;
            }
            if(afile[i].isFile() && !afile[i].isHidden())
                _scanMap.put(afile[i].getName(), afile[i]);
        }

        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); files.add(_scanMap.get(iterator.next())));
        _scanMap.clear();
        wArea.updateFiles(true);
    }

    private boolean checkSizes()
    {
        boolean flag = false;
        if(dirDX < dirDXMin)
        {
            dirDX = dirDXMin;
            flag = true;
        }
        float f = m(win.dx) - spaceLeft - dirDX - separateDX - spaceRight;
        if(f < areaDXMin)
        {
            dirDX -= areaDXMin - f;
            flag = true;
        }
        return flag;
    }

    public float M(float f)
    {
        return lookAndFeel().metric(f);
    }

    public float m(float f)
    {
        return f / lookAndFeel().metric();
    }

    public void computeFolders(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = M(dirDX);
        gregion.dy = M(titleDY);
        gregion.x = M(spaceLeft);
        gregion.y = M(titleSpaceUP);
    }

    public void computeContents(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = gregion1.dx - M(spaceLeft) - M(spaceRight) - M(dirDX) - M(separateDX);
        gregion.dy = M(titleDY);
        gregion.x = M(spaceLeft) + M(dirDX) + M(separateDX);
        gregion.y = M(titleSpaceUP);
    }

    public void computeDir(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = M(dirDX);
        gregion.dy = gregion1.dy - M(titleSpaceUP) - M(titleDY) - M(areaSpaceDOWN);
        gregion.x = M(spaceLeft);
        gregion.y = M(titleSpaceUP) + M(titleDY);
    }

    public void computeSeparate(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = M(separateDX);
        gregion.dy = gregion1.dy - M(titleSpaceUP) - M(areaSpaceDOWN);
        gregion.x = M(spaceLeft) + M(dirDX);
        gregion.y = M(titleSpaceUP);
    }

    public void computeArea(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = gregion1.dx - M(spaceLeft) - M(spaceRight) - M(dirDX) - M(separateDX);
        gregion.dy = gregion1.dy - M(titleSpaceUP) - M(titleDY) - M(areaSpaceDOWN);
        gregion.x = M(spaceLeft) + M(dirDX) + M(separateDX);
        gregion.y = M(titleSpaceUP) + M(titleDY);
    }

    public void computeFile(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = M(textDX);
        gregion.dy = lookAndFeel().getComboH();
        gregion.x = M(spaceLeft);
        gregion.y = gregion1.dy - M(fileSpaceDOWN);
    }

    public void computeFilter(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = M(textDX);
        gregion.dy = lookAndFeel().getComboH();
        gregion.x = M(spaceLeft);
        gregion.y = gregion1.dy - M(typeSpaceDOWN);
    }

    public void computeEdit(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = gregion1.dx - M(spaceLeft) - M(textDX) - 2.0F * M(spaceDX) - M(buttonDX) - M(spaceRight);
        gregion.dy = lookAndFeel().getComboH();
        gregion.x = M(spaceLeft) + M(textDX) + M(spaceDX);
        gregion.y = (gregion1.dy - M(fileSpaceDOWN)) + M(0.1F);
    }

    public void computeCFilter(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = gregion1.dx - M(spaceLeft) - M(textDX) - 2.0F * M(spaceDX) - M(buttonDX) - M(spaceRight);
        gregion.dy = lookAndFeel().getComboH();
        gregion.x = M(spaceLeft) + M(textDX) + M(spaceDX);
        gregion.y = (gregion1.dy - M(typeSpaceDOWN)) + M(0.1F);
    }

    public void computeOk(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = M(buttonDX);
        gregion.dy = M(buttonDY);
        gregion.x = gregion1.dx - gregion.dx - M(spaceRight);
        gregion.y = gregion1.dy - M(fileSpaceDOWN);
    }

    public void computeCancel(GRegion gregion, GRegion gregion1)
    {
        gregion.dx = M(buttonDX);
        gregion.dy = M(buttonDY);
        gregion.x = gregion1.dx - gregion.dx - M(spaceRight);
        gregion.y = gregion1.dy - M(typeSpaceDOWN);
    }

    public void _setWin(GWindow gwindow, GRegion gregion)
    {
        gwindow.setPos(gregion.x, gregion.y);
        gwindow.setSize(gregion.dx, gregion.dy);
    }

    public void computeWin()
    {
        GRegion gregion = getClientRegion(_client, 0.0F);
        if(wFolders != null)
        {
            computeFolders(_reg, gregion);
            _setWin(wFolders, _reg);
        }
        if(wContents != null)
        {
            computeContents(_reg, gregion);
            _setWin(wContents, _reg);
        }
        if(wDir != null)
        {
            computeDir(_reg, gregion);
            _setWin(wDir, _reg);
        }
        if(wSeparate != null)
        {
            computeSeparate(_reg, gregion);
            _setWin(wSeparate, _reg);
        }
        if(wArea != null)
        {
            computeArea(_reg, gregion);
            _setWin(wArea, _reg);
        }
        if(wFile != null)
        {
            computeFile(_reg, gregion);
            _setWin(wFile, _reg);
        }
        if(wFilter != null)
        {
            computeFilter(_reg, gregion);
            _setWin(wFilter, _reg);
        }
        if(wEdit != null)
        {
            computeEdit(_reg, gregion);
            _setWin(wEdit, _reg);
        }
        if(wCFilter != null)
        {
            computeCFilter(_reg, gregion);
            _setWin(wCFilter, _reg);
        }
        if(wOk != null)
        {
            computeOk(_reg, gregion);
            _setWin(wOk, _reg);
        }
        if(wCancel != null)
        {
            computeCancel(_reg, gregion);
            _setWin(wCancel, _reg);
        }
    }

    public void resized()
    {
        checkSizes();
        computeWin();
        super.resized();
    }

    public void createFolders(GWindowDialogClient gwindowdialogclient)
    {
        computeFolders(_reg, getClientRegion());
        wFolders = new Label(gwindowdialogclient, lAF().i18n("Folders"));
        _setWin(wFolders, _reg);
    }

    public void createContents(GWindowDialogClient gwindowdialogclient)
    {
        computeContents(_reg, getClientRegion());
        wContents = new Label(gwindowdialogclient, lAF().i18n("Contents"));
        _setWin(wContents, _reg);
    }

    public void createDir(GWindowDialogClient gwindowdialogclient)
    {
        computeDir(_reg, getClientRegion());
        wDir = new GWindowTree(gwindowdialogclient, m(_reg.x), m(_reg.y), m(_reg.dx), m(_reg.dy));
        wDir.setModel(modelDir);
        wDir.metricWin = null;
        wDir.bDrawIcons = true;
        wDir.bNotify = true;
        wDir.setRootVisible(true);
        wDir.setSelect(modelDir.root);
    }

    public void createSeparate(GWindowDialogClient gwindowdialogclient)
    {
        computeSeparate(_reg, getClientRegion());
        wSeparate = new Separate(gwindowdialogclient);
        _setWin(wSeparate, _reg);
    }

    public void createArea(GWindowDialogClient gwindowdialogclient)
    {
        computeArea(_reg, getClientRegion());
        wArea = new Area(gwindowdialogclient);
        _setWin(wArea, _reg);
    }

    public void createFile(GWindowDialogClient gwindowdialogclient)
    {
        computeFile(_reg, getClientRegion());
        wFile = (GWindowLabel)gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, m(_reg.x), m(_reg.y), m(_reg.dx), m(_reg.dy), lAF().i18n("File_name_"), null));
        wFile.metricWin = null;
    }

    public void createFilter(GWindowDialogClient gwindowdialogclient)
    {
        computeFilter(_reg, getClientRegion());
        wFilter = (GWindowLabel)gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, m(_reg.x), m(_reg.y), m(_reg.dx), m(_reg.dy), lAF().i18n("Files_of_type_"), null));
        wFilter.metricWin = null;
    }

    public void createEdit(GWindowDialogClient gwindowdialogclient)
    {
        computeEdit(_reg, getClientRegion());
        wEdit = (GWindowEditControl)gwindowdialogclient.addControl(new GWindowEditControl(gwindowdialogclient, m(_reg.x), m(_reg.y), m(_reg.dx), m(_reg.dy), null));
        wEdit.metricWin = null;
        wEdit.bSelectOnFocus = false;
    }

    public void createCFilter(GWindowDialogClient gwindowdialogclient)
    {
        computeCFilter(_reg, getClientRegion());
        wCFilter = (GWindowComboControl)gwindowdialogclient.addControl(new GWindowComboControl(gwindowdialogclient, m(_reg.x), m(_reg.y), m(_reg.dx)));
        wCFilter.metricWin = null;
        wCFilter.setEditable(false);
        if(filter != null)
        {
            for(int i = 0; i < filter.length; i++)
                wCFilter.add(filter[i].getDescription());

            wCFilter.setSelected(0, true, false);
        }
    }

    public void createOk(GWindowDialogClient gwindowdialogclient)
    {
        computeOk(_reg, getClientRegion());
        wOk = (GWindowButton)gwindowdialogclient.addDefault(new GWindowButton(gwindowdialogclient, m(_reg.x), m(_reg.y), m(_reg.dx), m(_reg.dy), lAF().i18n("&Open"), null));
        wOk.metricWin = null;
    }

    public void createCancel(GWindowDialogClient gwindowdialogclient)
    {
        computeCancel(_reg, getClientRegion());
        wCancel = (GWindowButton)gwindowdialogclient.addControl(new GWindowButton(gwindowdialogclient, m(_reg.x), m(_reg.y), m(_reg.dx), m(_reg.dy), lAF().i18n("&Cancel"), null));
        wCancel.metricWin = null;
    }

    public void created()
    {
        super.created();
        setMetricSize(defFullDX, defFullDY);
    }

    public void afterCreated()
    {
        GRegion gregion = getClientRegion();
        clientWindow = create(gregion.x, gregion.y, gregion.dx, gregion.dy, false, new Client());
        Client client = (Client)clientWindow;
        createFolders(client);
        createContents(client);
        createCFilter(client);
        createArea(client);
        createSeparate(client);
        createDir(client);
        createFile(client);
        createFilter(client);
        createEdit(client);
        createOk(client);
        createCancel(client);
        super.afterCreated();
        if(bModal)
            showModal();
        if(root == parentWindow)
            clampWin(root.getClientRegion());
    }

    public GWindowFileBox(GWindow gwindow, boolean flag, String s, String s1, GFileFilter agfilefilter[])
    {
        bIncludeHomeNameToResult = false;
        iResult = -1;
        files = new ArrayList();
        _scanMap = new TreeMap();
        minFullDX = 24F;
        minFullDY = 16F;
        defFullDX = 60F;
        defFullDY = 45F;
        spaceLeft = 0.2F;
        spaceRight = 0.2F;
        titleSpaceUP = 0.2F;
        titleDY = 1.6F;
        dirDX = 25F;
        separateDX = 0.2F;
        dirDXMin = dirDX;
        areaDXMin = 4F;
        areaSpaceDOWN = 4.6F;
        fileSpaceDOWN = 4F;
        typeSpaceDOWN = 2.0F;
        buttonDX = 6F;
        buttonDY = 1.8F;
        spaceDX = 1.0F;
        textDX = 8F;
        title = s;
        filter = agfilefilter;
        bModal = flag;
        modelDir = new GTreeModelDir95(HomePath.get(0) + "/" + s1);
        iResult = -1;
        SectFile sectfile = new SectFile("bldconf.ini");
        String s2 = sectfile.get("builder config", "defFullDX");
        if(s2 != null)
            defFullDX = Float.parseFloat(s2) > defFullDX ? Float.parseFloat(s2) : defFullDX;
        s2 = sectfile.get("builder config", "defFullDY");
        if(s2 != null)
            defFullDY = Float.parseFloat(s2) > defFullDY ? Float.parseFloat(s2) : defFullDY;
        doNew(gwindow, 0.0F, 0.0F, 100F, 100F, false);
        dirDXMin = 4F;
    }

    public boolean bModal;
    public boolean bIncludeHomeNameToResult;
    public GFileFilter filter[];
    public GWindowFileBoxExec exec;
    public int iResult;
    public String resultFileName;
    public GTreeModelDir modelDir;
    public ArrayList files;
    protected TreeMap _scanMap;
    public float minFullDX;
    public float minFullDY;
    public float defFullDX;
    public float defFullDY;
    public float spaceLeft;
    public float spaceRight;
    public float titleSpaceUP;
    public float titleDY;
    public float dirDX;
    public float separateDX;
    public float dirDXMin;
    public float areaDXMin;
    public float areaSpaceDOWN;
    public float fileSpaceDOWN;
    public float typeSpaceDOWN;
    public float buttonDX;
    public float buttonDY;
    public float spaceDX;
    public float textDX;
    public Label wFolders;
    public Label wContents;
    public GWindowTree wDir;
    public Separate wSeparate;
    public Area wArea;
    public GWindowLabel wFile;
    public GWindowLabel wFilter;
    public GWindowEditControl wEdit;
    public GWindowComboControl wCFilter;
    public GWindowButton wOk;
    public GWindowButton wCancel;
    private static GRegion _reg = new GRegion();
    private static GRegion _client = new GRegion();


}
