
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.rts.Property;


public class RocketGunAIR2Genie_gn16 extends RocketGun
{

    public RocketGunAIR2Genie_gn16()
    {
    }

    public void setRocketTimeLife(float f)
    {
        super.timeLife = 14.0F;
    }

	public void updateHook(String s) {
		if (((Missile) super.rocket).isReleased()) return;
		Class class1 = this.getClass();
		this.bullets(Property.intValue(class1, "bullets", 1));
		super.hook = (HookNamed) super.actor.findHook(s);
		try {
			super.rocket.destroy();
			super.rocket = (Rocket) super.bulletClass.newInstance();
			((Actor) (super.rocket)).pos.setBase(super.actor, super.hook, false);
			((Actor) (super.rocket)).pos.changeHookToRel();
			((Actor) (super.rocket)).pos.resetAsBase();
			super.rocket.visibilityAsBase(true);
			((Actor) (super.rocket)).pos.setUpdateEnable(false);
		} catch (Exception exception) {
		}
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

    static Class class$com$maddox$il2$objects$weapons$RocketGunAIR2Genie_gn16;
    static Class class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16;

    static 
    {
        Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunAIR2Genie_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunAIR2Genie_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGunAIR2Genie_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketGunAIR2Genie_gn16);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAIR2Genie_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketAIR2Genie_gn16);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 4F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
