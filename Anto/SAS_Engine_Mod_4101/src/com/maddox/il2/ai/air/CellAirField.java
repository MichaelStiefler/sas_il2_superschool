/*4.10.1 class + CTO Mod*/
package com.maddox.il2.ai.air;
import java.io.Serializable;
import java.util.ArrayList;

import com.maddox.JGP.Point3d;

public class CellAirField extends CellObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Point3d leftUpperCorner;
	private int resX;
	private int resY;
	
	public Point3d leftUpperCorner()
	{
		return leftUpperCorner;
	}
	
	public void setLeftUpperCorner(Point3d point3d)
	{
		leftUpperCorner.set(point3d);
	}
	
	public boolean findPlaceForAirPlane(CellAirPlane cellairplane)
	{
		if (!cellairplane.checkAirFieldSize(this))
			return false;
		if (!cellairplane.checkAirFieldCapacity(this))
			return false;
		for (int i = 0; i <= getHeight() - cellairplane.getHeight(); i++)
		{
			for (int i_0_ = 0; i_0_ <= getWidth() - cellairplane.getWidth(); i_0_++)
			{
				if (isThereFreePlace(cellairplane, i_0_, i))
				{
					resX = i_0_;
					resY = i;
					return true;
				}
			}
		}
		return false;
	}
	
	public int resX()
	{
		return resX;
	}
	
	public int resY()
	{
		return resY;
	}
	
	public boolean isThereFreePlace(CellAirPlane cellairplane, int i, int i_1_)
	{
		if (getCells() == null)
			return false;
		for (int i_2_ = i_1_; i_2_ < i_1_ + cellairplane.getHeight(); i_2_++)
		{
			for (int i_3_ = i; i_3_ < i + cellairplane.getWidth(); i_3_++)
			{
				if (getCells()[i_3_][i_2_] != null && cellairplane.getCells()[i_3_ - i][i_2_ - i_1_] != null)
					return false;
				if (cellairplane.getCells()[i_3_ - i][i_2_ - i_1_] != null)
				{
					for (int i_4_ = i_2_; i_4_ < getHeight(); i_4_++)
					{
						if (getCells()[i_3_][i_4_] != null && getCells()[i_3_][i_4_] != this)
							return false;
					}
				}
			}
		}
		return true;
	}
	
	public void placeAirPlane(CellAirPlane cellairplane, int i, int i_5_)
	{
		if (getCells() != null)
		{
			cellairplane.setWorldCoordinates(getXCoordinate() + (double)i * getCellSize(), getYCoordinate() + (double)i_5_ * getCellSize());
			for (int i_6_ = 0; i_6_ < cellairplane.getHeight(); i_6_++)
			{
				for (int i_7_ = 0; i_7_ < cellairplane.getWidth(); i_7_++)
				{
					if (cellairplane.getCells()[i_7_][i_6_] != null)
						getCells()[i + i_7_][i_5_ + i_6_] = cellairplane;
				}
			}
		}
	}
	
	public void placeAirPlaneId(CellAirPlane cellairplane, int i, int i_8_, String string)
	{
		if (getCells() != null)
		{
			cellairplane.setWorldCoordinates(getXCoordinate() + (double)i * getCellSize(), getYCoordinate() + (double)i_8_ * getCellSize());
			for (int i_9_ = 0; i_9_ < cellairplane.getHeight(); i_9_++)
			{
				for (int i_10_ = 0; i_10_ < cellairplane.getWidth(); i_10_++)
				{
					if (cellairplane.getCells()[i_10_][i_9_] != null)
					{
						getCells()[i + i_10_][i_8_ + i_9_] = cellairplane;
						getCells()[i + i_10_][i_8_ + i_9_].setId(string);
					}
				}
			}
		}
	}
	
	public boolean removeAirPlane(CellAirPlane cellairplane)
	{
		if (getCells() == null)
			return false;
		boolean bool = false;
		for (int i = 0; i < getCells().length; i++)
		{
			for (int i_11_ = 0; i_11_ < getCells()[0].length; i_11_++)
			{
				if (getCells()[i][i_11_] == cellairplane)
				{
					getCells()[i][i_11_] = null;
					bool = true;
				}
			}
		}
		return bool;
	}
	
	public void replaceAirPlane(CellAirPlane cellairplane, int i, int i_12_)
	{
		if (getCells() != null)
		{
			for (int i_13_ = 0; i_13_ < cellairplane.getWidth(); i_13_++)
			{
				for (int i_14_ = 0; i_14_ < cellairplane.getHeight(); i_14_++)
				{
					if (getCells()[i_13_][i_14_] == cellairplane)
						getCells()[i + i_13_][i_12_ + i_14_] = null;
				}
			}
		}
	}
	
	public void freeCells()
	{
		if (getCells() != null)
		{
			for (int i = 0; i < getCells().length; i++)
			{
				for (int i_15_ = 0; i_15_ < getCells()[0].length; i_15_++)
				{
					if (getCells()[i][i_15_] != this)
						getCells()[i][i_15_] = null;
				}
			}
		}
	}
	
	public Point3d getLeftUpperPoint(ArrayList arraylist)
	{
		Point3d point3d = new Point3d();
		double d = 1000.0;
		double d_16_ = -1000.0;
		for (int i = 0; i < arraylist.size(); i++)
		{
			Point3d point3d_17_ = (Point3d)arraylist.get(i);
			if (point3d_17_.x < d)
				d = point3d_17_.x;
			if (point3d_17_.y > d_16_)
				d_16_ = point3d_17_.y;
		}
		point3d.set(d, d_16_, 0.0);
		return point3d;
	}
	
	public Point3d getRightDownPoint(ArrayList arraylist)
	{
		Point3d point3d = new Point3d();
		double d = -1000.0;
		double d_18_ = 1000.0;
		for (int i = 0; i < arraylist.size(); i++)
		{
			Point3d point3d_19_ = (Point3d)arraylist.get(i);
			if (point3d_19_.x > d)
				d = point3d_19_.x;
			if (point3d_19_.y < d_18_)
				d_18_ = point3d_19_.y;
		}
		point3d.set(d, d_18_, 0.0);
		return point3d;
	}
	
	public void fieldInitWithComplexPoly(ArrayList arraylist, double d)
	{
		Point3d point3d = new Point3d();
		Point3d point3d_20_ = new Point3d();
		Point3d point3d_21_ = new Point3d();
		Point3d point3d_22_ = new Point3d();
		setCellSize(d);
		Point3d point3d_23_ = getLeftUpperPoint(arraylist);
		setLeftUpperCorner(point3d_23_);
		Point3d point3d_24_ = getRightDownPoint(arraylist);
		int i = (int)(Math.abs(point3d_24_.x - point3d_23_.x) / d);
		int i_25_ = (int)(Math.abs(point3d_24_.y - point3d_23_.y) / d);
		CellObject[][] cellobjects = new CellObject[i][i_25_];
		setCells(cellobjects);
		clearCells();
		for (int i_26_ = 0; i_26_ < getWidth(); i_26_++)
		{
			for (int i_27_ = 0; i_27_ < getHeight(); i_27_++)
			{
				point3d.x = point3d_23_.x + (double)i_26_ * d;
				point3d.y = point3d_23_.y - (double)i_27_ * d;
				point3d_20_.x = point3d_23_.x + (double)i_26_ * d + d;
				point3d_20_.y = point3d_23_.y - (double)i_27_ * d;
				point3d_21_.x = point3d_23_.x + (double)i_26_ * d;
				point3d_21_.y = point3d_23_.y - (double)i_27_ * d + d;
				point3d_22_.x = point3d_23_.x + (double)i_26_ * d + d;
				point3d_22_.y = point3d_23_.y - (double)i_27_ * d + d;
				if (CellTools.belongsToComplex(arraylist, point3d) && CellTools.belongsToComplex(arraylist, point3d_20_) && CellTools.belongsToComplex(arraylist, point3d_21_) && CellTools.belongsToComplex(arraylist, point3d_22_))
					getCells()[i_26_][i_27_] = null;
				else
					getCells()[i_26_][i_27_] = this;
			}
		}
	}
	
	public CellObject getClone()
	{
		if (getCells() == null)
			return null;
		CellObject[][] cellobjects = new CellObject[getWidth()][getHeight()];
		for (int i = 0; i < getCells().length; i++)
		{
			for (int i_28_ = 0; i_28_ < getCells()[0].length; i_28_++)
				cellobjects[i][i_28_] = getCells()[i][i_28_];
		}
		CellAirField cellairfield_29_ = new CellAirField(cellobjects, leftUpperCorner());
		cellairfield_29_.setCellSize(getCellSize());
		return cellairfield_29_;
	}
	
	public CellAirField(CellObject[][] cellobjects)
	{
		super(cellobjects);
		leftUpperCorner = new Point3d();
		resX = 0;
		resY = 0;
	}
	
	public CellAirField(CellObject[][] cellobjects, Point3d point3d)
	{
		super(cellobjects);
		leftUpperCorner = new Point3d();
		resX = 0;
		resY = 0;
		setLeftUpperCorner(point3d);
	}
	
	public CellAirField(CellObject[][] cellobjects, ArrayList arraylist, double d)
	{
		super(cellobjects);
		leftUpperCorner = new Point3d();
		resX = 0;
		resY = 0;
		fieldInitWithComplexPoly(arraylist, d);
	}
	
	//TODO: CTO Mod
	//-------------------------------
    public boolean findPlaceForAirPlaneCarrier(com.maddox.il2.ai.air.CellAirPlane cellairplane)
    {
        if(!cellairplane.checkAirFieldSizeCarrier(this))
            return false;
        if(!cellairplane.checkAirFieldCapacity(this))
            return false;
        for(int i = 0; i <= getHeight() - (cellairplane.getHeight() + 1); i++)
        {
            for(int j = 0; j <= getWidth() - cellairplane.getWidth(); j++)
                if(isThereFreePlaceCarrier(cellairplane, j, i))
                {
                    resX = j;
                    resY = i;
                    return true;
                }

        }

        return false;
    }
	//-------------------------------
    
  //TODO: CTO Mod
	//-------------------------------
    public boolean isThereFreePlaceCarrier(com.maddox.il2.ai.air.CellAirPlane cellairplane, int i, int j)
    {
        if(getCells() == null)
            return false;
        for(int k = j; k < j + cellairplane.getHeight() + 1; k++)
        {
            for(int l = i; l < i + cellairplane.getWidth(); l++)
                if(getCells()[l][k] != null)
                    return false;

        }

        return true;
    }
	//-------------------------------
    
    //TODO: CTO Mod
	//-------------------------------
    public void placeAirPlaneCarrier(com.maddox.il2.ai.air.CellAirPlane cellairplane, int i, int j)
    {
        if(getCells() == null)
            return;
        cellairplane.setWorldCoordinates(getXCoordinate() + (double)i * getCellSize(), getYCoordinate() + (double)j * getCellSize());
        for(int k = 0; k < cellairplane.getHeight() + 1; k++)
        {
            for(int l = 0; l < cellairplane.getWidth(); l++)
                getCells()[i + l][j + k] = cellairplane;

        }

    }
	//-------------------------------
}