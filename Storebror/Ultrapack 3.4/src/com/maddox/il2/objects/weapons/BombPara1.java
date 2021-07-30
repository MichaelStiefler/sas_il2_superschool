package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.EventLog;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.rts.Property;

public class BombPara1 extends Bomb {

    public void start() {
        Loc loc = new Loc();
//        Orient orient = new Orient();
        Class class1 = this.getClass();
        this.init(Property.floatValue(class1, "kalibr", 0.082F), Property.floatValue(class1, "massa", 6.8F));
        this.setOwner(this.pos.base(), false, false, false);
        this.pos.setBase(null, null, true);
        this.pos.setAbs(this.pos.getCurrent());
        this.pos.getAbs(loc);
        Actor actor = this.getOwner();
        if ((!actor.isNet() || actor.isNetMaster()) && Actor.isValid(actor)) {
            /*Paratrooper paratrooper = */new Paratrooper(this.getOwner(), this.getOwner().getArmy(), 255, loc, ((Aircraft) this.getOwner()).FM.Vwld);
            this.destroy();
        }
        if (Actor.isValid(actor)) {
            EventLog.DropParatrooper(actor);
        }
    }

    static {
        Class class1 = BombPara1.class;
        Property.set(class1, "mesh", "3DO/Arms/Null/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.5F);
        Property.set(class1, "massa", 90F);
    }
}
