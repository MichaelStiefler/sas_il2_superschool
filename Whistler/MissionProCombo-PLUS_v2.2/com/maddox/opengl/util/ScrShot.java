package com.maddox.opengl.util;

import com.maddox.TexImage.TexImage;
import com.maddox.il2.engine.Config;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.gl;
import com.maddox.rts.HomePath;
import com.maddox.rts.IniFile;
import com.sun.image.codec.jpeg.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScrShot
{

    private void swapRB()
    {
        int i = dx * 3;
        for(int j = 0; j < dy; j++)
        {
            for(int k = 0; k < i; k += 3)
            {
                byte byte0 = img.image(k, j);
                byte byte1 = img.image(k + 2, j);
                img.image(k, j, byte1);
                img.image(k + 2, j, byte0);
            }
        }
    }

    public void grab()
    {
        dx = GLContext.getCurrent().width();
        dy = GLContext.getCurrent().height();
        if(Config.cur.screenshotType != 1)
            gl.ReadPixels(0, 0, dx, dy, 32992, 5121, img.image);
        else
            gl.ReadPixels(0, 0, dx, dy, 6407, 5121, img.image);
        switch(Config.cur.screenshotType)
        {
        case 0: // '\0'
            grabTGA();
            break;

        case 1: // '\001'
            grabJPG();
            break;

        case 2: // '\002'
            grabTGA();
            grabJPG();
            break;

        default:
            grabTGA();
            break;
        }
        shotNum++;
    }

    private String scrName(int i)
    {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return scrDir.getPath() + "\\" + dateF.format(cal.getTime()) + (i <= 0 ? ".tga" : ".jpg");
    }

    public void grabTGA()
    {
        String s = scrName(0);
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            fileoutputstream.write(0);
            fileoutputstream.write(0);
            fileoutputstream.write(2);
            fileoutputstream.write(new byte[5]);
            fileoutputstream.write(0);
            fileoutputstream.write(0);
            fileoutputstream.write(0);
            fileoutputstream.write(0);
            fileoutputstream.write((short)dx);
            fileoutputstream.write((short)(dx >> 8));
            fileoutputstream.write((short)dy);
            fileoutputstream.write((short)(dy >> 8));
            fileoutputstream.write((byte)(img.BPP * 8));
            fileoutputstream.write(0);
            fileoutputstream.write(img.image, 0, dx * dy * img.BPP);
            fileoutputstream.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void grabJPG()
    {
        String s = scrName(1);
        FileOutputStream fileoutputstream = null;
        try
        {
            fileoutputstream = new FileOutputStream(s);
            if(Config.cur.screenshotType == 2)
                swapRB();
            DataBufferByte databufferbyte = new DataBufferByte(img.image, dx * dy * 3, 0);
            java.awt.image.WritableRaster writableraster = Raster.createInterleavedRaster(databufferbyte, dx, dy, dx * 3, 3, new int[] {
                0, 1, 2
            }, null);
            ComponentColorModel componentcolormodel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[] {
                8, 8, 8
            }, false, false, 1, 0);
            BufferedImage bufferedimage = new BufferedImage(componentcolormodel, writableraster, false, null);
            for(int i = 0; i < dy / 2 - 1; i++)
            {
                for(int j = 0; j < dx; j++)
                {
                    int k = bufferedimage.getRGB(j, i);
                    int l = bufferedimage.getRGB(j, dy - i - 1);
                    bufferedimage.setRGB(j, dy - i - 1, k);
                    bufferedimage.setRGB(j, i, l);
                }
            }
            JPEGImageEncoder jpegimageencoder = JPEGCodec.createJPEGEncoder(fileoutputstream);
            JPEGEncodeParam jpegencodeparam = jpegimageencoder.getDefaultJPEGEncodeParam(bufferedimage);
            jpegencodeparam.setQuality(jpgQuality, true);
            jpegimageencoder.encode(bufferedimage, jpegencodeparam);
            fileoutputstream.flush();
            bufferedimage.flush();
            bufferedimage = null;
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            System.out.println("Image " + s + " (jpg file) could not be created.");
        }
        catch(ImageFormatException imageformatexception)
        {
            System.out.println("Image Format Exception while trying to create a " + s + " jpg file");
            imageformatexception.printStackTrace();
        }
        catch(IOException ioexception)
        {
            System.out.println("IOException while trying to create a " + s + " jpg file");
            ioexception.printStackTrace();
        }
        finally
        {
            try
            {
                fileoutputstream.close();
            }
            catch(IOException ioexception1)
            {
                System.out.println("Unable to close stream while trying to create a " + s + " jpg file");
                ioexception1.printStackTrace();
            }
        }
    }

    public ScrShot(String s)
    {
        shotNum = 0;
        img = new TexImage();
        dx = GLContext.getCurrent().width();
        dy = GLContext.getCurrent().height();
        prefixName = s;
        img.set(dx, dy, 6407);
        jpgQuality = Config.cur.ini.get("game", "jpgQuality ", 1.0F, 0.0F, 1.0F);
        scrDir = new File(HomePath.toFileSystemName("MyScreenShots/", 0));
        if(!scrDir.exists())
            scrDir.mkdirs();
    }

    private String prefixName;
    private int shotNum;
    private int dx;
    private int dy;
    private TexImage img;
    private float jpgQuality;
    private static final String TGA = ".tga";
    private static final String JPG = ".jpg";
    private File scrDir;
}
