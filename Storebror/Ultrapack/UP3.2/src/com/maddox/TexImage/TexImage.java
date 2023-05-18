package com.maddox.TexImage;

import com.maddox.rts.SFSInputStream;
import java.io.*;

public class TexImage
{

    public TexImage()
    {
        set();
    }

    public TexImage(int i, int j, int k)
    {
        set(i, j, k);
    }

    public TexImage(TexImage teximage)
    {
        set(teximage);
    }

    public TexImage(TexImage teximage, int i, int j, int k, int l)
    {
        set(teximage, i, j, k, l);
    }

    public void set()
    {
        type = sx = sy = 0;
        image = null;
    }

    public void set(int i, int j, int k)
    {
        set();
        switch(k)
        {
        default:
            return;

        case 1:
            type = 6409;
            // fall through

        case 6406: 
        case 6409: 
            BPP = 1;
            break;

        case 2:
            type = 32854;
            // fall through

        case 32848: 
        case 32854: 
        case 32855: 
            BPP = 2;
            break;

        case 3:
            type = 6407;
            // fall through

        case 6407: 
        case 32849: 
            BPP = 3;
            break;

        case 4:
            type = 6408;
            // fall through

        case 6408: 
        case 32856: 
            BPP = 4;
            break;
        }
        if(type == 0)
            type = k;
        sx = i;
        sy = j;
        image = new byte[sx * sy * BPP];
    }

    public void set(TexImage teximage)
    {
        type = teximage.type;
        Palette = teximage.Palette;
        BytesPerEntry = teximage.BytesPerEntry;
        BPP = teximage.BPP;
        sx = teximage.sx;
        sy = teximage.sy;
        if(sx == 0 || sy == 0 || teximage.image == null)
            return;
        image = new byte[sx * sy * BPP];
        for(int i = 0; i < sy; i++)
            System.arraycopy(teximage.image, i * sx * BPP, image, i * sx * BPP, sx * BPP);

    }

    public void set(TexImage teximage, int i, int j, int k, int l)
    {
        type = teximage.type;
        Palette = teximage.Palette;
        BytesPerEntry = teximage.BytesPerEntry;
        BPP = teximage.BPP;
        sx = k;
        sy = l;
        if(sx == 0 || sy == 0 || teximage.image == null)
            return;
        image = new byte[k * l * BPP];
        for(int i1 = 0; i1 < l; i1++)
            System.arraycopy(teximage.image, ((j + i1) * teximage.sx + i) * BPP, image, i1 * sx * BPP, sx * BPP);

    }

    public final byte I(int i, int j)
    {
        return image(i, j);
    }

    public final byte R(int i, int j)
    {
        return image[(j * sx + i) * BPP];
    }

    public final byte G(int i, int j)
    {
        return image[(j * sx + i) * BPP + 1];
    }

    public final byte B(int i, int j)
    {
        return image[(j * sx + i) * BPP + 2];
    }

    public final byte A(int i, int j)
    {
        return image[(j * sx + i) * BPP + 3];
    }

    public final int intI(int i, int j)
    {
        return image(i, j) & 0xff;
    }

    public final int intR(int i, int j)
    {
        return image[(j * sx + i) * BPP] & 0xff;
    }

    public final int intG(int i, int j)
    {
        return image[(j * sx + i) * BPP + 1] & 0xff;
    }

    public final int intB(int i, int j)
    {
        return image[(j * sx + i) * BPP + 2] & 0xff;
    }

    public final int intA(int i, int j)
    {
        return image[(j * sx + i) * BPP + 3] & 0xff;
    }

