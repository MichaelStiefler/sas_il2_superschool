package com.maddox.rts;

import java.io.*;

public class KryptoInputFilter extends FilterInputStream
{

	public KryptoInputFilter(InputStream inputstream, int ai[])
    {
        super(inputstream);
        sw = 0;
        key = ai;
        if(ai != null && ai.length == 0)
            key = null;
        sw = 0;
    }

	public boolean markSupported()
    {
        return false;
    }

	public int read()
        throws IOException
    {
        int i = in.read();
        if(key == null)
            return i;
        sw = (sw + 1) % key.length;
        if(i != -1)
            i ^= key[sw];
        return i;
    }

	public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        int k = in.read(abyte0, i, j);
        if(key == null || k <= 0)
            return k;
        for(int l = 0; l < k; l++)
        {
            sw = (sw + 1) % key.length;
            abyte0[i + l] = (byte)(abyte0[i + l] ^ key[sw]);
        }

        return k;
    }

    private int key[] = {
        255, 170
    };
    private int sw;
}
