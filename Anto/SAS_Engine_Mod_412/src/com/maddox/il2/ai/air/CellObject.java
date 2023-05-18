/*Modified CellObject class for the SAS Engine Mod*/

package com.maddox.il2.ai.air;
import java.io.Serializable;

public class CellObject
    implements Serializable
{
	private static final long serialVersionUID = 1L;
    private CellObject cells[][];
    private String id;
    private double cellX;
    private double cellY;
    private double cellSize;
    
	public void setCells(CellObject acellobject[][])
    {
        cells = acellobject;
    }

    public CellObject[][] getCells()
    {
        return cells;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setCellX(double d)
    {
        cellX = d;
    }

    public double getCellX()
    {
        return cellX;
    }

    public void setCellY(double d)
    {
        cellY = d;
    }

    public double getCellY()
    {
        return cellY;
    }

    public void setCoordinates(double d, double d1)
    {
        setCellX(d);
        setCellY(d1);
    }

    public int getWidth()
    {
        if(cells == null)
            return 0;
        else
            return cells.length;
    }

    public int getHeight()
    {
        if(cells == null)
            return 0;
        if(cells[0] == null)
            return 0;
        else
            return cells[0].length;
    }

    public double getXCoordinate()
    {
        if(cells == null)
            return 0.0D;
        if(cells[0][0] == null)
            return 0.0D;
        else
            return cells[0][0].getCellX();
    }

    public double getYCoordinate()
    {
        if(cells == null)
            return 0.0D;
        if(cells[0][0] == null)
            return 0.0D;
        else
            return cells[0][0].getCellY();
    }

    public double getCellSize()
    {
        return cellSize;
    }

    public void setCellSize(double d)
    {
        cellSize = d;
    }

    public void setWorldCoordinates(double d, double d1)
    {
        if(cells == null)
            return;
        if(cells[0][0] == null)
            return;
        double d2 = cells[0][0].getCellSize();
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
                if(cells[i][j] != null)
                    cells[i][j].setCoordinates(d + (double)i * d2, d1 + (double)j * d2);

        }

    }

    public CellObject getClone()
    {
        if(cells == null)
            return null;
        CellObject acellobject[][] = new CellObject[getWidth()][getHeight()];
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
                if(cells[i][j] != null)
                    acellobject[i][j] = cells[i][j];

        }

        return new CellObject(acellobject);
    }

    public void clearCells()
    {
        if(cells == null)
            return;
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
                cells[i][j] = null;

        }

    }

    public int calcFilledCells()
    {
        int i = 0;
        if(cells == null)
            return 0;
        for(int j = 0; j < cells.length; j++)
        {
            for(int k = 0; k < cells[0].length; k++)
                if(cells[j][k] != null)
                    i++;

        }

        return i;
    }

    public int calcFreeCells()
    {
        int i = 0;
        if(cells == null)
            return 0;
        if(cells[0][0] == null)
            return 0;
        for(int j = 0; j < cells.length; j++)
        {
            for(int k = 0; k < cells[0].length; k++)
                if(cells[j][k] == null)
                    i++;

        }

        return i;
    }

    public void debugPrint()
    {
        if(cells == null)
            return;
        System.out.println();
        for(int i = 0; i < cells[0].length; i++)
        {
            for(int j = 0; j < cells.length; j++)
                if(cells[j][i] != null)
                    System.out.print(cells[j][i].getId());
                else
                    System.out.print("_");

            System.out.println();
        }

        System.out.println();
    }

    public void reInitReferences()
    {
        if(cells == null)
            return;
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
                if(cells[i][j] != null)
                    cells[i][j] = this;

        }

    }

    public CellObject(CellObject acellobject[][])
    {
        id = "*";
        cellSize = 0.5D;
        cells = acellobject;
        reInitReferences();
    }

    public CellObject()
    {
        id = "*";
        cellSize = 0.5D;
        cells = new CellObject[1][1];
        cells[0][0] = this;
    }
    
	//TODO: CTO Mod
	//-------------------------------
    public void shrinkWidth(int i)
    {
        for(int j = getWidth() - i; j < getWidth(); j++)
        {
            for(int k = 0; k < getHeight(); k++)
                cells[j][k] = this;

        }

    }

    public void shrinkHeight(int i)
    {
        for(int j = getHeight() - i; j < getHeight(); j++)
        {
            for(int k = 0; k < getWidth(); k++)
                cells[k][j] = this;

        }

    }

    public void fillRectangle(int i, int j, int k, int l)
    {
        int i1 = i;
        int j1 = j;
        int k1 = k;
        int l1 = l;
        for(int i2 = i1; i2 <= k1; i2++)
        {
            for(int j2 = j1; j2 <= l1; j2++)
                cells[i2][j2] = this;

        }

    }
	//-------------------------------
}