    public final void getPixel(int i, int j, byte abyte0[])
    {
        int k = (j * sx + i) * BPP;
        if(k < 0 || k + BPP >= image.length)
            switch(BPP)
            {
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
        switch(BPP)
        {
        case 4:
            abyte0[3] = image[k + 3];
            // fall through

        case 3:
            abyte0[2] = image[k + 2];
            // fall through

        case 2:
            abyte0[1] = image[k + 1];
            // fall through

        case 1:
            abyte0[0] = image[k];
            // fall through

        default:
            return;
        }
    }

    public final void I(int i, int j, int k)
    {
        image(i, j, k);
    }

    public final void R(int i, int j, int k)
    {
        image[(j * sx + i) * BPP] = (byte)k;
    }

    public final void G(int i, int j, int k)
    {
        image[(j * sx + i) * BPP + 1] = (byte)k;
    }

    public final void B(int i, int j, int k)
    {
        image[(j * sx + i) * BPP + 2] = (byte)k;
    }

    public final void A(int i, int j, int k)
    {
        image[(j * sx + i) * BPP + 3] = (byte)k;
    }

    public final byte image(int i, int j)
    {
        return image[j * sx * BPP + i];
    }

    public final int intI(float f, float f1)
    {
        if(f < 0.0F)
            f = 0.0F;
        else
        if(f > (float)(sx - 2))
            f = sx - 2;
        if(f1 < 0.0F)
            f1 = 0.0F;
        else
        if(f1 > (float)(sy - 2))
            f1 = sy - 2;
        if(f <= -1F && f1 <= -1F)
        {
            throw new RuntimeException("TexImage.intI(NaN,NaN);");
        } else
        {
            int i = (int)f;
            int j = (int)f1 * sx * BPP;
            int k = image[j + i] & 0xff;
            int l = image[j + i + 1] & 0xff;
            int i1 = image[j + sx + i] & 0xff;
            int j1 = image[j + sx + i + 1] & 0xff;
            float f2 = f % 1.0F;
            k += (int)((float)(l - k) * f2);
            i1 += (int)((float)(j1 - i1) * f2);
            f2 = f1 % 1.0F;
            return k + (int)((float)(i1 - k) * f2);
        }
    }

    public final void image(int i, int j, int k)
    {
        image[j * sx * BPP + i] = (byte)k;
    }

    public final void averageColor(float af[])
    {
        if(sx * sy <= 0)
            return;
        float af1[] = af;
        if(BPP > 0)
            af1[0] = 0.0F;
        if(BPP > 1)
            af1[1] = 0.0F;
        if(BPP > 2)
            af1[2] = 0.0F;
        if(BPP > 3)
            af1[3] = 0.0F;
        for(int j = 0; j < sy; j++)
        {
            for(int i = 0; i < sx; i++)
                if(BPP >= 1)
                {
                    int k;
                    af1[0] += image[k = (j * sx + i) * BPP] & 0xff;
                    if(BPP > 1)
                        af1[1] += image[k + 1] & 0xff;
                    if(BPP > 2)
                        af1[2] += image[k + 2] & 0xff;
                    if(BPP > 3)
                        af1[3] += image[k + 3] & 0xff;
                }

        }

        float f = 0.003921569F / (float)(sx * sy);
        if(BPP > 0)
            af1[0] *= f;
        if(BPP > 1)
            af1[1] *= f;
        if(BPP > 2)
            af1[2] *= f;
        if(BPP > 3)
            af1[3] *= f;
    }

    public void scaleHalf()
        throws Exception
    {
        TexImage teximage;
        int j1;
        int k1;
        switch(type)
        {
        default:
            throw new Exception("scaleHalf(): type of image not supported");

        case 6407: 
        case 6408: 
        case 32849: 
        case 32856: 
            j1 = sx / 2;
            k1 = sy / 2;
            teximage = new TexImage(this, 0, 0, j1, k1);
            int k2;
            for(int l = k2 = 0; l < k1;)
            {
                int l1;
                for(int i = l1 = 0; i < j1;)
                {
                    teximage.R(i, l, intR(l1, k2) + intR(l1 + 1, k2) + intR(l1, k2 + 1) + intR(l1 + 1, k2 + 1) >> 2);
                    teximage.G(i, l, intG(l1, k2) + intG(l1 + 1, k2) + intG(l1, k2 + 1) + intG(l1 + 1, k2 + 1) >> 2);
                    teximage.B(i, l, intB(l1, k2) + intB(l1 + 1, k2) + intB(l1, k2 + 1) + intB(l1 + 1, k2 + 1) >> 2);
                    i++;
                    l1 += 2;
                }

                if(BPP > 3)
                {
                    int i2;
                    for(int j = i2 = 0; j < j1;)
                    {
                        teximage.A(j, l, intA(i2, k2) + intA(i2 + 1, k2) + intA(i2, k2 + 1) + intA(i2 + 1, k2 + 1) >> 2);
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
            j1 = sx / 2;
            k1 = sy / 2;
            teximage = new TexImage(this, 0, 0, j1, k1);
            int l2;
            for(int i1 = l2 = 0; i1 < k1;)
            {
                int j2;
                for(int k = j2 = 0; k < j1;)
                {
                    teximage.I(k, i1, intI(j2, l2) + intI(j2 + 1, l2) + intI(j2, l2 + 1) + intI(j2 + 1, l2 + 1) >> 2);
                    k++;
                    j2 += 2;
                }

                i1++;
                l2 += 2;
            }

            break;
        }
        image = teximage.image;
        teximage.image = null;
        sx = j1;
        sy = k1;
    }

    public void LoadTGA(InputStream inputstream)
        throws Exception
    {
        TexImageTGA teximagetga = new TexImageTGA();
        try
        {
            teximagetga.Load(inputstream, this);
        }
        catch(Exception exception)
        {
            type = sx = sy = 0;
            image = null;
            throw exception;
        }
    }

    public void SaveTGA(OutputStream outputstream)
        throws Exception
    {
        TexImageTGA teximagetga = new TexImageTGA();
        teximagetga.Save(outputstream, this);
    }

    public void LoadTGA(String s)
        throws Exception
    {
        type = sx = sy = 0;
        image = null;
        
        // TODO: 4.13.3 True Color Skin issues fix
//        SFSInputStream sfsinputstream = new SFSInputStream(s);
//        LoadTGA(((InputStream) (sfsinputstream)));

        PrintDebugMessage("TexImage LoadTGA(" + s + ")");
        
        SFSInputStream sfsinputstream = null;
        try {
            sfsinputstream = new SFSInputStream(s);
            LoadTGA(((InputStream) (sfsinputstream)));
            sfsinputstream.close();
            sfsinputstream = null;
        } catch (Exception e) {
            // Don't dump this exception to the log, it's normal to occur
            // when true color plane skins are selected since the native
            // Texture creation from TexImageTGA class has to fail on
            // the first attempt in that case.
//            e.printStackTrace();
            throw e;
        }
        finally {
            if (sfsinputstream != null)
                try {
                    sfsinputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }       
    }

    public void SaveTGA(String s)
        throws Exception
    {
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
        try
        {
            fileoutputstream = new FileOutputStream(s);
            SaveTGA(((OutputStream) (fileoutputstream)));
            fileoutputstream.close();
            fileoutputstream = null;
        }
        catch(Exception exception)
        {
            if (fileoutputstream != null)
                fileoutputstream.close();
            File file = new File(s);
            file.delete();
            throw exception;
        }
        finally {
            if (fileoutputstream != null)
                try {
                    fileoutputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }       
    }

    public String toString()
    {
        String s;
        switch(type)
        {
        default:
            s = "Unknown" + type;
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
        return new String("TexImage[" + sx + "x" + sy + "," + s + ",BPP=" + BPP + "]");
    }

    public void hicolorDither()
    {
        if(BPP != 3)
            return;
        if(error.length < sx)
            error = new int[sx];
        for(int i = 0; i < BPP; i++)
        {
            int j2 = hiMask[i];
            int k2 = erMask[i];
            for(int j = 0; j < sx; j++)
                error[j] = 0;

            int i1 = i;
            for(int l = 0; l < sy; l++)
            {
                int j1 = i1;
                int i2;
                int k1 = i2 = 0;
                for(int k = 0; k < sx; k++)
                {
                    int l1 = (image[j1] & 0xff) + k1 + error[k];
                    if(l1 > 255)
                        l1 = 255;
                    image[j1] = (byte)(l1 & j2);
                    k1 = l1 &= k2;
                    error[k] = (k1 = k1 * 3 >> 3) + i2;
                    i2 = (l1 - k1 - k1) + (0x4b412c96 >> RND & 1);
                    if(++RND >= 31)
                        RND = 0;
                    j1 += BPP;
                }

                i1 += sx * BPP;
            }

        }

    }

/*
 * TODO: Unnecessary main method removed. This seems to be a 
 *       leftover from early days of IL-2 development.
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
    
    private static final boolean _DEBUG = false;

    public int type;
    public byte Palette[];
    public int BytesPerEntry;
    public int BPP;
    public int sx;
    public int sy;
    public byte image[];
    public static final int ALPHA = 6406;
    public static final int COLOR_INDEX = 6400;
    public static final int COLOR_INDEX8_EXT = 32997;
    public static final int LUMINANCE = 6409;
    public static final int RGB = 6407;
    public static final int RGB5 = 32848;
    public static final int RGB8 = 32849;
    public static final int RGBA = 6408;
    public static final int RGBA4 = 32854;
    public static final int RGB5_A1 = 32855;
    public static final int RGBA8 = 32856;
    private static int hiMask[] = {
        248, 252, 248
    };
    private static int erMask[] = {
        7, 3, 7
    };
    private static int RND;
    private static int error[] = new int[256];

}
