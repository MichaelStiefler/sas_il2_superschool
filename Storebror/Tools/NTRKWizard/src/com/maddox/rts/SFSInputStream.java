package com.maddox.rts;

import java.io.*;

public class SFSInputStream extends InputStream
{

	public int read()
        throws IOException
    {
        if(fis != null)
            return fis.read();
        else
            return read(fd);
    }

	public native int read(int i)
        throws IOException;

    private native int readBytes(int i, byte abyte0[], int j, int k)
        throws IOException;

    public int read(byte abyte0[])
        throws IOException
    {
        if(fis != null)
            return fis.read(abyte0);
        else
            return readBytes(fd, abyte0, 0, abyte0.length);
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        if(fis != null)
            return fis.read(abyte0, i, j);
        else
            return readBytes(fd, abyte0, i, j);
    }

    public long skip(long l)
        throws IOException
    {
        if(fis != null)
            return fis.skip(l);
        else
            return skip(fd, l);
    }

    private native long skip(int i, long l)
        throws IOException;

    public int available()
        throws IOException
    {
        if(fis != null)
            return fis.available();
        else
            return available(fd);
    }

    private native int available(int i)
        throws IOException;

    public void close()
        throws IOException
    {
        if(fis != null)
        {
            fis.close();
            fis = null;
        } else
        {
            close(fd);
            fd = -1;
        }
    }

    private native void close(int i)
        throws IOException;

    protected static final void loadNative()
    {
    }

    protected void finalize()
        throws IOException
    {
        if(fis != null || fd != -1)
            close();
    }

    private SFSInputStream()
    {
        fd = -1;
        fis = null;
    }

    private int fd;
    private FileInputStream fis;
    private static short ss[] = new short[2];
    private static boolean libLoaded = false;

    static 
    {
        loadNative();
    }
}
