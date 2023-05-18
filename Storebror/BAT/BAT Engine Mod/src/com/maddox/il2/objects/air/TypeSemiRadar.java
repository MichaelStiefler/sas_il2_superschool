// Source File Name: TypeSemiRadar
// Author:           SAS~S3 2015-01-13
// Last Modified by: western0221 2017-Nov-03 , not to using unsafe 'static' definition
// Now this interface needs real working codes on each implementing aircraft classes.

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;

// import com.maddox.JGP.Point3d;

public interface TypeSemiRadar
{
    public abstract Actor getSemiActiveRadarLockedActor();
    public abstract Actor setSemiActiveRadarLockedActor(Actor actor);
    public abstract boolean getSemiActiveRadarOn();
    public abstract boolean setSemiActiveRadarOn(boolean flag);

//    public Point3d target = new Point3d();

}