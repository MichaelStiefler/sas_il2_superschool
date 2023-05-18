/*4.10.1 class + CTO Mod*/
package com.maddox.il2.ai.air;
import java.io.Serializable;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;

public class CellAirPlane extends CellObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	public double ofsX;
	public double ofsY;
	
	public boolean checkAirFieldSize(CellAirField cellairfield)
	{
		if (cellairfield.getWidth() < getWidth())
			return false;
		if (cellairfield.getHeight() < getHeight())
			return false;
		return true;
	}
	
	public boolean checkAirFieldCapacity(CellAirField cellairfield)
	{
		if (cellairfield.calcFreeCells() >= calcFilledCells())
			return true;
		return true;
	}
	
	public void blurSiluetUp()
	{
		if (getCells() != null)
		{
			for (int i = 0; i < getHeight(); i++)
			{
				for (int i_0_ = 0; i_0_ < getWidth(); i_0_++)
				{
					if (getCells()[i_0_][i] == this)
					{
						for (int i_1_ = i; i_1_ >= 0; i_1_--)
							getCells()[i_0_][i_1_] = this;
					}
				}
			}
		}
	}
	
	public void blurSiluetDown()
	{
		if (getCells() != null)
		{
			for (int i = 0; i < getHeight(); i++)
			{
				for (int i_2_ = 0; i_2_ < getWidth(); i_2_++)
				{
					if (getCells()[i_2_][i] == this)
					{
						for (int i_3_ = i; i_3_ < getHeight(); i_3_++)
							getCells()[i_2_][i_3_] = this;
					}
				}
			}
		}
	}
	
	public void blurSiluet4x()
	{
		if (getCells() != null)
		{
			CellObject cellobject = getClone();
			cellobject.clearCells();
			for (int i = 0; i < getHeight(); i++)
			{
				for (int i_4_ = 0; i_4_ < getWidth(); i_4_++)
				{
					if (getCells()[i_4_][i] == this)
					{
						cellobject.getCells()[i_4_][i] = cellobject;
						if (i_4_ > 0)
							cellobject.getCells()[i_4_ - 1][i] = cellobject;
						if (i_4_ < getWidth() - 1)
							cellobject.getCells()[i_4_ + 1][i] = cellobject;
						if (i > 0)
							cellobject.getCells()[i_4_][i - 1] = cellobject;
						if (i < getHeight() - 1)
							cellobject.getCells()[i_4_][i + 1] = cellobject;
					}
				}
			}
			clearCells();
			setCells(cellobject.getCells());
			reInitReferences();
		}
	}
	
	public void blurSiluet8x()
	{
		if (getCells() != null)
		{
			CellObject cellobject = getClone();
			for (int i = 0; i < getHeight(); i++)
			{
				for (int i_5_ = 0; i_5_ < getWidth(); i_5_++)
				{
					if (cellobject.getCells()[i_5_][i] != null)
					{
						if (i_5_ > 0)
							getCells()[i_5_ - 1][i] = this;
						if (i_5_ < getWidth() - 1)
							getCells()[i_5_ + 1][i] = this;
						if (i > 0)
							getCells()[i_5_][i - 1] = this;
						if (i < getHeight() - 1)
							getCells()[i_5_][i + 1] = this;
						if (i_5_ > 0 && i > 0)
							getCells()[i_5_ - 1][i - 1] = this;
						if (i_5_ < getWidth() - 1 && i > 0)
							getCells()[i_5_ + 1][i - 1] = this;
						if (i_5_ > 0 && i < getHeight() - 1)
							getCells()[i_5_ - 1][i + 1] = this;
						if (i_5_ < getWidth() - 1 && i < getHeight() - 1)
							getCells()[i_5_ + 1][i + 1] = this;
					}
				}
			}
		}
	}
	
	public void clampCells()
	{
		CellObject[][] cellobjects = getCells();
		if (cellobjects != null)
		{
			int i = getWidth();
			int i_6_ = getHeight();
			boolean bool = false;
			int i_7_;
			for (i_7_ = 0; i_7_ < i && !bool; i_7_++)
			{
				for (int i_8_ = 0; i_8_ < i_6_; i_8_++)
				{
					if (cellobjects[i_8_][i_7_] != null)
					{
						bool = true;
						break;
					}
				}
			}
			bool = false;
			int i_9_;
			for (i_9_ = i - 1; i_9_ > 0 && !bool; i_9_--)
			{
				for (int i_10_ = 0; i_10_ < i_6_; i_10_++)
				{
					if (cellobjects[i_10_][i_9_] != null)
					{
						bool = true;
						break;
					}
				}
			}
			bool = false;
			int i_11_;
			for (i_11_ = 0; i_11_ < i_6_ && !bool; i_11_++)
			{
				for (int i_12_ = 0; i_12_ < i; i_12_++)
				{
					if (cellobjects[i_11_][i_12_] != null)
					{
						bool = true;
						break;
					}
				}
			}
			bool = false;
			int i_13_;
			for (i_13_ = i_6_ - 1; i_13_ > 0 && !bool; i_13_--)
			{
				for (int i_14_ = 0; i_14_ < i; i_14_++)
				{
					if (cellobjects[i_13_][i_14_] != null)
					{
						bool = true;
						break;
					}
				}
			}
			if ((i_7_ != 0 || i_7_ != i - 1 || i_11_ != 0 || i_13_ != i_6_ - 1) && (i_7_ <= i_9_ && i_11_ <= i_13_))
			{
				CellObject[][] cellobjects_15_ = new CellObject[i_13_ - i_11_ + 1][i_9_ - i_7_ + 1];
				for (int i_16_ = 0; i_16_ < i_13_ - i_11_ + 1; i_16_++)
				{
					for (int i_17_ = 0; i_17_ < i_9_ - i_7_ + 1; i_17_++)
					{
						if (cellobjects[i_16_ + i_11_][i_17_ + i_7_] != null)
							cellobjects_15_[i_16_][i_17_] = this;
					}
				}
				ofsY -= (double)i_7_ * getCellSize();
				ofsX -= (double)i_11_ * getCellSize();
				setCells(cellobjects_15_);
			}
		}
	}
	
	public void initCellsThroughCollision(HierMesh hiermesh, Loc loc, double d)
	{
		Point3d point3d = new Point3d();
		Point3d point3d_18_ = new Point3d();
		setCellSize(d);
		int i = (int)((double)(hiermesh.collisionR() * 2.0F) / d) + 6;
		CellObject[][] cellobjects = new CellObject[i][i];
		for (int i_19_ = 0; i_19_ < i; i_19_++)
		{
			for (int i_20_ = 0; i_20_ < i; i_20_++)
			{
				double d_21_ = loc.getY() + (double)(i / 2 - i_19_) * d - d * 0.5;
				double d_22_ = loc.getX() + (double)(i / 2 - i_20_) * d - d * 0.5;
				boolean bool = false;
				for (int i_23_ = -1; i_23_ <= 1 && !bool; i_23_++)
				{
					for (int i_24_ = -1; i_24_ <= 1 && !bool; i_24_++)
					{
						point3d.set(d_22_ + (double)i_23_ * d * 0.4, d_21_ + (double)i_24_ * d * 0.4, (loc.getZ() + (double)hiermesh.collisionR()));
						point3d_18_.set(d_22_ + (double)i_23_ * d * 0.4, d_21_ + (double)i_24_ * d * 0.4, (loc.getZ() - (double)hiermesh.collisionR()));
						float f = hiermesh.detectCollisionLine(loc, point3d, point3d_18_);
						if (f > 0.0F && f < 1.0F)
						{
							bool = true;
							break;
						}
					}
				}
				if (bool)
					cellobjects[i_19_][i_20_] = this;
				else
					cellobjects[i_19_][i_20_] = null;
			}
		}
		ofsX = ofsY = (double)(i / 2) * d;
		setCells(cellobjects);
	}
	
	public CellObject getClone()
	{
		if (getCells() == null)
			return null;
		CellObject[][] cellobjects = new CellObject[getWidth()][getHeight()];
		for (int i = 0; i < getCells().length; i++)
		{
			for (int i_25_ = 0; i_25_ < getCells()[0].length; i_25_++)
				cellobjects[i][i_25_] = getCells()[i][i_25_];
		}
		CellAirPlane cellairplane_26_ = new CellAirPlane(cellobjects);
		cellairplane_26_.setCellSize(getCellSize());
		cellairplane_26_.ofsX = ofsX;
		cellairplane_26_.ofsY = ofsY;
		return cellairplane_26_;
	}
	
	public CellAirPlane(CellObject[][] cellobjects)
	{
		super(cellobjects);
		
		//TODO: CTO Mod
		//-------------------------------
        iFoldedWidth = -1;
		//-------------------------------
	}
	
	public CellAirPlane(CellObject[][] cellobjects, HierMesh hiermesh, Loc loc, double d)
	{
		super(cellobjects);
		initCellsThroughCollision(hiermesh, loc, d);
		
		//TODO: CTO Mod
		//-------------------------------
        iFoldedWidth = -1;
		//-------------------------------
	}
	
	//TODO: CTO Mod
	//-------------------------------
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
	//-------------------------------
    
  //TODO: CTO Mod
	//-------------------------------
    public boolean checkAirFieldSizeCarrier(com.maddox.il2.ai.air.CellAirField cellairfield)
    {
        if(cellairfield.getWidth() < getWidth())
            return false;
        else
            return cellairfield.getHeight() >= getHeight() + 1;
    }
	//-------------------------------
    
  //TODO: CTO Mod
	//-------------------------------
    private int iFoldedWidth;
	//-------------------------------
}
