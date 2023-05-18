package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.EventLog;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.rts.Property;

public class BombPara1 extends Bomb
{

    public void start()
    {
        Loc loc = new Loc();
//        Orient orient = new Orient();
        Class class1 = getClass();
        init(Property.floatValue(class1, "kalibr", 0.082F), Property.floatValue(class1, "massa", 6.8F));
        setOwner(pos.base(), false, false, false);
        pos.setBase(null, null, true);
        pos.setAbs(pos.getCurrent());
        pos.getAbs(loc);
        Actor actor = getOwner();
        if((!actor.isNet() || actor.isNetMaster()) && Actor.isValid(actor))
        {
            /*Paratrooper paratrooper = */new Paratrooper(getOwner(), getOwner().getArmy(), 255, loc, ((Aircraft)getOwner()).FM.Vwld);
            destroy();
        }
        if(Actor.isValid(actor))
            EventLog.DropParatrooper(actor);
    }

    static 
    {
        Class class1 = BombPara1.class;
        Property.set(class1, "mesh", "3DO/Arms/Null/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.5F);
        Property.set(class1, "massa", 90F);
    }
}
