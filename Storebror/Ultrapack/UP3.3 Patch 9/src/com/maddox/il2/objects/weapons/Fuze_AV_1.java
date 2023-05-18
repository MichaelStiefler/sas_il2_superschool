package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.objects.ActorLand;
import com.maddox.rts.Property;

public class Fuze_AV_1 extends Fuze {

    public float getDetonationDelay() {
        float f = Atmosphere.temperature(0.0F) - 273.15F;
        if (f < 0.0F) {
            float f1 = Math.abs(f) * 0.1F;
            return super.getDetonationDelay() + f1;
        } else {
            return super.getDetonationDelay();
        }
    }

    public boolean isFuzeArmed(ActorPos actorpos, Vector3d vector3d, Actor actor) {
        double d = Math.toDegrees(vector3d.angle(Fuze.land));
        float f = Fuze.cvt((float) d, 45F, 90F, 0.0F, 0.5F);
        if (this.getOwnerBomb().getRnd().nextFloat() < f) {
            this.isArmed = false;
            return false;
        }
        boolean flag = false;
        if (actor instanceof ActorLand) {
            flag = Engine.land().isWater(actorpos.getAbsPoint().x, actorpos.getAbsPoint().y) || (World.cur().camouflage == 1);
        }
        if (flag && (this.getOwnerBomb().getRnd().nextFloat() < 0.050000000000000003D)) {
            this.isArmed = false;
            return false;
        } else {
            return super.isFuzeArmed(actorpos, vector3d, actor);
        }
    }

    static {
        Class class1 = Fuze_AV_1.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 155F);
        Property.set(class1, "fixedDelay", new float[] { 5F, 7F, 22F });
        Property.set(class1, "dateStart", 0);
        Property.set(class1, "dateEnd", 0x12853c5);
    }
}
