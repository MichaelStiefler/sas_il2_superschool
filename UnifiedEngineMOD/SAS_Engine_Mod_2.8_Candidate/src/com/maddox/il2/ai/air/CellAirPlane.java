/*Modified CellAirPlane class for the SAS Engine Mod*/

package com.maddox.il2.ai.air;

import java.io.Serializable;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;

public class CellAirPlane extends CellObject implements Serializable {
	private static final long serialVersionUID = 1L;
	public double ofsX;
	public double ofsY;

	public boolean checkAirFieldSize(CellAirField cellairfield) {
		if (cellairfield.getWidth() < getWidth()) return false;
		return cellairfield.getHeight() >= getHeight();
	}

	public boolean checkAirFieldCapacity(CellAirField cellairfield) {
		return cellairfield.calcFreeCells() < calcFilledCells() ? true : true;
	}

	public void blurSiluetUp() {
		if (getCells() == null) return;
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (getCells()[j][i] != this) continue;
				for (int k = i; k >= 0; k--)
					getCells()[j][k] = this;

			}

		}

	}

	public void blurSiluetDown() {
		if (getCells() == null) return;
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (getCells()[j][i] != this) continue;
				for (int k = i; k < getHeight(); k++)
					getCells()[j][k] = this;

			}

		}

	}

	public void blurSiluet4x() {
		if (getCells() == null) return;
		CellObject cellobject = getClone();
		cellobject.clearCells();
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (getCells()[j][i] != this) continue;
				cellobject.getCells()[j][i] = cellobject;
				if (j > 0) cellobject.getCells()[j - 1][i] = cellobject;
				if (j < getWidth() - 1) cellobject.getCells()[j + 1][i] = cellobject;
				if (i > 0) cellobject.getCells()[j][i - 1] = cellobject;
				if (i < getHeight() - 1) cellobject.getCells()[j][i + 1] = cellobject;
			}

		}

		clearCells();
		setCells(cellobject.getCells());
		reInitReferences();
	}

	public void blurSiluet8x() {
		if (getCells() == null) return;
		CellObject cellobject = getClone();
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (cellobject.getCells()[j][i] == null) continue;
				if (j > 0) getCells()[j - 1][i] = this;
				if (j < getWidth() - 1) getCells()[j + 1][i] = this;
				if (i > 0) getCells()[j][i - 1] = this;
				if (i < getHeight() - 1) getCells()[j][i + 1] = this;
				if (j > 0 && i > 0) getCells()[j - 1][i - 1] = this;
				if (j < getWidth() - 1 && i > 0) getCells()[j + 1][i - 1] = this;
				if (j > 0 && i < getHeight() - 1) getCells()[j - 1][i + 1] = this;
				if (j < getWidth() - 1 && i < getHeight() - 1) getCells()[j + 1][i + 1] = this;
			}

		}

	}

	public void clampCells() {
		CellObject acellobject[][] = getCells();
		if (acellobject == null) return;
		int i = getWidth();
		int j = getHeight();
		int k = 0, m = 0, n = 0, i1 = 0, i2 = 0, i3 = 0;
		for (m = 0; (m < i) && (k == 0); m++)
			for (n = 0; n < j; n++)
				if (acellobject[n][m] != null) {
					k = 1;
					break;
				}
		k = 0;
		for (n = i - 1; (n > 0) && (k == 0); n--)
			for (i1 = 0; i1 < j; i1++)
				if (acellobject[i1][n] != null) {
					k = 1;
					break;
				}
		k = 0;
		for (i1 = 0; (i1 < j) && (k == 0); i1++)
			for (i2 = 0; i2 < i; i2++)
				if (acellobject[i1][i2] != null) {
					k = 1;
					break;
				}
		k = 0;
		for (i2 = j - 1; (i2 > 0) && (k == 0); i2--)
			for (i3 = 0; i3 < i; i3++)
				if (acellobject[i2][i3] != null) {
					k = 1;
					break;
				}
		if ((m == 0) && (m == i - 1) && (i1 == 0) && (i2 == j - 1)) return;
		if ((m > n) || (i1 > i2)) {
			return;
		}
		CellObject[][] acellobject2 = new CellObject[i2 - i1 + 1][n - m + 1];
		for (int i4 = 0; i4 < i2 - i1 + 1; i4++) {
			for (int i5 = 0; i5 < n - m + 1; i5++) {
				if (acellobject[(i4 + i1)][(i5 + m)] != null) acellobject2[i4][i5] = this;
			}
		}
		this.ofsY -= m * getCellSize();
		this.ofsX -= i1 * getCellSize();
		setCells(acellobject2);
	}

	public void initCellsThroughCollision(HierMesh hiermesh, Loc loc, double d) {
		Point3d point3d = new Point3d();
		Point3d point3d1 = new Point3d();
		setCellSize(d);
		int i = (int) ((double) (hiermesh.collisionR() * 2.0F) / d) + 6;
		CellObject acellobject[][] = new CellObject[i][i];
		for (int j = 0; j < i; j++) {
			for (int k = 0; k < i; k++) {
				double d2 = (loc.getY() + (double) (i / 2 - j) * d) - d * 0.5D;
				double d1 = (loc.getX() + (double) (i / 2 - k) * d) - d * 0.5D;
				boolean flag = false;
				for (int l = -1; l <= 1 && !flag; l++) {
					for (int i1 = -1; (i1 <= 1) && !flag; i1++) {
						point3d.set(d1 + (double) l * d * 0.40000000000000002D, d2 + (double) i1 * d * 0.40000000000000002D, loc.getZ() + (double) hiermesh.collisionR());
						point3d1.set(d1 + (double) l * d * 0.40000000000000002D, d2 + (double) i1 * d * 0.40000000000000002D, loc.getZ() - (double) hiermesh.collisionR());
						float f = hiermesh.detectCollisionLine(loc, point3d, point3d1);
						if (f > 0.0F && f < 1.0F) {
							flag = true;
							break;
						}
					}
				}

				if (flag) acellobject[j][k] = this;
				else acellobject[j][k] = null;
			}

		}

		ofsX = ofsY = (double) (i / 2) * d;
		setCells(acellobject);
	}

	public CellObject getClone() {
		if (getCells() == null) return null;
		CellObject acellobject[][] = new CellObject[getWidth()][getHeight()];
		for (int i = 0; i < getCells().length; i++) {
			for (int j = 0; j < getCells()[0].length; j++)
				acellobject[i][j] = getCells()[i][j];

		}

		CellAirPlane cellairplane = new CellAirPlane(acellobject);
		cellairplane.setCellSize(getCellSize());
		cellairplane.ofsX = ofsX;
		cellairplane.ofsY = ofsY;
		return cellairplane;
	}

	public CellAirPlane(CellObject[][] cellobjects) {
		super(cellobjects);

		// TODO: CTO Mod
		// -------------------------------
		iFoldedWidth = -1;
		// -------------------------------
	}

	public CellAirPlane(CellObject[][] cellobjects, HierMesh hiermesh, Loc loc, double d) {
		super(cellobjects);
		initCellsThroughCollision(hiermesh, loc, d);

		// TODO: CTO Mod
		// -------------------------------
		iFoldedWidth = -1;
		// -------------------------------
	}

	// TODO: CTO Mod
	// -------------------------------
	public void setFoldedWidth(int i) {
		iFoldedWidth = i;
	}

	public int getWidth() {
		if (iFoldedWidth > 0) return iFoldedWidth;
		else return super.getWidth();
	}

	// -------------------------------

	// TODO: CTO Mod
	// -------------------------------
	public boolean checkAirFieldSizeCarrier(com.maddox.il2.ai.air.CellAirField cellairfield) {
		if (cellairfield.getWidth() < getWidth()) return false;
		else return cellairfield.getHeight() >= getHeight() + 1;
	}

	// -------------------------------

	// TODO: CTO Mod
	// -------------------------------
	private int iFoldedWidth;
	// -------------------------------
}
