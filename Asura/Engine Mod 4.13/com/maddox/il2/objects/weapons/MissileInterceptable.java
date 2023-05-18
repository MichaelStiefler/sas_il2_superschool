// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:18:47
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileInterceptable.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.ground.Predator;

public interface MissileInterceptable
    extends Predator
{

    public abstract boolean isReleased();
}