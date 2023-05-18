// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 17.01.2015 20:43:23
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RCS.java

package com.maddox.il2.objects.electronics;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.HomePath;
import java.io.*;
import java.util.Random;

public class RCS
{

    public RCS()
    {
        R_2_lambda = 10F;
        SMALL_LOBES_SIZE = 10F;
        SIDE_LOBE_SIZE = 15F;
        FRONT_LOBE_SIZE = 5F;
        REAR_LOBE_SIZE = 1.0F;
        TOP_LOBE_SIZE = 25F;
        rnd = new Random();
    }

    public void dumpValues()
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("RCS.txt", 0))));
            for(int i = 0; i <= 90; i += 5)
            {
                for(float f = 0.0F; f < 360F; f += 2.0F)
                    printwriter.print(baseRCS(f, i) + ";");

                printwriter.println();
            }

            printwriter.close();
        }
        catch(IOException ioexception)
        {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    protected float func(float f, float f1)
    {
        double d = Math.sin(Math.toRadians(f));
        d *= d;
        double d1 = Math.sin(Math.toRadians(f1)) * Math.cos(Math.toRadians(f));
        d1 *= d1;
        return ((float)Math.cos(3.141593F * R_2_lambda * (float)Math.sqrt(d + d1)) * f) / 90F;
    }

    protected float mult(float f, float f1)
    {
        float f2;
        if(f > 78F && f < 102F)
        {
            f2 = -0.006953F * (90F - f) * (90F - f) + 1.0F;
            f2 = SMALL_LOBES_SIZE + f2 * SIDE_LOBE_SIZE;
        } else
        if(f < 12F)
        {
            f2 = -0.006953F * f * f + 1.0F;
            f2 = SMALL_LOBES_SIZE + f2 * FRONT_LOBE_SIZE;
        } else
        if(f > 168F)
        {
            f2 = -0.006953F * (180F - f) * (180F - f) + 1.0F;
            f2 = SMALL_LOBES_SIZE + f2 * REAR_LOBE_SIZE;
        } else
        {
            f2 = SMALL_LOBES_SIZE;
        }
        float f3 = 1.0F;
        if(f1 > 78F)
        {
            f3 = -0.006953F * (90F - f1) * (90F - f1) + 1.0F;
            f3 = SMALL_LOBES_SIZE + f3 * TOP_LOBE_SIZE;
        } else
        {
            f3 = SMALL_LOBES_SIZE;
        }
        if(f3 > f2)
            return f3;
        else
            return f2;
    }

    protected float baseRCS(Orient orient)
    {
        float f1 = orient.getAzimut();
        float f = Math.abs(orient.getTangage());
        return baseRCS(f1, f);
    }

    protected float baseRCS(float f, float f1)
    {
        for(; f < 0.0F; f += 360F);
        for(; f > 360F; f -= 360F);
        if(f > 180F)
            f = 360F - f;
        float f3 = Math.abs(f - 90F);
        float f2 = Math.abs(func(f3, f1) + func(90F - f3, f1));
        f2 *= mult(f, f1);
        return f2;
    }

    float getRCS(Actor actor, Orient orient)
    {
        float f = baseRCS(orient);
        f -= 7F;
        if(Actor.isValid(actor))
        {
            float f1 = (((Aircraft)actor).FM.Wingspan * ((Aircraft)actor).FM.Length) / 310F;
            f += -3.053F / f1 + f1 * 3.152F;
        }
        f += -5F + 10F * rnd.nextFloat();
        return f;
    }

    protected float R_2_lambda;
    private static final float PI = 3.141593F;
    protected float SMALL_LOBES_SIZE;
    protected float SIDE_LOBE_SIZE;
    protected float FRONT_LOBE_SIZE;
    protected float REAR_LOBE_SIZE;
    protected float TOP_LOBE_SIZE;
    Random rnd;
}
