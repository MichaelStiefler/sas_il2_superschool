/*4.10.1 class + CTO Mod*/
package com.maddox.il2.ai.air;

import java.io.Serializable;

public class CellObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private CellObject[][]    cells;
    private String            id               = "*";
    private double            cellX;
    private double            cellY;
    private double            cellSize         = 0.5;

    public void setCells(CellObject[][] cellobjects) {
        this.cells = cellobjects;
    }

    public CellObject[][] getCells() {
        return this.cells;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setCellX(double d) {
        this.cellX = d;
    }

    public double getCellX() {
        return this.cellX;
    }

    public void setCellY(double d) {
        this.cellY = d;
    }

    public double getCellY() {
        return this.cellY;
    }

    public void setCoordinates(double d, double d_0_) {
        this.setCellX(d);
        this.setCellY(d_0_);
    }

    public int getWidth() {
        if (this.cells == null) return 0;
        return this.cells.length;
    }

    public int getHeight() {
        if (this.cells == null) return 0;
        if (this.cells[0] == null) return 0;
        return this.cells[0].length;
    }

    public double getXCoordinate() {
        if (this.cells == null) return 0.0;
        if (this.cells[0][0] == null) return 0.0;
        return this.cells[0][0].getCellX();
    }

    public double getYCoordinate() {
        if (this.cells == null) return 0.0;
        if (this.cells[0][0] == null) return 0.0;
        return this.cells[0][0].getCellY();
    }

    public double getCellSize() {
        return this.cellSize;
    }

    public void setCellSize(double d) {
        this.cellSize = d;
    }

    public void setWorldCoordinates(double d, double d_1_) {
        if (this.cells != null && this.cells[0][0] != null) {
            double d_2_ = this.cells[0][0].getCellSize();
            for (int i = 0; i < this.cells.length; i++)
                for (int i_3_ = 0; i_3_ < this.cells[0].length; i_3_++)
                    if (this.cells[i][i_3_] != null) this.cells[i][i_3_].setCoordinates(d + i * d_2_, d_1_ + i_3_ * d_2_);
        }
    }

    public CellObject getClone() {
        if (this.cells == null) return null;
        CellObject[][] cellobjects = new CellObject[this.getWidth()][this.getHeight()];
        for (int i = 0; i < this.cells.length; i++)
            for (int i_4_ = 0; i_4_ < this.cells[0].length; i_4_++)
                if (this.cells[i][i_4_] != null) cellobjects[i][i_4_] = this.cells[i][i_4_];
        return new CellObject(cellobjects);
    }

    public void clearCells() {
        if (this.cells != null) for (int i = 0; i < this.cells.length; i++)
            for (int i_5_ = 0; i_5_ < this.cells[0].length; i_5_++)
                this.cells[i][i_5_] = null;
    }

    public int calcFilledCells() {
        int i = 0;
        if (this.cells == null) return 0;
        for (int i_6_ = 0; i_6_ < this.cells.length; i_6_++)
            for (int i_7_ = 0; i_7_ < this.cells[0].length; i_7_++)
                if (this.cells[i_6_][i_7_] != null) i++;
        return i;
    }

    public int calcFreeCells() {
        int i = 0;
        if (this.cells == null) return 0;
        if (this.cells[0][0] == null) return 0;
        for (int i_8_ = 0; i_8_ < this.cells.length; i_8_++)
            for (int i_9_ = 0; i_9_ < this.cells[0].length; i_9_++)
                if (this.cells[i_8_][i_9_] == null) i++;
        return i;
    }

    public void debugPrint() {
        if (this.cells != null) {
            System.out.println();
            for (int i = 0; i < this.cells[0].length; i++) {
                for (int i_10_ = 0; i_10_ < this.cells.length; i_10_++)
                    if (this.cells[i_10_][i] != null) System.out.print(this.cells[i_10_][i].getId());
                    else System.out.print("_");
                System.out.println();
            }
            System.out.println();
        }
    }

    public void reInitReferences() {
        if (this.cells != null) for (int i = 0; i < this.cells.length; i++)
            for (int i_11_ = 0; i_11_ < this.cells[0].length; i_11_++)
                if (this.cells[i][i_11_] != null) this.cells[i][i_11_] = this;
    }

    public CellObject(CellObject[][] cellobjects) {
        this.cells = cellobjects;
        this.reInitReferences();
    }

    public CellObject() {
        this.cells = new CellObject[1][1];
        this.cells[0][0] = this;
    }

    // TODO: CTO Mod
    // -------------------------------
    public void shrinkWidth(int i) {
        for (int j = this.getWidth() - i; j < this.getWidth(); j++)
            for (int k = 0; k < this.getHeight(); k++)
                this.cells[j][k] = this;

    }

    public void shrinkHeight(int i) {
        for (int j = this.getHeight() - i; j < this.getHeight(); j++)
            for (int k = 0; k < this.getWidth(); k++)
                this.cells[k][j] = this;

    }

    public void fillRectangle(int i, int j, int k, int l) {
        int i1 = i;
        int j1 = j;
        int k1 = k;
        int l1 = l;
        for (int i2 = i1; i2 <= k1; i2++)
            for (int j2 = j1; j2 <= l1; j2++)
                this.cells[i2][j2] = this;

    }
    // -------------------------------
}
