// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS13_gn16 extends Rocket
{

    public RocketS13_gn16()
    {
    }

    public void start(float f, int i)
    {
        super.start(f, i);
        super.speed.normalize();
        super.speed.scale(610D);
        super.noGDelay = -1L;
    }

    static Class _mthclass$(String s)
    {
        Class class1;
        try
        {
            class1 = Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

    static Class class$com$maddox$il2$objects$weapons$RocketS13_gn16;

    static 
    {
        Class class1
        = (class$com$maddox$il2$objects$weapons$RocketS13_gn16 == null
           ? (class$com$maddox$il2$objects$weapons$RocketS13_gn16
          = _mthclass$("com.maddox.il2.objects.weapons.RocketS13_gn16"))
           : class$com$maddox$il2$objects$weapons$RocketS13_gn16);
        Property.set(class1, "mesh", "3DO/Arms/Zuni_5inch_gn16/Mk16Mk24close.sim");
        Property.set(class1, "meshFly", "3DO/Arms/Zuni_5inch_gn16/Mk16Mk24fly.sim");
        Property.set(class1, "sprite", (String)null);
        Property.set(class1, "flame", "3do/Effects/RocketKS1/RocketKS1Flame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketKS1/RocketKS1Smoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 2.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 6F);
        Property.set(class1, "timeLife", 60F);
        Property.set(class1, "timeFire", 4.1F);
        Property.set(class1, "force", 5000F);
        Property.set(class1, "power", 15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.122F);
        Property.set(class1, "massa", 57F);
        Property.set(class1, "massaEnd", 40F);
        Property.set(class1, "friendlyName", "S-13");
    }
}
