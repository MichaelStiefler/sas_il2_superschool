
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class RocketAIR2Genie_gn16 extends Rocket
{

    public RocketAIR2Genie_gn16()
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

    static Class class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16;

    static 
    {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAIR2Genie_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16);
        Property.set(class1, "mesh", "3DO/Arms/AIR2Genie_gn16/mono.sim");
        Property.set(class1, "meshFly", "3DO/Arms/AIR2Genie_gn16/monoFly.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 550F);
        Property.set(class1, "timeLife", 14.0F);
        Property.set(class1, "timeFire", 5.7F);
        Property.set(class1, "force", 66000F);
        Property.set(class1, "power", 150000F);  // 1.5 kilo ton Nuclear warhead, for realism reduced to 1/10th of it's RL weight.
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.441F);
        Property.set(class1, "massa", 373.0F);
        Property.set(class1, "massaEnd", 240.1F);
        Property.set(class1, "spinningStraightFactor", 1.1F);
        Property.set(class1, "friendlyName", "AIR-2");
		Property.set(class1, "dragCoefficient", 0.50F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
