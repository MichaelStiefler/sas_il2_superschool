/*4.10.1 class + CTO Mod*/
package com.maddox.il2.ai.air;
import java.io.Serializable;

public class CellObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	private CellObject[][] cells;
	private String id = "*";
	private double cellX;
	private double cellY;
	private double cellSize = 0.5;
	
	public void setCells(CellObject[][] cellobjects)
	{
		cells = cellobjects;
	}
	
	public CellObject[][] getCells()
	{
		return cells;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String string)
	{
		id = string;
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
	
	public void setCoordinates(double d, double d_0_)
	{
		setCellX(d);
		setCellY(d_0_);
	}
	
	public int getWidth()
	{
		if (cells == null)
			return 0;
		return cells.length;
	}
	
	public int getHeight()
	{
		if (cells == null)
			return 0;
		if (cells[0] == null)
			return 0;
		return cells[0].length;
	}
	
	public double getXCoordinate()
	{
		if (cells == null)
			return 0.0;
		if (cells[0][0] == null)
			return 0.0;
		return cells[0][0].getCellX();
	}
	
	public double getYCoordinate()
	{
		if (cells == null)
			return 0.0;
		if (cells[0][0] == null)
			return 0.0;
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
	
	public void setWorldCoordinates(double d, double d_1_)
	{
		if (cells != null && cells[0][0] != null)
		{
			double d_2_ = cells[0][0].getCellSize();
			for (int i = 0; i < cells.length; i++)
			{
				for (int i_3_ = 0; i_3_ < cells[0].length; i_3_++)
				{
					if (cells[i][i_3_] != null)
						cells[i][i_3_].setCoordinates(d + (double)i * d_2_, d_1_ + ((double)i_3_ * d_2_));
				}
			}
		}
	}
	
	public CellObject getClone()
	{
		if (cells == null)
			return null;
		CellObject[][] cellobjects = new CellObject[getWidth()][getHeight()];
		for (int i = 0; i < cells.length; i++)
		{
			for (int i_4_ = 0; i_4_ < cells[0].length; i_4_++)
			{
				if (cells[i][i_4_] != null)
					cellobjects[i][i_4_] = cells[i][i_4_];
			}
		}
		return new CellObject(cellobjects);
	}
	
	public void clearCells()
	{
		if (cells != null)
		{
			for (int i = 0; i < cells.length; i++)
			{
				for (int i_5_ = 0; i_5_ < cells[0].length; i_5_++)
					cells[i][i_5_] = null;
			}
		}
	}
	
	public int calcFilledCells()
	{
		int i = 0;
		if (cells == null)
			return 0;
		for (int i_6_ = 0; i_6_ < cells.length; i_6_++)
		{
			for (int i_7_ = 0; i_7_ < cells[0].length; i_7_++)
			{
				if (cells[i_6_][i_7_] != null)
					i++;
			}
		}
		return i;
	}
	
	public int calcFreeCells()
	{
		int i = 0;
		if (cells == null)
			return 0;
		if (cells[0][0] == null)
			return 0;
		for (int i_8_ = 0; i_8_ < cells.length; i_8_++)
		{
			for (int i_9_ = 0; i_9_ < cells[0].length; i_9_++)
			{
				if (cells[i_8_][i_9_] == null)
					i++;
			}
		}
		return i;
	}
	
	public void debugPrint()
	{
		if (cells != null)
		{
			System.out.println();
			for (int i = 0; i < cells[0].length; i++)
			{
				for (int i_10_ = 0; i_10_ < cells.length; i_10_++)
				{
					if (cells[i_10_][i] != null)
						System.out.print(cells[i_10_][i].getId());
					else
						System.out.print("_");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public void reInitReferences()
	{
		if (cells != null)
		{
			for (int i = 0; i < cells.length; i++)
			{
				for (int i_11_ = 0; i_11_ < cells[0].length; i_11_++)
				{
					if (cells[i][i_11_] != null)
						cells[i][i_11_] = this;
				}
			}
		}
	}
	
	public CellObject(CellObject[][] cellobjects)
	{
		cells = cellobjects;
		reInitReferences();
	}
	
	public CellObject()
	{
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
