package com.maddox.rts;

public final class RTS extends LDRCallBack
{
	public static final void loadNative()
    {
        SFSInputStream.loadNative();
    }

    static 
    {
        loadNative();
    }
}
