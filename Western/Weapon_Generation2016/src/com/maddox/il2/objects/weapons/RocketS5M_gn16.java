// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS5M_gn16 extends Rocket
{

    public RocketS5M_gn16()
    {
    }

    public void start(float f, int i)
    {
        super.start(f, i);
        super.speed.normalize();
        super.speed.scale(525D);
        super.noGDelay = -1L;
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

    static Class class$com$maddox$il2$objects$weapons$RocketS5M_gn16;

    static 
    {
        Class class1
        = (class$com$maddox$il2$objects$weapons$RocketS5M_gn16 == null
           ? (class$com$maddox$il2$objects$weapons$RocketS5M_gn16
          = _mthclass$("com.maddox.il2.objects.weapons.RocketS5M_gn16"))
           : class$com$maddox$il2$objects$weapons$RocketS5M_gn16);
        Property.set(class1, "mesh", "3DO/Arms/S5_Rocket_gn16/mono.sim");
        Property.set(class1, "meshFly", "3DO/Arms/S5_Rocket_gn16/mono_open.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 7F);
        Property.set(class1, "timeLife", 60F);
        Property.set(class1, "timeFire", 1.1F);
        Property.set(class1, "force", 1300F);
        Property.set(class1, "power", 0.108F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.055F);
        Property.set(class1, "massa", 3.86F);
        Property.set(class1, "massaEnd", 2.97F);
        Property.set(class1, "friendlyName", "S-5M");
    }
}
