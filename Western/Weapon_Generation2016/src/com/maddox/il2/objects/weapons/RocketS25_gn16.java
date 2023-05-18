// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS25_gn16 extends Rocket
{

    public RocketS25_gn16()
    {
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

    static Class class$com$maddox$il2$objects$weapons$RocketS25_gn16;

    static 
    {
        Class class1
        = (class$com$maddox$il2$objects$weapons$RocketS25_gn16 == null
           ? (class$com$maddox$il2$objects$weapons$RocketS25_gn16
          = _mthclass$("com.maddox.il2.objects.weapons.RocketS25_gn16"))
           : class$com$maddox$il2$objects$weapons$RocketS25_gn16);
        Property.set(class1, "mesh", "3do/arms/S25_Rocket_gn16/S25_fold.sim");
        Property.set(class1, "meshFly", "3do/arms/S25_Rocket_gn16/S25.sim");
        Property.set(class1, "sprite", "3do/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/Effects/RocketS25/RocketS25Flame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 98F);
        Property.set(class1, "timeLife", 120F);
        Property.set(class1, "timeFire", 5F);
        Property.set(class1, "force", 1400F);
        Property.set(class1, "power", 175F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.30F);
        Property.set(class1, "massa", 480F);
        Property.set(class1, "massaEnd", 260F);
        Property.set(class1, "spinningStraightFactor", 1.8F);
        Property.set(class1, "friendlyName", "S-25");
    }
}