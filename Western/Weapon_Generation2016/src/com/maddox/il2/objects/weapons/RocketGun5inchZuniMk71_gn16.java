
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class RocketGun5inchZuniMk71_gn16 extends RocketGun
{

    public RocketGun5inchZuniMk71_gn16()
    {
    }

    public void setRocketTimeLife(float f)
    {
        super.timeLife = -1F;
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

    static Class class$com$maddox$il2$objects$weapons$RocketGun5inchZuniMk71_gn16;
    static Class class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16;

    static 
    {
        Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGun5inchZuniMk71_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGun5inchZuniMk71_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGun5inchZuniMk71_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketGun5inchZuniMk71_gn16);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.Rocket5inchZuniMk71_gn16"))
	       : class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 4F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
