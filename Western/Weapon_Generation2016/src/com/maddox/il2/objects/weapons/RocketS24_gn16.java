// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS24_gn16 extends Rocket
{

    public RocketS24_gn16()
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

    static Class class$com$maddox$il2$objects$weapons$RocketS24_gn16;

    static 
    {
        Class class1
        = (class$com$maddox$il2$objects$weapons$RocketS24_gn16 == null
           ? (class$com$maddox$il2$objects$weapons$RocketS24_gn16
          = _mthclass$("com.maddox.il2.objects.weapons.RocketS24_gn16"))
           : class$com$maddox$il2$objects$weapons$RocketS24_gn16);
        Property.set(class1, "mesh", "3do/Arms/S24_Rocket_gn16/mono.sim");
        Property.set(class1, "sprite", "3do/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/Effects/RocketS25/RocketS25Flame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 75F);
        Property.set(class1, "timeLife", 120F);
        Property.set(class1, "timeFire", 5F);
        Property.set(class1, "force", 1300F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.24F);
        Property.set(class1, "massa", 235F);
        Property.set(class1, "massaEnd", 200F);
        Property.set(class1, "friendlyName", "S-24");
    }
}
