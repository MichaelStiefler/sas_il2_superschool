// Source File Name: TypeLaserDesignator
// Author:           western0221 2017-May-13

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;

public interface TypeLaserDesignator
{
    public abstract Point3d getLaserSpot();
    public abstract boolean setLaserSpot(Point3d p3d);
    public abstract boolean getLaserOn();
    public abstract boolean setLaserOn(boolean flag);
    public abstract boolean getLaserArmEngaged();
    public abstract boolean setLaserArmEngaged(boolean flag);

}