// Source File Name: Supersonic.java
// Author:           Storebror
package com.maddox.il2.fm;

import com.maddox.rts.SectFile;

public class Supersonic {
	public float VminMach;
	public float VsoundMach1;
	public float CfsoundMach1;
	public float VsoundMach2;
	public float CfsoundMach2;
	public float VmaxMach;
	public float CfmaxMach;
	public boolean allParamsSet;

	// Mach Drag Coefficient Factor tables
	private float[] fDragFactorX = { 0.95F, 0.99F, 1.1F, 2.5F }; // Mach Values
	private float[] fDragFactorY = { 1.0F, 5.0F, 5.0F, 1.5F }; // Cw Values

	public Supersonic() {
		this.fDragFactorX[0] = this.VminMach = 0.95F;
		this.fDragFactorY[0] = 1.0F;
		this.fDragFactorX[1] = this.VsoundMach1 = 0.99F;
		this.fDragFactorY[1] = this.CfsoundMach1 = 5.0F;
		this.fDragFactorX[2] = this.VsoundMach2 = 1.1F;
		this.fDragFactorY[2] = this.CfsoundMach2 = 5.0F;
		this.fDragFactorX[3] = this.VmaxMach = 2.5F;
		this.fDragFactorY[3] = this.CfmaxMach = 1.5F;
		this.allParamsSet = false;

	}

	public Supersonic(SectFile theSectFile) {
		this.load(theSectFile);
	}

	public final void load(SectFile theSectFile) {
		String strSection = "Supersonic";
		this.fDragFactorX[0] = this.VminMach = theSectFile.get(strSection, "VminMach", 0.95F);
		this.fDragFactorX[1] = this.VsoundMach1 = theSectFile.get(strSection, "VsoundMach1", 0.99F);
		this.fDragFactorY[1] = this.CfsoundMach1 = theSectFile.get(strSection, "CfsoundMach1", 5.0F);
		this.fDragFactorX[2] = this.VsoundMach2 = theSectFile.get(strSection, "VsoundMach2", 1.1F);
		this.fDragFactorY[2] = this.CfsoundMach2 = theSectFile.get(strSection, "CfsoundMach2", 5.0F);
		this.fDragFactorX[3] = this.VmaxMach = theSectFile.get(strSection, "VmaxMach", 2.5F);
		this.fDragFactorY[3] = this.CfmaxMach = theSectFile.get(strSection, "CfmaxMach", 1.5F);
		this.allParamsSet = theSectFile.exist(strSection, "VminMach") && theSectFile.exist(strSection, "VsoundMach1") && theSectFile.exist(strSection, "CfsoundMach1") && theSectFile.exist(strSection, "VsoundMach2")
				&& theSectFile.exist(strSection, "CfsoundMach2") && theSectFile.exist(strSection, "VmaxMach") && theSectFile.exist(strSection, "CfmaxMach");
	}

	public float getDragFactorForMach(float theMachValue) {
		if (theMachValue <= this.VminMach) return 1.0F;
		if (theMachValue >= this.VmaxMach) return this.CfmaxMach;

		int i = 0;
		for (i = 0; i < fDragFactorX.length - 1; i++) {
			if (fDragFactorX[i] > theMachValue) break;
		}
		if (i == 0) return fDragFactorY[0];
		float baseFactor = fDragFactorY[i - 1];
		float spanFactor = fDragFactorY[i] - baseFactor;
		float baseMach = fDragFactorX[i - 1];
		float spanMach = fDragFactorX[i] - baseMach;
		float spanMult = (theMachValue - baseMach) / spanMach;
		return baseFactor + (spanFactor * spanMult);
	}

}