// Source File Name: TypeSACLOS
// Author:           western0221 2018-Jul-24

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;

public interface TypeSACLOS
{
    public abstract Point3d getSACLOStarget();
    public abstract boolean setSACLOStarget(Point3d p3d);
    public abstract boolean getSACLOSenabled();
    public abstract boolean setSACLOSenable(boolean flag);

}