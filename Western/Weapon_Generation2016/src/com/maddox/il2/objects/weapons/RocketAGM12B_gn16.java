
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.*;


public class RocketAGM12B_gn16 extends RemoteControlRocket
{
    public RocketAGM12B_gn16()
    {
        super();
    }

    public RocketAGM12B_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        super(actor, netchannel, i, point3d, orient, f);
    }

    protected void doStart(float f)
    {
        super.doStart(f);
        if(Config.isUSE_RENDER())
        {
            fl1 = Eff3DActor.New(this, findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            fl2 = Eff3DActor.New(this, findHook("_NavLightG"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
            // super.flame.drawing(false);
        }
    }

    public void destroy()
    {
        if(Config.isUSE_RENDER())
        {
            Eff3DActor.finish(fl1);
            Eff3DActor.finish(fl2);
        }
        super.destroy();
    }

    static Class _mthclass$(String s)
    {
        Class c;
        try{
            c = Class.forName(s);
        } catch ( ClassNotFoundException e ){
            throw new NoClassDefFoundError(e.getMessage());
        }
        return c;
    }

    static Class class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16;

    private Eff3DActor fl1;
    private Eff3DActor fl2;

    static 
    {
   	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAGM12B_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16);
        Property.set(class1, "mesh", "3do/arms/AGM12B_gn16/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "timeLife", 330F);
        Property.set(class1, "timeFire", 6F);
        Property.set(class1, "force", 13000F);
        Property.set(class1, "power", 120F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.30F);
        Property.set(class1, "massa", 259F);
        Property.set(class1, "massaEnd", 183F);
        Property.set(class1, "friendlyName", "AGM-12B");
        Spawn.add(class1, new SPAWN());
    }


}