package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;

public class RocketWPFAC extends Rocket
{

    public RocketWPFAC()
    {
        marked = false;
        counter = 0;
    }

    public void start(float f, int i)
    {
        super.start(f, i);
        drawing(false);
        t1 = Time.current() + 0x1c3a90L + World.Rnd().nextLong(0L, 850L);
        t2 = Time.current() + 0x1c3a90L + World.Rnd().nextLong(0L, 3800L);
        Eff3DActor.New(this, null, new Loc(), 1.0F, "Effects/Smokes/WPsmoke.eff", (float)(t1 - Time.current()) / 1000F);
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(actor instanceof ActorLand)
        {
            if(Time.current() <= (t2 + t1) / 2L)
            {
                counter++;
                if(counter == 1000)
                    ((Maneuver) (pilot)).Group.setGroupTask(1);
                Point3d point3d = new Point3d();
                super.pos.getTime(Time.current(), point3d);
                Class class1 = getClass();
                float f = Property.floatValue(class1, "power", 0.0F);
                int i = Property.intValue(class1, "powerType", 0);
                float f1 = Property.floatValue(class1, "radius", 1.0F);
                MsgExplosion.send(actor, s1, point3d, getOwner(), super.M, f, i, f1);
                Vector3d vector3d = new Vector3d();
                getSpeed(vector3d);
                vector3d.x *= 0.5D;
                vector3d.y *= 0.5D;
                vector3d.z = 1.0D;
                setSpeed(vector3d);
                if(!marked)
                {
                    List list = Engine.targets();
                    int j = list.size();
                    for(int k = 0; k < j; k++)
                    {
                        Actor actor1 = (Actor)list.get(k);
                        Aircraft aircraft = (Aircraft)actor1;
                        if(((actor1 instanceof TypeStormovik) || (actor1 instanceof TypeFighter)) && !(actor1 instanceof TypeScout) && actor1.pos.getAbsPoint().distance(point3d) < 15000D)
                        {
                            pilot = (Pilot)((SndAircraft) ((Aircraft)actor1)).FM;
                            ((Maneuver) (pilot)).Group.setGroupTask(4);
                            ((Maneuver) (pilot)).Group.setGTargMode(0);
                            ((Maneuver) (pilot)).Group.setGTargMode(point3d, 100F);
                            Voice.speakOk(aircraft);
                        }
                        marked = true;
                    }

                }
            }
        } else
        {
            super.msgCollision(actor, s, s1);
        }
    }

    private long t1;
    private long t2;
    private boolean marked;
    private int counter;
    private static Pilot pilot = null;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketWPFAC.class;
        Property.set(class1, "mesh", "3DO/Arms/2-75inch/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "timeLife", 100F);
        Property.set(class1, "timeFire", 4F);
        Property.set(class1, "force", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.08F);
        Property.set(class1, "massa", 10.1F);
        Property.set(class1, "massaEnd", 5.1F);
    }
}