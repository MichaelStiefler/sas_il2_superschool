package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;

public class PaintSchemeBCSPar03 extends PaintSchemeBMPar03 {

    public PaintSchemeBCSPar03() {
    }

    public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k) {
        if (regiment.country() == PaintScheme.countryUSA) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countryUSABlue) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        } else {
            return super.typedNameNum(class1, regiment, i, j, k);
        }
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
        super.prepareNum(class1, hiermesh, regiment, i, j, k);
        int l = regiment.gruppeNumber() - 1;
        if (regiment.country() == PaintScheme.countryPoland) {
            this.changeMat(hiermesh, "Overlay1", "psBCS03POLCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay2", "polishcheckerboard", "Polish/checkerboard.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryRussia) {
            this.changeMat(hiermesh, "Overlay1", "psBCS03RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2], PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
            this.changeMat(class1, hiermesh, "Overlay2", "psAVGRUSMARKcolor" + i, "mark.tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
            this.changeMat(class1, hiermesh, "Overlay6", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryUSA) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "psBCS03USAREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBCS03USAREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBCS03USALNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", 0.960784F, 0.745098F, 0.145098F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "psBCS03USARNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, 0.960784F, 0.745098F, 0.145098F);
            this.changeMat(class1, hiermesh, "Overlay6", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryUSABlue) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "psBCS03USAREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBCS03USAREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBCS03USALNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", 0.960784F, 0.745098F, 0.145098F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "psBCS03USARNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, 0.960784F, 0.745098F, 0.145098F);
            this.changeMat(class1, hiermesh, "Overlay6", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBulgariaAllied) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNorthKoreaRed) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPRC) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryChinaRed) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNorthKorea) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            return;
        } else {
            return;
        }
    }

    public void prepareNumOff(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
        if (regiment.country() == PaintScheme.countryRussia) {
            this.changeMat(class1, hiermesh, "Overlay2", "psAVGRUSMARKcolor" + i, "mark.tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
        }
    }
}
