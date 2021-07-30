package com.maddox.TexImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.maddox.rts.SFSInputStream;

public class TexImage {

    public TexImage() {
        this.set();
    }

    public TexImage(int i, int j, int k) {
        this.set(i, j, k);
    }

    public TexImage(TexImage teximage) {
        this.set(teximage);
    }

    public TexImage(TexImage teximage, int i, int j, int k, int l) {
        this.set(teximage, i, j, k, l);
    }

    public void set() {
        this.type = this.sx = this.sy = 0;
        this.image = null;
    }

    public void set(int i, int j, int k) {
        this.set();
        switch (k) {
            default:
                return;

            case 1:
                this.type = 6409;
                // fall through

            case 6406:
            case 6409:
                this.BPP = 1;
                break;

            case 2:
                this.type = 32854;
                // fall through

            case 32848:
            case 32854:
            case 32855:
                this.BPP = 2;
                break;

            case 3:
                this.type = 6407;
                // fall through

            case 6407:
            case 32849:
                this.BPP = 3;
                break;

            case 4:
                this.type = 6408;
                // fall through

            case 6408:
            case 32856:
                this.BPP = 4;
                break;
        }
        if (this.type == 0) this.type = k;
        this.sx = i;
        this.sy = j;
        this.image = new byte[this.sx * this.sy * this.BPP];
    }

    public void set(TexImage teximage) {
        this.type = teximage.type;
        this.Palette = teximage.Palette;
        this.BytesPerEntry = teximage.BytesPerEntry;
        this.BPP = teximage.BPP;
        this.sx = teximage.sx;
        this.sy = teximage.sy;
        if (this.sx == 0 || this.sy == 0 || teximage.image == null) return;
        this.image = new byte[this.sx * this.sy * this.BPP];
        for (int i = 0; i < this.sy; i++)
            System.arraycopy(teximage.image, i * this.sx * this.BPP, this.image, i * this.sx * this.BPP, this.sx * this.BPP);

    }

    public void set(TexImage teximage, int i, int j, int k, int l) {
        this.type = teximage.type;
        this.Palette = teximage.Palette;
        this.BytesPerEntry = teximage.BytesPerEntry;
        this.BPP = teximage.BPP;
        this.sx = k;
        this.sy = l;
        if (this.sx == 0 || this.sy == 0 || teximage.image == null) return;
        this.image = new byte[k * l * this.BPP];
        for (int i1 = 0; i1 < l; i1++)
            System.arraycopy(teximage.image, ((j + i1) * teximage.sx + i) * this.BPP, this.image, i1 * this.sx * this.BPP, this.sx * this.BPP);

    }

    public final byte I(int i, int j) {
        return this.image(i, j);
    }

