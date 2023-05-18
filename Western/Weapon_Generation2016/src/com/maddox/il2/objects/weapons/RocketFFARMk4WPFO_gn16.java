
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;
import java.util.Random;


public class RocketFFARMk4WPFO_gn16 extends Rocket
{

    public RocketFFARMk4WPFO_gn16()
    {
        interval = 1000L;
        marked = false;
        hit = false;
        hittime = -1L;
        counter = 0L;
        oldcount = -1L;

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
                if(!marked && counter > 30 && counter != oldcount)
                {
                    marked = true;
                    HUD.logCenter("                          Splash!");
                }
                Random random = new Random();
                int j = random.nextInt(6);
                if(marked && counter % 10 == j && counter != oldcount)
                {
                    int k = random.nextInt(150);
                    int l = k - 75;
                    point3d.x += l;
                    k = random.nextInt(150);
                    l = k - 75;
                    point3d.y += l;
                    Explosions.generate(actor, point3d, 25F, 0, 136F, !Mission.isNet());
                    MsgExplosion.send(actor, s, point3d, getOwner(), 0.0F, 25F, 0, 136F);
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

    static Class class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16;

    private long t1;
    private long t2;
    private boolean marked;
    private boolean hit;
    private long hittime;
    private long counter;
    private long oldcount;
    private int myArmy;
    private long interval;

    static 
    {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketFFARMk4WPFO_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16);
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
        Property.set(class1, "friendlyName", "FFAR-Mk4FO");
    }
}
