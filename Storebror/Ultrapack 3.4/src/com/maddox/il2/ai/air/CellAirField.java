/*4.10.1 class + CTO Mod*/
package com.maddox.il2.ai.air;

import java.io.Serializable;
import java.util.ArrayList;

import com.maddox.JGP.Point3d;

public class CellAirField extends CellObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private Point3d           leftUpperCorner;
    private int               resX;
    private int               resY;

    public Point3d leftUpperCorner() {
        return this.leftUpperCorner;
    }

    public void setLeftUpperCorner(Point3d point3d) {
        this.leftUpperCorner.set(point3d);
    }

    public boolean findPlaceForAirPlane(CellAirPlane cellairplane) {
        if (!cellairplane.checkAirFieldSize(this)) return false;
        if (!cellairplane.checkAirFieldCapacity(this)) return false;
        for (int i = 0; i <= this.getHeight() - cellairplane.getHeight(); i++)
            for (int i_0_ = 0; i_0_ <= this.getWidth() - cellairplane.getWidth(); i_0_++)
                if (this.isThereFreePlace(cellairplane, i_0_, i)) {
                    this.resX = i_0_;
                    this.resY = i;
                    return true;
                }
        return false;
    }

    public int resX() {
        return this.resX;
    }

    public int resY() {
        return this.resY;
    }

    public boolean isThereFreePlace(CellAirPlane cellairplane, int i, int i_1_) {
        if (this.getCells() == null) return false;
        for (int i_2_ = i_1_; i_2_ < i_1_ + cellairplane.getHeight(); i_2_++)
            for (int i_3_ = i; i_3_ < i + cellairplane.getWidth(); i_3_++) {
                if (this.getCells()[i_3_][i_2_] != null && cellairplane.getCells()[i_3_ - i][i_2_ - i_1_] != null) return false;
                if (cellairplane.getCells()[i_3_ - i][i_2_ - i_1_] != null) for (int i_4_ = i_2_; i_4_ < this.getHeight(); i_4_++)
                    if (this.getCells()[i_3_][i_4_] != null && this.getCells()[i_3_][i_4_] != this) return false;
            }
        return true;
    }

    public void placeAirPlane(CellAirPlane cellairplane, int i, int i_5_) {
        if (this.getCells() != null) {
            cellairplane.setWorldCoordinates(this.getXCoordinate() + i * this.getCellSize(), this.getYCoordinate() + i_5_ * this.getCellSize());
            for (int i_6_ = 0; i_6_ < cellairplane.getHeight(); i_6_++)
                for (int i_7_ = 0; i_7_ < cellairplane.getWidth(); i_7_++)
                    if (cellairplane.getCells()[i_7_][i_6_] != null) this.getCells()[i + i_7_][i_5_ + i_6_] = cellairplane;
        }
    }

    public void placeAirPlaneId(CellAirPlane cellairplane, int i, int i_8_, String string) {
        if (this.getCells() != null) {
            cellairplane.setWorldCoordinates(this.getXCoordinate() + i * this.getCellSize(), this.getYCoordinate() + i_8_ * this.getCellSize());
            for (int i_9_ = 0; i_9_ < cellairplane.getHeight(); i_9_++)
                for (int i_10_ = 0; i_10_ < cellairplane.getWidth(); i_10_++)
                    if (cellairplane.getCells()[i_10_][i_9_] != null) {
                        this.getCells()[i + i_10_][i_8_ + i_9_] = cellairplane;
                        this.getCells()[i + i_10_][i_8_ + i_9_].setId(string);
                    }
        }
    }

    public boolean removeAirPlane(CellAirPlane cellairplane) {
        if (this.getCells() == null) return false;
        boolean bool = false;
        for (int i = 0; i < this.getCells().length; i++)
            for (int i_11_ = 0; i_11_ < this.getCells()[0].length; i_11_++)
                if (this.getCells()[i][i_11_] == cellairplane) {
                    this.getCells()[i][i_11_] = null;
                    bool = true;
                }
        return bool;
    }

    public void replaceAirPlane(CellAirPlane cellairplane, int i, int i_12_) {
        if (this.getCells() != null) for (int i_13_ = 0; i_13_ < cellairplane.getWidth(); i_13_++)
            for (int i_14_ = 0; i_14_ < cellairplane.getHeight(); i_14_++)
                if (this.getCells()[i_13_][i_14_] == cellairplane) this.getCells()[i + i_13_][i_12_ + i_14_] = null;
    }

    public void freeCells() {
        if (this.getCells() != null) for (int i = 0; i < this.getCells().length; i++)
            for (int i_15_ = 0; i_15_ < this.getCells()[0].length; i_15_++)
                if (this.getCells()[i][i_15_] != this) this.getCells()[i][i_15_] = null;
    }

    public Point3d getLeftUpperPoint(ArrayList arraylist) {
        Point3d point3d = new Point3d();
        double d = 1000.0;
        double d_16_ = -1000.0;
        for (int i = 0; i < arraylist.size(); i++) {
            Point3d point3d_17_ = (Point3d) arraylist.get(i);
            if (point3d_17_.x < d) d = point3d_17_.x;
            if (point3d_17_.y > d_16_) d_16_ = point3d_17_.y;
        }
        point3d.set(d, d_16_, 0.0);
        return point3d;
    }

    public Point3d getRightDownPoint(ArrayList arraylist) {
        Point3d point3d = new Point3d();
        double d = -1000.0;
        double d_18_ = 1000.0;
        for (int i = 0; i < arraylist.size(); i++) {
            Point3d point3d_19_ = (Point3d) arraylist.get(i);
            if (point3d_19_.x > d) d = point3d_19_.x;
            if (point3d_19_.y < d_18_) d_18_ = point3d_19_.y;
        }
        point3d.set(d, d_18_, 0.0);
        return point3d;
    }

    public void fieldInitWithComplexPoly(ArrayList arraylist, double d) {
        Point3d point3d = new Point3d();
        Point3d point3d_20_ = new Point3d();
        Point3d point3d_21_ = new Point3d();
        Point3d point3d_22_ = new Point3d();
        this.setCellSize(d);
        Point3d point3d_23_ = this.getLeftUpperPoint(arraylist);
        this.setLeftUpperCorner(point3d_23_);
        Point3d point3d_24_ = this.getRightDownPoint(arraylist);
        int i = (int) (Math.abs(point3d_24_.x - point3d_23_.x) / d);
        int i_25_ = (int) (Math.abs(point3d_24_.y - point3d_23_.y) / d);
        CellObject[][] cellobjects = new CellObject[i][i_25_];
        this.setCells(cellobjects);
        this.clearCells();
        for (int i_26_ = 0; i_26_ < this.getWidth(); i_26_++)
            for (int i_27_ = 0; i_27_ < this.getHeight(); i_27_++) {
                point3d.x = point3d_23_.x + i_26_ * d;
                point3d.y = point3d_23_.y - i_27_ * d;
                point3d_20_.x = point3d_23_.x + i_26_ * d + d;
                point3d_20_.y = point3d_23_.y - i_27_ * d;
                point3d_21_.x = point3d_23_.x + i_26_ * d;
                point3d_21_.y = point3d_23_.y - i_27_ * d + d;
                point3d_22_.x = point3d_23_.x + i_26_ * d + d;
                point3d_22_.y = point3d_23_.y - i_27_ * d + d;
                if (CellTools.belongsToComplex(arraylist, point3d) && CellTools.belongsToComplex(arraylist, point3d_20_) && CellTools.belongsToComplex(arraylist, point3d_21_) && CellTools.belongsToComplex(arraylist, point3d_22_))
                    this.getCells()[i_26_][i_27_] = null;
                else this.getCells()[i_26_][i_27_] = this;
            }
    }

    public CellObject getClone() {
        if (this.getCells() == null) return null;
        CellObject[][] cellobjects = new CellObject[this.getWidth()][this.getHeight()];
        for (int i = 0; i < this.getCells().length; i++)
            for (int i_28_ = 0; i_28_ < this.getCells()[0].length; i_28_++)
                cellobjects[i][i_28_] = this.getCells()[i][i_28_];
        CellAirField cellairfield_29_ = new CellAirField(cellobjects, this.leftUpperCorner());
        cellairfield_29_.setCellSize(this.getCellSize());
        return cellairfield_29_;
    }

    public CellAirField(CellObject[][] cellobjects) {
        super(cellobjects);
        this.leftUpperCorner = new Point3d();
        this.resX = 0;
        this.resY = 0;
    }

    public CellAirField(CellObject[][] cellobjects, Point3d point3d) {
        super(cellobjects);
        this.leftUpperCorner = new Point3d();
        this.resX = 0;
        this.resY = 0;
        this.setLeftUpperCorner(point3d);
    }

    public CellAirField(CellObject[][] cellobjects, ArrayList arraylist, double d) {
        super(cellobjects);
        this.leftUpperCorner = new Point3d();
        this.resX = 0;
        this.resY = 0;
        this.fieldInitWithComplexPoly(arraylist, d);
    }

    // TODO: CTO Mod
    // -------------------------------
    public boolean findPlaceForAirPlaneCarrier(com.maddox.il2.ai.air.CellAirPlane cellairplane) {
        if (!cellairplane.checkAirFieldSizeCarrier(this)) return false;
        if (!cellairplane.checkAirFieldCapacity(this)) return false;
        for (int i = 0; i <= this.getHeight() - (cellairplane.getHeight() + 1); i++)
            for (int j = 0; j <= this.getWidth() - cellairplane.getWidth(); j++)
                if (this.isThereFreePlaceCarrier(cellairplane, j, i)) {
                    this.resX = j;
                    this.resY = i;
                    return true;
                }

        return false;
    }
    // -------------------------------

    // TODO: CTO Mod
    // -------------------------------
    public boolean isThereFreePlaceCarrier(com.maddox.il2.ai.air.CellAirPlane cellairplane, int i, int j) {
        if (this.getCells() == null) return false;
        for (int k = j; k < j + cellairplane.getHeight() + 1; k++)
            for (int l = i; l < i + cellairplane.getWidth(); l++)
                if (this.getCells()[l][k] != null) return false;

        return true;
    }
    // -------------------------------

    // TODO: CTO Mod
    // -------------------------------
    public void placeAirPlaneCarrier(com.maddox.il2.ai.air.CellAirPlane cellairplane, int i, int j) {
        if (this.getCells() == null) return;
        cellairplane.setWorldCoordinates(this.getXCoordinate() + i * this.getCellSize(), this.getYCoordinate() + j * this.getCellSize());
        for (int k = 0; k < cellairplane.getHeight() + 1; k++)
            for (int l = 0; l < cellairplane.getWidth(); l++)
                this.getCells()[i + l][j + k] = cellairplane;

    }
    // -------------------------------
}
