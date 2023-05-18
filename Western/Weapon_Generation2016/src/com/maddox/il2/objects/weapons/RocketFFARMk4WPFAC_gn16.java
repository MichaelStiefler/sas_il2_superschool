
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


public class RocketFFARMk4WPFAC_gn16 extends Rocket
{

    public RocketFFARMk4WPFAC_gn16()
    {
        marked = false;
        hit = false;
        hittime = -1L;
        counter = 0L;
        oldcount = -1L;
        askedNum = 0;

        if(Config.isUSE_RENDER())
        {
            setMesh(Property.stringValue(getClass(), "mesh"));
            mesh.materialReplace("Ordnance1", "Ordnance1WP");
            mesh.materialReplace("Ordnance1P", "Ordnance1WPP");
            mesh.materialReplace("Ordnance1Q", "Ordnance1WPQ");
        }
    }

	public void start(float f, int i)
    {
        super.start(f, i);

        myArmy = getOwner().getArmy();

        if(Config.isUSE_RENDER())
        {
            setMesh(Property.stringValue(getClass(), "meshFly"));
            mesh.materialReplace("Ordnance1", "Ordnance1WP");
            mesh.materialReplace("Ordnance1P", "Ordnance1WPP");
            mesh.materialReplace("Ordnance1Q", "Ordnance1WPQ");
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(actor instanceof ActorLand)
        {
            if(!hit)
            {
                t1 = Time.current() + 0xc3500L + World.Rnd().nextLong(0L, 8000L);
                t2 = Time.current() + 0xc3500L + World.Rnd().nextLong(0L, 16000L);
                Eff3DActor.New(this, null, new Loc(), 1.0F, "Effects/Smokes/WPsmoke.eff", (float)(t1 - Time.current()) / 1000F);
                hit = true;
                hittime = (long)Math.floor((double)Time.current() / 1000D);
            }

            counter = (long)Math.floor((double)Time.current() / 1000D) - hittime;

            if(Time.current() <= (t2 + t1) / 2L)
            {
                if(askedNum > 0 && counter % 600 == 540 && counter != oldcount)
                {
                    for(int i = 0; i < askedNum; i++)
                    {
                        if(((Maneuver) (askedPilots[i])).Group.grTask == 4)
                            ((Maneuver) (askedPilots[i])).Group.setGroupTask(1);
                    }

                    askedNum = 0;
                }
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
                if(askedNum == 0 && counter % 600 == 0 && counter != oldcount)
                {
                    List list = Engine.targets();
                    int j = list.size();
                    askedPilots = new Pilot[j];
                    askedNum = 0;
                    for(int k = 0; k < j; k++)
                    {
                        Actor actor1 = (Actor)list.get(k);
                        if(!(actor1 instanceof Aircraft) || actor1.getArmy() != myArmy)
                            continue;
                        Aircraft aircraft = (Aircraft)actor1;
                        if(((aircraft instanceof TypeStormovik) || (aircraft instanceof TypeFighter) || (aircraft instanceof TypeBomber)) && !(aircraft instanceof TypeScout) && aircraft.pos.getAbsPoint().distance(point3d) < 15000D)
                        {
                            airgroup = ((Maneuver)((SndAircraft) aircraft).FM).Group;
                            pilot = (Pilot)((SndAircraft) aircraft).FM;
                            if(((Maneuver) (pilot)).Group.grTask != 4 && pilot.AP.way.curr().Action != 3)
                            {
                                ((Maneuver) (pilot)).Group.setGroupTask(4);
                                ((Maneuver) (pilot)).Group.setGTargMode(0);
                                ((Maneuver) (pilot)).Group.setGTargMode(point3d, 100F);
                                Voice.speakOk(aircraft);

                                askedPilots[askedNum] = pilot;
                                askedNum++;
                            }
                        }
                        marked = true;
                    }

                }
            }  else
            {
                if(askedNum > 0)
                {
                    for(int i = 0; i < askedNum; i++)
                        ((Maneuver) (askedPilots[i])).Group.setGroupTask(1);

                    askedNum = 0;
                }
            }
            oldcount = counter;
        } else
        {
            super.msgCollision(actor, s, s1);
        }
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static Class class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFAC_gn16;

    private long t1;
    private long t2;
    private boolean marked;
    private boolean hit;
    private long hittime;
    private long counter;
    private long oldcount;
    private AirGroup airgroup = null;
    private Pilot pilot = null;
    private Pilot askedPilots[];
    private int askedNum;
    private int myArmy;

    static 
    {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFAC_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFAC_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketFFARMk4WPFAC_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFAC_gn16);
        Property.set(class1, "mesh", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFAR.sim");
        Property.set(class1, "meshFly", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFARfly.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_82");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "timeLife", 999F);
        Property.set(class1, "timeFire", 1.5F);
        Property.set(class1, "force", 1300F);
        Property.set(class1, "power", 2F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.070F);
        Property.set(class1, "massa", 9.2F);
        Property.set(class1, "massaEnd", 5.2F);
        Property.set(class1, "spinningStraightFactor", 1.1F);
        Property.set(class1, "friendlyName", "FFAR-Mk4FAC");
    }
}