    public final byte R(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP];
    }

    public final byte G(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP + 1];
    }

    public final byte B(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP + 2];
    }

    public final byte A(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP + 3];
    }

    public final int intI(int i, int j) {
        return this.image(i, j) & 0xff;
    }

    public final int intR(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP] & 0xff;
    }

    public final int intG(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP + 1] & 0xff;
    }

    public final int intB(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP + 2] & 0xff;
    }

    public final int intA(int i, int j) {
        return this.image[(j * this.sx + i) * this.BPP + 3] & 0xff;
    }

    public final void getPixel(int i, int j, byte abyte0[]) {
        int k = (j * this.sx + i) * this.BPP;
        if (k < 0 || k + this.BPP >= this.image.length) switch (this.BPP) {
            case 4:
                abyte0[3] = 25;
                // fall through

            case 3:
                abyte0[2] = 25;
                // fall through

            case 2:
                abyte0[1] = 25;
                // fall through

            case 1:
                abyte0[0] = 25;
                // fall through

            default:
                return;
        }
        switch (this.BPP) {
            case 4:
                abyte0[3] = this.image[k + 3];
                // fall through

            case 3:
                abyte0[2] = this.image[k + 2];
                // fall through

            case 2:
                abyte0[1] = this.image[k + 1];
                // fall through

            case 1:
                abyte0[0] = this.image[k];
                // fall through

            default:
                return;
        }
    }

    public final void I(int i, int j, int k) {
        this.image(i, j, k);
    }

    public final void R(int i, int j, int k) {
        this.image[(j * this.sx + i) * this.BPP] = (byte) k;
    }

    public final void G(int i, int j, int k) {
        this.image[(j * this.sx + i) * this.BPP + 1] = (byte) k;
    }

    public final void B(int i, int j, int k) {
        this.image[(j * this.sx + i) * this.BPP + 2] = (byte) k;
    }

    public final void A(int i, int j, int k) {
        this.image[(j * this.sx + i) * this.BPP + 3] = (byte) k;
    }

    public final byte image(int i, int j) {
        return this.image[j * this.sx * this.BPP + i];
    }

    public final int intI(float f, float f1) {
        if (f < 0.0F) f = 0.0F;
        else if (f > this.sx - 2) f = this.sx - 2;
        if (f1 < 0.0F) f1 = 0.0F;
        else if (f1 > this.sy - 2) f1 = this.sy - 2;
        if (f <= -1F && f1 <= -1F) throw new RuntimeException("TexImage.intI(NaN,NaN);");
        else {
            int i = (int) f;
            int j = (int) f1 * this.sx * this.BPP;
            int k = this.image[j + i] & 0xff;
            int l = this.image[j + i + 1] & 0xff;
            int i1 = this.image[j + this.sx + i] & 0xff;
            int j1 = this.image[j + this.sx + i + 1] & 0xff;
            float f2 = f % 1.0F;
            k += (int) ((l - k) * f2);
            i1 += (int) ((j1 - i1) * f2);
            f2 = f1 % 1.0F;
            return k + (int) ((i1 - k) * f2);
        }
    }

    public final void image(int i, int j, int k) {
        this.image[j * this.sx * this.BPP + i] = (byte) k;
    }

    public final void averageColor(float af[]) {
        if (this.sx * this.sy <= 0) return;
        float af1[] = af;
        if (this.BPP > 0) af1[0] = 0.0F;
        if (this.BPP > 1) af1[1] = 0.0F;
        if (this.BPP > 2) af1[2] = 0.0F;
        if (this.BPP > 3) af1[3] = 0.0F;
        for (int j = 0; j < this.sy; j++)
            for (int i = 0; i < this.sx; i++)
                if (this.BPP >= 1) {
                    int k;
                    af1[0] += this.image[k = (j * this.sx + i) * this.BPP] & 0xff;
                    if (this.BPP > 1) af1[1] += this.image[k + 1] & 0xff;
                    if (this.BPP > 2) af1[2] += this.image[k + 2] & 0xff;
                    if (this.BPP > 3) af1[3] += this.image[k + 3] & 0xff;
                }

        float f = 0.003921569F / (this.sx * this.sy);
        if (this.BPP > 0) af1[0] *= f;
        if (this.BPP > 1) af1[1] *= f;
        if (this.BPP > 2) af1[2] *= f;
        if (this.BPP > 3) af1[3] *= f;
    }

    public void scaleHalf() throws Exception {
        TexImage teximage;
        int j1;
        int k1;
        switch (this.type) {
            default:
                throw new Exception("scaleHalf(): type of image not supported");

            case 6407:
            case 6408:
            case 32849:
            case 32856:
                j1 = this.sx / 2;
                k1 = this.sy / 2;
                teximage = new TexImage(this, 0, 0, j1, k1);
                int k2;
                for (int l = k2 = 0; l < k1;) {
                    int l1;
                    for (int i = l1 = 0; i < j1;) {
                        teximage.R(i, l, this.intR(l1, k2) + this.intR(l1 + 1, k2) + this.intR(l1, k2 + 1) + this.intR(l1 + 1, k2 + 1) >> 2);
                        teximage.G(i, l, this.intG(l1, k2) + this.intG(l1 + 1, k2) + this.intG(l1, k2 + 1) + this.intG(l1 + 1, k2 + 1) >> 2);
                        teximage.B(i, l, this.intB(l1, k2) + this.intB(l1 + 1, k2) + this.intB(l1, k2 + 1) + this.intB(l1 + 1, k2 + 1) >> 2);
                        i++;
                        l1 += 2;
                    }

                    if (this.BPP > 3) {
                        int i2;
                        for (int j = i2 = 0; j < j1;) {
                            teximage.A(j, l, this.intA(i2, k2) + this.intA(i2 + 1, k2) + this.intA(i2, k2 + 1) + this.intA(i2 + 1, k2 + 1) >> 2);
                            j++;
                            i2 += 2;
                        }

                    }
                    l++;
                    k2 += 2;
                }

                break;

            case 6406:
            case 6409:
                j1 = this.sx / 2;
                k1 = this.sy / 2;
                teximage = new TexImage(this, 0, 0, j1, k1);
                int l2;
                for (int i1 = l2 = 0; i1 < k1;) {
                    int j2;
                    for (int k = j2 = 0; k < j1;) {
                        teximage.I(k, i1, this.intI(j2, l2) + this.intI(j2 + 1, l2) + this.intI(j2, l2 + 1) + this.intI(j2 + 1, l2 + 1) >> 2);
                        k++;
                        j2 += 2;
                    }

                    i1++;
                    l2 += 2;
                }

                break;
        }
        this.image = teximage.image;
        teximage.image = null;
        this.sx = j1;
        this.sy = k1;
    }

    public void LoadTGA(InputStream inputstream) throws Exception {
        TexImageTGA teximagetga = new TexImageTGA();
        try {
            teximagetga.Load(inputstream, this);
        } catch (Exception exception) {
            this.type = this.sx = this.sy = 0;
            this.image = null;
            throw exception;
        }
    }

    public void SaveTGA(OutputStream outputstream) throws Exception {
        TexImageTGA teximagetga = new TexImageTGA();
        teximagetga.Save(outputstream, this);
    }

    public void LoadTGA(String s) throws Exception {
        this.type = this.sx = this.sy = 0;
        this.image = null;

        // TODO: 4.13.3 True Color Skin issues fix
//        SFSInputStream sfsinputstream = new SFSInputStream(s);
//        LoadTGA(((InputStream) (sfsinputstream)));

        PrintDebugMessage("TexImage LoadTGA(" + s + ")");

        SFSInputStream sfsinputstream = null;
        try {
            sfsinputstream = new SFSInputStream(s);
            this.LoadTGA(sfsinputstream);
            sfsinputstream.close();
            sfsinputstream = null;
        } catch (Exception e) {
            // Don't dump this exception to the log, it's normal to occur
            // when true color plane skins are selected since the native
            // Texture creation from TexImageTGA class has to fail on
            // the first attempt in that case.
//            e.printStackTrace();
            throw e;
        } finally {
            if (sfsinputstream != null) try {
                sfsinputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveTGA(String s) throws Exception {
        // TODO: 4.13.3 True Color Skin issues fix

//        FileOutputStream fileoutputstream = new FileOutputStream(s);
//        try
//        {
//            SaveTGA(((OutputStream) (fileoutputstream)));
//        }
//        catch(Exception exception)
//        {
//            fileoutputstream.close();
//            File file = new File(s);
//            file.delete();
//            throw exception;
//        }

        PrintDebugMessage("TexImage SaveTGA(" + s + ")");

        FileOutputStream fileoutputstream = null;
        try {
            fileoutputstream = new FileOutputStream(s);
            this.SaveTGA(fileoutputstream);
            fileoutputstream.close();
            fileoutputstream = null;
        } catch (Exception exception) {
            if (fileoutputstream != null) fileoutputstream.close();
            File file = new File(s);
            file.delete();
            throw exception;
        } finally {
            if (fileoutputstream != null) try {
                fileoutputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        String s;
        switch (this.type) {
            default:
                s = "Unknown" + this.type;
                break;

            case 6406:
                s = "ALPHA";
                break;

            case 6400:
                s = "COLOR_INDEX";
                break;

            case 32997:
                s = "COLOR_INDEX8_EXT";
                break;

            case 6409:
                s = "LUMINANCE";
                break;

            case 6407:
                s = "RGB";
                break;

            case 32848:
                s = "RGB5";
                break;

            case 32849:
                s = "RGB8";
                break;

            case 6408:
                s = "RGBA";
                break;

            case 32854:
                s = "RGBA4";
                break;

            case 32855:
                s = "RGB5_A1";
                break;

            case 32856:
                s = "RGBA8";
                break;
        }
        return new String("TexImage[" + this.sx + "x" + this.sy + "," + s + ",BPP=" + this.BPP + "]");
    }

    public void hicolorDither() {
        if (this.BPP != 3) return;
        if (error.length < this.sx) error = new int[this.sx];
        for (int i = 0; i < this.BPP; i++) {
            int j2 = hiMask[i];
            int k2 = erMask[i];
            for (int j = 0; j < this.sx; j++)
                error[j] = 0;

            int i1 = i;
            for (int l = 0; l < this.sy; l++) {
                int j1 = i1;
                int i2;
                int k1 = i2 = 0;
                for (int k = 0; k < this.sx; k++) {
                    int l1 = (this.image[j1] & 0xff) + k1 + error[k];
                    if (l1 > 255) l1 = 255;
                    this.image[j1] = (byte) (l1 & j2);
                    k1 = l1 &= k2;
                    error[k] = (k1 = k1 * 3 >> 3) + i2;
                    i2 = l1 - k1 - k1 + (0x4b412c96 >> RND & 1);
                    if (++RND >= 31) RND = 0;
                    j1 += this.BPP;
                }

                i1 += this.sx * this.BPP;
            }

        }

    }

    /*
     * TODO: Unnecessary main method removed. This seems to be a leftover from early days of IL-2 development.
     */

//    public static void main(String args[])
//        throws Exception
//    {
//        TexImage teximage = new TexImage();
//        teximage.LoadTGA(args.length <= 0 ? "Test.TGA" : args[0]);
//        System.out.println(teximage);
//        System.out.println("OK");
//    }

    // TODO: Added Debugging capability here.
    private static void PrintDebugMessage(String s) {
        if (_DEBUG) System.out.println(s);
    }

    private static final boolean _DEBUG           = false;

    public int                   type;
    public byte                  Palette[];
    public int                   BytesPerEntry;
    public int                   BPP;
    public int                   sx;
    public int                   sy;
    public byte                  image[];
    public static final int      ALPHA            = 6406;
    public static final int      COLOR_INDEX      = 6400;
    public static final int      COLOR_INDEX8_EXT = 32997;
    public static final int      LUMINANCE        = 6409;
    public static final int      RGB              = 6407;
    public static final int      RGB5             = 32848;
    public static final int      RGB8             = 32849;
    public static final int      RGBA             = 6408;
    public static final int      RGBA4            = 32854;
    public static final int      RGB5_A1          = 32855;
    public static final int      RGBA8            = 32856;
    private static int           hiMask[]         = { 248, 252, 248 };
    private static int           erMask[]         = { 7, 3, 7 };
    private static int           RND;
    private static int           error[]          = new int[256];

}
