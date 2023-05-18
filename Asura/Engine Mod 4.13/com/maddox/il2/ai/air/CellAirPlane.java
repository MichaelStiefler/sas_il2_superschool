// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CellAirPlane.java

package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import java.io.Serializable;

// Referenced classes of package com.maddox.il2.ai.air:
//            CellObject, CellAirField

public class CellAirPlane extends CellObject
    implements Serializable
{

    public boolean checkAirFieldSize(CellAirField cellairfield)
    {
        if(cellairfield.getWidth() < getWidth())
            return false;
        return cellairfield.getHeight() >= getHeight();
    }

    public boolean checkAirFieldCapacity(CellAirField cellairfield)
    {
        return cellairfield.calcFreeCells() < calcFilledCells() ? true : true;
    }

    public void blurSiluetUp()
    {
        if(getCells() == null)
            return;
        for(int i = 0; i < getHeight(); i++)
        {
            for(int j = 0; j < getWidth(); j++)
            {
                if(getCells()[j][i] != this)
                    continue;
                for(int k = i; k >= 0; k--)
                    getCells()[j][k] = this;

            }

        }

    }

    public void blurSiluetDown()
    {
        if(getCells() == null)
            return;
        for(int i = 0; i < getHeight(); i++)
        {
            for(int j = 0; j < getWidth(); j++)
            {
                if(getCells()[j][i] != this)
                    continue;
                for(int k = i; k < getHeight(); k++)
                    getCells()[j][k] = this;

            }

        }

    }

    public void blurSiluet4x()
    {
        if(getCells() == null)
            return;
        CellObject cellobject = getClone();
        cellobject.clearCells();
        for(int i = 0; i < getHeight(); i++)
        {
            for(int j = 0; j < getWidth(); j++)
            {
                if(getCells()[j][i] != this)
                    continue;
                cellobject.getCells()[j][i] = cellobject;
                if(j > 0)
                    cellobject.getCells()[j - 1][i] = cellobject;
                if(j < getWidth() - 1)
                    cellobject.getCells()[j + 1][i] = cellobject;
                if(i > 0)
                    cellobject.getCells()[j][i - 1] = cellobject;
                if(i < getHeight() - 1)
                    cellobject.getCells()[j][i + 1] = cellobject;
            }

        }

        clearCells();
        setCells(cellobject.getCells());
        reInitReferences();
    }

    public void blurSiluet8x()
    {
        if(getCells() == null)
            return;
        CellObject cellobject = getClone();
        for(int i = 0; i < getHeight(); i++)
        {
            for(int j = 0; j < getWidth(); j++)
            {
                if(cellobject.getCells()[j][i] == null)
                    continue;
                if(j > 0)
                    getCells()[j - 1][i] = this;
                if(j < getWidth() - 1)
                    getCells()[j + 1][i] = this;
                if(i > 0)
                    getCells()[j][i - 1] = this;
                if(i < getHeight() - 1)
                    getCells()[j][i + 1] = this;
                if(j > 0 && i > 0)
                    getCells()[j - 1][i - 1] = this;
                if(j < getWidth() - 1 && i > 0)
                    getCells()[j + 1][i - 1] = this;
                if(j > 0 && i < getHeight() - 1)
                    getCells()[j - 1][i + 1] = this;
                if(j < getWidth() - 1 && i < getHeight() - 1)
                    getCells()[j + 1][i + 1] = this;
            }

        }

    }

    public void clampCells()
    {
        CellObject acellobject[][] = getCells();
        if(acellobject == null)
            return;
        int i = getWidth();
        int j = getHeight();
        boolean flag = false;
        int k;
label0:
        for(k = 0; k < i && !flag; k++)
        {
            int l = 0;
            do
            {
                if(l >= j)
                    continue label0;
                if(acellobject[l][k] != null)
                {
                    flag = true;
                    continue label0;
                }
                l++;
            } while(true);
        }

        flag = false;
        int i1;
label1:
        for(i1 = i - 1; i1 > 0 && !flag; i1--)
        {
            int j1 = 0;
            do
            {
                if(j1 >= j)
                    continue label1;
                if(acellobject[j1][i1] != null)
                {
                    flag = true;
                    continue label1;
                }
                j1++;
            } while(true);
        }

        flag = false;
        int k1;
label2:
        for(k1 = 0; k1 < j && !flag; k1++)
        {
            int l1 = 0;
            do
            {
                if(l1 >= i)
                    continue label2;
                if(acellobject[k1][l1] != null)
                {
                    flag = true;
                    continue label2;
                }
                l1++;
            } while(true);
        }

        flag = false;
        int i2;
label3:
        for(i2 = j - 1; i2 > 0 && !flag; i2--)
        {
            int j2 = 0;
            do
            {
                if(j2 >= i)
                    continue label3;
                if(acellobject[i2][j2] != null)
                {
                    flag = true;
                    continue label3;
                }
                j2++;
            } while(true);
        }

        if(k == 0 && k == i - 1 && k1 == 0 && i2 == j - 1)
            return;
        if(k > i1 || k1 > i2)
            return;
        CellObject acellobject1[][] = new CellObject[(i2 - k1) + 1][(i1 - k) + 1];
        for(int k2 = 0; k2 < (i2 - k1) + 1; k2++)
        {
            for(int l2 = 0; l2 < (i1 - k) + 1; l2++)
                if(acellobject[k2 + k1][l2 + k] != null)
                    acellobject1[k2][l2] = this;

        }

        ofsY -= (double)k * getCellSize();
        ofsX -= (double)k1 * getCellSize();
        setCells(acellobject1);
    }

    public void initCellsThroughCollision(HierMesh hiermesh, Loc loc, double d)
    {
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        setCellSize(d);
        int i = (int)((double)(hiermesh.collisionR() * 2.0F) / d) + 6;
        CellObject acellobject[][] = new CellObject[i][i];
        for(int j = 0; j < i; j++)
        {
            for(int k = 0; k < i; k++)
            {
                double d2 = (loc.getY() + (double)(i / 2 - j) * d) - d * 0.5D;
                double d1 = (loc.getX() + (double)(i / 2 - k) * d) - d * 0.5D;
                boolean flag = false;
label0:
                for(int l = -1; l <= 1 && !flag; l++)
                {
                    int i1 = -1;
                    do
                    {
                        if(i1 > 1 || flag)
                            continue label0;
                        point3d.set(d1 + (double)l * d * 0.40000000000000002D, d2 + (double)i1 * d * 0.40000000000000002D, loc.getZ() + (double)hiermesh.collisionR());
                        point3d1.set(d1 + (double)l * d * 0.40000000000000002D, d2 + (double)i1 * d * 0.40000000000000002D, loc.getZ() - (double)hiermesh.collisionR());
                        float f = hiermesh.detectCollisionLine(loc, point3d, point3d1);
                        if(f > 0.0F && f < 1.0F)
                        {
                            flag = true;
                            continue label0;
                        }
                        i1++;
                    } while(true);
                }

                if(flag)
                    acellobject[j][k] = this;
                else
                    acellobject[j][k] = null;
            }

        }

        ofsX = ofsY = (double)(i / 2) * d;
        setCells(acellobject);
    }

    public CellObject getClone()
    {
        if(getCells() == null)
            return null;
        CellObject acellobject[][] = new CellObject[getWidth()][getHeight()];
        for(int i = 0; i < getCells().length; i++)
        {
            for(int j = 0; j < getCells()[0].length; j++)
                acellobject[i][j] = getCells()[i][j];

        }

        CellAirPlane cellairplane = new CellAirPlane(acellobject);
        cellairplane.setCellSize(getCellSize());
        cellairplane.ofsX = ofsX;
        cellairplane.ofsY = ofsY;
        return cellairplane;
    }

    public CellAirPlane(CellObject acellobject[][])
    {
        super(acellobject);
        iFoldedWidth = -1;
    }

    public CellAirPlane(CellObject acellobject[][], HierMesh hiermesh, Loc loc, double d)
    {
        super(acellobject);
        initCellsThroughCollision(hiermesh, loc, d);
        iFoldedWidth = -1;
    }

    public void setFoldedWidth(int i)
    {
        iFoldedWidth = i;
    }

    public int getWidth()
    {
        if(iFoldedWidth > 0)
            return iFoldedWidth;
        else
            return super.getWidth();
    }

    public boolean checkAirFieldSizeCarrier(CellAirField cellairfield)
    {
        if(cellairfield.getWidth() < getWidth())
            return false;
        return cellairfield.getHeight() >= getHeight() + 1;
    }

    private static final long serialVersionUID = 1L;
    public double ofsX;
    public double ofsY;
    private int iFoldedWidth;
}
