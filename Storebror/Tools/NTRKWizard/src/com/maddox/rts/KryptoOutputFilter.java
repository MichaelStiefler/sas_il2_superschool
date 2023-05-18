package com.maddox.rts;

import java.io.*;

public class KryptoOutputFilter extends FilterOutputStream
{

	public KryptoOutputFilter(OutputStream outputstream, int ai[])
    {
        super(outputstream);
        sw = 0;
        key = ai;
        if(ai != null && ai.length == 0)
            key = null;
        sw = 0;
    }

	public void write(int i)
        throws IOException
    {
        if(key != null)
        {
            sw = (sw + 1) % key.length;
            out.write(i ^ key[sw]);
        } else
        {
            out.write(i);
        }
    }

	public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        if(key != null)
        {
            for(int k = 0; k < j; k++)
            {
                sw = (sw + 1) % key.length;
                out.write(abyte0[i + k] ^ key[sw]);
            }

        } else
        {
            out.write(abyte0, i, j);
        }
    }

    private int key[] = {
        255, 170
    };
    private int sw;
}
