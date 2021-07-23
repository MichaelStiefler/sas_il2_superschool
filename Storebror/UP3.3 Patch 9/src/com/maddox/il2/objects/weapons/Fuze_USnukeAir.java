package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorPos;
import com.maddox.rts.Property;

public class Fuze_USnukeAir extends Fuze {

    public void rareAction(ActorPos actorpos, Vector3d vector3d) {
        if (this.getOwnerBomb() != null) {
            Point3d point3d = actorpos.getCurrentPoint();
            float f = (float) (point3d.z - World.land().HQ(point3d.x, point3d.y));
            if ((this.getOwnerBomb().delayExplosion > f) && this.isFuzeArmed(actorpos, vector3d, null)) {
                this.getOwnerBomb().doMidAirExplosion();
            }
        }
    }

    static {
        Class class1 = Fuze_USnukeAir.class;
        Property.set(class1, "type", 7);
        Property.set(class1, "airTravelToArm", 50F);
        Property.set(class1, "fixedDelay", new float[] { 10F, 25F, 50F, 75F, 100F, 150F, 200F, 300F, 400F, 500F });
    }
}
