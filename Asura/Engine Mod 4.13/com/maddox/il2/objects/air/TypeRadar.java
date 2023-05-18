// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:12:15
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TypeRadar.java

package com.maddox.il2.objects.air;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import java.io.IOException;

public interface TypeRadar
{

    public abstract boolean typeRadarToggleMode();

    public abstract void typeRadarRangePlus();

    public abstract void typeRadarRangeMinus();

    public abstract void typeRadarGainPlus();

    public abstract void typeRadarGainMinus();

    public abstract void typeRadarReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException;

    public abstract void typeRadarReplicateFromNet(NetMsgInput netmsginput)
        throws IOException;
}