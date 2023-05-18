package com.maddox.il2.engine;

import com.maddox.JGP.Point3d;

public class LightPointActorLong extends LightPointActor implements VisibilityLong {
    public LightPointActorLong(LightPoint lightpoint) {
        super(lightpoint);
    }

    public boolean isVisibilityLong() {
        return true;
    }

    public LightPointActorLong(LightPoint lightpoint, Point3d point3d) {
        super(lightpoint, point3d);
    }
}
