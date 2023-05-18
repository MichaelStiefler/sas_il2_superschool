package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class Fuze_APUV extends Fuze {

    public float getDetonationDelay() {
        if ((this.getOwnerBomb() != null) && (this.getOwnerBomb().getRnd().nextFloat() < 0.1F)) {
            return 0.0F;
        } else {
            return super.getDetonationDelay();
        }
    }

    public void rareAction(ActorPos actorpos, Vector3d vector3d) {
        if (this.getOwnerBomb() != null) {
            float f = Fuze.cvt((float) vector3d.length(), 200F, 350F, 0.0F, 0.003F);
            if ((this.getOwnerBomb().getRnd().nextFloat() < f) && super.isFuzeArmed(actorpos, vector3d, null)) {
                this.getOwnerBomb().doMidAirExplosion();
            }
        }
    }

    public boolean isFuzeArmed(ActorPos actorpos, Vector3d vector3d, Actor actor) {
        if ((Mission.curCloudsType() >= 5) && (Fuze.missionStartTime != -1F)) {
            float f = World.getTimeofDay();
            float f1 = (f - Fuze.missionStartTime) / 10F;
            if (this.getOwnerBomb().getRnd().nextFloat() < f1) {
                this.isArmed = false;
                return false;
            }
        }
        double d = Math.toDegrees(vector3d.angle(Fuze.land));
        float f2 = Fuze.cvt((float) d, 20F, 90F, 0.0F, 0.8F);
        if (this.getOwnerBomb().getRnd().nextFloat() < f2) {
            this.isArmed = false;
            return false;
        } else {
            return super.isFuzeArmed(actorpos, vector3d, actor);
        }
    }

    static {
        Class class1 = Fuze_APUV.class;
        Property.set(class1, "type", 0);
        Property.set(class1, "airTravelToArm", 610F);
        Property.set(class1, "fixedDelay", new float[] { 0.0F, 0.15F, 0.3F });
        Property.set(class1, "dateStart", 0);
        Property.set(class1, "dateEnd", 0x12853c5);
    }
}
