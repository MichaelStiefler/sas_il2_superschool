package com.maddox.rts;

public final class Compress
{

	public static final int code(int i, byte abyte0[], int j)
    {
        if(i == 0)
            return j;
        else
            return Code(i, abyte0, j);
    }

	public static final boolean decode(int i, byte abyte0[], int j)
    {
        if(i == 0)
            return true;
        else
            return Decode(i, abyte0, j);
    }

    private static native int Code(int i, byte abyte0[], int j);

    private static native boolean Decode(int i, byte abyte0[], int j);

    private Compress()
    {
    }

    static 
    {
        RTS.loadNative();
    }
}
