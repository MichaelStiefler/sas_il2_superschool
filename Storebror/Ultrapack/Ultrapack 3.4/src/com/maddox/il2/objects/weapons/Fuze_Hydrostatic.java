package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.objects.ActorLand;
import com.maddox.rts.Property;

public class Fuze_Hydrostatic extends Fuze {

    public boolean isFuzeArmed(ActorPos actorpos, Vector3d vector3d, Actor actor) {
        boolean flag = false;
        if (actor instanceof ActorLand) {
            flag = !Engine.land().isWater(((Tuple3d) (actorpos.getAbsPoint())).x, ((Tuple3d) (actorpos.getAbsPoint())).y);
        }
        if (flag) {
            super.isArmed = false;
            return false;
        } else {
            return super.isFuzeArmed(actorpos, vector3d, actor);
        }
    }

    static {
        Class localClass = Fuze_Hydrostatic.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F });
    }
}
