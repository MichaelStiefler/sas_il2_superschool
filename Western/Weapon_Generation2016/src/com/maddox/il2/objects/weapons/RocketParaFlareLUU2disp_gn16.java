// US night illumination flare bomb LUU-2 with a parachute, 5inch diameter.
// This Rocket class is backward deployed version by SUU-25 dispenser.
// SUU-25 dispenser carries 8x this LUU-2.

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class RocketParaFlareLUU2disp_gn16 extends Rocket
{

    public RocketParaFlareLUU2disp_gn16()
    {
        tOrient = new Orient();
        chute = null;
        bOnChute = false;
        bOnLight = false;
        bCollideLand = false;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public boolean interpolateStep()
    {
        if(tEStart > 0L)
            if(Time.current() > tEStart)
            {
                tEStart = -1L;
                setThrust(-600F);
            } else
            {
                return false;
            }
        return super.interpolateStep();
    }

    public void start(float f, int i)
    {
        super.start(-1F, i);
        FlightModel flightmodel = ((Aircraft)getOwner()).FM;
        tOrient.set(flightmodel.Or);
        speed.set(flightmodel.Vwld);
        noGDelay = -1L;
        tEStart = Time.current() + 50L;
        ttcurTM = Time.current() + World.Rnd().nextLong(800L, 1500L);
        ttlitTM = Time.current() + World.Rnd().nextLong(1600L, 2000L);
        ttexpTM = Time.current() + World.Rnd().nextLong(11200L, 17700L);
        if(Config.isUSE_RENDER())
            light.light.setEmit(0.0F, 0.0F);
    }

    public void destroy()
    {
        if(chute != null)
            chute.destroy();
        super.destroy();
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(bOnChute)
        {
            getSpeed(v3d);
            if(!bCollideLand)
            {
                v3d.scale(0.96999999999999997D);
                if(v3d.z < -2D)
                    v3d.z += 1.1F * Time.tickConstLenFs();
            }
            else
                v3d.set(0.0D, 0.0D, 0.0D);
            setSpeed(v3d);
        } else
        if(Time.current() > ttcurTM)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(0.5F);
            chute.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
            setThrust(0.001F);
        }

        if(!bOnLight)
        {
            if(Time.current() > ttlitTM)
            {
                bOnLight = true;
                if(Config.isUSE_RENDER())
                    light.light.setEmit(10F, 250F);
            }
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(actor instanceof ActorLand)
        {
            bCollideLand = true;
            if(chute != null)
                chute.landing();
        }
        super.msgCollision(actor, s, s1);
    }

    private Chute chute;
    private boolean bOnChute;
    private boolean bOnLight;
    private boolean bCollideLand;
    private static Vector3d v3d = new Vector3d();
    private long ttcurTM;
    private long ttlitTM;
    private long ttexpTM;
    private long tEStart;
    private Orient tOrient;

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

    static Class class$com$maddox$il2$objects$weapons$RocketParaFlareLUU2disp_gn16;

    static 
    {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketParaFlareLUU2disp_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketParaFlareLUU2disp_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketParaFlareLUU2disp_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketParaFlareLUU2disp_gn16);
        Property.set(class1, "mesh", "3DO/Arms/LUU2_ParaFlare_gn16/mono.sim");
        Property.set(class1, "sprite", (String) null);
        Property.set(class1, "flame", (String) null);
        Property.set(class1, "smoke", (String) null);
        Property.set(class1, "emitColor", new Color3f(1.0F, 0.99F, 0.95F));
        Property.set(class1, "emitLen", 250F);
        Property.set(class1, "emitMax", 10F);
        Property.set(class1, "sound", "weapon.bomb_phball");
        Property.set(class1, "radius", 0.0001F);
        Property.set(class1, "timeLife", 999F);
        Property.set(class1, "timeFire", 600F);
        Property.set(class1, "force", 0.900F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.12F);
        Property.set(class1, "massa", 13.6F);
        Property.set(class1, "massaEnd", 13.2F);
        Property.set(class1, "friendlyName", "LUU-2 ParaFlare");
    }
}
